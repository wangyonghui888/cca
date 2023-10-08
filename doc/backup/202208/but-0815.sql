###业务公共库
alter table tybss_merchant_common.t_user_level add tag_market_level_id int(11) COMMENT '赔率分组';
alter table tybss_merchant_y.t_user_level add tag_market_level_id int(11) COMMENT '赔率分组';
alter table tybss_merchant_s.t_user_level add tag_market_level_id int(11) COMMENT '赔率分组';
alter table tybss_merchant_b.t_user_level add tag_market_level_id int(11) COMMENT '赔率分组';
###业务汇总库
alter table tybss_merchant_common.t_user_level add tag_market_level_id int(11) COMMENT '赔率分组';


###业务公共库
update tybss_merchant_common.t_user set user_level =208 where user_level in (200,201)  and last_bet_time >=1638288000000;
update tybss_merchant_y.t_user set user_level =208 where user_level in (200,201)  and last_bet_time >=1638288000000;
update tybss_merchant_s.t_user set user_level =208 where user_level in (200,201)  and last_bet_time >=1638288000000;
update tybss_merchant_b.t_user set user_level =208 where user_level in (200,201)  and last_bet_time >=1638288000000;

###业务汇总库
update tybss_merchant_common.t_user set user_level =208 where user_level in (200,201)  and last_bet_time >=1638288000000;


###业务公共库
update tybss_merchant_common.t_user_level_relation set user_level =208 where uid in (select uid from t_user where user_level =208)  ;
update tybss_merchant_y.t_user_level_relation set user_level =208 where user_level in (select uid from t_user where user_level =208)   ;
update tybss_merchant_s.t_user_level_relation set user_level =208 where user_level in (select uid from t_user where user_level =208)  ;
update tybss_merchant_b.t_user_level_relation set user_level =208 where user_level in (select uid from t_user where user_level =208)  ;

###业务汇总库
update tybss_merchant_common.t_user_level_relation set user_level =208 where user_level in (select uid from t_user where user_level =208)  ;


