/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.1.183-dev
 Source Server Type    : MySQL
 Source Server Version : 50722
 Source Host           : 192.168.1.183:5567
 Source Schema         : ys_nposp

 Target Server Type    : MySQL
 Target Server Version : 50722
 File Encoding         : 65001

 Date: 22/10/2019 09:18:54
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for 11
-- ----------------------------
DROP TABLE IF EXISTS `11`;
CREATE TABLE `11`  (
  `id` bigint(20) NOT NULL,
  `order_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单号',
  `old_settle_status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '变更前的状态 0:未结算 1:已结算 2:结算中 3:结算失败 4:转T1结算 5:不结算',
  `curr_settle_status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '变更后的状态 0:未结算 1:已结算 2:结算中 3:结算失败 4:转T1结算 5:不结算',
  `operate_person` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作人',
  `operate_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '操作时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `order_id_index`(`order_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for aa
-- ----------------------------
DROP TABLE IF EXISTS `aa`;
CREATE TABLE `aa`  (
  `df` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `qq` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for acp_bear_bank
-- ----------------------------
DROP TABLE IF EXISTS `acp_bear_bank`;
CREATE TABLE `acp_bear_bank`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `bank_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银行名称',
  `acq_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '通道',
  `bank_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银行卡bin',
  `type` int(2) NULL DEFAULT 0 COMMENT '0不支持1支持',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1222 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '通道支持银行' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for acp_whitelist
-- ----------------------------
DROP TABLE IF EXISTS `acp_whitelist`;
CREATE TABLE `acp_whitelist`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '收单机构白名单ID',
  `merchant_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户编号',
  `acq_id` int(11) NOT NULL COMMENT '收单机构编号',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `create_person` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 28 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '收单机构白名单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for acq_merchant
-- ----------------------------
DROP TABLE IF EXISTS `acq_merchant`;
CREATE TABLE `acq_merchant`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `acq_org_id` int(11) NULL DEFAULT NULL COMMENT '收单机构ID',
  `acq_service_id` int(11) NULL DEFAULT NULL COMMENT '收单服务ID',
  `acq_merchant_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构商户号',
  `acq_merchant_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构商户名称',
  `merchant_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单商户对应的普通商户',
  `agent_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属代理商',
  `mcc` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '行业信息码',
  `large_small_flag` int(2) NULL DEFAULT NULL COMMENT '大套小标志(是否A类)',
  `rate_type` int(2) NULL DEFAULT NULL COMMENT '费率类型:1-每笔固定金额，2-扣率，3-扣率带保底封顶，4-扣率+固定金额,5-单笔阶梯 扣率 6-每月阶梯扣率',
  `single_amount` decimal(16, 2) NULL DEFAULT NULL COMMENT '单笔固定金额',
  `rate` decimal(10, 6) NULL DEFAULT NULL COMMENT '单笔扣率',
  `capping` decimal(10, 2) NULL DEFAULT NULL COMMENT '封顶金额',
  `ladder_rate` decimal(10, 6) NULL DEFAULT NULL COMMENT '单笔阶梯扣率',
  `ladder_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '阶梯金额',
  `quota` decimal(20, 2) NULL DEFAULT NULL COMMENT '限额',
  `quota_status` int(2) NULL DEFAULT 2 COMMENT '额度状态 1.是(已超额） 2.否',
  `locked` int(11) NULL DEFAULT 0 COMMENT '0正常,1锁定,2废弃',
  `locked_msg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `locked_time` timestamp(0) NULL DEFAULT NULL,
  `rep_pay` int(11) NULL DEFAULT 1 COMMENT '1.否  2.是',
  `over_quota` int(11) NULL DEFAULT 0 COMMENT '路由集群快钱和夏门民生是否已超额。1:已超额，0：未超额',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_person` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建人',
  `acq_status` int(1) NULL DEFAULT 1 COMMENT '状态  0关闭  1开通',
  `merchant_service_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `day_quota` decimal(20, 2) NULL DEFAULT NULL COMMENT '日交易总额',
  `acq_merchant_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单商户类型',
  `price` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '自选商户的价格',
  `acq_merchant_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构对应收单商户进件编号',
  `special` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '1 特殊商户 0 非特殊商户',
  `source` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '来源途径,数据字典acqMerSource值对应',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UNIQUE`(`acq_merchant_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1676 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for acq_merchant_copy1
-- ----------------------------
DROP TABLE IF EXISTS `acq_merchant_copy1`;
CREATE TABLE `acq_merchant_copy1`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `acq_org_id` int(11) NULL DEFAULT NULL COMMENT '收单机构ID',
  `acq_service_id` int(11) NULL DEFAULT NULL COMMENT '收单服务ID',
  `acq_merchant_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构商户号',
  `acq_merchant_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构商户名称',
  `merchant_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单商户对应的普通商户',
  `agent_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属代理商',
  `mcc` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '行业信息码',
  `large_small_flag` int(2) NULL DEFAULT NULL COMMENT '大套小标志(是否A类)',
  `rate_type` int(2) NULL DEFAULT NULL COMMENT '费率类型:1-每笔固定金额，2-扣率，3-扣率带保底封顶，4-扣率+固定金额,5-单笔阶梯 扣率 6-每月阶梯扣率',
  `single_amount` decimal(16, 2) NULL DEFAULT NULL COMMENT '单笔固定金额',
  `rate` decimal(10, 6) NULL DEFAULT NULL COMMENT '单笔扣率',
  `capping` decimal(10, 2) NULL DEFAULT NULL COMMENT '封顶金额',
  `ladder_rate` decimal(10, 6) NULL DEFAULT NULL COMMENT '单笔阶梯扣率',
  `ladder_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '阶梯金额',
  `quota` decimal(20, 2) NULL DEFAULT NULL COMMENT '限额',
  `quota_status` int(2) NULL DEFAULT 2 COMMENT '额度状态 1.是(已超额） 2.否',
  `locked` int(11) NULL DEFAULT 0 COMMENT '0正常,1锁定,2废弃',
  `locked_msg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `locked_time` timestamp(0) NULL DEFAULT NULL,
  `rep_pay` int(11) NULL DEFAULT 1 COMMENT '1.否  2.是',
  `over_quota` int(11) NULL DEFAULT 0 COMMENT '路由集群快钱和夏门民生是否已超额。1:已超额，0：未超额',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_person` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建人',
  `acq_status` int(1) NULL DEFAULT 1 COMMENT '状态  0关闭  1开通',
  `merchant_service_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `day_quota` decimal(20, 2) NULL DEFAULT NULL COMMENT '日交易总额',
  `acq_merchant_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单商户类型',
  `price` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '自选商户的价格',
  `acq_merchant_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构对应收单商户进件编号',
  `special` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '1 特殊商户 0 非特殊商户',
  `source` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '来源途径,数据字典acqMerSource值对应',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UNIQUE`(`acq_merchant_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1676 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for acq_merchant_file_info
-- ----------------------------
DROP TABLE IF EXISTS `acq_merchant_file_info`;
CREATE TABLE `acq_merchant_file_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `file_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件类型 进件项ID',
  `file_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件地址',
  `status` int(11) NOT NULL DEFAULT 1 COMMENT '文件状态 1 正常 2失效',
  `acq_into_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '进件编号',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `acq_into_no_file_index`(`acq_into_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2446 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '收单商户进件资质文件表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for acq_merchant_info
-- ----------------------------
DROP TABLE IF EXISTS `acq_merchant_info`;
CREATE TABLE `acq_merchant_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `merchant_type` int(11) NOT NULL COMMENT '进件类型:1个体收单商户，2-企业收单商户',
  `merchant_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户名称',
  `legal_person` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '法人姓名',
  `legal_person_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '法人身份证号',
  `id_valid_start` datetime(0) NOT NULL COMMENT '身份证有效期开始时间',
  `id_valid_end` datetime(0) NOT NULL COMMENT '身份证有效期结束时间',
  `province` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '经营地址(省)',
  `city` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '经营地址（市）',
  `district` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '经营地址（区）',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '详细地址',
  `one_scope` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '一级经营范围',
  `two_scope` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '二级经营范围',
  `charter_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '营业执照名称',
  `charter_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '营业执照编号',
  `charter_valid_start` datetime(0) NOT NULL COMMENT '营业执照有效开始时间',
  `charter_valid_end` datetime(0) NOT NULL COMMENT '营业执照有效期结束时间',
  `account_type` int(11) NOT NULL COMMENT '账户类型 1 对私 2对公',
  `bank_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '银行卡号',
  `account_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '开户名',
  `account_bank` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '开户银行',
  `account_province` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '开户地区（省）',
  `account_city` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '开户地区（市）',
  `account_district` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '开户地区（区）',
  `bank_branch` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '支行',
  `line_number` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '联行号',
  `acq_into_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '进件编号',
  `into_source` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '进件来源 1盛代宝 2 移通宝 3点付秘书4银惠宝5web代理商后台',
  `audit_status` int(11) NOT NULL COMMENT '审核状态 1.正常 2.审核通过 3 审核不通过',
  `audit_time` datetime(0) NULL DEFAULT NULL COMMENT '审核时间',
  `create_time` datetime(0) NOT NULL COMMENT '进件时间',
  `agent_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '所属代理商',
  `one_agent_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '所属一级代理商',
  `mcc` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'MCC码',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `acq_into_no_index`(`acq_into_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 257 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '收单商户进件基本信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for acq_merchant_info_log
-- ----------------------------
DROP TABLE IF EXISTS `acq_merchant_info_log`;
CREATE TABLE `acq_merchant_info_log`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `acq_merchant_info_id` bigint(20) NOT NULL COMMENT '收单商户进件基本信息表id',
  `audit_status` int(11) NOT NULL COMMENT '审核状态 1.待审核 2.审核通过 3 审核不通过',
  `examination_opinions` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '审核意见',
  `operator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作员',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '审核时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 491 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '收单商户进件审核记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for acq_operate_info
-- ----------------------------
DROP TABLE IF EXISTS `acq_operate_info`;
CREATE TABLE `acq_operate_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `unionpay_mer_no` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银联号',
  `business_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务类型',
  `request_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求流水号',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上游返回的code(0为成功，非0失败)',
  `msg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上游返回msg',
  `status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上游返回的状态',
  `last_update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `unionpay_type_index`(`unionpay_mer_no`, `business_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '修改操作记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for acq_org
-- ----------------------------
DROP TABLE IF EXISTS `acq_org`;
CREATE TABLE `acq_org`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `acq_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收单机构名称',
  `acq_enname` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收单机构英文简称',
  `host` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收单机构主机地址',
  `port` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收单机构端口',
  `LMK_OMK` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'LMK下的机构主密钥',
  `LMK_OMK_CV` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'LMK下的机构主密钥校验值',
  `LMK_OPK` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'LMK下的Pin密钥',
  `LMK_OPK_CV` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'LMK下的Pin密钥校验值',
  `LMK_OAK` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'LMK下的Mac密钥',
  `LMK_OAK_CV` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'LMK下的Mac密钥校验值',
  `WORK_KEY` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构工作秘钥',
  `acq_status` int(2) NOT NULL COMMENT '收单机构状态 0.关闭 1.开通',
  `settle_type` int(2) NULL DEFAULT NULL COMMENT '结算类型 1.代付',
  `day_altered_time` time(0) NULL DEFAULT NULL COMMENT '日切时间点',
  `settle_account_id` int(11) NULL DEFAULT NULL COMMENT '分润结算账户ID',
  `acq_trans_have_out` int(2) NULL DEFAULT NULL COMMENT '本收单机构的交易只能在本机构出款 1.是',
  `realtime_T0greaterT1` int(2) NULL DEFAULT NULL COMMENT '允许实时T0大于T1 1.否 2.是',
  `acq_success_amount` decimal(12, 2) NULL DEFAULT NULL COMMENT '收单服务日成功交易总额阀值',
  `phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '负责人手机号',
  `acq_def_dayAmount` decimal(12, 2) NULL DEFAULT NULL COMMENT '默认收单商户每日限额',
  `dayAmount_T0greaterT1` int(2) NULL DEFAULT NULL COMMENT '允许T0日额大于T1日额 1.否 2.是',
  `t0_advance_money` decimal(11, 2) NULL DEFAULT NULL COMMENT 'T0垫资额度 单位：元',
  `t0_own_money` decimal(11, 2) NULL DEFAULT NULL COMMENT 'T0自有额度 单位:元',
  `valves_amount` decimal(11, 2) NULL DEFAULT NULL COMMENT '冲量提醒阀值 单位：元',
  `t1_trans_amount` decimal(11, 2) NULL DEFAULT NULL COMMENT '当日T1交易总额',
  `t0_trans_advance_amount` decimal(11, 2) NULL DEFAULT NULL COMMENT '当日T0垫资交易总额',
  `t0_trans_own_amount` decimal(11, 2) NULL DEFAULT NULL COMMENT '当日T0自有交易额度',
  `close_type` int(2) NULL DEFAULT NULL COMMENT '关闭类型：1.全天 2.指定时段',
  `close_start_time` timestamp(0) NULL DEFAULT NULL COMMENT '收单机构指定关闭时段起始时间',
  `close_end_time` timestamp(0) NULL DEFAULT NULL COMMENT '收单机构指定关闭时段截止时间',
  `acq_close_tips` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构关闭提示语',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '收单机构创建时间',
  `create_person` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收单机构创建人',
  `channel_status` int(5) NULL DEFAULT 0 COMMENT '交易转直清状态，0直清 1集群',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `acq_name`(`acq_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 738 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '收单机构管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for acq_response_code
-- ----------------------------
DROP TABLE IF EXISTS `acq_response_code`;
CREATE TABLE `acq_response_code`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `acq_enname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构英文简称',
  `response_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '返回编码',
  `response_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作分类A：交易成功B：交易失败，可重试C：交易失败，不需要重试D：交易失败，终端操作员处理E：交易失败，系统故障，不需要重试',
  `response_text` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公司映射中文解释',
  `response_POS` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公司映射中文解释pos',
  `response_acq` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '通道编码中文解释',
  `reason` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '原因/采取的措施',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 636 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '交易返回错误代码映射表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for acq_service
-- ----------------------------
DROP TABLE IF EXISTS `acq_service`;
CREATE TABLE `acq_service`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '收单服务ID',
  `acq_id` int(11) NOT NULL COMMENT '收单机构ID',
  `acq_enname` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收单机构英文名',
  `service_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '服务类型 4-POS刷卡-标准类，9-支付宝扫码支付，10-微信扫码支付，11-快捷支付, 12-POS-刷卡优惠类',
  `service_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '服务名称',
  `fee_is_card` int(2) NOT NULL COMMENT '费率区分银行卡 1.是 2.否',
  `quota_is_card` int(2) NOT NULL COMMENT '限额区分银行卡 1.是 2.否',
  `bank_card_type` int(2) NOT NULL DEFAULT 0 COMMENT '可用银行卡 0.全部 1.仅信用卡 2.仅借记卡',
  `allow_trans_start_time` time(0) NULL DEFAULT NULL COMMENT '每日允许交易开始时间',
  `allow_trans_end_time` time(0) NULL DEFAULT NULL COMMENT '每日允许交易结束时间',
  `service_remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `service_status` int(2) NOT NULL COMMENT '服务状态 1.开启 0.关闭',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_person` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建人',
  `time_switch` int(1) NOT NULL DEFAULT 0 COMMENT '定时关闭开关 0关闭;1开启',
  `time_start_time` timestamp(0) NULL DEFAULT NULL COMMENT '定时开始时间',
  `time_end_time` timestamp(0) NULL DEFAULT NULL COMMENT '定时结束时间',
  `close_prompt` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '关闭提示语',
  `periodicity_start_time` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '周期性开始日期',
  `periodicity_end_time` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '周期性结束时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 244 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '收单服务表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for acq_service_rate
-- ----------------------------
DROP TABLE IF EXISTS `acq_service_rate`;
CREATE TABLE `acq_service_rate`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '收单服务费率ID',
  `acq_service_id` int(11) NOT NULL COMMENT '收单服务ID',
  `rate_type` int(2) NOT NULL COMMENT '费率类型:1-每笔固定金额，2-扣率，3-扣率带保底封顶，4-扣率+固定金额,5-单笔阶梯 扣率 6-每月阶梯扣率',
  `card_rate_type` int(2) NOT NULL COMMENT '卡种类型 0=服务费率(不区分银行卡种),2=储蓄卡费率,1=信用卡费率',
  `single_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '单笔固定金额',
  `rate` decimal(10, 6) NULL DEFAULT NULL COMMENT '单笔扣率',
  `capping` decimal(10, 6) NULL DEFAULT NULL COMMENT '封顶金额',
  `safe_line` decimal(10, 6) NULL DEFAULT NULL COMMENT '保底金额',
  `ladder1_rate` decimal(10, 6) NULL DEFAULT NULL COMMENT '阶梯区间1费率',
  `ladder1_max` decimal(20, 4) NULL DEFAULT NULL COMMENT '阶梯区间1上限',
  `ladder2_rate` decimal(10, 6) NULL DEFAULT NULL COMMENT '阶梯区间2费率',
  `ladder2_max` decimal(20, 4) NULL DEFAULT NULL COMMENT '阶梯区间2上限',
  `ladder3_rate` decimal(10, 6) NULL DEFAULT NULL COMMENT '阶梯区间3费率',
  `ladder3_max` decimal(20, 4) NULL DEFAULT NULL COMMENT '阶梯区间3上限',
  `ladder4_rate` decimal(10, 6) NULL DEFAULT NULL COMMENT '阶梯区间4费率',
  `ladder4_max` decimal(20, 4) NULL DEFAULT NULL COMMENT '阶梯区间4上限',
  `effective_status` int(2) NOT NULL DEFAULT 0 COMMENT '是否生效 1.是 2.否',
  `effective_date` datetime(0) NOT NULL COMMENT '生效日期',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_person` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 208 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '收单服务费率表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for acq_service_rate_task
-- ----------------------------
DROP TABLE IF EXISTS `acq_service_rate_task`;
CREATE TABLE `acq_service_rate_task`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '收单服务任务ID',
  `acq_service_rate_id` int(11) NOT NULL DEFAULT 0 COMMENT '收单服务费率ID',
  `rate_type` int(2) NOT NULL COMMENT '费率类型:1-每笔固定金额，2-扣率，3-扣率带保底封顶，4-扣率+固定金额,5-单笔阶梯 扣率 6-每月阶梯扣率',
  `card_rate_type` int(2) NOT NULL COMMENT '服务费率类型 0=服务费率(不区分银行卡种),2=储蓄卡费率,1=信用卡费率',
  `single_amount` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '单笔固定金额',
  `rate` decimal(10, 6) NULL DEFAULT 0.000000 COMMENT '单笔扣率',
  `capping` decimal(10, 6) NULL DEFAULT 0.000000 COMMENT '封顶金额',
  `safe_line` decimal(10, 6) NULL DEFAULT 0.000000 COMMENT '保底金额',
  `ladder1_rate` decimal(10, 6) NULL DEFAULT NULL COMMENT '阶梯区间1费率',
  `ladder1_max` decimal(20, 4) NULL DEFAULT NULL COMMENT '阶梯区间1上限',
  `ladder2_rate` decimal(10, 6) NULL DEFAULT NULL COMMENT '阶梯区间2费率',
  `ladder2_max` decimal(20, 4) NULL DEFAULT NULL COMMENT '阶梯区间2上限',
  `ladder3_rate` decimal(10, 6) NULL DEFAULT NULL COMMENT '阶梯区间3费率',
  `ladder3_max` decimal(20, 4) NULL DEFAULT NULL COMMENT '阶梯区间3上限',
  `ladder4_rate` decimal(10, 6) NULL DEFAULT NULL COMMENT '阶梯区间4费率',
  `ladder4_max` decimal(20, 4) NULL DEFAULT NULL COMMENT '阶梯区间4上限',
  `effective_status` int(2) NOT NULL DEFAULT 0 COMMENT '是否生效 1.是 2.否',
  `effective_date` timestamp(0) NOT NULL COMMENT '生效日期',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_person` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 245 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '收单服务费率任务表\r\n本表前期作为定时任务列表以及费率历史表使用\r\n1.定时任务每天凌晨开始检索\"生效日期为当天\"且\"状态为未生效\"的记录。并根据外键\"收单服务费率ID+服务费率类型\"以事务管理的方式更新收单服务费率表对应的记录，并将之前生效的记录由是改为否。\r\n2.历史费率界面根据外键查询出对应的历史记录\r\n' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for acq_service_trans_rules
-- ----------------------------
DROP TABLE IF EXISTS `acq_service_trans_rules`;
CREATE TABLE `acq_service_trans_rules`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '收单服务限额ID',
  `acq_service_id` int(11) NOT NULL COMMENT '收单服务ID',
  `bank_card_type` int(2) NOT NULL DEFAULT 0 COMMENT '可用银行卡 0.不限 2.仅借记卡 1.仅信用卡',
  `savings_single_min_amount` decimal(12, 2) NULL DEFAULT NULL COMMENT '储蓄卡单笔交易最小金额',
  `savings_single_max_amount` decimal(12, 2) NULL DEFAULT NULL COMMENT '储蓄卡单笔交易最大金额',
  `savings_day_total_amount` decimal(15, 2) NULL DEFAULT NULL COMMENT '储蓄卡日成功交易总额阀值',
  `credit_single_min_amount` decimal(12, 2) NULL DEFAULT NULL COMMENT '信用卡单笔交易最小金额',
  `credit_single_max_amount` decimal(12, 2) NULL DEFAULT NULL COMMENT '信用卡单笔交易最大金额',
  `credit_day_total_amount` decimal(15, 2) NULL DEFAULT NULL COMMENT '信用卡日成功交易总额阀值',
  `day_total_amount` decimal(15, 2) NOT NULL COMMENT '日成功交易总额阀值',
  `warning_phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '预警手机号',
  `trans_limit_min_amount` decimal(12, 2) NULL DEFAULT NULL COMMENT '单笔交易最小限额',
  `trans_limit_max_amount` decimal(12, 2) NULL DEFAULT NULL COMMENT '单笔交易最大限额',
  `clint_msg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户端提示语',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_person` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 140 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '收单服务限额表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for acq_terminal
-- ----------------------------
DROP TABLE IF EXISTS `acq_terminal`;
CREATE TABLE `acq_terminal`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `acq_org_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收单机构ID编号',
  `acq_merchant_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号',
  `acq_terminal_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '终端号',
  `batch_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '批次号',
  `serial_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银行终端流水号',
  `lmk_zmk` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '该终端LMK下的ZMK',
  `lmk_zmk_cv` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '该终端LMK下的ZMK校验值',
  `lmk_zpk` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '该终端LMK下的ZPK',
  `lmk_zpk_cv` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '该终端LMK下的ZPK校验值',
  `lmk_zak` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '该终端LMK下的ZAK',
  `lmk_zak_cv` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '该终端LMK下的ZAK校验值',
  `lmk_zdk` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `lmk_zdk_cv` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `work_key` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '工作密钥',
  `last_update_time` timestamp(0) NULL DEFAULT NULL COMMENT '最后签到时间',
  `last_used_time` timestamp(0) NULL DEFAULT NULL COMMENT '最后使用时间',
  `status` int(1) NULL DEFAULT 1 COMMENT '状态',
  `locked` int(11) NULL DEFAULT 0 COMMENT '0正常,1锁定,2废弃',
  `locked_msg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `locked_time` timestamp(0) NULL DEFAULT NULL,
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_person` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `acq_terminal_no`(`acq_terminal_no`) USING BTREE,
  INDEX `ind_acq_merchant_no`(`acq_merchant_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1046 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for acq_terminal_store
-- ----------------------------
DROP TABLE IF EXISTS `acq_terminal_store`;
CREATE TABLE `acq_terminal_store`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `acq_enname` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '机构英文名',
  `union_mer_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '报备商户号',
  `merchant_no` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '平台商户号',
  `ter_no` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '终端号',
  `tmk1` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'TMK1密文',
  `tmk1_ck` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'TMK1校验值',
  `tmk2` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'TMK2密文',
  `tmk2_ck` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'TMK2校验值',
  `status` int(11) NOT NULL DEFAULT 0 COMMENT '状态 0:已入库,1：已报备，2：已使用',
  `sn` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'mpos 编号',
  `mbp_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '进件ID',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP,
  `last_update_time` datetime(0) NULL DEFAULT NULL,
  `type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '1:真实,0:虚拟',
  `is_secret` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '是否走一机一密0否1是',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`, `acq_enname`) USING BTREE,
  INDEX `sn_ind`(`sn`) USING BTREE,
  INDEX `acq_status`(`acq_enname`, `status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 110351 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '报备机具与上游终端绑定' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for activity_config
-- ----------------------------
DROP TABLE IF EXISTS `activity_config`;
CREATE TABLE `activity_config`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `activity_code` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '活动代码(HLS)',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '活动开始时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '活动结束时间',
  `wait_day` int(11) NULL DEFAULT NULL COMMENT '等待期(天) ',
  `cash_service_id` int(11) NULL DEFAULT NULL COMMENT '活动提现服务id',
  `agent_service_id` int(11) NULL DEFAULT NULL COMMENT '代理商提现服务id',
  `cumulate_trans_day` int(3) NULL DEFAULT 0 COMMENT '累计交易天数（欢乐返）重复注册：累计交易奖励时间',
  `cumulate_amount_minus` decimal(13, 2) NULL DEFAULT 0.00 COMMENT '累计交易（扣）（欢乐返）',
  `cumulate_amount_add` decimal(13, 2) NULL DEFAULT 0.00 COMMENT '累计交易（奖）（欢乐返）',
  `cumulate_trans_minus_day` int(3) NULL DEFAULT 0 COMMENT '累计交易扣费时间（欢乐返）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '活动配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for activity_detail
-- ----------------------------
DROP TABLE IF EXISTS `activity_detail`;
CREATE TABLE `activity_detail`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `activity_code` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '活动代码(HLS)',
  `activity_id` int(11) NULL DEFAULT NULL COMMENT '活动编号',
  `merchant_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号',
  `agent_node` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商节点',
  `status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态(1:未激活、2:已激活、3:可提现、4:已提现、5:已过期、6:已返代理商、7:已扣分润账户、8:预调账已发起、9:已奖励)',
  `cash_order` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '提现订单',
  `active_order` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '激活订单',
  `active_time` datetime(0) NULL DEFAULT NULL COMMENT '激活时间',
  `cash_time` datetime(0) NULL DEFAULT NULL COMMENT '提现时间',
  `enter_time` datetime(0) NULL DEFAULT NULL COMMENT '进件时间',
  `frozen_amout` decimal(10, 2) NULL DEFAULT NULL COMMENT '冻结金额',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT ' 创建时间',
  `trans_total` decimal(10, 2) NULL DEFAULT NULL COMMENT '交易总量',
  `withdrawals_time` datetime(0) NULL DEFAULT NULL COMMENT '可提现时间',
  `overdue_time` datetime(0) NULL DEFAULT NULL COMMENT '过期时间(欢乐返扣款统计最大期限)',
  `check_status` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '3' COMMENT '核算状态：1：同意，2：不同意，3：未核算',
  `discount_status` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '扣回状态：0：未扣回，1：已扣回',
  `check_operator` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '核算人',
  `check_time` datetime(0) NULL DEFAULT NULL COMMENT '核算时间',
  `discount_operator` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扣回操作人',
  `discount_time` datetime(0) NULL DEFAULT NULL COMMENT '扣回时间',
  `liquidation_status` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '3' COMMENT '清算核算状态( 1：同意，2：不同意，3：未核算)',
  `liquidation_time` datetime(0) NULL DEFAULT NULL COMMENT '清算操作时间',
  `liquidation_operator` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '清算操作时间',
  `account_check_status` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '3' COMMENT '账务核算状态( 1：同意，2：不同意，3：未核算)',
  `account_check_time` datetime(0) NULL DEFAULT NULL COMMENT '账务操作时间',
  `account_check_operator` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账务操作人',
  `cash_back_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '欢乐返返现金额',
  `cumulate_trans_amount` decimal(13, 2) NULL DEFAULT 0.00 COMMENT '累计金额(欢乐返奖励累计时间内)',
  `end_cumulate_time` datetime(0) NULL DEFAULT NULL COMMENT '欢乐返满奖累计,最后统计时间',
  `cumulate_amount_minus` decimal(13, 2) NULL DEFAULT 0.00 COMMENT '累计交易（扣）（欢乐返）',
  `cumulate_amount_add` decimal(13, 2) NULL DEFAULT 0.00 COMMENT '累计交易（奖）（欢乐返）',
  `empty_amount` decimal(13, 2) NULL DEFAULT 0.00 COMMENT '未满扣N元（欢乐返）',
  `full_amount` decimal(13, 2) NULL DEFAULT 0.00 COMMENT '满奖M元（欢乐返）',
  `is_standard` int(1) NULL DEFAULT 0 COMMENT '奖励是否达标,0:未达标, 1:已达标（欢乐返）',
  `standard_time` datetime(0) NULL DEFAULT NULL COMMENT '奖励达标时间（欢乐返）',
  `minus_amount_time` datetime(0) NULL DEFAULT NULL COMMENT '扣款时间（欢乐返）',
  `add_amount_time` datetime(0) NULL DEFAULT NULL COMMENT '奖励时间（欢乐返）',
  `activity_type_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '欢乐返字类型编号',
  `repeat_register` int(5) NULL DEFAULT 0 COMMENT '是否重复注册 1是0否',
  `min_overdue_time` datetime(0) NULL DEFAULT NULL COMMENT '欢乐返奖励统计最大过期限',
  `cumulate_trans_min_amount` decimal(13, 2) NULL DEFAULT 0.00 COMMENT '累计交易金额(欢乐返扣减期限内)',
  `end_cumulate_min_time` datetime(0) NULL DEFAULT NULL COMMENT '扣减统计,最后统计时间',
  `billing_status` int(1) NULL DEFAULT 0 COMMENT '入账状态 0-未入账 1-已入账',
  `billing_msg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '入账信息',
  `billing_time` datetime(0) NULL DEFAULT NULL COMMENT '欢乐返入账时间',
  `active_sn` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '激活sn号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `merchantNo_activityCode`(`merchant_no`, `activity_code`) USING BTREE,
  INDEX `ind_agent_node`(`agent_node`) USING BTREE,
  INDEX `ind_active_order`(`active_order`) USING BTREE,
  INDEX `ind_overdue_time`(`overdue_time`) USING BTREE,
  INDEX `ind_active_time`(`active_time`) USING BTREE,
  INDEX `ind_min_overdue_time`(`min_overdue_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1513 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = ' 活动参与详情' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for activity_detail_20190520
-- ----------------------------
DROP TABLE IF EXISTS `activity_detail_20190520`;
CREATE TABLE `activity_detail_20190520`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `activity_code` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '活动代码(HLS)',
  `activity_id` int(11) NULL DEFAULT NULL COMMENT '活动编号',
  `merchant_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号',
  `agent_node` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商节点',
  `status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态(1:未激活、2:已激活、3:可提现、4:已提现、5:已过期、6:已返代理商、7:已扣分润账户、8:预调账已发起、9:已奖励)',
  `cash_order` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '提现订单',
  `active_order` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '激活订单',
  `active_time` datetime(0) NULL DEFAULT NULL COMMENT '激活时间',
  `cash_time` datetime(0) NULL DEFAULT NULL COMMENT '提现时间',
  `enter_time` datetime(0) NULL DEFAULT NULL COMMENT '进件时间',
  `frozen_amout` decimal(10, 2) NULL DEFAULT NULL COMMENT '冻结金额',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT ' 创建时间',
  `trans_total` decimal(10, 2) NULL DEFAULT NULL COMMENT '交易总量',
  `withdrawals_time` datetime(0) NULL DEFAULT NULL COMMENT '可提现时间',
  `overdue_time` datetime(0) NULL DEFAULT NULL COMMENT '过期时间(欢乐返扣款统计最大期限)',
  `check_status` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '3' COMMENT '核算状态：1：同意，2：不同意，3：未核算',
  `discount_status` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '扣回状态：0：未扣回，1：已扣回',
  `check_operator` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '核算人',
  `check_time` datetime(0) NULL DEFAULT NULL COMMENT '核算时间',
  `discount_operator` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扣回操作人',
  `discount_time` datetime(0) NULL DEFAULT NULL COMMENT '扣回时间',
  `liquidation_status` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '3' COMMENT '清算核算状态( 1：同意，2：不同意，3：未核算)',
  `liquidation_time` datetime(0) NULL DEFAULT NULL COMMENT '清算操作时间',
  `liquidation_operator` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '清算操作时间',
  `account_check_status` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '3' COMMENT '账务核算状态( 1：同意，2：不同意，3：未核算)',
  `account_check_time` datetime(0) NULL DEFAULT NULL COMMENT '账务操作时间',
  `account_check_operator` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账务操作人',
  `cash_back_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '欢乐返返现金额',
  `cumulate_trans_amount` decimal(13, 2) NULL DEFAULT 0.00 COMMENT '累计金额(欢乐返奖励累计时间内)',
  `end_cumulate_time` datetime(0) NULL DEFAULT NULL COMMENT '欢乐返满奖累计,最后统计时间',
  `cumulate_amount_minus` decimal(13, 2) NULL DEFAULT 0.00 COMMENT '累计交易（扣）（欢乐返）',
  `cumulate_amount_add` decimal(13, 2) NULL DEFAULT 0.00 COMMENT '累计交易（奖）（欢乐返）',
  `empty_amount` decimal(13, 2) NULL DEFAULT 0.00 COMMENT '未满扣N元（欢乐返）',
  `full_amount` decimal(13, 2) NULL DEFAULT 0.00 COMMENT '满奖M元（欢乐返）',
  `is_standard` int(1) NULL DEFAULT 0 COMMENT '奖励是否达标,0:未达标, 1:已达标（欢乐返）',
  `standard_time` datetime(0) NULL DEFAULT NULL COMMENT '奖励达标时间（欢乐返）',
  `minus_amount_time` datetime(0) NULL DEFAULT NULL COMMENT '扣款时间（欢乐返）',
  `add_amount_time` datetime(0) NULL DEFAULT NULL COMMENT '奖励时间（欢乐返）',
  `activity_type_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '欢乐返字类型编号',
  `repeat_register` int(5) NULL DEFAULT 0 COMMENT '是否重复注册 1是0否',
  `min_overdue_time` datetime(0) NULL DEFAULT NULL COMMENT '欢乐返奖励统计最大过期限',
  `cumulate_trans_min_amount` decimal(13, 2) NULL DEFAULT 0.00 COMMENT '累计交易金额(欢乐返扣减期限内)',
  `end_cumulate_min_time` datetime(0) NULL DEFAULT NULL COMMENT '扣减统计,最后统计时间',
  `billing_status` int(1) NULL DEFAULT 0 COMMENT '入账状态 0-未入账 1-已入账',
  `billing_msg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '入账信息',
  `billing_time` datetime(0) NULL DEFAULT NULL COMMENT '欢乐返入账时间',
  `active_sn` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '激活sn号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `merchantNo_activityCode`(`merchant_no`, `activity_code`) USING BTREE,
  INDEX `ind_agent_node`(`agent_node`) USING BTREE,
  INDEX `ind_active_order`(`active_order`) USING BTREE,
  INDEX `ind_overdue_time`(`overdue_time`) USING BTREE,
  INDEX `ind_active_time`(`active_time`) USING BTREE,
  INDEX `ind_min_overdue_time`(`min_overdue_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1208 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = ' 活动参与详情' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for activity_detail_backstage
-- ----------------------------
DROP TABLE IF EXISTS `activity_detail_backstage`;
CREATE TABLE `activity_detail_backstage`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `batch_no` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '批次号',
  `act_id` int(10) NOT NULL COMMENT '对应activity_detail的id',
  `act_state` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '操作状态,1欢乐送核算2欢乐返核算3欢乐返清算4欢乐返奖励入账',
  `user_id` int(10) NOT NULL COMMENT '对应用户表的id',
  `send_num` int(10) NOT NULL COMMENT '接口请求次数',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '欢乐送,欢乐返活动延时核算清算' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for activity_group
-- ----------------------------
DROP TABLE IF EXISTS `activity_group`;
CREATE TABLE `activity_group`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `activity_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '活动类型，参照数据字典ACTIVITY_TYPE',
  `group_no` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '群组号；序列号生成',
  `relation_type` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组员关系；0 互斥，1 同组',
  `create_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '活动关系群组表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for activity_hardware
-- ----------------------------
DROP TABLE IF EXISTS `activity_hardware`;
CREATE TABLE `activity_hardware`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '活动编号',
  `activity_code` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '活动代码(HLS)',
  `activiy_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '活动名称',
  `hard_id` int(11) NULL DEFAULT NULL COMMENT '硬件编号',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '价格',
  `target_amout` decimal(10, 2) NULL DEFAULT NULL COMMENT '目标金额',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `operator` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作人',
  `trans_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '交易金额--取子类型的字段时时同步',
  `cash_back_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '返现金额--取子类型的字段时时同步',
  `empty_amount` decimal(13, 2) NULL DEFAULT 0.00 COMMENT '首次注册不满扣N值（欢乐返）',
  `full_amount` decimal(13, 2) NULL DEFAULT 0.00 COMMENT '首次注册满奖M值（欢乐返）',
  `activity_type_no` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '欢乐返子类型编号',
  `cash_last_ally_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '返盟友金额',
  `default_status` int(3) NOT NULL DEFAULT 1 COMMENT '是否取默认活动内容，0：否，1：是',
  `cumulate_trans_day` int(3) NULL DEFAULT 0 COMMENT '首次注册累计交易奖励时间（奖）（欢乐返）',
  `cumulate_amount_minus` decimal(13, 2) NULL DEFAULT 0.00 COMMENT '首次累计交易（扣）（欢乐返）',
  `cumulate_amount_add` decimal(13, 2) NULL DEFAULT 0.00 COMMENT '首次累计交易（奖）（欢乐返）',
  `cumulate_trans_minus_day` int(3) NULL DEFAULT 0 COMMENT '首次注册累计交易扣费时间（扣）（欢乐返）',
  `team_id` bigint(20) NULL DEFAULT NULL COMMENT '所属组织',
  `repeat_empty_amount` decimal(13, 2) NULL DEFAULT 0.00 COMMENT '重复注册不满扣N值（欢乐返）',
  `repeat_full_amount` decimal(13, 2) NULL DEFAULT 0.00 COMMENT '重复注册满奖M值（欢乐返）',
  `repeat_cumulate_trans_day` int(3) NULL DEFAULT 0 COMMENT '重复注册累计交易奖励时间（奖）（欢乐返）',
  `repeat_cumulate_amount_minus` decimal(13, 2) NULL DEFAULT 0.00 COMMENT '重复累计交易（扣）（欢乐返）',
  `repeat_cumulate_amount_add` decimal(13, 2) NULL DEFAULT 0.00 COMMENT '重复累计交易（奖）（欢乐返）',
  `repeat_cumulate_trans_minus_day` int(3) NULL DEFAULT 0 COMMENT '重复注册累计交易扣费时间（扣）（欢乐返）',
  `activity_reward_config_id` int(11) NULL DEFAULT NULL COMMENT '0元个性化活动配置关联id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 233 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '活动配置硬件表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for activity_hardware_type
-- ----------------------------
DROP TABLE IF EXISTS `activity_hardware_type`;
CREATE TABLE `activity_hardware_type`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `activity_type_no` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '欢乐返子类型编号',
  `activity_type_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '欢乐返子类型名称',
  `activity_code` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '欢乐返类型',
  `trans_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '交易金额',
  `cash_back_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '返现金额',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `repeat_register_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '重复注册返现金额',
  `update_agent_status` int(3) NOT NULL DEFAULT 0 COMMENT '允许代理商更改，0：否，1：是',
  `empty_amount` decimal(13, 2) NULL DEFAULT 0.00 COMMENT '首次注册不满扣N值（欢乐返）',
  `full_amount` decimal(13, 2) NULL DEFAULT 0.00 COMMENT '首次注册满奖M值（欢乐返）',
  `repeat_empty_amount` decimal(13, 2) NULL DEFAULT 0.00 COMMENT '重复注册不满扣N值（欢乐返）',
  `repeat_full_amount` decimal(13, 2) NULL DEFAULT 0.00 COMMENT '重复注册满奖M值（欢乐返）',
  `count_trade_scope` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '欢乐返子类型交易统计类型范围,多类型以,分隔。值为数据字典key为PAY_METHOD_TYPE的值',
  `rule_id` int(11) NULL DEFAULT NULL COMMENT '欢乐返活跃商户配置表规则ID,hlf_activity_merchant_rule.rule_id',
  `sub_type` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '1' COMMENT '欢乐返子类型类型,1:原来的欢乐返,2:欢乐返新活动',
  `one_limit_days` int(5) NULL DEFAULT NULL COMMENT '激活后第一个周期,多少天内',
  `one_trans_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '激活后第一个周期,累计交易金额',
  `one_reward_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '激活后第一个周期，首次注册奖励金额',
  `one_repeat_reward_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '激活后第一个周期，重复注册奖励金额',
  `two_limit_days` int(5) NULL DEFAULT NULL COMMENT '激活后第二个周期,多少天内',
  `two_trans_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '激活后第二个周期,累计交易金额',
  `two_reward_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '激活后第二个周期，首次注册奖励金额',
  `two_repeat_reward_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '激活后第二个周期，重复注册奖励金额',
  `three_limit_days` int(5) NULL DEFAULT NULL COMMENT '激活后第三个周期,多少天内',
  `three_trans_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '激活后第三个周期,累计交易金额',
  `three_reward_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '激活后第三个周期，首次注册奖励金额',
  `three_repeat_reward_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '激活后第三个周期，重复注册奖励金额',
  `four_limit_days` int(5) NULL DEFAULT NULL COMMENT '激活后第四个周期,多少天内',
  `four_trans_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '激活后第四个周期,累计交易金额',
  `four_reward_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '激活后第四个周期，首次注册奖励金额',
  `four_repeat_reward_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '激活后第四个周期，重复注册奖励金额',
  `merchant_limit_days` int(5) NULL DEFAULT NULL COMMENT '激活后商户累计交易天数',
  `merchant_trans_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '激活后商户累计交易金额',
  `merchant_reward_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '激活后，商户首次注册奖励金额',
  `merchant_repeat_reward_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '激活后，商户重复注册奖励金额',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `activity_type_no`(`activity_type_no`) USING BTREE,
  UNIQUE INDEX `activity_type_name`(`activity_type_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 79 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for activity_order_info
-- ----------------------------
DROP TABLE IF EXISTS `activity_order_info`;
CREATE TABLE `activity_order_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单号',
  `mobile_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `merchant_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `pay_method` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易方式 1 POS，2 支付宝，3 微信，4 快捷',
  `coupon_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '优惠卷活动类型(详见字典表 COUPON_CODE)',
  `coupon_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `trans_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '订单金额',
  `pay_time` timestamp(0) NULL DEFAULT NULL COMMENT '订单支付时间',
  `trans_status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `pay_order_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '支付订单编号',
  `remark` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_update_time` datetime(0) NULL DEFAULT NULL COMMENT '最后更新时间',
  `activity_entity_id` int(20) NULL DEFAULT -1 COMMENT '劵的ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `订单号`(`order_no`) USING BTREE,
  INDEX `ind_coupon_no`(`coupon_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2033 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for activity_reward_config
-- ----------------------------
DROP TABLE IF EXISTS `activity_reward_config`;
CREATE TABLE `activity_reward_config`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `activity_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '活动名称',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '活动开始时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '活动结束时间',
  `cumulate_trans_minus_day` int(3) NULL DEFAULT 0 COMMENT '首次注册累计交易扣费时间（欢乐返）',
  `cumulate_trans_day` int(3) NULL DEFAULT 0 COMMENT '首次注册累计交易奖励时间（欢乐返）',
  `cumulate_amount_minus` decimal(13, 2) NULL DEFAULT 0.00 COMMENT '首次注册不满扣N值（扣）（欢乐返）',
  `cumulate_amount_add` decimal(13, 2) NULL DEFAULT 0.00 COMMENT '首次注册满奖M值（奖）（欢乐返）',
  `repeat_cumulate_trans_minus_day` int(3) NULL DEFAULT 0 COMMENT '重复注册累计交易扣费时间（欢乐返）',
  `repeat_cumulate_trans_day` int(3) NULL DEFAULT 0 COMMENT '重复注册累计交易奖励时间（欢乐返）',
  `repeat_cumulate_amount_minus` decimal(13, 2) NULL DEFAULT 0.00 COMMENT '重复注册不满扣N值（扣）（欢乐返）',
  `repeat_cumulate_amount_add` decimal(13, 2) NULL DEFAULT 0.00 COMMENT '重复注册满奖M值（奖）（欢乐返）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '活动奖励配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for activity_vip
-- ----------------------------
DROP TABLE IF EXISTS `activity_vip`;
CREATE TABLE `activity_vip`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `time` int(11) NULL DEFAULT NULL COMMENT '时间',
  `original_price` decimal(8, 2) NULL DEFAULT NULL COMMENT '原价',
  `discount_price` decimal(8, 2) NULL DEFAULT NULL COMMENT '折扣价',
  `sort_num` int(11) NULL DEFAULT NULL COMMENT '排序',
  `is_recommend` int(11) NULL DEFAULT 0 COMMENT '推荐 0关闭，1打开',
  `is_switch` int(11) NULL DEFAULT 0 COMMENT '开关 0关闭1打开',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `record_creator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `activity_code` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '活动代码(HLS)',
  `team_id` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组织',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'VIP优享配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for add_creaditcard_log
-- ----------------------------
DROP TABLE IF EXISTS `add_creaditcard_log`;
CREATE TABLE `add_creaditcard_log`  (
  `id` bigint(22) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `merchant_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户ID',
  `account_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '信用卡号',
  `bank_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银行名称',
  `bank_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银行编码',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `bind_status` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '绑定状态,0:否,1:是',
  `encrypt_account_no` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '加密后的卡号',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `merchant_no`(`merchant_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 158 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for add_require_item
-- ----------------------------
DROP TABLE IF EXISTS `add_require_item`;
CREATE TABLE `add_require_item`  (
  `item_id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '进件要求项ID',
  `item_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '要求项名称',
  `example_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '示例类型:1-图片，2-文件，3-文字',
  `example` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '示例',
  `remark` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `photo` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片来源：1 只允许拍照，2 拍照和相册',
  `data_all` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资料是否记录进件项内容：1-是，2-否',
  `photo_address` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片的地址',
  `check_status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否需要审核：1-是，2-否',
  `check_msg` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '审核错误提示',
  PRIMARY KEY (`item_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 40 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '进件要求项定义表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for agent_account_control
-- ----------------------------
DROP TABLE IF EXISTS `agent_account_control`;
CREATE TABLE `agent_account_control`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `agent_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商编号',
  `agent_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商名称',
  `retain_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '留存金额',
  `status` int(2) NULL DEFAULT 1 COMMENT '状态：打开-1，关闭-0',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `default_status` int(2) NULL DEFAULT 0 COMMENT '状态：0普通1默认',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 55 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for agent_activity
-- ----------------------------
DROP TABLE IF EXISTS `agent_activity`;
CREATE TABLE `agent_activity`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `activity_type_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '欢乐返子类型编号',
  `agent_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '代理商编号',
  `agent_node` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '代理商节点',
  `cash_back_amount` decimal(10, 2) NOT NULL COMMENT '返现金额',
  `tax_rate` decimal(10, 2) NOT NULL DEFAULT 100.00 COMMENT '税额百分比',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `repeat_register_amount` decimal(10, 2) NOT NULL COMMENT '重复返现金额',
  `repeat_register_ratio` decimal(10, 2) NOT NULL DEFAULT 1.00 COMMENT '重复注册返现比例',
  `full_prize_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '首次注册满奖金额',
  `repeat_full_prize_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '重复注册满奖金额',
  `not_full_deduct_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '首次注册不满扣金额',
  `repeat_not_full_deduct_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '重复注册不满扣金额',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT 'true-开启，false-关闭',
  `sub_type` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '1' COMMENT '欢乐返子类型类型,1:原来的欢乐返,2:欢乐返新活动',
  `one_reward_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '激活后第一个周期，首次注册奖励金额',
  `two_reward_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '激活后第二个周期，首次注册奖励金额',
  `three_reward_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '激活后第三个周期，首次注册奖励金额',
  `four_reward_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '激活后第四个周期，首次注册奖励金额',
  `one_repeat_reward_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '激活后第一个周期，重复注册奖励金额',
  `two_repeat_reward_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '激活后第二个周期，重复注册奖励金额',
  `three_repeat_reward_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '激活后第三个周期，重复注册奖励金额',
  `four_repeat_reward_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '激活后第四个周期，重复注册奖励金额',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `agent_activity`(`agent_no`, `activity_type_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3620 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '代理活动关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for agent_activity_reward_config
-- ----------------------------
DROP TABLE IF EXISTS `agent_activity_reward_config`;
CREATE TABLE `agent_activity_reward_config`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `agent_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '代理商编号',
  `agent_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '代理商名称',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作人',
  `activity_id` int(11) NULL DEFAULT NULL COMMENT '活动id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 37 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '活动奖励代理商配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for agent_app_info
-- ----------------------------
DROP TABLE IF EXISTS `agent_app_info`;
CREATE TABLE `agent_app_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `photo` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片',
  `text` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文本',
  `agent_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '一级代理商的id',
  `team_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组织ID',
  `version` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'app版本号',
  `download_url` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'app下载路径',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 107 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '代理商APP关于软件信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for agent_authorized_link
-- ----------------------------
DROP TABLE IF EXISTS `agent_authorized_link`;
CREATE TABLE `agent_authorized_link`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `record_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `agent_authorized` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '代理商编号(agent_authorized可以访问agent_link)',
  `agent_link` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '授权查询代理商编号',
  `check_user` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '审核人员',
  `record_check` int(1) NULL DEFAULT 0 COMMENT '审核状态：0未通过，1通过',
  `record_status` int(1) NULL DEFAULT 0 COMMENT '开启状态：0关闭;1:开启;',
  `record_creator` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `last_updat_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后修改时间',
  `top_agent` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '顶层代理商编号',
  `is_top` int(11) NULL DEFAULT 2 COMMENT '1.为是顶层代理商 2为不是顶层代理商',
  `is_look` int(11) NULL DEFAULT 0 COMMENT '数据查询开关：0关闭;1:开启;',
  `agent_node` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商链条节点',
  `link_level` int(11) NULL DEFAULT NULL COMMENT '最多5个层级 顶层代理商层级为1 ',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_link`(`agent_authorized`, `agent_link`) USING BTREE,
  INDEX `index_link_top_agent`(`top_agent`) USING BTREE,
  INDEX `idx_agent_link`(`agent_link`) USING BTREE,
  INDEX `idx_agent_node`(`agent_node`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2082 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '一级代理商间授权查询链接关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for agent_business_product
-- ----------------------------
DROP TABLE IF EXISTS `agent_business_product`;
CREATE TABLE `agent_business_product`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `agent_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商ID',
  `bp_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务产品ID',
  `status` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态:1-正常，0-关闭',
  `default_bp_flag` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '商户默认自定义费率 0:不默认 1:默认',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `agent_id_UNIQUE`(`agent_no`, `bp_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17030 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '代理商代理业务产品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for agent_dayhpb_share_collect
-- ----------------------------
DROP TABLE IF EXISTS `agent_dayhpb_share_collect`;
CREATE TABLE `agent_dayhpb_share_collect`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `agent_no` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商编号',
  `parent_id` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '父级代理商编号',
  `agent_node` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商节点',
  `total_count` int(10) NULL DEFAULT 0 COMMENT '条数',
  `total_money` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '金额总数',
  `acc_money` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '入账总数',
  `collec_time` date NULL DEFAULT NULL COMMENT '汇总时间',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 162934 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '返现数据汇总' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for agent_daysettle_share_collect
-- ----------------------------
DROP TABLE IF EXISTS `agent_daysettle_share_collect`;
CREATE TABLE `agent_daysettle_share_collect`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `agent_no` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商编号',
  `parent_id` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '父代理商级别',
  `agent_node` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商节点',
  `total_count` int(10) NULL DEFAULT 0 COMMENT '条数',
  `total_money` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '金额总数',
  `acc_money` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '入账总数',
  `collec_time` date NULL DEFAULT NULL COMMENT '汇总时间',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 149728 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '提现分润数据汇总' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for agent_daytrans_share_collect
-- ----------------------------
DROP TABLE IF EXISTS `agent_daytrans_share_collect`;
CREATE TABLE `agent_daytrans_share_collect`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `agent_no` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商编号',
  `parent_id` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '父代理商级别',
  `agent_node` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商节点',
  `total_count` int(10) NULL DEFAULT 0 COMMENT '条数',
  `total_trans_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '总交易金额',
  `total_money` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '金额总数',
  `acc_money` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '入账总数',
  `collec_time` date NULL DEFAULT NULL COMMENT '汇总时间',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `agent_time_index`(`agent_no`, `collec_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 146979 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '代理商分润数据汇总' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for agent_function_config
-- ----------------------------
DROP TABLE IF EXISTS `agent_function_config`;
CREATE TABLE `agent_function_config`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `agent_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '代理商编号',
  `function_number` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '功能编号',
  `one_agent_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '一级代理商编号',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `agent_function_number_only`(`agent_no`, `function_number`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 189 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '代理商系统功能管控设置配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for agent_function_manage
-- ----------------------------
DROP TABLE IF EXISTS `agent_function_manage`;
CREATE TABLE `agent_function_manage`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `agent_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '1级代理商编号',
  `agent_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '1级代理商名称',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作人',
  `function_number` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '功能编号',
  `team_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '是否直营',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `agent_function_number_only`(`agent_no`, `function_number`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 1039 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '代理商活体验证自审' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for agent_function_manage_blacklist
-- ----------------------------
DROP TABLE IF EXISTS `agent_function_manage_blacklist`;
CREATE TABLE `agent_function_manage_blacklist`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `agent_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '代理商编号',
  `agent_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '代理商名称',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作人',
  `function_number` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '功能编号',
  `team_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '是否直营',
  `blacklist` int(10) NOT NULL DEFAULT 0 COMMENT '是否黑名单 0 为不是 1为是',
  `contains_lower` int(2) NOT NULL DEFAULT 1 COMMENT '是否包含下级 0位不包含 1为包含',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `agent_function_number_only`(`agent_no`, `function_number`, `blacklist`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 1042 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '代理商功能控制黑名单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for agent_function_rule
-- ----------------------------
DROP TABLE IF EXISTS `agent_function_rule`;
CREATE TABLE `agent_function_rule`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `function_number` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '功能编号',
  `function_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '功能名称',
  `function_default_value` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '功能默认值 1为开启 0为关闭',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `function_number_only`(`function_number`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '代理商系统功能管控设置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for agent_function_rule_copy
-- ----------------------------
DROP TABLE IF EXISTS `agent_function_rule_copy`;
CREATE TABLE `agent_function_rule_copy`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `function_number` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '功能编号',
  `function_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '功能名称',
  `function_default_value` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '功能默认值 1为开启 0为关闭',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `function_number_only`(`function_number`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '代理商系统功能管控设置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for agent_info
-- ----------------------------
DROP TABLE IF EXISTS `agent_info`;
CREATE TABLE `agent_info`  (
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
  `agent_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商类型',
  `agent_share_level` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易分润等级最高可调级数',
  `id_card_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '身份证号',
  `safephone` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '安全手机',
  `full_prize_switch` int(11) NOT NULL DEFAULT 0 COMMENT '满奖开关 1-打开，0-关闭',
  `not_full_deduct_switch` int(11) NOT NULL DEFAULT 0 COMMENT '不满扣开关 1-打开，0-关闭',
  `safe_password` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '安全密码',
  `regist_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商注册类型:拓展代理为1,其他为空',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `agent_id_UNIQUE`(`agent_no`) USING BTREE,
  UNIQUE INDEX `mobile_UNIQUE`(`mobilephone`, `team_id`) USING BTREE,
  INDEX `agent_node_index`(`agent_node`) USING BTREE,
  INDEX `index_agent_no_one`(`one_level_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 925922 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '代理商信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for agent_monthhpb_share_collect
-- ----------------------------
DROP TABLE IF EXISTS `agent_monthhpb_share_collect`;
CREATE TABLE `agent_monthhpb_share_collect`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `agent_no` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商编号',
  `parent_id` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '父级代理商编号',
  `agent_node` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商节点',
  `total_count` int(10) NULL DEFAULT 0 COMMENT '条数',
  `total_money` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '金额总数',
  `acc_money` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '入账总数',
  `collec_time` date NULL DEFAULT NULL COMMENT '汇总时间',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '返现月数据汇总' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for agent_monthsettle_share_collect
-- ----------------------------
DROP TABLE IF EXISTS `agent_monthsettle_share_collect`;
CREATE TABLE `agent_monthsettle_share_collect`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `agent_no` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商编号',
  `parent_id` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '父代理商级别',
  `agent_node` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商节点',
  `total_count` int(10) NULL DEFAULT 0 COMMENT '条数',
  `total_money` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '金额总数',
  `acc_money` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '入账总数',
  `collec_time` date NULL DEFAULT NULL COMMENT '汇总时间',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '提现分润月数据汇总' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for agent_monthtrans_share_collect
-- ----------------------------
DROP TABLE IF EXISTS `agent_monthtrans_share_collect`;
CREATE TABLE `agent_monthtrans_share_collect`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `agent_no` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商编号',
  `parent_id` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '父代理商级别',
  `agent_node` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商节点',
  `total_trans_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '总交易金额',
  `total_count` int(10) NULL DEFAULT 0 COMMENT '条数',
  `total_money` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '金额总数',
  `acc_money` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '入账总数',
  `collec_time` date NULL DEFAULT NULL COMMENT '汇总时间',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10694 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '代理商分润月数据汇总' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for agent_oem_info
-- ----------------------------
DROP TABLE IF EXISTS `agent_oem_info`;
CREATE TABLE `agent_oem_info`  (
  `one_agent_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '一级代理商编号',
  `oem_type` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '参考字典OEM_TYPE',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后处理时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '最后处理时间',
  PRIMARY KEY (`one_agent_no`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'oem一级代理商信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for agent_oper_log
-- ----------------------------
DROP TABLE IF EXISTS `agent_oper_log`;
CREATE TABLE `agent_oper_log`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '日志编号',
  `agent_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商编号',
  `agent_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商名称',
  `request_method` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求方法',
  `method_desc` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '方法描述',
  `request_params` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '请求参数',
  `return_result` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '返回结果',
  `oper_ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求ip',
  `oper_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 44825 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '代理商操作日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for agent_role_rigth
-- ----------------------------
DROP TABLE IF EXISTS `agent_role_rigth`;
CREATE TABLE `agent_role_rigth`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `role_id` int(11) NULL DEFAULT NULL COMMENT '角色id',
  `rigth_id` int(11) NULL DEFAULT NULL COMMENT '权限id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6586 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '代理商角色权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for agent_share_detail
-- ----------------------------
DROP TABLE IF EXISTS `agent_share_detail`;
CREATE TABLE `agent_share_detail`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `share_month` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分润批次号',
  `agent_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商名称',
  `agent_no` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商编号',
  `total_trans_amount` decimal(30, 2) NULL DEFAULT NULL COMMENT '交易总金额',
  `agent_rate` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商扣率',
  `agent_share_rate` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商分润比例',
  `total_agent_fee` decimal(30, 2) NULL DEFAULT NULL COMMENT '代理商手续费总额',
  `total_merchant_fee` decimal(30, 2) NULL DEFAULT NULL COMMENT '商户手续费总额',
  `brand_used_amount` decimal(30, 2) NULL DEFAULT NULL COMMENT '品牌使用费',
  `total_share_amount` decimal(30, 2) NULL DEFAULT NULL COMMENT '分润总额',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `check_status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '核对状态1.已核对，2未核对',
  `ter_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '机型：POS,SMBOX',
  `total_acq_fee` decimal(30, 2) NULL DEFAULT NULL COMMENT '收单手续费总额',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 12511 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for agent_share_ladder
-- ----------------------------
DROP TABLE IF EXISTS `agent_share_ladder`;
CREATE TABLE `agent_share_ladder`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `agent_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商ID',
  `service_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '服务ID',
  `card_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银行卡种类:0-不限，1-只信用卡，2-只储蓄卡',
  `holidays_mark` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '节假日标志:1-只工作日，2-只节假日，0-不限',
  `efficient_date` date NULL DEFAULT NULL COMMENT '生效日期',
  `disabled_date` date NULL DEFAULT NULL COMMENT '失效日期',
  `trans_amount_floor` decimal(20, 4) NULL DEFAULT NULL COMMENT '交易额下限',
  `trans_amount_ceiling` decimal(20, 4) NULL DEFAULT NULL COMMENT '交易额上限',
  `share_profit_percent` decimal(20, 6) NULL DEFAULT NULL COMMENT '分润百分比',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `agent_id_UNIQUE`(`agent_id`, `service_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '代理商分润阶梯' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for agent_share_merchant_msg
-- ----------------------------
DROP TABLE IF EXISTS `agent_share_merchant_msg`;
CREATE TABLE `agent_share_merchant_msg`  (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `agent_no` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `share_month` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `merchant_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `merchant_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `real_flag` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `trans_amount` decimal(30, 2) NULL DEFAULT NULL,
  `merchant_fee` decimal(30, 2) NULL DEFAULT NULL,
  `agent_fee` decimal(30, 2) NULL DEFAULT NULL,
  `items` int(10) NULL DEFAULT NULL,
  `ter_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '机型：POS，SMBOX',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 35 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for agent_share_pf
-- ----------------------------
DROP TABLE IF EXISTS `agent_share_pf`;
CREATE TABLE `agent_share_pf`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '商户订单号',
  `agent_node` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商节点',
  `profits_1` decimal(30, 2) NULL DEFAULT NULL COMMENT '一级分润',
  `profits_2` decimal(30, 2) NULL DEFAULT NULL,
  `profits_3` decimal(30, 2) NULL DEFAULT NULL,
  `profits_4` decimal(30, 2) NULL DEFAULT NULL,
  `profits_5` decimal(30, 2) NULL DEFAULT NULL,
  `profits_6` decimal(30, 2) NULL DEFAULT NULL,
  `profits_7` decimal(30, 2) NULL DEFAULT NULL,
  `profits_8` decimal(30, 2) NULL DEFAULT NULL,
  `profits_9` decimal(30, 2) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1852 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for agent_share_register
-- ----------------------------
DROP TABLE IF EXISTS `agent_share_register`;
CREATE TABLE `agent_share_register`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `account_date` date NULL DEFAULT NULL COMMENT '账户账务系统记账日期',
  `account_serial_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '记账流水号',
  `serial_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '系统流水号',
  `one_agent_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '1级代理商ID',
  `one_agent_share_amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '1级代理商分润金额',
  `one_agent_share_rule_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '1级代理商分润规则ID',
  `two_agent_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '2级代理商ID',
  `two_agent_share_amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '2级代理商分润金额',
  `two_agent_share_rule_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '2级代理商分润规则ID',
  `three_agent_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '3级代理商ID',
  `three_agent_share_amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '3级代理商分润金额',
  `three_agent_share_rule_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '3级代理商分润规则ID',
  `four_agent_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '4级代理商ID',
  `four_agent_share_amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '4级代理商分润金额',
  `four_agent_share_rule_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '4级代理商分润规则ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `account_ date_UNIQUE`(`account_date`, `account_serial_no`, `serial_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'V1导入交易（刷卡，提现等涉及到资金变动的）流水各级代理商分润登记表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for agent_share_rule
-- ----------------------------
DROP TABLE IF EXISTS `agent_share_rule`;
CREATE TABLE `agent_share_rule`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `agent_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '代理商ID',
  `service_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '服务ID',
  `card_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '银行卡种类:0-不限，1-只信用卡，2-只储蓄卡',
  `holidays_mark` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '节假日标志:0-不限，1-只工作日，2-只节假日',
  `efficient_date` datetime(0) NULL DEFAULT NULL COMMENT '生效日期',
  `disabled_date` datetime(0) NULL DEFAULT NULL COMMENT '失效日期',
  `profit_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分润方式:1-每笔固定收益额；2-每笔固定收益率；3-每笔固定收益率+保底封顶；4-每笔固定收益金额+固定收益率5-商户签约费率与代理商成本费率差额百分比分润；6-商户签约费率与代理商成本费率差额按交易量阶梯百分比分润）（二级及往下的代理商只有前4种）',
  `per_fix_income` decimal(10, 2) NULL DEFAULT NULL COMMENT '每笔固定收益额',
  `per_fix_inrate` decimal(10, 6) NULL DEFAULT NULL COMMENT '每笔固定收益率',
  `safe_line` decimal(10, 6) NULL DEFAULT NULL COMMENT '保底',
  `capping` decimal(10, 6) NULL DEFAULT NULL COMMENT '封顶',
  `share_profit_percent` decimal(10, 6) NULL DEFAULT NULL COMMENT '代理商固定分润百分比',
  `ladder` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '阶梯方式:1-按月',
  `cost_rate_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商成本费率类型:1-每笔固定金额，2-扣率，3-扣率+保底封顶',
  `per_fix_cost` decimal(10, 2) NULL DEFAULT NULL COMMENT '代理商成本每笔固定值',
  `cost_rate` decimal(10, 6) NULL DEFAULT NULL COMMENT '代理商成本扣率',
  `cost_capping` decimal(10, 6) NULL DEFAULT NULL COMMENT '代理商成本封顶',
  `cost_safeline` decimal(10, 6) NULL DEFAULT NULL COMMENT '代理商成本保底',
  `check_status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '审核状态：0待审核，1 审核通过，2 审核未通过',
  `lock_status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '锁定状态：0未锁定；1锁定',
  `ladder1_rate` decimal(10, 6) NULL DEFAULT NULL COMMENT '阶梯区间1费率',
  `ladder1_max` decimal(20, 4) NULL DEFAULT NULL COMMENT '阶梯区间1上限',
  `ladder2_rate` decimal(10, 6) NULL DEFAULT NULL COMMENT '阶梯区间2费率',
  `ladder2_max` decimal(20, 4) NULL DEFAULT NULL COMMENT '阶梯区间2上限',
  `ladder3_rate` decimal(10, 6) NULL DEFAULT NULL COMMENT '阶梯区间3费率',
  `ladder3_max` decimal(20, 4) NULL DEFAULT NULL COMMENT '阶梯区间3上限',
  `ladder4_rate` decimal(10, 6) NULL DEFAULT NULL COMMENT '阶梯区间4费率',
  `ladder4_max` decimal(20, 4) NULL DEFAULT NULL COMMENT '阶梯区间4上限',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `agent_id_UNIQUE`(`agent_no`, `service_id`, `card_type`, `holidays_mark`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23678 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '代理商分润规则' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for agent_share_rule_task
-- ----------------------------
DROP TABLE IF EXISTS `agent_share_rule_task`;
CREATE TABLE `agent_share_rule_task`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '分润规则任务ID',
  `share_id` bigint(20) NOT NULL COMMENT '分润规则表的id',
  `efficient_date` datetime(0) NULL DEFAULT NULL COMMENT '生效日期',
  `effective_status` int(11) NOT NULL DEFAULT 0 COMMENT '是否生效:0-未生效，1-生效',
  `check_status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '审核状态：0待审核，1 审核通过，2 审核未通过',
  `profit_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分润方式:1-每笔固定收益额；2-每笔固定收益率；3-每笔固定收益率+保底封顶；4-每笔固定收益金额+固定收益率5-商户签约费率与代理商成本费率差额百分比分润；6-商户签约费率与代理商成本费率差额按交易量阶梯百分比分润）（二级及往下的代理商只有前4种）',
  `per_fix_income` decimal(10, 2) NULL DEFAULT NULL COMMENT '每笔固定收益额',
  `per_fix_inrate` decimal(10, 6) NULL DEFAULT NULL COMMENT '每笔固定收益率',
  `safe_line` decimal(10, 6) NULL DEFAULT NULL COMMENT '保底',
  `capping` decimal(10, 6) NULL DEFAULT NULL COMMENT '封顶',
  `share_profit_percent` decimal(10, 6) NULL DEFAULT NULL COMMENT '代理商固定分润百分比',
  `ladder` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '阶梯方式:1-按月',
  `cost_rate_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商成本费率类型:1-每笔固定金额，2-扣率，3-扣率+保底封顶',
  `per_fix_cost` decimal(10, 2) NULL DEFAULT NULL COMMENT '代理商成本每笔固定值',
  `cost_rate` decimal(10, 6) NULL DEFAULT NULL COMMENT '代理商成本扣率',
  `cost_capping` decimal(10, 6) NULL DEFAULT NULL COMMENT '代理商成本封顶',
  `cost_safeline` decimal(10, 6) NULL DEFAULT NULL COMMENT '代理商成本保底',
  `ladder1_rate` decimal(10, 6) NULL DEFAULT NULL COMMENT '阶梯区间1费率',
  `ladder1_max` decimal(20, 4) NULL DEFAULT NULL COMMENT '阶梯区间1上限',
  `ladder2_rate` decimal(10, 6) NULL DEFAULT NULL COMMENT '阶梯区间2费率',
  `ladder2_max` decimal(20, 4) NULL DEFAULT NULL COMMENT '阶梯区间2上限',
  `ladder3_rate` decimal(10, 6) NULL DEFAULT NULL COMMENT '阶梯区间3费率',
  `ladder3_max` decimal(20, 4) NULL DEFAULT NULL COMMENT '阶梯区间3上限',
  `ladder4_rate` decimal(10, 6) NULL DEFAULT NULL COMMENT '阶梯区间4费率',
  `ladder4_max` decimal(20, 4) NULL DEFAULT NULL COMMENT '阶梯区间4上限',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2467 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for agent_share_rule_tmp
-- ----------------------------
DROP TABLE IF EXISTS `agent_share_rule_tmp`;
CREATE TABLE `agent_share_rule_tmp`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `agent_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '代理商ID',
  `service_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '服务ID',
  `card_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '银行卡种类:0-不限，1-只信用卡，2-只储蓄卡',
  `holidays_mark` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '节假日标志:0-不限，1-只工作日，2-只节假日',
  `efficient_date` datetime(0) NULL DEFAULT NULL COMMENT '生效日期',
  `disabled_date` datetime(0) NULL DEFAULT NULL COMMENT '失效日期',
  `profit_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分润方式:1-每笔固定收益额；2-每笔固定收益率；3-每笔固定收益率+保底封顶；4-每笔固定收益金额+固定收益率5-商户签约费率与代理商成本费率差额百分比分润；6-商户签约费率与代理商成本费率差额按交易量阶梯百分比分润）（二级及往下的代理商只有前4种）',
  `per_fix_income` decimal(10, 2) NULL DEFAULT NULL COMMENT '每笔固定收益额',
  `per_fix_inrate` decimal(10, 6) NULL DEFAULT NULL COMMENT '每笔固定收益率',
  `safe_line` decimal(10, 6) NULL DEFAULT NULL COMMENT '保底',
  `capping` decimal(10, 6) NULL DEFAULT NULL COMMENT '封顶',
  `share_profit_percent` decimal(10, 6) NULL DEFAULT NULL COMMENT '代理商固定分润百分比',
  `ladder` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '阶梯方式:1-按月',
  `cost_rate_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商成本费率类型:1-每笔固定金额，2-扣率，3-扣率+保底封顶',
  `per_fix_cost` decimal(10, 2) NULL DEFAULT NULL COMMENT '代理商成本每笔固定值',
  `cost_rate` decimal(10, 6) NULL DEFAULT NULL COMMENT '代理商成本扣率',
  `cost_capping` decimal(10, 6) NULL DEFAULT NULL COMMENT '代理商成本封顶',
  `cost_safeline` decimal(10, 6) NULL DEFAULT NULL COMMENT '代理商成本保底',
  `check_status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '审核状态：0未审核，1 已审核',
  `lock_status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '锁定状态：0未锁定；1锁定',
  `ladder1_rate` decimal(10, 6) NULL DEFAULT NULL COMMENT '阶梯区间1费率',
  `ladder1_max` decimal(20, 4) NULL DEFAULT NULL COMMENT '阶梯区间1上限',
  `ladder2_rate` decimal(10, 6) NULL DEFAULT NULL COMMENT '阶梯区间2费率',
  `ladder2_max` decimal(20, 4) NULL DEFAULT NULL COMMENT '阶梯区间2上限',
  `ladder3_rate` decimal(10, 6) NULL DEFAULT NULL COMMENT '阶梯区间3费率',
  `ladder3_max` decimal(20, 4) NULL DEFAULT NULL COMMENT '阶梯区间3上限',
  `ladder4_rate` decimal(10, 6) NULL DEFAULT NULL COMMENT '阶梯区间4费率',
  `ladder4_max` decimal(20, 4) NULL DEFAULT NULL COMMENT '阶梯区间4上限',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `agent_id_UNIQUE`(`agent_no`, `service_id`, `card_type`, `holidays_mark`) USING BTREE,
  INDEX `service_node_index`(`service_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13375003 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '代理商分润规则' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for agent_shiro_rigth
-- ----------------------------
DROP TABLE IF EXISTS `agent_shiro_rigth`;
CREATE TABLE `agent_shiro_rigth`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `rigth_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限编码',
  `rigth_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限名称',
  `rigth_comment` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限说明',
  `rigth_type` int(11) NULL DEFAULT NULL COMMENT '权限类型',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 550 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '代理商权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for agent_shiro_role
-- ----------------------------
DROP TABLE IF EXISTS `agent_shiro_role`;
CREATE TABLE `agent_shiro_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `role_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色编号',
  `role_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  `role_remake` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色说明',
  `role_state` int(11) NULL DEFAULT NULL COMMENT '角色状态',
  `create_operator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后修改时间',
  `scope` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色所属范围  存放代理商ID,空为超级管理员角色，',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 191 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '代理商角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for agent_shiro_role_oem
-- ----------------------------
DROP TABLE IF EXISTS `agent_shiro_role_oem`;
CREATE TABLE `agent_shiro_role_oem`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `role_id` int(11) NULL DEFAULT NULL COMMENT '角色id',
  `agent_oem` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属品牌',
  `agent_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商类型',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `status` int(2) NOT NULL DEFAULT 0 COMMENT '0：可编辑1：默认',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 49 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for agent_shiro_user
-- ----------------------------
DROP TABLE IF EXISTS `agent_shiro_user`;
CREATE TABLE `agent_shiro_user`  (
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
  `update_pwd_time` timestamp(0) NOT NULL COMMENT '修改密码时间',
  `dept_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '代理商用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for agent_sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `agent_sys_menu`;
CREATE TABLE `agent_sys_menu`  (
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
) ENGINE = InnoDB AUTO_INCREMENT = 628 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for agent_terminal_operate
-- ----------------------------
DROP TABLE IF EXISTS `agent_terminal_operate`;
CREATE TABLE `agent_terminal_operate`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `agent_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商编号',
  `sn` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作的sn',
  `oper_detail_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '具体操作类型 1-出/入库，2-回收/被回收',
  `oper_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '筛选栏类型 1-入库  2-出库',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_sn_agent_type`(`agent_no`, `sn`, `oper_detail_type`, `oper_type`) USING BTREE COMMENT '保证数据只有一条记录',
  INDEX `idx_create_time`(`create_time`) USING BTREE,
  INDEX `idx_sn`(`sn`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 94 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '机具操作记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for agent_trans
-- ----------------------------
DROP TABLE IF EXISTS `agent_trans`;
CREATE TABLE `agent_trans`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `agent_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商ID',
  `service_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '服务ID',
  `card_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银行卡种类',
  `holidays_mark` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '节假日标志',
  `enter_accou_date` date NULL DEFAULT NULL COMMENT '记账日期',
  `time_area` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '时间段(1到24)',
  `accum_amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '累计交易金额',
  `accum_count` decimal(20, 2) NULL DEFAULT NULL COMMENT '累计交易笔数',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `agent_id_UNIQUE`(`agent_id`, `service_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '代理商交易累计表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for agent_trans_collect
-- ----------------------------
DROP TABLE IF EXISTS `agent_trans_collect`;
CREATE TABLE `agent_trans_collect`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `trans_time` date NULL DEFAULT NULL COMMENT '系统当天时间',
  `agent_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商ID',
  `agent_node` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商节点',
  `bp_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务产品ID',
  `service_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '服务ID',
  `trans_amount` decimal(30, 2) NULL DEFAULT NULL COMMENT '交易金额',
  `single_count` int(10) NULL DEFAULT NULL COMMENT '交易笔数',
  `agent_share_amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '分润金额',
  `calc_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '统计时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 63988 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '代理商交易汇总表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for agent_user_rigth
-- ----------------------------
DROP TABLE IF EXISTS `agent_user_rigth`;
CREATE TABLE `agent_user_rigth`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户ID',
  `rigth_id` int(11) NULL DEFAULT NULL COMMENT '权限ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '代理商用户权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for agent_user_role
-- ----------------------------
DROP TABLE IF EXISTS `agent_user_role`;
CREATE TABLE `agent_user_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户ID',
  `role_id` int(11) NULL DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3216 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '代理商用户角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for agent_valid
-- ----------------------------
DROP TABLE IF EXISTS `agent_valid`;
CREATE TABLE `agent_valid`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `mobile_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `valid_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` timestamp(0) NULL DEFAULT NULL,
  `team_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代表状态0已失效1正常',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1141 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for agent_web_info
-- ----------------------------
DROP TABLE IF EXISTS `agent_web_info`;
CREATE TABLE `agent_web_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `issue_object` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发布对象（直营，OEM）',
  `team_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组织ID',
  `logo` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'LOGO',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '代理商WEB软件信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for app_info
-- ----------------------------
DROP TABLE IF EXISTS `app_info`;
CREATE TABLE `app_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_no` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户端编号',
  `app_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户端名称',
  `team_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组织编号',
  `team_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组织名称',
  `last_version` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最新版本号',
  `status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '客户端状态:0不可用；1可用；2强制更新；3提醒更新；',
  `apply` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '1为代理商使用，2为商户使用',
  `code_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '二维码图片路径',
  `parent_id` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '上级',
  `login_limit` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '2' COMMENT '登录限制：1 单点登录，2 多点登录',
  `use_require` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '是否可用于进件业务产品：0 否，1 是',
  `protocol_ver` int(6) NULL DEFAULT 0 COMMENT '用户隐私政策版本号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `app_org_unique`(`app_no`, `team_id`) USING BTREE COMMENT '一个组织对于同一个APP只能有一条记录'
) ENGINE = InnoDB AUTO_INCREMENT = 52 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for app_reg_check_num
-- ----------------------------
DROP TABLE IF EXISTS `app_reg_check_num`;
CREATE TABLE `app_reg_check_num`  (
  `id` int(32) NOT NULL AUTO_INCREMENT,
  `mobile` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `teamId` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组织ID',
  `check_identity_status` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态',
  `reg_count` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '次数',
  `reg_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `mobile_teamId`(`mobile`, `teamId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4109 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for area_info
-- ----------------------------
DROP TABLE IF EXISTS `area_info`;
CREATE TABLE `area_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `code` int(8) NULL DEFAULT NULL COMMENT '编码',
  `parent_id` int(8) NULL DEFAULT NULL COMMENT '上级id',
  `level` int(2) NULL DEFAULT NULL COMMENT '级别',
  `zf_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '中付对应省市名称',
  `ys_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银盛对应省市名称',
  `sft_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '盛付通对应省市名称',
  `sft_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '盛付通对应省市名称code',
  `zg_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '中刚的省市区code',
  `zg_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '中刚的省市区名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5006 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '地区信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for area_info2
-- ----------------------------
DROP TABLE IF EXISTS `area_info2`;
CREATE TABLE `area_info2`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `code` int(8) NULL DEFAULT NULL COMMENT '编码',
  `parent_id` int(8) NULL DEFAULT NULL COMMENT '上级id',
  `level` int(2) NULL DEFAULT NULL COMMENT '级别',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5004 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '地区信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for area_info3
-- ----------------------------
DROP TABLE IF EXISTS `area_info3`;
CREATE TABLE `area_info3`  (
  `id` bigint(20) NOT NULL DEFAULT 0 COMMENT 'id',
  `name` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `zf_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '中付对应省市名称',
  `ys_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银盛对应省市名称',
  `sft_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '盛付通对应省市名称',
  `sft_code` bigint(20) NULL DEFAULT NULL COMMENT '盛付通对应省市名称code',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '地区信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for area_info_bak
-- ----------------------------
DROP TABLE IF EXISTS `area_info_bak`;
CREATE TABLE `area_info_bak`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `code` int(8) NULL DEFAULT NULL COMMENT '编码',
  `parent_id` int(8) NULL DEFAULT NULL COMMENT '上级id',
  `level` int(2) NULL DEFAULT NULL COMMENT '级别',
  `yl_code` int(10) NULL DEFAULT NULL COMMENT '银联code',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5004 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '地区信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for area_info_bak0831
-- ----------------------------
DROP TABLE IF EXISTS `area_info_bak0831`;
CREATE TABLE `area_info_bak0831`  (
  `id` bigint(20) NOT NULL DEFAULT 0 COMMENT 'id',
  `name` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `code` int(8) NULL DEFAULT NULL COMMENT '编码',
  `parent_id` int(8) NULL DEFAULT NULL COMMENT '上级id',
  `level` int(2) NULL DEFAULT NULL COMMENT '级别'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for area_info_bak0927
-- ----------------------------
DROP TABLE IF EXISTS `area_info_bak0927`;
CREATE TABLE `area_info_bak0927`  (
  `id` bigint(20) NOT NULL DEFAULT 0 COMMENT 'id',
  `name` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `code` int(8) NULL DEFAULT NULL COMMENT '编码',
  `parent_id` int(8) NULL DEFAULT NULL COMMENT '上级id',
  `level` int(2) NULL DEFAULT NULL COMMENT '级别',
  `yl_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `yl_code` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `zf_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '中付对应省市名称',
  `ys_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银盛对应省市名称',
  `sft_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '盛付通对应省市名称',
  `sft_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '盛付通对应省市名称code'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for area_info_bak1011
-- ----------------------------
DROP TABLE IF EXISTS `area_info_bak1011`;
CREATE TABLE `area_info_bak1011`  (
  `id` bigint(20) NOT NULL DEFAULT 0 COMMENT 'id',
  `name` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `code` int(8) NULL DEFAULT NULL COMMENT '编码',
  `parent_id` int(8) NULL DEFAULT NULL COMMENT '上级id',
  `level` int(2) NULL DEFAULT NULL COMMENT '级别',
  `yl_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `yl_code` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for area_info_bak20190411
-- ----------------------------
DROP TABLE IF EXISTS `area_info_bak20190411`;
CREATE TABLE `area_info_bak20190411`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `code` int(8) NULL DEFAULT NULL COMMENT '编码',
  `parent_id` int(8) NULL DEFAULT NULL COMMENT '上级id',
  `level` int(2) NULL DEFAULT NULL COMMENT '级别',
  `zf_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '中付对应省市名称',
  `ys_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银盛对应省市名称',
  `sft_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '盛付通对应省市名称',
  `sft_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '盛付通对应省市名称code',
  `zg_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '中刚的省市区code',
  `zg_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '中刚的省市区名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5006 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '地区信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for auditor_manager
-- ----------------------------
DROP TABLE IF EXISTS `auditor_manager`;
CREATE TABLE `auditor_manager`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `auditor_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '审核人员ID',
  `bp_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务产品ID',
  `status` int(11) NULL DEFAULT NULL COMMENT '状态，0：关闭，1：开启',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 384 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for auth_card
-- ----------------------------
DROP TABLE IF EXISTS `auth_card`;
CREATE TABLE `auth_card`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `card_no` varchar(55) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `merchant_no` varchar(55) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` timestamp(0) NULL DEFAULT NULL,
  `mobile_no` varchar(55) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 27 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for auto_check_result
-- ----------------------------
DROP TABLE IF EXISTS `auto_check_result`;
CREATE TABLE `auto_check_result`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `rule_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '规则标识',
  `check_result` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '验证结果',
  `check_verdict` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '验证结论',
  `check_info` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '校验数据',
  `merchant_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号',
  `res_info` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '响应信息',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `bp_id` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务产品ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 10000 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '自动审核数据' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for auto_check_rule
-- ----------------------------
DROP TABLE IF EXISTS `auto_check_rule`;
CREATE TABLE `auto_check_rule`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `rule_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '规则标识',
  `rule_dis` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '规则说明',
  `is_open` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '是否打开 1开 0 是关',
  `is_pass` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '是否必过 1开 0 是关',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 17 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '自动审核' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for awards_config
-- ----------------------------
DROP TABLE IF EXISTS `awards_config`;
CREATE TABLE `awards_config`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `func_code` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '对应抽奖配置luck_config的func_code',
  `award_desc` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '奖项说明',
  `award_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '奖品名称',
  `award_count` int(11) NOT NULL COMMENT '奖品数量',
  `prob` decimal(6, 3) NOT NULL COMMENT '中奖概率(单位%)',
  `sort` int(11) NOT NULL COMMENT '序列号',
  `day_count` int(11) NOT NULL COMMENT '每天最多派数量',
  `day_lottery_count` int(11) NOT NULL DEFAULT 0 COMMENT '每天中奖次数数量,凌晨更新为0',
  `mer_day_count` int(11) NOT NULL COMMENT '单个用户每天最多中奖次数',
  `award_hit` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '中奖提示',
  `award_pic` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '奖品图片',
  `award_type` int(11) NOT NULL DEFAULT 4 COMMENT '奖品类型1鼓励金2超级积分3现金劵4未中奖',
  `status` int(11) NOT NULL DEFAULT 0 COMMENT '状态 0:关闭，1:开启 2删除',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `operator` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '操作人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 53 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '奖品信息配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for awards_config_blacklist
-- ----------------------------
DROP TABLE IF EXISTS `awards_config_blacklist`;
CREATE TABLE `awards_config_blacklist`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `award_config_id` int(11) NOT NULL COMMENT '奖项配置 ID',
  `merchant_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户编号',
  `creater` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 46 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '奖项黑名单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for awards_item
-- ----------------------------
DROP TABLE IF EXISTS `awards_item`;
CREATE TABLE `awards_item`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `award_config_id` int(11) NOT NULL COMMENT '奖项配置 ID',
  `coupon_id` int(11) NULL DEFAULT NULL COMMENT '奖项编号,对应coupon_activity_entity的ID',
  `item_count` int(11) NULL DEFAULT NULL COMMENT '数量',
  `money` decimal(8, 2) NULL DEFAULT NULL COMMENT '金额',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 522 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '奖项明细' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for awards_item_log
-- ----------------------------
DROP TABLE IF EXISTS `awards_item_log`;
CREATE TABLE `awards_item_log`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `awards_recode_id` int(11) NOT NULL COMMENT '中奖记录ID',
  `coupon_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '奖项编号',
  `money` decimal(8, 2) NULL DEFAULT NULL COMMENT '金额',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `status` int(1) NULL DEFAULT 0 COMMENT '0未发放1已发放(暂用于系统内部)',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `Index_1`(`awards_recode_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2707 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '奖项发放明细' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for awards_recode
-- ----------------------------
DROP TABLE IF EXISTS `awards_recode`;
CREATE TABLE `awards_recode`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `awards_config_id` int(11) NULL DEFAULT NULL COMMENT '奖品信息配置',
  `seq` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '奖品码',
  `award_desc` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '奖项说明',
  `award_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '奖品名称',
  `status` int(11) NOT NULL COMMENT '状态 1:未中奖，2:已中奖，3:已发奖',
  `mobilephone` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '手机',
  `user_code` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户ID',
  `play_time` datetime(0) NULL DEFAULT NULL COMMENT '抽奖时间',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `award_type` int(11) NULL DEFAULT NULL COMMENT '奖品类型1鼓励金2超级积分3现金劵',
  `activity_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '抽奖活动名称',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `Ind_user_id`(`user_code`) USING BTREE,
  INDEX `Ind_mobile`(`mobilephone`) USING BTREE,
  INDEX `ind_play_date`(`play_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4472 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '中奖项信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for awards_times
-- ----------------------------
DROP TABLE IF EXISTS `awards_times`;
CREATE TABLE `awards_times`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_code` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户ID',
  `status` int(11) NOT NULL DEFAULT 0 COMMENT '状态 0:未抽奖，1:已抽奖',
  `order_no` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单号',
  `play_time` datetime(0) NULL DEFAULT NULL COMMENT '抽奖时间',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `ind_user_status`(`user_code`, `status`) USING BTREE,
  INDEX `ind_date`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 114682 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '抽奖机会' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bag_extraction_his
-- ----------------------------
DROP TABLE IF EXISTS `bag_extraction_his`;
CREATE TABLE `bag_extraction_his`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `mobile_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '提现金额',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '提现时间',
  `settle_time` timestamp(0) NULL DEFAULT NULL COMMENT '结算时间',
  `account_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '帐号',
  `account_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '帐户名',
  `fee` decimal(10, 2) NULL DEFAULT NULL COMMENT '手续费',
  `cash_profit` decimal(10, 2) NULL DEFAULT NULL COMMENT 'T+0收益',
  `settle_days` int(11) NULL DEFAULT NULL COMMENT '钱包类型  0当天余额 1历史余额',
  `cnaps` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '联行号',
  `bank_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '开户行',
  `open_status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '状态  0-未审核 1-审核通过 2-审核不通过 3-体现中 4-提现成功 5-提现失败',
  `check_remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '审核信息',
  `cash_channel` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '提现渠道    HXB-华夏银企直连',
  `cash_status` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '提现状态  1-未发送  2-发送成功  3-发送失败  4-发送未知 5-转账成功 6-转账失败',
  `cash_remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '提现信息',
  `cash_file_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '提现查询编号',
  `cash_time` datetime(0) NULL DEFAULT NULL COMMENT '发送银行转账时间',
  `is_back` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否需要余额冲正  1：需要   空或其他为不需要',
  `back_status` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '0:冲正失败 1：冲正成功  2：发送冲正失败 3：发送冲正超时',
  `back_remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `lock_stamp` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '提现记录锁',
  `bank_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '清算行号',
  `settle_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '结算金额',
  `check_id` int(11) NULL DEFAULT NULL COMMENT '审核人id',
  `check_person` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '审核人',
  `check_time` timestamp(0) NULL DEFAULT NULL COMMENT '审核时间',
  `app_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `batch` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `profit_status` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '分润收益状态0.未计算1已计算2失败',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bank_code
-- ----------------------------
DROP TABLE IF EXISTS `bank_code`;
CREATE TABLE `bank_code`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `bank_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '行号',
  `bank_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '行别',
  `bank_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '行名',
  `type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 291 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '行号行别对应表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for banner_info
-- ----------------------------
DROP TABLE IF EXISTS `banner_info`;
CREATE TABLE `banner_info`  (
  `banner_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Banner ID',
  `banner_name` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'Banner名称',
  `weight` decimal(10, 2) NULL DEFAULT NULL COMMENT '公告权重',
  `online_time` timestamp(0) NULL DEFAULT NULL COMMENT '上线时间',
  `offline_time` timestamp(0) NULL DEFAULT NULL COMMENT '下线时间',
  `team_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组织ID',
  `agent_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '一级代理商编号',
  `banner_content` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'banner文字',
  `banner_status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态，0-关闭，1-开启',
  `banner_attachment` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片',
  `banner_link` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '链接',
  `app_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'app编号',
  `banner_position` int(11) NULL DEFAULT NULL COMMENT 'banner位置',
  `banner_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'banner编号',
  PRIMARY KEY (`banner_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 262 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'Banner信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for batch_job_execution
-- ----------------------------
DROP TABLE IF EXISTS `batch_job_execution`;
CREATE TABLE `batch_job_execution`  (
  `JOB_EXECUTION_ID` bigint(20) NOT NULL,
  `VERSION` bigint(20) NULL DEFAULT NULL,
  `JOB_INSTANCE_ID` bigint(20) NOT NULL,
  `CREATE_TIME` datetime(0) NULL,
  `START_TIME` datetime(0) NULL DEFAULT NULL,
  `END_TIME` datetime(0) NULL DEFAULT NULL,
  `STATUS` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `EXIT_CODE` varchar(2500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `EXIT_MESSAGE` varchar(2500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `LAST_UPDATED` datetime(0) NULL DEFAULT NULL,
  `JOB_CONFIGURATION_LOCATION` varchar(2500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`JOB_EXECUTION_ID`) USING BTREE,
  INDEX `JOB_INST_EXEC_FK`(`JOB_INSTANCE_ID`) USING BTREE,
  CONSTRAINT `JOB_INST_EXEC_FK` FOREIGN KEY (`JOB_INSTANCE_ID`) REFERENCES `batch_job_instance` (`JOB_INSTANCE_ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for batch_job_execution_context
-- ----------------------------
DROP TABLE IF EXISTS `batch_job_execution_context`;
CREATE TABLE `batch_job_execution_context`  (
  `JOB_EXECUTION_ID` bigint(20) NOT NULL,
  `SHORT_CONTEXT` varchar(2500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `SERIALIZED_CONTEXT` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  PRIMARY KEY (`JOB_EXECUTION_ID`) USING BTREE,
  CONSTRAINT `JOB_EXEC_CTX_FK` FOREIGN KEY (`JOB_EXECUTION_ID`) REFERENCES `batch_job_execution` (`JOB_EXECUTION_ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for batch_job_execution_params
-- ----------------------------
DROP TABLE IF EXISTS `batch_job_execution_params`;
CREATE TABLE `batch_job_execution_params`  (
  `JOB_EXECUTION_ID` bigint(20) NOT NULL,
  `TYPE_CD` varchar(6) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `KEY_NAME` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `STRING_VAL` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `DATE_VAL` datetime(0) NULL DEFAULT NULL,
  `LONG_VAL` bigint(20) NULL DEFAULT NULL,
  `DOUBLE_VAL` double NULL DEFAULT NULL,
  `IDENTIFYING` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  INDEX `JOB_EXEC_PARAMS_FK`(`JOB_EXECUTION_ID`) USING BTREE,
  CONSTRAINT `JOB_EXEC_PARAMS_FK` FOREIGN KEY (`JOB_EXECUTION_ID`) REFERENCES `batch_job_execution` (`JOB_EXECUTION_ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for batch_job_execution_seq
-- ----------------------------
DROP TABLE IF EXISTS `batch_job_execution_seq`;
CREATE TABLE `batch_job_execution_seq`  (
  `ID` bigint(20) NOT NULL,
  `UNIQUE_KEY` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  UNIQUE INDEX `UNIQUE_KEY_UN`(`UNIQUE_KEY`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for batch_job_instance
-- ----------------------------
DROP TABLE IF EXISTS `batch_job_instance`;
CREATE TABLE `batch_job_instance`  (
  `JOB_INSTANCE_ID` bigint(20) NOT NULL,
  `VERSION` bigint(20) NULL DEFAULT NULL,
  `JOB_NAME` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `JOB_KEY` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`JOB_INSTANCE_ID`) USING BTREE,
  UNIQUE INDEX `JOB_INST_UN`(`JOB_NAME`, `JOB_KEY`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for batch_job_seq
-- ----------------------------
DROP TABLE IF EXISTS `batch_job_seq`;
CREATE TABLE `batch_job_seq`  (
  `ID` bigint(20) NOT NULL,
  `UNIQUE_KEY` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  UNIQUE INDEX `UNIQUE_KEY_UN`(`UNIQUE_KEY`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for batch_step_execution
-- ----------------------------
DROP TABLE IF EXISTS `batch_step_execution`;
CREATE TABLE `batch_step_execution`  (
  `STEP_EXECUTION_ID` bigint(20) NOT NULL,
  `VERSION` bigint(20) NOT NULL,
  `STEP_NAME` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `JOB_EXECUTION_ID` bigint(20) NOT NULL,
  `START_TIME` datetime(0) NULL,
  `END_TIME` datetime(0) NULL DEFAULT NULL,
  `STATUS` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `COMMIT_COUNT` bigint(20) NULL DEFAULT NULL,
  `READ_COUNT` bigint(20) NULL DEFAULT NULL,
  `FILTER_COUNT` bigint(20) NULL DEFAULT NULL,
  `WRITE_COUNT` bigint(20) NULL DEFAULT NULL,
  `READ_SKIP_COUNT` bigint(20) NULL DEFAULT NULL,
  `WRITE_SKIP_COUNT` bigint(20) NULL DEFAULT NULL,
  `PROCESS_SKIP_COUNT` bigint(20) NULL DEFAULT NULL,
  `ROLLBACK_COUNT` bigint(20) NULL DEFAULT NULL,
  `EXIT_CODE` varchar(2500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `EXIT_MESSAGE` varchar(2500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `LAST_UPDATED` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`STEP_EXECUTION_ID`) USING BTREE,
  INDEX `JOB_EXEC_STEP_FK`(`JOB_EXECUTION_ID`) USING BTREE,
  CONSTRAINT `JOB_EXEC_STEP_FK` FOREIGN KEY (`JOB_EXECUTION_ID`) REFERENCES `batch_job_execution` (`JOB_EXECUTION_ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for batch_step_execution_context
-- ----------------------------
DROP TABLE IF EXISTS `batch_step_execution_context`;
CREATE TABLE `batch_step_execution_context`  (
  `STEP_EXECUTION_ID` bigint(20) NOT NULL,
  `SHORT_CONTEXT` varchar(2500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `SERIALIZED_CONTEXT` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  PRIMARY KEY (`STEP_EXECUTION_ID`) USING BTREE,
  CONSTRAINT `STEP_EXEC_CTX_FK` FOREIGN KEY (`STEP_EXECUTION_ID`) REFERENCES `batch_step_execution` (`STEP_EXECUTION_ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for batch_step_execution_seq
-- ----------------------------
DROP TABLE IF EXISTS `batch_step_execution_seq`;
CREATE TABLE `batch_step_execution_seq`  (
  `ID` bigint(20) NOT NULL,
  `UNIQUE_KEY` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  UNIQUE INDEX `UNIQUE_KEY_UN`(`UNIQUE_KEY`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bindorunbindtmstpos
-- ----------------------------
DROP TABLE IF EXISTS `bindorunbindtmstpos`;
CREATE TABLE `bindorunbindtmstpos`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `merchant_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号',
  `mbp_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '进件ID',
  `unionpay_mer_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银联号',
  `sn` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'sn',
  `ter_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '终端号',
  `status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '0失败1成功',
  `batch_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '批次',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2536 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '机具解绑数据插入表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for black_data_deal_log
-- ----------------------------
DROP TABLE IF EXISTS `black_data_deal_log`;
CREATE TABLE `black_data_deal_log`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `orig_order_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '原始black_data_info表order_no',
  `operate_type` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '操作类型 0添加黑名单资料信息 1风控回复处理  2风控添加备注',
  `operator` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '操作人',
  `operate_table` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作表',
  `pre_value` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '操作前的值',
  `after_value` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '操作后的值',
  `operate_detail` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '日志操作详细',
  `operate_time` timestamp(0) NULL DEFAULT NULL COMMENT '操作时间',
  `bak1` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `bak2` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `ind_order_no`(`orig_order_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1162 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '黑名单资料处理日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for black_data_deal_record
-- ----------------------------
DROP TABLE IF EXISTS `black_data_deal_record`;
CREATE TABLE `black_data_deal_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '处理单号',
  `orig_order_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '原始black_data_info表order_no',
  `risk_deal_template_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '风控处理模板编号',
  `risk_deal_msg` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '风控处理内容',
  `status` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '1' COMMENT '处理记录状态 0已失效 1正常 2已完成（商户回复后即为已完成）3.解冻',
  `creater` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建人 system为系统',
  `create_time` timestamp(0) NOT NULL COMMENT '数据最后更新时间',
  `last_update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ind_order_no`(`order_no`) USING BTREE,
  INDEX `ind_orig_order_no`(`orig_order_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1144 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '黑名单资料风控处理记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for black_data_deal_template
-- ----------------------------
DROP TABLE IF EXISTS `black_data_deal_template`;
CREATE TABLE `black_data_deal_template`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `template_no` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '模板编号',
  `template_content` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '模板内容',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creator` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '0 禁用 1启用',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ind_no`(`template_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 39 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '风控处理模板表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for black_data_info
-- ----------------------------
DROP TABLE IF EXISTS `black_data_info`;
CREATE TABLE `black_data_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '处理单号',
  `merchant_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户编号',
  `trans_order_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '交易订单编号',
  `status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '1' COMMENT '资料处理信息状态 0已完成 1正常',
  `mer_last_deal_status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '商户最后回复状态 0初始化  1 未回复 2已回复',
  `mer_last_deal_time` timestamp(0) NULL DEFAULT NULL COMMENT '商户最后回复时间',
  `risk_last_deal_status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '风控最后处理状态 0未处理 1已处理 2解冻',
  `risk_last_deal_template_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '风控最后处理模板编号',
  `risk_last_deal_time` timestamp(0) NULL DEFAULT NULL COMMENT '风控最后处理时间',
  `risk_last_deal_operator` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '风控最后处理人',
  `risk_last_remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '风控备注',
  `one_agent_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '一级代理商编号',
  `one_agent_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '一级代理商名称',
  `agent_node` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商编号节点',
  `have_trigger_his` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户是否有历史触发记录 0没有 1有',
  `team_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组织ID',
  `black_creator` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '该商户黑名单记录的创建人',
  `black_create_remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '该商户黑名单记录创建时的备注',
  `mer_risk_rules_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户所有触发过的风险事件规则编号',
  `bak1` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `bak2` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '数据最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ind_order_no`(`order_no`) USING BTREE,
  UNIQUE INDEX `unique_merchant_order_no`(`merchant_no`, `trans_order_no`) USING BTREE,
  INDEX `ind_merchant_no`(`merchant_no`) USING BTREE,
  INDEX `ind_trans_order_no`(`trans_order_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 443 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '黑名单资料处理信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for black_data_reply_record
-- ----------------------------
DROP TABLE IF EXISTS `black_data_reply_record`;
CREATE TABLE `black_data_reply_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '回复单号',
  `merchant_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户编号',
  `orig_order_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '原始black_data_info表order_no',
  `deal_record_order_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '风控处理单号',
  `status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '1' COMMENT '回复记录状态 0已失效 1正常 ',
  `reply_remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '回复说明',
  `reply_files_name` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '回复附件名称,以英文逗号隔开',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `replier_type` int(2) NOT NULL DEFAULT 0 COMMENT '回复者类型0商户回复类型，1代理商回复类型',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `ind_mer`(`merchant_no`) USING BTREE,
  INDEX `ind_orig_order`(`orig_order_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 710 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '黑名单资料商户回复记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for black_oper_log
-- ----------------------------
DROP TABLE IF EXISTS `black_oper_log`;
CREATE TABLE `black_oper_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `roll_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '编号',
  `black_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '黑名单类型',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `operation_type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '操作类型',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '操作人',
  `remark` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17738 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '黑名单操作记录日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for blacklist_amount
-- ----------------------------
DROP TABLE IF EXISTS `blacklist_amount`;
CREATE TABLE `blacklist_amount`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `jump_rule_id` int(11) NOT NULL COMMENT '跳转路由集群规则ID',
  `amount` decimal(20, 2) NOT NULL COMMENT '金额',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operator` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作人',
  `last_update_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '金额黑名单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for boss_oper_log
-- ----------------------------
DROP TABLE IF EXISTS `boss_oper_log`;
CREATE TABLE `boss_oper_log`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '日志编号',
  `user_id` int(11) NULL DEFAULT NULL COMMENT ' 操作用户',
  `user_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ' 操作人名称',
  `request_method` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求方法',
  `request_params` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '请求参数',
  `return_result` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '返回结果',
  `oper_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单代码',
  `method_desc` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ' 方法描述',
  `oper_ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求ip',
  `oper_status` int(11) NULL DEFAULT NULL COMMENT '操作状态(1：成功0：失败)',
  `oper_time` datetime(0) NOT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 149809 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统操作日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for boss_right_menu
-- ----------------------------
DROP TABLE IF EXISTS `boss_right_menu`;
CREATE TABLE `boss_right_menu`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `right_id` int(11) NOT NULL COMMENT '权限ID',
  `menu_id` int(11) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `right_menu_index`(`right_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 417791 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for boss_role_right
-- ----------------------------
DROP TABLE IF EXISTS `boss_role_right`;
CREATE TABLE `boss_role_right`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NULL DEFAULT NULL COMMENT '角色id',
  `right_id` int(11) NULL DEFAULT NULL COMMENT '权限id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `role_right_index`(`role_id`, `right_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 788 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'BOSS角色权限表 ' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for boss_shiro_right
-- ----------------------------
DROP TABLE IF EXISTS `boss_shiro_right`;
CREATE TABLE `boss_shiro_right`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `right_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限编码',
  `right_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限名称',
  `right_comment` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限说明',
  `right_type` int(11) NULL DEFAULT NULL COMMENT '权限类型',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 171 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'BOSS权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for boss_shiro_role
-- ----------------------------
DROP TABLE IF EXISTS `boss_shiro_role`;
CREATE TABLE `boss_shiro_role`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色编号',
  `role_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  `role_remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色说明',
  `role_status` int(11) NULL DEFAULT NULL COMMENT '角色状态，0：无效，1：有效',
  `create_operator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'BOSS角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for boss_shiro_user
-- ----------------------------
DROP TABLE IF EXISTS `boss_shiro_user`;
CREATE TABLE `boss_shiro_user`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户唯一标识',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户编码',
  `real_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `tel_no` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电话',
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `status` int(11) NULL DEFAULT NULL COMMENT '用户状态，0-无效，1-有效',
  `theme` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户样式主题',
  `create_operator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_pwd_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改密码时间',
  `dept_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_name`(`user_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 208 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'BOSS用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for boss_sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `boss_sys_menu`;
CREATE TABLE `boss_sys_menu`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `parent_id` int(11) NULL DEFAULT NULL,
  `menu_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `menu_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `menu_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `rigth_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `menu_level` int(11) NULL DEFAULT NULL,
  `menu_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `order_no` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `log_flag` int(11) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `menu_code`(`menu_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1315 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for boss_sys_menu_copy
-- ----------------------------
DROP TABLE IF EXISTS `boss_sys_menu_copy`;
CREATE TABLE `boss_sys_menu_copy`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `parent_id` int(11) NULL DEFAULT NULL,
  `menu_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `menu_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `menu_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `rigth_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `menu_level` int(11) NULL DEFAULT NULL,
  `menu_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `order_no` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `log_flag` int(11) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `menu_code`(`menu_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 848 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for boss_sys_menubak
-- ----------------------------
DROP TABLE IF EXISTS `boss_sys_menubak`;
CREATE TABLE `boss_sys_menubak`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `parent_id` int(11) NULL DEFAULT NULL,
  `menu_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `menu_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `menu_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `rigth_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `menu_level` int(11) NULL DEFAULT NULL,
  `menu_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `order_no` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 248 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for boss_user_menu
-- ----------------------------
DROP TABLE IF EXISTS `boss_user_menu`;
CREATE TABLE `boss_user_menu`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户ID',
  `menu_id` int(11) NULL DEFAULT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_menu_index`(`user_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'BOSS用户菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for boss_user_right
-- ----------------------------
DROP TABLE IF EXISTS `boss_user_right`;
CREATE TABLE `boss_user_right`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户ID',
  `right_id` int(11) NULL DEFAULT NULL COMMENT '权限ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_right_index`(`user_id`, `right_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1199 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'BOSS用户权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for boss_user_role
-- ----------------------------
DROP TABLE IF EXISTS `boss_user_role`;
CREATE TABLE `boss_user_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户ID',
  `role_id` int(11) NULL DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_role_index`(`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 160 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'BOSS用户角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for branch_info
-- ----------------------------
DROP TABLE IF EXISTS `branch_info`;
CREATE TABLE `branch_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `branch_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `branch_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `bank_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 120904 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for busi_merchant_commodity
-- ----------------------------
DROP TABLE IF EXISTS `busi_merchant_commodity`;
CREATE TABLE `busi_merchant_commodity`  (
  `comdy_id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `comdy_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `merchant_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '经营商户ID',
  `comdy_price` decimal(10, 6) NULL DEFAULT NULL COMMENT '商品价格',
  `comdy_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品类别',
  PRIMARY KEY (`comdy_id`) USING BTREE,
  UNIQUE INDEX `comdy_id_UNIQUE`(`comdy_id`) USING BTREE,
  INDEX `merchant_id_index`(`merchant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '经营商户商品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for busi_merchant_info
-- ----------------------------
DROP TABLE IF EXISTS `busi_merchant_info`;
CREATE TABLE `busi_merchant_info`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `merchant_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '经营商户ID',
  `merchant_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '经营商户名称',
  `merchant_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户类型:1-实际商户，2-虚拟商户',
  `lawyer` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '法人姓名',
  `id_card_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '法人身份证号',
  `province` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '经营地址（省）',
  `city` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '经营地址（市）',
  `address` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '经营地址:详细地址',
  `mobilephone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `email` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'Email',
  `account_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账户类型:1-对公，2-对私',
  `bank_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '开户行全称',
  `cnaps_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联行行号',
  `account_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '开户名',
  `account_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '开户账号',
  `mcc` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户MCC',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `merchant_id_UNIQUE`(`merchant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '经营商户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for business_product_app
-- ----------------------------
DROP TABLE IF EXISTS `business_product_app`;
CREATE TABLE `business_product_app`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `bp_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务产品ID',
  `app_no` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'app编号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `bp_id_UNIQUE`(`bp_id`, `app_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 128 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '业务产品和app的关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for business_product_define
-- ----------------------------
DROP TABLE IF EXISTS `business_product_define`;
CREATE TABLE `business_product_define`  (
  `bp_id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '业务产品ID',
  `bp_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `agent_show_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商展示名称',
  `sale_starttime` date NULL DEFAULT NULL COMMENT '可销售起始日期',
  `sale_endtime` date NULL DEFAULT NULL COMMENT '可销售截止日期',
  `proxy` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '可否代理:1-可，0-否',
  `bp_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型:1-个人，2-个体商户，3-企业商户',
  `is_oem` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否OEM:1-是，0-否',
  `team_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'TEAM_INFO ID',
  `own_bp_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '关联自营业务产品ID',
  `two_code` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '二维码',
  `remark` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '说明',
  `bp_img` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '宣传图片',
  `not_check` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证件资料完整时无需人工审核',
  `link` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `rely_hardware` int(11) NULL DEFAULT NULL COMMENT '是否依赖硬件，1：是，0：否',
  `link_product` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '自动开通关联业务产品',
  `allow_web_item` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否允许web进件，0：否，1：是',
  `allow_individual_apply` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '允许单独申请，1：是，0：否',
  `effective_status` tinyint(2) NULL DEFAULT 1 COMMENT '生效状态,0:失效,1:生效,默认为1',
  `create_person` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`bp_id`) USING BTREE,
  UNIQUE INDEX `bp_id_UNIQUE`(`bp_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 436 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '业务产品（服务套餐）定义' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for business_product_group
-- ----------------------------
DROP TABLE IF EXISTS `business_product_group`;
CREATE TABLE `business_product_group`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `group_no` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '群组号；序列号生成',
  `bp_id` bigint(20) NULL DEFAULT NULL COMMENT '业务产品ID',
  `create_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 153 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '业务产品群组表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for business_product_hardware
-- ----------------------------
DROP TABLE IF EXISTS `business_product_hardware`;
CREATE TABLE `business_product_hardware`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `bp_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务产品ID',
  `hp_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '硬件产品ID，如果值为0，则表示不限',
  PRIMARY KEY (`id`, `hp_id`) USING BTREE,
  UNIQUE INDEX `pbh_key`(`bp_id`, `hp_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8326 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '业务产品关联硬件产品明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for business_product_info
-- ----------------------------
DROP TABLE IF EXISTS `business_product_info`;
CREATE TABLE `business_product_info`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `bp_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务产品ID',
  `service_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '服务ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `bp_id_UNIQUE`(`bp_id`, `service_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1936 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '业务产品(服务套餐)明细' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for business_require_item
-- ----------------------------
DROP TABLE IF EXISTS `business_require_item`;
CREATE TABLE `business_require_item`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `bp_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务产品ID',
  `br_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '进件要求项ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `bp_id_UNIQUE`(`bp_id`, `br_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7685 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '业务产品进件要求项表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for card_bins
-- ----------------------------
DROP TABLE IF EXISTS `card_bins`;
CREATE TABLE `card_bins`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `card_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '卡bin',
  `state` int(1) NULL DEFAULT 0 COMMENT '状态:0关闭,1打开',
  `card_type` int(1) NULL DEFAULT 0 COMMENT '卡种:0其他,1贷记卡,2借记卡',
  `card_bank` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发卡银行',
  `currency` int(11) NULL DEFAULT NULL COMMENT '交易币种:取数据字典值',
  `card_style` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '卡类型',
  `remarks` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_id` int(11) NULL DEFAULT NULL COMMENT '创建人id',
  `create_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `card_no_UNIQUE`(`card_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 52 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '卡bin管理列表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for card_first_password
-- ----------------------------
DROP TABLE IF EXISTS `card_first_password`;
CREATE TABLE `card_first_password`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `merchant_no` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号',
  `account_no` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '银行卡号',
  `merchant_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户名称',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `only_index`(`merchant_no`, `account_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '使用过交易密码的交易卡,记录商户号和卡号' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for card_jump_info
-- ----------------------------
DROP TABLE IF EXISTS `card_jump_info`;
CREATE TABLE `card_jump_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `card_no` varchar(37) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '卡号',
  `jump_count` int(11) NOT NULL DEFAULT 0 COMMENT '跳转的次数',
  `create_time` timestamp(0) NOT NULL COMMENT '创建时间',
  `last_jump_time` timestamp(0) NOT NULL COMMENT '最后一次跳转的时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for card_loan_hearten
-- ----------------------------
DROP TABLE IF EXISTS `card_loan_hearten`;
CREATE TABLE `card_loan_hearten`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名称',
  `phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '手机号码',
  `order_type` int(255) NOT NULL COMMENT '订单类型 2信用卡办理 6 贷款',
  `order_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单号',
  `mech_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单所属组织',
  `org_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '机构名称',
  `given_channel` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '赠送渠道',
  `given_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '赠送类型',
  `given_status` int(11) NOT NULL COMMENT '赠送状态 1为成功 2为失败 3未赠送',
  `oper_username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作用户名称',
  `oper_time` datetime(0) NULL DEFAULT NULL COMMENT '操作时间',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `ticket_id` bigint(20) NULL DEFAULT NULL COMMENT '赠送劵id',
  `trans_amount` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易金额',
  `trans_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易时间',
  `status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '5 为成功订单 其他为失败订单',
  `profit` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '佣金',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单备注',
  `success_time` datetime(0) NULL DEFAULT NULL COMMENT '赠送成功时间',
  `merchant_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号',
  `effective_days` int(10) NULL DEFAULT NULL COMMENT ' 赠送有效期',
  `coupon_amount` decimal(30, 2) NULL DEFAULT NULL COMMENT '赠送面值',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 62 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '办卡贷款鼓励日志信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for card_loan_hearten_log
-- ----------------------------
DROP TABLE IF EXISTS `card_loan_hearten_log`;
CREATE TABLE `card_loan_hearten_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `card_loan_hearten_id` bigint(20) NOT NULL,
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名称',
  `phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '手机号码',
  `order_type` int(255) NOT NULL COMMENT '订单类型 2信用卡办理 6 贷款',
  `order_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单号',
  `mech_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单所属组织',
  `org_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '机构名称',
  `given_channel` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '赠送渠道',
  `given_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '赠送类型',
  `given_status` int(11) NOT NULL COMMENT '赠送状态 1为成功 2为失败 3未赠送',
  `oper_username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作用户名称',
  `oper_time` datetime(0) NULL DEFAULT NULL COMMENT '操作时间',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `ticket_id` bigint(20) NULL DEFAULT NULL COMMENT '赠送劵id',
  `trans_amount` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易金额',
  `trans_time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易时间',
  `status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '5 为成功订单 其他为失败订单',
  `profit` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '佣金',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单备注',
  `success_time` datetime(0) NULL DEFAULT NULL COMMENT '赠送成功时间',
  `merchant_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号',
  `effective_days` int(10) NULL DEFAULT NULL COMMENT ' 赠送有效期',
  `coupon_amount` decimal(30, 2) NULL DEFAULT NULL COMMENT '赠送面值',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 158 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '办卡贷款鼓励日志信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cash_back_detail
-- ----------------------------
DROP TABLE IF EXISTS `cash_back_detail`;
CREATE TABLE `cash_back_detail`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `agent_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商编号',
  `cash_back_amount` decimal(13, 2) NULL DEFAULT 0.00 COMMENT '金额',
  `cash_back_switch` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '开关(1打开)',
  `entry_status` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'amount_type类型是返现和满奖时为：入账状态；类型是不满扣时为：扣款状态',
  `entry_time` datetime(0) NULL DEFAULT NULL COMMENT 'amount_type类型是返现和满奖时为：入账状态；类型是不满扣时为：扣款时间',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `agent_node` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商结点',
  `active_order` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '激活订单号',
  `amount_type` int(3) NOT NULL DEFAULT 1 COMMENT '本条记录所属类型，1：返现；2：满奖；3：不满扣',
  `pre_transfer_status` int(3) NULL DEFAULT NULL COMMENT '预调账状态(amount_type类型是不满扣时专用)0未入账',
  `pre_transfer_time` datetime(0) NULL DEFAULT NULL COMMENT '预调账时间(amount_type类型是不满扣时专用)',
  `ad_id` bigint(20) NULL DEFAULT NULL COMMENT '活动编号activity_detail id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `agent_no_order_type`(`agent_no`, `active_order`, `amount_type`, `ad_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3887 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for channel_business_cost
-- ----------------------------
DROP TABLE IF EXISTS `channel_business_cost`;
CREATE TABLE `channel_business_cost`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `cb_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上游通道业务ID',
  `channel_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上游通道ID',
  `card_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银行卡种类:0-不限，1-只信用卡，2-只储蓄卡',
  `holidays_mark` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '节假日标志:1-只工作日，2-只节假日，0-不限',
  `rate_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '费率类型:1每笔固定金额，2扣率，3扣率+保底封顶，4阶梯－扣率，5阶梯－固定金额',
  `single_num_amount` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '每笔固定值',
  `rate` decimal(20, 6) NULL DEFAULT 0.000000 COMMENT '扣率',
  `capping` decimal(10, 6) NULL DEFAULT 0.000000 COMMENT '封顶',
  `safe_line` decimal(10, 6) NULL DEFAULT 0.000000 COMMENT '保底',
  `ladder` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '阶梯类型:1-单笔，2-月交易额',
  `efficient_date` timestamp(0) NULL DEFAULT NULL COMMENT '生效时间',
  `disabled_date` timestamp(0) NULL DEFAULT NULL COMMENT '失效时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_cb_id`(`cb_id`, `channel_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '上游通道业务成本' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for channel_business_detail
-- ----------------------------
DROP TABLE IF EXISTS `channel_business_detail`;
CREATE TABLE `channel_business_detail`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `channel_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上游通道ID',
  `cb_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上游通道业务ID',
  `status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `channel_id_UNIQUE`(`channel_id`, `cb_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '上游通道业务明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for channel_business_info
-- ----------------------------
DROP TABLE IF EXISTS `channel_business_info`;
CREATE TABLE `channel_business_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `cb_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上游通道业务ID',
  `busi_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务名称',
  `card_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '可用银行卡集合:0-不限，1-只信用卡，2-只储蓄卡',
  `fee_card` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '费率是否区分银行卡种类:0-否，1-是',
  `fee_holiday` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '费率是否区分节假日:0-否，1-是',
  `quota_holiday` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '限额是否区分节假日:0-否，1-是',
  `quota_card` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '限额是否区分银行卡种类:0-否，1-是',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `cb_id_UNIQUE`(`cb_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '上游通道业务基本信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for channel_card_bin
-- ----------------------------
DROP TABLE IF EXISTS `channel_card_bin`;
CREATE TABLE `channel_card_bin`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `card_bin_id` int(11) NOT NULL COMMENT 'pos_card_bin的ID',
  `channel_card_bin_id` int(11) NOT NULL COMMENT '银盛的pos_card_bin的ID',
  `channel_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'YS:银盛',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5651 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for channel_info
-- ----------------------------
DROP TABLE IF EXISTS `channel_info`;
CREATE TABLE `channel_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `channel_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上游通道ID',
  `channel_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上游通道名称',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `channel_id_UNIQUE`(`channel_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '上游通道基本信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for channel_ladder_replenish
-- ----------------------------
DROP TABLE IF EXISTS `channel_ladder_replenish`;
CREATE TABLE `channel_ladder_replenish`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `cb_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上游通道业务ID',
  `insub_ladder_profit` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '阶梯分润补差时，内部账科目',
  `outsub_ladder_profit` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '阶梯分润补差时，外部账科目',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `cb_id_UNIQUE`(`cb_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '上游阶梯成本补差记账配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for channel_normal_rate
-- ----------------------------
DROP TABLE IF EXISTS `channel_normal_rate`;
CREATE TABLE `channel_normal_rate`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `cb_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上游通道业务ID',
  `card_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银行卡种类:0-不限，1-只信用卡，2-只储蓄卡',
  `holidays_mark` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '节假日标志:1-只工作日，2-只节假日，0-不限',
  `efficient_date` date NULL DEFAULT NULL COMMENT '生效日期',
  `disabled_date` date NULL DEFAULT NULL COMMENT '失效日期',
  `rate_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '费率类型:1-扣率，2-扣率+封顶',
  `rate` decimal(20, 6) NULL DEFAULT 0.000000 COMMENT '扣率',
  `capping` decimal(10, 6) NULL DEFAULT 0.000000 COMMENT '封顶',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `cb_id_UNIQUE`(`cb_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '上游通道业务银联标准费率' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for channel_share_rule
-- ----------------------------
DROP TABLE IF EXISTS `channel_share_rule`;
CREATE TABLE `channel_share_rule`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `cb_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上游通道业务ID',
  `channel_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上游通道ID',
  `card_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银行卡种类:0-不限，1-只信用卡，2-只储蓄卡',
  `holidays_mark` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '节假日标志:1-只工作日，2-只节假日，0-不限',
  `rate_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分润扣率类型:1-基于银联扣率，2基于成本扣率',
  `efficient_date` date NULL DEFAULT NULL COMMENT '生效日期',
  `disabled_date` date NULL DEFAULT NULL COMMENT '失效日期',
  `remark` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_cb_id`(`cb_id`, `channel_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '上游服务分润规则' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for channel_trans
-- ----------------------------
DROP TABLE IF EXISTS `channel_trans`;
CREATE TABLE `channel_trans`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `cb_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上游通道业务ID',
  `channel_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上游通道ID',
  `card_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银行卡种类:0-不限，1-只信用卡，2-只储蓄卡',
  `holidays_mark` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '节假日标志:1-只工作日，2-只节假日，0-不限',
  `enter_accou_date` date NULL DEFAULT NULL COMMENT '记账日期',
  `time_area` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '时间段（1到24）',
  `accum_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '累计交易金额',
  `accum_count` int(11) NULL DEFAULT 0 COMMENT '累计交易笔数',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_cb_id`(`cb_id`, `channel_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '上游通道交易累计表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for channle_business_ladder
-- ----------------------------
DROP TABLE IF EXISTS `channle_business_ladder`;
CREATE TABLE `channle_business_ladder`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `cb_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上游通道业务ID',
  `channel_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上游通道ID',
  `card_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银行卡种类:0-不限，1-只信用卡，2-只储蓄卡',
  `holidays_mark` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '节假日标志:1-只工作日，2-只节假日，0-不限',
  `trans_amount_floor` decimal(20, 4) NULL DEFAULT 0.0000 COMMENT '交易额下限',
  `trans_amount_ceiling` decimal(20, 4) NULL DEFAULT 0.0000 COMMENT '交易额上限',
  `rate` decimal(20, 6) NULL DEFAULT 0.000000 COMMENT '扣率',
  `amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '固定金额',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index2`(`cb_id`, `channel_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '上游通道业务成本阶梯' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for city
-- ----------------------------
DROP TABLE IF EXISTS `city`;
CREATE TABLE `city`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `city_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `city_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `city_grade` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `parent_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3226 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for city_bak
-- ----------------------------
DROP TABLE IF EXISTS `city_bak`;
CREATE TABLE `city_bak`  (
  `id` int(10) NOT NULL DEFAULT 0,
  `city_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `city_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `city_grade` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `parent_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `yl_parent_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  INDEX `city_name`(`city_name`) USING BTREE,
  INDEX `city_code`(`city_code`) USING BTREE,
  INDEX `p_code`(`parent_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cjt_activity_profit
-- ----------------------------
DROP TABLE IF EXISTS `cjt_activity_profit`;
CREATE TABLE `cjt_activity_profit`  (
  `id` bigint(15) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '达标单号',
  `merchant_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收益商户号',
  `from_merchant_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收益来源商户编号',
  `level` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收益级别（属于几级收益，即mer_no为from_mer_no的上几级），one上一级收益 two上二级收益',
  `status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态 0-未达标 1-已达标',
  `profit_amount` decimal(8, 2) NULL DEFAULT NULL COMMENT '奖励金额',
  `target_day` int(3) NULL DEFAULT NULL COMMENT '目标天数（到截止时间的天数）',
  `target_amount` decimal(8, 2) NULL DEFAULT NULL COMMENT '达标目标金额',
  `have_amount` decimal(8, 2) NULL DEFAULT 0.00 COMMENT '已满足条件金额',
  `end_time` timestamp(0) NULL DEFAULT NULL COMMENT '达标截止时间',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `target_time` timestamp(0) NULL DEFAULT NULL COMMENT '达标时间',
  `last_update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '数据最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `order_uni`(`order_no`) USING BTREE,
  UNIQUE INDEX `from_mer_level_uni`(`from_merchant_no`, `level`) USING BTREE,
  INDEX `mer_ind`(`merchant_no`) USING BTREE,
  INDEX `from_mer_ind`(`from_merchant_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '需达标活动奖励信息表,活动达标后会将收益记录到收益表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cjt_after_sale
-- ----------------------------
DROP TABLE IF EXISTS `cjt_after_sale`;
CREATE TABLE `cjt_after_sale`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '售后订单编号',
  `merchant_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '售后订单申请人商户编号',
  `after_sale_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '售后订单类型，对应数据字典AFTER_SALE_TYPE',
  `apply_remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '售后申请说明',
  `apply_img` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '售后申请图片，多张以英文逗号分开',
  `status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '售后申请订单状态 0-待平台处理1-处理中 2-已处理 3-已取消',
  `service_order_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '业务订单号（即cjt_order的order_no）',
  `deal_remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '处理结果说明',
  `deal_img` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '处理结果图片，多张以英文逗号分开',
  `deal_person` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后处理人',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `deal_time` timestamp(0) NULL DEFAULT NULL COMMENT '最后处理时间',
  `last_update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '数据最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `order_uni`(`order_no`) USING BTREE,
  INDEX `mer_ind`(`merchant_no`) USING BTREE,
  INDEX `service_order_ind`(`service_order_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '售后订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cjt_count_record
-- ----------------------------
DROP TABLE IF EXISTS `cjt_count_record`;
CREATE TABLE `cjt_count_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `count_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '统计类型 day日统计 month月统计',
  `count_time` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '统计的目标日期yyyy-MM或yyyy-MM-dd',
  `count_status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '统计状态 0运行中 1已完成',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `complete_time` timestamp(0) NULL DEFAULT NULL COMMENT '完成时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '超级推统计记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cjt_day_count
-- ----------------------------
DROP TABLE IF EXISTS `cjt_day_count`;
CREATE TABLE `cjt_day_count`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `merchant_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收益商户号',
  `count_day` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '统计日期-天（yyyy-MM-dd）',
  `profit_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '奖励类型 recommend推荐奖励 activity活动补贴 posTrade刷卡交易 noCardTrade无卡交易',
  `profit_amount` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '分润金额',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后更新时间',
  `profit_from_amount` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '收益来源交易金额',
  `count_day_sum` int(11) NOT NULL DEFAULT 0 COMMENT '每天商户的奖励类型的总笔数',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `mer_type_day`(`merchant_no`, `count_day`, `profit_type`) USING BTREE,
  INDEX `mer_ind`(`merchant_no`) USING BTREE,
  INDEX `day_ind`(`count_day`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 60 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商户收益日统计表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cjt_goods
-- ----------------------------
DROP TABLE IF EXISTS `cjt_goods`;
CREATE TABLE `cjt_goods`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `goods_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品编号',
  `status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态 0：下架，1：上架',
  `white_status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '白名单状态,0:否,1:是',
  `goods_name` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `goods_desc` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '商品描述',
  `main_img` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品主图,多张以英文逗号分开',
  `desc_img` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品详情图,多张以英文逗号分开',
  `price` decimal(8, 2) NULL DEFAULT NULL COMMENT '单价',
  `size` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '尺寸',
  `color` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '颜色，多种以英文逗号分开',
  `min_num` int(11) NULL DEFAULT 1 COMMENT '起购量',
  `hp_id` int(11) NULL DEFAULT NULL COMMENT '硬件产品ID,对应hardware_product表',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `creater` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `last_update_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `un_goods_code`(`goods_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '超级推商品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cjt_merchant_info
-- ----------------------------
DROP TABLE IF EXISTS `cjt_merchant_info`;
CREATE TABLE `cjt_merchant_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户ID,对应user_info表',
  `status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '1' COMMENT '用户状态 0异常 1正常,2未完善',
  `merchant_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '本级商户编号',
  `source` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '来源 1商户分享链接 2绑定超级推机具',
  `one_merchant_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上一级商户编号',
  `two_merchant_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上二级商户编号',
  `merchant_node` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '本级商户节点(two_merchant_no-one_merchant_no-merchant_no)',
  `agent_node` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '本级商户直属代理商的节点',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '数据最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_id_uni`(`user_id`) USING BTREE,
  UNIQUE INDEX `merchant_no_uni`(`merchant_no`) USING BTREE,
  INDEX `one_ind`(`one_merchant_no`) USING BTREE,
  INDEX `two_ind`(`two_merchant_no`) USING BTREE,
  INDEX `mer_node_ind`(`merchant_node`) USING BTREE,
  INDEX `agent_node_ind`(`agent_node`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 279 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '新版超级推商户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cjt_month_count
-- ----------------------------
DROP TABLE IF EXISTS `cjt_month_count`;
CREATE TABLE `cjt_month_count`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `merchant_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收益商户号',
  `count_month` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '统计月份（yyyy-MM）',
  `profit_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '奖励类型 recommend推荐奖励 activity活动补贴 posTrade刷卡交易 noCardTrade无卡交易',
  `profit_amount` decimal(10, 2) NOT NULL DEFAULT 0.00,
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后更新时间',
  `profit_from_amount` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '收益交易交易金额',
  `count_month_sum` int(11) NOT NULL DEFAULT 0 COMMENT '每月商户的奖励类型的总笔数',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `mer_type_month`(`merchant_no`, `count_month`, `profit_type`) USING BTREE,
  INDEX `mer_ind`(`merchant_no`) USING BTREE,
  INDEX `month_ind`(`count_month`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 38 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商户收益月统计表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cjt_order
-- ----------------------------
DROP TABLE IF EXISTS `cjt_order`;
CREATE TABLE `cjt_order`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单编号',
  `merchant_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '购买者商户编号',
  `merchant_node` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '购买者商户超级推节点',
  `order_status` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单状态 0-待付款  1-待发货  2-已发货  4-已关闭',
  `goods_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品编号',
  `hp_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '硬件产品ID,对应hardware_product表',
  `num` int(6) NOT NULL COMMENT '商品数量',
  `price` decimal(8, 2) NOT NULL COMMENT '单价',
  `size` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '购买的尺寸',
  `color` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '购买的颜色',
  `total_price` decimal(8, 2) NOT NULL COMMENT '订单总金额',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `receiver` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收货人',
  `receiver_phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收货人联系方式',
  `receive_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收货地址',
  `logistics_company` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '物流公司',
  `logistics_order_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '物流单号',
  `logistics_time` timestamp(0) NULL DEFAULT NULL COMMENT '物流时间',
  `logistics_operator` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发货人',
  `last_after_sale_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后一次售后订单的订单号',
  `create_time` timestamp(0) NULL DEFAULT NULL,
  `last_update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '数据最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `order_uni`(`order_no`) USING BTREE,
  UNIQUE INDEX `after_sale_ind`(`last_after_sale_no`) USING BTREE,
  INDEX `mer_ind`(`merchant_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 28 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商户购买订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cjt_order_sn
-- ----------------------------
DROP TABLE IF EXISTS `cjt_order_sn`;
CREATE TABLE `cjt_order_sn`  (
  `id` bigint(15) NOT NULL AUTO_INCREMENT,
  `merchant_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '购买者商户编号',
  `buy_order_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '购买订单号',
  `sn` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '机具sn号',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `sn_uni`(`sn`) USING BTREE,
  INDEX `mer_ind`(`merchant_no`) USING BTREE,
  INDEX `order_ind`(`buy_order_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '超级推商户机具购买信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cjt_profit_count
-- ----------------------------
DROP TABLE IF EXISTS `cjt_profit_count`;
CREATE TABLE `cjt_profit_count`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `merchant_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户编号',
  `profit_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '奖励类型 recommend推荐奖励 activity活动补贴 posTrade刷卡交易 noCardTrade无卡交易',
  `profit_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '总分润金额',
  `profit_from_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '总分润来源金额',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '数据最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `mer_ind`(`merchant_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商户收益汇总表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cjt_profit_detail
-- ----------------------------
DROP TABLE IF EXISTS `cjt_profit_detail`;
CREATE TABLE `cjt_profit_detail`  (
  `id` bigint(15) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单号',
  `merchant_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收益商户号',
  `from_merchant_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收益来源商户编号',
  `from_level` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收益来源级别（属于几级收益，即mer_no为from_mer_no的上几级），zero本级收益 one上一级收益 two上二级收益',
  `profit_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '奖励类型 recommend推荐奖励 activity活动补贴 posTrade刷卡交易 noCardTrade无卡交易',
  `profit_from_order_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收益来源订单号',
  `profit_from_amount` decimal(8, 2) NULL DEFAULT NULL COMMENT '收益来源业务金额',
  `profit_rate` decimal(8, 5) NULL DEFAULT NULL COMMENT '收益比例(如0.00015，固定金额时为空)',
  `profit_amount` decimal(8, 2) NULL DEFAULT NULL COMMENT '收益金额',
  `recharge_status` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '入账状态 0-未入账 1-已入账 2-入账失败',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `recharge_time` timestamp(0) NULL DEFAULT NULL COMMENT '入账时间',
  `last_update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '数据最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `order_uni`(`order_no`) USING BTREE,
  UNIQUE INDEX `profit_uni`(`merchant_no`, `profit_type`, `profit_from_order_no`) USING BTREE,
  INDEX `mer_ind`(`merchant_no`) USING BTREE,
  INDEX `from_mer_ind`(`from_merchant_no`) USING BTREE,
  INDEX `create_time_ind`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 27 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '超级推收益明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cjt_profit_rule
-- ----------------------------
DROP TABLE IF EXISTS `cjt_profit_rule`;
CREATE TABLE `cjt_profit_rule`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `profit_rule_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '规则编号',
  `profit_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '奖励类型 recommend推荐奖励 activity活动补贴 posTrade刷卡交易 noCardTrade无卡交易 register注册奖励',
  `status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '奖励规则状态 0关闭 1正常',
  `profit_condition` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '奖励条件 无限制条件则为空',
  `profit_orgs` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '条件内对应的参数',
  `profit_mode` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '奖励方式 1固定金额 2固定比例 3参与活动',
  `profit_0` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '本级商户奖励',
  `profit_1` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上一级商户奖励',
  `profit_2` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上二级商户奖励',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '数据最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `no_uni`(`profit_rule_no`) USING BTREE,
  UNIQUE INDEX `type_uni`(`profit_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '超级推分润奖励规则' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cjt_push_template
-- ----------------------------
DROP TABLE IF EXISTS `cjt_push_template`;
CREATE TABLE `cjt_push_template`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `type` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型,trans:交易分润,recommend:推荐奖励,activity_1:活动补贴直推,activity_2:活动补贴间推',
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '极光推送title',
  `content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模板内容',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operator` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作人',
  `last_update_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '超级推极光推送模板' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cjt_receive_info
-- ----------------------------
DROP TABLE IF EXISTS `cjt_receive_info`;
CREATE TABLE `cjt_receive_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `merchant_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户编号',
  `receiver` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收货人',
  `receiver_phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收货人联系方式',
  `address_province` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '省',
  `address_city` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '市',
  `address_county` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '区/县',
  `address_detail` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '详细地址',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '总地址',
  `status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '状态 0-失效 1-有效',
  `is_default` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '是否是默认地址 0-不是 1-是',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '数据最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `mer_ind`(`merchant_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商户收货地址信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cjt_share_info
-- ----------------------------
DROP TABLE IF EXISTS `cjt_share_info`;
CREATE TABLE `cjt_share_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'APP编号',
  `share_title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '发给朋友显示的标题',
  `share_content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '发给朋友显示的内容',
  `jump_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '点击跳转地址，参数以%s代替',
  `status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '0:关闭,1:正常',
  `create_time` timestamp(0) NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商户分享配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cjt_team_hardware
-- ----------------------------
DROP TABLE IF EXISTS `cjt_team_hardware`;
CREATE TABLE `cjt_team_hardware`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `team_id` int(11) NULL DEFAULT NULL COMMENT '组织ID,对应team_info表',
  `hp_id` int(11) NULL DEFAULT NULL COMMENT '硬件产品ID,对应hardware_product表',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '超级推组织-硬件产品关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cjt_trans_order
-- ----------------------------
DROP TABLE IF EXISTS `cjt_trans_order`;
CREATE TABLE `cjt_trans_order`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `merchant_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单创建人商户编号',
  `service` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务类型 goodsOrder-机具购买',
  `service_order_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务订单号',
  `trans_amount` decimal(8, 2) NOT NULL COMMENT '交易金额(元)',
  `trans_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '支付方式 WX-微信，ZFB-支付宝，KJ-快捷',
  `acq_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '支付方式通道 官方的为主题简称如QHYL，非官方时为具体支付通道简称',
  `acq_order_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '渠道订单号(微信支付宝官方订单号或交易主表订单号)',
  `trans_status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '支付订单状态 0-未支付，1-已提交，2-支付成功，3-支付失败',
  `create_time` timestamp(0) NOT NULL COMMENT '创建时间',
  `trans_time` timestamp(0) NULL DEFAULT NULL COMMENT '交易时间',
  `last_update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '数据最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `order_uni`(`order_no`) USING BTREE,
  INDEX `mer_ind`(`merchant_no`) USING BTREE,
  INDEX `service_no_uni`(`service`, `service_order_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '支付订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cjt_unique_seq
-- ----------------------------
DROP TABLE IF EXISTS `cjt_unique_seq`;
CREATE TABLE `cjt_unique_seq`  (
  `id` bigint(25) NOT NULL AUTO_INCREMENT,
  `create_time` timestamp(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 31 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '超级推自增序列表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cjt_white_mer
-- ----------------------------
DROP TABLE IF EXISTS `cjt_white_mer`;
CREATE TABLE `cjt_white_mer`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `merchant_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户编号',
  `status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '1' COMMENT '白名单状态 0关闭 1开启',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creater` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `merchant_uni`(`merchant_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '超级推商户白名单表（已下架的商品，白名单用户仍能看到）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cloud_pay_profit
-- ----------------------------
DROP TABLE IF EXISTS `cloud_pay_profit`;
CREATE TABLE `cloud_pay_profit`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `merchant_no` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户编号',
  `trans_amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '交易金额',
  `trans_fee` decimal(20, 2) NULL DEFAULT NULL COMMENT '交易手续费(商户手续费)',
  `discount_fee` decimal(20, 2) NULL DEFAULT NULL COMMENT '优惠后手续费',
  `merchant_profit` decimal(20, 2) NULL DEFAULT NULL COMMENT '收益金额',
  `trans_rate` decimal(10, 2) NULL DEFAULT NULL COMMENT '活动交易费率',
  `activity_code` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '活动类型(数据字典KEY：COUPON_CODE)',
  `order_no` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '交易订单号',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_person` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建人',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注（备用字段）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `merchant_no_indx`(`merchant_no`) USING BTREE,
  INDEX `order_no_indx`(`order_no`) USING BTREE,
  INDEX `create_time_indx`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2115 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '云闪付商户收益表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for collective_trans_extended
-- ----------------------------
DROP TABLE IF EXISTS `collective_trans_extended`;
CREATE TABLE `collective_trans_extended`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `source_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '原记录唯一标识',
  `phone_model` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机型号',
  `os_version` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机操作系统版本',
  `app_version` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'APP版本号',
  `imsi` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'SIM卡标识',
  `imei` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备标识',
  `location` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地理位置信息',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6018 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '交易信息扩展表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for collective_trans_order
-- ----------------------------
DROP TABLE IF EXISTS `collective_trans_order`;
CREATE TABLE `collective_trans_order`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '商户订单号',
  `mobile_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登陆手机号',
  `merchant_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '商户编号',
  `trans_amount` decimal(30, 2) NULL DEFAULT NULL COMMENT '交易金额',
  `pay_method` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易方式 1 POS，2 支付宝，3 微信，4 快捷',
  `trans_status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易状态:SUCCESS：成功,FAILED：失败,INIT：初始化,REVERSED：已冲正,REVOKED：已撤销,SETTLE：已结算,OVERLIMIT：已退款,REFUND：失败,COMPLETE：已完成,CLOSED：关闭;',
  `trans_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易类型\\r\\n 消费PURCHASE, 冲正REVERSED, 消费撤销PURCHASE_VOID,预授权PRE_AUTH, 预授权撤销PRE_AUTH_VOID, 预授权完成PRE_AUTH_COMPLETA,预授权完成撤销PRE_AUTH_COMPLETE_VOID, 退货PURCHASE_REFUND, 查余额BALANCE_QUERY,转账TRANSFER_ACCOUNTS;',
  `trans_time` timestamp(0) NULL DEFAULT NULL COMMENT '交易时间',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `account_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易账号',
  `card_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易卡种（借记卡，贷记卡，未知）',
  `holidays_mark` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '节假日标志:1-只工作日，2-只节假日，0-不限',
  `business_product_id` int(10) NULL DEFAULT NULL COMMENT '业务产品id',
  `service_id` int(10) NULL DEFAULT NULL COMMENT '服务ID',
  `acq_org_id` int(10) NULL DEFAULT NULL COMMENT '收单机构id',
  `acq_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构名称',
  `acq_enname` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构英文名称',
  `acq_service_id` int(10) NULL DEFAULT NULL COMMENT '收单服务ID',
  `merchant_fee` decimal(30, 2) NULL DEFAULT NULL COMMENT '商户手续费',
  `merchant_rate` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户扣率',
  `merchant_rate_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户服务费率类型 费率类型:1-每笔固定金额，2-扣率，3-扣率带保底封顶，4-扣率+固定金额,5-单笔阶梯 扣率',
  `acq_merchant_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单商户编号',
  `acq_merchant_fee` decimal(30, 2) NULL DEFAULT NULL COMMENT '收单机构手续费',
  `acq_merchant_rate` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单商户扣率',
  `acq_rate_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构服务费率类型 费率类型:1-每笔固定金额，2-扣率，3-扣率带保底封顶，4-扣率+固定金额,5-单笔阶梯 扣率 6-每月阶梯扣率',
  `agent_node` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商节点',
  `settlement_method` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '结算方式 0 t0 ,1 t1',
  `settle_status` int(1) NOT NULL DEFAULT 0 COMMENT '结算状态(0或空:未结算,1:已结算,2:结算中,3:结算失败,4:转T1结算,5:不结算,6:已返代理商)',
  `settle_msg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '结算备注',
  `settle_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '出款类型',
  `settle_order` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '出款流水订单号',
  `syn_status` int(1) NOT NULL DEFAULT 1 COMMENT '出款同步状态 1.未提交 2.已提交',
  `freeze_status` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '冻结标识  0为未冻结,1为风控冻结,2活动冻结,3财务冻结  ',
  `device_sn` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '机具，二维码唯一标识',
  `hardware_product` int(10) NULL DEFAULT NULL COMMENT '硬件产品id',
  `account` int(11) NULL DEFAULT 0 COMMENT '0:未记账，1:记账成功，2:记账失败',
  `trans_msg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '记账状态',
  `res_msg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '响应信息',
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
  `profits_20` decimal(30, 2) NULL DEFAULT NULL,
  `gather_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收款码',
  `last_update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '来源，用途',
  `deduction_fee` decimal(30, 2) NULL DEFAULT NULL COMMENT '抵扣交易手续费',
  `actual_fee` decimal(30, 2) NULL DEFAULT NULL COMMENT '实际交易手续费',
  `activity_source` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易活动来源',
  `order_type` int(2) NULL DEFAULT 0 COMMENT '订单类型：0 普通订单、1 超级推订单',
  `unionpay_mer_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '直清银联报备号',
  `mbp_id` bigint(20) NULL DEFAULT NULL COMMENT '商户进件编号ID',
  `origin_fee` decimal(30, 2) NULL DEFAULT NULL COMMENT '原始手续费',
  `quick_rate` decimal(30, 2) NULL DEFAULT 0.00 COMMENT '云闪付费率',
  `quick_fee` decimal(30, 2) NULL DEFAULT 0.00 COMMENT '云闪付金额',
  `merchant_price` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '自选商户手续费',
  `deduction_mer_fee` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '抵扣自选商户手续费',
  `isZJX` int(11) NULL DEFAULT NULL COMMENT '是否有保险1:有,0:无',
  `n_prm` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '保费(保单信息对应表zjx_trans_order)',
  `zx_rate` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '自选行业费率',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `order_id`(`order_no`) USING BTREE,
  INDEX `ind_agent_trans`(`agent_node`, `trans_time`) USING BTREE,
  INDEX `fhind_ano_time`(`agent_node`, `create_time`) USING BTREE,
  INDEX `fhind_mno_time`(`merchant_no`, `create_time`) USING BTREE,
  INDEX `ind_create_tiem`(`create_time`) USING BTREE,
  INDEX `ind_mobile_no`(`mobile_no`) USING BTREE,
  INDEX `trans_create`(`trans_status`, `create_time`) USING BTREE,
  INDEX `ind_trans_time`(`trans_time`) USING BTREE,
  INDEX `fh_ind_ano_transtime`(`agent_node`, `trans_time`) USING BTREE,
  INDEX `idx_business_product_id`(`business_product_id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 204670639 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '交易总记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for collective_trans_order_bak
-- ----------------------------
DROP TABLE IF EXISTS `collective_trans_order_bak`;
CREATE TABLE `collective_trans_order_bak`  (
  `id` int(10) NOT NULL DEFAULT 0 COMMENT '主键',
  `order_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '商户订单号',
  `mobile_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登陆手机号',
  `merchant_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '商户编号',
  `trans_amount` decimal(30, 2) NULL DEFAULT NULL COMMENT '交易金额',
  `pay_method` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易方式 1 POS，2 支付宝，3 微信，4 快捷',
  `trans_status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易状态:SUCCESS：成功,FAILED：失败,INIT：初始化,REVERSED：已冲正,REVOKED：已撤销,SETTLE：已结算,OVERLIMIT：已退款,REFUND：失败,COMPLETE：已完成,CLOSED：关闭;',
  `trans_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易类型\\r\\n 消费PURCHASE, 冲正REVERSED, 消费撤销PURCHASE_VOID,预授权PRE_AUTH, 预授权撤销PRE_AUTH_VOID, 预授权完成PRE_AUTH_COMPLETA,预授权完成撤销PRE_AUTH_COMPLETE_VOID, 退货PURCHASE_REFUND, 查余额BALANCE_QUERY,转账TRANSFER_ACCOUNTS;',
  `trans_time` timestamp(0) NULL DEFAULT NULL COMMENT '交易时间',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `account_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易账号',
  `card_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易卡种（借记卡，贷记卡，未知）',
  `holidays_mark` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '节假日标志:1-只工作日，2-只节假日，0-不限',
  `business_product_id` int(10) NULL DEFAULT NULL COMMENT '业务产品id',
  `service_id` int(10) NULL DEFAULT NULL COMMENT '服务ID',
  `acq_org_id` int(10) NULL DEFAULT NULL COMMENT '收单机构id',
  `acq_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构名称',
  `acq_enname` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构英文名称',
  `acq_service_id` int(10) NULL DEFAULT NULL COMMENT '收单服务ID',
  `merchant_fee` decimal(30, 2) NULL DEFAULT NULL COMMENT '商户手续费',
  `merchant_rate` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户扣率',
  `merchant_rate_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户服务费率类型 费率类型:1-每笔固定金额，2-扣率，3-扣率带保底封顶，4-扣率+固定金额,5-单笔阶梯 扣率',
  `acq_merchant_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单商户编号',
  `acq_merchant_fee` decimal(30, 2) NULL DEFAULT NULL COMMENT '收单机构手续费',
  `acq_merchant_rate` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单商户扣率',
  `acq_rate_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构服务费率类型 费率类型:1-每笔固定金额，2-扣率，3-扣率带保底封顶，4-扣率+固定金额,5-单笔阶梯 扣率 6-每月阶梯扣率',
  `agent_node` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商节点',
  `settlement_method` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '结算方式 0 t0 ,1 t1',
  `settle_status` int(1) NOT NULL DEFAULT 0 COMMENT '结算状态1：已结算  0或空：未结算   2.结算中   3.结算失败  4.转T1结算',
  `settle_msg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '结算备注',
  `syn_status` int(1) NOT NULL DEFAULT 1 COMMENT '出款同步状态 1.未提交 2.已提交',
  `freeze_status` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '冻结标识  1为冻结  0为未冻结',
  `device_sn` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '机具，二维码唯一标识',
  `hardware_product` int(10) NULL DEFAULT NULL COMMENT '硬件产品id',
  `account` int(11) NULL DEFAULT 0 COMMENT '0:未记账，1:记账成功，2:记账失败',
  `trans_msg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '交易错误信息',
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
  `profits_20` decimal(30, 2) NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for company_info
-- ----------------------------
DROP TABLE IF EXISTS `company_info`;
CREATE TABLE `company_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `channel_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '通道编码',
  `order_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单号',
  `acq_order_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '上游订单号',
  `company_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '企业名称',
  `license_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组织机构代码',
  `license_social_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '统一社会信用代码',
  `register_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '企业工商注册号',
  `legal_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '法人名称',
  `company_status` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '企业状态 0-不存在 1-在营 2-吊销  3-注销  4-迁出  5-停业  9-其他',
  `company_status_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '企业状态名称',
  `business_scope` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '经营范围',
  `register_amount` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '注册资本',
  `register_time` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '注册日期',
  `business_end_time` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '经营截止日期',
  `return_msg` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '上游返回详细信息',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operator` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作人',
  `last_update_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `company_name_inde`(`company_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '企业信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for coupon_activity_entity
-- ----------------------------
DROP TABLE IF EXISTS `coupon_activity_entity`;
CREATE TABLE `coupon_activity_entity`  (
  `id` int(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `activetiy_code` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '活动编号(详见字典表 COUPON_TYPE)',
  `coupon_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '优惠卷类型(详见字典表 COUPON_TYPE)',
  `coupon_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '优惠券名称',
  `coupon_amount` decimal(30, 2) NULL DEFAULT NULL COMMENT '优惠券面值',
  `coupon_count` int(20) NULL DEFAULT NULL COMMENT '可参与次数、可获取数量、优惠券日发行数（-1：不限）',
  `cancel_verification_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用途、核销方式(详见字典表CANCEL_VERIFICATION_TYPE)',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `operator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作人',
  `status` int(2) NULL DEFAULT 0 COMMENT '状态:0 有效、1关闭',
  `gift_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '赠送金额',
  `coupon_explain` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '券说明',
  `purchase_count` int(11) NULL DEFAULT 0 COMMENT '商户每天可购买数',
  `purchase_total` int(11) NULL DEFAULT 0 COMMENT '可购买总数',
  `integral_scale` int(11) NULL DEFAULT 100 COMMENT '与积分兑换比例',
  `isshelves` int(2) NULL DEFAULT 1 COMMENT '是否上架：0.上架  1.下架',
  `activity_first` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '活动优先级：A-E优先级递增',
  `effective_days` int(20) NULL DEFAULT NULL COMMENT '有效天数',
  `order_by` int(5) NULL DEFAULT NULL COMMENT '排序字段',
  `trans_rate` decimal(6, 2) NULL DEFAULT 0.00 COMMENT '交易费率(%)',
  `activity_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '机具活动类型（暂定注册返使用）',
  `coupon_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '卷原有类型（抽奖）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 194 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '活动优惠券配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for coupon_activity_info
-- ----------------------------
DROP TABLE IF EXISTS `coupon_activity_info`;
CREATE TABLE `coupon_activity_info`  (
  `id` int(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `activetiy_code` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '活动编号',
  `activetiy_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '活动类别(详见字典表 COUPON_TYPE)',
  `activity_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '活动名称',
  `activity_explain` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '活动说明',
  `activity_first` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '活动优先级：A-E优先级递增',
  `effective_days` int(20) NULL DEFAULT NULL COMMENT '有效天数',
  `activity_notice` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '活动下发通知信息',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `operator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作人',
  `status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '状态:0 有效、1关闭',
  `up_open_explan` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '开通说明',
  `open_explan` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '已开通说明',
  `buy_push` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '购买推送',
  `maturity_give_push` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '到期送推送',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '优惠券活动管理表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for coupon_activity_time
-- ----------------------------
DROP TABLE IF EXISTS `coupon_activity_time`;
CREATE TABLE `coupon_activity_time`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `entity_id` int(11) NULL DEFAULT NULL COMMENT '活动券id',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 64 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '上架起止时间' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for credit_card
-- ----------------------------
DROP TABLE IF EXISTS `credit_card`;
CREATE TABLE `credit_card`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `merchant_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号',
  `bank_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银行名称',
  `bank_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银行编码',
  `account_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '信用卡号',
  `repayment_date` int(11) NULL DEFAULT NULL COMMENT '还款日期,每个月1~31任选',
  `warn_day` int(11) NULL DEFAULT NULL COMMENT '提前多少天提醒',
  `remove_warn_date` datetime(0) NULL DEFAULT NULL COMMENT '去掉当前提醒的当前日期',
  `warn_switch` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设置提醒开关，0：关闭，1：开启',
  `effective_date` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '生效日期',
  `mobile` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银行预留手机号',
  `update_date` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `last_trans_time` datetime(0) NULL DEFAULT NULL COMMENT '最后交易时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 637 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '信用卡管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for customer_service_problem
-- ----------------------------
DROP TABLE IF EXISTS `customer_service_problem`;
CREATE TABLE `customer_service_problem`  (
  `problem_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `problem_type` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '问题类型 名词解释、注册使用、交易、结算 对应1234',
  `problem_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '问题名称',
  `problem_content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '问题内容',
  `app_scope` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '适用范围  组织ID 如200010-100070',
  `clicks` int(11) NOT NULL DEFAULT 0 COMMENT '点击次数',
  `create_time` datetime(0) NOT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `update_time` datetime(0) NULL DEFAULT NULL,
  `create_user` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `update_user` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `problem_status` int(1) NOT NULL DEFAULT 0 COMMENT '0 正常 1删除',
  `solve_num` int(11) NULL DEFAULT 0 COMMENT '解决次数',
  `no_solve_num` int(11) NULL DEFAULT 0 COMMENT '未解决次数',
  PRIMARY KEY (`problem_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 88 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '客服问题管理表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for def_awards_recode
-- ----------------------------
DROP TABLE IF EXISTS `def_awards_recode`;
CREATE TABLE `def_awards_recode`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `award_desc` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '奖项说明',
  `award_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '奖品名称',
  `sort` int(11) NULL DEFAULT NULL COMMENT '序列号',
  `status` int(11) NULL DEFAULT 0 COMMENT '状态 0:关闭，1:开启 2删除',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for def_trans_route_group
-- ----------------------------
DROP TABLE IF EXISTS `def_trans_route_group`;
CREATE TABLE `def_trans_route_group`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '默认集群ID',
  `product_id` int(11) NOT NULL COMMENT '业务产品ID',
  `acq_org_id` int(11) NULL DEFAULT NULL COMMENT '收单机构ID',
  `rate_type` int(2) NULL DEFAULT NULL COMMENT '费率类型:1-每笔固定金额，2-扣率，3-扣率带保底封顶，4-扣率+固定金额,5-单笔阶梯 扣率 6-每月阶梯扣率',
  `def_group_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '集群编号',
  `def_type` int(2) NULL DEFAULT NULL COMMENT '默认集群类型',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `create_person` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `service_id` int(11) NULL DEFAULT NULL COMMENT '商户服务ID',
  `start_pc` int(1) NULL DEFAULT NULL COMMENT '启用自动按地点匹配集群 1开启，2不开启',
  `acq_service_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单服务类型',
  `service_model` int(5) NULL DEFAULT NULL COMMENT '服务模式：sys_dict <SERVICE_MODEL>',
  `liquidation_channel` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '直清通道: sys_dict <LIQ_CHANNEL>',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `only_def_group`(`product_id`, `acq_org_id`, `service_id`, `def_group_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 256 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '默认集群表\r\n相同的默认集群类型+相同的集群编号+相同的扣率类型 不允许重复' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for def_trans_route_group_20181024
-- ----------------------------
DROP TABLE IF EXISTS `def_trans_route_group_20181024`;
CREATE TABLE `def_trans_route_group_20181024`  (
  `id` int(11) NOT NULL DEFAULT 0 COMMENT '默认集群ID',
  `product_id` int(11) NOT NULL COMMENT '业务产品ID',
  `acq_org_id` int(11) NULL DEFAULT NULL COMMENT '收单机构ID',
  `rate_type` int(2) NULL DEFAULT NULL COMMENT '费率类型:1-每笔固定金额，2-扣率，3-扣率带保底封顶，4-扣率+固定金额,5-单笔阶梯 扣率 6-每月阶梯扣率',
  `def_group_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '集群编号',
  `def_type` int(2) NULL DEFAULT NULL COMMENT '默认集群类型',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `create_person` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `service_id` int(11) NULL DEFAULT NULL COMMENT '商户服务ID',
  `start_pc` int(1) NULL DEFAULT NULL COMMENT '启用自动按地点匹配集群 1开启，2不开启',
  `acq_service_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单服务类型',
  `service_model` int(5) NULL DEFAULT NULL COMMENT '服务模式：sys_dict <SERVICE_MODEL>',
  `liquidation_channel` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '直清通道: sys_dict <LIQ_CHANNEL>'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for def_trans_route_group_20181025
-- ----------------------------
DROP TABLE IF EXISTS `def_trans_route_group_20181025`;
CREATE TABLE `def_trans_route_group_20181025`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '默认集群ID',
  `product_id` int(11) NOT NULL COMMENT '业务产品ID',
  `acq_org_id` int(11) NULL DEFAULT NULL COMMENT '收单机构ID',
  `rate_type` int(2) NULL DEFAULT NULL COMMENT '费率类型:1-每笔固定金额，2-扣率，3-扣率带保底封顶，4-扣率+固定金额,5-单笔阶梯 扣率 6-每月阶梯扣率',
  `def_group_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '集群编号',
  `def_type` int(2) NULL DEFAULT NULL COMMENT '默认集群类型',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `create_person` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `service_id` int(11) NULL DEFAULT NULL COMMENT '商户服务ID',
  `start_pc` int(1) NULL DEFAULT NULL COMMENT '启用自动按地点匹配集群 1开启，2不开启',
  `acq_service_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单服务类型',
  `service_model` int(5) NULL DEFAULT NULL COMMENT '服务模式：sys_dict <SERVICE_MODEL>',
  `liquidation_channel` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '直清通道: sys_dict <LIQ_CHANNEL>',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `only_def_group`(`product_id`, `acq_org_id`, `service_id`, `def_group_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 158 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '默认集群表\r\n相同的默认集群类型+相同的集群编号+相同的扣率类型 不允许重复' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for dk_order_info
-- ----------------------------
DROP TABLE IF EXISTS `dk_order_info`;
CREATE TABLE `dk_order_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '我们生成的订单号，关联collective_trans_order',
  `merchant_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户号',
  `bill_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '德坤运单号',
  `bill_date` datetime(0) NULL DEFAULT NULL COMMENT '德坤开单时间',
  `payment_amout` decimal(30, 2) NULL DEFAULT NULL COMMENT '金额',
  `pay_method` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易方式 1 POS，2 支付宝，3 微信，4 快捷',
  `trans_status` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'INIT' COMMENT '订单状态：SUCCESS：成功,FAILED：失败,INIT：初始化',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT NULL COMMENT '更新时间（交易成功、失败等）',
  `notity_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '回调路径',
  `notice_times` int(11) NULL DEFAULT 0 COMMENT '通知次数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 346 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '德坤订单交易记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for examinations_log
-- ----------------------------
DROP TABLE IF EXISTS `examinations_log`;
CREATE TABLE `examinations_log`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `item_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户进件编号',
  `bp_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务产品id',
  `open_status` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '开通状态 1审核成功，2审核失败 3 复审退件',
  `examination_opinions` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '审核内容',
  `operator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作员',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '审核时间',
  `examine_type` int(11) NULL DEFAULT 1 COMMENT '审核类型：初审 1，复审2',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `merchant_no`(`item_no`) USING BTREE,
  INDEX `create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12319200 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商户审核记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for examinations_log_ext
-- ----------------------------
DROP TABLE IF EXISTS `examinations_log_ext`;
CREATE TABLE `examinations_log_ext`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `examinations_log_id` int(10) NOT NULL COMMENT '商户审核记录ID',
  `merchant_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户类型:1-个人，2-个体商户，3-企业商户',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `examinations_log_id_ind`(`examinations_log_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商户审核记录扩展表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for exchange_awards_config
-- ----------------------------
DROP TABLE IF EXISTS `exchange_awards_config`;
CREATE TABLE `exchange_awards_config`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `func_code` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '对应func_code',
  `award_desc` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '奖项说明',
  `award_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '奖品名称',
  `coupon_id` int(11) NULL DEFAULT NULL COMMENT '关联奖品配置',
  `effect_days` int(11) NULL DEFAULT NULL COMMENT '有效期',
  `award_hit` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '兑奖提示',
  `money` decimal(8, 2) NULL DEFAULT NULL COMMENT '金额',
  `award_pic` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '奖品图片',
  `award_type` int(11) NOT NULL DEFAULT 4 COMMENT '奖品类型1鼓励金2超级积分3现金劵',
  `status` int(11) NOT NULL DEFAULT 0 COMMENT '状态 0:关闭，1:开启 2删除',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `operator` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '操作人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 98 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '奖品信息配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for exchange_awards_recode
-- ----------------------------
DROP TABLE IF EXISTS `exchange_awards_recode`;
CREATE TABLE `exchange_awards_recode`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `awards_config_id` int(11) NULL DEFAULT NULL COMMENT '奖品信息配置',
  `exc_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '奖品码',
  `award_desc` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '奖项说明',
  `award_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '奖品名称',
  `status` int(11) NOT NULL COMMENT '状态  0 未兑奖 1 已兑奖 2 已过期 3 已作废',
  `user_code` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户ID',
  `award_time` datetime(0) NULL DEFAULT NULL COMMENT '兑奖时间',
  `off_begin_time` datetime(0) NULL DEFAULT NULL COMMENT '有效开始时间',
  `off_end_time` datetime(0) NULL DEFAULT NULL COMMENT '有效结束时间',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `award_type` int(11) NULL DEFAULT NULL COMMENT '奖品类型1鼓励金2超级积分3现金劵',
  `mobilephone` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `coupon_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '券编号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uni_exc_code`(`exc_code`) USING BTREE,
  INDEX `Ind_user_id`(`user_code`) USING BTREE,
  INDEX `ind_award_time`(`award_time`) USING BTREE,
  INDEX `ind_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2912 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '兑奖信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for function_manage
-- ----------------------------
DROP TABLE IF EXISTS `function_manage`;
CREATE TABLE `function_manage`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `function_number` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '功能编号',
  `function_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '功能名称',
  `function_switch` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '功能开关1为开启0为关闭',
  `agent_control` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商控制1为打开0为关闭',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `agent_is_control` int(11) NOT NULL DEFAULT 0 COMMENT '0:默认 不显示代理商控制 1,显示代理商控制',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 51 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '功能控制表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for gather_code
-- ----------------------------
DROP TABLE IF EXISTS `gather_code`;
CREATE TABLE `gather_code`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `SN` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收款码的SN号，序列生成的',
  `gather_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收款码',
  `merchant_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户编号',
  `status` tinyint(4) NOT NULL COMMENT '收款码状态：0 未导出，1 已导出，2 已使用，3 弃用 ',
  `material_type` tinyint(4) NULL DEFAULT NULL COMMENT '物料种类',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '使用时间',
  `record_id` int(11) NULL DEFAULT NULL COMMENT '导出记录ID，关联收款码导出表gather_export_reocrd',
  `gather_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收款码名称',
  `device_sn` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '机具sn',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `SN_unique`(`SN`) USING BTREE COMMENT 'sn的唯一索引',
  UNIQUE INDEX `gather_code`(`gather_code`) USING BTREE COMMENT '收款码的唯一索引'
) ENGINE = InnoDB AUTO_INCREMENT = 20020 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '收款码管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for gather_export_record
-- ----------------------------
DROP TABLE IF EXISTS `gather_export_record`;
CREATE TABLE `gather_export_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `operator` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 55 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '收款码导出记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for hardware_product
-- ----------------------------
DROP TABLE IF EXISTS `hardware_product`;
CREATE TABLE `hardware_product`  (
  `hp_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '硬件产品ID',
  `type_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '种类名称',
  `model` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '型号',
  `version_nu` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '版本号',
  `sale_starttime` date NULL DEFAULT NULL COMMENT '可销售起始日期',
  `sale_endtime` date NULL DEFAULT NULL COMMENT '可销售终止日期',
  `prod_starttime` date NULL DEFAULT NULL COMMENT '可生产起始日期',
  `prod_endtime` date NULL DEFAULT NULL COMMENT '可生产终止日期',
  `use_starttime` date NULL DEFAULT NULL COMMENT '可使用起始日期',
  `use_endtime` date NULL DEFAULT NULL COMMENT '可使用终止日期',
  `repa_starttime` date NULL DEFAULT NULL COMMENT '可维修起始日期',
  `repa_endtime` date NULL DEFAULT NULL COMMENT '可维修终止日期',
  `oem_mark` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'OEM标识',
  `oem_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'OEM ID',
  `facturer_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '生产产商英文标识',
  `manufacturer` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '生产产商',
  `pos_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备类型',
  `image_url` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '示例图片',
  `org_id` bigint(20) NULL DEFAULT NULL COMMENT '组织ID',
  `secret_type` int(3) NOT NULL DEFAULT 0 COMMENT '机具密钥类型，0：无,1：双密钥',
  `device_pn` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '机具PN型号',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_person` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人英文名',
  `team_entry_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '子级组织ID',
  PRIMARY KEY (`hp_id`) USING BTREE,
  UNIQUE INDEX `hp_id_UNIQUE`(`hp_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 322 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '硬件产品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for hlf_activity_merchant_order
-- ----------------------------
DROP TABLE IF EXISTS `hlf_activity_merchant_order`;
CREATE TABLE `hlf_activity_merchant_order`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `active_order` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '激活订单号',
  `active_time` timestamp(0) NULL DEFAULT NULL COMMENT '激活时间',
  `target_status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '达标状态,0:未达标,1:已达标',
  `target_time` timestamp(0) NULL DEFAULT NULL COMMENT '达标时间',
  `total_amount` decimal(15, 2) NULL DEFAULT NULL COMMENT '累计交易金额',
  `reward_amount` decimal(15, 2) NULL DEFAULT NULL COMMENT '奖励金额',
  `reward_account_status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '奖励入账状态,0:未入账,1:已入账',
  `reward_account_time` timestamp(0) NULL DEFAULT NULL COMMENT '奖励入账时间',
  `reward_start_time` timestamp(0) NULL DEFAULT NULL COMMENT '奖励考核开始时间',
  `reward_end_time` timestamp(0) NULL DEFAULT NULL COMMENT '奖励考核结束时间',
  `deduct_amount` decimal(15, 2) NULL DEFAULT NULL COMMENT '扣款金额',
  `deduct_status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扣款状态,0:未扣款,1:已扣款,2:已发起预调账,3:待扣款',
  `deduct_time` timestamp(0) NULL DEFAULT NULL COMMENT '扣款或者调账时间',
  `deduct_start_time` timestamp(0) NULL DEFAULT NULL COMMENT '扣款考核开始时间',
  `deduct_end_time` timestamp(0) NULL DEFAULT NULL COMMENT '扣款考核结束时间',
  `merchant_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户号',
  `agent_node` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '所属代理商节点',
  `operator` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作人',
  `last_update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '数据最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `merchant_no_uni`(`merchant_no`) USING BTREE,
  INDEX `active_time_ind`(`active_time`) USING BTREE,
  INDEX `active_order_ind`(`active_order`) USING BTREE,
  INDEX `agent_node_ind`(`agent_node`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 89 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '欢乐返活跃商户订单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for hlf_activity_merchant_record
-- ----------------------------
DROP TABLE IF EXISTS `hlf_activity_merchant_record`;
CREATE TABLE `hlf_activity_merchant_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `merchant_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户号',
  `activity_type_no` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '欢乐返子类型编号',
  `repeat_status` int(2) NULL DEFAULT NULL COMMENT '是否重复注册,0:否,1:是',
  `reward_type` int(2) NOT NULL COMMENT '奖励考核类型,0:按月数,1:任意一个月',
  `reward_month` int(5) NULL DEFAULT NULL COMMENT '奖励考核月数',
  `reward_amount` decimal(15, 2) NOT NULL COMMENT '奖励金额',
  `reward_total_amount` decimal(15, 2) NULL DEFAULT NULL COMMENT '奖励考核累计金额',
  `deduct_type` int(2) NOT NULL COMMENT '扣款考核类型,0:按月数,1:任意一个月',
  `deduct_month` int(5) NULL DEFAULT NULL COMMENT '扣款考核月数',
  `deduct_amount` decimal(15, 2) NOT NULL COMMENT '扣款金额',
  `deduct_total_amount` decimal(15, 2) NULL DEFAULT NULL COMMENT '扣款考核累计金额',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operator` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作人',
  `last_update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '数据最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `merchant_no_uni`(`merchant_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 151 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '欢乐返活跃商户记录表，用来记录活跃商户激活时的配置信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for hlf_activity_merchant_rule
-- ----------------------------
DROP TABLE IF EXISTS `hlf_activity_merchant_rule`;
CREATE TABLE `hlf_activity_merchant_rule`  (
  `rule_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '活动规则ID',
  `rule_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '活动名称',
  `start_time` timestamp(0) NOT NULL COMMENT '活动开始时间',
  `end_time` timestamp(0) NOT NULL COMMENT '活动结束时间',
  `first_reward_type` int(2) NOT NULL COMMENT '首次奖励考核类型,0:按月数,1:任意一个月',
  `first_reward_month` int(5) NULL DEFAULT NULL COMMENT '首次奖励考核月数',
  `first_reward_total_amount` decimal(15, 2) NOT NULL COMMENT '首次奖励考核累计金额',
  `first_reward_amount` decimal(15, 2) NOT NULL COMMENT '首次奖励金额',
  `first_deduct_type` int(2) NOT NULL COMMENT '首次扣款考核类型,0:按月数,1:任意一个月',
  `first_deduct_month` int(5) NULL DEFAULT NULL COMMENT '首次扣款考核月数',
  `first_deduct_total_amount` decimal(15, 2) NOT NULL COMMENT '首次扣款考核累计金额',
  `first_deduct_amount` decimal(15, 2) NOT NULL COMMENT '首次扣款金额',
  `first_repeat_status` int(3) NOT NULL COMMENT '重复注册与首次注册一致,0:否,1:是',
  `repeat_reward_type` int(2) NULL DEFAULT NULL COMMENT '重复奖励考核类型,0:按月数,1:任意一个月',
  `repeat_reward_month` int(5) NULL DEFAULT NULL COMMENT '重复奖励考核月数',
  `repeat_reward_total_amount` decimal(15, 2) NULL DEFAULT NULL COMMENT '重复奖励考核累计金额',
  `repeat_reward_amount` decimal(15, 2) NULL DEFAULT NULL COMMENT '重复奖励金额',
  `repeat_deduct_type` int(2) NULL DEFAULT NULL COMMENT '重复扣款考核类型,0:按月数,1:任意一个月',
  `repeat_deduct_month` int(5) NULL DEFAULT NULL COMMENT '重复扣款考核月数',
  `repeat_deduct_total_amount` decimal(15, 2) NULL DEFAULT NULL COMMENT '重复扣款考核累计金额',
  `repeat_deduct_amount` decimal(15, 2) NULL DEFAULT NULL COMMENT '重复扣款金额',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operator` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作人',
  `last_update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '数据最后更新时间',
  PRIMARY KEY (`rule_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '欢乐返活跃商户配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for hlf_hardware
-- ----------------------------
DROP TABLE IF EXISTS `hlf_hardware`;
CREATE TABLE `hlf_hardware`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `activity_code` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '活动代码',
  `hard_id` int(11) NULL DEFAULT NULL COMMENT '硬件编号',
  `trans_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '交易金额',
  `cash_back_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '返现金额',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `operator` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '欢乐返活动硬件配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for import_log
-- ----------------------------
DROP TABLE IF EXISTS `import_log`;
CREATE TABLE `import_log`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `batch_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '批次号',
  `log_source` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '入口来源,对应数据字典值LOG_SOURCE',
  `msg` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '总反馈处理结果',
  `operator` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '处理状态,0处理中,1处理完成',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniqueKey_batch_no`(`batch_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 64 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for import_log_entry
-- ----------------------------
DROP TABLE IF EXISTS `import_log_entry`;
CREATE TABLE `import_log_entry`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `batch_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '批次号',
  `data1` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据1,针对不同的EXCEL导入字段不同，预留字段',
  `data2` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据2',
  `data3` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据3',
  `data4` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据4',
  `data5` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据5',
  `data6` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据6',
  `data7` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据7',
  `data8` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据8',
  `data9` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据9',
  `result` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '单条处理结果',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `key_batch_no`(`batch_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 373767 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for industry_mcc
-- ----------------------------
DROP TABLE IF EXISTS `industry_mcc`;
CREATE TABLE `industry_mcc`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `channel_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '通道编码',
  `mer_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户类型 1-小微商户 2-个体工商户  3-企业商户',
  `industry_level` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '行业级别',
  `industry_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '行业名称',
  `mcc` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '行业商户MCC码',
  `parent_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '上级id，一级为0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 285 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '通道行业关系与MCC对应表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for industry_switch
-- ----------------------------
DROP TABLE IF EXISTS `industry_switch`;
CREATE TABLE `industry_switch`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `acq_merchant_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户类型',
  `start_time` time(0) NOT NULL COMMENT '开始时间',
  `end_time` time(0) NOT NULL COMMENT '结束时间',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 48 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '行业切换活动配置表\r\n' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for invite_prizes_config
-- ----------------------------
DROP TABLE IF EXISTS `invite_prizes_config`;
CREATE TABLE `invite_prizes_config`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `agent_no` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '代理商',
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `activity_action` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '活动编号, 4 邀请有奖, 来源coupon_activity_info.activetiy_code',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `operator` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '操作人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `agent_no`(`agent_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 60 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '邀请有奖配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for invite_prizes_merchant_info
-- ----------------------------
DROP TABLE IF EXISTS `invite_prizes_merchant_info`;
CREATE TABLE `invite_prizes_merchant_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `merchant_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户编号',
  `agent_node` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户直属代理商节点',
  `prizes_amount` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '奖励金额',
  `account_status` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '入账状态. 0 未入账, 1已入账，2入账失败',
  `account_time` datetime(0) NULL DEFAULT NULL COMMENT '入账时间',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `operator` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作人',
  `order_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户订单号',
  `prizes_type` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '2' COMMENT '奖励对象类型：1商户,2一级代理商',
  `prizes_object` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '奖励对象编号',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `agent_node_ind`(`agent_node`) USING BTREE,
  INDEX `merchant_no_ind`(`merchant_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 217 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '邀请有奖商户信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for jpush_config
-- ----------------------------
DROP TABLE IF EXISTS `jpush_config`;
CREATE TABLE `jpush_config`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `appNo` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `team_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `ios_master_secret` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `ios_app_key` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `android_master_secret` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `android_app_key` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '极光推送配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for jpush_device
-- ----------------------------
DROP TABLE IF EXISTS `jpush_device`;
CREATE TABLE `jpush_device`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户类型 v2Mer、repayMer、v2Agent',
  `user_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户编号（商户编号/代理商编号）',
  `app_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'app编号',
  `device_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '设备编号',
  `device_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备类型 1:android 2：ios',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_update_time` timestamp(0) NULL DEFAULT NULL COMMENT '最后更新时间',
  `bak1` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ind_mer_app`(`user_type`, `user_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2819 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '新激光推送设备信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for jpush_device_tmp
-- ----------------------------
DROP TABLE IF EXISTS `jpush_device_tmp`;
CREATE TABLE `jpush_device_tmp`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户类型 v2Mer、repayMer、v2Agent',
  `user_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户编号（商户编号/代理商编号）',
  `app_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'app编号',
  `device_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '设备编号',
  `device_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备类型 1:android 2：ios',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_update_time` timestamp(0) NULL DEFAULT NULL COMMENT '最后更新时间',
  `bak1` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ind_mer_app`(`user_type`, `user_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12336805 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '新激光推送设备信息表-20190816' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for jpush_service
-- ----------------------------
DROP TABLE IF EXISTS `jpush_service`;
CREATE TABLE `jpush_service`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_no` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT 'app编号',
  `app_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'app名称',
  `android_app_key` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '极光推送android key',
  `android_master_secret` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '极光推送android secret',
  `ios_app_key` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '极光推送ios key',
  `ios_master_secret` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '极光推送ios secret',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `app_no_index`(`app_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 30 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'V2极光推送注册配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for js_modules
-- ----------------------------
DROP TABLE IF EXISTS `js_modules`;
CREATE TABLE `js_modules`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '模块名称',
  `files` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '组件名称',
  `cache` int(1) NOT NULL DEFAULT 1 COMMENT '是否缓存',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for js_route
-- ----------------------------
DROP TABLE IF EXISTS `js_route`;
CREATE TABLE `js_route`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `url` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '路径',
  `abstract` int(1) NULL DEFAULT NULL COMMENT 'abstract',
  `templateUrl` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'templateUrl',
  `views` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'views',
  `controller` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'controller',
  `loadPlugin` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组件',
  `deps` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '依赖包',
  `parent` int(11) NOT NULL COMMENT '上级依赖: 0-无上级',
  `orders` int(3) NOT NULL COMMENT '排序',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'angularjs 路由' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for jump_route_config
-- ----------------------------
DROP TABLE IF EXISTS `jump_route_config`;
CREATE TABLE `jump_route_config`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `acq_id` int(11) NOT NULL COMMENT '收单机构ID编号',
  `group_code` int(11) NULL DEFAULT NULL COMMENT '跳转目标路由集群编号',
  `card_type` int(11) NULL DEFAULT NULL COMMENT '卡类型 1：货记卡，2：借记卡',
  `status` int(11) NULL DEFAULT NULL COMMENT '状态 0:关闭，1：开启',
  `start_date` date NULL DEFAULT NULL COMMENT '开始生效日期',
  `end_date` date NULL DEFAULT NULL COMMENT '截止生效日期',
  `start_time` time(0) NULL DEFAULT NULL COMMENT '每天生效时间',
  `end_time` time(0) NULL DEFAULT NULL COMMENT '每天截止时间',
  `min_trans_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '最小交易金额',
  `max_trans_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '最大交易金额',
  `jump_times` int(11) NULL DEFAULT NULL COMMENT '跳转次数',
  `apart_days` int(11) NULL DEFAULT NULL COMMENT '相隔天数',
  `week_days` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '每周重复:0,1,2,3,4,5,6,7分别表示周,周六，如果有多个，用逗号隔开',
  `bp_ids` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务产品:0为不限，如果有多个，用逗号隔开',
  `merchant_provinces` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户省份:0位不限，如果有多个，用逗号隔开',
  `remark` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `card_bin_ids` varchar(5000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发卡行:0为不限，如果有多个，用逗号隔开',
  `merchant_city` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '商户市区:0为所选省下的市不限,如果有多个,用逗号隔开',
  `acq_merchant_type` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '指定行业:0为不限,如果有多个,用逗号隔开',
  `service_types` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0',
  `effective_date_type` int(3) NOT NULL DEFAULT 1 COMMENT '生效日期类型，1：每天，\r\n2：周一至周五，3：法定节假日，4：自定义',
  `target_amount` decimal(20, 3) NULL DEFAULT NULL COMMENT '目标金额',
  `total_amount` decimal(20, 3) NOT NULL DEFAULT 0.000 COMMENT '已累计金额',
  `sms_warning_date` timestamp(0) NULL DEFAULT NULL COMMENT '短信预警发送时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 51 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '按交易跳转路由集群配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for jump_route_whitelist
-- ----------------------------
DROP TABLE IF EXISTS `jump_route_whitelist`;
CREATE TABLE `jump_route_whitelist`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '交易跳转路由集群白名单ID',
  `merchant_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户编号',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `create_person` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '交易跳转路由集群白名单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for jump_router_config
-- ----------------------------
DROP TABLE IF EXISTS `jump_router_config`;
CREATE TABLE `jump_router_config`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `start_day` int(11) NULL DEFAULT NULL COMMENT '生效日期的开始X号',
  `end_day` int(11) NULL DEFAULT NULL COMMENT '生效日期的结束X号',
  `card_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '每天生效开始的时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '每天生效结束的时间',
  `jump_count` int(11) NULL DEFAULT NULL,
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` timestamp(0) NULL DEFAULT NULL,
  `days_apart` int(11) NULL DEFAULT NULL COMMENT '相隔天数',
  `global_min_amount` decimal(30, 2) NULL DEFAULT NULL COMMENT '总控最小金额',
  `global_max_amount` decimal(30, 2) NULL DEFAULT NULL COMMENT '总控最大金额',
  `order_index` int(10) NULL DEFAULT NULL,
  `status` int(2) NOT NULL DEFAULT 1 COMMENT '状态, 0不起效，1起效',
  `pos_types` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '机具类型,以逗号，分割',
  `router_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '跳转集群编号',
  `acq_ennames` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ladder_share_replenish
-- ----------------------------
DROP TABLE IF EXISTS `ladder_share_replenish`;
CREATE TABLE `ladder_share_replenish`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `service_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '服务ID',
  `insub_ladder_profit` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '阶梯分润补差时，内部账科目',
  `outsub_ladder_profit` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '阶梯分润补差时，外部账科目',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `service_id_UNIQUE`(`service_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '服务代理商阶梯分润补差记账配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for live_verify_channel
-- ----------------------------
DROP TABLE IF EXISTS `live_verify_channel`;
CREATE TABLE `live_verify_channel`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `channel_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '验证通道代码',
  `channel_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '验证通道名称',
  `account` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '通道分配的账号/appid',
  `pwd` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '通道分配的账号对应密码/appsecret',
  `status` int(1) NULL DEFAULT 1 COMMENT '状态 关闭0  正常1',
  `percent` int(3) NULL DEFAULT 0 COMMENT '路由比例',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `route_type` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '路由类型:1:活体检测,2:OCR,3:人脸/人证',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '活体验证通道信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for login_log
-- ----------------------------
DROP TABLE IF EXISTS `login_log`;
CREATE TABLE `login_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `mobile_phone` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登录手机号',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `team_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组织ID',
  `msg` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '返回信息',
  `params` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '参数',
  `status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '状态(1:正常,2失败,3密码失败)',
  `ip` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'ip地址',
  `method` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '方法类型',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1854 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for luck_config
-- ----------------------------
DROP TABLE IF EXISTS `luck_config`;
CREATE TABLE `luck_config`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `func_code` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '活动类型',
  `func_name` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '活动名称',
  `func_desc` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '活动说明',
  `start_time` timestamp(0) NOT NULL COMMENT '开始时间',
  `end_time` timestamp(0) NOT NULL COMMENT '结束时间',
  `warn` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '活动为零提示',
  `amount` double(8, 2) NOT NULL COMMENT '抽奖条件金额',
  `operator` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '操作人',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_update_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '抽奖配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for luck_report
-- ----------------------------
DROP TABLE IF EXISTS `luck_report`;
CREATE TABLE `luck_report`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `req_date` date NOT NULL COMMENT '日期',
  `req_sole` int(11) NOT NULL COMMENT '独立访问',
  `req_count` int(11) NOT NULL COMMENT '访问量',
  `req_person` int(11) NOT NULL COMMENT '参与人次',
  `req_mer` int(11) NOT NULL COMMENT '参与商户',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 68 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '抽奖统计' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mer_acq_bind_card
-- ----------------------------
DROP TABLE IF EXISTS `mer_acq_bind_card`;
CREATE TABLE `mer_acq_bind_card`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `merchant_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户编号',
  `channel_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '通道代码',
  `card_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '卡号',
  `order_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单号',
  `txn_date` timestamp(0) NULL DEFAULT NULL COMMENT '订单发送时间',
  `status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '绑卡状态 0失败  1成功',
  `code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '返回状态码',
  `msg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '返回描述',
  `create_time` timestamp(0) NULL DEFAULT NULL,
  `last_update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ind_card`(`channel_code`, `card_no`, `status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 34 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商户通道绑卡信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mer_acq_mobile_msg
-- ----------------------------
DROP TABLE IF EXISTS `mer_acq_mobile_msg`;
CREATE TABLE `mer_acq_mobile_msg`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `merchant_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户编号',
  `mobile_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '手机号码',
  `channel_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '通道编码',
  `order_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单号',
  `txn_date` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单发送时间',
  `code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '返回码',
  `msg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `have_used` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '是否已使用 0未使用 1已使用',
  `bak1` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `bak2` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 175 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '通道短信记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mer_address_update_log
-- ----------------------------
DROP TABLE IF EXISTS `mer_address_update_log`;
CREATE TABLE `mer_address_update_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `merchant_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `old_address` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改前地址',
  `new_address` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改后地址',
  `update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1028 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商户地址修改记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mer_bal_temp
-- ----------------------------
DROP TABLE IF EXISTS `mer_bal_temp`;
CREATE TABLE `mer_bal_temp`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `mer_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号',
  `merba` decimal(20, 2) NULL DEFAULT NULL COMMENT '本地余额',
  `hlbba` decimal(20, 2) NULL DEFAULT NULL COMMENT '上游',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_card_no`(`mer_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12051 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '余额表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mer_key
-- ----------------------------
DROP TABLE IF EXISTS `mer_key`;
CREATE TABLE `mer_key`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户类型，  A:代理商，M:商户',
  `user_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '当代理商则是代理商编号，商户则是商户编号',
  `user_ip` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户IP',
  `key_content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密钥',
  `status` int(11) NOT NULL DEFAULT 1 COMMENT '状态，0：无效，1：有效',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `operator` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作人Id',
  `operator_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作人名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '下放商户密钥' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mer_name_update_log
-- ----------------------------
DROP TABLE IF EXISTS `mer_name_update_log`;
CREATE TABLE `mer_name_update_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `merchant_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号',
  `old_merchant_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改前商户名称',
  `new_merchant_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改后商户名称',
  `update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1029 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商户名称修改记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mer_wechat_info
-- ----------------------------
DROP TABLE IF EXISTS `mer_wechat_info`;
CREATE TABLE `mer_wechat_info`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户ID',
  `openid` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `public_account` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公众号账号',
  `status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'NORMAL' COMMENT '用户状态  NORMAL正常    LOCK锁定    CLOSE关闭',
  `nickname` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `sex` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户的性别，值为1时是男性，值为2时是女性，值为0时是未知',
  `province` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户个人资料填写的省份',
  `city` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '普通用户个人资料填写的城市',
  `country` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '国家，如中国为CN',
  `headimgurl` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像URL将失效。',
  `unionid` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '将公众号绑定到微信开放平台帐号后，才会出现该字段',
  `wechat_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '微信号',
  `create_time` timestamp(0) NULL DEFAULT NULL,
  `last_update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后更新时间',
  PRIMARY KEY (`id`, `openid`) USING BTREE,
  UNIQUE INDEX `NewIndex1`(`openid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '微信用户绑定客户账号信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for merchant_auto_check_log
-- ----------------------------
DROP TABLE IF EXISTS `merchant_auto_check_log`;
CREATE TABLE `merchant_auto_check_log`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `merchant_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '代理商编号',
  `oper_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '操作',
  `oper_status` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '修改状态',
  `oper_time` timestamp(0) NULL DEFAULT NULL COMMENT '操作时间',
  `oper_user` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作人',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 287 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '活体验证自审日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for merchant_business_product
-- ----------------------------
DROP TABLE IF EXISTS `merchant_business_product`;
CREATE TABLE `merchant_business_product`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `merchant_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户ID',
  `bp_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务产品ID',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '申请时间',
  `sale_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属销售',
  `status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态 1待一审  2待平台审核  3审核失败  4正常 5已转自动审件 0关闭',
  `examination_opinions` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '审核意见',
  `item_source` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '进件来源： 1：商户APP 2：代理商APP 3：代理商WEB 4:关联进件系统自动开通,5还款系统',
  `last_update_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `auditor_id` int(11) NULL DEFAULT NULL COMMENT '审核人员',
  `auto_check_times` int(11) NULL DEFAULT 0 COMMENT '自动审核次数',
  `trade_type` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '商户交易模式 0：集群模式，1：直清模式',
  `zf_picture` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '是否上传照片到中付1是0为否',
  `auto_mbp_channel` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '活体检测通道',
  `reexamine_status` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '复审状态：未复审0，复审通过1，复审不通过2，复审退件3',
  `reexamine_time` timestamp(0) NULL DEFAULT NULL COMMENT '复审时间',
  `reexamine_operator` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '复审人员',
  `reexamine_tip_end_time` timestamp(0) NULL DEFAULT NULL COMMENT '提示复审不通过的用户修改资料的截止时间',
  `audit_num` int(11) NULL DEFAULT 0 COMMENT '审核次数,只记录人工审核(初审)',
  `ocr` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'OCR通道',
  `face` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '人脸/人证通道',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `merchant_id_UNIQUE`(`merchant_no`, `bp_id`) USING BTREE,
  INDEX `mer_no_index`(`merchant_no`) USING BTREE,
  INDEX `ind_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 205000542 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商户业务产品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for merchant_business_product_20190413
-- ----------------------------
DROP TABLE IF EXISTS `merchant_business_product_20190413`;
CREATE TABLE `merchant_business_product_20190413`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `merchant_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户ID',
  `bp_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务产品ID',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '申请时间',
  `sale_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属销售',
  `status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态 1待一审  2待平台审核  3审核失败  4正常 5已转自动审件 0关闭',
  `examination_opinions` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '审核意见',
  `item_source` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '进件来源： 1：商户APP 2：代理商APP 3：代理商WEB 4:关联进件系统自动开通,5还款系统',
  `last_update_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `auditor_id` int(11) NULL DEFAULT NULL COMMENT '审核人员',
  `auto_check_times` int(11) NULL DEFAULT 0 COMMENT '自动审核次数',
  `trade_type` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '商户交易模式 0：集群模式，1：直清模式',
  `zf_picture` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '是否上传照片到中付1是0为否',
  `auto_mbp_channel` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '自动审件通道编码',
  `reexamine_status` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '复审状态：未复审0，复审通过1，复审不通过2，复审退件3',
  `reexamine_time` timestamp(0) NULL DEFAULT NULL COMMENT '复审时间',
  `reexamine_operator` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '复审人员',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `merchant_id_UNIQUE`(`merchant_no`, `bp_id`) USING BTREE,
  INDEX `mer_no_index`(`merchant_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12520410 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商户业务产品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for merchant_business_product_history
-- ----------------------------
DROP TABLE IF EXISTS `merchant_business_product_history`;
CREATE TABLE `merchant_business_product_history`  (
  `id` int(4) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `source_bp_id` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '原业务产品ID',
  `new_bp_id` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '新业务产品ID',
  `operation_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作类型1关闭2更换',
  `operation_person_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作人类型1运营2商户',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  `operation_person_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作人编号',
  `merchant_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 83 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商户业务产品历史' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for merchant_card_info
-- ----------------------------
DROP TABLE IF EXISTS `merchant_card_info`;
CREATE TABLE `merchant_card_info`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `merchant_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户ID',
  `card_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型:借记卡1、贷记卡2',
  `quick_pay` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否快捷支付:1-是,2-否',
  `def_quick_pay` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否默认快捷支付:1-是,2-否',
  `def_settle_card` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否默认结算卡:1-是,2-否',
  `account_type` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账户类型:1-对公,2-对私',
  `bank_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '开户行全称',
  `cnaps_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联行行号',
  `account_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '开户名',
  `account_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '开户账号',
  `status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `account_province` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '开户行地区：省',
  `account_city` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '开户行地区：市',
  `account_district` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '开户行地区：区或县',
  `update_settle_card_time` timestamp(0) NULL DEFAULT NULL COMMENT '修改结算卡的时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `merchant_id`(`merchant_no`, `account_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 72217 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商户银行卡信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for merchant_card_trans
-- ----------------------------
DROP TABLE IF EXISTS `merchant_card_trans`;
CREATE TABLE `merchant_card_trans`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `merchant_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户ID',
  `card_nu` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '卡号',
  `service_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '服务ID',
  `card_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银行卡种类:0-不限，1-只信用卡，2-只储蓄卡',
  `holidays_mark` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '节假日标志:1-只工作日，2-只节假日，0-不限',
  `interval` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '区间:如2016－1－30或2016－1或2016',
  `accum_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '累计类型:日、月、年',
  `accum_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '累计交易金额',
  `accum_count` int(10) NULL DEFAULT 0 COMMENT '累计交易笔数',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `merchant_id_UNIQUE`(`merchant_id`, `service_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商户卡片交易累计表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for merchant_check_person
-- ----------------------------
DROP TABLE IF EXISTS `merchant_check_person`;
CREATE TABLE `merchant_check_person`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '审核人管理ID编号',
  `user_id` int(11) NOT NULL COMMENT '商户审核人ID',
  `agent_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '指定审核代理商编号',
  `pos_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '指定审核设备类型',
  `check_type` int(11) NOT NULL COMMENT '审核类型：1.顺序、2.设备类型、3.代理商',
  `check_status` int(11) NOT NULL COMMENT '审核人状态：1.启用 2.关闭',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_person` int(11) NOT NULL COMMENT '创建人ID',
  `remarks` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '备注',
  `check_count` int(20) NOT NULL DEFAULT 0,
  `check_count_init` int(20) NULL DEFAULT NULL COMMENT '审件数量',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '商户审核人管理表\r\n本表用于管理商户进件审核人分发，可根据设备类型、代理商指定审核人员，以及按顺序队列方式依次分发给审核人。' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for merchant_goods
-- ----------------------------
DROP TABLE IF EXISTS `merchant_goods`;
CREATE TABLE `merchant_goods`  (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `merchant_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户编号',
  `name` varchar(55) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `shop_price` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `goods_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品类别（1电器，2百货，3餐饮，4娱乐场所，5虚拟服务）',
  `goods_img` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品图片',
  `create_time` timestamp(0) NULL DEFAULT NULL,
  `stock` int(10) NULL DEFAULT NULL COMMENT '库存',
  `supply_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '供应类别 1 内部，2外部',
  `shop_id` int(20) NULL DEFAULT NULL COMMENT '商店id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for merchant_info
-- ----------------------------
DROP TABLE IF EXISTS `merchant_info`;
CREATE TABLE `merchant_info`  (
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
  `status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户状态：0：商户关闭；1：正常；2 冻结',
  `parent_node` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商节点',
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
  `recommended_source` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '推广来源(0:正常,1:为超级推，2：代理商推荐的商户，3人人代理推荐)',
  `source_sys` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `source_reference_no` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `pre_frozen_amount` decimal(30, 2) NULL DEFAULT 0.00 COMMENT '预冻结金额',
  `push_flag` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易推送开关(1：推送,2:不推送)',
  `voice_flag` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '语音开关(1：开,2:关)',
  `bonus_flag` int(4) NULL DEFAULT NULL COMMENT '鼓励金标识 0没有 1有',
  `risk_settle` int(2) NULL DEFAULT 1 COMMENT '标记触发119风控的状态:1正常，2已触发3已解除风控4已过校验',
  `risk_face_times` int(2) NULL DEFAULT 0 COMMENT '人脸识别次数',
  `hlf_active` int(2) NULL DEFAULT 0 COMMENT '是否欢乐返激活0 否 1 是',
  `team_entry_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '子级组织ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `merchant_id_UNIQUE`(`merchant_no`) USING BTREE,
  UNIQUE INDEX `mobilephone`(`mobilephone`, `team_id`) USING BTREE,
  INDEX `id_card_no`(`id_card_no`, `team_id`) USING BTREE,
  INDEX `index_lawyer`(`lawyer`) USING BTREE,
  INDEX `index_mer_agent_no`(`agent_no`) USING BTREE,
  INDEX `idx_one_agent_no`(`one_agent_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4023094 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for merchant_info_bak1
-- ----------------------------
DROP TABLE IF EXISTS `merchant_info_bak1`;
CREATE TABLE `merchant_info_bak1`  (
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
  `status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户状态：0：商户关闭；1：正常；2 冻结',
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
  `recommended_source` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '推广来源(0:正常,1:为超级推，2：代理商推荐的商户，3人人代理推荐)',
  `source_sys` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `source_reference_no` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `pre_frozen_amount` decimal(30, 2) NULL DEFAULT 0.00 COMMENT '预冻结金额',
  `push_flag` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易推送开关(1：推送,2:不推送)',
  `voice_flag` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '语音开关(1：开,2:关)',
  `bonus_flag` int(4) NULL DEFAULT NULL COMMENT '鼓励金标识 0没有 1有',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `merchant_id_UNIQUE`(`merchant_no`) USING BTREE,
  UNIQUE INDEX `mobilephone`(`mobilephone`, `team_id`) USING BTREE,
  INDEX `id_card_no`(`id_card_no`, `team_id`) USING BTREE,
  INDEX `index_lawyer`(`lawyer`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 617129 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for merchant_info_bak20181102
-- ----------------------------
DROP TABLE IF EXISTS `merchant_info_bak20181102`;
CREATE TABLE `merchant_info_bak20181102`  (
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
  `status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户状态：0：商户关闭；1：正常；2 冻结',
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
  `recommended_source` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '推广来源(0:正常,1:为超级推，2：代理商推荐的商户，3人人代理推荐)',
  `source_sys` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `source_reference_no` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `pre_frozen_amount` decimal(30, 2) NULL DEFAULT 0.00 COMMENT '预冻结金额',
  `push_flag` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易推送开关(1：推送,2:不推送)',
  `voice_flag` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '语音开关(1：开,2:关)',
  `bonus_flag` int(4) NULL DEFAULT NULL COMMENT '鼓励金标识 0没有 1有',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `merchant_id_UNIQUE`(`merchant_no`) USING BTREE,
  UNIQUE INDEX `mobilephone`(`mobilephone`, `team_id`) USING BTREE,
  INDEX `id_card_no`(`id_card_no`, `team_id`) USING BTREE,
  INDEX `index_lawyer`(`lawyer`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 616730 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for merchant_ladder_rate
-- ----------------------------
DROP TABLE IF EXISTS `merchant_ladder_rate`;
CREATE TABLE `merchant_ladder_rate`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `merchant_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户ID',
  `service_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '服务ID',
  `card_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银行卡种类:0-不限，1-只信用卡，2-只储蓄卡',
  `holidays_mark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '节假日标志:1-只工作日，2-只节假日，0-不限',
  `efficient_date` date NULL DEFAULT NULL COMMENT '生效日期',
  `disabled_date` date NULL DEFAULT NULL COMMENT '失效日期',
  `trans_amount_floor` decimal(20, 2) NULL DEFAULT NULL COMMENT '交易额下限',
  `trans_amount_ceiling` decimal(20, 2) NULL DEFAULT NULL COMMENT '交易额上限',
  `rate` decimal(20, 6) NULL DEFAULT NULL COMMENT '扣率',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `bph_key`(`merchant_id`, `service_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商户服务阶梯费率表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for merchant_migrate
-- ----------------------------
DROP TABLE IF EXISTS `merchant_migrate`;
CREATE TABLE `merchant_migrate`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `oa_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '工作流编号',
  `agent_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '目标一级代理商编号',
  `node_agent_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '所属代理商编号',
  `check_person` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '审核人',
  `check_status` int(2) NOT NULL DEFAULT 1 COMMENT '状态 1.待审核 2.审核通过 3.审核不通过 4.已迁移 5.已撤销 6.迁移失败 7.部分成功',
  `check_remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '审核描述',
  `create_person` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '申请人',
  `go_sn` int(11) NOT NULL DEFAULT 2 COMMENT '设备随迁 1.不随迁  2.随迁',
  `file_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件名称',
  `check_time` timestamp(0) NULL DEFAULT NULL COMMENT '审核时间',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
  `migrate_time` timestamp(0) NULL DEFAULT NULL COMMENT '迁移完成时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `ind_agent_no`(`agent_no`) USING BTREE,
  INDEX `ind_node_agent`(`node_agent_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 158 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商户迁移\r\n本表用于存放需要转移代理商的商户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for merchant_migrate_info
-- ----------------------------
DROP TABLE IF EXISTS `merchant_migrate_info`;
CREATE TABLE `merchant_migrate_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `migrate_id` int(11) NOT NULL COMMENT '商户迁移主表ID',
  `merchant_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户编号',
  `migrate_status` int(2) NOT NULL DEFAULT 1 COMMENT '1.未迁移 2.已迁移 3.迁移失败 4.已撤销 5.审核失败 ',
  `before_agentNo` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '迁移前的一级代理商编号',
  `modify_agent_no` int(2) NOT NULL COMMENT '是否修改一级代理商 1.是 2.否 本字段值为1时由定时任务次月1日凌晨修改',
  `before_node_agentNo` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '迁移前的所属代理商编号',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `migrate_time` timestamp(0) NULL DEFAULT NULL COMMENT '迁移时间',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `migrate_id_merchant_no`(`migrate_id`, `merchant_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 154 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商户迁移信息子表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for merchant_push_device
-- ----------------------------
DROP TABLE IF EXISTS `merchant_push_device`;
CREATE TABLE `merchant_push_device`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `mobilephone` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号码',
  `device_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备编号',
  `os_type` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备类型 1:android 2：ios',
  `last_update_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `merchant_no`(`mobilephone`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7704 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for merchant_require_history
-- ----------------------------
DROP TABLE IF EXISTS `merchant_require_history`;
CREATE TABLE `merchant_require_history`  (
  `id` bigint(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `merchant_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号',
  `mri_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '进件要求项ID',
  `modify_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改方式',
  `history_content` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '历史数据',
  `new_content` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '历史数据',
  `batch_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改批次号',
  `status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '1成功0失败',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `creator` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `remark` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `attribute1` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '备用字段1',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1488 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商户进件项修改记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for merchant_require_item
-- ----------------------------
DROP TABLE IF EXISTS `merchant_require_item`;
CREATE TABLE `merchant_require_item`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `merchant_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户编号',
  `mri_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '进件要求项ID',
  `content` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '附件地址',
  `status` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态：0待审核；1通过；2审核失败',
  `audit_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '审核时间',
  `last_update_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `pic_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `merchant_id_UNIQUE`(`merchant_no`, `mri_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 62778 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商户进件要求项资料表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for merchant_service
-- ----------------------------
DROP TABLE IF EXISTS `merchant_service`;
CREATE TABLE `merchant_service`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `bp_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务产品ID',
  `merchant_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户ID',
  `service_id` bigint(20) NULL DEFAULT NULL COMMENT '服务ID',
  `create_date` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
  `disabled_date` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '失效时间',
  `status` int(10) NULL DEFAULT 1 COMMENT '状态:1-正常状态，0-关闭',
  `trade_type` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '商户服务交易模式 0：集群模式，1：直清模式',
  `channel_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '通道编码',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `merchant_service_UNIQUE`(`merchant_no`, `service_id`, `bp_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11146455 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商户服务表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for merchant_service_quota
-- ----------------------------
DROP TABLE IF EXISTS `merchant_service_quota`;
CREATE TABLE `merchant_service_quota`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `service_id` bigint(20) NULL DEFAULT NULL COMMENT '服务ID',
  `card_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银行卡种类:0-不限，1-只信用卡，2-只储蓄卡',
  `holidays_mark` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '节假日标志:1-只工作日，2-只节假日，0-不限',
  `merchant_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户ID',
  `single_day_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '单日最大交易额',
  `single_min_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '单笔最小交易额',
  `single_count_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '单笔最大交易额',
  `single_daycard_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '单日单卡最大交易额',
  `single_daycard_count` int(10) NULL DEFAULT 0 COMMENT '单日单卡最大交易笔数',
  `efficient_date` date NULL DEFAULT NULL COMMENT '生效时间',
  `disabled_date` timestamp(0) NULL DEFAULT NULL COMMENT '失效时间',
  `useable` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否生效：0无效，1有效',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `service_id_UNIQUE`(`service_id`, `merchant_no`, `card_type`, `holidays_mark`, `efficient_date`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3280 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商户服务限额表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for merchant_service_rate
-- ----------------------------
DROP TABLE IF EXISTS `merchant_service_rate`;
CREATE TABLE `merchant_service_rate`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `service_id` bigint(20) NULL DEFAULT NULL COMMENT '服务ID',
  `merchant_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户ID',
  `holidays_mark` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '节假日标志:1-只工作日，2-只节假日，0-不限',
  `card_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银行卡种类:0-不限，1-只信用卡，2-只储蓄卡',
  `rate_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '费率类型:1-每笔固定金额，2-扣率，3-扣率带保底封顶，4-扣率+固定金额,5-单笔阶梯 扣率',
  `single_num_amount` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '每笔固定值',
  `rate` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '扣率',
  `capping` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '封顶',
  `safe_line` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '保底',
  `efficient_date` date NULL DEFAULT NULL COMMENT '生效日期',
  `disabled_date` timestamp(0) NULL DEFAULT NULL COMMENT '失效日期',
  `ladder1_rate` decimal(10, 2) NULL DEFAULT NULL COMMENT '阶梯区间1费率',
  `ladder1_max` decimal(20, 2) NULL DEFAULT NULL COMMENT '阶梯区间1上限',
  `ladder2_rate` decimal(10, 2) NULL DEFAULT NULL COMMENT '阶梯区间2费率',
  `ladder2_max` decimal(20, 2) NULL DEFAULT NULL COMMENT '阶梯区间2上限',
  `ladder3_rate` decimal(10, 2) NULL DEFAULT NULL COMMENT '阶梯区间3费率',
  `ladder3_max` decimal(20, 2) NULL DEFAULT NULL COMMENT '阶梯区间3上限',
  `ladder4_rate` decimal(10, 2) NULL DEFAULT NULL COMMENT '阶梯区间4费率',
  `ladder4_max` decimal(20, 2) NULL DEFAULT NULL COMMENT '阶梯区间4上限',
  `useable` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否生效：0无效，1有效',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `service_id_UNIQUE`(`service_id`, `merchant_no`, `card_type`, `holidays_mark`, `efficient_date`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2921 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商户服务签约费率' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for merchant_subscribe_vip
-- ----------------------------
DROP TABLE IF EXISTS `merchant_subscribe_vip`;
CREATE TABLE `merchant_subscribe_vip`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `merchant_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户编号',
  `validity_end` datetime(0) NULL DEFAULT NULL COMMENT '会员到期时间',
  `last_update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_mer_vip`(`merchant_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 39 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商户会员记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for merchant_trans
-- ----------------------------
DROP TABLE IF EXISTS `merchant_trans`;
CREATE TABLE `merchant_trans`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `merchant_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户ID',
  `service_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '服务ID',
  `card_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银行卡种类:0-不限，1-只信用卡，2-只储蓄卡',
  `holidays_mark` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '节假日标志:1-只工作日，2-只节假日，0-不限',
  `enter_accou_date` date NULL DEFAULT NULL COMMENT '记账日期',
  `time_area` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '时间段:1~24',
  `accum_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '累计交易金额',
  `accum_count` int(11) NULL DEFAULT 0 COMMENT '累计交易笔数',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `merchant_id_UNIQUE`(`merchant_id`, `service_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商户交易累计表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for merchant_trans_total_day
-- ----------------------------
DROP TABLE IF EXISTS `merchant_trans_total_day`;
CREATE TABLE `merchant_trans_total_day`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `merchant_no` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号',
  `day` date NULL DEFAULT NULL COMMENT '日期',
  `amount_count` decimal(15, 2) NULL DEFAULT NULL COMMENT '累计金额',
  `trans_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易类型',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `Unique_merchant_no_day`(`merchant_no`, `day`) USING BTREE,
  INDEX `Normal_merchant_no`(`merchant_no`) USING BTREE,
  INDEX `Normal_day`(`day`) USING BTREE,
  INDEX `Normal_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 357 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '活跃商户活动跑批日结表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for merchant_warning_service
-- ----------------------------
DROP TABLE IF EXISTS `merchant_warning_service`;
CREATE TABLE `merchant_warning_service`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `warning_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'NO_TRAN:无交易,TRAN_SLIDE:交易下滑,UNCERTIFIED:未认证商户',
  `warning_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '服务名称',
  `team_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组织id',
  `is_used` int(1) NULL DEFAULT 1 COMMENT '是否使用 0 否 1 是',
  `warning_img` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '显示图标',
  `warning_title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '显示标题',
  `warning_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '跳转连接',
  `no_tran_day` int(11) NULL DEFAULT 0 COMMENT '无交易天数',
  `tran_slide_rate` decimal(10, 2) NULL DEFAULT 30.00 COMMENT '交易下滑百分比',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商户预警服务表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mobile_ver_info
-- ----------------------------
DROP TABLE IF EXISTS `mobile_ver_info`;
CREATE TABLE `mobile_ver_info`  (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `PLATFORM` int(10) NULL DEFAULT NULL COMMENT '0:android 1:iOS',
  `VERSION` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `APP_URL` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `DOWN_FLAG` int(10) NULL DEFAULT NULL COMMENT '0：不需要,1:需要更新,2：需要强制下载',
  `VER_DESC` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `APP_TYPE` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '0:银联商宝1:移小宝 2:通付宝 3 中宽支付',
  `lowest_version` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最低版本',
  `APP_LOGO` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `CREATE_TIME` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '二维码路径',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 2525 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mycat
-- ----------------------------
DROP TABLE IF EXISTS `mycat`;
CREATE TABLE `mycat`  (
  `id` int(11) NULL DEFAULT NULL,
  `name` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for new_msg_code
-- ----------------------------
DROP TABLE IF EXISTS `new_msg_code`;
CREATE TABLE `new_msg_code`  (
  `msg_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `msg_type` enum('1','2','3','4','5','6','7','8') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `user_msg` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `reason` tinytext CHARACTER SET utf8 COLLATE utf8_general_ci NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '协作添加响应信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for notice_info
-- ----------------------------
DROP TABLE IF EXISTS `notice_info`;
CREATE TABLE `notice_info`  (
  `nt_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `title` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公告标题',
  `title_img` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公告图片标题',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '公告内容',
  `attachment` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公告附件',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `issued_time` timestamp(0) NULL DEFAULT NULL COMMENT '下发时间',
  `login_user` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '当前登录用户',
  `sys_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '下发对象类型 1：商户，2：代理商',
  `receive_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '1：所有级别代理商，2：所有一级代理商，3：直营所有级别代理商，4：直营所有一级代理商，5：非直营所有级别代理商，6：非直营所有一级代理商，7：（非直营下发的公告）所有下级代理商，8：（非直营下发的公告）非直营该代理商下所有二级代理商',
  `agent_role` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商用户角色：1管理员，2销售员',
  `agent_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '指定一级代理商ID,给其下所有的商户公告',
  `bp_id` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '业务产品ID',
  `STATUS` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态：1-待下发，2-正常，3-已删除',
  `issued_org` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '下发组织：0移联,盛钱包一级代理商id',
  `link` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '链接',
  `message_img` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '消息图片',
  `merchant_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户编号',
  `msg_type` int(11) NULL DEFAULT 0 COMMENT '消息类型  0：业务消息  1：系统消息',
  `strong` int(11) NULL DEFAULT 0 COMMENT '置顶  0：未置顶  1：置顶',
  `oem_type` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT 'oem类型,参考字典OEM_TYPE',
  `oem_list` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '超级盟主所属品牌',
  `valid_begin_time` timestamp(0) NULL DEFAULT NULL COMMENT '有效时间段，开始',
  `valid_end_time` timestamp(0) NULL DEFAULT NULL COMMENT '有效时间段，结束',
  `show_status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '弹窗提示  0：非弹窗提示  1：弹窗提示',
  PRIMARY KEY (`nt_id`) USING BTREE,
  INDEX `ind_agent_no`(`agent_no`) USING BTREE,
  INDEX `ind_merchant_no`(`merchant_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 43289 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '公告信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for notice_info_bak
-- ----------------------------
DROP TABLE IF EXISTS `notice_info_bak`;
CREATE TABLE `notice_info_bak`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `nt_id` int(11) NOT NULL COMMENT '公告ID',
  `title` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公告标题',
  `content` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公告内容',
  `attachment` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公告附件',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `issued_time` timestamp(0) NULL DEFAULT NULL COMMENT '下发时间',
  `issued_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '下发对象类型 1：商户，2：代理商，3：OEM',
  `bp_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务产品ID',
  `status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态：1-待下发，2-正常',
  `issued_people` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '下发人（创建人）类型:1-公司，2:OEM',
  `issued_org` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '下发人组织ID（如OEM ID，移付宝）',
  `issued_level` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '下发级别',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `nt_id`(`nt_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '公告信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for online
-- ----------------------------
DROP TABLE IF EXISTS `online`;
CREATE TABLE `online`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ter_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `sn` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `mer_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `type` int(11) NULL DEFAULT NULL,
  `delete_flag` int(10) NULL DEFAULT 0,
  `remark` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_ter_no`(`ter_no`) USING BTREE,
  INDEX `idx_sn`(`sn`) USING BTREE,
  INDEX `ide_mer_id`(`mer_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1015799 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for out_account_service
-- ----------------------------
DROP TABLE IF EXISTS `out_account_service`;
CREATE TABLE `out_account_service`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '出款服务管理ID',
  `acq_org_id` int(11) NOT NULL COMMENT '收单机构ID',
  `service_type` int(4) NOT NULL COMMENT '出款服务类型 1：单笔代付-自有资金 2：单笔代付-垫资  3：批量代付 4：T1线上单笔代付 5：T1线下单笔代付',
  `service_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '出款服务名称',
  `day_total_amount` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '当日累计已出款额度',
  `out_account_min_amount` decimal(10, 2) NOT NULL COMMENT '单笔出款最小限额',
  `out_account_max_amount` decimal(10, 2) NOT NULL COMMENT '单笔出款最大限额',
  `day_out_account_amount` decimal(15, 2) NOT NULL COMMENT '单日出款限额',
  `out_amount_warning` decimal(12, 2) NOT NULL COMMENT '出款预警额度',
  `transformation_amount` decimal(12, 2) NOT NULL COMMENT '跳转服务预警额度',
  `level` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '优先等级',
  `anto_close_msg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '提现自动关闭提示',
  `out_account_status` int(2) NOT NULL COMMENT '状态 1.打开 0.关闭',
  `acq_enname` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收单机构英文简称',
  `day_reset_limit` int(1) NOT NULL DEFAULT 1 COMMENT '每日重置限额 0不重置;1重置',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `user_balance` decimal(15, 2) NULL DEFAULT NULL COMMENT '平台在上游余额',
  `user_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '平台在上游的账号',
  `ic_month_max_amount` decimal(15, 2) NULL DEFAULT NULL COMMENT '身份证月累积最大提现额',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 152 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '出款服务表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for out_account_service_function
-- ----------------------------
DROP TABLE IF EXISTS `out_account_service_function`;
CREATE TABLE `out_account_service_function`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `close_advance_mobile` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '关闭提现预警手机号，如有多个，请用英文逗号,隔开',
  `out_account_mobile` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '打款预警手机号，如有多个，请用英文逗号,隔开',
  `skip_channel_mobile` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '跳转通道预警手机号，如有多个，请用英文逗号,隔开',
  `second_out` int(1) NULL DEFAULT NULL COMMENT '秒出账开关，1.开启，0.关闭',
  `self_audit` int(1) NULL DEFAULT NULL COMMENT '自审开关，1.开启，0.关闭',
  `out_account` int(1) NULL DEFAULT NULL COMMENT '提现开关，1.开启，0.关闭',
  `out_account_failure` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '出款失败预警手机号可以多个逗号隔开',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '出款服务功能表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for out_account_service_rate
-- ----------------------------
DROP TABLE IF EXISTS `out_account_service_rate`;
CREATE TABLE `out_account_service_rate`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '出款服务费率ID',
  `out_account_service_id` int(11) NULL DEFAULT NULL COMMENT '出款服务ID',
  `agent_rate_type` int(2) NULL DEFAULT NULL COMMENT '代付服务费率类型 1：每笔固定金额 2：每笔扣率 3：每笔扣率带保底封顶 4：每笔扣率+每笔固定金额  5：每月日均累计交易量阶梯扣率带保底',
  `cost_rate_type` int(2) NULL DEFAULT NULL COMMENT '垫资资金成本费率类型 1：每笔固定金额 2：每笔扣率  3：每笔扣率带保底  4：每笔扣率+每笔固定金额 5：每月日均累计交易量阶梯扣率带保底',
  `single_amount` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '单笔固定金额',
  `rate` decimal(10, 6) NULL DEFAULT 0.000000 COMMENT '扣率',
  `capping` decimal(10, 6) NULL DEFAULT 0.000000 COMMENT '封顶金额',
  `safe_line` decimal(10, 6) NULL DEFAULT 0.000000 COMMENT '保底金额',
  `ladder1_rate` decimal(10, 6) NULL DEFAULT NULL COMMENT '阶梯区间1费率',
  `ladder1_max` decimal(20, 4) NULL DEFAULT NULL COMMENT '阶梯区间1上限',
  `ladder1_safe_line` decimal(10, 4) NULL DEFAULT NULL COMMENT '阶梯区间1保底金额',
  `ladder2_rate` decimal(10, 6) NULL DEFAULT NULL COMMENT '阶梯区间2费率',
  `ladder2_max` decimal(20, 4) NULL DEFAULT NULL COMMENT '阶梯区间2上限',
  `ladder2_safe_line` decimal(10, 4) NULL DEFAULT NULL COMMENT '阶梯区间2保底金额',
  `ladder3_rate` decimal(10, 6) NULL DEFAULT NULL COMMENT '阶梯区间3费率',
  `ladder3_max` decimal(20, 4) NULL DEFAULT NULL COMMENT '阶梯区间3上限',
  `ladder3_safe_line` decimal(10, 4) NULL DEFAULT NULL COMMENT '阶梯区间3保底金额',
  `ladder4_rate` decimal(10, 6) NULL DEFAULT NULL COMMENT '阶梯区间4费率',
  `ladder4_max` decimal(20, 4) NULL DEFAULT NULL COMMENT '阶梯区间4上限',
  `ladder4_safe_line` decimal(10, 4) NULL DEFAULT NULL COMMENT '阶梯区间4保底金额',
  `effective_status` int(2) NULL DEFAULT 0 COMMENT '是否生效 1.是 2.否',
  `effective_date` datetime(0) NULL DEFAULT NULL COMMENT '生效日期',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_person` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 146 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '出款服务费率表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for out_account_service_rate_task
-- ----------------------------
DROP TABLE IF EXISTS `out_account_service_rate_task`;
CREATE TABLE `out_account_service_rate_task`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '出款服务费率任务ID',
  `out_account_service_rate_id` int(11) NULL DEFAULT NULL COMMENT '出款服务费率ID',
  `agent_rate_type` int(2) NULL DEFAULT NULL COMMENT '代付服务费率类型1：每笔固定金额 2：每笔扣率 3：每笔扣率带保底封顶 4：每笔扣率+每笔固定金额  5：每月日均累计交易量阶梯扣率带保底\'',
  `cost_rate_type` int(2) NULL DEFAULT NULL COMMENT '垫资资金成本费率类型 1：每笔固定金额 2：每笔扣率  3：每笔扣率带保底  4：每笔扣率+每笔固定金额 5：每月日均累计交易量阶梯扣率带保底\'',
  `single_amount` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '单笔固定金额',
  `rate` decimal(10, 6) NULL DEFAULT 0.000000 COMMENT '扣率',
  `capping` decimal(10, 6) NULL DEFAULT 0.000000 COMMENT '封顶金额',
  `safe_line` decimal(10, 6) NULL DEFAULT 0.000000 COMMENT '保底金额',
  `ladder1_rate` decimal(10, 6) NULL DEFAULT NULL COMMENT '阶梯区间1费率',
  `ladder1_max` decimal(20, 4) NULL DEFAULT NULL COMMENT '阶梯区间1上限',
  `ladder2_rate` decimal(10, 6) NULL DEFAULT NULL COMMENT '阶梯区间2费率',
  `ladder2_max` decimal(20, 4) NULL DEFAULT NULL COMMENT '阶梯区间2上限',
  `ladder3_rate` decimal(10, 6) NULL DEFAULT NULL COMMENT '阶梯区间3费率',
  `ladder3_max` decimal(20, 4) NULL DEFAULT NULL COMMENT '阶梯区间3上限',
  `ladder4_rate` decimal(10, 6) NULL DEFAULT NULL COMMENT '阶梯区间4费率',
  `ladder4_max` decimal(20, 4) NULL DEFAULT NULL COMMENT '阶梯区间4上限',
  `effective_status` int(2) NULL DEFAULT 0 COMMENT '是否生效 1.是 2.否',
  `effective_date` datetime(0) NULL DEFAULT NULL COMMENT '生效日期',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_person` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建人',
  `ladder1_safe_line` decimal(10, 4) NULL DEFAULT NULL COMMENT '阶梯区间1保底金额',
  `ladder2_safe_line` decimal(10, 4) NULL DEFAULT NULL COMMENT '阶梯区间2保底金额',
  `ladder3_safe_line` decimal(10, 4) NULL DEFAULT NULL COMMENT '阶梯区间3保底金额',
  `ladder4_safe_line` decimal(10, 4) NULL DEFAULT NULL COMMENT '阶梯区间4保底金额',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 150 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '出款服务费率任务表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pa_change_log
-- ----------------------------
DROP TABLE IF EXISTS `pa_change_log`;
CREATE TABLE `pa_change_log`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `change_pre` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '修改前',
  `change_after` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '修改后',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注，修改内容的简要说明',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `operater` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作人',
  `oper_method` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作的方法',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1996 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '修改记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pa_ter_info
-- ----------------------------
DROP TABLE IF EXISTS `pa_ter_info`;
CREATE TABLE `pa_ter_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_code` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户编号',
  `agent_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商编号',
  `status` int(11) NULL DEFAULT NULL COMMENT '状态  1:可销售，2: 可下发，3：商户已使用；',
  `order_no` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单号',
  `send_lock` int(1) NULL DEFAULT 0 COMMENT '已失效，请勿判断',
  `sn` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '机具SN',
  `merchant_no` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_update` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `start_time` timestamp(0) NULL DEFAULT NULL COMMENT '机具启用时间',
  `callback_lock` int(11) NULL DEFAULT 0 COMMENT '回拨锁定 1锁定 0不锁定',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `Ind_order_no`(`order_no`) USING BTREE,
  INDEX `ind_user_no`(`user_code`) USING BTREE,
  INDEX `ind_sn`(`sn`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9064 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '机具表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pay_address
-- ----------------------------
DROP TABLE IF EXISTS `pay_address`;
CREATE TABLE `pay_address`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `merchant_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号',
  `province` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '省',
  `city` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '市',
  `district` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '区',
  `longitude` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '经度',
  `latitude` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '维度',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT ' 创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 366 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '支付地理位置记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pos_acq_auth
-- ----------------------------
DROP TABLE IF EXISTS `pos_acq_auth`;
CREATE TABLE `pos_acq_auth`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `merchant_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号',
  `card_no` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银行卡号',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `state` int(2) NULL DEFAULT 0 COMMENT '0-未认证，1-已认证',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `merchant_no`(`merchant_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pos_card_bin
-- ----------------------------
DROP TABLE IF EXISTS `pos_card_bin`;
CREATE TABLE `pos_card_bin`  (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `bank_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银行名称',
  `card_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '卡名称',
  `card_length` int(11) NULL DEFAULT NULL COMMENT '卡号长度',
  `card_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '卡类别',
  `verify_length` int(11) NULL DEFAULT NULL COMMENT '卡标识符长度',
  `verify_code` bigint(50) NULL DEFAULT NULL COMMENT '卡标识号取值',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `bank_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `bank_desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `bank_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `full_index`(`card_length`, `verify_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10261 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pos_card_bin1
-- ----------------------------
DROP TABLE IF EXISTS `pos_card_bin1`;
CREATE TABLE `pos_card_bin1`  (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `bank_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银行名称',
  `card_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '卡名称',
  `card_length` int(11) NULL DEFAULT NULL COMMENT '卡号长度',
  `card_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '卡类别',
  `verify_length` int(11) NULL DEFAULT NULL COMMENT '卡标识符长度',
  `verify_code` bigint(50) NULL DEFAULT NULL COMMENT '卡标识号取值',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `bank_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `bank_desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `bank_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `is_norm` int(2) NULL DEFAULT NULL COMMENT '是否银联标准卡,1:是，0:否',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4609 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pos_cnaps
-- ----------------------------
DROP TABLE IF EXISTS `pos_cnaps`;
CREATE TABLE `pos_cnaps`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `cnaps_no` bigint(20) NULL DEFAULT 0,
  `bank_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 330707 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pos_cnaps_test
-- ----------------------------
DROP TABLE IF EXISTS `pos_cnaps_test`;
CREATE TABLE `pos_cnaps_test`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `cnaps_no` bigint(20) NULL DEFAULT 0,
  `bank_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 752650 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pos_type
-- ----------------------------
DROP TABLE IF EXISTS `pos_type`;
CREATE TABLE `pos_type`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `pos_type_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备名称',
  `pos_type` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备类型',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_person` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 101 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '设备类型' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for problem_type
-- ----------------------------
DROP TABLE IF EXISTS `problem_type`;
CREATE TABLE `problem_type`  (
  `id` int(11) NULL DEFAULT NULL,
  `problem_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '问题类型',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `type_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '问题名称'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for profit_update_record
-- ----------------------------
DROP TABLE IF EXISTS `profit_update_record`;
CREATE TABLE `profit_update_record`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `share_id` bigint(20) NOT NULL COMMENT '分润规则id',
  `cost_history` varchar(20) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '修改前代理商成本',
  `cost` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改前代理商成本',
  `share_profit_percent_history` decimal(10, 6) NULL DEFAULT NULL COMMENT '修改前分润比例',
  `share_profit_percent` decimal(10, 6) NULL DEFAULT NULL COMMENT '修改后分润比例',
  `efficient_date` datetime(0) NULL DEFAULT NULL COMMENT '生效日期',
  `effective_status` varchar(20) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '是否生效:0-未生效,1-已生效',
  `update_date` datetime(0) NULL DEFAULT NULL COMMENT '修改日期',
  `auther` varchar(50) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '修改人',
  `share_task_id` int(20) NULL DEFAULT NULL COMMENT '对应agent_share_rule_task表id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '修改分润记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for push_config
-- ----------------------------
DROP TABLE IF EXISTS `push_config`;
CREATE TABLE `push_config`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `target_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '推送目标唯一标识',
  `target_impl` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '推送功能的实现,可用来指定特定的实现类>方法',
  `push_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '推送类型:transaction 交易,merchant 商户,terminal 机具',
  `push_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '推送地址',
  `push_max_num` int(11) NULL DEFAULT NULL COMMENT '最多推送次数',
  `secure_key` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '加密密钥',
  `target_status` int(1) NULL DEFAULT 0 COMMENT '推送目标的状态:0无需推送,1需要推送',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_push_config`(`target_id`, `push_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '推送配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for push_manager
-- ----------------------------
DROP TABLE IF EXISTS `push_manager`;
CREATE TABLE `push_manager`  (
  `id` bigint(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `push_title` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '推送标题',
  `push_content` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '推送内容',
  `jump_url` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '跳转链接',
  `push_obj` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '推送组织,(app_info的appNo,多个逗号间隔)',
  `mobile_terminal_type` int(2) NULL DEFAULT NULL COMMENT '移动端类型  1:android 2:ios',
  `target_user` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '目标用户 0:全部 1部分',
  `push_time` timestamp(0) NULL DEFAULT NULL COMMENT '推送时间',
  `timer_time` timestamp(0) NULL DEFAULT NULL COMMENT '定时时间 展示时间',
  `actual_time` timestamp(0) NULL DEFAULT NULL COMMENT '定时时间 定时任务判断发送时间',
  `push_status` int(2) NULL DEFAULT 0 COMMENT '推送状态 0:未推送 1:已推送 2:推送失败',
  `create_person` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `dingshi_or_now` int(11) NULL DEFAULT NULL COMMENT '定时或者实时 0:实时,1:定时 ',
  `err_msg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '失败原因',
  `push_times` int(1) UNSIGNED ZEROFILL NULL DEFAULT 0 COMMENT '推送次数',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE,
  INDEX `idx_push_time`(`push_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 75 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '推送管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for push_manager_detail
-- ----------------------------
DROP TABLE IF EXISTS `push_manager_detail`;
CREATE TABLE `push_manager_detail`  (
  `id` bigint(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `merchant_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户编号',
  `push_status` int(2) NULL DEFAULT 0 COMMENT '推送状态 0:未推送 1:已推送',
  `mobile_id` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备id',
  `push_id` bigint(20) NULL DEFAULT NULL COMMENT 'push_manager 推送id',
  `push_all` int(2) NULL DEFAULT 0 COMMENT '是否全部 0:否 1:是 ',
  `msg_id` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '推送信息id',
  `msg_result` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '调用推送信息返回结果',
  `push_obj` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '推送对象',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_merchant_no`(`merchant_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 174 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '推送商户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for push_record
-- ----------------------------
DROP TABLE IF EXISTS `push_record`;
CREATE TABLE `push_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `source_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '需要推送的数据的主键',
  `push_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '推送类型:transaction 交易,merchant 商户,terminal 机具,active 激活,settlement 结算',
  `push_num` int(11) NULL DEFAULT 0 COMMENT '已经推送次数',
  `push_status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'RETRY' COMMENT '推送状态:SUCCESS 成功,RETRY 重推',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_push_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后推送时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `unique_push_record`(`source_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5100 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '推送记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for py_identification
-- ----------------------------
DROP TABLE IF EXISTS `py_identification`;
CREATE TABLE `py_identification`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id编号',
  `bat_No` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '流水号，该流水号上游返回',
  `report_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '报告编号',
  `ident_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '验证人姓名',
  `id_card` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '验证对象身份证号',
  `identi_status` int(11) NOT NULL DEFAULT 0 COMMENT '验证状态:1.通过 2.不通过 3.返回异常4.节点为空 5.接口已改变6.链接超时7.未知异常',
  `error_msg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '错误消息',
  `account_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '开户行账号',
  `by_system` int(11) NOT NULL DEFAULT 0 COMMENT '所属系统 1.BOSS 2.代理商',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '验证时间',
  `create_person` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '操作人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '实名认证' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for quickpass_card_bin
-- ----------------------------
DROP TABLE IF EXISTS `quickpass_card_bin`;
CREATE TABLE `quickpass_card_bin`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `bank_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银行名称',
  `bank_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银行简码',
  `card_length` int(11) NULL DEFAULT NULL COMMENT '卡长度',
  `verify_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '卡标识号',
  `verify_length` int(11) NULL DEFAULT NULL COMMENT '卡标识号长度',
  `card_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '卡类型',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 34 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '闪付支持的卡类型' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for rdmp_agent_share_config
-- ----------------------------
DROP TABLE IF EXISTS `rdmp_agent_share_config`;
CREATE TABLE `rdmp_agent_share_config`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `agent_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '代理商编号',
  `share_rate` decimal(20, 2) NOT NULL COMMENT '分润比例',
  `oem_fee` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '积分兑换oem成本',
  `share_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '分润类型  D 报单',
  `remark` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `receive_share_rate` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '收款分润比例',
  `repayment_share_rate` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '还款分润比例',
  `receive_fee` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '收款成本',
  `repayment_fee` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '还款成本',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `agent_no_key`(`agent_no`) USING BTREE,
  INDEX `create_time_key`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 46 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '代理商分润配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for real_auth
-- ----------------------------
DROP TABLE IF EXISTS `real_auth`;
CREATE TABLE `real_auth`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '姓名',
  `id_card` varchar(18) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '身份证号',
  `card_no` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '银行卡号',
  `mobile` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '银行预留手机号',
  `merchant_no` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号',
  `status` int(10) NULL DEFAULT NULL COMMENT '认证状态 1是成功,0失败',
  `white` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否为白名单',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `create_time_ind`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 679 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '实名认证' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for real_auth_pos
-- ----------------------------
DROP TABLE IF EXISTS `real_auth_pos`;
CREATE TABLE `real_auth_pos`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `card_no` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '银行卡号',
  `merchant_no` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 183 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for receive
-- ----------------------------
DROP TABLE IF EXISTS `receive`;
CREATE TABLE `receive`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `acqBatchNo` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `acqMerchantNo` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `acqMerchantName` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `acqReferenceNo` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `acqSerialNo` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `acqTerminalNo` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `acqAuthNo` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `batchNo` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `merchantName` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `merchantNo` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `orderNo` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `respCode` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `respMsg` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `respType` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `seriallNo` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `terminalNo` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `sign` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for replace_card_history
-- ----------------------------
DROP TABLE IF EXISTS `replace_card_history`;
CREATE TABLE `replace_card_history`  (
  `id` bigint(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `history_card_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '历史银行卡号',
  `mobilephone` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `card_file_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上传银行卡照片的名字',
  `merchant_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号',
  `status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '1成功0失败',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `new_card_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '新的卡号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 33 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '修改结算卡号记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for request
-- ----------------------------
DROP TABLE IF EXISTS `request`;
CREATE TABLE `request`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `amount` decimal(10, 2) NULL DEFAULT NULL,
  `acqMerchantNo` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `acqSerialNo` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `acqTerminalNo` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `cardNo` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `icMsg` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `orderNo` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `pin` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `readCardType` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `seq` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `sign` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `track2` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `acqMerMsg` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `acqEnname` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20436 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for reward_activity_info
-- ----------------------------
DROP TABLE IF EXISTS `reward_activity_info`;
CREATE TABLE `reward_activity_info`  (
  `id` int(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键、活动编号',
  `activity_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '活动名称',
  `activity_type` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '活动类型（默认活动名称）',
  `activity_explain` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '活动说明',
  `activity_amount` decimal(30, 2) NULL DEFAULT NULL COMMENT '赠送金额',
  `activity_first` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '活动优先级：A-E优先级递增',
  `participate_count` int(20) NULL DEFAULT NULL COMMENT '可参与次数、可获取数量（-1：不限）',
  `effective_days` int(20) NULL DEFAULT NULL COMMENT '有效天数',
  `effective_time` datetime(0) NULL DEFAULT NULL COMMENT '有效日期、到期时间',
  `purpose` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用途（抵扣提现手续费）',
  `activity_notice` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '活动下发通知信息',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `operator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '鼓励金活动管理表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for risk_event_detail
-- ----------------------------
DROP TABLE IF EXISTS `risk_event_detail`;
CREATE TABLE `risk_event_detail`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `r_id` bigint(20) NOT NULL COMMENT '外键',
  `order_no` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单号',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_r_id`(`r_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3523 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '风控事件详情' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for risk_event_record
-- ----------------------------
DROP TABLE IF EXISTS `risk_event_record`;
CREATE TABLE `risk_event_record`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `roll_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '编号（关联黑名单主表）',
  `merchant_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户ID',
  `merchant_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户名称',
  `agent_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '代理商编号',
  `rules_no` int(10) UNSIGNED NOT NULL COMMENT '规则编号',
  `rule_desc` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '规则描述',
  `rules_instruction` int(10) NULL DEFAULT NULL COMMENT '规则指令（与黑名单表（黑名单类型对应）,1商户黑名单,2身份证黑名单,3银行卡黑名单,4钱包出账黑名单,5实名认证,6风险预警',
  `handle_status` int(1) NULL DEFAULT NULL COMMENT '处理状态：1已处理  0未处理',
  `handle_results` int(1) NULL DEFAULT NULL COMMENT '处理结果：1安全  2可疑  3风险',
  `handle_time` timestamp(0) NULL DEFAULT NULL COMMENT '处理时间',
  `handle_person` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '处理人',
  `handle_remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '处理备注',
  `order_no` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1057 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '风控事件记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for risk_problem
-- ----------------------------
DROP TABLE IF EXISTS `risk_problem`;
CREATE TABLE `risk_problem`  (
  `problem_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '问题编号',
  `problem_type` int(1) NOT NULL COMMENT '问题类型 1：调单 2：风控规则  3：其他',
  `risk_rules_no` int(10) NOT NULL COMMENT '风控规则编号',
  `problem_title` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '问题标题',
  `problem_description` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '问题描述',
  `status` int(1) NOT NULL COMMENT '问题状态 1：待处理  2：待审核  3：审核通过  4：审核不通过',
  `deal_person` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '处理人',
  `deal_measures` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '处理措施',
  `create_person` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`problem_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '风控问题管理表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for risk_problem_audit_record
-- ----------------------------
DROP TABLE IF EXISTS `risk_problem_audit_record`;
CREATE TABLE `risk_problem_audit_record`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `audit_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '审核时间',
  `problem_id` int(10) NULL DEFAULT NULL COMMENT '风控问题ID',
  `audit_status` int(1) NULL DEFAULT NULL COMMENT '审核状态  1：通过  2：不通过',
  `audit_opinion` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '审核意见',
  `audit_person` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '审核人员',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 60 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '风控问题管理审核记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for risk_roll
-- ----------------------------
DROP TABLE IF EXISTS `risk_roll`;
CREATE TABLE `risk_roll`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `roll_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '编号',
  `roll_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名单名称',
  `roll_type` int(2) NOT NULL COMMENT '名单类型1：商户编号 2：身份证号 3：银行卡号 4：钱包T0出账 5：商户号白名单 6：实名认证白名单 7:闪付交易',
  `roll_status` int(1) NULL DEFAULT 1 COMMENT '名单状态 1：开启 0：关闭',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_person` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `roll_belong` int(2) NOT NULL COMMENT '名单所属：1：白名单  2：黑名单',
  `remark` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UNIQUE1`(`roll_no`, `roll_type`, `roll_belong`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20080 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '名单主表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for risk_roll_list
-- ----------------------------
DROP TABLE IF EXISTS `risk_roll_list`;
CREATE TABLE `risk_roll_list`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `roll_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '名单编号',
  `roll_number` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '名单号码',
  `create_person` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '名单集合表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for risk_rules
-- ----------------------------
DROP TABLE IF EXISTS `risk_rules`;
CREATE TABLE `risk_rules`  (
  `rules_no` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '规则编号',
  `rules_engine` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '规则引擎',
  `rules_values` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '规则值',
  `rules_instruction` int(10) NULL DEFAULT NULL COMMENT '规则指令 1商户黑名单,2身份证黑名单,3银行卡黑名单,4钱包出账黑名单,5实名认证,6风险预警,7银行卡号风险预警,8商户进行控制',
  `efficient_node_no` int(10) NULL DEFAULT NULL COMMENT '生效节点 1：交易  2：审件 3：提现  4：实名认证',
  `black_roll_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '黑名单编号',
  `white_roll_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '白名单编号',
  `status` int(1) NULL DEFAULT 1 COMMENT '状态：1打开  0关闭',
  `treatment_measures` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '处理措施',
  `warning_no` int(10) NULL DEFAULT NULL COMMENT '预警方式 1:电话 2：短信 3:邮件',
  `warning_info` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '预警电话/邮件',
  `remark` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `create_person` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `mer_white_roll` int(2) NULL DEFAULT 0 COMMENT '商户号白名单类型 0：规则对该类型白名单不生效  1：生效',
  `real_name_white_roll` int(2) NULL DEFAULT 0 COMMENT '实名认证白名单类型 0：规则对该类型白名单不生效  1：生效',
  `wallet_white_roll` int(2) NULL DEFAULT 0 COMMENT '钱包出账白名单类型 0：规则对该类型白名单不生效  1：生效',
  `rules_provinces` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '省份:如果有多个，用逗号隔开',
  `rules_city` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '市区:如果有多个,用逗号隔开',
  `rules_team_ids` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '适用范围组织id,用逗号隔开',
  `rules_merchant_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '适用商户类型  0-全部,1-小微商户，2-个体工商户，3-企业商户',
  PRIMARY KEY (`rules_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 127 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '风控规则表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for risk_rules_log
-- ----------------------------
DROP TABLE IF EXISTS `risk_rules_log`;
CREATE TABLE `risk_rules_log`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `rules_no` int(10) NULL DEFAULT NULL COMMENT '规则编号',
  `content` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改的内容',
  `update_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `update_person` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 385 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '风控规则修改日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for risk_trans_limit
-- ----------------------------
DROP TABLE IF EXISTS `risk_trans_limit`;
CREATE TABLE `risk_trans_limit`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `merchant_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户号',
  `order_no` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '交易订单号',
  `type` int(1) NOT NULL COMMENT '限制类型：1、银行卡号，2、商户号，3、其他',
  `limit_number` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '限制号码',
  `status` int(1) NULL DEFAULT 0 COMMENT '限制状态: 0初始化,1开启,2关闭,3失效',
  `roll_no` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '风控规则编号',
  `agent_no` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '代理商编号',
  `trigger_number` int(3) NULL DEFAULT 0 COMMENT '当天累计触发次数',
  `result_no` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '失败返回码',
  `invalid_time` timestamp(0) NULL DEFAULT NULL COMMENT '限制失效时间',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_person` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作人',
  `operation_time` timestamp(0) NULL DEFAULT NULL COMMENT '操作时间',
  `remark` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `hour_time` int(4) NULL DEFAULT 0 COMMENT '限制时间（小时）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `select_index`(`merchant_no`, `limit_number`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 60 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '风控交易限制记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for scan_code_trans
-- ----------------------------
DROP TABLE IF EXISTS `scan_code_trans`;
CREATE TABLE `scan_code_trans`  (
  `trade_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键 订单号',
  `trade_type` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易类型(alipay:支付宝；weixin:微信)',
  `auth_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '授权码、二维码信息（生成二维码支付此列为空）',
  `result_code` int(11) NULL DEFAULT NULL COMMENT '请求通信标识,是否创建此订单(0表示成功,非0表示失败)',
  `trade_state` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易状态（INIT—初始化;SUCCESS—支付成功;REFUND—转入退款;NOTPAY—未支付;CLOSED—已关闭;REVERSE—已冲正;REVOK—已撤销;FAILED:交易失败）',
  `merchant_no` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号，外键商户表',
  `acq_enname` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构',
  `acq_trans_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构返回的订单ID',
  `acq_merchant_no` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构商户编号',
  `total_fee` decimal(30, 2) NULL DEFAULT NULL COMMENT '支付金额',
  `merchant_fee` decimal(10, 2) NULL DEFAULT NULL COMMENT '商户手续费',
  `acq_fee` decimal(10, 2) NULL DEFAULT NULL COMMENT '收单机构手续费',
  `refund_fee` decimal(30, 2) NULL DEFAULT NULL COMMENT '退款金额（交易状态为退款是有值）',
  `refund_channel` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '退款渠道（交易状态为退款是有值）ORIGINAL-原路退款，BALANCE-余额，默认ORIGINAL',
  `mch_create_ip` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户端IP',
  `nonce_str` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '随机字符串，不长于 32 位',
  `message` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '返回信息',
  `code_url` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '二维码链接',
  `code_img_url` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '二维码图片',
  `openid` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户支付宝的账户名，或者微信支付银行订单号',
  `client_type` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机系统类型（android/ios）',
  `jpush_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '推送Id',
  `isT0` int(4) NULL DEFAULT 1 COMMENT '结算方式 0 t0 ,1 t1',
  `time_start` timestamp(0) NULL DEFAULT NULL COMMENT '订单生成时间',
  `time_expire` timestamp(0) NULL DEFAULT NULL COMMENT '订单失效时间',
  `time_end` timestamp(0) NULL DEFAULT NULL COMMENT '支付完成时间',
  `time_last` timestamp(0) NULL DEFAULT NULL COMMENT '最后操作时间(结算时间)',
  `settle_status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '结算状态',
  `cashier` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收款对象',
  `acq_reference_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构参考号',
  PRIMARY KEY (`trade_no`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '扫码支付表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for secret_key
-- ----------------------------
DROP TABLE IF EXISTS `secret_key`;
CREATE TABLE `secret_key`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `device_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `key_type` varchar(22) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `key_content` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `device_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `check_value` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` timestamp(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `device_id`(`device_id`, `key_content`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 241767448 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for secret_ts_key
-- ----------------------------
DROP TABLE IF EXISTS `secret_ts_key`;
CREATE TABLE `secret_ts_key`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `factory_code` varchar(225) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '厂家名',
  `key` varchar(225) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `cv` varchar(225) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `sn` varchar(225) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备前两位',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '厂家密钥表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sensitive_words
-- ----------------------------
DROP TABLE IF EXISTS `sensitive_words`;
CREATE TABLE `sensitive_words`  (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `sensitive_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '编码',
  `key_word` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '关键字',
  `status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '启用状态:1-启用,2-禁用,3-删除',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` timestamp(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 180 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '敏感词列表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sequence
-- ----------------------------
DROP TABLE IF EXISTS `sequence`;
CREATE TABLE `sequence`  (
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `current_value` bigint(22) NOT NULL,
  `increment` int(11) NOT NULL DEFAULT 1,
  PRIMARY KEY (`name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '序列表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for service_info
-- ----------------------------
DROP TABLE IF EXISTS `service_info`;
CREATE TABLE `service_info`  (
  `service_id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '服务ID',
  `service_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '服务名称',
  `agent_show_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商展示名称',
  `service_type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '服务类型',
  `hardware_is` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否与硬件相关:1-是，0-否',
  `bank_card` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '可用银行卡集合:1-信用卡，2-银行卡，0-不限',
  `exclusive` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '可否单独申请:1-可，0-否',
  `business` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务归属',
  `sale_starttime` date NULL DEFAULT NULL COMMENT '可销售起始日期',
  `sale_endtime` date NULL DEFAULT NULL COMMENT '可销售终止日期',
  `use_starttime` date NULL DEFAULT NULL COMMENT '可使用起始日期',
  `use_endtime` date NULL DEFAULT NULL COMMENT '可使用终止日期',
  `proxy` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '可否代理:1-可，0-否',
  `getcash_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '提现服务ID',
  `rate_card` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '费率是否区分银行卡种类:1-是，0-否',
  `rate_holidays` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '费率是否区分节假日:1-是，0-否',
  `quota_holidays` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '限额是否区分节假日:1-是，0-否',
  `quota_card` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '限额是否区分银行卡种类:1-是，0-否',
  `oem_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'OEM ID',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `t_flag` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'T0T1标志：0-不涉及，1-T0，2-T1, 3-T0和T1',
  `cash_subject` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '仅服务类型为账户提现，存储科目，账号',
  `fixed_rate` int(10) NULL DEFAULT 0 COMMENT '费率固定标志:1-固定，0-不固定',
  `fixed_quota` int(11) NULL DEFAULT 0 COMMENT '额度固定标志:1-固定，0-不固定',
  `service_status` int(11) NULL DEFAULT 1 COMMENT '服务状态：1开启，0关闭',
  `trad_start` time(0) NOT NULL COMMENT '交易开始时间',
  `trad_end` time(0) NOT NULL COMMENT '交易截至时间',
  `link_service` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '关联提现服务',
  `t0_turn_t1` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '不在T0提现时间范围时允许T0转T1交易标志',
  `effective_status` tinyint(2) NULL DEFAULT 1 COMMENT '生效状态,0:失效,1:生效,默认为1',
  `create_person` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`service_id`) USING BTREE,
  UNIQUE INDEX `service_name_index`(`service_name`) USING BTREE,
  INDEX `statuts_index`(`service_status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1362 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '服务基本信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for service_ladder_rate
-- ----------------------------
DROP TABLE IF EXISTS `service_ladder_rate`;
CREATE TABLE `service_ladder_rate`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `service_id` bigint(20) NOT NULL COMMENT '服务ID',
  `card_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银行卡种类:0-不限，1-只信用卡，2-只储蓄卡',
  `holidays_mark` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '节假日标志:1-只工作日，2-只节假日，0-不限',
  `quota_level` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '限额等级:0-全局、1-某个代理商下的商户',
  `agent_id` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商ID',
  `trans_amount_floor` decimal(20, 4) NULL DEFAULT NULL COMMENT '交易额下限',
  `trans_amount_ceiling` decimal(20, 4) NULL DEFAULT NULL COMMENT '交易额上限',
  `rate` decimal(20, 6) NULL DEFAULT NULL COMMENT '扣率',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `cb_id_UNIQUE`(`service_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '1.	服务管控阶梯费率表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for service_manage_quota
-- ----------------------------
DROP TABLE IF EXISTS `service_manage_quota`;
CREATE TABLE `service_manage_quota`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `service_id` bigint(20) NOT NULL COMMENT '服务ID',
  `holidays_mark` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '节假日标志:1-只工作日，2-只节假日，0-不限',
  `card_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银行卡种类:0-不限，1-只信用卡，2-只储蓄卡',
  `quota_level` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '限额等级:0-全局、1-某个代理商下的商户',
  `agent_no` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商编号',
  `single_day_amount` decimal(45, 0) NULL DEFAULT NULL COMMENT '单日最大交易额',
  `single_min_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '单笔最小交易额',
  `single_count_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '单笔最大交易额',
  `single_daycard_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '单日单卡最大交易额',
  `single_daycard_count` int(10) NULL DEFAULT 0 COMMENT '单日单卡最大交易笔数',
  `check_status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '审核状态：1审核；0未审核',
  `lock_status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '锁定状态：1锁定；0未锁定',
  `is_global` int(11) NULL DEFAULT 0 COMMENT '代理商额度与全局相同：1相同，0不相同',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `service_id_index`(`service_id`) USING BTREE,
  INDEX `agent_id_index`(`agent_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 79139 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '服务管控限额' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for service_manage_rate
-- ----------------------------
DROP TABLE IF EXISTS `service_manage_rate`;
CREATE TABLE `service_manage_rate`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `service_id` bigint(20) NOT NULL COMMENT '服务ID',
  `holidays_mark` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '节假日标志:1-只工作日，2-只节假日，0-不限',
  `card_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银行卡种类:0-不限，1-只信用卡，2-只储蓄卡',
  `quota_level` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '限额等级:0-全局、1-某个代理商下的商户',
  `agent_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商编号',
  `rate_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '费率类型:1-每笔固定金额，2-扣率，3-扣率带保底封顶，4-扣率+固定金额,5-单笔阶梯 扣率',
  `single_num_amount` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '每笔固定值',
  `rate` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '扣率',
  `capping` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '封顶',
  `safe_line` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '保底',
  `check_status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '审核状态：0未锁定，1 已审核',
  `lock_status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '锁定状态：0未锁定；1锁定',
  `ladder1_rate` decimal(10, 2) NULL DEFAULT NULL COMMENT '阶梯区间1费率',
  `ladder1_max` decimal(20, 2) NULL DEFAULT NULL COMMENT '阶梯区间1上限',
  `ladder2_rate` decimal(10, 2) NULL DEFAULT NULL COMMENT '阶梯区间2费率',
  `ladder2_max` decimal(20, 2) NULL DEFAULT NULL COMMENT '阶梯区间2上限',
  `ladder3_rate` decimal(10, 2) NULL DEFAULT NULL COMMENT '阶梯区间3费率',
  `ladder3_max` decimal(20, 2) NULL DEFAULT NULL COMMENT '阶梯区间3上限',
  `ladder4_rate` decimal(10, 2) NULL DEFAULT NULL COMMENT '阶梯区间4费率',
  `ladder4_max` decimal(20, 2) NULL DEFAULT NULL COMMENT '阶梯区间4上限',
  `is_global` int(11) NULL DEFAULT 0 COMMENT '代理商费率与全局相同：1相同，0不相同',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `service_id_index`(`service_id`, `agent_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 83924 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '服务管控费率' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for service_manage_rate_bak_20180117
-- ----------------------------
DROP TABLE IF EXISTS `service_manage_rate_bak_20180117`;
CREATE TABLE `service_manage_rate_bak_20180117`  (
  `id` bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'id',
  `service_id` bigint(20) NOT NULL COMMENT '服务ID',
  `holidays_mark` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '节假日标志:1-只工作日，2-只节假日，0-不限',
  `card_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银行卡种类:0-不限，1-只信用卡，2-只储蓄卡',
  `quota_level` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '限额等级:0-全局、1-某个代理商下的商户',
  `agent_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商编号',
  `rate_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '费率类型:1-每笔固定金额，2-扣率，3-扣率带保底封顶，4-扣率+固定金额,5-单笔阶梯 扣率',
  `single_num_amount` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '每笔固定值',
  `rate` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '扣率',
  `capping` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '封顶',
  `safe_line` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '保底',
  `check_status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '审核状态：0未锁定，1 已审核',
  `lock_status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '锁定状态：0未锁定；1锁定',
  `ladder1_rate` decimal(10, 2) NULL DEFAULT NULL COMMENT '阶梯区间1费率',
  `ladder1_max` decimal(20, 2) NULL DEFAULT NULL COMMENT '阶梯区间1上限',
  `ladder2_rate` decimal(10, 2) NULL DEFAULT NULL COMMENT '阶梯区间2费率',
  `ladder2_max` decimal(20, 2) NULL DEFAULT NULL COMMENT '阶梯区间2上限',
  `ladder3_rate` decimal(10, 2) NULL DEFAULT NULL COMMENT '阶梯区间3费率',
  `ladder3_max` decimal(20, 2) NULL DEFAULT NULL COMMENT '阶梯区间3上限',
  `ladder4_rate` decimal(10, 2) NULL DEFAULT NULL COMMENT '阶梯区间4费率',
  `ladder4_max` decimal(20, 2) NULL DEFAULT NULL COMMENT '阶梯区间4上限',
  `is_global` int(11) NULL DEFAULT 0 COMMENT '代理商费率与全局相同：1相同，0不相同'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for settle_order_detail
-- ----------------------------
DROP TABLE IF EXISTS `settle_order_detail`;
CREATE TABLE `settle_order_detail`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '子出账单明细编号',
  `order_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单号',
  `source_order_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '来源订单号',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `order_no`(`order_no`) USING BTREE,
  INDEX `source_order_no`(`source_order_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 922 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '出款订单明细表-针对手工提现' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for settle_order_info
-- ----------------------------
DROP TABLE IF EXISTS `settle_order_info`;
CREATE TABLE `settle_order_info`  (
  `settle_order` int(11) NOT NULL AUTO_INCREMENT COMMENT '出款流水号',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `settle_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '来源类型：1T0交易；2手工提现；3T1线上代付；4T1线下代付;5通用代付;6Tn线下',
  `source_order_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '来源订单号；',
  `source_batch_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '来源批次编号，来源系统自定义',
  `source_system` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '来源系统(core:交易系统,account:账户系统,boss:运营系统,agentapp:代理商app,car:车管家)',
  `create_user` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `settle_user_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '结算用户类型：M商户；A代理商；U一般用户',
  `settle_user_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '结算用户标识，商户编号，代理商编号',
  `settle_status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '结算状态 0或空：未结算  1：已结算   2.结算中   3.结算失败 ',
  `settle_msg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '结算备注，错误信息',
  `syn_status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '\'出款同步状态 1.未提交 2.已提交\',',
  `settle_order_status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ' 0 未确认 ；  1 已确认； 2；确认失败',
  `settle_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '出款提交金额',
  `acq_enname` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '出款通道',
  `agent_node` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商节点',
  `holidays_mark` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '节假日标志:1-只工作日，2-只节假日，0-不限',
  `sub_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '出款子类型(1:手刷,2:实体商户,3:活动补贴出款,4:欢乐送代理商,5:代理商分润,6:超级推分润,7:车管家代付,8:云闪付商户,9:超级还代理商,10:超级银行家,11:超级银行家代理商,12:超级兑)',
  `settle_bank_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '结算卡银行',
  `settle_account_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '结算卡号',
  `settle_account_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银行卡户名',
  `ser_service_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上游商户号',
  `settle_service_id` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '提现服务id',
  `user_code` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '来源系统，用户id 在账户系统开户',
  `agent_no` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '来源系统用户所属代理',
  `one_agent_no` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '来源代理商一级代理',
  `per_agent_fee` decimal(10, 2) NULL DEFAULT NULL COMMENT '人人代用税费',
  PRIMARY KEY (`settle_order`) USING BTREE,
  INDEX `settle_user_no`(`settle_user_no`) USING BTREE,
  INDEX `create_time`(`create_time`) USING BTREE,
  INDEX `source_order_node`(`source_order_no`) USING BTREE,
  INDEX `idx_agent_node`(`agent_node`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2171095 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '出款订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for settle_status_change_log
-- ----------------------------
DROP TABLE IF EXISTS `settle_status_change_log`;
CREATE TABLE `settle_status_change_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单号',
  `old_settle_status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '变更前的状态 0:未结算 1:已结算 2:结算中 3:结算失败 4:转T1结算 5:不结算',
  `curr_settle_status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '变更后的状态 0:未结算 1:已结算 2:结算中 3:结算失败 4:转T1结算 5:不结算',
  `operate_person` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作人',
  `operate_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '操作时间',
  `order_origin` int(2) NULL DEFAULT NULL COMMENT '订单来源 0：collective_trans_order 1：settle_transfer',
  `change_settle_status_reason` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '变更原因',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `ind_order_id`(`order_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 206 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for settle_t1_orig
-- ----------------------------
DROP TABLE IF EXISTS `settle_t1_orig`;
CREATE TABLE `settle_t1_orig`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `source_batch_no` int(11) NOT NULL COMMENT '来源批次号',
  `source_order_no` int(11) NOT NULL COMMENT '来源订单号',
  `origOrderNo` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '原订单',
  `origOrderDate` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '原订单交易日期   格式 ：yyyyMMdd',
  `create_time` timestamp(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `ind_creat_time`(`create_time`) USING BTREE,
  INDEX `ind_settle_order`(`source_batch_no`, `source_order_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '结算T1原订单信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for settle_transfer
-- ----------------------------
DROP TABLE IF EXISTS `settle_transfer`;
CREATE TABLE `settle_transfer`  (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '出款明细ID',
  `trans_id` int(11) NOT NULL COMMENT '原始交易记录ID',
  `order_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易订单号',
  `account_serial_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '记账流水',
  `settle_bank` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '结算机构',
  `in_acc_no` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '入账卡号',
  `in_acc_name` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '入账开户名',
  `in_settle_bank_no` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '结算行号',
  `in_bank_no` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '结算联行号',
  `in_bank_name` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '结算银行名称',
  `out_acc_no` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '出账银行卡卡号',
  `amount` decimal(20, 2) NOT NULL COMMENT '原始交易金额减去交易手续费后(元)',
  `out_amount` decimal(20, 2) NOT NULL COMMENT '实际出款金额',
  `fee_amount` decimal(20, 2) NOT NULL COMMENT '出款手续费',
  `proOrCost` int(2) NOT NULL COMMENT '1.代付 2.垫资 3.T1结算',
  `rate_type` int(2) NOT NULL COMMENT '出款费率类型',
  `out_service_id` int(20) NOT NULL COMMENT '出款服务ID',
  `out_service_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '出款服务类型 1：单笔代付-自有资金 2：单笔代付-垫资  3：批量代付',
  `out_sar_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代付出款服务费率类型 outServiceAgentRateType',
  `service_rate_id` int(20) NOT NULL COMMENT '提现服务费率ID',
  `out_rate` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '出款费率',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '状态  0.未提交 1.已提交 2.提交失败 3.超时 4.交易成功 5.交易失败 6.未知',
  `err_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '错误码',
  `syn_account_status` int(1) NOT NULL DEFAULT 1 COMMENT '1.未记账 2.已记账 3.记账失败',
  `correction` int(1) NOT NULL DEFAULT 0 COMMENT '冲正 0.未冲正 1.已冲正',
  `err_msg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '错误信息',
  `out_scr_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '垫资出款服务费率类型 outServiceConstRateType',
  `settle_type` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '来源类型：1T0交易；2手工提现；3T1线上代付；4T1线下代付;5通用代付;6Tn线下',
  `settle_user_type` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '\'结算用户类型：M商户；A代理商\'',
  `settle_user_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '\'结算用户标识，商户编号，代理商编号\',',
  `profits_1` decimal(30, 2) NULL DEFAULT NULL,
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
  `profits_20` decimal(30, 2) NULL DEFAULT NULL,
  `settle_creator` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '结算人',
  `sub_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '提现类型(1:手刷,2:实体商户,3:活动补贴出款,4:欢乐送代理商,5:代理商分润,6:超级推分润,7:车管家代付,8:云闪付商户,9:超级还代理商,10:超级银行家,11:超级银行家代理商,12:超级兑)',
  `agent_rate` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代付费率格式',
  `agent_fee` decimal(20, 2) NULL DEFAULT NULL COMMENT '代付手续费',
  `cost_rate` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '垫资费率格式',
  `cost_fee` decimal(20, 2) NULL DEFAULT NULL COMMENT '垫资手续费',
  `deduction_fee` decimal(20, 2) NULL DEFAULT NULL COMMENT '抵扣手续费',
  `actual_fee` decimal(20, 2) NULL DEFAULT NULL COMMENT '实际手续费',
  `zq_merchant_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '直清商户',
  `source_operation` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '源操作状态：0待操作 1已操作',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `ind_create_time`(`create_time`) USING BTREE,
  INDEX `ind_order_no`(`order_no`) USING BTREE,
  INDEX `ind_account_serial_no`(`account_serial_no`) USING BTREE,
  INDEX `trans_id`(`trans_id`) USING BTREE,
  INDEX `settle_user_no`(`settle_user_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4577671 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '出款明细' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for settle_transfer_extra
-- ----------------------------
DROP TABLE IF EXISTS `settle_transfer_extra`;
CREATE TABLE `settle_transfer_extra`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_serial_no` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '出款明细表记账流水',
  `in_id_card_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '入账者身份证id',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `bak1` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `bak2` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `settle_id_uni`(`account_serial_no`) USING BTREE,
  INDEX `id_no_ind`(`in_id_card_no`) USING BTREE,
  INDEX `create_time_ind`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '出款明细拓展表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for settle_update_copy
-- ----------------------------
DROP TABLE IF EXISTS `settle_update_copy`;
CREATE TABLE `settle_update_copy`  (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `source_id` int(20) NOT NULL COMMENT '原表id',
  `source_settle_status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '原表结算状态',
  `source_account_status` int(1) NULL DEFAULT NULL COMMENT '原表记账状态',
  `source_correction` int(1) NULL DEFAULT NULL COMMENT '原表冲正状态',
  `table_name` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '表名',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '结算更新备份(拆分用)' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sft_merchant_import
-- ----------------------------
DROP TABLE IF EXISTS `sft_merchant_import`;
CREATE TABLE `sft_merchant_import`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `merchant_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户名称',
  `address` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '详细地址',
  `lawyer` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '法人姓名',
  `merchant_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户编号,自己公司的',
  `terminal_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '逻辑终端号',
  `mbp_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户进件编号,盛付通的',
  `mpos_merchant_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'POS商户号,盛付通的',
  `id_card_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '身份证号',
  `id_front_Img` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '身份证正面照',
  `id_back_Img` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '身份证背面照',
  `id_hand_Img` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手持身份证照片',
  `mobilephone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `account_no` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '卡号',
  `bank_code` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '支行联行行号',
  `card_city` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '提现银行卡市名称',
  `register_city` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '注册市名称',
  `wx_fee` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '微信费率档,格式:SG01,SG02(旧费率档，新费率档)',
  `zfb_fee` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '支付宝费率档,格式:SG01,SG02(旧费率档，新费率档)',
  `bank_card_fee` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银行卡费率档,格式:SG01,SG02(旧费率档，新费率档)',
  `sync_status` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '同步状态 0：初始化，1：同步成功，2：同步失败，3审核中',
  `fee_status` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '费率修改状态 1：修改成功',
  `error_msg` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '失败原因',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '同步时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `mobilephone`(`mobilephone`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sft_rate
-- ----------------------------
DROP TABLE IF EXISTS `sft_rate`;
CREATE TABLE `sft_rate`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `sft_fee` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '盛付通费率档',
  `merchant_fee` decimal(10, 2) NOT NULL COMMENT '对应商户费率',
  `proposal_fee` decimal(10, 2) NULL DEFAULT NULL COMMENT '提现费',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 185 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '盛付通费率档' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for share_info
-- ----------------------------
DROP TABLE IF EXISTS `share_info`;
CREATE TABLE `share_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `appNo` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'APP编号',
  `merchant_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户编号',
  `shareWechatTitle` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '发给朋友显示的标题',
  `shareContent` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '发给朋友显示的内容',
  `shareFriendTitle` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '朋友圈里面的标题',
  `status` int(11) NOT NULL COMMENT '0:删除,1:正常',
  `create_time` datetime(0) NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for share_settle
-- ----------------------------
DROP TABLE IF EXISTS `share_settle`;
CREATE TABLE `share_settle`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `share_settle_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '分润代付结算单号',
  `order_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '来源单号',
  `merchant_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号',
  `type` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '来源类型：1交易 2出款',
  `acq_enname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '收单机构',
  `zq_merchant_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单商户号',
  `amount` decimal(30, 2) NULL DEFAULT NULL COMMENT '金额',
  `status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态（0成功 1失败 2待处理  3处理中 4需要重出）',
  `start_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `end_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '完成时间',
  `err_code` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '错误代码',
  `err_msg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '错误消息',
  `trans_id` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '渠道流水',
  `opertor` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'system' COMMENT '操作人',
  `terminal_no` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '终端号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `order_no`(`order_no`, `merchant_no`, `type`, `start_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 34564 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '分润代付结算表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sign_in
-- ----------------------------
DROP TABLE IF EXISTS `sign_in`;
CREATE TABLE `sign_in`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `mer_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号',
  `date_time` datetime(0) NULL DEFAULT NULL COMMENT '签到时间',
  `numbers` int(11) NULL DEFAULT NULL COMMENT '当日签到次数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 881 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '签到表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sms_validate
-- ----------------------------
DROP TABLE IF EXISTS `sms_validate`;
CREATE TABLE `sms_validate`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `user_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `order_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `sms_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for static_user_info
-- ----------------------------
DROP TABLE IF EXISTS `static_user_info`;
CREATE TABLE `static_user_info`  (
  `static_user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '统计用户ID',
  `mobilephone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `enable_flag` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '1有效；0失效',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `department` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`static_user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sub_order_info
-- ----------------------------
DROP TABLE IF EXISTS `sub_order_info`;
CREATE TABLE `sub_order_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '我们生成的订单号，关联collective_trans_order',
  `merchant_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户号',
  `bill_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '子商户订单',
  `bill_date` datetime(0) NULL DEFAULT NULL COMMENT '子商户交易时间',
  `payment_amout` decimal(30, 2) NULL DEFAULT NULL COMMENT '金额',
  `pay_method` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易方式 1 POS，2 支付宝，3 微信，4 快捷,21：支付宝收款码，22：支付宝主扫，31：微信收款码，32：微信主扫',
  `trans_status` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'INIT' COMMENT '订单状态：SUCCESS：成功,FAILED：失败,INIT：初始化,PROCESS:处理中',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT NULL COMMENT '更新时间（交易成功、失败等）',
  `notity_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '回调路径',
  `notice_times` int(11) NULL DEFAULT 0 COMMENT '通知次数',
  `auth_code` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主扫授权码/收款码',
  `m_code` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密钥类型',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1028 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for subscribe_vip
-- ----------------------------
DROP TABLE IF EXISTS `subscribe_vip`;
CREATE TABLE `subscribe_vip`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '会员订单号',
  `payment_order_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '支付订单号',
  `user_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订阅会员的用户ID,为商户号,',
  `vip_type` int(11) NULL DEFAULT NULL COMMENT '订阅的会员类型,1个月,3个月,6个月等',
  `subscribe_status` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订阅状态:SUCCESS：成功,FAILED：失败,INIT：初始化',
  `subscribe_remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订阅状态描述',
  `validity_start` datetime(0) NULL DEFAULT NULL COMMENT '会员生效时间',
  `validity_end` datetime(0) NULL DEFAULT NULL COMMENT '会员截止时间',
  `validity_days` int(10) NULL DEFAULT NULL COMMENT '有效期天数',
  `amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '付款金额',
  `payment_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '付款方式:alipay 支付宝,wechat 微信,bycard 刷卡,demo 休验',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `trans_time` datetime(0) NULL DEFAULT NULL COMMENT '交易完成时间',
  `last_update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_subscribe_vip`(`order_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 624 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户订阅会员记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for super_collection_switch
-- ----------------------------
DROP TABLE IF EXISTS `super_collection_switch`;
CREATE TABLE `super_collection_switch`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `number` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '编码',
  `start_time` time(0) NULL DEFAULT NULL COMMENT '交易开始时间',
  `end_time` time(0) NULL DEFAULT NULL COMMENT '交易结束时间',
  `day_lines` decimal(30, 2) NULL DEFAULT NULL COMMENT '每日额度',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` timestamp(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '超级收款设置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for super_push_config
-- ----------------------------
DROP TABLE IF EXISTS `super_push_config`;
CREATE TABLE `super_push_config`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `bp_id` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务产品ID',
  `tx_service_id` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '超级推提现服务ID',
  `app_agent_no_list` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '自定义客户端代理商集合',
  `message_module` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '推送短信模板',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_date` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `operator` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作人',
  `incentive_fund_switch` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '关联鼓励金开关(0关闭1打开)',
  `reward_point_switch` int(11) NOT NULL DEFAULT 0 COMMENT '分享奖励开关(0关闭1打开)',
  `prizes_amount` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '奖励金额',
  `bonus_message_Module` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分享奖励推送短信模板',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `bp_id`(`bp_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '超级推配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for super_push_config_bak_170805
-- ----------------------------
DROP TABLE IF EXISTS `super_push_config_bak_170805`;
CREATE TABLE `super_push_config_bak_170805`  (
  `id` int(11) NOT NULL DEFAULT 0,
  `bp_id` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务产品ID',
  `tx_service_id` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '超级推提现服务ID',
  `app_agent_no_list` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '自定义客户端代理商集合',
  `message_module` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '推送短信模板',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_date` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `operator` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作人'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for super_push_share
-- ----------------------------
DROP TABLE IF EXISTS `super_push_share`;
CREATE TABLE `super_push_share`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单号',
  `trans_amount` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '交易金额',
  `trans_time` datetime(0) NULL DEFAULT NULL COMMENT '交易时间',
  `merchant_no` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '支付商户',
  `mobile` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易商户手机号',
  `agent_node` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易商户直属代理商node',
  `share_type` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '0: 一级代理商分润, 1: 直属代理商分润, 2 上一级商户 3 上二级商户 4 上三级商户',
  `share_no` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分润的商户/代理商编号',
  `share_amount` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '分润金额',
  `share_rate` decimal(10, 6) NULL DEFAULT 0.000000 COMMENT '分润费率',
  `share_status` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '分润入账状态,0 未入账, 1 入账, 2,入账失败',
  `share_time` datetime(0) NULL DEFAULT NULL COMMENT '分润入账时间',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '分润创建时间',
  `collection_status` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'NOCOLLECTION' COMMENT '汇总状态(NOCOLLECTION未汇总,COLLECTIONED已汇总)',
  `collection_batch_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分润汇总批次号',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `order_no_ind`(`order_no`) USING BTREE,
  INDEX `merchant_no_ind`(`merchant_no`) USING BTREE,
  INDEX `share_no_ind`(`share_no`) USING BTREE,
  INDEX `agent_node_ind`(`agent_node`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2006 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '超级推商户分润记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for super_push_share_bak_170805
-- ----------------------------
DROP TABLE IF EXISTS `super_push_share_bak_170805`;
CREATE TABLE `super_push_share_bak_170805`  (
  `id` int(11) NOT NULL DEFAULT 0 COMMENT '主键',
  `order_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单号',
  `amount` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '交易金额',
  `trans_time` datetime(0) NULL DEFAULT NULL COMMENT '交易时间',
  `merchant_no` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '支付商户',
  `mobile` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易商户手机号',
  `self_share` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '本级商户分润',
  `one_share` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '一级商户分润',
  `two_share` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '二级商户分润',
  `three_share` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '三级商户分润',
  `self_rule` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '本级分润公式',
  `one_rule` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '一级商户分润公式',
  `two_rule` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '二级商户分润公式',
  `three_rule` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '三级商户分润公式'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for super_push_share_copy
-- ----------------------------
DROP TABLE IF EXISTS `super_push_share_copy`;
CREATE TABLE `super_push_share_copy`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单号',
  `trans_amount` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '交易金额',
  `trans_time` datetime(0) NULL DEFAULT NULL COMMENT '交易时间',
  `merchant_no` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '支付商户',
  `mobile` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易商户手机号',
  `agent_node` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易商户直属代理商node',
  `share_type` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '0: 一级代理商分润, 1: 直属代理商分润, 2 上一级商户 3 上二级商户 4 上三级商户',
  `share_no` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分润的商户/代理商编号',
  `share_amount` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '分润金额',
  `share_rate` decimal(10, 6) NULL DEFAULT 0.000000 COMMENT '分润费率',
  `share_status` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '分润入账状态,0 未入账, 1 入账, 2,入账失败',
  `share_time` datetime(0) NULL DEFAULT NULL COMMENT '分润入账时间',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '分润创建时间',
  `collection_status` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'NOCOLLECTION' COMMENT '汇总状态(NOCOLLECTION未汇总,COLLECTIONED已汇总)',
  `collection_batch_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分润汇总批次号',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `order_no_ind`(`order_no`) USING BTREE,
  INDEX `merchant_no_ind`(`merchant_no`) USING BTREE,
  INDEX `share_no_ind`(`share_no`) USING BTREE,
  INDEX `agent_node_ind`(`agent_node`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1224 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '超级推商户分润记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for super_push_share_rule
-- ----------------------------
DROP TABLE IF EXISTS `super_push_share_rule`;
CREATE TABLE `super_push_share_rule`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `bp_id` bigint(20) NOT NULL COMMENT '业务产品id',
  `service_id` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '服务ID',
  `service_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '服务类型',
  `profit_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '本级分润方式:1-每笔固定收益额；2-每笔固定收益率',
  `per_fix_income` decimal(10, 2) NULL DEFAULT NULL COMMENT '本级每笔固定收益额',
  `per_fix_inrate` decimal(10, 6) NULL DEFAULT NULL COMMENT '本级每笔固定收益率',
  `profit_type1` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '一级分润方式:1-每笔固定收益额；2-每笔固定收益率',
  `per_fix_income1` decimal(10, 2) NULL DEFAULT NULL COMMENT '一级每笔固定收益额',
  `per_fix_inrate1` decimal(10, 6) NULL DEFAULT NULL COMMENT '一级每笔固定收益率',
  `profit_type2` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '二级分润方式:1-每笔固定收益额；2-每笔固定收益率',
  `per_fix_income2` decimal(10, 2) NULL DEFAULT NULL COMMENT '二级每笔固定收益额',
  `per_fix_inrate2` decimal(10, 6) NULL DEFAULT NULL COMMENT '二级每笔固定收益率',
  `profit_type3` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '三级分润方式:1-每笔固定收益额；2-每笔固定收益率',
  `per_fix_income3` decimal(10, 2) NULL DEFAULT NULL COMMENT '三级每笔固定收益额',
  `per_fix_inrate3` decimal(10, 6) NULL DEFAULT NULL COMMENT '三级每笔固定收益率',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_date` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `operator` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作人',
  `agent_profit_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '直属代理商分润方式:1-每笔固定收益额；2-每笔固定收益率',
  `agent_per_fix_income` decimal(10, 2) NULL DEFAULT NULL COMMENT '直属代理商每笔固定收益额',
  `agent_per_fix_inrate` decimal(10, 6) NULL DEFAULT NULL COMMENT '直属代理商每笔固定收益率',
  `one_agent_profit_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '一级代理商分润方式:1-每笔固定收益额；2-每笔固定收益率',
  `one_agent_per_fix_income` decimal(10, 2) NULL DEFAULT NULL COMMENT '一级代理商每笔固定收益额',
  `one_agent_per_fix_inrate` decimal(10, 6) NULL DEFAULT NULL COMMENT '一级代理商每笔固定收益率',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 44 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '超级推分润规则表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for super_push_share_rule_bak_170805
-- ----------------------------
DROP TABLE IF EXISTS `super_push_share_rule_bak_170805`;
CREATE TABLE `super_push_share_rule_bak_170805`  (
  `id` int(11) NOT NULL DEFAULT 0,
  `service_id` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '服务ID',
  `service_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '服务类型',
  `profit_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '本级分润方式:1-每笔固定收益额；2-每笔固定收益率',
  `per_fix_income` decimal(10, 2) NULL DEFAULT NULL COMMENT '本级每笔固定收益额',
  `per_fix_inrate` decimal(10, 6) NULL DEFAULT NULL COMMENT '本级每笔固定收益率',
  `profit_type1` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '一级分润方式:1-每笔固定收益额；2-每笔固定收益率',
  `per_fix_income1` decimal(10, 2) NULL DEFAULT NULL COMMENT '一级每笔固定收益额',
  `per_fix_inrate1` decimal(10, 6) NULL DEFAULT NULL COMMENT '一级每笔固定收益率',
  `profit_type2` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '二级分润方式:1-每笔固定收益额；2-每笔固定收益率',
  `per_fix_income2` decimal(10, 2) NULL DEFAULT NULL COMMENT '二级每笔固定收益额',
  `per_fix_inrate2` decimal(10, 6) NULL DEFAULT NULL COMMENT '二级每笔固定收益率',
  `profit_type3` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '三级分润方式:1-每笔固定收益额；2-每笔固定收益率',
  `per_fix_income3` decimal(10, 2) NULL DEFAULT NULL COMMENT '三级每笔固定收益额',
  `per_fix_inrate3` decimal(10, 6) NULL DEFAULT NULL COMMENT '三级每笔固定收益率',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_date` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `operator` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作人'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for super_push_user
-- ----------------------------
DROP TABLE IF EXISTS `super_push_user`;
CREATE TABLE `super_push_user`  (
  `user_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '商户用户ID',
  `merchant_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '本级商户编号',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `business_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '推荐商户的业务编号',
  `recommended_source` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '推荐来源：1是商户链接2是代理商链接',
  `recommended_user_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '推荐人ID',
  `one_merchant_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上一级商户编号',
  `two_merchant_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上二级商户编号',
  `three_merchant_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上三级商户编号',
  `agent_node` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '超级推商户直属代理商的node',
  PRIMARY KEY (`user_id`) USING BTREE,
  INDEX `agent_node_ind`(`agent_node`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '超级推用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for super_push_user_bak_170805
-- ----------------------------
DROP TABLE IF EXISTS `super_push_user_bak_170805`;
CREATE TABLE `super_push_user_bak_170805`  (
  `user_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '商户用户ID',
  `merchant_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '本级商户编号',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `business_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '推荐商户的业务编号',
  `recommended_source` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '推荐来源：1是商户链接2是代理商链接',
  `recommended_user_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '推荐人ID',
  `one_merchant_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上一级商户编号',
  `two_merchant_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上二级商户编号',
  `three_merchant_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上三级商户编号',
  `agent_node` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '超级推商户直属代理商的node'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for supertui_order
-- ----------------------------
DROP TABLE IF EXISTS `supertui_order`;
CREATE TABLE `supertui_order`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `order_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单ID',
  `merchant_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '进件商户ID',
  `bp_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务产品ID',
  `supertui_rule_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '超级推规则ID',
  `order_status` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单状态',
  `lawyer` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '被推荐人姓名',
  `mobilephone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `address` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地址',
  `remark` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `merchant_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '推荐商户编号',
  `recommended_amount` decimal(30, 2) NULL DEFAULT NULL COMMENT '推荐人奖励金额',
  `order_user_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接单人类型',
  `order_user_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接单用户ID',
  `order_agent_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接单代理商ID',
  `order_agent_fee` decimal(30, 2) NULL DEFAULT NULL COMMENT '代理商接单费用',
  `order_time` timestamp(0) NULL DEFAULT NULL COMMENT '接单时间',
  `order_disabled_time` timestamp(0) NULL DEFAULT NULL COMMENT '接单失效时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `order_id_UNIQUE`(`order_id`, `merchant_id`, `bp_id`, `supertui_rule_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '超级推订单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for supertui_rule
-- ----------------------------
DROP TABLE IF EXISTS `supertui_rule`;
CREATE TABLE `supertui_rule`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `bp_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务产品ID',
  `recommended_amount` decimal(30, 2) NULL DEFAULT NULL COMMENT '推荐人奖励金额',
  `reward_valid_node` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '奖励有效节点',
  `order_agent_fee` decimal(30, 2) NULL DEFAULT NULL COMMENT '代理商接单费用',
  `fee_valid_node` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '费用有效节点',
  `efficient_date` date NULL DEFAULT NULL COMMENT '生效日期',
  `disabled_date` date NULL DEFAULT NULL COMMENT '失效日期',
  `order_threshold` decimal(10, 1) NULL DEFAULT NULL COMMENT '待处理订单阈值',
  `grab_order_time` decimal(10, 1) NULL DEFAULT NULL COMMENT '抢单时间',
  `feedback_time` decimal(10, 1) NULL DEFAULT NULL COMMENT '反馈时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `supertui_rule_id_UNIQUE`(`bp_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 189 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '超级推规则' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for survey_order_deduct
-- ----------------------------
DROP TABLE IF EXISTS `survey_order_deduct`;
CREATE TABLE `survey_order_deduct`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '调单单号',
  `acq_deduct_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '上游扣款金额',
  `acq_deduct_time` timestamp(0) NULL DEFAULT NULL COMMENT '上游扣款时间',
  `acq_deduct_remark` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '上游扣款备注',
  `mer_deduct_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '商户扣款金额',
  `mer_deduct_time` timestamp(0) NULL DEFAULT NULL COMMENT '商户扣款时间',
  `mer_deduct_remark` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '商户扣款备注（风控扣款备注）',
  `agent_need_deduct_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '代理商需扣款金额',
  `agent_remain_deduct_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '代理商待扣款金额',
  `agent_have_deduct_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '代理商已扣款金额',
  `agent_deduct_time` timestamp(0) NULL DEFAULT NULL COMMENT '代理商扣款时间',
  `agent_deduct_remark` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '代理商扣款备注',
  `acq_issue_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '上游下发金额',
  `acq_issue_time` timestamp(0) NULL DEFAULT NULL COMMENT '上游下发时间',
  `acq_issue_remark` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '上游下发备注',
  `mer_issue_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '商户下发金额',
  `mer_issue_time` timestamp(0) NULL DEFAULT NULL COMMENT '商户下发时间',
  `mer_issue_remark` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '商户下发备注（风控下发备注）',
  `agent_need_issue_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '代理商需下发金额',
  `agent_remain_issue_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '代理商待下发金额',
  `agent_have_issue_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '代理商已下发金额',
  `agent_issue_time` timestamp(0) NULL DEFAULT NULL COMMENT '代理商下发时间',
  `agent_issue_remark` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '代理商下发备注',
  `operator` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作人',
  `operate_time` timestamp(0) NULL DEFAULT NULL COMMENT '操作时间',
  `operator_type` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作类型 0上游扣款，1上游下发，2商户扣款，3商户下发，4代理商扣款，5代理商下发',
  `last_update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '数据最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `order_ind`(`order_no`) USING BTREE,
  INDEX `operate_time_index`(`operate_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 73 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '调单扣款明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for survey_order_info
-- ----------------------------
DROP TABLE IF EXISTS `survey_order_info`;
CREATE TABLE `survey_order_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '调单单号',
  `merchant_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户编号',
  `trans_order_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易订单编号',
  `acq_reference_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构参考号',
  `trans_order_database` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '交易订单所在数据库 now当前库 old历史库',
  `order_service_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '业务类型编号，对应字典表数据',
  `order_type_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '调单类型编号，对应字典表数据',
  `reply_end_time` timestamp(0) NOT NULL COMMENT '回复截止时间',
  `template_files_name` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '模板附件，多个以,隔开',
  `order_remark` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '调单添加说明',
  `order_status` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '1' COMMENT '调单状态（针对调单） 0异常（已删除）  1正常',
  `reply_status` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '回复状态（针对代理商） 0未提交，1已提交，2已确认，3已逾期，4逾期提交，5逾期确认，6无需处理',
  `deal_status` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '调单处理状态(针对业务人员) 0未处理，1部分提供，2持卡人承认交易，3全部提供，4无法提供，5逾期部分提供，6逾期全部提供，7逾期未回，8已回退，9已处理完，无需代理商提交资料',
  `deal_remark` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '调单处理备注说明',
  `final_have_look_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '终态时浏览过的用户编号，代理商编号/商户编号，以英文逗号隔开',
  `agent_node` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '代理商编号节点',
  `agent_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属代理商编号',
  `one_agent_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '一级代理商编号',
  `trans_amount` decimal(10, 2) NOT NULL COMMENT '交易金额',
  `trans_account_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易卡号',
  `trans_time` timestamp(0) NULL DEFAULT NULL COMMENT '交易时间',
  `trans_status` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易状态:SUCCESS：成功,FAILED：失败,INIT：初始化,REVERSED：已冲正,REVOKED：已撤销,SETTLE：已结算,OVERLIMIT：已退款,REFUND：失败,COMPLETE：已完成,CLOSED：关闭,TRADING：交易中,UNKNOWN：未知',
  `acq_code` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构编号',
  `acq_merchant_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构商户编号',
  `pay_method` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易方式 1 POS，2 支付宝，3 微信，4 快捷',
  `urge_num` int(5) NOT NULL DEFAULT 0 COMMENT '催单次数',
  `have_add_deduct` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '是否添加了扣款  0没有添加 1已添加',
  `agent_deduct_deal_status` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '代理商扣款处理状态 0未处理 1已处理',
  `agent_deduct_deal_remark` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '代理商扣款处理备注',
  `agent_issue_deal_status` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '代理商下发处理状态 0未处理 1已处理',
  `agent_issue_deal_remark` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '代理商下发处理备注',
  `acq_reply_status` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '上游回复状态 0未回复 1已回复',
  `acq_reply_remark` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '上游回复备注',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creator` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `last_reply_time` timestamp(0) NULL DEFAULT NULL COMMENT '代理商最后回复时间',
  `last_update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '数据最后更新时间',
  `bak1` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `bak2` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `pa_user_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '超级盟主编码',
  `user_node` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '超级盟主用户节点',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `order_no_ind`(`order_no`) USING BTREE,
  INDEX `mer_ind`(`merchant_no`) USING BTREE,
  INDEX `agent_ind`(`agent_node`) USING BTREE,
  INDEX `create_ind`(`create_time`) USING BTREE,
  INDEX `trans_no_ind`(`trans_order_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 984 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '调单信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for survey_order_log
-- ----------------------------
DROP TABLE IF EXISTS `survey_order_log`;
CREATE TABLE `survey_order_log`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '调单编号',
  `operate_type` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '操作类型 0添加调单，1回退，2处理，3催单，4删除，5添加扣款，6标注上游扣款信息,7标注上游下发信息,8标注商户扣款信息,9标注商户下发信息,10标注代理商扣款信息,11标注代理商下发信息,12标注扣款处理状态,13标注下发处理状态',
  `operator` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '操作人',
  `operate_table` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作表',
  `pre_value` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '操作前的值',
  `after_value` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '操作后的值',
  `operate_detail` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '日志操作详细',
  `operate_time` timestamp(0) NOT NULL COMMENT '操作时间',
  `bak1` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `bak2` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `order_ind`(`order_no`) USING BTREE,
  INDEX `operator_ind`(`operate_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2254 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '调单日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for survey_reply_record
-- ----------------------------
DROP TABLE IF EXISTS `survey_reply_record`;
CREATE TABLE `survey_reply_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '调单单号',
  `agent_node` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '代理商编号节点',
  `reply_role_type` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '回复的角色类型 A：代理商  M:商户 P 盟主编号',
  `reply_role_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '回复的角色编号。商户编号/代理商编号/盟主编号',
  `reply_result` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '回复结果 0交易成功，提供凭证；1交易成功，无法提供凭证；2交易未成功，退持卡人；3交易成功，已协商，需退持卡人；4长款需结算给商户',
  `mer_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户名称',
  `mer_mobile` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户电话',
  `card_person_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '持卡人姓名',
  `card_person_mobile` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '持卡人电话',
  `real_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '真实商户名称',
  `province` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '归属省',
  `city` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '归属市',
  `trans_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '真实交易地址',
  `reply_files_name` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '回复附件名称',
  `reply_remark` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '回复说明',
  `last_deal_status` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最后一次调单处理状态(针对业务人员) 0未处理，1部分提供，2持卡人承认交易，3全部提供，4无法提供，5逾期部分提供，6逾期全部提供，7逾期未回，8已回退，9已处理完，无需代理商提交资料',
  `last_deal_remark` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '最后一次处理备注',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '数据最后更新时间',
  `bak1` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `bak2` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `order_ind`(`order_no`) USING BTREE,
  INDEX `role_no_ind`(`reply_role_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 939 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '调单回复信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for survey_urge_record
-- ----------------------------
DROP TABLE IF EXISTS `survey_urge_record`;
CREATE TABLE `survey_urge_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '调单单号',
  `agent_node` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '代理商编号节点',
  `status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '催单状态 0失效 1有效',
  `have_look_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '浏览过的用户编号，代理商编号/商户编号，以英文逗号隔开',
  `operator` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '操作人',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '数据最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `order_ind`(`order_no`) USING BTREE,
  INDEX `agent_ind`(`agent_node`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 223 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '调单催单记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_calendar
-- ----------------------------
DROP TABLE IF EXISTS `sys_calendar`;
CREATE TABLE `sys_calendar`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `event_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '事件名称',
  `year` int(11) NOT NULL COMMENT '年',
  `month` int(11) NOT NULL COMMENT '月',
  `day` int(11) NOT NULL COMMENT '日',
  `week` int(11) NOT NULL COMMENT '周',
  `sys_date` date NOT NULL COMMENT '系统日期',
  `type` int(11) NULL DEFAULT NULL COMMENT '类型：1,法定假日，2,周末',
  `status` int(11) NULL DEFAULT NULL COMMENT '状态:1,正常，0,失效',
  `creator` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `sys_date`(`sys_date`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2061 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `PARAM_KEY` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'key',
  `PARAM_VALUE` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '值',
  `REMARK` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '说明',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 204 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统配置参数表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_config1
-- ----------------------------
DROP TABLE IF EXISTS `sys_config1`;
CREATE TABLE `sys_config1`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `sys_key` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '键值类型',
  `sys_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `sys_value` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '值',
  `order_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` int(11) NULL DEFAULT 1,
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 116 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统字典表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_config_bak
-- ----------------------------
DROP TABLE IF EXISTS `sys_config_bak`;
CREATE TABLE `sys_config_bak`  (
  `id` int(11) NOT NULL DEFAULT 0,
  `PARAM_KEY` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'key',
  `PARAM_VALUE` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '值',
  `REMARK` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '说明'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sys_key` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标识字典类型',
  `sys_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `sys_value` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '值',
  `parent_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上级ID',
  `order_no` bigint(20) NULL DEFAULT NULL,
  `status` int(11) NULL DEFAULT 1,
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `sys_key_index`(`sys_key`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2180 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '字典表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_mcc
-- ----------------------------
DROP TABLE IF EXISTS `sys_mcc`;
CREATE TABLE `sys_mcc`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `mcc` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'mcc',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'mcc对应的名称',
  `channel_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '通道编码',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 51 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_msg_info
-- ----------------------------
DROP TABLE IF EXISTS `sys_msg_info`;
CREATE TABLE `sys_msg_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `msg_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '消息编码，全局唯一',
  `msg_type` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '消息类型：1错误信息  2警告',
  `module_name` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模块名；A交易内部处理； B交易上游返回 C出款内部处理 D出款上游返回 E进件 Z其他',
  `user_msg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '提示给用户的信息',
  `reason` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '错误原因',
  `solution` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '建议解决方案',
  `source_org` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '来源组织；收单机构',
  `source_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '来源错误编号；收单机构错误编号',
  `source_type` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `source_msg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构错误信息',
  `source_remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构错误备注',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后更新时间',
  `status` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '0失效；1生效,默认为1',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_msg_code`(`msg_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 272 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_warning
-- ----------------------------
DROP TABLE IF EXISTS `sys_warning`;
CREATE TABLE `sys_warning`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '预警默认内容',
  `cycle` int(11) NULL DEFAULT NULL COMMENT '时间周期(单位:分钟)',
  `num` int(11) NULL DEFAULT NULL COMMENT '预警次数',
  `cd_time` int(11) NULL DEFAULT NULL COMMENT '预警CD,单位分钟',
  `phones` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号码多个逗号隔开',
  `type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '预警类型,1:实名认证预警失败,2:活体认证预警,3:已达到目标金额预警,4:未达到目标金额预警,5:实名认证超时预警,6:企业对比预警',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统短信预警表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_test
-- ----------------------------
DROP TABLE IF EXISTS `t_test`;
CREATE TABLE `t_test`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tbl_mpos_city_map
-- ----------------------------
DROP TABLE IF EXISTS `tbl_mpos_city_map`;
CREATE TABLE `tbl_mpos_city_map`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `PROVINCE` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `GD_CITY` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `GD_AD_CODE` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `GD_CITY_CODE` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `UN_CITY` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `UN_DITRICT` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `UN_CODE` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '行政区码对照表（中付给的）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for team_ad
-- ----------------------------
DROP TABLE IF EXISTS `team_ad`;
CREATE TABLE `team_ad`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `team_id` bigint(20) NOT NULL COMMENT '组织ID',
  `team_ad_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '拓展代理商推广海报地址（多个逗号隔开）',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '组织海报表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for team_bp_info
-- ----------------------------
DROP TABLE IF EXISTS `team_bp_info`;
CREATE TABLE `team_bp_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `team_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组织ID',
  `bp_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务产品ID',
  `hp_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收款码ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `team_id_index`(`team_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '人人代理组织业务产品对应表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for team_info
-- ----------------------------
DROP TABLE IF EXISTS `team_info`;
CREATE TABLE `team_info`  (
  `team_id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '组织ID',
  `team_type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组织类型，1-直营，2-非直营',
  `team_name` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组织名称',
  `lawyer` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '法人名称',
  `bus_license_no` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '营业执照编码',
  `logo` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'LOGO',
  `pub_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公众号名称',
  `public_qrcode` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公众号二维码URL',
  `brand_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '1为银盛',
  `online_service_URL` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '在线客服URL',
  PRIMARY KEY (`team_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 900011 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '组织表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for team_info_entry
-- ----------------------------
DROP TABLE IF EXISTS `team_info_entry`;
CREATE TABLE `team_info_entry`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `team_id` bigint(20) NOT NULL COMMENT '组织ID',
  `team_entry_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '子组织id',
  `team_entry_name` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '子组织名称',
  `remark` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '数据最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `team_entry_id_uni`(`team_entry_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '子级组织表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for temp_1level_share
-- ----------------------------
DROP TABLE IF EXISTS `temp_1level_share`;
CREATE TABLE `temp_1level_share`  (
  `agent_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `mer_rate_pos` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `agent_share_pos` decimal(10, 6) NULL DEFAULT NULL,
  `agent_flow_pos` decimal(10, 2) NULL DEFAULT NULL,
  `mer_rate_wk` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `agent_share_wk` decimal(10, 6) NULL DEFAULT NULL,
  `agent_flow_wk` decimal(10, 2) NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for temp_v2_data_cutdown
-- ----------------------------
DROP TABLE IF EXISTS `temp_v2_data_cutdown`;
CREATE TABLE `temp_v2_data_cutdown`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `data_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `object_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `object_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `reamrk2` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `reamrk3` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 163 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ter_activity_check
-- ----------------------------
DROP TABLE IF EXISTS `ter_activity_check`;
CREATE TABLE `ter_activity_check`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `sn` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '机具sn号',
  `due_days` int(3) NULL DEFAULT NULL COMMENT '剩余天数',
  `status` tinyint(1) NULL DEFAULT 0 COMMENT '0-未激活,1-已激活',
  `update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `check_time` timestamp(0) NULL DEFAULT NULL COMMENT '考核时间（创建时间）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `sn`(`sn`) USING BTREE,
  INDEX `check_time`(`check_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 38 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '机具活动考核' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ter_no_log
-- ----------------------------
DROP TABLE IF EXISTS `ter_no_log`;
CREATE TABLE `ter_no_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sn` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `union_mer_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `trmNo` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `method` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `mbp_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `old_sn` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for terminal_apply
-- ----------------------------
DROP TABLE IF EXISTS `terminal_apply`;
CREATE TABLE `terminal_apply`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `merchant_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号',
  `SN` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '一级代理商下发的机具sn号',
  `status` int(1) NULL DEFAULT 0 COMMENT '状态 0:待直属处理  1:已处理  2:待一级处理',
  `product_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '机具类型',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后处理时间',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户地址',
  `mobilephone` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '最后处理时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 157 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '机具申请表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for terminal_info
-- ----------------------------
DROP TABLE IF EXISTS `terminal_info`;
CREATE TABLE `terminal_info`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `SN` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '机器SN号',
  `terminal_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '终端号',
  `merchant_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号',
  `PSAM_NO` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'psam编号',
  `agent_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商编号',
  `agent_node` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商节点',
  `open_status` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '开通状态1为已分配2为已使用',
  `type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '机具硬件种类',
  `allot_batch` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '机具出库分配给代理商的批次号',
  `model` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `tmk` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '传统pos主秘钥',
  `tmk_tpk` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主秘钥tmk加密的tpk密文',
  `tmk_tak` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主秘钥tmk加密的tak密文',
  `START_TIME` timestamp(0) NULL DEFAULT NULL COMMENT '启用时间',
  `CREATE_TIME` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `pos_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备类型2.传统POS 3移小宝 4移联商通 具体参照pos_type表',
  `need_check` tinyint(4) NULL DEFAULT 1 COMMENT '是否需要签到0不需要1需要',
  `last_check_in_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后签到时间',
  `cashier_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作员编号',
  `serial_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '流水号',
  `batch_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '批次号',
  `single_share_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '单笔分润金额',
  `bp_id` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务产品ID',
  `collection_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收款码号码',
  `activity_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '活动类型，对照数据字典',
  `recommended_source` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '1商户链接，2代理商链接',
  `terminal_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '机具名称',
  `channel` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '机具通道',
  `activity_type_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '活动子类型编号',
  `type_temp1` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备份硬件产品种类',
  `type_temp2` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '临时存储合并后的硬件产品种类',
  `receiptdate` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `downdate` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `terminal_id`(`terminal_id`) USING BTREE,
  UNIQUE INDEX `PSAM_NO`(`PSAM_NO`) USING BTREE,
  INDEX `sn_index`(`SN`) USING BTREE,
  INDEX `full_he_index`(`merchant_no`, `agent_no`) USING BTREE,
  INDEX `index_ter_agent_no`(`agent_no`) USING BTREE,
  INDEX `index_agent_no`(`agent_node`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 741390 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '机具信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for terminal_operate
-- ----------------------------
DROP TABLE IF EXISTS `terminal_operate`;
CREATE TABLE `terminal_operate`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `agent_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商编号',
  `for_operater` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '出库指从谁出库 ，入库指从入库给谁',
  `oper_num` int(11) NULL DEFAULT NULL COMMENT '操作数量',
  `sn_array` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '操作的sn数组集合',
  `oper_detail_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '具体操作类型 1-出/入库，2-回收/被回收',
  `oper_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '筛选栏类型 1-入库  2-出库',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 116 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '机具操作记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for terminal_work_key_batch
-- ----------------------------
DROP TABLE IF EXISTS `terminal_work_key_batch`;
CREATE TABLE `terminal_work_key_batch`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sn` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '机具SN号',
  `batch_no` int(6) NULL DEFAULT 0 COMMENT '当前批次序号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `sn_index`(`sn`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for test11
-- ----------------------------
DROP TABLE IF EXISTS `test11`;
CREATE TABLE `test11`  (
  `id` int(11) NOT NULL,
  `test2` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for three_recorded_flow
-- ----------------------------
DROP TABLE IF EXISTS `three_recorded_flow`;
CREATE TABLE `three_recorded_flow`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `agent_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '入账代理商编号',
  `create_time` datetime(0) NULL,
  `activte_amount` decimal(20, 2) NOT NULL COMMENT '收益金额',
  `recorded_status` int(2) NOT NULL DEFAULT 0 COMMENT '入账状态',
  `recorded_sum` decimal(20, 2) NOT NULL COMMENT '入账总额',
  `from_serial_no` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '来源系统流水号',
  `trans_order_no` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '交易流水号',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_agent_no`(`agent_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '三方收益 代理商流水表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for time_task_record
-- ----------------------------
DROP TABLE IF EXISTS `time_task_record`;
CREATE TABLE `time_task_record`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `running_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '运行编号',
  `running_status` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '运行状态 init,running,complete',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `source_system` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '来源系统',
  `interface_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接口名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1171505 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for timed_task
-- ----------------------------
DROP TABLE IF EXISTS `timed_task`;
CREATE TABLE `timed_task`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `task_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '定时任务名称',
  `task_group` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '定时任务组',
  `program_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '程序名称',
  `description` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '程序描述',
  `remarks` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '运营备注',
  `task_status` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '当时定时状态：NONE,NORMAL,PAUSED,COMPLETE,ERROR,BLOCKED',
  `enabled_state` int(1) NULL DEFAULT 0 COMMENT '启动状态:0未启动,1启动',
  `expression` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '表达式',
  `retrieval_time` timestamp(0) NULL DEFAULT NULL COMMENT '最新检索时间',
  `abnormal_time` timestamp(0) NULL DEFAULT NULL COMMENT '异常开始时间',
  `warning_state` int(1) NULL DEFAULT 0 COMMENT '是否预警:0否,1是',
  `error_warning_state` int(1) NULL DEFAULT 0 COMMENT '服务停止预警开关:0否,1是',
  `overtime_warning_state` int(1) NULL DEFAULT 0 COMMENT '超时预警开关:0否,1是',
  `early_warning_threshold` int(11) NULL DEFAULT NULL COMMENT '超时阀值(分钟)',
  `last_time` timestamp(0) NULL DEFAULT NULL COMMENT '上次执行时间',
  `next_time` timestamp(0) NULL DEFAULT NULL COMMENT '预计下次执行时间',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1082 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '定时任务监控表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tmk_log
-- ----------------------------
DROP TABLE IF EXISTS `tmk_log`;
CREATE TABLE `tmk_log`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sn` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `key_content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `check_value` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` int(11) NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tmp_agent_service
-- ----------------------------
DROP TABLE IF EXISTS `tmp_agent_service`;
CREATE TABLE `tmp_agent_service`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `agent_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商ID',
  `service_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '服务ID',
  `status` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态:1-正常，0-状态',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `agent_id_UNIQUE`(`agent_id`, `service_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '代理商代理服务表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tmp_hardware_product_set
-- ----------------------------
DROP TABLE IF EXISTS `tmp_hardware_product_set`;
CREATE TABLE `tmp_hardware_product_set`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `hps_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '硬件产品集ID',
  `set_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '硬件产品集名称',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `hps_id_UNIQUE`(`hps_id`) USING BTREE,
  UNIQUE INDEX `set_name_UNIQUE`(`set_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '硬件产品集表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tmp_hardware_product_setinfo
-- ----------------------------
DROP TABLE IF EXISTS `tmp_hardware_product_setinfo`;
CREATE TABLE `tmp_hardware_product_setinfo`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `hps_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '硬件产品集ID',
  `hp_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '硬件产品ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `hps_id_UNIQUE`(`hps_id`, `hp_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '硬件产品集明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tmp_oem_info
-- ----------------------------
DROP TABLE IF EXISTS `tmp_oem_info`;
CREATE TABLE `tmp_oem_info`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `oem_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'OEM ID',
  `oem_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'OEM名称',
  `weblogo` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商WEBLOGO',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `oem_id_UNIQUE`(`oem_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'OEM基本信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tmp_prod_columns
-- ----------------------------
DROP TABLE IF EXISTS `tmp_prod_columns`;
CREATE TABLE `tmp_prod_columns`  (
  `TABLE_CATALOG` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `TABLE_SCHEMA` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `TABLE_NAME` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `COLUMN_NAME` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `ORDINAL_POSITION` bigint(21) UNSIGNED NOT NULL DEFAULT 0,
  `COLUMN_DEFAULT` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `IS_NULLABLE` varchar(3) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `DATA_TYPE` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `CHARACTER_MAXIMUM_LENGTH` bigint(21) UNSIGNED NULL DEFAULT NULL,
  `CHARACTER_OCTET_LENGTH` bigint(21) UNSIGNED NULL DEFAULT NULL,
  `NUMERIC_PRECISION` bigint(21) UNSIGNED NULL DEFAULT NULL,
  `NUMERIC_SCALE` bigint(21) UNSIGNED NULL DEFAULT NULL,
  `DATETIME_PRECISION` bigint(21) UNSIGNED NULL DEFAULT NULL,
  `CHARACTER_SET_NAME` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `COLLATION_NAME` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `COLUMN_TYPE` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `COLUMN_KEY` varchar(3) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `EXTRA` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `PRIVILEGES` varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `COLUMN_COMMENT` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ''
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tmp_ys_data_import
-- ----------------------------
DROP TABLE IF EXISTS `tmp_ys_data_import`;
CREATE TABLE `tmp_ys_data_import`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `sid` bigint(11) NULL DEFAULT NULL,
  `sn` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `ter_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `batch_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `remark` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1826608 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for trade_sum_info
-- ----------------------------
DROP TABLE IF EXISTS `trade_sum_info`;
CREATE TABLE `trade_sum_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime(0) NULL,
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  `agent_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `branch` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `one_level` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `two_level` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `three_level` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `four_level` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `five_level` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `trade_sum` decimal(20, 2) NULL DEFAULT NULL COMMENT '交易量',
  `mer_sum` int(11) NULL DEFAULT NULL COMMENT '商户总数',
  `activate_sum` int(11) NULL DEFAULT NULL COMMENT '激活总数',
  `machines_stock` int(11) NULL DEFAULT NULL COMMENT '机具库存',
  `unused_machines` int(11) NULL DEFAULT NULL COMMENT '未使用机具',
  `expired_not_activated` int(11) NULL DEFAULT NULL COMMENT '到期未激活',
  `three_income` decimal(20, 2) NULL DEFAULT NULL COMMENT '三方收益',
  `recorded_date` datetime(0) NULL DEFAULT NULL COMMENT '入账日期',
  `recorded_status` int(11) NULL DEFAULT NULL COMMENT '入账状态',
  `team_id` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务产品组织ID',
  `income_calc` int(1) NULL DEFAULT 0 COMMENT '三方收益是否计算 0未计算，1已计算',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_trade_create_time`(`create_time`) USING BTREE,
  INDEX `index_trade_agent_no`(`agent_no`) USING BTREE,
  INDEX `index_trade_recorded_date`(`recorded_date`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23936 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for trans_day_summary
-- ----------------------------
DROP TABLE IF EXISTS `trans_day_summary`;
CREATE TABLE `trans_day_summary`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `agent_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '代理商编号',
  `parent_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '父级代理商编号',
  `agent_node` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '代理商节点',
  `trans_date` date NOT NULL COMMENT '交易日期',
  `agent_level` int(10) NOT NULL COMMENT '代理商级别',
  `self_trans_amount` decimal(30, 2) NOT NULL DEFAULT 0.00 COMMENT '直属交易总金额',
  `self_trans_num` int(10) NOT NULL DEFAULT 0 COMMENT '直属交易总笔数',
  `all_trans_amount` decimal(30, 2) NOT NULL DEFAULT 0.00 COMMENT '直属以及下级交易总金额',
  `all_trans_num` int(10) NOT NULL DEFAULT 0 COMMENT '直属以及下级交易总笔数',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `agent_no`(`agent_no`, `trans_date`) USING BTREE,
  INDEX `agent_node`(`agent_node`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 468272 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '交易日汇总表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for trans_info
-- ----------------------------
DROP TABLE IF EXISTS `trans_info`;
CREATE TABLE `trans_info`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单编号',
  `acq_enname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构英文名',
  `acq_code` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构编号',
  `acq_merchant_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构商户号',
  `acq_terminal_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构终端号',
  `acq_auth_no` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构授权码',
  `acq_reference_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构参考号',
  `acq_batch_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '收单机构批次号',
  `acq_serial_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构流水号',
  `acq_response_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构返回的处理码',
  `agent_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商编号',
  `merchant_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号',
  `terminal_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '终端号',
  `batch_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '批次号',
  `serial_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '流水号',
  `account_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易账号',
  `card_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '卡类型',
  `read_card` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '读卡方式：IC（插卡）、tranck（刷卡） quickPass（闪付）',
  `currency_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易币种',
  `trans_amount` decimal(30, 2) NULL DEFAULT NULL COMMENT '交易金额',
  `merchant_fee` decimal(30, 2) NULL DEFAULT NULL COMMENT '商户手续费',
  `merchant_rate` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户扣率',
  `acq_merchant_fee` decimal(30, 2) NULL DEFAULT NULL COMMENT '收单机构手续费',
  `acq_merchant_rate` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单商户扣率',
  `trans_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易类型\r\n 消费PURCHASE, 冲正REVERSED, 消费撤销PURCHASE_VOID,预授权PRE_AUTH, 预授权撤销PRE_AUTH_VOID, 预授权完成PRE_AUTH_COMPLETA,预授权完成撤销PRE_AUTH_COMPLETE_VOID, 退货PURCHASE_REFUND, 查余额BALANCE_QUERY,转账TRANSFER_ACCOUNTS;',
  `trans_status` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易状态 	\r\n成功SUCCESS,失败FAILED, 初始化INIT, 已冲正REVERSED, 已撤销REVOKED,* 已结算SETTLE, 超额OVERLIMIT,已退款REFUND 已完成, COMPLETE 关闭,CLOSED;',
  `trans_source` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易来源',
  `ori_acq_batch_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构原交易批次号',
  `ori_acq_serial_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构原交易流水号',
  `ori_batch_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '终端原交易批次号',
  `ori_serial_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '终端原交易流水号',
  `acq_settle_date` timestamp(0) NULL DEFAULT NULL COMMENT '收单机构清算日期',
  `merchant_settle_date` timestamp(0) NULL DEFAULT NULL COMMENT '商户清算日期',
  `my_settle` int(11) NULL DEFAULT 0 COMMENT '二清标志，0否，1是',
  `review_status` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '审核状态 1正常，2待审核，3审核失败',
  `trans_time` timestamp(0) NULL DEFAULT NULL COMMENT '交易时间（保存为收单机构返回的报文里面的交易时间）',
  `last_update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后更新时间',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `settle_status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '结算状态1：已结算  0或空：未结算   2.结算中   3.结算失败',
  `settle_err_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '结算错误编号',
  `settle_err_msg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '结算错误信息',
  `belong_pay` int(11) NULL DEFAULT NULL COMMENT '隶属支付：1.龙宝支付 ',
  `settlement_method` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '结算方式 0 t0 ,1 t1',
  `bag_settle` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '结算标志1.钱包支付 0否',
  `trans_msg` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `cardholder_phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '持卡人手机号',
  `trans_id` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易流水号-翰鑫自定义的',
  `freeze_status` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '冻结标识  1为冻结  0为未冻结',
  `sign_img` varchar(150) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '签名图片文件名称， 设备类型是传统POS，并且为Null说明签名不存在，其他设备使用之前规则',
  `device_sn` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备编号',
  `sign_check_person` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '小票审核人',
  `sign_check_time` timestamp(0) NULL DEFAULT NULL COMMENT '审核时间',
  `is_iccard` int(1) NULL DEFAULT NULL COMMENT '是否是ic卡交易 0 否 1是',
  `ic_msg` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'IC???55????????ˉ',
  `insurance` int(1) NULL DEFAULT NULL COMMENT '是否同意刷卡保障服务 0否，1是',
  `expired` varchar(4) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '过期',
  `pos_type` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户类型',
  `msg_id` char(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易日志ID',
  `acc_status` int(11) NULL DEFAULT 0 COMMENT '对账状态，0未对账，1已对账',
  `issued_status` int(11) NULL DEFAULT 0 COMMENT '下发状态，0未下发，1已下发',
  `service_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户服务id',
  `single_share_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '单笔分润',
  `acq_service_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单服务id',
  `syn_status` int(2) NOT NULL DEFAULT 1 COMMENT '出款同步状态 1.未提交 2.已提交',
  `holidays_mark` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '节假日标志：1-工作日，2-节假日',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `order_no`(`order_no`) USING BTREE,
  UNIQUE INDEX `order_no_ind`(`order_no`) USING BTREE,
  INDEX `trans_time`(`trans_time`) USING BTREE,
  INDEX `create_time`(`create_time`) USING BTREE,
  INDEX `agent_no`(`agent_no`) USING BTREE,
  INDEX `merchant_no`(`merchant_no`) USING BTREE,
  INDEX `acq_merchant_no`(`acq_merchant_no`) USING BTREE,
  INDEX `fh_index`(`acq_merchant_no`, `acq_terminal_no`, `acq_serial_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 45956334 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '交易表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for trans_info_freeze_new_log
-- ----------------------------
DROP TABLE IF EXISTS `trans_info_freeze_new_log`;
CREATE TABLE `trans_info_freeze_new_log`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `order_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单编号',
  `trans_type` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '交易类型，0pos交易，1快捷交易',
  `oper_type` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作类型,0冻结,1解冻',
  `oper_reason` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作原因',
  `freeze_way` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '冻结类型,0无期冻结，1有期冻结',
  `freeze_day` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '冻结天数',
  `oper_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  `oper_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '操作人ID',
  `oper_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '操作人',
  `settle_time` date NULL DEFAULT NULL COMMENT '操作后交易的结算时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 662 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for trans_info_pre_freeze_log
-- ----------------------------
DROP TABLE IF EXISTS `trans_info_pre_freeze_log`;
CREATE TABLE `trans_info_pre_freeze_log`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `merchant_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户编号',
  `pre_freeze_note` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '预冻结备注',
  `oper_log` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作日志',
  `oper_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  `oper_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '操作人ID',
  `oper_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '操作人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1846 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '预冻结日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for trans_order_ext
-- ----------------------------
DROP TABLE IF EXISTS `trans_order_ext`;
CREATE TABLE `trans_order_ext`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '交易订单号',
  `group_code` int(11) NOT NULL COMMENT '集群编号',
  `merchant_no` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户号',
  `acq_merchant_no` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收单机构商户号',
  `trans_type` int(2) NOT NULL DEFAULT 0 COMMENT '交易类型 1.标准刷卡 2.优享 3.活动 4.vip刷卡 5.跳转集群',
  `trans_time` datetime(0) NOT NULL COMMENT '交易时间',
  `remark` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注，预留',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `index_order_no`(`order_no`) USING BTREE,
  INDEX `index_merchant_no`(`merchant_no`) USING BTREE,
  INDEX `index_trans_time`(`trans_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '交易订单拓展表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for trans_order_init
-- ----------------------------
DROP TABLE IF EXISTS `trans_order_init`;
CREATE TABLE `trans_order_init`  (
  `order_no` varchar(225) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单号 外键交易表ID',
  `acq_enname` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构英文名称',
  `resp_msg` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '请求/响应数据',
  PRIMARY KEY (`order_no`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '初始化的交易 请求/响应数据记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for trans_push
-- ----------------------------
DROP TABLE IF EXISTS `trans_push`;
CREATE TABLE `trans_push`  (
  `id` tinyint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `merchant_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号',
  `device_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备类型(1:android，2:ios)',
  `push_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '推送id',
  `push_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '推送类型(1:通知，2:类型)',
  `app_no` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'app_no',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 127 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '交易推送表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for trans_repeat
-- ----------------------------
DROP TABLE IF EXISTS `trans_repeat`;
CREATE TABLE `trans_repeat`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `card_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `amount` decimal(10, 2) NULL DEFAULT NULL,
  `create_time` timestamp(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for trans_res_code
-- ----------------------------
DROP TABLE IF EXISTS `trans_res_code`;
CREATE TABLE `trans_res_code`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '错误代码',
  `res_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '错误类型',
  `user_msg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '提示用户界面的内容',
  `code_msg` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '程序分析的内容',
  `reason` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '原因',
  `solution` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '解决方案',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `code_unique`(`code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '交易返回错误信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for trans_route_group
-- ----------------------------
DROP TABLE IF EXISTS `trans_route_group`;
CREATE TABLE `trans_route_group`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `group_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '群组名称',
  `group_code` int(11) NULL DEFAULT NULL COMMENT '群组编号',
  `acq_id` int(11) NOT NULL COMMENT '收单机构ID编号',
  `acq_service_id` int(11) NULL DEFAULT NULL COMMENT '收单服务ID编号',
  `agent_no` int(11) NULL DEFAULT NULL COMMENT '所属代理商编号',
  `service_type` int(11) NULL DEFAULT NULL COMMENT '商户服务类型',
  `accounts_period` int(11) NULL DEFAULT NULL COMMENT '集群结算周期',
  `my_settle` int(11) NULL DEFAULT NULL COMMENT '是否优质商户：1.是 0.否  必填',
  `sales_no` int(11) NULL DEFAULT NULL COMMENT '所属销售编号',
  `allow_min_amount` decimal(12, 2) NULL DEFAULT NULL COMMENT '允许交易最小金额',
  `allow_max_amount` decimal(12, 2) NULL DEFAULT NULL COMMENT '允许交易最大金额',
  `allow_trans_start_time` time(0) NULL DEFAULT NULL COMMENT '允许交易起始时间',
  `allow_trans_end_time` time(0) NULL DEFAULT NULL COMMENT '允许交易截止时间',
  `def_acq_day_amount` decimal(12, 0) NULL DEFAULT NULL COMMENT '默认收单商户每日限额',
  `backups_group_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备用集群编号',
  `merchant_type` int(11) NULL DEFAULT NULL COMMENT '集群所属商户类型',
  `route_last` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '集群后缀',
  `route_describe` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '集群描述',
  `route_type` int(11) NULL DEFAULT NULL COMMENT '集群类型：1.销售 2.公司 3.测试',
  `status` int(11) NOT NULL DEFAULT 0 COMMENT '0：正常,1：停用',
  `group_province` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '集群所属省份',
  `group_city` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '集群归属城市',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_person` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建人',
  `warn_mobile` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '预警手机号，当集群中找不到可用的收单商户，则发送警告给此手机号。',
  `service_model` int(5) NULL DEFAULT NULL COMMENT '服务模式：sys_dict <SERVICE_MODEL>',
  `liquidation_channel` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '直清通道: sys_dict <LIQ_CHANNEL>',
  `map_group_id` int(20) NULL DEFAULT NULL COMMENT '映射集群',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `group_code`(`group_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 225 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '产品：从V1照搬到V2' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for trans_route_group_acq_merchant
-- ----------------------------
DROP TABLE IF EXISTS `trans_route_group_acq_merchant`;
CREATE TABLE `trans_route_group_acq_merchant`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '集群收单商户ID',
  `group_code` int(11) NOT NULL COMMENT '集群编号',
  `acq_merchant_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收单机构商户编号',
  `last_use_time` timestamp(0) NULL DEFAULT NULL COMMENT '最后使用时间',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_person` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `acq_merchant_no`(`acq_merchant_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 443 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '集群收单商户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for trans_route_group_acq_merchant_20181024
-- ----------------------------
DROP TABLE IF EXISTS `trans_route_group_acq_merchant_20181024`;
CREATE TABLE `trans_route_group_acq_merchant_20181024`  (
  `id` int(11) NOT NULL DEFAULT 0 COMMENT '集群收单商户ID',
  `group_code` int(11) NOT NULL COMMENT '集群编号',
  `acq_merchant_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收单机构商户编号',
  `last_use_time` timestamp(0) NULL DEFAULT NULL COMMENT '最后使用时间',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_person` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建人'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for trans_route_group_merchant
-- ----------------------------
DROP TABLE IF EXISTS `trans_route_group_merchant`;
CREATE TABLE `trans_route_group_merchant`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '集群普通商户ID',
  `group_code` int(11) NOT NULL COMMENT '集群编号',
  `pos_merchant_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户编号',
  `service_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '服务类型',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_person` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `pos_merchant_no_service_type`(`pos_merchant_no`, `service_type`) USING BTREE,
  INDEX `trgm_group_code`(`group_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7426 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '集群普通商户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for trans_route_group_merchant_20181024
-- ----------------------------
DROP TABLE IF EXISTS `trans_route_group_merchant_20181024`;
CREATE TABLE `trans_route_group_merchant_20181024`  (
  `id` int(11) NOT NULL DEFAULT 0 COMMENT '集群普通商户ID',
  `group_code` int(11) NOT NULL COMMENT '集群编号',
  `pos_merchant_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户编号',
  `service_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '服务类型',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_person` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建人'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for trans_to_vip
-- ----------------------------
DROP TABLE IF EXISTS `trans_to_vip`;
CREATE TABLE `trans_to_vip`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `order_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单号',
  `status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态 0 未处理 1 已处理',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '交易上送会员积分系统' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for unified_err_code
-- ----------------------------
DROP TABLE IF EXISTS `unified_err_code`;
CREATE TABLE `unified_err_code`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `err_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '错误码',
  `err_msg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '错误提示语',
  `team_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'ALL' COMMENT '组织id，ALL为不限',
  `client_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'ALL' COMMENT '客户端类型  ALL/POS/MPOS',
  `device_pn` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'ALL' COMMENT '机具pn型号 ，ALL为不限',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uni`(`err_code`, `team_id`, `client_type`, `device_pn`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '统一错误码' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for unique_encourage_seq
-- ----------------------------
DROP TABLE IF EXISTS `unique_encourage_seq`;
CREATE TABLE `unique_encourage_seq`  (
  `id` bigint(25) NOT NULL AUTO_INCREMENT,
  `create_time` timestamp(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 146 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '鼓励金订单自增序列表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for unique_seq
-- ----------------------------
DROP TABLE IF EXISTS `unique_seq`;
CREATE TABLE `unique_seq`  (
  `id` bigint(25) NOT NULL AUTO_INCREMENT,
  `create_time` timestamp(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 123 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '自增序列表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_center_menu
-- ----------------------------
DROP TABLE IF EXISTS `user_center_menu`;
CREATE TABLE `user_center_menu`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `merchant_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户号',
  `menu_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '菜单编码',
  `click_status` int(2) NULL DEFAULT NULL COMMENT '是否点击过,0:否,1:是',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '数据最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `merchant_no_menu_code_uni`(`merchant_no`, `menu_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户中心菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_coupon
-- ----------------------------
DROP TABLE IF EXISTS `user_coupon`;
CREATE TABLE `user_coupon`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `coupon_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '优惠卷编号',
  `face_value` decimal(30, 2) NULL DEFAULT NULL COMMENT '优惠卷面值',
  `balance` decimal(30, 2) NULL DEFAULT NULL COMMENT '优惠卷可用金额',
  `coupon_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '优惠卷活动类型(详见字典表 COUPON_CODE)',
  `cancel_verification_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '核销方式(详见字典表CANCEL_VERIFICATION_CODE)',
  `coupon_status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '优惠卷状态(详见字典表)',
  `start_time` timestamp(0) NULL DEFAULT NULL COMMENT '优惠卷开始时间',
  `end_time` timestamp(0) NULL DEFAULT NULL COMMENT '优惠卷结束时间',
  `token` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '唯一标识',
  `merchant_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户编号',
  `activity_first` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '活动优先级：A-E优先级递增',
  `last_update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `activity_entity_id` int(20) NULL DEFAULT 0 COMMENT '优惠卷配置id',
  `coupon_amount` decimal(30, 2) NULL DEFAULT 0.00 COMMENT '优惠券面值',
  `gift_amount` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '赠送金额',
  `coupon_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '卷原有类型（抽奖）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `token`(`token`) USING BTREE,
  INDEX `create_time_indx`(`create_time`) USING BTREE,
  INDEX `ind_start_time`(`start_time`) USING BTREE,
  INDEX `coupon_no_UNIQUE`(`coupon_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8343695 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_entity_info
-- ----------------------------
DROP TABLE IF EXISTS `user_entity_info`;
CREATE TABLE `user_entity_info`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户ID',
  `user_type` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户类型1为代理商,2为商户',
  `entity_id` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对应实体编号代理商编号',
  `apply` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '应用（理财,收单）1为代理商系统2为商户系统',
  `manage` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否管理员:1管理员0销售员2.店员',
  `status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态:1为正常0为失效',
  `last_notice_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '公告最后时间',
  `loginkey` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登录KEY',
  `login_time` timestamp(0) NULL DEFAULT NULL COMMENT '登录时间',
  `is_agent` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '是否是代理商，0：不是，1：是',
  `access_team_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务范围',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15768 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '1管理员0销售员' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_feedback_problem
-- ----------------------------
DROP TABLE IF EXISTS `user_feedback_problem`;
CREATE TABLE `user_feedback_problem`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '反馈用户 ID',
  `user_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '反馈用户类型:1-代理商,2-商户',
  `problem_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '问题类型',
  `content` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '内容',
  `printscreen` varchar(2048) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '截图',
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标题',
  `submit_time` timestamp(0) NULL DEFAULT NULL COMMENT '提交时间',
  `complainter` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '投诉对象 1 对客户服务不满意 2 对渠道经理不满意 3 对上级代理不满意',
  `mobile` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电话号码',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 279 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户反馈问题表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户ID',
  `user_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户姓名',
  `mobilephone` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态',
  `update_pwd_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改密码时间',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户密码',
  `team_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组织ID',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `email` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `pay_pwd` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '支付密码',
  `manage_pwd` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '管理密码',
  `second_user_node` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `register_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '人人代理注册编号',
  `wrong_password_count` int(11) NOT NULL DEFAULT 0 COMMENT '错误密码次数',
  `lock_time` datetime(0) NULL DEFAULT NULL COMMENT '锁定时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 126871 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_read_notice
-- ----------------------------
DROP TABLE IF EXISTS `user_read_notice`;
CREATE TABLE `user_read_notice`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户id',
  `entity_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户对应实体id',
  `message` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公告消息',
  `read_time` timestamp(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fuhe_key`(`user_id`, `entity_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户已阅读公告信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_shop
-- ----------------------------
DROP TABLE IF EXISTS `user_shop`;
CREATE TABLE `user_shop`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `shop_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '店铺名称',
  `phone` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电话',
  `shop_address` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地址',
  `shopkeeper` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '店主',
  `create_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `show_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '0 全部供应，1 本店私营',
  `user_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户id',
  `sequence` int(10) NULL DEFAULT NULL COMMENT '序号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1985 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_shop_trans
-- ----------------------------
DROP TABLE IF EXISTS `user_shop_trans`;
CREATE TABLE `user_shop_trans`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `shop_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '店铺名称',
  `create_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `user_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_subject_info
-- ----------------------------
DROP TABLE IF EXISTS `user_subject_info`;
CREATE TABLE `user_subject_info`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户类型',
  `subject_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'varchar(100)',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `subject_no`(`subject_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户类型科目关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for v1_1level_agent
-- ----------------------------
DROP TABLE IF EXISTS `v1_1level_agent`;
CREATE TABLE `v1_1level_agent`  (
  `v2_agent_no` int(11) NULL DEFAULT NULL,
  `v1_id` int(11) NULL DEFAULT NULL,
  `v1_agent_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `v1_mobile` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `import_batch_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for v1_agent_info
-- ----------------------------
DROP TABLE IF EXISTS `v1_agent_info`;
CREATE TABLE `v1_agent_info`  (
  `id` int(10) NULL DEFAULT NULL,
  `agent_status` int(11) NULL DEFAULT NULL,
  `parent_id` int(10) NULL DEFAULT NULL,
  `agent_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `agent_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `agent_link_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `agent_link_tel` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `agent_link_mail` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `agent_area` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `agent_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `sale_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `manager_logo` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `client_logo` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  `agent_pay` decimal(22, 0) NULL DEFAULT NULL,
  `mix_amout` decimal(22, 0) NULL DEFAULT NULL,
  `agent_rate` decimal(22, 0) NULL DEFAULT NULL,
  `bank_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_person` varchar(150) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `account_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `account_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `cnaps_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `account_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `agent_area_type` char(6) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `profit_sharing` char(3) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `group_code` int(11) NULL DEFAULT NULL,
  `sharing_rate` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `parent_node` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `self_node` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `bag_settle` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `brand_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `agent_no_top` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `agent_no_referee` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `province` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `city` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `level` int(3) NULL DEFAULT NULL,
  `examination_opinions` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `attachment` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `is_invest` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `invest_amount` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `common_deposit_amount` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `common_deposit_rate` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `over_deposit_rate` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `deposit_rate` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `is_channel` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `rep_pay_fee` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `qr_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `fastpay_switch` varchar(6) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `import_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `import_status` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0',
  `import_msg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `v2_1level_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `import_batch_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for v1_pos_merchant
-- ----------------------------
DROP TABLE IF EXISTS `v1_pos_merchant`;
CREATE TABLE `v1_pos_merchant`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `merchant_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户编号',
  `merchant_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户全称',
  `merchant_short_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户简称',
  `lawyer` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '法人',
  `my_settle` int(11) NULL DEFAULT 0 COMMENT '1二清',
  `merchant_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户类型',
  `mobile_username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机登录用户名',
  `mobile_password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机登录密码',
  `open_status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '开通状态:0关闭，1正常,2额度提升',
  `agent_no` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商编号',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '注册地址',
  `sale_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '经营地址',
  `province` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '省份',
  `city` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '城市',
  `link_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人',
  `phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `mcc` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '行业代码',
  `sale_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '销售名称',
  `settle_cycle` int(11) NULL DEFAULT NULL COMMENT '结算周期',
  `bank_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '开户行',
  `account_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '卡户名',
  `account_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账户名称',
  `cnaps_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联行行号',
  `account_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账户类型',
  `attachment` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '附件',
  `examination_opinions` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '审核意见',
  `real_flag` int(11) NULL DEFAULT NULL COMMENT '是否实名商户0否，1是',
  `clear_card_no` int(11) NULL DEFAULT NULL COMMENT '是否可以手输卡号0否1是',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `id_card_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '法人身份证号码',
  `terminal_count` int(11) NULL DEFAULT NULL COMMENT '商户终端数量',
  `main_business` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主营业务',
  `checker` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '审核人',
  `remark` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注字段（可存放商户psam卡号）',
  `code_word` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改密码暗语',
  `belong_to_agent` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `bag` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '钱包',
  `open_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '商户开启时间',
  `trans_time_start` timestamp(0) NULL DEFAULT NULL,
  `trans_time_end` timestamp(0) NULL DEFAULT NULL,
  `pos_type` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备类型 1移联商宝,2传统POS,3移小宝,4移联商通,5超级刷',
  `merchant_api_used` int(1) NULL DEFAULT 0 COMMENT '是否开通了接口 0 没开通 1开通了',
  `add_type` int(1) NULL DEFAULT 0 COMMENT '进件类型0网站 1客户端',
  `terminal_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '机具号用;隔开',
  `question` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `answer` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `pay_method` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '10' COMMENT '支付方式  第一位代表可以Pos支付，第二位代表可以快捷支付  1表示是 0表示否',
  `trans_cancel` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易是否容许撤销 0 否 1是',
  `open_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '开通方式：1:手动；2:自动',
  `weixin` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '微信账号绑定',
  `parent_node` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上级代理商节点',
  `bus_license_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '营业执照编号',
  `bag_settle` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '钱包结算1是0否',
  `agent_lock` int(1) NULL DEFAULT 0 COMMENT '是否锁定，是否能修改一级代理商;0:锁定 1:已解锁',
  `app_no` int(11) NULL DEFAULT NULL COMMENT '对应APP编号',
  `lift_status` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '额度提升状态: 1 额度提升',
  `xy_sign` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '协议签名',
  `bag_prepare` int(1) NULL DEFAULT 0 COMMENT '表示钱包是否准备就绪，0没有准备就绪  1已准备就绪',
  `send_hlf` int(11) NULL DEFAULT 0 COMMENT '同步好乐付状态 200成功',
  `freeze_status` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '商户冻结状态0,未冻结，1冻结',
  `failed_item` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '审核失败项,多个项目间用逗号分隔',
  `account_province` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '开户行的省',
  `account_city` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '开户行的市',
  `check_identity_status` int(11) NULL DEFAULT NULL COMMENT '1.结算中心验证通过。2.鹏远征信验证通过。3.各项验证均失败',
  `has_credit_card` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '标识是否有信用卡：0无,1有',
  `scan_pay` int(2) NOT NULL DEFAULT 1 COMMENT '扫码支付 1：未开通 2：开通 ',
  `last_update_time` timestamp(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  `examine_repeat_time` datetime(0) NULL DEFAULT NULL COMMENT '复审时间',
  `examine_repeat_status` int(2) NULL DEFAULT NULL COMMENT '复审状态  null或为空表示未复审 1.复审审核通过、2.复审失败',
  `examine_repeat_person` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '复审人',
  `examine_repeat_desc` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '复审描述',
  `check_type` varchar(4) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '审核类型:1人工审核;2自动审核',
  `reg_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'device' COMMENT '商户来源 device 机具 activitcode 激活码 superpush 超级推',
  `manager_pwd` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户端管理密码',
  `real_fixed_tag` varchar(4) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '是否实体商户:0否,1是',
  `trans_mode` int(2) NOT NULL DEFAULT 0 COMMENT '交易模式, 0集群模式，1直清模式',
  `pay_password` varchar(266) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '快捷支付支付密码',
  `import_batch_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `merchant_no`(`merchant_no`) USING BTREE,
  UNIQUE INDEX `agent_no`(`agent_no`, `pos_type`, `mobile_username`) USING BTREE,
  INDEX `mobile_username`(`mobile_username`) USING BTREE,
  INDEX `create_time`(`create_time`) USING BTREE,
  INDEX `merchant_name`(`merchant_name`) USING BTREE,
  INDEX `mobile_username, pos_type`(`mobile_username`, `pos_type`) USING BTREE,
  INDEX `belong_to_agent`(`belong_to_agent`) USING BTREE,
  INDEX `parent_node`(`parent_node`) USING BTREE,
  INDEX `merchant_short_name`(`merchant_short_name`) USING BTREE,
  INDEX `ind_terminal_no`(`terminal_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1067807 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for v1_pos_terminal
-- ----------------------------
DROP TABLE IF EXISTS `v1_pos_terminal`;
CREATE TABLE `v1_pos_terminal`  (
  `ID` int(10) NULL DEFAULT NULL,
  `SN` varchar(765) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `TERMINAL_NO` varchar(765) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `MERCHANT_NO` varchar(765) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `PSAM_NO` varchar(765) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `belong_agent_no` varchar(765) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `belong_three_agent_no` varchar(765) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `agent_no` varchar(765) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `open_status` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `type` varchar(765) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `allot_batch` varchar(765) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `model` varchar(765) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `tmk` varchar(765) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `tmk_tpk` varchar(765) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `tmk_tak` varchar(765) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `START_TIME` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  `CREATE_TIME` timestamp(0) NOT NULL,
  `pos_type` int(11) NULL DEFAULT NULL,
  `need_check` tinyint(4) NULL DEFAULT NULL,
  `last_check_in_time` timestamp(0) NOT NULL,
  `cashier_no` varchar(765) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `serial_no` varchar(765) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `batch_no` varchar(765) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `is_event` varchar(6) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `import_batch_no` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '导入的批次号'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for verification
-- ----------------------------
DROP TABLE IF EXISTS `verification`;
CREATE TABLE `verification`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '核销订单号',
  `merchant_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户ID',
  `coupon_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '优惠卷编号',
  `face_value` decimal(30, 2) NULL DEFAULT NULL COMMENT '优惠卷面值',
  `balance` decimal(30, 2) NULL DEFAULT NULL COMMENT '优惠卷可用金额',
  `verification_fee` decimal(30, 2) NULL DEFAULT NULL COMMENT '核销金额',
  `status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态（0核销成功 1核销失败 2核销中【1、2暂时不用】）',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '核销类型: 1或\'提现,  2交易3过期4抵扣自选手续费',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `order_no`(`order_no`, `coupon_no`, `create_time`, `type`) USING BTREE,
  INDEX `create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4161 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '核销表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for warning_events
-- ----------------------------
DROP TABLE IF EXISTS `warning_events`;
CREATE TABLE `warning_events`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '预警事件ID',
  `event_number` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '编号',
  `acq_id` int(11) NULL DEFAULT NULL COMMENT '收单机构ID',
  `acq_enname` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构英文名',
  `acq_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单机构中文名',
  `acq_service_id` int(11) NULL DEFAULT NULL COMMENT '收单服务ID',
  `service_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单服务名',
  `trans_status` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易异常状态集合',
  `trans_status_number` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易异常状态统计数量',
  `out_service_id` int(11) NULL DEFAULT NULL COMMENT '出款服务ID',
  `out_service_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '出款服务名',
  `message` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '预警内容',
  `status` int(1) NOT NULL DEFAULT 1 COMMENT '来源系统:1-交易系统,2-出款系统,3-定时任务监控系统',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `sms_time` timestamp(0) NULL DEFAULT NULL COMMENT '短信最后一次发送时间',
  `sms_count` int(1) NOT NULL DEFAULT 0 COMMENT '短信发送次数',
  `task_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '定时任务名称',
  `task_group` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '定时任务名称',
  `task_status` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '定时任务名称',
  `assignment_task` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分配的定时任务预警',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7381 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '预警事件' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for warning_people
-- ----------------------------
DROP TABLE IF EXISTS `warning_people`;
CREATE TABLE `warning_people`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '预警人ID',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `user_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `phone` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `status` int(1) NOT NULL DEFAULT 1 COMMENT '状态：1-收单机构预警人员，2-出款预警人员',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` timestamp(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  `assignment_task` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分配的定时任务预警',
  `sids` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 75 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '预警人员管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for warning_set
-- ----------------------------
DROP TABLE IF EXISTS `warning_set`;
CREATE TABLE `warning_set`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `service_id` int(11) NOT NULL COMMENT '服务id',
  `service_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `waring_cycle` int(11) NULL DEFAULT NULL COMMENT '预警周期',
  `exception_number` int(11) NULL DEFAULT NULL COMMENT '异常笔数(交易失败或者结算中)',
  `failur_waring_cycle` int(11) NULL DEFAULT NULL COMMENT '失败预警周期',
  `failur_exception_number` int(11) NULL DEFAULT NULL COMMENT '失败异常笔数(结算失败)',
  `status` int(1) NULL DEFAULT NULL COMMENT '出单1 收单2',
  `warn_time_type` int(3) NOT NULL DEFAULT 1 COMMENT '预警时间类型，1：全天，2：个性化',
  `warn_start_time` time(0) NULL DEFAULT NULL COMMENT '预警开始时间',
  `warn_end_time` time(0) NULL DEFAULT NULL COMMENT '预警结束时间',
  `warn_status` int(3) NOT NULL DEFAULT 1 COMMENT '预警状态，0：关闭，1：打开，2：删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_service_id`(`service_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 63 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '预警阈值设置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for wechat_config
-- ----------------------------
DROP TABLE IF EXISTS `wechat_config`;
CREATE TABLE `wechat_config`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `team_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '微信公众号配置对应的组织编号',
  `appid` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'appid',
  `appsecret` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `public_account` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公众号账号',
  `public_account_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公众号名称',
  `wx_token` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'token',
  `wx_ticket` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'token',
  `create_time` timestamp(0) NULL DEFAULT NULL,
  `last_update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `NewIndex1`(`appid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '微信公众号配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for wf_merchant_info_temp
-- ----------------------------
DROP TABLE IF EXISTS `wf_merchant_info_temp`;
CREATE TABLE `wf_merchant_info_temp`  (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `merchant_no` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号',
  `bp_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务产品ID',
  `mbp_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '进件ID',
  `status` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '状态(1成功2失败)',
  `bath_no` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '批次',
  `msg` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '返回信息',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '开放平台报备商户临时表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for xf_mer_rz_config
-- ----------------------------
DROP TABLE IF EXISTS `xf_mer_rz_config`;
CREATE TABLE `xf_mer_rz_config`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `mer_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户号',
  `ser_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '业务码',
  `channel` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '通道标识',
  `status` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '1' COMMENT '通道状态',
  `ord_by` int(10) NULL DEFAULT NULL COMMENT '排序',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `ind_mer_no`(`mer_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商户认证通道配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for xf_mer_service
-- ----------------------------
DROP TABLE IF EXISTS `xf_mer_service`;
CREATE TABLE `xf_mer_service`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `mer_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户号',
  `ser_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '业务代码',
  `ser_price` decimal(20, 2) NOT NULL COMMENT '单价',
  `status` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '1' COMMENT '状态',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_user` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '操作人',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `ind_mer_no`(`mer_no`) USING BTREE,
  INDEX `ind_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商户业务表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for xf_oob_config
-- ----------------------------
DROP TABLE IF EXISTS `xf_oob_config`;
CREATE TABLE `xf_oob_config`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `mer_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户号',
  `link_phone` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '联系人电话,逗号隔开',
  `oob_tag` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '短信模版标识',
  `oob_content` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '余额不足短信模版',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `ind_phone`(`link_phone`) USING BTREE,
  INDEX `ind_mer_no`(`mer_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '认证短信配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for xf_rz_channel
-- ----------------------------
DROP TABLE IF EXISTS `xf_rz_channel`;
CREATE TABLE `xf_rz_channel`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `channel` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '通道标识',
  `ser_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '业务代码',
  `status` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '1' COMMENT '通道状态',
  `ord_by` int(10) NULL DEFAULT NULL COMMENT '排序',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `ind_channle`(`channel`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '认证通道配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for xf_rz_channel_merchant
-- ----------------------------
DROP TABLE IF EXISTS `xf_rz_channel_merchant`;
CREATE TABLE `xf_rz_channel_merchant`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `channel` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '通道标识',
  `mer_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户号',
  `mer_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '通道商户名',
  `status` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '1' COMMENT '商户状态',
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '接口地址',
  `secret_key` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'key',
  `public_key` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT 'RSA公钥',
  `exponent` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '实名验证Exponent',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `ind_channle`(`channel`) USING BTREE,
  INDEX `ind_mer_no`(`mer_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '通道商户配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for xf_rz_record
-- ----------------------------
DROP TABLE IF EXISTS `xf_rz_record`;
CREATE TABLE `xf_rz_record`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `mer_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户名称',
  `mer_no` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户代码',
  `order_no` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户订单号',
  `channel` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '通道标识',
  `rz_type` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'rz类型',
  `acc_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '银行卡号',
  `acc_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '验证用户姓名',
  `id_card` varchar(18) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '验证用户身份证号码',
  `phone` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '验证用户手机号',
  `status` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '验证状态PASS 认证通过 FAIL 认证失败 UNKNOWN 未知',
  `resp_code` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '响应码',
  `resp_msg` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '响应信息',
  `req_serial_no` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求流水号（厦门民生需要使用serno查询数据）',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `order_no`(`order_no`) USING BTREE,
  INDEX `ind_mer_no`(`mer_no`) USING BTREE,
  INDEX `ind_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '认证外放订单记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for xf_ser_code_class
-- ----------------------------
DROP TABLE IF EXISTS `xf_ser_code_class`;
CREATE TABLE `xf_ser_code_class`  (
  `ser_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '业务标识',
  `class_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '对应的服务类',
  INDEX `ind_ser_code`(`ser_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '业务标识和class配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for xf_service
-- ----------------------------
DROP TABLE IF EXISTS `xf_service`;
CREATE TABLE `xf_service`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '业务名称',
  `ser_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '业务代码',
  `ser_price` decimal(20, 2) NOT NULL COMMENT '参考单价',
  `status` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '1' COMMENT '状态',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务描述',
  `create_user` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '操作人',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unind_sercode`(`ser_code`) USING BTREE,
  INDEX `ind_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '业务配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for xf_service_change_task
-- ----------------------------
DROP TABLE IF EXISTS `xf_service_change_task`;
CREATE TABLE `xf_service_change_task`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `mer_ser_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户业务id',
  `status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '1' COMMENT '状态',
  `task_tag` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '预约操作标识',
  `pre_data` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '修改前的数据',
  `after_data` decimal(20, 2) NOT NULL COMMENT '修改后的数据',
  `task_time` date NOT NULL COMMENT '预约时间',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_user` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `ind_msid`(`mer_ser_id`) USING BTREE,
  INDEX `ind_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '预约修改业务表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for xf_service_charge_record
-- ----------------------------
DROP TABLE IF EXISTS `xf_service_charge_record`;
CREATE TABLE `xf_service_charge_record`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `mer_no` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户编号',
  `amount` decimal(20, 2) NOT NULL DEFAULT 0.00 COMMENT '充值金额',
  `balance_pre` decimal(20, 2) NOT NULL DEFAULT 0.00 COMMENT '充值前金额',
  `balance_after` decimal(20, 2) NOT NULL DEFAULT 0.00 COMMENT '充值后金额',
  `charger` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '充值人',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `ind_mer_no`(`mer_no`) USING BTREE,
  INDEX `ind_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商户充值记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for xf_service_merchant
-- ----------------------------
DROP TABLE IF EXISTS `xf_service_merchant`;
CREATE TABLE `xf_service_merchant`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `mer_no` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户代码',
  `mer_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户名称',
  `mer_status` int(2) NOT NULL DEFAULT 1 COMMENT '商户状态',
  `mer_mobile` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '联系电话',
  `mer_contact` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '负责人姓名',
  `mer_idcard` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '负责人身份证',
  `secret_key` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'key',
  `public_key` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT 'RSA公钥',
  `private_key` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT 'RSA私钥',
  `pass_ip` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '商户ip 配置',
  `balance` decimal(20, 2) NOT NULL DEFAULT 0.00 COMMENT '余额',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `charge` decimal(20, 2) NOT NULL DEFAULT 0.00 COMMENT '充值阀值',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unind_mer_no`(`mer_no`) USING BTREE,
  INDEX `ind_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '业务商户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for xf_service_order
-- ----------------------------
DROP TABLE IF EXISTS `xf_service_order`;
CREATE TABLE `xf_service_order`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `mer_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户号',
  `ser_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '业务代码',
  `order_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单号',
  `status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单状态',
  `order_info` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单信息',
  `resp_info` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '响应信息',
  `cost` decimal(20, 2) NOT NULL COMMENT '本次扣费',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `order_no`(`order_no`) USING BTREE,
  INDEX `ind_mer_no`(`mer_no`) USING BTREE,
  INDEX `ind_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '业务订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for xhlf_activity_merchant_order
-- ----------------------------
DROP TABLE IF EXISTS `xhlf_activity_merchant_order`;
CREATE TABLE `xhlf_activity_merchant_order`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '流水号',
  `active_order` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '激活订单号',
  `active_time` timestamp(0) NULL DEFAULT NULL COMMENT '激活时间',
  `target_amount` decimal(15, 2) NULL DEFAULT NULL COMMENT '目标金额',
  `total_amount` decimal(15, 2) NULL DEFAULT NULL COMMENT '累计交易金额',
  `reward_amount` decimal(15, 2) NULL DEFAULT NULL COMMENT '奖励金额',
  `reward_account_status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '奖励入账状态,0:未入账,1:已入账',
  `reward_account_time` timestamp(0) NULL DEFAULT NULL COMMENT '奖励入账时间',
  `activity_target_status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '活动达标状态,0:考核中,1:已达标,2:未达标',
  `activity_target_time` timestamp(0) NULL DEFAULT NULL COMMENT '活动达标时间',
  `reward_start_time` timestamp(0) NULL DEFAULT NULL COMMENT '奖励考核开始时间',
  `reward_end_time` timestamp(0) NULL DEFAULT NULL COMMENT '奖励考核结束时间',
  `merchant_limit_days` int(5) NULL DEFAULT NULL COMMENT '激活后商户累计交易天数',
  `repeat_status` int(2) NULL DEFAULT NULL COMMENT '是否重复注册,0:否,1:是',
  `reward_amount_config` decimal(10, 2) NULL DEFAULT NULL COMMENT '配置的奖励金额',
  `merchant_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户号',
  `agent_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '所属代理商编号',
  `operator` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作人',
  `last_update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '数据最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `merchant_no_uni`(`merchant_no`) USING BTREE,
  INDEX `active_time_ind`(`active_time`) USING BTREE,
  INDEX `active_order_ind`(`active_order`) USING BTREE,
  INDEX `agent_no_ind`(`agent_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100000 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '新欢乐返活动商户奖励订单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for xhlf_activity_order
-- ----------------------------
DROP TABLE IF EXISTS `xhlf_activity_order`;
CREATE TABLE `xhlf_activity_order`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '流水号',
  `active_order` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '激活订单号',
  `active_time` timestamp(0) NULL DEFAULT NULL COMMENT '激活时间',
  `target_amount` decimal(15, 2) NULL DEFAULT NULL COMMENT '目标金额',
  `total_amount` decimal(15, 2) NULL DEFAULT NULL COMMENT '累计交易金额',
  `reward_amount` decimal(15, 2) NULL DEFAULT NULL COMMENT '奖励金额',
  `current_cycle` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '考核周期',
  `current_target_status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '当前考核达标状态,0:未开始,1:考核中,2:已达标,3:未达标',
  `current_target_time` timestamp(0) NULL DEFAULT NULL COMMENT '当前考核达标时间',
  `reward_account_status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '奖励入账状态,0:未入账,1:已入账',
  `reward_account_time` timestamp(0) NULL DEFAULT NULL COMMENT '奖励入账时间',
  `activity_target_status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '活动达标状态,0:考核中,1:已达标,2:未达标',
  `activity_target_time` timestamp(0) NULL DEFAULT NULL COMMENT '活动达标时间',
  `reward_start_time` timestamp(0) NULL DEFAULT NULL COMMENT '奖励考核开始时间',
  `reward_end_time` timestamp(0) NULL DEFAULT NULL COMMENT '奖励考核结束时间',
  `merchant_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户号',
  `agent_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '所属代理商编号',
  `operator` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作人',
  `last_update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '数据最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `merchant_no_ind`(`merchant_no`) USING BTREE,
  INDEX `active_time_ind`(`active_time`) USING BTREE,
  INDEX `active_order_ind`(`active_order`) USING BTREE,
  INDEX `agent_no_ind`(`agent_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100000 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '新欢乐返活动订单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for xhlf_activity_record
-- ----------------------------
DROP TABLE IF EXISTS `xhlf_activity_record`;
CREATE TABLE `xhlf_activity_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `merchant_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户号',
  `activity_type_no` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '欢乐返子类型编号',
  `repeat_status` int(2) NULL DEFAULT NULL COMMENT '是否重复注册,0:否,1:是',
  `one_limit_days` int(5) NULL DEFAULT NULL COMMENT '激活后第一个周期,多少天内',
  `one_trans_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '激活后第一个周期,累计交易金额',
  `two_limit_days` int(5) NULL DEFAULT NULL COMMENT '激活后第二个周期,多少天内',
  `two_trans_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '激活后第二个周期,累计交易金额',
  `three_limit_days` int(5) NULL DEFAULT NULL COMMENT '激活后第三个周期,多少天内',
  `three_trans_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '激活后第三个周期,累计交易金额',
  `four_limit_days` int(5) NULL DEFAULT NULL COMMENT '激活后第四个周期,多少天内',
  `four_trans_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '激活后第四个周期,累计交易金额',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operator` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作人',
  `last_update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '数据最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `merchant_no_uni`(`merchant_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '新欢乐返记录表，用来记录新欢乐返商户激活时的配置信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for xhlf_agent_account_detail
-- ----------------------------
DROP TABLE IF EXISTS `xhlf_agent_account_detail`;
CREATE TABLE `xhlf_agent_account_detail`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '流水号',
  `xhlf_activity_order_id` int(11) NOT NULL COMMENT 'xhlf_activity_order.id',
  `active_order` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '激活订单号',
  `current_cycle` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '考核周期',
  `agent_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '代理商编号',
  `agent_level` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '代理商级别',
  `parent_agent_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上级代理商编号',
  `amount` decimal(15, 2) NULL DEFAULT NULL COMMENT '奖励金额',
  `account_status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '奖励入账状态,0:未入账,1:已入账,-1:未开始,还没进到入账逻辑',
  `account_time` timestamp(0) NULL DEFAULT NULL COMMENT '奖励入账时间',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `operator` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作人',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '数据最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `xhlf_activity_order_id_ind`(`xhlf_activity_order_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '新欢乐返代理商入账明细' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for xhlf_merchant_trans_total_day
-- ----------------------------
DROP TABLE IF EXISTS `xhlf_merchant_trans_total_day`;
CREATE TABLE `xhlf_merchant_trans_total_day`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `merchant_no` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号',
  `total_day` date NULL DEFAULT NULL COMMENT '日期',
  `total_amount` decimal(15, 2) NULL DEFAULT NULL COMMENT '累计金额',
  `type` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '统计交易表的金额,目的是给:1.xhlf_activity_order累计金额,2.xhlf_activity_merchant_order累计金额',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `merchant_no_type_total_day_uni`(`merchant_no`, `type`, `total_day`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '新欢乐返商户交易日累计表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for yc_temp
-- ----------------------------
DROP TABLE IF EXISTS `yc_temp`;
CREATE TABLE `yc_temp`  (
  `url` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for yfb_access_token
-- ----------------------------
DROP TABLE IF EXISTS `yfb_access_token`;
CREATE TABLE `yfb_access_token`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `public_account` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '公众号账号',
  `data_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'access_token,jsapi_ticket',
  `data_value` varchar(600) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据值',
  `last_time` timestamp(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 823 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for yfb_activation_code
-- ----------------------------
DROP TABLE IF EXISTS `yfb_activation_code`;
CREATE TABLE `yfb_activation_code`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `uuid_code` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '激活码',
  `unified_merchant_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户编号',
  `oem_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'oem编号',
  `root_agent_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '一级代理商编号',
  `agent_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商编号',
  `agent_node` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商节点',
  `code_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'repay' COMMENT '激活码类型,repay: 信用卡超级还款',
  `status` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '激活码状态, 0:入库, 1:已分配, 2:已激活',
  `can_use` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '是否可使用  0不可使用  1可使用',
  `activate_time` timestamp(0) NULL DEFAULT NULL COMMENT '激活时间',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uuid_code_ind`(`uuid_code`) USING BTREE,
  INDEX `user_no_ind`(`unified_merchant_no`) USING BTREE,
  INDEX `agent_no_ind`(`agent_no`) USING BTREE,
  INDEX `agent_node_ind`(`agent_node`) USING BTREE,
  INDEX `oem_no_ind`(`oem_no`) USING BTREE,
  INDEX `root_agent_no_ind`(`root_agent_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100000001018 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '激活码信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for yfb_balance
-- ----------------------------
DROP TABLE IF EXISTS `yfb_balance`;
CREATE TABLE `yfb_balance`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `balance_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '钱包账户编号',
  `mer_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '业务商户号',
  `status` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '1' COMMENT '状态  0锁定  1正常',
  `balance_channel` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '余额通道类型  代付余额：DaiFu ',
  `balance` decimal(20, 2) NOT NULL DEFAULT 0.00 COMMENT '今日余额',
  `balance1` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '历史余额',
  `encrypt_total_balance` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '校验值',
  `freeze_amount` decimal(20, 2) NULL DEFAULT NULL COMMENT '冻结金额',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后更新时间',
  `row_lock` int(11) NULL DEFAULT 0 COMMENT '行锁标识(操作次数)',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `no_ind`(`balance_no`) USING BTREE,
  UNIQUE INDEX `com_ind`(`mer_no`, `balance_channel`) USING BTREE,
  INDEX `mer_ind`(`mer_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 77 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '钱包余额表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for yfb_balance_his
-- ----------------------------
DROP TABLE IF EXISTS `yfb_balance_his`;
CREATE TABLE `yfb_balance_his`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `his_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '余额流水记录编号',
  `mer_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '业务商户号',
  `balance_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '钱包账户编号',
  `amount_front` decimal(20, 2) NOT NULL COMMENT '变动前总余额',
  `amount` decimal(20, 2) NOT NULL COMMENT '变动余额',
  `amount_behind` decimal(20, 2) NOT NULL COMMENT '变动后总余额',
  `trans_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '变动类型  IN钱包入账   OUT钱包出账',
  `source` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '变动来源  repay:超级还',
  `balance_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '操作余额类型',
  `service` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '变动来源业务   0：交易  1：提现  2：冲正  3：调账   4：自动结算 ',
  `service_order_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '变动来源业务单号',
  `result_status` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '结果状态  0:初始化   1:成功   2:失败',
  `result_msg` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '结果信息',
  `remark` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `behind_service_status` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '后置业务处理状态  0无需处理  1成功  2失败',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uni_ind`(`source`, `service`, `service_order_no`) USING BTREE,
  UNIQUE INDEX `his_no_ind`(`his_no`) USING BTREE,
  INDEX `mer_ind`(`mer_no`) USING BTREE,
  INDEX `no_ind`(`balance_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 738 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '钱包流水表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for yfb_balance_his_seq
-- ----------------------------
DROP TABLE IF EXISTS `yfb_balance_his_seq`;
CREATE TABLE `yfb_balance_his_seq`  (
  `id` bigint(25) NOT NULL AUTO_INCREMENT,
  `create_time` timestamp(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 735 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '余额流水编号自增序列表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for yfb_balance_seq
-- ----------------------------
DROP TABLE IF EXISTS `yfb_balance_seq`;
CREATE TABLE `yfb_balance_seq`  (
  `id` bigint(25) NOT NULL AUTO_INCREMENT,
  `create_time` timestamp(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 317 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '余额编号自增序列表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for yfb_bind_card_record
-- ----------------------------
DROP TABLE IF EXISTS `yfb_bind_card_record`;
CREATE TABLE `yfb_bind_card_record`  (
  `id` int(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `bind_no` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '绑卡编号',
  `acq_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '绑卡通道',
  `merchant_no` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户号',
  `card_no` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '卡片编号',
  `account_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '开户账号',
  `mobile_no` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '手机号',
  `business_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '个人身份证号',
  `account_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '开户姓名',
  `resp_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '应答码',
  `resp_msg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '应答描述',
  `create_time` timestamp(0) NOT NULL COMMENT '创建时间',
  `last_update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后更新时间',
  `bak1` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备用1',
  `bak2` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备用2',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UNIQUE_BIND_NO`(`bind_no`) USING BTREE,
  INDEX `KEY_MERCHANT_NO`(`merchant_no`) USING BTREE,
  INDEX `KEY_CARD_NO`(`card_no`) USING BTREE,
  INDEX `KEY_ACCOUNT_NO`(`account_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 159 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '银联二维码绑卡记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for yfb_card_manage
-- ----------------------------
DROP TABLE IF EXISTS `yfb_card_manage`;
CREATE TABLE `yfb_card_manage`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `card_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '卡片编号',
  `account_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '开户账号',
  `account_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '开户名',
  `business_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '个人身份证号/企业信用代码',
  `card_type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '卡类型（借记卡DEBIT/贷记卡CREDIT）',
  `account_type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '账户类型（2对公、1对私）',
  `bank_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '清算行号',
  `bank_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银行名称',
  `bank_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银行代码',
  `cnaps` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联行行号',
  `account_province` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '开户行地区：省',
  `account_city` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '开户行地区：市',
  `account_district` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '开户行地区：区',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `statement_date` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账单日',
  `repayment_date` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '还款日',
  `yhkzm_url` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银行卡正面URL',
  `zh_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '支行',
  `last_update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `mobile_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银行留的手机号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_card_no`(`card_no`) USING BTREE,
  UNIQUE INDEX `unique_account_no`(`account_no`) USING BTREE,
  INDEX `business_code_key`(`business_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 44934 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '统一卡片表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for yfb_card_manage_log
-- ----------------------------
DROP TABLE IF EXISTS `yfb_card_manage_log`;
CREATE TABLE `yfb_card_manage_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `id_card_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '身份证号',
  `account_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '账号',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `card_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '卡编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 197 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for yfb_channel_bank_info
-- ----------------------------
DROP TABLE IF EXISTS `yfb_channel_bank_info`;
CREATE TABLE `yfb_channel_bank_info`  (
  `id` int(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `bank_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银行名称',
  `bank_no` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联行号',
  `bank_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '开户行代码',
  `channel` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '通道',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UNIQUE_CHANNEL_BANK_NO`(`channel`, `bank_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 41 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '通道银行信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for yfb_charge_back
-- ----------------------------
DROP TABLE IF EXISTS `yfb_charge_back`;
CREATE TABLE `yfb_charge_back`  (
  `id` bigint(25) UNSIGNED NOT NULL AUTO_INCREMENT,
  `order_no` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '统一冲正编号',
  `mer_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户类型 A：代理商  M:商户',
  `mer_no` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户号',
  `service` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '冲正业务类型编码  repayPlan:还款  withdraw:提现 等等',
  `service_order_no` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '业务订单号',
  `amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '冲正金额',
  `status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '状态  0-初始化 1-冲正成功 2-冲正失败 3-冲正未知',
  `back_remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `service_time` timestamp(0) NULL DEFAULT NULL COMMENT '业务处理时间',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `order_no_ind`(`order_no`) USING BTREE,
  INDEX `mer_no_ind`(`mer_no`) USING BTREE,
  INDEX `service_order_no_ind`(`service_order_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10243 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '冲正表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for yfb_client_ver_control
-- ----------------------------
DROP TABLE IF EXISTS `yfb_client_ver_control`;
CREATE TABLE `yfb_client_ver_control`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `platform` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '平台  android/ios',
  `version` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最新版本',
  `app_url` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '下载更新链接',
  `update_flag` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新标致，0：不需要,1:需要更新,2：需要强制下载',
  `oem_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'oem编号',
  `lowest_version` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最低版本',
  `update_remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新描述',
  `create_time` timestamp(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 122 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '客户端版本控制' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for yfb_credit_card
-- ----------------------------
DROP TABLE IF EXISTS `yfb_credit_card`;
CREATE TABLE `yfb_credit_card`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bank_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银行代码',
  `bank_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银行名称',
  `channel_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'ZM',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 52 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for yfb_credit_repay_notice
-- ----------------------------
DROP TABLE IF EXISTS `yfb_credit_repay_notice`;
CREATE TABLE `yfb_credit_repay_notice`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `notice_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '通告编号',
  `title` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标题',
  `link` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '链接',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` timestamp(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  `issued_time` timestamp(0) NULL DEFAULT NULL COMMENT '下发时间',
  `status` int(1) NULL DEFAULT 1 COMMENT '状态：1-待下发，2-正常',
  `one_level_agent_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'default' COMMENT '公告所属一级代理商商户  default为对所有代理商商户生效',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 447 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '行用卡还款公告信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for yfb_exception_plan
-- ----------------------------
DROP TABLE IF EXISTS `yfb_exception_plan`;
CREATE TABLE `yfb_exception_plan`  (
  `id` int(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `batch_no` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '异常计划批次号',
  `status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '处理状态 1：已处理',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UNIQUE_BATCH_NO`(`batch_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7297 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '异常计划批次号' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for yfb_exclude_card
-- ----------------------------
DROP TABLE IF EXISTS `yfb_exclude_card`;
CREATE TABLE `yfb_exclude_card`  (
  `id` int(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `bank_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银行代码',
  `bank_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银行名称',
  `channel_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '通道编码',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UNIQUE_BANK_CHANNEL`(`bank_code`, `channel_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 106 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '通道不支持卡列表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for yfb_extraction_his
-- ----------------------------
DROP TABLE IF EXISTS `yfb_extraction_his`;
CREATE TABLE `yfb_extraction_his`  (
  `id` bigint(25) UNSIGNED NOT NULL AUTO_INCREMENT,
  `order_no` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '提现编号',
  `mer_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户类型 A：代理商  M:商户',
  `mer_no` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户号',
  `card_no` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '提现卡片编号',
  `acc_no` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账号',
  `acc_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '户名',
  `mobile_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '提现金额(扣手续费前金额)',
  `fee` decimal(10, 2) NULL DEFAULT NULL COMMENT '手续费',
  `status` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '状态  0-初始化 1-提现中 2-提现成功 3-提现失败',
  `result_msg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '提现结果描述',
  `is_reverse` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '是否需要冲正1 需要 0 不需要',
  `reverse_status` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '冲正状态 1 冲正成功，2 冲正失败  ',
  `order_data` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单数据',
  `trade_source` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'default' COMMENT '来源 默认路由default',
  `create_time` timestamp(0) NOT NULL COMMENT '创建时间',
  `last_update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `order_no_ind`(`order_no`) USING BTREE,
  INDEX `mer_no_ind`(`mer_no`) USING BTREE,
  INDEX `card_no_ind`(`card_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 544 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '提现表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for yfb_mer_temp
-- ----------------------------
DROP TABLE IF EXISTS `yfb_mer_temp`;
CREATE TABLE `yfb_mer_temp`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `merchant_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '还款项目商户号',
  `msg` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'msg',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_merchant_no`(`merchant_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 34 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商户号临时表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for yfb_mer_token_info
-- ----------------------------
DROP TABLE IF EXISTS `yfb_mer_token_info`;
CREATE TABLE `yfb_mer_token_info`  (
  `id` int(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `merchant_no` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号',
  `acq_code` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '通道名称',
  `card_no` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '卡片编号',
  `token` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户token',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `bak1` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备用1',
  `bak2` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备用2',
  `settle_card_status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '是否结算卡 0否  1是',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UNIQUE_MER_ACQ_CARD`(`merchant_no`, `acq_code`, `card_no`) USING BTREE,
  INDEX `KEY_MERCHANT_NO`(`merchant_no`) USING BTREE,
  INDEX `KEY_CARD_NO`(`card_no`) USING BTREE,
  INDEX `KEY_CREATE_TIME`(`create_time`) USING BTREE,
  INDEX `TOKEN`(`token`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 247 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商户token信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for yfb_model_msg
-- ----------------------------
DROP TABLE IF EXISTS `yfb_model_msg`;
CREATE TABLE `yfb_model_msg`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `template_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '模板id',
  `open_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '接收者openId',
  `first` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '第一行字',
  `keyword1` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `keyword2` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `keyword3` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `keyword4` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `remark` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `desc_url` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '点击跳转的地址',
  `errcode` int(11) NULL DEFAULT NULL COMMENT '结果状态',
  `errmsg` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '结果信息',
  `msgid` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发送信息id',
  `status` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否送达成功通知',
  `create_time` timestamp(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `open_msg_id_index`(`open_id`, `msgid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6436 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '微信模板消息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for yfb_oem_service
-- ----------------------------
DROP TABLE IF EXISTS `yfb_oem_service`;
CREATE TABLE `yfb_oem_service`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `oem_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'OEM编号',
  `oem_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'OEM名称',
  `agent_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '代理商编号',
  `oem_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'OEM类型  repay超级还',
  `trade_fee_rate` decimal(10, 6) NULL DEFAULT 0.000000 COMMENT 'OEM服务商商户交易费率',
  `trade_single_fee` decimal(10, 2) NULL DEFAULT 0.00 COMMENT 'OEM服务商商户交易单笔手续费',
  `withdraw_fee_rate` decimal(10, 6) NULL DEFAULT 0.000000 COMMENT 'OEM服务商商户提现费率',
  `withdraw_single_fee` decimal(10, 2) NULL DEFAULT 0.00 COMMENT 'OEM服务商商户提现单笔手续费',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `company_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主体编号',
  `company_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主体名称',
  `service_phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主体客服电话',
  `team_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组织ID',
  `android_app_key` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '极光推送android key',
  `android_master_secret` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '极光推送android secret',
  `ios_app_key` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '极光推送ios key',
  `ios_master_secret` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '极光推送ios secret',
  `full_repay_fee_rate` decimal(10, 6) NULL DEFAULT 0.026000 COMMENT 'OEM服务商商户全额还款交易费率',
  `full_repay_single_fee` decimal(10, 2) NULL DEFAULT 1.00 COMMENT 'OEM服务商商户交易全额还款单笔手续费',
  `white_card_team_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '小蜜白卡组织',
  `perfect_repay_fee_rate` decimal(10, 6) NULL DEFAULT 0.026000 COMMENT 'OEM服务商商户完美还款交易费率',
  `perfect_repay_single_fee` decimal(10, 2) NULL DEFAULT 1.00 COMMENT 'OEM服务商商户交易完美还款单笔手续费',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ind`(`agent_no`, `oem_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 178 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'oem信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for yfb_oem_template
-- ----------------------------
DROP TABLE IF EXISTS `yfb_oem_template`;
CREATE TABLE `yfb_oem_template`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `oem_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'OEM编号',
  `template_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '模板类型 repay：还款 consume：消费 exception：还款异常 finish：还款完成 other:其他',
  `template_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '模板ID',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `remark` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `repay_type` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '计划类型	1：分期还款，2：全额还款',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'oem模板信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for yfb_operate_log
-- ----------------------------
DROP TABLE IF EXISTS `yfb_operate_log`;
CREATE TABLE `yfb_operate_log`  (
  `id` bigint(25) NOT NULL AUTO_INCREMENT,
  `operator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作人',
  `operate_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作业务（可存方法名）',
  `operate_table` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作表',
  `pre_value` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '操作前的值',
  `after_value` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '操作后的值',
  `operate_detail` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '日志操作详细',
  `operate_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  `operate_from` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '日志操作来源',
  `be_operator` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '被操作者',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `operator_ind`(`operator`) USING BTREE,
  INDEX `code_ind`(`operate_code`) USING BTREE,
  INDEX `ind_be_operator`(`be_operator`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 445 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '操作日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for yfb_order_seq
-- ----------------------------
DROP TABLE IF EXISTS `yfb_order_seq`;
CREATE TABLE `yfb_order_seq`  (
  `id` bigint(25) NOT NULL AUTO_INCREMENT,
  `create_time` timestamp(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5980 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '订单号自增序列表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for yfb_pay_bank_merchant
-- ----------------------------
DROP TABLE IF EXISTS `yfb_pay_bank_merchant`;
CREATE TABLE `yfb_pay_bank_merchant`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `bank_code` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '出款渠道编码标识',
  `bank_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '出款渠道名称',
  `bank_merchant_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '渠道下发的出款商户号(出款账户)',
  `bank_merchant_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '渠道下发的出款商户名称',
  `bank_desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '渠道描述',
  `pay_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '支付请求地址',
  `query_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '查询请求地址',
  `notify_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '通知地址',
  `bank_status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '1' COMMENT '渠道状态 1.开启 2.关闭',
  `pay_business_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '渠道支付业务类型或编码',
  `query_business_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '渠道查询业务类型或编码',
  `bank_settle_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '结算类型  1.非直清 2.直清',
  `trans_type` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易类型：1.出款 2.交易',
  `encryption_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '加密方式',
  `pri_key_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '私钥路径',
  `pub_key_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公钥路径',
  `encryption_key` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '秘钥',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '出款渠道商户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for yfb_pay_channel
-- ----------------------------
DROP TABLE IF EXISTS `yfb_pay_channel`;
CREATE TABLE `yfb_pay_channel`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `channel_code` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '渠道编码',
  `channel_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '渠道名称',
  `pay_merchant_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '渠道下发的交易商户号',
  `pay_merchant_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '渠道下发的交易商户名称',
  `pay_quick_fee_rate` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '通道交易费率',
  `withdraw_single_fee` decimal(10, 2) NULL DEFAULT NULL,
  `channel_desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '渠道描述',
  `channel_status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '1' COMMENT '渠道状态 1.开启 0.关闭',
  `pri_key_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '私钥路径',
  `pub_key_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公钥路径',
  `encryption_key` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '秘钥',
  `allow_begin_time` int(5) NULL DEFAULT NULL COMMENT '还款开始时间点',
  `allow_end_time` int(5) NULL DEFAULT NULL COMMENT '还款结束时间点',
  `repay_type` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '支持还款类型 1：分期还款，2全额还款，3：完美还款',
  `percent` int(5) NULL DEFAULT 0 COMMENT '多通道开启时的百分比',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `allow_split_minute` int(5) NULL DEFAULT NULL COMMENT '相邻计划间隔分钟数',
  `allow_quick_min_amount` int(5) NULL DEFAULT NULL COMMENT '允许交易最低金额',
  `allow_quick_max_amount` int(7) NULL DEFAULT NULL COMMENT '允许交易最大金额',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `chan_mer_ind`(`channel_code`, `pay_merchant_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 55 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '交易渠道信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for yfb_pay_order
-- ----------------------------
DROP TABLE IF EXISTS `yfb_pay_order`;
CREATE TABLE `yfb_pay_order`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `merchant_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务商户号',
  `order_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易订单号',
  `account_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易卡号',
  `account_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '帐号',
  `service` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易类型编码 repayPlan-还款计划消费  ensure-保证金  perfectPlan-完美还款计划消费',
  `service_order_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易业务订单号,保证金消费存还款计划编号，还款消费存计划明细编号',
  `trans_amount` decimal(30, 2) NULL DEFAULT NULL COMMENT '交易金额',
  `trans_fee` decimal(30, 2) NULL DEFAULT NULL COMMENT '交易商户手续费',
  `trans_fee_rate` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易时的费率',
  `trans_type` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易方式  quickPay快捷',
  `acq_code` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易所走通道',
  `acq_merchant_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '在所走上游通道的商户号',
  `acq_fee_rate` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '通道手续费率',
  `acq_fee` decimal(30, 2) NULL DEFAULT NULL COMMENT '通道手续费',
  `acq_order_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '通道订单号',
  `one_level_profit` decimal(10, 2) NULL DEFAULT NULL COMMENT '一级代理商分润金额',
  `body` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` timestamp(0) NULL DEFAULT NULL,
  `trans_status` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易状态 0初始化  1交易中  2交易成功  3交易失败  4未知 ',
  `trans_time` timestamp(0) NULL DEFAULT NULL COMMENT '支付时间',
  `pay_token` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易用户信息在上游标识',
  `bank_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易银行类型',
  `res_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '应答码',
  `res_msg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '应答描述',
  `card_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易卡类型,DEBIT：借记卡标识,CREDIT：信用卡标识,CFT:零钱',
  `success_notify_num` int(2) NULL DEFAULT 0 COMMENT '成功通知业务次数',
  `have_cal_profit` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '是否已计算分润  0没有  1已计算',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `order_no`(`order_no`) USING BTREE,
  INDEX `ind_user_no`(`merchant_no`) USING BTREE,
  INDEX `create_time`(`create_time`) USING BTREE,
  INDEX `trans_status`(`trans_status`) USING BTREE,
  INDEX `yfb_service_order_no`(`service_order_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 146 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '交易订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for yfb_pay_transfer_seq
-- ----------------------------
DROP TABLE IF EXISTS `yfb_pay_transfer_seq`;
CREATE TABLE `yfb_pay_transfer_seq`  (
  `id` bigint(25) NOT NULL AUTO_INCREMENT,
  `create_time` timestamp(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 102622 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '出款自增序列表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for yfb_perfect_plan_info
-- ----------------------------
DROP TABLE IF EXISTS `yfb_perfect_plan_info`;
CREATE TABLE `yfb_perfect_plan_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单类型  plan计划   planDetail 计划明细',
  `order_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单编号，计划批次号/明细编号',
  `acq_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '通道代码',
  `acq_order_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '通道订单编号',
  `province_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '落地省名称',
  `city_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '落地市名称',
  `merchant_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '落地商户类型',
  `mcc` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '落地商户macc码',
  `create_time` timestamp(0) NULL DEFAULT NULL,
  `res_info` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '上游返回信息',
  `access_ip` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '访问IP',
  `bak1` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备用字段1',
  `bak2` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备用字段2',
  `area_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '落地区域码',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `type_no_ind`(`order_type`, `order_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 128 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '完美计划落地订单信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for yfb_plan_info
-- ----------------------------
DROP TABLE IF EXISTS `yfb_plan_info`;
CREATE TABLE `yfb_plan_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `plan_type` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '计划类型 1分期还款  2全额还款  3完美还款',
  `plan_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '计划名称',
  `status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '计划服务状态 0关闭 1开启',
  `allow_begin_time` int(5) NULL DEFAULT NULL COMMENT '计划服务允许开始时间，格式:时的整数（包含）',
  `allow_end_time` int(5) NULL DEFAULT NULL COMMENT '计划服务允许结束时间，格式:时的整数（不包含）',
  `allow_repay_min_amount` int(7) NULL DEFAULT NULL COMMENT '计划服务允许还款最小金额（包含）',
  `allow_repay_max_amount` int(7) NULL DEFAULT NULL COMMENT '计划服务允许还款最大金额（包含）',
  `allow_first_min_amount` int(7) NULL DEFAULT NULL COMMENT '计划服务允许首笔交易最小金额（包含）',
  `allow_first_max_amount` int(7) NULL DEFAULT NULL COMMENT '计划服务允许首笔交易最大金额（包含）',
  `allow_day_min_num` int(2) NULL DEFAULT NULL COMMENT '计划服务每日还款最小笔数（包含）',
  `allow_day_max_num` int(2) NULL DEFAULT NULL COMMENT '计划服务每日还款最大笔数（包含）',
  `close_tip` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '计划服务关闭提示语',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '超级还计划服务信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for yfb_profit_collection
-- ----------------------------
DROP TABLE IF EXISTS `yfb_profit_collection`;
CREATE TABLE `yfb_profit_collection`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `collection_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分润汇总编号',
  `collection_batch_no` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '汇总批次号',
  `mer_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户类型 A：代理商  M:商户',
  `mer_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号/代理商编号',
  `agent_node` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商节点',
  `service_cost_rate` decimal(10, 6) NULL DEFAULT NULL COMMENT '服务商成本费率',
  `service_cost_single_fee` decimal(10, 2) NULL DEFAULT NULL COMMENT '服务商单笔代付成本',
  `profit_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '分润金额',
  `operator` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '汇总人',
  `allow_income` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '是否需要入账 0：不需要，1：需要',
  `income_status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '入账状态  0否 1是',
  `income_time` timestamp(0) NULL DEFAULT NULL COMMENT '入账时间',
  `collection_time` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后更新时间',
  `profit_type` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '1' COMMENT '分润类型	1：超级还分期还款，2：超级还全额还款，3：保证金',
  `remark` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_profit_mer_no`(`mer_no`, `collection_time`, `profit_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '分润汇总表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for yfb_profit_detail
-- ----------------------------
DROP TABLE IF EXISTS `yfb_profit_detail`;
CREATE TABLE `yfb_profit_detail`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `profit_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分润明细编号',
  `order_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单号/计划批次号',
  `profit_mer_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分润的商户类型 A：代理商  M:商户',
  `profit_mer_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号/代理商编号',
  `trans_amount` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '交易金额',
  `trans_time` timestamp(0) NULL DEFAULT NULL COMMENT '交易时间/计划终态时间',
  `merchant_no` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '支付商户',
  `agent_node` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易商户直属代理商node',
  `share_amount` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '分润总金额',
  `share_pay_amount` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '交易分润金额',
  `share_withdraw_amount` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '代付分润金额',
  `service_cost_rate` decimal(10, 6) NULL DEFAULT NULL COMMENT '服务商成本费率',
  `service_cost_single_fee` decimal(10, 2) NULL DEFAULT NULL COMMENT '服务商单笔代付成本',
  `mer_pay_rate` decimal(10, 6) NULL DEFAULT NULL COMMENT '商户交易费率',
  `mer_single_fee` decimal(10, 2) NULL DEFAULT NULL COMMENT '商户代付单笔手续费',
  `share_rate` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易分润费率+代付分润金额，如：0.001+1',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `collection_batch_no` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '汇总批次号',
  `collection_time` timestamp(0) NULL DEFAULT NULL COMMENT '汇总时间',
  `profit_type` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '1' COMMENT '分润类型	1：超级还分期还款，2：超级还全额还款，3：保证金分润，4：完美还款',
  `to_profit_amount` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '产生分润的金额（保证金或者计划成功消费金额）',
  `bak1` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '预留字段1',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `merchant_no_ind`(`merchant_no`) USING BTREE,
  INDEX `profit_no_ind`(`profit_no`) USING BTREE,
  INDEX `order_no_ind`(`order_no`) USING BTREE,
  INDEX `profit_mer_no_ind`(`profit_mer_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 117503 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '分润明细记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for yfb_public_account
-- ----------------------------
DROP TABLE IF EXISTS `yfb_public_account`;
CREATE TABLE `yfb_public_account`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `public_account` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '公众号账号',
  `public_account_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '公众号名称',
  `appid` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '公众号appid',
  `secret` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '公众号secret',
  `encoding_aes_key` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公众号密文',
  `token` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '公众号token',
  `oem_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '公众号所属oem编号',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `app_appid` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `app_secret` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '公众号信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for yfb_repay_merchant_info
-- ----------------------------
DROP TABLE IF EXISTS `yfb_repay_merchant_info`;
CREATE TABLE `yfb_repay_merchant_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `merchant_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '还款项目商户号',
  `openid` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `card_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '结算卡卡片编号',
  `trans_rate` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易费率',
  `one_agent_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '一级代理商编号',
  `agent_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属代理商编号',
  `agent_node` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属代理商节点',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `user_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `id_card_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '身份证号',
  `mobile_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号码',
  `mer_account` int(2) NULL DEFAULT 0 COMMENT '商户是否已在账户(1为是0为否)',
  `last_update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '商户状态 0正常  1不允许建立计划',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `openid`(`openid`) USING BTREE,
  UNIQUE INDEX `unique_merchant_no`(`merchant_no`) USING BTREE,
  INDEX `id_card_no_key`(`id_card_no`) USING BTREE,
  INDEX `card_no_key`(`card_no`) USING BTREE,
  INDEX `mobile_no_key`(`mobile_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 200 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '还款项目商户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for yfb_repay_plan
-- ----------------------------
DROP TABLE IF EXISTS `yfb_repay_plan`;
CREATE TABLE `yfb_repay_plan`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `merchant_no` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '业务商户号',
  `batch_no` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '批次号',
  `card_no` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '卡片编号',
  `repay_amount` decimal(10, 2) NOT NULL COMMENT '还款总金额',
  `repay_begin_time` timestamp(0) NOT NULL COMMENT '还款开始时间',
  `repay_end_time` timestamp(0) NOT NULL COMMENT '还款结束时间',
  `ensure_amount_rate` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '保证金比例',
  `ensure_amount` decimal(10, 2) NOT NULL COMMENT '分期保证金或完美第一笔交易金额',
  `status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '计划状态 0：初始化（未生成还款明细），1：未执行（还没执行还款明细），2：还款中，3：还款成功，4：还款失败，5:挂起 ，6：终止，7：逾期待还',
  `repay_fee_rate` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '手续费扣除比例+固定费用，如0.006+2',
  `repay_fee` decimal(10, 2) NOT NULL COMMENT '手续费',
  `repay_num` int(3) NULL DEFAULT NULL COMMENT '计划任务还款次数',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `row_lock` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '行锁因子',
  `expect_status` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '预处理状态 0终止',
  `settle_status` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '清算状态  0未清算  1已清算',
  `settle_order_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '清算单号',
  `settle_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '清算金额',
  `create_time` timestamp(0) NOT NULL COMMENT '创建时间',
  `last_update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后更新时间',
  `acq_code` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易所走通道',
  `acq_merchant_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '在所走上游通道的商户号',
  `complete_time` timestamp(0) NULL DEFAULT NULL COMMENT '计划变为终态时间',
  `success_repay_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '已成功还款总金额',
  `success_pay_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '已成功消费总金额',
  `success_repay_num` int(3) NULL DEFAULT 0 COMMENT '已成功还款总笔数',
  `actual_pay_fee` decimal(10, 2) NULL DEFAULT NULL COMMENT '商户实际交易手续费',
  `actual_withdraw_fee` decimal(10, 2) NULL DEFAULT NULL COMMENT '商户实际代付手续费',
  `have_cal_profit` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '是否已计算分润   0没有  1已计算',
  `repay_type` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '1' COMMENT '计划类型	1：分期还款，2：全额还款，3：完美还款',
  `res_msg` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '返回消息',
  `service_fee_rate` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '代理商交易费率成本链条，json字符串',
  `service_single_fee` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '代理商单笔代付手续费链条，json字符串',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `batch_no_ind`(`batch_no`) USING BTREE,
  INDEX `card_no_ind`(`card_no`) USING BTREE,
  INDEX `merchant_no_ind`(`merchant_no`) USING BTREE,
  INDEX `row_lock_key`(`row_lock`) USING BTREE,
  INDEX `index_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1099361 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '还款计划表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for yfb_repay_plan_detail
-- ----------------------------
DROP TABLE IF EXISTS `yfb_repay_plan_detail`;
CREATE TABLE `yfb_repay_plan_detail`  (
  `id` int(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `merchant_no` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '业务商户号',
  `card_no` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '卡片编号',
  `batch_no` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '计划批次号',
  `plan_no` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '计划编号',
  `plan_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '计划类型 IN：给用户还款，即代付，OUT：用户消费，即快捷支付',
  `plan_time` timestamp(0) NOT NULL COMMENT '计划时间',
  `plan_index` int(5) NOT NULL COMMENT '计划序次号 同一计划批次下面的计划任务，序次号自增',
  `plan_count` int(2) NOT NULL DEFAULT 0 COMMENT '计划已执行的次数',
  `plan_amount` decimal(10, 2) NOT NULL COMMENT '计划执行金额',
  `plan_status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '计划执行状态 0：未执行，1：执行中， 2：执行成功，3：执行失败',
  `res_msg` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '响应信息',
  `row_lock` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '行锁因子',
  `create_time` timestamp(0) NOT NULL COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT NULL COMMENT '最后更新时间',
  `bak1` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备用1',
  `bak2` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备用2',
  `acq_code` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易所走通道',
  `acq_merchant_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '在所走上游通道的商户号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UNIQUE_PLAN_NO`(`plan_no`) USING BTREE,
  INDEX `KEY_MERCHANT_NO`(`merchant_no`) USING BTREE,
  INDEX `KEY_CARD_NO`(`card_no`) USING BTREE,
  INDEX `KEY_PLAN_TIME`(`plan_time`) USING BTREE,
  INDEX `KEY_PLAN_STATUS`(`plan_status`) USING BTREE,
  INDEX `KEY_CREATE_TIME`(`create_time`) USING BTREE,
  INDEX `KEY_ROW_LOCK`(`row_lock`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8141 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '还款明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for yfb_schedule_job
-- ----------------------------
DROP TABLE IF EXISTS `yfb_schedule_job`;
CREATE TABLE `yfb_schedule_job`  (
  `id` int(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `spring_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'Spring容器管理的Bean名称',
  `job_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '执行的任务名称',
  `job_group` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务分组',
  `description` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务描述',
  `cron_expression` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'cron表达式',
  `bean_class` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务执行时调用哪个类的方法 包名+类名',
  `method_name` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '任务调用的方法名',
  `is_concurrent` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '相同的任务，是否允许并发运行，0：不允许，1允许',
  `job_status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否运行 0：停止，1：运行',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creator` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `update_time` timestamp(0) NULL DEFAULT NULL COMMENT '修改时间',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `bak1` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备用1',
  `bak2` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备用2',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `KEY_CREATE_TIME`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '定时任务配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for yfb_service_cost
-- ----------------------------
DROP TABLE IF EXISTS `yfb_service_cost`;
CREATE TABLE `yfb_service_cost`  (
  `agent_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代理商编号',
  `rate` decimal(10, 6) NULL DEFAULT NULL COMMENT '费率',
  `single_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '单笔固定金额',
  `service_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'repay' COMMENT 'repay: 信用卡超级还款',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后更新时间',
  `full_repay_rate` decimal(10, 6) NULL DEFAULT 0.026000 COMMENT '代理商全额还款费率',
  `full_repay_single_amount` decimal(10, 2) NULL DEFAULT 1.00 COMMENT '代理商全额还款单笔固定金额',
  `account_ratio` int(5) UNSIGNED NULL DEFAULT 100 COMMENT '分润入账比例',
  `perfect_repay_single_amount` decimal(10, 2) NULL DEFAULT 1.00 COMMENT '代理商完美还款单笔固定金额',
  `perfect_repay_rate` decimal(10, 6) NULL DEFAULT NULL COMMENT '代理商完美还款费率',
  UNIQUE INDEX `agent_no_service_type_ind`(`agent_no`, `service_type`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '代理商(服务商)服务成本' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for yfb_tally_his
-- ----------------------------
DROP TABLE IF EXISTS `yfb_tally_his`;
CREATE TABLE `yfb_tally_his`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '记账单号',
  `mer_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户类型 A：代理商  M:商户',
  `mer_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '业务商户号',
  `one_agent_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '一级代理商编号',
  `real_mer_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '真实记账商户',
  `acq_org_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '通道标识',
  `service` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '业务类型 trade-交易  withdraw-提现  profit分润  refund退款',
  `service_order_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '业务订单号',
  `tally_fee` decimal(10, 2) NULL DEFAULT NULL COMMENT '手续费',
  `tally_acq_fee` decimal(10, 2) NULL DEFAULT NULL COMMENT '收单服务费成本（所有的成本）',
  `deposit_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '押金',
  `imbalance_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '差额',
  `tally_amount` decimal(10, 2) NOT NULL COMMENT '记账金额',
  `charge_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '记账类型TRANS 交易  REVERSE 冲正  BOOKED 入账  CASH 提现 BOND 保证金',
  `tally_data` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '记账数据',
  `status` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '记账状态  0未记账 1记账中  2记账成功  3记账失败',
  `tally_result_msg` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '记账返回数据',
  `operate_num` int(2) NOT NULL DEFAULT 0 COMMENT '发起记账次数',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `tally_time` timestamp(0) NULL DEFAULT NULL COMMENT '记账时间',
  `last_update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `order_no_uni`(`order_no`) USING BTREE,
  INDEX `mer_ind`(`mer_no`) USING BTREE,
  INDEX `order_ind`(`service_order_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4676 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '业务记账表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for yfb_tally_his_temp
-- ----------------------------
DROP TABLE IF EXISTS `yfb_tally_his_temp`;
CREATE TABLE `yfb_tally_his_temp`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '记账单号',
  `mer_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户类型 A：代理商  M:商户',
  `mer_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '业务商户号',
  `one_agent_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '一级代理商编号',
  `real_mer_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '真实记账商户',
  `acq_org_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '通道标识',
  `service` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '业务类型 trade-交易  withdraw-提现  profit分润  refund退款',
  `service_order_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '业务订单号',
  `tally_fee` decimal(10, 2) NULL DEFAULT NULL COMMENT '手续费',
  `tally_acq_fee` decimal(10, 2) NULL DEFAULT NULL COMMENT '收单服务费成本（所有的成本）',
  `deposit_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '押金',
  `imbalance_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '差额',
  `tally_amount` decimal(10, 2) NOT NULL COMMENT '记账金额',
  `charge_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '记账类型TRANS 交易  REVERSE 冲正  BOOKED 入账  CASH 提现 BOND 保证金',
  `tally_data` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '记账数据',
  `status` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '记账状态  0未记账 1记账中  2记账成功  3记账失败',
  `tally_result_msg` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '记账结果信息',
  `operate_num` int(2) NOT NULL DEFAULT 0 COMMENT '发起记账次数',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `tally_time` timestamp(0) NULL DEFAULT NULL COMMENT '记账时间',
  `last_update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `order_no_uni`(`order_no`) USING BTREE,
  INDEX `mer_ind`(`mer_no`) USING BTREE,
  INDEX `order_ind`(`service_order_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 155 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '业务记账表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for yfb_transfer
-- ----------------------------
DROP TABLE IF EXISTS `yfb_transfer`;
CREATE TABLE `yfb_transfer`  (
  `id` bigint(25) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单号',
  `org_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '支付渠道',
  `org_order_no` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '渠道订单号',
  `pay_bank` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '支付通道',
  `bank_order_no` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '提交通道订单号',
  `pay_bank_order_no` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '通道订单号',
  `trade_source` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易来源',
  `out_acc_no` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `out_acc_name` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `out_settle_bank_no` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `out_bank_no` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `out_bank_name` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `bank_merchant_no` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `product_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '产品类型',
  `in_acc_no` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `in_acc_name` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `in_settle_bank_no` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `in_bank_no` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `in_bank_name` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `mobile_no` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `identity_type` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证件类型 01：身份证',
  `identity_no` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证件号',
  `amount` decimal(20, 2) NOT NULL COMMENT '付款金额(元)',
  `fee` decimal(20, 2) NULL DEFAULT 0.00 COMMENT '手续费(元)',
  `create_time` timestamp(0) NULL,
  `pay_time` timestamp(0) NULL DEFAULT NULL,
  `summary` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代付摘要',
  `pay_data` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代付数据',
  `status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '状态  0.未提交 1.提交成功 2.提交失败 3.超时 4.支付成功  5.支付失败 6.未知',
  `notify_num` int(10) NULL DEFAULT 0 COMMENT '通知次数',
  `err_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '错误码',
  `err_msg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '错误信息',
  `bak1` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备用1',
  `bak2` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备用2',
  `last_update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `order_no_key`(`order_no`) USING BTREE,
  UNIQUE INDEX `org_order_no_key`(`org_code`, `org_order_no`) USING BTREE,
  UNIQUE INDEX `bank_order_no_key`(`pay_bank`, `bank_order_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3632 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '单笔代付表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for yfb_unified_account
-- ----------------------------
DROP TABLE IF EXISTS `yfb_unified_account`;
CREATE TABLE `yfb_unified_account`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `un_account_mer_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '统一商户号',
  `unionid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公众号unionid',
  `id_card_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '身份证号码',
  `user_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `mobile_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号码',
  `login_pwd` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登录密码',
  `scsfz_url` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手持身份证URL',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `sfzzm_url` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '身份证正面URL',
  `sfzfm_url` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '身份证反面URL',
  `company_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '主体编号',
  `last_update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_un_mer_no`(`un_account_mer_no`) USING BTREE,
  UNIQUE INDEX `uni_ind`(`unionid`) USING BTREE,
  UNIQUE INDEX `unique_mobile_no_company_no`(`mobile_no`, `company_no`) USING BTREE,
  INDEX `id_card_no_key`(`id_card_no`) USING BTREE,
  INDEX `mobile_no_key`(`mobile_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 227 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '统一账户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for yfb_unified_account_card
-- ----------------------------
DROP TABLE IF EXISTS `yfb_unified_account_card`;
CREATE TABLE `yfb_unified_account_card`  (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `un_account_mer_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '统一商户号',
  `card_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '卡编号',
  `create_time` timestamp(0) NULL DEFAULT NULL,
  `pro_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '\"repay\"',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `un_acc_no`(`un_account_mer_no`, `card_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 455 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '统一商户卡关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for yfb_unified_account_product
-- ----------------------------
DROP TABLE IF EXISTS `yfb_unified_account_product`;
CREATE TABLE `yfb_unified_account_product`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `un_account_mer_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '统一账户商户号',
  `pro_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务代码(repay)',
  `pro_mer_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务商户号',
  `pro_status` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务状态(1打开0关闭)',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_un_account`(`un_account_mer_no`) USING BTREE,
  INDEX `index_pro_mer`(`pro_mer_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 242 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for yfb_unique_seq
-- ----------------------------
DROP TABLE IF EXISTS `yfb_unique_seq`;
CREATE TABLE `yfb_unique_seq`  (
  `id` bigint(25) NOT NULL AUTO_INCREMENT,
  `create_time` timestamp(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 466647 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '自增序列表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for yfb_warn_info
-- ----------------------------
DROP TABLE IF EXISTS `yfb_warn_info`;
CREATE TABLE `yfb_warn_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `warn_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '预警任务编号',
  `warn_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '预警任务名称',
  `status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '1' COMMENT '状态 0关闭 1开启',
  `warn_trigger_value` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '预警阈值',
  `warn_count_time` int(11) NULL DEFAULT NULL COMMENT '定时任务统计间隔时间（分钟数）',
  `warn_phone` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '预警短信通知人,多人以;隔开',
  `warn_msg_model` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '预警短信模板，变量以%s代替',
  `create_time` timestamp(0) NULL DEFAULT NULL,
  `last_update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `bak1` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `bak2` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '超级还预警配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for yfb_wechat_info
-- ----------------------------
DROP TABLE IF EXISTS `yfb_wechat_info`;
CREATE TABLE `yfb_wechat_info`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `openid` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `public_account` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公众号账号',
  `status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'NORMAL' COMMENT '用户状态  NORMAL正常    LOCK锁定    CLOSE关闭',
  `nickname` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `sex` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户的性别，值为1时是男性，值为2时是女性，值为0时是未知',
  `province` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户个人资料填写的省份',
  `city` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '普通用户个人资料填写的城市',
  `country` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '国家，如中国为CN',
  `headimgurl` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像URL将失效。',
  `unionid` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '将公众号绑定到微信开放平台帐号后，才会出现该字段',
  `weixinhao` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '微信号',
  `create_time` timestamp(0) NULL DEFAULT NULL,
  `is_unsubscribe` int(1) NULL DEFAULT 0 COMMENT '是否取消关注 0没有取关  1已取关',
  `unsubscribe_time` timestamp(0) NULL DEFAULT NULL COMMENT '取关时间',
  `last_update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `NewIndex1`(`openid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16607 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '微信用户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for yfb_withdraw_his
-- ----------------------------
DROP TABLE IF EXISTS `yfb_withdraw_his`;
CREATE TABLE `yfb_withdraw_his`  (
  `id` bigint(25) UNSIGNED NOT NULL AUTO_INCREMENT,
  `order_no` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '统一出账编号',
  `mer_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户类型 A：代理商  M:商户',
  `mer_no` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户号',
  `service` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '出账类型编码  repayPlan:还款  withdraw:提现 等等',
  `service_order_no` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '出账业务订单号,还款存还款计划编号，提现存提现表编号',
  `transfer_order_no` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '出款平台订单号',
  `extraction_order_no` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '提现编号',
  `card_no` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '统一卡片编号',
  `acc_no` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账号',
  `acc_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '户名',
  `mobile_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '出账金额(扣手续费前金额)',
  `fee` decimal(10, 2) NULL DEFAULT NULL COMMENT '手续费',
  `status` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '状态  0-初始化 1-出账中 2-出账成功 3-出账失败',
  `result_msg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '出账结果描述',
  `order_data` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单数据',
  `success_notify_num` int(8) NULL DEFAULT 0 COMMENT '成功通知业务次数',
  `withdraw_channel` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'default' COMMENT '出款通道 默认路由default',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `order_no_ind`(`order_no`) USING BTREE,
  INDEX `mer_no_ind`(`mer_no`) USING BTREE,
  INDEX `service_order_no_ind`(`service_order_no`) USING BTREE,
  INDEX `transfer_order_no_ind`(`transfer_order_no`) USING BTREE,
  INDEX `card_no_ind`(`card_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3321 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '统一出账表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for yfb_zq_merchant_info
-- ----------------------------
DROP TABLE IF EXISTS `yfb_zq_merchant_info`;
CREATE TABLE `yfb_zq_merchant_info`  (
  `id` int(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `report_no` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '报备编号',
  `merchant_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号',
  `zq_merchant_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银联报备商户号',
  `terminal_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上游返回终端号',
  `sync_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上游返回码',
  `report_status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '同步状态:0未同步  1:同步成功  2同步失败  3审核中',
  `effective_status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否生效 0：不生效，1：生效',
  `channel_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '通道编码',
  `sync_remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上游同步备注',
  `create_time` timestamp(0) NOT NULL COMMENT '创建时间',
  `last_update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后更新时间',
  `bak1` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '预留字段1',
  `card_no` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '统一卡片编号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `zq_merchant_no_unique`(`merchant_no`, `zq_merchant_no`) USING BTREE,
  INDEX `merInd`(`merchant_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 43 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '直清商户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for yfb_zq_sync_log
-- ----------------------------
DROP TABLE IF EXISTS `yfb_zq_sync_log`;
CREATE TABLE `yfb_zq_sync_log`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `merchant_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户号',
  `channel_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '通道编码',
  `operator` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '操作人',
  `operate_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '操作类型  report初始报备  sync修改同步',
  `operate_result` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '0失败  1成功  2未知',
  `channel_back_log` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '同步上游返回结果信息',
  `create_time` timestamp(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `noInd`(`merchant_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 37 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '直清商户报备/同步日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for yl_temp
-- ----------------------------
DROP TABLE IF EXISTS `yl_temp`;
CREATE TABLE `yl_temp`  (
  `url` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ys_0604
-- ----------------------------
DROP TABLE IF EXISTS `ys_0604`;
CREATE TABLE `ys_0604`  (
  `id` int(11) NOT NULL DEFAULT 0,
  `ter_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `sn` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `mer_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `type` int(11) NULL DEFAULT 0,
  `status` int(11) NULL DEFAULT 0
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ys_bank_code
-- ----------------------------
DROP TABLE IF EXISTS `ys_bank_code`;
CREATE TABLE `ys_bank_code`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `bankcode` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '行号',
  `banktype` varchar(9) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '平台银行行别',
  `bankname` varchar(150) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银行名称',
  `citycode` varchar(4) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '城市代码',
  `superior` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上级行号',
  `view_banktype` varchar(9) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '筛选行号所使用行别，与行别表（BANK_TYPE_INFO）中KEYWORD配合使用',
  `onepoint` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '一点接入行行号',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `bank_code`(`bankcode`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 142450 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ys_card_bin
-- ----------------------------
DROP TABLE IF EXISTS `ys_card_bin`;
CREATE TABLE `ys_card_bin`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `bank_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '银行行别',
  `bank_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '银行名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1147 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '银盛银行行别表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ys_card_sync
-- ----------------------------
DROP TABLE IF EXISTS `ys_card_sync`;
CREATE TABLE `ys_card_sync`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `unionpay_mer_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `account_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `account_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `bank_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `bank_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `bank_province` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `bank_city` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `bank_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0',
  `response_msg` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `batch_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `from_sys` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12694965 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ys_merchant_import
-- ----------------------------
DROP TABLE IF EXISTS `ys_merchant_import`;
CREATE TABLE `ys_merchant_import`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `merchant_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户名称',
  `address` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地址',
  `lawyer` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '法人姓名',
  `mbp_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户进件编号',
  `id_card_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '身份证号',
  `province` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '省',
  `city` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '市',
  `district` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '区',
  `mobilephone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `account_no` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '卡号',
  `account_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '卡号名称',
  `bank_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '支行名称',
  `bank_code` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '支行联行行号',
  `bank_type` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '大行行别',
  `account_province` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '开户省',
  `account_city` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '开户市',
  `unionpay_mer_no` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `terminal_no` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `trm_sn` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `regid` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '统一信息编码',
  `batch_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `error_msg` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '失败原因',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `batch_no`(`batch_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13181160 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for yx_mer
-- ----------------------------
DROP TABLE IF EXISTS `yx_mer`;
CREATE TABLE `yx_mer`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `merchant_no` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '平台商户号',
  `acq_merchant_type` int(11) NULL DEFAULT NULL COMMENT '优享类型 经营范围',
  `times` int(11) NULL DEFAULT 0 COMMENT '更新次数（以天为单位)',
  `times_select` int(11) NULL DEFAULT 1 COMMENT '勾选次数（单位:次)',
  `times_selct_date` date NULL DEFAULT NULL COMMENT '最后勾选日期',
  `update_date` date NULL DEFAULT NULL COMMENT '更新经营范围日期',
  `lost_time` datetime(0) NULL DEFAULT NULL COMMENT '收单商户失效时间',
  `acq_merchant_no` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收单商户号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `merchant_no_indx`(`merchant_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '优享计划表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for zf_mpos_city_map
-- ----------------------------
DROP TABLE IF EXISTS `zf_mpos_city_map`;
CREATE TABLE `zf_mpos_city_map`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `PROVINCE` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `GD_CITY` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `GD_DITRICT` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `GD_AD_CODE` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `GD_CITY_CODE` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `UN_CITY` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `UN_DITRICT` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `UN_CODE` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11051 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '行政区码对照表（中付给的）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for zftp_error
-- ----------------------------
DROP TABLE IF EXISTS `zftp_error`;
CREATE TABLE `zftp_error`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `item_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'ID',
  `merchant_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '1为商户,2为进件项',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 73 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '中付图片下载异常信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for zjx_check_bill
-- ----------------------------
DROP TABLE IF EXISTS `zjx_check_bill`;
CREATE TABLE `zjx_check_bill`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `batch_no` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '对账批次号',
  `insurer` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '承保人，保险单位 见数据字典：insurer_number',
  `order_type` int(1) NOT NULL COMMENT '订单类型：1 投保 2 退保',
  `acq_total_amount` decimal(11, 2) NOT NULL COMMENT '上游含税保费总金额',
  `acq_total_count` int(11) NOT NULL COMMENT '上游对账文件总笔数',
  `acq_success_count` int(11) NOT NULL COMMENT '上游对账文件成功笔数',
  `acq_fail_count` int(11) NOT NULL COMMENT '上游对账文件失败笔数',
  `sys_total_amount` decimal(11, 2) NOT NULL COMMENT '平台含税保费总金额-成本价',
  `sys_total_count` int(11) NOT NULL COMMENT '平台交易总笔数',
  `sys_success_count` int(11) NOT NULL COMMENT '平台对账成功总笔数',
  `sys_fail_count` int(11) NOT NULL COMMENT '平台对账失败总笔数',
  `check_status` int(11) NOT NULL COMMENT '对账结果： 0 初始化 1成功 2失败',
  `file_date` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '对账文件日期',
  `check_time` datetime(0) NOT NULL COMMENT '对账时间',
  `file_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '对账文件名称',
  `create_person` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '操作员，创建人',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `batch_no_index`(`batch_no`) USING BTREE,
  INDEX `create_time_index`(`create_time`) USING BTREE,
  INDEX `order_type_index`(`order_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 28 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '对账信息汇总表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for zjx_check_bill_entry
-- ----------------------------
DROP TABLE IF EXISTS `zjx_check_bill_entry`;
CREATE TABLE `zjx_check_bill_entry`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `batch_no` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对账批次号',
  `insurer` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '承保人，保险单位 见数据字典：insurer_number',
  `order_type` int(1) NOT NULL COMMENT '订单类型：1 投保 2 退保',
  `product_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '产品编码',
  `holder` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '投保人',
  `acq_order_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '上游保险订单号',
  `sys_order_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '平台保险订单号',
  `acq_amount` decimal(11, 2) NOT NULL COMMENT '上游渠道含税保费',
  `sys_amount` decimal(11, 2) NOT NULL COMMENT '平台含税保费',
  `acq_bill_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '上游保单号',
  `sys_bill_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '平台保单号',
  `insure_amount` decimal(11, 2) NOT NULL COMMENT '保额',
  `trans_status` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `insure_status` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `acq_bill_status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上游保单状态',
  `sys_bill_status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '平台保单状态',
  `insure_time` timestamp(0) NULL DEFAULT NULL COMMENT '投保时间',
  `bill_time` timestamp(0) NULL DEFAULT NULL COMMENT '保单生成日期',
  `effective_stime` timestamp(0) NULL DEFAULT NULL COMMENT '保险起期',
  `effective_etime` timestamp(0) NULL DEFAULT NULL COMMENT '保险止期',
  `check_status` int(1) NOT NULL DEFAULT 5 COMMENT '对账状态： 1 核对成功、2 上游单边、3 平台单边，4 金额不符、5 未核对',
  `report_status` int(1) NOT NULL DEFAULT 2 COMMENT '汇总状态： 1 已汇总、2 未汇总',
  `create_person` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '操作员，创建人',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `one_agent_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '一级代理商编号',
  `report_batch_no` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '汇总批次号',
  `bill_amount` decimal(11, 2) NULL DEFAULT NULL COMMENT '平台售价',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `bill_time_index`(`bill_time`) USING BTREE,
  INDEX `batch_no_index`(`batch_no`) USING BTREE,
  INDEX `order_type_index`(`order_type`) USING BTREE,
  INDEX `check_status_index`(`check_status`) USING BTREE,
  INDEX `insure_status_index`(`insure_status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 54 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '对账详情表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for zjx_safe_config
-- ----------------------------
DROP TABLE IF EXISTS `zjx_safe_config`;
CREATE TABLE `zjx_safe_config`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `pro_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '产品编码',
  `pro_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '产品名称',
  `t0_time` int(11) NULL DEFAULT NULL COMMENT 'T0约定到账时间(单位:小时)',
  `t1_time` int(11) NULL DEFAULT NULL COMMENT 'T1约定到账时间(单位:小时)',
  `phone` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '理赔电话',
  `status` int(2) NULL DEFAULT 0 COMMENT 'App默认勾选状态0否1是',
  `agent_share` decimal(20, 2) NULL DEFAULT NULL COMMENT '代理商分润(只对一级代理商生效,单位%)',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_update_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_pro_code`(`pro_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 212 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '资金保险配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for zjx_safe_ladder
-- ----------------------------
DROP TABLE IF EXISTS `zjx_safe_ladder`;
CREATE TABLE `zjx_safe_ladder`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `title` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标题',
  `pro_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '产品编码',
  `min_num` decimal(20, 2) NULL DEFAULT NULL COMMENT '阶梯最小值',
  `max_num` decimal(20, 2) NULL DEFAULT NULL COMMENT '阶梯最大值,-1标识无穷大',
  `safe_quota` decimal(20, 2) NULL DEFAULT NULL COMMENT '保额-资金安全限额',
  `cost` decimal(20, 2) NULL DEFAULT NULL COMMENT '单笔保费成本',
  `price` decimal(20, 2) NULL DEFAULT NULL COMMENT '单笔保费售价(元)',
  `sort` int(11) NULL DEFAULT NULL COMMENT '阶梯等级',
  `ins_safe_quota` decimal(20, 2) NULL DEFAULT NULL COMMENT '资金安全限额部分',
  `delay_quota` decimal(20, 2) NULL DEFAULT NULL COMMENT '延迟到帐部分',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_update_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_pro_code`(`pro_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 216 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '资金保险阶梯' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for zjx_share_report
-- ----------------------------
DROP TABLE IF EXISTS `zjx_share_report`;
CREATE TABLE `zjx_share_report`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `batch_no` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '汇总批次号',
  `bill_month` varchar(7) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '保单创建月份',
  `one_agent_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '一级代理商编号',
  `total_amount` decimal(11, 2) NOT NULL COMMENT '保费总额',
  `total_count` int(11) NOT NULL COMMENT '保单总数',
  `share_rate` decimal(5, 2) NOT NULL COMMENT '代理商分润百分比',
  `share_amount` decimal(11, 2) NOT NULL COMMENT '代理商分润金额',
  `account_status` int(1) NOT NULL DEFAULT 3 COMMENT '入账状态： 1 入账成功、2 入账失败 3 未入账',
  `account_time` datetime(0) NULL DEFAULT NULL COMMENT '入账时间',
  `create_person` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '操作员，创建人',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `batch_no_index`(`batch_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '代理商分润月表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for zjx_trans_order
-- ----------------------------
DROP TABLE IF EXISTS `zjx_trans_order`;
CREATE TABLE `zjx_trans_order`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `bx_order_no` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '保险订单号',
  `order_no` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易订单号(外键交易表)',
  `third_order_no` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '保单号(合作方的订单号)',
  `merchant_no` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号(外键商户表)',
  `one_agent_no` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '一级代理商编号',
  `bx_unit` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '承保单位(目前只有前海财险)',
  `prod_no` int(11) NULL DEFAULT NULL COMMENT '产品编码(前海财险提供的)',
  `prod_detail` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '产品描述',
  `n_amt` decimal(10, 2) NULL DEFAULT NULL COMMENT '保额(元，保留两位小数)',
  `n_prm` decimal(10, 2) NULL DEFAULT NULL COMMENT '保费-售价(元，保留两位小数)',
  `n_fee` decimal(10, 2) NULL DEFAULT NULL COMMENT '保费-成本价',
  `bx_type` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '投保状态:SUCCESS：成功,FAILED：失败,INIT：初始化,OVERLIMIT：已退保,RECEDEFAILED:退保失败',
  `result_msg` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '结果信息',
  `t_time` timestamp(0) NULL DEFAULT NULL COMMENT '保单生成时间',
  `t_begin_time` datetime(0) NULL DEFAULT NULL COMMENT '保险起期',
  `t_end_time` datetime(0) NULL DEFAULT NULL COMMENT '保险止期',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `bx_term` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '保险期限(单位:天)',
  `c_payer_name` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '支付方名称',
  `c_tran_no` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '支付成交单号',
  `c_arrival_time` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '约定到账时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 171 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '保险订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for zjx_trans_order_people
-- ----------------------------
DROP TABLE IF EXISTS `zjx_trans_order_people`;
CREATE TABLE `zjx_trans_order_people`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键自增',
  `bx_order_no` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '保险订单号',
  `c_app_nme` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '投保人客户名称',
  `c_clnt_mrk` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '投保人性质 1为自然人,0为非自然人',
  `c_certf_cls` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '投保人证件类型 120001居民身份证,120002护照,120003军人证,120009其他',
  `c_certf_cde` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '投保人证件号码',
  `c_mobile` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '投保人手机号码',
  `c_rel_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '与被保人关系601001雇佣,601002子女,601003父母,601004配偶,601005本人,601006其它',
  `c_tel_phone` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '投保人固定电话',
  `c_clnt_addr` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '投保人地址',
  `c_zip_cde` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '投保人邮编',
  `c_nme` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '被保人客户名称',
  `c_clnt_mrk1` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '被保人性质1为自然人,0为非自然人',
  `c_cert_typ` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '被保人证件类型 120001居民身份证,120002护照,120003军人证,120009其他',
  `c_cert_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '被保人证件号码',
  `c_sex` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '被保人性别1-男2-女',
  `c_clnt_addr1` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '被保人地址',
  `c_mobile1` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '被保人移动电话',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 172 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '保险订单详情' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for zq_file_sync
-- ----------------------------
DROP TABLE IF EXISTS `zq_file_sync`;
CREATE TABLE `zq_file_sync`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `batch_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '批次号',
  `channel_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '通道编码',
  `file_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件名',
  `file_url` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件地址',
  `inner_num` int(11) NOT NULL DEFAULT 0 COMMENT '文件内有效数据总条数',
  `operator` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '操作人',
  `create_time` timestamp(0) NOT NULL COMMENT '创建时间',
  `last_update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '数据最后更新时间',
  `status` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '状态,1:报备中,2:报备完成',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `batch_no`(`batch_no`) USING BTREE,
  INDEX `create_ind`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '报表报备信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for zq_file_sync_record
-- ----------------------------
DROP TABLE IF EXISTS `zq_file_sync_record`;
CREATE TABLE `zq_file_sync_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `batch_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '批次号',
  `status` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态,1:报备成功,2:报备失败',
  `channel_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '通道编码',
  `merchant_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号',
  `bp_id` bigint(20) NULL DEFAULT NULL COMMENT '业务产品ID',
  `result_msg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '返回结果',
  `operator` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '操作人',
  `create_time` timestamp(0) NOT NULL COMMENT '创建时间',
  `last_update_time` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '数据最后更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '直清商户导入报备记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for zq_merchant_info
-- ----------------------------
DROP TABLE IF EXISTS `zq_merchant_info`;
CREATE TABLE `zq_merchant_info`  (
  `id` int(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `merchant_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号',
  `unionpay_mer_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银联报备商户号',
  `mer_service_id_` int(20) NULL DEFAULT NULL COMMENT '商户服务ID',
  `sync_status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '同步状态 0：初始化，1：同步成功，2：同步失败，3审核中',
  `effective_status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否生效 0：不生效，1：生效',
  `channel_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '通道编码,包含中付和盛付通',
  `sync_remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上游同步备注',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `operator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作人',
  `terminal_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上游返回终端号',
  `sync_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上游返回码',
  `report_status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '报备状态:(1:已报备)',
  `mpos_Merchant_No` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '盛付通返回的pos号,中付为空',
  `mobilephone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号,盛付通进件主键,不可修改',
  `regid` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '统一社会信用代码',
  `partner_system_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '合作方产生的唯一流水号(银盛),其他为空',
  `mbp_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户进件编号',
  `trm_sn` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '终端序列号',
  `service_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '服务类型',
  `current_use` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '是否当前使用:1是0否',
  `report_method` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '1真实报备2子商户报备',
  `behind_service_status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '后置业务处理状态:0初始化 1成功 2失败3无需处理',
  `bankup_fleld_1` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备用字段1(YS_ZQ存一机一密代理商编号)',
  `bankup_fleld_2` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备用字段2',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unionpayMerNo`(`unionpay_mer_no`) USING BTREE,
  INDEX `mbpid`(`merchant_no`, `mbp_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5877 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '直清商户信息表,包含中付和盛付通' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for zq_merchant_info1
-- ----------------------------
DROP TABLE IF EXISTS `zq_merchant_info1`;
CREATE TABLE `zq_merchant_info1`  (
  `id` int(20) UNSIGNED NOT NULL DEFAULT 0,
  `merchant_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号',
  `unionpay_mer_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银联报备商户号',
  `mer_service_id_` int(20) NULL DEFAULT NULL COMMENT '商户服务ID',
  `sync_status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '同步状态 0：初始化，1：同步成功，2：同步失败，3审核中',
  `effective_status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否生效 0：不生效，1：生效',
  `channel_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '通道编码,包含中付和盛付通',
  `sync_remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上游同步备注',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `operator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作人',
  `terminal_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上游返回终端号',
  `sync_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上游返回码',
  `report_status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '报备状态:(1:已报备)',
  `mpos_Merchant_No` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '盛付通返回的pos号,中付为空',
  `mobilephone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号,盛付通进件主键,不可修改',
  `regid` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '统一社会信用代码',
  `partner_system_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '合作方产生的唯一流水号(银盛),其他为空',
  `mbp_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户进件编号',
  `trm_sn` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '终端序列号'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for zq_merchant_info3
-- ----------------------------
DROP TABLE IF EXISTS `zq_merchant_info3`;
CREATE TABLE `zq_merchant_info3`  (
  `id` int(20) UNSIGNED NOT NULL DEFAULT 0,
  `merchant_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号',
  `unionpay_mer_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银联报备商户号',
  `mer_service_id` int(20) NULL DEFAULT NULL COMMENT '商户服务ID',
  `sync_status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '同步状态 0：初始化，1：同步成功，2：同步失败，3审核中',
  `effective_status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否生效 0：不生效，1：生效',
  `channel_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '通道编码,包含中付和盛付通',
  `sync_remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上游同步备注',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `operator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作人',
  `terminal_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上游返回终端号',
  `sync_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上游返回码',
  `report_status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '报备状态:(1:已报备)',
  `mpos_Merchant_No` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '盛付通返回的pos号,中付为空',
  `mobilephone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号,盛付通进件主键,不可修改',
  `regid` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '统一社会信用代码',
  `partner_system_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '合作方产生的唯一流水号(银盛),其他为空',
  `mbp_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户进件编号',
  `trm_sn` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '终端序列号'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for zq_merchant_log
-- ----------------------------
DROP TABLE IF EXISTS `zq_merchant_log`;
CREATE TABLE `zq_merchant_log`  (
  `id` int(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `merchant_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号',
  `mer_service_id_t` int(20) NULL DEFAULT NULL,
  `sync_status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '同步状态 0：初始化，1：同步成功，2：同步失败',
  `channel_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '通道编码',
  `unionpay_mer_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '银联报备商户号',
  `sync_remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上游同步备注',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作人',
  `terminal_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上游返回终端号',
  `sync_requires` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '同步要素',
  `mpos_Merchant_No` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '盛付通返回的pos号,中付为空',
  `mobilephone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号,盛付通进件主键,不可修改',
  `mbp_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户进件编号',
  `partner_system_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '合作方产生的唯一流水',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2775 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '直清商户同步记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for zq_service_info
-- ----------------------------
DROP TABLE IF EXISTS `zq_service_info`;
CREATE TABLE `zq_service_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `merchant_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号',
  `mbp_id` bigint(20) NULL DEFAULT NULL COMMENT '进件ID',
  `service_id` bigint(20) NULL DEFAULT NULL COMMENT '服务ID',
  `status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '1进件失败2进件成功',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_update_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最后更新时间',
  `msg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '消息描述',
  `code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上游返回状态码',
  `operator` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作人',
  `channel_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '通道编码',
  `bp_id` bigint(20) NULL DEFAULT NULL COMMENT '业务产品ID',
  `acq_service_mer_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '服务对应的上游商户号',
  `mer_wx_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户申请服务的微信号',
  `mer_real_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户微信对应真实姓名',
  `deal_status` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '业务处理状态 0初始化 1未处理 2处理成功 3处理失败',
  `deal_operator` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务处理操作人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `mer_service_code_index`(`merchant_no`, `service_id`, `channel_code`) USING BTREE,
  INDEX `create_time_index`(`create_time`) USING BTREE,
  INDEX `mbp_id_index`(`mbp_id`) USING BTREE,
  INDEX `acq_mer_no_ind`(`acq_service_mer_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 107 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '直清商户服务报件表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for zx_industry_config
-- ----------------------------
DROP TABLE IF EXISTS `zx_industry_config`;
CREATE TABLE `zx_industry_config`  (
  `id` int(5) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `activetiy_code` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '活动编号',
  `team_id` bigint(20) NOT NULL COMMENT '组织ID',
  `rate_type` int(5) NULL DEFAULT NULL COMMENT '费率,1:固定金额,2:费率',
  `fixed_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '固定金额',
  `rate` decimal(10, 2) NULL DEFAULT NULL COMMENT '费率',
  `operator` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_activetiy_team`(`activetiy_code`, `team_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '自选行业配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- View structure for fixed_service_rate
-- ----------------------------
DROP VIEW IF EXISTS `fixed_service_rate`;
CREATE ALGORITHM = UNDEFINED DEFINER = `lius`@`192.168.3.55` SQL SECURITY DEFINER VIEW `fixed_service_rate` AS select `mr`.`id` AS `id`,`mr`.`service_id` AS `service_id`,`mr`.`holidays_mark` AS `holidays_mark`,`mr`.`card_type` AS `card_type`,`mr`.`quota_level` AS `quota_level`,`mr`.`agent_no` AS `agent_no`,`mr`.`rate_type` AS `rate_type`,`mr`.`single_num_amount` AS `single_num_amount`,`mr`.`rate` AS `rate`,`mr`.`capping` AS `capping`,`mr`.`safe_line` AS `safe_line`,`mr`.`check_status` AS `check_status`,`mr`.`lock_status` AS `lock_status`,`mr`.`ladder1_rate` AS `ladder1_rate`,`mr`.`ladder1_max` AS `ladder1_max`,`mr`.`ladder2_rate` AS `ladder2_rate`,`mr`.`ladder2_max` AS `ladder2_max`,`mr`.`ladder3_rate` AS `ladder3_rate`,`mr`.`ladder3_max` AS `ladder3_max`,`mr`.`ladder4_rate` AS `ladder4_rate`,`mr`.`ladder4_max` AS `ladder4_max`,`mr`.`is_global` AS `is_global`,`si`.`fixed_rate` AS `fixed_rate` from (`service_manage_rate` `mr` left join `service_info` `si` on((`mr`.`service_id` = `si`.`service_id`))) where ((`si`.`service_type` < 10000) and (`mr`.`agent_no` = 0) and (`si`.`fixed_rate` = 1));

-- ----------------------------
-- Function structure for nextval
-- ----------------------------
DROP FUNCTION IF EXISTS `nextval`;
delimiter ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `nextval`(seq_name VARCHAR(50)) RETURNS bigint(20)
BEGIN 
DECLARE val BIGINT; 
SET val = 0; 
UPDATE sequence SET current_value = current_value + increment WHERE NAME = seq_name; 
SELECT current_value INTO val FROM sequence WHERE NAME = seq_name; 
RETURN val; 
END
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
