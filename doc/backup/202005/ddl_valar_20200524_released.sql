ALTER TABLE oubao.`t_account`
    ADD `merchant_code` VARCHAR(200) NULL COMMENT '商户编码' AFTER `del_flag`;

ALTER TABLE oubao.`t_order`
    ADD `merchant_code` VARCHAR(200) NULL COMMENT '商户编码' AFTER `settle_amount`;

drop table if exists oubao.t_merchant;
CREATE TABLE oubao.t_merchant
(
    `id`            bigint(20) NOT NULL COMMENT '自动编号',
    `merchant_code` varchar(255) DEFAULT NULL COMMENT '商户编码',
    `merchant_key`  varchar(200) DEFAULT NULL COMMENT '密钥'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = DYNAMIC COMMENT ='模拟商户表';
ALTER TABLE oubao.`t_merchant`
    ADD PRIMARY KEY (`id`) USING BTREE;

ALTER TABLE oubao.`t_merchant`
    MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自动编号',
    AUTO_INCREMENT = 1;
COMMIT;

INSERT INTO oubao.`t_merchant`(`merchant_code`, `merchant_key`)
VALUES ('oubao', 'sdfsdfdsfsd');

INSERT INTO oubao.`t_merchant`(`merchant_code`, `merchant_key`)
VALUES ('test', 'I%xs>byi3U&@pDZ~0>nH6k<f_1Ho+z');

INSERT INTO oubao.`t_merchant`(`merchant_code`, `merchant_key`)
VALUES ('959378', '!j@%HZov3Ud73D#RqnE48AsE1b#qmL');

INSERT INTO oubao.`t_merchant`(`merchant_code`, `merchant_key`)
VALUES ('000000', 'Q4uGGwP97hYU?@x~wH7~@Mm1da0qNr');

INSERT INTO oubao.`t_merchant`(`merchant_code`, `merchant_key`)
VALUES ('345896', '3Pz$N<HPi#fTm6iPQN%ZEzAXT~L*@p');

INSERT INTO oubao.`t_merchant`(`merchant_code`, `merchant_key`)
VALUES ('853363', 'uB0Ox4+v#3mm5Qp*eUF7S9SS@rFPB<');

INSERT INTO oubao.`t_merchant`(`merchant_code`, `merchant_key`)
VALUES ('439324', 'z7LD6jGPT&?5+LBOj:@XRZTJ$Jk5ki');

INSERT INTO oubao.`t_merchant`(`merchant_code`, `merchant_key`)
VALUES ('238976', 'z7LD6jGPT&?5+LBOj:@XRZTJ$Jk5ki');


update oubao.t_user set merchant_code='oubao';

