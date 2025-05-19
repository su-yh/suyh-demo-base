package com.suyh.ruoyi.web.component;

import com.suyh.base.web.constants.enums.BaseWebErrorCodeEnums;
import com.suyh.base.web.exception.ExceptionUtil;
import com.suyh.base.web.user.LoginUser;
import com.suyh.ruoyi.web.mybatis.entity.SysUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * @author suyh
 * @since 2025-05-19
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class SysUserComponent {

    public static SysUser getUser(@NonNull LoginUser loginUser) {
        // TODO: suyh - 需要实现
        throw ExceptionUtil.business(BaseWebErrorCodeEnums.NO_IMPLEMENT);
    }
}
