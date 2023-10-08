-- 0613
alter table merchant.merchant_notice add column `notice_result_type` tinyint(1)  DEFAULT null COMMENT '公告类型0赛果1事件';


-- CREATE UNIQUE INDEX idx_* ON merchant.table (name);