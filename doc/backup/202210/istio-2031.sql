ALTER TABLE `merchant.`multi_terminal_notice`
ADD COLUMN `bi_title` varchar(1000) CHARACTER SET utf8mb4 DEFAULT '' COMMENT '印尼语公告标题' after ma_title;

ALTER TABLE `merchant.`multi_terminal_notice`
ADD COLUMN `bi_context` varchar(1000) CHARACTER SET utf8mb4 DEFAULT '' COMMENT '印尼语公告正文' after ma_context;


ALTER TABLE `merchant.`merchant_notice`
ADD COLUMN `bi_title` varchar(1000) CHARACTER SET utf8mb4 DEFAULT '' COMMENT '印尼语公告标题' after ma_title;

ALTER TABLE `merchant.`merchant_notice`
ADD COLUMN `bi_context` varchar(1000) CHARACTER SET utf8mb4 DEFAULT '' COMMENT '印尼语公告正文' after ma_context;

ALTER TABLE `tybss_new.`t_match_notice`
ADD COLUMN `bi_title` varchar(1000) CHARACTER SET utf8mb4 DEFAULT '' COMMENT '印尼语公告标题' after ma_title;

ALTER TABLE `tybss_new.`t_match_notice`
ADD COLUMN `bi_context` varchar(1000) CHARACTER SET utf8mb4 DEFAULT '' COMMENT '印尼语公告正文' after ma_context;

