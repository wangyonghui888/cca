ALTER TABLE `tybss_new`.`t_merchant_group`
    DROP `merchant_codes`;

ALTER TABLE `tybss_new`.`t_merchant_group`
    DROP `merchant_names`;

alter table tybss_new.t_merchant
    add column merchant_group_id  bigint(20)  DEFAULT 0 COMMENT '商户组ID';

insert into `tybss_new`.`t_merchant_group`(id,group_name,status,time_type,times,update_time) VALUES(0,'系统预设商户组',2,1,10,null);
update t_merchant_group set id=0 where group_name ='系统预设商户组';