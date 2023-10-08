alter table tybss_merchant_common.t_merchant_config add column `is_app` tinyint(1)  DEFAULT 0 COMMENT '是否对接原生APP(0：否，1：是)';


ALTER TABLE `merchant`.`merchant_notice`
ADD COLUMN `th_context` longtext NULL DEFAULT null COMMENT '正文-泰语' AFTER `vi_context`,
ADD COLUMN `ma_context` longtext NULL DEFAULT null COMMENT '正文-马来语' AFTER `vi_context`,
ADD COLUMN `th_title`	varchar(1000)	 NULL DEFAULT null COMMENT '泰语公告标题' AFTER `vi_title`,
ADD COLUMN `ma_title` 	varchar(1000)	 NULL DEFAULT null COMMENT '马来语公告标题' AFTER `vi_title`;


ALTER TABLE `merchant`.`multi_terminal_notice`
ADD COLUMN `th_context` longtext NULL DEFAULT null COMMENT '正文-泰语' AFTER `vi_context`,
ADD COLUMN `ma_context` longtext NULL DEFAULT null COMMENT '正文-马来语' AFTER `vi_context`,
ADD COLUMN `th_title`	varchar(1000)	 NULL DEFAULT null COMMENT '泰语公告标题' AFTER `vi_title`,
ADD COLUMN `ma_title` 	varchar(1000)	 NULL DEFAULT null COMMENT '马来语公告标题' AFTER `vi_title`;



ALTER TABLE `tybss_new`.`t_match_notice`
ADD COLUMN `th_context` longtext NULL DEFAULT null COMMENT '正文-泰语' AFTER `vi_context`,
ADD COLUMN `ma_context` longtext NULL DEFAULT null COMMENT '正文-马来语' AFTER `vi_context`,
ADD COLUMN `th_title`	varchar(1000)	 NULL DEFAULT null COMMENT '泰语公告标题' AFTER `vi_title`,
ADD COLUMN `ma_title` 	varchar(1000)	 NULL DEFAULT null COMMENT '马来语公告标题' AFTER `vi_title`;

