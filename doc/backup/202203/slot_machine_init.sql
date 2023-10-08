-- s_slot_jackpot_cfg: table
CREATE TABLE `s_slot_jackpot_cfg`
(
    `id`              bigint(32) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `slot_machine_id` bigint(32)          NOT NULL COMMENT '老虎机ID',
    `place`           int(10)             NOT NULL COMMENT '1：个位\r\n2：十位\r\n3：百位\r\n4：千位',
    `number`          int(10)             NOT NULL COMMENT '数字：0-9',
    `appearance_rate` int(10)             NOT NULL COMMENT '出现概率',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_as_cs COMMENT ='老虎机彩金配置表';

-- s_slot_machine_cfg: table
CREATE TABLE `s_slot_machine_cfg`
(
    `id`                bigint(32) unsigned                                          NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `slot_machine_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '老虎机名称',
    `slot_ticket_id`    int(10)                                                      NOT NULL COMMENT '老虎机抽奖消耗奖券ID',
    `lottery_number`    int(10)                                                      NOT NULL COMMENT '单次抽奖消耗奖券数',
    `daily_game_number` int(10)                                                      NOT NULL DEFAULT '1' COMMENT '0：不限制次数\r\n大于0：每日可参与游戏次数',
    `props_cfg`         json                                                         NOT NULL COMMENT '道具配置',
    `state`             int(10) unsigned                                             NOT NULL DEFAULT '0' COMMENT '0：关闭\r\n1：开启',
    `sort_no`           int(10)                                                      NOT NULL COMMENT '排序字段：客户端老虎机排序',
    `create_time`       bigint(32)                                                   NOT NULL COMMENT '创建时间',
    `last_update_time`  bigint(32)                                                   NOT NULL COMMENT '最后修改时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_as_cs COMMENT ='老虎机游戏配置表';

-- s_slot_props_dict: table
CREATE TABLE `s_slot_props_dict`
(
    `id`               bigint(32) unsigned                                          NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `props_type`       int(10)                                                      NOT NULL COMMENT '1：奖金倍率卡\r\n2：幸运奖券',
    `props_name`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '道具名称',
    `bonus_multiple`   int(10)                                                      NOT NULL COMMENT '奖金倍数或者幸运奖券数',
    `state`            int(10) unsigned                                             NOT NULL DEFAULT '0' COMMENT '0：关闭\r\n1：开启',
    `create_time`      bigint(32)                                                   NOT NULL COMMENT '创建时间',
    `last_update_time` bigint(32)                                                   NOT NULL COMMENT '最后修改时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_as_cs COMMENT ='老虎机道具字典表';

-- s_slot_ticket_dict: table
CREATE TABLE `s_slot_ticket_dict`
(
    `id`                       bigint(32) unsigned                                          NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `ticket_name`              varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '奖券名称',
    `ticket_type`              int(10)                                                      NOT NULL COMMENT '奖券类型\r\n1：基础奖券（不可编辑）\r\n2：游戏奖券（可配置）',
    `base_ticket_id`           int(10)                                                      NOT NULL COMMENT '合成材料：基础奖券ID',
    `synthetic_rate`           int(10) unsigned                                             NOT NULL DEFAULT '0' COMMENT '合成率',
    `base_ticket_number`       int(10)                                                      NOT NULL COMMENT '单次合成材料数',
    `return_rate`              int(10) unsigned                                             NOT NULL DEFAULT '0' COMMENT '返还率',
    `return_ticket_id`         int(10)                                                      NOT NULL COMMENT '返还奖券ID',
    `synthetic_improve_rate`   int(10)                                                      NOT NULL COMMENT '幸运奖券提升合成概率',
    `synthetic_improve_number` int(10)                                                      NOT NULL COMMENT '单次提升合成概率消耗幸运奖券数',
    `state`                    int(10) unsigned                                             NOT NULL DEFAULT '0' COMMENT '0：关闭\r\n1：开启',
    `create_time`              bigint(32)                                                   NOT NULL COMMENT '创建时间',
    `last_update_time`         bigint(21)                                                   NOT NULL COMMENT '最后修改时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_as_cs COMMENT ='老虎机奖券字典表';

INSERT INTO `s_slot_ticket_dict`(`id`, `ticket_name`, `ticket_type`, `base_ticket_id`, `synthetic_rate`,
                                 `base_ticket_number`, `return_rate`, `return_ticket_id`, `synthetic_improve_rate`,
                                 `synthetic_improve_number`, `state`, `create_time`, `last_update_time`)
VALUES (1, '普通奖券', 1, 0, 0, 0, 0, 0, 0, 0, 1, 1645169614000, 1645169614000);
INSERT INTO `s_slot_ticket_dict`(`id`, `ticket_name`, `ticket_type`, `base_ticket_id`, `synthetic_rate`,
                                 `base_ticket_number`, `return_rate`, `return_ticket_id`, `synthetic_improve_rate`,
                                 `synthetic_improve_number`, `state`, `create_time`, `last_update_time`)
VALUES (2, '白银奖券', 2, 1, 0, 1, 0, 1, 0, 1, 0, 1645169614000, 1646661411838);
INSERT INTO `s_slot_ticket_dict`(`id`, `ticket_name`, `ticket_type`, `base_ticket_id`, `synthetic_rate`,
                                 `base_ticket_number`, `return_rate`, `return_ticket_id`, `synthetic_improve_rate`,
                                 `synthetic_improve_number`, `state`, `create_time`, `last_update_time`)
VALUES (3, '黄金奖券', 2, 1, 0, 1, 0, 1, 0, 1, 0, 1645169614000, 1646661179052);
INSERT INTO `s_slot_ticket_dict`(`id`, `ticket_name`, `ticket_type`, `base_ticket_id`, `synthetic_rate`,
                                 `base_ticket_number`, `return_rate`, `return_ticket_id`, `synthetic_improve_rate`,
                                 `synthetic_improve_number`, `state`, `create_time`, `last_update_time`)
VALUES (4, '钻石奖券', 2, 1, 0, 1, 0, 1, 0, 1, 0, 1645169614000, 1646658230319);


-- s_slots_lottery_records: table
CREATE TABLE `s_slots_lottery_records`
(
    `id`            bigint(20) unsigned                                           NOT NULL AUTO_INCREMENT,
    `slots_name`    varchar(255) COLLATE utf8mb4_0900_as_cs                       DEFAULT NULL COMMENT '老虎机名称',
    `prop_type`     varchar(255) COLLATE utf8mb4_0900_as_cs                       DEFAULT NULL COMMENT '道具类型(名称)',
    `prop_times`    varchar(255) COLLATE utf8mb4_0900_as_cs                       DEFAULT NULL COMMENT '道具赔率',
    `slots_id`      bigint(20)                                                    NOT NULL COMMENT '老虎机id',
    `merchant_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '商户ID',
    `use_token`     int(10)                                                       DEFAULT NULL COMMENT '消耗奖券数',
    `award`         bigint(20)                                                    DEFAULT NULL COMMENT '滚轴奖金(单位 分)',
    `total_award`   bigint(20)                                                    DEFAULT NULL COMMENT '总计奖金(单位 分)',
    `uid`           bigint(20)                                                    DEFAULT NULL COMMENT '用户表id',
    `user_name`     varchar(255) COLLATE utf8mb4_0900_as_cs                       DEFAULT NULL COMMENT '用户名',
    `remark`        varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '备注',
    `created_by`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT '' COMMENT '创建人',
    `create_time`   bigint(20)                                                    DEFAULT NULL COMMENT '领取时间',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `index_uid` (`uid`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_as_cs
  ROW_FORMAT = DYNAMIC COMMENT ='老虎机彩金领取记录';

-- No native definition for element: index_uid (index)

-- s_slots_prop_reset_records: table
CREATE TABLE `s_slots_prop_reset_records`
(
    `id`             bigint(20) unsigned                                           NOT NULL AUTO_INCREMENT,
    `uid`            bigint(20)                                                    DEFAULT NULL COMMENT '用户表id',
    `user_name`      varchar(255) COLLATE utf8mb4_0900_as_cs                       DEFAULT NULL COMMENT '用户名',
    `merchant_code`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '商户ID',
    `prop_type`      varchar(255) COLLATE utf8mb4_0900_as_cs                       DEFAULT NULL COMMENT '道具类型(名称)',
    `prop_times`     varchar(255) COLLATE utf8mb4_0900_as_cs                       DEFAULT NULL COMMENT '道具赔率',
    `use_token`      int(10)                                                       DEFAULT NULL COMMENT '消耗奖券数',
    `use_token_type` varchar(255) COLLATE utf8mb4_0900_as_cs                       DEFAULT NULL COMMENT '消耗奖券类型(名称)',
    `remark`         varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '备注',
    `created_by`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT '' COMMENT '创建人',
    `create_time`    bigint(20)                                                    DEFAULT NULL COMMENT '重置时间',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `index_uid` (`uid`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_as_cs
  ROW_FORMAT = DYNAMIC COMMENT ='老虎机道具重置记录';

-- No native definition for element: index_uid (index)

-- s_slots_synthetic_records: table
CREATE TABLE `s_slots_synthetic_records`
(
    `id`                bigint(20) unsigned                                           NOT NULL AUTO_INCREMENT,
    `uid`               bigint(20)                                                    DEFAULT NULL COMMENT '用户表id',
    `user_name`         varchar(255) COLLATE utf8mb4_0900_as_cs                       DEFAULT NULL COMMENT '用户名',
    `merchant_code`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '商户ID',
    `source_token`      int(10)                                                       DEFAULT NULL COMMENT '消耗奖券数',
    `source_token_type` varchar(255) COLLATE utf8mb4_0900_as_cs                       DEFAULT NULL COMMENT '消耗奖券类型(名称)',
    `source_token_id`   bigint(20)                                                    DEFAULT NULL COMMENT '消耗奖券类型id',
    `target_token`      int(10)                                                       DEFAULT NULL COMMENT '合成奖券数',
    `target_token_type` varchar(255) COLLATE utf8mb4_0900_as_cs                       DEFAULT NULL COMMENT '合成奖券类型(名称)',
    `target_token_id`   bigint(20)                                                    DEFAULT NULL COMMENT '合成奖券类型id',
    `return_token`      int(10)                                                       DEFAULT NULL COMMENT '返还奖券数',
    `return_token_type` varchar(255) COLLATE utf8mb4_0900_as_cs                       DEFAULT NULL COMMENT '返还奖券类型(名称)',
    `return_token_id`   bigint(20)                                                    DEFAULT NULL COMMENT '返还奖券类型id',
    `remark`            varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '备注',
    `created_by`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT '' COMMENT '创建人',
    `create_time`       bigint(20)                                                    DEFAULT NULL COMMENT '合成时间',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `index_uid` (`uid`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_as_cs
  ROW_FORMAT = DYNAMIC COMMENT ='老虎机奖券合成记录';

-- No native definition for element: index_uid (index)

-- s_slots_token_change_history: table
CREATE TABLE `s_slots_token_change_history`
(
    `id`               bigint(20)                              NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `merchant_code`    varchar(255) COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '商户ID',
    `uid`              bigint(20)                                       DEFAULT NULL COMMENT '用户uid',
    `user_name`        varchar(255) COLLATE utf8mb4_0900_as_cs          DEFAULT NULL COMMENT '用户名',
    `change_type`      int(5)                                           DEFAULT '1' COMMENT '账变类型 1:任务奖励，2:盲盒消耗，3:合成奖励，4:游戏消耗，5:合成返还，6:合成消耗，7:提升消耗，8:道具奖励，9:系统补发',
    `slot_ticket_id`   bigint(20)                                       DEFAULT NULL COMMENT '奖券类型',
    `slot_ticket_name` varchar(255) COLLATE utf8mb4_0900_as_cs          DEFAULT NULL COMMENT '奖券类型名称',
    `change_token`     int(10)                                          DEFAULT NULL COMMENT '账变奖券数',
    `before_token`     int(10)                                          DEFAULT NULL COMMENT '转帐前奖券数',
    `after_token`      int(10)                                          DEFAULT NULL COMMENT '转帐后奖券数',
    `created_by`       varchar(255) COLLATE utf8mb4_0900_as_cs          DEFAULT '系统' COMMENT '创建人',
    `change_result`    varchar(255) COLLATE utf8mb4_0900_as_cs NOT NULL DEFAULT '成功' COMMENT '账变结果情况',
    `create_time`      bigint(20)                                       DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_as_cs COMMENT ='用户奖券变更历史记录';

-- s_slots_user_token: table
CREATE TABLE `s_slots_user_token`
(
    `id`               bigint(20) unsigned NOT NULL AUTO_INCREMENT,
    `user_id`          bigint(20)          NOT NULL COMMENT '用户ID',
    `token_num`        bigint(20)                              DEFAULT NULL COMMENT '拥有奖券数量',
    `token_id`         bigint(20)          NOT NULL COMMENT '奖券类型id ,幸运奖 id 为 0',
    `create_time`      varchar(255) COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '创建时间',
    `last_update_time` bigint(20)                              DEFAULT NULL COMMENT '最后更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_as_cs
  ROW_FORMAT = DYNAMIC COMMENT ='用户老虎机奖券表';

