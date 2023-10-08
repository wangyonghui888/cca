SELECT od.bet_no,
       od.order_no,
       od.uid,
       od.create_time,
       od.modify_time,
       od.original_bet_amount,
       od.bet_amount,
       od.sport_id,
       od.sport_name,
       i.sport_name_zs,
       i.sport_name_en,
       od.play_id,
       od.play_name,
       i.play_name_en,
       i.play_name_zs,
       od.tournament_id,
       od.tournament_level,
       od.match_id,
       od.match_name,
       i.tournament_name_en,
       i.tournament_name_zs,
       od.match_type,
       od.match_info,
       i.match_info_en,
       i.match_info_zs,
       od.market_id,
       od.market_type,
       od.market_value,
       od.play_options_id,
       od.play_options,
       od.play_option_name,
       i.playoption_en,
       i.playoption_zs,
       od.odds_value,
       od.odd_finally,
       od.bet_result,
       od.bet_status,
       od.score_benchmark,
       od.remark,
       od.cancel_type,
       od.settle_score,
       od.place_num,
       od.settle_times,
       od.cancel_time,
       od.risk_event,
       (CASE
            WHEN o.manager_code not in (3, 4) THEN (SELECT CONCAT(@a := ifNULL(t2.begin_time, 0),
                                                                  @f := ifNULL(sl1.zs, '0'),
                                                                  @g := ifNULL(sl2.zs, '0'))
                                                    from s_match_info t2
                                                             left join s_language sl1 on sl1.name_code = t2.home_name_code
                                                             left join s_language sl2 on sl2.name_code = t2.away_name_code
                                                    where od.match_id = t2.id)
            WHEN o.manager_code = 3 THEN (select CONCAT(@a := ifNULL(t1.begin_time, 0),
                                                        @f := ifNULL(sl1.zs, '0'),
                                                        @g := ifNULL(sl2.zs, '0'))
                                          from s_virtual_match_info t1
                                                   left join s_language sl1 on sl1.name_code = t1.home_name_code
                                                   left join s_language sl2 on sl2.name_code = t1.away_name_code
                                          where t1.id = od.match_id)
            WHEN o.manager_code = 4 THEN (select CONCAT(@a := ifNULL(t1.start_time, 0),
                                                        @f := ifNULL(sl1.zs, '0'),
                                                        @g := ifNULL(sl2.zs, '0'))
                                          from s_esport_matches t1
                                                   left join s_esport_language sl1 on sl1.name_code = t1.home_name_code
                                                   left join s_esport_language sl2 on sl2.name_code = t1.away_name_code
                                          where t1.id = od.match_id) end)
           as temp,
       @a  as begin_time,
       @f  as home_name,
       @g  as away_name,
       od.settle_var,
       od.mts_cancel_result,
       od.child_play_id,
       od.trade_type,
       od.odds_data_sourse
FROM t_order o
         RIGHT JOIN (SELECT o1.id,
                            o1.uid,
                            o1.order_no,
                            o1.bet_no,
                            o1.create_time,
                            o1.modify_time,
                            o1.bet_amount,
                            o1.original_bet_amount,
                            o1.sport_id,
                            o1.play_id,
                            o1.market_id,
                            o1.sport_name,
                            o1.match_type,
                            o1.market_type,
                            o1.play_options,
                            o1.market_value,
                            o1.play_name,
                            o1.play_options_id,
                            o1.match_id,
                            o1.tournament_id,
                            o1.tournament_level,
                            o1.match_name,
                            o1.match_info,
                            o1.play_option_name,
                            o1.odds_value,
                            o1.odd_finally,
                            o1.bet_result,
                            o1.bet_status,
                            o1.remark,
                            o1.cancel_type,
                            o1.settle_score,
                            o1.trade_type,
                            o1.score_benchmark,
                            o1.place_num,
                            o1.settle_times,
                            o1.cancel_time,
                            o1.risk_event,
                            o1.settle_var,
                            o1.mts_cancel_result,
                            o1.child_play_id,
                            o1.odds_data_sourse
                     FROM `t_order_detail` o1
                     where o1.modify_time >= :sql_last_value
                       AND o1.modify_time <= UNIX_TIMESTAMP(now()) * 1000 - 1000 * 60 * 1
                     ORDER BY o1.modify_time asc
                     LIMIT 150000) od ON o.order_no = od.order_no
         left join t_order_internationalize i on od.bet_no = i.bet_no and od.order_no = i.order_no
where o.merchant_id not in (1261552851050696704,
                            1592141832077316096,
                            1592141557199409152,
                            1592140025108238336,
                            1592140973121605632,
                            1272529733137076224,
                            1528744571750715392,
                            1397510526795386880,
                            2);