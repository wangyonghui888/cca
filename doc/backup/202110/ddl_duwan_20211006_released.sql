
alter table tybss_new.t_operations_banner_set
    add column show_num bigint(4)  DEFAULT 1 COMMENT '展示次数';

alter table tybss_new.t_operations_banner_set
    add column show_login_tag bigint(4)  DEFAULT 1 COMMENT '1是登录展示 2活动时间内弹出';