alter table t_activity_config
    add column h5_url varchar(128) DEFAULT NULL COMMENT 'h5活动图片地址' after in_end_time;
alter table t_activity_config
    add column pc_url varchar(128) DEFAULT NULL COMMENT 'pc活动图片地址' after in_end_time;

alter table t_activity_merchant
    add column entrance_status bigint(20) DEFAULT 0 COMMENT '活动入口状态' after status;

alter table t_activity_merchant alter column status set default 0;
