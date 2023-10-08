alter table t_system_config add create_by varchar(255) default null comment '创建人';
alter table t_system_config add create_time bigint(20) default null comment '创建时间';
alter table t_system_config add update_by varchar(255) default null comment '更新人';
alter table t_system_config add switch_type tinyint(4) default null comment '开关类型';