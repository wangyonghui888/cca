
update tybss_new.s_virtual_sport_type
set introduction='虚拟赛狗',
    remark='虚拟赛狗'
where id = 1002;

update tybss_new.s_virtual_sport_type
set introduction='虚拟赛马',
    remark='虚拟赛狗'
where id = 1011;

update tybss_new.s_virtual_sport_type
set introduction='虚拟泥地赛车',
    remark='虚拟泥地赛车'
where id = 1007;

update tybss_new.s_virtual_sport_type
set introduction='虚拟卡丁车',
    remark='虚拟卡丁车'
where id = 1008;

update tybss_new.s_virtual_sport_type
set introduction='虚拟泥地摩托车',
    remark='虚拟泥地摩托车'
where id = 1009;

update tybss_new.s_virtual_sport_type
set introduction='虚拟摩托车',
    remark='虚拟摩托车'
where id = 1010;

update tybss_new.s_virtual_sport_type
set introduction='虚拟摩托车',
    remark='虚拟摩托车'
where id = 1010;

drop index index_create_time on tybss_new.t_order;

create index index_create_time_merchant
    on tybss_new.t_order (create_time, merchant_id);


alter table tybss_report.r_merchant_finance_day drop order_back_num;
alter table tybss_report.r_merchant_finance_day drop transfer_num;

alter table tybss_report.r_merchant_finance_day drop settle_order_back_num;
alter table tybss_report.r_merchant_finance_day drop settle_transfer_num;


alter table tybss_report.r_merchant_finance_day drop order_num;
alter table tybss_report.r_merchant_finance_day drop settle_order_valid_num;

alter table tybss_report.r_merchant_finance_day drop profit_amount;

alter table tybss_report.r_merchant_finance_day drop create_time;

alter table tybss_report.r_merchant_finance_day drop modify_time;



alter table tybss_report.r_merchant_finance_day_utc8 drop order_back_num;
alter table tybss_report.r_merchant_finance_day_utc8 drop transfer_num;

alter table tybss_report.r_merchant_finance_day_utc8 drop settle_order_back_num;
alter table tybss_report.r_merchant_finance_day_utc8 drop settle_transfer_num;

alter table tybss_report.r_merchant_finance_day_utc8 drop order_num;
alter table tybss_report.r_merchant_finance_day_utc8 drop settle_order_valid_num;


alter table tybss_report.r_merchant_finance_day_utc8 drop profit_amount;

alter table tybss_report.r_merchant_finance_day_utc8 drop modify_time;
alter table tybss_report.r_merchant_finance_day_utc8 drop create_time;


alter table tybss_report.r_merchant_finance_day add column vr_order_amount_total decimal (32,2)  NULL COMMENT 'VR投注额';
alter table tybss_report.r_merchant_finance_day add column vr_settle_amount decimal (32,2)  NULL COMMENT 'VR返还额';
alter table tybss_report.r_merchant_finance_day add column vr_platform_profit decimal (32,2)  NULL COMMENT 'VR盈利';
alter table tybss_report.r_merchant_finance_day add column vr_order_valid_num int   NULL COMMENT 'VR注单数';
alter table tybss_report.r_merchant_finance_day add column vr_order_user_num int   NULL COMMENT 'VR用户数';



alter table tybss_report.r_merchant_finance_day add column vr_settle_order_amount_total decimal (32,2)  NULL COMMENT 'VR投注额';
alter table tybss_report.r_merchant_finance_day add column vr_settle_settle_amount decimal (32,2)  NULL COMMENT 'VR返还额';
alter table tybss_report.r_merchant_finance_day add column vr_settle_platform_profit decimal (32,2)  NULL COMMENT 'VR盈利';
alter table tybss_report.r_merchant_finance_day add column vr_settle_order_user_num int   NULL COMMENT 'VR注单数';
alter table tybss_report.r_merchant_finance_day add column vr_settle_order_num int   NULL COMMENT 'VR用户数';
alter table tybss_report.r_merchant_finance_day add column vr_settle_order_num int   NULL COMMENT 'VR用户数';
alter table tybss_report.r_merchant_finance_day add column t_order_user_num int   NULL COMMENT '用户数';
alter table tybss_report.r_merchant_finance_day add column t_settle_order_user int   NULL COMMENT '用户数';


alter table tybss_report.r_merchant_finance_day_utc8 add column vr_order_amount_total decimal (32,2)  NULL COMMENT 'VR投注额';
alter table tybss_report.r_merchant_finance_day_utc8 add column vr_settle_amount decimal (32,2)  NULL COMMENT 'VR返还额';
alter table tybss_report.r_merchant_finance_day_utc8 add column vr_platform_profit decimal (32,2)  NULL COMMENT 'VR盈利';
alter table tybss_report.r_merchant_finance_day_utc8 add column vr_order_valid_num int   NULL COMMENT 'VR注单数';
alter table tybss_report.r_merchant_finance_day_utc8 add column vr_order_user_num int   NULL COMMENT 'VR用户数';



alter table tybss_report.r_merchant_finance_day_utc8 add column vr_settle_order_amount_total decimal (32,2)  NULL COMMENT 'VR投注额';
alter table tybss_report.r_merchant_finance_day_utc8 add column vr_settle_settle_amount decimal (32,2)  NULL COMMENT 'VR返还额';
alter table tybss_report.r_merchant_finance_day_utc8 add column vr_settle_platform_profit decimal (32,2)  NULL COMMENT 'VR盈利';
alter table tybss_report.r_merchant_finance_day_utc8 add column vr_settle_order_num int   NULL COMMENT 'VR注单数';
alter table tybss_report.r_merchant_finance_day_utc8 add column vr_settle_order_user_num int   NULL COMMENT 'VR用户数';
alter table tybss_report.r_merchant_finance_day_utc8 add column t_order_user_num int   NULL COMMENT '用户数';
alter table tybss_report.r_merchant_finance_day_utc8 add column t_settle_order_user int   NULL COMMENT '用户数';





