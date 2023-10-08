DROP TABLE IF EXISTS merchant_order_day;
CREATE EXTERNAL TABLE dws_report.`merchant_order_day`
(
    `id`                 string,
    `merchant_code`      string,
    `time`               bigint COMMENT '格式:20210110\r\n',
    `bet_user_rate`      double COMMENT '投注用户数/注册用户数\r\n',
    `profit`             bigint COMMENT '投注量-派彩金额，即指商户注单的毛盈利，暂不计算返水等',
    `register_amount`    bigint COMMENT '注册用户数\t',
    `user_all_count`     bigint COMMENT '所有用户',
    `profit_rate`        double COMMENT '利润率',
    `return_amount`      bigint COMMENT '派彩金额\r\n\r\n',
    `return_rate`        double COMMENT '\r\n\r\n返奖率,派彩金额/总投注量，大于等于100%的要标红',
    `merchant_level`     string COMMENT '商户等级\r\n\r\n',
    `bet_amount`         bigint COMMENT '\r\n\r\n投注额',
    `order_num`          bigint COMMENT '订单数',
    `first_bet_user`     bigint COMMENT '首投用户数',
    `bet_user_amount`    bigint COMMENT '投注用户数\r\n\r\n',
    `settle_user_rate`   double COMMENT '当天结算注单用户数/注册用户数',
    `settle_profit`      bigint COMMENT '当天结算注单-当天结算注单派彩金额',
    `settle_return`      bigint COMMENT '当天结算注单派彩金额',
    `settle_return_rate` double COMMENT '返奖率,结算注单派彩金额/结算注单总投注量，大于等于100%的要标红',
    `settle_bet_amount`  bigint COMMENT '结算注单总额',
    `settle_order_num`   bigint COMMENT '结算注单数',
    `live_user_rate`     double COMMENT '当天比赛注单用户数/注册用户数',
    `live_profit`        bigint COMMENT '当天比赛注单-当天比赛注单派彩金额',
    `live_return`        bigint COMMENT '当天比赛注单派彩金额',
    `live_return_rate`   double COMMENT '返奖率,比赛注单派彩金额/比赛注单总投注量，大于等于100%的要标红',
    `live_bet_amount`    bigint COMMENT '当日比赛注单总额',
    `live_order_num`     bigint COMMENT '当日比赛注单数',
    `merchant_name`      string COMMENT '商户名称'
)
    STORED BY 'org.elasticsearch.hadoop.hive.EsStorageHandler'
    TBLPROPERTIES (
        'es.nodes' = 'lan-big-data-node6:9200',
        'es.index.auto.create' = 'true',
        'es.resource' = 'merchant_order_day/doc',
        'es.read.metadata' = 'true',
        "es.mapping.id" = "id",
        'es.mapping.names' = "id:id, merchant_code:merchantCode, time:time,bet_user_rate:betUserRate,profit:profit,
register_amount:registerAmount,user_all_count:userAllCount,profit_rate:profitRate,return_amount:returnAmount,
return_rate:returnRate,merchant_level:merchantLevel,bet_amount:betAmount,
order_num:orderNum,first_bet_user:firstBetUser,bet_user_amount:betUserAmount,
settle_user_rate:settleUserRate,settle_profit:settleProfit,settle_return:settleReturn,settle_return_rate:settleReturnRate,settle_bet_amount:settleBetAmount,
settle_order_num:settleOrderNum,live_user_rate:liveUserRate,live_profit:liveProfit,live_return:liveReturn,live_return_rate:liveReturnRate,
live_bet_amount:liveBetAmount,live_order_num:liveOrderNum,merchant_name:merchantName",
        "es.write.operation" = "upsert");

------------------------------------------------------
DROP TABLE IF EXISTS merchant_order_week;

CREATE EXTERNAL TABLE dws_report.`merchant_order_week`
(
    `id`                 string,
    `merchant_code`      string,
    `time`               bigint COMMENT '格式:202101\r\n',
    `bet_user_rate`      double COMMENT '投注用户数/注册用户数\r\n',
    `profit`             bigint COMMENT '投注量-派彩金额，即指商户注单的毛盈利，暂不计算返水等',
    `register_amount`    bigint COMMENT '注册用户数\t',
    `user_all_count`     bigint COMMENT '所有用户数',
    `profit_rate`        double COMMENT '利润率',
    `return_amount`      bigint COMMENT '派彩金额\r\n\r\n',
    `return_rate`        double COMMENT '\r\n\r\n返奖率,派彩金额/总投注量，大于等于100%的要标红',
    `merchant_level`     string COMMENT '商户等级\r\n\r\n',
    `bet_amount`         bigint COMMENT '\r\n\r\n投注额',
    `order_num`          bigint COMMENT '订单数',
    `first_bet_user`     bigint COMMENT '首投用户数',
    `bet_user_amount`    bigint COMMENT '投注用户数\r\n\r\n',
    `settle_user_rate`   double COMMENT '当周结算注单用户数/注册用户数',
    `settle_profit`      bigint COMMENT '当周结算注单-周天结算注单派彩金额',
    `settle_return`      bigint COMMENT '当周结算注单派彩金额',
    `settle_return_rate` double COMMENT '返奖率,结算注单派彩金额/结算注单总投注量，大于等于100%的要标红',
    `settle_bet_amount`  bigint COMMENT '结算注单总额',
    `settle_order_num`   bigint COMMENT '结算注单数',
    `live_user_rate`     double COMMENT '当周比赛注单用户数/注册用户数',
    `live_profit`        bigint COMMENT '当周比赛注单-当周比赛注单派彩金额',
    `live_return`        bigint COMMENT '当周比赛注单派彩金额',
    `live_return_rate`   double COMMENT '返奖率,比赛注单派彩金额/比赛注单总投注量，大于等于100%的要标红',
    `live_bet_amount`    bigint COMMENT '当周比赛注单总额',
    `live_order_num`     bigint COMMENT '当周比赛注单数',
    `merchant_name`      string COMMENT '商户名称'
)
    STORED BY 'org.elasticsearch.hadoop.hive.EsStorageHandler'
    TBLPROPERTIES (
        'es.nodes' = 'lan-big-data-node6:9200',
        'es.index.auto.create' = 'true',
        'es.resource' = 'merchant_order_week/doc',
        'es.read.metadata' = 'true',
        "es.mapping.id" = "id",
        'es.mapping.names' = "id:id, merchant_code:merchantCode, time:time,bet_user_rate:betUserRate,profit:profit,
register_amount:registerAmount,user_all_count:userAllCount,profit_rate:profitRate,return_amount:returnAmount,
return_rate:returnRate,merchant_level:merchantLevel,bet_amount:betAmount,
order_num:orderNum,first_bet_user:firstBetUser,bet_user_amount:betUserAmount,
settle_user_rate:settleUserRate,settle_profit:settleProfit,settle_return:settleReturn,settle_return_rate:settleReturnRate,settle_bet_amount:settleBetAmount,
settle_order_num:settleOrderNum,live_user_rate:liveUserRate,live_profit:liveProfit,live_return:liveReturn,live_return_rate:liveReturnRate,
live_bet_amount:liveBetAmount,live_order_num:liveOrderNum,merchant_name:merchantName",
        "es.write.operation" = "upsert");
