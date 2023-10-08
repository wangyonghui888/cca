ALTER TABLE `tybss_new`.`t_user`
    MODIFY COLUMN `profit` decimal(32, 2) NULL DEFAULT NULL,
    MODIFY COLUMN `total_tickets` int(8) NULL DEFAULT NULL,
    MODIFY COLUMN `bet_amount` decimal(32, 2) NULL DEFAULT NULL,
    MODIFY COLUMN `seven_day_bet_amount` decimal(32, 2) NULL DEFAULT NULL,
    MODIFY COLUMN `seven_day_profit_amount` decimal(32, 2) NULL DEFAULT NULL,
    MODIFY COLUMN `settled_bet_amount` decimal(32, 2) NULL DEFAULT NULL;