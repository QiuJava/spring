package cn.eeepay.framework.dao.bill;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.ExtAccount;
import cn.eeepay.framework.model.bill.ExtAccountInfo;
import cn.eeepay.framework.model.bill.ExtTransInfo;

/**
 * 外部账号表相关
 * 
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2016年4月12日13:45:54
 *
 */
public interface ExtAccountMapper {
	static final Logger log = LoggerFactory.getLogger(ExtAccountMapper.class);
	
	@Insert("insert into bill_ext_account(account_no,account_name,org_no,currency_no,subject_no,curr_balance,account_status,creator,create_time,balance_add_from,balance_from,day_bal_flag,sum_flag"
			+ ",settling_amount,pre_freeze_amount,parent_trans_day,parent_trans_balance,control_amount)" 
			+"values(#{extAccount.accountNo},#{extAccount.accountName},#{extAccount.orgNo},#{extAccount.currencyNo},#{extAccount.subjectNo},#{extAccount.currBalance},#{extAccount.accountStatus},"
			+ "#{extAccount.creator},#{extAccount.createTime},#{extAccount.balanceAddFrom},#{extAccount.balanceFrom},#{extAccount.dayBalFlag},#{extAccount.sumFlag},#{extAccount.settlingAmount},"
			+ "#{extAccount.preFreezeAmount},#{extAccount.parentTransDay},#{extAccount.parentTransBalance},#{extAccount.controlAmount})"
			)
	int insertExtAccount(@Param("extAccount")ExtAccount extAccount);
	
	@Update("update bill_ext_account set account_name=#{extAccount.accountName},org_no=#{extAccount.orgNo},"
			+ "currency_no=#{extAccount.currencyNo},subject_no=#{extAccount.subjectNo},curr_balance=#{extAccount.currBalance},account_status=#{extAccount.accountStatus},"
			+ "create_time=#{extAccount.createTime},balance_add_from=#{extAccount.balanceAddFrom},balance_from=#{extAccount.balanceFrom},"
			+ "day_bal_flag=#{extAccount.dayBalFlag},sum_flag=#{extAccount.sumFlag},settling_amount=#{extAccount.settlingAmount},pre_freeze_amount=#{extAccount.preFreezeAmount},control_amount=#{extAccount.controlAmount} "
			+ "where account_no = #{extAccount.accountNo}" 
			)
	int updateExtAccount(@Param("extAccount")ExtAccount extAccount);
	
	@Select("select * from bill_ext_account")
	@ResultType(ExtAccount.class)
	List<ExtAccount> findAll();
	
	@Select("select account_no,org_no,currency_no,subject_no,account_name,curr_balance,control_amount,parent_trans_day,parent_trans_balance,account_status,creator,create_time,balance_add_from,balance_from,day_bal_flag,sum_flag from bill_ext_account where day_bal_flag = #{dayBalFlag}")
	@ResultMap("cn.eeepay.framework.dao.bill.ExtAccountMapper.BaseResultMap")
	List<ExtAccount> findExtAccountByDayBalFlag(@Param("dayBalFlag")String dayBalFlag);
	
	@Update("update bill_ext_account set account_status = #{extAccount.accountStatus} where account_no =#{extAccount.accountNo}")
	int updateExtAccountStatus(@Param("extAccount")ExtAccount extAccount);
	
	@Update("update bill_ext_account set settling_hold_amount = #{extAccount.settlingHoldAmount} where account_no =#{extAccount.accountNo}")
	int updateExtAccountSettlingHoldAmount(@Param("extAccount")ExtAccount extAccount);
	
	
	@Select("select account_no,account_name,org_no,currency_no,subject_no,curr_balance,creator,create_time from bill_ext_account where currency_no = #{currencyNo} AND subject_no= #{subjectNo} AND org_no=#{org_no}")
	@ResultMap("cn.eeepay.framework.dao.bill.ExtAccountMapper.BaseResultMap")
	ExtAccount exsitsExtAccount(@Param("currencyNo")String currencyNo,@Param("subjectNo") String subjectNo, @Param("org_no") String org_no);	

