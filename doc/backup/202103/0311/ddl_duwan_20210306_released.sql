alter table tybss_new.t_merchant_config
    add column load_logo_url  varchar(255) null COMMENT 'pc列表页图片';

alter table tybss_new.t_merchant_config
    add column league_logo_url  varchar(255) null COMMENT '无联赛logo';

alter table tybss_new.t_activity_config
    add column in_start_time   	bigint(20)  null COMMENT '玩家参与开始时间';

alter table tybss_new.t_activity_config
    add column in_end_time   	bigint(20)  null COMMENT '玩家参与结束时间'

alter table tybss_new.t_activity_config
    add column pc_url   	varchar(128) 	  null COMMENT 'PC 地址';

alter table tybss_new.t_activity_config
    add column  	h5_url    varchar(128)  null COMMENT 'H5地址';