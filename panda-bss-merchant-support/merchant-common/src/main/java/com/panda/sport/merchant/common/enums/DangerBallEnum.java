package com.panda.sport.merchant.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 危险球枚举
 */
public enum DangerBallEnum {
    temporary_interruption("temporary_interruption", "临时中断", "仅用于短暂中断（例如受伤）", "拒单事件-仅用于短暂中断（例如受伤）"),
    game_on("game_on", "继续比赛", "比赛被临时中断后继续进行", ""),
    suspension_over("suspension_over", "暂停结束", "因球员引起的临时暂停结束当球员犯规或者被罚下场重新开始比赛时发出该指令", "拒单事件-暂停结束"),
    goal("goal", "危险(进球)", "确定进球后触发该事件", "拒单事件-进球"),
    yellow_card("yellow_card", "危险(黄牌)", "当确定被罚黄牌时触发该事件", "拒单事件-黄牌(罚牌玩法集)"),
    suspension("suspension", "比赛暂停", "球员临时暂停。当球员在一场比赛中因犯规被罚下或适用其他联赛规则判罚下场时产生该暂停", "拒单事件-比赛暂停"),
    yellow_red_card("yellow_red_card", "危险(黄红牌)", "当确认球员因两张黄牌被罚下场时使用", "拒单事件-黄红牌"),
    red_card("red_card", "危险(红牌)", "确定使用红牌时使用", "拒单事件-红牌"),
    substitution("substitution", "换人", "发生在某个球队需要换人的时候；需要球队示意", "拒单事件-换人"),
    injury_time("injury_time", "伤停补时", "伤停补时预计时长信息", "拒单事件-伤停补时"),
    possession("possession", "控球权", "", "拒单事件-控球权"),
    free_kick_count("free_kick_count", "任意球次数", "当某个球队获得任意球时手工更新任意球数", "拒单事件-任意球次数"),
    goal_kick_count("goal_kick_count", "球门球次数", "当某个球队有球门球产生时手工更新球门球次数", "拒单事件-球门球次数"),
    throw_in_count("throw_in_count", "掷界外球次数", "当某个球队掷界外球时人工更新进球数", "拒单事件-掷界外球次数"),
    off_side_count("off_side_count", "越位次数", "当某个球队有越位犯规时手工更新越位次数", "拒单事件-越位次数"),
    corner_kick_count("corner_kick_count", "角球次数", "当某个球队产生角球时手工更新角球数", "拒单事件-角球次数"),
    shot_on_target_count("shot_on_target_count", "射正次数", "当某个球队射正时手工更新射正数", "拒单事件-射正次数"),
    shot_off_target_count("shot_off_target_count", "射偏次数", "当某个球队射偏时手工更新射偏数", "拒单事件-射偏次数"),
    goal_keeper_save_count("goal_keeper_save_count", "守门员扑救数", "当某个球队守门员防守成功时手工更新守门员防守成功数", "拒单事件-守门员扑救数"),
    free_kick("free_kick", "任意球", "当某个球队产生任意球时产生该事件", "拒单事件-当某个球队产生任意球时产生该事件"),
    goal_kick("goal_kick", "球门球", "当某个球队产生球门球时使用", "拒单事件-当某个球队产生球门球时使用"),
    throw_in("throw_in", "掷界外球", "当某个球队掷界外球时", "拒单事件-当某个球队掷界外球时"),
    offside("offside", "越位", "当某个球队产生越位犯规时", "拒单事件-当某个球队产生越位犯规时"),
    corner("corner", "危险(角球)", "当确认产生角球时", "拒单事件-角球(角球玩法集)"),
    shot_on_target("shot_on_target", "射正", "当某个球队射中球门时产生", "拒单事件-当某个球队射中球门时产生"),
    shot_off_target("shot_off_target", "射偏", "当某个球队射门射偏时产生", "拒单事件-当某个球队射门射偏时产生"),
    goal_keeper_save("goal_keeper_save", "守门员扑救", "当守门员防守成功时产生", "拒单事件-当守门员防守成功时产生"),
    injury("injury", "受伤", "当比赛因为球员受伤而中断时使用；需要球队示意", "拒单事件-当比赛因为球员受伤而中断时使用；需要球队示意"),
    penalty_awarded("penalty_awarded", "危险(获得点球)", "", "拒单事件-获得点球"),
    weather_conditions("weather_conditions", "天气状况", "", "拒单事件-天气状况"),
    attendance("attendance", "出席", "", "拒单事件-出席"),
    player_back_from_injury("player_back_from_injury", "受伤后回归", "比赛的天气情况。当比赛中的天气情况需要发生变化时产生该事件", "拒单事件-受伤后回归"),
    shots_blocked_counts("shots_blocked_counts", "被挡出射门次数", "官方给出的比赛出席详情", "拒单事件-被挡出射门次数"),
    shot_blocked("shot_blocked", "被挡出射门", "当比赛因球员受伤暂停后重新开始比赛时使用", "拒单事件-被挡出射门"),
    penalty_missed("penalty_missed", "点球未进", "当某个球队射门被阻止时手工更新阻止射门次数", "拒单事件-点球未进"),
    penalty_shootout_event("penalty_shootout_event", "点球大战", "当某个球队射门被阻止时产生", "拒单事件-点球大战"),
    betstart("betstart", "开盘", "当点球罚失时使用", "拒单事件-当点球罚失时使用"),
    betstop("betstop", "封盘", "此事件只有当比赛进入到点球大赛时才会更深入的报道", "拒单事件-此事件只有当比赛进入到点球大赛时才会更深入的报道"),
    kick_off_team("kick_off_team", "开球球队", "开盘表明比赛盘口可以开始投注。开盘不仅仅是赛前，而整个比赛过程中都可以操作开盘", "拒单事件-开盘表明比赛盘口可以开始投注。开盘不仅仅是赛前，而整个比赛过程中都可以操作开盘"),
    match_status("match_status", "比赛状态", "封盘表明所有盘口很有可能是因为一个进球、点球、红牌或者赛场上存在任何其他不确认因素而被关闭", "拒单事件-封盘表明所有盘口很有可能是因为一个进球、点球、红牌或者赛场上存在任何其他不确认因素而被关闭"),
    pitch_conditions("pitch_conditions", "场地状况", "在某场比赛中获得开球的球队", "拒单事件-在某场比赛中获得开球的球队"),
    free_comment("free_comment", "赛事评论", "当比赛状态发生改变时发送", "拒单事件-当比赛状态发生改变时发送"),
    possible_corner("possible_corner", "可能角球", "比赛场地的情况。当比赛过程中比赛场地状况发生改变时产生该事件", "拒单事件-比赛场地的情况。当比赛过程中比赛场地状况发生改变时产生该事件"),
    canceled_corner("canceled_corner", "取消角球", "用于比赛过程中产生的任何形式的附加信息", "拒单事件-用于比赛过程中产生的任何形式的附加信息"),
    possible_goal("possible_goal", "危险(可能进球)", "可能会产生角球", "拒单事件-可能进球"),
    canceled_goal("canceled_goal", "取消进球", "因未确认而被取消的角球", "拒单事件-因未确认而被取消的角球"),
    match_about_to_start("match_about_to_start", "即将开赛", "当进球得分或者极有可能进球得分时发送该事件", "拒单事件-当进球得分或者极有可能进球得分时发送该事件"),
    dangerous_attack("dangerous_attack", "危险进攻", "因未确认而被取消的进球", "拒单事件-因未确认而被取消的进球"),
    ball_safe("ball_safe", "己方半场控球", "控球方在自己半场", "拒单事件-控球方在自己半场"),
    manual_time_adjustment("manual_time_adjustment", "手工调时", "手动调整时间", "拒单事件-手动调整时间"),
    possible_red_card("possible_red_card", "可能红牌", "很有可能出现红牌时产生该事件，包括红牌和黄红牌", "拒单事件-很有可能出现红牌时产生该事件，包括红牌和黄红牌"),
    canceled_red_card("canceled_red_card", "取消红牌", "当可能红牌未被确认而取消时产生该事件，包括红牌和黄红牌", "拒单事件-当可能红牌未被确认而取消时产生该事件，包括红牌和黄红牌"),
    possible_penalty("possible_penalty", "可能点球", "当很有可能出现点球时产生该事件", "拒单事件-当很有可能出现点球时产生该事件"),
    canceled_penalty("canceled_penalty", "点球取消", "当可能点球未确认而被取消时产生该事件", "拒单事件-当可能点球未确认而被取消时产生该事件"),
    deleted_event_alert("deleted_event_alert", "删除事件", "手动删除事件", "拒单事件-手动删除事件"),
    play_resumes_after_goal("play_resumes_after_goal", "进球后继续比赛", "用于确认进球得分后的开球球员", "拒单事件-用于确认进球得分后的开球球员"),
    disable_corner_markets("disable_corner_markets", "角球盘口封盘", "当可能产生角球时触发", "拒单事件-当可能产生角球时触发"),
    disable_booking_markets("disable_booking_markets", "罚牌盘口封盘", "当可能产生罚牌时触发", "拒单事件-当可能产生罚牌时触发"),
    possible_yellow_card("possible_yellow_card", "可能出黄牌", "很有可能产生黄牌时使用该事件", "拒单事件-很有可能产生黄牌时使用该事件"),
    canceled_yellow_card("canceled_yellow_card", "取消黄牌", "可能产生的黄牌因未确认而取消时产生该事件", "拒单事件-可能产生的黄牌因未确认而取消时产生该事件"),
    possible_free_kick("possible_free_kick", "可能任意球", "很有可能产生任意球时使用该事件", "拒单事件-很有可能产生任意球时使用该事件"),
    early_betstatus("early_betstatus", "赛前盘投注状态", "通常有betstart和betstop两种状态，在下一次开球前赛前盘就已经开始，赛前盘需要在进球确认", "拒单事件-通常有betstart和betstop两种状态，在下一次开球前赛前盘就已经开始，赛前盘需要在进球确认"),
    coverage_status("coverage_status", "报道状态", "当比赛的报道状态发生改变时发送", "拒单事件-当比赛的报道状态发生改变时发送"),
    penalty_shootout_starting_team("penalty_shootout_starting_team", "点球大赛开始球队", "点球大赛首先踢球的球队", "拒单事件-点球大赛首先踢球的球队"),
    attack("attack", "进攻", "球队带球进入对方半场至进行危险区域之前的进攻", "拒单事件-球队带球进入对方半场至进行危险区域之前的进攻"),
    take_penalty("take_penalty", "危险(开始点球)", "当球员将要踢出点球的时候产生该事件", "拒单事件-开始点球"),
    video_assistant_referee("video_assistant_referee", "视频辅助裁判", "当确定需要视频辅助裁判时使用该事件。一般用于通过回放为进球、点球和红牌提供额外信息", "拒单事件-当确定需要视频辅助裁判时使用该事件。一般用于通过回放为进球、点球和红牌提供额外信息"),
    video_assistant_referee_over("video_assistant_referee_over", "视频辅助裁判结束", "当视频裁判给出维持原判或者推翻判定的决定时产生该事件", "拒单事件-当视频裁判给出维持原判或者推翻判定的决定时产生该事件"),
    possible_video_assistant_referee("possible_video_assistant_referee", "可能需要视频辅助裁判", "当很可能需要使用视频回放系统辅助裁判的时候使用该事件", "拒单事件-当很可能需要使用视频回放系统辅助裁判的时候使用该事件"),
    canceled_video_assistant_referee("canceled_video_assistant_referee", "视频辅助裁判取消", "当视频辅助裁判因未确认而取消时产生该事件", "拒单事件-当视频辅助裁判因未确认而取消时产生该事件"),
    canceled_free_kick("canceled_free_kick", "取消可能的任意球", "可能产生的任意球因未确认而取消时产生该事件", "拒单事件-可能产生的任意球因未确认而取消时产生该事件"),
    match_period_update("match_period_update", "比赛阶段更新", "比赛阶段更新", "拒单事件-比赛阶段更新"),

