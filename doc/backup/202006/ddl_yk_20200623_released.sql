drop table if exists `merchant`.`merchant_notice_type`;
CREATE TABLE `merchant`.`merchant_notice_type`
(
    `id`        int NOT NULL AUTO_INCREMENT,
    `type_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT '' COMMENT '类型名称',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_as_cs COMMENT ='公告类型';

ALTER TABLE `merchant`.`merchant_notice`
    ADD COLUMN `notice_type_id` smallint(3) NULL DEFAULT 1 COMMENT '公告类型' AFTER `is_tag`;

ALTER TABLE `merchant`.`merchant_notice`
    ADD COLUMN `standard_match_id` bigint(20) NULL DEFAULT 0 COMMENT 's_match_info的赛事id' AFTER `notice_type_id`;

ALTER TABLE `merchant`.`merchant_notice`
    ADD COLUMN `is_from` smallint(3) NULL DEFAULT 0 COMMENT '公告来源' AFTER `standard_match_id`;

ALTER TABLE `merchant`.`merchant_notice`
    ADD COLUMN `t_match_notice_id` bigint(20) NULL DEFAULT 0 COMMENT 'tybss_new数据库种的t_match_noitce的主键ID，给投注用户发消息的时候返回ID,否则默认0' AFTER `is_from`;

ALTER TABLE `merchant`.`merchant_notice`
    MODIFY COLUMN `release_range` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NULL DEFAULT '' COMMENT '发布范围,1:直营商户，2:渠道商户, 3:二级商户,4投注用户，以英文逗号拼接' AFTER `modify_time`;

ALTER TABLE `merchant`.`merchant_notice`
    ADD COLUMN `sport_id` bigint(20) NULL DEFAULT 0 COMMENT '赛事id的赛种' AFTER `standard_match_id`;


ALTER TABLE `merchant`.`merchant_notice`
    MODIFY COLUMN `status` smallint(3) NULL DEFAULT 0 COMMENT '公告状态  0:草稿，1:已发布，2:取消发布' AFTER `title`;


ALTER TABLE `merchant`.`merchant_notice`
    MODIFY COLUMN `standard_match_id` varchar(255) NULL DEFAULT '' COMMENT 's_match_info的赛事id' AFTER `notice_type_id`;


INSERT INTO `merchant`.`merchant_notice_type` (`id`, `type_name`)
VALUES (1, '普通公告'),
       (2, '商务公告'),
       (3, '维护公告'),
       (4, '足球赛事'),
       (5, '篮球赛事'),
       (6, '网球赛事'),
       (7, '羽毛球赛事'),
       (8, '乒乓球赛事'),
       (9, '斯若克赛事'),
       (10, '活动公告');


ALTER TABLE `merchant`.`merchant_notice`
MODIFY COLUMN `title` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NULL DEFAULT '' COMMENT '公告标题' AFTER `id`;



ALTER TABLE `merchant`.`merchant_notice`
ADD COLUMN `play_id` int(8) NULL DEFAULT 0 COMMENT '玩法ID' AFTER `sport_id`;
