alter table tybss_merchant_common.t_merchant_group
    add column third_status tinyint(4)  DEFAULT 2 COMMENT '域名第三方检测开关 1关闭 2开启';