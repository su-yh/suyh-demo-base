package com.web.sys.event;

import com.web.ruoyi.mybatis.entity.SysUser;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author suyh
 * @since 2025-07-21
 */
@Getter
public class SysUserUpdateEvent extends ApplicationEvent {
    private final Long userId;
    private final SysUser sysUser;

    public SysUserUpdateEvent(Long userId, SysUser sysUser) {
        super("");

        this.userId = userId;
        this.sysUser = sysUser;
    }
}
