drop PROCEDURE if exists `p_r_merchant_order_month_parent`;


DELIMITER $$
CREATE PROCEDURE `p_r_merchant_order_month_parent`(IN `executetimeL` BIGINT(10))
BEGIN
		DECLARE parent_code_value  VARCHAR(100);

		# 创建结束标志变量
	  DECLARE done INT default false;

		DECLARE total INT DEFAULT 0;
		DECLARE parent_code_cursor CURSOR FOR
SELECT
    parent_code
FROM
    tybss_report.r_merchant_order_month
where
    parent_code is not null	and time =  executetimeL;



#指定游标循环结束时的返回值
DECLARE continue HANDLER for not found set done = true;


		# 打开游标
		open parent_code_cursor;
		# 开始循环游标里的数据
		read_loop: LOOP
			# 根据游标当前指向的一条数据 插入到上面申明的局部变量中
			FETCH parent_code_cursor INTO parent_code_value;


			# 判断游标的循环是否结束
			if done then
				leave read_loop;	# 跳出游标循环
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
    SUM(settle_series_profit)  as                                                       settle_series_profit
FROM `r_merchant_order_month`
where time = executetimeL AND parent_code=parent_code_value
group by time;


set total=total+1;

END LOOP;
CLOSE parent_code_cursor;


select total;

END$$
DELIMITER ;









drop PROCEDURE if exists `p_r_merchant_order_month`;

DELIMITER $$
CREATE PROCEDURE `p_r_merchant_order_month`(IN `str_date` VARCHAR(100))
BEGIN
    DECLARE task_type INT(2) DEFAULT 14;
    DECLARE result_code CHAR(5) DEFAULT '0';
    DECLARE start_time VARCHAR(30) DEFAULT get_cur_ymdhms();
    DECLARE end_time VARCHAR(30);
    DECLARE result INT(2) DEFAULT 1;
    DECLARE exce_msg VARCHAR(512) DEFAULT 'p_r_merchant_order_month成功';
    DECLARE msg TEXT;
    DECLARE execute_date date;
    DECLARE executetimeL BIGINT(10);

    DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN
        GET DIAGNOSTICS CONDITION 1 result_code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
        SET end_time = get_cur_ymdhms();
        SET exce_msg = CONCAT('错误码：', result_code, 'p_r_merchant_order_month错误信息:', msg);
        SET result = 2;
CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
END;
    SET execute_date = str_to_date(str_date, '%Y-%m-%d');
    set executetimeL = DATE_FORMAT(execute_date, '%Y%m');



    REPLACE INTO r_merchant_order_month
SELECT a.id,
       a.merchant_code,
       a.time,
       b.betUser / a.register_total_user_sum,
       a.profit,
       a.register_total_user_sum,
       IFNULL(a.return_rate,0) return_rate,
       a.return_amount,
       a.merchant_level,
       a.bet_amount,
       IFNULL(a.valid_bet_amount, 0) valid_bet_amount,
       IFNULL(a.filed_bet_amount, 0) filed_bet_amount,
       a.order_sum,
       IFNULL(a.valid_tickets, 0)    valid_tickets,
       IFNULL(a.filed_tickets, 0)    filed_tickets,
       a.first_bet_user_sum,
       b.betUser,
       IFNULL(b.valid_bet_users, 0),
       b.settleUser / a.register_total_user_sum,
       a.settle_profit,
       a.settle_return,
       a.settle_return_rate,
       a.settle_bet_amount,
       a.settle_order_num,
       a.merchant_name,
       a.add_user,
       UNIX_TIMESTAMP(CURRENT_TIMESTAMP(3)),
       a.parent_code,
       a.parent_name,
       IFNULL(b.settleUser, 0) AS    settleUsers,
       a.agent_level           as    agent_level,
       a.bet_amount_settled    as    bet_amount_settled,
       a.ticket_settled        as    ticket_settled,
       b.bet_settled_users     as    bet_settled_users,
       a.profit / IF(a.bet_amount_settled = '0', NULL, a.bet_amount_settled),
       a.settleExpectProfit    as    settleExpectProfit,
       a.settle_series_amount  as    settle_series_amount,
       a.settle_series_tickets as    settle_series_tickets,
       b.settle_series_users   as    settle_series_users,
       a.settle_series_profit  as    settle_series_profit
