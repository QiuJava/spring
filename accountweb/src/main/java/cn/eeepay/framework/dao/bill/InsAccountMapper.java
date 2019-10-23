package cn.eeepay.framework.dao.bill;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.InsAccount;
import cn.eeepay.framework.model.bill.InsideTransInfo;
/**
 * 内部账号
 * @author Administrator
 *
 */
public interface InsAccountMapper {
	
	@Insert("insert into bill_ins_account(account_no,org_no,currency_no,subject_no,account_name,curr_balance,control_amount,parent_trans_day,parent_trans_balance,account_status,create_time,balance_add_from,balance_from,day_bal_flag,sum_flag,creator)"
			+"values(#{insAccount.accountNo},#{insAccount.orgNo},#{insAccount.currencyNo},#{insAccount.subjectNo},#{insAccount.accountName},#{insAccount.currBalance},#{insAccount.controlAmount},#{insAccount.parentTransDay},#{insAccount.parentTransBalance},#{insAccount.accountStatus},#{insAccount.createTime},#{insAccount.balanceAddFrom},#{insAccount.balanceFrom},#{insAccount.dayBalFlag},#{insAccount.sumFlag},#{insAccount.creator})"
			)
	int insertInsAccount(@Param("insAccount")InsAccount insAccount);
	
	@Insert("update bill_ins_account set org_no=#{insAccount.orgNo},currency_no=#{insAccount.currencyNo},subject_no=#{insAccount.subjectNo},account_name=#{insAccount.accountName},curr_balance=#{insAccount.currBalance},"
			+ "control_amount=#{insAccount.controlAmount},parent_trans_day=#{insAccount.parentTransDay},parent_trans_balance=#{insAccount.parentTransBalance},account_status=#{insAccount.accountStatus},create_time=#{insAccount.createTime},"
			+ "balance_add_from=#{insAccount.balanceAddFrom},balance_from=#{insAccount.balanceFrom},day_bal_flag=#{insAccount.dayBalFlag},sum_flag=#{insAccount.sumFlag} where account_no=#{insAccount.accountNo}"
			)
	int updateInsAccount(@Param("insAccount")InsAccount insAccount);
	
	@Select("select * from bill_ins_account")
	@ResultType(InsAccount.class)
	List<InsAccount> findAll();
	
	@Select("select account_no,org_no,currency_no,subject_no,account_name,curr_balance,control_amount,parent_trans_day,parent_trans_balance,account_status,create_time,balance_add_from,balance_from,day_bal_flag,sum_flag,creator from bill_ins_account where account_no = #{accountNo}")
	@ResultMap("cn.eeepay.framework.dao.bill.InsAccountMapper.BaseResultMap")
	InsAccount getInsAccountByAccountNo(@Param("accountNo")String accountNo);
	
	
	@Select("select account_no,org_no,currency_no,subject_no,account_name,curr_balance,control_amount,parent_trans_day,parent_trans_balance,account_status,create_time,balance_add_from,balance_from,day_bal_flag,sum_flag,creator from bill_ins_account where day_bal_flag = #{dayBalFlag}")
	@ResultMap("cn.eeepay.framework.dao.bill.InsAccountMapper.BaseResultMap")
	List<InsAccount> findInsAccountByDayBalFlag(@Param("dayBalFlag")String dayBalFlag);
	
	@Select("select account_no,org_no,currency_no,subject_no,account_name,curr_balance,control_amount,parent_trans_day,parent_trans_balance,account_status,create_time,balance_add_from,balance_from,day_bal_flag,sum_flag,creator from bill_ins_account where subject_no = #{subjectNo}")
	@ResultMap("cn.eeepay.framework.dao.bill.InsAccountMapper.BaseResultMap")
	List<InsAccount> findInsAccountBySubjectNo(@Param("subjectNo")String subjectNo);
	
