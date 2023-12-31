INSERT INTO `m_data_permissions`(`id`,`name`,`data_code`) VALUES (9, '体育运维数据', 'ty');
INSERT INTO `m_data_permissions`(`id`,`name`,`data_code`) VALUES (10, '彩票运维数据', 'cp');
INSERT INTO `m_data_permissions`(`id`,`name`,`data_code`) VALUES (11, '电竞运维数据', 'dj');
INSERT INTO `m_permissions`(`id`,`name`,`pid`,`sort`,`url`) VALUES (1, '首页', NULL, 0, 'home');
INSERT INTO `m_permissions`(`id`,`name`,`pid`,`sort`,`url`) VALUES (3, '维护日志查询', NULL, 2, 'maintain_log');
INSERT INTO `m_permissions`(`id`,`name`,`pid`,`sort`,`url`) VALUES (4, '踢用户', NULL, 3, 'delete_user');
INSERT INTO `m_permissions`(`id`,`name`,`pid`,`sort`,`url`) VALUES (5, '踢用户日志查询', NULL, 4, 'delete_user_log');
INSERT INTO `m_role`(`id`,`role_name`,`creat_time`) VALUES (4, '电竞运维', '2022-03-12 13:21:58');
INSERT INTO `m_role`(`id`,`role_name`,`creat_time`) VALUES (5, '彩票运维', '2022-03-12 13:22:25');
INSERT INTO `m_role`(`id`,`role_name`,`creat_time`) VALUES (6, '体育运维', '2022-03-12 13:22:39');
INSERT INTO `m_role`(`id`,`role_name`,`creat_time`) VALUES (7, '总监', '2022-03-12 13:22:54');
INSERT INTO `merchant`.`m_user`(`id`, `name`, `password`, `creat_time`) VALUES (1, 'admin', '123456', '2022-03-11 13:37:48');
INSERT INTO `merchant`.`m_user`(`id`, `name`, `password`, `creat_time`) VALUES (3, 'test', '123456', '2022-03-11 13:46:08');
INSERT INTO `merchant`.`m_user`(`id`, `name`, `password`, `creat_time`) VALUES (4, 'djtest', '123456', '2022-03-27 15:37:03');
INSERT INTO `merchant`.`m_user`(`id`, `name`, `password`, `creat_time`) VALUES (5, 'QQ', '123456', '2022-04-10 13:18:48');
INSERT INTO `merchant`.`m_user`(`id`, `name`, `password`, `creat_time`) VALUES (6, 'Kaven', '123456', '2022-04-10 13:19:10');
INSERT INTO `merchant`.`m_user`(`id`, `name`, `password`, `creat_time`) VALUES (7, 'Aison', '123456', '2022-04-10 13:19:27');
INSERT INTO `merchant`.`m_user`(`id`, `name`, `password`, `creat_time`) VALUES (8, 'Christion', '123456', '2022-04-10 13:19:47');
INSERT INTO `merchant`.`m_user`(`id`, `name`, `password`, `creat_time`) VALUES (9, 'Eric', '123456', '2022-04-10 13:20:06');
INSERT INTO `merchant`.`m_user`(`id`, `name`, `password`, `creat_time`) VALUES (10, 'Aden', '123456', '2022-04-10 13:20:23');
INSERT INTO `merchant`.`m_user`(`id`, `name`, `password`, `creat_time`) VALUES (11, 'Fashion', '123456', '2022-04-10 13:20:57');
INSERT INTO `merchant`.`m_user`(`id`, `name`, `password`, `creat_time`) VALUES (12, 'Lebron', '123456', '2022-04-10 13:21:16');
INSERT INTO `merchant`.`m_user`(`id`, `name`, `password`, `creat_time`) VALUES (13, 'Jaydon', '123456', '2022-04-10 13:21:33');
INSERT INTO `merchant`.`m_user`(`id`, `name`, `password`, `creat_time`) VALUES (14, 'Bernie', '123456', '2022-04-10 13:21:55');
INSERT INTO `merchant`.`m_user`(`id`, `name`, `password`, `creat_time`) VALUES (15, 'Cliff', '123456', '2022-04-10 13:22:12');
INSERT INTO `merchant`.`m_user`(`id`, `name`, `password`, `creat_time`) VALUES (16, 'OT', '123456', '2022-04-10 13:22:30');
INSERT INTO `merchant`.`m_user`(`id`, `name`, `password`, `creat_time`) VALUES (17, 'Dema', '123456', '2022-04-10 13:22:48');
INSERT INTO `merchant`.`m_user`(`id`, `name`, `password`, `creat_time`) VALUES (18, 'Danny', '123456', '2022-04-10 13:23:07');
INSERT INTO `merchant`.`m_user`(`id`, `name`, `password`, `creat_time`) VALUES (19, 'Mongo', '123456', '2022-04-10 14:27:55');
INSERT INTO `merchant`.`m_user`(`id`, `name`, `password`, `creat_time`) VALUES (20, 'Abner', '123456', '2022-04-10 14:28:14');
INSERT INTO `merchant`.`m_user`(`id`, `name`, `password`, `creat_time`) VALUES (21, 'Ausan', '123456', '2022-04-10 14:28:29');
INSERT INTO `merchant`.`m_user`(`id`, `name`, `password`, `creat_time`) VALUES (22, 'Cabe', '123456', '2022-04-10 14:28:49');
INSERT INTO `merchant`.`m_user`(`id`, `name`, `password`, `creat_time`) VALUES (23, 'Goofy', '123456', '2022-04-10 14:29:03');
INSERT INTO `merchant`.`m_user`(`id`, `name`, `password`, `creat_time`) VALUES (24, 'Banna', '123456', '2022-04-10 14:29:16');
INSERT INTO `merchant`.`m_user`(`id`, `name`, `password`, `creat_time`) VALUES (25, 'Zizi', '123456', '2022-04-10 14:29:28');
INSERT INTO `merchant`.`m_user`(`id`, `name`, `password`, `creat_time`) VALUES (26, 'Ford', '123456', '2022-04-10 14:30:24');
INSERT INTO `merchant`.`m_user`(`id`, `name`, `password`, `creat_time`) VALUES (27, 'YC', '123456', '2022-04-10 14:30:36');
INSERT INTO `merchant`.`m_user`(`id`, `name`, `password`, `creat_time`) VALUES (28, 'AK', '123456', '2022-04-10 14:30:51');
INSERT INTO `merchant`.`m_user`(`id`, `name`, `password`, `creat_time`) VALUES (29, 'Easy', '123456', '2022-04-10 14:31:03');

