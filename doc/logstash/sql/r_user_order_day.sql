SELECT id,
       user_id                   userId,
       user_name                 userName,
       merchant_code             merchantCode,
       merchant_name             merchantName,
       `time`,
       profit,
       return_amount             returnAmount,
       profit_rate               profitRate,
       bet_amount                betAmount,
       valid_bet_amount          validBetAmount,
       bet_num                   betNum,
       valid_tickets             validTickets,
       bet_amount_settled        betAmountSettled,
       ticket_settled            ticketSettled,
       settle_order_num          settleOrderNum,
       settle_order_amount       settleOrderAmount,
       settle_profit             settleProfit,
       settle_return             settleReturn,
       settle_profit_rate        settleProfitRate,
       parent_code            as parentCode,
       parent_name            as parentName,
       `year`,
       order_valid_bet_money  as orderValidBetMoney,
       settle_valid_bet_money as settleValidBetMoney,
       updated_time           AS updatedTime
FROM tybss_report.r_user_order_day
WHERE (updated_time >= :sql_last_value AND updated_time < UNIX_TIMESTAMP(now()))
  and id is not null
  and id != ''
ORDER BY updated_time ASC
LIMIT 100000;