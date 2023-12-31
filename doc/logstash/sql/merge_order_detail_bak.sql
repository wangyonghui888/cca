SELECT od.bet_no,
       od.order_no,
       od.uid,
       od.create_time,
       od.modify_time,
       od.original_bet_amount,
       od.bet_amount,
       od.sport_id,
       od.sport_name,
       od.sport_name       sport_name_zs,
       od.sport_name       sport_name_en,
       od.play_id,
       od.play_name,
       od.play_name        play_name_en,
       od.play_name        play_name_zs,
       od.tournament_id,
       od.tournament_level,
       od.match_id,
       od.match_name,
       od.match_name       tournament_name_en,
       od.match_name       tournament_name_zs,
       od.match_type,
       od.match_info,
       od.match_info       match_info_en,
       od.match_info       match_info_zs,
       od.market_id,
       od.market_type,
       od.market_value,
       od.play_options_id,
       od.play_options,
       od.play_option_name,
       od.play_option_name playoption_en,
       od.play_option_name playoption_zs,
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
       mi.begin_time,
       mi.home_name_code,
       mi.away_name_code,
       od.settle_var,
       od.mts_cancel_result,
       od.child_play_id,
       od.trade_type,
       od.odds_data_sourse
FROM tybss_new_bak.t_order_detail_his od
         left join t_user u ON u.uid = od.uid and u.merchant_code not in
                                                  ('oubao', '719919', '105627', '472028', '553452', '2N12', '2I859',
                                                   '2G325', '2M175')
         left join s_match_info mi on mi.id = od.match_id
where od.create_time >= :sql_last_value
  AND od.create_time <= UNIX_TIMESTAMP(now()) * 1000 - 1000 * 3600
  and od.create_time >= 1668873600000
ORDER BY od.create_time asc
LIMIT 150000