package cn.eeepay.framework.dao.bill;

import cn.eeepay.framework.model.bill.AgentAccountDay;
import cn.eeepay.framework.model.bill.AgentEverydayBalance;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 代理商余额汇总
 *
 *
 */
public interface AgentDayBalanceMapper {


	@Insert("<script>"
			+" insert into agent_everyday_balance(agent_no,agent_name,agent_node,agent_level,trans_in_amount,trans_out_amount,freeze_amount,"
			+ " account_no,subject_no,account_status,create_time,balance,init_balance,batch_no,record_date)"
			+ " values "
			+ " <foreach collection =\"list\" item=\"item\" index= \"index\" separator =\",\"> "
			+ "( #{item.agentNo},#{item.agentName},#{item.agentNode},#{item.agentLevel},"
			+ " #{item.transInAmount},#{item.transOutAmount},#{item.freezeAmount},"
			+ " #{item.accountNo},#{item.subjectNo},#{item.accountStatus},#{item.createTime},#{item.balance},#{item.initBalance},#{item.batchNo},#{item.recordDate})"
			+ " </foreach > "
			+ " </script>")
	int insertAgentDayBalanceBatch(@Param("list") List<AgentEverydayBalance> list);



	//查询代理商当天入账出账汇总金额
	@Select(" SELECT dd.* ,  sum(dd.record_amount) as record_auount_sum FROM ( SELECT cc.account_status ,cc.control_amount, bb.user_id,bb.subject_no, aa.record_amount , aa.balance,aa.avali_balance , " +
			"aa.account_no ,aa.id ,aa.trans_type, aa.debit_credit_side FROM (  SELECT serial_no, debit_credit_side , trans_type, record_amount ,balance,avali_balance , account_no ,id " +
			" from ext_trans_info  WHERE record_date=#{recordDate} and serial_no not in ( SELECT serial_no   FROM ext_trans_info  WHERE trans_type='000000' )) as aa ," +
			" ext_account_info bb , bill_ext_account cc  WHERE aa.account_no=bb.account_no and aa.account_no=cc.account_no " +
			" and bb.subject_no =#{subjectNo} and debit_credit_side=#{debitCreditSide} and trans_type!='000000' ORDER BY aa.id DESC ) dd  GROUP BY account_no  ")
	@ResultMap("cn.eeepay.framework.dao.bill.AgentDayBalanceMapper.agentAccountDay")
	List<AgentAccountDay> findAgentShareAccountList(@Param("debitCreditSide")String debitCreditSide, @Param("recordDate")String recordDate,@Param("subjectNo")String subjectNo) ;



	//查询用户某科目账号
	@Select(" SELECT  i.account_no , b.control_amount ,b.account_status FROM ext_account_info i , bill_ext_account b  " +
			" WHERE i.account_no=b.account_no and  i.user_id=#{userId} and i.subject_no=#{subjectNo}  ")
	@ResultMap("cn.eeepay.framework.dao.bill.AgentDayBalanceMapper.agentAccountDay")
	AgentAccountDay findAccountByUserId(@Param("userId")String userId, @Param("subjectNo")String subjectNo) ;

	//查询账户某时间的余额
	@Select("SELECT aa.account_no, IFNULL(aa.balance,0) as nowBalance , IFNULL(bb.balance,0) as befBalance " +
			" from ( SELECT id,account_no,balance , record_date FROM ext_trans_info WHERE account_no=#{accountNo} and  record_date <=#{recordDate} ORDER BY id DESC LIMIT 1 ) aa " +
			"left JOIN ( SELECT id,account_no,balance , record_date FROM ext_trans_info WHERE account_no=#{accountNo} and  record_date <#{recordDate}  ORDER BY id DESC LIMIT 1 ) bb " +
			"on aa.account_no=bb.account_no")
	@ResultMap("cn.eeepay.framework.dao.bill.AgentDayBalanceMapper.agentAccountDay")
	AgentAccountDay findAccountNowBalanceByDate(@Param("accountNo")String accountNo, @Param("recordDate")String recordDate) ;

	

		









}
