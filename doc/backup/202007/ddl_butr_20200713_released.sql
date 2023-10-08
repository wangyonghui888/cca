ALTER TABLE tybss_report.`r_merchant_order_day`
    ADD `agent_level` tinyint(2) DEFAULT NULL COMMENT '代理级别(0,直营;1:渠道;2:二级代理)';
ALTER TABLE tybss_report.`r_merchant_sport_order_day`
    ADD `agent_level` tinyint(2) DEFAULT NULL COMMENT '代理级别(0,直营;1:渠道;2:二级代理)';

ALTER TABLE tybss_report.`r_merchant_order_day_utc8`
    ADD `agent_level` tinyint(2) DEFAULT NULL COMMENT '代理级别(0,直营;1:渠道;2:二级代理)';

ALTER TABLE tybss_report.`r_merchant_order_month`
    ADD `agent_level` tinyint(2) DEFAULT NULL COMMENT '代理级别(0,直营;1:渠道;2:二级代理)';

ALTER TABLE tybss_report.`r_merchant_sport_order_month`
    ADD `agent_level` tinyint(2) DEFAULT NULL COMMENT '代理级别(0,直营;1:渠道;2:二级代理)';

ALTER TABLE tybss_report.`r_merchant_order_month_utc8`
    ADD `agent_level` tinyint(2) DEFAULT NULL COMMENT '代理级别(0,直营;1:渠道;2:二级代理)';


drop table if exists tybss_report.r_user_sport_order_day_utc8;

