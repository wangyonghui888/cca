#ALTER TABLE tybss_new.`t_account_change_history` ADD `transfer_id` varchar(128) NULL COMMENT '交易ID';

ALTER TABLE tybss_new.`t_account_change_history` DROP INDEX `index_biz_type`;

ALTER TABLE tybss_new.`t_user` DROP INDEX `idx_phone`;


ALTER TABLE tybss_new.`t_user_level_relation` DROP INDEX `user_level_id`;

ALTER TABLE tybss_new.`t_user_level_relation_history` DROP INDEX `user_level_id`;

CREATE INDEX index_account_uid ON tybss_new.t_account_change_history (uid);

ALTER TABLE `tybss_new`.`t_log_history` ADD INDEX `index_uid` (`uid`);
