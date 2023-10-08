
####备注：C、Y、B、S、业务汇总库均执行
alter table t_merchant_config add closed_without_operation int(5) default 0 comment '长时间未操作关闭视频(0关,1开)';
alter table t_merchant_config add video_settings int(5) default 0 comment '视频设置(0默认,1自定义)';
alter table t_merchant_config add viewing_time bigint(20) default 15 comment '默认视频观看时长(15分钟)';
alter table t_merchant_config add custom_viewing_time bigint(20) default null comment '自定义视频观看时长(5~120分钟)';
alter table t_merchant_config add no_background_play int(5) default 0 comment '不可背景播放(0关,1开)';