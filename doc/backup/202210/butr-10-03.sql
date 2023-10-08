###业务公共库
alter table tybss_merchant_common.t_merchant add is_open_bill tinyint(1) NOT NULL DEFAULT 0	COMMENT '是否开启对帐：默认0关闭';
alter table tybss_merchant_y.t_merchant add is_open_bill tinyint(1) NOT NULL DEFAULT 0	COMMENT '是否开启对帐：默认0关闭';
alter table tybss_merchant_s.t_merchant add is_open_bill tinyint(1) NOT NULL DEFAULT 0	COMMENT '是否开启对帐：默认0关闭';
alter table tybss_merchant_b.t_merchant add is_open_bill tinyint(1) NOT NULL DEFAULT 0	COMMENT '是否开启对帐：默认0关闭';
###业务汇总库
alter table tybss_merchant_common.t_merchant add is_open_bill tinyint(1)	NOT NULL DEFAULT 0	COMMENT '是否开启对帐：默认0关闭';