	@Select("select account_no,org_no,currency_no,subject_no,account_name,curr_balance,control_amount,parent_trans_day,parent_trans_balance,account_status,create_time,balance_add_from,balance_from,day_bal_flag,sum_flag,creator from bill_ins_account where org_no = #{orgNo} and subject_no = #{subjectNo} and currency_no = #{currencyNo}")
	@ResultMap("cn.eeepay.framework.dao.bill.InsAccountMapper.BaseResultMap")
	InsAccount getInsAccountByParams(@Param("orgNo")String orgNo,@Param("subjectNo")String subjectNo,@Param("currencyNo")String currencyNo);
	
	@Select("select account_no,org_no,currency_no,subject_no,account_name,curr_balance,control_amount,parent_trans_day,parent_trans_balance,account_status,create_time,balance_add_from,balance_from,day_bal_flag,sum_flag,creator from bill_ins_account where currency_no = #{currencyNo} AND subject_no= #{subjectNo} AND org_no=#{org_no}")
	@ResultMap("cn.eeepay.framework.dao.bill.InsAccountMapper.BaseResultMap")
	InsAccount exsitsSubject(@Param("currencyNo")String currencyNo,@Param("subjectNo") String subjectNo,
			@Param("org_no") String org_no);	
	
	@SelectProvider( type=SqlProvider.class,method="findInsAccountListInfo")
	@ResultMap("cn.eeepay.framework.dao.bill.InsAccountMapper.OneToOneResultMap")
	List<InsAccount> findInsAccountListInfo(@Param("insAccount")InsAccount  insAccount,@Param("sort")Sort sort,Page<InsAccount> page);
	
	@Select("SELECT ba.account_no,ba.org_no,ba.currency_no,ba.subject_no,ba.account_name,ba.curr_balance,"
			+"bs.subject_name FROM bill_ins_account ba ,bill_subject bs WHERE ba.subject_no=bs.subject_no ")
	@ResultMap("cn.eeepay.framework.dao.bill.InsAccountMapper.OneToOneResultMap")
	public List<InsAccount> findInsAccountList();
	
	
	@SelectProvider( type=SqlProvider.class,method="findInsideTransList")
	@ResultMap("cn.eeepay.framework.dao.bill.InsideTransInfoMapper.BaseResultMap")
	public  List<InsideTransInfo> findInsideTransList(@Param("insideTransInfo")InsideTransInfo insideTransInfo,@Param("params")Map<String, String> params ,@Param("sort")Sort sort,Page<InsideTransInfo> page);
	
	/**
	 * 导出   内部账户交易  
	 * @param insideTransInfo
	 * @param params
	 * @return
	 */
	@SelectProvider( type=SqlProvider.class,method="findExportInsideTransList")
	@ResultMap("cn.eeepay.framework.dao.bill.InsideTransInfoMapper.BaseResultMap")
	public  List<InsideTransInfo> findExportInsideTransList(@Param("insideTransInfo")InsideTransInfo insideTransInfo,@Param("params")Map<String, String> params );
	 
	/**
	 * 导出  内部账户  
	 * @param insAccount
	 * @return
	 */
	@SelectProvider( type=SqlProvider.class,method="findExportInsAccountList")
	@ResultMap("cn.eeepay.framework.dao.bill.InsAccountMapper.OneToOneResultMap")
	public  List<InsAccount> findExportInsAccountList(@Param("insAccount")InsAccount insAccount );
	 
	
	public class SqlProvider{
		
		public String findExportInsideTransList(final Map<String, Object> parameter) {
			final InsideTransInfo insideTransInfo = (InsideTransInfo) parameter.get("insideTransInfo");
			@SuppressWarnings("unchecked")
			final Map<String, String> params = (Map<String, String>) parameter.get("params");
			final String beginDate = params.get("beginDate");
			final String endDate = params.get("endDate");

			return new SQL(){{
				SELECT("account_no,"
						+ "record_amount,"
						+ "balance,"
						+ "avali_balance," 
						+ "serial_no,"
						+ "child_serial_no,"
						+ "record_date,"
						+ "record_time,"
						+ "debit_credit_side,"
						+ "summary_info");						
				FROM("inside_trans_info ");		
				if (!StringUtils.isBlank(insideTransInfo.getAccountNo())||!insideTransInfo.getDebitCreditSide().endsWith(""))
				WHERE("  account_no = #{insideTransInfo.accountNo} ");
				if (!StringUtils.isBlank(insideTransInfo.getDebitCreditSide()) && !"ALL".equals(insideTransInfo.getDebitCreditSide()))
					WHERE(" debit_credit_side =#{insideTransInfo.debitCreditSide} ");					
				if (beginDate != null && StringUtils.isNotBlank(beginDate))
					WHERE(" record_date >= #{params.beginDate} ");
				if (endDate != null && StringUtils.isNotBlank(endDate))
					WHERE(" record_date <= #{params.endDate} ");
				
				ORDER_BY(" record_date desc , record_time desc ") ;
			}}.toString();
		}
		
