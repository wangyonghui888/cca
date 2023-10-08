####备注：C、Y、B、S、业务汇总库均执行
alter table t_merchant_config add chat_room_switch tinyint(4) default 0 comment '聊天室开关(0关,1开)';
alter table t_merchant_config add chat_min_bet_amount tinyint(4) default 1 comment '发言最低累计投注额(0关,1开)';
alter table t_merchant_config add is_default tinyint(4) default 0 comment '是否默认设置(0默认,1自定义)';
alter table t_merchant_config add three_day_amount double(10,2) default 500 comment '前三天累计投注金额';
alter table t_merchant_config add seven_day_amount double(10,2) default 1500 comment '前七天累计投注金额';