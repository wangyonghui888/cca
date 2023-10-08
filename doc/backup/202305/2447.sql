alter table t_user_level_relation
    add column secondary_label_json text default null COMMENT '二级标签--二级用户标签';

alter table t_user_level_relation
    add column secondary_label_ids_json text default null COMMENT '二级标签-二级用户标签';


alter table t_user_level_relation_history
    add column secondary_label_json text default null COMMENT '二级标签--二级用户标签';

alter table t_user_level_relation_history
    add column secondary_label_ids_json text default null COMMENT '二级标签-二级用户标签';