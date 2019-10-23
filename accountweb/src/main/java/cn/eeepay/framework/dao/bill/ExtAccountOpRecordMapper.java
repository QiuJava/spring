package cn.eeepay.framework.dao.bill;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import cn.eeepay.framework.model.bill.ExtAccountOpRecord;
import cn.eeepay.framework.model.bill.ExtTransInfo;

/**
 * 外部账户冻结解冻记录表
 * @author Administrator
 *
 */
public interface ExtAccountOpRecordMapper {
	
	/*@Insert("insert into ext_account_op_record(account_no,record_date,serial_no,operation_type,freeze_balance)" 
			+"values(#{extAccountOpRecord.accountNo},#{extAccountOpRecord.recordDate},#{extAccountOpRecord.serialNo},#{extAccountOpRecord.operationType},#{extAccountOpRecord.freezeBalance})"
			)
	int insertExtAccountOpRecord(@Param("extAccountOpRecord")ExtAccountOpRecord extAccountOpRecord);
	
	@Insert("update ext_account_op_record set record_date=#{extAccountOpRecord.recordDate},serial_no=#{extAccountOpRecord.serialNo},operation_type=#{extAccountOpRecord.operationType},"
			+ "freeze_balance=#{extAccountOpRecord.freezeBalance} where account_no = #{extAccountOpRecord.accountNo}" 
			)
	int updateExtAccountOpRecord(@Param("extAccountOpRecord")ExtAccountOpRecord extAccountOpRecord);
	*/
	
	//插入一条外部账户冻结解冻记录
	@Insert("INSERT INTO ext_account_op_record(account_no,record_date,serial_no,operation_type,operation_balance,trans_order_no,summary_info) "
			+ "VALUE(#{opRecord.accountNo},#{opRecord.recordDate},#{opRecord.serialNo},#{opRecord.operationType},#{opRecord.operationBalance},#{opRecord.transOrderNo},#{opRecord.summaryInfo})")
	int insertExtAccountOpRecord(@Param("opRecord")ExtAccountOpRecord opRecord) ;
	
	//更新(解冻)外部账户冻结解冻记录
	@Update("UPDATE ext_account_op_record SET operation_type=#{opRecord.operationType},summary_info=#{opRecord.summaryInfo} WHERE trans_order_no=#{opRecord.transOrderNo} ")
	int updateExtAccountOpRecord(@Param("opRecord")ExtAccountOpRecord opRecord) ;
		
	//获得  外部账户交易明细  通过 交易订单号
	@Select("SELECT * FROM ext_trans_info WHERE trans_order_no = #{transOrderNo}")
	@ResultMap("cn.eeepay.framework.dao.bill.ExtTransInfoMapper.BaseResultMap")
	ExtTransInfo findExtTransInfoByOrderNo(@Param("transOrderNo")String transOrderNo) ;
	
}
