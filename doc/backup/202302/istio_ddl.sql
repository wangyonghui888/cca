--  #备注： C、Y、B、S、业务汇总库均执行
ALTER TABLE t_merchant_config ADD COLUMN backend_switch int(5) NOT NULL DEFAULT 1 COMMENT '商户后台开关1开0关';
-- 更新默认数据
update t_merchant_config inner join t_merchant on t_merchant.merchant_code = t_merchant_config.merchant_code
set t_merchant_config.backend_switch = t_merchant.status;
update t_merchant_config set backend_switch = 1 where backend_switch is null or backend_switch = '';

--  #备注： C、Y、B、S、业务汇总库均执行
ALTER TABLE t_merchant ADD COLUMN backend_switch int(5) NOT NULL DEFAULT 1 COMMENT '商户后台开关1开0关';
update t_merchant SET backend_switch = 0 WHERE `status` = 0;