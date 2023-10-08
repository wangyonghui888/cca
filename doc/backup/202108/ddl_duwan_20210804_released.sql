alter table tybss_new.t_merchant
    add column kana_code varchar (255)  DEFAULT '' COMMENT '商户编码';

alter table tybss_new.t_merchant
    add column kana_code_time DATETIME  DEFAULT NULL COMMENT '商户编码更新时间';