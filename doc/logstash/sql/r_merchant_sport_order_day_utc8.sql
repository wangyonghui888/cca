select id                      as "id",
       sport_id                as "sportId",
       merchant_code           as "merchantCode",
       `time`                  as "time",
       bet_user_rate           as "betUserRate",
       profit                  as "profit",
       return_rate             as "returnRate",
       return_amount           as "returnAmount",
       merchant_level          as "merchantLevel",
       bet_amount              as "betAmount",
       order_sum               as "orderSum",
       bet_user_sum            as "betUserSum",
       settle_user_rate        as "settleUserRate",
       settle_profit           as "settleProfit",
       settle_return           as "settleReturn",
       settle_return_rate      as "settleReturnRate",
       settle_bet_amount       as "settleBetAmount",
       settle_order_num        as "settleOrderNum",
       merchant_name           as "merchantName",
       add_user                as "addUser",
       first_bet_user_sum      as "firstBetUserSum",
       register_total_user_sum as "registerTotalUserSum",
       parent_code             as parentCode,
       parent_name             as parentName,
       `year`,
       settle_users               settleUsers,
       agent_level                agentLevel,
       bet_amount_settled      as "betAmountSettled",
       ticket_settled          as "ticketSettled",
       bet_settled_users       as betSettledUsers,
       order_valid_bet_money   as orderValidBetMoney,
       settle_valid_bet_money  as settleValidBetMoney,
       updated_time            AS updatedTime
from tybss_report.r_merchant_sport_order_day_utc8
WHERE (updated_time >= :sql_last_value AND updated_time < UNIX_TIMESTAMP(now()))
  and id is not null
  and id != ''
ORDER BY updated_time ASC
LIMIT 100000;
