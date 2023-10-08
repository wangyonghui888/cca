DROP PROCEDURE if EXISTS `p_r_merchant_finance_day_big`;
DELIMITER //

CREATE PROCEDURE `p_r_merchant_finance_day_big`(in execute_date varchar(100))
BEGIN
    DECLARE task_type INT(2) DEFAULT 111;
    DECLARE result_code CHAR(5) DEFAULT '0';
    DECLARE start_time VARCHAR(30) DEFAULT get_cur_ymdhms();
    DECLARE end_time VARCHAR(30);
    DECLARE result INT(2) DEFAULT 1;
    DECLARE exce_msg VARCHAR(512) DEFAULT '成功';
    DECLARE msg TEXT;
    DECLARE endTimeL BIGINT(16);
    DECLARE startTimeL BIGINT(16);
    DECLARE endTimeUTCL BIGINT(16);
    DECLARE startTimeUTCL BIGINT(16);
    DECLARE EXIT
        HANDLER FOR SQLEXCEPTION BEGIN
        GET DIAGNOSTICS CONDITION 1 result_code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
        SET end_time = get_cur_ymdhms();
        SET exce_msg = CONCAT(execute_date, '错误码：', result_code, ' p_r_merchant_finance_day_big错误信息：', msg);
        SET result = 2;
        CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
    END;

    set startTimeL = unix_timestamp(execute_date) * 1000 + 12 * 60 * 60 * 1000;
    set endTimeL = unix_timestamp(date_add(execute_date, INTERVAL 1 DAY)) * 1000 + (12 * 60 * 60 * 1000 - 1);

    set startTimeUTCL = unix_timestamp(execute_date) * 1000;
    set endTimeUTCL = unix_timestamp(date_add(execute_date, INTERVAL 1 DAY)) * 1000 - 1;


