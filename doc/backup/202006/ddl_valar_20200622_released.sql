ALTER TABLE tybss_new.`t_settle`
    DROP INDEX `index_status_time`;

ALTER TABLE tybss_new.`t_settle`
    ADD INDEX index_create_time (`create_time`);

alter table tybss_new.t_user add column total_tickets INT(6) NULL   COMMENT '订单数' AFTER `profit`;


ALTER TABLE tybss_report.`r_user_order_day` DROP `live_order_num`;

ALTER TABLE tybss_report.`r_user_order_day` DROP `live_order_amount`;

ALTER TABLE tybss_report.`r_user_order_day` DROP `live_profit`;

ALTER TABLE tybss_report.`r_user_order_day` DROP `live_profit_rate`;

ALTER TABLE tybss_report.`r_user_order_day` DROP `live_return`;
######################
ALTER TABLE tybss_report.`r_user_order_week` DROP `live_order_num`;

ALTER TABLE tybss_report.`r_user_order_week` DROP `live_order_amount`;

ALTER TABLE tybss_report.`r_user_order_week` DROP `live_profit`;

ALTER TABLE tybss_report.`r_user_order_week` DROP `live_profit_rate`;

ALTER TABLE tybss_report.`r_user_order_week` DROP `live_return`;
#########
ALTER TABLE tybss_report.`r_user_order_month` DROP `live_order_num`;

ALTER TABLE tybss_report.`r_user_order_month` DROP `live_order_amount`;

ALTER TABLE tybss_report.`r_user_order_month` DROP `live_profit`;

ALTER TABLE tybss_report.`r_user_order_month` DROP `live_profit_rate`;

ALTER TABLE tybss_report.`r_user_order_month` DROP `live_return`;

drop table tybss_report.r_user_order_year;
#############
ALTER TABLE tybss_report.`r_user_sport_order_day` DROP `live_order_num`;

ALTER TABLE tybss_report.`r_user_sport_order_day` DROP `live_order_amount`;

ALTER TABLE tybss_report.`r_user_sport_order_day` DROP `live_profit`;

ALTER TABLE tybss_report.`r_user_sport_order_day` DROP `live_profit_rate`;

ALTER TABLE tybss_report.`r_user_sport_order_day` DROP `live_return`;
##############
ALTER TABLE tybss_report.`r_user_sport_order_week` DROP `live_order_num`;

ALTER TABLE tybss_report.`r_user_sport_order_week` DROP `live_order_amount`;

ALTER TABLE tybss_report.`r_user_sport_order_week` DROP `live_profit`;

ALTER TABLE tybss_report.`r_user_sport_order_week` DROP `live_profit_rate`;

ALTER TABLE tybss_report.`r_user_sport_order_week` DROP `live_return`;

###################
ALTER TABLE tybss_report.`r_user_sport_order_month` DROP `live_order_num`;

ALTER TABLE tybss_report.`r_user_sport_order_month` DROP `live_order_amount`;

ALTER TABLE tybss_report.`r_user_sport_order_month` DROP `live_profit`;

ALTER TABLE tybss_report.`r_user_sport_order_month` DROP `live_profit_rate`;

ALTER TABLE tybss_report.`r_user_sport_order_month` DROP `live_return`;

drop table tybss_report.r_user_sport_order_year;

