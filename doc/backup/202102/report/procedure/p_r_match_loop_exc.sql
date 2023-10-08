#循环调用 日期类存储过程   start_date<=目标<=end_date
DROP PROCEDURE IF EXISTS p_r_match_loop_exc;

DELIMITER //
CREATE
    DEFINER = `root`@`%` PROCEDURE p_r_match_loop_exc(in start_date varchar(100), end_date varchar(100),
                                                         procedure_name varchar(100))
BEGIN
    DECLARE startDate date DEFAULT NOW();
    DECLARE endDate date DEFAULT NOW();
    set startDate = DATE_FORMAT(start_date, '%Y-%m-%d');
    set endDate = DATE_FORMAT(end_date, '%Y-%m-%d');
#日统计

        WHILE startDate <= endDate
            DO
                IF 'p_r_match_bet_interval_day_utc8' = procedure_name THEN
                    CALL p_r_match_bet_interval_day_utc8(startDate);

                ELSEIF 'p_r_match_market_settle_day_utc8' = procedure_name THEN
                    CALL p_r_match_market_settle_day_utc8(startDate);
                    CALL p_r_match_play_settle_day_utc8(startDate);
                     CALL p_r_match_settle_bet_info(startDate);
                        CALL p_r_rist_duty_trader_day(startDate);
                ELSEIF 'p_r_match_play_settle_day_utc8' = procedure_name THEN
                    CALL p_r_match_play_settle_day_utc8(startDate);

                ELSEIF 'p_r_match_settle_bet_info' = procedure_name THEN
                    CALL p_r_match_settle_bet_info(startDate);

                ELSEIF 'p_r_match_user_settle_day_utc8' = procedure_name THEN
                    CALL p_r_match_user_settle_day_utc8(startDate);

                ELSEIF 'p_r_sport_duty_trader_day' = procedure_name THEN
                    CALL p_r_sport_duty_trader_day();
                ELSEIF 'p_r_rist_duty_trader_day' = procedure_name THEN
                    CALL p_r_rist_duty_trader_day(startDate);
                 ELSEIF 'p_r_device_type_day' = procedure_name THEN
                    CALL p_r_device_type_day(startDate);
                END IF;

                set startDate = DATE_ADD(startDate, INTERVAL 1 DAY);
            END WHILE;



END//
DELIMITER ;

/**
CALL  p_r_match_loop_exc('2020-08-01','2021-03-26','p_r_match_market_settle_day_utc8');
CALL  p_r_match_loop_exc('2020-08-01','2021-03-26','p_r_match_play_settle_day_utc8');
CALL  p_r_match_loop_exc('2020-08-01','2021-03-26','p_r_match_settle_bet_info');
CALL  p_r_match_loop_exc('2020-08-01','2021-03-26','p_r_match_user_settle_day_utc8');
CALL  p_r_match_loop_exc('2020-08-01','2021-03-26','p_r_match_bet_interval_day_utc8');
*/

