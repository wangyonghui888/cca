##-- 需求2418
INSERT INTO `merchant`.`merchant_notice_type`(`id`, `type_name`, `type_en`)  values (37,'异常赛事用户','Abnormal event user')

ALTER TABLE merchant_notice ADD abnormal_user_ids longtext  COMMENT '异常用户ids';


## -- bug 42884
ALTER TABLE t_domain_program ADD merchant_group_id varchar(30)  COMMENT '商户组id';
