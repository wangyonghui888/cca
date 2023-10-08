drop table if exists tybss_report.t_ticket;
create table tybss_report.t_ticket
(
    merchant_name               varchar(200)    default 0    null comment '商户名称',
    merchant_code               varchar(200)    default 0    null comment '商户编码',
    uid                         bigint          default 0    not null comment '用户ID',
    username                    varchar(255)    default 0    null comment '用户名称',
    user_level                  int             default 0    null comment '用户投注该单时的级别',
    currency_code               varchar(50)                  null comment '币种编码',
    order_no                    varchar(255)    default ''   not null comment '订单单号' primary key,
    create_time                 bigint                       not null comment '创建时间',
    modify_time                 bigint          default 0    not null comment '修改时间',
    confirm_time                bigint          default 0    null comment '定单确认时间',
    remark                      varchar(1024)   default ''   null comment '备注 （订单为什么无效？)',
    order_amount_total          bigint          default 0    not null comment '实际付款金额',
    original_order_amount_total bigint          default 0    not null comment '(原始币种)实际付款金额',
    product_amount_total        bigint          default 0    not null comment '注单总价',
    pre_bet_amount              bigint                       null comment '提前结算注单金额，单注本金*100，若为null则从未进行过提前结算，否则处理过（取消或者未取消）',
    original_pre_bet_amount     bigint                       null comment '(原始币种)提前结算注单金额，单注本金*100，若为null则从未进行过提前结算，否则处理过（取消或者未取消）',
    product_count               bigint          default 0    not null comment '注单项数量',
    series_type                 int(10)         default 1    not null comment '串关类型(1：单关(默认) 、N00F：串关投注)',
    series_value                varchar(200)    default '单关' not null comment '串关值(单关(默认) 、N串F：串关投注)',
    manager_code                tinyint(1)                   null comment '操盘平台：1-PANDA，2-MTS，3-虚拟赛事，4-电竞，5-MTS-0',
    ac_code                     tinyint                      null comment '活动编码 为空则非活动订单,1028以后征用为冻结标识，0：解冻，3：冻结',
    ip                          varchar(255)    default ''   null comment 'ip地址',
    ip_area                     varchar(200)                 null comment 'ip区域',
    device_type                 int(5) unsigned default 1    null comment '1:H5，2：PC,3:Android,4:IOS',
    device_imei                 varchar(128)    default ''   null comment '设备imei码，只有手机有，没有不添加',
    order_status                tinyint                      null comment '订单状态(0:待处理,1:已处理,2:取消交易【自动，手动】,3:待确认,4:已拒绝【风控】)',
    pre_settle                  tinyint(1)                   null comment '提前结算部分或者全额区分 1：部分(包含两次瓜分投注额或者仅有1次提前结算)、2：全额、 0或者null：没有提前结算',
    vip_level                   int(2)          default 0    null comment 'VIP等级： 0 非VIP，1 VIP用户',
    lang_code                   varchar(16)                  null comment '语言编码 对应 t_language 表中的name_code',
    max_win_amount              bigint unsigned default 0    not null comment '最高可赢金额(注单金额*注单赔率)',
    original_max_win_amount     bigint unsigned default 0    not null comment '(原始币种)最高可赢金额(注单金额*注单赔率)',
    settle_amount               bigint                       null comment '结算金额，是x100过后的金额数',
    profit_amount               bigint                       null comment '净盈利',
    original_profit_amount      bigint                       null comment '(原始币种)净盈利',
    original_settle_amount      bigint          default 0    null comment '(原始币种)结算金额，是x100过后的金额数',
    settle_time                 bigint                       null comment '结算时间',
    out_come                    int(50)                      null comment '结算（2是走水，3-输，4-赢，5-半赢，6-半输，7赛事取消，8赛事延期）',
    settle_type                 tinyint                      null comment '结算类型(0:手工结算,1:自动结算,2:结算回滚,3:结算回滚之后再次结算（格式：3X，比如：31，再次结算第1次）)',
    settle_times                bigint unsigned              null comment '结算次数'
) comment '订单表';


create
    index index_create_time
    on tybss_report.t_ticket (create_time);

create
    index index_modify_time
    on tybss_report.t_ticket (modify_time);

create
    index index_settle_time
    on tybss_report.t_ticket (settle_time);

create
    index index_uid_createTime
    on tybss_report.t_ticket (uid, create_time);

create
    index index_merchant_settle_time
    on tybss_report.t_ticket (merchant_code, settle_time);


drop table if exists tybss_report.t_ticket_detail;