----------------------------------------------------------------

DROP TABLE IF EXISTS merchant_order_month;
CREATE EXTERNAL TABLE dws_report.`merchant_order_month`
(
    `id`                 string,
    `merchant_code`      string,
    `time`               bigint COMMENT '格式:202101\r\n',
    `bet_user_rate`      double COMMENT '投注用户数/注册用户数\r\n',
    `profit`             bigint COMMENT '投注量-派彩金额，即指商户注单的毛盈利，暂不计算返水等',
    `register_amount`    bigint COMMENT '注册用户数\t',
    `user_all_count`     bigint COMMENT '所有用户数',
    `profit_rate`        double COMMENT '利润率',
    `return_amount`      bigint COMMENT '派彩金额\r\n\r\n',
    `return_rate`        double COMMENT '\r\n\r\n返奖率,派彩金额/总投注量，大于等于100%的要标红',
    `merchant_level`     string COMMENT '商户等级\r\n\r\n',
    `bet_amount`         bigint COMMENT '\r\n\r\n投注额',
    `order_num`          bigint COMMENT '订单数',
    `first_bet_user`     bigint COMMENT '首投用户数',
    `bet_user_amount`    bigint COMMENT '投注用户数\r\n\r\n',
    `settle_user_rate`   double COMMENT '当周结算注单用户数/注册用户数',
    `settle_profit`      bigint COMMENT '当周结算注单-周天结算注单派彩金额',
    `settle_return`      bigint COMMENT '当周结算注单派彩金额',
    `settle_return_rate` double COMMENT '返奖率,结算注单派彩金额/结算注单总投注量，大于等于100%的要标红',
    `settle_bet_amount`  bigint COMMENT '结算注单总额',
    `settle_order_num`   bigint COMMENT '结算注单数',
    `live_user_rate`     double COMMENT '当周比赛注单用户数/注册用户数',
    `live_profit`        bigint COMMENT '当周比赛注单-当周比赛注单派彩金额',
    `live_return`        bigint COMMENT '当周比赛注单派彩金额',
    `live_return_rate`   double COMMENT '返奖率,比赛注单派彩金额/比赛注单总投注量，大于等于100%的要标红',
    `live_bet_amount`    bigint COMMENT '当周比赛注单总额',
    `live_order_num`     bigint COMMENT '当周比赛注单数',
    `merchant_name`      string COMMENT '商户名称'
)
    STORED BY 'org.elasticsearch.hadoop.hive.EsStorageHandler'
    TBLPROPERTIES (
        'es.nodes' = 'lan-big-data-node6:9200',
        'es.index.auto.create' = 'true',
        'es.resource' = 'merchant_order_month/doc',
        'es.read.metadata' = 'true',
        "es.mapping.id" = "id",
        'es.mapping.names' = 'id:id, merchant_code:merchantCode, time:time,bet_user_rate:betUserRate,profit:profit,
register_amount:registerAmount,user_all_count:userAllCount,profit_rate:profitRate,return_amount:returnAmount,
return_rate:returnRate,merchant_level:merchantLevel,bet_amount:betAmount,order_num:orderNum,first_bet_user:firstBetUser,bet_user_amount:betUserAmount,
settle_user_rate:settleUserRate,settle_profit:settleProfit,settle_return:settleReturn,settle_return_rate:settleReturnRate,settle_bet_amount:settleBetAmount,
settle_order_num:settleOrderNum,live_user_rate:liveUserRate,live_profit:liveProfit,live_return:liveReturn,live_return_rate:liveReturnRate,
live_bet_amount:liveBetAmount,live_order_num:liveOrderNum,merchant_name:merchantName',
        "es.write.operation" = "upsert");


DROP TABLE IF EXISTS user_all_life;
CREATE EXTERNAL TABLE `user_all_life`
(
    `id`                 bigint,
    `merchant_code`      string,
    `merchant_name`      string,
    `username`           string,
    `uid`                bigint,
    `create_time`        bigint,
    `user_level`         bigint,
    `bet_amount`         bigint,
    `settle_amount`      bigint,
    `profit`             bigint,
    `profit_rate`        double,
    `settle_amount_rate` double,
    `bet_num`            bigint,
    `last_login`         bigint,
    `last_bet`           bigint,
    `balance`            bigint
)
    STORED BY 'org.elasticsearch.hadoop.hive.EsStorageHandler'
    TBLPROPERTIES (
        'es.nodes' = 'lan-big-data-node6:9200',
        'es.index.auto.create' = 'true',
        'es.resource' = 'user_all_life/doc',
        'es.read.metadata' = 'true',
        'es.mapping.id' = 'id',
        'es.mapping.names' = 'id:id, merchant_code:merchantCode, merchant_name:merchantName,username:username,uid:uid,
create_time:createTime,user_level:userLevel,
bet_amount:betAmount,
settle_amount:settleAmount,
profit:profit,profit_rate:profitRate,settle_amount_rate:settleAmountRate,bet_num:betNum,last_login:lastLogin,
last_bet:lastBet,balance:balance',
        'es.write.operation' = 'upsert');


