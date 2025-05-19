
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


ALTER TABLE sys_user
    ADD COLUMN dept_id bigint NULL COMMENT '部门ID';
ALTER TABLE sys_user
    ADD COLUMN user_type varchar(2) default '00' COMMENT '用户类型（00系统用户 01注册用户）';
ALTER TABLE sys_user
    ADD COLUMN email varchar(50) default '' COMMENT '用户邮箱';
ALTER TABLE sys_user
    ADD COLUMN phonenumber varchar(20) default '' COMMENT '手机号码';
ALTER TABLE sys_user
    ADD COLUMN sex char default '0' COMMENT '用户性别（0男 1女 2未知）';
ALTER TABLE sys_user
    ADD COLUMN avatar varchar(100) default '' COMMENT '头像路径';
ALTER TABLE sys_user
    ADD COLUMN status char default '0' COMMENT '帐号状态（0正常 1停用）';
ALTER TABLE sys_user
    ADD COLUMN del_flag char default '0' COMMENT '删除标志（0代表存在 2代表删除）';
ALTER TABLE sys_user
    ADD COLUMN login_ip varchar(128)    default '' COMMENT '最后登录IP';
ALTER TABLE sys_user
    ADD COLUMN login_date datetime    COMMENT '最后登录时间';
ALTER TABLE sys_user
    ADD COLUMN pwd_update_date datetime    COMMENT '密码最后更新时间';
ALTER TABLE sys_user
    ADD COLUMN create_by varchar(64)    default '' COMMENT '创建者';
ALTER TABLE sys_user
    ADD COLUMN update_by varchar(64)    default '' COMMENT '更新者';
ALTER TABLE sys_user
    ADD COLUMN remark varchar(500)    default '' COMMENT '备注';

ALTER TABLE sys_user ADD INDEX idx_name(username) USING BTREE;

-- 初始化一个admin 用户: 密码也是admin
-- admin 用户的ID 必须 是1 ，这在后面的关联关系的时候需要用到
INSERT INTO sys_user (id, username, password, nickname, salt, two_factor_auth_key)
VALUES (1, 'admin', '$2a$10$RO4jsSW3Xv1d9LMFk5vj/eF9Od2w5eCTroLMhOZCJBsanez/QMWA2', 'admin',
        '837dc2085500480f8b0c2b0222d15f10', 'HMG6NIOZPH65HGKIMLUW7YIDCG2M65XN');