    //后置检查失败
    match_status_exception("match_status_exception", "赛事状态异常", "赛事状态异常", "【后置检查】赛事状态异常(数据商)"),
    match_status_exceptionPA("match_status_exception-PA", "赛事状态异常", "赛事状态异常", "【后置检查】赛事状态异常(数据商)"),
    match_status_exceptionDS("match_status_exception-DS", "赛事状态异常", "赛事状态异常", "【后置检查】赛事状态异常(数据商)"),

    match_status_closed("match_status_closed", "比赛结束", "比赛结束", "【后置检查】比赛结束(数据商)"),
    match_status_closedPA("match_status_closed-PA", "比赛结束", "比赛结束", "【后置检查】比赛结束(数据商)"),
    match_status_closedDS("match_status_closed-DS", "比赛结束", "比赛结束", "【后置检查】比赛结束(数据商)"),

    match_over_status_exception("match_over_status_exception", "赛事状态异常", "赛事状态异常", "【后置检查】比赛结束状态异常(PA)"),
    match_over_status_exceptionPA("match_over_status_exception-PA", "赛事状态异常", "赛事状态异常", "【后置检查】比赛结束状态异常(PA)"),
    match_over_status_exceptionDS("match_over_status_exception-DS", "赛事状态异常", "赛事状态异常", "【后置检查】比赛结束状态异常(PA)"),

