INSERT INTO tybss_new.`t_activity_config` (`id`, `name`, `type`, `terminal`, `start_time`, `end_time`, `time_limit`,
                                           `ip_limit`, `sport_id`, `reward_type`, `total_cost`, `reward_percentage`,
                                           `reward_guy`,
                                           `single_day_max`, `single_user_max`, `user_partition_times`, `auto_check`,
                                           `partition_rule`, `reward_rule`, `settle_cycle`, `status`, `in_start_time`,
                                           `in_end_time`)
VALUES ('10006', '爱游戏活动', '3', 'H5,PC', '1628352000000', '1630339199000', NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', '1628352000000', '1630260000000');


INSERT INTO tybss_new.`t_activity_merchant` (`activity_id`, `merchant_code`, `status`)
VALUES (10006, 'oubao', 1);

INSERT INTO tybss_new.`t_activity_merchant` (`activity_id`, `merchant_code`, `status`)
VALUES (10006, '976523', 1);