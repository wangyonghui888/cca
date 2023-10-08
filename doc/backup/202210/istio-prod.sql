ALTER TABLE `merchant`.`multi_terminal_notice`
    ADD COLUMN `bi_title` varchar(1000) CHARACTER SET utf8mb4 DEFAULT '' COMMENT '印尼语公告标题' after ma_title;

ALTER TABLE `merchant`.`multi_terminal_notice`
    ADD COLUMN `bi_context` varchar(1000) CHARACTER SET utf8mb4 DEFAULT '' COMMENT '印尼语公告正文' after ma_context;


ALTER TABLE `merchant`.`merchant_notice`
    ADD COLUMN `bi_title` varchar(1000) CHARACTER SET utf8mb4 DEFAULT '' COMMENT '印尼语公告标题' after ma_title;

ALTER TABLE `merchant`.`merchant_notice`
    ADD COLUMN `bi_context` varchar(1000) CHARACTER SET utf8mb4 DEFAULT '' COMMENT '印尼语公告正文' after ma_context;


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
UPDATE merchant.t_domain SET t_domain.tab = "ty";
UPDATE merchant.t_domain SET t_domain.tab = "dj"
where (merchant.t_domain.merchant_group_id in (SELECT id from merchant.t_merchant_group where group_code = 1));
UPDATE merchant.t_domain SET t_domain.tab = "cp"
where (merchant.t_domain.merchant_group_id in (SELECT id from merchant.t_merchant_group where group_code = 2));

-- ==================================域名组和域名中间表t_domain_group_relation============================================
-- 域名组和域名关系设置为ty
update merchant.t_domain_group_relation set tab = 'ty';

-- ==================================商户组表============================================
-- 商户表修复旧数据
UPDATE merchant.t_merchant_group SET tab = 'dj' WHERE group_code = 1;
UPDATE merchant.t_merchant_group SET tab = 'cp' WHERE group_code = 2;


-- ==================================域名组，域名方案，以及中间目标初始化============================================

-- 初始化域名组默认数据
INSERT INTO `merchant`.`t_domain_group` ( `domain_group_name`, `group_type`, `alarm_threshold`, `exclusive_type`, `belong_area`, `user_value`, `status`, `create_time`, `update_time`, `last_updated`, `tab`, `del_tag`)
SELECT GROUP_CONCAT("默认组-",UPPER(t_merchant_group.tab),"-",t_merchant_group.group_name),
       1,0,1,0,null,1,(SELECT UNIX_TIMESTAMP(NOW()) * 1000),(SELECT UNIX_TIMESTAMP(NOW()) * 1000),'system',t_merchant_group.tab,1
FROM t_merchant_group GROUP BY t_merchant_group.id ;

-- 初始化域名方案默认数据
INSERT INTO `merchant`.`t_domain_program` (`program_name`, `group_type`, `push_domain_num`, `status`, `create_time`, `update_time`, `last_updated`, `tab`, `del_tag`)
SELECT GROUP_CONCAT("默认方案-",UPPER(t_merchant_group.tab),"-",t_merchant_group.group_name),
       1,0,1,(SELECT UNIX_TIMESTAMP(NOW()) * 1000),(SELECT UNIX_TIMESTAMP(NOW()) * 1000),'system',t_merchant_group.tab,1
FROM t_merchant_group GROUP BY t_merchant_group.id ;

-- 补充域名组和方案关系默认数据
INSERT INTO `merchant`.`t_domain_program_relation`
(`domain_group_id`, `program_id`, `group_type`, `create_time`, `update_time`, `last_updated`)
SELECT
    (SELECT id FROM merchant.t_domain_group WHERE t_domain_group.domain_group_name = GROUP_CONCAT("默认组-",UPPER(t_merchant_group.tab),"-",t_merchant_group.group_name)),
    (SELECT id FROM merchant.t_domain_program WHERE t_domain_program.program_name = GROUP_CONCAT("默认方案-",UPPER(t_merchant_group.tab),"-",t_merchant_group.group_name)),
    1, (SELECT UNIX_TIMESTAMP(NOW()) * 1000), (SELECT UNIX_TIMESTAMP(NOW()) * 1000), 'system'
