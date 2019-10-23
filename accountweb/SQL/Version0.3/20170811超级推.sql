CREATE TABLE `super_push_share_day_settle` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `collection_batch_no` varchar(50) DEFAULT NULL COMMENT '分润汇总批次号',
  `group_time` timestamp NULL DEFAULT NULL COMMENT '汇总时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '分润创建时间',
  `share_type` varchar(15) DEFAULT NULL COMMENT '0: 一级代理商分润, 1: 直属代理商分润, 2 上一级商户 3 上二级商户 4 上三级商户',
  `share_no` varchar(15) DEFAULT NULL COMMENT '分润的商户/代理商编号',
  `share_name` varchar(15) DEFAULT NULL COMMENT '分润的商户/代理商名称',
  `share_total_amount` decimal(20,2) DEFAULT NULL COMMENT '分润总金额',
  `share_total_num` int(11) DEFAULT NULL COMMENT '分润总笔数',
  `enter_account_status` varchar(20) DEFAULT NULL COMMENT '入账状态(NOENTERACCOUNT未入账,ENTERACCOUNTED已入账)',
  `enter_account_time` timestamp NULL DEFAULT NULL COMMENT '入账时间',
  `enter_account_message` varchar(255) DEFAULT NULL COMMENT '入账信息',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='超级推分润日结表';



INSERT INTO `generic_table` (`table_name`, `primary_key`, `increment`) VALUES ('super_push_share_collection_batch_no', '0', '1');
