alter table tybss_new.t_domain
    add column merchant_group_id bigint(20)  DEFAULT 0 COMMENT '商户分组id';

alter table tybss_new.t_domain
    MODIFY COLUMN  `enable` tinyint(4) DEFAULT 0 COMMENT '0 未使用 1已使用 2待使用 3被攻击 4被劫持';