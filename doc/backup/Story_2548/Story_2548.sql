## 商户后台 merchant 数据库

ALTER TABLE `merchant_config`
    ADD COLUMN `abnormal_click_time` bigint(20) DEFAULT NULL COMMENT '异常用户名单点击时间';