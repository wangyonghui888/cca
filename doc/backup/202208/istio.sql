################################三端服务
###区域
CREATE TABLE `merchant`.`m_domain_area`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '逻辑id',
    `name`        varchar(500) DEFAULT NULL COMMENT '区域名称',
    `status`      tinyint(1)   DEFAULT 0 COMMENT '0 未启用 1启用',
    `delete_tag`  tinyint(1)   DEFAULT 0 COMMENT '删除状态 0 未删除 1已删除',
    `create_time` bigint(20)   DEFAULT '0' COMMENT '创建时间',
    `create_user` varchar(255) DEFAULT NULL COMMENT '创建人',
    `update_time` bigint(20)   DEFAULT '0' COMMENT '创建时间',
    `update_user` varchar(255) DEFAULT NULL COMMENT '更新人',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1 COMMENT = '域名区域管理';

###===============添加区域默认数据开始===================
INSERT INTO `merchant`.`m_domain_area`(`name`, `status`, `delete_tag`, `create_time`, `create_user`, `update_time`,
                                       `update_user`)
VALUES ('广东', 1, 0, 1657148827000, 'admin', 1657148827000, 'admin');
INSERT INTO `merchant`.`m_domain_area`(`name`, `status`, `delete_tag`, `create_time`, `create_user`, `update_time`,
                                       `update_user`)
VALUES ('江西', 1, 0, 1657148827000, 'admin', 1657148827000, 'admin');
INSERT INTO `merchant`.`m_domain_area`(`name`, `status`, `delete_tag`, `create_time`, `create_user`, `update_time`,
                                       `update_user`)
VALUES ('浙江', 1, 0, 1657148827000, 'admin', 1657148827000, 'admin');
INSERT INTO `merchant`.`m_domain_area`(`name`, `status`, `delete_tag`, `create_time`, `create_user`, `update_time`,
                                       `update_user`)
VALUES ('江苏', 1, 0, 1657148827000, 'admin', 1657148827000, 'admin');
INSERT INTO `merchant`.`m_domain_area`(`name`, `status`, `delete_tag`, `create_time`, `create_user`, `update_time`,
                                       `update_user`)
VALUES ('青海', 1, 0, 1657148827000, 'admin', 1657148827000, 'admin');
INSERT INTO `merchant`.`m_domain_area`(`name`, `status`, `delete_tag`, `create_time`, `create_user`, `update_time`,
                                       `update_user`)
VALUES ('甘肃', 1, 0, 1657148827000, 'admin', 1657148827000, 'admin');
INSERT INTO `merchant`.`m_domain_area`(`name`, `status`, `delete_tag`, `create_time`, `create_user`, `update_time`,
                                       `update_user`)
VALUES ('北京', 1, 0, 1657148827000, 'admin', 1657148827000, 'admin');
INSERT INTO `merchant`.`m_domain_area`(`name`, `status`, `delete_tag`, `create_time`, `create_user`, `update_time`,
                                       `update_user`)
VALUES ('上海', 1, 0, 1657148827000, 'admin', 1657148827000, 'admin');
INSERT INTO `merchant`.`m_domain_area`(`name`, `status`, `delete_tag`, `create_time`, `create_user`, `update_time`,
                                       `update_user`)
VALUES ('重庆', 1, 0, 1657148827000, 'admin', 1657148827000, 'admin');
INSERT INTO `merchant`.`m_domain_area`(`name`, `status`, `delete_tag`, `create_time`, `create_user`, `update_time`,
                                       `update_user`)
VALUES ('四川', 1, 0, 1657148827000, 'admin', 1657148827000, 'admin');
INSERT INTO `merchant`.`m_domain_area`(`name`, `status`, `delete_tag`, `create_time`, `create_user`, `update_time`,
                                       `update_user`)
VALUES ('天津', 1, 0, 1657148827000, 'admin', 1657148827000, 'admin');
INSERT INTO `merchant`.`m_domain_area`(`name`, `status`, `delete_tag`, `create_time`, `create_user`, `update_time`,
                                       `update_user`)
VALUES ('内蒙古', 1, 0, 1657148827000, 'admin', 1657148827000, 'admin');
INSERT INTO `merchant`.`m_domain_area`(`name`, `status`, `delete_tag`, `create_time`, `create_user`, `update_time`,
                                       `update_user`)
VALUES ('新疆', 1, 0, 1657148827000, 'admin', 1657148827000, 'admin');
INSERT INTO `merchant`.`m_domain_area`(`name`, `status`, `delete_tag`, `create_time`, `create_user`, `update_time`,
                                       `update_user`)
