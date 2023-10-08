alter table merchant.t_line_carrier
    add column `tab` varchar(10) DEFAULT NULL COMMENT 'tab :ty dj cp' after `line_carrier_status`;

alter table merchant.t_domain_group
    add column `other_threshold` bigint(10) DEFAULT 0 COMMENT 'other阈值' after `group_type`;
alter table merchant.t_domain_group
    add column `img_threshold` bigint(10) DEFAULT 0 COMMENT 'img阈值' after `group_type`;
alter table merchant.t_domain_group
    add column `api_threshold` bigint(10) DEFAULT 0 COMMENT 'api阈值' after `group_type`;
alter table merchant.t_domain_group
    add column `pc_threshold` bigint(10) DEFAULT 0 COMMENT 'pc阈值' after `group_type`;
alter table merchant.t_domain_group
    add column `h5_threshold` bigint(10) DEFAULT 0 COMMENT 'h5阈值' after `group_type`;


alter table merchant.t_domain_program
    add column `other_push_domain_num` tinyint(10) DEFAULT 1 COMMENT 'other主推数' after `group_type`;
alter table merchant.t_domain_program
    add column `img_push_domain_num` tinyint(10) DEFAULT 1 COMMENT 'img主推数' after `group_type`;
alter table merchant.t_domain_program
    add column `api_push_domain_num` tinyint(10) DEFAULT 1 COMMENT 'api主推数' after `group_type`;
alter table merchant.t_domain_program
    add column `pc_push_domain_num` tinyint(10) DEFAULT 1 COMMENT 'pc主推数' after `group_type`;
alter table merchant.t_domain_program
    add column `h5_push_domain_num` tinyint(10) DEFAULT 1 COMMENT 'h5主推数' after `group_type`;

ALTER TABLE `merchant`.`t_domain_program` DROP COLUMN `push_domain_num`;
ALTER TABLE `merchant`.`t_domain_group` DROP COLUMN `alarm_threshold`;

alter table merchant.t_merchant_group_ty
    add column `tab` varchar(10) DEFAULT 'ty' COMMENT 'tab :ty dj cp' after `program_id`;

