select id,
       finance_date       as financeDate,
       finance_time       as financeTime,
       merchant_id        as merchantId,
       merchant_code      as merchantCode,
       merchant_name      as merchantName,
       agent_level        as agentLevel,
       currency           as currency,
       order_amount_total as orderAmountTotal,
       settle_amount      as settleAmount,
       order_num          as orderNum,
       order_valid_num    as orderValidNum,
       order_back_num     as orderBackNum,
       transfer_num       as transferNum,
       create_time        as createTime,
       modify_time        as modifyTime,
       updated_time       AS updatedTime
from tybss_report.r_merchant_finance_day
WHERE (updated_time >= :sql_last_value AND updated_time < UNIX_TIMESTAMP(now()))
ORDER BY updated_time ASC LIMIT 100000;