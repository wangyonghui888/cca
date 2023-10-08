CREATE TABLE `tybss_new.t_user_level_relation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `uid` bigint(20) NOT NULL,
  `level_id` bigint(20) NOT NULL,
  `create_time` bigint(20) NOT NULL,
  `create_user` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL,
  `modify_time` bigint(20) NOT NULL,
  `modify_user` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL,
  `remark` varchar(200) COLLATE utf8mb4_0900_as_cs DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_level_uid` (`uid`) USING BTREE,
  KEY `user_level_id` (`level_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_as_cs;

CREATE TABLE `tybss_new.t_user_level` (
  `level_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '会员等级ID',
  `level_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称(普通、白银、黄金、钻石)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status` int(2) NOT NULL DEFAULT '1' COMMENT '状态，1为有效。0为无效，默认1\r\n',
  `bg_color` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `color` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`level_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='会员等级表';

CREATE TABLE `tybss_new.t_user_level_relation_history` (
  `id` bigint(20) NOT NULL,
  `uid` bigint(20) NOT NULL,
  `level_id` bigint(20) NOT NULL,
  `create_time` bigint(32) NOT NULL,
  `create_user` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL,
  `modify_time` bigint(32) NOT NULL,
  `modify_user` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL,
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL,
  `sid` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`sid`) USING BTREE,
  KEY `user_level_uid` (`uid`) USING BTREE,
  KEY `user_level_id` (`level_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_as_cs;

INSERT INTO `tybss_new.t_user_level` VALUES (1, '正常用户', '2020-02-26 10:59:35', 1, NULL, NULL);
INSERT INTO `tybss_new.t_user_level` VALUES (2, 'UFO-蛇', '2020-02-26 11:55:15', 1, '#DCBD7A', '#E0464D');
INSERT INTO `tybss_new.t_user_level` VALUES (3, 'UFO-$', '2020-02-26 11:11:20', 1, '#DCBD7A', NULL);
INSERT INTO `tybss_new.t_user_level` VALUES (4, 'UFO-资讯客', '2020-02-26 11:11:26', 1, '#DCBD7A', NULL);
INSERT INTO `tybss_new.t_user_level` VALUES (5, 'UFO', '2020-02-26 11:11:32', 1, '#DCBD7A', NULL);
INSERT INTO `tybss_new.t_user_level` VALUES (6, '蛇-1级', '2020-02-26 11:11:37', 1, '#E0464D', NULL);
INSERT INTO `tybss_new.t_user_level` VALUES (7, '蛇-2级', '2020-02-26 11:11:47', 1, '#E0464D', NULL);
INSERT INTO `tybss_new.t_user_level` VALUES (8, '蛇-3级', '2020-02-26 11:11:53', 1, '#E0464D', NULL);
INSERT INTO `tybss_new.t_user_level` VALUES (9, '水军', '2020-02-26 11:12:09', 1, '#82ED4F', NULL);
INSERT INTO `tybss_new.t_user_level` VALUES (10, '资讯客', '2020-02-26 11:12:17', 1, '#E86E90', NULL);
INSERT INTO `tybss_new.t_user_level` VALUES (11, '好脚', '2020-02-26 11:12:07', 1, '#788299', NULL);
INSERT INTO `tybss_new.t_user_level` VALUES (12, '赢家', '2020-02-26 11:12:21', 1, 'black', NULL);
INSERT INTO `tybss_new.t_user_level` VALUES (13, '专家', '2020-02-26 11:12:32', 1, '#408ADB', NULL);
INSERT INTO `tybss_new.t_user_level` VALUES (14, '投资家', '2020-02-26 11:12:33', 1, '#7D4EB0', NULL);


