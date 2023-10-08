CREATE TABLE tybss_report.s_sport_duty_trader (
                                                     `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
                                                     `shift` int(2) DEFAULT NULL COMMENT '班次 1早班:2:中班3:晚班4:晚班(1)5:晚班(2)',
                                                     `sport_id` bigint(20) DEFAULT NULL COMMENT '体育种类',
                                                     `user_code` varchar(32) DEFAULT NULL COMMENT '操盘手code',
                                                     `user_id` int(10) DEFAULT NULL COMMENT '操盘手id',
                                                     `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                                     `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                                     PRIMARY KEY (`id`),
                                                     UNIQUE KEY `shift_sport_id_user_id` (`shift`,`sport_id`,`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_as_cs COMMENT='赛种班次操盘员';