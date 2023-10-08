update tybss_new.t_merchant
set open_esport=0
where merchant_code not in('oubao');



DELETE FROM tybss_new.`s_sport` WHERE `s_sport`.`id` IN (100,101,102,103,104);