FROM (SELECT Concat(t.merchant_code, t.MONTH) id,
             t.merchant_code,
             t.MONTH                          time,
                 SUM(t.profit)                    profit,
                 max(t.register_total_user_sum)   register_total_user_sum,
                 ifnull(SUM(t.return_amount) / IF(SUM(t.bet_amount) = '0', NULL, SUM(t.bet_amount)),
                        0)                    AS  return_rate,
                 SUM(t.return_amount)             return_amount,
                 MIN(t.merchant_level)            merchant_level,
                 SUM(t.bet_amount)                bet_amount,
                 SUM(t.bet_amount_settled)        bet_amount_settled,
                 SUM(t.ticket_settled)            ticket_settled,
                 SUM(t.valid_bet_amount)          valid_bet_amount,
                 SUM(t.filed_bet_amount)          filed_bet_amount,
                 SUM(t.order_sum)                 order_sum,
                 SUM(t.valid_tickets)             valid_tickets,
                 SUM(t.filed_tickets)             filed_tickets,
                 SUM(t.first_bet_user_sum)        first_bet_user_sum,
                 SUM(t.settle_profit)             settle_profit,
                 SUM(t.settle_return)             settle_return,
                 ifnull(SUM(t.settle_return) / IF(SUM(t.settle_bet_amount) = '0', NULL, SUM(t.settle_bet_amount)),
                        0)                    AS  settle_return_rate,
                 SUM(t.settle_bet_amount)         settle_bet_amount,
                 SUM(t.settle_order_num)          settle_order_num,
                 MIN(t.merchant_name)             merchant_name,
                 SUM(t.add_user)                  add_user,
                 max(t.parent_code)           as  parent_code,
                 max(t.parent_name)           as  parent_name,
                 MIN(t.agent_level)               agent_level,
                 SUM(t.settle_expect_profit)  as  settleExpectProfit,
                 SUM(t.settle_series_amount)  as  settle_series_amount,
                 SUM(t.settle_series_tickets) as  settle_series_tickets,
                 SUM(t.settle_series_profit)  as  settle_series_profit
      FROM (SELECT *,
          substring(time, 1, 6) AS 'month'
          FROM r_merchant_order_day
          WHERE time >= DATE_FORMAT(DATE_ADD(execute_date, INTERVAL - DAY(execute_date) + 1 DAY), '%Y%m%d')
          AND time <= DATE_FORMAT(last_day(execute_date), '%Y%m%d')) t
      GROUP BY t.merchant_code, t.MONTH) a
         LEFT JOIN (SELECT merchant_code,
                        time,
                        COUNT(CASE WHEN bet_amount > 0 THEN user_id END)           AS betUser,
                        COUNT(CASE WHEN valid_bet_amount > 0 THEN user_id END)     AS valid_bet_users,
                        COUNT(CASE WHEN settle_order_amount > 0 THEN user_id END)  AS settleUser,
                        Count(CASE WHEN bet_amount_settled > 0 THEN user_id END)   AS bet_settled_users,
                        Count(CASE WHEN settle_series_amount > 0 THEN user_id END) AS settle_series_users
                    FROM r_user_order_month
                    WHERE time = executetimeL
                    GROUP BY merchant_code, time) b ON a.merchant_code = b.merchant_code AND a.time = b.time;



call p_r_merchant_order_month_parent(executetimeL);

REPLACE INTO r_merchant_order_month_utc8
SELECT a.id,
       a.merchant_code,
       a.time,
       a.merchant_name,
       a.merchant_level,
       a.add_user,
       a.register_total_user_sum,
       b.betUser / a.register_total_user_sum,
       a.profit,
       a.profit / IF(a.bet_amount_settled = '0', NULL, a.bet_amount_settled),
       a.return_amount,
       a.return_rate,
       a.bet_amount,
       a.valid_bet_amount,
       a.filed_bet_amount,
       a.total_tickets,
       a.valid_tickets,
       a.filed_tickets,
       b.betUser,
       b.valid_bet_users,
       a.first_bet_user_sum,
       b.settleUser / a.register_total_user_sum,
       a.settle_profit,
       a.settle_return,
       a.settle_return_rate,
       a.settle_bet_amount,
       a.settle_order_num,
       UNIX_TIMESTAMP(CURRENT_TIMESTAMP(3)),
       a.parent_code,
       a.parent_name,
       IFNULL(b.settleUser, 0) AS settleUsers,
       a.agent_level           as agent_level,
       a.bet_amount_settled    as bet_amount_settled,
       a.ticket_settled        as ticket_settled,
       b.bet_settled_users     as bet_settled_users,
       a.settleExpectProfit    as settleExpectProfit,
       a.settle_series_amount  as settle_series_amount,
       a.settle_series_tickets as settle_series_tickets,
       b.settle_series_users   as settle_series_users,
       a.settle_series_profit  as settle_series_profit
