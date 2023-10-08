alter table  t_pre_order add column `initial_margin`  int(10)     DEFAULT NULL COMMENT '抽水值' AFTER original_remaining_bet_amount ;
alter table  t_pre_order add column `final_margin`  int(10)    DEFAULT NULL COMMENT '最终抽水值' AFTER original_remaining_bet_amount ;

alter table  t_pre_order add column `probabilities`  decimal(20,18)   DEFAULT NULL COMMENT '概率' AFTER original_remaining_bet_amount ;
alter table   t_pre_order add column `cash_out_margin`  decimal(20,18)   DEFAULT NULL COMMENT '抽水值' AFTER original_remaining_bet_amount;

alter table  t_pre_order  add column `draw_calc_prob`  decimal(20,18)   DEFAULT NULL COMMENT '和局计算概率' AFTER original_remaining_bet_amount;
alter table  t_pre_order  add column `full_draw_calc_prob`  decimal(20,18)   DEFAULT NULL COMMENT '1/4盘 对应整求盘 概率' AFTER original_remaining_bet_amount;

alter table  t_pre_order  add column `calc_probability`  decimal(20,18)   DEFAULT NULL COMMENT '无和局概率盘口重新计算后的概率' AFTER original_remaining_bet_amount;

alter table  t_pre_order  add column `half_prob`  decimal(20,18)   DEFAULT NULL COMMENT '   1/4盘对应半球盘 概率' AFTER original_remaining_bet_amount;
alter table  t_pre_order  add column `full_calc_prob`  decimal(20,18)   DEFAULT NULL COMMENT '1/4盘对应整球盘 概率' AFTER original_remaining_bet_amount;