

CREATE TABLE example_test
(
    id                BIGINT AUTO_INCREMENT COMMENT '主键ID' PRIMARY KEY,
    code              VARCHAR(20)    NULL,
    created           DATETIME       NULL COMMENT '创建时间',
    updated           DATETIME       NULL COMMENT '更新时间'
) ENGINE = innodb COMMENT '示例测试表';


