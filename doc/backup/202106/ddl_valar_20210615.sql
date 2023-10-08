
drop index index_crater_time_type on tybss_new.t_transfer_record;
create index index_craterTime
    on tybss_new.t_transfer_record (create_time);

create index index_bizType_craterTime
    on tybss_new.t_transfer_record (status, biz_type, create_time);

