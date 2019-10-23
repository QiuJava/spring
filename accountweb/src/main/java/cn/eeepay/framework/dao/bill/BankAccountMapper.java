package cn.eeepay.framework.dao.bill;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.BankAccount;

/**
 * 银行账户管理底层
 * @author Administrator
 *
 */
public interface BankAccountMapper {

	/**
	 * 新增银行账户
	 * @param bankAccount
	 * @return
	 */
	@Insert("insert into bank_account_info(bank_name,account_name,account_no,org_no,currency_no,account_type,subject_no,cnaps_no,ins_account_no)"
			+"values(#{bankAccount.bankName},#{bankAccount.accountName},#{bankAccount.accountNo},#{bankAccount.orgNo},#{bankAccount.currencyNo},#{bankAccount.accountType},#{bankAccount.subjectNo},#{bankAccount.cnapsNo},#{bankAccount.insAccountNo})"
			)
	int insertBankAccount(@Param("bankAccount")BankAccount bankAccount);
	
	/**
	 * 判断当前银行账户是否存在
	 * @param accountNo
	 * @return
	 */
	@Select("select * from bank_account_info where account_no = #{accountNo}")
	@ResultMap("cn.eeepay.framework.dao.bill.BankAccountMapper.BaseResultMap")
	BankAccount exsitBankAccount(@Param("accountNo")String accountNo);
	
	/**
	 * 查询银行账户信息
	 * @param bankAccount
	 * @param sort
	 * @param page
	 * @return
	 */
	@SelectProvider( type=SqlProvider.class,method="findBankAccountList")
	@ResultMap("cn.eeepay.framework.dao.bill.BankAccountMapper.OneToOneResultMap")
	List<BankAccount> findBankAccountList(@Param("bankAccount")BankAccount bankAccount, @Param("sort")Sort sort, Page<BankAccount> page);
	
	/**
	 * 通过  id  查询银行账户详情
	 * @param id
	 * @return
	 */
	@Select("SELECT bai.org_no,bai.bank_name,bai.account_name,bai.account_no,bai.currency_no,bai.account_type,bai.subject_no,bai.cnaps_no,bia.account_status "
			+ "FROM bank_account_info as bai, bill_ins_account as bia WHERE bai.ins_account_no = bia.account_no AND bai.id = #{id} ")
	@ResultMap("cn.eeepay.framework.dao.bill.BankAccountMapper.OneToOneResultMap")
	BankAccount findBankAccountById(@Param("id")String id) ;
	
	/**
	 * 修改  银行账户  
	 * @param bankAccount
	 * @return
	 */
	@Update("update bank_account_info as bai LEFT OUTER JOIN bill_ins_account as bia ON bai.ins_account_no = bia.account_no set "
			+ "bai.org_no=#{bankAccount.orgNo},bai.bank_name=#{bankAccount.bankName},bai.account_name=#{bankAccount.accountName},bai.account_no=#{bankAccount.accountNo},"
			+ "bai.currency_no=#{bankAccount.currencyNo},bai.account_type=#{bankAccount.accountType},bai.subject_no=#{bankAccount.subjectNo},bai.cnaps_no=#{bankAccount.cnapsNo},"
			+ "bia.account_status=#{bankAccount.insAccount.accountStatus} "
			+ "WHERE bai.id = #{bankAccount.id} ")
	int updateBankAccount(@Param("bankAccount")BankAccount bankAccount);
	
	
	
	public class SqlProvider{
		
		
		public String findBankAccountList(final Map<String, Object> parameter) {
			final BankAccount bankAccount = (BankAccount) parameter.get("bankAccount");
			final Sort sord=(Sort)parameter.get("sort");
			return new SQL(){{
				SELECT("bai.id,bai.org_no,bai.bank_name,bai.account_name,bai.account_no,bai.currency_no,bai.account_type,bia.account_status,bai.ins_account_no,bia.curr_balance,(bia.curr_balance - bia.control_amount) as avail_balance ");
				FROM(" bank_account_info as bai, bill_ins_account as bia ");
				WHERE(" bai.ins_account_no = bia.account_no ") ;
				if (!StringUtils.isBlank(bankAccount.getBankName()) )
					WHERE(" bai.bank_name like  \"%\"#{bankAccount.bankName}\"%\" ");
				if (!StringUtils.isBlank(bankAccount.getAccountName()))
					WHERE(" bai.account_name like  \"%\"#{bankAccount.accountName}\"%\" ");
				if (!StringUtils.isBlank(bankAccount.getAccountNo()))
					WHERE(" bai.account_no like  \"%\"#{bankAccount.accountNo}\"%\" ");
				if (!StringUtils.isBlank(bankAccount.getOrgNo()) && !"ALL".equals(bankAccount.getOrgNo()))
					WHERE(" bai.org_no like  \"%\"#{bankAccount.orgNo}\"%\" ");
				if (!StringUtils.isBlank(bankAccount.getCurrencyNo()) && !"ALL".equals(bankAccount.getCurrencyNo()))
					WHERE(" bai.currency_no like  \"%\"#{bankAccount.currencyNo}\"%\" ");
				if (!StringUtils.isBlank(bankAccount.getAccountType()) && !"ALL".equals(bankAccount.getAccountType()))
					WHERE(" bai.account_type like  \"%\"#{bankAccount.accountType}\"%\" ");
				if(sord != null && StringUtils.isNotBlank(sord.getSidx())){
					ORDER_BY(propertyMapping(sord.getSidx(), 0)+" "+sord.getSord());
				}
			}}.toString();
		}
		
		
		public String propertyMapping(String name,int type){
			/*final String[] propertys={"subjectNo","subjectName","subjectLevel","parentSubjectNo","subjectType","balanceFrom"};
		    final String[] columns={"subject_no","subject_name","subject_level","parent_subject_no","subject_type","balance_from"};*/
			final String[] propertys={"currBalance","availBalance"};
		    final String[] columns={"curr_balance","avail_balance"};
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