	@Select("select * from ext_account_info where currency_no = #{extAccountInfo.currencyNo} AND subject_no= #{extAccountInfo.subjectNo} AND account_owner=#{extAccountInfo.accountOwner} "
			+ "AND (card_no = #{extAccountInfo.cardNo} OR ISNULL(card_no)) AND user_id = #{extAccountInfo.userId} AND account_type = #{extAccountInfo.accountType}")
	@ResultMap("cn.eeepay.framework.dao.bill.ExtAccountInfoMapper.BaseResultMap")
	ExtAccountInfo exsitsExtAccountInfo(@Param("extAccountInfo")ExtAccountInfo extAccountInfo);	
	
	
	@Select("select * from bill_ext_account where account_no = #{accountNo} ")
	@ResultMap("cn.eeepay.framework.dao.bill.ExtAccountMapper.BaseResultMap")
	ExtAccount getExtAccount(@Param("accountNo") String accountNo);	
	
	@Select("select account_no,account_name,org_no,currency_no,subject_no,curr_balance,settling_amount,creator,create_time from bill_ext_account where subject_no = #{subjectNo} ")
	@ResultMap("cn.eeepay.framework.dao.bill.ExtAccountMapper.BaseResultMap")
	List<ExtAccount> findExtAccountBySubjectNo(@Param("subjectNo") String subjectNo);	
	
	@SelectProvider( type=SqlProvider.class,method="findExtAccountInfoByParams")
	@ResultMap("cn.eeepay.framework.dao.bill.ExtAccountInfoMapper.BaseResultMap")
	ExtAccountInfo findExtAccountInfoByParams(@Param("accountType") String accountType,@Param("userId") String userId,@Param("accountOwner") String accountOwner,@Param("cardNo") String cardNo,@Param("subjectNo") String subjectNo,@Param("currencyNo") String currencyNo);	
	
	
	@Select("select * from ext_account_info where account_type = #{accountType} "
			+ " and user_id = #{userId} "
			+ " and account_owner = #{accountOwner} "
			+ " and card_no is null "
			+ " and subject_no = #{subjectNo} "
			+ " and currency_no = #{currencyNo} ")
	@ResultMap("cn.eeepay.framework.dao.bill.ExtAccountInfoMapper.BaseResultMap")
	ExtAccountInfo findExtAccountInfoByManyParams(@Param("accountType") String accountType,@Param("userId") String userId,@Param("accountOwner") String accountOwner,@Param("subjectNo") String subjectNo,@Param("currencyNo") String currencyNo);	
	
	
	@Select("select account_no,account_type,user_id,account_owner,card_no,subject_no,currency_no from ext_account_info "
			+ "where account_type = #{extAccountInfo.accountType} and user_id = #{extAccountInfo.userId} and account_owner = #{extAccountInfo.accountOwner} "
			+ "and card_no = #{extAccountInfo.cardNo} and subject_no = #{extAccountInfo.subjectNo} and currency_no = #{extAccountInfo.currencyNo} ")
	@ResultMap("cn.eeepay.framework.dao.bill.ExtAccountMapper.BaseResultMap")
	ExtAccountInfo getExtAccountInfoByParams2(@Param("extAccountInfo") ExtAccountInfo extAccountInfo);	
	
	
	@Select("select account_no,account_type,user_id,account_owner,card_no,subject_no,currency_no from ext_account_info "
			+ "where account_no = #{accountNo} ")
	@ResultMap("cn.eeepay.framework.dao.bill.ExtAccountInfoMapper.BaseResultMap")
	ExtAccountInfo getExtAccountInfoByAccountNo(@Param("accountNo") String accountNo);	
	
	
	@SelectProvider( type=SqlProvider.class,method="findAllAccountInfo")
	@ResultMap("cn.eeepay.framework.dao.bill.ExtAccountMapper.BaseResultMap")
	List<ExtAccount> findAllAccountInfo(@Param("extAccount")ExtAccount extAccount,@Param("sort")Sort sort,Page<ExtAccount> page,
			@Param("userNoStrs")String userNoStrs);
	
	@SelectProvider( type=SqlProvider.class,method="findExtAccountByMerchantNo")
	@ResultMap("cn.eeepay.framework.dao.bill.ExtAccountMapper.BaseResultMap")
	ExtAccount findExtAccountByMerchantNo(@Param("merchantNo")String merchantNo);
	
