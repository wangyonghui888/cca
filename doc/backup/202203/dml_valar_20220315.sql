INSERT INTO t_activity_config (id, name, type, terminal, start_time, end_time, status, in_start_time,
                               in_end_time, pc_url, h5_url, h5_maintain_url, pc_maintain_url, maintain_status,
                               maintain_end_time, title, content)
VALUES (10010, '老虎机活动', 2, null, null, null, 1,
        1647263209000, 0, '',
        '', '', '', 0, 1647229184204, '运营活动上线了.........................',
        '运营活动上线了.........................11111111');



INSERT INTO tybss_merchant_common.t_activity_merchant (activity_id, merchant_code, status, entrance_status) VALUES ( 10010, 'oubao', '1', 1);