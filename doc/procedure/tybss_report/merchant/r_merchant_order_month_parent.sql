DROP PROCEDURE IF EXISTS p_r_merchant_order_month_parent;

DELIMITER //
CREATE PROCEDURE `p_r_merchant_order_month_parent`(IN `executetimeL` BIGINT(10))
BEGIN
		DECLARE parent_code_value  VARCHAR(100);


	  DECLARE done INT default false;

		DECLARE total INT DEFAULT 0;
		DECLARE parent_code_cursor CURSOR FOR
SELECT
    parent_code
FROM
    tybss_report.r_merchant_order_month
where
    parent_code is not null	and time =  executetimeL;




DECLARE continue HANDLER for not found set done = true;



open parent_code_cursor;

read_loop: LOOP

			FETCH parent_code_cursor INTO parent_code_value;



			if done then
				leave read_loop;
end if;


			REPLACE INTO r_merchant_order_month
SELECT Concat(parent_code, time)                                                           id,
       parent_code_value,
    time,
    CAST(sum(bet_user_sum) / sum(register_total_user_sum) AS  DECIMAL(32,4))            bet_user_rate,
    sum(profit)                                                                         profit,
    sum(register_total_user_sum)                                                        register_total_user_sum,
    sum(return_amount) / IF(sum(valid_bet_amount) = '0', NULL, sum(valid_bet_amount))   return_rate,
    sum(return_amount)                                                                  return_amount,
    0,
    sum(bet_amount)                                                                     bet_amount,
    sum(valid_bet_amount)                                                               valid_bet_amount,
    sum(filed_bet_amount)                                                               filed_bet_amount,
    sum(order_sum)                                                                      total_tickets,
    sum(valid_tickets)                                                                  valid_tickets,
    sum(filed_tickets)                                                                  filed_tickets,
    sum(first_bet_user_sum)                                                             first_bet_user_sum,
    sum(bet_user_sum)                                                                   bet_users,
    sum(valid_bet_users)                                                                valid_bet_users,
    sum(settle_users) / sum(register_total_user_sum)                                    settle_user_rate,
    sum(settle_profit)                                                                  settle_profit,
    sum(settle_return)                                                                  settle_return,
    sum(settle_return) / IF(sum(settle_bet_amount) = '0', NULL, sum(settle_bet_amount)) settle_return_rate,
    sum(settle_bet_amount)                                                              settle_bet_amount,
    sum(settle_order_num)                                                               settle_order_num,
    min(parent_name),
    sum(add_user)                                                                       add_user,
    UNIX_TIMESTAMP(current_timestamp(3)),
    null,
    null,
    sum(settle_users)                                                                   settle_users,
    1,
    sum(bet_amount_settled)                                                             bet_amount_settled,
    sum(ticket_settled)                                                                 ticket_settled,
    sum(bet_settled_users)                                                              bet_settled_users,
    sum(profit) / IF(sum(valid_bet_amount) = '0', NULL, sum(valid_bet_amount))          profit_rate,
    SUM(settle_expect_profit)  as                                                       settleExpectProfit,
    SUM(settle_series_amount)  as                                                       settle_series_amount,
    SUM(settle_series_tickets) as                                                       settle_series_tickets,
    SUM(settle_series_users)   as                                                       settle_series_users,
    SUM(settle_series_profit)  as                                                       settle_series_profit,
    SUM(order_valid_bet_money) as                                                       order_valid_bet_money,
    SUM(settle_valid_bet_money) as                                                      settle_valid_bet_money
FROM `r_merchant_order_month`
where time = executetimeL AND parent_code=parent_code_value
group by time;


set total=total+1;

END LOOP;
CLOSE parent_code_cursor;


select total;

END
//
DELIMITER ;
