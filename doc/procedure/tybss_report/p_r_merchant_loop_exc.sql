#循环调用 日期类存储过程   start_date<=目标<=end_date
DROP PROCEDURE IF EXISTS p_r_merchant_loop_exc;

DELIMITER //
CREATE
    DEFINER = `root`@`%` PROCEDURE p_r_merchant_loop_exc(in start_date varchar(100), end_date varchar(100),
                                                         procedure_name varchar(100), type_num varchar(100))
BEGIN
    DECLARE startDate date DEFAULT NOW();
    DECLARE endDate date DEFAULT NOW();
    set startDate = DATE_FORMAT(start_date, '%Y-%m-%d');
    set endDate = DATE_FORMAT(end_date, '%Y-%m-%d');
#日统计
    IF type_num = '1' THEN
        WHILE startDate <= endDate
            DO
                IF 'p_r_merchant_finance_day' = procedure_name THEN
                    CALL p_r_merchant_finance_day(startDate);

                ELSEIF 'p_r_merchant_order_day' = procedure_name THEN
                    CALL p_r_merchant_order_day(startDate);

                ELSEIF 'p_r_merchant_sport_order_day' = procedure_name THEN
                    CALL p_r_merchant_sport_order_day(startDate);

                ELSEIF 'p_r_user_order_day' = procedure_name THEN
                    CALL p_r_user_order_day(startDate);

                ELSEIF 'p_r_user_sport_order_day' = procedure_name THEN
                    CALL p_r_user_sport_order_day(startDate);

                ELSEIF 'p_r_market_bet_info' = procedure_name THEN
                    CALL p_r_market_bet_info(startDate);

                ELSEIF 'p_r_match_bet_info' = procedure_name THEN
                    CALL p_r_match_bet_info(startDate);

                ELSEIF 'p_r_merchant_market_bet_info' = procedure_name THEN
                    CALL p_r_merchant_market_bet_info(startDate);

                ELSEIF 'p_r_merchant_match_bet_info' = procedure_name THEN
                    CALL p_r_merchant_match_bet_info(startDate);

                ELSEIF 'p_r_bc_match_bet_info' = procedure_name THEN
                    CALL p_r_bc_match_bet_info(startDate);

                END IF;

                set startDate = DATE_ADD(startDate, INTERVAL 1 DAY);
            END WHILE;
    END IF;
#周统计
    IF type_num = '2' THEN
        WHILE DATE_FORMAT(date_add(startDate, INTERVAL -1 day), '%Y%U') <=
              DATE_FORMAT(date_add(endDate, INTERVAL -1 day), '%Y%U')
            DO

                IF 'p_r_merchant_order_week' = procedure_name THEN
                    CALL p_r_merchant_order_week(startDate);

                ELSEIF 'p_r_merchant_sport_order_week' = procedure_name THEN
                    CALL p_r_merchant_sport_order_week(startDate);

                ELSEIF 'p_r_user_order_week' = procedure_name THEN
                    CALL p_r_user_order_week(startDate);

                ELSEIF 'p_r_user_sport_order_week' = procedure_name THEN
                    CALL p_r_user_sport_order_week(startDate);

                END IF;

                set startDate = DATE_ADD(startDate, INTERVAL 1 WEEK);
            END WHILE;
    END IF;
#月统计
    IF type_num = '3' THEN
        WHILE DATE_FORMAT(startDate, '%Y-%m') <= DATE_FORMAT(endDate, '%Y-%m')
            DO
                IF 'p_r_merchant_finance_month' = procedure_name THEN
                    CALL p_r_merchant_finance_month(startDate);

                ELSEIF 'p_r_merchant_order_month' = procedure_name THEN
                    CALL p_r_merchant_order_month(startDate);

                ELSEIF 'p_r_merchant_sport_order_month' = procedure_name THEN
                    CALL p_r_merchant_sport_order_month(startDate);

                ELSEIF 'p_r_user_order_month' = procedure_name THEN
                    CALL p_r_user_order_month(startDate);

                ELSEIF 'p_r_user_sport_order_month' = procedure_name THEN
                    CALL p_r_user_sport_order_month(startDate);
                END IF;

                set startDate = DATE_ADD(startDate, INTERVAL 1 MONTH);
            END WHILE;
    END IF;

END//
DELIMITER ;


/**
执行对应的存储过程。如果需要可以加入对应的 IF语句
start_date: 开始时间（包含）
end_date: 截止时间（包含）
procedure_name: 存储过程名称
type_num:  1:日期类型   2:周类型  3：月类型

一下参考：
CALL  p_r_merchant_loop_exc('2020-05-01','2020-08-31','p_r_user_order_day','1');
CALL  p_r_merchant_loop_exc('2020-05-01','2020-08-31','p_r_user_sport_order_day','1');
CALL  p_r_merchant_loop_exc('2020-05-01','2020-08-31','p_r_user_order_month','3');
CALL  p_r_merchant_loop_exc('2020-05-01','2020-08-31','p_r_user_sport_order_month','3');
CALL  p_r_merchant_loop_exc('2020-05-01','2020-08-31','p_r_merchant_order_day','1');
CALL  p_r_merchant_loop_exc('2020-05-01','2020-08-31','p_r_merchant_order_month','3');
CALL  p_r_merchant_loop_exc('2020-05-01','2020-08-31','p_r_merchant_sport_order_day','1');
CALL  p_r_merchant_loop_exc('2020-05-01','2020-08-31','p_r_merchant_sport_order_month','3');
CALL  p_r_merchant_loop_exc('2020-05-01','2020-08-31','p_r_merchant_finance_day','1');
CALL  p_r_merchant_loop_exc('2020-05-01','2020-08-31','p_r_merchant_finance_month','3');

CALL  p_r_merchant_loop_exc('2020-05-01','2020-09-01','p_r_market_bet_info','1');
CALL  p_r_merchant_loop_exc('2020-05-01','2020-09-01','p_r_match_bet_info','1');
CALL  p_r_merchant_loop_exc('2020-05-01','2020-09-01','p_r_merchant_market_bet_info','1');
CALL  p_r_merchant_loop_exc('2020-05-01','2020-09-01','p_r_merchant_match_bet_info','1');

CALL  p_r_merchant_loop_exc('2020-06-01','2020-09-10','p_r_bc_match_bet_info','1');
上线 end_date 最好使用日期函数：
DATE_FORMAT(now(),'%Y-%m-%d')
DATE_FORMAT(DATE_ADD(now(),INTERVAL -1 DAY),'%Y-%m-%d')
比如：
CALL  p_r_merchant_loop_exc('2020-05-01',DATE_FORMAT(DATE_ADD(now(),INTERVAL -1 DAY),'%Y-%m-%d'),'p_r_merchant_finance_day','1');
 */