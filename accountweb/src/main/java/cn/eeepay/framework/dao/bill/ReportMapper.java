package cn.eeepay.framework.dao.bill;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.CoreTransInfo;
import cn.eeepay.framework.model.bill.ExtAccountHistoryBalance;
import cn.eeepay.framework.model.bill.InsAccountHistoryBalance;
import cn.eeepay.framework.model.bill.SubjectInfo;
import cn.eeepay.framework.model.bill.TransImportInfo;
/**
 * 报表管理  DAO
 * @author Administrator
 *
 */
public interface ReportMapper {
	
	@SelectProvider( type=SqlProvider.class,method="findSubjectDayAmountList")
	@Results(value = {  
            @Result(id = true, property = "orgNo", column = "org_no"),  
            @Result(property = "currencyNo", column = "currency_no"),  
            @Result(property = "subjectNo", column = "subject_no"),
            @Result(property = "createDate", column = "create_date"),  
            @Result(property = "balanceFrom", column = "balance_from"),  
            @Result(property = "yesterdayAmount", column = "yesterday_amount"),  
            @Result(property = "todayCreditAmount", column = "today_credit_amount"),  
            @Result(property = "todayDebitAmount", column = "today_debit_amount"),
            @Result(property = "todayBalance", column = "today_balance"),  
            @Result(property = "subjectName", column = "subject_name"),  
            @Result(property = "subjectLevel", column = "subject_level"),  
        }) 
	List<Map<String, Object>> findSubjectDayAmountList(@Param("subjectInfo")SubjectInfo subjectInfo,@Param("sort")Sort sort,Page<Map<String, Object>> page,@Param("params")Map<String, String> params);
	
	@SelectProvider( type=SqlProvider.class,method="findSubjectDayAmountExport")
	@Results(value = {  
            @Result(id = true, property = "orgNo", column = "org_no"),  
            @Result(property = "currencyNo", column = "currency_no"),  
            @Result(property = "subjectNo", column = "subject_no"),
            @Result(property = "createDate", column = "create_date"),  
            @Result(property = "balanceFrom", column = "balance_from"),  
            @Result(property = "yesterdayAmount", column = "yesterday_amount"),  
            @Result(property = "todayCreditAmount", column = "today_credit_amount"),  
            @Result(property = "todayDebitAmount", column = "today_debit_amount"),
            @Result(property = "todayBalance", column = "today_balance"),  
            @Result(property = "subjectName", column = "subject_name"),  
            @Result(property = "subjectLevel", column = "subject_level"),  
        }) 
	List<Map<String, Object>> exportSubjectDayAmount(@Param("subjectInfo")SubjectInfo subjectInfo,@Param("params")Map<String, String> params);
	
	@SelectProvider( type=SqlProvider.class,method="findInAccountHistoryAmount")
	@Results(value = {  
            @Result(id = true, property = "accountNo", column = "account_no"),  
            @Result(property = "billDate", column = "bill_date"),  
            @Result(property = "currencyNo", column = "currency_no"),  
            @Result(property = "subjectNo", column = "subject_no"),  
            @Result(property = "accountName", column = "account_name"),  
            @Result(property = "currBalance", column = "curr_balance"),  
            @Result(property = "controlAmount", column = "control_amount"),
            @Result(property = "parentTransBalance", column = "parent_trans_balance"),  
            @Result(property = "accountStatus", column = "account_status"),  
            @Result(property = "orgNo", column = "org_no"),  
            @Result(property = "orgName", column = "org_name"),  
        }) 
	List<Map<String, Object>> findInAccountHistoryAmount(@Param("iaHistoryBalance")InsAccountHistoryBalance iaHistoryBalance,@Param("sort")Sort sort,Page<Map<String, Object>> page,@Param("params")Map<String, String> params);
	
	@SelectProvider( type=SqlProvider.class,method="findInAccountHistoryAmountExport")
	@Results(value = {  
            @Result(id = true, property = "accountNo", column = "account_no"),  
            @Result(property = "billDate", column = "bill_date"),  
            @Result(property = "currencyNo", column = "currency_no"),  
            @Result(property = "subjectNo", column = "subject_no"),  
            @Result(property = "accountName", column = "account_name"),  
            @Result(property = "currBalance", column = "curr_balance"),  
            @Result(property = "controlAmount", column = "control_amount"),
            @Result(property = "parentTransBalance", column = "parent_trans_balance"),  
            @Result(property = "accountStatus", column = "account_status"),  
            @Result(property = "orgNo", column = "org_no"),  
            @Result(property = "orgName", column = "org_name"),  
        }) 
	List<Map<String, Object>> exportInAccountHistoryAmount(@Param("iaHistoryBalance")InsAccountHistoryBalance iaHistoryBalance,@Param("params")Map<String, String> params);

