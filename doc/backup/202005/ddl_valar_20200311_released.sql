
alter table tybss_new.t_merchant drop bonus_mode;
alter table tybss_new.t_merchant MODIFY transfer_mode tinyint(2) null;
alter table tybss_new.t_merchant MODIFY level tinyint(2) null;

alter table tybss_new.t_merchant add direct_sale  tinyint(1) null COMMENT '是否直营权限(1:是,2否)';
alter table tybss_new.t_merchant add child_connect_mode  tinyint(1) null COMMENT '下级商户对接模式(1:对接panda,2:对接渠道)';
alter table tybss_new.t_merchant add child_max_amount  int null COMMENT '下级商户最大数量';
alter table tybss_new.t_merchant add  commerce  VARCHAR(100);

alter table merchant.user_order_all add first_bet_date date DEFAULT null;

CREATE UNIQUE INDEX index_merchant_code ON tybss_new.t_merchant (merchant_code);


