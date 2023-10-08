DELETE
from tybss_new.t_currency_rate
where id in (12, 13)
  and currency_code in ('RMB', 'CNY');
Drop table if exists `tybss_new`.`t_user_activity`;
CREATE TABLE `tybss_new`.`t_user_activity`
(
    `id`                  BIGINT  NOT NULL,
    `uid`                 BIGINT  NOT NULL,
    `activity_id`         BIGINT  NOT NULL COMMENT '活动ID',
    `create_time`         BIGINT  NOT NULL,
    `update_time`         BIGINT  NULL,
    `status`              TINYINT NOT NULL,
    `participation_times` INT     NULL COMMENT '参与次数',
    `win_times`           INT     NULL COMMENT '中奖次数',
    `amount`              BIGINT  NULL COMMENT '参与活动礼金总额度',
    `profit`              BIGINT  NULL COMMENT '参与活动盈利',
    PRIMARY KEY (`id`)
)
    COMMENT = '用户礼金详情表';

/*Drop table if exists `tybss_new`.`t_activity_config`;

CREATE TABLE `tybss_new`.`t_activity_config`
(
    `id`                   BIGINT       NOT NULL,
    `name`                 VARCHAR(45)  NOT NULL,
    `type`                 INT          NULL COMMENT '活动类型(1常规活动 2自定义活动 3特殊活动)',
    `terminal`             VARCHAR(45)  NULL COMMENT '活动终端',
    `start_time`           BIGINT       NULL,
    `end_time`             BIGINT       NULL,
    `time_limit`           INT          NULL COMMENT '领取时效(单位Hour)',
    `ip_limit`             TINYINT      NULL COMMENT '是否有IP限制',
    `sport_id`             VARCHAR(45)  NULL COMMENT '参与活动赛种',
    `reward_type`          TINYINT      NULL COMMENT '奖励类型:  1 直接上分额度,2筹码彩金,3机会(如抽奖次数)',
    `total_cost`           BIGINT       NULL COMMENT '活动总投入:根据活动的预算,来限制活动礼金发放.',
    `reward_percentage`    DOUBLE       NULL COMMENT '中奖比例:可以按一定的算法把总预算礼金分到固定数量的玩家上.(如1000个玩家参与活动,要保证不低于100个玩家获奖)',
    `reward_guy`           VARCHAR(255) NULL COMMENT '指定中奖:可以指定某些VIP玩家中奖.',
    `single_day_max`       BIGINT       NULL COMMENT '单日最高中奖额度:把总预算分配到活动的每天里面,对每天的中奖额度进行限制.',
    `single_user_max`      BIGINT       NULL COMMENT '单用户最高中奖额度.',
    `user_partition_times` INT          NULL COMMENT '参与次数:单个用户参与活动的次数.',
    `auto_check`           TINYINT      NULL COMMENT '是否自动审批:会员参与活动中奖时,是否把对应奖励额度自动发放,或者需要运营人员审核之后才能发放.',
    `partition_rule`       BIGINT       NULL COMMENT '参与活动规则.(会员需要满足参与活动的条件)',
    `reward_rule`          BIGINT       NULL COMMENT '领奖规则',
    `settle_cycle`         TINYINT      NULL COMMENT '结算周期(1h,单位小时)',
    `status`               VARCHAR(45)  NOT NULL COMMENT '状态(0,关闭;1,开启)',
    PRIMARY KEY (`id`)
)
    COMMENT = '活动管理配置表';*/

/*Drop table if exists `tybss_new`.`t_activity_merchant`;

CREATE TABLE `tybss_new`.`t_activity_merchant`
(
    `id`            BIGINT      NOT NULL,
    `activity_id`   BIGINT      NOT NULL,
    `merchant_code` VARCHAR(45) NOT NULL COMMENT '活动类型(1常规活动 2自定义活动 3特殊活动)',
    `status`        VARCHAR(45) NOT NULL COMMENT '状态(0,关闭;1,开启)',
    PRIMARY KEY (`id`)
)
    COMMENT = '活动商户配置表';*/

Drop table if exists `tybss_new`.`t_prize_record`;

