ALTER TABLE `sys_role`
DROP COLUMN `role_system_flag`,
ADD COLUMN `role_type` tinyint NOT NULL DEFAULT 10 COMMENT '角色类型：10-系统角色，15-业务角色，20-公司角色' AFTER `remark`,
ADD COLUMN `role_company_id` bigint NULL COMMENT '角色所属公司id，当角色类型为20时传此值' AFTER `role_type`;

ALTER TABLE `sys_user_role`
ADD COLUMN `role_type` tinyint NOT NULL DEFAULT 10 COMMENT '角色类型：10-系统角色，15-业务角色，20-公司角色' AFTER `role_id`,
ADD COLUMN `role_org_id` bigint NULL DEFAULT NULL COMMENT '用户所属机构id' AFTER `role_type`;