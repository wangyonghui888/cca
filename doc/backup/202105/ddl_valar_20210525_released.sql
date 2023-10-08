drop table if exists tybss_report.ac_user_order_hour;
CREATE TABLE tybss_report.`ac_user_order_hour`
(
    `id`                  varchar(64) NOT NULL COMMENT '自动编号' primary key,
    `uid`                 bigint(32)                                                   DEFAULT NULL,
    `time`                bigint(32)                                                   DEFAULT NULL COMMENT 'yyyyMMddHH',
    `user_name`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL,
    `merchant_code`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL,
    `profit`              decimal(16, 2)                                               DEFAULT NULL COMMENT '盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)',
    `return_amount`       decimal(16, 2)                                               DEFAULT NULL COMMENT '返还金额',
    `valid_bet_amount`    decimal(16, 2)                                               DEFAULT NULL COMMENT '有效投注金额',
    `eu_valid_bet_amount` decimal(16, 2)                                               DEFAULT NULL COMMENT 'EU有效投注数',
    `updated_time`        bigint(20)  NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_as_cs
  ROW_FORMAT = DYNAMIC;


drop table if exists tybss_report.ac_user_order_day;
CREATE TABLE tybss_report.`ac_user_order_day`
(
    `id`                  varchar(64) NOT NULL COMMENT '自动编号' primary key,
    `uid`                 bigint(32)                                                   DEFAULT NULL,
    `time`                bigint(32)                                                   DEFAULT NULL COMMENT 'yyyyMMdd',
    `user_name`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL,
    `merchant_code`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL,
    `profit`              decimal(16, 2)                                               DEFAULT NULL COMMENT '盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)',
    `return_amount`       decimal(16, 2)                                               DEFAULT NULL COMMENT '返还金额',
    `valid_bet_amount`    decimal(16, 2)                                               DEFAULT NULL COMMENT '有效投注金额',
    `eu_valid_bet_amount` decimal(16, 2)                                               DEFAULT NULL COMMENT 'EU有效投注数',
    `updated_time`        bigint(20)  NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_as_cs
  ROW_FORMAT = DYNAMIC;

drop TABLE if EXISTS tybss_new.user_statistic;
