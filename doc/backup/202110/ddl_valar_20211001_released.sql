ALTER TABLE tybss_new.t_merchant
    ADD `open_esport` TINYINT NULL DEFAULT NULL COMMENT '电竞开关' AFTER `open_vr_sport`;


CREATE INDEX index_login_time ON tybss_new.t_log_history (login_time);
