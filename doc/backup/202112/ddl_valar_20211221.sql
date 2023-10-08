ALTER TABLE tybss_merchant_common.`t_account_change_history`
    DROP INDEX `index_account_uid`;


alter table tybss_merchant_common.t_merchant_config
    add cash_out_update bigint null COMMENT '赔率分组最后更新时间' after settle_switch_advance;


ALTER TABLE tybss_merchant_common.t_order_detail ADD INDEX index_modify_time (`modify_time`);