##r_merchant_finance_day 自然日表新增 与账务日差别就是时间计算不一样
    replace INTO tybss_report.r_merchant_finance_day_utc8
    (id, finance_day_id, finance_date, finance_time, merchant_id, merchant_code, merchant_name, agent_level, currency,
     order_amount_total, settle_amount, order_valid_num, updated_time, parent_id, merchant_num, computing_standard,
     terrace_rate, vip_amount,
     technique_amount, payment_cycle, vip_payment_cycle, technique_payment_cycle, execute_rate, platform_profit,
     order_user_num, platform_profit_rate, settle_order_amount_total, settle_settle_amount, settle_order_num,
     settle_platform_profit, settle_order_user_num, settle_platform_profit_rate, vr_order_amount_total,
     vr_settle_amount, vr_platform_profit, vr_order_valid_num,
     vr_order_user_num, vr_settle_order_amount_total, vr_settle_settle_amount,
     vr_settle_platform_profit, vr_settle_order_num, vr_settle_order_user_num, t_order_user_num, t_settle_order_user,
     vip_level, original_order_amount_total, original_platform_profit, settle_original_order_amount_total,
     settle_original_platform_profit, vr_original_order_amount_total, vr_original_platform_profit,
     vr_settle_original_order_amount_total,
     vr_settle_original_platform_profit, order_valid_bet_money, original_order_valid_bet_acount,settle_valid_bet_money,original_settle_valid_bet_acount, bet_promo_tickets,
     settle_promo_tickets, cashout_amount, cashout_profit,
     settle_cashout_amount, settle_cashout_profit, cashout_tickets, cashout_users, settle_cashout_tickets,
     settle_cashout_users,original_cashout_profit_amount,original_cashout_amount,original_settle_cashout_money,original_settle_cashout_profit_amount)
    SELECT concat(execute_date, '-', m.merchant_code, '-', IFNULL(a.currency_code, '1'), '-', a.vipLevel) as id,
           concat(execute_date, '-', m.merchant_code)                                                     as finance_day_id,
           execute_date                                                                                   as finance_date,
           unix_timestamp(date_add(execute_date, INTERVAL 1 DAY)) * 1000 - 1                              as finance_time,
           m.id                                                                                           as merchant_id,
           m.merchant_code,
           m.merchant_name,
           m.agent_level,
           IFNULL(a.currency_code, '1')                                                                   as currency,
           IFNULL(b.order_amount_total, 0) / 100                                                          as order_amount_total,
           IFNULL(b.settle_amount, 0) / 100                                                               as settle_amount,
           IFNULL(b.order_valid_num, 0)                                                                   as order_valid_num,

           unix_timestamp(now())                                                                          as updated_time,
           m.parent_id                                                                                    as parent_id,
           1                                                                                              as merchant_num,
           m.computing_standard                                                                           as computing_standard,
           m.terrace_rate                                                                                 as terrace_rate,
           m.vip_amount                                                                                   as vip_amount,
           m.technique_amount                                                                             as technique_amount,
           m.payment_cycle                                                                                as payment_cycle,
           m.vip_payment_cycle                                                                            as vip_payment_cycle,
           m.technique_payment_cycle                                                                      as technique_payment_cycle,
           m.execute_rate                                                                                 as execute_rate,
           IFNULL(b.platform_profit, 0) / 100                                                             as platform_profit,
           IFNULL(b.order_user_num, 0)                                                                    as order_user_num,
           IFNULL(b.platform_profit / IF(b.order_amount_total = '0', NULL, b.order_amount_total), 0) *
           100                                                                                            as platform_profit_rate,

           IFNULL(a.settle_order_amount_total, 0) / 100                                                   as settle_order_amount_total,
           IFNULL(a.settle_settle_amount, 0) / 100                                                        as settle_settle_amount,
           IFNULL(a.settle_order_num, 0)                                                                  as settle_order_num,
           IFNULL(a.settle_platform_profit, 0) / 100                                                      as settle_platform_profit,
           IFNULL(a.settle_order_user_num, 0)                                                             as settle_order_user_num,
           IFNULL(a.settle_platform_profit / IF(a.settle_order_amount_total = '0', NULL, a.settle_order_amount_total),
                  0) * 100
                                                                                                          as settle_platform_profit_rate,

           IFNULL(b.vr_order_amount_total, 0) / 100                                                       as vr_order_amount_total,
           IFNULL(b.vr_settle_amount, 0) / 100                                                            as vr_settle_amount,
           IFNULL(b.vr_platform_profit, 0) / 100                                                          as vr_platform_profit,
           IFNULL(b.vr_order_valid_num, 0)                                                                as vr_order_valid_num,
           IFNULL(b.vr_order_user_num, 0)                                                                 as vr_order_user_num,

           IFNULL(a.vr_settle_order_amount_total, 0) / 100                                                as vr_settle_order_amount_total,
           IFNULL(a.vr_settle_settle_amount, 0) / 100                                                     as vr_settle_settle_amount,
           IFNULL(a.vr_settle_platform_profit, 0) / 100                                                   as vr_settle_platform_profit,
           IFNULL(a.vr_settle_order_num, 0)                                                               as vr_settle_order_num,
           IFNULL(a.vr_settle_order_user_num, 0)                                                          as vr_settle_order_user_num,

           IFNULL(b.t_order_user, 0)                                                                      as t_order_user,
           IFNULL(a.t_settle_order_user, 0)                                                               as t_settle_order_user,
           a.vipLevel,
           IFNULL(b.original_order_amount_total, 0) / 100                                                 as original_order_amount_total,
           IFNULL(b.original_platform_profit, 0) / 100                                                    as original_platform_profit,

           IFNULL(a.settle_original_order_amount_total, 0) / 100                                          as settle_original_order_amount_total,
           IFNULL(a.settle_original_platform_profit, 0) / 100                                             as settle_original_platform_profit,
           IFNULL(b.vr_original_order_amount_total, 0) / 100                                              as vr_original_order_amount_total,
           IFNULL(b.vr_original_platform_profit, 0) / 100                                                 as vr_original_platform_profit,
           IFNULL(a.vr_settle_original_order_amount_total, 0) / 100                                       as vr_settle_original_order_amount_total,
           IFNULL(a.vr_settle_original_platform_profit, 0) / 100                                          as vr_settle_original_platform_profit,

           TRUNCATE(IFNULL(b.order_valid_bet_money, 0),2)                                                      as order_valid_bet_money,
           TRUNCATE(IFNULL(b.original_order_valid_bet_acount, 0),2)                                             as original_order_valid_bet_acount,
           TRUNCATE(IFNULL(a.settle_valid_bet_money, 0),2)                                                     as settle_valid_bet_money,
           TRUNCATE(IFNULL(a.original_settle_valid_bet_acount, 0),2)                                            as original_settle_valid_bet_acount,

           IFNULL(b.betPromoTickets, 0)                                                                   as betPromoTickets,
           IFNULL(a.settlePromoTickets, 0)                                                                as settlePromoTickets,

           IFNULL(b.cashout_amount / 100, 0)                                                              as cashout_amount,
           IFNULL(b.cashout_profit / 100, 0)                                                              as cashout_profit,

           IFNULL(a.cashout_amount / 100, 0)                                                              as settle_cashout_amount,

           IFNULL(a.cashout_profit / 100, 0)                                                              as settle_cashout_profit,

           IFNULL(a.cashout_tickets, 0)                                                                   as cashout_tickets,
           IFNULL(a.cashout_users, 0)                                                                     as cashout_users,
           IFNULL(b.cashout_tickets, 0)                                                                   as settle_cashout_tickets,
           IFNULL(b.cashout_users, 0)                                                                     as settle_cashout_users,
           IFNULL(b.original_cashout_profit_amount,0) / 100                                                as original_cashout_profit_amount,
           IFNULL(b.original_cashout_amount,0) / 100                                                       as original_cashout_amount,
           IFNULL(a.original_settle_cashout_money,0) / 100                                                 as original_settle_cashout_money,
           IFNULL(a.original_settle_cashout_profit_amount,0) / 100                                         as original_settle_cashout_profit_amount
    FROM (SELECT tm.id,
                 tm.agent_level,
                 tm.merchant_code,
                 tm.merchant_name,
                 tm.parent_id,
                 IFNULL(tm.terrace_rate, 0)                          as terrace_rate,
                 IFNULL(tm.terrace_rate, IFNULL(ml.terrace_rate, 0)) as execute_rate,
                 IFNULL(tm.payment_cycle, '1')                       as payment_cycle,
                 IFNULL(tm.vip_payment_cycle, '1')                   as vip_payment_cycle,
                 IFNULL(tm.technique_payment_cycle, '1')             as technique_payment_cycle,
                 IFNULL(tm.computing_standard, '1')                  as computing_standard,
                 case tm.vip_payment_cycle
                     when 1 and tm.agent_level = 0 then tm.vip_amount
                     when 2 and tm.agent_level = 0 then (case
                                                             WHEN month(CURRENT_DATE) in (1, 4, 7, 10)
                                                                 then tm.vip_amount
                                                             else 0 end)
                     when 3 and tm.agent_level = 0
                         then (case WHEN month(CURRENT_DATE) = 1 then tm.vip_amount else 0 end)
                     else 0 end                                      as vip_amount,
                 case tm.technique_payment_cycle
                     when 1 and tm.agent_level = 0 then tm.technique_amount
                     when 2 and tm.agent_level = 0 then (case
                                                             WHEN month(CURRENT_DATE) in (1, 4, 7, 10)
                                                                 then tm.technique_amount
                                                             else 0 end)
                     when 3 and tm.agent_level = 0 then (case
                                                             WHEN month(CURRENT_DATE) = 1 then tm.technique_amount
                                                             else 0 end)
                     else 0 end                                      as technique_amount
          FROM tybss_merchant_common.t_merchant tm
                   LEFT JOIN tybss_merchant_common.t_merchant_level ml on tm.level = ml.level
          WHERE tm.agent_level in (0, 2)) m
             LEFT JOIN (SELECT s.merchant_code,
                               s.currency_code,
                               IFNULL(s.vip_level, 0)                                             as vipLevel,
                               SUM(s.order_amount_total)                                          AS settle_order_amount_total,
                               SUM(s.settle_amount)                                               as settle_settle_amount,
                               count(distinct s.order_no)                                         as settle_order_num,
                               Count(distinct s.uid)                                              AS settle_order_user_num,
                               SUM(s.profit_amount) * -1                                          as settle_platform_profit,

                               SUM(case when s.manager_code = 3 then s.order_amount_total end)    AS vr_settle_order_amount_total,
                               SUM(case when s.manager_code = 3 then s.settle_amount end)         as vr_settle_settle_amount,
                               count(distinct (case when s.manager_code = 3 then s.order_no end)) as vr_settle_order_num,
                               Count(distinct (case when s.manager_code = 3 then s.uid end))      AS vr_settle_order_user_num,
                               Count(distinct (case when s.manager_code != 3 then s.uid end))     AS t_settle_order_user,
                               SUM(case when s.manager_code = 3 then s.profit_amount end) * -1    as vr_settle_platform_profit,

                               SUM(s.original_order_amount_total)                                          as settle_original_order_amount_total,
                               SUM(s.original_profit_amount) * -1                                 as settle_original_platform_profit,
                               SUM(case when s.manager_code = 3 then s.order_amount_total end)    as vr_settle_original_order_amount_total,
                               SUM(case when s.manager_code = 3 then s.original_profit_amount end) *
                               -1                                                                 as vr_settle_original_platform_profit,
                               SUM(CASE
                                       WHEN s.out_come in (3, 4, 5, 6) and
                                            ABS(s.order_amount_total) = ABS(s.profit_amount)
                                           THEN ABS(s.profit_amount / 100)
                                       WHEN s.out_come in (3, 4, 5, 6) and
                                            ABS(s.order_amount_total) > ABS(s.profit_amount)
                                           THEN ABS(s.profit_amount / 100)
                                       WHEN s.out_come in (3, 4, 5, 6) and
                                            ABS(s.order_amount_total) < ABS(s.profit_amount)
                                           THEN ABS(s.order_amount_total / 100)
                                   END)                                                           as settle_valid_bet_money,

                         SUM(CASE
                                      WHEN s.out_come in (3, 4, 5, 6) and
                                           ABS(s.original_order_amount_total) = ABS(s.original_profit_amount)
                                          THEN ABS(s.original_profit_amount / 100)
                                      WHEN s.out_come in (3, 4, 5, 6) and
                                           ABS(s.original_order_amount_total) > ABS(s.original_profit_amount)
                                          THEN ABS(s.original_profit_amount / 100)
                                      WHEN s.out_come in (3, 4, 5, 6) and
                                           ABS(s.original_order_amount_total) < ABS(s.original_profit_amount)
                                          THEN ABS(s.original_order_amount_total / 100) END)              as original_settle_valid_bet_acount,

                               count(CASE
                                         WHEN s.out_come in (3, 4, 5, 6) and
                                              ABS(s.order_amount_total) = ABS(s.profit_amount)
                                             THEN s.order_no
                                         WHEN s.out_come in (3, 4, 5, 6) and
                                              ABS(s.order_amount_total) > ABS(s.profit_amount)
                                             THEN s.order_no
                                         WHEN s.out_come in (3, 4, 5, 6) and
                                              ABS(s.order_amount_total) < ABS(s.profit_amount)
                                             THEN s.order_no
                                   END)                                                           as settlePromoTickets,

                               SUM(case when s.pre_bet_amount > 0 then s.profit_amount end)       as cashout_profit,
                               SUM(s.pre_bet_amount)                                              as cashout_amount,

                               COUNT(case when s.pre_bet_amount > 0 then s.order_no end)          as cashout_tickets,
                               COUNT(distinct s.uid)                                              as cashout_users,
                               SUM(s.original_pre_bet_amount)            as original_settle_cashout_money,
                               SUM(case when s.original_pre_bet_amount > 0 then s.original_profit_amount end)             as original_settle_cashout_profit_amount
                        FROM t_ticket s
                        where s.settle_time >= startTimeUTCL
                          and s.settle_time <= endTimeUTCL
                        GROUP BY s.merchant_code, s.currency_code, vipLevel) a on a.merchant_code = m.merchant_code
             LEFT JOIN (SELECT o.merchant_code,
                               o.currency_code,
                               IFNULL(o.vip_level, 0)                                               as vipLevel,
                               SUM(o.order_amount_total)                                            AS order_amount_total,
                               SUM(o.settle_amount)                                                 as settle_amount,
                               Count(case when o.order_status in (0, 1) then o.order_no else 0 end) AS order_valid_num,
                               Count(distinct o.uid)                                                AS order_user_num,
                               SUM(o.profit_amount) * -1                                            as platform_profit,

                               SUM(case when o.manager_code = 3 then o.order_amount_total end)      AS vr_order_amount_total,
                               SUM(case when o.manager_code = 3 then o.settle_amount end)           as vr_settle_amount,
                               Count(case when o.manager_code = 3 then o.order_no end)              AS vr_order_valid_num,
                               Count(distinct (case when o.manager_code = 3 then o.uid end))        AS vr_order_user_num,
                               Count(distinct (case when o.manager_code != 3 then o.uid end))       AS t_order_user,
                               SUM(case when o.manager_code = 3 then o.profit_amount end) * -1      as vr_platform_profit,

                               SUM(o.original_order_amount_total)                                   as original_order_amount_total,
                               SUM(o.original_profit_amount) * -1                                   as original_platform_profit,
                               SUM(case when o.manager_code = 3 then o.original_order_amount_total end)
                                                                                                    as vr_original_order_amount_total,
                               SUM(case when o.manager_code = 3 then o.original_profit_amount end) *
                               -1                                                                   as vr_original_platform_profit,
                               SUM(CASE
                                       WHEN o.out_come in (3, 4, 5, 6) and
                                            ABS(o.order_amount_total) = ABS(o.profit_amount)
                                           THEN ABS(o.profit_amount / 100)
                                       WHEN o.out_come in (3, 4, 5, 6) and
                                            ABS(o.order_amount_total) > ABS(o.profit_amount)
                                           THEN ABS(o.profit_amount / 100)
                                       WHEN o.out_come in (3, 4, 5, 6) and
                                            ABS(o.order_amount_total) < ABS(o.profit_amount)
                                           THEN ABS(o.order_amount_total / 100)
                                   END)                                                             as order_valid_bet_money,

                          SUM(CASE
                                       WHEN o.out_come in (3, 4, 5, 6) and
                                            ABS(o.original_order_amount_total) = ABS(o.original_profit_amount)
                                           THEN ABS(o.original_profit_amount / 100)
                                       WHEN o.out_come in (3, 4, 5, 6) and
                                            ABS(o.original_order_amount_total) > ABS(o.original_profit_amount)
                                           THEN ABS(o.original_profit_amount / 100)
                                       WHEN o.out_come in (3, 4, 5, 6) and
                                            ABS(o.original_order_amount_total) < ABS(o.original_profit_amount)
                                           THEN ABS(o.original_order_amount_total / 100) END)                    as original_order_valid_bet_acount,

                               count(CASE
                                         WHEN o.out_come in (3, 4, 5, 6) and
                                              ABS(o.order_amount_total) = ABS(o.profit_amount)
                                             THEN o.order_no
                                         WHEN o.out_come in (3, 4, 5, 6) and
                                              ABS(o.order_amount_total) > ABS(o.profit_amount)
                                             THEN o.order_no
                                         WHEN o.out_come in (3, 4, 5, 6) and
                                              ABS(o.order_amount_total) < ABS(o.profit_amount)
                                             THEN o.order_no
                                   END)                                                             as betPromoTickets,

                               SUM(case when o.pre_bet_amount > 0 then o.profit_amount end)         as cashout_profit,
                               SUM(o.pre_bet_amount)                                                as cashout_amount,
                               COUNT(case when o.pre_bet_amount > 0 then o.order_no end)            as cashout_tickets,
                               COUNT(distinct o.uid)                                                as cashout_users,
                               SUM(case when o.original_pre_bet_amount > 0 then o.original_profit_amount end)             as original_cashout_profit_amount,
                               SUM(o.original_pre_bet_amount)                                                             as original_cashout_amount
                        FROM t_ticket o
                        WHERE o.create_time >= startTimeUTCL
                          and o.create_time <= endTimeUTCL
                        GROUP BY o.merchant_code, o.currency_code, vipLevel) b
                       on b.merchant_code = m.merchant_code and a.vipLevel = b.vipLevel and
                          a.currency_code = b.currency_code
    WHERE b.order_valid_num > 0
       or a.settle_order_num > 0;


    replace INTO tybss_report.r_merchant_finance_day_utc8
    (id, finance_day_id, finance_date, finance_time, merchant_id, merchant_code, merchant_name, agent_level, currency,
     order_amount_total, settle_amount, order_valid_num,
     updated_time, parent_id, merchant_num, computing_standard, terrace_rate, vip_amount,
     technique_amount, payment_cycle, vip_payment_cycle, technique_payment_cycle, execute_rate, platform_profit,
     order_user_num, platform_profit_rate, settle_order_amount_total, settle_settle_amount, settle_order_num,
     settle_platform_profit, settle_order_user_num,
     settle_platform_profit_rate, vr_order_amount_total, vr_settle_amount, vr_platform_profit, vr_order_valid_num,
     vr_order_user_num, vr_settle_order_amount_total, vr_settle_settle_amount,
     vr_settle_platform_profit, vr_settle_order_num, vr_settle_order_user_num, t_order_user_num, t_settle_order_user,
     vip_level, original_order_amount_total, original_platform_profit, settle_original_order_amount_total,
     settle_original_platform_profit,
     vr_original_order_amount_total, vr_original_platform_profit, vr_settle_original_order_amount_total,
     vr_settle_original_platform_profit, order_valid_bet_money,original_order_valid_bet_acount,settle_valid_bet_money,original_settle_valid_bet_acount, bet_promo_tickets,
     settle_promo_tickets, cashout_amount, cashout_profit, settle_cashout_amount, settle_cashout_profit,
     cashout_tickets, cashout_users, settle_cashout_tickets, settle_cashout_users,
        original_cashout_profit_amount,original_cashout_amount,original_settle_cashout_money,original_settle_cashout_profit_amount)
    SELECT concat(execute_date, '-', m.merchant_code, '-', IFNULL(bb.currency_code, '1'), '-', vip_level) as id,
           concat(execute_date, '-', m.merchant_code)                                                     as finance_day_id,
           execute_date                                                                                   as finance_date,
           unix_timestamp(date_add(execute_date, INTERVAL 1 DAY)) * 1000 - 1                              as finance_time,
           m.id                                                                                           as merchant_id,
           m.merchant_code,
           m.merchant_name,
           m.agent_level,
           IFNULL(bb.currency_code, '1')                                                                  as currency,
           IFNULL(bb.order_amount_total, 0)                                                               as order_amount_total,
           IFNULL(bb.settle_amount, 0)                                                                    as settle_amount,
           IFNULL(bb.order_valid_num, 0)                                                                  as order_valid_num,
           unix_timestamp(now())                                                                          as updated_time,
           m.parent_id                                                                                    as parent_id,
           (SELECT count(id)
            FROM tybss_merchant_common.t_merchant
            WHERE status = 1
              and parent_id = m.id)                                                                       as merchant_num,
           m.computing_standard                                                                           as computing_standard,
           m.terrace_rate                                                                                 as terrace_rate,
           m.vip_amount                                                                                   as vip_amount,
           m.technique_amount                                                                             as technique_amount,
           m.payment_cycle                                                                                as payment_cycle,
           m.vip_payment_cycle                                                                            as vip_payment_cycle,
           m.technique_payment_cycle                                                                      as technique_payment_cycle,
           m.execute_rate                                                                                 as execute_rate,
           IFNULL(bb.platform_profit, 0)                                                                  as platform_profit,
           IFNULL(bb.order_user_num, 0)                                                                   as order_user_num,
           IFNULL(bb.platform_profit_rate, 0)                                                             as platform_profit_rate,
           IFNULL(bb.settle_order_amount_total, 0)                                                        as settle_order_amount_total,
           IFNULL(bb.settle_settle_amount, 0)                                                             as settle_settle_amount,
           IFNULL(bb.settle_order_num, 0)                                                                 as settle_order_num,
           IFNULL(bb.settle_platform_profit, 0)                                                           as settle_platform_profit,
           IFNULL(bb.settle_order_user_num, 0)                                                            as settle_order_user_num,
           IFNULL(bb.settle_platform_profit_rate, 0)                                                      as settle_platform_profit_rate,

           IFNULL(bb.vr_order_amount_total, 0)                                                            as vr_order_amount_total,
           IFNULL(bb.vr_settle_amount, 0)                                                                 as vr_settle_amount,
           IFNULL(bb.vr_platform_profit, 0)                                                               as vr_platform_profit,
           IFNULL(bb.vr_order_valid_num, 0)                                                               as vr_order_valid_num,
           IFNULL(bb.vr_order_user_num, 0)                                                                as vr_order_user_num,
           IFNULL(bb.vr_settle_order_amount_total, 0)                                                     as vr_settle_order_amount_total,
           IFNULL(bb.vr_settle_settle_amount, 0)                                                          as vr_settle_settle_amount,
           IFNULL(bb.vr_settle_platform_profit, 0)                                                        as vr_settle_platform_profit,
           IFNULL(bb.vr_settle_order_num, 0)                                                              as vr_settle_order_num,
           IFNULL(bb.vr_settle_order_user_num, 0)                                                         as vr_settle_order_user_num,
           IFNULL(bb.t_order_user_num, 0)                                                                 as t_order_user_num,
           IFNULL(bb.t_settle_order_user, 0)                                                              as t_settle_order_user,

           vip_level,
           IFNULL(original_order_amount_total, 0)                                                         as original_order_amount_total,
           IFNULL(original_platform_profit, 0)                                                            as original_platform_profit,
           IFNULL(settle_original_order_amount_total, 0)                                                  as settle_original_order_amount_total,
           IFNULL(settle_original_platform_profit, 0)                                                     as settle_original_platform_profit,
           IFNULL(vr_original_order_amount_total, 0)                                                      as vr_original_order_amount_total,
           IFNULL(vr_original_platform_profit, 0)                                                         as vr_original_platform_profit,
           IFNULL(vr_settle_original_order_amount_total, 0)                                               as vr_settle_original_order_amount_total,
           IFNULL(vr_settle_original_platform_profit, 0)                                                  as vr_settle_original_platform_profit,
           TRUNCATE(IFNULL(order_valid_bet_money, 0),2)                                                        as order_valid_bet_money,
           TRUNCATE(IFNULL(original_order_valid_bet_acount, 0),2)                                              as original_order_valid_bet_acount,
           TRUNCATE(IFNULL(settle_valid_bet_money, 0),2)                                                       as settle_valid_bet_money,
           TRUNCATE(IFNULL(original_settle_valid_bet_acount, 0),2)                                             as original_settle_valid_bet_acount,
           IFNULL(bb.bet_promo_tickets, 0)                                                                as betPromoTickets,
           IFNULL(bb.settle_promo_tickets, 0)                                                             as settlePromoTickets,

           IFNULL(bb.cashout_amount, 0)                                                                   as cashout_amount,
           IFNULL(bb.cashout_profit, 0)                                                                   as cashout_profit,
           IFNULL(bb.settle_cashout_amount, 0)                                                            as settle_cashout_amount,
           IFNULL(bb.settle_cashout_profit, 0)                                                            as settle_cashout_profit,
           IFNULL(bb.cashout_tickets, 0)                                                                  as cashout_tickets,
           IFNULL(bb.cashout_users, 0)                                                                    as cashout_users,
           IFNULL(bb.settle_cashout_tickets, 0)                                                           as settle_cashout_tickets,
           IFNULL(bb.settle_cashout_users, 0)                                                             as settle_cashout_users,

           IFNULL(bb.original_cashout_profit_amount,0) / 100                                                as original_cashout_profit_amount,
           IFNULL(bb.original_cashout_amount,0) / 100                                                       as original_cashout_amount,
           IFNULL(bb.original_settle_cashout_money,0) / 100                                                 as original_settle_cashout_money,
           IFNULL(bb.original_settle_cashout_profit_amount,0) / 100                                         as original_settle_cashout_profit_amount
    FROM (SELECT tm.id,
                 tm.agent_level,
                 tm.merchant_code,
                 tm.merchant_name,
                 tm.parent_id,
                 IFNULL(tm.terrace_rate, 0)                          as terrace_rate,
                 IFNULL(tm.terrace_rate, IFNULL(ml.terrace_rate, 0)) as execute_rate,
                 IFNULL(tm.payment_cycle, '1')                       as payment_cycle,
                 IFNULL(tm.vip_payment_cycle, '1')                   as vip_payment_cycle,
                 IFNULL(tm.technique_payment_cycle, '1')             as technique_payment_cycle,
                 IFNULL(tm.computing_standard, '1')                  as computing_standard,
                 case tm.vip_payment_cycle
                     when 1 then tm.vip_amount
                     when 2 then (case
                                      WHEN month(CURRENT_DATE) in (1, 4, 7, 10) then tm.vip_amount
                                      else 0 end)
                     when 3 then (case WHEN month(CURRENT_DATE) = 1 then tm.vip_amount else 0 end)
                     else 0 end                                      as vip_amount,
                 case tm.technique_payment_cycle
                     when 1 then tm.technique_amount
                     when 2 then (case
                                      WHEN month(CURRENT_DATE) in (1, 4, 7, 10)
                                          then tm.technique_amount
                                      else 0 end)
                     when 3 then (case
                                      WHEN month(CURRENT_DATE) = 1 then tm.technique_amount
                                      else 0 end)
                     else 0 end                                      as technique_amount
          FROM tybss_merchant_common.t_merchant tm
                   LEFT JOIN tybss_merchant_common.t_merchant_level ml on tm.level = ml.level
          WHERE tm.agent_level = 1) as m
             LEFT JOIN(select parent_id,
                              currency                                   as currency_code,
                              IFNUll(vip_level, 0)                       as vip_level,
                              sum(order_amount_total)                    as order_amount_total,
                              sum(settle_amount)                         as settle_amount,
                              sum(order_valid_num)                       as order_valid_num,
                              sum(merchant_num)                          as merchant_num,
                              sum(platform_profit)                       as platform_profit,
                              sum(order_user_num)                        as order_user_num,
                              IFNULL(sum(platform_profit) /
                                     IF(sum(order_amount_total) = '0', NULL, sum(order_amount_total)), 0) * 100
                                                                         as platform_profit_rate,
                              sum(settle_order_amount_total)             as settle_order_amount_total,
                              sum(settle_settle_amount)                  as settle_settle_amount,
                              sum(settle_order_num)                      as settle_order_num,
                              sum(settle_platform_profit)                as settle_platform_profit,
                              sum(settle_order_user_num)                 as settle_order_user_num,
                              IFNULL(sum(settle_platform_profit) /
                                     IF(sum(settle_order_amount_total) = '0', NULL, sum(settle_order_amount_total)),
                                     0) * 100
                                                                         as settle_platform_profit_rate,
                              SUM(vr_order_amount_total)                 AS vr_order_amount_total,
                              SUM(vr_settle_amount)                      as vr_settle_amount,
                              SUM(vr_order_valid_num)                    AS vr_order_valid_num,
                              SUM(vr_order_user_num)                     AS vr_order_user_num,
                              SUM(vr_platform_profit)                    as vr_platform_profit,

                              SUM(vr_settle_order_amount_total)          AS vr_settle_order_amount_total,
                              SUM(vr_settle_settle_amount)               as vr_settle_settle_amount,
                              SUM(vr_settle_order_num)                   as vr_settle_order_num,
                              SUM(vr_settle_order_user_num)              AS vr_settle_order_user_num,
                              SUM(t_order_user_num)                      AS t_order_user_num,
                              SUM(t_settle_order_user)                   AS t_settle_order_user,
                              SUM(vr_settle_platform_profit)             as vr_settle_platform_profit,

                              SUM(original_order_amount_total)           as original_order_amount_total,
                              SUM(original_platform_profit)              as original_platform_profit,
                              SUM(settle_original_order_amount_total)    as settle_original_order_amount_total,
                              SUM(settle_original_platform_profit)       as settle_original_platform_profit,
                              SUM(vr_original_order_amount_total)        as vr_original_order_amount_total,
                              SUM(vr_original_platform_profit)           as vr_original_platform_profit,
                              SUM(vr_settle_original_order_amount_total) as vr_settle_original_order_amount_total,
                              SUM(vr_settle_original_platform_profit)    as vr_settle_original_platform_profit,
                              SUM(order_valid_bet_money)                 as order_valid_bet_money,
                              SUM(original_order_valid_bet_acount)       as original_order_valid_bet_acount,
                              SUM(settle_valid_bet_money)                as settle_valid_bet_money,
                              SUM(original_settle_valid_bet_acount)      as original_settle_valid_bet_acount,
                              SUM(bet_promo_tickets)                     as bet_promo_tickets,
                              SUM(settle_promo_tickets)                  as settle_promo_tickets,
                              sum(cashout_amount)                           cashout_amount,
                              sum(cashout_profit)                           cashout_profit,
                              sum(settle_cashout_amount)                    settle_cashout_amount,
                              sum(settle_cashout_profit)                    settle_cashout_profit,
                              sum(cashout_tickets)                          cashout_tickets,
                              sum(cashout_users)                            cashout_users,
                              sum(settle_cashout_tickets)                   settle_cashout_tickets,
                              sum(settle_cashout_users)                     settle_cashout_users,
                              sum(original_cashout_profit_amount)           original_cashout_profit_amount,
                              sum(original_cashout_amount)                  original_cashout_amount,
                              sum(original_settle_cashout_money)            original_settle_cashout_money,
                              sum(original_settle_cashout_profit_amount)    original_settle_cashout_profit_amount
                       from r_merchant_finance_day_utc8
                       where finance_date = execute_date
                         and (order_amount_total > 0 or settle_order_amount_total > 0)
                         and agent_level in (0, 2)
                       GROUP BY parent_id, currency, vip_level) as bb on bb.parent_id = m.id
    where bb.order_valid_num > 0
       or bb.settle_order_num > 0;

    SET end_time = get_cur_ymdhms();
    SET exce_msg = CONCAT(execute_date, ',p_r_merchant_finance_day_big,utc8 done!', startTimeUTCL, ',', endTimeUTCL);
    CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);


    ##r_merchant_finance_day 账务日表新增  此处不使用replace
    replace INTO tybss_report.r_merchant_finance_day
    (id, finance_day_id, finance_date, finance_time, merchant_id, merchant_code, merchant_name, agent_level, currency,
     order_amount_total, settle_amount, order_valid_num,
     updated_time, parent_id, merchant_num, computing_standard, terrace_rate, vip_amount,
     technique_amount, payment_cycle, vip_payment_cycle, technique_payment_cycle, execute_rate, platform_profit,
     order_user_num, platform_profit_rate, settle_order_amount_total, settle_settle_amount, settle_order_num,
     settle_platform_profit, settle_order_user_num,
     settle_platform_profit_rate, vr_order_amount_total, vr_settle_amount, vr_platform_profit, vr_order_valid_num,
     vr_order_user_num, vr_settle_order_amount_total, vr_settle_settle_amount,
     vr_settle_platform_profit, vr_settle_order_num, vr_settle_order_user_num, t_order_user_num, t_settle_order_user,
     vip_level, original_order_amount_total, original_platform_profit, settle_original_order_amount_total,
     settle_original_platform_profit, vr_original_order_amount_total, vr_original_platform_profit,
     vr_settle_original_order_amount_total, vr_settle_original_platform_profit,
     order_valid_bet_money,original_order_valid_bet_acount, settle_valid_bet_money,original_settle_valid_bet_acount, bet_promo_tickets, settle_promo_tickets, cashout_amount,
     cashout_profit, settle_cashout_amount,
     settle_cashout_profit, cashout_tickets, cashout_users, settle_cashout_tickets, settle_cashout_users)
    SELECT concat(execute_date, '-', m.merchant_code, '-', IFNULL(a.currency_code, '1'), '-', a.vip_level) as id,
           concat(execute_date, '-', m.merchant_code)                                                      as finance_day_id,
           execute_date                                                                                    as finance_date,
           unix_timestamp(date_sub(execute_date, interval 1 day)) * 1000 +
           (12 * 60 * 60 * 1000)                                                                           as finance_time,
           m.id                                                                                            as merchant_id,
           m.merchant_code,
           m.merchant_name,
           m.agent_level,
           IFNULL(a.currency_code, '1')                                                                    as currency,
           IFNULL(a.order_amount_total, 0) / 100                                                           as order_amount_total,
           IFNULL(a.settle_amount, 0) / 100                                                                as settle_amount,
           IFNULL(a.order_valid_num, 0)                                                                    as order_valid_num,
           unix_timestamp(now())                                                                           as updated_time,
           m.parent_id                                                                                     as parent_id,
           1                                                                                               as merchant_num,
           m.computing_standard                                                                            as computing_standard,
           m.terrace_rate                                                                                  as terrace_rate,
           m.vip_amount                                                                                    as vip_amount,
           m.technique_amount                                                                              as technique_amount,
           m.payment_cycle                                                                                 as payment_cycle,
           m.vip_payment_cycle                                                                             as vip_payment_cycle,
           m.technique_payment_cycle                                                                       as technique_payment_cycle,
           m.execute_rate                                                                                  as execute_rate,
           IFNULL(a.platform_profit, 0) / 100                                                              as platform_profit,
           IFNULL(a.order_user_num, 0)                                                                     as order_user_num,
           ROUND(IFNULL(a.platform_profit / a.order_amount_total, 0) * 100, 2)                             as platform_profit_rate,
           IFNULL(b.settle_order_amount_total, 0) / 100                                                    as settle_order_amount_total,
           IFNULL(b.settle_settle_amount, 0) / 100                                                         as settle_settle_amount,
           IFNULL(b.settle_order_num, 0)                                                                   as settle_order_num,
           IFNULL(b.settle_platform_profit, 0) / 100                                                       as settle_platform_profit,
           IFNULL(b.settle_order_user_num, 0)                                                              as settle_order_user_num,
           ROUND(IFNULL(b.settle_platform_profit / b.settle_order_amount_total, 0) * 100,
                 2)                                                                                        as settle_platform_profit_rate,

           IFNULL(a.vr_order_amount_total, 0) / 100                                                        as vr_order_amount_total,
           IFNULL(a.vr_settle_amount, 0) / 100                                                             as vr_settle_amount,
           IFNULL(a.vr_platform_profit, 0) / 100                                                           as vr_platform_profit,
           IFNULL(a.vr_order_valid_num, 0)                                                                 as vr_order_valid_num,
           IFNULL(a.vr_order_user_num, 0)                                                                  as vr_order_user_num,
           IFNULL(b.vr_settle_order_amount_total, 0) / 100                                                 as vr_settle_order_amount_total,
           IFNULL(b.vr_settle_settle_amount, 0) / 100                                                      as vr_settle_settle_amount,
           IFNULL(b.vr_settle_platform_profit, 0) / 100                                                    as vr_settle_platform_profit,
           IFNULL(b.vr_settle_order_user_num, 0)                                                           as vr_settle_order_user_num,
           IFNULL(b.vr_settle_order_num, 0)                                                                as vr_settle_order_num,
           IFNULL(a.t_order_user_num, 0)                                                                   as t_order_user_num,
           IFNULL(b.t_settle_order_user_num, 0)                                                            as t_settle_order_user_num,
           a.vip_level,
           IFNULL(a.original_order_amount_total, 0) / 100                                                  as original_order_amount_total,
           IFNULL(a.original_platform_profit, 0) / 100                                                     as original_platform_profit,
           IFNULL(b.settle_original_order_amount_total, 0) / 100                                           as settle_original_order_amount_total,
           IFNULL(b.settle_original_platform_profit, 0) / 100                                              as settle_original_platform_profit,
           IFNULL(a.vr_original_order_amount_total, 0) / 100                                               as vr_original_order_amount_total,
           IFNULL(a.vr_original_platform_profit, 0) / 100                                                  as vr_original_platform_profit,
           IFNULL(b.vr_settle_original_order_amount_total, 0) / 100                                        as vr_settle_original_order_amount_total,
           IFNULL(b.vr_settle_original_platform_profit, 0) / 100                                           as vr_settle_original_platform_profit,
           TRUNCATE(IFNULL(a.order_valid_bet_money, 0),2)                                                       as order_valid_bet_money,
           TRUNCATE(IFNULL(a.original_order_valid_bet_acount, 0),2)                                             as original_order_valid_bet_acount,
           TRUNCATE(IFNULL(b.settle_valid_bet_money, 0),2)                                                      as settle_valid_bet_money,
           TRUNCATE(IFNULL(b.original_settle_valid_bet_acount, 0),2)                                            as original_settle_valid_bet_acount,
           IFNULL(a.betPromoTickets, 0)                                                                    as betPromoTickets,
           IFNULL(b.settlePromoTickets, 0)                                                                 as settlePromoTickets,
           IFNULL(a.cashout_amount / 100, 0)                                                               as cashout_amount,
           IFNULL(a.cashout_profit / 100, 0)                                                               as cashout_profit,
           IFNULL(b.cashout_amount / 100, 0)                                                               as settle_cashout_amount,
           IFNULL(b.cashout_profit / 100, 0)                                                               as settle_cashout_profit,
           IFNULL(a.cashout_tickets, 0)                                                                    as cashout_tickets,
           IFNULL(a.cashout_users, 0)                                                                      as cashout_users,
           IFNULL(b.cashout_tickets, 0)                                                                    as settle_cashout_tickets,
           IFNULL(b.cashout_users, 0)                                                                      as settle_cashout_users
    FROM (SELECT tm.id,
                 tm.agent_level,
                 tm.merchant_code,
                 tm.merchant_name,
                 tm.parent_id,
                 IFNULL(tm.terrace_rate, 0)                          as terrace_rate,
                 IFNULL(tm.terrace_rate, IFNULL(ml.terrace_rate, 0)) as execute_rate,
                 IFNULL(tm.payment_cycle, '1')                       as payment_cycle,
                 IFNULL(tm.vip_payment_cycle, '1')                   as vip_payment_cycle,
                 IFNULL(tm.technique_payment_cycle, '1')             as technique_payment_cycle,
                 IFNULL(tm.computing_standard, '1')                  as computing_standard,
                 case tm.vip_payment_cycle
                     when 1 and tm.agent_level = 0 then tm.vip_amount
                     when 2 and tm.agent_level = 0 then (case
                                                             WHEN month(CURRENT_DATE) in (1, 4, 7, 10)
                                                                 then tm.vip_amount
                                                             else 0 end)
                     when 3 and tm.agent_level = 0
                         then (case WHEN month(CURRENT_DATE) = 1 then tm.vip_amount else 0 end)
                     else 0 end                                      as vip_amount,
                 case tm.technique_payment_cycle
                     when 1 and tm.agent_level = 0 then tm.technique_amount
                     when 2 and tm.agent_level = 0 then (case
                                                             WHEN month(CURRENT_DATE) in (1, 4, 7, 10)
                                                                 then tm.technique_amount
                                                             else 0 end)
                     when 3 and tm.agent_level = 0 then (case
                                                             WHEN month(CURRENT_DATE) = 1 then tm.technique_amount
                                                             else 0 end)
                     else 0 end                                      as technique_amount
          FROM tybss_merchant_common.t_merchant tm
                   LEFT JOIN tybss_merchant_common.t_merchant_level ml on tm.level = ml.level
          WHERE tm.agent_level in (0, 2)) as m
             LEFT JOIN (SELECT o.merchant_code,
                               o.currency_code,
                               IFNULL(o.vip_level, 0)                                                   as vip_level,
                               SUM(o.order_amount_total)                                                AS order_amount_total,
                               SUM(o.settle_amount)                                                     as settle_amount,
                               Count(0)                                                                 AS order_valid_num,
                               Count(distinct o.uid)                                                    AS order_user_num,
                               SUM(o.profit_amount) * -1                                                as platform_profit,

                               SUM(case when o.manager_code = 3 then o.order_amount_total end)          AS vr_order_amount_total,
                               SUM(case when o.manager_code = 3 then o.settle_amount end)               as vr_settle_amount,
                               Count(case when o.manager_code = 3 then o.order_no end)                  AS vr_order_valid_num,
                               Count(distinct (case when o.manager_code = 3 then o.uid end))            AS vr_order_user_num,
                               Count(distinct (case when o.manager_code != 3 then o.uid end))           AS t_order_user_num,
                               SUM(case when o.manager_code = 3 then o.profit_amount end) * -1          as vr_platform_profit,

                               SUM(o.original_order_amount_total)                                       as original_order_amount_total,
                               SUM(o.original_profit_amount) * -1                                       as original_platform_profit,
                               SUM(case when o.manager_code = 3 then o.original_order_amount_total end) as
                                                                                                           vr_original_order_amount_total,
                               SUM(case when o.manager_code = 3 then o.original_profit_amount end) *
                               -1                                                                       as vr_original_platform_profit,

                               SUM(CASE
                                       WHEN o.out_come in (3, 4, 5, 6) and
                                            ABS(o.order_amount_total) = ABS(o.profit_amount)
                                           THEN ABS(o.profit_amount / 100)
                                       WHEN o.out_come in (3, 4, 5, 6) and
                                            ABS(o.order_amount_total) > ABS(o.profit_amount)
                                           THEN ABS(o.profit_amount / 100)
                                       WHEN o.out_come in (3, 4, 5, 6) and
                                            ABS(o.order_amount_total) < ABS(o.profit_amount)
                                           THEN ABS(o.order_amount_total / 100)
                                   END)                                                                 as order_valid_bet_money,

                          SUM(CASE
                                       WHEN o.out_come in (3, 4, 5, 6) and
                                            ABS(o.original_order_amount_total) = ABS(o.original_profit_amount)
                                           THEN ABS(o.original_profit_amount / 100)
                                       WHEN o.out_come in (3, 4, 5, 6) and
                                            ABS(o.original_order_amount_total) > ABS(o.original_profit_amount)
                                           THEN ABS(o.original_profit_amount / 100)
                                       WHEN o.out_come in (3, 4, 5, 6) and
                                            ABS(o.original_order_amount_total) < ABS(o.original_profit_amount)
                                           THEN ABS(o.original_order_amount_total / 100) END)                as original_order_valid_bet_acount,

                               count(CASE
                                         WHEN o.out_come in (3, 4, 5, 6) and
                                              ABS(o.order_amount_total) = ABS(o.profit_amount)
                                             THEN o.order_no
                                         WHEN o.out_come in (3, 4, 5, 6) and
                                              ABS(o.order_amount_total) > ABS(o.profit_amount)
                                             THEN o.order_no
                                         WHEN o.out_come in (3, 4, 5, 6) and
                                              ABS(o.order_amount_total) < ABS(o.profit_amount)
                                             THEN o.order_no
                                   END)                                                                 as betPromoTickets,

                               SUM(case when o.pre_bet_amount > 0 then o.profit_amount end)             as cashout_profit,
                               SUM(o.pre_bet_amount)                                                    as cashout_amount,
                               COUNT(case when o.pre_bet_amount > 0 then o.order_no end)                as cashout_tickets,
                               COUNT(distinct o.uid)                                                    as cashout_users,
                               SUM(case when o.original_pre_bet_amount > 0 then o.original_profit_amount end)             as original_cashout_profit_amount,
                               SUM(o.original_pre_bet_amount)                                                             as original_cashout_amount
                        FROM t_ticket o
                        WHERE o.create_time >= startTimeL
                          and o.create_time <= endTimeL
                        GROUP BY o.merchant_code, o.currency_code, o.vip_level) a on a.merchant_code = m.merchant_code
             LEFT JOIN(SELECT s.merchant_code,
                              s.currency_code,
                              IFNULL(s.vip_level, 0)                                                   as vip_level,
                              SUM(s.original_order_amount_total)                                       AS settle_order_amount_total,
                              SUM(s.settle_amount)                                                     as settle_settle_amount,
                              count(distinct s.order_no)                                               as settle_order_num,
                              Count(distinct s.uid)                                                    AS settle_order_user_num,
                              SUM(s.profit_amount) * -1                                                as settle_platform_profit,
                              SUM(case when s.manager_code = 3 then s.original_order_amount_total end) AS
                                                                                                          vr_settle_order_amount_total,
                              SUM(case when s.manager_code = 3 then s.settle_amount end)               as vr_settle_settle_amount,
                              count(distinct (case when s.manager_code = 3 then s.order_no end))       as vr_settle_order_num,
                              Count(distinct (case when s.manager_code = 3 then s.uid end))            AS vr_settle_order_user_num,
                              Count(distinct (case when s.manager_code != 3 then s.uid end))           AS t_settle_order_user_num,
                              SUM(case when s.manager_code = 3 then s.profit_amount end) * -1          as vr_settle_platform_profit,

                              SUM(s.original_order_amount_total)                                       as settle_original_order_amount_total,
                              SUM(s.original_profit_amount) * -1                                       as settle_original_platform_profit,
                              SUM(case when s.manager_code = 3 then s.original_order_amount_total end) as
                                                                                                          vr_settle_original_order_amount_total,
                              SUM(case when s.manager_code = 3 then s.original_profit_amount end) *
                              -1                                                                       as vr_settle_original_platform_profit,
                              SUM(CASE
                                      WHEN s.out_come in (3, 4, 5, 6) and
                                           ABS(s.order_amount_total) = ABS(s.profit_amount)
                                          THEN ABS(s.profit_amount / 100)
                                      WHEN s.out_come in (3, 4, 5, 6) and
                                           ABS(s.order_amount_total) > ABS(s.profit_amount)
                                          THEN ABS(s.profit_amount / 100)
                                      WHEN s.out_come in (3, 4, 5, 6) and
                                           ABS(s.order_amount_total) < ABS(s.profit_amount)
                                          THEN ABS(s.order_amount_total / 100)
                                  END)                                                                 as settle_valid_bet_money,
                         SUM(CASE
                                       WHEN s.out_come in (3, 4, 5, 6) and
                                            ABS(s.original_order_amount_total) = ABS(s.original_profit_amount)
                                           THEN ABS(s.original_profit_amount / 100)
                                       WHEN s.out_come in (3, 4, 5, 6) and
                                            ABS(s.original_order_amount_total) > ABS(s.original_profit_amount)
                                           THEN ABS(s.original_profit_amount / 100)
                                       WHEN s.out_come in (3, 4, 5, 6) and
                                            ABS(s.original_order_amount_total) < ABS(s.original_profit_amount)
                                           THEN ABS(s.original_order_amount_total / 100) END)              as original_settle_valid_bet_acount,
                              count(CASE
                                        WHEN s.out_come in (3, 4, 5, 6) and
                                             ABS(s.order_amount_total) = ABS(s.profit_amount)
                                            THEN s.order_no
                                        WHEN s.out_come in (3, 4, 5, 6) and
                                             ABS(s.order_amount_total) > ABS(s.profit_amount)
                                            THEN s.order_no
                                        WHEN s.out_come in (3, 4, 5, 6) and
                                             ABS(s.order_amount_total) < ABS(s.profit_amount)
                                            THEN s.order_no
                                  END)                                                                 as settlePromoTickets,

                              SUM(case when s.pre_bet_amount > 0 then s.profit_amount end)             as cashout_profit,
                              SUM(s.pre_bet_amount)                                                    as cashout_amount,
                              COUNT(case when s.pre_bet_amount > 0 then s.order_no end)                as cashout_tickets,
                              COUNT(distinct s.uid)                                                    as cashout_users,
                              SUM(s.original_pre_bet_amount)            as original_settle_cashout_money,
                              SUM(case when s.original_pre_bet_amount > 0 then s.original_profit_amount end)             as original_settle_cashout_profit_amount
                       FROM t_ticket s
                       where s.settle_time >= startTimeL
                         and s.settle_time <= endTimeL
                       GROUP BY s.merchant_code, s.currency_code, s.vip_level) b
                      on b.merchant_code = m.merchant_code and a.vip_level = b.vip_level
                          and a.currency_code = b.currency_code
    WHERE a.order_valid_num > 0
       or b.settle_order_num > 0;


    replace INTO tybss_report.r_merchant_finance_day
    (id, finance_day_id, finance_date, finance_time, merchant_id, merchant_code, merchant_name, agent_level, currency,
     order_amount_total, settle_amount, order_valid_num, updated_time, parent_id, merchant_num, computing_standard,
     terrace_rate, vip_amount, technique_amount, payment_cycle, vip_payment_cycle, technique_payment_cycle,
     execute_rate, platform_profit, order_user_num, platform_profit_rate, settle_order_amount_total,
     settle_settle_amount, settle_order_num,
     settle_platform_profit, settle_order_user_num, settle_platform_profit_rate, vr_order_amount_total,
     vr_settle_amount,
     vr_platform_profit, vr_order_valid_num, vr_order_user_num, vr_settle_order_amount_total, vr_settle_settle_amount,
     vr_settle_platform_profit, vr_settle_order_num, vr_settle_order_user_num, t_order_user_num, t_settle_order_user,
     vip_level, original_order_amount_total, original_platform_profit, settle_original_order_amount_total,
     settle_original_platform_profit, vr_original_order_amount_total, vr_original_platform_profit,
     vr_settle_original_order_amount_total,
     vr_settle_original_platform_profit, order_valid_bet_money, original_order_valid_bet_acount,settle_valid_bet_money,original_settle_valid_bet_acount, bet_promo_tickets,
     settle_promo_tickets, cashout_amount, cashout_profit,
     settle_cashout_amount, settle_cashout_profit, cashout_tickets, cashout_users, settle_cashout_tickets,
     settle_cashout_users,original_cashout_profit_amount,original_cashout_amount,original_settle_cashout_money,original_settle_cashout_profit_amount)
    SELECT concat(execute_date, '-', m.merchant_code, '-', IFNULL(bb.currency_code, '1'), '-', vip_level) as id,
           concat(execute_date, '-', m.merchant_code)                                                     as finance_day_id,
           execute_date                                                                                   as finance_date,
           unix_timestamp(date_sub(execute_date, interval 1 day)) * 1000 +
           (12 * 60 * 60 * 1000)                                                                          as finance_time,
           m.id                                                                                           as merchant_id,
           m.merchant_code,
           m.merchant_name,
           m.agent_level,
           IFNULL(bb.currency_code, '1')                                                                  as currency,
           IFNULL(bb.order_amount_total, 0)                                                               as order_amount_total,
           IFNULL(bb.settle_amount, 0)                                                                    as settle_amount,
           IFNULL(bb.order_valid_num, 0)                                                                  as order_valid_num,
           unix_timestamp(now())                                                                          as updated_time,
           m.parent_id                                                                                    as parent_id,
           (SELECT count(id)
            FROM tybss_merchant_common.t_merchant
            WHERE status = 1
              and parent_id = m.id)                                                                       as merchant_num,
           m.computing_standard                                                                           as computing_standard,
           m.terrace_rate                                                                                 as terrace_rate,
           m.vip_amount                                                                                   as vip_amount,
           m.technique_amount                                                                             as technique_amount,
           m.payment_cycle                                                                                as payment_cycle,
           m.vip_payment_cycle                                                                            as vip_payment_cycle,
           m.technique_payment_cycle                                                                      as technique_payment_cycle,
           m.execute_rate                                                                                 as execute_rate,
           IFNULL(bb.platform_profit, 0)                                                                  as platform_profit,
           IFNULL(bb.order_user_num, 0)                                                                   as order_user_num,
           IFNULL(bb.platform_profit_rate, 0)                                                             as platform_profit_rate,
           IFNULL(bb.settle_order_amount_total, 0)                                                        as settle_order_amount_total,
           IFNULL(bb.settle_settle_amount, 0)                                                             as settle_settle_amount,
           IFNULL(bb.settle_order_num, 0)                                                                 as settle_order_num,
           IFNULL(bb.settle_platform_profit, 0)                                                           as settle_platform_profit,
           IFNULL(bb.settle_order_user_num, 0)                                                            as settle_order_user_num,
           IFNULL(bb.settle_platform_profit_rate, 0)                                                      as settle_platform_profit_rate,

           IFNULL(bb.vr_order_amount_total, 0)                                                            as vr_order_amount_total,
           IFNULL(bb.vr_settle_amount, 0)                                                                 as vr_settle_amount,
           IFNULL(bb.vr_platform_profit, 0)                                                               as vr_platform_profit,
           IFNULL(bb.vr_order_valid_num, 0)                                                               as vr_order_valid_num,
           IFNULL(bb.vr_order_user_num, 0)                                                                as vr_order_user_num,
           IFNULL(bb.vr_settle_order_amount_total, 0)                                                     as vr_settle_order_amount_total,
           IFNULL(bb.vr_settle_settle_amount, 0)                                                          as vr_settle_settle_amount,
           IFNULL(bb.vr_settle_platform_profit, 0)                                                        as vr_settle_platform_profit,
           IFNULL(bb.vr_settle_order_num, 0)                                                              as vr_settle_order_num,
           IFNULL(bb.vr_settle_order_user_num, 0)                                                         as vr_settle_order_user_num,
           IFNULL(bb.t_order_user_num, 0)                                                                 as t_order_user_num,
           IFNULL(bb.t_settle_order_user, 0)                                                              as t_settle_order_user,


           vip_level,
           IFNULL(bb.original_order_amount_total, 0)                                                      as original_order_amount_total,
           IFNULL(bb.original_platform_profit, 0)                                                         as original_platform_profit,
           IFNULL(bb.settle_original_order_amount_total, 0)                                               as settle_original_order_amount_total,
           IFNULL(bb.settle_original_platform_profit, 0)                                                  as settle_original_platform_profit,
           IFNULL(bb.vr_original_order_amount_total, 0)                                                   as vr_original_order_amount_total,
           IFNULL(bb.vr_original_platform_profit, 0)                                                      as vr_original_platform_profit,
           IFNULL(bb.vr_settle_original_order_amount_total, 0)                                            as vr_settle_original_order_amount_total,
           IFNULL(bb.vr_settle_original_platform_profit, 0)                                               as vr_settle_original_platform_profit,
           TRUNCATE(IFNULL(bb.order_valid_bet_money, 0),2)                                                     as order_valid_bet_money,
           TRUNCATE(IFNULL(bb.original_order_valid_bet_acount, 0),2)                                           as original_order_valid_bet_acount,

           TRUNCATE(IFNULL(bb.settle_valid_bet_money, 0),2)                                                    as settle_valid_bet_money,
           TRUNCATE(IFNULL(bb.original_settle_valid_bet_acount, 0),2)                                          as original_settle_valid_bet_acount,
           IFNULL(bb.bet_promo_tickets, 0)                                                                as betPromoTickets,
           IFNULL(bb.settle_promo_tickets, 0)                                                             as settlePromoTickets,

           IFNULL(bb.cashout_amount, 0)                                                                   as cashout_amount,
           IFNULL(bb.cashout_profit, 0)                                                                   as cashout_profit,
           IFNULL(bb.settle_cashout_amount, 0)                                                            as settle_cashout_amount,
           IFNULL(bb.settle_cashout_profit, 0)                                                            as settle_cashout_profit,
           IFNULL(bb.cashout_tickets, 0)                                                                  as cashout_tickets,
           IFNULL(bb.cashout_users, 0)                                                                    as cashout_users,
           IFNULL(bb.settle_cashout_tickets, 0)                                                           as settle_cashout_tickets,
           IFNULL(bb.settle_cashout_users, 0)                                                             as settle_cashout_users,
           IFNULL(bb.original_cashout_profit_amount,0) / 100                                                as original_cashout_profit_amount,
           IFNULL(bb.original_cashout_amount,0) / 100                                                       as original_cashout_amount,
           IFNULL(bb.original_settle_cashout_money,0) / 100                                                 as original_settle_cashout_money,
           IFNULL(bb.original_settle_cashout_profit_amount,0) / 100                                         as original_settle_cashout_profit_amount
    FROM (SELECT tm.id,
                 tm.agent_level,
                 tm.merchant_code,
                 tm.merchant_name,
                 tm.parent_id,
                 IFNULL(tm.terrace_rate, 0)                          as terrace_rate,
                 IFNULL(tm.terrace_rate, IFNULL(ml.terrace_rate, 0)) as execute_rate,
                 IFNULL(tm.payment_cycle, '1')                       as payment_cycle,
                 IFNULL(tm.vip_payment_cycle, '1')                   as vip_payment_cycle,
                 IFNULL(tm.technique_payment_cycle, '1')             as technique_payment_cycle,
                 IFNULL(tm.computing_standard, '1')                  as computing_standard,
                 case tm.vip_payment_cycle
                     when 1 then tm.vip_amount
                     when 2 then (case
                                      WHEN month(CURRENT_DATE) in (1, 4, 7, 10) then tm.vip_amount
                                      else 0 end)
                     when 3 then (case WHEN month(CURRENT_DATE) = 1 then tm.vip_amount else 0 end)
                     else 0 end                                      as vip_amount,
                 case tm.technique_payment_cycle
                     when 1 then tm.technique_amount
                     when 2 then (case
                                      WHEN month(CURRENT_DATE) in (1, 4, 7, 10)
                                          then tm.technique_amount
                                      else 0 end)
                     when 3 then (case
                                      WHEN month(CURRENT_DATE) = 1 then tm.technique_amount
                                      else 0 end)
                     else 0 end                                      as technique_amount
          FROM tybss_merchant_common.t_merchant tm
                   LEFT JOIN tybss_merchant_common.t_merchant_level ml on tm.level = ml.level
          WHERE tm.agent_level = 1) as m
             LEFT JOIN(select parent_id,
                              currency                                   as currency_code,
                              vip_level                                  as vip_level,
                              sum(order_amount_total)                    as order_amount_total,
                              sum(settle_amount)                         as settle_amount,
                              sum(order_valid_num)                       as order_valid_num,
                              sum(merchant_num)                          as merchant_num,
                              sum(platform_profit)                       as platform_profit,
                              sum(order_user_num)                        as order_user_num,
                              IFNULL(sum(platform_profit) /
                                     IF(sum(order_amount_total) = '0', NULL, sum(order_amount_total)), 0) * 100
                                                                         as platform_profit_rate,
                              sum(settle_order_amount_total)             as settle_order_amount_total,
                              sum(settle_settle_amount)                  as settle_settle_amount,
                              sum(settle_order_num)                      as settle_order_num,
                              sum(settle_platform_profit)                as settle_platform_profit,
                              sum(settle_order_user_num)                 as settle_order_user_num,
                              IFNULL(sum(settle_platform_profit) /
                                     IF(sum(settle_order_amount_total) = '0', NULL, sum(settle_order_amount_total)),
                                     0) * 100
                                                                         as settle_platform_profit_rate,
                              SUM(vr_order_amount_total)                 AS vr_order_amount_total,
                              SUM(vr_settle_amount)                      as vr_settle_amount,
                              SUM(vr_order_valid_num)                    AS vr_order_valid_num,
                              SUM(vr_order_user_num)                     AS vr_order_user_num,
                              SUM(vr_platform_profit)                    as vr_platform_profit,
                              SUM(vr_settle_order_amount_total)          AS vr_settle_order_amount_total,
                              SUM(vr_settle_settle_amount)               as vr_settle_settle_amount,
                              SUM(vr_settle_order_num)                   as vr_settle_order_num,
                              SUM(vr_settle_order_user_num)              AS vr_settle_order_user_num,
                              SUM(t_order_user_num)                      AS t_order_user_num,
                              SUM(t_settle_order_user)                   AS t_settle_order_user,
                              SUM(vr_settle_platform_profit)             as vr_settle_platform_profit,

                              SUM(original_order_amount_total)           as original_order_amount_total,
                              SUM(original_platform_profit)              as original_platform_profit,
                              SUM(settle_original_order_amount_total)    as settle_original_order_amount_total,
                              SUM(settle_original_platform_profit)       as settle_original_platform_profit,
                              SUM(vr_original_order_amount_total)        as vr_original_order_amount_total,
                              SUM(vr_original_platform_profit)           as vr_original_platform_profit,
                              SUM(vr_settle_original_order_amount_total) as vr_settle_original_order_amount_total,
                              SUM(vr_settle_original_platform_profit)    as vr_settle_original_platform_profit,
                              SUM(order_valid_bet_money)                 as order_valid_bet_money,
                              SUM(original_order_valid_bet_acount)       as original_order_valid_bet_acount,
                              SUM(settle_valid_bet_money)                as settle_valid_bet_money,
                              SUM(original_settle_valid_bet_acount)      as original_settle_valid_bet_acount,
                              SUM(bet_promo_tickets)                     as bet_promo_tickets,
                              SUM(settle_promo_tickets)                  as settle_promo_tickets,

                              sum(cashout_amount)                           cashout_amount,
                              sum(cashout_profit)                           cashout_profit,
                              sum(settle_cashout_amount)                    settle_cashout_amount,
                              sum(settle_cashout_profit)                    settle_cashout_profit,
                              sum(cashout_tickets)                          cashout_tickets,
                              sum(cashout_users)                            cashout_users,
                              sum(settle_cashout_tickets)                   settle_cashout_tickets,
                              sum(settle_cashout_users)                     settle_cashout_users,
                              sum(original_cashout_profit_amount)        as original_cashout_profit_amount,
                              sum(original_cashout_amount)               as original_cashout_amount,
                              sum(original_settle_cashout_money)         as original_settle_cashout_money,
                              sum(original_settle_cashout_profit_amount)   as original_settle_cashout_profit_amount
                       from r_merchant_finance_day
                       where finance_date = execute_date
                         and (order_amount_total > 0 or settle_order_amount_total > 0)
                         and agent_level in (0, 2)
                       GROUP BY parent_id, currency, vip_level) as bb on bb.parent_id = m.id
    where bb.order_valid_num > 0
       or bb.settle_order_num > 0;


    /*sql结束*/
/*执行成功，添加日志*/
    SET end_time = get_cur_ymdhms();

    SET exce_msg = CONCAT(execute_date, ' p_r_merchant_finance_day_big success!');

    CALL p_add_task_event_log(task_type, start_time, end_time, result, exce_msg);
END//
DELIMITER ;