		public String findInsideTransList(final Map<String, Object> parameter) {
			final InsideTransInfo insideTransInfo = (InsideTransInfo) parameter.get("insideTransInfo");
			final Sort sord=(Sort)parameter.get("sort");
			@SuppressWarnings("unchecked")
			final Map<String, String> params = (Map<String, String>) parameter.get("params");
			final String beginDate = params.get("beginDate");
			final String endDate = params.get("endDate");

			return new SQL(){{
				SELECT("id,account_no,"
						+ "record_amount,"
						+ "balance,"
						+ "avali_balance," 
						+ "serial_no,"
						+ "child_serial_no,"
						+ "record_date,"
						+ "record_time,"
						+ "debit_credit_side,"
						+ "summary_info");						
				FROM("inside_trans_info ");		
				if (!StringUtils.isBlank(insideTransInfo.getAccountNo())||!insideTransInfo.getDebitCreditSide().endsWith(""))
				/*WHERE("  account_no like  \"%\"#{insideTransInfo.accountNo}\"%\" ");*/
					WHERE("  account_no = #{insideTransInfo.accountNo} ");
				if (!StringUtils.isBlank(insideTransInfo.getDebitCreditSide()) && !"ALL".equals(insideTransInfo.getDebitCreditSide()))
					WHERE(" debit_credit_side =#{insideTransInfo.debitCreditSide} ");					
				if (beginDate != null && StringUtils.isNotBlank(beginDate))
					WHERE(" record_date >= #{params.beginDate} ");
				if (endDate != null && StringUtils.isNotBlank(endDate))
					WHERE(" record_date <= #{params.endDate} ");
				if(StringUtils.isNotBlank(sord.getSidx())){
					ORDER_BY(propertyMapping(sord.getSidx(), 0)+" "+sord.getSord());
				}else{
					ORDER_BY(" record_date desc , record_time desc ") ;
				}
			}}.toString();
		}
		
		public String findExportInsAccountList(final Map<String, Object> parameter) {
			final InsAccount insAccount = (InsAccount) parameter.get("insAccount");
			return new SQL(){{
				SELECT("ba.account_no,"
						+ "ba.org_no,"
						+ "ba.currency_no,"
						+ "ba.subject_no,"
						+ "ba.account_name,"
						+ "ba.curr_balance,"
						+ "ba.control_amount,"
						+ "ba.parent_trans_day,"
						+ "ba.parent_trans_balance,"
						+ "ba.account_status,"
						+ "ba.create_time,"
						+ "ba.balance_add_from,"
						+ "ba.balance_from,"
						+ "ba.day_bal_flag, "					
						+ "ba.sum_flag, "
						+ "ba.creator, "
						+ "(ba.curr_balance - ba.control_amount) as avail_balance,"
						+"bs.subject_name ");					
				FROM("bill_ins_account ba ,bill_subject bs ");
				WHERE(" ba.subject_no=bs.subject_no ");
				
				if (StringUtils.isNotBlank(insAccount.getAccountNo()))
					WHERE(" ba.account_no like  \"%\"#{insAccount.accountNo}\"%\" ");
				if (StringUtils.isNotBlank(insAccount.getOrgNo()) && !insAccount.getOrgNo().equals("ALL"))
					WHERE(" ba.org_no = #{insAccount.orgNo}");
				if (StringUtils.isNotBlank(insAccount.getSubjectNo())&&insAccount.getSubjectNo()!=null)
					WHERE(" ba.subject_no = #{insAccount.subjectNo} ");
				if (StringUtils.isNotBlank(insAccount.getCurrencyNo()) && !insAccount.getCurrencyNo().equals("ALL"))
					WHERE(" ba.currency_no = #{insAccount.currencyNo} ");
				if (StringUtils.isNotBlank(insAccount.getAccountName()))
					WHERE(" ba.account_name like  \"%\"#{insAccount.accountName}\"%\" ");
				if (StringUtils.isNotBlank(insAccount.getAccountStatus()) && !insAccount.getAccountStatus().equals("ALL"))
					WHERE(" ba.account_status = #{insAccount.accountStatus} ");
				
				ORDER_BY(" ba.create_time desc ") ;
			}}.toString();
		}
		