FROM (SELECT Concat(t.merchant_code, t.MONTH) id,
             t.merchant_code,
             t.MONTH                          time,
                 SUM(t.profit)                    profit,
                 max(t.register_total_user_sum)   register_total_user_sum,
                 ifnull(SUM(t.return_amount) / IF(SUM(t.bet_amount) = '0', NULL, SUM(t.bet_amount)),
                        0)                    AS  return_rate,
                 SUM(t.return_amount)             return_amount,
                 MIN(t.merchant_level)            merchant_level,
                 SUM(t.bet_amount)                bet_amount,
                 SUM(t.bet_amount_settled)        bet_amount_settled,
                 SUM(t.ticket_settled)            ticket_settled,
                 SUM(t.valid_bet_amount)          valid_bet_amount,
                 SUM(t.filed_bet_amount)          filed_bet_amount,
                 SUM(t.total_tickets)             total_tickets,
                 SUM(t.valid_tickets)             valid_tickets,
                 SUM(t.filed_tickets)             filed_tickets,
                 SUM(t.first_bet_user_sum)        first_bet_user_sum,
                 SUM(t.settle_profit)             settle_profit,
                 SUM(t.settle_return)             settle_return,
                 ifnull(SUM(t.settle_return) / IF(SUM(t.settle_bet_amount) = '0', NULL, SUM(t.settle_bet_amount)),
                        0)                    AS  settle_return_rate,
                 SUM(t.settle_bet_amount)         settle_bet_amount,
                 SUM(t.settle_order_num)          settle_order_num,
                 MIN(t.merchant_name)             merchant_name,
                 SUM(t.add_user)                  add_user,
                 max(t.parent_code)           as  parent_code,
                 max(t.parent_name)           as  parent_name,
                 MIN(t.agent_level)               agent_level,
                 SUM(t.settle_expect_profit)  as  settleExpectProfit,
                 SUM(t.settle_series_amount)  as  settle_series_amount,
                 SUM(t.settle_series_tickets) as  settle_series_tickets,
                 SUM(t.settle_series_profit)  as  settle_series_profit
      FROM (SELECT *,
          substring(time, 1, 6) AS 'month'
          FROM r_merchant_order_day_utc8
          WHERE time >= DATE_FORMAT(DATE_ADD(execute_date, INTERVAL - DAY(execute_date) + 1 DAY), '%Y%m%d')
          AND time <= DATE_FORMAT(last_day(execute_date), '%Y%m%d')) t
      GROUP BY t.merchant_code,
          t.MONTH) a
         LEFT JOIN (SELECT merchant_code,
                        time,
                        COUNT(CASE WHEN bet_amount > 0 THEN user_id END)           AS betUser,
                        COUNT(CASE WHEN valid_bet_amount > 0 THEN user_id END)     AS valid_bet_users,
                        COUNT(CASE WHEN settle_order_amount > 0 THEN user_id END)  AS settleUser,
                        Count(CASE WHEN bet_amount_settled > 0 THEN user_id END)   AS bet_settled_users,
                        Count(CASE WHEN settle_series_amount > 0 THEN user_id END) AS settle_series_users
                    FROM r_user_order_month_utc8
                    WHERE time = executetimeL
                    GROUP BY merchant_code, time) b ON a.merchant_code = b.merchant_code AND a.time = b.time;

