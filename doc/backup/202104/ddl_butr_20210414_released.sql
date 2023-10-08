alter table tybss_new.t_merchant_config
    add column tag_market_status int(1)  DEFAULT 0 COMMENT '商户行情开关,0关闭，1为开';

alter table tybss_new.t_user
    add column market_level bigint(20) default 0 COMMENT '行情等级';


Drop table if exists `tybss_new`.`t_tag_market_level`;
CREATE TABLE `tybss_new`.`t_tag_market_level`
(
    `tag_id`               BIGINT  NOT NULL PRIMARY KEY COMMENT '标签ID	',
    `tag_name`            varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs  DEFAULT NULL COMMENT '标签名称	',
    `create_time`         BIGINT  NOT NULL,
    `update_time`         BIGINT  NULL,
    `level_id`              BIGINT NOT NULL,
    `level_name`            varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs  DEFAULT NULL COMMENT '等级名称	',
    `odds_value` decimal(32,4)     DEFAULT NULL COMMENT '赔率增减'
)
COMMENT = '行情等級賠率表';