VALUES ('湖北', 1, 0, 1657148827000, 'admin', 1657148827000, 'admin');
INSERT INTO `merchant`.`m_domain_area`(`name`, `status`, `delete_tag`, `create_time`, `create_user`, `update_time`,
                                       `update_user`)
VALUES ('湖南', 1, 0, 1657148827000, 'admin', 1657148827000, 'admin');
INSERT INTO `merchant`.`m_domain_area`(`name`, `status`, `delete_tag`, `create_time`, `create_user`, `update_time`,
                                       `update_user`)
VALUES ('福建', 1, 0, 1657148827000, 'admin', 1657148827000, 'admin');
INSERT INTO `merchant`.`m_domain_area`(`name`, `status`, `delete_tag`, `create_time`, `create_user`, `update_time`,
                                       `update_user`)
VALUES ('云南', 1, 0, 1657148827000, 'admin', 1657148827000, 'admin');
INSERT INTO `merchant`.`m_domain_area`(`name`, `status`, `delete_tag`, `create_time`, `create_user`, `update_time`,
                                       `update_user`)
VALUES ('西藏', 1, 0, 1657148827000, 'admin', 1657148827000, 'admin');
INSERT INTO `merchant`.`m_domain_area`(`name`, `status`, `delete_tag`, `create_time`, `create_user`, `update_time`,
                                       `update_user`)
VALUES ('河南', 1, 0, 1657148827000, 'admin', 1657148827000, 'admin');
INSERT INTO `merchant`.`m_domain_area`(`name`, `status`, `delete_tag`, `create_time`, `create_user`, `update_time`,
                                       `update_user`)
VALUES ('河北', 1, 0, 1657148827000, 'admin', 1657148827000, 'admin');
INSERT INTO `merchant`.`m_domain_area`(`name`, `status`, `delete_tag`, `create_time`, `create_user`, `update_time`,
                                       `update_user`)
VALUES ('山西', 1, 0, 1657148827000, 'admin', 1657148827000, 'admin');
INSERT INTO `merchant`.`m_domain_area`(`name`, `status`, `delete_tag`, `create_time`, `create_user`, `update_time`,
                                       `update_user`)
VALUES ('陕西', 1, 0, 1657148827000, 'admin', 1657148827000, 'admin');
INSERT INTO `merchant`.`m_domain_area`(`name`, `status`, `delete_tag`, `create_time`, `create_user`, `update_time`,
                                       `update_user`)
VALUES ('台湾', 1, 0, 1657148827000, 'admin', 1657148827000, 'admin');
INSERT INTO `merchant`.`m_domain_area`(`name`, `status`, `delete_tag`, `create_time`, `create_user`, `update_time`,
                                       `update_user`)
VALUES ('辽宁', 1, 0, 1657148827000, 'admin', 1657148827000, 'admin');
INSERT INTO `merchant`.`m_domain_area`(`name`, `status`, `delete_tag`, `create_time`, `create_user`, `update_time`,
                                       `update_user`)
VALUES ('吉林', 1, 0, 1657148827000, 'admin', 1657148827000, 'admin');
INSERT INTO `merchant`.`m_domain_area`(`name`, `status`, `delete_tag`, `create_time`, `create_user`, `update_time`,
                                       `update_user`)
VALUES ('黑龙江', 1, 0, 1657148827000, 'admin', 1657148827000, 'admin');
INSERT INTO `merchant`.`m_domain_area`(`name`, `status`, `delete_tag`, `create_time`, `create_user`, `update_time`,
                                       `update_user`)
VALUES ('安徽', 1, 0, 1657148827000, 'admin', 1657148827000, 'admin');
INSERT INTO `merchant`.`m_domain_area`(`name`, `status`, `delete_tag`, `create_time`, `create_user`, `update_time`,
                                       `update_user`)
VALUES ('山东', 1, 0, 1657148827000, 'admin', 1657148827000, 'admin');
INSERT INTO `merchant`.`m_domain_area`(`name`, `status`, `delete_tag`, `create_time`, `create_user`, `update_time`,
                                       `update_user`)
VALUES ('海南', 1, 0, 1657148827000, 'admin', 1657148827000, 'admin');
INSERT INTO `merchant`.`m_domain_area`(`name`, `status`, `delete_tag`, `create_time`, `create_user`, `update_time`,
                                       `update_user`)
