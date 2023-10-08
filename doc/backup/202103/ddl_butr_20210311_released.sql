
alter table tybss_new.t_user
    add column finance_tag bigint(20) default 0 COMMENT '财务标签';

alter table tybss_new.t_user_level_relation
    add column level_name varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs  DEFAULT NULL COMMENT '标签名称';

alter table tybss_new.t_user_level_relation_history
       add column level_name varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs  DEFAULT NULL COMMENT '标签名称';

