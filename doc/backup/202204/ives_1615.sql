-- 业务商户 merchant 主库
CREATE TABLE merchant.`multi_terminal_notice` (
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

-- 业务商户 panda_auth 主库
INSERT INTO panda_auth.`sys_url` (`url`, `type`, `create_time`) VALUES ('/manage/notice/addESportsNotice', '1', CURRENT_TIMESTAMP);
INSERT INTO panda_auth.`sys_url` (`url`, `type`, `create_time`) VALUES ('/manage/notice/editESportsNotice', '1', CURRENT_TIMESTAMP);
INSERT INTO panda_auth.`sys_url` (`url`, `type`, `create_time`) VALUES ('/manage/notice/delESportsNotice', '1', CURRENT_TIMESTAMP);
