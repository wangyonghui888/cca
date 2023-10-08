
-- test
INSERT INTO `t_system_config`( `id`,`config_key`, `config_value`, `update_time`, `create_by`, `create_time`, `update_by`, `switch_type`, `remark`)
VALUES (20,'video_merchant_group_系统预设商户组','1','1681975896000','admin','1681975898000','admin','1','{"videoAll":"http://test-user-pc-bw4.sportxxxifbdxm2.com","videoExcitingEditing":"http://test-user-h5-bw3.sportxxxifbdxm2.com"}');

-- uat
INSERT INTO `t_system_config`( `id`,`config_key`, `config_value`, `update_time`, `create_by`, `create_time`, `update_by`, `remark`)
VALUES (20,'video_merchant_group_系统预设商户组','1','1681975896000','admin','1681975898000','admin','{"videoAll":"http://user-pc.sportxxx14bl5.com/","videoExcitingEditing":"http://user-h5.sportxxx14bl5.com"}');

-- prod
INSERT INTO `t_system_config`( `id`,`config_key`, `config_value`, `update_time`, `create_by`, `create_time`, `update_by`, `remark`)
VALUES (20,'video_merchant_group_系统预设商户组','1','1681975896000','admin','1681975898000','admin','{"videoAll":"https://avi.juy8xwxi.com/","videoExcitingEditing":"https://playback.clean-yks.com/"}');


INSERT INTO `admin_menu`(`id`, `name`, `name_en`, `pid`, `sort`, `path`, `icon`, `create_time`) VALUES (42 ,'注单查询(ES)','Tickets Center(ES)','13','28','bet_slip_es','',CURRENT_TIMESTAMP);

INSERT INTO `merchant`.`admin_roles_menus`(`role_id`, `menu_id`)  SELECT id,42 FROM merchant.admin_role;
