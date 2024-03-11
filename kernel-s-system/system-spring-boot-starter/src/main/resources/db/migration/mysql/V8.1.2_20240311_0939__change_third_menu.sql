-- 删除部分三级菜单
DELETE FROM sys_menu WHERE menu_id IN (
		'1673525798674657282',
		'1673526946869571585',
		'1673527098267168769',
		'1673527401095917570',
		'1684222436082364417',
		'1673525723072327682',
		'1673526227621933057',
	'1673526479934484481');

-- 新增三级菜单的功能权限
INSERT INTO `sys_menu_options`(`menu_option_id`, `app_id`, `menu_id`, `option_name`, `option_code`, `create_time`, `create_user`, `update_time`, `update_user`) VALUES (1766013401104175106, 1671406745336016898, 1673525659931275265, '业务日志', 'BUSINESS_LOG', '2024-03-08 16:09:40', 1339550467939639299, NULL, NULL);
INSERT INTO `sys_menu_options`(`menu_option_id`, `app_id`, `menu_id`, `option_name`, `option_code`, `create_time`, `create_user`, `update_time`, `update_user`) VALUES (1766013493466943489, 1671406745336016898, 1673525659931275265, '登录日志', 'LOG_LOGIN', '2024-03-08 16:10:02', 1339550467939639299, '2024-03-08 16:41:49', 1339550467939639299);
INSERT INTO `sys_menu_options`(`menu_option_id`, `app_id`, `menu_id`, `option_name`, `option_code`, `create_time`, `create_user`, `update_time`, `update_user`) VALUES (1766013586857316353, 1671406745336016898, 1673525659931275265, 'API日志', 'OPERATE_LOG', '2024-03-08 16:10:25', 1339550467939639299, '2024-03-08 16:41:24', 1339550467939639299);
INSERT INTO `sys_menu_options`(`menu_option_id`, `app_id`, `menu_id`, `option_name`, `option_code`, `create_time`, `create_user`, `update_time`, `update_user`) VALUES (1766025381470851073, 1671406745336016898, 1673525912344489985, 'SQL监控', 'SQL_MONITOR', '2024-03-08 16:57:17', 1339550467939639299, NULL, NULL);
INSERT INTO `sys_menu_options`(`menu_option_id`, `app_id`, `menu_id`, `option_name`, `option_code`, `create_time`, `create_user`, `update_time`, `update_user`) VALUES (1766025455466762241, 1671406745336016898, 1673525912344489985, '服务器信息', 'SERVER_MONITOR', '2024-03-08 16:57:34', 1339550467939639299, NULL, NULL);
INSERT INTO `sys_menu_options`(`menu_option_id`, `app_id`, `menu_id`, `option_name`, `option_code`, `create_time`, `create_user`, `update_time`, `update_user`) VALUES (1766027897772232705, 1671406745336016898, 1673526656565014530, '主题管理', 'THEME_MANAGER', '2024-03-08 17:07:17', 1339550467939639299, NULL, NULL);
INSERT INTO `sys_menu_options`(`menu_option_id`, `app_id`, `menu_id`, `option_name`, `option_code`, `create_time`, `create_user`, `update_time`, `update_user`) VALUES (1766027932140359682, 1671406745336016898, 1673526656565014530, '主题模板', 'THEME_TEMPLATE', '2024-03-08 17:07:25', 1339550467939639299, NULL, NULL);
INSERT INTO `sys_menu_options`(`menu_option_id`, `app_id`, `menu_id`, `option_name`, `option_code`, `create_time`, `create_user`, `update_time`, `update_user`) VALUES (1766027969373196290, 1671406745336016898, 1673526656565014530, '主题属性', 'THEME_ATTR', '2024-03-08 17:07:34', 1339550467939639299, NULL, NULL);
