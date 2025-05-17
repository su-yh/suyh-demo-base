package com.suyh.sys.web.service;

import com.suyh.sys.web.mybatis.entity.SysUserEntity;
import com.suyh.sys.web.mybatis.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author suyh
 * @since 2025-05-16
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SysUserService {
    private final SysUserMapper sysUserMapper;

    public SysUserEntity obtainUserById(Long id) {
        if (id == null) {
            return null;
        }

        return sysUserMapper.selectById(id);
    }
}