    match_over_status_no_bet("match_over_status_no_bet", "比赛结束状态不可投注", "比赛结束状态不可投注", "【后置检查】比赛结束状态不可投注(PA)"),
    match_over_status_no_betPA("match_over_status_no_bet-PA", "比赛结束状态不可投注", "比赛结束状态不可投注", "【后置检查】比赛结束状态不可投注(PA)"),
    match_over_status_no_betDS("match_over_status_no_bet-DS", "比赛结束状态不可投注", "比赛结束状态不可投注", "【后置检查】比赛结束状态不可投注(PA)"),

    match_over_status_over("match_over_status_over", "比赛结束", "比赛结束", "【后置检查】比赛结束(PA)"),
    match_over_status_overPA("match_over_status_over-PA", "比赛结束", "比赛结束", "【后置检查】比赛结束(PA)"),
    match_over_status_overDS("match_over_status_over-DS", "比赛结束", "比赛结束", "【后置检查】比赛结束(PA)"),

    match_handicap_status_exception("match_handicap_status_exception", "赛事异常", "赛事状态异常", "【后置检查】赛事操盘状态异常(PA)"),
    match_handicap_status_exceptionPA("match_handicap_status_exception-PA", "赛事异常", "赛事状态异常", "【后置检查】赛事操盘状态异常(PA)"),
    match_handicap_status_exceptionDS("match_handicap_status_exception-DS", "赛事异常", "赛事状态异常", "【后置检查】赛事操盘状态异常(PA)"),

