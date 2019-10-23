## 作者：zrj
## 2017-05-25
/*
##全部在v2 bill数据库执行
*/

alter table trans_short_info ADD COLUMN agent_no varchar(50) DEFAULT not NULL COMMENT '代理商编号' after one_agent_name;
alter table trans_short_info ADD COLUMN agent_name varchar(50) DEFAULT NULL COMMENT '代理商名称' after agent_no;
alter table trans_short_info ADD COLUMN agent_node varchar(255) DEFAULT NULL COMMENT '代理商节点' after agent_name;
alter table trans_short_info ADD COLUMN agent_level varchar(50) DEFAULT NULL COMMENT '代理商级别' after agent_node;
alter table trans_short_info ADD COLUMN parent_agent_no varchar(50) DEFAULT NULL COMMENT '父级代理商编号' after agent_level;
alter table trans_short_info ADD COLUMN profits_1 decimal(30,2) DEFAULT NULL COMMENT '一级分润';
alter table trans_short_info ADD COLUMN profits_2 decimal(30,2) DEFAULT NULL;
alter table trans_short_info ADD COLUMN profits_3 decimal(30,2) DEFAULT NULL;
alter table trans_short_info ADD COLUMN profits_4 decimal(30,2) DEFAULT NULL;
alter table trans_short_info ADD COLUMN profits_5 decimal(30,2) DEFAULT NULL;
alter table trans_short_info ADD COLUMN profits_6 decimal(30,2) DEFAULT NULL;
alter table trans_short_info ADD COLUMN profits_7 decimal(30,2) DEFAULT NULL;
alter table trans_short_info ADD COLUMN profits_8 decimal(30,2) DEFAULT NULL;
alter table trans_short_info ADD COLUMN profits_9 decimal(30,2) DEFAULT NULL;
alter table trans_short_info ADD COLUMN profits_10 decimal(30,2) DEFAULT NULL;
alter table trans_short_info ADD COLUMN profits_11 decimal(30,2) DEFAULT NULL;
alter table trans_short_info ADD COLUMN profits_12 decimal(30,2) DEFAULT NULL;
alter table trans_short_info ADD COLUMN profits_13 decimal(30,2) DEFAULT NULL;
alter table trans_short_info ADD COLUMN profits_14 decimal(30,2) DEFAULT NULL;
alter table trans_short_info ADD COLUMN profits_15 decimal(30,2) DEFAULT NULL;
alter table trans_short_info ADD COLUMN profits_16 decimal(30,2) DEFAULT NULL;
alter table trans_short_info ADD COLUMN profits_17 decimal(30,2) DEFAULT NULL;
alter table trans_short_info ADD COLUMN profits_18 decimal(30,2) DEFAULT NULL;
alter table trans_short_info ADD COLUMN profits_19 decimal(30,2) DEFAULT NULL;
alter table trans_short_info ADD COLUMN profits_20 decimal(30,2) DEFAULT NULL;

alter table trans_short_info ADD COLUMN settle_profits_1 decimal(30,2) DEFAULT NULL COMMENT '一级提现分润';
alter table trans_short_info ADD COLUMN settle_profits_2 decimal(30,2) DEFAULT NULL;
alter table trans_short_info ADD COLUMN settle_profits_3 decimal(30,2) DEFAULT NULL;
alter table trans_short_info ADD COLUMN settle_profits_4 decimal(30,2) DEFAULT NULL;
alter table trans_short_info ADD COLUMN settle_profits_5 decimal(30,2) DEFAULT NULL;
alter table trans_short_info ADD COLUMN settle_profits_6 decimal(30,2) DEFAULT NULL;
alter table trans_short_info ADD COLUMN settle_profits_7 decimal(30,2) DEFAULT NULL;
alter table trans_short_info ADD COLUMN settle_profits_8 decimal(30,2) DEFAULT NULL;
alter table trans_short_info ADD COLUMN settle_profits_9 decimal(30,2) DEFAULT NULL;
alter table trans_short_info ADD COLUMN settle_profits_10 decimal(30,2) DEFAULT NULL;
alter table trans_short_info ADD COLUMN settle_profits_11 decimal(30,2) DEFAULT NULL;
alter table trans_short_info ADD COLUMN settle_profits_12 decimal(30,2) DEFAULT NULL;
alter table trans_short_info ADD COLUMN settle_profits_13 decimal(30,2) DEFAULT NULL;
alter table trans_short_info ADD COLUMN settle_profits_14 decimal(30,2) DEFAULT NULL;
alter table trans_short_info ADD COLUMN settle_profits_15 decimal(30,2) DEFAULT NULL;
alter table trans_short_info ADD COLUMN settle_profits_16 decimal(30,2) DEFAULT NULL;
alter table trans_short_info ADD COLUMN settle_profits_17 decimal(30,2) DEFAULT NULL;
alter table trans_short_info ADD COLUMN settle_profits_18 decimal(30,2) DEFAULT NULL;
alter table trans_short_info ADD COLUMN settle_profits_19 decimal(30,2) DEFAULT NULL;
alter table trans_short_info ADD COLUMN settle_profits_20 decimal(30,2) DEFAULT NULL;

INSERT INTO sys_dict (sys_key, sys_name, html_name, sys_value, order_no, status, remark) VALUES ( 'sys_agents_profit', 'enter_scale', NULL, '', NULL, '1', NULL);


alter table agent_share_day_settle ADD COLUMN agent_no varchar(50) DEFAULT NULL COMMENT '代理商编号' after one_agent_name;
alter table agent_share_day_settle ADD COLUMN agent_name varchar(50) DEFAULT NULL COMMENT '代理商名称' after agent_no;
alter table agent_share_day_settle ADD COLUMN agent_node varchar(255) DEFAULT NULL COMMENT '代理商节点' after agent_name;
alter table agent_share_day_settle ADD COLUMN agent_level varchar(50) DEFAULT NULL COMMENT '代理商级别' after agent_node;
alter table agent_share_day_settle ADD COLUMN parent_agent_no varchar(50) DEFAULT NULL COMMENT '父级代理商编号' after agent_level;
alter table agent_share_day_settle ADD COLUMN real_enter_share_amount decimal(20,2) DEFAULT NULL COMMENT '实际到账分润';


CREATE TABLE `agent_unfreeze` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `agent_no` varchar(50) DEFAULT NULL COMMENT '代理商编号',
  `agent_name` varchar(255) DEFAULT NULL COMMENT '代理商名称',
  `unfreeze_time` timestamp NULL DEFAULT NULL COMMENT '解冻时间',
  `operater` varchar(255) DEFAULT NULL COMMENT '操作人',
  `amount` decimal(20,2) DEFAULT '0.00' COMMENT '解冻金额',
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='代理商解冻表';


ALTER TABLE agent_pre_record_total CHANGE one_agent_no agent_no varchar(50) NOT NULL COMMENT '代理商编号';
ALTER TABLE agent_pre_record_total CHANGE one_agent_name agent_name varchar(50) NOT NULL COMMENT '代理商名称';

ALTER TABLE agent_pre_freeze CHANGE one_agent_no agent_no varchar(50) NOT NULL COMMENT '代理商编号';
ALTER TABLE agent_pre_freeze CHANGE one_agent_name agent_name varchar(50) NOT NULL COMMENT '代理商名称';

ALTER TABLE agent_pre_adjust CHANGE one_agent_no agent_no varchar(50) NOT NULL COMMENT '代理商编号';
ALTER TABLE agent_pre_adjust CHANGE one_agent_name agent_name varchar(50) NOT NULL COMMENT '代理商名称';


