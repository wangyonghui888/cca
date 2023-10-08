select `id`               as id,
       `match_id`         as matchId,
       `match_info`       as matchInfo,
       `sport_id`         as sportId,
       `sport_name`       as sportName,
       `begin_time`       as beginTime,
       `match_status`     as matchStatus,
       `bet_amount`       as betAmount,
       `valid_bet_amount` as validBetAmount,
       `settle_amount`    as settleAmount,
       `profit`           as profit,
       `tournament_id`    as tournamentId,
       `user_amount`      as userAmount,
       `order_amount`     as orderAmount,
       `profit_rate`      as profitRate,
       `play_amount`      as playAmount,
       `tournament_name`  as tournamentName,
        tournament_level  as tournamentLevel,
       merchant_code      as merchantCode,
       merchant_name      as merchantName,
       updated_time       AS updatedTime
from tybss_report.`r_merchant_match_bet_info`
WHERE (updated_time >= :sql_last_value AND updated_time < UNIX_TIMESTAMP(now()))
ORDER BY updated_time ASC;