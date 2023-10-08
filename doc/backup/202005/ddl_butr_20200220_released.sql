alter table tybss_new.t_merchant add column file_name VARCHAR(2000);
alter table tybss_new.t_merchant add column commerce  VARCHAR(100);

alter table tybss_new.t_merchant_level  add column `computing_standard` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs  COMMENT '计算模式(1,投注;2盈利)';
alter table tybss_new.t_merchant_level  add column`range_amount_begin` bigint(32) DEFAULT '0' COMMENT '金额范围起始';
alter table tybss_new.t_merchant_level  add column`range_amount_end` bigint(32) DEFAULT '0' COMMENT '金额范围结束';
alter table tybss_new.t_merchant_level  add column`terrace_rate` double(10,2) DEFAULT '0.00' COMMENT '平台费率单位%';
alter table tybss_new.t_merchant_level  add column`payment_cycle` int(1) DEFAULT '0' COMMENT '缴纳周期(1:月,2:季,3半年，4年)';