/*update tybss_new.t_user
set modify_time =modify_time / 100
where modify_time >= 2001979340000;
*/

UPDATE tybss_new.t_user tu inner join (SELECT uid, max(login_time) login
                                       from tybss_new.t_log_history
                                       GROUP by uid) uo on tu.uid = uo.uid
set tu.modify_time=uo.login;


update tybss_new.t_order_detail
set match_name='瑞士甲级联赛',
    match_info='阿劳 v 纳沙泰尔'
WHERE `match_id` = 300517;