CREATE TABLE merchant.`admin_user`
(
    `id`                       bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `username`                 varchar(255)                                            DEFAULT '' COMMENT '用户名',
    `password`                 varchar(255)                                            DEFAULT '' COMMENT '密码',
    `avatar`                   varchar(255)                                            DEFAULT '' COMMENT '头像地址',
    `email`                    varchar(255)                                            DEFAULT '' COMMENT '邮箱',
    `enabled`                  smallint                                                DEFAULT '0' COMMENT '状态：1启用、0禁用',
    `phone`                    varchar(255)                                            DEFAULT '' COMMENT '电话号码',
    `merchant_id`              bigint(20)                                              DEFAULT '0' COMMENT '所属商户id',
    `merchant_code`            varchar(255)                                            DEFAULT '' COMMENT '商户code',
    `merchant_name`            varchar(255)                                            DEFAULT '' COMMENT '商户名称',
    `parent_merchant_code`     varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '' COMMENT '父级商户code',
    `agent_level`              tinyint(3)                                              DEFAULT '0' COMMENT '代理级别，0:直营，1渠道 ，2二级',
    `role_id`                  bigint(20)                                              DEFAULT NULL COMMENT '用户角色',
    `create_time`              datetime                                                DEFAULT NULL COMMENT '创建日期',
    `last_password_reset_time` datetime                                                DEFAULT NULL COMMENT '最后修改密码的日期',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8 COMMENT ='商户后台账号';


CREATE TABLE merchant.`admin_role`
(
    `id`            bigint(20)                                              NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `create_time`   datetime                                                         DEFAULT NULL COMMENT '创建日期',
    `name`          varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '名称',
    `remark`        varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci          DEFAULT '' COMMENT '备注',
    `merchant_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci          DEFAULT '' COMMENT '商户code',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8 COMMENT ='商户角色';


CREATE TABLE merchant.`admin_permission`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `alias`       varchar(255) DEFAULT '' COMMENT '别名',
    `create_time` datetime     DEFAULT NULL COMMENT '创建日期',
    `name`        varchar(255) DEFAULT '' COMMENT '名称',
    `pid`         int(11)    NOT NULL COMMENT '上级权限',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 18
  DEFAULT CHARSET = utf8 COMMENT ='商户后台权限';


CREATE TABLE merchant.`admin_roles_permissions`
(
    `role_id`       bigint(20) NOT NULL COMMENT '角色ID',
    `permission_id` bigint(20) NOT NULL COMMENT '权限ID',
    PRIMARY KEY (`role_id`, `permission_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='商户后台用户权限分配';

CREATE TABLE merchant.`admin_menu`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `name`        varchar(255) DEFAULT NULL COMMENT '菜单名称',
    `pid`         bigint(20) NOT NULL COMMENT '上级菜单ID',
    `sort`        bigint(20) NOT NULL COMMENT '排序',
    `path`        varchar(255) DEFAULT NULL COMMENT '链接地址',
    `create_time` datetime     DEFAULT NULL COMMENT '创建日期',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT '菜单';

CREATE TABLE merchant.`admin_roles_menus`
(
    `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
    `role_id` bigint(20) NOT NULL COMMENT '角色ID',
    PRIMARY KEY (`menu_id`, `role_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT '用户所属菜单';

  ALTER TABLE `merchant`.`admin_user`
MODIFY COLUMN `avatar` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '头像地址' AFTER `password`;


ALTER TABLE `merchant`.`admin_role`
ADD COLUMN `create_username` varchar(255) NULL DEFAULT '' COMMENT '创建人' AFTER `merchant_code`,
ADD COLUMN `authorized_time` datetime NULL COMMENT '授权时间' AFTER `create_username`;


ALTER TABLE `merchant`.`admin_user`
MODIFY COLUMN `merchant_id` varchar(32) NULL DEFAULT '' COMMENT '所属商户id' AFTER `phone`;

ALTER TABLE `merchant`.`admin_menu`
ADD COLUMN `icon` varchar(255) NULL DEFAULT '' COMMENT '标签' AFTER `path`;

ALTER TABLE `merchant`.`admin_user`
ADD COLUMN `is_admin` smallint(3) NULL DEFAULT 0 COMMENT '超级管理员 0否 1是' AFTER `role_id`;


ALTER TABLE `merchant`.`merchant_news`
MODIFY COLUMN `is_read` smallint(3) NULL DEFAULT 0 COMMENT 'panda是否以读，0未读  1以读' AFTER `merchant_name`,
ADD COLUMN `self_is_read` smallint(3) NULL COMMENT '商户自己是否以读，0未读  1以读' AFTER `is_read`;

ALTER TABLE `merchant`.`merchant_news`
MODIFY COLUMN `self_is_read` smallint(3) NULL DEFAULT 0 COMMENT '商户自己是否以读，0未读  1以读' AFTER `is_read`;