alter table tybss_merchant_common.t_merchant_config
    add tag_market_level_pc bigint null COMMENT '赔率分组PC' after tag_market_level;


update tybss_merchant_common.t_merchant_config set tag_market_level_pc =tag_market_level;