	@SelectProvider( type=SqlProvider.class,method="findExtAccountHistoryAmount")
	@Results(value = {  
            @Result(id = true, property = "accountNo", column = "account_no"),  
            @Result(property = "userId", column = "user_id"),  
            @Result(property = "accountType", column = "account_type"),  
            @Result(property = "billDate", column = "bill_date"), 
            @Result(property = "currencyNo", column = "currency_no"),  
            @Result(property = "subjectNo", column = "subject_no"),  
            @Result(property = "currBalance", column = "curr_balance"),  
            @Result(property = "controlAmount", column = "control_amount"),
            @Result(property = "parentTransBalance", column = "parent_trans_balance"),  
            @Result(property = "accountStatus", column = "account_status"),  
            @Result(property = "orgNo", column = "org_no"),  
            @Result(property = "orgName", column = "org_name"),  
        }) 
	List<Map<String, Object>> findExtAccountHistoryAmount(@Param("eaHistoryBalance")ExtAccountHistoryBalance eaHistoryBalance,@Param("sort")Sort sort
			,Page<Map<String, Object>> page,@Param("params")Map<String, String> params, @Param("userNoStrs")String userNoStrs);
	
	@SelectProvider( type=SqlProvider.class,method="findExtAccountHistoryAmountExport")
	@Results(value = {  
            @Result(id = true, property = "accountNo", column = "account_no"),  
            @Result(property = "userId", column = "user_id"),  
            @Result(property = "accountType", column = "account_type"),  
            @Result(property = "billDate", column = "bill_date"), 
            @Result(property = "currencyNo", column = "currency_no"),  
            @Result(property = "subjectNo", column = "subject_no"),  
            @Result(property = "currBalance", column = "curr_balance"),  
            @Result(property = "controlAmount", column = "control_amount"),
            @Result(property = "parentTransBalance", column = "parent_trans_balance"),  
            @Result(property = "accountStatus", column = "account_status"),  
            @Result(property = "orgNo", column = "org_no"),  
            @Result(property = "orgName", column = "org_name"),  
        }) 
	List<Map<String, Object>> exportExtAccountHistoryAmount(@Param("eaHistoryBalance")ExtAccountHistoryBalance eaHistoryBalance,
			@Param("params")Map<String, String> params, @Param("userNoStrs")String userNoStrs);

	@SelectProvider( type=SqlProvider.class,method="findTransFlow")
	@ResultMap("cn.eeepay.framework.dao.bill.TransImportInfoMapper.BaseResultMap")
	List<TransImportInfo> findTransFlow(@Param("transImportInfo")TransImportInfo transImportInfo,@Param("sort")Sort sort,Page<TransImportInfo> page,@Param("params")Map<String, String> params);

	@SelectProvider( type=SqlProvider.class,method="findTransFlowExport")
	@ResultMap("cn.eeepay.framework.dao.bill.TransImportInfoMapper.BaseResultMap")
	List<TransImportInfo> exportTransFlow(@Param("transImportInfo")TransImportInfo transImportInfo,@Param("params")Map<String, String> params);

	@SelectProvider( type=SqlProvider.class,method="findRecordFlow")
	@ResultMap("cn.eeepay.framework.dao.bill.CoreTransInfoMapper.BaseResultMap")
	List<CoreTransInfo> findRecordFlow(@Param("coreTransInfo")CoreTransInfo coreTransInfo,@Param("sort")Sort sort,Page<CoreTransInfo> page,@Param("params")Map<String, String> params);

	@SelectProvider( type=SqlProvider.class,method="findRecordFlowExport")
	@ResultMap("cn.eeepay.framework.dao.bill.CoreTransInfoMapper.BaseResultMap")
	List<CoreTransInfo> exportRecordFlow(@Param("coreTransInfo")CoreTransInfo coreTransInfo,@Param("params")Map<String, String> params);

	@Select("select * from trans_import_info where id=#{id}")
	@ResultType(TransImportInfo.class)
	TransImportInfo getById(@Param("id")Integer id);
	
	
	public class SqlProvider{
		
		private static final Logger log = LoggerFactory.getLogger(SqlProvider.class);
		
		public String findSubjectDayAmountList(final Map<String, Object> parameter) {
			final SubjectInfo subjectInfo = (SubjectInfo) parameter.get("subjectInfo");
			final Sort sord=(Sort)parameter.get("sort");
			@SuppressWarnings("unchecked")
			final Map<String, String> params = (Map<String, String>) parameter.get("params");
			final String beginDate = params.get("beginDate");
			final String endDate = params.get("endDate");

			return new SQL(){{
				SELECT(" bsi.org_no ,"
						+ " bsi.currency_no ,"
						+ " bs.subject_no ,"
						+ " bsi.create_date ,"
						+ " bs.balance_from ,"
						+ " bsi.yesterday_amount ,"
						+ " bsi.today_credit_amount ,"
						+ " bsi.today_debit_amount ,"
						+ " bsi.today_balance ,"
						+ " bs.subject_name ,"
						+ " bs.subject_level ");						
				FROM(" bill_subject_info as bsi,bill_subject as bs ");
				WHERE(" bsi.subject_no = bs.subject_no ") ;
				if (!StringUtils.isBlank(subjectInfo.getSubjectNo()))
				WHERE("  bsi.subject_no like  \"%\"#{subjectInfo.subjectNo}\"%\" ");	
				if (subjectInfo.getSubjectLevel() != null && !subjectInfo.getSubjectLevel().equals(9999))
					WHERE(" bsi.subject_level = #{subjectInfo.subjectLevel} ");	
				if (!StringUtils.isBlank(subjectInfo.getOrgNo()))
					WHERE("  bsi.org_no like  \"%\"#{subjectInfo.orgNo}\"%\" ");
				if (!StringUtils.isBlank(subjectInfo.getCurrencyNo()) && !subjectInfo.getCurrencyNo().equals("ALL"))
					WHERE(" bsi.currency_no = #{subjectInfo.currencyNo} ");	
				if (beginDate != null && StringUtils.isNotBlank(beginDate))
					WHERE(" bsi.create_date >= #{params.beginDate} ");
				if (endDate != null && StringUtils.isNotBlank(endDate))
					WHERE(" bsi.create_date <= #{params.endDate} ");
				if(StringUtils.isNotBlank(sord.getSidx())){
					String sidx = propertyMapping(sord.getSidx(), 0);
					if (sidx != null) {
						ORDER_BY(sidx +" " + sord.getSord());
					}
					else{
						log.info("没有找到对应的排序对应属性");
					}
					
				}else{
					ORDER_BY(" create_date desc");
				}
			}}.toString();
		}

