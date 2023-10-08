alter table tybss_new.t_user
    add column language_name varchar(10) null COMMENT '用户设置语种' after settled_bet_amount;


alter table tybss_new.t_user
    drop column language_type;