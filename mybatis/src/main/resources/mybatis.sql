/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.3.31-write
 Source Server Type    : MySQL
 Source Server Version : 50721
 Source Host           : 192.168.3.31:3306
 Source Schema         : mybatis

 Target Server Type    : MySQL
 Target Server Version : 50721
 File Encoding         : 65001

 Date: 30/04/2020 10:44:26
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for attendance_record
-- ----------------------------
DROP TABLE IF EXISTS `attendance_record`;
CREATE TABLE `attendance_record`  (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `employee_id` int(9) NOT NULL,
  `clock_time` datetime(0) NOT NULL COMMENT '打卡时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '考勤记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for attendance_summary
-- ----------------------------
DROP TABLE IF EXISTS `attendance_summary`;
CREATE TABLE `attendance_summary`  (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `attendance_day` int(2) NOT NULL COMMENT '应出勤天数',
  `actual_attendance_day` float(2, 1) NOT NULL COMMENT '实际出勤天数',
  `personal_leave_day` float(2, 1) NULL DEFAULT NULL COMMENT '事假天数',
  `sick_leave_day` float(2, 1) NULL DEFAULT NULL COMMENT '病假天数',
  `lieu_leave_day` float(2, 1) NULL DEFAULT NULL COMMENT '补休天数',
  `funeral_leave_day` float(2, 1) NULL DEFAULT NULL COMMENT '丧假天数',
  `annual_leave_day` float(2, 1) NULL DEFAULT NULL COMMENT '年假天数',
  `marriage_leave_day` float(2, 1) NULL DEFAULT NULL COMMENT '婚假天数',
  `absenteeism_day` float(2, 1) NULL DEFAULT NULL COMMENT '旷工天数',
  `go_out_day` float(2, 1) NULL DEFAULT NULL COMMENT '外出天数',
  `leave_early_minute` int(3) NULL DEFAULT NULL COMMENT '早退分钟',
  `late_number` int(2) NULL DEFAULT NULL COMMENT '迟到次数',
  `not_clock_number` int(2) NULL DEFAULT NULL COMMENT '未打卡次数',
  `tolerance_day` float(2, 1) NULL DEFAULT NULL COMMENT '公差天数',
  `paternity_leave_day` float(2, 1) NULL DEFAULT NULL COMMENT '陪产假天数',
  `maternity_leave_day` float(2, 1) NULL DEFAULT NULL COMMENT '产假天数',
  `overtime_day` float(2, 1) NULL DEFAULT NULL COMMENT '加班天数',
  `overtime_twenty_number` int(2) NULL DEFAULT NULL COMMENT '20点加班次数',
  `overtime_twenty_three_number` int(2) NULL DEFAULT NULL COMMENT '23点加班次数',
  `create_time` datetime(0) NULL,
  `update_time` datetime(0) NOT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `employee_id` int(9) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '考勤汇总表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for data_dictionary
-- ----------------------------
DROP TABLE IF EXISTS `data_dictionary`;
CREATE TABLE `data_dictionary`  (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `data_key` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `data_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `data_value` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL,
  `update_time` datetime(0) NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `data_key_data_value_unique`(`data_key`, `data_value`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '数据字典表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of data_dictionary
-- ----------------------------
INSERT INTO `data_dictionary` VALUES (1, 'EMPLOYEE_TYPE', '超级管理员', 'SUPER_ADMIN_TYPE', '2020-04-21 10:28:52', '2020-04-21 10:28:54');
INSERT INTO `data_dictionary` VALUES (2, 'EMPLOYEE_TYPE', '管理员', 'ADMIN_TYPE', '2020-04-21 11:04:56', '2020-04-21 11:04:59');
INSERT INTO `data_dictionary` VALUES (3, 'GENDER', '男', 'MAN', '2020-04-21 11:07:51', '2020-04-21 11:07:53');
INSERT INTO `data_dictionary` VALUES (4, 'GENDER', '女', 'WOMAN', '2020-04-21 11:08:13', '2020-04-21 11:08:15');
INSERT INTO `data_dictionary` VALUES (5, 'EMPLOYEE_STATUS', '正常', 'NORMAL_STATUS', '2020-04-23 08:53:33', '2020-04-23 08:53:35');
INSERT INTO `data_dictionary` VALUES (6, 'EMPLOYEE_STATUS', '锁定', 'LOCK_STATUS', '2020-04-23 08:54:13', '2020-04-23 08:54:15');
INSERT INTO `data_dictionary` VALUES (7, 'EMPLOYEE_STATUS', '失效', 'INVALID_STATUS', '2020-04-23 08:54:44', '2020-04-23 08:54:46');
INSERT INTO `data_dictionary` VALUES (8, 'EMPLOYEE_DYNAMIC', '在岗', 'ON_DUTY_DYNAMIC', '2020-04-23 08:56:05', '2020-04-23 08:56:07');
INSERT INTO `data_dictionary` VALUES (9, 'EMPLOYEE_DYNAMIC', '外出', 'EGRESS_DYNAMIC', '2020-04-23 08:56:47', '2020-04-23 08:56:49');
INSERT INTO `data_dictionary` VALUES (10, 'EMPLOYEE_DYNAMIC', '会议', 'MEETING_DYNAMIC', '2020-04-23 08:57:26', '2020-04-23 08:57:29');
INSERT INTO `data_dictionary` VALUES (11, 'EMPLOYEE_DYNAMIC', '休假', 'VACATION_DYNAMIC', '2020-04-23 08:58:10', '2020-04-23 08:58:12');

-- ----------------------------
-- Table structure for department
-- ----------------------------
DROP TABLE IF EXISTS `department`;
CREATE TABLE `department`  (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `department_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `principal_id` int(9) NOT NULL COMMENT '部门负责人',
  `set_up_time` datetime(0) NOT NULL COMMENT '设立时间',
  `update_time` datetime(0) NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '部门表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of department
-- ----------------------------
INSERT INTO `department` VALUES (1, '技术部', 1, '2020-04-23 14:34:18', '2020-04-23 14:34:20');

-- ----------------------------
-- Table structure for employee
-- ----------------------------
DROP TABLE IF EXISTS `employee`;
CREATE TABLE `employee`  (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `employee_name` varchar(4) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `phone_number` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `age` int(2) NOT NULL,
  `entry_time` datetime(0) NOT NULL COMMENT '入职时间',
  `resignation_time` datetime(0) NULL DEFAULT NULL COMMENT '离职时间',
  `gender` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '性别',
  `email_address` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `position_id` int(9) NOT NULL COMMENT '职位ID',
  `employee_status` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '员工状态',
  `profile_photo_link_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像链接地址',
  `create_time` datetime(0) NULL,
  `update_time` datetime(0) NULL,
  `id_card_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '身份证号码',
  `bank_card` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `remaining_annual_leave_day` float(2, 1) NOT NULL COMMENT '剩余年假天数',
  `remaining_lieu_leave_day` float(2, 1) NOT NULL COMMENT '剩余补休天数',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password_errors` int(2) NULL DEFAULT NULL,
  `employee_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `social_security_fund_ratio` decimal(5, 2) NULL DEFAULT NULL COMMENT '社保公积金比例',
  `employee_dynamic` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '员工动态',
  `locking_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '员工表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of employee
-- ----------------------------
INSERT INTO `employee` VALUES (3, 'qiu', '', 0, '2020-04-24 16:27:22', NULL, 'MAN', '', 0, 'NORMAL_STATUS', NULL, '2020-04-24 16:27:22', '2020-04-24 16:27:22', '', '', 0.0, 0.0, '$2a$10$7L2ld763DENLIerwrQOWbOO2FdbUBiDqsANbjzcG4DOyprHCaEFsa', 0, 'SUPER_ADMIN_TYPE', NULL, 'ON_DUTY_DYNAMIC', NULL);
INSERT INTO `employee` VALUES (4, '邱健', '17328375167', 25, '2020-04-24 16:51:30', NULL, 'MAN', '1186335831@qq.com', 2, 'NORMAL_STATUS', NULL, '2020-04-24 16:51:31', '2020-04-26 08:36:29', '421022199408084833', '621234567890', 0.0, 0.0, '$2a$10$V.RP1chKt8pmcNQqXRHa2OfnfFi4z7.7iJedFfL/K1tNmn2CY4aoG', 0, 'SUPER_ADMIN_TYPE', 0.10, 'ON_DUTY_DYNAMIC', NULL);
INSERT INTO `employee` VALUES (5, '张三', '17312341234', 28, '2020-04-24 17:09:26', NULL, 'MAN', '1@qq.com', 1, 'NORMAL_STATUS', NULL, '2020-04-24 17:09:28', '2020-04-26 08:36:32', '421102120156413213', '621245321584321545', 0.0, 0.0, '$2a$10$sB27l.RXf41f1es7L9Voiusjpg4eRH829XPT6LEZAebxaGW/m1/gi', 0, 'SUPER_ADMIN_TYPE', 0.05, 'ON_DUTY_DYNAMIC', NULL);

-- ----------------------------
-- Table structure for employee_role
-- ----------------------------
DROP TABLE IF EXISTS `employee_role`;
CREATE TABLE `employee_role`  (
  `employee_id` int(9) NOT NULL AUTO_INCREMENT,
  `role_id` int(9) NOT NULL,
  INDEX `idx_employee_id`(`employee_id`) USING BTREE,
  INDEX `idx_role_id`(`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for login_log
-- ----------------------------
DROP TABLE IF EXISTS `login_log`;
CREATE TABLE `login_log`  (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `remote_address` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '登录IP地址',
  `employee_id` int(9) NOT NULL,
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL,
  `login_status` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 52 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '登录日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of login_log
-- ----------------------------
INSERT INTO `login_log` VALUES (1, '0:0:0:0:0:0:0:1', 1, NULL, '2020-04-21 15:21:01', 'LOGIN_SUCCESS_STATUS');
INSERT INTO `login_log` VALUES (2, '0:0:0:0:0:0:0:1', 1, NULL, '2020-04-21 15:28:38', 'LOGIN_SUCCESS_STATUS');
INSERT INTO `login_log` VALUES (3, '0:0:0:0:0:0:0:1', 1, NULL, '2020-04-21 16:59:05', 'LOGIN_SUCCESS_STATUS');
INSERT INTO `login_log` VALUES (4, '0:0:0:0:0:0:0:1', 1, NULL, '2020-04-21 17:04:23', 'LOGIN_SUCCESS_STATUS');
INSERT INTO `login_log` VALUES (5, '0:0:0:0:0:0:0:1', 1, NULL, '2020-04-21 17:24:45', 'LOGIN_SUCCESS_STATUS');
INSERT INTO `login_log` VALUES (6, '0:0:0:0:0:0:0:1', 1, NULL, '2020-04-21 17:27:09', 'LOGIN_SUCCESS_STATUS');
INSERT INTO `login_log` VALUES (7, '0:0:0:0:0:0:0:1', 1, NULL, '2020-04-21 17:29:00', 'LOGIN_SUCCESS_STATUS');
INSERT INTO `login_log` VALUES (8, '0:0:0:0:0:0:0:1', 1, '用户名或密码错误', '2020-04-21 17:50:15', 'LOGIN_FAILURE_STATUS');
INSERT INTO `login_log` VALUES (9, '0:0:0:0:0:0:0:1', 1, '用户名或密码错误', '2020-04-21 17:51:10', 'LOGIN_FAILURE_STATUS');
INSERT INTO `login_log` VALUES (10, '0:0:0:0:0:0:0:1', 1, '用户名或密码错误', '2020-04-21 17:51:14', 'LOGIN_FAILURE_STATUS');
INSERT INTO `login_log` VALUES (11, '0:0:0:0:0:0:0:1', 1, '用户名或密码错误', '2020-04-21 17:51:19', 'LOGIN_FAILURE_STATUS');
INSERT INTO `login_log` VALUES (12, '0:0:0:0:0:0:0:1', 1, '账户已锁定，请60秒后再试', '2020-04-22 08:20:50', 'LOGIN_FAILURE_STATUS');
INSERT INTO `login_log` VALUES (13, '0:0:0:0:0:0:0:1', 1, '用户名或密码错误', '2020-04-22 08:22:49', 'LOGIN_FAILURE_STATUS');
INSERT INTO `login_log` VALUES (14, '0:0:0:0:0:0:0:1', 1, '用户名或密码错误', '2020-04-22 08:23:08', 'LOGIN_FAILURE_STATUS');
INSERT INTO `login_log` VALUES (15, '0:0:0:0:0:0:0:1', 1, '用户名或密码错误', '2020-04-22 08:23:10', 'LOGIN_FAILURE_STATUS');
INSERT INTO `login_log` VALUES (16, '0:0:0:0:0:0:0:1', 1, '用户名或密码错误', '2020-04-22 08:23:12', 'LOGIN_FAILURE_STATUS');
INSERT INTO `login_log` VALUES (17, '0:0:0:0:0:0:0:1', 1, '账户已锁定，请60秒后再试', '2020-04-22 08:23:14', 'LOGIN_FAILURE_STATUS');
INSERT INTO `login_log` VALUES (18, '0:0:0:0:0:0:0:1', 1, NULL, '2020-04-22 08:48:09', 'LOGIN_SUCCESS_STATUS');
INSERT INTO `login_log` VALUES (19, '0:0:0:0:0:0:0:1', 1, NULL, '2020-04-22 08:59:00', 'LOGIN_SUCCESS_STATUS');
INSERT INTO `login_log` VALUES (20, '0:0:0:0:0:0:0:1', 1, NULL, '2020-04-22 09:56:32', 'LOGIN_SUCCESS_STATUS');
INSERT INTO `login_log` VALUES (21, '0:0:0:0:0:0:0:1', 1, NULL, '2020-04-22 10:48:42', 'LOGIN_SUCCESS_STATUS');
INSERT INTO `login_log` VALUES (22, '0:0:0:0:0:0:0:1', 1, NULL, '2020-04-22 11:02:16', 'LOGIN_SUCCESS_STATUS');
INSERT INTO `login_log` VALUES (23, '0:0:0:0:0:0:0:1', 1, NULL, '2020-04-22 14:06:29', 'LOGIN_SUCCESS_STATUS');
INSERT INTO `login_log` VALUES (24, '0:0:0:0:0:0:0:1', 1, NULL, '2020-04-23 09:04:57', 'LOGIN_SUCCESS_STATUS');
INSERT INTO `login_log` VALUES (25, '0:0:0:0:0:0:0:1', 1, NULL, '2020-04-23 11:02:23', 'LOGIN_SUCCESS_STATUS');
INSERT INTO `login_log` VALUES (26, '0:0:0:0:0:0:0:1', 1, '用户名或密码错误', '2020-04-23 11:06:29', 'LOGIN_FAILURE_STATUS');
INSERT INTO `login_log` VALUES (27, '0:0:0:0:0:0:0:1', 1, NULL, '2020-04-23 11:06:40', 'LOGIN_SUCCESS_STATUS');
INSERT INTO `login_log` VALUES (28, '0:0:0:0:0:0:0:1', 1, NULL, '2020-04-23 11:08:20', 'LOGIN_SUCCESS_STATUS');
INSERT INTO `login_log` VALUES (29, '0:0:0:0:0:0:0:1', 1, NULL, '2020-04-23 11:08:40', 'LOGIN_SUCCESS_STATUS');
INSERT INTO `login_log` VALUES (30, '0:0:0:0:0:0:0:1', 1, NULL, '2020-04-23 11:34:49', 'LOGIN_SUCCESS_STATUS');
INSERT INTO `login_log` VALUES (31, '0:0:0:0:0:0:0:1', 1, NULL, '2020-04-23 11:35:05', 'LOGIN_SUCCESS_STATUS');
INSERT INTO `login_log` VALUES (32, '0:0:0:0:0:0:0:1', 1, '用户名或密码错误', '2020-04-23 13:38:56', 'LOGIN_FAILURE_STATUS');
INSERT INTO `login_log` VALUES (33, '0:0:0:0:0:0:0:1', 1, NULL, '2020-04-23 13:39:02', 'LOGIN_SUCCESS_STATUS');
INSERT INTO `login_log` VALUES (34, '0:0:0:0:0:0:0:1', 1, NULL, '2020-04-23 13:39:16', 'LOGIN_SUCCESS_STATUS');
INSERT INTO `login_log` VALUES (35, '0:0:0:0:0:0:0:1', 1, NULL, '2020-04-23 15:18:45', 'LOGIN_SUCCESS_STATUS');
INSERT INTO `login_log` VALUES (36, '0:0:0:0:0:0:0:1', 1, NULL, '2020-04-23 16:48:03', 'LOGIN_SUCCESS_STATUS');
INSERT INTO `login_log` VALUES (37, '0:0:0:0:0:0:0:1', 1, NULL, '2020-04-23 17:30:01', 'LOGIN_SUCCESS_STATUS');
INSERT INTO `login_log` VALUES (38, '0:0:0:0:0:0:0:1', 1, NULL, '2020-04-24 10:27:55', 'LOGIN_SUCCESS_STATUS');
INSERT INTO `login_log` VALUES (39, '0:0:0:0:0:0:0:1', 1, '用户名或密码错误', '2020-04-24 11:44:33', 'LOGIN_FAILURE_STATUS');
INSERT INTO `login_log` VALUES (40, '0:0:0:0:0:0:0:1', 1, NULL, '2020-04-24 11:44:37', 'LOGIN_SUCCESS_STATUS');
INSERT INTO `login_log` VALUES (41, '0:0:0:0:0:0:0:1', 1, NULL, '2020-04-24 14:11:06', 'LOGIN_SUCCESS_STATUS');
INSERT INTO `login_log` VALUES (42, '0:0:0:0:0:0:0:1', 3, NULL, '2020-04-24 16:27:39', 'LOGIN_SUCCESS_STATUS');
INSERT INTO `login_log` VALUES (43, '0:0:0:0:0:0:0:1', 3, NULL, '2020-04-24 16:29:33', 'LOGIN_SUCCESS_STATUS');
INSERT INTO `login_log` VALUES (44, '0:0:0:0:0:0:0:1', 3, NULL, '2020-04-24 18:56:02', 'LOGIN_SUCCESS_STATUS');
INSERT INTO `login_log` VALUES (45, '0:0:0:0:0:0:0:1', 3, NULL, '2020-04-26 08:24:10', 'LOGIN_SUCCESS_STATUS');
INSERT INTO `login_log` VALUES (46, '0:0:0:0:0:0:0:1', 3, NULL, '2020-04-26 08:36:21', 'LOGIN_SUCCESS_STATUS');
INSERT INTO `login_log` VALUES (47, '0:0:0:0:0:0:0:1', 4, NULL, '2020-04-26 08:36:54', 'LOGIN_SUCCESS_STATUS');
INSERT INTO `login_log` VALUES (48, '0:0:0:0:0:0:0:1', 3, NULL, '2020-04-26 08:37:10', 'LOGIN_SUCCESS_STATUS');
INSERT INTO `login_log` VALUES (49, '0:0:0:0:0:0:0:1', 3, NULL, '2020-04-26 09:55:13', 'LOGIN_SUCCESS_STATUS');
INSERT INTO `login_log` VALUES (50, '0:0:0:0:0:0:0:1', 3, NULL, '2020-04-30 08:26:01', 'LOGIN_SUCCESS_STATUS');
INSERT INTO `login_log` VALUES (51, '0:0:0:0:0:0:0:1', 3, NULL, '2020-04-30 10:00:35', 'LOGIN_SUCCESS_STATUS');

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu`  (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `menu_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '菜单名称',
  `parent_id` int(9) NULL DEFAULT NULL,
  `mapping_address` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `create_time` datetime(0) NULL,
  `update_time` datetime(0) NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES (1, '系统管理', NULL, '#', '2020-04-21 16:56:47', '2020-04-21 16:56:50');
INSERT INTO `menu` VALUES (2, '员工管理', 1, '/employee', '2020-04-21 16:58:18', '2020-04-21 16:58:21');
INSERT INTO `menu` VALUES (3, '角色管理', 1, '/role', '2020-04-21 17:02:15', '2020-04-21 17:02:17');
INSERT INTO `menu` VALUES (4, '菜单管理', 1, '/menu', '2020-04-21 17:02:40', '2020-04-21 17:02:42');
INSERT INTO `menu` VALUES (5, '数据字典管理', 1, '/dataDictionary', '2020-04-30 09:58:05', '2020-04-30 09:58:05');
INSERT INTO `menu` VALUES (6, '登录日志管理', 1, '/loginLog', '2020-04-30 09:58:45', '2020-04-30 09:58:45');
INSERT INTO `menu` VALUES (7, '人力资源管理', NULL, '#', '2020-04-30 09:59:22', '2020-04-30 09:59:22');
INSERT INTO `menu` VALUES (8, '部门管理', 7, '/department', '2020-04-30 09:59:44', '2020-04-30 09:59:44');
INSERT INTO `menu` VALUES (9, '职位管理', 7, '/position', '2020-04-30 10:00:20', '2020-04-30 10:03:49');

-- ----------------------------
-- Table structure for payroll
-- ----------------------------
DROP TABLE IF EXISTS `payroll`;
CREATE TABLE `payroll`  (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `push_money` decimal(10, 2) NULL DEFAULT NULL COMMENT '提成',
  `meal_subsidy` decimal(10, 2) NULL DEFAULT NULL COMMENT '餐补',
  `other_subsidy` decimal(10, 2) NULL DEFAULT NULL COMMENT '其他补贴',
  `performance_appraisal` decimal(10, 2) NULL DEFAULT NULL,
  `late_buckle` decimal(10, 2) NULL DEFAULT NULL COMMENT '迟到乐捐',
  `other_buckle` decimal(10, 2) NULL DEFAULT NULL COMMENT '其他扣款',
  `personal_social_security` decimal(10, 2) NOT NULL COMMENT '个人社保',
  `personal_housing_fund` decimal(10, 2) NOT NULL COMMENT '个人住房公积金',
  `personal_income_tax` decimal(10, 2) NOT NULL COMMENT '个人所得税',
  `employee_id` int(9) NOT NULL,
  `create_time` datetime(0) NOT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `update_time` datetime(0) NOT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '工资单表\r\n' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission`  (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `permission_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '权限名称',
  `authority` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '权限编号',
  `mapping_address` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限对应的请求路径',
  `menu_id` int(9) NOT NULL,
  `create_time` datetime(0) NULL,
  `update_time` datetime(0) NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of permission
-- ----------------------------
INSERT INTO `permission` VALUES (1, '系统管理', 'SYS_MANAGE', '#', 1, '2020-04-21 16:56:21', '2020-04-21 16:56:23');
INSERT INTO `permission` VALUES (2, '员工管理', 'EMPLOYEE_MANAGE', '/employee', 2, '2020-04-21 16:57:46', '2020-04-21 16:57:48');
INSERT INTO `permission` VALUES (3, '修改密码', 'CHANGE_PASSWORD', '/employee/changePassword', 2, '2020-04-21 17:03:41', '2020-04-21 17:03:44');

-- ----------------------------
-- Table structure for position
-- ----------------------------
DROP TABLE IF EXISTS `position`;
CREATE TABLE `position`  (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `position_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `position_level` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '职位等级',
  `position_salary` decimal(10, 2) NOT NULL COMMENT '职位薪资',
  `create_time` datetime(0) NULL,
  `update_time` datetime(0) NULL,
  `department_id` int(9) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '职位表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of position
-- ----------------------------
INSERT INTO `position` VALUES (1, '架构师', 'A', 12000.00, '2020-04-23 14:35:14', '2020-04-23 14:35:16', 1);
INSERT INTO `position` VALUES (2, '部门经理', 'A', 15000.00, '2020-04-24 15:29:51', '2020-04-24 15:29:49', 1);

-- ----------------------------
-- Table structure for qrtz_blob_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_blob_triggers`;
CREATE TABLE `qrtz_blob_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_NAME` varchar(190) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(190) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `BLOB_DATA` blob NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  INDEX `SCHED_NAME`(`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_calendars
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_calendars`;
CREATE TABLE `qrtz_calendars`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `CALENDAR_NAME` varchar(190) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `CALENDAR` blob NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `CALENDAR_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_cron_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_cron_triggers`;
CREATE TABLE `qrtz_cron_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_NAME` varchar(190) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(190) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `CRON_EXPRESSION` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TIME_ZONE_ID` varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_fired_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_fired_triggers`;
CREATE TABLE `qrtz_fired_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `ENTRY_ID` varchar(95) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_NAME` varchar(190) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(190) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `INSTANCE_NAME` varchar(190) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `FIRED_TIME` bigint(13) NOT NULL,
  `SCHED_TIME` bigint(13) NOT NULL,
  `PRIORITY` int(11) NOT NULL,
  `STATE` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `JOB_NAME` varchar(190) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `JOB_GROUP` varchar(190) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `IS_NONCONCURRENT` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `REQUESTS_RECOVERY` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `ENTRY_ID`) USING BTREE,
  INDEX `IDX_QRTZ_FT_TRIG_INST_NAME`(`SCHED_NAME`, `INSTANCE_NAME`) USING BTREE,
  INDEX `IDX_QRTZ_FT_INST_JOB_REQ_RCVRY`(`SCHED_NAME`, `INSTANCE_NAME`, `REQUESTS_RECOVERY`) USING BTREE,
  INDEX `IDX_QRTZ_FT_J_G`(`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_FT_JG`(`SCHED_NAME`, `JOB_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_FT_T_G`(`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_FT_TG`(`SCHED_NAME`, `TRIGGER_GROUP`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_job_details
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_job_details`;
CREATE TABLE `qrtz_job_details`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `JOB_NAME` varchar(190) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `JOB_GROUP` varchar(190) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `DESCRIPTION` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `JOB_CLASS_NAME` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `IS_DURABLE` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `IS_NONCONCURRENT` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `IS_UPDATE_DATA` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `REQUESTS_RECOVERY` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `JOB_DATA` blob NULL,
  PRIMARY KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_J_REQ_RECOVERY`(`SCHED_NAME`, `REQUESTS_RECOVERY`) USING BTREE,
  INDEX `IDX_QRTZ_J_GRP`(`SCHED_NAME`, `JOB_GROUP`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_locks
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_locks`;
CREATE TABLE `qrtz_locks`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `LOCK_NAME` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `LOCK_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_locks
-- ----------------------------
INSERT INTO `qrtz_locks` VALUES ('quartzScheduler', 'TRIGGER_ACCESS');

-- ----------------------------
-- Table structure for qrtz_paused_trigger_grps
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_paused_trigger_grps`;
CREATE TABLE `qrtz_paused_trigger_grps`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(190) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_GROUP`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_scheduler_state
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_scheduler_state`;
CREATE TABLE `qrtz_scheduler_state`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `INSTANCE_NAME` varchar(190) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `LAST_CHECKIN_TIME` bigint(13) NOT NULL,
  `CHECKIN_INTERVAL` bigint(13) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `INSTANCE_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_simple_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simple_triggers`;
CREATE TABLE `qrtz_simple_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_NAME` varchar(190) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(190) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `REPEAT_COUNT` bigint(7) NOT NULL,
  `REPEAT_INTERVAL` bigint(12) NOT NULL,
  `TIMES_TRIGGERED` bigint(10) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_simprop_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simprop_triggers`;
CREATE TABLE `qrtz_simprop_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_NAME` varchar(190) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(190) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `STR_PROP_1` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `STR_PROP_2` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `STR_PROP_3` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `INT_PROP_1` int(11) NULL DEFAULT NULL,
  `INT_PROP_2` int(11) NULL DEFAULT NULL,
  `LONG_PROP_1` bigint(20) NULL DEFAULT NULL,
  `LONG_PROP_2` bigint(20) NULL DEFAULT NULL,
  `DEC_PROP_1` decimal(13, 4) NULL DEFAULT NULL,
  `DEC_PROP_2` decimal(13, 4) NULL DEFAULT NULL,
  `BOOL_PROP_1` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `BOOL_PROP_2` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_triggers`;
CREATE TABLE `qrtz_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_NAME` varchar(190) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_GROUP` varchar(190) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `JOB_NAME` varchar(190) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `JOB_GROUP` varchar(190) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `DESCRIPTION` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `NEXT_FIRE_TIME` bigint(13) NULL DEFAULT NULL,
  `PREV_FIRE_TIME` bigint(13) NULL DEFAULT NULL,
  `PRIORITY` int(11) NULL DEFAULT NULL,
  `TRIGGER_STATE` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `TRIGGER_TYPE` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `START_TIME` bigint(13) NOT NULL,
  `END_TIME` bigint(13) NULL DEFAULT NULL,
  `CALENDAR_NAME` varchar(190) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `MISFIRE_INSTR` smallint(2) NULL DEFAULT NULL,
  `JOB_DATA` blob NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_T_J`(`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_T_JG`(`SCHED_NAME`, `JOB_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_T_C`(`SCHED_NAME`, `CALENDAR_NAME`) USING BTREE,
  INDEX `IDX_QRTZ_T_G`(`SCHED_NAME`, `TRIGGER_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_T_STATE`(`SCHED_NAME`, `TRIGGER_STATE`) USING BTREE,
  INDEX `IDX_QRTZ_T_N_STATE`(`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`, `TRIGGER_STATE`) USING BTREE,
  INDEX `IDX_QRTZ_T_N_G_STATE`(`SCHED_NAME`, `TRIGGER_GROUP`, `TRIGGER_STATE`) USING BTREE,
  INDEX `IDX_QRTZ_T_NEXT_FIRE_TIME`(`SCHED_NAME`, `NEXT_FIRE_TIME`) USING BTREE,
  INDEX `IDX_QRTZ_T_NFT_ST`(`SCHED_NAME`, `TRIGGER_STATE`, `NEXT_FIRE_TIME`) USING BTREE,
  INDEX `IDX_QRTZ_T_NFT_MISFIRE`(`SCHED_NAME`, `MISFIRE_INSTR`, `NEXT_FIRE_TIME`) USING BTREE,
  INDEX `IDX_QRTZ_T_NFT_ST_MISFIRE`(`SCHED_NAME`, `MISFIRE_INSTR`, `NEXT_FIRE_TIME`, `TRIGGER_STATE`) USING BTREE,
  INDEX `IDX_QRTZ_T_NFT_ST_MISFIRE_GRP`(`SCHED_NAME`, `MISFIRE_INSTR`, `NEXT_FIRE_TIME`, `TRIGGER_GROUP`, `TRIGGER_STATE`) USING BTREE,
  CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `qrtz_job_details` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色名称',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (1, '管理员', '2020-04-24 14:13:45', '2020-04-24 14:13:47');
INSERT INTO `role` VALUES (2, '销售员', '2020-04-26 09:15:23', '2020-04-26 09:15:23');
INSERT INTO `role` VALUES (3, '仓库管理员', '2020-04-26 09:15:51', '2020-04-26 09:15:51');
INSERT INTO `role` VALUES (4, '人力资源专员', '2020-04-26 09:16:48', '2020-04-26 09:16:48');
INSERT INTO `role` VALUES (5, '财务专员', '2020-04-30 08:44:03', '2020-04-30 08:44:03');

-- ----------------------------
-- Table structure for role_permission
-- ----------------------------
DROP TABLE IF EXISTS `role_permission`;
CREATE TABLE `role_permission`  (
  `role_id` int(9) NOT NULL,
  `permission_id` int(9) NOT NULL,
  INDEX `idx_role_id`(`role_id`) USING BTREE,
  INDEX `idx_permission_id`(`permission_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
