ALTER TABLE `sys_app` DROP COLUMN `tenant_id`;

ALTER TABLE `sys_group` DROP COLUMN `tenant_id`;

ALTER TABLE `sys_menu` DROP COLUMN `tenant_id`;

ALTER TABLE `sys_menu_options` DROP COLUMN `tenant_id`;

ALTER TABLE `sys_table_width` DROP COLUMN `tenant_id`;

ALTER TABLE `sys_user_org` DROP COLUMN `tenant_id`;

ALTER TABLE `sys_user_role` DROP COLUMN `tenant_id`;

ALTER TABLE `sys_role_menu_options` DROP COLUMN `tenant_id`;

ALTER TABLE `sys_hr_org_approver` DROP COLUMN `tenant_id`;

ALTER TABLE `sys_portal_user_app` DROP COLUMN `tenant_id`;

-- 增加默认租户数据，默认租户id是1
update sys_user set tenant_id = 1 where tenant_id is null;

update sys_role set tenant_id = 1 where tenant_id is null;

update sys_hr_position set tenant_id = 1 where tenant_id is null;

update sys_hr_organization set tenant_id = 1 where tenant_id is null;