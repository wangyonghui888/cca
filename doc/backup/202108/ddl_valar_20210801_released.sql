ALTER TABLE tybss_new.`t_user`
    DROP `phone`;

ALTER TABLE tybss_new.`t_user`
    DROP `id_card`;

ALTER TABLE tybss_new.`t_user`
    DROP `email`;


alter table tybss_new.t_user_level_relation_history
    add column level_name varchar(200) DEFAULT null COMMENT '标签名称' after order_stage_ids_json;

alter table tybss_new.t_user_level_relation_history
    add column type tinyint DEFAULT 1 COMMENT '日志类型(1用户标签 2用户赔率分组,3风险自负)' after level_name;


ALTER TABLE tybss_new.`t_user_level_relation_history`
    DROP INDEX `user_level_uid`;

CREATE INDEX index_user_id ON tybss_new.t_user_level_relation_history (uid);
CREATE INDEX index_create_time ON tybss_new.t_user_level_relation_history (create_time);

