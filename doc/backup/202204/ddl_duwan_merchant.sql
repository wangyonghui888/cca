CREATE TABLE `m_data_permissions` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) CHARACTER SET utf8mb4   DEFAULT NULL COMMENT '资源名称',
  `data_code` varchar(100) CHARACTER SET utf8mb4   DEFAULT NULL COMMENT '资源标识',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4  COMMENT='数据资源表';
CREATE TABLE `m_maintenance_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `maintenance_platform_id` bigint(20) NOT NULL COMMENT '维护平台表ID',
  `maintenance_start_time` bigint(20) NOT NULL COMMENT '维护开始时间',
  `real_start_time` bigint(20) DEFAULT NULL COMMENT '实际开始时间',
  `maintenance_end_time` bigint(20) NOT NULL COMMENT '维护结束时间',
  `remind_time` int(11) DEFAULT '0' COMMENT '提前提醒用户时间(默认0)',
  `superimpose_time` int(11) NOT NULL DEFAULT '0' COMMENT '叠加时间(默认0)',
  `is_send_notice` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否发送维护公告|0 否|1是，默认0',
  `notice_send_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '公告发送状态|0 未发送 |1已发送，默认未发送',
  `is_kick_user` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否维护时自动踢用户|0 否|1 是，默认0',
  `maintenance_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '维护状态(默认无维护0，未开始1，维护中2，已结束3)',
  `create_time` bigint(20) NOT NULL COMMENT '创建时间',
  `update_time` bigint(20) NOT NULL COMMENT '修改时间',
  `is_remind` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否提醒(0,不提醒,1,提醒)',
  `remind_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '提醒状态0未提醒 1已提醒',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=833 DEFAULT CHARSET=utf8mb4  COMMENT='维护记录表';
CREATE TABLE `m_kick_user_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `data_code` varchar(100)  NOT NULL COMMENT '数据类型',
  `server_name` varchar(100) CHARACTER SET utf8mb4  NOT NULL COMMENT '服务名称',
  `operation_type` tinyint(4) NOT NULL COMMENT '(按商户踢用户，按设备踢用户等)',
  `operators` varchar(100)  NOT NULL COMMENT '操作人',
  `operation_content` longtext CHARACTER SET utf8mb4  NOT NULL COMMENT '操作内容',
  `create_time` bigint(20) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1511599098456551426 DEFAULT CHARSET=utf8mb4 COMMENT='踢用户日志表';
CREATE TABLE `m_maintenance_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `data_code` varchar(100)  NOT NULL COMMENT '数据类型',
  `operation_type` tinyint(4) NOT NULL COMMENT '操作类型(设置维护，踢用户等)',
  `server_name` varchar(200)  NOT NULL COMMENT '服务名称',
  `operators` varchar(100) CHARACTER SET utf8mb4  NOT NULL COMMENT '操作人',
  `operation_content` varchar(2000) CHARACTER SET utf8mb4  NOT NULL COMMENT '操作内容',
  `create_time` bigint(20) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1511598777051230210 DEFAULT CHARSET=utf8mb4 COMMENT='维护日志表';
CREATE TABLE `m_maintenance_platform` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `server_name` varchar(255) CHARACTER SET utf8mb4  NOT NULL COMMENT '平台或服务名称',
  `serve_code` varchar(100)  DEFAULT NULL COMMENT '服务编码',
  `data_code` varchar(100) CHARACTER SET utf8mb4  DEFAULT NULL COMMENT '所属项目',
  `base_url` varchar(100) CHARACTER SET utf8mb4  DEFAULT NULL COMMENT '服务主路径',
  `kick_user_type` varchar(100) CHARACTER SET utf8mb4  DEFAULT NULL COMMENT '踢除用户类型1 2 3 4',
  `kick_user_url` varchar(255) CHARACTER SET utf8mb4  DEFAULT NULL COMMENT '踢用户用的URL地址',
  `create_time` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint(20) DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COMMENT='服务表';
