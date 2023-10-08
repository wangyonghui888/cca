## 二级商户不能有二级商户管理菜单,上隔离和生产
DELETE FROM `merchant`.`admin_roles_menus` WHERE menu_id=4 AND role_id=5;