	@SelectProvider( type=SqlProvider.class,method="findExtAccountByAccountType")
	@ResultMap("cn.eeepay.framework.dao.bill.ExtAccountMapper.OneToOneResultMap")
	List<ExtAccount> findExtAccountByAccountType(@Param("accountType")String accountType, @Param("transTime")Date transTime);
	
	@SelectProvider( type=SqlProvider.class,method="findExtAccountInfoByAccountTypeAndUserId")
	@ResultMap("cn.eeepay.framework.dao.bill.ExtAccountInfoMapper.BaseResultMap")
	List<ExtAccountInfo> findExtAccountInfoByAccountTypeAndUserId(@Param("accountType")String accountType,@Param("userId")String userId);
	
	
	@SelectProvider( type=SqlProvider.class,method="findAllAccountStatusUpdateInfo")
	@ResultMap("cn.eeepay.framework.dao.bill.ExtAccountMapper.OneToOneResultMap")
	List<ExtAccount> findAllAccountStatusUpdateInfo(@Param("extAccount")ExtAccount extAccount,@Param("sort")Sort sort,Page<ExtAccount> page);
	
	@SelectProvider( type=SqlProvider.class,method="getAllExtTransInfo")
	@Results(value = {  
            @Result(property = "accountNo", column = "account_no"),  
            @Result(property = "transType", column = "trans_type"),
            @Result(property = "recordAmount", column = "record_amount"),  
            @Result(property = "balance", column = "balance"),
            @Result(property = "avaliBalance", column = "avali_balance"),
            @Result(property = "controlAmount", column = "control_amount"),  
            @Result(property = "settlingAmount", column = "settling_amount"),  
            @Result(property = "preFreezeAmount", column = "pre_freeze_amount"),  
            @Result(property = "serialNo", column = "serial_no"),  
            @Result(property = "childSerialNo", column = "child_serial_no"),  
            @Result(property = "recordDate", column = "record_date"),  
            @Result(property = "recordTime", column = "record_time"),
            @Result(property = "debitCreditSide", column = "debit_credit_side"),  
            @Result(property = "summaryInfo", column = "summary_info"),  
            @Result(property = "balanceAddFrom", column = "balance_add_from"),  
        })
	List<Map<String,Object>> getAllExtTransInfo(@Param("extTransInfo")ExtTransInfo extTransInfo,@Param("params") Map<String, String> params,@Param("sort")Sort sort,Page<Map<String,Object>> page);
	
	@SelectProvider( type=SqlProvider.class,method="exportAllExtTransInfo")
	@Results(value = {  
            @Result(property = "accountNo", column = "account_no"),  
            @Result(property = "transType", column = "trans_type"),
            @Result(property = "recordAmount", column = "record_amount"),  
            @Result(property = "balance", column = "balance"),
            @Result(property = "avaliBalance", column = "avali_balance"),
            @Result(property = "controlAmount", column = "control_amount"),  
            @Result(property = "settlingAmount", column = "settling_amount"),  
            @Result(property = "preFreezeAmount", column = "pre_freeze_amount"),  
            @Result(property = "serialNo", column = "serial_no"),  
            @Result(property = "childSerialNo", column = "child_serial_no"),  
            @Result(property = "recordDate", column = "record_date"),  
            @Result(property = "recordTime", column = "record_time"),
            @Result(property = "debitCreditSide", column = "debit_credit_side"),  
            @Result(property = "summaryInfo", column = "summary_info"),  
            @Result(property = "balanceAddFrom", column = "balance_add_from"),  
        })
	List<ExtTransInfo> exportAllExtTransInfo(@Param("extTransInfo")ExtTransInfo extTransInfo, @Param("params")Map<String, String> params);
	
	
	@SelectProvider( type=SqlProvider.class,method="findAllExtTransInfo")
	@ResultMap("cn.eeepay.framework.dao.bill.ExtTransInfoMapper.BaseResultMap")
	List<ExtTransInfo> findAllExtTransInfo(@Param("extTransInfo")ExtTransInfo extTransInfo,@Param("params") Map<String, String> params,@Param("sort")Sort sort,Page<ExtTransInfo> page);
	