DROP TABLE IF EXISTS user_order_day;
CREATE EXTERNAL TABLE dws_report.`user_order_day`(
                                                            id string,
                                                            user_id bigint COMMENT '投注用户数\r\n',
                                                            user_name string COMMENT '用户名\r\n',
                                                            merchant_code string COMMENT '商户编码\r\n',
                                                            bet_num bigint COMMENT '投注笔数\r\n',
                                                            bet_amount bigint COMMENT '投注金额\r\n',
                                                            profit bigint COMMENT '盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)\r\n',
                                                            profit_rate bigint COMMENT '百分数：盈亏/投注\r\n',
                                                            settle_order_num bigint COMMENT '已结算注单数\r\n',
                                                            settle_order_amount bigint COMMENT '已结算注单总金额\r\n',
                                                            settle_profit bigint COMMENT '已结算注单盈亏金额盈亏金额\r\n',
                                                            settle_profit_rate bigint COMMENT '百分数：盈亏/投注\r\n',
                                                            live_order_num bigint COMMENT '当周比赛投注注单\r\n',
                                                            live_order_amount bigint COMMENT '当周比赛投注注单总金额\r\n',
                                                            live_profit bigint COMMENT '当周投注注单盈亏金额盈亏金额\r\n',
                                                            live_profit_rate bigint COMMENT '百分数：盈亏/投注\r\n',
                                                            time bigint COMMENT '格式202001\r\n',
                                                            merchant_name string COMMENT '商户名称\r\n'

)
    STORED BY 'org.elasticsearch.hadoop.hive.EsStorageHandler'
    TBLPROPERTIES(
        'es.nodes' = 'lan-big-data-node6:9200',
        'es.index.auto.create' = 'true',
        'es.resource' = 'user_order_day/doc',
        'es.read.metadata' = 'true',
        'es.mapping.id' = 'id',
        'es.mapping.names' = 'id:id,user_id:userId,user_name:username,merchant_code:merchantCode,
         bet_num:betNum, bet_amount:betAmount,profit:profit,profit_rate:profitRate,settle_order_num:settleOrderNum,
        settle_order_amount:settleOrderAmount,settle_profit:settleProfit,settle_profit_rate:settleProfitRate,
        live_order_num:liveOrderNum,live_order_amount:liveOrderAmount,live_profit:liveProfit,live_profit_rate:liveProfitRate,time:time,
        merchant_name:merchantName',
        'es.write.operation' = 'upsert');


DROP TABLE IF EXISTS user_order_month;
CREATE EXTERNAL TABLE dws_report.`user_order_month`(
                                                            id string,
                                                            user_id bigint COMMENT '投注用户数\r\n',
                                                            user_name string COMMENT '用户名\r\n',
                                                            merchant_code string COMMENT '商户编码\r\n',
                                                            bet_num bigint COMMENT '投注笔数\r\n',
                                                            bet_amount bigint COMMENT '投注金额\r\n',
                                                            profit bigint COMMENT '盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)\r\n',
                                                            profit_rate bigint COMMENT '百分数：盈亏/投注\r\n',
                                                            settle_order_num bigint COMMENT '已结算注单数\r\n',
                                                            settle_order_amount bigint COMMENT '已结算注单总金额\r\n',
                                                            settle_profit bigint COMMENT '已结算注单盈亏金额盈亏金额\r\n',
                                                            settle_profit_rate bigint COMMENT '百分数：盈亏/投注\r\n',
                                                            live_order_num bigint COMMENT '当周比赛投注注单\r\n',
                                                            live_order_amount bigint COMMENT '当周比赛投注注单总金额\r\n',
                                                            live_profit bigint COMMENT '当周投注注单盈亏金额盈亏金额\r\n',
                                                            live_profit_rate bigint COMMENT '百分数：盈亏/投注\r\n',
                                                            time bigint COMMENT '格式202001\r\n',
                                                            active_days bigint COMMENT '活跃天数\r\n',
                                                            merchant_name string COMMENT '商户名称\r\n'

)
    STORED BY 'org.elasticsearch.hadoop.hive.EsStorageHandler'
    TBLPROPERTIES(
        'es.nodes' = 'lan-big-data-node6:9200',
        'es.index.auto.create' = 'true',
        'es.resource' = 'user_order_month/doc',
        'es.read.metadata' = 'true',
        'es.mapping.id' = 'id',
        'es.mapping.names' = 'id:id,user_id:userId,user_name:username,merchant_code:merchantCode,
         bet_num:betNum, bet_amount:betAmount,profit:profit,profit_rate:profitRate,settle_order_num:settleOrderNum,
        settle_order_amount:settleOrderAmount,settle_profit:settleProfit,settle_profit_rate:settleProfitRate,
        live_order_num:liveOrderNum,live_order_amount:liveOrderAmount,live_profit:liveProfit,live_profit_rate:liveProfitRate,time:time,
         active_days:activeDays,merchant_name:merchantName',
        'es.write.operation' = 'upsert');


DROP TABLE IF EXISTS user_order_week;
CREATE EXTERNAL TABLE dws_report.`user_order_week`(
                                                          id string,
                                                            user_id bigint COMMENT '投注用户数\r\n',
                                                            user_name string COMMENT '用户名\r\n',
                                                            merchant_code string COMMENT '商户编码\r\n',
                                                            bet_num bigint COMMENT '投注笔数\r\n',
                                                            bet_amount bigint COMMENT '投注金额\r\n',
                                                            profit bigint COMMENT '盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)\r\n',
                                                            profit_rate bigint COMMENT '百分数：盈亏/投注\r\n',
                                                            settle_order_num bigint COMMENT '已结算注单数\r\n',
                                                            settle_order_amount bigint COMMENT '已结算注单总金额\r\n',
                                                            settle_profit bigint COMMENT '已结算注单盈亏金额盈亏金额\r\n',
                                                            settle_profit_rate bigint COMMENT '百分数：盈亏/投注\r\n',
                                                            live_order_num bigint COMMENT '当周比赛投注注单\r\n',
                                                            live_order_amount bigint COMMENT '当周比赛投注注单总金额\r\n',
                                                            live_profit bigint COMMENT '当周投注注单盈亏金额盈亏金额\r\n',
                                                            live_profit_rate bigint COMMENT '百分数：盈亏/投注\r\n',
                                                            time bigint COMMENT '格式202001\r\n',
                                                            active_days bigint COMMENT '活跃天数\r\n',
                                                            merchant_name string COMMENT '商户名称\r\n'

)
    STORED BY 'org.elasticsearch.hadoop.hive.EsStorageHandler'
    TBLPROPERTIES(
        'es.nodes' = 'lan-big-data-node6:9200',
        'es.index.auto.create' = 'true',
        'es.resource' = 'user_order_week/doc',
        'es.read.metadata' = 'true',
        'es.mapping.id' = 'id',
        'es.mapping.names' = 'id:id,user_id:userId,user_name:username,merchant_code:merchantCode,
         bet_num:betNum, bet_amount:betAmount,profit:profit,profit_rate:profitRate,settle_order_num:settleOrderNum,
        settle_order_amount:settleOrderAmount,settle_profit:settleProfit,settle_profit_rate:settleProfitRate,
        live_order_num:liveOrderNum,live_order_amount:liveOrderAmount,live_profit:liveProfit,live_profit_rate:liveProfitRate,time:time,
        active_days:activeDays,merchant_name:merchantName',
        'es.write.operation' = 'upsert');

