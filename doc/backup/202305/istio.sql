ALTER TABLE t_system_config ADD COLUMN remark VARCHAR(4000) NULL DEFAULT NULL COMMENT '备注';

-- test
INSERT INTO `t_system_config`( `id`,`config_key`, `config_value`, `update_time`, `create_by`, `create_time`, `update_by`, `switch_type`, `remark`)
VALUES (10,'front_merchant_group_系统预设商户组','1','1681975896000','admin','1681975898000','admin','1','{"pc":"http://test-user-pc-bw4.sportxxxifbdxm2.com","h5":"http://test-user-h5-bw3.sportxxxifbdxm2.com"}');

-- uat
INSERT INTO `t_system_config`( `id`,`config_key`, `config_value`, `update_time`, `create_by`, `create_time`, `update_by`, `remark`)
VALUES (10,'front_merchant_group_系统预设商户组','1','1681975896000','admin','1681975898000','admin','{"pc":"http://user-pc.sportxxx14bl5.com/","h5":"http://user-h5.sportxxx14bl5.com"}');

-- prod
INSERT INTO `t_system_config`( `id`,`config_key`, `config_value`, `update_time`, `create_by`, `create_time`, `update_by`, `remark`)
VALUES (10,'front_merchant_group_系统预设商户组','1','1681975896000','admin','1681975898000','admin','{"pc":"https://user-pc-bw4.bricblogy.com/","h5":"https://user-h5-bw3.bricblogy.com/"}');


-- OSMC库
INSERT INTO panda_auth.`sys_url` (`url`, `type`, `create_time`) VALUES ('/manage/merchant/getMerchantByName', '1', CURRENT_TIMESTAMP);
INSERT INTO panda_auth.`sys_url` (`url`, `type`, `create_time`) VALUES ('/manage/merchant/queryMerchantListByParams', '1', CURRENT_TIMESTAMP);
INSERT INTO panda_auth.`sys_url` (`url`, `type`, `create_time`) VALUES ('/manage/merchant/queryMerchantSimpleListByParams', '1', CURRENT_TIMESTAMP);
