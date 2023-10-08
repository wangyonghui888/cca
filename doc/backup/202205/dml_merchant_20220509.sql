delete from m_maintenance_platform where server_name in ('上下分服务','开奖网后台','运营管理后台') and data_code ='cp';

update  m_maintenance_platform set serve_code = '1' where server_name = '客户端' and data_code ='cp';
update  m_maintenance_platform set serve_code = '2' where server_name = '商户后台' and data_code ='cp';
update  m_maintenance_platform set serve_code = '3' where server_name = '总控后台' and data_code ='cp';
update  m_maintenance_platform set kick_user_type = '234' where server_name = '客户端' and data_code ='cp';


delete from m_permissions where 1 = 1;

INSERT INTO `m_permissions` (`id`, `name`, `pid`, `sort`, `url`) VALUES
(1, '首页', NULL, 0, 'home'),
(9, '系统管理', NULL, 6, 'system_namage'),
(10, '菜单管理', 9, 7, 'menu_manage'),
(11, '用户管理', 9, 8, 'user_manage'),
(12, '角色管理', 9, 8, 'role_manage'),
(13, '域名设置', NULL, 1, 'domain_setting'),
(14, '线路商管理', 13, 2, 'lineman_management'),
(15, '区域管理', 13, 2, 'regional_management'),
(16, '域名管理', 13, 2, 'domain_management'),
(17, '域名组管理', 13, 4, 'domain_group_management'),
(18, '域名切换方案管理', 13, 5, 'domain_switching_scheme_management'),
(19, '商户组设置', 13, 6, 'merchant_group_setting'),
(20, '域名切换日志', 13, 7, 'domain_switch_log'),
(21, '异常IP池管理5', 13, 8, 'abnormal_ip_domain_pool'),
(22, '商户前端域名管理', 13, 9, 'domain_manager'),
(23, '商户APP域名管理', 13, 10, 'app_manager'),
(24, '全局接口配置', 13, 11, 'global_interface'),
(25, '域名池管理', 13, 12, 'domain_pool_manage'),
(28, '维护设置', NULL, 1, 'maintain'),
(29, '维护日志查询', 28, 1, 'maintain_log'),
(30, '踢用户', 28, 2, 'delete_user'),
(31, '踢用户日志查询', 28, 3, 'delete_user_log');

INSERT INTO `m_role` (`id`, `role_name`, `creat_time`) VALUES
(8, '超级管理员', '2022-04-26 05:02:39');

INSERT INTO `m_user_roles` (`id`, `user_id`, `role_id`) VALUES
(42, 1, 8);
