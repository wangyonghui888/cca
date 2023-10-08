ALTER TABLE `merchant`.`merchant_notice`
    ADD COLUMN `multi_terminal_notice_id` bigint(20) NULL COMMENT '其他端的公告表对应的ID主键，用于联动修改和删除用' AFTER `is_shuf`;

INSERT INTO `merchant`.`merchant_notice_type` (`id`, `type_name`, `type_en`) VALUES (31, '英雄联盟赛事', 'LOL');
INSERT INTO `merchant`.`merchant_notice_type` (`id`, `type_name`, `type_en`) VALUES (32, 'Dota2赛事', 'Dota2');
INSERT INTO `merchant`.`merchant_notice_type` (`id`, `type_name`, `type_en`) VALUES (33, '王者荣耀赛事', 'KoG');
INSERT INTO `merchant`.`merchant_notice_type` (`id`, `type_name`, `type_en`) VALUES (34, 'CS:GO赛事', 'CS:GO');

INSERT INTO `merchant`.`merchant_notice_type` (`id`, `type_name`, `type_en`) VALUES (35, 'NBA2K赛事', 'NBA2K');
INSERT INTO `merchant`.`merchant_notice_type` (`id`, `type_name`, `type_en`) VALUES (36, 'FIFA赛事', 'FIFA');