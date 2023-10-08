alter table tybss_merchant_common.t_merchant_config  add book_bet tinyint(4)  default 0  COMMENT '预约投注开关' after is_app;


alter table tybss_merchant_common.t_order_times_settle_info  add chang_reason varchar(100)  COMMENT '变动原因' after remark ;
alter table tybss_merchant_common.t_order_times_settle_info  add is_damage tinyint(4)  COMMENT '是否赔偿' after  chang_reason ;