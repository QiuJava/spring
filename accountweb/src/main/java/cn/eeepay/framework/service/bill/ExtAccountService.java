package cn.eeepay.framework.service.bill;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.fasterxml.jackson.databind.JsonMappingException;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.ExtAccount;
import cn.eeepay.framework.model.bill.ExtAccountInfo;
import cn.eeepay.framework.model.bill.ExtTransInfo;
import cn.eeepay.framework.model.bill.MsgEntity;

/**
 * 外部用户Service
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2016年6月29日09:44:36
 *
 */
public interface ExtAccountService {
	/**
	 * 开立外部账号
	 * @param params 外部账号所需参数
	 * @return true 成功,false 失败
	 * @throws Exception
	 */
	boolean createOutAccount(Map<String, String> params)  throws Exception;
	/**
	 * 开立默认外部账号
	 * @param params 外部账号所需参数
	 * @return true 成功,false 失败
	 * @throws Exception
	 */
	boolean createDefaultExtAccount(Map<String, String> params)  throws Exception;
	boolean recordExtAccountApi(Map<String, String> params) throws Exception ;
	MsgEntity extAccountLessAmountApi(Map<String, String> params) throws Exception ;
	
	MsgEntity extAccountForzenAmountApi(Map<String, String> params) throws Exception ;
	MsgEntity extAccountThawAmountApi(Map<String, String> params) throws Exception ;
	int insertExtAccount(ExtAccount outAccount,ExtAccountInfo extAccountInfo)  throws Exception;
	int updateExtAccount(ExtAccount outAccount)  throws Exception;
	
	/**
	 * 查询所有外部账账户信息
	 * @return
	 */
	List<ExtAccount> findAll();
	/**
	 * 修改外部账号状态
	 * @param outAccount 外部账号对象
	 * @return 1 成功,0 失败
	 * @throws Exception
	 */
	int updateExtAccountStatus(ExtAccount outAccount)  throws Exception;
	
	/**
	 * 同时修改账号状态和结算中保留金额
	 * @param extAccount
	 * @return
	 */
	int updateExtAccountSettlingHoldAmount(ExtAccount extAccount);
	/**
	 * 取到外部账号对象
	 * @param accountNo 账号
	 * @return 外部账号对象
	 * @throws Exception
	 */
	ExtAccount getExtAccount(String accountNo) throws Exception;
	ExtAccountInfo findExtAccountInfoByParams(String accountType,String userId,String accountOwner,String cardNo,String subjectNo,String currencyNo) throws Exception;
	ExtAccountInfo findExtAccountInfoByManyParams(String accountType,String userId,String accountOwner,String subjectNo,String currencyNo) throws Exception;
	ExtAccountInfo getExtAccountInfoByParams(ExtAccountInfo extAccountInfo) throws Exception;
	ExtAccountInfo getExtAccountInfoByAccountNo(String accountNo) throws Exception;
	List<ExtAccount> findAllAccountInfo(ExtAccount extAccount,Sort sort,Page<ExtAccount> page,String userNoStrs) throws Exception;
	List<ExtAccount> findAllAccountStatusUpdateInfo(ExtAccount outAccount,Sort sort,Page<ExtAccount> page) throws Exception;
	List<Map<String,Object>> findAllExtTransInfo(ExtTransInfo extTransInfo,Map<String, String> params,Sort sort,Page<Map<String,Object>> page) throws Exception;
	int insertExtTransInfo(ExtTransInfo extTransInfo) throws Exception;
	List<ExtAccount> findExtAccountBySubjectNo(String subjectNo) throws Exception;
	List<ExtAccount> findExtAccountByAccountType(String accountType, Date transTime) throws Exception;
	/**
	 * 取得外部账号的集合
	 * @param accountType 用户类型(商户,代理商)
	 * @param UserId 用户编号
	 * @return 外部账号的集合
	 * @throws Exception
	 */
	List<ExtAccountInfo> findExtAccountInfoByAccountTypeAndUserId(String accountType,String userId) throws Exception;
	
	
	//冻结解冻开始
	ExtAccount findExtAccountByAccountNo(String accountNo) throws Exception ;
	
	List<ExtAccount> findExtAccountByDayBalFlag(String dayBalFlag);
	
	
	//计算商户结算中金额
	boolean updateSettlingAmount() throws Exception; 
	
	/**
	 * 查询 ExtAccountInfo  是否存在
	 * @param extAccountInfo(account_type、user_id、subject_no、currency_no、card_no、account_owner)
	 * @return
	 * @throws Exception
	 */
	ExtAccountInfo exsitsExtAccountInfo(ExtAccountInfo extAccountInfo) throws Exception ;
	
	Map<String,Object> findByMerchantNo(String merchantNo);
	
	ExtAccountInfo getByUserId(String userId);
	
	//查询商户信息
	ExtAccount findExtAccountByMerchantNo(String merchantNo);
	
	//在交易被加入出账单时，再将对应的待出款金额加入到“结算中金额”中
	int  updateAddSettlingAmount(BigDecimal outAccountTaskAmount, String merchantNo);
	
	/**
	 * 代理商冻结金额
	 * @param agentNo 代理商编号
	 * @param amount 冻结金额
	 * @return
	 * @throws Exception
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	Map<String, Object> agentFreezeAmount(String agentNo,String subjectNo,BigDecimal amount) throws Exception, JsonMappingException, IOException;
	
	/**
	 * 代理商解冻金额
	 * @param agentNo 代理商编号
	 * @param amount 解冻金额
	 * @return
	 * @throws Exception
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	Map<String, Object> agentUnFreezeAmount(String agentNo,String subjectNo,BigDecimal amount) throws Exception, JsonMappingException, IOException;
	
	/**
	 * 客户账明细查询 导出
	 * @param extTransInfo
	 * @param params
	 * @return
	 */
	List<ExtTransInfo> exportAllExtTransInfo(ExtTransInfo extTransInfo, Map<String, String> params) throws Exception;
	
	
}
