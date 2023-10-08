INSERT INTO `merchant`.`admin_menu`(`id`, `name`, `pid`, `sort`, `path`, `icon`, `create_time`) VALUES (26, '渠道商户管理', 2, 23, 'channel_merchant', '', '2020-12-12 15:28:18');

INSERT INTO `merchant`.`admin_menu`(`id`, `name`, `pid`, `sort`, `path`, `icon`, `create_time`) VALUES (27, '任务中心', 0, 24, 'my_task', 'p-icon-renwu', '2020-12-12 15:31:41');

INSERT INTO `merchant`.`admin_menu`(`id`, `name`, `pid`, `sort`, `path`, `icon`, `create_time`) VALUES (28, '我的导出任务', 27, 25, 'download_list', '', '2020-12-12 15:34:07');

INSERT INTO `merchant`.`admin_permission`(`id`, `alias`, `create_time`, `name`, `pid`, `menu_id`) VALUES (24, '渠道商户管理', '2020-12-12 15:41:56', 'channel_merchant', 2, 26);