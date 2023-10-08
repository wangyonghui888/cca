alter table tybss_new.t_merchant_config
    add column user_prefix int DEFAULT NULL COMMENT '用户名前缀' after market_default;

alter table tybss_new.t_user
    add column fake_name varchar(80) DEFAULT NULL COMMENT '用户假名' after real_name;


drop index index_modify_time on tybss_new.t_order;

create index index_modify_time_merchant
    on tybss_new.t_order (modify_time, merchant_id);

drop index idx_bet_time on tybss_new.t_order_detail;



drop index index_uid_sportId on tybss_report.r_user_sport_order_hour_sub;
drop index index_uid_sportId on tybss_report.r_user_sport_order_hour;


create index index_time_uid on tybss_report.r_user_sport_order_hour (time,sport_id,user_id);

create index index_time_uid on tybss_report.r_user_sport_order_hour_sub (time,sport_id,user_id);



create index index_time_uid on tybss_report.r_user_sport_order_day (time,user_id,sport_id);

create index index_uid on tybss_report.r_user_sport_order_day (user_id,sport_id);

create index index_time_uid on tybss_report.r_user_sport_order_day_utc8 (time,user_id,sport_id);

create index index_uid on tybss_report.r_user_sport_order_day_utc8 (user_id,sport_id);

create index index_time_uid on tybss_report.r_user_sport_order_day_utc8_sub (time,user_id,sport_id);

create index index_uid on tybss_report.r_user_sport_order_day_utc8_sub (user_id,sport_id);




create index index_time_uid on tybss_report.r_user_sport_order_month (time,user_id,sport_id);

create index index_uid on tybss_report.r_user_sport_order_month (user_id,sport_id);

create index index_time_uid on tybss_report.r_user_sport_order_month_utc8 (time,user_id,sport_id);

create index index_uid on tybss_report.r_user_sport_order_month_utc8 (user_id,sport_id);

create index index_time_uid on tybss_report.r_user_sport_order_month_utc8_sub (time,user_id,sport_id);

create index index_uid on tybss_report.r_user_sport_order_month_utc8_sub (user_id,sport_id);




create index index_time_uid on tybss_report.r_user_order_hour (time,user_id);


create index index_time_uid on tybss_report.r_user_order_day (time,user_id);
create index index_time_uid on tybss_report.r_user_order_day_utc8 (time,user_id);
create index index_time_uid on tybss_report.r_user_order_day_utc8_sub (time,user_id);


create index index_time_uid on tybss_report.r_user_order_month (time,user_id);
create index index_time_uid on tybss_report.r_user_order_month_utc8 (time,user_id);
create index index_time_uid on tybss_report.r_user_order_month_utc8_sub (time,user_id);



drop index index_uid on tybss_report.user_order_all;
create index index_username on tybss_report.user_order_all (merchant_code,user_name);