	@Insert("insert into ext_account_info(account_no,account_type,user_id,account_owner,card_no,subject_no,currency_no)"
			+"values(#{extAccountInfo.accountNo},#{extAccountInfo.accountType},#{extAccountInfo.userId},#{extAccountInfo.accountOwner},#{extAccountInfo.cardNo},#{extAccountInfo.subjectNo},#{extAccountInfo.currencyNo})"
			)
	int insertExtAccountInfo(@Param("extAccountInfo")ExtAccountInfo extAccountInfo);
	
	@Insert("insert into ext_trans_info(account_no,record_amount,balance,avali_balance,serial_no,child_serial_no,record_date,record_time,debit_credit_side,summary_info,trans_order_no)"
			+"values(#{extTransInfo.accountNo},#{extTransInfo.recordAmount},#{extTransInfo.balance},#{extTransInfo.avaliBalance},#{extTransInfo.serialNo},#{extTransInfo.childSerialNo},"
			+ "#{extTransInfo.recordDate},#{extTransInfo.recordTime},#{extTransInfo.debitCreditSide},#{extTransInfo.summaryInfo},#{extTransInfo.transOrderNo})"
			)
	int insertExtTransInfo(@Param("extTransInfo")ExtTransInfo extTransInfo);
	
	//冻结解冻
	@Select("select * from bill_ext_account where account_no = #{accountNo}")
	ExtAccount findExtAccountByAccountNo(@Param("accountNo")String accountNo) ;
	
	//商户结算中金额更新
	@Update("update bill_ext_account set settling_amount=curr_balance-control_amount where subject_no=#{subjectNo}")
	int updateExtAccountByMAndA(@Param("subjectNo")String subjectNo);
	
	@Select("select ea.user_id,b.settling_amount,b.account_status from ext_account_info ea,bill_ext_account b where ea.account_no = b.account_no and ea.user_id=#{merchantNo} and  ea.account_type = 'M' and ea.subject_no = '224101001'")
	@Results(value = {  
            @Result(property = "merchantNo", column = "user_id"),  
            @Result(property = "settlingAmount", column = "settling_amount"),  
            @Result(property = "accountStatus", column = "account_status"),
        })
	Map<String,Object> findByMerchantNo(String merchantNo);
	
	@Select("select * from ext_account_info where user_id=#{userId}")
	@ResultMap("cn.eeepay.framework.dao.bill.ExtTransInfoMapper.BaseResultMap")
	ExtAccountInfo getByUserId(@Param("userId")String userId);
	
	@Update("update bill_ext_account as bea, ext_account_info as eai "
			+ "set bea.settling_amount = IFNULL(bea.settling_amount,0) + #{outAccountTaskAmount} "
			+ "where bea.account_no = eai.account_no "
			+ "and eai.account_type = 'M' "
			+ "and bea.subject_no = '224101001' "
			+ "and eai.user_id = #{merchantNo}")
	int updateAddSettlingAmount( @Param("outAccountTaskAmount")BigDecimal outAccountTaskAmount, @Param("merchantNo")String merchantNo);
	
	@Update(" update bill_ext_account as bea, ext_account_info as eai "
			+ " set bea.settling_amount = bea.settling_amount - #{outAccountTaskAmount} "
			+ " where bea.account_no = eai.account_no "
			+ " and eai.account_type = 'M' "
			+ " and bea.subject_no = '224101001' "
			+ " and bea.settling_amount >= #{outAccountTaskAmount} "
			+ " and eai.user_id = #{merchantNo}")
	int updateSubtractSettlingAmount(@Param("outAccountTaskAmount")BigDecimal outAccountTaskAmount, @Param("merchantNo")String merchantNo);
	
	public class SqlProvider{
		
