package cn.eeepay.framework.dao.bill;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import cn.eeepay.framework.model.bill.ShadowAccount;

public interface ShadowAccountMapper {
	@Select("select account_no,account_flag,trans_date,debit_amount,credit_amount,booked_flag from bill_shadow_account "
			+ "where account_no = #{accountNo} and account_flag = #{accountFlag} and trans_date = #{transDate}")
	@ResultMap("cn.eeepay.framework.dao.ShadowAccountMapper.BaseResultMap")
	ShadowAccount getShadowAccount(@Param("accountNo") String accountNo,@Param("accountFlag") String accountFlag,@Param("transDate") String transDate);
	
	@Update("update bill_shadow_account set debit_amount = #{shadowAccount.debitAmount} ,credit_amount = #{shadowAccount.creditAmount} ,booked_flag = #{shadowAccount.bookedFlag} "
			+ "where account_no =#{shadowAccount.accountNo} and account_flag =#{shadowAccount.accountFlag} and trans_date =#{shadowAccount.transDate}")
	int updateShadowAccount(@Param("shadowAccount")ShadowAccount shadowAccount);
	
	
	@Insert("insert into bill_shadow_account(account_no,account_flag,trans_date,debit_amount,credit_amount,booked_flag)" 
			+"values(#{shadowAccount.accountNo},#{shadowAccount.accountFlag},#{shadowAccount.transDate},#{shadowAccount.debitAmount},#{shadowAccount.creditAmount},#{shadowAccount.bookedFlag})"
			)
	int insertShadowAccount(@Param("shadowAccount")ShadowAccount shadowAccount);
	
	@Select("select account_no,account_flag,trans_date,debit_amount,credit_amount,booked_flag from bill_shadow_account "
			+ "where account_flag = #{accountFlag}")
	@ResultMap("cn.eeepay.framework.dao.bill.ShadowAccountMapper.BaseResultMap")
	List<ShadowAccount> findShadowAccountByAccountFlag(@Param("accountFlag") String accountFlag);
	
	
}
