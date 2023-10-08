alter table tybss_new.t_merchant_config
    add column merchant_tag int(1)  DEFAULT 0 COMMENT '0 现金网，1 信用网';

alter table tybss_new.t_user
    add column agent_id   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs  DEFAULT NULL COMMENT '信用代理ID';