		public String findExtAccountInfoByParams(final Map<String, Object> parameter) {
			final String accountType = (String) parameter.get("accountType");
			final String userId = (String) parameter.get("userId");
			final String accountOwner = (String) parameter.get("accountOwner");
			final String cardNo = (String) parameter.get("cardNo");
			final String subjectNo = (String) parameter.get("subjectNo");
			final String currencyNo = (String) parameter.get("currencyNo");
//			for (Map.Entry<String, Object> entry : parameter.entrySet()) {
//				log.info("{}={}", new Object[] { entry.getKey(), entry.getValue() });
//			}
			return new SQL(){{
				SELECT(" * ");
				FROM(" ext_account_info as a ");
				if (StringUtils.isNotBlank(accountType))
					WHERE(" a.account_type = #{accountType} ");
				if (StringUtils.isNotBlank(userId)){
					WHERE(" a.user_id = #{userId} ");
				}else{
					WHERE(" (a.user_id IS NULL OR a.user_id = '') ");
					}
				if (StringUtils.isNotBlank(accountOwner))
						WHERE(" a.account_owner = #{accountOwner} ");	
				if (StringUtils.isNotBlank(cardNo)){
					WHERE(" a.card_no = #{cardNo} ");	
				}else{
					WHERE(" (a.card_no IS NULL OR a.card_no = '') ");	
				}
				//WHERE("bea.account_no = eai.account_no and bea.subject_no = s.subject_no");
				if (StringUtils.isNotBlank(subjectNo))
						WHERE(" a.subject_no = #{subjectNo} ");
				if (StringUtils.isNotBlank(currencyNo)) 
						WHERE(" a.currency_no = #{currencyNo} ");
//				if(StringUtils.isNotBlank(sord.getSidx())){
//					ORDER_BY(propertyMapping(sord.getSidx(), 0)+" "+sord.getSord());
//				}
			}}.toString();
		}
		
		public String findAllAccountStatusUpdateInfo(final Map<String, Object> parameter) {
			final ExtAccount extAccount = (ExtAccount) parameter.get("extAccount");
			final Sort sord=(Sort)parameter.get("sort");
			return new SQL(){{
				SELECT("bea.account_no,"
						+ "bea.account_name,"
						+ "eai.account_type,"
						+ "bea.org_no,"
						+ "bea.currency_no,"
						+ "bea.subject_no,"
						+ "bea.curr_balance,"
						+ "bea.account_status,"
						+ "eai.card_no,"
						+ "eai.user_id,"
						+ "s.subject_name,"
						+ "eai.account_owner,"
						+ "(bea.curr_balance - bea.control_amount - bea.settling_amount) as avail_balance");
				FROM("bill_ext_account as bea, ext_account_info as eai,bill_subject as s ");
				WHERE("bea.account_no = eai.account_no and bea.subject_no = s.subject_no");
				if (StringUtils.isNotBlank(extAccount.getAccountNo()))
					WHERE(" bea.account_no  = #{extAccount.accountNo} ");
				if (StringUtils.isNotBlank(extAccount.getSubjectNo()))
					WHERE(" bea.subject_no = #{extAccount.subjectNo} ");
				if (StringUtils.isNotBlank(extAccount.getExtAccountInfo().getUserId()))
					WHERE(" eai.user_id like  \"%\"#{extAccount.extAccountInfo.userId}\"%\" ");
				if (StringUtils.isNotBlank(extAccount.getCurrencyNo()))
					WHERE(" bea.currency_no = #{extAccount.currencyNo} ");
				if (StringUtils.isNotBlank(extAccount.getOrgNo()))
					WHERE(" bea.org_no = #{extAccount.orgNo} ");
				if(StringUtils.isNotBlank(sord.getSidx())){
					ORDER_BY(propertyMapping(sord.getSidx(), 0)+" "+sord.getSord());
				}
			}}.toString();
		}
		
