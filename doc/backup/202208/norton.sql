###业务common库
alter table tybss_merchant_common.t_user modify is_test int(11) COMMENT '是否VIP用户,1否2是';

###业务Y库
alter table tybss_merchant_y.t_user modify is_test int(11) COMMENT '是否VIP用户,1否2是';

###业务S库
alter table tybss_merchant_s.t_user modify is_test int(11) COMMENT '是否VIP用户,1否2是';

###业务B库
alter table tybss_merchant_b.t_user modify is_test int(11) COMMENT '是否VIP用户,1否2是';

###业务汇总库：
alter table tybss_merchant_common.t_user modify is_test int(11) COMMENT '是否VIP用户,1否2是';