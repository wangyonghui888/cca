/*
 Navicat Premium Data Transfer

 Source Server         : test_ty_dj
 Source Server Type    : MySQL
 Source Server Version : 80027
 Source Host           : 34.92.58.208:3350
 Source Schema         : activity

 Target Server Type    : MySQL
 Target Server Version : 80027
 File Encoding         : 65001

 Date: 09/02/2022 16:38:57
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ac_user_order_day
-- ----------------------------
DROP TABLE IF EXISTS `ac_user_order_day`;
CREATE TABLE `ac_user_order_day`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '自动编号',
  `uid` bigint NOT NULL,
  `time` bigint NOT NULL COMMENT 'yyyyMMdd',
  `user_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL,
  `merchant_code` bigint NOT NULL,
  `profit` decimal(16, 2) NOT NULL COMMENT '盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)',
  `return_amount` decimal(16, 2) NOT NULL COMMENT '返还金额',
  `valid_bet_amount` decimal(16, 2) NOT NULL COMMENT '有效投注金额',
  `valid_tickets` int NOT NULL COMMENT '有效注单数',
  `updated_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `index_time_user_id`(`time`, `uid`) USING BTREE,
  INDEX `idx_user_name`(`user_name`) USING BTREE,
  INDEX `idx_merchant_code`(`merchant_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_as_cs;

-- ----------------------------
-- Table structure for ac_user_order_hour
-- ----------------------------
DROP TABLE IF EXISTS `ac_user_order_hour`;
CREATE TABLE `ac_user_order_hour`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '自动编号',
  `uid` bigint NOT NULL,
  `time` bigint NOT NULL COMMENT 'yyyyMMddHH',
  `user_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL,
  `merchant_code` bigint NOT NULL,
  `profit` decimal(16, 2) NOT NULL COMMENT '盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)',
  `return_amount` decimal(16, 2) NOT NULL COMMENT '返还金额',
  `valid_bet_amount` decimal(16, 2) NOT NULL COMMENT '有效投注金额',
  `valid_tickets` int NOT NULL COMMENT '有效注单数',
  `updated_time` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_as_cs;

-- ----------------------------
-- Table structure for res_all_task_event_log
-- ----------------------------
DROP TABLE IF EXISTS `res_all_task_event_log`;
CREATE TABLE `res_all_task_event_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自动编号',
  `task_type` int NOT NULL DEFAULT 0 COMMENT '任务类型：1：market_bet_info，2：match_bet_info，3：merchant_order_day，4：merchant_order_month，5：merchant_order_week，6：merchant_sport_order_day ，7：merchant_sport_order_month，8：merchant_sport_order_week，9：order_full_detail，10：order_match_detail，11：settle_full_detail，12：user_full_info，13：user_order_day，14：user_order_month 15：user_order_week，16：user_sport_order_day，17：user_sport_order_month，18：user_sport_order_week，19：rcs_order_statistic_date,20：日志本身删除5天之前日志',
  `start_time` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NULL DEFAULT NULL COMMENT '任务执行开始时间',
  `end_time` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NULL DEFAULT NULL COMMENT '任务执行结束时间',
  `result` int NOT NULL DEFAULT 0 COMMENT '成功：1，失败：2',
  `exce_msg` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NULL DEFAULT NULL COMMENT '异常信息，result为2时才有',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_as_cs COMMENT = '所有任务时间执行结果表';

-- ----------------------------
-- Table structure for s_daily_lucky_box_number
-- ----------------------------
DROP TABLE IF EXISTS `s_daily_lucky_box_number`;
CREATE TABLE `s_daily_lucky_box_number`  (
  `box_type` smallint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '盲盒类型，1：白银盲盒  2：黄金盲盒  3：钻石盲盒',
  `daily_number` int NULL DEFAULT 0 COMMENT '每日出现次数',
  `show_rate` int NULL DEFAULT 0 COMMENT '时间间隔单位：分钟',
  `show_number` int NULL DEFAULT 0 COMMENT '在指定的时间间隔内开放的数量',
  `token` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '所需奖券数',
  `create_time` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '创建时间',
  `modify_time` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '修改时间',
  `created_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL DEFAULT '' COMMENT '创建人',
  `updated_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL DEFAULT '' COMMENT '更新人',
  PRIMARY KEY (`box_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_as_cs COMMENT = '每日数量表';

-- ----------------------------
-- Table structure for s_olympic_luckybox_dict
-- ----------------------------
DROP TABLE IF EXISTS `s_olympic_luckybox_dict`;
CREATE TABLE `s_olympic_luckybox_dict`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `box_type` smallint NOT NULL DEFAULT 0 COMMENT '盲盒类型，1：白银盲盒  2：黄金盲盒  3：钻石盲盒',
  `type` int NOT NULL DEFAULT 1 COMMENT '奖品类型：1 普通奖品，2 实物奖品',
  `visit_number` int NULL DEFAULT 0 COMMENT '出现次数',
  `award` bigint NOT NULL DEFAULT 0 COMMENT '单次奖金(单位 分)',
  `must_hit_date` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL DEFAULT '' COMMENT '必中日期格式 yyyy-MM-dd 如：2021-07-28',
  `must_hit_rate` bigint NOT NULL DEFAULT 0 COMMENT '必中概率 * 100, 比如 20.24% 存值 2024',
  `must_hit_number` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '必中次数',
  `is_allocate` tinyint UNSIGNED NOT NULL DEFAULT 1 COMMENT '是否派发奖品 1派发 0不派发',
  `is_up` int NOT NULL DEFAULT 1 COMMENT '是否上架:1 上架，0 不上架',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL DEFAULT '' COMMENT '名称比如：万国IWC 38003 工程师腕表',
  `user_limit` tinyint NOT NULL DEFAULT 0 COMMENT '限制用户次数，-1表示不限制',
  `top10_user` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否top10用户特有,1 是，0 不是',
  `create_time` bigint NOT NULL DEFAULT 0 COMMENT '创建时间',
  `modify_time` bigint NOT NULL DEFAULT 0 COMMENT '修改时间',
  `created_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL DEFAULT '' COMMENT '创建人',
  `updated_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL DEFAULT '' COMMENT '更新人',
  `order_num` int NOT NULL DEFAULT 0 COMMENT '排序值',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_box_type`(`box_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_as_cs COMMENT = '奥运盲盒运营数据配置表';

-- ----------------------------
-- Table structure for s_olympic_luckybox_records
-- ----------------------------
DROP TABLE IF EXISTS `s_olympic_luckybox_records`;
CREATE TABLE `s_olympic_luckybox_records`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `box_type` smallint NOT NULL DEFAULT 0 COMMENT '盲盒类型，1：白银盲盒  2：黄金盲盒  3：钻石盲盒',
  `box_id` bigint NOT NULL DEFAULT 0 COMMENT '盲合id',
  `open_number` smallint NOT NULL DEFAULT 1 COMMENT '拆盒次数',
  `use_token` smallint NOT NULL DEFAULT 0 COMMENT '消耗奖券数',
  `award` bigint NOT NULL DEFAULT 0 COMMENT '单次奖金(单位 分)',
  `merchant_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '商户ID',
  `merchant_account` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '商户名称',
  `uid` bigint NOT NULL DEFAULT 0 COMMENT '用户表id',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL DEFAULT '' COMMENT '用户名',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL DEFAULT '' COMMENT '备注',
  `activity_type` int NOT NULL DEFAULT 0 COMMENT '活动类型记录:0 普通盲盒，1 奥运盲盒, 2 世界杯....',
  `created_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` bigint NOT NULL DEFAULT 0 COMMENT '创建时间',
  `modify_time` bigint NOT NULL DEFAULT 0 COMMENT '修改时间',
  `updated_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL DEFAULT '' COMMENT '更新人',
  `top_merchant_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '顶层商户id',
  `top_merchant_account` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL DEFAULT '' COMMENT '顶层商户账号',
  `parent_merchant_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '父商户ID',
  `parent_merchant_account` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL DEFAULT '' COMMENT '父商户账号',
  `sort_level` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL DEFAULT '' COMMENT '商户排序层级',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_uid`(`uid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_as_cs COMMENT = '奥运拆盒历史记录';

-- ----------------------------
-- Table structure for t_ac_bonus
-- ----------------------------
DROP TABLE IF EXISTS `t_ac_bonus`;
CREATE TABLE `t_ac_bonus`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `uid` bigint NOT NULL COMMENT '用户ID',
  `act_id` bigint NOT NULL COMMENT '活动ID',
  `act_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '活动名称',
  `task_id` bigint NOT NULL COMMENT '任务ID',
  `task_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '任务名称',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '用户名',
  `create_time` bigint NOT NULL COMMENT '创建时间',
  `update_time` bigint NOT NULL COMMENT '修改时间',
  `ticket_num` int NOT NULL COMMENT '奖励数量',
  `bonus_type` int NOT NULL DEFAULT 2 COMMENT '奖励状态：3：可领取 2：未完成 1：已领取',
  `task_type` int NOT NULL COMMENT '0：每日任务\r\n1：成长任务',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '备注',
  `last_update` bigint NOT NULL DEFAULT 0 COMMENT '上次更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uid`(`uid`, `task_id`) USING BTREE,
  INDEX `uid_2`(`uid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '活动奖励表';

-- ----------------------------
-- Table structure for t_ac_bonus_log
-- ----------------------------
DROP TABLE IF EXISTS `t_ac_bonus_log`;
CREATE TABLE `t_ac_bonus_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `merchant_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '商户ID',
  `merchant_account` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '商户名称',
  `uid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '用户uid',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '用户名',
  `task_id` bigint NOT NULL DEFAULT 0 COMMENT '任务iD',
  `act_id` bigint NOT NULL DEFAULT 0 COMMENT '活动ID',
  `act_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL DEFAULT '' COMMENT '活动名称',
  `task_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL DEFAULT '' COMMENT '活动的任务名称',
  `ticket_num` int NOT NULL DEFAULT 0 COMMENT '劵的数量',
  `bonus_id` bigint NOT NULL DEFAULT 0 COMMENT 't_ac_bonus的主键ID',
  `receive_time` bigint NOT NULL DEFAULT 0 COMMENT '领取时间',
  `bonus_time` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL DEFAULT '' COMMENT '领取的时间，天',
  `created_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL DEFAULT '系统' COMMENT '创建人',
  `created_from` bigint NOT NULL DEFAULT 1 COMMENT '数据来源：1-系统奖励，2-系统补发',
  `top_merchant_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '顶层商户id',
  `top_merchant_account` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL DEFAULT '' COMMENT '顶层商户账号',
  `parent_merchant_id` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '父商户ID',
  `parent_merchant_account` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL DEFAULT '' COMMENT '父商户账号',
  `sort_level` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL DEFAULT '' COMMENT '商户排序层级',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_bonus_time`(`bonus_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_as_cs COMMENT = '优惠券领取日志表';

-- ----------------------------
-- Table structure for t_ac_task
-- ----------------------------
DROP TABLE IF EXISTS `t_ac_task`;
CREATE TABLE `t_ac_task`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `act_id` int NOT NULL COMMENT '活动ID',
  `act_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '活动名称',
  `task_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '任务名字',
  `condition_id` int NOT NULL COMMENT '条件ID\r\n每日任务：\r\n1：每日投注x笔\r\n2：当日单笔有效投注 >= x 元\r\n3：当日投注注单数 >= x 笔\r\n4：当日完成 x 笔串关玩法\r\n成长任务：\r\n1：本月累计投注天数 x 天\r\n2：本周累计有效投注 >= x 元\r\n3：本月累计有效投注 >= x 元',
  `task_condition` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '任务条件1',
  `task_condition2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL DEFAULT '' COMMENT '任务条件2',
  `task_condition3` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL DEFAULT '' COMMENT '任务条件3',
  `forward_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL DEFAULT '' COMMENT '去完成路径',
  `type` int NOT NULL DEFAULT 0 COMMENT '0：每日任务\r\n1：成长任务\r\n2：抽奖任务',
  `ticket_num` int NOT NULL COMMENT '奖券数量',
  `status` int NOT NULL DEFAULT 1 COMMENT '0：隐藏 1：显示',
  `invalidation` int NOT NULL DEFAULT 1 COMMENT '0：失效 1：有效',
  `create_time` bigint NOT NULL COMMENT '创建时间',
  `update_time` bigint NOT NULL COMMENT '修改时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL DEFAULT '' COMMENT '备注',
  `order_no` int NOT NULL DEFAULT 0 COMMENT '排序值',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `act_id`(`act_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_as_cs COMMENT = '活动任务表';

-- ----------------------------
-- Table structure for t_ac_user_token
-- ----------------------------
DROP TABLE IF EXISTS `t_ac_user_token`;
CREATE TABLE `t_ac_user_token`  (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `token_num` bigint NOT NULL DEFAULT 0 COMMENT '剩余令牌数',
  `cost_token_num` bigint NOT NULL DEFAULT 0 COMMENT '消费令牌数量',
  `bill_amount` bigint NOT NULL DEFAULT 0 COMMENT '有效投注额',
  `create_time` bigint NOT NULL DEFAULT 0 COMMENT '创建时间',
  `update_time` bigint NOT NULL DEFAULT 0 COMMENT '修改时间',
  `last_update_time` bigint NOT NULL DEFAULT 0 COMMENT '有效投注最后更新时间(yyyyMMddHH)',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_as_cs;

-- ----------------------------
-- Table structure for t_activity_config
-- ----------------------------
DROP TABLE IF EXISTS `t_activity_config`;
CREATE TABLE `t_activity_config`  (
  `id` bigint NOT NULL,
  `name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL,
  `type` int NOT NULL COMMENT '活动类型(1常规活动 2自定义活动 3特殊活动)',
  `terminal` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '活动终端',
  `start_time` bigint NOT NULL,
  `end_time` bigint NOT NULL,
  `time_limit` int NOT NULL COMMENT '领取时效(单位Hour)',
  `sport_id` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '参与活动赛种',
  `reward_type` tinyint NOT NULL COMMENT '奖励类型:  1 直接上分额度,2筹码彩金,3机会(如抽奖次数)',
  `total_cost` bigint NOT NULL COMMENT '活动总投入:根据活动的预算,来限制活动礼金发放.',
  `reward_percentage` double NOT NULL COMMENT '中奖比例:可以按一定的算法把总预算礼金分到固定数量的玩家上.(如1000个玩家参与活动,要保证不低于100个玩家获奖)',
  `single_day_max` bigint NOT NULL COMMENT '单日最高中奖额度:把总预算分配到活动的每天里面,对每天的中奖额度进行限制.',
  `single_user_max` bigint NOT NULL COMMENT '单用户最高中奖额度.',
  `user_partition_times` int NOT NULL COMMENT '参与次数:单个用户参与活动的次数.',
  `auto_check` tinyint NOT NULL COMMENT '是否自动审批:会员参与活动中奖时,是否把对应奖励额度自动发放,或者需要运营人员审核之后才能发放.',
  `partition_rule` bigint NOT NULL COMMENT '参与活动规则.(会员需要满足参与活动的条件)',
  `reward_rule` bigint NOT NULL COMMENT '领奖规则',
  `settle_cycle` tinyint NOT NULL COMMENT '结算周期(1h,单位小时)',
  `status` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '状态(0,关闭;1,开启)',
  `in_start_time` bigint NOT NULL COMMENT '玩家参与开始时间',
  `in_end_time` bigint NOT NULL COMMENT '玩家参与结束时间',
  `pc_url` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT 'pc活动图片地址',
  `h5_url` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT 'h5活动图片地址',
  `h5_maintain_url` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT 'h5活动维护图片地址',
  `pc_maintain_url` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT 'pc活动维护图片地址',
  `maintain_status` tinyint NOT NULL COMMENT '0关闭维护,开启活动;1开启维护',
  `maintain_end_time` bigint NOT NULL COMMENT '维护结束时间',
  `title` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '维护结束时间',
  `content` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '维护结束时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_as_cs COMMENT = '活动管理配置表';

-- ----------------------------
-- Table structure for t_activity_merchant
-- ----------------------------
DROP TABLE IF EXISTS `t_activity_merchant`;
CREATE TABLE `t_activity_merchant`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `activity_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL DEFAULT '' COMMENT '商户开启的活动，活动ID逗号分割',
  `merchant_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '商户ID',
  `merchant_account` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '商户名称',
  `status` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL DEFAULT '0' COMMENT '状态(0,关闭;1,开启) 暂时废弃',
  `entrance_status` bigint NOT NULL DEFAULT 0 COMMENT '活动入口状态 0：关闭 1：开启',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `index_merchantcode_activity`(`merchant_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_as_cs COMMENT = '活动商户配置表';

-- ----------------------------
-- Table structure for t_activity_switch
-- ----------------------------
DROP TABLE IF EXISTS `t_activity_switch`;
CREATE TABLE `t_activity_switch`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `merchant_id` bigint NOT NULL COMMENT '商户ID',
  `merchant_account` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '商户名称',
  `ty_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '体育参数key',
  `ty_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '体育参数value',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

SET FOREIGN_KEY_CHECKS = 1;

-- init activity config
INSERT INTO `t_activity_config`(`id`, `name`, `type`, `terminal`, `start_time`, `end_time`, `time_limit`, `sport_id`,
                                `reward_type`, `total_cost`, `reward_percentage`, `single_day_max`, `single_user_max`,
                                `user_partition_times`, `auto_check`, `partition_rule`, `reward_rule`, `settle_cycle`,
                                `status`, `in_start_time`, `in_end_time`, `pc_url`, `h5_url`, `h5_maintain_url`,
                                `pc_maintain_url`, `maintain_status`, `maintain_end_time`, `title`, `content`)
VALUES (10007, '每日任务', 2, '0', 0, 0, 0, '0', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, '1', 0, 0, '', '', '', '', 0,
        1650532664642, '', '');
INSERT INTO `t_activity_config`(`id`, `name`, `type`, `terminal`, `start_time`, `end_time`, `time_limit`, `sport_id`,
                                `reward_type`, `total_cost`, `reward_percentage`, `single_day_max`, `single_user_max`,
                                `user_partition_times`, `auto_check`, `partition_rule`, `reward_rule`, `settle_cycle`,
                                `status`, `in_start_time`, `in_end_time`, `pc_url`, `h5_url`, `h5_maintain_url`,
                                `pc_maintain_url`, `maintain_status`, `maintain_end_time`, `title`, `content`)
VALUES (10008, '成长任务', 2, '0', 0, 0, 0, '0', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, '1', 0, 0, '', '', '', '', 0,
        1650532664642, '', '');
INSERT INTO `t_activity_config`(`id`, `name`, `type`, `terminal`, `start_time`, `end_time`, `time_limit`, `sport_id`,
                                `reward_type`, `total_cost`, `reward_percentage`, `single_day_max`, `single_user_max`,
                                `user_partition_times`, `auto_check`, `partition_rule`, `reward_rule`, `settle_cycle`,
                                `status`, `in_start_time`, `in_end_time`, `pc_url`, `h5_url`, `h5_maintain_url`,
                                `pc_maintain_url`, `maintain_status`, `maintain_end_time`, `title`, `content`)
VALUES (10009, '幸运盲盒', 2, '0', 0, 0, 0, '0', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, '1', 0, 0, '', '', '', '', 0,
        1650532664642, '', '');

-- init lucky box config
INSERT INTO `s_daily_lucky_box_number` (`box_type`, `daily_number`, `show_rate`, `show_number`, `token`, `create_time`, `modify_time`, `created_by`, `updated_by`)VALUES (1, 1200, 50, 40, 30, 0, 1644044819059, '', '');
INSERT INTO `s_daily_lucky_box_number` (`box_type`, `daily_number`, `show_rate`, `show_number`, `token`, `create_time`, `modify_time`, `created_by`, `updated_by`)VALUES (2, 720, 50, 20, 50, 0, 1644044819059, '', '');
INSERT INTO `s_daily_lucky_box_number` (`box_type`, `daily_number`, `show_rate`, `show_number`, `token`, `create_time`, `modify_time`, `created_by`, `updated_by`)VALUES (3, 361, 50, 10, 100, 0, 1644044819059, '', '');

