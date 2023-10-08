INSERT INTO `merchant`.`admin_menu` (`id`,`name`, `name_en`, `pid`, `sort`, `path`, `icon`, `create_time`) VALUES (29,'取消注单', 'cancel order', '13', '26', 'cancelOrder', '', '2021-08-08 10:44:47');

INSERT INTO `merchant`.`admin_permission` (`id`,`alias`, `create_time`, `name`, `pid`, `menu_id`) VALUES (25, '取消注单', '2021-08-08 10:48:39', 'cancel_order', '14', '29');

INSERT INTO `merchant`.`admin_roles_menus`(`role_id`, `menu_id`)  SELECT DISTINCT(role_id),29 FROM `merchant`.`admin_user` WHERE `merchant_code` IN (
	SELECT merchant_code FROM `tybss_new`.`t_merchant_config` WHERE `merchant_tag` = 1
);

INSERT INTO `merchant`.`admin_roles_permissions`(`role_id`, `permission_id`)  SELECT DISTINCT(role_id),25 FROM `merchant`.`admin_user` WHERE `merchant_code` IN (
	SELECT merchant_code FROM `tybss_new`.`t_merchant_config` WHERE `merchant_tag` = 1
);


INSERT INTO `merchant`.`merchant_notice_type` (`id`, `type_name`, `type_en`) VALUES ('16', '手球赛事', 'Handbal Match'), ('17', '拳击赛事', 'Boxing Match'),('18', '沙滩排球赛事', 'Beach Volleyball'), ('19', '水球赛事', 'Water Polo'), ('20', '曲棍球赛事', 'Hockey'), ('21', '联合式橄榄球赛事', 'Rugby Union'), ('22', '其他赛事', 'Other Match');



INSERT INTO `merchant`.`admin_menu` (`id`, `name`, `name_en`, `pid`, `sort`, `path`, `icon`, `create_time`) VALUES ('30', '特殊限额', 'Special Limit', '2', '26', 'special_limit', '', '2021-08-24 15:31:35');

INSERT INTO `merchant`.`admin_permission` (`id`, `alias`, `create_time`, `name`, `pid`, `menu_id`) VALUES ('26', '特殊限额', '2021-08-21 11:44:16', 'special_limit', '2', '30');


INSERT INTO `merchant`.`admin_roles_menus`(`role_id`, `menu_id`)  SELECT id,30 FROM merchant.admin_role;

INSERT INTO `merchant`.`admin_roles_permissions`(`role_id`, `permission_id`)  SELECT id,26 FROM merchant.admin_role;