####admin_user id bigint改成 string 32位
alter table merchant.admin_user modify column id varchar(32);

####merchant_log id user_id改成 string 32位
alter table merchant.merchant_log modify column user_id varchar(32);

####admin_user 新增psw_code字段
alter table merchant.admin_user add column psw_code varchar(255) DEFAULT NULL  comment '密码明码';

