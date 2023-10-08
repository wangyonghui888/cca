select id,
       sport_id                  sprotId,
       user_id                   userId,
       user_name                 userName,
       merchant_code             merchantCode,
       `time`,
       bet_num                   betNum,
       bet_amount                betAmount,
       profit,
       return_amount             returnAmount,
       profit_rate               profitRate,
       settle_order_num          settleOrderNum,
       settle_order_amount       settleOrderAmount,
       settle_profit             settleProfit,
       settle_profit_rate        settleProfitRate,
       settle_return             settleReturn,
       merchant_name             merchantName,
       parent_code            as parentCode,
       parent_name            as parentName,
       `year`,
       bet_amount_settled        betAmountSettled,
       ticket_settled            ticketSettled,
       order_valid_bet_money  as orderValidBetMoney,
       settle_valid_bet_money as settleValidBetMoney,
       updated_time           AS updatedTime
from tybss_report.r_user_sport_order_day
WHERE (updated_time >= :sql_last_value AND updated_time < UNIX_TIMESTAMP(now()))
  and id is not null
  and id != ''
ORDER BY updated_time ASC
LIMIT 100000;
