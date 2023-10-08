###域名切换方案表
CREATE TABLE `merchant`.`t_domain_program` (
                                    `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
                                    `program_name` varchar(255)  NOT NULL COMMENT '方案名称',
                                    `group_type` tinyint(4) NOT NULL COMMENT '分组类型',
                                    `push_domain_num` tinyint(10) unsigned NOT NULL DEFAULT '1' COMMENT '主推域名数量',
                                    `status` tinyint(4) NOT NULL DEFAULT '2' COMMENT '是否开启 1为开启 2为关闭',
                                    `create_time` bigint(20) NOT NULL COMMENT '创建时间',
                                    `update_time` bigint(20) DEFAULT NULL COMMENT '更新时间',
                                    `last_updated` varchar(255)  NOT NULL COMMENT '最后更新人',
                                    `tab` varchar(10)  DEFAULT NULL COMMENT 'tab :ty dj cp',
                                    `del_tag` tinyint(2) unsigned DEFAULT '0' COMMENT '默认数据删除标签',
                                    PRIMARY KEY (`id`)
)  COMMENT='域名切换方案表';

###域名组表
CREATE TABLE `merchant`.`t_domain_group` (
                                  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
                                  `domain_group_name` varchar(255)  NOT NULL COMMENT '域名名称',
                                  `group_type` tinyint(4) NOT NULL COMMENT '分组类型',
                                  `alarm_threshold` bigint(10) DEFAULT '0' COMMENT '报警阀值',
                                  `exclusive_type` tinyint(4) NOT NULL COMMENT '专属类型 1 区域，2 VIP',
                                  `belong_area` bigint(20) DEFAULT NULL COMMENT '所属区域',
                                  `user_value` varchar(255)  DEFAULT NULL COMMENT '用户价值',
                                  `status` tinyint(4) NOT NULL DEFAULT '2' COMMENT '是否开启 1为开启 2为关闭',
                                  `create_time` bigint(20) NOT NULL COMMENT '创建时间',
                                  `update_time` bigint(20) DEFAULT NULL COMMENT '更新时间',
                                  `last_updated` varchar(255)  NOT NULL COMMENT '最后更新人',
                                  `tab` varchar(10)  DEFAULT NULL COMMENT 'tab :ty dj cp',
                                  `del_tag` tinyint(2) unsigned DEFAULT '0' COMMENT '默认数据删除标签',
                                  PRIMARY KEY (`id`)
)   COMMENT='域名组表';

###域名方案关系表
CREATE TABLE `merchant`.`t_domain_program_relation` (
                                             `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键Id',
                                             `domain_group_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '域名组id(域名组表中的主键id)',
                                             `program_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '域名切换方案id(域名切换方案表中的主键id)',
                                             `group_type` tinyint(4) DEFAULT NULL COMMENT '分组类型',
                                             `create_time` bigint(20) NOT NULL COMMENT '创建时间',
                                             `update_time` bigint(20) DEFAULT NULL COMMENT '更新时间',
                                             `last_updated` varchar(255)  NOT NULL COMMENT '最后更新人',
                                             PRIMARY KEY (`id`)
) COMMENT='域名方案关系表';

###域名组关系表
CREATE TABLE `merchant`.`t_domain_group_relation` (
                                           `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键Id',
                                           `domain_group_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '域名组id(域名组表中的主键id)',
                                           `domain_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '域名id(域名表中的主键id)',
                                           `create_time` bigint(20) NOT NULL COMMENT '创建时间',
                                           `update_time` bigint(20) DEFAULT NULL COMMENT '更新时间',
                                           `last_updated` varchar(255)  NOT NULL COMMENT '最后更新人',
                                           PRIMARY KEY (`id`)
)  COMMENT='域名组关系表';

###商户分组-体育表(merchant-common)
CREATE TABLE `merchant`.`t_merchant_group_ty` (
                                       `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
                                       `group_name` varchar(2000)  NOT NULL COMMENT '商户组名称',
                                       `group_type` tinyint(3) unsigned DEFAULT '1' COMMENT '1:公共组，2:Y组(Y系),3: S组(S系),4:B组(B系)',
                                       `group_code` varchar(255)  DEFAULT NULL COMMENT '商户组code,跟t_merchant中的domain_group_code进行关联,common,y,s,b',
                                       `status` tinyint(4) NOT NULL DEFAULT '2' COMMENT '是否开启 1为开启 2为关闭',
                                       `time_type` tinyint(4) NOT NULL DEFAULT '1' COMMENT '时间类型  1为分钟 2为小时 3为日  4为月',
                                       `times` tinyint(4) NOT NULL COMMENT '时间值',
                                       `update_time` bigint(20) DEFAULT NULL COMMENT '上次更新时间',
                                       `alarm_num` bigint(10) DEFAULT '20' COMMENT '报警数字',
                                       `third_status` tinyint(4) DEFAULT '1' COMMENT '域名第三方检测开关 1关闭 2开启',
                                       `program_id` bigint(20) DEFAULT NULL COMMENT '域名切换方案ID',
                                       PRIMARY KEY (`id`)
) COMMENT='商户分组-体育表(merchant-common)';


