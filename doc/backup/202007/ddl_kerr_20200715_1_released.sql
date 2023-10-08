DROP PROCEDURE if EXISTS p_r_merchant_finance_day;
DELIMITER //
CREATE
    DEFINER = `root`@`%` PROCEDURE p_r_merchant_finance_day(in execute_date varchar(100))
BEGIN
    DECLARE task_type INT(2) DEFAULT 11;
    DECLARE result_code CHAR(5) DEFAULT '0';
    DECLARE start_time VARCHAR(30) DEFAULT get_cur_ymdhms();
    DECLARE end_time VARCHAR(30);
    DECLARE result INT(2) DEFAULT 1;
    DECLARE exce_msg VARCHAR(512) DEFAULT '成功';
    DECLARE msg TEXT;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN
        GET DIAGNOSTICS CONDITION 1 result_code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
        SET end_time = get_cur_ymdhms();
        SET exce_msg = CONCAT(execute_date, "错误码：", result_code, " p_r_merchant_finance_day错误信息：", msg);
        SET result = 2;
        CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
    END;

##r_merchant_finance_day 账务日表新增  此处不使用replace
    DELETE FROM tybss_report.r_merchant_finance_day WHERE finance_date = execute_date;
    INSERT INTO tybss_report.r_merchant_finance_day
    (id, finance_day_id, finance_date, finance_time, merchant_id, merchant_code, merchant_name, agent_level, currency,
    order_amount_total, settle_amount, order_num, order_valid_num, order_back_num, transfer_num, create_time, modify_time,
    updated_time, parent_id, merchant_num, profit_amount, computing_standard, terrace_rate, vip_amount, technique_amount,
    payment_cycle, vip_payment_cycle, technique_payment_cycle, execute_rate, platform_profit, order_user_num, platform_profit_rate,
    settle_order_amount_total, settle_settle_amount, settle_order_num, settle_order_valid_num, settle_order_back_num,
    settle_transfer_num, settle_platform_profit, settle_order_user_num, settle_platform_profit_rate)
    SELECT concat(execute_date, '-', m.merchant_code, '-', IFNULL(z.currency_code, '1'))         as id,
           concat(execute_date, '-', m.merchant_code)                                            as finance_day_id,
           execute_date                                                                          as finance_date,
           unix_timestamp(date_sub(execute_date, interval 1 day)) * 1000 + (12 * 60 * 60 * 1000) as finance_time,
           m.id                                                                                  as merchant_id,
           m.merchant_code,
           m.merchant_name,
           m.agent_level,
           IFNULL(z.currency_code, '1')                                                          as currency,
           IFNULL(z.order_amount_total, 0) / 100                                                 as order_amount_total,
           IFNULL(z.settle_amount, 0) / 100                                                      as settle_amount,
           IFNULL(z.order_num, 0)                                                                as order_num,
           IFNULL(z.order_valid_num, 0)                                                          as order_valid_num,
           IFNULL(z.order_back_num, 0)                                                           as order_back_num,
           IFNULL(x.transfer_num, 0)                                                             as transfer_num,
           unix_timestamp(now())                                                                 as create_time,
           unix_timestamp(now())                                                                 as modify_time,
           unix_timestamp(now())                                                                 as updated_time,
           m.parent_id                                                                           as parent_id,
           (SELECT count(id) FROM tybss_new.t_merchant WHERE status = 1 and parent_id = m.id)    as merchant_num,
           IFNULL(z.profit_amount, 0) / 100                                                      as profit_amount,
           m.computing_standard                                                               as computing_standard,
           m.terrace_rate                                                                     as terrace_rate,
           m.vip_amount                                                                       as vip_amount,
           m.technique_amount                                                                 as technique_amount,
           m.payment_cycle                                                                    as payment_cycle,
           m.vip_payment_cycle                                                                as vip_payment_cycle,
           m.technique_payment_cycle                                                          as technique_payment_cycle,
           m.execute_rate                                                                     as execute_rate,

           IFNULL(z.platform_profit, 0) / 100                                                   as platform_profit,
           IFNULL(z.order_user_num, 0)                                                          as order_user_num,
           IFNULL(z.platform_profit_rate, 0)                                                    as platform_profit_rate,
           IFNULL(z.settle_order_amount_total, 0)  / 100                                        as settle_order_amount_total,
           IFNULL(z.settle_settle_amount, 0) / 100                                              as settle_settle_amount,
           IFNULL(z.settle_order_num, 0)                                                        as settle_order_num,
           IFNULL(z.settle_order_valid_num, 0)                                                  as settle_order_valid_num,
           IFNULL(z.settle_order_back_num, 0)                                                   as settle_order_back_num,
           IFNULL(x.transfer_num, 0)                                                            as settle_transfer_num,
           IFNULL(z.settle_platform_profit, 0) / 100                                            as settle_platform_profit,
           IFNULL(z.settle_order_user_num, 0)                                                   as settle_order_user_num,
           IFNULL(z.settle_platform_profit_rate, 0)                                             as settle_platform_profit_rate
    FROM (  SELECT
						tm.id,
						tm.agent_level,
                        tm.merchant_code,
                        tm.merchant_name,
                        tm.parent_id,
						IFNULL(tma.terrace_rate,0) as terrace_rate,
						IFNULL(tma.terrace_rate, IFNULL(ml.terrace_rate, 0)) as execute_rate,
						IFNULL(tma.payment_cycle,'1') as payment_cycle,
						IFNULL(tma.vip_payment_cycle,'1') as vip_payment_cycle,
						IFNULL(tma.technique_payment_cycle,'1') as technique_payment_cycle,
						IFNULL(tma.computing_standard, '1') as computing_standard,
						0 as vip_amount,
						0 as technique_amount
						FROM tybss_new.t_merchant tm
						LEFT JOIN  tybss_new.t_merchant tma on tm.parent_id = tma.id
						LEFT JOIN tybss_new.t_merchant_level ml on tm.level = ml.level
						WHERE tm.agent_level = 2
						and tm.status = 1
					UNION
					SELECT
						tm.id,
						tm.agent_level,
                        tm.merchant_code,
                        tm.merchant_name,
                        tm.parent_id,
						IFNULL(tm.terrace_rate,0) as terrace_rate,
						IFNULL(tm.terrace_rate, IFNULL(ml.terrace_rate, 0)) as execute_rate,
						IFNULL(tm.payment_cycle,'1') as payment_cycle,
						IFNULL(tm.vip_payment_cycle,'1') as vip_payment_cycle,
						IFNULL(tm.technique_payment_cycle,'1') as technique_payment_cycle,
						IFNULL(tm.computing_standard, '1') as computing_standard,
						case tm.vip_payment_cycle
                           when 1 then tm.vip_amount
                           when 2 then (case
                                            WHEN month(CURRENT_DATE) in (1, 4, 7, 10) then tm.vip_amount
                                            else 0 end)
                           when 3 then (case WHEN month(CURRENT_DATE) = 1 then tm.vip_amount else 0 end)
                           else 0 end                                                                        as vip_amount,
                        case tm.technique_payment_cycle
                           when 1 then tm.technique_amount
                           when 2 then (case
                                            WHEN month(CURRENT_DATE) in (1, 4, 7, 10)
                                                then tm.technique_amount
                                            else 0 end)
                           when 3 then (case
                                            WHEN month(CURRENT_DATE) = 1 then tm.technique_amount
                                            else 0 end)
                           else 0 end                                                                        as technique_amount
						FROM tybss_new.t_merchant tm
						LEFT JOIN tybss_new.t_merchant_level ml on tm.level = ml.level
						WHERE tm.agent_level != 2
						and tm.status = 1
				) as m
             LEFT JOIN(SELECT u.merchant_code,
                              count(0) as transfer_num
                       FROM tybss_new.t_account_change_history ta
                                left join tybss_new.t_user u
                                          on u.uid = ta.uid
                       where ta.create_time >= unix_timestamp(execute_date) * 1000 + 12 * 60 * 60 * 1000
                         and ta.create_time <=
                             unix_timestamp(date_add(execute_date, INTERVAL 1 DAY)) * 1000 + (12 * 60 * 60 * 1000 - 1)
                         and u.merchant_code is not null
                       GROUP BY u.merchant_code) as x on x.merchant_code = m.merchant_code
             LEFT JOIN (SELECT a.merchant_id,
                               a.currency_code,
                               a.order_amount_total,
                               a.settle_amount,
                               IFNULL(a.order_num, 0) as order_num,
                               a.order_valid_num,
                               a.order_back_num,
                               a.order_user_num,
                               a.platform_profit,
                               ROUND(IFNULL(a.platform_profit/a.order_amount_total, 0)*100,2)  as platform_profit_rate,
                               b.settle_order_amount_total,
                               b.settle_settle_amount,
                               IFNULL(b.settle_order_num, 0) as settle_order_num,
                               b.settle_order_valid_num,
                               b.settle_order_back_num,
                               b.settle_order_user_num,
                               b.settle_platform_profit,
                               ROUND(IFNULL(b.settle_platform_profit/b.settle_order_amount_total, 0)*100,2)  as settle_platform_profit_rate,
                               b.profit_amount
                         FROM (SELECT o.merchant_id,
                                     o.currency_code,
                                     SUM(CASE WHEN o.order_status in (0, 1) THEN o.order_amount_total END)      AS order_amount_total,
                                     SUM(s.settle_amount)                                                       as settle_amount,
                                     count(0)                                                                   as order_num,
                                     Count(CASE WHEN o.order_status in (0, 1) THEN 1 END)                       AS order_valid_num,
                                     Count(CASE WHEN o.order_status in (2, 4, 5) THEN 1 END)                    AS order_back_num,
                                     Count(distinct o.uid)                                                      AS order_user_num,
                                     SUM(s.profit_amount) * -1                                                  as platform_profit
                              FROM tybss_new.t_order o
                              LEFT JOIN tybss_new.t_settle s ON o.order_no = s.order_no AND s.last_settle = 1
                              WHERE o.create_time >= unix_timestamp(execute_date) * 1000 + 12 * 60 * 60 * 1000
                                and o.create_time <= unix_timestamp(date_add(execute_date, INTERVAL 1 DAY)) * 1000 + (12 * 60 * 60 * 1000 - 1)
                                and o.order_status in (0, 1)
                              GROUP BY o.merchant_id, o.currency_code) a
                         LEFT JOIN(SELECT o.merchant_id,
                                     o.currency_code,
                                     SUM(CASE WHEN o.order_status in (0, 1) THEN o.order_amount_total END)      AS settle_order_amount_total,
                                     SUM(s.settle_amount)                                                       as settle_settle_amount,
                                     count(0)                                                                   as settle_order_num,
                                     Count(CASE WHEN o.order_status in (0, 1) THEN 1 END)                       AS settle_order_valid_num,
                                     Count(CASE WHEN o.order_status in (2, 4, 5) THEN 1 END)                    AS settle_order_back_num,
                                     Count(distinct o.uid)                                                      AS settle_order_user_num,
                                     SUM(s.profit_amount) * -1                                                  as settle_platform_profit,
                                     SUM(s.profit_amount) * -1                                                  as profit_amount
                                   FROM tybss_new.t_settle s
                                   LEFT JOIN tybss_new.t_order o on o.order_no = s.order_no
                                   where s.settle_time >= unix_timestamp(execute_date) * 1000 + 12 * 60 * 60 * 1000
                                     and s.settle_time <= unix_timestamp(date_add(execute_date, INTERVAL 1 DAY)) * 1000 + (12 * 60 * 60 * 1000 - 1)
                                     and s.last_settle = 1
                                     and o.order_status = 1
                                   GROUP BY o.merchant_id, o.currency_code) b
                         on a.merchant_id = b.merchant_id and a.currency_code = b.currency_code) as z
             on z.merchant_id = m.id
             LEFT JOIN (
				 SELECT o.merchant_id,
						Count(1)              AS num
                        FROM (SELECT o.order_no, CASE WHEN tm.agent_level =2 THEN tm.parent_id ELSE tm.id END as merchant_id
                                    FROM tybss_new.t_order o
                                    LEFT JOIN tybss_new.t_merchant tm on o.merchant_id = tm.id
                                    WHERE o.create_time >= unix_timestamp(execute_date) * 1000 + 12 * 60 * 60 * 1000
                                    and o.create_time <= unix_timestamp(date_add(execute_date, INTERVAL 1 DAY)) * 1000 + (12 * 60 * 60 * 1000 - 1)
                                    and o.order_status in (0, 1)
                                    ) o
                        GROUP BY o.merchant_id
			 ) as aa on aa.merchant_id = m.id
			 LEFT JOIN (
				 SELECT o.merchant_id,
						Count(1)              AS num
                        FROM (SELECT CASE WHEN tm.agent_level =2 THEN tm.parent_id ELSE tm.id END as merchant_id
                                   FROM tybss_new.t_settle s
                                   LEFT JOIN tybss_new.t_order o on o.order_no = s.order_no
                                   LEFT JOIN tybss_new.t_merchant tm on o.merchant_id = tm.id
                                   where s.settle_time >= unix_timestamp(execute_date) * 1000 + 12 * 60 * 60 * 1000
                                     and s.settle_time <= unix_timestamp(date_add(execute_date, INTERVAL 1 DAY)) * 1000 + (12 * 60 * 60 * 1000 - 1)
                                     and s.last_settle = 1
                                     and o.order_status = 1
                                    ) o
                        GROUP BY o.merchant_id
			 ) as bb on bb.merchant_id = m.id
    WHERE z.order_valid_num > 0 or z.settle_order_valid_num > 0 or aa.num > 0 or bb.num > 0;


