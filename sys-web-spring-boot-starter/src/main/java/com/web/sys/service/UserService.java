package com.web.sys.service;

import com.base.web.exception.ExceptionUtil;
import com.base.web.util.TokenUtils;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.web.ruoyi.constants.UserConstants;
import com.web.ruoyi.mybatis.entity.SysUser;
import com.web.ruoyi.mybatis.mapper.SysUserMapper;
import com.web.sys.authentication.user.LoginUser;
import com.web.sys.cache.CacheWrapper;
import com.web.sys.constants.enums.SysWebErrorCodeEnums;
import com.web.sys.event.SysUserUpdateEvent;
import com.web.sys.properties.SysWebProperties;
import io.jsonwebtoken.impl.TextCodec;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author suyh
 * @since 2024-08-31
 */
@RequiredArgsConstructor
@Slf4j
public class UserService implements IUserService {
    protected final Random RANDOM = new Random();
    protected volatile String base64EncodedSecretKey;

    // 每个用户一把双重检测锁
    protected final Map<Long, ReentrantLock> userDclLock = new ConcurrentHashMap<>();
    protected final Cache<Long, CacheWrapper<SysUser>> cacheSysUser
            = Caffeine.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES)
            .initialCapacity(128).build();

    @Resource
    protected ApplicationContext context;

    @Resource
    protected GoogleAuthenticator googleAuthenticator;

    @Resource
    protected SysWebProperties sysWebProperties;
    @Resource
    protected PasswordEncoder passwordEncoder;
    @Resource
    protected SysUserMapper userMapper;

    @EventListener(SysUserUpdateEvent.class)
    public void sysUserChangeEvent(SysUserUpdateEvent event) {
        Long userId = event.getUserId();
        if (userId == null) {
            SysUser sysUser = event.getSysUser();
            if (sysUser != null) {
                userId = sysUser.getId();
            }

            if (userId == null) {
                log.warn("{} no user id", SysUserUpdateEvent.class.getSimpleName());
                return;
            }
        }

        cacheSysUser.invalidate(userId);
    }

    @Override
    public String getBase64EncodedSecretKey() {
        if (base64EncodedSecretKey == null) {
            synchronized (this) {
                if (base64EncodedSecretKey == null) {
                    String tokenSecretKey = sysWebProperties.getUser().getTokenSecretKey();
                    base64EncodedSecretKey = TextCodec.BASE64.encode(tokenSecretKey);
                }
            }
        }

        return base64EncodedSecretKey;
    }

    protected int generateTokenId() {
        return RANDOM.nextInt(Integer.MAX_VALUE);
    }

    @Override
    public String login(@NonNull String username, @NonNull String password, @NonNull Integer code) {
        SysUser historyEntity = userMapper.selectByUni(username);
        if (historyEntity == null) {
            throw ExceptionUtil.business(SysWebErrorCodeEnums.USER_BAD_CREDENTIALS);
        }

        validUser2Fa(historyEntity.getTwoFactorAuthKey(), code);

        String sourcePassword = password + historyEntity.getSalt();
        boolean matchFlag = passwordEncoder.matches(sourcePassword, historyEntity.getPassword());
        if (!matchFlag) {
            throw ExceptionUtil.business(SysWebErrorCodeEnums.USER_BAD_CREDENTIALS);
        }

        String status = historyEntity.getStatus();
        if (status == null || !status.trim().equals(UserConstants.NORMAL)) {
            throw ExceptionUtil.business(SysWebErrorCodeEnums.SYSTEM_USER_USER_DISABLED);
        }

        int tokenId = generateTokenId();
        resetLoginTokenId(historyEntity.getId(), tokenId);

        Map<String, Object> claims = new HashMap<>();
        claims.put(TokenUtils.USER_ID_KEY, historyEntity.getId());
        String base64EncodedSecretKey = getBase64EncodedSecretKey();
        return TokenUtils.createToken(base64EncodedSecretKey, claims, tokenId, username, sysWebProperties.getUser().getTokenSeconds());
    }

    private void resetLoginTokenId(long userId, int tokenId) {
        SysUser updateUserEntity = new SysUser();
        updateUserEntity.setId(userId);
        updateUserEntity.setTokenId(tokenId);
        userMapper.updateUser(updateUserEntity);

        SysUserUpdateEvent event = new SysUserUpdateEvent(userId, null);
        context.publishEvent(event);
    }

    @Override
    public void logout(LoginUser loginUser) {
        if (loginUser == null) {
            return;
        }

        int tokenId = generateTokenId();
        resetLoginTokenId(loginUser.getId(), tokenId);
    }

    @Transactional
    public int createUser(SysUser sysUser) {
        SysUser historyEntity = userMapper.selectByUni(sysUser.getUsername());
        if (historyEntity != null) {
            throw ExceptionUtil.business(SysWebErrorCodeEnums.USER_EXISTS, sysUser.getUsername());
        }

        String password = sysUser.getPassword();
        String salt = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);

        String sourcePassword = password + salt;
        String ciphertextPwd = passwordEncoder.encode(sourcePassword);

        String twoFactorAuthkey = googleAuthenticator.createCredentials().getKey();

        sysUser.setPassword(ciphertextPwd);
        sysUser.setSalt(salt);
        sysUser.setTwoFactorAuthKey(twoFactorAuthkey);

        return userMapper.insertUser(sysUser);
    }

    protected void validUser2Fa(String twoFactorAuthKey, Integer codeFa) {
        if (!sysWebProperties.getUser().getCaptcha().isTwoFactorAuthEnabled()) {
            return;
        }

        boolean isCodeValid = googleAuthenticator.authorize(twoFactorAuthKey, codeFa);
        if (!isCodeValid) {
            throw ExceptionUtil.business(SysWebErrorCodeEnums.USER_LOGIN_CAPTCHA_ERROR);
        }
    }

    @Override
    public SysUser obtainUserById(Long userId) {
        if (userId == null) {
            return null;
        }

        CacheWrapper<SysUser> wrapperUser = cacheSysUser.getIfPresent(userId);
        if (wrapperUser == null) {
            ReentrantLock lock = userDclLock.computeIfAbsent(userId, (id) -> new ReentrantLock());
            lock.lock();
            try {
                wrapperUser = cacheSysUser.getIfPresent(userId);
                if (wrapperUser == null) {
                    SysUser sysUser = userMapper.selectUserById(userId);
                    wrapperUser = new CacheWrapper<>();
                    wrapperUser.setData(sysUser);
                    cacheSysUser.put(userId, wrapperUser);
                }
            } finally {
                lock.unlock();
            }
        }

        return wrapperUser.getData();
    }

    @Override
    @Transactional
    public void updateUserPwd(@NonNull Long userId, @NonNull String password) {
        SysUser historyEntity = userMapper.selectUserById(userId);
        if (historyEntity == null) {
            throw ExceptionUtil.business(SysWebErrorCodeEnums.USER_NOT_EXISTS);
        }

        String salt = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
        String ciphertextPwd = passwordEncoder.encode(password + salt);

        SysUser user = new SysUser();
        user.setUserId(userId);
        user.setPassword(ciphertextPwd);
        user.setSalt(salt);

        userMapper.updateUser(user);
    }

    @Override
    @Transactional
    public String resetTwoFactorAuthKey(@NonNull Long id) {
        SysUser historyEntity = userMapper.selectUserById(id);
        if (historyEntity == null) {
            throw ExceptionUtil.business(SysWebErrorCodeEnums.USER_NOT_EXISTS);
        }

        String twoFactorAuthkey = googleAuthenticator.createCredentials().getKey();

        SysUser updateEntity = new SysUser();
        updateEntity.setId(id).setTwoFactorAuthKey(twoFactorAuthkey);

        userMapper.updateUser(updateEntity);

        return twoFactorAuthkey;
    }

    @Override
    @Transactional
    public void updatePwdByOldValue(
            @NonNull Long userId, @NonNull String oldPassword, @NonNull String newPassword) {
        SysUser historyEntity = userMapper.selectUserById(userId);
        if (historyEntity == null) {
            throw ExceptionUtil.business(SysWebErrorCodeEnums.USER_NOT_EXISTS);
        }

        String sourcePassword = oldPassword + historyEntity.getSalt();
        boolean matchFlag = passwordEncoder.matches(sourcePassword, historyEntity.getPassword());
        if (!matchFlag) {
            throw ExceptionUtil.business(SysWebErrorCodeEnums.USER_OLD_PASSWORD_NOT_MATCH);
        }

        updateUserPwd(userId, newPassword);
    }
}
