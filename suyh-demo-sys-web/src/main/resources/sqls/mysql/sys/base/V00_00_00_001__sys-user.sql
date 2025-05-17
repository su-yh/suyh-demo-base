
-- drop table if exists sys_user;

create table sys_user
(
    id                  bigint auto_increment comment '主键' primary key,
    username            VARCHAR(64) NOT NULL COMMENT '用户名',
    password            VARCHAR(64) NOT NULL COMMENT '通过盐值加密后的密码',
    nickname            VARCHAR(64) NOT NULL COMMENT '昵称',
    salt                VARCHAR(32) NOT NULL COMMENT '盐',
    two_factor_auth_key VARCHAR(32) NOT NULL COMMENT 'google 二次认证密钥',

    created             datetime DEFAULT CURRENT_TIMESTAMP,
    updated             datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) engine = innodb comment '用户信息';