REPLACE INTO r_merchant_order_month_utc8
SELECT Concat(parent_code, time)                                                           id,
       parent_code,
    time,
    min(parent_name),
    0,
    sum(add_user)                                                                       add_user,
    sum(register_total_user_sum)                                                        register_total_user_sum,
    sum(bet_users) / sum(register_total_user_sum)                                       bet_user_rate,
    sum(profit)                                                                         profit,
    sum(profit) / IF(sum(valid_bet_amount) = '0', NULL, sum(valid_bet_amount))          profit_rate,
    sum(return_amount)                                                                  return_amount,
    sum(return_amount) / IF(sum(valid_bet_amount) = '0', NULL, sum(valid_bet_amount))   return_rate,
    sum(bet_amount)                                                                     bet_amount,
    sum(valid_bet_amount)                                                               valid_bet_amount,
    sum(filed_bet_amount)                                                               filed_bet_amount,
    sum(total_tickets)                                                                  total_tickets,
    sum(valid_tickets)                                                                  valid_tickets,
    sum(filed_tickets)                                                                  filed_tickets,
    sum(bet_users)                                                                      bet_users,
    sum(valid_bet_users)                                                                valid_bet_users,
    sum(first_bet_user_sum)                                                             first_bet_user_sum,
    sum(settle_users) / sum(register_total_user_sum)                                    settle_user_rate,
    sum(settle_profit)                                                                  settle_profit,
    sum(settle_return)                                                                  settle_return,
    sum(settle_return) / IF(sum(settle_bet_amount) = '0', NULL, sum(settle_bet_amount)) settle_return_rate,
    sum(settle_bet_amount)                                                              settle_bet_amount,
    sum(settle_order_num)                                                               settle_order_num,
    UNIX_TIMESTAMP(current_timestamp(3)),
    null,
    null,
    sum(settle_users)                                                                   settle_users,
    1,
    sum(bet_amount_settled)                                                             bet_amount_settled,
    sum(ticket_settled)                                                                 ticket_settled,
    sum(bet_settled_users)                                                              bet_settled_users,
    SUM(settle_expect_profit)  as                                                       settleExpectProfit,
    SUM(settle_series_amount)  as                                                       settle_series_amount,
    SUM(settle_series_tickets) as                                                       settle_series_tickets,
    SUM(settle_series_users)   as                                                       settle_series_users,
    SUM(settle_series_profit)  as                                                       settle_series_profit
FROM `r_merchant_order_month_utc8`
where parent_code is not null
  and time = executetimeL
group by parent_code, time;


REPLACE INTO r_merchant_order_month_utc8_sub
select Concat(a.merchant_code, executetimeL) AS id,
       a.merchant_code,
       a.merchant_name                       AS merchantName,
       a.parent_code,
       a.parent_name,
       executetimeL                          AS time,
           a.agent_level                            agentLevel,
           a.merchant_level,
           pre_bet_tickets,
           b.pre_bet_users,
           pra_bet_amount,
           pre_valid_tickets,
           pre_valid_bet_amount,
           pre_profit,
           pre_return_amount,
           pre_bet_amount_settled,
           pre_ticket_settled,
           pre_failed_bet_amount,
           pre_failed_tickets,
           pre_settle_tickets,
           pre_settle_bet_amount,
           pre_settle_profit,
           pre_settle_return,
           b.preSettleUser,
           b.live_betUsers,
           live_bet_tickets,
           live_bet_amount,
           live_valid_tickets,
           live_valid_bet_amount,
           live_profit,
           live_return_amount,
           live_bet_amount_settled,
           live_ticket_settled,
           live_failed_bet_amount,
           live_failed_tickets,
           live_settle_tickets,
           live_settle_bet_amount,
           live_settle_profit,
           live_settle_return,
           b.live_settleUsers,
           b.pa_bet_users,
           pa_bet_tickets,
           pa_bet_amount,
           pa_valid_tickets,
           pa_valid_bet_amount,
           pa_profit,
           pa_return_amount,
           pa_bet_amount_settled,
           pa_ticket_settled,
           pa_failed_bet_amount,
           pa_failed_tickets,
           pa_settle_tickets,
           pa_settle_bet_amount,
           pa_settle_profit,
           pa_settle_return,
           b.pa_SettleUsers,
           b.mts_betUsers,
           mts_bet_tickets,
           mts_bet_amount,
           mts_valid_tickets,
           mts_valid_bet_amount,
           mts_profit,
           mts_return_amount,
           mts_bet_amount_settled,
           mts_ticket_settled,
           mts_failed_bet_amount,
           mts_failed_tickets,
           mts_settle_tickets,
           mts_settle_bet_amount,
           mts_settle_profit,
           mts_settle_return,
           b.mts_settleUsers,
           b.series_betUsers,
           series_bet_tickets,
           series_bet_amount,
           series_valid_tickets,
           series_valid_bet_amount,
           series_profit,
           series_return_amount,
           series_bet_amount_settled,
           series_ticket_settled,
           series_failed_bet_amount,
           series_failed_tickets,
           series_settle_tickets,
           series_settle_bet_amount,
           series_settle_profit,
           series_settle_return,
           b.series_settleUsers,
           single_bet_users,
           single_settle_users,
           UNIX_TIMESTAMP(current_timestamp(3))
