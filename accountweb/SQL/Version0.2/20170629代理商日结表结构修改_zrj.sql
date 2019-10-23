## 作者：zrj
## 2017-06-29
/*
##全部在v2 bill数据库执行
*/

alter table agent_share_day_settle ADD COLUMN enter_account_message varchar(255) DEFAULT NULL COMMENT '入账信息';