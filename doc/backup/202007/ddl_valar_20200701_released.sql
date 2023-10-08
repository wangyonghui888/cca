
ALTER TABLE tybss_report.`r_user_order_day` ADD `bet_amount_settled` DECIMAL(16,2) NULL DEFAULT NULL COMMENT '已结算投注额' AFTER `valid_tickets`;
ALTER TABLE tybss_report.`r_user_order_day` ADD `ticket_settled` INT(6) NULL DEFAULT NULL COMMENT '已结算订单数' AFTER `bet_amount_settled`;

ALTER TABLE tybss_report.`r_user_order_day_utc8` ADD `bet_amount_settled` DECIMAL(16,2) NULL DEFAULT NULL COMMENT '已结算投注额' AFTER `valid_tickets`;
ALTER TABLE tybss_report.`r_user_order_day_utc8` ADD `ticket_settled` INT(6) NULL DEFAULT NULL COMMENT '已结算订单数' AFTER `bet_amount_settled`;

#########################


ALTER TABLE tybss_report.`r_user_order_month` ADD `bet_amount_settled` DECIMAL(16,2) NULL DEFAULT NULL COMMENT '已结算投注额' ;
ALTER TABLE tybss_report.`r_user_order_month` ADD `ticket_settled` INT(6) NULL DEFAULT NULL COMMENT '已结算订单数' ;

ALTER TABLE tybss_report.`r_user_order_month_utc8` ADD `bet_amount_settled` DECIMAL(16,2) NULL DEFAULT NULL COMMENT '已结算投注额' ;
ALTER TABLE tybss_report.`r_user_order_month_utc8` ADD `ticket_settled` INT(6) NULL DEFAULT NULL COMMENT '已结算订单数' ;

#############################
ALTER TABLE tybss_report.`r_merchant_order_day` ADD `bet_amount_settled` DECIMAL(16,2) NULL DEFAULT NULL COMMENT '已结算投注额' AFTER `filed_bet_amount`;
ALTER TABLE tybss_report.`r_merchant_order_day` ADD `ticket_settled` INT(6) NULL DEFAULT NULL COMMENT '已结算订单数' AFTER `bet_amount_settled`;

ALTER TABLE tybss_report.`r_merchant_order_day_utc8` ADD `bet_amount_settled` DECIMAL(16,2) NULL DEFAULT NULL COMMENT '已结算投注额' AFTER `filed_bet_amount`;
ALTER TABLE tybss_report.`r_merchant_order_day_utc8` ADD `ticket_settled` INT(6) NULL DEFAULT NULL COMMENT '已结算订单数' AFTER `bet_amount_settled`;


ALTER TABLE tybss_report.`r_merchant_order_day` ADD `bet_settled_users` INT(8) NULL DEFAULT NULL  COMMENT '投注已结算用户';

ALTER TABLE tybss_report.`r_merchant_order_day_utc8` ADD `bet_settled_users` INT(8) NULL DEFAULT NULL  COMMENT '投注已结算用户';




##################
ALTER TABLE tybss_report.`r_merchant_order_month` ADD `bet_amount_settled` DECIMAL(16,2) NULL DEFAULT NULL COMMENT '已结算投注额';
ALTER TABLE tybss_report.`r_merchant_order_month` ADD `ticket_settled` INT(6) NULL DEFAULT NULL COMMENT '已结算订单数' ;
ALTER TABLE tybss_report.`r_merchant_order_month` ADD `bet_settled_users` INT(8) NULL DEFAULT NULL  COMMENT '投注已结算用户';


ALTER TABLE tybss_report.`r_merchant_order_month_utc8` ADD `bet_amount_settled` DECIMAL(16,2) NULL DEFAULT NULL COMMENT '已结算投注额';
ALTER TABLE tybss_report.`r_merchant_order_month_utc8` ADD `ticket_settled` INT(6) NULL DEFAULT NULL COMMENT '已结算订单数' ;
ALTER TABLE tybss_report.`r_merchant_order_month_utc8` ADD `bet_settled_users` INT(8) NULL DEFAULT NULL  COMMENT '投注已结算用户';


