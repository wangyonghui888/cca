CREATE DATABASE IF NOT EXISTS `bc_order`;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin_menu
-- ----------------------------
DROP TABLE IF EXISTS `bc_order`.`admin_menu`;
CREATE TABLE `bc_order`.`admin_menu`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单名称',
  `en` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '英文',
  `pid` bigint(0) NOT NULL COMMENT '上级菜单ID',
  `sort` bigint(0) NOT NULL COMMENT '排序',
  `path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '链接地址',
  `icon` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '标签',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '菜单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for admin_permission
-- ----------------------------
DROP TABLE IF EXISTS `bc_order`.`admin_permission`;
CREATE TABLE `bc_order`.`admin_permission`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `alias` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '别名',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '名称',
  `pid` int(0) NOT NULL COMMENT '上级权限',
  `menu_id` bigint(0) NULL DEFAULT 0 COMMENT '菜单ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商户后台权限' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for admin_role
-- ----------------------------
DROP TABLE IF EXISTS `bc_order`.`admin_role`;
CREATE TABLE `bc_order`.`admin_role`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '名称',
  `en` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '英文',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '备注',
  `create_username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建人',
  `authorized_time` datetime(0) NULL DEFAULT NULL COMMENT '授权时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商户角色' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for admin_roles_menus
-- ----------------------------
DROP TABLE IF EXISTS `bc_order`.`admin_roles_menus`;
CREATE TABLE `bc_order`.`admin_roles_menus`  (
  `role_id` bigint(0) NOT NULL COMMENT '角色ID',
  `menu_id` bigint(0) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`menu_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户所属菜单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for admin_roles_permissions
-- ----------------------------
DROP TABLE IF EXISTS `bc_order`.`admin_roles_permissions`;
CREATE TABLE `bc_order`.`admin_roles_permissions`  (
  `role_id` bigint(0) NOT NULL COMMENT '角色ID',
  `permission_id` bigint(0) NOT NULL COMMENT '权限ID',
  PRIMARY KEY (`role_id`, `permission_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商户后台用户权限分配' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for admin_user
-- ----------------------------
DROP TABLE IF EXISTS `bc_order`.`admin_user`;
CREATE TABLE `bc_order`.`admin_user`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '密码',
  `avatar` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '头像地址',
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '邮箱',
  `enabled` smallint(0) NULL DEFAULT 0 COMMENT '状态：1启用、0禁用',
  `phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '电话号码',
  `role_id` bigint(0) NULL DEFAULT NULL COMMENT '用户角色',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `last_password_reset_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改密码的日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'BC后台账号' ROW_FORMAT = Dynamic;


INSERT INTO `bc_order`.`admin_menu` (`id`, `name`, `en`, `pid`, `sort`, `path`, `icon`, `create_time`) VALUES
(1, '数据中心', 'Data Center', 0, 1, 'data_center', 'p-icon-shuju', '2020-09-08 11:35:58'),
(2, '赛事报表', 'Match Report', 1, 2, 'match_bonus', '', '2020-09-08 11:40:33'),
(3, '注单查询', 'Bet Query', 1, 3, 'bet_slip', '', '2020-09-08 11:41:26'),
(4, '授权中心', 'Authorization Center', 0, 4, 'auth', 'p-icon-shouquan', '2020-09-11 11:02:07'),
(5, '用户管理', 'User Management', 4, 5, 'user', '', '2020-09-11 11:03:26'),
(6, '角色管理', 'Role Management', 4, 6, 'role', '', '2020-09-11 11:03:26');


INSERT INTO `bc_order`.`admin_role` (`id`, `create_time`, `name`, `en`, `remark`, `create_username`, `authorized_time`) VALUES
(1, '2020-09-08 11:44:27', '超级管理员', 'Administrator', '超级管理员', '', '2020-09-08 11:44:27'),
(2, '2020-09-11 16:56:21', '测试人员', 'Tester', '测试人员', '', '2020-09-11 16:56:21');


INSERT INTO `bc_order`.`admin_roles_menus` (`role_id`, `menu_id`) VALUES
(1, 1),
(2, 1),
(1, 2),
(2, 2),
(1, 3),
(2, 3),
(1, 4),
(1, 5);

INSERT INTO `bc_order`.`admin_user` (`id`, `username`, `password`, `avatar`, `email`, `enabled`, `phone`, `role_id`, `create_time`, `last_password_reset_time`) VALUES
(18, 'admin', 'e10adc3949ba59abbe56e057f20f883e', '', '456@qq.com', 1, '16545132132', 1, '2020-09-01 19:28:47', '2020-09-01 19:28:47');


INSERT INTO `bc_order`.`admin_permission` (`id`, `alias`, `create_time`, `name`, `pid`, `menu_id`) VALUES
(1, '授权中心', '2020-09-23 20:31:45', 'auth', 0, 4),
(2, '用户管理', '2020-09-23 20:33:02', 'user', 1, 5),
(3, '角色管理', '2020-09-23 20:36:31', 'role', 1, 6);

INSERT INTO `bc_order`.`admin_roles_permissions` (`role_id`, `permission_id`) VALUES
(1, 1),
(1, 2),
(1, 3);