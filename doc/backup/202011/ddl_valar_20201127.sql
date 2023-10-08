ALTER TABLE oubao.`t_merchant`
    ADD `transfer_mode` INT NOT NULL DEFAULT 2 COMMENT '1:免转,2转账' AFTER `merchant_key`;