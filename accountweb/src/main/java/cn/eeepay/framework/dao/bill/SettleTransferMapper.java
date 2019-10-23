package cn.eeepay.framework.dao.bill;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import cn.eeepay.framework.model.bill.SettleTransfer;

/**
 * 结算转账文件表
 * @author Administrator
 *
 */
public interface SettleTransferMapper {

	/**
	 * 
	 * @param subject
	 * @return
	 */
	@Insert("insert into settle_transfer(batch_id,file_id,seq_no,file_name,settle_bank,in_acc_no,in_acc_name,in_settle_bank_no,in_bank_no,in_bank_name,amount,create_time,status,err_code,err_msg,bak1,bak2)"
			+"values(#{st.batchId},#{st.fileId},#{st.seqNo},#{st.fileName},#{st.settleBank},#{st.inAccNo},#{st.inAccName},#{st.inSettleBankNo},#{st.inBankNo},#{st.inBankName},#{st.amount},#{st.createTime},"
			+ "#{st.status},#{st.errCode},#{st.errMsg},#{st.bak1},#{st.bak2})"
			)
	int insertSettleTransfer(@Param("st")SettleTransfer st);
	
	
	@Update("update settle_transfer set batch_id=#{st.batchId},file_id=#{st.fileId},seq_no=#{st.seqNo},file_name=#{st.fileName},"
			+" settle_bank=#{st.settleBank},in_acc_no=#{st.inAccNo},in_acc_name=#{st.inAccName},in_settle_bank_no=#{st.inSettleBankNo},"
			+" in_bank_no=#{st.inBankNo},in_bank_name=#{st.inBankName},amount=#{st.amount},create_time=#{st.createTime},status=#{st.status},"
			+" err_code=#{st.errCode},err_msg=#{st.errMsg},bak1=#{st.bak1},bak2=#{st.bak2}"
			+" where id=#{st.id}")
	int updateSettleTransferById(@Param("st")SettleTransfer st);
}
