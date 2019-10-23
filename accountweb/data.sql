/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.1.183-dev
 Source Server Type    : MySQL
 Source Server Version : 50722
 Source Host           : 192.168.1.183:5567
 Source Schema         : kq_bill

 Target Server Type    : MySQL
 Target Server Version : 50722
 File Encoding         : 65001

 Date: 23/10/2019 11:58:48
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for acq_out_bill
-- ----------------------------
DROP TABLE IF EXISTS `acq_out_bill`;
CREATE TABLE `acq_out_bill`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `out_bill_id` bigint(20) NULL DEFAULT NULL COMMENT '出账单ID',
  `acq_org_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构编号',
  `today_amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '当日交易金额',
  `today_history_balance` decimal(20, 2) NULL DEFAULT NULL COMMENT '当日历史余额',
  `today_balance` decimal(20, 2) NULL DEFAULT NULL COMMENT '当日余额',
  `out_account_task_amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '出账任务金额',
  `up_balance` decimal(20, 2) NULL DEFAULT NULL COMMENT '历史余额',
  `calc_out_amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '计算出账金额(也叫出账单金额)',
  `out_amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '实际出账金额',
  `out_bill_result` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '出账结果（成功/异常（实际出账金额与预计出账金额相比，不完整出账/多出账/未出账的都是异常））',
  `acq_reference` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参考号',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `out_bill_id_ind`(`out_bill_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '出账单收单机构表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for adjust_account
-- ----------------------------
DROP TABLE IF EXISTS `adjust_account`;
CREATE TABLE `adjust_account`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `applicant` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '提交人',
  `applicant_time` timestamp(0) NULL DEFAULT NULL COMMENT '提交时间',
  `approver` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '审批人',
  `approve_time` timestamp(0) NULL DEFAULT NULL COMMENT '审批时间',
  `status` int(11) NULL DEFAULT 0 COMMENT '状态：1--待提交审核，2--待审批，3--审批通过，4--记账失败，5--审批不通过',
  `approve_remark` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '审核备注',
  `remark` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '提交备注',
  `account_type` int(11) NULL DEFAULT 1 COMMENT '调账类型：1--长款，2--短款，3--其他',
  `file_path` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上传excel模板路径',
  `record_fail_remark` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '记账失败原因',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '调账信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for adjust_detail
-- ----------------------------
DROP TABLE IF EXISTS `adjust_detail`;
CREATE TABLE `adjust_detail`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `adjust_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '调账ID',
  `amount_from` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '借贷标识：debit-借,credit-贷,frozen-冻结,thaw-解冻',
  `account_flag` int(11) NULL DEFAULT 11 COMMENT '1:内部账户，0：外部账户',
  `account` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账号',
  `amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '调帐金额',
  `remark` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `account_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '外部账用户类型(商户、代理商、收单机构)',
  `user_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '外部用户编号',
  `account_owner` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账户归属',
  `subject_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '科目编号',
  `currency_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '币种号',
  `card_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '卡号（预付卡号，若没有，则为空）',
  `trans_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易序号',
  `journal_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分录号',
  `child_trans_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '子交易号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '调账明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for agent_acc_pre_adjust