		public String findSubjectDayAmountExport(final Map<String, Object> parameter) {
			final SubjectInfo subjectInfo = (SubjectInfo) parameter.get("subjectInfo");
			@SuppressWarnings("unchecked")
			final Map<String, String> params = (Map<String, String>) parameter.get("params");
			final String beginDate = params.get("beginDate");
			final String endDate = params.get("endDate");

			return new SQL(){{
				SELECT("bsi.org_no,bsi.currency_no,bsi.subject_no,bsi.create_date,bsi.balance_from,"
						+ "bsi.yesterday_amount,bsi.today_credit_amount,bsi.today_debit_amount,bsi.today_balance,bs.subject_name,bsi.subject_level");						
				FROM(" bill_subject_info as bsi,bill_subject as bs ");
				WHERE(" bsi.subject_no = bs.subject_no ") ;
				if (!StringUtils.isBlank(subjectInfo.getSubjectNo()))
				WHERE("  bsi.subject_no like  \"%\"#{subjectInfo.subjectNo}\"%\" ");		
				if (subjectInfo.getSubjectLevel() != null && !subjectInfo.getSubjectLevel().equals(9999))
					WHERE(" bsi.subject_level = #{subjectInfo.subjectLevel} ");	
				if (!StringUtils.isBlank(subjectInfo.getOrgNo()))
					WHERE("  bsi.org_no like  \"%\"#{subjectInfo.orgNo}\"%\" ");
				if (!StringUtils.isBlank(subjectInfo.getCurrencyNo()) && !subjectInfo.getCurrencyNo().equals("ALL"))
					WHERE(" bsi.currency_no = #{subjectInfo.currencyNo} ");	
				if (beginDate != null && StringUtils.isNotBlank(beginDate))
					WHERE(" bsi.create_date >= #{params.beginDate} ");
				if (endDate != null && StringUtils.isNotBlank(endDate))
					WHERE(" bsi.create_date <= #{params.endDate} ");
					
				ORDER_BY(" create_date desc");
			}}.toString();
		}
		
		public String findInAccountHistoryAmount(final Map<String, Object> parameter) {
			final InsAccountHistoryBalance iaHistoryBalance = (InsAccountHistoryBalance) parameter.get("iaHistoryBalance");
			final Sort sord=(Sort)parameter.get("sort");
			@SuppressWarnings("unchecked")
			final Map<String, String> params = (Map<String, String>) parameter.get("params");
			final String beginDate = params.get("beginDate");
			final String endDate = params.get("endDate");

			return new SQL(){{
				SELECT("bihb.bill_date,bihb.account_no,bihb.currency_no,bihb.subject_no,bihb.account_name,bihb.curr_balance,bihb.control_amount,"
						+ "bihb2.curr_balance as parent_trans_balance,bihb.account_status,bihb.org_no,oi.org_name");						
				FROM(" bill_ins_history_balance as bihb INNER JOIN org_info as oi ON bihb.org_no = oi.org_no LEFT JOIN bill_ins_history_balance bihb2 ON bihb2.account_no=bihb.account_no and bihb2.bill_date=DATE_SUB(bihb.bill_date, INTERVAL 1 DAY) ");
				if (!StringUtils.isBlank(iaHistoryBalance.getOrgNo()))
					WHERE("  bihb.org_no like  \"%\"#{iaHistoryBalance.orgNo}\"%\" ");	
				if (!StringUtils.isBlank(iaHistoryBalance.getSubjectNo()))
					WHERE("  bihb.subject_no like  \"%\"#{iaHistoryBalance.subjectNo}\"%\" ");
				if (!StringUtils.isBlank(iaHistoryBalance.getAccountNo()))
					WHERE("  bihb.account_no like  \"%\"#{iaHistoryBalance.accountNo}\"%\" ");
				if (!StringUtils.isBlank(iaHistoryBalance.getCurrencyNo()) && !iaHistoryBalance.getCurrencyNo().equals("ALL"))
					WHERE(" bihb.currency_no = #{iaHistoryBalance.currencyNo} ");	
				if (beginDate != null && StringUtils.isNotBlank(beginDate))
					WHERE(" bihb.bill_date >= #{params.beginDate} ");
				if (endDate != null && StringUtils.isNotBlank(endDate))
					WHERE(" bihb.bill_date <= #{params.endDate} ");
				if(StringUtils.isNotBlank(sord.getSidx())){
					ORDER_BY(AccountPropertyMapping(sord.getSidx(), 0)+" "+sord.getSord());
				}else{
					ORDER_BY(" bill_date desc");
				}
			}}.toString();
		}

