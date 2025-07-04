
-- DROP TABLE IF EXISTS example_test;
CREATE TABLE example_test
(
    id      BIGINT AUTO_INCREMENT COMMENT '主键ID' PRIMARY KEY,
    code    VARCHAR(20) NULL,
    created DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = innodb COMMENT '示例测试表';

INSERT INTO example_test (code) VALUES ('slave_code_001');
INSERT INTO example_test (code) VALUES ('slave_code_002');
INSERT INTO example_test (code) VALUES ('slave_code_003');
INSERT INTO example_test (code) VALUES ('slave_code_004');
INSERT INTO example_test (code) VALUES ('slave_code_005');
INSERT INTO example_test (code) VALUES ('slave_code_006');
INSERT INTO example_test (code) VALUES ('slave_code_007');
INSERT INTO example_test (code) VALUES ('slave_code_008');
INSERT INTO example_test (code) VALUES ('slave_code_009');
INSERT INTO example_test (code) VALUES ('slave_code_010');
INSERT INTO example_test (code) VALUES ('slave_code_011');
INSERT INTO example_test (code) VALUES ('slave_code_012');
