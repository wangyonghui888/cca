
-- tybss_new
alter table tybss_new.t_aggregate_festival_resource_cfg
    add column `img_8` VARCHAR(255) null COMMENT 'TY：PC轮播1（夜间版）' after img_7_url;

alter table tybss_new.t_aggregate_festival_resource_cfg
    add column `img_8_type` VARCHAR(10) null COMMENT '0：无连接 1：内部导航 2：弹窗连接' after img_8;

alter table tybss_new.t_aggregate_festival_resource_cfg
    add column `img_8_url` VARCHAR(255) null COMMENT 'img8跳转连接' after img_8_type;


alter table tybss_new.t_aggregate_festival_resource_cfg
    add column `img_9` VARCHAR(255) null COMMENT 'TY：PC轮播2（夜间版）' after img_8_url;

alter table tybss_new.t_aggregate_festival_resource_cfg
    add column `img_9_type` VARCHAR(10) null COMMENT '0：无连接 1：内部导航 2：弹窗连接' after img_9;

alter table tybss_new.t_aggregate_festival_resource_cfg
    add column `img_9_url` VARCHAR(255) null COMMENT 'img8跳转连接' after img_9_type;


alter table tybss_new.t_aggregate_festival_resource_cfg
    add column `img_10` VARCHAR(255) null COMMENT 'TY：PC轮播3（夜间版）' after img_9_url;

alter table tybss_new.t_aggregate_festival_resource_cfg
    add column `img_10_type` VARCHAR(10) null COMMENT '0：无连接 1：内部导航 2：弹窗连接' after img_10;

alter table tybss_new.t_aggregate_festival_resource_cfg
    add column `img_10_url` VARCHAR(255) null COMMENT 'img8跳转连接' after img_10_type;

ALTER TABLE tybss_new.t_aggregate_festival_resource_cfg MODIFY COLUMN img_1 VARCHAR(255) COMMENT 'DJ：PC节日资源图 CP：PC节日资源图 TY：PC顶部左侧（日间版）';
ALTER TABLE tybss_new.t_aggregate_festival_resource_cfg MODIFY COLUMN img_2 VARCHAR(255) COMMENT 'DJ：PC推荐位 CP：PC节日资源图 TY：PC顶部左侧（夜间版）';
ALTER TABLE tybss_new.t_aggregate_festival_resource_cfg MODIFY COLUMN img_3 VARCHAR(255) COMMENT 'DJ：PC弹窗资源图 CP：H5节日资源图 TY：H5节日资源图（日间版）';
ALTER TABLE tybss_new.t_aggregate_festival_resource_cfg MODIFY COLUMN img_4 VARCHAR(255) COMMENT 'DJ：H5节日资源图 CP：H5节日资源图 TY：H5节日资源图（夜间版）';
ALTER TABLE tybss_new.t_aggregate_festival_resource_cfg MODIFY COLUMN img_5 VARCHAR(255) COMMENT 'DJ：H5推荐位 CP：H5节日资源图 TY：PC轮播1（日间版）';
ALTER TABLE tybss_new.t_aggregate_festival_resource_cfg MODIFY COLUMN img_6 VARCHAR(255) COMMENT 'DJ：H5弹窗资源图 CP：H5节日资源图 TY：PC轮播2（日间版）';
ALTER TABLE tybss_new.t_aggregate_festival_resource_cfg MODIFY COLUMN img_7 VARCHAR(255) COMMENT 'CP：PC节日资源图 TY：PC轮播3（日间版）';