		public String findInAccountHistoryAmountExport(final Map<String, Object> parameter) {
			final InsAccountHistoryBalance iaHistoryBalance = (InsAccountHistoryBalance) parameter.get("iaHistoryBalance");
			@SuppressWarnings("unchecked")
			final Map<String, String> params = (Map<String, String>) parameter.get("params");
			final String beginDate = params.get("beginDate");
			final String endDate = params.get("endDate");

			return new SQL(){{
				SELECT("bihb.bill_date,bihb.account_no,bihb.currency_no,bihb.subject_no,bihb.account_name,bihb.curr_balance,bihb.control_amount,"
						+ "bihb.parent_trans_balance,bihb.account_status,bihb.org_no,oi.org_name");						
				FROM(" bill_ins_history_balance as bihb,org_info as oi ");
				WHERE(" bihb.org_no = oi.org_no ") ;
				if (!StringUtils.isBlank(iaHistoryBalance.getOrgNo()))
				WHERE("  bihb.org_no like  \"%\"#{iaHistoryBalance.orgNo}\"%\" ");	
				if (!StringUtils.isBlank(iaHistoryBalance.getSubjectNo()))
					WHERE("  bihb.subject_no like  \"%\"#{iaHistoryBalance.subjectNo}\"%\" ");
				if (!StringUtils.isBlank(iaHistoryBalance.getAccountNo()))
					WHERE("  bihb.account_no like  \"%\"#{iaHistoryBalance.accountNo}\"%\" ");
				if (!StringUtils.isBlank(iaHistoryBalance.getCurrencyNo()) && !iaHistoryBalance.getCurrencyNo().equals("ALL"))
					WHERE(" bihb.currency_no = #{iaHistoryBalance.currencyNo} ");	
				if (beginDate != null && StringUtils.isNotBlank(beginDate))
					WHERE(" bihb.bill_date >= #{params.beginDate} ");
				if (endDate != null && StringUtils.isNotBlank(endDate))
					WHERE(" bihb.bill_date <= #{params.endDate} ");
				
				ORDER_BY(" bill_date desc");
			}}.toString();
		}

		
		public String findExtAccountHistoryAmount(final Map<String, Object> parameter) {
			final ExtAccountHistoryBalance eaHistoryBalance = (ExtAccountHistoryBalance) parameter.get("eaHistoryBalance");

			final Sort sord=(Sort)parameter.get("sort");
			@SuppressWarnings("unchecked")
			final Map<String, String> params = (Map<String, String>) parameter.get("params");
			final String beginDate = params.get("beginDate");
			final String endDate = params.get("endDate");
			final String userNoStrs = (String) parameter.get("userNoStrs") ;
			
			String sql = new SQL(){{
				SELECT("eai.user_id,eai.account_type,behb.bill_date,behb.account_no,behb.currency_no,behb.subject_no,behb.curr_balance,behb.control_amount,"
						+ "behb2.curr_balance as parent_trans_balance,behb.account_status,behb.org_no,oi.org_name");						
				FROM(" bill_ext_history_balance as behb inner join org_info as oi on behb.org_no = oi.org_no inner join ext_account_info as eai on behb.account_no = eai.account_no left join bill_ext_history_balance behb2 on behb2.account_no=behb.account_no and behb2.bill_date=DATE_SUB(behb.bill_date, INTERVAL 1 DAY)  ");
				if (!StringUtils.isBlank(eaHistoryBalance.getAccountNo()))
					WHERE("  behb.account_no =  #{eaHistoryBalance.accountNo} ");
				if (!StringUtils.isBlank(eaHistoryBalance.getExtAccountInfo().getUserId()))
				WHERE("  eai.user_id =  #{eaHistoryBalance.extAccountInfo.userId}");
				if (!StringUtils.isBlank(eaHistoryBalance.getSubjectNo()))
					WHERE("  behb.subject_no = #{eaHistoryBalance.subjectNo} ");
				if (!StringUtils.isBlank(eaHistoryBalance.getCurrencyNo()) && !eaHistoryBalance.getCurrencyNo().equals("ALL"))
					WHERE(" behb.currency_no = #{eaHistoryBalance.currencyNo} ");	
				if (beginDate != null && StringUtils.isNotBlank(beginDate))
					WHERE(" behb.bill_date >= #{params.beginDate} ");
				if (endDate != null && StringUtils.isNotBlank(endDate))
					WHERE(" behb.bill_date <= #{params.endDate} ");
				if (eaHistoryBalance.getExtAccountInfo() != null && StringUtils.isNotBlank(eaHistoryBalance.getExtAccountInfo().getAccountType()) && !eaHistoryBalance.getExtAccountInfo().getAccountType().equals("ALL"))
					WHERE(" eai.account_type = #{eaHistoryBalance.extAccountInfo.accountType} ");
			/*	if(!"isBlank".equalsIgnoreCase(userNoStrs))
					WHERE(" eai.user_id in ("+userNoStrs+")") ;
				*/
				if(StringUtils.isNotBlank(sord.getSidx())){
					ORDER_BY(behbAccountPropertyMapping(sord.getSidx(), 0)+" "+sord.getSord());
				}else{
					ORDER_BY(" behb.bill_date desc");
				}
			}}.toString();
			
			return sql;
		}
		
