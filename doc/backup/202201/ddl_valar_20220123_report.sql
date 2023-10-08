drop table if exists tybss_report.t_match_info;
create table tybss_report.t_match_info
(
    id          bigint default 0 not null comment '主键ID' primary key,
    sport_id    bigint           null comment '体种ID',
    score       varchar(2048)    null comment '比分',
    begin_time  bigint           null comment '开赛时间',
    match_over  int              null comment '比赛状态',
    modify_time bigint           null comment '更新时间'
) comment '订单表';

create
    index index_begin_time on tybss_report.t_match_info (begin_time);

