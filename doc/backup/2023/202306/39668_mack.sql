-- 水军投注延时&赔率分组
update t_user set special_betting_limit_delay_time = 6, modify_time = UNIX_TIMESTAMP(now()) * 1000, modify_user = 'mack' where user_level = 9;

update t_user set market_level = 15, modify_time = UNIX_TIMESTAMP(now()) * 1000, modify_user = 'mack'  where user_level = 9 and merchant_code != '976523';

-- 资讯客投注延时&赔率分组
update t_user set special_betting_limit_delay_time = 6, modify_time = UNIX_TIMESTAMP(now()) * 1000, modify_user = 'mack' where user_level = 10;

select sleep(10);

update t_user set market_level = 15, modify_time = UNIX_TIMESTAMP(now()) * 1000, modify_user = 'mack' where user_level = 10 and merchant_code != '976523';


-- 进球点投注投注延时&赔率分组
update t_user set special_betting_limit_delay_time = 10, modify_time = UNIX_TIMESTAMP(now()) * 1000, modify_user = 'mack' where user_level = 144;


update t_user set market_level = null, modify_time = UNIX_TIMESTAMP(now()) * 1000, modify_user = 'mack' where user_level = 144;

select sleep(10);


-- 系统投注投注延时&赔率分组
update t_user set special_betting_limit_delay_time = 6, modify_time = UNIX_TIMESTAMP(now()) * 1000, modify_user = 'mack' where user_level = 19;


update t_user set market_level = 15, modify_time = UNIX_TIMESTAMP(now()) * 1000, modify_user = 'mack' where user_level = 19 and merchant_code != '976523';


-- 价值投注投注延时&赔率分组
update t_user set special_betting_limit_delay_time = null, modify_time = UNIX_TIMESTAMP(now()) * 1000, modify_user = 'mack' where user_level = 231;

select sleep(10);

update t_user set market_level = 13, modify_time = UNIX_TIMESTAMP(now()) * 1000, modify_user = 'mack' where user_level = 231 and merchant_code != '976523';

-- Y0-水军、Y0-资讯客、Y0-进球点投注、Y0-系统投注、Y0-价值投注标签ID修改
update t_user set user_level = 9, modify_time = UNIX_TIMESTAMP(now()) * 1000, modify_user = 'mack' where user_level = 245;
update t_user set user_level = 10, modify_time = UNIX_TIMESTAMP(now()) * 1000, modify_user = 'mack' where user_level = 250;
select sleep(10);

update t_user set user_level = 144, modify_time = UNIX_TIMESTAMP(now()) * 1000, modify_user = 'mack' where user_level = 246;
update t_user set user_level = 19, modify_time = UNIX_TIMESTAMP(now()) * 1000, modify_user = 'mack' where user_level = 248;
update t_user set user_level = 231, modify_time = UNIX_TIMESTAMP(now()) * 1000, modify_user = 'mack' where user_level = 249;