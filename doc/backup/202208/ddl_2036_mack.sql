
####备注：C、Y、B、S、业务汇总库均执行
alter table t_merchant_config add video_switch tinyint(4) default 0 comment '视频流量管控开关(0关,1开)';