DROP TABLE IF EXISTS user_sport_order_day;
CREATE EXTERNAL TABLE dws_report.`user_sport_order_day`(
                                                            id string,
                                                            sport_id bigint COMMENT '体育种类\r\n',
                                                            user_id bigint COMMENT '投注用户数\r\n',
                                                            user_name string COMMENT '用户名\r\n',
                                                            merchant_code string COMMENT '商户编码\r\n',
                                                            bet_num bigint COMMENT '投注笔数\r\n',
                                                            bet_amount bigint COMMENT '投注金额\r\n',
                                                            profit bigint COMMENT '盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)\r\n',
                                                            profit_rate bigint COMMENT '百分数：盈亏/投注\r\n',
                                                            settle_order_num bigint COMMENT '已结算注单数\r\n',
                                                            settle_order_amount bigint COMMENT '已结算注单总金额\r\n',
                                                            settle_profit bigint COMMENT '已结算注单盈亏金额盈亏金额\r\n',
                                                            settle_profit_rate bigint COMMENT '百分数：盈亏/投注\r\n',
                                                            live_order_num bigint COMMENT '当周比赛投注注单\r\n',
                                                            live_order_amount bigint COMMENT '当周比赛投注注单总金额\r\n',
                                                            live_profit bigint COMMENT '当周投注注单盈亏金额盈亏金额\r\n',
                                                            live_profit_rate bigint COMMENT '百分数：盈亏/投注\r\n',
                                                            time bigint COMMENT '格式202001\r\n',
                                                            merchant_name string COMMENT '商户名称\r\n'
)
    STORED BY 'org.elasticsearch.hadoop.hive.EsStorageHandler'
    TBLPROPERTIES(
      'es.nodes' = 'lan-big-data-node6:9200',
        'es.index.auto.create' = 'true',
        'es.resource' = 'user_sport_order_day/doc',
        'es.read.metadata' = 'true',
        'es.mapping.id' = 'id',
        'es.mapping.names' = 'id:id,sport_id:sportId,user_id:userId,user_name:username,merchant_code:merchantCode,
         bet_num:betNum, bet_amount:betAmount,profit:profit,profit_rate:profitRate,settle_order_num:settleOrderNum,
        settle_order_amount:settleOrderAmount,settle_profit:settleProfit,settle_profit_rate:settleProfitRate,
        live_order_num:liveOrderNum,live_order_amount:liveOrderAmount,live_profit:liveProfit,live_profit_rate:liveProfitRate,time:time,
        merchant_name:merchantName',
        'es.write.operation' = 'upsert');


DROP TABLE IF EXISTS user_sport_order_week;
CREATE EXTERNAL TABLE dws_report.`user_sport_order_week`(
                                                               id string,
                                                            sport_id bigint COMMENT '体育种类\r\n',
                                                            user_id bigint COMMENT '投注用户数\r\n',
                                                            user_name string COMMENT '用户名\r\n',
                                                            merchant_code string COMMENT '商户编码\r\n',
                                                            bet_num bigint COMMENT '投注笔数\r\n',
                                                            bet_amount bigint COMMENT '投注金额\r\n',
                                                            profit bigint COMMENT '盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)\r\n',
                                                            profit_rate bigint COMMENT '百分数：盈亏/投注\r\n',
                                                            settle_order_num bigint COMMENT '已结算注单数\r\n',
                                                            settle_order_amount bigint COMMENT '已结算注单总金额\r\n',
                                                            settle_profit bigint COMMENT '已结算注单盈亏金额盈亏金额\r\n',
                                                            settle_profit_rate bigint COMMENT '百分数：盈亏/投注\r\n',
                                                            live_order_num bigint COMMENT '当周比赛投注注单\r\n',
                                                            live_order_amount bigint COMMENT '当周比赛投注注单总金额\r\n',
                                                            live_profit bigint COMMENT '当周投注注单盈亏金额盈亏金额\r\n',
                                                            live_profit_rate bigint COMMENT '百分数：盈亏/投注\r\n',
                                                            time bigint COMMENT '格式202001\r\n',
                                                            active_days bigint COMMENT '活跃天数\r\n',
                                                            merchant_name string COMMENT '商户名称\r\n'
)
    STORED BY 'org.elasticsearch.hadoop.hive.EsStorageHandler'
    TBLPROPERTIES(
      'es.nodes' = 'lan-big-data-node6:9200',
        'es.index.auto.create' = 'true',
        'es.resource' = 'user_sport_order_week/doc',
        'es.read.metadata' = 'true',
        'es.mapping.id' = 'id',
        'es.mapping.names' = 'id:id,sport_id:sportId,user_id:userId,user_name:username,merchant_code:merchantCode,
        bet_num:betNum, bet_amount:betAmount,profit:profit,profit_rate:profitRate,settle_order_num:settleOrderNum,
        settle_order_amount:settleOrderAmount,settle_profit:settleProfit,settle_profit_rate:settleProfitRate,
        live_order_num:liveOrderNum,live_order_amount:liveOrderAmount,live_profit:liveProfit,live_profit_rate:liveProfitRate,time:time,
        active_days:activeDays,merchant_name:merchantName',
        'es.write.operation' = 'upsert');

DROP TABLE IF EXISTS user_sport_order_month;
CREATE EXTERNAL TABLE dws_report.`user_sport_order_month`(
                                                                id string,
                                                            sport_id bigint COMMENT '体育种类\r\n',
                                                            user_id bigint COMMENT '投注用户数\r\n',
                                                            user_name string COMMENT '用户名\r\n',
                                                            merchant_code string COMMENT '商户编码\r\n',
                                                            bet_num bigint COMMENT '投注笔数\r\n',
                                                            bet_amount bigint COMMENT '投注金额\r\n',
                                                            profit bigint COMMENT '盈亏金额(会计计数方式，金额以人民币为基准，盈利为正数，亏损为负数。盈亏是从投注用户角度来说的，盈利不含本金，亏损指本金亏损的部分)\r\n',
                                                            profit_rate bigint COMMENT '百分数：盈亏/投注\r\n',
                                                            settle_order_num bigint COMMENT '已结算注单数\r\n',
                                                            settle_order_amount bigint COMMENT '已结算注单总金额\r\n',
                                                            settle_profit bigint COMMENT '已结算注单盈亏金额盈亏金额\r\n',
                                                            settle_profit_rate bigint COMMENT '百分数：盈亏/投注\r\n',
                                                            live_order_num bigint COMMENT '当周比赛投注注单\r\n',
                                                            live_order_amount bigint COMMENT '当周比赛投注注单总金额\r\n',
                                                            live_profit bigint COMMENT '当周投注注单盈亏金额盈亏金额\r\n',
                                                            live_profit_rate bigint COMMENT '百分数：盈亏/投注\r\n',
                                                            time bigint COMMENT '格式202001\r\n',
                                                            active_days bigint COMMENT '活跃天数\r\n',
                                                            merchant_name string COMMENT '商户名称\r\n'
)
    STORED BY 'org.elasticsearch.hadoop.hive.EsStorageHandler'
    TBLPROPERTIES(
      'es.nodes' = 'lan-big-data-node6:9200',
        'es.index.auto.create' = 'true',
        'es.resource' = 'user_sport_order_month/doc',
        'es.read.metadata' = 'true',
        'es.mapping.id' = 'id',
        'es.mapping.names' = 'id:id,sport_id:sportId,user_id:userId,user_name:username,merchant_code:merchantCode,
         bet_num:betNum, bet_amount:betAmount,profit:profit,profit_rate:profitRate,settle_order_num:settleOrderNum,
        settle_order_amount:settleOrderAmount,settle_profit:settleProfit,settle_profit_rate:settleProfitRate,
        live_order_num:liveOrderNum,live_order_amount:liveOrderAmount,live_profit:liveProfit,live_profit_rate:liveProfitRate,time:time,
        active_days:activeDays,merchant_name:merchantName',
        'es.write.operation' = 'upsert');



