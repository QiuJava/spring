package cn.eeepay.framework.dao.bill;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import cn.eeepay.framework.model.bill.ExtNobookedDetail;
/**
 * 外部账未入账流水表
 * @author Administrator
 *
 */
public interface ExtNobookedDetailMapper {
	
	@Insert("insert into ext_nobooked_detail(account_no,record_amount,serial_no,child_serial_no,record_date,debit_credit_side,booked_flag,summary_info)"
			+"values(#{extNobookedDetail.accountNo},#{extNobookedDetail.recordAmount},#{extNobookedDetail.serialNo},#{extNobookedDetail.childSerialNo},"
			+ "#{extNobookedDetail.recordDate},#{extNobookedDetail.debitCreditSide},#{extNobookedDetail.bookedFlag},#{extNobookedDetail.summaryInfo})"
			)
	int insertExtNobookedDetail(@Param("extNobookedDetail")ExtNobookedDetail extNobookedDetail);
	
	@Insert("update ext_nobooked_detail set record_amount=#{extNobookedDetail.recordAmount},serial_no=#{extNobookedDetail.serialNo},child_serial_no=#{extNobookedDetail.childSerialNo},"
			+ "record_date=#{extNobookedDetail.recordDate},debit_credit_side=#{extNobookedDetail.debitCreditSide},booked_flag=#{extNobookedDetail.bookedFlag},summary_info=#{extNobookedDetail.summaryInfo}"
			+ " where account_no=#{extNobookedDetail.accountNo}"
			)
	int updateExtNobookedDetail(@Param("extNobookedDetail")ExtNobookedDetail extNobookedDetail);
	
	
	@Select("select id,account_no,record_amount,serial_no,child_serial_no,record_date,debit_credit_side,booked_flag,summary_info from ext_nobooked_detail"
			+ " where account_no=#{accountNo} and record_date=#{recordDate} and booked_flag=#{bookedFlag} ")
	@ResultMap("cn.eeepay.framework.dao.ExtAccountMapper.BaseResultMap")
	List<ExtNobookedDetail> findExtNobookedDetailByParams(@Param("accountNo")String accountNo,@Param("recordDate")Date recordDate,@Param("bookedFlag")String bookedFlag);

	
	public class SqlProvider{

	}
}