ALTER TABLE tybss_report.`r_merchant_order_month` ADD `profit_rate` decimal(16,4) 	 NULL DEFAULT NULL COMMENT '盈利率';

##########################################################sportID


ALTER TABLE tybss_report.`r_user_sport_order_day` ADD `bet_amount_settled` DECIMAL(16,2) NULL DEFAULT NULL COMMENT '已结算投注额' ;
ALTER TABLE tybss_report.`r_user_sport_order_day` ADD `ticket_settled` INT(6) NULL DEFAULT NULL COMMENT '已结算订单数' ;

ALTER TABLE tybss_report.`r_user_sport_order_day_utc8` ADD `bet_amount_settled` DECIMAL(16,2) NULL DEFAULT NULL COMMENT '已结算投注额' ;
ALTER TABLE tybss_report.`r_user_sport_order_day_utc8` ADD `ticket_settled` INT(6) NULL DEFAULT NULL COMMENT '已结算订单数' ;

#########################


ALTER TABLE tybss_report.`r_user_sport_order_month` ADD `bet_amount_settled` DECIMAL(16,2) NULL DEFAULT NULL COMMENT '已结算投注额' ;
ALTER TABLE tybss_report.`r_user_sport_order_month` ADD `ticket_settled` INT(6) NULL DEFAULT NULL COMMENT '已结算订单数' ;

ALTER TABLE tybss_report.`r_user_sport_order_month_utc8` ADD `bet_amount_settled` DECIMAL(16,2) NULL DEFAULT NULL COMMENT '已结算投注额' ;
ALTER TABLE tybss_report.`r_user_sport_order_month_utc8` ADD `ticket_settled` INT(6) NULL DEFAULT NULL COMMENT '已结算订单数' ;

##############

ALTER TABLE tybss_report.`r_merchant_sport_order_day` ADD `bet_amount_settled` DECIMAL(16,2) NULL DEFAULT NULL COMMENT '已结算投注额';

ALTER TABLE tybss_report.`r_merchant_sport_order_day` ADD `ticket_settled` INT(6) NULL DEFAULT NULL COMMENT '已结算订单数' ;

ALTER TABLE tybss_report.`r_merchant_sport_order_day_utc8` ADD `bet_amount_settled` DECIMAL(16,2) NULL DEFAULT NULL COMMENT '已结算投注额' ;
ALTER TABLE tybss_report.`r_merchant_sport_order_day_utc8` ADD `ticket_settled` INT(6) NULL DEFAULT NULL COMMENT '已结算订单数';


ALTER TABLE tybss_report.`r_merchant_sport_order_day` ADD `bet_settled_users` INT(8) NULL DEFAULT NULL  COMMENT '投注已结算用户';

ALTER TABLE tybss_report.`r_merchant_sport_order_day_utc8` ADD `bet_settled_users` INT(8) NULL DEFAULT NULL  COMMENT '投注已结算用户';

####################

ALTER TABLE tybss_report.`r_merchant_sport_order_month` ADD `bet_amount_settled` DECIMAL(16,2) NULL DEFAULT NULL COMMENT '已结算投注额';
ALTER TABLE tybss_report.`r_merchant_sport_order_month` ADD `ticket_settled` INT(6) NULL DEFAULT NULL COMMENT '已结算订单数' ;

ALTER TABLE tybss_report.`r_merchant_sport_order_month_utc8` ADD `bet_amount_settled` DECIMAL(16,2) NULL DEFAULT NULL COMMENT '已结算投注额' ;
ALTER TABLE tybss_report.`r_merchant_sport_order_month_utc8` ADD `ticket_settled` INT(6) NULL DEFAULT NULL COMMENT '已结算订单数' ;

ALTER TABLE tybss_report.`r_merchant_sport_order_month` ADD `bet_settled_users` INT(8) NULL DEFAULT NULL  COMMENT '投注已结算用户';

ALTER TABLE tybss_report.`r_merchant_sport_order_month_utc8` ADD `bet_settled_users` INT(8) NULL DEFAULT NULL  COMMENT '投注已结算用户';