CREATE TABLE tybss_new.t_prize_record
(
    id          BIGINT auto_increment NOT NULL,
    uid         BIGINT                NOT NULL,
    activity_id BIGINT                NOT NULL,
    amount      BIGINT                NULL COMMENT '中奖额度',
    status      TINYINT               NOT NULL COMMENT '无效(0),有效(1)',
    create_time BIGINT                NOT NULL,
    update_time BIGINT                NULL,
    biz_type    INT                   NOT NULL COMMENT '业务来源',
    remark      varchar(100)          NULL,
    PRIMARY KEY (`id`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_0900_as_cs
    COMMENT ='中奖记录表';

/*Drop table if exists tybss_new.`user_statistic`;

CREATE TABLE tybss_new.`user_statistic`
(
    `id`               BIGINT auto_increment NOT NULL COMMENT '自动编号',
    `user_id`          bigint(32)                                                    DEFAULT NULL,
    `user_name`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs  DEFAULT NULL,
    `merchant_code`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs  DEFAULT NULL,
    `merchant_name`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL,
    `profit`           decimal(16, 2)                                                DEFAULT NULL COMMENT '盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)',
    `return_amount`    decimal(16, 2)                                                DEFAULT NULL COMMENT '返还金额',
    `bet_amount`       decimal(16, 2)                                                DEFAULT NULL COMMENT '投注金额',
    `valid_bet_amount` decimal(16, 2)                                                DEFAULT NULL COMMENT '有效投注金额',
    `bet_tickets`          int(6)                                                        DEFAULT NULL COMMENT '投注笔数',
    `valid_tickets`    int(6)                                                        DEFAULT NULL COMMENT '有效投注笔数',
    `updated_time`     bigint(20)            NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_as_cs
  ROW_FORMAT = DYNAMIC;*/

SELECT id,
       order_no AS orderNo,
       playId,
       playName,
       sportId
FROM (select distinct t.id, t.order_no, t.play_id playId, l.zs playName, t.sport_id sportId
      from s_betting_play t
               LEFT JOIN s_language l on l.name_code = t.play_name_code
      where t.status = 1
        and t.sport_id = 1
      union
      select distinct t.id, t.order_no, t.play_id playId, l.zs playName, t.sport_id sportId
      from s_virtual_betting_play t
               LEFT JOIN s_virtual_language l on l.name_code = t.name_code
      where t.status = 1
        and t.sport_id = 1
     ) AS tt
ORDER BY tt.order_no;

SELECT *
FROM t_order o
         left join t_settle s on o.order_no = s.order_no
where o.create_time > 1617292799000
  and s.create_time > 1617292799000
  and o.order_status = 2
  and s.out_come = 2
  and s.last_settle = 1;


update t_settle
set out_come=7
where order_no in ('19043166224303',
                   '16090423222293',
                   '15587001884614',
                   '12633218695150',
                   '18858559004666',
                   '10620028272681',
                   '12382281875401',
                   '12381623369717',
                   '14142891311103',
                   '12129931575212',
                   '19294971265078',
                   '10871212556240',
                   '13071551946730',
                   '17096942936000',
                   '18354542075906',
                   '15083546992606',
                   '12886206529544',
                   '10554067771391',
                   '11626858364981',
                   '15083010121749',
                   '13388507987919',
                   '19865456205884',
                   '14389542871036',
                   '16351443779538',
                   '11066612350998',
                   '12115377348662',
                   '13055907201017',
                   '12115389931571',
                   '16237220052977',
                   '15547434082396',
                   '18569631440870',
                   '19257278316592',
                   '12210126176273',
                   '12596989558740',
                   '10766510530532',
                   '17059648446492',
                   '19507510493111',
                   '11020383363027',
                   '12779063418820',
                   '11521694965710',
                   '16806966796223',
                   '14515288424441',
                   '19534591737821',
                   '11548918816773',
                   '16329875922942',
                   '11222526033946',
                   '18713333555214',
                   '13681842282406',
                   '16011527372855',
                   '12861305520123',
                   '13998928347119',
                   '13244335308868',
                   '13746439634965',
                   '12939101659143',
                   '11098871562267',
                   '16514797715464',
                   '14872574951444',
                   '17456954957887',
                   '11536089128929',
                   '17204571103258',
                   '15257626394646',
                   '17084418629630',
                   '15760439558102',
                   '14502597148654',
                   '16012148129754',
                   '19840974626806',
                   '14492355100664',
                   '19711452520442',
                   '18453761105982',
                   '15425243373515',
                   '13900262236108',
                   '15595636785116',
                   '17305153839115',
                   '15926097797148',
                   '17940009639913',
                   '18694699147208',
                   '19132055126029',
                   '15620054769685',
                   '10960189538381',
                   '11430959669279',
                   '18914238857251',
                   '13007528116243',
                   '15643214413821',
                   '15775420628902',
                   '14319268896734',
                   '18610020229159',
                   '19047111966751',
                   '11127298883567',
                   '18358403932195',
                   '16345016377345')
  and id in (8279024512446815417,
             8279024512446815420,
             8279024512446821114,
             8279024512446821115,
             8279024512446821118,
             8279024512446821121,
             8279024512446821122,
             8279024512446821124,
             8279024512446821129,
             8279024512446821132,
             8279024512446821133,
             8279024512446821136,
             8279024512446821137,
             8279024512446821140,
             8279024512446821141,
             8279024512446821144,
             8279024512446821145,
             8279024512446821146,
             8279024512446821147,
             8279024512446821150,
             8279024512446821153,
             8279024512446821155,
             8279024512447025529,
             8279024512447025531,
             8279024512447025532,
             8279024512447226856,
             8279024512447226857,
             8279024512447226858,
             8279024512447445397,
             8279024512447445398,
             8279024512447445399,
             8279024512447445400,
             8279024512447445401,
             8279024512447445451,
             8279024512447445466,
             8279024512447445469,
             8279024512447445471,
             8279024512447445474,
             8279024512447445480,
             8279024512447445481,
             8279024512447445482,
             8279024512447483416,
             8279024512447483418,
             8279024512447483419,
             8279024512447483420,
             8279024512447483421,
             8279024512447483424,
             8279024512447483425,
             8279024512447483427,
             8279024512447483428,
             8279024512447483429,
             8279024512447483430,
             8279024512447483431,
             8279024512447483432,
             8279024512447483433,
             8279024512447483434,
             8279024512447483435,
             8279024512447483436,
             8279024512447483437,
             8279024512447483438,
             8279024512447483439,
             8279024512447483440,
             8279024512447483441,
             8279024512447483442,
             8279024512447483443,
             8279024512447483444,
             8279024512447483445,
             8279024512447483446,
             8279024512447483447,
             8279024512447659313,
             8279024512447659314,
             8279024512447659315,
             8279024512447659316,
             8279024512447659317,
             8279024512447659318,
             8279024512447659319,
             8279024512447659320,
             8279024512447659324,
             8279024512447659325,
             8279024512448062444,
             8279024512448062445,
             8279024512448062446,
             8279024512448062447,
             8279024512448062448,
             8279024512448062450,
             8279024512448062451,
             8279024512448062452,
             8279024512448062453,
             8279024512448062454,
             8279024512448062456);


SELECT u.username,
       u.merchant_code,
       u.currency_code,
       u.uid,
       od.bet_no,
       od.sport_id,
       od.tournament_id,
       od.sport_name,
       od.match_type,
       od.market_type,
       od.market_value,
       od.play_name,
       od.play_id,
       od.play_options,
       od.play_options_id,
       od.match_id,
       od.match_name,
       od.match_info,
       od.odds_value / 100000                                                odds_value,
       od.odd_finally,
       od.bet_amount / 100                                                   bet_amount,
       od.order_no,
       od.play_option_name,
       od.bet_result,
       od.settle_score,
       ROUND(o.original_order_amount_total / 100, 2)                         order_amount_total,
       ROUND(o.original_product_amount_total / 100, 2)                       product_amount_total,
       o.product_count,
       o.order_status,
       o.series_type,
       o.series_value,
       o.create_time,
       s.out_come,
       ROUND(s.original_settle_amount / 100, 2)                              settle_amount,
       ROUND(s.original_profit_amount / 100, 2)                              profit_amount,
       s.settle_time,
       s.settle_times,
       (CASE
            WHEN od.match_type = 3 THEN (select match_begion_time
                                         from s_outright_match_info
                                         where id = od.match_id)
            WHEN o.manager_code != 3 THEN (SELECT t2.begin_time
                                           from s_match_info t2
                                           where od.match_id = t2.id)
            WHEN o.manager_code = 3 THEN (select t1.begin_time
                                          from s_virtual_match_info t1
                                          where t1.id = od.match_id) end) as begin_time
FROM `t_order_detail` od
         RIGHT JOIN (SELECT s1.id,
                            s1.uid,
                            s1.order_no,
                            s1.create_time,
                            s1.bet_amount,
                            s1.out_come,
                            s1.settle_time,
                            s1.settle_times,
                            s1.original_settle_amount,
                            s1.original_profit_amount,
                            s1.settle_type
                     FROM `t_settle` s1
                     where s1.last_settle = 1
                       AND s1.create_time >= 1618588800000
                       and s1.out_come in (2, 3, 4, 5, 6)
                       and s1.bet_amount > 0
                       AND s1.create_time <= 1618675199000
                       and s1.uid in (select tu.uid
                                      from t_user tu
                                      where tu.merchant_code = '912697')
                     ORDER BY s1.id desc
                     LIMIT 0, 1000) s ON s.order_no = od.order_no
         LEFT JOIN t_order o on s.order_no = o.order_no
         LEFT JOIN t_user u ON u.uid = od.uid;


select a.userId,
       a.userName,
       a.validTickets,
       a.activeDays,
       a.betAmount,
       a.betAmountSettled,
       a.merchantCode,
       a.merchantName,
       a.profit,
       a.settleBetAmount,
       a.settleProfit,
       a.settleTickets,
       a.tickets,
       a.ticketSettled,
       a.validBetAmount
from (SELECT user_id                                     userId,
             min(merchant_code)                          merchantCode,
             min(merchant_name)                          merchantName,
             min(user_name)                              userName,
             sum(bet_amount_settled)                     betAmountSettled,
             sum(bet_amount)                             betAmount,
             sum(profit)                                 profit,
             sum(bet_num)                                tickets,
             sum(settle_order_num)                       settleTickets,
             sum(settle_order_amount)                    settleBetAmount,
             sum(settle_profit)                          settleProfit,
             sum(valid_tickets)                          validTickets,
             sum(valid_bet_amount)                       validBetAmount,
             sum(ticket_settled)                         ticketSettled,
             count(case when bet_amount > 0 then id end) activeDays
      from r_user_order_day_utc8
      where time >= 20210427
        and time <= 20210429
        and user_id = 1
        and merchant_code = 2
      GROUP by user_id) a
where a.validBetAmount > 1
  and a.validBetAmount < 122
  and a.profit < 122
  and a.profit < 122
  and a.settleProfit < 122
  and a.settleProfit < 122
  and a.settleBetAmount < 122
  and a.settleBetAmount < 122
order by a.validTickets desc
limit 0,10;



select count(1)               userSum,
       sum(a.validTickets)    validTickets,
       sum(a.profit)          profit,
       sum(a.settleBetAmount) settleOrderAmount,
       sum(a.settleProfit)    settleProfit,
       sum(a.settleTickets)   settleOrderNum,
       sum(a.validBetAmount)  validBetAmount
from (SELECT user_id                  userId,
             sum(profit)              profit,
             sum(settle_order_num)    settleTickets,
             sum(settle_order_amount) settleBetAmount,
             sum(settle_profit)       settleProfit,
             sum(valid_tickets)       validTickets,
             sum(valid_bet_amount)    validBetAmount
      from r_user_sport_order_day
      where time >= 20210401
        and time <= 20210430
      GROUP by user_id) a;

SELECT m.callback_url                                                      callbackUrl,
       m.balance_url                                                       balanceUrl,
       m.agent_level                                                       agentLevel,
       m.id,
       m.`level`,
       m.max_bet                                                           maxBet,
       m.merchant_code                                                     merchantCode,
       m.`key`                                                             merchantKey,
       m.merchant_name                                                     merchantName,
       m.parent_id                                                         parentId,
       m.sport_list                                                        sportList,
       m.`status`,
       (case when m.currency is null then pm.currency else m.currency end) currency,
       m.transfer_mode                                                     transferMode,
       m.url,
       m.commerce,
       m.topic,
       pm.topic                                                            parentTopic,
       pm.merchant_code                                                    parentCode,
       m.white_ip                                                          whiteIp,
       m.pc_domain                                                         pcDomain,
       m.h5_domain                                                         h5Domain,
       m.open_vr_sport                                                     openVrSport,
       m.language_list                                                     languageList,
       (select settle_switch_advance from t_merchant_config where merchant_code = m.merchant_code)
                                                                           settleSwitchAdvance,
       (select user_prefix from t_merchant_config where merchant_code = m.merchant_code)
                                                                           userPrefix
FROM t_merchant m
         LEFT JOIN t_merchant pm ON m.parent_id = pm.id
WHERE m.merchant_code = 'oubao'
  AND m.`status` = 1;



SELECT (case when u.fake_name is null then u.username else u.fake_name end)  userName,
       ifnull(o.user_level, 1)                                            as userLevel,
       u.merchant_code                                                       merchantCode,
       o.create_time                                                         createTime,
       o.uid                                                                 userId,
       o.id,
       od.modify_time                                                        modifyTime,
       od.bet_no                                                             betNo,
       od.sport_id                                                           sportId,
       od.tournament_id                                                      tournamentId,
       (case
            when i.order_no is not null then
                (case when 'en' = 'en' then i.sport_name_en else i.sport_name_zs end)
            else od.sport_name end)                                          sportName,
       od.match_type                                                         matchType,
       od.market_type                                                        marketType,
       od.play_options                                                       playOptions,
       od.market_id                                                          marketId,
       od.market_value                                                       marketValue,
       (CASE
            WHEN od.sport_id = 1 and od.play_id in (4, 19, 27, 29, 128, 130, 143, 113, 121) and od.match_type = 2
                THEN od.score_benchmark END)                                 scoreBenchmark,
       (case
            when i.order_no is not null then
                (case when 'en' = '1' then i.play_name_en else i.play_name_zs end)
            else od.play_name end)                                           playName,

       od.play_id                                                            playId,
       od.play_options_id                                                    playOptionsId,
       od.match_id                                                           matchId,
       (CASE
            WHEN o.manager_code = 3 THEN
                (SELECT svmi.match_manage_id FROM s_virtual_match_info svmi WHERE svmi.id = od.match_id)
            WHEN o.manager_code != 3 AND od.match_type = 3 THEN
                (SELECT outright_manager_id FROM s_outright_match_info WHERE id = od.match_id)
            ELSE (SELECT match_manage_id FROM s_match_info WHERE id = od.match_id)
           END)                                                              matchManageId,

       (case
            when i.order_no is not null then
                (case when 'en' = 111 then i.tournament_name_en else i.tournament_name_zs end)
            else od.match_name end)                                          matchName,
       (case
            when i.order_no is not null then
                (case when 'en' = 222 then i.match_info_en else i.match_info_zs end)
            else od.match_info end)                                          matchInfo,
       od.bet_amount / 100                                                   betAmount,
       od.bet_result                                                         betResult,
       od.bet_status                                                         betStatus,
       od.odds_value / 100000                                                oddsValue,
       od.odd_finally                                                        oddFinally,
       (case
            when i.order_no is not null then
                (case when 'en' = '1' then i.playoption_en else i.playoption_zs end)
            else od.play_option_name end)                                    playOptionName,
       od.risk_event                                                         riskEvent,
       (CASE
            WHEN od.sport_id = 1 and od.play_id in (4, 19, 27, 29, 128, 130, 143, 113, 121) and od.match_type = 2
                THEN od.score_benchmark END)                                 scoreBenchmark,
       ROUND(o.order_amount_total / 100, 2)                                  localBetAmount,
       ROUND(o.original_order_amount_total / 100, 2)                         orderAmountTotal,
       o.product_count                                                       productCount,
       o.order_no                                                            orderNo,
       o.ip,
       o.ip_area                                                             ipArea,
       o.device_type                                                         deviceType,
       o.device_imei                                                         deviceImei,
       o.currency_code                                                       currencyCode,
       od.remark,
       o.series_type                                                         seriesType,
       o.series_value                                                        seriesValue,
       o.manager_code                                                        managerCode,
       o.confirm_time                                                        confirmTime,
       s.out_come                                                            outcome,
       o.order_status                                                        orderStatus,
       ROUND(s.settle_amount / 100, 2)                                       localSettleAmount,
       ROUND(s.original_settle_amount / 100, 2)                              settleAmount,
       ROUND(s.profit_amount / 100, 2)                                       localProfitAmount,
       ROUND(s.original_profit_amount / 100, 2)                              profitAmount,
       (CASE
            WHEN o.manager_code != 3 THEN (SELECT CONCAT(@a := ifNULL(t2.begin_time, 0), @b := ifNULL(t2.score, '0'),
                                                         @c := ifNULL(sl1.zs, '0'), @d := ifNULL(sl2.zs, '0'))
                                           from s_match_info t2
                                                    left join s_language sl1 on sl1.name_code = t2.home_name_code
                                                    left join s_language sl2 on sl2.name_code = t2.away_name_code
                                           where od.match_id = t2.id)
            WHEN o.manager_code = 3 THEN (select CONCAT(@a := ifNULL(t1.begin_time, 0),
                                                        @b := ifNULL(t1.score_result, '0'),
                                                        @c := ifNULL(sl1.zs, '0'), @d := ifNULL(sl2.zs, '0'))
                                          from s_virtual_match_info t1
                                                   left join s_virtual_language sl1 on sl1.name_code = t1.home_name_code
                                                   left join s_virtual_language sl2 on sl2.name_code = t1.away_name_code
                                          where t1.id = od.match_id) end) as temp,
       @a                                                                 as beginTime,
       @b                                                                 as matchTestScore,
       @c                                                                 as homeName,
       @d                                                                 as awayName,
       s.settle_time                                                         settleTime,
       sd.description
FROM `t_order_detail` od
         RIGHT JOIN (SELECT DISTINCT o1.id,
                                     o1.modify_time,
                                     o1.order_no,
                                     o1.uid,
                                     o1.create_time,
                                     o1.product_count,
                                     o1.order_amount_total,
                                     o1.original_order_amount_total,
                                     o1.product_amount_total,
                                     o1.original_product_amount_total,
                                     o1.series_type,
                                     o1.series_value,
                                     o1.ip,
                                     o1.ip_area,
                                     o1.device_type,
                                     o1.device_imei,
                                     o1.currency_code,
                                     o1.remark,
                                     o1.confirm_time,
                                     o1.order_status,
                                     o1.manager_code,
                                     o1.user_level
                     FROM `t_order` o1
                     WHERE o1.create_time >= 1111

                       and o1.create_time <= 1

                       and o1.series_type = 1

                       and o1.series_type != 1

                       and o1.uid = 111

                       and o1.vip_level = 111

                       and o1.uid IN (select tu.uid
                                      from t_user tu
                                      where tu.fake_name = 1)
                     ORDER BY o1.id DESC
                     LIMIT 1,111111) o
                    ON o.order_no = od.order_no
         LEFT JOIN t_settle s ON s.order_no = o.order_no and s.last_settle = 1 and s.bet_amount > 0 and
                                 s.out_come in (2, 3, 4, 5, 6)
         left join t_order_internationalize i on od.bet_no = i.bet_no and od.order_no = i.order_no
         LEFT JOIN t_user u ON u.uid = od.uid
         LEFT JOIN s_item_dict sd
                   ON sd.`value` = od.settle_match_process_id AND sd.addition1 = od.sport_id AND sd.parent_type_id = 8
