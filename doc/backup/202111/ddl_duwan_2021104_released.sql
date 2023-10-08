
alter table tybss_merchant_common.t_merchant_config
    add column filter_sport varchar(1000)  DEFAULT NULL COMMENT '过滤赛种';

alter table tybss_merchant_common.t_merchant_config
    add column filter_league varchar(1000)  DEFAULT NULL COMMENT '过滤联赛';