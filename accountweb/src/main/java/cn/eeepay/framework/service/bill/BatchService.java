package cn.eeepay.framework.service.bill;

import java.util.Map;

public interface BatchService {
	 boolean runCutOff() throws Exception;
	 boolean runAccoutUpdateAmount() throws Exception;
	 /**
	  * 入总账
	  * @return
	  * @throws Exception
	  */
	 boolean runCheckInAllAccount() throws Exception;
	 boolean runCheckBalance() throws Exception;
	 boolean runBakHistoryTrans() throws Exception;
	 boolean runAppend() throws Exception;
	 boolean runBeginOfMonthAgentDiff() throws Exception;
	 boolean runBeginOfMonthUpChannelDiff() throws Exception;
	 boolean runNormal() throws Exception;
	 /**
	  * 	计算商户，上游，代理商结算中金额
	  * @return
	  * @throws Exception
	  */
	 boolean runCountSettlingAmount() throws Exception;
	 
	 /**
	  * 日终修改余额账户类的余额处理-外部账
	  * @return
	  * @throws Exception
	  */
	 boolean runExtAccoutUpdateAmount() throws Exception;
	 
	 /**
	  * 	月处出款通道补上月阶梯差
	  */
	 Map<String,Object> outBeginOfMonthUpChannelDiff() throws Exception;
	 
	 //boolean runMakeOutAccountTask() throws Exception;
	
}