		public String findAllAccountInfo(final Map<String, Object> parameter) {
			final ExtAccount extAccount = (ExtAccount) parameter.get("extAccount");
			
			final String userNoStrs = (String) parameter.get("userNoStrs") ;
			final Sort sord=(Sort)parameter.get("sort");
			String sql = new SQL(){{
				SELECT("bea.account_no,bea.account_name,eai.account_type,bea.org_no,bea.currency_no,bea.subject_no,"
						+ "bea.curr_balance,bea.control_amount,bea.settling_amount,bea.settling_hold_amount,bea.pre_freeze_amount,bea.account_status,bea.creator,bea.create_time,"
						+ "eai.user_id,s.subject_name,eai.account_owner,(bea.curr_balance - bea.control_amount - bea.settling_amount) as avail_balance ");
				FROM(" bill_ext_account as bea, ext_account_info as eai, bill_subject as s  ");
				WHERE(" bea.account_no = eai.account_no AND bea.subject_no = s.subject_no ") ;
				if (StringUtils.isNotBlank(extAccount.getAccountNo()))
					WHERE(" bea.account_no  = #{extAccount.accountNo} ");
				if (StringUtils.isNotBlank(extAccount.getSubjectNo())  && !extAccount.getSubjectNo().equals("ALL"))
					WHERE(" bea.subject_no = #{extAccount.subjectNo} ");
				if (extAccount.getExtAccountInfo() != null && StringUtils.isNotBlank(extAccount.getExtAccountInfo().getAccountType()) && !extAccount.getExtAccountInfo().getAccountType().equals("ALL"))
					WHERE(" eai.account_type = #{extAccount.extAccountInfo.accountType} ");
				if (extAccount.getExtAccountInfo() != null && StringUtils.isNotBlank(extAccount.getExtAccountInfo().getUserId()))
					WHERE(" eai.user_id = #{extAccount.extAccountInfo.userId} ");
				if (StringUtils.isNotBlank(extAccount.getAccountStatus()) && !extAccount.getAccountStatus().equals("ALL"))
					WHERE(" bea.account_status = #{extAccount.accountStatus} ");
				if (StringUtils.isNotBlank(extAccount.getCurrencyNo()) && !extAccount.getCurrencyNo().equals("ALL"))
					WHERE(" bea.currency_no = #{extAccount.currencyNo} ");
				if (StringUtils.isNotBlank(extAccount.getOrgNo()) && !extAccount.getOrgNo().equals("ALL"))
					WHERE(" bea.org_no = #{extAccount.orgNo} ");
				
				if (StringUtils.isNotBlank(extAccount.getControlBeginAmount()))
					WHERE(" bea.control_amount >= #{extAccount.controlBeginAmount} ");
				if (StringUtils.isNotBlank(extAccount.getControlEndAmount()))
					WHERE(" bea.control_amount <= #{extAccount.controlEndAmount} ");
				
				if (StringUtils.isNotBlank(extAccount.getCurrBalanceBeginAmount()))
					WHERE(" bea.curr_balance >= #{extAccount.currBalanceBeginAmount} ");
				if (StringUtils.isNotBlank(extAccount.getCurrBalanceEndAmount()))
					WHERE(" bea.curr_balance <= #{extAccount.currBalanceEndAmount} ");
				
				if(!"isBlank".equalsIgnoreCase(userNoStrs))
				WHERE(" eai.user_id in ("+userNoStrs+")") ;
				if(sord != null && StringUtils.isNotBlank(sord.getSidx())){
					ORDER_BY(propertyMapping(sord.getSidx(), 0)+" "+sord.getSord());
				}else{
					ORDER_BY(" bea.create_time desc ") ;
				}
			}}.toString();
			
			return sql;
		}
		
		public String findExtAccountByAccountType(final Map<String, Object> parameter) {
			final String accountType = (String) parameter.get("accountType");
			return new SQL(){{
				SELECT("  bea.account_no, "+
						" bea.account_name, "+
						" eai.account_type, "+
						" bea.org_no, "+
						" bea.currency_no, "+
						" bea.subject_no, "+
						" bea.curr_balance, "+
						" bea.account_status, "+
						" (bea.curr_balance - bea.control_amount - bea.settling_amount) as avail_balance, "+
						" bea.settling_amount-IFNULL(b.filled_amout,0) as settling_amount,"+
						" eai.user_id, "+
						" eai.account_owner ");
				FROM(" bill_ext_account as bea, ext_account_info as eai left join (select o2.merchant_no,sum(o2.out_account_task_amount) filled_amout from out_bill_detail o2 where o2.create_time = #{transTime} group by o2.merchant_no) b on eai.user_id = b.merchant_no");
				WHERE(" bea.account_no = eai.account_no and bea.account_status=1");
				WHERE(" bea.subject_no = '224101001'");
				WHERE(" eai.account_type = #{accountType} ");
				WHERE(" bea.settling_amount-IFNULL(b.filled_amout,0)>0");
			}}.toString();
		}
		
