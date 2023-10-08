alter table tybss_new.t_user
    add column special_betting_limit_type  int(1) NOT NULL DEFAULT 1 COMMENT '  1无  2特殊百分比限额 3特殊单注单场限额 4特殊vip限额' after user_market_prefer;

alter table tybss_new.t_user
    add column special_betting_limit_time  bigint(20) null COMMENT '设置限额时间' after special_betting_limit_type;

alter table tybss_new.t_user
    add column special_betting_limit_delay_time  int(5) null COMMENT '设置限额延时，单位为s' after special_betting_limit_time;

alter table tybss_new.t_user
    add column special_betting_limit_remark  varchar(1000) null COMMENT '设置限额备注' after special_betting_limit_delay_time;

CREATE TABLE tybss_new.rcs_user_special_bet_limit_config (
                                                     `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
                                                     `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
                                                     `special_betting_limit_type` int(4) DEFAULT NULL COMMENT '所属的特殊限额的种类   1无  2特殊百分比限额 3特殊单注单场限额 4特殊vip限额',
                                                     `order_type` int(4) DEFAULT NULL COMMENT '订单类型  1单关  2串关',
                                                     `sport_id` int(11) DEFAULT NULL COMMENT '体育种类  0其他   -1全部',
                                                     `single_note_claim_limit` bigint(20) DEFAULT NULL COMMENT '单注赔付限额',
                                                     `single_game_claim_limit` bigint(20) DEFAULT NULL COMMENT '单场赔付限额',
                                                     `percentage_limit` decimal(8,4) DEFAULT NULL COMMENT '百分比限额数据',
                                                     `status` int(4) DEFAULT NULL COMMENT '0 无效  1有效',
                                                     `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                                     `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                                     PRIMARY KEY (`id`),
                                                     UNIQUE KEY `user_id_order_type_sport_id` (`user_id`,`special_betting_limit_type`,`order_type`,`sport_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_as_cs COMMENT='用户投注特殊限额配置';