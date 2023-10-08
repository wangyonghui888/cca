INSERT INTO `panda_auth`.`sys_resource`( `resource_code`, `resource_name`, `app_id`, `resouce_url`, `parent_id`, `enable`, `create_time`, `logic_delete`) 
VALUES ( '', '交易记录查询', 10008, '', 1033, 1, '2020-07-01 09:25:35', 0);
 -- 初始化权限
INSERT INTO `panda_auth`.`sys_permission`( `permission_code`, `permission_name`, `permission_type`, `app_id`, `resource_id`, `enable`, `exclusion_id`, `create_time`, `logic_delete`, `is_allocated`, `api`)
VALUES ('', '交易记录查询', 0, 10008, (SELECT MAX(id) FROM sys_resource), 1, '', '2020-07-01 16:46:36', 0, 0, '');




INSERT INTO `panda_auth`.`sys_resource`(`resource_code`, `resource_name`, `app_id`, `resouce_url`, `parent_id`, `enable`, `create_time`, `logic_delete`)
VALUES ('', '操作日志查询', 10008, '', 1044, 1, '2020-07-01 09:30:14', 0);

-- 初始化权限
INSERT INTO `panda_auth`.`sys_permission`( `permission_code`, `permission_name`, `permission_type`, `app_id`, `resource_id`, `enable`, `exclusion_id`, `create_time`, `logic_delete`, `is_allocated`, `api`)
VALUES ('', '操作日志查询', 0, 10008, (SELECT MAX(id) FROM sys_resource), 1, '', '2020-07-01 16:46:36', 0, 0, '');