from (
    SELECT ru.merchant_code,
    min(ru.merchant_name) AS          merchant_name,
    min(ru.parent_code)               parent_code,
    min(ru.parent_name)               parent_name,
    min(ru.agent_level)               agent_level,
    min(ru.merchant_level)            merchant_level,
    sum(ru.pre_bet_tickets)           pre_bet_tickets,
    sum(ru.pre_bet_users)             pre_bet_users,
    sum(ru.pre_bet_amount)            pra_bet_amount,
    sum(ru.pre_valid_tickets)         pre_valid_tickets,
    sum(ru.pre_valid_bet_amount)      pre_valid_bet_amount,
    sum(ru.pre_profit)                pre_profit,
    sum(ru.pre_return_amount)         pre_return_amount,
    sum(ru.pre_bet_amount_settled)    pre_bet_amount_settled,
    sum(ru.pre_ticket_settled)        pre_ticket_settled,
    sum(ru.pre_failed_bet_amount)     pre_failed_bet_amount,
    sum(ru.pre_failed_tickets)        pre_failed_tickets,
    sum(ru.pre_settle_tickets)        pre_settle_tickets,
    sum(ru.pre_settle_bet_amount)     pre_settle_bet_amount,
    sum(ru.pre_settle_profit)         pre_settle_profit,
    sum(ru.pre_settle_return)         pre_settle_return,
    sum(ru.pre_settle_users)          pre_settle_users,
    sum(ru.live_bet_users)            live_bet_users,
    sum(ru.live_bet_tickets)          live_bet_tickets,
    sum(ru.live_bet_amount)           live_bet_amount,
    sum(ru.live_valid_tickets)        live_valid_tickets,
    sum(ru.live_valid_bet_amount)     live_valid_bet_amount,
    sum(ru.live_profit)               live_profit,
    sum(ru.live_return_amount)        live_return_amount,
    sum(ru.live_bet_amount_settled)   live_bet_amount_settled,
    sum(ru.live_ticket_settled)       live_ticket_settled,
    sum(ru.live_failed_bet_amount)    live_failed_bet_amount,
    sum(ru.live_failed_tickets)       live_failed_tickets,
    sum(ru.live_settle_tickets)       live_settle_tickets,
    sum(ru.live_settle_bet_amount)    live_settle_bet_amount,
    sum(ru.live_settle_profit)        live_settle_profit,
    sum(ru.live_settle_return)        live_settle_return,
    sum(ru.live_settle_users)         live_settle_users,
    sum(ru.pa_bet_tickets)            pa_bet_tickets,
    sum(ru.pa_bet_amount)             pa_bet_amount,
    sum(ru.pa_valid_tickets)          pa_valid_tickets,
    sum(ru.pa_valid_bet_amount)       pa_valid_bet_amount,
    sum(ru.pa_profit)                 pa_profit,
    sum(ru.pa_return_amount)          pa_return_amount,
    sum(ru.pa_bet_amount_settled)     pa_bet_amount_settled,
    sum(ru.pa_ticket_settled)         pa_ticket_settled,
    sum(ru.pa_failed_bet_amount)      pa_failed_bet_amount,
    sum(ru.pa_failed_tickets)         pa_failed_tickets,
    sum(ru.pa_settle_tickets)         pa_settle_tickets,
    sum(ru.pa_settle_bet_amount)      pa_settle_bet_amount,
    sum(ru.pa_settle_profit)          pa_settle_profit,
    sum(ru.pa_settle_return)          pa_settle_return,
    sum(ru.pa_settle_users)           pa_settle_users,
    sum(ru.mts_bet_users)             mts_bet_users,
    sum(ru.mts_bet_tickets)           mts_bet_tickets,
    sum(ru.mts_bet_amount)            mts_bet_amount,
    sum(ru.mts_valid_tickets)         mts_valid_tickets,
    sum(ru.mts_valid_bet_amount)      mts_valid_bet_amount,
    sum(ru.mts_profit)                mts_profit,
    sum(ru.mts_return_amount)         mts_return_amount,
    sum(ru.mts_bet_amount_settled)    mts_bet_amount_settled,
    sum(ru.mts_ticket_settled)        mts_ticket_settled,
    sum(ru.mts_failed_bet_amount)     mts_failed_bet_amount,
    sum(ru.mts_failed_tickets)        mts_failed_tickets,
    sum(ru.mts_settle_tickets)        mts_settle_tickets,
    sum(ru.mts_settle_bet_amount)     mts_settle_bet_amount,
    sum(ru.mts_settle_profit)         mts_settle_profit,
    sum(ru.mts_settle_return)         mts_settle_return,
    sum(ru.mts_settle_users)          mts_settle_users,
    sum(ru.series_bet_users)          series_bet_users,
    sum(ru.series_bet_tickets)        series_bet_tickets,
    sum(ru.series_bet_amount)         series_bet_amount,
    sum(ru.series_valid_tickets)      series_valid_tickets,
    sum(ru.series_valid_bet_amount)   series_valid_bet_amount,
    sum(ru.series_profit)             series_profit,
    sum(ru.series_return_amount)      series_return_amount,
    sum(ru.series_bet_amount_settled) series_bet_amount_settled,
    sum(ru.series_ticket_settled)     series_ticket_settled,
    sum(ru.series_failed_bet_amount)  series_failed_bet_amount,
    sum(ru.series_failed_tickets)     series_failed_tickets,
    sum(ru.series_settle_tickets)     series_settle_tickets,
    sum(ru.series_settle_bet_amount)  series_settle_bet_amount,
    sum(ru.series_settle_profit)      series_settle_profit,
    sum(ru.series_settle_return)      series_settle_return,
    sum(ru.series_settle_users)       series_settle_users,
    sum(ru.single_bet_users)          single_bet_users,
    sum(ru.single_settle_users)       single_settle_users,
    UNIX_TIMESTAMP(current_timestamp(3))
    from tybss_report.r_merchant_order_day_utc8_sub ru
    where ru.time like concat(executetimeL, '%')
    group by ru.merchant_code) a
    LEFT JOIN (SELECT merchant_code,
    COUNT(CASE WHEN u.pa_bet_tickets > 0 THEN user_id END)        AS pa_bet_users,
    COUNT(CASE WHEN u.pa_settle_tickets > 0 THEN user_id END)     AS pa_SettleUsers,
    COUNT(CASE WHEN u.pre_bet_tickets > 0 THEN user_id END)       AS pre_bet_users,
    COUNT(CASE WHEN u.pre_settle_tickets > 0 THEN user_id END)    AS preSettleUser,
    Count(CASE WHEN u.live_bet_tickets > 0 THEN user_id END)      AS live_betUsers,
    Count(CASE WHEN u.live_settle_tickets > 0 THEN user_id END)   AS live_settleUsers,
    Count(CASE WHEN u.mts_bet_tickets > 0 THEN user_id END)       AS mts_betUsers,
    Count(CASE WHEN u.mts_settle_tickets > 0 THEN user_id END)    AS mts_settleUsers,
    Count(CASE WHEN u.series_bet_tickets > 0 THEN user_id END)    AS series_betUsers,
    Count(CASE WHEN u.series_settle_tickets > 0 THEN user_id END) AS series_settleUsers
    FROM r_user_order_month_utc8_sub u
    WHERE time = executetimeL
    GROUP BY merchant_code) b
ON a.merchant_code = b.merchant_code;


SET end_time = get_cur_ymdhms();

    SET exce_msg = CONCAT(execute_date, ' p_r_merchant_order_month success!');

CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);

END$$
DELIMITER ;