DROP TABLE IF EXISTS merchant_sport_order_day;
CREATE TABLE dws_report.`merchant_sport_order_day`(
                                     `id` string,
									  `sport_id` string,
                                     `merchant_code` string,
                                     `time` bigint COMMENT '格式:20210110\r\n',
                                     `bet_user_rate` double COMMENT '投注用户数/注册用户数\r\n',
                                     `profit` bigint COMMENT '投注量-派彩金额，即指商户注单的毛盈利，暂不计算返水等',
                                     `register_amount` bigint COMMENT '注册用户数\t',
                                     `user_all_count` bigint COMMENT '所有用户',
                                     `return_rate` double COMMENT '\r\n\r\n返奖率,派彩金额/总投注量，大于等于100%的要标红',
                                     `return_amount` bigint COMMENT '派彩金额\r\n\r\n',
                                     `merchant_level` string COMMENT '商户等级\r\n\r\n',
                                     `bet_amount` bigint COMMENT '\r\n\r\n投注额',
                                     `order_num` bigint COMMENT '订单数',
                                     `first_bet_user` bigint COMMENT '首投用户数',
                                     `bet_user_amount` bigint COMMENT '投注用户数\r\n\r\n',
                                     `settle_user_rate` double COMMENT '当天结算注单用户数/注册用户数',
                                     `settle_profit` bigint COMMENT '当天结算注单-当天结算注单派彩金额',
                                     `settle_return` bigint COMMENT '当天结算注单派彩金额',
                                     `settle_return_rate` double COMMENT '返奖率,结算注单派彩金额/结算注单总投注量，大于等于100%的要标红',
                                     `settle_bet_amount` bigint COMMENT '结算注单总额',
                                     `settle_order_num` bigint COMMENT '结算注单数',
                                     `live_user_rate` double COMMENT '当天比赛注单用户数/注册用户数',
                                     `live_profit` bigint COMMENT '当天比赛注单-当天比赛注单派彩金额',
                                     `live_return` bigint COMMENT '当天比赛注单派彩金额',
                                     `live_return_rate` double COMMENT '返奖率,比赛注单派彩金额/比赛注单总投注量，大于等于100%的要标红',
                                     `live_bet_amount` bigint COMMENT '当日比赛注单总额',
                                     `live_order_num` bigint COMMENT '当日比赛注单数',
                                     `merchant_name` string COMMENT '商户名称')
    STORED BY 'org.elasticsearch.hadoop.hive.EsStorageHandler'
    TBLPROPERTIES(
        'es.nodes' = 'lan-big-data-node6:9200,lan-big-data-node7:9200,lan-big-data-node8:9200',
        'es.index.auto.create' = 'true',
        'es.resource' = 'merchant_sport_order_day/doc',
        'es.read.metadata' = 'true',
        'es.mapping.id' = 'id',
        'es.mapping.names' = 'id:id,sport_id:sportId,merchant_code:merchantCode, time:time,bet_user_rate:betUserRate,profit:profit,
user_all_count:userAllCount,
register_amount:registerAmount,
return_rate:returnRate,
return_amount:returnAmount,merchant_level:merchantLevel,bet_amount:betAmount,
order_num:orderNum,first_bet_user:firstBetUser,bet_user_amount:betUserAmount,
settle_user_rate:settleUserRate,settle_profit:settleProfit,settle_return:settleReturn,settle_return_rate:settleReturnRate,settle_bet_amount:settleBetAmount,
settle_order_num:settleOrderNum,live_user_rate:liveUserRate,live_profit:liveProfit,live_return:liveReturn,live_return_rate:liveReturnRate,
live_bet_amount:liveBetAmount,live_order_num:liveOrderNum,merchant_name:merchantName',
        'es.write.operation' = 'upsert');


