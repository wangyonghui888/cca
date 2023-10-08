select id,
       finance_id         as financeId,
       currency           as currency,
       bill_order_amount  as billOrderAmount,
       bill_order_num     as billOrderNum,
       bill_profit_amount as billProfitAmount,
       create_time        as createTime,
       modify_time        as modifyTime,
       updated_time       AS updatedTime
from tybss_report.r_merchant_finance_bill_month
WHERE (updated_time >= :sql_last_value AND updated_time < UNIX_TIMESTAMP(now()))
ORDER BY updated_time ASC LIMIT 100000;