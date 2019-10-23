package cn.eeepay.framework.dao.bill;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;

import cn.eeepay.framework.model.bill.SettleTransferFile;

/**
 * 结算转账文件表
 * @author Administrator
 *
 */
public interface SettleTransferFileMapper {

	/**
	 * 新增结算转账文件
	 * @param subject
	 * @return
	 */
	@Insert("insert into settle_transfer_file(file_name,file_md5,operator_id,operator_name,transfer_operator_id,transfer_operator_name,total_num,total_amount,summary,create_time,transfer_time,settle_bank,out_acc_no,out_acc_name,out_bank_no,out_bank_name,out_settle_bank_no,status,err_code,err_msg,bak1,bak2)"
			+"values(#{stf.fileName},#{stf.fileMd5},#{stf.operatorId},#{stf.operatorName},#{stf.transferOperatorId},#{stf.transferOperatorName},#{stf.totalNum},#{stf.totalAmount},#{stf.summary},#{stf.createTime},#{stf.transferTime},#{stf.settleBank},#{stf.outAccNo},#{stf.outAccName},#{stf.outBankNo},"
			+ "#{stf.outBankName},#{stf.outSettleBankNo},#{stf.status},#{stf.errCode},#{stf.errMsg},#{stf.bak1},#{stf.bak2})"
			)
	@SelectKey(statement=" SELECT LAST_INSERT_ID() AS id", keyProperty="stf.id", before=false, resultType=int.class) 
	int insertSettleTransferFile(@Param("stf")SettleTransferFile stf);
	
	
	@Update("update settle_transfer_file set file_name=#{stf.fileName},file_md5=#{stf.fileMd5},operator_id=#{stf.operatorId},operator_name=#{stf.operatorName},"
			+" transfer_operator_id=#{stf.transferOperatorId},transfer_operator_name=#{stf.transferOperatorName},total_num=#{stf.totalNum},total_amount=#{stf.totalAmount},"
			+" summary=#{stf.summary},create_time=#{stf.createTime},transfer_time=#{stf.transferTime},settle_bank=#{stf.settleBank},out_acc_no=#{stf.outAccNo},out_acc_name=#{stf.outAccName}, "
			+" out_bank_no=#{stf.outBankNo},out_bank_name=#{stf.outBankName},out_settle_bank_no=#{stf.outSettleBankNo},status=#{stf.status},err_code=#{stf.errCode},err_msg=#{stf.errMsg},"
			+" bak1=#{stf.bak1},bak2=#{stf.bak2}"
			+" where id=#{stf.id}")
	int updateSettleTransferFileById(@Param("stf")SettleTransferFile stf);
	
	@Update("update settle_transfer_file set file_name=#{stf.fileName},file_md5=#{stf.fileMd5},operator_id=#{stf.operatorId},operator_name=#{stf.operatorName},"
			+" transfer_operator_id=#{stf.transferOperatorId},transfer_operator_name=#{stf.transferOperatorName},total_num=#{stf.totalNum},total_amount=#{stf.totalAmount},"
			+" summary=#{stf.summary},create_time=#{stf.createTime},transfer_time=#{stf.transferTime},settle_bank=#{stf.settleBank},out_acc_no=#{stf.outAccNo},out_acc_name=#{stf.outAccName}, "
			+" out_bank_no=#{stf.outBankNo},out_bank_name=#{stf.outBankName},out_settle_bank_no=#{stf.outSettleBankNo},err_code=#{stf.errCode},err_msg=#{stf.errMsg},"
			+" bak1=#{stf.bak1},bak2=#{stf.bak2}"
			+" where id=#{stf.id} and status=#{stf.status} ")
	int updateSettleTransferFileByIdAndStatus(@Param("stf")SettleTransferFile stf);
			
	@Select("select * from settle_transfer_file where file_name = #{fileName}")
	@ResultMap("cn.eeepay.framework.dao.bill.SettleTransferFileMapper.BaseResultMap")
	List<SettleTransferFile> findFileByFileName(@Param("fileName")String fileName);
	
	@Select("select * from settle_transfer_file where file_md5 = #{fileMd5}")
	@ResultMap("cn.eeepay.framework.dao.bill.SettleTransferFileMapper.BaseResultMap")
	List<SettleTransferFile> findFileByFileMD5(@Param("fileMd5")String fileMd5);
	

	
	
}
