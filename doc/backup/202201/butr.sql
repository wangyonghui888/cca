drop table if exists `tybss_merchant_common`.`t_user_check_log`;
CREATE TABLE `tybss_merchant_common`.`t_user_check_log`
(
    `id`             bigint(20)                                                    NOT NULL AUTO_INCREMENT,
    `uid`            bigint(20)                                                    NOT NULL,
    `merchant_code`  varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs  NOT NULL,
    `user_name`      varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL,
    `submit_user`    varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL,
    `check_user`     varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs,
    `check_reason`   varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs,
    `check_explain`  varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs,
    `check_time`     bigint(20),
    `create_time`    bigint(20)                                                    NOT NULL,
    `create_user`    varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL,
    `modify_time`    bigint(20)                                                    NOT NULL,
    `modify_user`    varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL,
    `check_result`   varchar(1000) COLLATE utf8mb4_0900_as_cs,
    `result_explain` varchar(1000) COLLATE utf8mb4_0900_as_cs,
    `status`         bigint(1) DEFAULT 0,
    PRIMARY KEY (`id`),
    KEY `user_check_uid` (`uid`) USING BTREE,
    KEY `user_check_user_name` (`user_name`) USING BTREE,
    KEY `user_check_merchant_code` (`merchant_code`) USING BTREE,
    KEY `user_check_create_time` (`create_time`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_as_cs;
