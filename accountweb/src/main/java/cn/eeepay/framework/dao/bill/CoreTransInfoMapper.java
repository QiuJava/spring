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
import cn.eeepay.framework.model.bill.CoreTransInfo;
import cn.eeepay.framework.model.bill.ExtAccount;
import cn.eeepay.framework.model.bill.ExtAccountInfo;
import cn.eeepay.framework.model.bill.ExtTransInfo;

/**
 * 内部账交易流水表
 * @author Administrator
 *
 */
public interface CoreTransInfoMapper {
	
	@Insert("insert into core_trans_info(account_no,trans_amount,serial_no,child_serial_no,journal_no,subject_no,currency_no,trans_date,reverse_flag,account_flag,debit_credit_flag,debit_credit_side,summary_info)" 
			+"values(#{coreTransInfo.accountNo},#{coreTransInfo.transAmount},#{coreTransInfo.serialNo},#{coreTransInfo.childSerialNo},#{coreTransInfo.journalNo},#{coreTransInfo.subjectNo},#{coreTransInfo.currencyNo},#{coreTransInfo.transDate},#{coreTransInfo.reverseFlag},"
			+ "#{coreTransInfo.accountFlag},#{coreTransInfo.debitCreditFlag},#{coreTransInfo.debitCreditSide},#{coreTransInfo.summaryInfo})"
			)
	int insertTransInfo(@Param("coreTransInfo")CoreTransInfo coreTransInfo);
	
//	@Update("update trans_info set subject_legal_no=#{subject.subjectLegalNo},subject_name=#{subject.subjectName}"
//			+", subject_level=#{subject.subjectLevel},parent_Subject_no=#{subject.parentSubjectNo},subject_type=#{subject.subjectType}"
//			+", balance_from=#{subject.balanceFrom},add_balance_from=#{subject.addBalanceFrom},debit_credit_flag=#{subject.debitCreditFlag}"
//			+", is_inner_account=#{subject.isInnerAccount},inner_day_bal_flag=#{subject.innerDayBalFlag},inner_sum_flag=#{subject.innerSumFlag}"
//			+ " where subject_no=#{subject.subjectNo}")
//	int updateTransInfo(@Param("transInfo")TransInfo transInfo);
	
	@Select("select sum(trans_amount) as trans_amount from core_trans_info "
			+ "where find_in_set(subject_no,get_bill_subject_child_list(#{parentSubjectNo})) "
			+ "and debit_credit_side= #{debitCreditSide} "
			+ "and trans_date = #{transDate}")
	@ResultMap("cn.eeepay.framework.dao.bill.CoreTransInfoMapper.BaseResultMap")
	CoreTransInfo findSubjectAllTransAmount(@Param("parentSubjectNo")String parentSubjectNo,@Param("debitCreditSide")String debitCreditSide,@Param("transDate")Date transDate);	
	
	
	@SelectProvider( type=SqlProvider.class,method="findAllCoreTransInfo")
	@ResultMap("cn.eeepay.framework.dao.bill.CoreTransInfoMapper.OneToOneResultMap")
	List<CoreTransInfo> findAllTransInfo(@Param("transInfo")CoreTransInfo transInfo,@Param("params") Map<String, String> params,@Param("sort")Sort sort,Page<CoreTransInfo> page);
	
	
	@Insert("insert into ext_account_info(account_no,user_id,account_owner,card_no,subject_no,currency_no)"
			+"values(#{extAccountInfo.accountNo},#{extAccountInfo.userId},#{extAccountInfo.accountOwner},#{extAccountInfo.cardNo},#{extAccountInfo.subjectNo},#{extAccountInfo.currencyNo})"
			)
	int insertExtAccountInfo(@Param("extAccountInfo")ExtAccountInfo extAccountInfo);
	
	@Insert("insert into ext_trans_info(account_no,record_amount,balance,avali_balance,serial_no,child_serial_no,record_date,record_time,debit_credit_side,summary_info)"
			+"values(#{extTransInfo.accountNo},#{extTransInfo.recordAmount},#{extTransInfo.balance},#{extTransInfo.avaliBalance},#{extTransInfo.serialNo},#{extTransInfo.childSerialNo},#{extTransInfo.recordDate},#{extTransInfo.recordTime},#{extTransInfo.debitCreditSide},#{extTransInfo.summaryInfo})"
			)
	int insertExtTransInfo(@Param("extTransInfo")ExtTransInfo extTransInfo);
	
	
	public class SqlProvider{
		public String findAllAccountStatusUpdateInfo(final Map<String, Object> parameter) {
			final ExtAccount outAccount = (ExtAccount) parameter.get("outAccount");
			final Sort sord=(Sort)parameter.get("sort");
			return new SQL(){{
				SELECT("bea.account_no,"
						+ "bea.account_name,"
						+ "bea.account_type,"
						+ "bea.org_no,"
						+ "bea.currency_no,"
						+ "bea.subject_no,"
						+ "bea.curr_balance,"
						+ "bea.account_status,"
						+ "eai.card_no,"
						+ "eai.user_id,"
						+ "s.subject_name,"
						+ "eai.account_owner,"
						+ "(bea.curr_balance - bea.control_amount) as avail_balance");
				FROM("bill_ext_account as bea, ext_account_info as eai,bill_subject as s ");
				WHERE("bea.account_no = eai.account_no and bea.subject_no = s.subject_no");
				if (StringUtils.isNotBlank(outAccount.getAccountNo()))
					WHERE(" bea.account_no  like  \"%\"#{outAccount.accountNo}\"%\" ");
				if (StringUtils.isNotBlank(outAccount.getSubjectNo()))
					WHERE(" bea.subject_no = #{outAccount.subjectNo} ");
				if (StringUtils.isNotBlank(outAccount.getExtAccountInfo().getUserId()))
					WHERE(" eai.user_id like  \"%\"#{outAccount.extAccountInfo.userId}\"%\" ");
				if (StringUtils.isNotBlank(outAccount.getCurrencyNo()))
					WHERE(" bea.currency_no = #{outAccount.currencyNo} ");
				if (StringUtils.isNotBlank(outAccount.getOrgNo()))
					WHERE(" bea.org_no = #{outAccount.orgNo} ");
				if(StringUtils.isNotBlank(sord.getSidx())){
					ORDER_BY(propertyMapping(sord.getSidx(), 0)+" "+sord.getSord());
				}
			}}.toString();
		}
		
