update tybss_new.t_merchant
set parent_id=null,
    agent_level=0
where merchant_code in ('526016', '442254', '976523', '812774');


update merchant.admin_user
set parent_merchant_code=null,
    agent_level=0
where merchant_code in ('526016', '442254', '976523', '812774');

