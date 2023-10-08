
-- 公告添加字段 0829 热更已操作
alter table merchant.multi_terminal_notice
    add column `match_id` varchar(50) DEFAULT 0 COMMENT '赛事ID' after `multi_terminal_notice_id`;
