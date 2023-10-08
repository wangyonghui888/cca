delete
from tybss_new.t_activity_merchant;
INSERT INTO `t_activity_merchant` (`id`, `activity_id`, `merchant_code`, `status`)
VALUES (1, 10000, 'oubao', '1');


delete
from tybss_new.t_activity_config;
INSERT INTO tybss_new.`t_activity_config` (`id`, `name`, `type`, `terminal`, `start_time`, `end_time`, `time_limit`,
                                           `ip_limit`, `sport_id`, `reward_type`, `total_cost`, `reward_percentage`,
                                           `reward_guy`, `single_day_max`, `single_user_max`, `user_partition_times`,
                                           `auto_check`, `partition_rule`, `reward_rule`, `settle_cycle`, `status`)
VALUES (10000, '特殊活动202104', 3, 'PC,H5', 1618329600000, 1649865600000, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
        NULL, NULL, NULL, NULL, NULL, NULL, '1');



alter table t_activity_merchant
    modify id int auto_increment;



delete
from t_activity_merchant
where merchant_code = 'oubao'
  and id = 110;
delete
from tybss_new.t_activity_merchant;
INSERT INTO `t_activity_merchant` (`id`, `activity_id`, `merchant_code`, `status`)
VALUES (1, 10000, 'oubao', '1');
INSERT INTO `t_activity_merchant` (`id`, `activity_id`, `merchant_code`, `status`)
VALUES (1, 10000, 'oubao', '1');
INSERT INTO `t_activity_merchant` (`id`, `activity_id`, `merchant_code`, `status`)
VALUES (1, 10000, 'oubao', '1');
INSERT INTO `t_activity_merchant` (`id`, `activity_id`, `merchant_code`, `status`)
VALUES (1, 10000, 'oubao', '1');
INSERT INTO `t_activity_merchant` (`id`, `activity_id`, `merchant_code`, `status`)
VALUES (1, 10000, 'oubao', '1');

INSERT INTO tybss_new.t_activity_merchant (activity_id, merchant_code, status)
    (select 10001, merchant_code, 1 from t_merchant_config where merchant_tag != 1);

alter table tybss_new.t_activity_merchant change id id bigint AUTO_INCREMENT;



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
       (case
            when i.order_no is not null then
                (case when ${language} = 'en' then i.sport_name_en else i.sport_name_zs end)
            else od.sport_name end)                  sport_name,
       od.play_id,
       od.match_type,
       od.market_type,
       od.play_options,
       ROUND(od.bet_amount / 100, 2)                 bet_amount,
       od.market_value,
       od.play_name                                  original_play,
       (case
            when i.order_no is not null then
                (case when ${language} = 'en' then i.play_name_en else i.play_name_zs end)
            else od.play_name end)                   play_name,

       od.play_options_id,
       od.match_id,
       (case
            when i.order_no is not null then
                (case when ${language} = 'en' then i.tournament_name_en else i.tournament_name_zs end)
            else od.match_name end)                  tournament_name,
       od.match_name,
       (case
            when i.order_no is not null then
                (case when ${language} = 'en' then i.match_info_en else i.match_info_zs end)
            else od.match_info end)                  match_info,
       od.odds_value / 100000                        odds_value,
       od.odd_finally,
       od.order_no,
       (case
            when i.order_no is not null then
                (case when ${language} = 'en' then i.playoption_en else i.playoption_zs end)
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
                                                         @f := ifNULL(sl1.${language}, '0'),
                                                         @g := ifNULL(sl2.${language}, '0'), @h := 1)
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
                     where o1.create_time >= 111111
                       and o1.series_type = 1
                       AND o1.uid = 111
                       AND o1.order_status in (1)

                       and o1.order_no in (select distinct order_no
                                           from t_order_detail tod
                                           where tod.create_time >= 111
                                             AND tod.sport_id = 1
                                             AND tod.tournament_id = 222222)
                       and o1.order_no in (select distinct order_no
                                           from t_settle s1
                                           where s1.create_time >= 22222
                                             and s1.last_settle = 1
                                             and s1.profit_amount >= 11
                                             and s1.settle_type > 2
                                             and s1.settle_type not in (4, 5))
                       and o1.uid in (select tu.uid
                                      from t_user tu
                                      where tu.username = 11
                                        and tu.fake_name = '1')
                     ORDER BY o1.id desc
                     LIMIT 1,100) o ON o.order_no = od.order_no
         LEFT JOIN t_settle s ON s.order_no = od.order_no and s.last_settle = 1
         left join t_order_internationalize i on od.bet_no = i.bet_no and od.order_no = i.order_no
         LEFT JOIN t_user u ON u.uid = od.uid
         LEFT JOIN t_merchant m ON u.merchant_code = m.merchant_code;