		public String findExtAccountByMerchantNo(final Map<String, Object> parameter) {
			return new SQL(){{
				SELECT("  bea.account_no, "+
						" bea.account_name, "+
						" eai.account_type, "+
						" bea.org_no, "+
						" bea.currency_no, "+
						" bea.subject_no, "+
						" bea.curr_balance, "+
						" bea.account_status, "+
						" (bea.curr_balance - bea.control_amount - bea.settling_amount) as avail_balance, "+
						" bea.settling_amount,"+
						" eai.user_id, "+
						" eai.account_owner ");
				FROM(" bill_ext_account as bea, ext_account_info as eai ");
				WHERE(" bea.account_no = eai.account_no ");
				WHERE(" eai.account_type = 'M' ");
				WHERE(" bea.subject_no = '224101001'");
				WHERE(" eai.user_id = #{merchantNo}");
			}}.toString();
		}
		
		public String findExtAccountInfoByAccountTypeAndUserId(final Map<String, Object> parameter) {
			final String accountType = (String) parameter.get("accountType");
			final String userId = (String) parameter.get("userId");
			return new SQL(){{
				SELECT("  * ");
				FROM(" ext_account_info as eai ");
				WHERE(" eai.account_type = #{accountType} ");
				WHERE(" eai.user_id = #{userId} ");
			}}.toString();
		}
		
		public String getAllExtTransInfo(final Map<String, Object> parameter) {
			final ExtTransInfo extTransInfo = (ExtTransInfo) parameter.get("extTransInfo");
			@SuppressWarnings("unchecked")
			final Map<String, String> params = (Map<String, String>) parameter.get("params");
			final Sort sord=(Sort)parameter.get("sort");
			final String recordDate1 = params.get("recordDate1");
			final String recordDate2 = params.get("recordDate2");
			return new SQL(){{
				SELECT("eti.account_no ,"
						+ "eti.trans_type ,"
						+ "eti.record_amount ,"
						+ "eti.balance ,"
						+ "eti.avali_balance ,"
						+ "eti.control_amount ,"
						+ "eti.settling_amount ,"
						+ "eti.pre_freeze_amount ,"
						+ "eti.serial_no ,"
						+ "eti.child_serial_no ,"
						+ "eti.record_date ,"
						+ "eti.record_time ,"
						+ "eti.debit_credit_side ,"
						+ "eti.summary_info ,"
						+ "bea.balance_add_from ");
				FROM("ext_trans_info as eti,bill_ext_account as bea ");
				WHERE("eti.account_no = bea.account_no");
				if (StringUtils.isNotBlank(extTransInfo.getAccountNo()))
					WHERE(" eti.account_no  =  #{extTransInfo.accountNo} ");
				if (recordDate1 != null && StringUtils.isNotBlank(recordDate1))
					WHERE(" eti.record_date >= #{params.recordDate1} ");
				if (recordDate2 != null && StringUtils.isNotBlank(recordDate2))
					WHERE(" eti.record_date <= #{params.recordDate2} ");
				if(StringUtils.isNotBlank(sord.getSidx())){
					ORDER_BY(propertyMapping(sord.getSidx(), 0)+" "+sord.getSord());
				}else{
					ORDER_BY(" eti.id desc ") ;
				}
			}}.toString();
		}
		
