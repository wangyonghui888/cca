alter table tybss_new.t_merchant_config
    add column settle_switch_advance  int(1) DEFAULT 0 COMMENT '提前结算开关,0关闭，1为开';

update tybss_new.t_merchant_language set status =1 where id =4;

delete from tybss_report.r_user_order_day_utc8 where id =22430718094743552020210327;