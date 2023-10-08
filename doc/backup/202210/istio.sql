-- ddl

-- 域名(DJ,CP)新增字段
alter table merchant.t_domain
    add column `group_type` tinyint(4) DEFAULT NULL COMMENT '1:运维组，2:业务组，3:公用组' after domain_name;
ALTER TABLE merchant.t_domain
    MODIFY COLUMN `domain_type` tinyint(4) COMMENT '域名类型 1h5域名 2PC域名 3App域名 4图片域名 5其他域名';
alter table merchant.t_domain
    add column `line_carrier_id` bigint(20) DEFAULT NULL COMMENT '线路商ID' after `domain_type`;
alter table merchant.t_domain
    add column `status` tinyint(1) DEFAULT 0 COMMENT '0 未启用 1启用' after `line_carrier_id`;
alter table merchant.t_domain
    add column `self_test_tag` tinyint(1) DEFAULT 0 COMMENT '自检开关 0 未启用 1启用' after `status`;
alter table merchant.t_domain
    add column `tab` varchar(10) DEFAULT NULL COMMENT 'tab :ty dj cp' after `self_test_tag`;
ALTER TABLE merchant.`t_domain` CHANGE `enable` `enable` TINYINT(4) NULL DEFAULT '2' COMMENT '1已使用 2待使用 3被攻击 4被劫持';

-- 域名和域名组关系表新增字段
alter table merchant.t_domain_group_relation
    add column `tab` varchar(10) DEFAULT NULL COMMENT 'tab :ty dj cp' after `domain_id`;

-- 商户表增加tab,program_id字段
alter table merchant.t_merchant_group
    add column `tab` varchar(10) DEFAULT NULL COMMENT 'tab :ty dj cp' after `group_code`;
alter table merchant.t_merchant_group
    add column `program_id` bigint(10) DEFAULT NULL COMMENT '域名切换方案id(域名切换方案表中的主键id)' after `alarm_num`;


-- DML
-- ==================================域名t_domain============================================
-- 补充线路商数据
UPDATE merchant.t_domain SET t_domain.line_carrier_id = (SELECT id FROM `t_line_carrier` order by create_time limit 1);
-- 补充状态开关字段
UPDATE merchant.t_domain SET t_domain.status = 1;
-- 自检状态开关
UPDATE merchant.t_domain SET t_domain.self_test_tag = 1;
-- 域名状态0删除 改为2
UPDATE merchant.t_domain SET t_domain.enable = 2 where t_domain.enable = 0;
-- 补充tab字段
UPDATE merchant.t_domain SET t_domain.tab = "dj"
where (merchant.t_domain.merchant_group_id in (SELECT id from merchant.t_merchant_group where group_code = 1));
UPDATE merchant.t_domain SET t_domain.tab = "cp"
where (merchant.t_domain.merchant_group_id in (SELECT id from merchant.t_merchant_group where group_code = 2));
-- 删除不属于DJ/CP的脏数据
DELETE FROM merchant.t_domain where merchant.t_domain.tab is null;

-- ==================================域名组和域名中间表t_domain_group_relation============================================
-- 域名组和域名关系设置为ty
update merchant.t_domain_group_relation set tab = 'ty';

-- ==================================域名组，域名方案，以及中间目标初始化============================================

-- 初始化域名组默认数据
INSERT INTO `merchant`.`t_domain_group` ( `domain_group_name`, `group_type`, `alarm_threshold`, `exclusive_type`, `belong_area`, `user_value`, `status`, `create_time`, `update_time`, `last_updated`, `tab`, `del_tag`) VALUES
    ('默认组-CP-topcp', 1, 0, 1, 0, null, 1, 1657633695000, 1657633695000, 'system', 'cp', 1);
INSERT INTO `merchant`.`t_domain_group` ( `domain_group_name`, `group_type`, `alarm_threshold`, `exclusive_type`, `belong_area`, `user_value`, `status`, `create_time`, `update_time`, `last_updated`, `tab`, `del_tag`) VALUES
    ('默认组-CP-bw', 1, 0, 1, 0, null, 1, 1657633695000, 1657633695000, 'system', 'cp', 1);
INSERT INTO `merchant`.`t_domain_group` ( `domain_group_name`, `group_type`, `alarm_threshold`, `exclusive_type`, `belong_area`, `user_value`, `status`, `create_time`, `update_time`, `last_updated`, `tab`, `del_tag`) VALUES
    ('默认组-DJ-bob', 1, 0, 1, 0, null, 1, 1657633695000, 1657633695000, 'system', 'dj', 1);
INSERT INTO `merchant`.`t_domain_group` ( `domain_group_name`, `group_type`, `alarm_threshold`, `exclusive_type`, `belong_area`, `user_value`, `status`, `create_time`, `update_time`, `last_updated`, `tab`, `del_tag`) VALUES
    ('默认组-DJ-bw', 1, 0, 1, 0, null, 1, 1657633695000, 1657633695000, 'system', 'dj', 1);