		public String findInsAccountListInfo(final Map<String, Object> parameter) {
			final InsAccount insAccount = (InsAccount) parameter.get("insAccount");
			final Sort sord=(Sort)parameter.get("sort");
			return new SQL(){{
				SELECT("ba.account_no,"
						+ "ba.org_no,"
						+ "ba.currency_no,"
						+ "ba.subject_no,"
						+ "ba.account_name,"
						+ "ba.curr_balance,"
						+ "ba.control_amount,"
						+ "ba.parent_trans_day,"
						+ "ba.parent_trans_balance,"
						+ "ba.account_status,"
						+ "ba.create_time,"
						+ "ba.balance_add_from,"
						+ "ba.balance_from,"
						+ "ba.day_bal_flag,"					
						+ "ba.sum_flag, "
						+ "ba.creator, "
						+ "(ba.curr_balance - ba.control_amount) as avail_balance,"
						+"bs.subject_name ");					
				FROM("bill_ins_account ba ,bill_subject bs ");
				WHERE(" ba.subject_no=bs.subject_no ");
				
				if (StringUtils.isNotBlank(insAccount.getAccountNo()))
					WHERE(" ba.account_no like  \"%\"#{insAccount.accountNo}\"%\" ");
				if (StringUtils.isNotBlank(insAccount.getOrgNo()) && !insAccount.getOrgNo().equals("ALL"))
					WHERE(" ba.org_no = #{insAccount.orgNo}");
				if (StringUtils.isNotBlank(insAccount.getSubjectNo()) && !insAccount.getSubjectNo().equals("ALL"))
					WHERE(" ba.subject_no = #{insAccount.subjectNo} ");
				if (StringUtils.isNotBlank(insAccount.getCurrencyNo()) && !insAccount.getCurrencyNo().equals("ALL"))
					WHERE(" ba.currency_no = #{insAccount.currencyNo} ");
				if (StringUtils.isNotBlank(insAccount.getAccountName()))
					WHERE(" ba.account_name like  \"%\"#{insAccount.accountName}\"%\" ");
				if (StringUtils.isNotBlank(insAccount.getAccountStatus()) && !insAccount.getAccountStatus().equals("ALL"))
					WHERE(" ba.account_status = #{insAccount.accountStatus} ");
				if(StringUtils.isNotBlank(sord.getSidx())){
					ORDER_BY(propertyMapping(sord.getSidx(), 0)+" "+sord.getSord());
				}else{
					ORDER_BY(" ba.create_time desc ") ;
				}
			}}.toString();
		}
		
		public String propertyMapping(String name,int type){
			/*final String[] propertys={"accountNo","subjectNo","currencyNo","subjectName","balanceFrom","AccountStatus","currBalance",
					"recordDate","recordTime","serialNo","childSerialNo","recordAmount","balance","availBalance","debitCreditSide","summaryInfo"};
		    final String[] columns={"account_no","subject_no","currency_no","subject_name","balance_from","account_status","curr_balance",
		    		"record_date","record_time","serial_no","child_serial_no","record_amount","balance","avail_balance","debit_credit_side","summary_info"};*/
			final String[] propertys={"currBalance","recordDate","recordTime","recordAmount","balance","availBalance","avaliBalance","createTime"};
		    final String[] columns={"curr_balance","record_date","record_time","record_amount","balance","avail_balance","avali_balance","create_time"};
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
