update tybss_new.t_transfer_record
set biz_type=7
where create_time <= 1610002802000
  and create_time >= 1599462002000
  and biz_type = 1
  and before_transfer is not null;

update tybss_new.t_transfer_record
set biz_type=8
where create_time <= 1610002802000
  and create_time >= 1599462002000
  and biz_type = 2
  and before_transfer is not null;

alter table tybss_new.t_transfer_record
    modify column biz_type int comment '1投注(扣款) 2结算派彩(加款)3注单取消(加款) 4 注单取消回滚(扣款) 5结算回滚(扣款) 6 拒单 (加款) 7 转入 8 转出';

alter table tybss_new.t_user
    modify column del_flag  int(1) default 0 null comment '0:未删除，1：已删除出';

alter table tybss_new.t_account_change_history
    modify column del_flag  int(1) default 0 null comment '0:未删除，1：已删除出';

alter table oubao.t_user
    modify column del_flag  int(1) default 0 null comment '0:未删除，1：已删除出';

alter table oubao.t_account_change_history
    modify column del_flag  int(1) default 0 null comment '0:未删除，1：已删除出';

UPDATE tybss_new.t_user_level t
SET t.bg_color = '#5C974B'
WHERE t.level_id = 16;

UPDATE tybss_new.t_user_level t
SET t.bg_color = '#427ED1'
WHERE t.level_id = 15;

UPDATE tybss_new.t_user_level t
SET t.bg_color = '#ACADA5'
WHERE t.level_id = 17;

drop index index_last_settle on tybss_new.t_settle;

drop index index_create_time on tybss_new.t_settle;

DROP INDEX `index_order_no/out_come/settle_type/settle_times` on tybss_new.t_settle;

create index index_create_time
    on tybss_new.t_settle (create_time, last_settle, out_come, bet_amount);

create index index_uid
    on tybss_new.t_settle (uid);