##r_merchant_finance_day 自然日表新增 与账务日差别就是时间计算不一样
    DELETE FROM tybss_report.r_merchant_finance_day_utc8 WHERE finance_date = execute_date;
    INSERT INTO tybss_report.r_merchant_finance_day_utc8
    (id, finance_day_id, finance_date, finance_time, merchant_id, merchant_code, merchant_name, agent_level, currency,
    order_amount_total, settle_amount, order_num, order_valid_num, order_back_num, transfer_num, create_time, modify_time,
    updated_time, parent_id, merchant_num, profit_amount, computing_standard, terrace_rate, vip_amount, technique_amount,
    payment_cycle, vip_payment_cycle, technique_payment_cycle, execute_rate, platform_profit, order_user_num, platform_profit_rate,
    settle_order_amount_total, settle_settle_amount, settle_order_num, settle_order_valid_num, settle_order_back_num,
    settle_transfer_num, settle_platform_profit, settle_order_user_num, settle_platform_profit_rate)
    SELECT concat(execute_date, '-', m.merchant_code, '-', IFNULL(z.currency_code, '1'))         as id,
           concat(execute_date, '-', m.merchant_code)                                            as finance_day_id,
           execute_date                                                                          as finance_date,
           unix_timestamp(date_sub(execute_date, interval 1 day)) * 1000 + (12 * 60 * 60 * 1000) as finance_time,
           m.id                                                                                  as merchant_id,
           m.merchant_code,
           m.merchant_name,
           m.agent_level,
           IFNULL(z.currency_code, '1')                                                          as currency,
           IFNULL(z.order_amount_total, 0) / 100                                                 as order_amount_total,
           IFNULL(z.settle_amount, 0) / 100                                                      as settle_amount,
           IFNULL(z.order_num, 0)                                                                as order_num,
           IFNULL(z.order_valid_num, 0)                                                          as order_valid_num,
           IFNULL(z.order_back_num, 0)                                                           as order_back_num,
           IFNULL(x.transfer_num, 0)                                                             as transfer_num,
           unix_timestamp(now())                                                                 as create_time,
           unix_timestamp(now())                                                                 as modify_time,
           unix_timestamp(now())                                                                 as updated_time,
           m.parent_id                                                                           as parent_id,
           (SELECT count(id) FROM tybss_new.t_merchant WHERE status = 1 and parent_id = m.id)    as merchant_num,
           IFNULL(z.profit_amount, 0) / 100                                                      as profit_amount,
           m.computing_standard                                                               as computing_standard,
           m.terrace_rate                                                                     as terrace_rate,
           m.vip_amount                                                                       as vip_amount,
           m.technique_amount                                                                 as technique_amount,
           m.payment_cycle                                                                    as payment_cycle,
           m.vip_payment_cycle                                                                as vip_payment_cycle,
           m.technique_payment_cycle                                                          as technique_payment_cycle,
           m.execute_rate                                                                     as execute_rate,

           IFNULL(z.platform_profit, 0) / 100                                                   as platform_profit,
           IFNULL(z.order_user_num, 0)                                                          as order_user_num,
           IFNULL(z.platform_profit_rate, 0)                                                    as platform_profit_rate,
           IFNULL(z.settle_order_amount_total, 0)  / 100                                        as settle_order_amount_total,
           IFNULL(z.settle_settle_amount, 0) / 100                                              as settle_settle_amount,
           IFNULL(z.settle_order_num, 0)                                                        as settle_order_num,
           IFNULL(z.settle_order_valid_num, 0)                                                  as settle_order_valid_num,
           IFNULL(z.settle_order_back_num, 0)                                                   as settle_order_back_num,
           IFNULL(x.transfer_num, 0)                                                            as settle_transfer_num,
           IFNULL(z.settle_platform_profit, 0) / 100                                            as settle_platform_profit,
           IFNULL(z.settle_order_user_num, 0)                                                   as settle_order_user_num,
           IFNULL(z.settle_platform_profit_rate, 0)                                             as settle_platform_profit_rate
    FROM (  SELECT
						tm.id,
						tm.agent_level,
                        tm.merchant_code,
                        tm.merchant_name,
                        tm.parent_id,
						IFNULL(tma.terrace_rate,0) as terrace_rate,
						IFNULL(tma.terrace_rate, IFNULL(ml.terrace_rate, 0)) as execute_rate,
						IFNULL(tma.payment_cycle,'1') as payment_cycle,
						IFNULL(tma.vip_payment_cycle,'1') as vip_payment_cycle,
						IFNULL(tma.technique_payment_cycle,'1') as technique_payment_cycle,
						IFNULL(tma.computing_standard, '1') as computing_standard,
						0 as vip_amount,
						0 as technique_amount
						FROM tybss_new.t_merchant tm
						LEFT JOIN  tybss_new.t_merchant tma on tm.parent_id = tma.id
						LEFT JOIN tybss_new.t_merchant_level ml on tm.level = ml.level
						WHERE tm.agent_level = 2
						and tm.status = 1
					UNION
					SELECT
						tm.id,
						tm.agent_level,
                        tm.merchant_code,
                        tm.merchant_name,
                        tm.parent_id,
						IFNULL(tm.terrace_rate,0) as terrace_rate,
						IFNULL(tm.terrace_rate, IFNULL(ml.terrace_rate, 0)) as execute_rate,
						IFNULL(tm.payment_cycle,'1') as payment_cycle,
						IFNULL(tm.vip_payment_cycle,'1') as vip_payment_cycle,
						IFNULL(tm.technique_payment_cycle,'1') as technique_payment_cycle,
						IFNULL(tm.computing_standard, '1') as computing_standard,
						case tm.vip_payment_cycle
                           when 1 then tm.vip_amount
                           when 2 then (case
                                            WHEN month(CURRENT_DATE) in (1, 4, 7, 10) then tm.vip_amount
                                            else 0 end)
                           when 3 then (case WHEN month(CURRENT_DATE) = 1 then tm.vip_amount else 0 end)
                           else 0 end                                                                        as vip_amount,
                        case tm.technique_payment_cycle
                           when 1 then tm.technique_amount
                           when 2 then (case
                                            WHEN month(CURRENT_DATE) in (1, 4, 7, 10)
                                                then tm.technique_amount
                                            else 0 end)
                           when 3 then (case
                                            WHEN month(CURRENT_DATE) = 1 then tm.technique_amount
                                            else 0 end)
                           else 0 end                                                                        as technique_amount
						FROM tybss_new.t_merchant tm
						LEFT JOIN tybss_new.t_merchant_level ml on tm.level = ml.level
						WHERE tm.agent_level != 2
						and tm.status = 1
				) as m
             LEFT JOIN(SELECT u.merchant_code,
                              count(0) as transfer_num
                       FROM tybss_new.t_account_change_history ta
                                left join tybss_new.t_user u
                                          on u.uid = ta.uid
                       where ta.create_time >= unix_timestamp(execute_date) * 1000
                         and ta.create_time <= unix_timestamp(date_add(execute_date, INTERVAL 1 DAY)) * 1000 - 1
                         and u.merchant_code is not null
                       GROUP BY u.merchant_code) as x on x.merchant_code = m.merchant_code
             LEFT JOIN (SELECT a.merchant_id,
                               a.currency_code,
                               a.order_amount_total,
                               a.settle_amount,
                               IFNULL(a.order_num, 0) as order_num,
                               a.order_valid_num,
                               a.order_back_num,
                               a.order_user_num,
                               a.platform_profit,
                               ROUND(IFNULL(a.platform_profit/a.order_amount_total, 0)*100,2)  as platform_profit_rate,
                               b.settle_order_amount_total,
                               b.settle_settle_amount,
                               IFNULL(b.settle_order_num, 0) as settle_order_num,
                               b.settle_order_valid_num,
                               b.settle_order_back_num,
                               b.settle_order_user_num,
                               b.settle_platform_profit,
                               ROUND(IFNULL(b.settle_platform_profit/b.settle_order_amount_total, 0)*100,2)  as settle_platform_profit_rate,
                               b.profit_amount
                         FROM (SELECT o.merchant_id,
                                     o.currency_code,
                                     SUM(CASE WHEN o.order_status in (0, 1) THEN o.order_amount_total END)      AS order_amount_total,
                                     SUM(s.settle_amount)                                                       as settle_amount,
                                     count(0)                                                                   as order_num,
                                     Count(CASE WHEN o.order_status in (0, 1) THEN 1 END)                       AS order_valid_num,
                                     Count(CASE WHEN o.order_status in (2, 4, 5) THEN 1 END)                    AS order_back_num,
                                     Count(distinct o.uid)                                                      AS order_user_num,
                                     SUM(s.profit_amount) * -1                                                  as platform_profit
                              FROM tybss_new.t_order o
                              LEFT JOIN tybss_new.t_settle s ON o.order_no = s.order_no AND s.last_settle = 1
                              WHERE o.create_time >= unix_timestamp(execute_date) * 1000
                                and o.create_time <= unix_timestamp(date_add(execute_date, INTERVAL 1 DAY)) * 1000 - 1
                                and o.order_status in (0, 1)
                              GROUP BY o.merchant_id, o.currency_code) a
                         LEFT JOIN(SELECT o.merchant_id,
                                     o.currency_code,
                                     SUM(CASE WHEN o.order_status in (0, 1) THEN o.order_amount_total END)      AS settle_order_amount_total,
                                     SUM(s.settle_amount)                                                       as settle_settle_amount,
                                     count(0)                                                                   as settle_order_num,
                                     Count(CASE WHEN o.order_status in (0, 1) THEN 1 END)                       AS settle_order_valid_num,
                                     Count(CASE WHEN o.order_status in (2, 4, 5) THEN 1 END)                    AS settle_order_back_num,
                                     Count(distinct o.uid)                                                      AS settle_order_user_num,
                                     SUM(s.profit_amount) * -1                                                  as settle_platform_profit,
                                     SUM(s.profit_amount) * -1                                                  as profit_amount
                                   FROM tybss_new.t_settle s
                                   LEFT JOIN tybss_new.t_order o on o.order_no = s.order_no
                                   where s.settle_time >= unix_timestamp(execute_date) * 1000
                                     and s.settle_time <= unix_timestamp(date_add(execute_date, INTERVAL 1 DAY)) * 1000 - 1
                                     and s.last_settle = 1
                                     and o.order_status = 1
                                   GROUP BY o.merchant_id, o.currency_code) b
                         on a.merchant_id = b.merchant_id and a.currency_code = b.currency_code) as z
             on z.merchant_id = m.id
             LEFT JOIN (
				 SELECT o.merchant_id,
						Count(1)              AS num
                        FROM (SELECT o.order_no, CASE WHEN tm.agent_level =2 THEN tm.parent_id ELSE tm.id END as merchant_id
                                    FROM tybss_new.t_order o
                                    LEFT JOIN tybss_new.t_merchant tm on o.merchant_id = tm.id
                                    WHERE o.create_time >= unix_timestamp(execute_date) * 1000
                                    and o.create_time <= unix_timestamp(date_add(execute_date, INTERVAL 1 DAY)) * 1000 - 1
                                    and o.order_status in (0, 1)
                                    ) o
                        GROUP BY o.merchant_id
			 ) as aa on aa.merchant_id = m.id
			 LEFT JOIN (
				 SELECT o.merchant_id,
						Count(1)              AS num
                        FROM (SELECT CASE WHEN tm.agent_level =2 THEN tm.parent_id ELSE tm.id END as merchant_id
                                   FROM tybss_new.t_settle s
                                   LEFT JOIN tybss_new.t_order o on o.order_no = s.order_no
                                   LEFT JOIN tybss_new.t_merchant tm on o.merchant_id = tm.id
                                   where s.settle_time >= unix_timestamp(execute_date) * 1000
                                     and s.settle_time <= unix_timestamp(date_add(execute_date, INTERVAL 1 DAY)) * 1000 - 1
                                     and s.last_settle = 1
                                     and o.order_status = 1
                                    ) o
                        GROUP BY o.merchant_id
			 ) as bb on bb.merchant_id = m.id
    WHERE z.order_valid_num > 0 or z.settle_order_valid_num > 0 or aa.num > 0 or bb.num > 0;


    /*sql结束*/
    /*执行成功，添加日志*/
    SET end_time = get_cur_ymdhms();

    SET exce_msg = CONCAT(execute_date, " p_r_merchant_finance_day success!");

    CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
END//
DELIMITER ;

/**
CALL p_r_merchant_finance_day('2020-05-01');
账务日：2020-05-01 12：00：00   到 2020-05-02 11：59：59
自然日：2020-05-01 00：00：00   到 2020-05-01 23：59：59
 */



##执行
CALL  p_r_merchant_loop_exc('2020-05-01',DATE_FORMAT(DATE_ADD(now(),INTERVAL -1 DAY),'%Y-%m-%d'),'p_r_merchant_finance_day','1');
CALL  p_r_merchant_loop_exc('2020-05-01',DATE_FORMAT(DATE_ADD(now(),INTERVAL -1 DAY),'%Y-%m-%d'),'p_r_merchant_finance_month','3');