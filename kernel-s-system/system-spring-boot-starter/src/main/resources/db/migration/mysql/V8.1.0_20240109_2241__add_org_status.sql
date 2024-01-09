-- 用户机构表，增加状态，代表是否启用
ALTER TABLE `sys_user_org`
ADD COLUMN `status_flag` tinyint NOT NULL DEFAULT 1 COMMENT '是否启用：1-启用，2-禁用' AFTER `main_flag`;