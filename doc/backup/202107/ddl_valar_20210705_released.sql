alter table tybss_new.t_merchant_config
    add column tag_market_level int NULL COMMENT '行情等级' after tag_market_status;