		public String findExtAccountHistoryAmountExport(final Map<String, Object> parameter) {
			final ExtAccountHistoryBalance eaHistoryBalance = (ExtAccountHistoryBalance) parameter.get("eaHistoryBalance");
			@SuppressWarnings("unchecked")
			final Map<String, String> params = (Map<String, String>) parameter.get("params");
			final String beginDate = params.get("beginDate");
			final String endDate = params.get("endDate");
			final String userNoStrs = (String) parameter.get("userNoStrs") ;
			
			return new SQL(){{
				SELECT("eai.user_id,eai.account_type,behb.bill_date,behb.account_no,behb.currency_no,behb.subject_no,behb.curr_balance,behb.control_amount,"
						+ "behb.parent_trans_balance,behb.account_status,behb.org_no,oi.org_name");						
				FROM(" bill_ext_history_balance as behb,org_info as oi,ext_account_info as eai ");
				WHERE(" behb.org_no = oi.org_no AND behb.account_no = eai.account_no ") ;
				if (!StringUtils.isBlank(eaHistoryBalance.getAccountNo()))
					WHERE("  behb.account_no = #{eaHistoryBalance.accountNo}");
				if (!StringUtils.isBlank(eaHistoryBalance.getExtAccountInfo().getUserId()))
				WHERE("  eai.user_id = #{eaHistoryBalance.extAccountInfo.userId}");
				if (!StringUtils.isBlank(eaHistoryBalance.getSubjectNo()))
					WHERE("  behb.subject_no l= #{eaHistoryBalance.subjectNo} ");
				if (!StringUtils.isBlank(eaHistoryBalance.getCurrencyNo()) && !eaHistoryBalance.getCurrencyNo().equals("ALL"))
					WHERE(" behb.currency_no = #{eaHistoryBalance.currencyNo} ");	
				if (beginDate != null && StringUtils.isNotBlank(beginDate))
					WHERE(" behb.bill_date >= #{params.beginDate} ");
				if (endDate != null && StringUtils.isNotBlank(endDate))
					WHERE(" behb.bill_date <= #{params.endDate} ");
				if (eaHistoryBalance.getExtAccountInfo() != null && StringUtils.isNotBlank(eaHistoryBalance.getExtAccountInfo().getAccountType()) && !eaHistoryBalance.getExtAccountInfo().getAccountType().equals("ALL"))
					WHERE(" eai.account_type = #{eaHistoryBalance.extAccountInfo.accountType} ");
				if(!"isBlank".equalsIgnoreCase(userNoStrs))
					WHERE(" eai.user_id in ("+userNoStrs+")") ;	
				
				ORDER_BY(" bill_date desc");
			}}.toString();
		}

		public String findTransFlow(final Map<String, Object> parameter) {
			final TransImportInfo transImportInfo = (TransImportInfo) parameter.get("transImportInfo");
			final Sort sord=(Sort)parameter.get("sort");
			@SuppressWarnings("unchecked")
			final Map<String, String> params = (Map<String, String>) parameter.get("params");
			final String recordBeginDate = params.get("recordBeginDate");
			final String recordEndDate = params.get("recordEndDate");
			final String transBeginDate = params.get("transBeginDate");
			final String transEndDate = params.get("transEndDate");
			final String transBeginAmount = params.get("transBeginAmount");
			final String transEndAmount = params.get("transEndAmount");

			return new SQL(){{
				SELECT("*");						
				FROM(" trans_import_info");
				if (!StringUtils.isBlank(transImportInfo.getFromSystem()) && !transImportInfo.getFromSystem().equals("ALL"))
					WHERE(" from_system = #{transImportInfo.fromSystem} ");	
				if (!StringUtils.isBlank(transImportInfo.getReverseFlag()) && !transImportInfo.getReverseFlag().equals("ALL"))
					WHERE(" reverse_flag = #{transImportInfo.reverseFlag} ");
				if (!StringUtils.isBlank(transImportInfo.getReverseStatus()) && !transImportInfo.getReverseStatus().equals("ALL"))
					WHERE(" reverse_status = #{transImportInfo.reverseStatus} ");	
				if (!StringUtils.isBlank(transImportInfo.getRecordStatus()) && !transImportInfo.getRecordStatus().equals("ALL"))
					WHERE(" record_status = #{transImportInfo.recordStatus} ");	
				if (!StringUtils.isBlank(transImportInfo.getTransType()) && !transImportInfo.getTransType().equals("ALL"))
					WHERE(" trans_type = #{transImportInfo.transType} ");	
				if (StringUtils.isNotBlank(transImportInfo.getFromSerialNo())) 
					WHERE("  from_serial_no =  #{transImportInfo.fromSerialNo} ");
				if (StringUtils.isNotBlank(transImportInfo.getRecordSerialNo())) 
					WHERE("  record_serial_no =  #{transImportInfo.recordSerialNo} ");
				if (StringUtils.isNotBlank(transImportInfo.getTransOrderNo()))
					WHERE("  trans_order_no =  #{transImportInfo.transOrderNo}");
				if (!StringUtils.isBlank(transImportInfo.getCardNo()))
					WHERE("  card_no =  #{transImportInfo.cardNo} ");
				if (!StringUtils.isBlank(transImportInfo.getMerchantNo()))
					WHERE("  merchant_no =  #{transImportInfo.merchantNo}");
				if (!StringUtils.isBlank(transImportInfo.getDirectAgentNo()))
					WHERE("  direct_agent_no =   #{transImportInfo.directAgentNo}  ");
				if (!StringUtils.isBlank(transImportInfo.getAcqEnname()) && !transImportInfo.getAcqEnname().equals("ALL"))
					WHERE(" acq_enname = #{transImportInfo.acqEnname} ");	
				if (!StringUtils.isBlank(transImportInfo.getOutAcqEnname()) && !transImportInfo.getOutAcqEnname().equals("ALL"))
					WHERE(" out_acq_enname = #{transImportInfo.outAcqEnname} ");	
				if (recordBeginDate != null && StringUtils.isNotBlank(recordBeginDate))
					WHERE(" record_date >= #{params.recordBeginDate} ");
				if (recordEndDate != null && StringUtils.isNotBlank(recordEndDate))
					WHERE(" record_date <= #{params.recordEndDate} ");
				if (transBeginDate != null && StringUtils.isNotBlank(transBeginDate))
					WHERE(" trans_date >= #{params.transBeginDate} ");
				if (transEndDate != null && StringUtils.isNotBlank(transEndDate))
					WHERE(" trans_date <= #{params.transEndDate} ");
				if (transBeginAmount != null && StringUtils.isNotBlank(transBeginAmount))
					WHERE(" trans_amount >= #{params.transBeginAmount} ");
				if (transEndAmount != null && StringUtils.isNotBlank(transEndAmount))
					WHERE(" trans_amount <= #{params.transEndAmount} ");
				if(StringUtils.isNotBlank(sord.getSidx())){
					ORDER_BY(flowPropertyMapping(sord.getSidx(), 0)+" "+sord.getSord());
				}else{
					ORDER_BY(" id desc");
				}
			}}.toString();
		}

