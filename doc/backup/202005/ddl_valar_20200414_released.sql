alter table tybss_report.r_match_bet_info  add tournament_name varchar2(200) null COMMENT '联赛名称';
ALTER TABLE tybss_report.r_merchant_order_day  ADD active_user BIGINT(200) DEFAULT 0 COMMENT '活跃用户数';

alter table tybss_report.r_match_bet_info MODIFY profit_rate decimal(32,4)	 null;

alter table tybss_report.r_market_bet_info MODIFY profit_rate decimal(32,4)	 null;





ALTER TABLE tybss_new.t_merchant  ADD computing_standard VARCHAR(128) DEFAULT null COMMENT '分成方式(1,投注;2盈利)';
ALTER TABLE tybss_new.t_merchant  ADD technique_amount BIGINT(32) DEFAULT null COMMENT '技术费用';
ALTER TABLE tybss_new.t_merchant  ADD payment_cycle int(1) DEFAULT null COMMENT '缴纳周期(1:月,2:季,3年)';
ALTER TABLE tybss_new.t_merchant  ADD vip_amount BIGINT(32) DEFAULT null COMMENT 'VIP费用';
ALTER TABLE tybss_new.t_merchant  ADD vip_payment_cycle tinyint(2) DEFAULT null COMMENT '缴纳周期(1:月,2:季,3年)';
ALTER TABLE tybss_new.t_merchant  ADD terrace_rate double(10,2) DEFAULT null COMMENT '标准费率%';
ALTER TABLE tybss_new.t_merchant  ADD technique_payment_cycle tinyint(2) DEFAULT null COMMENT '缴纳周期(1:月,2:季,3年)';
ALTER TABLE tybss_new.t_merchant  ADD range_amount_begin BIGINT(32) DEFAULT null COMMENT '分成方式(1,投注;2盈利)';
ALTER TABLE tybss_new.t_merchant  ADD range_amount_end BIGINT(32) DEFAULT null COMMENT '分成方式(1,投注;2盈利)';