    match_handicap_status_suspended("match_handicap_status_suspended", "赛事封盘", "赛事封盘", "【后置检查】赛事封盘(PA)"),
    match_handicap_status_suspendedPA("match_handicap_status_suspended-PA", "赛事封盘", "赛事封盘", "【后置检查】赛事封盘(PA)"),
    match_handicap_status_suspendedDS("match_handicap_status_suspended-DS", "赛事封盘", "赛事封盘", "【后置检查】赛事封盘(PA)"),

    match_handicap_status_deactivated("match_handicap_status_deactivated", "赛事关盘", "赛事关盘", "【后置检查】赛事关盘(PA)"),
    match_handicap_status_deactivatedPA("match_handicap_status_deactivated-PA", "赛事关盘", "赛事关盘", "【后置检查】赛事关盘(PA)"),
    match_handicap_status_deactivatedDS("match_handicap_status_deactivated-DS", "赛事关盘", "赛事关盘", "【后置检查】赛事关盘(PA)"),

    match_handicap_status_lock("match_handicap_status_lock", "赛事锁盘", "赛事锁盘", "【后置检查】赛事锁盘(PA)"),
    match_handicap_status_lockDS("match_handicap_status_lock-DS", "赛事锁盘", "赛事锁盘", "【后置检查】赛事锁盘(PA)"),
    match_handicap_status_lockPA("match_handicap_status_lock-PA", "赛事锁盘", "赛事锁盘", "【后置检查】赛事锁盘(PA)"),

    //后置检查新增
    order_odds_change("order_odds_change", "赔率变更", "赔率变更", "用户选择的为不接受赔率变动，赔率已变动"),
    order_odds_change_low("order_odds_change_low", "赔率变低", "赔率变低", "用户选择的为接受更好的赔率变动，赔率变低"),
    merchant_handle_fail("merchant_handle_fail", "商户扣款失败", "商户扣款失败", "接口调用超时或异常"),

    //盘口状态
    market_status_exception("market_status_exception", "盘口状态异常", "盘口状态异常", "【后置检查】盘口状态异常"),
    market_status_exceptionDS("market_status_exception-DS", "盘口状态异常", "盘口状态异常", "【后置检查】盘口状态异常(数据商)"),
    market_status_exceptionPA("market_status_exception-PA", "盘口状态异常", "盘口状态异常", "【后置检查】盘口状态异常(PA)"),

    market_status_no_active("market_status_no_active", "盘口非激活", "盘口非激活", "【后置检查】盘口非激活"),
    market_status_no_activeDS("market_status_no_active-DS", "盘口非激活", "盘口非激活", "【后置检查】盘口非激活(数据商)"),
    market_status_no_activePA("market_status_no_active-PA", "盘口非激活", "盘口非激活", "【后置检查】盘口非激活(PA)"),

    market_status_suspended("market_status_suspended", "盘口封盘", "盘口封盘", "【后置检查】盘口封盘"),
    market_status_suspendedDS("market_status_suspended-DS", "盘口封盘", "盘口封盘", "【后置检查】盘口封盘(数据商)"),
    market_status_suspendedPA("market_status_suspended-PA", "盘口封盘", "盘口封盘", "【后置检查】盘口封盘(PA)"),

