package cn.eeepay.framework.dao.bill;


import java.math.BigDecimal;
import java.util.Date;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import cn.eeepay.framework.model.bill.TransImportInfo;

/**
 * 交易计算表
 * @author admin
 *
 */
public interface TransImportInfoMapper {
	@Select("SELECT sum(trans_amount) as money ,(sum(trans_amount)/#{day}) as days,out_service_id FROM `trans_import_info` "
			+ "where trans_date>=#{d1} and trans_date<=#{d2} and out_service_id=#{outId} "
			+ "and out_rate_type1=5 and reverse_flag='0'")
	@ResultMap("cn.eeepay.framework.dao.bill.TransImportInfoMapper.BaseResultMap")
	TransImportInfo findServiceFeeByMonth(@Param("d1")String d1,@Param("d2")String d2,@Param("outId")String outId,@Param("day")Integer day);
	
	@Select("SELECT sum(trans_amount) as money ,(sum(trans_amount)/#{day}) as days,out_service_id FROM `trans_import_info` "
			+ "where trans_date>=#{d1} and trans_date<=#{d2} and out_service_id=#{outId} "
			+ "and out_rate_type2=5 and reverse_flag='0'")
	@ResultMap("cn.eeepay.framework.dao.bill.TransImportInfoMapper.BaseResultMap")
	TransImportInfo findDianFeeByMonth(@Param("d1")String d1,@Param("d2")String d2,@Param("outId")String outId,@Param("day")Integer day);
	
	@Select("select sum(trans_amount) from trans_import_info where acq_enname=#{acqEName} and reverse_flag=#{reverseFlag} and reverse_status=#{reverseStatus} and record_date>=DATE_FORMAT(#{transDate},'%Y-%m-%d 00:00:00') and record_date<=DATE_FORMAT(#{transDate},'%Y-%m-%d 23:59:59')")
	@ResultType(BigDecimal.class)
	BigDecimal countByParam(@Param("acqEName")String acqEName, @Param("reverseFlag")String reverseFlag, @Param("reverseStatus")String reverseStatus, @Param("transDate")Date transDate);
}
