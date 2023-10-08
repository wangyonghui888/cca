alter table tybss_report.r_merchant_finance_month
    MODIFY merchant_id varchar(64) null;

alter table tybss_new.t_transfer_record
    MODIFY transfer_id bigint(64);

alter table tybss_new.t_transfer_record
    MODIFY orderStr varchar(255) null;

alter table tybss_new.t_account_change_history
    MODIFY order_no varchar(255) null;


alter table tybss_new.t_account_change_history
    add column before_transfer bigint(16) null COMMENT '转帐前金额';

alter table tybss_new.t_account_change_history
    add column after_transfer bigint(16) null COMMENT '转帐后金额';

alter table merchant.t_order_settle
    add column tag varchar(32) null COMMENT '订单状态改变来源';