VALUES ('贵州', 1, 0, 1657148827000, 'admin', 1657148827000, 'admin');
INSERT INTO `merchant`.`m_domain_area`(`name`, `status`, `delete_tag`, `create_time`, `create_user`, `update_time`,
                                       `update_user`)
VALUES ('广西', 1, 0, 1657148827000, 'admin', 1657148827000, 'admin');
INSERT INTO `merchant`.`m_domain_area`(`name`, `status`, `delete_tag`, `create_time`, `create_user`, `update_time`,
                                       `update_user`)
VALUES ('宁夏', 1, 0, 1657148827000, 'admin', 1657148827000, 'admin');
INSERT INTO `merchant`.`m_domain_area`(`name`, `status`, `delete_tag`, `create_time`, `create_user`, `update_time`,
                                       `update_user`)
VALUES ('香港', 1, 0, 1657148827000, 'admin', 1657148827000, 'admin');
INSERT INTO `merchant`.`m_domain_area`(`name`, `status`, `delete_tag`, `create_time`, `create_user`, `update_time`,
                                       `update_user`)
VALUES ('澳门', 1, 0, 1657148827000, 'admin', 1657148827000, 'admin');
###===============添加默认数据结束===================

CREATE TABLE `merchant`.`t_domain_ty`
(
    `id`                bigint(20) NOT NULL AUTO_INCREMENT COMMENT '逻辑id',
    `domain_type`       tinyint(4)   DEFAULT 0 COMMENT '域名类型 1h5域名 2PC域名 3App域名 4图片域名 5其他域名 ',
    `domain_name`       varchar(500) DEFAULT NULL COMMENT '域名',
    `group_type`        tinyint(4)   DEFAULT 0 COMMENT '分组类型(1COMMON2Y3S4B)',
    `line_carrier_id`   bigint(20)   DEFAULT NULL COMMENT '线路商ID',
    `status`            tinyint(1)   DEFAULT 0 COMMENT '启用状态 0 未启用 1启用',
    `self_test_tag`     tinyint(1)   DEFAULT 0 COMMENT '自检开关 0 未启用 1启用',
    `tab`               varchar(10)  DEFAULT NULL COMMENT 'tab :ty dj cp',
    `enable`            tinyint(4)   DEFAULT 2 COMMENT '1使用 2待使用 3已失效',
    `enable_time`       bigint(20)   DEFAULT NULL COMMENT '启用时间',
    `create_time`       bigint(20)   DEFAULT NULL COMMENT '创建时间',
    `create_user`       varchar(255) DEFAULT NULL COMMENT '创建人',
    `update_time`       bigint(20)   DEFAULT NULL COMMENT '修改时间',
    `update_user`       varchar(255) DEFAULT NULL COMMENT '更新人',
    `merchant_group_id` bigint(20)   DEFAULT NULL COMMENT '商户组ID',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1 COMMENT = '体育域名表';

###===============域名迁移数据开始===================
INSERT INTO `merchant`.`t_domain_ty` (
    `id`,
      `domain_type`,
      `domain_name`,
      `enable`,
      `enable_time`,
      `create_time`,
      `create_user`,
      `update_time`,
      `update_user`,
      `merchant_group_id`,
      `group_type`,
      `tab`)
SELECT otd.`id`,
       otd.`domain_type`,
       otd.`domain_name`,
       otd.`enable`,
       UNIX_TIMESTAMP(otd.`enable_time`) * 1000,
       UNIX_TIMESTAMP(otd.`create_time`) * 1000,
       otd.`create_user`,
       IFNULL(UNIX_TIMESTAMP(otd.`update_time`) * 1000, UNIX_TIMESTAMP(otd.`create_time`) * 1000),
       otd.`update_user`,
       otd.`merchant_group_id`,
       otmg.`group_type`,
       'ty'
FROM `tybss_merchant_common`.`t_domain` otd
         LEFT JOIN
     `tybss_merchant_common`.`t_merchant_group` otmg
     ON otd.merchant_group_id = otmg.id
WHERE otd.delete_tag = 0;

-- 修复 status,enable 字段数据
UPDATE `merchant`.t_domain_ty tdty
SET tdty.enable =2
WHERE tdty.enable = 0;

UPDATE `merchant`.t_domain_ty tdty
SET tdty.status = 1
WHERE tdty.enable in (1, 2);

UPDATE `merchant`.t_domain_ty tdty
SET tdty.self_test_tag = 1;

-- ===============迁移数据结束===================

