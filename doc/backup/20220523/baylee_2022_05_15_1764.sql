alter table t_aggregate_festival_resource_cfg
    add column `img_1_type` varchar(10) NOT NULL DEFAULT '0' COMMENT '0：无连接\r\n1：内部导航\r\n2：弹窗连接' after img_1;
alter table t_aggregate_festival_resource_cfg
    add column `img_1_url` varchar(255) DEFAULT '' COMMENT 'img1跳转连接' after img_1_type;

alter table t_aggregate_festival_resource_cfg
    add column `img_2_type` varchar(10) NOT NULL DEFAULT '0' COMMENT '0：无连接\r\n1：内部导航\r\n2：弹窗连接' after img_2;
alter table t_aggregate_festival_resource_cfg
    add column `img_2_url` varchar(255) DEFAULT '' COMMENT 'img2跳转连接' after img_2_type;

alter table t_aggregate_festival_resource_cfg
    add column `img_3_type` varchar(10) NOT NULL DEFAULT '0' COMMENT '0：无连接\r\n1：内部导航\r\n2：弹窗连接' after img_3;
alter table t_aggregate_festival_resource_cfg
    add column `img_3_url` varchar(255) DEFAULT '' COMMENT 'img3跳转连接' after img_3_type;

alter table t_aggregate_festival_resource_cfg
    add column `img_4_type` varchar(10) NOT NULL DEFAULT '0' COMMENT '0：无连接\r\n1：内部导航\r\n2：弹窗连接' after img_4;
alter table t_aggregate_festival_resource_cfg
    add column `img_4_url` varchar(255) DEFAULT '' COMMENT 'img4跳转连接' after img_4_type;

alter table t_aggregate_festival_resource_cfg
    add column `img_5_type` varchar(10) NOT NULL DEFAULT '0' COMMENT '0：无连接\r\n1：内部导航\r\n2：弹窗连接' after img_5;
alter table t_aggregate_festival_resource_cfg
    add column `img_5_url` varchar(255) DEFAULT '' COMMENT 'img5跳转连接' after img_5_type;

alter table t_aggregate_festival_resource_cfg
    add column `img_6_type` varchar(10) NOT NULL DEFAULT '0' COMMENT '0：无连接\r\n1：内部导航\r\n2：弹窗连接' after img_6;
alter table t_aggregate_festival_resource_cfg
    add column `img_6_url` varchar(255) DEFAULT '' COMMENT 'img6跳转连接' after img_6_type;

alter table t_aggregate_festival_resource_cfg
    add column `img_7_type` varchar(10) NOT NULL DEFAULT '0' COMMENT '0：无连接\r\n1：内部导航\r\n2：弹窗连接' after img_7;
alter table t_aggregate_festival_resource_cfg
    add column `img_7_url` varchar(255) DEFAULT '' COMMENT 'img7跳转连接' after img_7_type;