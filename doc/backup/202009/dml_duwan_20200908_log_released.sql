DELETE FROM `merchant`.`admin_menu` WHERE id in(24,25);
INSERT INTO `merchant`.`admin_menu`(`id`, `name`, `pid`, `sort`, `path`, `icon`, `create_time`) VALUES (24, '操作日志查询', 2, 8, 'log_search', '', '2020-09-08 14:58:19');
INSERT INTO `merchant`.`admin_menu`(`id`, `name`, `pid`, `sort`, `path`, `icon`, `create_time`) VALUES (25, '交易记录查询', 13, 22, 'record_query', '', '2020-09-08 14:58:19');

INSERT INTO `merchant`.`admin_roles_menus`(`role_id`, `menu_id`)  SELECT id,24 FROM merchant.admin_role;
INSERT INTO `merchant`.`admin_roles_menus`(`role_id`, `menu_id`)  SELECT id,25 FROM merchant.admin_role;
COMMIT;
