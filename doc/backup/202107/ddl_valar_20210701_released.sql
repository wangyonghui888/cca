ALTER TABLE `tybss_report`.`user_order_all`
    MODIFY COLUMN `valid_bet_amount` decimal(32, 2) NULL DEFAULT NULL;

ALTER TABLE `tybss_report`.`user_order_all`
    MODIFY COLUMN `bet_amount` decimal(32, 2) NULL DEFAULT NULL;

ALTER TABLE `tybss_report`.`user_order_all`
    MODIFY COLUMN `profit` decimal(32, 2) NULL DEFAULT NULL;

ALTER TABLE `tybss_report`.`user_order_all`
    MODIFY COLUMN `settle_amount` decimal(32, 5) NULL DEFAULT NULL;