INSERT INTO `merchant`.`m_user_roles`(`id`, `user_id`, `role_id`) VALUES (2, 1, 7);
INSERT INTO `merchant`.`m_user_roles`(`id`, `user_id`, `role_id`) VALUES (3, 3, 6);
INSERT INTO `merchant`.`m_user_roles`(`id`, `user_id`, `role_id`) VALUES (5, 3, 7);
INSERT INTO `merchant`.`m_user_roles`(`id`, `user_id`, `role_id`) VALUES (4, 4, 4);
INSERT INTO `merchant`.`m_user_roles`(`id`, `user_id`, `role_id`) VALUES (6, 5, 7);
INSERT INTO `merchant`.`m_user_roles`(`id`, `user_id`, `role_id`) VALUES (7, 6, 7);
INSERT INTO `merchant`.`m_user_roles`(`id`, `user_id`, `role_id`) VALUES (8, 7, 7);
INSERT INTO `merchant`.`m_user_roles`(`id`, `user_id`, `role_id`) VALUES (9, 8, 7);
INSERT INTO `merchant`.`m_user_roles`(`id`, `user_id`, `role_id`) VALUES (10, 9, 7);
INSERT INTO `merchant`.`m_user_roles`(`id`, `user_id`, `role_id`) VALUES (11, 10, 7);
INSERT INTO `merchant`.`m_user_roles`(`id`, `user_id`, `role_id`) VALUES (12, 11, 7);
INSERT INTO `merchant`.`m_user_roles`(`id`, `user_id`, `role_id`) VALUES (13, 12, 7);
INSERT INTO `merchant`.`m_user_roles`(`id`, `user_id`, `role_id`) VALUES (14, 13, 7);
INSERT INTO `merchant`.`m_user_roles`(`id`, `user_id`, `role_id`) VALUES (15, 14, 7);
INSERT INTO `merchant`.`m_user_roles`(`id`, `user_id`, `role_id`) VALUES (16, 15, 7);
INSERT INTO `merchant`.`m_user_roles`(`id`, `user_id`, `role_id`) VALUES (17, 16, 7);
INSERT INTO `merchant`.`m_user_roles`(`id`, `user_id`, `role_id`) VALUES (18, 17, 7);
INSERT INTO `merchant`.`m_user_roles`(`id`, `user_id`, `role_id`) VALUES (19, 18, 7);
INSERT INTO `merchant`.`m_user_roles`(`id`, `user_id`, `role_id`) VALUES (20, 19, 6);
INSERT INTO `merchant`.`m_user_roles`(`id`, `user_id`, `role_id`) VALUES (21, 20, 6);
INSERT INTO `merchant`.`m_user_roles`(`id`, `user_id`, `role_id`) VALUES (22, 21, 6);
INSERT INTO `merchant`.`m_user_roles`(`id`, `user_id`, `role_id`) VALUES (23, 22, 6);
INSERT INTO `merchant`.`m_user_roles`(`id`, `user_id`, `role_id`) VALUES (24, 23, 6);
INSERT INTO `merchant`.`m_user_roles`(`id`, `user_id`, `role_id`) VALUES (25, 24, 6);
INSERT INTO `merchant`.`m_user_roles`(`id`, `user_id`, `role_id`) VALUES (28, 25, 4);
INSERT INTO `merchant`.`m_user_roles`(`id`, `user_id`, `role_id`) VALUES (35, 25, 5);
INSERT INTO `merchant`.`m_user_roles`(`id`, `user_id`, `role_id`) VALUES (29, 26, 4);
INSERT INTO `merchant`.`m_user_roles`(`id`, `user_id`, `role_id`) VALUES (36, 26, 5);
INSERT INTO `merchant`.`m_user_roles`(`id`, `user_id`, `role_id`) VALUES (30, 27, 4);
INSERT INTO `merchant`.`m_user_roles`(`id`, `user_id`, `role_id`) VALUES (37, 27, 5);
INSERT INTO `merchant`.`m_user_roles`(`id`, `user_id`, `role_id`) VALUES (31, 28, 4);
INSERT INTO `merchant`.`m_user_roles`(`id`, `user_id`, `role_id`) VALUES (38, 28, 5);
INSERT INTO `merchant`.`m_user_roles`(`id`, `user_id`, `role_id`) VALUES (32, 29, 4);
INSERT INTO `merchant`.`m_user_roles`(`id`, `user_id`, `role_id`) VALUES (39, 29, 5);

