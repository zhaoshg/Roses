ALTER TABLE `sys_user` ADD COLUMN `employee_number` varchar(100) NULL COMMENT '工号' AFTER `user_sort`;

CREATE TABLE `sys_user_certificate`  (
  `user_certificate_id` bigint NOT NULL COMMENT '主键',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `certificate_type` bigint NOT NULL COMMENT '证书类型，取字典id',
  `certificate_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '证书编号',
  `issuing_authority` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发证机构名称',
  `date_issued` date NULL DEFAULT NULL COMMENT '证书发证日期',
  `date_expires` date NULL DEFAULT NULL COMMENT '证书到期日期，如果为空，则为长期',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user` bigint NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `update_user` bigint NULL DEFAULT NULL COMMENT '修改人',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '是否删除：Y-删除，N-未删除',
  PRIMARY KEY (`user_certificate_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户证书' ROW_FORMAT = Dynamic;