		public String exportAllExtTransInfo(final Map<String, Object> parameter) {
			final ExtTransInfo extTransInfo = (ExtTransInfo) parameter.get("extTransInfo");
			@SuppressWarnings("unchecked")
			final Map<String, String> params = (Map<String, String>) parameter.get("params");
			final String recordDate1 = params.get("recordDate1");
			final String recordDate2 = params.get("recordDate2");
			return new SQL(){{
				SELECT("eti.account_no ,"
						+ "eti.trans_type ,"
						+ "eti.record_amount ,"
						+ "eti.balance ,"
						+ "eti.avali_balance ,"
						+ "eti.control_amount ,"
						+ "eti.settling_amount ,"
						+ "eti.pre_freeze_amount ,"
						+ "eti.serial_no ,"
						+ "eti.child_serial_no ,"
						+ "eti.record_date ,"
						+ "eti.record_time ,"
						+ "eti.debit_credit_side ,"
						+ "eti.summary_info ,"
						+ "bea.balance_add_from ");
				FROM("ext_trans_info as eti,bill_ext_account as bea ");
				WHERE("eti.account_no = bea.account_no");
				if (StringUtils.isNotBlank(extTransInfo.getAccountNo()))
					WHERE(" eti.account_no = #{extTransInfo.accountNo} ");
				if (recordDate1 != null && StringUtils.isNotBlank(recordDate1))
					WHERE(" eti.record_date >= #{params.recordDate1} ");
				if (recordDate2 != null && StringUtils.isNotBlank(recordDate2))
					WHERE(" eti.record_date <= #{params.recordDate2} ");
				ORDER_BY(" eti.id desc ") ;
				
			}}.toString();
		}
		
		
		@SuppressWarnings("unchecked")
		public String findAllExtTransInfo(final Map<String, Object> parameter) {
			final ExtTransInfo extTransInfo = (ExtTransInfo) parameter.get("extTransInfo");
			final Map<String, String> params = (Map<String, String>) parameter.get("params");
			final Sort sord=(Sort)parameter.get("sort");
			final String recordDate1 = params.get("recordDate1");
			final String recordDate2 = params.get("recordDate2");
//			for (Map.Entry<String, Object> entry : parameter.entrySet()) {
//				log.info("{}={}", new Object[] { entry.getKey(), entry.getValue() });
//			}
			return new SQL(){{
				SELECT("eti.account_no,"
						+ "eti.record_amount,"
						+ "eti.balance,"
						+ "eti.avali_balance,"
						+ "eti.serial_no,"
						+ "eti.child_serial_no,"
						+ "eti.record_date,"
						+ "eti.record_time,"
						+ "eti.debit_credit_side,"
						+ "eti.summary_info");
				FROM("ext_trans_info as eti,bill_ext_account as bea,ext_account_info as eai ");
				WHERE("eti.account_no = bea.account_no and bea.account_no = eai.account_no ");
				if (StringUtils.isNotBlank(extTransInfo.getAccountNo()))
					WHERE(" eti.account_no  = #{extTransInfo.accountNo} ");
				if (extTransInfo.getExtAccount()!= null && StringUtils.isNotBlank(extTransInfo.getExtAccount().getSubjectNo()))
					WHERE(" bea.subject_no = #{extTransInfo.extAccount.subjectNo} ");
				if (extTransInfo.getExtAccount()!= null && extTransInfo.getExtAccount().getExtAccountInfo()!= null 
						&& StringUtils.isNotBlank(extTransInfo.getExtAccount().getExtAccountInfo().getUserId()))
					WHERE(" eai.user_id like  \"%\"#{extTransInfo.extAccount.extAccountInfo.userId}\"%\" ");
				if (extTransInfo.getExtAccount()!= null && StringUtils.isNotBlank(extTransInfo.getExtAccount().getCurrencyNo()))
					WHERE(" bea.currency_no = #{extTransInfo.extAccount.currencyNo} ");
				if (extTransInfo.getExtAccount()!= null && StringUtils.isNotBlank(extTransInfo.getExtAccount().getOrgNo()))
					WHERE(" bea.org_no = #{extTransInfo.extAccount.orgNo} ");
				if (recordDate1 != null && StringUtils.isNotBlank(recordDate1))
					WHERE(" eti.record_date >= #{params.recordDate1} ");
				if (recordDate2 != null && StringUtils.isNotBlank(recordDate2))
					WHERE(" eti.record_date <= #{params.recordDate2} ");
				if(StringUtils.isNotBlank(sord.getSidx())){
					ORDER_BY(propertyMapping(sord.getSidx(), 0)+" "+sord.getSord());
				}else{
					ORDER_BY(" record_date desc , record_time desc ") ;
				}
			}}.toString();
		}
		public String propertyMapping(String name,int type){
			final String[] propertys={"currBalance","availBalance","controlAmount","settlingAmount","preFreezeAmount","recordDate","recordTime","recordAmount","balance","avaliBalance","createTime","creator"};
		    final String[] columns={"curr_balance","avail_balance","control_amount","settling_amount","pre_freeze_amount","record_date","record_time","record_amount","balance","avali_balance","create_time","creator"};
		    if(StringUtils.isNotBlank(name)){
		    	if(type==0){//属性查出字段名
		    		for(int i=0;i<propertys.length;i++){
		    			if(name.equalsIgnoreCase(propertys[i])){
		    				return columns[i];
		    			}
		    		}
		    	}else if(type==1){//字段名查出属性
		    		for(int i=0;i<propertys.length;i++){
		    			if(name.equalsIgnoreCase(columns[i])){
		    				return propertys[i];
		    			}
		    		}
		    	}
		    }
			return null;
		}
	}

	


}