    market_status_deactivated("market_status_deactivated", "盘口关盘", "盘口关盘", "【后置检查】盘口关盘(数据商)"),
    market_status_deactivatedDS("market_status_deactivated-DS", "盘口关盘", "盘口关盘", "【后置检查】盘口关盘(数据商)"),
    market_status_deactivatedPA("market_status_deactivated-PA", "盘口关盘", "盘口关盘", "【后置检查】盘口关盘(PA)"),

    market_status_settled("market_status_settled", "盘口已结算", "盘口已结算", "【后置检查】盘口已结算"),

    market_status_cancelled("market_status_cancelled", "盘口取消", "盘口取消", "【后置检查】盘口取消(数据商)"),
    market_status_cancelledDS("market_status_cancelled-DS", "盘口取消", "盘口取消", "【后置检查】盘口取消(数据商)"),
    market_status_cancelledPA("market_status_cancelled-PA", "盘口取消", "盘口取消", "【后置检查】盘口取消(数据商)"),

    market_status_handed_over("market_status_handed_over", "盘口移交", "盘口移交", "【后置检查】盘口移交(数据商)"),
    market_status_handed_overDS("market_status_handed_over-DS", "盘口移交", "盘口移交", "【后置检查】盘口移交(数据商)"),
    market_status_handed_overPA("market_status_handed_over-PA", "盘口移交", "盘口移交", "【后置检查】盘口移交(数据商)"),

    market_status_locked("market_status_locked", "盘口锁盘", "盘口锁盘", "【后置检查】盘口锁盘"),
    market_status_lockedDS("market_status_locked-DS", "盘口锁盘", "盘口锁盘", "【后置检查】盘口锁盘(数据商)"),
    market_status_lockedPA("market_status_locked-PA", "盘口锁盘", "盘口锁盘", "【后置检查】盘口锁盘(PA)"),

    bus_refuse_120s("bus_refuse_120s", "业务超时(120s)", "业务超时(120s)", "足球PA滚球"),
    bus_refuse_18s("bus_refuse_18s", "业务超时(18s)", "业务超时(18s)", "MTS赛事&篮球PA"),   //已弃用，改为16s
    bus_refuse_16s("bus_refuse_16s", "业务超时(16s)", "业务超时(16s)", "MTS赛事&篮球PA"),

    bet_order_place_num_change("bet_order_place_num_change", "盘口值已变更", "坑位已发生改变，投注失败", "对应坑位的盘口值已变更"),

    bet_order_place_num_invalid("bet_order_place_num_invalid", "坑位已失效", "坑位已失效", "对应坑位的已失效"),

    bet_order_place_num_changeDS("bet_order_place_num_change-DS", "盘口值已变更", "坑位已发生改变，投注失败", "对应坑位的盘口值已变更"),

    bet_order_place_num_changePA("bet_order_place_num_change-PA", "盘口值已变更", "坑位已发生改变，投注失败", "对应坑位的盘口值已变更"),

    match_status_ended("match_status_ended", "比赛已结束", "比赛结束", "【后置检查】比赛已结束(PA)"),

    //风控争对投注新流程 currentEvent 特殊值处理
    market_expired("market_expired", "盘口变更", "投注项无效", "投注项无效"),
    over_payout("over_payout", "超出限额", "超出限额", "超出限额"),
    odds_hight("odds_hight", "投注项赔率太高", "投注项赔率太高", "投注项赔率太高"),
    bus_refuse_305s("bus_refuse_305s", "业务超时（305s)", "业务超时（305s)", "业务超时（305s)"),
    ;


    private String code;
    private String name;
    private String desc;
    private String remark;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    DangerBallEnum(String code, String name, String desc, String remark) {
        this.code = code;
        this.name = name;
        this.desc = desc;
        this.remark = remark;
    }

    private static Map<String, DangerBallEnum> dangerBallEnumMap = new HashMap();

    /**
     * 根据状态码获取对象
     *
     * @param code
     * @return
     */
    public static DangerBallEnum getRiskEventEnum(String code) {
        if (dangerBallEnumMap != null && dangerBallEnumMap.size() > 0) {
            DangerBallEnum result = dangerBallEnumMap.get(code);
            if (result == null) {
                for (DangerBallEnum obj : values()) {
                    dangerBallEnumMap.put(obj.getCode(), obj);
                }
            }
        } else {
            for (DangerBallEnum obj : values()) {
                dangerBallEnumMap.put(obj.getCode(), obj);
            }
        }
        return dangerBallEnumMap.get(code);
    }
}