		public String findAllAccountInfo(final Map<String, Object> parameter) {
			final ExtAccount outAccount = (ExtAccount) parameter.get("outAccount");
			final Sort sord=(Sort)parameter.get("sort");
			return new SQL(){{
				SELECT("bea.account_no,"
						+ "bea.account_name,"
						+ "bea.account_type,"
						+ "bea.org_no,"
						+ "bea.currency_no,"
						+ "bea.subject_no,"
						+ "bea.curr_balance,"
						+ "bea.account_status,"
						+ "eai.user_id,"
						+ "s.subject_name,"
						+ "eai.account_owner,"
						+ "(bea.curr_balance - bea.control_amount) as avail_balance,"
						+ "eti.record_amount,"
						+ "eti.balance,"
						+ "eti.avali_balance,"
						+ "eti.serial_no,"
						+ "eti.child_serial_no,"
						+ "eti.record_date,"
						+ "eti.record_time,"
						+ "eti.debit_credit_side,"
						+ "eti.summary_info");
				FROM("bill_ext_account as bea, ext_account_info as eai,bill_subject as s,ext_trans_info as eti ");
				WHERE("bea.account_no = eai.account_no and bea.subject_no = s.subject_no and bea.account_no = eti.account_no");
				if (StringUtils.isNotBlank(outAccount.getAccountNo()))
					WHERE(" bea.account_no  like  \"%\"#{outAccount.accountNo}\"%\" ");
				if (StringUtils.isNotBlank(outAccount.getSubjectNo()))
					WHERE(" bea.subject_no = #{outAccount.subjectNo} ");
				if (outAccount.getExtAccountInfo() != null && StringUtils.isNotBlank(outAccount.getExtAccountInfo().getUserId()))
					WHERE(" eai.user_id like  \"%\"#{outAccount.extAccountInfo.userId}\"%\" ");
				if (StringUtils.isNotBlank(outAccount.getCurrencyNo()))
					WHERE(" bea.currency_no = #{outAccount.currencyNo} ");
				if (StringUtils.isNotBlank(outAccount.getOrgNo()))
					WHERE(" bea.org_no = #{outAccount.orgNo} ");
				if(sord != null && StringUtils.isNotBlank(sord.getSidx())){
					ORDER_BY(propertyMapping(sord.getSidx(), 0)+" "+sord.getSord());
				}
			}}.toString();
		}
		
		public String findAllCoreTransInfo(final Map<String, Object> parameter) {
			final CoreTransInfo transInfo = (CoreTransInfo) parameter.get("transInfo");
			@SuppressWarnings("unchecked")
			final Map<String, String> params = (Map<String, String>) parameter.get("params");
			final Sort sord=(Sort)parameter.get("sort");
			final String recordDate1 = params.get("recordDate1");
			final String recordDate2 = params.get("recordDate2");
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
				if (StringUtils.isNotBlank(transInfo.getAccountNo()))
					WHERE(" eti.account_no  like  \"%\"#{extTransInfo.accountNo}\"%\" ");
//				if (transInfo.getOutAccount()!= null && StringUtils.isNotBlank(transInfo.getOutAccount().getSubjectNo()))
//					WHERE(" bea.subject_no = #{extTransInfo.outAccount.subjectNo} ");
//				if (transInfo.getOutAccount()!= null && transInfo.getOutAccount().getExtAccountInfo()!= null 
//						&& StringUtils.isNotBlank(transInfo.getOutAccount().getExtAccountInfo().getUserId()))
//					WHERE(" eai.user_id like  \"%\"#{extTransInfo.outAccount.extAccountInfo.userId}\"%\" ");
//				if (transInfo.getOutAccount()!= null && StringUtils.isNotBlank(transInfo.getOutAccount().getCurrencyNo()))
//					WHERE(" bea.currency_no = #{extTransInfo.outAccount.currencyNo} ");
//				if (transInfo.getOutAccount()!= null && StringUtils.isNotBlank(transInfo.getOutAccount().getOrgNo()))
//					WHERE(" bea.org_no = #{extTransInfo.outAccount.orgNo} ");
//				if (recordDate1 != null && StringUtils.isNotBlank(recordDate1))
//					WHERE(" eti.record_date >= #{params.recordDate1} ");
//				if (recordDate2 != null && StringUtils.isNotBlank(recordDate2))
//					WHERE(" eti.record_date <= #{params.recordDate2} ");
				if(StringUtils.isNotBlank(sord.getSidx())){
					ORDER_BY(propertyMapping(sord.getSidx(), 0)+" "+sord.getSord());
				}
			}}.toString();
		}
		
		public String propertyMapping(String name,int type){
			final String[] propertys={"accountNo","accountName","orgNo","currencyNo","subjectNo","currBalance","balanceFrom"};
		    final String[] columns={"account_no","account_name","org_no","currency_no","subject_no","curr_balance","balance_from"};
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