		public String findTransFlowExport(final Map<String, Object> parameter) {
			final TransImportInfo transImportInfo = (TransImportInfo) parameter.get("transImportInfo");
			@SuppressWarnings("unchecked")
			final Map<String, String> params = (Map<String, String>) parameter.get("params");
			final String recordBeginDate = params.get("recordBeginDate");
			final String recordEndDate = params.get("recordEndDate");
			final String transBeginDate = params.get("transBeginDate");
			final String transEndDate = params.get("transEndDate");
			final String transBeginAmount = params.get("transBeginAmount");
			final String transEndAmount = params.get("transEndAmount");

			return new SQL(){{
				SELECT("*");						
				FROM(" trans_import_info");
				if (!StringUtils.isBlank(transImportInfo.getFromSystem()) && !transImportInfo.getFromSystem().equals("ALL"))
					WHERE(" from_system = #{transImportInfo.fromSystem} ");	
				if (!StringUtils.isBlank(transImportInfo.getReverseFlag()) && !transImportInfo.getReverseFlag().equals("ALL"))
					WHERE(" reverse_flag = #{transImportInfo.reverseFlag} ");
				if (!StringUtils.isBlank(transImportInfo.getReverseStatus()) && !transImportInfo.getReverseStatus().equals("ALL"))
					WHERE(" reverse_status = #{transImportInfo.reverseStatus} ");	
				if (!StringUtils.isBlank(transImportInfo.getRecordStatus()) && !transImportInfo.getRecordStatus().equals("ALL"))
					WHERE(" record_status = #{transImportInfo.recordStatus} ");	
				if (!StringUtils.isBlank(transImportInfo.getTransType()) && !transImportInfo.getTransType().equals("ALL"))
					WHERE(" trans_type = #{transImportInfo.transType} ");	
				if (StringUtils.isNotBlank(transImportInfo.getFromSerialNo())) 
					WHERE("  from_serial_no =  #{transImportInfo.fromSerialNo} ");
				if (StringUtils.isNotBlank(transImportInfo.getRecordSerialNo())) 
					WHERE("  record_serial_no = #{transImportInfo.recordSerialNo}  ");
				if (StringUtils.isNotBlank(transImportInfo.getTransOrderNo()))
					WHERE("  trans_order_no =   #{transImportInfo.transOrderNo}  ");
				if (!StringUtils.isBlank(transImportInfo.getCardNo()))
					WHERE("  card_no =  #{transImportInfo.cardNo}  ");
				if (!StringUtils.isBlank(transImportInfo.getMerchantNo()))
					WHERE("  merchant_no =   #{transImportInfo.merchantNo}  ");
				if (!StringUtils.isBlank(transImportInfo.getDirectAgentNo()))
					WHERE("  direct_agent_no =   #{transImportInfo.directAgentNo}  ");
				if (!StringUtils.isBlank(transImportInfo.getAcqEnname()) && !transImportInfo.getAcqEnname().equals("ALL"))
					WHERE(" acq_enname = #{transImportInfo.acqEnname} ");	
				if (!StringUtils.isBlank(transImportInfo.getOutAcqEnname()) && !transImportInfo.getOutAcqEnname().equals("ALL"))
					WHERE(" out_acq_enname = #{transImportInfo.outAcqEnname} ");	
				if (recordBeginDate != null && StringUtils.isNotBlank(recordBeginDate))
					WHERE(" record_date >= #{params.recordBeginDate} ");
				if (recordEndDate != null && StringUtils.isNotBlank(recordEndDate))
					WHERE(" record_date <= #{params.recordEndDate} ");
				if (transBeginDate != null && StringUtils.isNotBlank(transBeginDate))
					WHERE(" trans_date >= #{params.transBeginDate} ");
				if (transEndDate != null && StringUtils.isNotBlank(transEndDate))
					WHERE(" trans_date <= #{params.transEndDate} ");
				if (transBeginAmount != null && StringUtils.isNotBlank(transBeginAmount))
					WHERE(" trans_amount >= #{params.transBeginAmount} ");
				if (transEndAmount != null && StringUtils.isNotBlank(transEndAmount))
					WHERE(" trans_amount <= #{params.transEndAmount} ");
					
				ORDER_BY(" id desc");
			}}.toString();
		}


