DELETE
FROM merchant.admin_roles_menus a
where a.menu_id in (27, 28);

INSERT INTO `merchant`.`admin_roles_menus`(`role_id`, `menu_id`)
SELECT id, 27
FROM merchant.admin_role;
INSERT INTO `merchant`.`admin_roles_menus`(`role_id`, `menu_id`)
SELECT id, 28
FROM merchant.admin_role;
COMMIT;
