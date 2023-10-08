SELECT group_concat(od.bet_no order by od.bet_no desc)                     as betNo,

       Concat(",", group_concat(od.sport_id order by od.bet_no desc), ",") as sportId,
       group_concat(od.sport_name order by od.bet_no desc)                 as sportName,

       group_concat(od.sport_name_zs order by od.bet_no desc)              as sportNameZs,

       group_concat(od.sport_name_en order by od.bet_no desc)              as sportNameEn,
       Concat(",", group_concat(od.play_id order by od.bet_no desc), ",")  as playId,
       group_concat(od.play_name order by od.bet_no desc)                  as playName,
       group_concat(od.play_name_en order by od.bet_no desc)               as playNameEn,
       group_concat(od.play_name_zs order by od.bet_no desc)               as playNameZs,
       group_concat(od.tournament_id order by od.bet_no desc)              as tournamentId,
       group_concat(od.tournament_level order by od.bet_no desc)           as tournamentLevel,
       group_concat(od.away_name order by od.bet_no desc)                  as awayName,
       group_concat(od.home_name order by od.bet_no desc)                  as homeName,
       group_concat(od.match_id order by od.bet_no desc)                   as matchId,
       group_concat(od.match_name order by od.bet_no desc)                 as matchName,

       group_concat(od.tournament_name_en order by od.bet_no desc)         as tournamentNameEn,

       group_concat(od.tournament_name_zs order by od.bet_no desc)         as tournamentNameZs,
       group_concat(od.match_type order by od.bet_no desc)                 as matchType,
       group_concat(od.match_info order by od.bet_no desc)                 as matchInfo,

       group_concat(od.match_info_en order by od.bet_no desc)              as matchInfoEn,
       group_concat(od.match_info_zs order by od.bet_no desc)              as matchInfoZs,

       group_concat(od.market_id order by od.bet_no desc)                  as marketId,

       group_concat(od.market_type order by od.bet_no desc)                as marketType,

       group_concat(od.market_value order by od.bet_no desc)               as marketValue,

       group_concat(od.play_options_id order by od.bet_no desc)            as playOptionsId,

       group_concat(od.play_options order by od.bet_no desc)               as playOptions,
       group_concat(od.play_option_name order by od.bet_no desc)           as playOptionName,
       group_concat(od.playoption_en order by od.bet_no desc)              as playOptionEn,
       group_concat(od.playoption_zs order by od.bet_no desc)              as playOptionZs,
       group_concat(od.odds_value order by od.bet_no desc)                 as oddValue,
       group_concat(od.odd_finally order by od.bet_no desc)                as oddFinally,

       group_concat(od.bet_result order by od.bet_no desc)                 as betResult,
       group_concat(od.bet_status order by od.bet_no desc)                 as betStatus,


       group_concat(od.score_benchmark order by od.bet_no desc)            as scoreBenchmark,
       group_concat(od.remark order by od.bet_no desc)                     as cRemark,


       group_concat(od.cancel_type order by od.bet_no desc)                as cancelType,
       group_concat(od.settle_score order by od.bet_no desc)               as settleScore,


       group_concat(od.place_num order by od.bet_no desc)                  as placeNum,
       group_concat(od.cancel_time order by od.bet_no desc)                as cancelTime,
       group_concat(od.risk_event order by od.bet_no desc)                 as riskEvent,

       group_concat(od.settle_var order by od.bet_no desc)                 as settleVar,

       group_concat(od.mts_cancel_result order by od.bet_no desc)          as mtsCancelResult,

       group_concat(od.child_play_id order by od.bet_no desc)              as childPlayId,

       group_concat(od.trade_type order by od.bet_no desc)                 as tradeType,
       group_concat(od.odds_data_sourse order by od.bet_no desc)           as oddsDataSourse,
       group_concat(od.bet_amount order by od.bet_no desc)                 as betAmount,
       group_concat(od.original_bet_amount order by od.bet_no desc)        as originalBetAmount,
       group_concat(CASE
                        WHEN o.manager_code not in (3, 4) THEN (SELECT ifNULL(t2.begin_time, 0)
                                                                from tybss_merchant_common.s_match_info t2
                                                                where od.match_id = t2.id)
                        WHEN o.manager_code = 3 THEN (select ifNULL(t1.begin_time, 0)
                                                      from tybss_merchant_common.s_virtual_match_info t1
                                                      where t1.id = od.match_id)
                        WHEN o.manager_code = 4 THEN (select ifNULL(t1.start_time, 0)
                                                      from tybss_merchant_common.s_esport_matches t1
                                                      where t1.id = od.match_id) end
                    order by od.bet_no desc)                               as beginTime,
       o.merchant_name                                                        merchantName,
       o.merchant_code                                                        merchantCode,
       o.uid,
       o.username,
       o.user_level                                                           userLevel,
       o.currency_code                                                        currencyCode,
       o.order_no                                                             id,
       o.create_time                                                          createTime,
       o.modify_time                                                          modifyTime,
       o.confirm_time                                                         confirmTime,
       o.remark,
       o.order_amount_total                                                   orderAmountTotal,
       o.original_order_amount_total                                          originalOrderAmountTotal,
       o.product_amount_total                                                 productAmountTotal,
       o.pre_bet_amount                                                       preBetAmount,
       o.original_pre_bet_amount                                              originalPreBetAmount,
       o.product_count                                                        productCount,
       o.series_type                                                          seriesType,
       o.series_value                                                         seriesValue,
       o.manager_code                                                         managerCode,
       o.ac_code                                                              acCode,
       o.ip,
       o.ip_area                                                              ipArea,
       o.device_type                                                          deviceType,
       o.device_imei                                                          deviceImei,
       o.order_status                                                         orderStatus,
       o.pre_settle                                                           preSettle,
       o.vip_level                                                            vipLevel,
       o.lang_code                                                            langCode,
       o.max_win_amount                                                       maxWinAmount,
       o.original_max_win_amount                                              originalMaxWinAmount,
       o.settle_amount                                                        settleAmount,
       o.profit_amount                                                        profitAmount,
       o.original_profit_amount                                               originalProfitAmount,
       o.original_settle_amount                                               originalSettleAmount,
       o.settle_time                                                          settleTime,
       o.out_come                                                             outCome,
       o.settle_type                                                          settleType,
       o.settle_times                                                         settleTimes,
       o.dangerous_id                                                         dangerousId,
       o.pre_order                                                            preOrder,
       o.sumValidOriginalBetMoney                                             sumValidOriginalBetMoney,
       o.sumValidBetMoney                                                     sumValidBetMoney,
       o.sumValidBetNo                                                        sumValidBetNo
