alter table tybss_report.t_ticket
    add column dangerous_id int DEFAULT NULL COMMENT '危险注单ID' after settle_times;

alter table tybss_report.t_ticket_detail
    add column settle_var int DEFAULT NULL COMMENT 'settle_var' after risk_event;

alter table tybss_report.t_ticket_detail
    add column mts_cancel_result int DEFAULT NULL COMMENT 'settle_var' after settle_var;

alter table tybss_report.t_ticket_detail
    add column child_play_id bigint DEFAULT NULL COMMENT 'settle_var' after mts_cancel_result;

alter table tybss_report.t_ticket_detail
    add column trade_type int DEFAULT NULL COMMENT 'settle_var' after child_play_id;

alter table tybss_report.t_ticket_detail
    add column odds_data_sourse varchar(20) DEFAULT NULL COMMENT 'settle_var' after trade_type;

alter table tybss_report.t_ticket_detail
    add column settle_match_process_id bigint DEFAULT NULL COMMENT 'settle_match_process_id' after odds_data_sourse;