###域名组数据
INSERT INTO `merchant`.`t_domain_group` (`id`, `domain_group_name`, `group_type`, `alarm_threshold`, `exclusive_type`, `belong_area`, `user_value`, `status`, `create_time`, `update_time`, `last_updated`, `tab`, `del_tag`) VALUES (1, '默认组-C', 1, 0, 1, 0, null, 1, 1657633695000, 1657633695000, 'system', 'ty', 1);

INSERT INTO `merchant`.`t_domain_group` (`id`, `domain_group_name`, `group_type`, `alarm_threshold`, `exclusive_type`, `belong_area`, `user_value`, `status`, `create_time`, `update_time`, `last_updated`, `tab`, `del_tag`) VALUES (2, '默认组-Y', 2, 0, 1, 0, null, 1, 1657633695000, 1657633695000, 'system', 'ty', 1);

INSERT INTO `merchant`.`t_domain_group` (`id`, `domain_group_name`, `group_type`, `alarm_threshold`, `exclusive_type`, `belong_area`, `user_value`, `status`, `create_time`, `update_time`, `last_updated`, `tab`, `del_tag`) VALUES (3, '默认组-S', 3, 0, 1, 0, null, 1, 1657633695000, 1657633695000, 'system', 'ty', 1);

INSERT INTO `merchant`.`t_domain_group` (`id`, `domain_group_name`, `group_type`, `alarm_threshold`, `exclusive_type`, `belong_area`, `user_value`, `status`, `create_time`, `update_time`, `last_updated`, `tab`, `del_tag`) VALUES (4, '默认组-B', 4, 0, 1, 0, null, 1, 1657633695000, 1657633695000, 'system', 'ty', 1);

###域名切换方案数据
INSERT INTO `merchant`.`t_domain_program` (`id`, `program_name`, `group_type`, `push_domain_num`, `status`, `create_time`, `update_time`, `last_updated`, `tab`, `del_tag`) VALUES (1, '默认方案-C', 1, 0, 1, 1657633695000, 1657633695000, 'system', 'ty', 1);

INSERT INTO `merchant`.`t_domain_program` (`id`, `program_name`, `group_type`, `push_domain_num`, `status`, `create_time`, `update_time`, `last_updated`, `tab`, `del_tag`) VALUES (2, '默认方案-Y', 2, 0, 1, 1657633695000, 1657633695000, 'system', 'ty', 1);

INSERT INTO `merchant`.`t_domain_program` (`id`, `program_name`, `group_type`, `push_domain_num`, `status`, `create_time`, `update_time`, `last_updated`, `tab`, `del_tag`) VALUES (3, '默认方案-S', 3, 0, 1, 1657633695000, 1657633695000, 'system', 'ty', 1);

INSERT INTO `merchant`.`t_domain_program` (`id`, `program_name`, `group_type`, `push_domain_num`, `status`, `create_time`, `update_time`, `last_updated`, `tab`, `del_tag`) VALUES (4, '默认方案-B', 4, 0, 1, 1657633695000, 1657633695000, 'system', 'ty', 1);


###域名方案关系数据
INSERT INTO `merchant`.`t_domain_program_relation` (`domain_group_id`, `program_id`, `group_type`, `create_time`, `update_time`, `last_updated`) VALUES (1, 1, 1, 1657633695000, 1657633695000, 'system');

INSERT INTO `merchant`.`t_domain_program_relation` (`domain_group_id`, `program_id`, `group_type`, `create_time`, `update_time`, `last_updated`) VALUES (2, 2, 2, 1657633695000, 1657633695000, 'system');

INSERT INTO `merchant`.`t_domain_program_relation` (`domain_group_id`, `program_id`, `group_type`, `create_time`, `update_time`, `last_updated`) VALUES (3, 3, 3, 1657633695000, 1657633695000, 'system');

INSERT INTO `merchant`.`t_domain_program_relation` (`domain_group_id`, `program_id`, `group_type`, `create_time`, `update_time`, `last_updated`) VALUES (4, 4, 4, 1657633695000, 1657633695000, 'system');


-- ===============商户组迁移数据开始===================
INSERT INTO `merchant`.`t_merchant_group_ty` (
    `id`,
    `group_name`,
    `group_type`,
    `group_code`,
    `status`,
    `time_type`,
    `times`,
    `update_time`,
    `alarm_num`,
    `third_status`)
SELECT
    otd.`id`,
    otd.`group_name`,
    otd.`group_type`,
    otd.`group_code`,
    otd.`status`,
    otd.`time_type`,
    otd.`times`,
    otd.`update_time`,
    otd.`alarm_num`,
    otd.third_status
FROM
    `tybss_merchant_common`.`t_merchant_group` otd;




####设置域名表merchant_group_id = 0
UPDATE `merchant`.t_domain_ty tdty
SET tdty.merchant_group_id =0
WHERE tdty.merchant_group_id is null;

####初始化域名组和域名关系数据
INSERT INTO `merchant`.t_domain_group_relation (domain_group_id,domain_id,create_time,update_time,last_updated)
SELECT 1, id,1660031504897,1660031504897,"system" FROM `merchant`.t_domain_ty WHERE merchant_group_id =0 and group_type = 1;