####备注：C、Y、B、S、业务汇总库均执行
alter table t_merchant_config add book_market_switch tinyint(4) default 1 comment '预约盘口开关(0关,1开)';