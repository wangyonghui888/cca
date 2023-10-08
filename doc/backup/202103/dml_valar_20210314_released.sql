update tybss_new.t_merchant_language set status =1 where id =3;

drop index index_modify_time_merchant on tybss_new.t_order;
create index index_merchant_modify_time
    on tybss_new.t_order (merchant_id, modify_time);

drop index index_uid on tybss_new.t_order;
create index index_uid_createTime
    on tybss_new.t_order (uid, create_time);

drop index index_create_time_merchant on tybss_new.t_order;
create index index_create_time
    on tybss_new.t_order (create_time);

create index index_modify_time
    on tybss_new.t_order (modify_time);

create index index_fake_name
    on tybss_new.t_user (fake_name);