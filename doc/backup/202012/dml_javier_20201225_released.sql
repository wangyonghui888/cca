#清除之前创建角色时生成[电子账单]及[渠道商户管理]菜单表及权限表脏数据
DELETE from merchant.admin_roles_menus where role_id in (select role_id from admin_user where agent_level=2) and menu_id in (select id from admin_menu where name in ('电子账单','渠道商户管理'));

DELETE from merchant.admin_roles_permissions where role_id in (select role_id from admin_user where agent_level=2) and permission_id in (select id from admin_permission where alias in ('电子账单','渠道商户管理'));