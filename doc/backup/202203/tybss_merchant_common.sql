ALTER TABLE t_user  modify last_bet_time  bigint(20)  default 0    COMMENT '最近的投注时间	';

CREATE TABLE `t_transfer_record_error` (
                                     `transfer_id` varchar(32) COLLATE utf8mb4_0900_as_cs NOT NULL,
                                     `merchant_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '商户代码',
                                     `user_id` bigint(16) NOT NULL COMMENT '用户ID',
                                     `transfer_type` tinyint(1) NOT NULL COMMENT '1:加款,2扣款',
                                     `biz_type` int(11) DEFAULT NULL COMMENT '1投注(扣款) 2结算派彩(加款)3注单取消(加款) 4 注单取消回滚(扣款) 5结算回滚(扣款) 6 拒单 (加款) 7 转入 8 转出',
                                     `amount` bigint(16) NOT NULL COMMENT '金额',
                                     `before_transfer` bigint(16) DEFAULT NULL COMMENT '转帐前金额',
                                     `after_transfer` bigint(16) DEFAULT NULL COMMENT '转账后余额',
                                     `status` tinyint(1) NOT NULL COMMENT '0:失败,1:成功',
                                     `mag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '原因',
                                     `transfer_mode` tinyint(1) DEFAULT NULL COMMENT '1:免转,2:转账',
                                     `create_time` bigint(16) DEFAULT NULL COMMENT '创建时间',
                                     `orderStr` varchar(1000) COLLATE utf8mb4_0900_as_cs DEFAULT NULL,
                                     `retry_count` int(10) DEFAULT '0' COMMENT '重试次数',
                                     PRIMARY KEY (`transfer_id`) USING BTREE,
                                     KEY `index_record_userid` (`user_id`),
                                     KEY `index_craterTime` (`create_time`),
                                     KEY `index_bizType_craterTime` (`status`,`biz_type`,`create_time`),
                                     KEY `index_merchant_time` (`merchant_code`,`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_as_cs ROW_FORMAT=DYNAMIC;



alter table t_transfer_record_retry
    add COLUMN merchant_code varchar(100) not null COMMENT '商户号';

alter table t_transfer_record_retry
    add COLUMN user_name varchar(64) not null COMMENT '用户名';

alter table t_transfer_record_retry
    add COLUMN biz_type int(11) DEFAULT NULL COMMENT '1投注(扣款) 2结算派彩(加款)3注单取消(加款) 4 注单取消回滚(扣款) 5结算回滚(扣款) 6 拒单 (加款) 7 转入 8 转出';

alter table t_transfer_record_retry
    add COLUMN amount bigint(16) NOT NULL COMMENT '金额';

UPDATE `t_user` set last_bet_time =0 WHERE modify_time > 1646064000000  and modify_time <= now() and  last_bet_time is null;
UPDATE `t_user` set last_bet_time =0 WHERE modify_time > 1645459200000  and modify_time <= 1646064000000 and  last_bet_time is null;
UPDATE `t_user` set last_bet_time =0 WHERE modify_time > 1645200000000  and modify_time <= 1645459200000 and  last_bet_time is null;
UPDATE `t_user` set last_bet_time =0 WHERE modify_time > 1644940800000  and modify_time <= 1645200000000 and  last_bet_time is null;
UPDATE `t_user` set last_bet_time =0 WHERE modify_time > 1644249600000  and modify_time <= 1644940800000 and  last_bet_time is null;
UPDATE `t_user` set last_bet_time =0 WHERE modify_time > 1643644800000  and modify_time <= 1644249600000 and  last_bet_time is null;
UPDATE `t_user` set last_bet_time =0 WHERE modify_time > 1642262400000  and modify_time <= 1643644800000 and  last_bet_time is null;
UPDATE `t_user` set last_bet_time =0 WHERE modify_time > 1641571200000  and modify_time <= 1642262400000 and  last_bet_time is null;
UPDATE `t_user` set last_bet_time =0 WHERE modify_time > 1640966400000  and modify_time <= 1641571200000 and  last_bet_time is null;
UPDATE `t_user` set last_bet_time =0 WHERE modify_time > 1640102400000  and modify_time <= 1640966400000 and  last_bet_time is null;
UPDATE `t_user` set last_bet_time =0 WHERE modify_time > 1639584000000  and modify_time <= 1640102400000 and  last_bet_time is null;
UPDATE `t_user` set last_bet_time =0 WHERE modify_time > 1638892800000  and modify_time <= 1639584000000 and  last_bet_time is null;
UPDATE `t_user` set last_bet_time =0 WHERE modify_time > 1638288000000  and modify_time <= 1638892800000 and  last_bet_time is null;
UPDATE `t_user` set last_bet_time =0 WHERE modify_time > 1637510400000  and modify_time <= 1638288000000 and  last_bet_time is null;
UPDATE `t_user` set last_bet_time =0 WHERE modify_time > 1636905600000  and modify_time <= 1637510400000 and  last_bet_time is null;
UPDATE `t_user` set last_bet_time =0 WHERE modify_time > 1636214400000  and modify_time <= 1636905600000 and  last_bet_time is null;
UPDATE `t_user` set last_bet_time =0 WHERE modify_time > 1635696000000  and modify_time <= 1636214400000 and  last_bet_time is null;
UPDATE `t_user` set last_bet_time =0 WHERE modify_time > 1634227200000  and modify_time <= 1635696000000 and  last_bet_time is null;
UPDATE `t_user` set last_bet_time =0 WHERE modify_time > 1633017600000  and modify_time <= 1634227200000 and  last_bet_time is null;
UPDATE `t_user` set last_bet_time =0 WHERE modify_time > 1631635200000  and modify_time <= 1633017600000 and  last_bet_time is null;
UPDATE `t_user` set last_bet_time =0 WHERE modify_time > 1630425600000  and modify_time <= 1631635200000 and  last_bet_time is null;
UPDATE `t_user` set last_bet_time =0 WHERE modify_time > 1628956800000  and modify_time <= 1630425600000 and  last_bet_time is null;
UPDATE `t_user` set last_bet_time =0 WHERE modify_time > 1627747200000  and modify_time <= 1628956800000 and  last_bet_time is null;
UPDATE `t_user` set last_bet_time =0 WHERE modify_time > 1626278400000  and modify_time <= 1627747200000 and  last_bet_time is null;
UPDATE `t_user` set last_bet_time =0 WHERE modify_time > 1625068800000  and modify_time <= 1626278400000 and  last_bet_time is null;
UPDATE `t_user` set last_bet_time =0 WHERE modify_time > 1624291200000  and modify_time <= 1625068800000 and  last_bet_time is null;
UPDATE `t_user` set last_bet_time =0 WHERE modify_time > 1623686400000  and modify_time <= 1624291200000 and  last_bet_time is null;
UPDATE `t_user` set last_bet_time =0 WHERE modify_time > 1622476800000  and modify_time <= 1623686400000 and  last_bet_time is null;
UPDATE `t_user` set last_bet_time =0 WHERE modify_time > 1619798400000  and modify_time <= 1622476800000 and  last_bet_time is null;
UPDATE `t_user` set last_bet_time =0 WHERE modify_time > 1617206400000  and modify_time <= 1619798400000 and  last_bet_time is null;
UPDATE `t_user` set last_bet_time =0 WHERE modify_time > 1614528000000  and modify_time <= 1617206400000 and  last_bet_time is null;
UPDATE `t_user` set last_bet_time =0 WHERE modify_time > 1612108800000  and modify_time <= 1614528000000 and  last_bet_time is null;
UPDATE `t_user` set last_bet_time =0 WHERE modify_time > 1609430400000  and modify_time <= 1612108800000 and  last_bet_time is null;
UPDATE `t_user` set last_bet_time =0 WHERE modify_time > 1606752000000  and modify_time <= 1609430400000 and  last_bet_time is null;
UPDATE `t_user` set last_bet_time =0 WHERE modify_time > 1604160000000  and modify_time <= 1606752000000 and  last_bet_time is null;
UPDATE `t_user` set last_bet_time =0 WHERE modify_time > 1601481600000  and modify_time <= 1604160000000 and  last_bet_time is null;
UPDATE `t_user` set last_bet_time =0 WHERE modify_time > 1598889600000  and modify_time <= 1601481600000 and  last_bet_time is null;
UPDATE `t_user` set last_bet_time =0 WHERE modify_time > 1596211200000  and modify_time <= 1598889600000 and  last_bet_time is null;
UPDATE `t_user` set last_bet_time =0 WHERE modify_time > 1593532800000  and modify_time <= 1596211200000 and  last_bet_time is null;
UPDATE `t_user` set last_bet_time =0 WHERE modify_time > 1590940800000  and modify_time <= 1593532800000 and  last_bet_time is null;
UPDATE `t_user` set last_bet_time =0 WHERE modify_time > 1588262400000  and modify_time <= 1590940800000 and  last_bet_time is null;
UPDATE `t_user` set last_bet_time =0 WHERE modify_time > 1585670400000  and modify_time <= 1588262400000 and  last_bet_time is null;
UPDATE `t_user` set last_bet_time =0 WHERE modify_time > 1582992000000  and modify_time <= 1585670400000 and  last_bet_time is null;
UPDATE `t_user` set last_bet_time =0 WHERE modify_time > 1580486400000  and modify_time <= 1582992000000 and  last_bet_time is null;