		public String findRecordFlow(final Map<String, Object> parameter) {
			final CoreTransInfo coreTransInfo = (CoreTransInfo) parameter.get("coreTransInfo");
			final Sort sord=(Sort)parameter.get("sort");
			@SuppressWarnings("unchecked")
			final Map<String, String> params = (Map<String, String>) parameter.get("params");
			final String beginDate = params.get("beginDate");
			final String endDate = params.get("endDate");
			final String transType = params.get("transType");
			final String importIdStr = coreTransInfo.getImportId() + "" ;
			
			return new SQL(){{
				SELECT("cti.id,cti.trans_date,"
						+ " cti.serial_no,"
						+ " tii.trans_type,"
						+ " cti.account_type,"
						+ " cti.journal_no,"
						+ " cti.child_serial_no,"
						+ " cti.trans_amount,"
						+ " cti.debit_credit_side,"
						+ " cti.account_flag,"
						+ " cti.user_id,"
						+ " cti.reverse_flag,"
						+ " cti.account_no,"
						+ " cti.subject_no,"
						+ " cti.currency_no,"
						+ " cti.org_no,"
						+ " cti.summary_info,"
						+ " cti.import_id,"
						+ " (select bs.subject_name from bill_subject bs where bs.subject_no=cti.subject_no) as subject_name");						
				FROM(" core_trans_info cti left join trans_import_info tii on cti.import_id=tii.id ");
				if (!("null".equals(importIdStr)))
					WHERE("  cti.import_id = '"+importIdStr+"'");
				if (!StringUtils.isBlank(transType) && !transType.equals("ALL"))
					WHERE(" tii.trans_type = #{params.transType} ");
				if (!StringUtils.isBlank(coreTransInfo.getUserId()))
					WHERE("  cti.user_id =  #{coreTransInfo.userId}");
				if (!StringUtils.isBlank(coreTransInfo.getCurrencyNo()) && !coreTransInfo.getCurrencyNo().equals("ALL"))
					WHERE(" cti.currency_no = #{coreTransInfo.currencyNo} ");	
				if (!StringUtils.isBlank(coreTransInfo.getOrgNo()))
					WHERE("  cti.org_no = #{coreTransInfo.orgNo}");
				if (!StringUtils.isBlank(coreTransInfo.getAccountFlag()) && !coreTransInfo.getAccountFlag().equals("ALL"))
					WHERE(" cti.account_flag = #{coreTransInfo.accountFlag} ");	
				if (!StringUtils.isBlank(coreTransInfo.getReverseFlag()) && !coreTransInfo.getReverseFlag().equals("ALL"))
					WHERE(" cti.reverse_flag = #{coreTransInfo.reverseFlag} ");
				if (StringUtils.isNotBlank(coreTransInfo.getSerialNo())) 
					WHERE(" cti.serial_no = #{coreTransInfo.serialNo} ");
				if (StringUtils.isNotBlank(coreTransInfo.getAccountType()) && !"ALL".equals(coreTransInfo.getAccountType()))
					WHERE(" cti.account_type = #{coreTransInfo.accountType} ");
				if (!StringUtils.isBlank(coreTransInfo.getAccountNo()))
					WHERE("  cti.account_no =  #{coreTransInfo.accountNo} ");
				if (!StringUtils.isBlank(coreTransInfo.getDebitCreditSide()) && !coreTransInfo.getDebitCreditSide().equals("ALL"))
					WHERE(" cti.debit_credit_side = #{coreTransInfo.debitCreditSide} ");	
				if (!StringUtils.isBlank(coreTransInfo.getSubjectNo()))
					WHERE(" cti.subject_no =  #{coreTransInfo.subjectNo} ");
				if (beginDate != null && StringUtils.isNotBlank(beginDate))
					WHERE(" cti.trans_date >= #{params.beginDate} ");
				if (endDate != null && StringUtils.isNotBlank(endDate))
					WHERE(" cti.trans_date <= #{params.endDate} ");
				if(StringUtils.isNotBlank(sord.getSidx())){
					ORDER_BY(flowPropertyMapping(sord.getSidx(), 0)+" "+sord.getSord());
				}else{
					ORDER_BY(" cti.serial_no asc,cti.journal_no asc,cti.child_serial_no asc");
				}
			}}.toString();
		}