DROP TABLE IF EXISTS merchant_sport_order_month;
CREATE EXTERNAL TABLE dws_report.`merchant_sport_order_month`(
                                                           `id` string,
                                                             `sport_id` string,
                                                           `merchant_code` string,
                                                           `time` bigint COMMENT '格式:202101\r\n',
                                                           `bet_user_rate` double COMMENT '投注用户数/注册用户数\r\n',
                                                           `profit` bigint COMMENT '投注量-派彩金额，即指商户注单的毛盈利，暂不计算返水等',
                                                           `user_all_count` bigint COMMENT '所有用户数',
                                                           `register_amount` bigint COMMENT '注册用户数\t',
                                                           `return_rate` double COMMENT '\r\n\r\n返奖率,派彩金额/总投注量，大于等于100%的要标红',
                                                           `return_amount` bigint COMMENT '派彩金额\r\n\r\n',
                                                           `merchant_level` string COMMENT '商户等级\r\n\r\n',
                                                           `bet_amount` bigint COMMENT '\r\n\r\n投注额',
                                                           `order_num` bigint COMMENT '订单数',
                                                           `first_bet_user` bigint COMMENT '首投用户数',
                                                           `bet_user_amount` bigint COMMENT '投注用户数\r\n\r\n',
                                                           `settle_user_rate` double COMMENT '当月结算注单用户数/注册用户数',
                                                           `settle_profit` bigint COMMENT '当月结算注单-月结算注单派彩金额',
                                                           `settle_return` bigint COMMENT '当月结算注单派彩金额',
                                                           `settle_return_rate` double COMMENT '返奖率,结算注单派彩金额/结算注单总投注量，大于等于100%的要标红',
                                                           `settle_bet_amount` bigint COMMENT '结算注单总额',
                                                           `settle_order_num` bigint COMMENT '结算注单数',
                                                           `live_user_rate` double COMMENT '当月比赛注单用户数/注册用户数',
                                                           `live_profit` bigint COMMENT '当月比赛注单-当周比赛注单派彩金额',
                                                           `live_return` bigint COMMENT '当月比赛注单派彩金额',
                                                           `live_return_rate` double COMMENT '返奖率,比赛注单派彩金额/比赛注单总投注量，大于等于100%的要标红',
                                                           `live_bet_amount` bigint COMMENT '当月比赛注单总额',
                                                           `live_order_num` bigint COMMENT '当月比赛注单数',
                                                           `merchant_name` string COMMENT '商户名称')

    STORED BY 'org.elasticsearch.hadoop.hive.EsStorageHandler'
    TBLPROPERTIES(
        'es.nodes' = 'lan-big-data-node6:9200,lan-big-data-node7:9200,lan-big-data-node8:9200',
        'es.index.auto.create' = 'true',
        'es.resource' = 'merchant_sport_order_month/doc',
        'es.read.metadata' = 'true',
        'es.mapping.id' = 'id',
        'es.mapping.names' = 'id:id, sport_id:sportId,merchant_code:merchantCode, time:time,bet_user_rate:betUserRate,profit:profit,
user_all_count:userAllCount,
register_amount:registerAmount,
return_rate:returnRate,
return_amount:returnAmount,merchant_level:merchantLevel,bet_amount:betAmount,
order_num:orderNum,first_bet_user:firstBetUser,bet_user_amount:betUserAmount,
settle_user_rate:settleUserRate,settle_profit:settleProfit,settle_return:settleReturn,settle_return_rate:settleReturnRate,settle_bet_amount:settleBetAmount,
settle_order_num:settleOrderNum,live_user_rate:liveUserRate,live_profit:liveProfit,live_return:liveReturn,live_return_rate:liveReturnRate,
live_bet_amount:liveBetAmount,live_order_num:liveOrderNum,merchant_name:merchantName',
        'es.write.operation' = 'upsert');


DROP TABLE IF EXISTS merchant_sport_order_week;
CREATE EXTERNAL TABLE dws_report.`merchant_sport_order_week`(
                                                           `id` string,
                                                             `sport_id` string,
                                                           `merchant_code` string,
                                                           `time` bigint COMMENT '格式:202101\r\n',
                                                           `bet_user_rate` double COMMENT '投注用户数/注册用户数\r\n',
                                                           `profit` bigint COMMENT '投注量-派彩金额，即指商户注单的毛盈利，暂不计算返水等',
                                                           `user_all_count` bigint COMMENT '所有用户数',
                                                           `register_amount` bigint COMMENT '注册用户数\t',
                                                           `return_rate` double COMMENT '\r\n\r\n返奖率,派彩金额/总投注量，大于等于100%的要标红',
                                                           `return_amount` bigint COMMENT '派彩金额\r\n\r\n',
                                                           `merchant_level` string COMMENT '商户等级\r\n\r\n',
                                                           `bet_amount` bigint COMMENT '\r\n\r\n投注额',
                                                           `order_num` bigint COMMENT '订单数',
                                                           `first_bet_user` bigint COMMENT '首投用户数',
                                                           `bet_user_amount` bigint COMMENT '投注用户数\r\n\r\n',
                                                           `settle_user_rate` double COMMENT '当月结算注单用户数/注册用户数',
                                                           `settle_profit` bigint COMMENT '当月结算注单-月结算注单派彩金额',
                                                           `settle_return` bigint COMMENT '当月结算注单派彩金额',
                                                           `settle_return_rate` double COMMENT '返奖率,结算注单派彩金额/结算注单总投注量，大于等于100%的要标红',
                                                           `settle_bet_amount` bigint COMMENT '结算注单总额',
                                                           `settle_order_num` bigint COMMENT '结算注单数',
                                                           `live_user_rate` double COMMENT '当月比赛注单用户数/注册用户数',
                                                           `live_profit` bigint COMMENT '当月比赛注单-当周比赛注单派彩金额',
                                                           `live_return` bigint COMMENT '当月比赛注单派彩金额',
                                                           `live_return_rate` double COMMENT '返奖率,比赛注单派彩金额/比赛注单总投注量，大于等于100%的要标红',
                                                           `live_bet_amount` bigint COMMENT '当月比赛注单总额',
                                                           `live_order_num` bigint COMMENT '当月比赛注单数',
                                                           `merchant_name` string COMMENT '商户名称')

    STORED BY 'org.elasticsearch.hadoop.hive.EsStorageHandler'
    TBLPROPERTIES(
         'es.nodes' = 'lan-big-data-node6:9200,lan-big-data-node7:9200,lan-big-data-node8:9200',
        'es.index.auto.create' = 'true',
        'es.resource' = 'merchant_sport_order_week/doc',
        'es.read.metadata' = 'true',
        'es.mapping.id' = 'id',
        'es.mapping.names' = 'id:id, sport_id:sportId,merchant_code:merchantCode, time:time,bet_user_rate:betUserRate,profit:profit,
user_all_count:userAllCount,
register_amount:registerAmount,
return_rate:returnRate,
return_amount:returnAmount,merchant_level:merchantLevel,bet_amount:betAmount,
order_num:orderNum,first_bet_user:firstBetUser,bet_user_amount:betUserAmount,
settle_user_rate:settleUserRate,settle_profit:settleProfit,settle_return:settleReturn,settle_return_rate:settleReturnRate,settle_bet_amount:settleBetAmount,
settle_order_num:settleOrderNum,live_user_rate:liveUserRate,live_profit:liveProfit,live_return:liveReturn,live_return_rate:liveReturnRate,
live_bet_amount:liveBetAmount,live_order_num:liveOrderNum,merchant_name:merchantName',
        'es.write.operation' = 'upsert');

---------------------------------概况分析表-------------------------------------

