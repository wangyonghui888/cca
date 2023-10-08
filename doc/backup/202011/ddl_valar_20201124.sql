####
ALTER TABLE tybss_new.`t_order`
    DROP INDEX `uid_order_no`;

ALTER TABLE tybss_new.`t_merchant` DROP `fee_json`;


####
ALTER TABLE tybss_new.t_order
    ADD INDEX `index_uid` (`uid`, `order_status`) USING BTREE COMMENT '用户索引';


alter table tybss_new.t_merchant add column ftp_user varchar(100)    COMMENT 'FTP用户名';
alter table tybss_new.t_merchant add column ftp_password varchar(100)    COMMENT 'FTP密码';


