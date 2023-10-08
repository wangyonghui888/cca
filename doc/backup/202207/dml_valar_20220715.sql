delete from tybss_merchant_common.t_account where uid in(SELECT uid  FROM `t_user` WHERE merchant_code='oubao' and length(username)>15);

delete from tybss_merchant_common.t_user WHERE merchant_code='oubao' and length(username)>15;