CREATE EXTERNAL TABLE dws_report.rcs_analyze_overview_day (
          sid string,
          merchant_id bigint,
          merchant_code string,
          match_date bigint,
          order_day string,
          order_year string,
          order_phase string,
          order_week string,
          sport_id int,
          tournament_id int,
          play_id int,
          match_type int,
          bet_order_amount bigint,
          bet_order_nums bigint,
          match_bet_times bigint,
          profit_amount bigint,
          profit_point bigint,
          per_bet_order_nums double,
          per_capita_order_amount bigint,
          per_capita_order_nums double,
          amount_ltone_thousand_count bigint,
          amount_gtone_thousand_count bigint,
          amount_gttwo_thousand_count bigint,
          amount_gtfive_thousand_count bigint,
          amount_gtten_thousand_count bigint
)
    STORED BY 'org.elasticsearch.hadoop.hive.EsStorageHandler'
    TBLPROPERTIES(
        'es.nodes' = '172.18.178.76:9200,172.18.178.77:9200,172.18.178.78:9200',
        'es.index.auto.create' = 'true',
        'es.resource' = 'rcs_analyze_overview_day/doc',
        'es.read.metadata' = 'true',
        'es.mapping.id'= 'sid',
        'es.mapping.names' = 'sid:sid,
            merchant_id:merchant_id,
            merchant_code:merchant_code,
            sport_id:sport_id,
            tournament_id:tournament_id,
            play_id:play_id,
            match_type:match_type,
            bet_order_amount:bet_order_amount,
            bet_order_nums:bet_order_nums,
            match_bet_times:match_bet_times,
            profit_amount:profit_amount,
            profit_point:profit_point,
            per_bet_order_nums:per_bet_order_nums,
            per_capita_order_amount:per_capita_order_amount,
            per_capita_order_nums:per_capita_order_nums,
            amount_ltone_thousand_count:amount_ltone_thousand_count,
            amount_gtone_thousand_count:amount_gtone_thousand_count,
            amount_gttwo_thousand_count:amount_gttwo_thousand_count,
            amount_gtfive_thousand_count:amount_gtfive_thousand_count,
            amount_gtten_thousand_count:amount_gtten_thousand_count
    ');

--------用户报表-----------------------------------------------------------------------------------
DROP TABLE IF EXISTS dws_report.rcs_user_statistic_day;

CREATE EXTERNAL TABLE dws_report.rcs_user_statistic_day (
                                                 sid string,
                                                 uid string,
                                                 user_level string,
                                                 match_date bigint,
                                                 username string,
                                                 merchant_id bigint,
                                                 merchant_code string,
                                                 bet_amount bigint,
                                                 bet_order_nums bigint,
                                                 losses bigint,
                                                 losses_point float,
                                                 profit_bet_nums bigint,
                                                 profit_point float,
                                                 refuse_order_nums bigint,
                                                 refuse_order_amount bigint,
                                                 cancel_order_nums bigint,
                                                 cancel_order_amount bigint,
                                                 odds_high bigint,
                                                 odds_low bigint,
                                                 football_asian_handicap_master_makert bigint,
                                                 football_asian_handicap_slave_makert bigint,
                                                 football_goal_line_master_makert bigint,
                                                 football_goal_line_slave_makert bigint,
                                                 race_football bigint,
                                                 race_basketball bigint,
                                                 race_other bigint,
                                                 race_multiple_bet bigint,
                                                 bsketball_goal_line bigint,
                                                 bsketball_asian_handicap bigint
)
    STORED BY 'org.elasticsearch.hadoop.hive.EsStorageHandler'
    TBLPROPERTIES(
        'es.nodes' = '172.18.178.76:9200,172.18.178.77:9200,172.18.178.78:9200',
        'es.index.auto.create' = 'true',
        'es.resource' = 'rcs_user_statistic_day/doc',
        'es.read.metadata' = 'true',
        'es.mapping.names' = 'sid:sid, uid:uid,user_level:user_level,
            match_date:match_date,
            username:username,
            merchant_id:merchant_id,
            merchant_code:merchant_code,
            bet_amount:bet_amount,
            bet_order_nums:bet_order_nums,
            losses:losses,
            losses_point:losses_point,
            profit_bet_nums:profit_bet_nums,
            profit_point:profit_point,
            refuse_order_nums:refuse_order_nums,
            refuse_order_amount:refuse_order_amount,
            cancel_order_nums:cancel_order_nums,
            cancel_order_amount:cancel_order_amount,
            odds_high:odds_high,
            odds_low:odds_low,
            football_asian_handicap_master_makert:football_asian_handicap_master_makert,
            football_asian_handicap_slave_makert:football_asian_handicap_slave_makert,
            football_goal_line_master_makert:football_goalLine_master_makert,
            football_goal_line_slave_makert:football_goal_line_slave_makert,
            race_football:race_football,
            race_basketball:race_basketball,
            race_other:race_other,
            ace_multiple_bet:race_multiple_bet,
            bsketball_goal_line:bsketball_goal_line,
            bsketball_asian_handicap:bsketball_asian_handicap
            ',
        'es.mapping.id' = 'sid');

--------赛事注单用户统计表-----------------------------------------------------------------------------------
DROP TABLE IF EXISTS rcs_match_user_day;

CREATE EXTERNAL TABLE dws_report.rcs_match_user_day (
                                                        mud_id string,
                                                        begin_time bigint,
                                                        match_id bigint,
                                                        sport_id bigint,
                                                        tournament_id bigint,
                                                        match_info string,
                                                        uid bigint,
                                                        username string,
                                                        level_id string,
                                                        total_bet_amount bigint,
                                                        bet_amount bigint,
                                                        settle_amount bigint,
                                                        order_num bigint,
                                                        profit_amount bigint,
                                                        live_bet_amount bigint,
                                                        live_order_num bigint,
                                                        live_profit_amount bigint,
                                                        pre_bet_amount bigint,
                                                        pre_order_num bigint,
                                                        pre_profit_amount bigint,
                                                        pre_two_bet_amount bigint,
                                                        pre_two_order_num bigint,
                                                        pre_two_profit_amount bigint,
                                                        pre_one_bet_amount bigint,
                                                        pre_one_order_num bigint,
                                                        pre_one_profit_amount bigint
)
    STORED BY 'org.elasticsearch.hadoop.hive.EsStorageHandler'
    TBLPROPERTIES(
        'es.nodes' = '172.18.178.76:9200,172.18.178.77:9200,172.18.178.78:9200',
        'es.index.auto.create' = 'true',
        'es.resource' = 'rcs_match_user_day/doc',
        'es.read.metadata' = 'true',
        'es.mapping.id' = 'mud_id',
        'es.mapping.names' = 'mud_id:mud_id,begin_time:begin_time,match_id:match_id,sport_id:sport_id,tournament_id:tournament_id,match_info:match_info,uid:uid,username:username,
level_id:level_id,total_bet_amount:total_bet_amount,bet_amount:bet_amount,settle_amount:settle_amount,order_num:order_num,profit_amount:profit_amount,live_bet_amount:live_bet_amount,
live_order_num:live_order_num,live_profit_amount:live_profit_amount,pre_bet_amount:pre_bet_amount,pre_order_num:pre_order_num,pre_profit_amount:pre_profit_amount,
pre_two_bet_amount:pre_two_bet_amount,pre_two_order_num:pre_two_order_num,pre_two_profit_amount:pre_two_profit_amount,pre_one_bet_amount:pre_one_bet_amount,
pre_one_order_num:pre_one_order_num,pre_one_profit_amount:pre_one_profit_amount');

--------赛事注单玩法统计表-----------------------------------------------------------------------------------
DROP TABLE IF EXISTS rcs_match_play_day;

CREATE EXTERNAL TABLE dws_report.rcs_match_play_day (
                                                        mpd_id string,
                                                        begin_time bigint,
                                                        match_id bigint,
                                                        sport_id bigint,
                                                        tournament_id bigint,
                                                        play_id bigint,
                                                        match_type bigint,
                                                        match_info string,
                                                        total_bet_amount bigint,
                                                        play_name string,
                                                        bet_amount bigint,
                                                        settle_amount bigint,
                                                        order_num bigint,
                                                        user_num bigint,
                                                        bet_proportion float,
                                                        profit_amount bigint,
                                                        average_amount bigint,
                                                        person_order_num float,
                                                        person_bet_amount bigint,
                                                        option_one_amount bigint,
                                                        option_one_num bigint,
                                                        option_two_amount bigint,
                                                        option_two_num bigint
)
    STORED BY 'org.elasticsearch.hadoop.hive.EsStorageHandler'
    TBLPROPERTIES(
        'es.nodes' = '172.18.178.76:9200,172.18.178.77:9200,172.18.178.78:9200',
        'es.index.auto.create' = 'true',
        'es.resource' = 'rcs_match_play_day/doc',
        'es.read.metadata' = 'true',
        'es.mapping.id' = 'mpd_id',
        'es.mapping.names' = 'mpd_id:mpd_id,begin_time:begin_time,match_id:match_id,sport_id:sport_id,tournament_id:tournament_id,play_id:play_id,match_type:match_type,match_info:match_info,
total_bet_amount:total_bet_amount,play_name:play_name,bet_amount:bet_amount,settle_amount:settle_amount,order_num:order_num,user_num:user_num,bet_proportion:bet_proportion,
profit_amount:profit_amount,average_amount:average_amount,person_order_num:person_order_num,person_bet_amount:person_bet_amount,option_one_amount:option_one_amount,
option_one_num:option_one_num,option_two_amount:option_two_amount,option_two_num:option_two_num');

--------赛事注单汇总统计表-----------------------------------------------------------------------------------
DROP TABLE IF EXISTS rcs_match_order_day;

CREATE EXTERNAL TABLE dws_report.rcs_match_order_day (
                                                         mod_id string,
                                                         begin_time bigint,
                                                         match_id bigint,
                                                         sport_id bigint,
                                                         tournament_id bigint,
                                                         match_info string,
                                                         match_amount bigint,
                                                         match_settle_amount bigint,
                                                         match_num bigint,
                                                         match_actual_amount bigint,
                                                         match_theory_amount bigint,
                                                         match_profit_amount bigint,
                                                         match_proportion float,
                                                         live_amount bigint,
                                                         live_num bigint,
                                                         live_average_amount bigint,
                                                         live_average_num bigint,
                                                         live_user_amount bigint,
                                                         pre_amount bigint,
                                                         pre_num bigint,
                                                         pre_average_amount bigint,
                                                         pre_average_num bigint,
                                                         pre_user_amount bigint,
                                                         pre_two_amount bigint,
                                                         pre_two_num bigint,
                                                         pre_two_average_amount bigint,
                                                         pre_two_average_num bigint,
                                                         pre_two_user_amount bigint,
                                                         pre_one_amount bigint,
                                                         pre_one_num bigint,
                                                         pre_one_average_amount bigint,
                                                         pre_one_average_num bigint,
                                                         pre_one_user_amount bigint,
                                                         live_actual_amount bigint,
                                                         live_theory_amount bigint,
                                                         live_profit_amount bigint,
                                                         live_proportion float,
                                                         pre_actual_amount bigint,
                                                         pre_theory_amount bigint,
                                                         pre_profit_amount bigint,
                                                         pre_proportion float,
                                                         pre_two_actual_amount bigint,
                                                         pre_two_theory_amount bigint,
                                                         pre_two_profit_amount bigint,
                                                         pre_two_proportion float,
                                                         pre_one_actual_amount bigint,
                                                         pre_one_theory_amount bigint,
                                                         pre_one_profit_amount bigint,
                                                         pre_one_proportion float
)
    STORED BY 'org.elasticsearch.hadoop.hive.EsStorageHandler'
    TBLPROPERTIES(
        'es.nodes' = '172.18.178.76:9200,172.18.178.77:9200,172.18.178.78:9200',
        'es.index.auto.create' = 'true',
        'es.resource' = 'rcs_match_order_day/doc',
        'es.read.metadata' = 'true',
        'es.mapping.id' = 'mod_id',
        'es.mapping.names' = 'mod_id:mod_id,begin_time:begin_time,match_id:match_id,sport_id:sport_id,tournament_id:tournament_id,match_info:match_info,match_amount:match_amount,
match_settle_amount:match_settle_amount,match_num:match_num,match_actual_amount:match_actual_amount,match_theory_amount:match_theory_amount,match_profit_amount:match_profit_amount,
match_proportion:match_proportion,live_amount:live_amount,live_num:live_num,live_average_amount:live_average_amount,live_average_num:live_average_num,live_user_amount:live_user_amount,
pre_amount:pre_amount,pre_num:pre_num,pre_average_amount:pre_average_amount,pre_average_num:pre_average_num,pre_user_amount:pre_user_amount,pre_two_amount:pre_two_amount,
pre_two_num:pre_two_num,pre_two_average_amount:pre_two_average_amount,pre_two_average_num:pre_two_average_num,pre_two_user_amount:pre_two_user_amount,pre_one_amount:pre_one_amount,
pre_one_num:pre_one_num,pre_one_average_amount:pre_one_average_amount,pre_one_average_num:pre_one_average_num,pre_one_user_amount:pre_one_user_amount,live_actual_amount:live_actual_amount,
live_theory_amount:live_theory_amount,live_profit_amount:live_profit_amount,live_proportion:live_proportion,pre_actual_amount:pre_actual_amount,pre_theory_amount:pre_theory_amount,
pre_profit_amount:pre_profit_amount,pre_proportion:pre_proportion,pre_two_actual_amount:pre_two_actual_amount,pre_two_theory_amount:pre_two_theory_amount,pre_two_profit_amount:pre_two_profit_amount,
pre_two_proportion:pre_two_proportion,pre_one_actual_amount:pre_one_actual_amount,pre_one_theory_amount:pre_one_theory_amount,pre_one_profit_amount:pre_one_profit_amount,
pre_one_proportion:pre_one_proportion');