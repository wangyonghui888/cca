ALTER TABLE `merchant_config`
    ADD COLUMN `reset_password_switch` tinyint(1) DEFAULT '1' COMMENT '商户后台重置密码1开0关';