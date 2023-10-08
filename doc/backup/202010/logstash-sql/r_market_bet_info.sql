select id,
       match_id           matchId,
       play_id            playId,
       market_id          marketId,
       match_info         matchInfo,
       play_name          playName,
       market_value       marketValue,
       market_type        marketType,
       bet_amount         betAmount,
       order_amount_total orderAmountTotal,
       valid_bet_amount   validBetAmount,
       settle_amount      settleAmount,
       profit,
       profit_rate        profitRate,
       user_amount        userAmount,
       order_amount       orderAmount,
       begin_time    as beginTime,
       tournament_level as tournamentLevel,
       match_status  as matchStatus,
       updated_time AS    updatedTime
from tybss_report.r_market_bet_info
WHERE (updated_time >= :sql_last_value AND updated_time < UNIX_TIMESTAMP(now()))
ORDER BY updated_time ASC;