FROM t_ticket_detail od
         RIGHT JOIN (SELECT o1.uid,
                            o1.order_no,
                            o1.create_time,
                            o1.modify_time,
                            o1.merchant_name,
                            o1.merchant_code,
                            o1.remark,
                            o1.order_amount_total,
                            o1.original_order_amount_total,
                            o1.product_amount_total,
                            o1.pre_bet_amount,
                            o1.original_pre_bet_amount,
                            o1.product_count,
                            o1.series_type,
                            o1.series_value,
                            o1.manager_code,
                            o1.ac_code,
                            o1.ip,
                            o1.ip_area,
                            o1.device_type,
                            o1.device_imei,
                            o1.order_status,
                            o1.pre_settle,
                            o1.vip_level,
                            o1.lang_code,
                            o1.max_win_amount,
                            o1.original_max_win_amount,
                            o1.dangerous_id,
                            o1.pre_order,
                            o1.confirm_time,
                            o1.username,
                            o1.user_level,
                            o1.currency_code,
                            o1.settle_amount,
                            o1.profit_amount,
                            o1.original_profit_amount,
                            o1.original_settle_amount,
                            o1.settle_time,
                            o1.out_come,
                            o1.settle_type,
                            o1.settle_times,
                            CASE
                                WHEN o1.out_come in (3, 4, 5, 6) and
                                     ABS(o1.order_amount_total) = ABS(o1.profit_amount)
                                    THEN ABS(o1.original_profit_amount)
                                WHEN o1.out_come in (3, 4, 5, 6) and
                                     ABS(o1.order_amount_total) > ABS(o1.profit_amount)
                                    THEN ABS(o1.original_profit_amount)
                                WHEN o1.out_come in (3, 4, 5, 6) and
                                     ABS(o1.order_amount_total) < ABS(o1.profit_amount)
                                    THEN ABS(o1.original_order_amount_total) END as sumValidOriginalBetMoney,
                            CASE
                                WHEN o1.out_come in (3, 4, 5, 6) and
                                     ABS(o1.order_amount_total) = ABS(o1.profit_amount)
                                    THEN ABS(o1.profit_amount)
                                WHEN o1.out_come in (3, 4, 5, 6) and
                                     ABS(o1.order_amount_total) > ABS(o1.profit_amount)
                                    THEN ABS(o1.profit_amount)
                                WHEN o1.out_come in (3, 4, 5, 6) and
                                     ABS(o1.order_amount_total) < ABS(o1.profit_amount)
                                    THEN ABS(o1.order_amount_total) end          as sumValidBetMoney,
                            CASE
                                WHEN o1.out_come in (3, 4, 5, 6)
                                    THEN 1 end                                   as sumValidBetNo
                     FROM t_ticket o1
                     where o1.modify_time >= :sql_last_value
                       AND o1.modify_time <= UNIX_TIMESTAMP(now()) * 1000 - 1000 * 60 * 3
                     ORDER BY o1.modify_time asc
                     LIMIT 200000) o ON o.order_no = od.order_no
group by o.order_no
order by o.modify_time asc;
