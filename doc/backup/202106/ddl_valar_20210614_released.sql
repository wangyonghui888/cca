

drop table if exists tybss_new.test;

drop table if exists tybss_new.t_user_blank;

drop table if exists tybss_new.t_user_action_history;

drop table if exists tybss_new.t_merchant_key;

alter table tybss_new.t_transfer_record_retry
    change transfer_id transfer_id varchar(32);


drop index index_modify_time_merchant on tybss_new.t_order;
create index index_merchant_modify_time
    on tybss_new.t_order (merchant_id, modify_time);