FROM t_merchant_group GROUP BY t_merchant_group.id ;

-- 旧的域名数据绑定域名组
INSERT INTO `merchant`.`t_domain_group_relation`(`domain_group_id`, `domain_id`, `tab`, `create_time`, `update_time`, `last_updated`)
SELECT
    (SELECT id FROM t_domain_group WHERE domain_group_name = GROUP_CONCAT("默认组-",UPPER(t_domain.tab),"-",(SELECT group_name FROM t_merchant_group WHERE id = t_domain.merchant_group_id))) AS domain_group_id,
    id,t_domain.tab,(SELECT UNIX_TIMESTAMP(NOW()) * 1000),(SELECT UNIX_TIMESTAMP(NOW()) * 1000),'admin'
FROM t_domain GROUP BY id;


-- 每个商户组 绑定一个默认方案

UPDATE `merchant`.`t_merchant_group` SET program_id =
                                             (SELECT id FROM t_domain_program WHERE program_name ="默认方案-CP-demo") where id = 1;
UPDATE `merchant`.`t_merchant_group` SET program_id =
                                             (SELECT id FROM t_domain_program WHERE program_name ="默认方案-DJ-topdj") where id = 2;
UPDATE `merchant`.`t_merchant_group` SET program_id =
                                             (SELECT id FROM t_domain_program WHERE program_name ="默认方案-DJ-bw") where id = 3;
UPDATE `merchant`.`t_merchant_group` SET program_id =
                                             (SELECT id FROM t_domain_program WHERE program_name ="默认方案-DJ-yb02") where id = 4;
UPDATE `merchant`.`t_merchant_group` SET program_id =
                                             (SELECT id FROM t_domain_program WHERE program_name ="默认方案-DJ-yb") where id = 5;
UPDATE `merchant`.`t_merchant_group` SET program_id =
                                             (SELECT id FROM t_domain_program WHERE program_name ="默认方案-DJ-bob") where id = 6;
UPDATE `merchant`.`t_merchant_group` SET program_id =
                                             (SELECT id FROM t_domain_program WHERE program_name ="默认方案-CP-test1") where id = 7;
UPDATE `merchant`.`t_merchant_group` SET program_id =
                                             (SELECT id FROM t_domain_program WHERE program_name ="默认方案-DJ-demo") where id = 8;
UPDATE `merchant`.`t_merchant_group` SET program_id =
                                             (SELECT id FROM t_domain_program WHERE program_name ="默认方案-CP-bob") where id = 9;
UPDATE `merchant`.`t_merchant_group` SET program_id =
                                             (SELECT id FROM t_domain_program WHERE program_name ="默认方案-CP-yabo") where id = 10;
UPDATE `merchant`.`t_merchant_group` SET program_id =
                                             (SELECT id FROM t_domain_program WHERE program_name ="默认方案-CP-yb02") where id = 11;
UPDATE `merchant`.`t_merchant_group` SET program_id =
                                             (SELECT id FROM t_domain_program WHERE program_name ="默认方案-CP-bob02") where id = 12;
UPDATE `merchant`.`t_merchant_group` SET program_id =
                                             (SELECT id FROM t_domain_program WHERE program_name ="默认方案-CP-topcp") where id = 13;
UPDATE `merchant`.`t_merchant_group` SET program_id =
                                             (SELECT id FROM t_domain_program WHERE program_name ="默认方案-CP-topcp02") where id = 14;
UPDATE `merchant`.`t_merchant_group` SET program_id =
                                             (SELECT id FROM t_domain_program WHERE program_name ="默认方案-CP-bw") where id = 15;




