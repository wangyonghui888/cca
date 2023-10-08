## 商户后台 merchant 数据库

ALTER TABLE `merchant_notice`
    ADD COLUMN `ko_title` longtext NULL COMMENT '韩语公告标题' ;
ALTER TABLE `merchant_notice`
    ADD COLUMN `ko_context` longtext NULL COMMENT '韩语公告正文' ;

ALTER TABLE `merchant_notice`
    ADD COLUMN `pt_title` longtext NULL COMMENT '葡萄牙语标题' ;
ALTER TABLE `merchant_notice`
    ADD COLUMN `pt_context` longtext NULL COMMENT '葡萄牙语公告正文' ;

ALTER TABLE `merchant_notice`
    ADD COLUMN `es_title` longtext NULL COMMENT '西班牙语公告标题' ;
ALTER TABLE `merchant_notice`
    ADD COLUMN `es_context` longtext NULL COMMENT '西班牙语公告正文' ;

ALTER TABLE `merchant_notice`
    ADD COLUMN `mya_title` longtext NULL COMMENT '缅甸语公告标题' ;
ALTER TABLE `merchant_notice`
    ADD COLUMN `mya_context` longtext NULL COMMENT '缅甸语公告正文' ;


## 商户后台 merchant 数据库  multi_terminal_notice 电竞公告表

ALTER TABLE `multi_terminal_notice`
    ADD COLUMN `ko_title` longtext NULL COMMENT '韩语公告标题' ;
ALTER TABLE `multi_terminal_notice`
    ADD COLUMN `ko_context` longtext NULL COMMENT '韩语公告正文' ;

ALTER TABLE `multi_terminal_notice`
    ADD COLUMN `pt_title` longtext NULL COMMENT '葡萄牙语标题' ;
ALTER TABLE `multi_terminal_notice`
    ADD COLUMN `pt_context` longtext NULL COMMENT '葡萄牙语公告正文' ;

ALTER TABLE `multi_terminal_notice`
    ADD COLUMN `es_title` longtext NULL COMMENT '西班牙语公告标题' ;
ALTER TABLE `multi_terminal_notice`
    ADD COLUMN `es_context` longtext NULL COMMENT '西班牙语公告正文' ;

ALTER TABLE `multi_terminal_notice`
    ADD COLUMN `mya_title` longtext NULL COMMENT '缅甸语公告标题' ;
ALTER TABLE `multi_terminal_notice`
    ADD COLUMN `mya_context` longtext NULL COMMENT '缅甸语公告正文' ;

## 赛程数据库tybss_new
ALTER TABLE `t_match_notice`
    ADD COLUMN `ko_title` longtext NULL COMMENT '韩语公告标题' ;
ALTER TABLE `t_match_notice`
    ADD COLUMN `ko_context` longtext NULL COMMENT '韩语公告正文' ;

ALTER TABLE `t_match_notice`
    ADD COLUMN `pt_title` longtext NULL COMMENT '葡萄牙语标题' ;
ALTER TABLE `t_match_notice`
    ADD COLUMN `pt_context` longtext NULL COMMENT '葡萄牙语公告正文' ;

ALTER TABLE `t_match_notice`
    ADD COLUMN `es_title` longtext NULL COMMENT '西班牙语公告标题' ;
ALTER TABLE `t_match_notice`
    ADD COLUMN `es_context` longtext NULL COMMENT '西班牙语公告正文' ;

ALTER TABLE `t_match_notice`
    ADD COLUMN `mya_title` longtext NULL COMMENT '缅甸语公告标题' ;
ALTER TABLE `t_match_notice`
    ADD COLUMN `mya_context` longtext NULL COMMENT '缅甸语公告正文' ;