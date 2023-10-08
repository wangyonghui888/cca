select id,
       match_id         matchId,
       match_info       matchInfo,
       sport_id         sportId,
       sport_name       sportName,
       begin_time       beginTime,
       match_status     matchStatus,
       bet_amount       betAmount,
       valid_bet_amount validBetAmount,
       settle_amount    settleAmount,
       profit,
       tournament_id    tournamentId,
       user_amount      userAmount,
       order_amount     orderAmount,
       profit_rate      profitRate,
       play_amount      playAmount,
       tournament_name  tournamentName,
       tournament_level  tournamentLevel,
       un_settle_order  as unSettleOrder,
       un_settle_amount  as unSettleAmount,
       updated_time AS  updatedTime
from tybss_report.r_match_bet_info
WHERE (updated_time >= :sql_last_value AND updated_time < UNIX_TIMESTAMP(now()))
ORDER BY updated_time ASC;