-- 初始化域名方案默认数据
INSERT INTO `merchant`.`t_domain_program` (`program_name`, `group_type`, `push_domain_num`, `status`, `create_time`, `update_time`, `last_updated`, `tab`, `del_tag`)
VALUES ('默认方案-DJ-bob', 1, 0, 1, 1657633695000, 1657633695000, 'system', 'dj', 1);
INSERT INTO `merchant`.`t_domain_program` (`program_name`, `group_type`, `push_domain_num`, `status`, `create_time`, `update_time`, `last_updated`, `tab`, `del_tag`)
VALUES ('默认方案-DJ-bw', 1, 0, 1, 1657633695000, 1657633695000, 'system', 'dj', 1);
INSERT INTO `merchant`.`t_domain_program` (`program_name`, `group_type`, `push_domain_num`, `status`, `create_time`, `update_time`, `last_updated`, `tab`, `del_tag`)
VALUES ('默认方案-CP-topcp', 1, 0, 1, 1657633695000, 1657633695000, 'system', 'cp', 1);
INSERT INTO `merchant`.`t_domain_program` (`program_name`, `group_type`, `push_domain_num`, `status`, `create_time`, `update_time`, `last_updated`, `tab`, `del_tag`)
VALUES ('默认方案-CP-bw', 1, 0, 1, 1657633695000, 1657633695000, 'system', 'cp', 1);
-- 补充域名组和域名关系默认数据
INSERT INTO `merchant`.`t_domain_program_relation`
(`domain_group_id`, `program_id`, `group_type`, `create_time`, `update_time`, `last_updated`) VALUES
    ((SELECT id FROM merchant.t_domain_group WHERE domain_group_name = '默认组-CP-topcp'),
     (SELECT id FROM merchant.t_domain_program WHERE program_name = '默认方案-CP-topcp'),
     1, 1657633695000, 1657633695000, 'system');
INSERT INTO `merchant`.`t_domain_program_relation`
(`domain_group_id`, `program_id`, `group_type`, `create_time`, `update_time`, `last_updated`) VALUES
    ((SELECT id FROM merchant.t_domain_group WHERE domain_group_name = '默认组-CP-bw'),
     (SELECT id FROM merchant.t_domain_program WHERE program_name = '默认方案-CP-bw'),
     1, 1657633695000, 1657633695000, 'system');
INSERT INTO `merchant`.`t_domain_program_relation`
(`domain_group_id`, `program_id`, `group_type`, `create_time`, `update_time`, `last_updated`) VALUES
    ((SELECT id FROM merchant.t_domain_group WHERE domain_group_name = '默认组-DJ-bw'),
     (SELECT id FROM merchant.t_domain_program WHERE program_name = '默认方案-DJ-bw'),
     1, 1657633695000, 1657633695000, 'system');
INSERT INTO `merchant`.`t_domain_program_relation`
(`domain_group_id`, `program_id`, `group_type`, `create_time`, `update_time`, `last_updated`) VALUES
    ((SELECT id FROM merchant.t_domain_group WHERE domain_group_name = '默认组-DJ-bob'),
     (SELECT id FROM merchant.t_domain_program WHERE program_name = '默认方案-DJ-bob'),
     1, 1657633695000, 1657633695000, 'system');


-- ==================================商户组表============================================
-- 商户表修复旧数据
UPDATE merchant.t_merchant_group SET tab = 'dj' WHERE group_code = 1;
UPDATE merchant.t_merchant_group SET tab = 'cp' WHERE group_code = 2;
-- 初始化商户组数据
INSERT INTO `merchant`.`t_merchant_group`(`group_name`, `group_type`, `tab`, `group_code`, `status`, `time_type`, `times`, `update_time`, `alarm_num`, `program_id`) VALUES
    ('bw', 1, 'dj', 1, 1, 4, 100, 1662109560015, 1, (SELECT id FROM merchant.t_domain_program WHERE program_name = '默认方案-DJ-bw'));
INSERT INTO `merchant`.`t_merchant_group`(`group_name`, `group_type`, `tab`, `group_code`, `status`, `time_type`, `times`, `update_time`, `alarm_num`, `program_id`) VALUES
    ('bob', 1, 'dj', 1, 1, 4, 100, 1662109560015, 1, (SELECT id FROM merchant.t_domain_program WHERE program_name = '默认方案-DJ-bob'));
INSERT INTO `merchant`.`t_merchant_group`(`group_name`, `group_type`, `tab`, `group_code`, `status`, `time_type`, `times`, `update_time`, `alarm_num`, `program_id`) VALUES
    ('bw', 1, 'cp', 2, 1, 4, 100, 1662109560015, 1, (SELECT id FROM merchant.t_domain_program WHERE program_name = '默认方案-CP-bw'));
INSERT INTO `merchant`.`t_merchant_group`(`group_name`, `group_type`, `tab`, `group_code`, `status`, `time_type`, `times`, `update_time`, `alarm_num`, `program_id`) VALUES
    ('topcp', 1, 'cp', 2, 1, 4, 100, 1662109560015, 1, (SELECT id FROM merchant.t_domain_program WHERE program_name = '默认方案-CP-topcp'));