INSERT INTO `m_roles_data_permissions`(`id`,`role_id`,`data_permission_id`) VALUES (27, 4, 11);
INSERT INTO `m_roles_data_permissions`(`id`,`role_id`,`data_permission_id`) VALUES (26, 6, 9);
INSERT INTO `m_roles_data_permissions`(`id`,`role_id`,`data_permission_id`) VALUES (23, 7, 9);
INSERT INTO `m_roles_data_permissions`(`id`,`role_id`,`data_permission_id`) VALUES (24, 7, 10);
INSERT INTO `m_roles_data_permissions`(`id`,`role_id`,`data_permission_id`) VALUES (25, 7, 11);
INSERT INTO `m_roles_permissions`(`id`,`role_id`,`permission_id`) VALUES (26, 4, 1);
INSERT INTO `m_roles_permissions`(`id`,`role_id`,`permission_id`) VALUES (27, 4, 3);
INSERT INTO `m_roles_permissions`(`id`,`role_id`,`permission_id`) VALUES (28, 4, 4);
INSERT INTO `m_roles_permissions`(`id`,`role_id`,`permission_id`) VALUES (29, 4, 5);
INSERT INTO `m_roles_permissions`(`id`,`role_id`,`permission_id`) VALUES (23, 6, 1);
INSERT INTO `m_roles_permissions`(`id`,`role_id`,`permission_id`) VALUES (24, 6, 3);
INSERT INTO `m_roles_permissions`(`id`,`role_id`,`permission_id`) VALUES (25, 6, 4);
INSERT INTO `m_roles_permissions`(`id`,`role_id`,`permission_id`) VALUES (8, 7, 1);
INSERT INTO `m_roles_permissions`(`id`,`role_id`,`permission_id`) VALUES (10, 7, 3);
INSERT INTO `m_roles_permissions`(`id`,`role_id`,`permission_id`) VALUES (11, 7, 4);
INSERT INTO `m_roles_permissions`(`id`,`role_id`,`permission_id`) VALUES (12, 7, 5);
INSERT INTO `m_maintenance_platform`(`id`,`server_name`,`serve_code`,`data_code`,`base_url`,`kick_user_type`,`kick_user_url`,`create_time`,`update_time`) VALUES (1, '客户端', 'ty_server', 'ty', 'http://172.18.178.243:10710', '23', '/api/user/kickOutUserMerchant', 1647657238000, 1647657238000);
INSERT INTO `m_maintenance_platform`(`id`,`server_name`,`serve_code`,`data_code`,`base_url`,`kick_user_type`,`kick_user_url`,`create_time`,`update_time`) VALUES (2, '客户端', NULL, 'cp', 'www.cp.com', '', '/kickUser', 11647657238000, 1647657238000);
INSERT INTO `m_maintenance_platform`(`id`,`server_name`,`serve_code`,`data_code`,`base_url`,`kick_user_type`,`kick_user_url`,`create_time`,`update_time`) VALUES (3, '客户端', '1', 'dj', 'http://www.phiqui.com', '234', '/v1/member/offLime', 1647657238000, 1647657238000);
INSERT INTO `m_maintenance_platform`(`id`,`server_name`,`serve_code`,`data_code`,`base_url`,`kick_user_type`,`kick_user_url`,`create_time`,`update_time`) VALUES (4, '赛程管理平台', '10006', 'ty', 'https://permission-manage.sportxxxwo8.com', '24', '/v1/admin/appliction/kickOut', 1647657238000, 1647657238000);
INSERT INTO `m_maintenance_platform`(`id`,`server_name`,`serve_code`,`data_code`,`base_url`,`kick_user_type`,`kick_user_url`,`create_time`,`update_time`) VALUES (5, '操盘风控平台', '10020', 'ty', 'https://permission-manage.sportxxxwo8.com', '24', '/v1/admin/appliction/kickOut', 1647657238000, 1647657238000);
INSERT INTO `m_maintenance_platform`(`id`,`server_name`,`serve_code`,`data_code`,`base_url`,`kick_user_type`,`kick_user_url`,`create_time`,`update_time`) VALUES (6, 'TOB后台', '10008', 'ty', 'https://permission-manage.sportxxxwo8.com', '24', '/v1/admin/appliction/kickOut', 1647657238000, 1647657238000);
INSERT INTO `m_maintenance_platform`(`id`,`server_name`,`serve_code`,`data_code`,`base_url`,`kick_user_type`,`kick_user_url`,`create_time`,`update_time`) VALUES (7, '商户后台', NULL, 'ty', NULL, '', '/kickUser', 1647657238000, 1647657238000);
INSERT INTO `m_maintenance_platform`(`id`,`server_name`,`serve_code`,`data_code`,`base_url`,`kick_user_type`,`kick_user_url`,`create_time`,`update_time`) VALUES (8, '权限管理平台', NULL, 'ty', 'https://permission-manage.sportxxxwo8.com', '', '', 1647657238000, 1647657238000);
INSERT INTO `m_maintenance_platform`(`id`,`server_name`,`serve_code`,`data_code`,`base_url`,`kick_user_type`,`kick_user_url`,`create_time`,`update_time`) VALUES (9, '体育报表系统', NULL, 'ty', 'https://permission-manage.sportxxxwo8.com', '', '', 1647657238000, 1647657238000);
INSERT INTO `m_maintenance_platform`(`id`,`server_name`,`serve_code`,`data_code`,`base_url`,`kick_user_type`,`kick_user_url`,`create_time`,`update_time`) VALUES (10, '上下分服务', NULL, 'ty', NULL, NULL, '/kickUser', 1647657238000, 1647657238000);
INSERT INTO `m_maintenance_platform`(`id`,`server_name`,`serve_code`,`data_code`,`base_url`,`kick_user_type`,`kick_user_url`,`create_time`,`update_time`) VALUES (11, '总控后台', '3', 'dj', 'http://www.phiqui.com', '24', '/v1/member/offLime', 1647657238000, 1647657238000);
INSERT INTO `m_maintenance_platform`(`id`,`server_name`,`serve_code`,`data_code`,`base_url`,`kick_user_type`,`kick_user_url`,`create_time`,`update_time`) VALUES (12, '商户后台', '2', 'dj', 'http://www.phiqui.com', '234', '/v1/member/offLime', 1647657238000, 1647657238000);
INSERT INTO `m_maintenance_platform`(`id`,`server_name`,`serve_code`,`data_code`,`base_url`,`kick_user_type`,`kick_user_url`,`create_time`,`update_time`) VALUES (14, '总控后台', NULL, 'cp', NULL, '', '/kickUser', 1647657238000, 1647657238000);
INSERT INTO `m_maintenance_platform`(`id`,`server_name`,`serve_code`,`data_code`,`base_url`,`kick_user_type`,`kick_user_url`,`create_time`,`update_time`) VALUES (15, '商户后台', NULL, 'cp', NULL, '', '/kickUser', 1647657238000, 1647657238000);
INSERT INTO `m_maintenance_platform`(`id`,`server_name`,`serve_code`,`data_code`,`base_url`,`kick_user_type`,`kick_user_url`,`create_time`,`update_time`) VALUES (16, '上下分服务', NULL, 'cp', NULL, '', '/kickUser', 1647657238000, 1647657238000);
INSERT INTO `m_maintenance_platform`(`id`,`server_name`,`serve_code`,`data_code`,`base_url`,`kick_user_type`,`kick_user_url`,`create_time`,`update_time`) VALUES (17, '开奖网后台', NULL, 'cp', NULL, '', '/kickUser', 1647657238000, 1647657238000);
INSERT INTO `m_maintenance_platform`(`id`,`server_name`,`serve_code`,`data_code`,`base_url`,`kick_user_type`,`kick_user_url`,`create_time`,`update_time`) VALUES (18, '运营管理后台', NULL, 'cp', NULL, '', '/kickUser', 1647657238000, 1647657238000);

-- 业务商户 panda_auth 主库
INSERT INTO panda_auth.`sys_url` (`url`, `type`, `create_time`) VALUES ('/manage/notice/addESportsNotice', '1', CURRENT_TIMESTAMP);
INSERT INTO panda_auth.`sys_url` (`url`, `type`, `create_time`) VALUES ('/manage/notice/editESportsNotice', '1', CURRENT_TIMESTAMP);
INSERT INTO panda_auth.`sys_url` (`url`, `type`, `create_time`) VALUES ('/manage/notice/delESportsNotice', '1', CURRENT_TIMESTAMP);
