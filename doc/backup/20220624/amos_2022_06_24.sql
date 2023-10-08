alter table t_merchant_config  add book_bet tinyint(4)  default 1  COMMENT '预约投注开关' after is_app;


#关闭免转钱包的商户的预约投注开关
update t_merchant_config a ,t_merchant b set a.book_bet = 0   where a.merchant_code = b.merchant_code and b.transfer_mode = 1;

#批量赛事开关  球赛开关
alter table t_merchant_config  MODIFY `filter_sport` 	longtext  null COMMENT '过滤赛种';
alter table t_merchant_config  MODIFY `filter_league` 	longtext  null COMMENT '过滤联赛';


#分组赔率 
alter table tybss_merchant_common.dynamic_odds_grouping_config add profit_threshold 	decimal(11,2)	 default 5000  COMMENT '盈利金额阈值' after  	bet_num_threshold ;

alter table tybss_merchant_common.dynamic_odds_grouping_config add profit_rate_threshold 	varchar(10)	 default 100  COMMENT '盈利率阈值' after  	profit_threshold ;

alter table tybss_merchant_common.dynamic_odds_grouping_config add win_rate_threshold 	varchar(10)  default 100  COMMENT '胜率阈值' after  	profit_rate_threshold ;

