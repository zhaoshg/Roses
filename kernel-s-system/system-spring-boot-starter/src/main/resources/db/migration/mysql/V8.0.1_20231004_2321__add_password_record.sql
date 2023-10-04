CREATE TABLE `sys_user_password_record`  (
  `record_id` bigint(0) NOT NULL COMMENT '主键',
  `user_id` bigint(0) NOT NULL COMMENT '用户id',
  `history_password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '历史密码记录',
  `history_password_salt` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '历史密码记录盐值',
  `update_password_time` datetime(0) NOT NULL COMMENT '修改密码时间',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user` bigint(0) NULL DEFAULT NULL COMMENT '创建人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `update_user` bigint(0) NULL DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`record_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户历史密码记录' ROW_FORMAT = Dynamic;