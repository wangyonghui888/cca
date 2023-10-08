drop table if exists tybss_new.`bs_order_rate`;
CREATE TABLE tybss_new.`bs_order_rate`
(
    `id`    int(11)       NOT NULL,
    `rate`  decimal(4, 2) NOT NULL,
    `start` bigint(20)    NOT NULL,
    `end`   bigint(20)    NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_as_cs;

INSERT INTO tybss_new.`bs_order_rate` (`id`, `rate`, `start`, `end`)
VALUES (1, '0.50', 1559377799000, 1748766599999);

ALTER TABLE tybss_new.`bs_order_rate`
    ADD PRIMARY KEY (`id`);
COMMIT;