CREATE TABLE tybss_report.`r_user_sport_order_day_utc8`
(
    `id`                  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT ' 自动编号 ',
    `sport_id`            int(2)                                                        DEFAULT NULL COMMENT '球类id',
    `user_id`             bigint(32)                                                    DEFAULT NULL,
    `user_name`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs  DEFAULT NULL,
    `merchant_code`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs  DEFAULT NULL,
    `merchant_name`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL,
    `time`                int(32)                                                       DEFAULT NULL COMMENT '格式20200110',
    `bet_num`             int(6)                                                        DEFAULT NULL COMMENT '投注笔数',
    `bet_amount`          decimal(16, 2)                                                DEFAULT NULL COMMENT '投注金额',
    `profit`              decimal(16, 2)                                                DEFAULT NULL COMMENT '盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)',
    `return_amount`       decimal(16, 2)                                                DEFAULT NULL COMMENT '返还金额',
    `profit_rate`         decimal(16, 4)                                                DEFAULT NULL,
    `settle_order_num`    int(6)                                                        DEFAULT NULL COMMENT '已结算注单数',
    `settle_order_amount` decimal(16, 2)                                                DEFAULT NULL COMMENT '已结算注单总金额',
    `settle_profit`       decimal(16, 2)                                                DEFAULT NULL COMMENT '已结算注单盈亏金额盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)',
    `settle_profit_rate`  decimal(16, 4)                                                DEFAULT NULL,
    `settle_return`       decimal(16, 0)                                                DEFAULT NULL,
    `updated_time`        bigint(20)                                                   NOT NULL COMMENT '根据此字段增量同步到elasticsearch',
    `year`                int(11)                                                       DEFAULT NULL,
    `parent_code`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs  DEFAULT NULL COMMENT '商户父级编码',
    `parent_name`         varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '商户父级名称',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_as_cs
  ROW_FORMAT = DYNAMIC;

drop table if exists tybss_report.r_user_sport_order_month_utc8;
CREATE TABLE `r_user_sport_order_month_utc8`
(
    `id`                  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '自动编号',
    `sport_id`            tinyint(4)                                                    DEFAULT NULL,
    `user_id`             bigint(32)                                                    DEFAULT NULL,
    `user_name`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs  DEFAULT NULL,
    `merchant_code`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs  DEFAULT NULL,
    `merchant_name`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL,
    `time`                int(32)                                                       DEFAULT NULL COMMENT '格式202001',
    `bet_num`             int(6)                                                        DEFAULT NULL COMMENT '投注笔数',
    `bet_amount`          decimal(16, 2)                                                DEFAULT NULL COMMENT '投注金额',
    `profit`              decimal(16, 2)                                                DEFAULT NULL COMMENT '盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)',
    `profit_rate`         decimal(16, 4)                                                DEFAULT NULL,
    `settle_order_num`    int(6)                                                        DEFAULT NULL COMMENT '已结算注单数',
    `settle_order_amount` decimal(16, 2)                                                DEFAULT NULL COMMENT '已结算注单总金额',
    `settle_profit`       decimal(16, 2)                                                DEFAULT NULL COMMENT '已结算注单盈亏金额盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)',
    `settle_profit_rate`  decimal(16, 4)                                                DEFAULT NULL,
    `settle_return`       decimal(16, 0)                                                DEFAULT NULL,
    `active_days`         int(4)                                                        DEFAULT NULL COMMENT '活跃天数',
    `updated_time`        bigint(20)                                                   NOT NULL COMMENT '根据此字段增量同步到elasticsearch',
    `parent_code`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs  DEFAULT NULL COMMENT '商户父级编码',
    `parent_name`         varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '商户父级名称',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_as_cs
  ROW_FORMAT = DYNAMIC;

drop table if exists tybss_report.r_merchant_sport_order_day_utc8;

CREATE TABLE `r_merchant_sport_order_day_utc8`
(
    `id`                      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '自动编号',
    `sport_id`                tinyint(4)                                                    DEFAULT NULL,
    `merchant_code`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs  DEFAULT NULL,
    `merchant_name`           varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '商户名称',
    `time`                    int(32)                                                       DEFAULT NULL COMMENT '格式:20210110',
    `merchant_level`          tinyint(2)                                                    DEFAULT NULL COMMENT '商户等级',
    `add_user`                int(16)                                                       DEFAULT '0' COMMENT '新增用户数',
    `register_total_user_sum` int(11)                                                       DEFAULT NULL,
    `bet_user_rate`           decimal(16, 4)                                                DEFAULT NULL,
    `profit`                  decimal(32, 0)                                                DEFAULT NULL COMMENT '投注量-派彩金额，即指商户注单的毛盈利，暂不计算返水等',
    `profit_rate`             decimal(16, 4)                                                DEFAULT NULL,
    `return_rate`             decimal(16, 4)                                                DEFAULT NULL,
    `return_amount`           decimal(32, 2)                                                DEFAULT NULL COMMENT '派彩金额',
    `bet_amount`              decimal(32, 2)                                                DEFAULT NULL COMMENT '投注额',
    `order_sum`               int(16)                                                       DEFAULT NULL COMMENT '订单数',
    `bet_user_sum`            int(16)                                                       DEFAULT NULL COMMENT '投注用户数',
    `first_bet_user_sum`      int(11)                                                       DEFAULT NULL,
    `settle_user_rate`        decimal(16, 4)                                                DEFAULT NULL,
    `settle_profit`           decimal(32, 2)                                                DEFAULT NULL COMMENT '当天结算注单-当天结算注单派彩金额',
    `settle_return`           decimal(32, 2)                                                DEFAULT NULL COMMENT '当天结算注单派彩金额',
    `settle_return_rate`      decimal(16, 4)                                                DEFAULT NULL,
    `settle_bet_amount`       decimal(32, 2)                                               NOT NULL COMMENT '结算注单总额',
    `settle_order_num`        int(16)                                                       DEFAULT NULL COMMENT '结算注单数',
    `updated_time`            bigint(20)                                                   NOT NULL COMMENT '根据此字段增量同步到elasticsearch',
    `year`                    int(11)                                                       DEFAULT NULL,
    `parent_code`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs  DEFAULT NULL COMMENT '商户父级编码',
    `parent_name`             varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '商户父级名称',
    `settle_users`            int(16)                                                       DEFAULT NULL COMMENT '总结算玩家数',
    `agent_level`             tinyint(2)                                                    DEFAULT NULL COMMENT '代理级别(0,直营;1:渠道;2:二级代理)',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_as_cs
  ROW_FORMAT = DYNAMIC;


drop table if exists tybss_report.r_merchant_sport_order_month_utc8;

CREATE TABLE `r_merchant_sport_order_month_utc8`
(
    `id`                      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '自动编号',
    `sport_id`                tinyint(4)                                                    DEFAULT NULL,
    `merchant_code`           varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs  DEFAULT NULL,
    `merchant_name`           varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '商户名称',
    `time`                    int(32)                                                       DEFAULT NULL COMMENT '格式:202101',
    `merchant_level`          tinyint(2)                                                    DEFAULT NULL COMMENT '商户等级',
    `add_user`                int(16)                                                       DEFAULT '0' COMMENT '新增用户数',
    `register_total_user_sum` int(11)                                                       DEFAULT NULL,
    `bet_user_rate`           decimal(16, 4)                                                DEFAULT NULL,
    `profit`                  decimal(32, 2)                                                DEFAULT NULL COMMENT '投注量-派彩金额，即指商户注单的毛盈利，暂不计算返水等',
    `profit_rate`             decimal(16, 4)                                                DEFAULT NULL,
    `return_rate`             decimal(16, 4)                                                DEFAULT NULL,
    `return_amount`           decimal(32, 2)                                                DEFAULT NULL COMMENT '派彩金额',
    `bet_amount`              decimal(32, 2)                                                DEFAULT NULL COMMENT '投注额',
    `order_sum`               int(16)                                                       DEFAULT NULL COMMENT '订单数',
    `bet_user_sum`            int(16)                                                       DEFAULT NULL COMMENT '投注用户数',
    `first_bet_user_sum`      int(16)                                                       DEFAULT NULL,
    `settle_user_rate`        decimal(16, 4)                                                DEFAULT NULL,
    `settle_profit`           decimal(32, 2)                                                DEFAULT NULL COMMENT '当天结算注单-当天结算注单派彩金额',
    `settle_return`           decimal(32, 2)                                                DEFAULT NULL COMMENT '当天结算注单派彩金额',
    `settle_return_rate`      decimal(16, 4)                                                DEFAULT NULL,
    `settle_bet_amount`       decimal(32, 2)                                                DEFAULT NULL COMMENT '结算注单总额',
    `settle_order_num`        int(16)                                                       DEFAULT NULL COMMENT '结算注单数',
    `updated_time`            bigint(20)                                                   NOT NULL COMMENT '根据此字段增量同步到elasticsearch',
    `parent_code`             varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs  DEFAULT NULL COMMENT '商户父级编码',
    `parent_name`             varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '商户父级名称',
    `settle_users`            int(16)                                                       DEFAULT NULL COMMENT '总结算玩家数',
    `agent_level`             tinyint(2)                                                    DEFAULT NULL COMMENT '代理级别(0,直营;1:渠道;2:二级代理)',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_as_cs
  ROW_FORMAT = DYNAMIC;