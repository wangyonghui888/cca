Drop table if exists tybss_new.`t_order_internationalize`;

CREATE TABLE tybss_new.`t_order_internationalize`
(
    `id`                 BIGINT auto_increment NOT NULL COMMENT '自动编号',
    `order_no`           varchar(32)           not NULL,
    `bet_no`             varchar(32)           not NULL,
    `tournament_name_en` varchar(255)          NULL COMMENT '联赛英文',
    `tournament_name_zs` varchar(255)          NULL COMMENT '联赛中文',
    `sport_name_en`      varchar(255)          NULL COMMENT '体种英文',
    `sport_name_zs`      varchar(255)          NULL COMMENT '体种中文',
    `play_name_en`       varchar(255)          NULL COMMENT '玩法英文',
    `play_name_zs`       varchar(255)          NULL COMMENT '玩法中文',
    `playoption_en`      varchar(255)          NULL COMMENT '投注项英文',
    `playoption_zs`      varchar(255)          NULL COMMENT '投注项中文',
    `match_info_en`      varchar(255)          NULL COMMENT '赛事对阵英文',
    `match_info_zs`      varchar(255)          NULL COMMENT '赛事对阵中文',
    `match_id`           bigint(20)            NULL COMMENT '赛事',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_as_cs
  ROW_FORMAT = DYNAMIC;

create index index_order_no
    on tybss_new.t_order_internationalize (order_no);
create index index_bet_no
    on tybss_new.t_order_internationalize (bet_no);
create index index_match_id
    on tybss_new.t_order_internationalize (match_id);
create index index_match_info_zs
    on tybss_new.t_order_internationalize (match_info_zs);
create index index_match_info_en
    on tybss_new.t_order_internationalize (match_info_en);


Drop table if exists merchant.`merchant_notice_type`;

CREATE TABLE merchant.`merchant_notice_type`
(
    `id`        int(11) NOT NULL,
    `type_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT '' COMMENT '类型名称',
    `type_en`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT '' COMMENT '类型名称EN'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_as_cs COMMENT ='公告类型';


INSERT INTO merchant.`merchant_notice_type` (`id`, `type_name`, `type_en`)
VALUES (1, '普通公告', 'NORMAL'),
       (2, '商务公告', 'BUSSINESS'),
       (3, '维护公告', 'MAINTENANCE'),
       (4, '足球赛事', 'SOCCER'),
       (5, '篮球赛事', 'BASKETBALL'),
       (6, '网球赛事', 'TENNIS'),
       (7, '羽毛球赛事', 'BADMINTON'),
       (8, '乒乓球赛事', 'TABLE TENNIS'),
       (9, '斯诺克赛事', 'SNOOKER'),
       (10, '活动公告', 'ANNOUNCEMENT'),
       (11, '冰球赛事', 'ICEHOCKEY'),
       (12, '棒球赛事', 'BASEBALL'),
       (13, '排球赛事', 'VOLLEYBALL'),
       (14, '美式足球赛事', 'FOOTBALL'),
       (15, '政治娱乐赛事', 'POLITIC&ENTERTAINMENT');


alter table merchant.admin_menu
    add column name_en varchar(255) DEFAULT NULL COMMENT '英文菜单' after name;

UPDATE merchant.`admin_menu`
SET `name_en` = 'Home'
WHERE `admin_menu`.`id` = 1;
UPDATE merchant.`admin_menu`
SET `name_en` = 'UserCenter'
WHERE `admin_menu`.`id` = 2;
UPDATE merchant.`admin_menu`
SET `name_en` = 'MerchantInfo'
WHERE `admin_menu`.`id` = 3;
UPDATE merchant.`admin_menu`
SET `name_en` = 'ChildrenManage'
WHERE `admin_menu`.`id` = 4;
UPDATE merchant.`admin_menu`
SET `name_en` = 'My Key'
WHERE `admin_menu`.`id` = 5;
UPDATE merchant.`admin_menu`
SET `name_en` = 'Player Center'
WHERE `admin_menu`.`id` = 6;
UPDATE merchant.`admin_menu`
SET `name_en` = 'Authorization'
WHERE `admin_menu`.`id` = 7;
UPDATE merchant.`admin_menu`
SET `name_en` = 'User Manage'
WHERE `admin_menu`.`id` = 8;
UPDATE merchant.`admin_menu`
SET `name_en` = 'Role Manage'
WHERE `admin_menu`.`id` = 9;
UPDATE merchant.`admin_menu`
SET `name_en` = 'Finance Report'
WHERE `admin_menu`.`id` = 10;
UPDATE merchant.`admin_menu`
SET `name_en` = 'Bill'
WHERE `admin_menu`.`id` = 11;
UPDATE merchant.`admin_menu`
SET `name_en` = 'Finance Bill'
WHERE `admin_menu`.`id` = 12;
UPDATE merchant.`admin_menu`
SET `name_en` = 'Report Center'
WHERE `admin_menu`.`id` = 13;
UPDATE merchant.`admin_menu`
SET `name_en` = 'Tickets Center'
WHERE `admin_menu`.`id` = 14;
UPDATE merchant.`admin_menu`
SET `name_en` = 'Merchant Report'
WHERE `admin_menu`.`id` = 15;
UPDATE merchant.`admin_menu`
SET `name_en` = 'Player Report'
WHERE `admin_menu`.`id` = 16;
UPDATE merchant.`admin_menu`
SET `name_en` = 'Message Center'
WHERE `admin_menu`.`id` = 17;
UPDATE merchant.`admin_menu`
SET `name_en` = 'Billboard'
WHERE `admin_menu`.`id` = 18;
UPDATE merchant.`admin_menu`
SET `name_en` = 'My Message'
WHERE `admin_menu`.`id` = 19;
UPDATE merchant.`admin_menu`
SET `name_en` = 'Match Report'
WHERE `admin_menu`.`id` = 23;
UPDATE merchant.`admin_menu`
SET `name_en` = 'Operate Log'
WHERE `admin_menu`.`id` = 24;
UPDATE merchant.`admin_menu`
SET `name_en` = 'Transaction Record'
WHERE `admin_menu`.`id` = 25;
UPDATE merchant.`admin_menu`
SET `name_en` = 'Agent Manage'
WHERE `admin_menu`.`id` = 26;
UPDATE merchant.`admin_menu`
SET `name_en` = 'Task Center'
WHERE `admin_menu`.`id` = 27;
UPDATE merchant.`admin_menu`
SET `name_en` = 'My Task'
WHERE `admin_menu`.`id` = 28;


SELECT u.username,
       u.fake_name,
       u.user_level,
       u.currency_code,
       o.uid,
       o.create_time,
       o.id,
       o.remark,
       ROUND(o.order_amount_total / 100, 2)          order_amount_total,
       ROUND(o.original_order_amount_total / 100, 2) original_order_amount_total,
       ROUND(o.pre_bet_amount / 100, 2)              pre_bet_amount,
       ROUND(o.product_amount_total / 100, 2)        product_amount_total,
       o.product_count,
       o.series_type,
       o.series_value,
       o.manager_code,
       o.ip,
       o.ip_area,
       o.device_type,
       o.device_imei,
       s.out_come,
       o.order_status,
       ROUND(s.settle_amount / 100, 2)               settle_amount,
       ROUND(s.profit_amount / 100, 2)               profit_amount,
       ROUND(s.original_profit_amount / 100, 2)      original_profit_amount,
       ROUND(s.original_settle_amount / 100, 2)      original_settle_amount,
       s.settle_time,
       s.settle_type,
       m.merchant_name,
       m.merchant_code,
       m.transfer_mode,
       od.create_user,
       od.bet_no,
       od.sport_id,
       od.play_id,
       (case
            when i.order_no is not null then
                (case when 'en' = 'en' then i.sport_name_en else i.sport_name_zs end)
            else od.sport_name end)                  sport_name,
       od.match_type,
       od.market_type,
       od.play_options,
       ROUND(od.bet_amount / 100, 2)                 bet_amount,
       ROUND(od.original_bet_amount / 100, 2)        original_bet_amount,
       od.market_value,
       od.play_name                                  original_play,
       (case
            when i.order_no is not null then
                (case when 'en' = 'en' then i.play_name_en else i.play_name_zs end)
            else od.play_name end)                   play_name,
       od.play_options_id,
       od.match_id,
       (case
            when i.order_no is not null then
                (case when 'en' = 'en' then i.tournament_name_en else i.tournament_name_zs end)
            else od.match_name end)                  tournament_name,
       od.match_name,
       (case
            when i.order_no is not null then
                (case when 'en' = 'en' then i.match_info_en else i.match_info_zs end)
            else od.match_info end)                  match_info,
       od.odds_value / 100000                        odds_value,
       od.odd_finally,
       od.order_no,
       (case
            when i.order_no is not null then
                (case when 'en' = 'en' then i.playoption_en else i.playoption_zs end)
            else od.play_option_name end)            play_option_name,
       od.bet_result,
       od.bet_status,
       od.remark                                     od_remark,
       od.cancel_type,
       od.settle_score,
       od.trade_type,
       od.risk_event,
       (CASE
            WHEN od.sport_id in (1, 2) and od.match_type = 2
                THEN od.score_benchmark END)         score_benchmark,
       (CASE
            WHEN o.manager_code != 3 THEN (SELECT CONCAT(@a := ifNULL(t2.begin_time, 0), @b := 0,
                                                         @c := ifNULL(t2.score, '0'),
                                                         @d := ifNULL(t2.first_num, 0),
                                                         @e := ifNULL(t2.second_num, 0),
                                                         @f := ifNULL(sl1.zs, '0'),
                                                         @g := ifNULL(sl2.zs, '0'), @h := 1)
                                           from s_match_info t2
                                                    left join s_language sl1 on sl1.name_code = t2.home_name_code
                                                    left join s_language sl2 on sl2.name_code = t2.away_name_code
                                           where od.match_id = t2.id)
            WHEN o.manager_code = 3 THEN (select CONCAT(@a := ifNULL(t1.begin_time, 0), @b := ifNULL(t1.phase, '0'),
                                                        @c := ifNULL(t1.score_result, '0'),
                                                        @d := ifNULL(t1.leg_order, '0'),
                                                        @e := ifNULL(t1.match_day, '0'), @f := ifNULL(sl1.zs, '0'),
                                                        @g := ifNULL(sl2.zs, '0'), @h := ifNULL(t1.batch_no, 0))
                                          from s_virtual_match_info t1
                                                   left join s_language sl1 on sl1.name_code = t1.home_name_code
                                                   left join s_language sl2 on sl2.name_code = t1.away_name_code
                                          where t1.id = od.match_id) end)
           as                                        temp,
       @a  as                                        begin_time,
       @b  as                                        phase,
       @c  as                                        score,
       @d  as                                        leg_order,
       @e  as                                        match_day,
       @f  as                                        home_name,
       @g  as                                        away_name,
       @h  as                                        batch_no
FROM `t_order_detail` od
         RIGHT JOIN (SELECT o1.id,
                            o1.uid,
                            o1.order_no,
                            o1.create_time,
                            o1.order_amount_total,
                            o1.pre_bet_amount,
                            o1.original_order_amount_total,
                            o1.product_amount_total,
                            o1.product_count,
                            o1.series_type,
                            o1.series_value,
                            o1.ip,
                            o1.ip_area,
                            o1.device_type,
                            o1.device_imei,
                            o1.currency_code,
                            o1.remark,
                            o1.order_status,
                            o1.manager_code
                     FROM `t_order` o1
                     where o1.create_time >= 1620705600000

                       AND o1.create_time < 1620878399999

                       and o1.uid in (select tu.uid
                                      from t_user tu
                                      where tu.merchant_code in
                                            ('732655', '186692', '011541', '352942',
                                             '602625', '618513', '912697', '659806',
                                             '033011', '497149', '976523', '819596',
                                             '547945', '296483', '676233', '307404',
                                             '074886', '880621', '926716', '677557',
                                             '723003', '025453', '461119', '263889',
                                             '078138', '744301', '446701', '235298',
                                             '644407', '541584', '442254', '812774',
                                             '526016', '962966'))
                     ORDER BY o1.id DESC
                     LIMIT 0,20) o ON o.order_no = od.order_no
         LEFT JOIN t_settle s ON s.order_no = o.order_no and s.last_settle = 1
         LEFT JOIN t_user u ON u.uid = od.uid
         left join t_order_internationalize i on od.bet_no = i.bet_no and od.order_no = i.order_no
         LEFT JOIN t_merchant m ON u.merchant_code = m.merchant_code;


####settle
SELECT u.username,
       u.fake_name,
       u.user_level,
       u.currency_code,
       s.uid,
       o.remark,
       s.order_no,
       s.id,
       ROUND(o.order_amount_total / 100, 2)          order_amount_total,
       ROUND(o.pre_bet_amount / 100, 2)              pre_bet_amount,
       ROUND(o.original_order_amount_total / 100, 2) original_order_amount_total,
       ROUND(o.product_amount_total / 100, 2)        product_amount_total,
       o.product_count,
       o.series_type,
       o.series_value,
       o.ip,
       o.ip_area,
       o.device_type,
       o.device_imei,
       o.order_status,
       o.manager_code,
       s.out_come,
       s.settle_time,
       s.settle_type,
       ROUND(s.settle_amount / 100, 2)               settle_amount,
       ROUND(s.profit_amount / 100, 2)               profit_amount,
       ROUND(s.original_profit_amount / 100, 2)      original_profit_amount,
       ROUND(s.original_settle_amount / 100, 2)      original_settle_amount,
       m.merchant_name,
       m.merchant_code,
       m.transfer_mode,
       od.create_time,
       od.create_user,
       od.bet_no,
       od.sport_id,
       od.play_id,
       (case
            when i.order_no is not null then
                (case when 'en' = 'en' then i.sport_name_en else i.sport_name_zs end)
            else od.sport_name end)                  sport_name,
       od.match_type,
       od.market_type,
       od.play_options,
       ROUND(od.bet_amount / 100, 2)                 bet_amount,
       ROUND(od.original_bet_amount / 100, 2)        original_bet_amount,
       od.market_value,
       od.play_name                                  original_play,
       (case
            when i.order_no is not null then
                (case when 'en' = 'en' then i.play_name_en else i.play_name_zs end)
            else od.play_name end)                   play_name,
       od.play_options_id,
       od.match_id,
       (case
            when i.order_no is not null then
                (case when 'en' = 'en' then i.tournament_name_en else i.tournament_name_zs end)
            else od.match_name end)                  tournament_name,
       (case
            when i.order_no is not null then
                (case when 'en' = 'en' then i.match_info_en else i.match_info_zs end)
            else od.match_info end)                  match_info,
       od.match_name,
       od.odds_value / 100000                        odds_value,
       od.odd_finally,
       (case
            when i.order_no is not null then
                (case when 'en' = 'en' then i.playoption_en else i.playoption_zs end)
            else od.play_option_name end)            play_option_name,
       od.bet_result,
       od.bet_status,
       od.remark                                     od_remark,
       od.cancel_type,
       od.settle_score,
       od.trade_type,
       od.risk_event,
       (CASE
            WHEN od.sport_id in (1, 2) and od.match_type = 2
                THEN od.score_benchmark END)         score_benchmark,
       (CASE
            WHEN o.manager_code != 3 THEN (SELECT CONCAT(@a := ifNULL(t2.begin_time, 0), @b := 0,
                                                         @c := ifNULL(t2.score, '0'),
                                                         @d := ifNULL(t2.first_num, 0),
                                                         @e := ifNULL(t2.second_num, 0), @f := ifNULL(sl1.zs, '0'),
                                                         @g := ifNULL(sl2.zs, '0'), @h := 1)
                                           from s_match_info t2
                                                    left join s_language sl1 on sl1.name_code = t2.home_name_code
                                                    left join s_language sl2 on sl2.name_code = t2.away_name_code
                                           where od.match_id = t2.id)
            WHEN o.manager_code = 3 THEN (select CONCAT(@a := ifNULL(t1.begin_time, 0), @b := ifNULL(t1.phase, '0'),
                                                        @c := ifNULL(t1.score_result, '0'),
                                                        @d := ifNULL(t1.leg_order, 999),
                                                        @e := ifNULL(t1.match_day, '0'), @f := ifNULL(sl1.zs, '0'),
                                                        @g := ifNULL(sl2.zs, '0'), @h := ifNULL(t1.batch_no, 0))
                                          from s_virtual_match_info t1
                                                   left join s_language sl1 on sl1.name_code = t1.home_name_code
                                                   left join s_language sl2 on sl2.name_code = t1.away_name_code
                                          where t1.id = od.match_id) end)
           as                                        temp,
       @a  as                                        begin_time,
       @b  as                                        phase,
       @c  as                                        score,
       @d  as                                        leg_order,
       @e  as                                        match_day,
       @f  as                                        home_name,
       @g  as                                        away_name,
       @h  as                                        batch_no
FROM `t_order_detail` od
         RIGHT JOIN (SELECT s1.id,
                            s1.uid,
                            s1.order_no,
                            s1.create_time,
                            s1.bet_amount,
                            s1.out_come,
                            s1.settle_time,
                            s1.settle_amount,
                            s1.original_settle_amount,
                            s1.profit_amount,
                            s1.original_profit_amount,
                            s1.settle_type
                     FROM `t_settle` s1
                     where s1.last_settle = 1
                       and s1.bet_amount > 0
                       and s1.out_come in (2, 3, 4, 5, 6)
                       AND s1.create_time >= 1620705600000
                       AND s1.create_time <= 1620878399999

                       and s1.uid in (select tu.uid
                                      from t_user tu
                                      where tu.merchant_code in
                                            ('732655', '186692', '011541', '352942',
                                             '602625', '618513', '912697', '659806',
                                             '033011', '497149', '976523', '819596',
                                             '547945', '296483', '676233', '307404',
                                             '074886', '880621', '926716', '677557',
                                             '723003', '025453', '461119', '263889',
                                             '078138', '744301', '446701', '235298',
                                             '644407', '541584', '442254', '812774',
                                             '526016', '962966'))

                     ORDER BY s1.id desc
                     LIMIT 0,20) s ON s.order_no = od.order_no
         left join t_order o on s.order_no = o.order_no
         left join t_order_internationalize i on od.bet_no = i.bet_no and od.order_no = i.order_no
         LEFT JOIN t_user u ON u.uid = od.uid
         LEFT JOIN t_merchant m ON u.merchant_code = m.merchant_code

create index index_merchant
    on tybss_new.t_user (merchant_code);