create table tybss_report.t_ticket_detail
(
    bet_no              varchar(50)                        not null comment '注单编号' primary key,
    order_no            varchar(255)          default ''   not null comment '订单编号',
    uid                 bigint                default 0    not null comment '用户id',
    create_time         bigint                default 0    not null comment '下注时间',
    modify_time         bigint                default 0    null comment '修改时间',
    original_bet_amount bigint                default 0    null comment '(原始币种)注单金额，单注本金*100',
    bet_amount          bigint                default 0    null comment '注单金额，指的是下注本金2位小数，投注时x10000',
    begin_time          bigint                             null comment '开赛时间',
    sport_id            int(5)                default 0    null comment '运动种类编号',
    sport_name          varchar(255)          default ''   null comment '运动种类名称',
    sport_name_zs       varchar(255)          default ''   null comment '运动种类名称(chinese)',
    sport_name_en       varchar(255)          default ''   null comment '运动种类名称(english)',
    play_id             int(8)                default 0    null comment '玩法ID',
    play_name           varchar(255)          default ''   null comment '玩法名称',
    play_name_zs        varchar(255)          default ''   null comment '玩法名称chinese',
    play_name_en        varchar(255)          default ''   null comment '玩法名称english',
    tournament_id       bigint                default 0    null comment '联赛id',
    tournament_level    bigint                default 0    null comment '联赛级别',
    match_id            bigint                default 0    null comment '赛事编号',
    match_name          varchar(255)          default ''   null comment '赛事名称',
    tournament_name_zs  varchar(255)          default ''   null comment '赛事名称chinese',
    tournament_name_en  varchar(255)          default ''   null comment '赛事名称english',
    match_type          int(1)                default 1    null comment '赛事类型：1 ：早盘赛事 ，2： 滚球盘赛事，3： 冠军盘赛事，5：活动赛事',
    match_info          varchar(255)          default ''   null comment '对阵信息',
    match_info_zs       varchar(255)          default ''   null comment '对阵信息chinese',
    match_info_en       varchar(255)          default ''   null comment '对阵信息english',
    home_name           varchar(255)          default ''   null comment '主队',
    away_name           varchar(255)          default ''   null comment '客队',
    market_id           bigint(50)            default 0    null comment '盘口id',
    market_type         varchar(2)            default 'EU' null comment '盘口类型(EU:欧盘 HK:香港盘 US:美式盘 ID:印尼盘 MY:马来盘 GB:英式盘）',
    market_value        varchar(100)          default ''   null comment '盘口值',
    play_options_id     bigint(11)            default 0    null comment '投注类型ID(对应上游的投注项ID),传给风控的',
    play_options        varchar(255)          default ''   null comment '投注类型(投注时下注的投注类型 比如1 X 2)，规则引擎用',
    play_option_name    varchar(200)          default ''   null comment '投注项名称',
    playoption_zs       varchar(200)          default ''   null comment '投注项名称chinese',
    playoption_en       varchar(200)          default ''   null comment '投注项名称english',
    odds_value          double(20, 2)         default 0.00 null comment '注单赔率,固定2位小数 【欧洲赔率】',
    odd_finally         varchar(20)           default ''   null comment '最终赔率,可能是1/20',
    bet_status          int                   default 0    null comment '注单状态(0未结算 1已结算 2结算异常 3手动注单取消[不可逆] 4消息注单取消[可逆] 5拒单[PA手动拒单，PA自动拒单，业务拒单，MTS拒单] 6冻结)',
    bet_result          int(3)                default 0    null comment '注项结算结果 0-无结果 2-走水 3-输 4-赢 5-赢一半 6-输一半 7-赛事取消 8-赛事延期 11-比赛延迟 12-比赛中断 13-未知 15-比赛放弃 16-异常盘口 17未知赛事状态 18比赛取消 19比赛延期 20SR-其他 21SR-无进球球员 22SR-正确比分丢失 23SR-无法确认的赛果 24SR-格式变更 25SR-进球球员丢失 26SR-主动弃赛 27SR-并列获胜 28SR-中途弃赛 29SR-赔率错误 30SR-统计错误 31SR-投手变更',
    score_benchmark     varchar(200)          default ''   null comment '基准比分(下注时已产生的比分)',
    remark              varchar(1024)         default ''   null comment '备注 （订单为什么无效？)',
    cancel_type         int(3)                default 0    null comment '取消类型 0未取消，1比赛取消，2比赛延期， 3比赛中断，4比赛重赛，5比赛腰斩，6比赛放弃，7盘口错误，8赔率错误，9队伍错误，10联赛错误，11比分错误，12电视裁判， 13主客场错误，14赛制错误，15赛程错误，16时间错误，17赛事提前，18自定义原因，19数据源取消，20比赛延迟，21操盘手取消，2主动弃赛，23并列获胜，24中途弃赛，25统计错误， 40PA手动拒单，41PA自动拒单，42业务拒单，43MTS拒单，44虚拟自动拒单，45商户扣款失败',
    settle_score        varchar(512)          default ''   null comment '投注项结算比分',
    place_num           int(2)                default 0    null comment '坑位(盘口位置) 1：表示主盘，2：表示第一副盘',
    settle_times        tinyint(100) unsigned default 0    null comment '结算次数，已经被结算过的次数，持续累加',
    cancel_time         bigint                default 0    null comment '取消时间',
    risk_event          varchar(64)                        null comment '风控事件'
) comment '投注单详细信息表';

create index create_time on tybss_report.t_ticket_detail (create_time);
create  index index_modify_time on tybss_report.t_ticket_detail (modify_time);
create  index index_begin_time on tybss_report.t_ticket_detail (begin_time);
create  index idx_market_id on tybss_report.t_ticket_detail (market_id) comment '盘口ID索引';
create   index idx_match_id on tybss_report.t_ticket_detail (match_id);
create index index_order_no  on tybss_report.t_ticket_detail (order_no) comment '订单号';
create index play_options_id on tybss_report.t_ticket_detail (play_options_id);