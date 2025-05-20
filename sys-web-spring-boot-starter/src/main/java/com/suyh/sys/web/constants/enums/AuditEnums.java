package com.suyh.sys.web.constants.enums;

import com.base.web.audit.IAudit;

/**
 * @author suyh
 * @since 2024-10-19
 */
public enum AuditEnums implements IAudit {
    SYSTEM_USER_CREATE("System Manage/User Manage", "新建用户"),
    SYSTEM_USER_EDIT("System Manage/User Manage", "编辑用户"),
    SYSTEM_USER_DELETE("System Manage/User Manage", "删除用户"),
    SYSTEM_ROLE_CREATE("System Manage/Role Manage", "新建角色"),
    SYSTEM_ROLE_EDIT("System Manage/Role Manage", "编辑角色"),
    SYSTEM_ROLE_DELETE("System Manage/Role Manage", "删除角色"),
    ;

    private final String page;
    private final String operation;

    AuditEnums(String page, String operation) {
        this.page = page;
        this.operation = operation;
    }

    @Override
    public String getPage() {
        return page;
    }

    @Override
    public String getOperation() {
        return operation;
    }
}
