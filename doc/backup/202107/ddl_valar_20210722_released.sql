drop table if exists tybss_new.t_valid_flow;
drop table if exists tybss_new.t_callback;

ALTER TABLE tybss_new.`t_user`
    DROP INDEX `index_merchant`;

CREATE UNIQUE INDEX index_merchantcode_username ON tybss_new.t_user (merchant_code, username);



ALTER TABLE tybss_report.`ac_user_order_day` DROP INDEX `index_time_user_id`;


CREATE UNIQUE INDEX index_time_user_id ON tybss_report.ac_user_order_day (time, uid);

