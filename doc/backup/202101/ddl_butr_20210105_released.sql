alter table tybss_new.t_user_level_relation
    modify column remark varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL;
alter table tybss_new.t_user_level_relation_history
    modify column remark varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL;

alter table tybss_new.t_user_level_relation
    add column sport_json text default null COMMENT '二级标签--赛种集合';

alter table tybss_new.t_user_level_relation
    add column tournament_json text default null COMMENT '二级标签-联赛集合';

alter table tybss_new.t_user_level_relation
    add column order_type_json text default null COMMENT '二级标签-投注类型集合';

alter table tybss_new.t_user_level_relation
    add column play_json text default null COMMENT '二级标签-玩法集合';

alter table tybss_new.t_user_level_relation
    add column order_stage_json text default null COMMENT '二级标签-投注阶段集合';


alter table tybss_new.t_user_level_relation
    add column sport_ids_json text default null COMMENT '二级标签--赛种集合';

alter table tybss_new.t_user_level_relation
    add column tournament_ids_json text default null COMMENT '二级标签-联赛集合';

alter table tybss_new.t_user_level_relation
    add column order_type_ids_json text default null COMMENT '二级标签-投注类型集合';

alter table tybss_new.t_user_level_relation
    add column play_ids_json text default null COMMENT '二级标签-玩法集合';

alter table tybss_new.t_user_level_relation
    add column order_stage_ids_json text default null COMMENT '二级标签-投注阶段集合';


alter table tybss_new.t_user_level_relation_history
    add column sport_json text default null COMMENT '二级标签--赛种集合';

alter table tybss_new.t_user_level_relation_history
    add column tournament_json text default null COMMENT '二级标签-联赛集合';

alter table tybss_new.t_user_level_relation_history
    add column order_type_json text default null COMMENT '二级标签-投注类型集合';

alter table tybss_new.t_user_level_relation_history
    add column play_json text default null COMMENT '二级标签-玩法集合';

alter table tybss_new.t_user_level_relation_history
    add column order_stage_json text default null COMMENT '二级标签-投注阶段集合';


alter table tybss_new.t_user_level_relation_history
    add column sport_ids_json text default null COMMENT '二级标签--赛种集合';

alter table tybss_new.t_user_level_relation_history
    add column tournament_ids_json text default null COMMENT '二级标签-联赛集合';

alter table tybss_new.t_user_level_relation_history
    add column order_type_ids_json text default null COMMENT '二级标签-投注类型集合';

alter table tybss_new.t_user_level_relation_history
    add column play_ids_json text default null COMMENT '二级标签-玩法集合';

alter table tybss_new.t_user_level_relation_history
    add column order_stage_ids_json text default null COMMENT '二级标签-投注阶段集合';


alter table tybss_new.t_transfer_record
    add COLUMN retry_count int(10) DEFAULT 0 COMMENT '重试次数';

CREATE TABLE tybss_new.`t_transfer_record_retry`
(
    `id`          bigint(64) NOT NULL,
    `transfer_id` bigint(64) NOT NULL,
    `status`      tinyint(1) NOT NULL COMMENT '0:失败,1:成功',
    `mag`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '原因',
    `create_time` bigint(16)                                                    DEFAULT NULL COMMENT '创建时间',
    `create_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '执行人',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `index_retry_transfer` (`transfer_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_as_cs
  ROW_FORMAT = DYNAMIC;