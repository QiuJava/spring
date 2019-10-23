package cn.eeepay.framework.dao.bill;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.InsAccount;
import cn.eeepay.framework.model.bill.InsideNobookedDetail;
import cn.eeepay.framework.model.bill.InsideTransInfo;
/**
 * 内部账未入账流水表
 * @author Administrator
 *
 */
public interface InsideNobookedDetailMapper {
	
	@Insert("insert into inside_nobooked_detail(account_no,trans_amount,serial_no,child_serial_no,trans_date,debit_credit_side,booked_flag,summary_info)"
			+"values(#{insideNobookedDetail.accountNo},#{insideNobookedDetail.transAmount},#{insideNobookedDetail.serialNo},#{insideNobookedDetail.childSerialNo},"
			+ "#{insideNobookedDetail.transDate},#{insideNobookedDetail.debitCreditSide},#{insideNobookedDetail.bookedFlag},#{insideNobookedDetail.summaryInfo})"
			)
	int insertInsideNobookedDetail(@Param("insideNobookedDetail")InsideNobookedDetail insideNobookedDetail);
	
	@Insert("update inside_nobooked_detail set trans_amount=#{insideNobookedDetail.transAmount},serial_no=#{insideNobookedDetail.serialNo},child_serial_no=#{insideNobookedDetail.childSerialNo},"
			+ "trans_date=#{insideNobookedDetail.transDate},debit_credit_side=#{insideNobookedDetail.debitCreditSide},booked_flag=#{insideNobookedDetail.bookedFlag},summary_info=#{insideNobookedDetail.summaryInfo}"
			+ " where account_no=#{insideNobookedDetail.accountNo}"
			)
	int updateInsideNobookedDetail(@Param("insideNobookedDetail")InsideNobookedDetail insideNobookedDetail);
	
	
	@Select("select id,account_no,trans_amount,serial_no,child_serial_no,trans_date,debit_credit_side,booked_flag,summary_info from inside_nobooked_detail"
			+ " where account_no=#{accountNo} and trans_date=#{transDate} and booked_flag=#{bookedFlag} ")
	@ResultMap("cn.eeepay.framework.dao.bill.InputAccountMapper.BaseResultMap")
	List<InsideNobookedDetail> findInsideNobookedDetailByParams(@Param("accountNo")String accountNo,@Param("transDate")Date transDate,@Param("bookedFlag")String bookedFlag);
	
//	@Select("select * from bill_ins_account where currency_no = #{currencyNo} AND subject_no= #{subjectNo} AND org_no=#{org_no}")
//	@ResultMap("cn.eeepay.framework.dao.InputAccountMapper.BaseResultMap")
//	InputAccount exsitsSubject(@Param("currencyNo")String currencyNo,@Param("subjectNo") String subjectNo,
//			@Param("org_no") String org_no);	
//	
//	@SelectProvider( type=SqlProvider.class,method="findInputAccount")
//	@ResultMap("cn.eeepay.framework.dao.InputAccountMapper.OneToOneResultMap")
//	List<InputAccount> findInputAccount(@Param("inputAccount")InputAccount  inputAccount,@Param("sort")Sort sort,Page<InputAccount> page);
//	
//	@Select("SELECT ba.account_no,ba.org_no,ba.currency_no,ba.subject_no,ba.account_name,ba.curr_balance,"
//			+"bs.subject_name FROM bill_ins_account ba ,bill_subject bs WHERE ba.subject_no=bs.subject_no ")
//	@ResultMap("cn.eeepay.framework.dao.InputAccountMapper.OneToOneResultMap")
//	public List<InputAccount> findInputAccountList();
//	
//	
//	@SelectProvider( type=SqlProvider.class,method="findInsideTransList")
//	@ResultMap("cn.eeepay.framework.dao.InsideTransInfoMapper.BaseResultMap")
//	public  List<InsideTransInfo> findInsideTransList(@Param("insideTransInfo")InsideTransInfo   insideTransInfo,@Param("params")Map<String, String> params ,@Param("sort")Sort sort,Page<InsideTransInfo> page);
	
	
	public class SqlProvider{
		/*public String getSubjectInfoList(Map<String,Object> map){
			System.out.println(map);
			StringBuffer sbf=new StringBuffer("select * from bill_subject ");
			return sbf.toString();
		}
		public String exsitsSubject(final Map<String,Object> map){
			return new SQL(){{
				SELECT(" subject_no,subject_name,subject_legal_no,subject_level ");
				FROM(" bill_subject ");
				String type=map.get("type")==null?"inner":map.get("type").toString();
				if(StringUtils.equalsIgnoreCase("legal", type)){
					WHERE(" subject_legal_no=#{subjectNo} ");
				}else{
					WHERE(" subject_no=#{subjectNo} ");
				}
			}}.toString();
		}*/
		
		
		
//		public String findInsideTransList(final Map<String, Object> parameter) {
//			final InsideTransInfo insideTransInfo = (InsideTransInfo) parameter.get("insideTransInfo");
//			final Sort sord=(Sort)parameter.get("sort");
//			@SuppressWarnings("unchecked")
//			final Map<String, String> params = (Map<String, String>) parameter.get("params");
//			final String beginDate = params.get("beginDate");
//			final String endDate = params.get("endDate");
//
//			return new SQL(){{
//				SELECT("account_no,"
//						+ "record_amount,"
//						+ "balance,"
//						+ "avali_balance," 
//						+ "serial_no,"
//						+ "child_serial_no,"
//						+ "record_date,"
//						+ "record_time,"
//						+ "debit_credit_side,"
//						+ "summary_info");						
//				FROM("inside_trans_info ");		
//				if (!StringUtils.isBlank(insideTransInfo.getAccountNo())||!insideTransInfo.getDebitCreditSide().endsWith(""))
//				WHERE("  account_no like  \"%\"#{insideTransInfo.accountNo}\"%\" ");		
//				if (!StringUtils.isBlank(insideTransInfo.getDebitCreditSide())||!insideTransInfo.getDebitCreditSide().endsWith(""))
//					WHERE(" debit_credit_side =#{insideTransInfo.debitCreditSide} ");					
//				if (beginDate != null && StringUtils.isNotBlank(beginDate))
//					WHERE(" record_date >= #{params.beginDate} ");
//				if (endDate != null && StringUtils.isNotBlank(endDate))
//					WHERE(" record_date <= #{params.endDate} ");
//				if(StringUtils.isNotBlank(sord.getSidx())){
//					ORDER_BY(propertyMapping(sord.getSidx(), 0)+" "+sord.getSord());
//				}
//			}}.toString();
//		}
//		
//		
//		public String findInputAccount(final Map<String, Object> parameter) {
//			final InputAccount inputAccount = (InputAccount) parameter.get("inputAccount");
//			final Sort sord=(Sort)parameter.get("sort");
//			return new SQL(){{
//				SELECT("ba.account_no,"
//						+ "ba.org_no,"
//						+ "ba.currency_no,"
//						+ "ba.subject_no,"
//						+ "ba.account_name,"
//						+ "ba.curr_balance,"
//						+ "ba.control_amount,"
//						+ "ba.parent_trans_day,"
//						+ "ba.parent_trans_balance,"
//						+ "ba.account_status,"
//						+ "ba.create_time,"
//						+ "ba.balance_add_from,"
//						+ "ba.balance_from,"
//						+ "ba.day_bal_flag,"					
//						+ "ba.sum_flag,"
//						+ "(ba.curr_balance - ba.control_amount) as avail_balance,"
//						+"bs.subject_name ");					
//				FROM("bill_ins_account ba ,bill_subject bs ");
//				WHERE(" ba.subject_no=bs.subject_no ");
//				
//				if (!StringUtils.isBlank(inputAccount.getAccountNo())||!inputAccount.getAccountNo().endsWith(""))
//					WHERE(" ba.account_no like  \"%\"#{inputAccount.accountNo}\"%\" ");
//				if (!StringUtils.isBlank(inputAccount.getOrgNo()))
//					WHERE(" ba.org_no = #{inputAccount.orgNo}");
//				if (StringUtils.isNotBlank(inputAccount.getSubjectNo())&&inputAccount.getSubjectNo()!=null)
//					WHERE("ba.subject_no = #{inputAccount.subjectNo} ");
//				if (!StringUtils.isBlank(inputAccount.getCurrencyNo())||!inputAccount.getCurrencyNo().endsWith(""))
//					WHERE(" ba.currency_no = #{inputAccount.currencyNo} ");
//				if (!StringUtils.isBlank(inputAccount.getAccountName())||!inputAccount.getAccountName().endsWith(""))
//					WHERE(" ba.account_name like  \"%\"#{inputAccount.accountName}\"%\" ");
//				if (!StringUtils.isBlank(inputAccount.getAccountStatus())||!inputAccount.getAccountName().endsWith(""))
//					WHERE(" ba.account_status = #{inputAccount.accountStatus} ");
//				if(StringUtils.isNotBlank(sord.getSidx())){
//					ORDER_BY(propertyMapping(sord.getSidx(), 0)+" "+sord.getSord());
//				}
//			}}.toString();
//		}
		/*public String findSelectSubject(final Map<String, Object> parameter) {
			final Subject subject = (Subject) parameter.get("subject");
			return new SQL(){{
				SELECT("subject_no,"
						+ "subject_name ");
				FROM("bill_subject");
				if (!StringUtils.isBlank(subject.getSubjectNo()))
					WHERE(" subject_no like  \"%\"#{subject.subjectNo}\"%\" or subject_name like  \"%\"#{subject.subjectNo}\"%\" ");
			}}.toString();
		}*/
		
//		public String propertyMapping(String name,int type){
//			final String[] propertys={"accountNo","subjectNo","currencyNo","subjectName","balanceFrom","AccountStatus",};
//		    final String[] columns={"account_no","subject_no","currency_no","subject_name","balance_from","account_status",};
//		    if(StringUtils.isNotBlank(name)){
//		    	if(type==0){//属性查出字段名
//		    		for(int i=0;i<propertys.length;i++){
//		    			if(name.equalsIgnoreCase(propertys[i])){
//		    				return columns[i];
//		    			}
//		    		}
//		    	}else if(type==1){//字段名查出属性
//		    		for(int i=0;i<propertys.length;i++){
//		    			if(name.equalsIgnoreCase(columns[i])){
//		    				return propertys[i];
//		    			}
//		    		}
//		    	}
//		    }
//			return null;
//		}
	}
}