###settle
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
                (case when ${language} = 'en' then i.sport_name_en else i.sport_name_zs end)
            else od.sport_name end)                  sport_name,
       od.match_type,
       od.market_type,
       od.play_options,
       ROUND(od.bet_amount / 100, 2)                 bet_amount,
       od.market_value,
       od.play_name                                  original_play,
       (case
            when i.order_no is not null then
                (case when ${language} = 'en' then i.play_name_en else i.play_name_zs end)
            else od.play_name end)                   play_name,
       od.play_options_id,
       od.match_id,
       (case
            when i.order_no is not null then
                (case when ${language} = 'en' then i.tournament_name_en else i.tournament_name_zs end)
            else od.match_name end)                  tournament_name,
       (case
            when i.order_no is not null then
                (case when ${language} = 'en' then i.match_info_en else i.match_info_zs end)
            else od.match_info end)                  match_info,
       od.match_name,
       od.odds_value / 100000                        odds_value,
       od.odd_finally,
       (case
            when i.order_no is not null then
                (case when ${language} = 'en' then i.playoption_en else i.playoption_zs end)
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
                       AND s1.create_time >= 11

                       AND s1.uid = 222222

                       AND s1.create_time <= 111

                       AND s1.order_no = 11

                       and s1.order_no in (select order_no
                                           from t_order o1
                                           where o1.create_time >=
                                                 UNIX_TIMESTAMP(CAST(SYSDATE() AS DATE) - INTERVAL 120 DAY) * 1000
                                             and o1.series_type = 1)

                       and s1.order_no in (select distinct order_no
                                           from t_order_detail tod
                                           where tod.create_time >=
                                                 UNIX_TIMESTAMP(CAST(SYSDATE() AS DATE) - INTERVAL 120 DAY) * 1000
                                             AND tod.sport_id = 1

                                             AND tod.tournament_id = 2222

                                             AND tod.play_id = 1)

                     ORDER BY s1.id desc
                     LIMIT 1,1000) s ON s.order_no = od.order_no
         left join t_order o on s.order_no = o.order_no
         left join t_order_internationalize i on od.bet_no = i.bet_no and od.order_no = i.order_no
         LEFT JOIN t_user u ON u.uid = od.uid
         LEFT JOIN t_merchant m ON u.merchant_code = m.merchant_code;
#beginTime

SELECT u.username,
       u.fake_name,
       u.user_level,
       u.currency_code,
       o.id,
       o.uid,
       m.merchant_name,
       m.merchant_code,
       m.transfer_mode,
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
       o.manager_code,
       o.order_status,
       ROUND(s.settle_amount / 100, 2)               settle_amount,
       ROUND(s.profit_amount / 100, 2)               profit_amount,
       ROUND(s.original_profit_amount / 100, 2)      original_profit_amount,
       ROUND(s.original_settle_amount / 100, 2)      original_settle_amount,
       s.out_come,
       s.settle_time,
       s.settle_type,
       od.bet_no,
       od.sport_id,
       od.play_id,
       (case
            when i.order_no is not null then
                (case when ${language} = 'en' then i.sport_name_en else i.sport_name_zs end)
            else od.sport_name end)                  sport_name,
       od.match_type,
       od.market_type,
       od.play_options,
       ROUND(od.bet_amount / 100, 2)                 bet_amount,
       od.market_value,
       od.play_name                                  original_play,
       (case
            when i.order_no is not null then
                (case when ${language} = 'en' then i.play_name_en else i.play_name_zs end)
            else od.play_name end)                   play_name,
       od.play_options_id,
       od.match_id,
       (case
            when i.order_no is not null then
                (case when ${language} = 'en' then i.tournament_name_en else i.tournament_name_zs end)
            else od.match_name end)                  tournament_name,
       (case
            when i.order_no is not null then
                (case when ${language} = 'en' then i.match_info_en else i.match_info_zs end)
            else od.match_info end)                  match_info,
       od.match_name,
       od.odds_value / 100000                        odds_value,
       od.odd_finally,
       od.create_time,
       od.create_user,
       od.order_no,
       (case
            when i.order_no is not null then
                (case when ${language} = 'en' then i.playoption_en else i.playoption_zs end)
            else od.play_option_name end)            play_option_name,
       od.bet_result,
       od.bet_status,
       od.remark                                     od_remark,
       (CASE
            WHEN od.sport_id in (1, 2) and od.match_type = 2
                THEN od.score_benchmark END)         score_benchmark,
       od.cancel_type,
       od.settle_score,
       od.trade_type,
       od.risk_event,
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
                     where o1.create_time >= 1111
                       and o1.series_type = 1

                       AND o1.series_type <> 1

                       AND o1.uid = 111111111

                       and o1.order_no in (select distinct order_no
                                           from t_order_detail tod
                                                    left join s_match_info mi on tod.match_id = mi.id
                                           where tod.create_time >= 777
                                             and mi.begin_time >= 777
                                             and mi.begin_time <= 88)
                     ORDER BY o1.id desc
                     LIMIT 1,1000) o ON o.order_no = od.order_no
         left join t_order_internationalize i on od.bet_no = i.bet_no and od.order_no = i.order_no
         LEFT JOIN t_settle s ON s.order_no = od.order_no and s.last_settle = 1 and s.bet_amount > 0
         LEFT JOIN t_user u ON u.uid = od.uid
         LEFT JOIN t_merchant m ON u.merchant_code = m.merchant_code