CREATE TABLE `m_permissions` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) CHARACTER SET utf8mb4  DEFAULT NULL COMMENT '资源名称',
  `pid` bigint(20) DEFAULT NULL COMMENT '父资源id',
  `sort` bigint(20) DEFAULT NULL COMMENT '排序id',
  `url` varchar(100) CHARACTER SET utf8mb4  DEFAULT NULL COMMENT '请求地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COMMENT='资源表';

CREATE TABLE `m_role` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_name` varchar(128) CHARACTER SET utf8mb4  NOT NULL COMMENT '角色名',
  `creat_time` timestamp NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_m_role_name_index` (`role_name`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COMMENT='三端维护角色表';

CREATE TABLE `m_roles_data_permissions` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色id',
  `data_permission_id` bigint(20) DEFAULT NULL COMMENT '权限名id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_idx_roles_permissions` (`role_id`,`data_permission_id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COMMENT='角色数据权限表';

CREATE TABLE `m_roles_permissions` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色id',
  `permission_id` bigint(20) DEFAULT NULL COMMENT '权限名id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_idx_roles_permissions` (`role_id`,`permission_id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COMMENT='角色权限表';

CREATE TABLE `m_user` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(128)  NOT NULL COMMENT '用户名',
  `password` varchar(128)  NOT NULL COMMENT '密码',
  `creat_time` timestamp NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_m_name_index` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COMMENT='三端维护用户表';

CREATE TABLE `m_user_roles` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户名id',
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色名id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_idx_user_roles` (`user_id`,`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COMMENT='三端维护用户角色表';

-- 业务商户 merchant 主库
CREATE TABLE `multi_terminal_notice` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `notice_type` tinyint(4) NOT NULL COMMENT '电竞公告类型|1 DOTA2 |2 CS:GO 具体根据代码定义',
    `title` varchar(1000) CHARACTER SET utf8mb4 DEFAULT '' COMMENT '中文简体公告标题',
    `en_title` varchar(1000) CHARACTER SET utf8mb4 DEFAULT '' COMMENT '英文公告标题',
    `zh_title` varchar(1000) CHARACTER SET utf8mb4 DEFAULT '' COMMENT '繁体公告标题',
    `jp_title` varchar(1000) CHARACTER SET utf8mb4 DEFAULT '' COMMENT '日文公告标题',
    `vi_title` varchar(1000) CHARACTER SET utf8mb4 DEFAULT '' COMMENT '越南公告标题',
    `context` longtext CHARACTER SET utf8mb4 COMMENT '正文-中文简体',
    `en_context` longtext CHARACTER SET utf8mb4 COMMENT '正文-英文',
    `zh_context` longtext CHARACTER SET utf8mb4 COMMENT '正文-繁体',
    `jp_context` longtext CHARACTER SET utf8mb4 COMMENT '正文-日文',
    `vi_context` longtext CHARACTER SET utf8mb4 COMMENT '正文-越南语',
    `is_top` tinyint(4) NOT NULL COMMENT '是否置顶|0 否 |1 是',
    `is_important` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否重要|0 否 |1 是，默认为否',
    `is_login_popup` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否登录弹出|0 否 |1 是，默认为否',
    `is_rotation` tinyint(4) NOT NULL DEFAULT '1' COMMENT '是否轮播|0 否 |1 是，默认为是',
    `effective_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '生效开始时间，默认为调用接口时间',
    `effective_end_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '生效结束时间，默认为开始时间3天后的相同时间点',
    `source` tinyint(4) NOT NULL COMMENT '消息来源|1 电竞 | 2 彩票',
    `published_by` varchar(255) CHARACTER SET utf8mb4 NOT NULL COMMENT '发布人，默认为Auto',
    `multi_terminal_notice_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '其他端的公告表对应的ID主键，用于联动修改和删除用',
    `create_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '创建时间',
    `update_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uniq_multi_terminal_notice_id` (`multi_terminal_notice_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='多端公告本地记录表';