		public String findRecordFlowExport(final Map<String, Object> parameter) {
			final CoreTransInfo coreTransInfo = (CoreTransInfo) parameter.get("coreTransInfo");
			@SuppressWarnings("unchecked")
			final Map<String, String> params = (Map<String, String>) parameter.get("params");
			final String beginDate = params.get("beginDate");
			final String endDate = params.get("endDate");
			final String importIdStr = coreTransInfo.getImportId() + "" ;
			final String transType = params.get("transType");
			
			return new SQL(){{
				SELECT("cti.trans_date,cti.serial_no,tii.trans_type,cti.account_type,cti.journal_no,cti.child_serial_no,cti.trans_amount,cti.debit_credit_side,cti.account_flag,cti.user_id,cti.reverse_flag,cti.account_no,"
						+ "cti.subject_no,cti.currency_no,cti.org_no,cti.summary_info,cti.import_id,"
						+ "(select bs.subject_name from bill_subject bs where bs.subject_no=cti.subject_no) as subject_name");				
				FROM(" core_trans_info cti left join trans_import_info tii on cti.import_id=tii.id ");
				if (!("null".equals(importIdStr)))
					WHERE("  cti.import_id = '"+importIdStr+"' ");
				if (!StringUtils.isBlank(transType) && !transType.equals("ALL"))
					WHERE(" tii.trans_type = #{params.transType} ");
				if (!StringUtils.isBlank(coreTransInfo.getUserId()))
					WHERE("  cti.user_id =  #{coreTransInfo.userId}  ");
				if (!StringUtils.isBlank(coreTransInfo.getCurrencyNo()) && !coreTransInfo.getCurrencyNo().equals("ALL"))
					WHERE(" cti.currency_no = #{coreTransInfo.currencyNo} ");	
				if (!StringUtils.isBlank(coreTransInfo.getOrgNo()))
					WHERE("  cti.org_no =   #{coreTransInfo.orgNo} ");
				if (!StringUtils.isBlank(coreTransInfo.getAccountFlag()) && !coreTransInfo.getAccountFlag().equals("ALL"))
					WHERE(" cti.account_flag = #{coreTransInfo.accountFlag} ");	
				if (!StringUtils.isBlank(coreTransInfo.getReverseFlag()) && !coreTransInfo.getReverseFlag().equals("ALL"))
					WHERE(" cti.reverse_flag = #{coreTransInfo.reverseFlag} ");
				if (StringUtils.isNotBlank(coreTransInfo.getSerialNo())) 
					WHERE(" cti.serial_no =  #{coreTransInfo.serialNo}  ");
				if (StringUtils.isNotBlank(coreTransInfo.getAccountType()) && !"ALL".equals(coreTransInfo.getAccountType()))
					WHERE(" cti.account_type = #{coreTransInfo.accountType} ");
				if (!StringUtils.isBlank(coreTransInfo.getAccountNo()))
					WHERE("  cti.account_no =   #{coreTransInfo.accountNo}  ");
				if (!StringUtils.isBlank(coreTransInfo.getDebitCreditSide()) && !coreTransInfo.getDebitCreditSide().equals("ALL"))
					WHERE(" cti.debit_credit_side = #{coreTransInfo.debitCreditSide} ");	
				if (!StringUtils.isBlank(coreTransInfo.getSubjectNo()))
					WHERE(" cti.subject_no =   #{coreTransInfo.subjectNo}  ");
				if (beginDate != null && StringUtils.isNotBlank(beginDate))
					WHERE(" cti.trans_date >= #{params.beginDate} ");
				if (endDate != null && StringUtils.isNotBlank(endDate))
					WHERE(" cti.trans_date <= #{params.endDate} ");
					
				ORDER_BY(" cti.trans_date desc");
			}}.toString();
		}

		
		public String propertyMapping(String name,int type){
			final String[] propertys={"orgNo","currencyNo","subjectNo","subjectName","subjectLevel","createDate","balanceFrom",
								      "yesterdayAmount","todayDebitAmount","todayCreditAmount","todayBalance"};
		    final String[] columns={"org_no","currency_no","subject_no","subject_name","subject_level","create_date","balance_from",
		    						  "yesterday_amount","today_debit_amount","today_credit_amount","today_balance"};
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

		public String AccountPropertyMapping(String name,int type){
			final String[] propertys={"currBalance","controlAmount","parentTransBalance","billDate"};
		    final String[] columns={"curr_balance","control_amount","parent_trans_balance","bill_date"};
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
		
		public String behbAccountPropertyMapping(String name,int type){
			final String[] propertys={"currBalance","controlAmount","parentTransBalance","billDate"};
		    final String[] columns={"behb.curr_balance","behb.control_amount","behb.parent_trans_balance","behb.bill_date"};
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

		public String flowPropertyMapping(String name,int type){
			final String[] propertys={"recordDate","transDate","transAmount","merchantFee","agentShareAmount","importId"};
		    final String[] columns={"record_date","trans_date","trans_amount","merchant_fee","agent_share_amount","import_id"};
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