-- ----------------------------
DROP TABLE IF EXISTS `agent_acc_pre_adjust`;
CREATE TABLE `agent_acc_pre_adjust`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `adjust_time` timestamp(0) NULL DEFAULT NULL,
  `applicant` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '提交人',
  `agent_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '代理商编号',
  `agent_node` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商节点',
  `agent_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商名称',
  `agent_level` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商级别',
  `adjust_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '预调账金额',
  `adjust_reason` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '调账原因',
  `subject_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '科目内部编号',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `agent_no`(`agent_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 31 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '代理商账户预调账表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for agent_pre_adjust
-- ----------------------------
DROP TABLE IF EXISTS `agent_pre_adjust`;
CREATE TABLE `agent_pre_adjust`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `adjust_time` timestamp(0) NULL DEFAULT NULL,
  `applicant` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '提交人',
  `agent_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '代理商编号',
  `agent_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '代理商名称',
  `open_back_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '开通返现',
  `rate_diff_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '费率差异',
  `tui_cost_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '超级推成本',
  `risk_sub_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '风控扣款',
  `bail_sub_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '保证金预扣款',
  `mer_mg_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '商户管理费',
  `other_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '其他',
  `adjust_reason` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '调账原因',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `activity_available_amount` decimal(11, 2) NULL DEFAULT 0.00 COMMENT '账户可用余额调账金额',
  `activity_freeze_amount` decimal(11, 2) NULL DEFAULT 0.00 COMMENT '账户冻结余额调账金额',
  `generate_amount` decimal(11, 2) NULL DEFAULT 0.00 COMMENT '预调账金额',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '代理商预调账表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for agent_pre_freeze
-- ----------------------------
DROP TABLE IF EXISTS `agent_pre_freeze`;
CREATE TABLE `agent_pre_freeze`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `agent_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '代理商编号',
  `agent_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '代理商名称',
  `freeze_time` timestamp(0) NULL DEFAULT NULL COMMENT '冻结时间',
  `operater` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作人',
  `terminal_freeze_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '机具款冻结',
  `other_freeze_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '其他冻结',
  `freeze_reason` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '冻结原因',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `fen_freeze_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '分润账户预冻金额',
  `activity_freeze_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '活动补贴账户预冻金额',
  `freeze_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '预冻金额',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 64 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '代理商预冻结表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for agent_pre_record_total
-- ----------------------------
DROP TABLE IF EXISTS `agent_pre_record_total`;
CREATE TABLE `agent_pre_record_total`  (
  `agent_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '代理商编号',
  `agent_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '代理商名称',
  `open_back_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '开通返现',
  `rate_diff_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '费率差异',
  `tui_cost_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '超级推成本',
  `risk_sub_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '风控扣款',
  `mer_mg_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '商户管理费',
  `other_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '其他',
  `terminal_freeze_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '机具款冻结',
  `other_freeze_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '其他冻结',
  `bail_sub_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '保证金预扣款',
  PRIMARY KEY (`agent_no`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '代理商预记账累计表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for agent_share_day_settle
-- ----------------------------
DROP TABLE IF EXISTS `agent_share_day_settle`;
CREATE TABLE `agent_share_day_settle`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `collection_batch_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分润汇总批次号',
  `group_time` timestamp(0) NULL DEFAULT NULL COMMENT '汇总时间',
  `trans_date` date NULL DEFAULT NULL COMMENT '交易日期',
  `one_agent_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商编号',
  `one_agent_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商名称',
  `agent_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商编号',
  `agent_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商名称',
  `agent_node` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商节点',
  `agent_level` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商级别',
  `parent_agent_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '父级代理商编号',
  `sale_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属销售',
  `trans_total_amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '交易总金额',
  `trans_total_num` int(11) NULL DEFAULT NULL COMMENT '交易总笔数',
  `dui_succ_trans_total_amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '对账成功交易总金额',
  `dui_succ_trans_total_num` int(11) NULL DEFAULT NULL COMMENT '对账成功交易总笔数',
  `cash_total_num` int(11) NULL DEFAULT NULL COMMENT '提现总笔数',
  `mer_fee` decimal(20, 2) NULL DEFAULT NULL COMMENT '商户交易手续费',
  `mer_cash_fee` decimal(20, 2) NULL DEFAULT NULL COMMENT '商户提现手续费',
  `deduction_fee` decimal(30, 2) NULL DEFAULT NULL COMMENT '抵扣商户提现手续费',
  `acq_out_cost` decimal(20, 2) NULL DEFAULT NULL COMMENT '收单成本',
  `acq_out_profit` decimal(20, 2) NULL DEFAULT NULL COMMENT '收单收益',
  `dai_cost` decimal(20, 2) NULL DEFAULT NULL COMMENT '代付成本',
  `dian_cost` decimal(20, 2) NULL DEFAULT NULL COMMENT '垫资成本',
  `pre_trans_share_amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '原交易分润',
  `pre_trans_cash_amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '原提现分润',
  `open_back_amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '开通返现',
  `rate_diff_amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '费率差异',
  `tui_cost_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '超级推成本',
  `risk_sub_amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '风控扣款',
  `bail_sub_amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '保证金扣除',
  `mer_mg_amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '商户管理费',
  `other_amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '其他',
  `adjust_trans_share_amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '调整后交易分润',
  `adjust_trans_cash_amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '调整后提现分润',
  `adjust_total_share_amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '调整后总分润',
  `terminal_freeze_amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '机具款冻结',
  `other_freeze_amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '其他冻结',
  `enter_account_status` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '入账状态(NOENTERACCOUNT未入账,ENTERACCOUNTED已入账)',
  `real_enter_share_amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '实际到账分润',
  `enter_account_message` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '入账信息',
  `enter_account_time` timestamp(0) NULL DEFAULT NULL COMMENT '入账时间',
  `operator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作人',
  `trans_deduction_fee` decimal(30, 2) NULL DEFAULT 0.00 COMMENT '抵扣交易商户手续费',
  `actual_fee` decimal(30, 2) NULL DEFAULT 0.00 COMMENT '实际交易手续费',
  `merchant_price` decimal(30, 2) NULL DEFAULT 0.00 COMMENT '自选商户手续费',
  `deduction_mer_fee` decimal(30, 2) NULL DEFAULT 0.00 COMMENT '抵扣自选商户手续费',
  `actual_optional_fee` decimal(30, 2) NULL DEFAULT 0.00 COMMENT '实际自选商户手续费',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `asds_tea_index`(`trans_date`, `enter_account_status`, `agent_level`) USING BTREE,
  INDEX `asds_caa_index`(`collection_batch_no`, `agent_node`, `agent_level`) USING BTREE,
  INDEX `asds_cp_index`(`collection_batch_no`, `parent_agent_no`) USING BTREE,
  INDEX `trans_data`(`trans_date`, `agent_no`, `enter_account_status`) USING BTREE,
  INDEX `ind_agent_no`(`agent_no`) USING BTREE,
  INDEX `parent_node_index`(`parent_agent_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '代理商分润日结表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for agent_unfreeze
-- ----------------------------
DROP TABLE IF EXISTS `agent_unfreeze`;
CREATE TABLE `agent_unfreeze`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `agent_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商编号',
  `agent_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商名称',
  `unfreeze_time` timestamp(0) NULL DEFAULT NULL COMMENT '解冻时间',
  `operater` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作人',
  `amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '解冻金额',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `terminal_freeze_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '机具预解冻金额',
  `other_freeze_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '其他预解冻金额',
  `fen_freeze_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '分润账户预解冻金额',
  `activity_freeze_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '活动补贴账户预解冻金额',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '代理商解冻表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bank_account_info
-- ----------------------------
DROP TABLE IF EXISTS `bank_account_info`;
CREATE TABLE `bank_account_info`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `bank_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '开户行全称',
  `account_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '开户名',
  `account_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '开户帐号',
  `org_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '支付机构号',
  `currency_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '币种号',
  `account_type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账户类别：1-结算账户',
  `subject_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '科目内部编号',
  `cnaps_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联行行号',
  `ins_account_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '内部(虚拟)账号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 63 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '银行帐户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for batches
-- ----------------------------
DROP TABLE IF EXISTS `batches`;
CREATE TABLE `batches`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` int(11) NULL DEFAULT NULL COMMENT '跑批结果  0:失败  1：成功',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '跑批时间',
  `current_date` date NULL DEFAULT NULL COMMENT 'systeminfo当前日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 84 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for batches_detail
-- ----------------------------
DROP TABLE IF EXISTS `batches_detail`;
CREATE TABLE `batches_detail`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `step_id` int(11) NULL DEFAULT NULL COMMENT '步骤编号',
  `execute_mode` int(11) NULL DEFAULT NULL COMMENT '执行方式  0：自动执行   1：人工执行',
  `execute_result` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '执行日志',
  `status` int(11) NULL DEFAULT NULL COMMENT '运行结果  0:未运行  1:成功 2:失败',
  `execute_person` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '人工执行跑批的人',
  `execute_time` timestamp(0) NULL DEFAULT NULL COMMENT '人工执行跑批时间',
  `batches_id` int(11) NULL DEFAULT NULL COMMENT '跑批批次id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 831 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for batches_step
-- ----------------------------
DROP TABLE IF EXISTS `batches_step`;
CREATE TABLE `batches_step`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `parent_id` int(11) NULL DEFAULT NULL,
  `step_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `step_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_operator` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for before_adjust_apply
-- ----------------------------
DROP TABLE IF EXISTS `before_adjust_apply`;
CREATE TABLE `before_adjust_apply`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `agent_no` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商编号',
  `agent_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商名称',
  `applicant` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '申请人',
  `freeze_amount` decimal(11, 2) NULL DEFAULT 0.00 COMMENT '申请调账金额',
  `activity_available_amount` decimal(11, 2) NULL DEFAULT 0.00 COMMENT '活动补贴账户可用余额调账金额',
  `activity_freeze_amount` decimal(11, 2) NULL DEFAULT 0.00 COMMENT '活动补贴账户冻结余额调账金额',
  `generate_amount` decimal(11, 2) NULL DEFAULT 0.00 COMMENT '生成预调账金额',
  `apply_date` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '申请调账时间',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `is_apply` int(1) NULL DEFAULT 0 COMMENT '调账申请：1 其他：0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 175 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '预调账申请表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bill_ext_account
-- ----------------------------
DROP TABLE IF EXISTS `bill_ext_account`;
CREATE TABLE `bill_ext_account`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `account_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '外部账号',
  `account_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账户名称',
  `org_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '机构号',
  `currency_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '币种号',
  `subject_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '科目内部编号',
  `curr_balance` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '余额',
  `control_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '冻结金额',
  `settling_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '结算中金额',
  `settling_hold_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '结算保留中金额',
  `pre_freeze_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '预冻结金额',
  `parent_trans_day` date NULL DEFAULT NULL COMMENT '上一个交易日',
  `parent_trans_balance` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '上一个交易余额',
  `account_status` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账户状态:1-正常,2-销户,3-冻结只进不出,4-冻结不进不出',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '开户日期时间',
  `balance_add_from` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '余额增加记借方还是贷方：debit-借方,credit-贷方',
  `balance_from` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '余额方向：debit-借方,credit-贷方',
  `day_bal_flag` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '日终修改余额标志:0-日间，1-日终',
  `sum_flag` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '汇总入明细标志:0-日间单笔，1-日终单笔，2-日终汇总',
  `creator` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `account_no`(`account_no`) USING BTREE,
  INDEX `subject_no`(`subject_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2385 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '外部用户账户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bill_ext_history_balance
-- ----------------------------
DROP TABLE IF EXISTS `bill_ext_history_balance`;
CREATE TABLE `bill_ext_history_balance`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `bill_date` date NULL DEFAULT NULL COMMENT '记账日期',
  `account_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '外部账号',
  `org_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '机构号',
  `currency_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '币种号',
  `subject_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '科目内部编号',
  `curr_balance` decimal(20, 2) NOT NULL COMMENT '当前余额',
  `control_amount` decimal(20, 2) NOT NULL DEFAULT 0.00 COMMENT '控制金额',
  `parent_trans_balance` decimal(20, 2) NOT NULL DEFAULT 0.00 COMMENT '上一个交易余额',
  `account_status` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账户状态:1-正常，2-销户，3-冻结',
  `create_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '开户日期时间',
  `balance_add_from` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '余额增加借贷方向:debit-借方,credit-贷方',
  `balance_from` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '余额方向：debit-借方,credit-贷方',
  `account_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账户名称',
  `overdraft_flag` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否允许透支：1允许，0不允许',
  `overdraft_amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '允许透支金额',
  `day_bal_flag` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '日终修改余额标志:0-日间，1-日终',
  `sum_flag` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '汇总入明细标志:0-日间单笔，1-日终单笔，2-日终汇总',
  `settling_amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '结算中金额',
  `pre_freeze_amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '预冻结金额',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `account_no`(`bill_date`, `account_no`) USING BTREE,
  INDEX `account_no_ind`(`account_no`) USING BTREE,
  INDEX `account_no_done`(`account_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '外部用户账户历史余额表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bill_ext_user_type_subject
-- ----------------------------
DROP TABLE IF EXISTS `bill_ext_user_type_subject`;
CREATE TABLE `bill_ext_user_type_subject`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '外部用户类型:M-商户、A-代理商、C-收单机构',
  `subject_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '科目内部编号',
  `creator` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updator` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` timestamp(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `merchant_id_UNIQUE`(`user_type`, `subject_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 142 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '外部用户类型科目关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bill_ext_user_type_subject_temp
-- ----------------------------
DROP TABLE IF EXISTS `bill_ext_user_type_subject_temp`;
CREATE TABLE `bill_ext_user_type_subject_temp`  (
  `id` bigint(20) NOT NULL DEFAULT 0 COMMENT 'id',
  `user_type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '外部用户类型:M-商户、A-代理商、C-收单机构',
  `subject_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '科目内部编号'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bill_ins_account
-- ----------------------------
DROP TABLE IF EXISTS `bill_ins_account`;
CREATE TABLE `bill_ins_account`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `account_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '内部账号',
  `org_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '机构号',
  `currency_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '币种号',
  `subject_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '科目内部编号',
  `account_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账户名称',
  `curr_balance` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '余额',
  `control_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '冻结金额',
  `parent_trans_day` date NULL DEFAULT NULL COMMENT '上一个交易日',
  `parent_trans_balance` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '上一个交易余额',
  `account_status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账户状态:1-正常，2-销户，3-冻结',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '开户日期时间',
  `balance_add_from` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '余额增加借贷方向:debit-借方,credit-贷方',
  `balance_from` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '余额方向：debit-借方,credit-贷方',
  `day_bal_flag` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '日终修改余额标志:0-日间，1-日终',
  `sum_flag` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '汇总入明细标志:0-日间单笔，1-日终单笔，2-日终汇总',
  `creator` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `account_no`(`account_no`) USING BTREE,
  UNIQUE INDEX `unique_key`(`org_no`, `currency_no`, `subject_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 554 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '内部账账户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bill_ins_history_balance
-- ----------------------------
DROP TABLE IF EXISTS `bill_ins_history_balance`;
CREATE TABLE `bill_ins_history_balance`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `account_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '账号',
  `bill_date` date NULL DEFAULT NULL COMMENT '记账日期',
  `org_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '机构号',
  `currency_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '币种号',
  `subject_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '科目内部编号',
  `account_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账户名称',
  `curr_balance` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '余额',
  `control_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '控制金额',
  `parent_trans_balance` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '上一个交易日余额',
  `account_status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账户状态:1-正常，2-销户，3-冻结',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_key`(`account_no`, `bill_date`, `org_no`, `currency_no`, `subject_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '内部帐账户历史余额表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bill_shadow_account
-- ----------------------------
DROP TABLE IF EXISTS `bill_shadow_account`;
CREATE TABLE `bill_shadow_account`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `account_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账号',
  `account_flag` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '内部账外部账标志:0-外部账号，1-内部账号',
  `trans_date` date NULL DEFAULT NULL COMMENT '交易日期',
  `debit_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '借方发生额',
  `credit_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '贷方发生额',
  `booked_flag` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否入账户标志(1:入账;0:未入账)',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `acccount_no_key`(`account_no`) USING BTREE,
  INDEX `trans_date`(`trans_date`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '影子分户账表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bill_subject
-- ----------------------------
DROP TABLE IF EXISTS `bill_subject`;
CREATE TABLE `bill_subject`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `subject_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '科目编号',
  `subject_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '科目名称',
  `subject_level` int(11) NOT NULL DEFAULT 0 COMMENT '科目级别',
  `parent_subject_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上级科目内部编号',
  `subject_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '科目类型：1-资产类科目，2-负债类科目，3-所有者权益类科目，4-收入类科目，5-支出类科目，6-共同类科目',
  `balance_from` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '余额方向：debit-借方,credit-贷方',
  `add_balance_from` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '额增加借贷方向：debit-借方,credit-贷方',
  `debit_credit_flag` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参加借贷平衡检查标志：0-没参加，1-参加',
  `is_inner_account` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否开立内部账户：0-否，1-是',
  `inner_day_bal_flag` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '日终修改余额标志：0-日间，1-日终',
  `inner_sum_flag` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '明细处理方式：0-日间单笔，1-日终单笔，2-日终汇总',
  `creator` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updator` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `update_time` timestamp(0) NULL DEFAULT NULL,
  `subject_alias` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '科目别名',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `subject_no`(`subject_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 199 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '科目表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bill_subject_info
-- ----------------------------
DROP TABLE IF EXISTS `bill_subject_info`;
CREATE TABLE `bill_subject_info`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `subject_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '科目内部编号',
  `org_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '机构号',
  `currency_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '币种号',
  `create_date` date NOT NULL COMMENT '日期',
  `subject_level` int(11) UNSIGNED NULL DEFAULT 0 COMMENT '科目级别',
  `balance_from` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '余额方向：debit-借方,credit-贷方',
  `today_balance` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '本日贷方发生额',
  `today_debit_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '本日借方发生额',
  `today_credit_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '本日贷方发生额',
  `yesterday_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '昨日日终余额',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `fuh_key`(`subject_no`, `org_no`, `currency_no`, `create_date`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24368 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '科目动态信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bill_subject_info_temp
-- ----------------------------
DROP TABLE IF EXISTS `bill_subject_info_temp`;
CREATE TABLE `bill_subject_info_temp`  (
  `id` bigint(20) UNSIGNED NOT NULL DEFAULT 0,
  `subject_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '科目内部编号',
  `org_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '机构号',
  `currency_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '币种号',
  `create_date` date NOT NULL COMMENT '日期',
  `subject_level` int(11) UNSIGNED NULL DEFAULT 0 COMMENT '科目级别',
  `balance_from` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '余额方向：debit-借方,credit-贷方',
  `today_balance` decimal(20, 2) NOT NULL DEFAULT 0.00 COMMENT '本日日终余额',
  `today_debit_amount` decimal(20, 2) NOT NULL DEFAULT 0.00 COMMENT '本日借方发生额',
  `today_credit_amount` decimal(20, 2) NOT NULL COMMENT '本日贷方发生额',
  `yesterday_amount` decimal(20, 2) NOT NULL COMMENT '昨日日终余额'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bill_subject_temp
-- ----------------------------
DROP TABLE IF EXISTS `bill_subject_temp`;
CREATE TABLE `bill_subject_temp`  (
  `id` bigint(20) UNSIGNED NOT NULL DEFAULT 0,
  `subject_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '科目编号',
  `subject_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '科目名称',
  `subject_level` int(11) NOT NULL DEFAULT 0 COMMENT '科目级别',
  `parent_subject_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上级科目内部编号',
  `subject_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '科目类型：1-资产类科目，2-负债类科目，3-所有者权益类科目，4-收入类科目，5-支出类科目，6-共同类科目',
  `balance_from` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '余额方向：debit-借方,credit-贷方',
  `add_balance_from` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '额增加借贷方向：debit-借方,credit-贷方',
  `debit_credit_flag` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参加借贷平衡检查标志：0-没参加，1-参加',
  `is_inner_account` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否开立内部账户：0-否，1-是',
  `inner_day_bal_flag` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '内部账日终修改余额标志：0-日间，1-日终',
  `inner_sum_flag` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '内部账汇总入明细标志：0-日间单笔，1-日终单笔，2-日终汇总',
  `creator` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updator` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `update_time` timestamp(0) NULL DEFAULT NULL,
  `subject_alias` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '科目别名'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bill_unique_seq
-- ----------------------------
DROP TABLE IF EXISTS `bill_unique_seq`;
CREATE TABLE `bill_unique_seq`  (
  `id` bigint(25) NOT NULL AUTO_INCREMENT,
  `create_time` timestamp(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2450 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '自增序列表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for blocked_ip
-- ----------------------------
DROP TABLE IF EXISTS `blocked_ip`;
CREATE TABLE `blocked_ip`  (
  `deny_ip` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '禁用ip',
  `deny_day` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '禁用日期',
  `deny_num` int(11) NULL DEFAULT NULL COMMENT '错误次数',
  `deny_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '禁用时间',
  PRIMARY KEY (`deny_ip`, `deny_day`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for business_account
-- ----------------------------
DROP TABLE IF EXISTS `business_account`;
CREATE TABLE `business_account`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `applicant` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '提交人',
  `applicant_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '提交时间',
  `approver` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '审批人',
  `approve_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '审批时间',
  `status` int(11) NULL DEFAULT 0 COMMENT '状态（0待提交，1待审批，2审批通过，3审批不通过，4已记账，5记账失败）',
  `record_fail_remark` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '记账失败原因',
  `remark` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `file_path` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上传excel模板路径',
  `approve_remark` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '审核备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '业务调账表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for business_account_detail
-- ----------------------------
DROP TABLE IF EXISTS `business_account_detail`;
CREATE TABLE `business_account_detail`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `business_id` int(11) NOT NULL,
  `trans_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易序号',
  `out_user_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '调出外部用户（商户、代理商、收单机构）编号',
  `in_user_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '调入外部用户（商户、代理商、收单机构）编号',
  `account_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '调账类型',
  `amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '交易金额',
  `reason` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '调账原因',
  `record_status` int(11) NULL DEFAULT 2 COMMENT '记账状态：2：未记账  1：记账成功  0：记账失败',
  `record_result` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '记账结果',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '业务调账详情表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for check_account_batch
-- ----------------------------
DROP TABLE IF EXISTS `check_account_batch`;
CREATE TABLE `check_account_batch`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `check_batch_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对账批次号',
  `acq_enname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构编号',
  `acq_cnname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构名称',
  `acq_total_amount` decimal(20, 2) NULL DEFAULT NULL COMMENT ' 收单机构交易总金额',
  `acq_total_items` int(10) NULL DEFAULT NULL COMMENT '收单机构交易总笔数',
  `acq_total_success_items` int(10) NULL DEFAULT NULL COMMENT '收单机构对账成功总笔数',
  `acq_total_failed_items` int(10) NULL DEFAULT NULL COMMENT '收单机构对账失败笔数',
  `total_amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '平台交易总金额',
  `total_items` int(10) NULL DEFAULT NULL COMMENT '平台交易总笔数',
  `total_success_items` int(10) NULL DEFAULT NULL COMMENT '平台对账成功总笔数',
  `total_failed_items` int(10) NULL DEFAULT NULL COMMENT '平台对账失败总笔数',
  `check_result` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对账结果',
  `check_file_date` timestamp(0) NULL DEFAULT NULL COMMENT '对账文件日期',
  `check_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '对账日期',
  `check_file_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对账文件名称',
  `operator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作员',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `record_status` int(11) NULL DEFAULT 2 COMMENT '记账状态    0：记账失败  1：记账成功  2：未记账',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '对账批次表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for check_account_detail
-- ----------------------------
DROP TABLE IF EXISTS `check_account_detail`;
CREATE TABLE `check_account_detail`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `acq_trans_serial_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构交易流水号',
  `acq_merchant_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构商户号',
  `acq_merchant_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构商户名称',
  `acq_terminal_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构终端号',
  `access_org_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接入机构编号',
  `access_org_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接入机构名称',
  `acq_batch_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构批次号',
  `acq_serial_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构流水号',
  `acq_account_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构账号',
  `acq_card_sequence_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构卡序列号',
  `acq_trans_time` timestamp(0) NULL DEFAULT NULL COMMENT '收单机构交易时间',
  `acq_reference_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构系统参考号',
  `acq_settle_date` timestamp(0) NULL DEFAULT NULL COMMENT '收单机构结算日期',
  `acq_trans_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构交易码',
  `acq_trans_status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构交易状态',
  `acq_trans_amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '收单机构交易金额',
  `acq_refund_amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '收单机构退货金额',
  `acq_check_date` timestamp(0) NULL DEFAULT NULL COMMENT '收单机构对账日期',
  `acq_ori_trans_serial_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构原交易流水号',
  `acq_enname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构编号',
  `plate_trans_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '平台交易id',
  `plate_acq_merchant_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '平台收单机构商户号',
  `plate_acq_terminal_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '平台收单机构终端号',
  `plate_merchant_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '平台商户号',
  `plate_terminal_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '平台终端号',
  `plate_acq_batch_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '平台收单机构批次号',
  `plate_acq_serial_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '平台收单机构流水号',
  `plate_batch_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '平台批次号',
  `plate_serial_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '平台流水号',
  `plate_account_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '平台交易账号',
  `plate_trans_amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '平台交易金额',
  `plate_acq_merchant_fee` decimal(20, 2) NULL DEFAULT NULL COMMENT '平台收单机构商户手续费',
  `plate_merchant_fee` decimal(20, 2) NULL DEFAULT NULL COMMENT '平台商户手续费',
  `plate_agent_fee` decimal(20, 2) NULL DEFAULT NULL COMMENT '平台交易代理商成本',
  `plate_acq_merchant_rate` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单商户扣率',
  `plate_merchant_rate` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ' 商户扣率',
  `plate_acq_reference_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '平台收单机构系统参考号',
  `plate_acq_trans_time` timestamp(0) NULL DEFAULT NULL COMMENT '平台收单机构交易时间',
  `plate_merchant_settle_date` timestamp(0) NULL DEFAULT NULL COMMENT '商户结算周期',
  `plate_trans_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '平台交易类型',
  `plate_trans_status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '平台交易状态',
  `check_account_status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对账状态',
  `check_batch_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对账批次号',
  `mend_batch_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '补单批次号',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对账描述',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `plate_agent_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '平台代理商用户ID',
  `plate_trans_source` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `my_settle` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否优质商户',
  `bag_settle` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '是否钱包，0否1是',
  `pos_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备类型',
  `error_handle_status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对账差错处理状态',
  `error_msg` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '失败原因',
  `plate_agent_share_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '代理商分润金额',
  `record_status` int(11) NULL DEFAULT 2 COMMENT '存疑记账状态    0：记账失败  1：记账成功  2：未记账',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `settlement_method` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '结算方式 0 t0 ,1 t1',
  `settle_status` int(1) NOT NULL DEFAULT 0 COMMENT '结算状态1：已结算  0或空：未结算   2.结算中   3.结算失败  4.转T1结算',
  `account` int(11) NULL DEFAULT 0 COMMENT '交易记账0:未记账，1:记账成功，2:记账失败',
  `error_handle_creator` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '差错处理人',
  `acq_merchant_order_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单商户订单号',
  `acq_order_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构订单号',
  `acq_trans_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构交易类型',
  `acq_trans_order_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构交易订单号',
  `order_reference_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单参考号',
  `settle_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '出款类型',
  `plate_order_time` timestamp(0) NULL DEFAULT NULL COMMENT '平台订单时间',
  `plate_order_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '平台订单号',
  `plate_card_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '平台交易卡号',
  `acq_auth_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构授权码',
  `out_account_bill_method` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '出账方式',
  `is_add_bill` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '是否加入出账单 1-已加入，0-未加入',
  `freeze_status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '冻结标识 0为未冻结,1为风控冻结,2活动冻结,3财务冻结',
  `out_bill_status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '确认出账状态 0:未出账,1:出账,2:已关闭',
  `plate_merchant_entry_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '平台商户进件编号',
  `db_cut_flag` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'None' COMMENT '单边日切标识None:无ShortCashAuto：短款自动匹配MoreCashAuto：长款自动匹配',
  `treatment_method` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '处理方式0：未处理1：预冻结2：冻结3：标记',
  `check_remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '初审备注',
  `error_time` timestamp(0) NULL DEFAULT NULL COMMENT '差错处理时间',
  `error_check_creator` varchar(225) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '初审人',
  `deduction_fee` decimal(30, 2) NULL DEFAULT NULL COMMENT '抵扣交易手续费',
  `actual_fee` decimal(30, 2) NULL DEFAULT NULL COMMENT '实际交易手续费',
  `coupon_nos` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '抵扣券编号',
  `pay_method` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易方式',
  `check_no` varchar(225) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '初审编号',
  `check_status` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '差错初审状态0：待初审1：已初审',
  `quick_rate` decimal(30, 2) NULL DEFAULT 0.00 COMMENT '云闪付金额',
  `quick_fee` decimal(30, 2) NULL DEFAULT 0.00 COMMENT '云闪付费率',
  `merchant_price` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '自选商户手续费',
  `deduction_mer_fee` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '抵扣自选商户手续费',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `order_reference_no`(`order_reference_no`) USING BTREE,
  INDEX `check_batch_no`(`check_batch_no`) USING BTREE,
  INDEX `plate_trans_id`(`plate_trans_id`) USING BTREE,
  INDEX `ind_detail_order_no`(`plate_order_no`) USING BTREE,
  INDEX `ind_arno_asno`(`plate_acq_reference_no`, `plate_acq_serial_no`) USING BTREE,
  INDEX `plate_time`(`plate_acq_trans_time`) USING BTREE,
  INDEX `ied_create_time`(`create_time`) USING BTREE,
  INDEX `ind_acq_reference`(`acq_reference_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 969 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '对账明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for core_trans_info
-- ----------------------------
DROP TABLE IF EXISTS `core_trans_info`;
CREATE TABLE `core_trans_info`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `account_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账号',
  `trans_amount` decimal(20, 2) NOT NULL DEFAULT 0.00 COMMENT '交易金额',
  `serial_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '记账流水号',
  `child_serial_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '子记账流水号',
  `journal_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分录号',
  `subject_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '科目内部编号',
  `currency_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '币种号',
  `trans_date` date NULL DEFAULT NULL COMMENT '交易日期',
  `reverse_flag` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '冲销标志:NORMAL正常，REVERSED-已冲销）',
  `account_flag` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '内部账外部账标志:0-外部账号，1-内部账号',
  `debit_credit_flag` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '借贷平衡检查标志:0-不平衡，1-平衡',
  `debit_credit_side` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '借贷方向:debit-借方,credit-贷方',
  `summary_info` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '摘要',
  `account_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '外部账户类型:A-代理商,M-商户,Acq-商户',
  `user_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '外部用户ID:消费者ID或商户ID或代理商ID等支付机构外部用户ID',
  `org_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '机构号',
  `trans_order_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易订单号',
  `import_id` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `account_no_key`(`account_no`) USING BTREE,
  INDEX `trans_date_key`(`trans_date`) USING BTREE,
  INDEX `serial_no_key`(`serial_no`) USING BTREE,
  INDEX `cserial_no_key`(`child_serial_no`) USING BTREE,
  INDEX `im_id`(`import_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 25263 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '记账流水表（会计分录表）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for currency_info
-- ----------------------------
DROP TABLE IF EXISTS `currency_info`;
CREATE TABLE `currency_info`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `currency_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '币种号',
  `currency_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '币种名称',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `currency_no`(`currency_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '币种信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ext_account_info
-- ----------------------------
DROP TABLE IF EXISTS `ext_account_info`;
CREATE TABLE `ext_account_info`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `account_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '外部用户账户账号',
  `account_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '外部账用户类型:A-代理商,M-商户,C-收单机构',
  `user_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '外部用户ID:消费者ID或商户ID或代理商ID等支付机构外部用户ID',
  `account_owner` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账户归属:支付机构号或商户号',
  `card_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '卡号',
  `subject_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '科目内部编号',
  `currency_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '币种号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `account_no`(`account_no`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2385 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '外部用户账户关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ext_account_op_record
-- ----------------------------
DROP TABLE IF EXISTS `ext_account_op_record`;
CREATE TABLE `ext_account_op_record`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `account_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '帐号',
  `record_date` date NULL DEFAULT NULL COMMENT '记账日期',
  `serial_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '记账流水号',
  `operation_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作类型:0-解冻，1-冻结',
  `operation_balance` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '操作金额',
  `trans_order_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易订单号',
  `summary_info` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '摘要',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `account_no_UNIQUE`(`account_no`, `serial_no`, `record_date`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 133 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '外部账户冻结解冻记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ext_nobooked_detail
-- ----------------------------
DROP TABLE IF EXISTS `ext_nobooked_detail`;
CREATE TABLE `ext_nobooked_detail`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `account_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '外部账号',
  `record_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '记账金额',
  `serial_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '记账流水号',
  `child_serial_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '子记账流水号',
  `record_date` date NULL DEFAULT NULL COMMENT '记账日期',
  `debit_credit_side` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '借贷方向:debit-借方,credit-贷方',
  `booked_flag` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '入账标志:0-未入账，1-入账',
  `summary_info` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '摘要',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `account_no_UNIQUE`(`account_no`, `serial_no`, `child_serial_no`, `record_date`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '外部账未入账流水表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ext_trans_info
-- ----------------------------
DROP TABLE IF EXISTS `ext_trans_info`;
CREATE TABLE `ext_trans_info`  (
  `id` bigint(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `account_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '外部账号',
  `record_amount` decimal(20, 2) NOT NULL DEFAULT 0.00 COMMENT '记账金额',
  `balance` decimal(20, 2) NOT NULL DEFAULT 0.00 COMMENT '余额',
  `avali_balance` decimal(20, 2) NOT NULL DEFAULT 0.00 COMMENT '可用余额',
  `control_amount` decimal(20, 2) NOT NULL DEFAULT 0.00 COMMENT '控制金额',
  `settling_amount` decimal(20, 2) NOT NULL DEFAULT 0.00 COMMENT '结算中金额',
  `pre_freeze_amount` decimal(20, 2) NOT NULL DEFAULT 0.00 COMMENT '预冻结金额',
  `serial_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '记账流水号',
  `child_serial_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '子交易流水号',
  `record_date` date NULL DEFAULT NULL COMMENT '记账日期',
  `record_time` time(0) NULL DEFAULT NULL COMMENT '记账时间',
  `debit_credit_side` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '借贷方向:debit-借方,credit-贷方',
  `summary_info` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '摘要',
  `trans_order_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易订单号',
  `trans_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易类型',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `account_no_key`(`account_no`) USING BTREE,
  INDEX `record_date_ind`(`record_date`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16108 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '外部用户账户交易明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for fast_check_account_batch
-- ----------------------------
DROP TABLE IF EXISTS `fast_check_account_batch`;
CREATE TABLE `fast_check_account_batch`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `check_batch_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对账批次号',
  `acq_enname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构编号',
  `acq_total_amount` decimal(20, 2) NULL DEFAULT NULL COMMENT ' 收单机构交易总金额',
  `acq_total_items` int(10) NULL DEFAULT NULL COMMENT '收单机构交易总笔数',
  `acq_total_success_items` int(10) NULL DEFAULT NULL COMMENT '收单机构对账成功总笔数',
  `acq_total_failed_items` int(10) NULL DEFAULT NULL COMMENT '收单机构对账失败笔数',
  `total_amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '平台交易总金额',
  `total_items` int(10) NULL DEFAULT NULL COMMENT '平台交易总笔数',
  `total_success_items` int(10) NULL DEFAULT NULL COMMENT '平台对账成功总笔数',
  `total_failed_items` int(10) NULL DEFAULT NULL COMMENT '平台对账失败总笔数',
  `check_result` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对账结果',
  `check_file_date` timestamp(0) NULL DEFAULT NULL COMMENT '对账文件日期',
  `check_time` timestamp(0) NULL DEFAULT NULL COMMENT '对账日期',
  `check_file_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对账文件名称',
  `operator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作员',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `record_status` int(11) NULL DEFAULT 2,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for fast_check_account_detail
-- ----------------------------
DROP TABLE IF EXISTS `fast_check_account_detail`;
CREATE TABLE `fast_check_account_detail`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `check_batch_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对账批次号',
  `acq_order_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构订单号',
  `acq_trans_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易类型',
  `acq_trans_order_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构交易订单号',
  `acq_order_time` timestamp(0) NULL DEFAULT NULL COMMENT '收单机构交易时间',
  `acq_order_status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构订单状态',
  `acq_trans_amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '收单机构交易金额',
  `acq_refund_amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '收单机构佣金金额',
  `acq_check_date` timestamp(0) NULL DEFAULT NULL COMMENT '收单机构对账日期',
  `acq_enname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构英文名称',
  `plate_order_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '平台订单号',
  `plate_agent_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '平台代理商编号',
  `plate_merchant_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '平台商户号',
  `plate_trans_amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '平台交易金额',
  `plate_acq_merchant_fee` decimal(20, 2) NULL DEFAULT NULL COMMENT '平台收单机构商户手续费',
  `plate_merchant_fee` decimal(20, 2) NULL DEFAULT NULL COMMENT '平台商户手续费',
  `plate_agent_fee` decimal(20, 2) NULL DEFAULT NULL COMMENT '平台代理商手续费',
  `plate_order_time` timestamp(0) NULL DEFAULT NULL COMMENT '平台交易时间',
  `plate_trans_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '平台交易类型',
  `plate_trans_status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '平台交易状态',
  `plate_trans_id` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '平台交易id',
  `check_account_status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对账状态',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `record_status` int(11) NULL DEFAULT 2,
  `error_handle_status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对账差错处理状态 \r\naccountField记账失败 ,checkForzen系统自动对账冻结 ,thawSettle解冻结算  ,platformPayment平台单边赔付商户  ,upstreamDoubt上游单边记存疑问  ,upstreamRecordAccount上游单边补记账结算商户 ,pendingTreatment待处理  ,upstreamRefund上游单边退款给持卡人 ,financialRefund财务退款',
  `error_msg` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '差错原因',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `settlement_method` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '结算方式 0 t0 ,1 t1',
  `settle_status` int(1) NOT NULL DEFAULT 0 COMMENT '结算状态1：已结算  0或空：未结算   2.结算中   3.结算失败  4.转T1结算',
  `account` int(11) NULL DEFAULT 0 COMMENT '0:未记账，1:记账成功，2:记账失败',
  `error_handle_creator` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '差错处理人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for generic_table
-- ----------------------------
DROP TABLE IF EXISTS `generic_table`;
CREATE TABLE `generic_table`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `table_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '表名',
  `primary_key` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '当前主键值',
  `increment` int(11) NOT NULL DEFAULT 1 COMMENT '增量值',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `table_name`(`table_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '自定义主键策略' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for high_account_trade
-- ----------------------------
DROP TABLE IF EXISTS `high_account_trade`;
CREATE TABLE `high_account_trade`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '外部账号',
  `amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '交易金额',
  `order_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单号',
  `batch_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '批次号',
  `status` int(1) NULL DEFAULT 0 COMMENT '处理状态 0：未处理 1：已处理',
  `trade_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '借贷类型 debit：借方  credit：贷方',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `trans_info_id` bigint(20) NULL DEFAULT NULL COMMENT '交易流水id',
  `account_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账户类型：ins:内部账户 ext:外部账户',
  `source` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据来源：冲正 交易 出款',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_account`(`account_no`, `trade_type`, `account_type`) USING BTREE,
  INDEX `index_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15670 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '高频账户交易表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for hlf_agent_debt_record
-- ----------------------------
DROP TABLE IF EXISTS `hlf_agent_debt_record`;
CREATE TABLE `hlf_agent_debt_record`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `agent_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '代理商编号',
  `agent_level` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商级别',
  `agent_node` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商节点',
  `parent_agent_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上级代理商编号',
  `one_agent_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '一级代理商编号',
  `adjust_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '累计待扣款金额',
  `debt_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '实际扣除金额',
  `should_debt_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '应扣除金额',
  `order_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单编号',
  `debt_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '欠款时间',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `merchant_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户编号',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `ind_agent_no`(`agent_no`) USING BTREE,
  INDEX `ind_parent_agent_no`(`parent_agent_no`) USING BTREE,
  INDEX `ind_debt_time`(`debt_time`) USING BTREE,
  INDEX `ind_order_no`(`order_no`) USING BTREE,
  INDEX `ind_one_agent_no`(`one_agent_no`) USING BTREE,
  INDEX `ind_merchant_no`(`merchant_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 328 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '欢乐返代理商欠款流水表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for inside_nobooked_detail
-- ----------------------------
DROP TABLE IF EXISTS `inside_nobooked_detail`;
CREATE TABLE `inside_nobooked_detail`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `account_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '内部账号',
  `trans_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '交易金额',
  `serial_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易流水号',
  `child_serial_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '子交易流水号',
  `trans_date` date NULL DEFAULT NULL COMMENT '交易日期',
  `debit_credit_side` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '借贷方向:debit-借方,credit-贷方',
  `booked_flag` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '入账标志:0-未入账，1-入账',
  `summary_info` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '摘要',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '内部账未入账流水表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for inside_trans_info
-- ----------------------------
DROP TABLE IF EXISTS `inside_trans_info`;
CREATE TABLE `inside_trans_info`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `account_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '内部账号',
  `record_amount` decimal(20, 2) NOT NULL DEFAULT 0.00 COMMENT '记账金额',
  `balance` decimal(20, 2) NOT NULL DEFAULT 0.00 COMMENT '余额',
  `avali_balance` decimal(20, 2) NOT NULL DEFAULT 0.00 COMMENT '可用余额',
  `serial_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '记账流水号',
  `child_serial_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '记账子流水号',
  `record_date` date NULL DEFAULT NULL COMMENT '记账日期',
  `record_time` time(0) NULL DEFAULT NULL COMMENT '记账时间',
  `debit_credit_side` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '借贷方向:debit-借方,credit-贷方',
  `summary_info` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '摘要',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `account_no_key`(`account_no`) USING BTREE,
  INDEX `trans_date_key`(`record_date`) USING BTREE,
  INDEX `serial_no_key`(`serial_no`) USING BTREE,
  INDEX `cserial_no_key`(`child_serial_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9156 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '内部账交易明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for login_user
-- ----------------------------
DROP TABLE IF EXISTS `login_user`;
CREATE TABLE `login_user`  (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '登录用户名',
  `login_pwd` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '登录密码密文',
  `status` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户状态 0停用  1启用',
  `real_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '真实姓名',
  `role_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关联角色编码',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录者邮箱',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录者电话',
  `error_num` int(5) NOT NULL DEFAULT 0 COMMENT '一次登录密码错误次数',
  `last_login_time` timestamp(0) NULL DEFAULT NULL COMMENT '最近一次登录时间',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username_ind`(`user_name`) USING BTREE,
  UNIQUE INDEX `realname_ind`(`real_name`, `role_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '登录用户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for opt_logs
-- ----------------------------
DROP TABLE IF EXISTS `opt_logs`;
CREATE TABLE `opt_logs`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `out_bill_id` bigint(20) NULL DEFAULT NULL COMMENT '出账单ID',
  `check_batch_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对账批次号',
  `log_msg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作日志信息',
  `log_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '日志类型',
  `operator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作员',
  `operate_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '操作日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for org_info
-- ----------------------------
DROP TABLE IF EXISTS `org_info`;
CREATE TABLE `org_info`  (
  `id` bigint(20) NOT NULL,
  `org_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '机构号',
  `org_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '机构名称',
  UNIQUE INDEX `org_no`(`org_no`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '机构组织表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for out_account_task
-- ----------------------------
DROP TABLE IF EXISTS `out_account_task`;
CREATE TABLE `out_account_task`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `trans_time` timestamp(0) NULL DEFAULT NULL COMMENT '交易时间',
  `trans_amount` decimal(20, 2) NULL DEFAULT NULL,
  `up_balance` decimal(20, 2) NULL DEFAULT NULL COMMENT '历史余额',
  `up_today_balance` decimal(20, 2) NULL DEFAULT NULL COMMENT '上游当日余额',
  `out_account_task_amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '出账任务金额',
  `up_company_count` int(10) NULL DEFAULT NULL COMMENT '上游个数',
  `out_account_id` bigint(20) NULL DEFAULT NULL COMMENT '出账单id',
  `sys_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '系统计算时间',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `bill_status` int(11) NULL DEFAULT 0 COMMENT '出账单状态  0：未生成   1：已生成',
  `acq_enname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上游名称',
  `creator` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `updator` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` timestamp(0) NULL DEFAULT NULL COMMENT '修改时间',
  `out_bill_range` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'No' COMMENT '出账范围 ：No 不限,T1 t+1,Tn t+n',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `out_account_id_ind`(`out_account_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '出账任务表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for out_account_task_detail
-- ----------------------------
DROP TABLE IF EXISTS `out_account_task_detail`;
CREATE TABLE `out_account_task_detail`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `out_account_task_id` bigint(20) NULL DEFAULT NULL COMMENT '出账任务ID',
  `acq_org_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构ID',
  `today_amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '当日交易金额',
  `up_balance` decimal(20, 2) NULL DEFAULT NULL COMMENT '上游余额',
  `today_balance` decimal(20, 2) NULL DEFAULT NULL COMMENT '当日余额',
  `out_account_amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '出账金额',
  `sys_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '系统计算时间',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '出账任务明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for out_bill
-- ----------------------------
DROP TABLE IF EXISTS `out_bill`;
CREATE TABLE `out_bill`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `out_account_task_id` bigint(20) NULL DEFAULT NULL,
  `out_account_task_amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '出账任务金额',
  `calc_out_amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '计算出账金额',
  `balance_up_count` int(20) NULL DEFAULT NULL COMMENT '有余额上游数量',
  `balance_merchant_count` int(20) NULL DEFAULT NULL COMMENT '有余额商户数量',
  `out_bill_status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '确认出账状态 0:未出账,1:出账,2:已关闭',
  `sys_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '系统计算时间',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `account_owner` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '机构组织',
  `acq_enname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上游名称',
  `file_name` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '出账单导出文件名',
  `creator` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `updator` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` timestamp(0) NULL DEFAULT NULL COMMENT '修改时间',
  `back_operator` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '回盘人',
  `back_time` timestamp(0) NULL DEFAULT NULL COMMENT '回盘时间',
  `out_account_bill_method` int(11) NULL DEFAULT NULL COMMENT '出账方式',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `create_time_ind`(`create_time`) USING BTREE,
  INDEX `out_account_task_id_ind`(`out_account_task_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '出账单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for out_bill_detail
-- ----------------------------
DROP TABLE IF EXISTS `out_bill_detail`;
CREATE TABLE `out_bill_detail`  (
  `id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '出账单明细编号',
  `out_bill_id` bigint(20) NULL DEFAULT NULL COMMENT '出账单ID',
  `merchant_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户编号',
  `merchant_balance` decimal(20, 2) NULL DEFAULT NULL COMMENT '商户余额',
  `out_account_task_amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '出账任务金额',
  `out_amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '实际出账金额',
  `acq_org_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构编号',
  `today_balance` decimal(20, 2) NULL DEFAULT NULL COMMENT '当日余额',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `change_remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `change_operator_id` bigint(20) NULL DEFAULT NULL,
  `change_operator_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `out_bill_result` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `export_status` int(11) NULL DEFAULT 0 COMMENT '导出状态, 0:未处理，1：已处理，2：成功，3：失败',
  `export_file_serial` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '导出文件序列号,eg:001,002',
  `export_date` date NULL DEFAULT NULL COMMENT '出账日期',
  `verify_flag` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '0 未校验 1校验通过 2校验失败 ',
  `verify_msg` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '校验错误信息 ',
  `record_status` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '记账状态;SUCCESS 记账成功,FAILURE 记账失败,NORCORD 未记账,CHONGZHENGED 已冲正',
  `out_bill_status` int(11) NULL DEFAULT 0 COMMENT '出款状态  0：未出款   1：出款成功   2：出款失败 3:已提交出款系统',
  `plate_merchant_entry_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '平台进件编号',
  `acq_merchant_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银联报备编号',
  `trans_time` timestamp(0) NULL DEFAULT NULL COMMENT '交易时间',
  `order_reference_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单参考号',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `out_bill_id_ind`(`out_bill_id`) USING BTREE,
  INDEX `export_date_ind`(`export_date`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '出账单明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for out_channel_ladder_rate_rebalance
-- ----------------------------
DROP TABLE IF EXISTS `out_channel_ladder_rate_rebalance`;
CREATE TABLE `out_channel_ladder_rate_rebalance`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `out_acq_enname` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '出款服务通道英文名',
  `out_service_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '出账服务编号',
  `re_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '还差类型',
  `re_year` int(20) NULL DEFAULT NULL COMMENT '年份',
  `re_month` int(20) NULL DEFAULT NULL COMMENT '月份',
  `total_out_amount_month` decimal(20, 2) NULL DEFAULT NULL COMMENT '月累计出款服务总额',
  `total_avg_day_out_amount_month` decimal(20, 2) NULL DEFAULT NULL COMMENT '该月日均累计出款服务总额',
  `out_amount_month_fee` decimal(20, 2) NULL DEFAULT NULL COMMENT '该月出款费用',
  `rebalance` decimal(20, 2) NULL DEFAULT NULL COMMENT '应还差金额',
  `real_rebalance` decimal(20, 2) NULL DEFAULT NULL COMMENT '实际还差金额',
  `record_status` int(255) NULL DEFAULT NULL COMMENT '记账状态：  1记账成功，0记账失败，2未记账(中间状态)）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '出款通道阶梯费率返差' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission`  (
  `id` int(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `permission_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `permission_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_permission_code`(`permission_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 104 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for record_account_rule
-- ----------------------------
DROP TABLE IF EXISTS `record_account_rule`;
CREATE TABLE `record_account_rule`  (
  `rule_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `rule_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '记账规则号',
  `rule_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '记账规则名称',
  `program` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '记账程序',
  `remark` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '说明',
  `creator` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updator` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` timestamp(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`rule_id`) USING BTREE,
  UNIQUE INDEX `record_account_rule_no_UNIQUE`(`rule_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 190 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '记账规则定义表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for record_account_rule_config
-- ----------------------------
DROP TABLE IF EXISTS `record_account_rule_config`;
CREATE TABLE `record_account_rule_config`  (
  `rule_config_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `rule_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '记账规则号',
  `journal_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分录号',
  `child_trans_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '子交易号',
  `account_flag` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '内部账外部账标志:0-外部账号，1内部账号',
  `debit_credit_side` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '借贷方向:debit-借方,credit-贷方',
  `debit_credit_flag` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '借贷平衡检查标志:false-否，true-是',
  `subject_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '科目号',
  `remark` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '说明',
  `account_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '外部账用户类型',
  `currency_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '币种号',
  `amount` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '金额',
  PRIMARY KEY (`rule_config_id`) USING BTREE,
  UNIQUE INDEX `rule_no_UNIQUE`(`rule_no`, `journal_no`, `child_trans_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 935 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '记账规则配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for record_account_rule_trans_type
-- ----------------------------
DROP TABLE IF EXISTS `record_account_rule_trans_type`;
CREATE TABLE `record_account_rule_trans_type`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID(交易类型与记账规则定义表ID)',
  `trans_type_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易类型编码',
  `trans_type_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易类型名称',
  `from_system` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '来源系统',
  `rule_id` bigint(20) NULL DEFAULT NULL COMMENT '记账规则id',
  `remark` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '说明',
  `creator` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updator` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` timestamp(0) NULL DEFAULT NULL COMMENT '修改时间',
  `trans_group` varchar(600) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易分组',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `trans_type_UNIQUE`(`trans_type_code`, `from_system`) USING BTREE,
  UNIQUE INDEX `trans_type_code`(`trans_type_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 166 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '交易类型与记账规则定义表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `role_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `parent_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_role_code`(`role_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for role_permission_relation
-- ----------------------------
DROP TABLE IF EXISTS `role_permission_relation`;
CREATE TABLE `role_permission_relation`  (
  `id` int(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `role_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `permission_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_role_permission`(`role_code`, `permission_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1281 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for role_rigth
-- ----------------------------
DROP TABLE IF EXISTS `role_rigth`;
CREATE TABLE `role_rigth`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `role_id` int(11) NULL DEFAULT NULL COMMENT '角色id',
  `rigth_id` int(11) NULL DEFAULT NULL COMMENT '权限id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10919 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for service_info
-- ----------------------------
DROP TABLE IF EXISTS `service_info`;
CREATE TABLE `service_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `service_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '服务代码',
  `service_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '服务名称',
  `service_path` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '服务地址',
  `service_remark` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '服务备注',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `warn_mobile` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '服务对应统一预警人员手机号,英文逗号分隔',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `code_ind`(`service_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '服务（系统）信息列表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for settle_batch
-- ----------------------------
DROP TABLE IF EXISTS `settle_batch`;
CREATE TABLE `settle_batch`  (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `settle_batch_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '结算批次号',
  `check_batch_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对账批次号',
  `total_trans_amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '结算总交易金额',
  `total_merchant_fee` decimal(20, 2) NULL DEFAULT NULL COMMENT '结算总手续费金额',
  `total_acq_refound_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '大商户手续费',
  `total_acq_merchant_fee` decimal(20, 2) NULL DEFAULT NULL,
  `total_settle_amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '总结算金额',
  `total_acq_settle_amount` decimal(20, 2) NULL DEFAULT NULL,
  `total_items` int(10) NULL DEFAULT NULL COMMENT '总笔数',
  `settle_time` timestamp(0) NULL DEFAULT NULL COMMENT '结算时间',
  `settle_file_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '结算文件名称',
  `file_create_time` timestamp(0) NULL DEFAULT NULL,
  `file_create_operator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `operator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作员',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `acq_enname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `acq_enname_channel` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `supply_amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '补单金额',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '结算批次表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for settle_transfer
-- ----------------------------
DROP TABLE IF EXISTS `settle_transfer`;
CREATE TABLE `settle_transfer`  (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `batch_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '批量凭证号',
  `file_id` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `seq_no` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '序号',
  `file_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `settle_bank` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '结算机构',
  `in_acc_no` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `in_acc_name` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `in_settle_bank_no` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `in_bank_no` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `in_bank_name` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `amount` decimal(20, 2) NOT NULL COMMENT '付款金额(元)',
  `create_time` datetime(0) NULL,
  `status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '状态  0.未提交 1.已提交 2.提交失败 3.超时 ',
  `err_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '错误码',
  `err_msg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '错误信息',
  `bak1` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备用1',
  `bak2` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备用2',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for settle_transfer_file
-- ----------------------------
DROP TABLE IF EXISTS `settle_transfer_file`;
CREATE TABLE `settle_transfer_file`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `file_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `file_md5` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `operator_id` int(10) NULL DEFAULT NULL,
  `operator_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `transfer_operator_id` int(10) NULL DEFAULT NULL,
  `transfer_operator_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `total_num` int(10) NOT NULL COMMENT '总笔数',
  `total_amount` decimal(20, 2) NOT NULL COMMENT '总金额 单位元',
  `summary` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '摘要',
  `create_time` datetime(0) NULL,
  `transfer_time` datetime(0) NULL DEFAULT NULL,
  `settle_bank` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '结算机构',
  `out_acc_no` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `out_acc_name` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `out_bank_no` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `out_bank_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `out_settle_bank_no` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '出款清算行号',
  `status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '状态  0.未提交 1.已提交 2.提交失败 3.超时',
  `err_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '错误码',
  `err_msg` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '错误信息',
  `bak1` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备用1',
  `bak2` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备用2',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for shiro_rigth
-- ----------------------------
DROP TABLE IF EXISTS `shiro_rigth`;
CREATE TABLE `shiro_rigth`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `rigth_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限编码',
  `rigth_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限名称',
  `rigth_comment` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限说明',
  `rigth_type` int(11) NULL DEFAULT NULL COMMENT '权限类型',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1747 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for shiro_role
-- ----------------------------
DROP TABLE IF EXISTS `shiro_role`;
CREATE TABLE `shiro_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `role_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色编号',
  `role_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  `role_remake` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色说明',
  `role_state` int(11) NULL DEFAULT NULL COMMENT '角色状态',
  `create_operator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 71 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for shiro_user
-- ----------------------------
DROP TABLE IF EXISTS `shiro_user`;
CREATE TABLE `shiro_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户唯一标识',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户编码',
  `real_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `tel_no` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电话',
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `state` int(11) NULL DEFAULT NULL COMMENT '用户状态',
  `theme` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户样式主题',
  `create_operator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_pwd_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改密码时间',
  `dept_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 225 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sub_out_bill_detail
-- ----------------------------
DROP TABLE IF EXISTS `sub_out_bill_detail`;
CREATE TABLE `sub_out_bill_detail`  (
  `id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '子出账单明细编号',
  `out_bill_detail_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '出账单明细编号',
  `out_bill_id` bigint(20) NULL DEFAULT NULL COMMENT '出账单ID',
  `trans_time` timestamp(0) NULL DEFAULT NULL COMMENT '交易时间',
  `trans_amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '交易金额',
  `order_reference_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单参考号',
  `out_account_note` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '出账备注',
  `record_status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '记账状态',
  `out_bill_status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '出账结果',
  `verify_flag` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '校验通过',
  `verify_msg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '校验错误信息',
  `acq_enname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构',
  `merchant_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户编号',
  `merchant_balance` decimal(20, 2) NULL DEFAULT NULL COMMENT '商户结算中金额',
  `out_account_task_amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '商户出账金额',
  `is_add_bill` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '是否加入出账单 1-已加入，0-未加入',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `acq_org_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构编号',
  `change_remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '财务调整备注',
  `change_operator_id` bigint(20) NULL DEFAULT NULL COMMENT '操作人id',
  `change_operator_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作人',
  `settle_time` timestamp(0) NULL DEFAULT NULL COMMENT '结算时间',
  `plate_merchant_entry_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '平台商户进件编号',
  `acq_merchant_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构商户号',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `ind_sub_detail_id`(`out_bill_detail_id`) USING BTREE,
  INDEX `ind_out_merchant`(`out_bill_id`, `merchant_no`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '子出账单明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sub_out_bill_detail_logs
-- ----------------------------
DROP TABLE IF EXISTS `sub_out_bill_detail_logs`;
CREATE TABLE `sub_out_bill_detail_logs`  (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `sub_out_bill_detail_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '子出账单明细',
  `out_bill_detail_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '出账单明细编号',
  `out_bill_id` bigint(20) NULL DEFAULT NULL COMMENT '出账单ID',
  `trans_time` timestamp(0) NULL DEFAULT NULL COMMENT '交易时间',
  `trans_amount` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易金额',
  `order_reference_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单参考号',
  `out_account_note` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '出账备注',
  `record_status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '记账状态',
  `out_bill_status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '出账结果',
  `verify_flag` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '校验通过',
  `verify_msg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '校验错误信息',
  `acq_enname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构',
  `merchant_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户编号',
  `merchant_balance` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户结算中金额',
  `out_account_task_amount` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户出账金额',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `acq_org_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构编号',
  `change_remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '财务调整备注',
  `change_operator_id` bigint(20) NULL DEFAULT NULL COMMENT '操作人id',
  `change_operator_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作人',
  `settle_time` timestamp(0) NULL DEFAULT NULL COMMENT '结算时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '子出账单明细日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for super_push_share_day_settle
-- ----------------------------
DROP TABLE IF EXISTS `super_push_share_day_settle`;
CREATE TABLE `super_push_share_day_settle`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `collection_batch_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分润汇总批次号',
  `group_time` timestamp(0) NULL DEFAULT NULL COMMENT '汇总时间',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '分润创建时间',
  `share_type` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '0: 代理商, 1: 商户',
  `share_no` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分润的商户/代理商编号',
  `share_name` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分润的商户/代理商名称',
  `share_total_amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '分润总金额',
  `share_total_num` int(11) NULL DEFAULT NULL COMMENT '分润总笔数',
  `enter_account_status` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '入账状态(NOENTERACCOUNT未入账,ENTERACCOUNTED已入账)',
  `enter_account_time` timestamp(0) NULL DEFAULT NULL COMMENT '入账时间',
  `enter_account_message` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '入账信息',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '超级推分润日结表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `param_key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `param_value` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `key_ind`(`param_key`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dept_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `sys_key` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '键值类型',
  `sys_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `html_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'html名称',
  `sys_value` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '值',
  `order_no` int(11) NULL DEFAULT NULL COMMENT '排序号',
  `status` int(11) NULL DEFAULT 1 COMMENT '状态 0-无效,1-有效',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 345 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统字典表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_logs
-- ----------------------------
DROP TABLE IF EXISTS `sys_logs`;
CREATE TABLE `sys_logs`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `type` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `method` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `args` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `ip` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_operator` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统操作日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `parent_id` int(11) NULL DEFAULT NULL,
  `menu_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `menu_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `menu_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `rigth_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `menu_level` int(11) NULL DEFAULT NULL,
  `menu_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `order_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `icons` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 589 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for system_info
-- ----------------------------
DROP TABLE IF EXISTS `system_info`;
CREATE TABLE `system_info`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '系统状态:N-日间运行状态normal，C-日切运行状态cutoff, A-追帐运行状态append, S-系统关闭shutdown',
  `current_date` date NULL DEFAULT NULL COMMENT '当前交易日期',
  `parent_trans_date` date NULL DEFAULT NULL COMMENT '上一个交易日期',
  `next_trans_date` date NULL DEFAULT NULL COMMENT '下一个交易日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统状态信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for temp_agent_info
-- ----------------------------
DROP TABLE IF EXISTS `temp_agent_info`;
CREATE TABLE `temp_agent_info`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `agent_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '代理商编号',
  `agent_node` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商节点',
  `agent_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商名称',
  `agent_level` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商级别',
  `parent_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上级代理商ID',
  `one_level_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '一级代理商ID',
  `is_oem` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否OEM',
  `team_id` bigint(20) NULL DEFAULT NULL COMMENT '组织ID',
  `email` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `cluster` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '归属集群',
  `invest` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否投资',
  `agent_area` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理区域',
  `mobilephone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `link_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商联系人',
  `invest_amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '投资金额',
  `address` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商地址',
  `account_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '开户名',
  `account_type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账户类型',
  `account_no` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '开户账户',
  `bank_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '开户行全称',
  `cnaps_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联行行号',
  `sale_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '销售人员（谁拓展的代理商）',
  `creator` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `mender` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `last_update_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后更新时间',
  `status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态：正常-1，关闭进件-0，冻结-2',
  `create_date` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `public_qrcode` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公众号二维码URL',
  `manager_logo` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '管理系统LOGO',
  `logo_remark` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `client_logo` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户端LOGO',
  `custom_tel` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客服电话',
  `is_approve` int(1) NULL DEFAULT 0 COMMENT '一级代理商对商户进件进行初审:0-否，1-是',
  `count_level` int(10) NULL DEFAULT NULL COMMENT '代理商层级链条长度限制:-1:不限',
  `has_account` int(2) NOT NULL DEFAULT 0 COMMENT '是否已有账号：1已开，0否开',
  `province` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '省',
  `city` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '市',
  `area` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '区',
  `sub_bank` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '支行',
  `account_province` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '开户行地区：省',
  `account_city` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '开户行地区：市',
  `source_sys` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `source_reference_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `source_reference_no2` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `profit_switch` int(11) NOT NULL DEFAULT 0 COMMENT '分润开关 1-打开, 0-关闭',
  `promotion_switch` int(11) NOT NULL DEFAULT 0 COMMENT '代理商推广开关 1-打开, 0-关闭',
  `cash_back_switch` int(11) NOT NULL DEFAULT 0 COMMENT '返现开关 1-打开, 0-关闭',
  `agent_oem` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属品牌',
  `agent_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '101' COMMENT '代理商类型',
  `agent_share_level` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易分润等级最高可调级数',
  `id_card_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '身份证号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `agent_id_UNIQUE`(`agent_no`) USING BTREE,
  UNIQUE INDEX `mobile_UNIQUE`(`mobilephone`, `team_id`) USING BTREE,
  INDEX `ind_srno`(`source_reference_no`) USING BTREE,
  INDEX `ind_agent_node`(`agent_node`) USING BTREE,
  INDEX `ind_agent_name`(`agent_name`) USING BTREE,
  INDEX `ind_email`(`email`) USING BTREE,
  INDEX `ind_parent_id`(`parent_id`) USING BTREE,
  INDEX `one_level_id`(`one_level_id`) USING BTREE,
  INDEX `sale_name`(`sale_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '代理商信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for temp_merchant_info
-- ----------------------------
DROP TABLE IF EXISTS `temp_merchant_info`;
CREATE TABLE `temp_merchant_info`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `merchant_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户ID',
  `merchant_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户名称',
  `merchant_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户类型:1-个人，2-个体商户，3-企业商户',
  `lawyer` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '法人姓名',
  `business_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '经营范围-商户类别：\r\n餐娱类；\r\n批发类；\r\n民生类；\r\n一般类；\r\n房车类；\r\n其他；',
  `industry_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '行业类型',
  `id_card_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '法人身份证号',
  `province` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '经营地址（省）',
  `city` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '经营地址（市）',
  `district` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '经营地址（区）',
  `address` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '经营地址:详细地址',
  `mobilephone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `email` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'Email',
  `operator` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务人员',
  `agent_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商ID',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '商户状态：0：商户关闭；1：正常；2 冻结',
  `parent_node` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商节点',
  `sale_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '销售人员（谁拓展的商户）',
  `creator` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `mender` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `last_update_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后更新时间',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `one_agent_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '一级代理商编号',
  `team_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组织ID',
  `mer_account` int(2) NULL DEFAULT 0 COMMENT '商户是否已在账户(1为是0为否)',
  `register_source` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户注册来源： 1：商户APP 2：代理商APP 3：代理商WEB',
  `risk_status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '冻结状态字段：1 正常，2 只进不出，3 不进不出',
  `recommended_source` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '推广来源(0:正常,1:为超级推，2：代理商推荐的商户，3人人代理推)',
  `source_sys` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `source_reference_no` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `push_flag` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易推送开关(1：推送,2:不推送)',
  `voice_flag` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '语音开关(1：开,2:关)',
  `pre_frozen_amount` decimal(30, 2) NULL DEFAULT 0.00 COMMENT '预冻结金额',
  `bonus_flag` int(4) NULL DEFAULT NULL COMMENT '鼓励金标识 0没有 1有',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `merchant_id_UNIQUE`(`merchant_no`) USING BTREE,
  UNIQUE INDEX `mobilephone`(`mobilephone`, `team_id`) USING BTREE,
  INDEX `id_card_no`(`id_card_no`, `team_id`) USING BTREE,
  INDEX `create_time`(`create_time`) USING BTREE,
  INDEX `parent_node`(`parent_node`) USING BTREE,
  INDEX `ind_merchant_name`(`merchant_name`) USING BTREE,
  INDEX `index_lawyer`(`lawyer`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for temp_v2_bill_cutdown
-- ----------------------------
DROP TABLE IF EXISTS `temp_v2_bill_cutdown`;
CREATE TABLE `temp_v2_bill_cutdown`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `data_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `object_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `object_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `remark2` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `remark3` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for time_task_record
-- ----------------------------
DROP TABLE IF EXISTS `time_task_record`;
CREATE TABLE `time_task_record`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `running_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '运行编号',
  `running_status` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '运行状态',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_update_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `running_no`(`running_no`) USING BTREE,
  INDEX `running_no_indx`(`running_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4843 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for trans_import_info
-- ----------------------------
DROP TABLE IF EXISTS `trans_import_info`;
CREATE TABLE `trans_import_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '交易流水导入表ID',
  `from_system` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '来源系统',
  `from_serial_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '来源系统流水号:钱包交易流水或刷卡交易流水',
  `from_date` timestamp(0) NULL DEFAULT NULL COMMENT '来源系统日期',
  `record_date` timestamp(0) NULL DEFAULT NULL COMMENT '账户账务系统记账日期',
  `trans_date` timestamp(0) NULL DEFAULT NULL COMMENT 'V1系统日期时间',
  `record_serial_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '记账流水号',
  `trans_amount` decimal(30, 2) NULL DEFAULT NULL COMMENT '交易金额',
  `trans_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易类型',
  `merchant_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号',
  `card_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易卡号',
  `terminal_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '终端号',
  `card_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '卡片类型',
  `acq_merchant_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单商户号',
  `reverse_status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '冲销状态：NORMAL正常，REVERSED-已冲销',
  `reverse_flag` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '冲销交易标志:NORMAL正常 ,REVERSE 冲销',
  `direct_agent_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户直属代理商',
  `device_sn` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '机具号',
  `record_status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '已记账标（1记账成功，0记账失败）',
  `merchant_fee` decimal(20, 2) NULL DEFAULT NULL COMMENT '商户服务手续费',
  `bp_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务产品ID',
  `agent_share_amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '各级代理商分润金额（代理商节点－分润金额）',
  `merchant_rate_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户费率类型:1-每笔固定金额，2-扣率，3-扣率带保底封顶，4-扣率+固定金额,5-单笔阶梯扣率 6-每月阶梯扣率',
  `merchant_rate` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `acq_service_rate` decimal(20, 2) NULL DEFAULT NULL COMMENT '收单服务扣率',
  `holidays_mark` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '节假日标志:1-工作日，2-节假日',
  `service_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '服务ID',
  `hp_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '硬件产品ID',
  `acq_enname` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构英文名称',
  `acq_service_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单（出款）服务ID',
  `acq_rate_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构服务费率类型1（费率类型:1-每笔固定金额，2-扣率，3-扣率带保底封顶，4-扣率+固定金额,5-单笔阶梯扣率6-每月阶梯扣率）',
  `acq_rate_type2` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构服务费率类型2',
  `acq_org_fee1` decimal(20, 2) NULL DEFAULT NULL COMMENT '收单机构手续费1',
  `acq_org_fee2` decimal(20, 2) NULL DEFAULT NULL COMMENT '收单机构手续费2',
  `one_agent_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '一级代理商编号',
  `out_service_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '出账服务编号',
  `reverse_serial_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '原交易系统流水号',
  `reverse_trans_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '要冲正的交易类型',
  `merchant_settle_fee` decimal(20, 2) NULL DEFAULT NULL COMMENT '商户提现手续费',
  `agent_settle_share_amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '代理商提现分润金额',
  `out_rate_type1` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '出款服务费率类型1',
  `out_rate_fee1` decimal(20, 2) NULL DEFAULT NULL COMMENT '出款服务费1',
  `out_rate_type2` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '出款服务费率类型2',
  `out_rate_fee2` decimal(20, 2) NULL DEFAULT NULL COMMENT '出款服务费2',
  `out_acq_enname` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '出款服务通道英文名',
  `out_rate1` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '出款服务扣率1',
  `out_rate2` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '出款服务扣率2',
  `trans_order_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易订单号',
  `acq_reference_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易参考号',
  `acq_rate2` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单服务扣率2',
  `acq_rate` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单服务扣率',
  `record_result` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '记账结果',
  `record_flag` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '记账标记 NORMAL正常记账 ,REVERSE 补记账',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `serial_no_UNIQUE`(`from_serial_no`, `record_date`, `record_serial_no`) USING BTREE,
  INDEX `record_date`(`record_date`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13094 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'V1交易（刷卡，提现等涉及到资金变动的）流水导入表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for trans_short_info
-- ----------------------------
DROP TABLE IF EXISTS `trans_short_info`;
CREATE TABLE `trans_short_info`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `plate_order_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '平台订单号',
  `trans_time` timestamp(0) NULL DEFAULT NULL COMMENT '交易时间',
  `hardware_product` int(10) NULL DEFAULT NULL COMMENT '硬件产品id',
  `business_product_id` int(10) NULL DEFAULT NULL COMMENT '业务产品id',
  `service_id` int(10) NULL DEFAULT NULL COMMENT '服务ID',
  `card_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易卡种（借记卡，贷记卡，未知）',
  `merchant_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '商户编号',
  `merchant_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户名称',
  `one_agent_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '代理商编号',
  `one_agent_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商名称',
  `agent_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商编号',
  `agent_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商名称',
  `agent_node` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商节点',
  `agent_level` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商级别',
  `parent_agent_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '父级代理商编号',
  `trans_amount` decimal(30, 2) NULL DEFAULT NULL COMMENT '交易金额',
  `merchant_rate` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户扣率',
  `merchant_fee` decimal(30, 2) NULL DEFAULT NULL COMMENT '商户手续费',
  `acq_org_id` int(10) NULL DEFAULT NULL COMMENT '收单机构id',
  `acq_enname` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构英文名称',
  `acq_out_cost` decimal(20, 2) NULL DEFAULT NULL COMMENT '收单服务成本',
  `agent_share_amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '交易代理商分润',
  `mer_cash_fee` decimal(20, 2) NULL DEFAULT NULL COMMENT '商户提现手续费',
  `cash_agent_share_amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '提现代理商分润',
  `dai_cost` decimal(20, 2) NULL DEFAULT NULL COMMENT '代付成本',
  `dian_cost` decimal(20, 2) NULL DEFAULT NULL COMMENT '垫资成本',
  `sale_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属销售',
  `acq_out_profit` decimal(20, 2) NULL DEFAULT NULL COMMENT '收单收益',
  `hardware_product_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '硬件产品名称',
  `business_product_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务产品名称',
  `service_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '服务名称',
  `agent_profit_collection_status` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商分润汇总(COLLECTED 已汇总,NOCOLLECT 未汇总)',
  `collection_batch_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分润汇总批次号',
  `agent_profit_group_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '分润汇总时间',
  `deduction_fee` decimal(30, 2) NULL DEFAULT NULL COMMENT '抵扣提现手续费',
  `profits_20` decimal(30, 2) NULL DEFAULT NULL,
  `profits_1` decimal(30, 2) NULL DEFAULT NULL COMMENT '一级分润',
  `profits_2` decimal(30, 2) NULL DEFAULT NULL,
  `profits_3` decimal(30, 2) NULL DEFAULT NULL,
  `profits_4` decimal(30, 2) NULL DEFAULT NULL,
  `profits_5` decimal(30, 2) NULL DEFAULT NULL,
  `profits_6` decimal(30, 2) NULL DEFAULT NULL,
  `profits_7` decimal(30, 2) NULL DEFAULT NULL,
  `profits_8` decimal(30, 2) NULL DEFAULT NULL,
  `profits_9` decimal(30, 2) NULL DEFAULT NULL,
  `profits_10` decimal(30, 2) NULL DEFAULT NULL,
  `profits_11` decimal(30, 2) NULL DEFAULT NULL,
  `profits_12` decimal(30, 2) NULL DEFAULT NULL,
  `profits_13` decimal(30, 2) NULL DEFAULT NULL,
  `profits_14` decimal(30, 2) NULL DEFAULT NULL,
  `profits_15` decimal(30, 2) NULL DEFAULT NULL,
  `profits_16` decimal(30, 2) NULL DEFAULT NULL,
  `profits_17` decimal(30, 2) NULL DEFAULT NULL,
  `profits_18` decimal(30, 2) NULL DEFAULT NULL,
  `profits_19` decimal(30, 2) NULL DEFAULT NULL,
  `settle_profits_1` decimal(30, 2) NULL DEFAULT NULL COMMENT '一级提现分润',
  `settle_profits_2` decimal(30, 2) NULL DEFAULT NULL,
  `settle_profits_3` decimal(30, 2) NULL DEFAULT NULL,
  `settle_profits_4` decimal(30, 2) NULL DEFAULT NULL,
  `settle_profits_5` decimal(30, 2) NULL DEFAULT NULL,
  `settle_profits_6` decimal(30, 2) NULL DEFAULT NULL,
  `settle_profits_7` decimal(30, 2) NULL DEFAULT NULL,
  `settle_profits_8` decimal(30, 2) NULL DEFAULT NULL,
  `settle_profits_9` decimal(30, 2) NULL DEFAULT NULL,
  `settle_profits_10` decimal(30, 2) NULL DEFAULT NULL,
  `settle_profits_11` decimal(30, 2) NULL DEFAULT NULL,
  `settle_profits_12` decimal(30, 2) NULL DEFAULT NULL,
  `settle_profits_13` decimal(30, 2) NULL DEFAULT NULL,
  `settle_profits_14` decimal(30, 2) NULL DEFAULT NULL,
  `settle_profits_15` decimal(30, 2) NULL DEFAULT NULL,
  `settle_profits_16` decimal(30, 2) NULL DEFAULT NULL,
  `settle_profits_17` decimal(30, 2) NULL DEFAULT NULL,
  `settle_profits_18` decimal(30, 2) NULL DEFAULT NULL,
  `settle_profits_19` decimal(30, 2) NULL DEFAULT NULL,
  `settle_profits_20` decimal(30, 2) NULL DEFAULT NULL,
  `trans_deduction_fee` decimal(30, 2) NULL DEFAULT 0.00 COMMENT '抵扣交易商户手续费',
  `actual_fee` decimal(30, 2) NULL DEFAULT 0.00 COMMENT '实际交易手续费',
  `merchant_price` decimal(30, 2) NULL DEFAULT 0.00 COMMENT '自选商户手续费',
  `deduction_mer_fee` decimal(30, 2) NULL DEFAULT 0.00 COMMENT '抵扣自选商户手续费',
  `actual_optional_fee` decimal(30, 2) NULL DEFAULT 0.00 COMMENT '实际自选商户手续费',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `plate_order_no`(`plate_order_no`) USING BTREE,
  INDEX `index_agent_no`(`agent_no`) USING BTREE,
  INDEX `index_agent_node`(`agent_node`) USING BTREE,
  INDEX `ind_pagent_no`(`parent_agent_no`) USING BTREE,
  INDEX `ind_trans_time`(`trans_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 944 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '交易部分信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for unify_seq
-- ----------------------------
DROP TABLE IF EXISTS `unify_seq`;
CREATE TABLE `unify_seq`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `table_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '表名称',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '统一自增序列表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `age` int(11) NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `creattime` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 47 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_permission_extra
-- ----------------------------
DROP TABLE IF EXISTS `user_permission_extra`;
CREATE TABLE `user_permission_extra`  (
  `user_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名称',
  `permission_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '权限编码',
  PRIMARY KEY (`user_name`, `permission_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '附加权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_rigth
-- ----------------------------
DROP TABLE IF EXISTS `user_rigth`;
CREATE TABLE `user_rigth`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户ID',
  `rigth_id` int(11) NULL DEFAULT NULL COMMENT '权限ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11197 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户ID',
  `role_id` int(11) NULL DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1018 CHARACTER SET = latin2 COLLATE = latin2_general_ci COMMENT = '用户角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_setting
-- ----------------------------
DROP TABLE IF EXISTS `user_setting`;
CREATE TABLE `user_setting`  (
  `user_id` int(1) NOT NULL,
  `back_last_page` int(11) NULL DEFAULT NULL,
  `collapse_menu` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for zf_bill_ext_account
-- ----------------------------
DROP TABLE IF EXISTS `zf_bill_ext_account`;
CREATE TABLE `zf_bill_ext_account`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `account_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '外部账号',
  `account_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账户名称',
  `org_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '机构号',
  `currency_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '币种号',
  `subject_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '科目内部编号',
  `curr_balance` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '余额',
  `control_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '冻结金额',
  `settling_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '结算中金额',
  `settling_hold_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '结算保留中金额',
  `pre_freeze_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '预冻结金额',
  `parent_trans_day` date NULL DEFAULT NULL COMMENT '上一个交易日',
  `parent_trans_balance` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '上一个交易余额',
  `account_status` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账户状态:1-正常,2-销户,3-冻结只进不出,4-冻结不进不出',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '开户日期时间',
  `balance_add_from` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '余额增加记借方还是贷方：debit-借方,credit-贷方',
  `balance_from` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '余额方向：debit-借方,credit-贷方',
  `day_bal_flag` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '日终修改余额标志:0-日间，1-日终',
  `sum_flag` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '汇总入明细标志:0-日间单笔，1-日终单笔，2-日终汇总',
  `creator` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `account_no`(`account_no`) USING BTREE,
  INDEX `subject_no`(`subject_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '备份中付外部用户账户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for zf_core_trans_info
-- ----------------------------
DROP TABLE IF EXISTS `zf_core_trans_info`;
CREATE TABLE `zf_core_trans_info`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `account_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账号',
  `trans_amount` decimal(20, 2) NOT NULL DEFAULT 0.00 COMMENT '交易金额',
  `serial_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '记账流水号',
  `child_serial_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '子记账流水号',
  `journal_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分录号',
  `subject_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '科目内部编号',
  `currency_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '币种号',
  `trans_date` date NULL DEFAULT NULL COMMENT '交易日期',
  `reverse_flag` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '冲销标志:NORMAL正常，REVERSED-已冲销）',
  `account_flag` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '内部账外部账标志:0-外部账号，1-内部账号',
  `debit_credit_flag` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '借贷平衡检查标志:0-不平衡，1-平衡',
  `debit_credit_side` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '借贷方向:debit-借方,credit-贷方',
  `summary_info` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '摘要',
  `account_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '外部账户类型:A-代理商,M-商户,Acq-商户',
  `user_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '外部用户ID:消费者ID或商户ID或代理商ID等支付机构外部用户ID',
  `org_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '机构号',
  `trans_order_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易订单号',
  `import_id` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `account_no_key`(`account_no`) USING BTREE,
  INDEX `trans_date_key`(`trans_date`) USING BTREE,
  INDEX `serial_no_key`(`serial_no`) USING BTREE,
  INDEX `cserial_no_key`(`child_serial_no`) USING BTREE,
  INDEX `im_id`(`import_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '记账流水表（会计分录表）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for zf_ext_account_info
-- ----------------------------
DROP TABLE IF EXISTS `zf_ext_account_info`;
CREATE TABLE `zf_ext_account_info`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `account_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '外部用户账户账号',
  `account_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '外部账用户类型:A-代理商,M-商户,C-收单机构',
  `user_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '外部用户ID:消费者ID或商户ID或代理商ID等支付机构外部用户ID',
  `account_owner` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账户归属:支付机构号或商户号',
  `card_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '卡号',
  `subject_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '科目内部编号',
  `currency_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '币种号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `account_no`(`account_no`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '备份中付外部用户账户关系表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
