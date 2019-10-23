package cn.eeepay.framework.dao.bill;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import cn.eeepay.framework.model.bill.ExtTransInfo;

/**
 * 外部账交易明细表
 * @author yangle
 *
 */
public interface ExtTransInfoMapper {
	@Insert("insert into ext_trans_info(account_no,record_amount,balance,avali_balance,control_amount,settling_amount,pre_freeze_amount,serial_no,child_serial_no,record_date,record_time,debit_credit_side,summary_info,trans_order_no,trans_type)"
			+"values(#{extTransInfo.accountNo},#{extTransInfo.recordAmount},#{extTransInfo.balance},#{extTransInfo.avaliBalance},#{extTransInfo.controlAmount},#{extTransInfo.settlingAmount},#{extTransInfo.preFreezeAmount},#{extTransInfo.serialNo},"
			+ "#{extTransInfo.childSerialNo},#{extTransInfo.recordDate},#{extTransInfo.recordTime},#{extTransInfo.debitCreditSide},#{extTransInfo.summaryInfo},#{extTransInfo.transOrderNo},#{extTransInfo.transType})"
			)
	int insertExtTransInfo(@Param("extTransInfo")ExtTransInfo extTransInfo);
	
	@Insert("update ext_trans_info set record_amount=#{extTransInfo.recordAmount},balance=#{extTransInfo.balance},avali_balance=#{extTransInfo.avaliBalance},control_amount=#{extTransInfo.controlAmount},settling_amount=#{extTransInfo.settlingAmount},"
			+ " pre_freeze_amount=#{extTransInfo.preFreezeAmount},serial_no=#{extTransInfo.serialNo},child_serial_no=#{extTransInfo.childSerialNo},record_date=#{extTransInfo.recordDate},record_time=#{extTransInfo.recordTime},"
			+ " debit_credit_side=#{extTransInfo.debitCreditSide},summary_info=#{extTransInfo.summaryInfo},trans_order_no=#{extTransInfo.transOrderNo},trans_type=#{extTransInfo.transType}"
			+ " where account_no=#{extTransInfo.accountNo}"
			)
	int updateExtTransInfo(@Param("extTransInfo")ExtTransInfo extTransInfo);
	
	@Select("select a.debitMoney-b.creditMoney from "
			+ "(select sum(record_amount)as debitMoney from ext_trans_info where account_no=#{accountNo} and record_date=#{date} and debit_credit_side='debit') as a, "
			+ "(select sum(record_amount)as creditMoney from ext_trans_info where account_no=#{accountNo} and record_date=#{date} and debit_credit_side='credit') as b"
			)
	@ResultType(BigDecimal.class)
	BigDecimal findByAccountNoAndDate(@Param("accountNo")String accountNo, @Param("date")Date date);
	
}
