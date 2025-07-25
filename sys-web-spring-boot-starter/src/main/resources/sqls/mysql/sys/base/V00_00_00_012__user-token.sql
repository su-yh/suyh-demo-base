

ALTER TABLE sys_user
    ADD COLUMN token_id int NULL COMMENT '用户登录状态的有效token_id(>= 0 有效，负值无效)';


