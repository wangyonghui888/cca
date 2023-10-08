ALTER TABLE tybss_new.`t_transfer_record` ADD INDEX index_crater_time_type ( `create_time`,`biz_type` );
ALTER TABLE tybss_new.`t_account_change_history` ADD INDEX index_crater_time_type ( `create_time`,`biz_type` );
ALTER TABLE tybss_new.`t_account_change_history` ADD INDEX index_order_id ( `order_no`);