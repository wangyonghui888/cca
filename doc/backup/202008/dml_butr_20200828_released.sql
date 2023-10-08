update tybss_new.t_order o
set o.user_level = (select t.level_id
                    from tybss_new.t_user_level_relation_history t
                    where t.uid = o.uid
                      and t.modify_time > o.create_time
                    order by t.create_time asc
                    LIMIT 1)
where o.create_time >= 1592409600000;

update tybss_new.t_order o
set o.user_level = (select t.level_id from tybss_new.t_user_level_relation t where t.uid = o.uid)
where o.create_time > (select t.create_time from tybss_new.t_user_level_relation t where t.uid = o.uid)
  and o.user_level = 0
  and o.create_time >= 1592409600000;


update tybss_new.t_order o
set o.user_level = 1
where o.create_time < (select t.create_time from tybss_new.t_user_level_relation t where t.uid = o.uid)
  and o.user_level = 0
  and o.create_time >= 1592409600000;


update tybss_new.t_order o
set o.user_level = 1
where o.uid not in (select t.uid from tybss_new.t_user_level_relation t)
  and o.user_level = 0
  and o.create_time >= 1592409600000;

