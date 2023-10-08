--  #备注： C组库 merchant 库
UPDATE `merchant`.`m_domain_area` SET `code` = 'GuangDong' WHERE `name` = '广东';
UPDATE `merchant`.`m_domain_area` SET `code` = 'JiangXi' WHERE `name` = '江西';
UPDATE `merchant`.`m_domain_area` SET `code` = 'ZheJiang' WHERE `name` = '浙江';
UPDATE `merchant`.`m_domain_area` SET `code` = 'JiangSu' WHERE `name` = '江苏';
UPDATE `merchant`.`m_domain_area` SET `code` = 'QingHai' WHERE `name` = '青海';
UPDATE `merchant`.`m_domain_area` SET `code` = 'GanSu' WHERE `name` = '甘肃';
UPDATE `merchant`.`m_domain_area` SET `code` = 'BeiJing' WHERE `name` = '北京';
UPDATE `merchant`.`m_domain_area` SET `code` = 'ShangHai' WHERE `name` = '上海';
UPDATE `merchant`.`m_domain_area` SET `code` = 'ChongQing' WHERE `name` = '重庆';
UPDATE `merchant`.`m_domain_area` SET `code` = 'SiChuan' WHERE `name` = '四川';
UPDATE `merchant`.`m_domain_area` SET `code` = 'TianJin' WHERE `name` = '天津';
UPDATE `merchant`.`m_domain_area` SET `code` = 'NeiMengGu' WHERE `name` = '内蒙古';
UPDATE `merchant`.`m_domain_area` SET `code` = 'XinJiang' WHERE `name` = '新疆';
UPDATE `merchant`.`m_domain_area` SET `code` = 'HuBei' WHERE `name` = '湖北';
UPDATE `merchant`.`m_domain_area` SET `code` = 'HuNan' WHERE `name` = '湖南';
UPDATE `merchant`.`m_domain_area` SET `code` = 'FuJian' WHERE `name` = '福建';
UPDATE `merchant`.`m_domain_area` SET `code` = 'YunNan' WHERE `name` = '云南';
UPDATE `merchant`.`m_domain_area` SET `code` = 'XiZang' WHERE `name` = '西藏';
UPDATE `merchant`.`m_domain_area` SET `code` = 'HeNan' WHERE `name` = '河南';
UPDATE `merchant`.`m_domain_area` SET `code` = 'HeBei' WHERE `name` = '河北';
UPDATE `merchant`.`m_domain_area` SET `code` = 'ShanXi' WHERE `name` = '山西';
UPDATE `merchant`.`m_domain_area` SET `code` = 'ShaanXi' WHERE `name` = '陕西';
UPDATE `merchant`.`m_domain_area` SET `code` = 'TaiWan' WHERE `name` = '台湾';
UPDATE `merchant`.`m_domain_area` SET `code` = 'LiaoNing' WHERE `name` = '辽宁';
UPDATE `merchant`.`m_domain_area` SET `code` = 'JiLin' WHERE `name` = '吉林';
UPDATE `merchant`.`m_domain_area` SET `code` = 'HeiLongJiang' WHERE `name` = '黑龙江';
UPDATE `merchant`.`m_domain_area` SET `code` = 'AnHui' WHERE `name` = '安徽';
UPDATE `merchant`.`m_domain_area` SET `code` = 'ShanDong' WHERE `name` = '山东';
UPDATE `merchant`.`m_domain_area` SET `code` = 'HaiNan' WHERE `name` = '海南';
UPDATE `merchant`.`m_domain_area` SET `code` = 'GuiZhou' WHERE `name` = '贵州';
UPDATE `merchant`.`m_domain_area` SET `code` = 'GuangXi' WHERE `name` = '广西';
UPDATE `merchant`.`m_domain_area` SET `code` = 'NingXia' WHERE `name` = '宁夏';
UPDATE `merchant`.`m_domain_area` SET `code` = 'HongKong' WHERE `name` = '香港';
UPDATE `merchant`.`m_domain_area` SET `code` = 'MaCao' WHERE `name` = '澳门';

-- 需求2245 投注用户白名单 CYSB 四组都要同步
UPDATE t_user u SET u.disabled = 2 WHERE merchant_code in (SELECT m.merchant_code FROM t_merchant m WHERE m.is_test = 1);
UPDATE t_user u SET u.disabled = 3 WHERE u.is_test = 2;
-- TODO 测试账号手动导入
UPDATE t_user u SET u.disabled = 4 WHERE uid in ();


-- 需求1889 c组merchant库
INSERT INTO `merchant`.`m_match_info`(`id`, `name`, `tab`, `status`) VALUES (1, 'lol', 'ty', 1);
INSERT INTO `merchant`.`m_match_info`(`id`, `name`, `tab`, `status`) VALUES (2, 'hok', 'ty', 1);
INSERT INTO `merchant`.`m_match_info`(`id`, `name`, `tab`, `status`) VALUES (3, 'dota2', 'ty', 1);
INSERT INTO `merchant`.`m_match_info`(`id`, `name`, `tab`, `status`) VALUES (4, 'csgo', 'ty', 1);
INSERT INTO `merchant`.`m_match_info`(`id`, `name`, `tab`, `status`) VALUES (5, 'fifa', 'dj', 1);
INSERT INTO `merchant`.`m_match_info`(`id`, `name`, `tab`, `status`) VALUES (6, 'nba2k', 'dj', 1);
INSERT INTO `merchant`.`m_match_info`(`id`, `name`, `tab`, `status`) VALUES (7, 'vrfootball', 'dj', 1);



