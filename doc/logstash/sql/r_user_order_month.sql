select id,
       user_id                   userId,
       user_name                 userName,
       merchant_code             merchantCode,
       merchant_name             merchantName,
       `time`,
       bet_num                   betNum,
       valid_tickets             validTickets,
       bet_amount                betAmount,
       valid_bet_amount          validBetAmount,
       profit,
       return_amount             returnAmount,
       profit_rate               profitRate,
       settle_order_num          settleOrderNum,
       settle_order_amount       settleOrderAmount,
       settle_profit             settleProfit,
       settle_profit_rate        settleProfitRate,
       settle_return             settleReturn,
       active_days               activeDays,
       parent_code            as parentCode,
       parent_name            as parentName,
       bet_amount_settled     as betAmountSettled,
       ticket_settled         as ticketSettled,
       order_valid_bet_money  as orderValidBetMoney,
       settle_valid_bet_money as settleValidBetMoney,
       updated_time           AS updatedTime
from tybss_report.r_user_order_month
WHERE (updated_time >= :sql_last_value AND updated_time < UNIX_TIMESTAMP(now()))
  and id is not null
  and id != ''
ORDER BY updated_time ASC
LIMIT 100000;