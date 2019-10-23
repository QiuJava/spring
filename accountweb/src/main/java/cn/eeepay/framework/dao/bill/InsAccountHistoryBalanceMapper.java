package cn.eeepay.framework.dao.bill;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.eeepay.framework.model.bill.InsAccount;
import cn.eeepay.framework.model.bill.InsAccountHistoryBalance;

/**
 * 内部帐账户历史余额表
 * 
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2016年4月12日13:45:54
 *
 */
public interface InsAccountHistoryBalanceMapper {
	static final Logger log = LoggerFactory.getLogger(InsAccountHistoryBalanceMapper.class);
	
	@Insert("insert into bill_ins_history_balance(account_no,bill_date,org_no,currency_no,subject_no,curr_balance,account_status,control_amount,parent_trans_balance)" 
			+"values(#{entity.accountNo},#{entity.billDate},#{entity.orgNo},#{entity.currencyNo},#{entity.subjectNo},#{entity.currBalance},#{entity.accountStatus},#{entity.controlAmount},#{entity.parentTransBalance})"
			)
	int insertInsAccountHistoryBalance(@Param("entity")InsAccountHistoryBalance entity);
	
	@Insert("update bill_ins_history_balance set org_no=#{entity.orgNo},currency_no=#{entity.currencyNo},subject_no=#{entity.subjectNo},"
			+ "curr_balance=#{entity.currBalance},account_status=#{entity.accountStatus},control_amount=#{entity.controlAmount}"
			+ ",parent_trans_balance=#{entity.parentTransBalance} where account_no = #{entity.accountNo} and bill_date =#{entity.billDate}" 
			)
	int updateInsAccountHistoryBalance(@Param("entity")InsAccountHistoryBalance entity);
	
	@InsertProvider(type = SqlProvider.class, method = "insertBatch")
	int insertBatch(@Param("list")List<InsAccount> list);
	
	@InsertProvider(type = SqlProvider.class, method = "insertInto")
	int insertInto(@Param("transDate")String transDate);
	
	@Delete("delete from bill_ins_history_balance where bill_date=#{billDate}")
	int deleteByBillDate(@Param("billDate")String billDate);
	
	public class SqlProvider {
		public String insertBatch(Map<String, List<InsAccount>> map) {
			List<InsAccount> list = map.get("list");
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("insert into bill_ins_history_balance (account_no, bill_date, org_no, currency_no,subject_no,account_name,curr_balance,control_amount,parent_trans_balance,account_status) values ");
			MessageFormat messageFormat = new MessageFormat("(#'{'list[{0}].accountNo},CURDATE(),#'{'list[{0}].orgNo},#'{'list[{0}].currencyNo},#'{'list[{0}].subjectNo},#'{'list[{0}].accountName},#'{'list[{0}].currBalance},#'{'list[{0}].controlAmount},#'{'list[{0}].parentTransBalance},#'{'list[{0}].accountStatus})");
			for (int i = 0; i < list.size(); i++) {
				stringBuilder.append(messageFormat.format(new String[]{i+""}));
				stringBuilder.append(",");
			}
			stringBuilder.setLength(stringBuilder.length() - 1);
			System.out.println(stringBuilder.toString());
			return stringBuilder.toString();
		}
		
		public String insertInto(Map<String, Object> parameter) {
			StringBuilder sb = new StringBuilder();
			sb.append("INSERT INTO bill_ins_history_balance"
					+ "(account_no,"
					+ "bill_date,"
					+ "org_no,"
					+ "currency_no,"
					+ "subject_no,"
					+ "account_name,"
					+ "curr_balance,"
					+ "control_amount,"
					+ "parent_trans_balance,"
					+ "account_status)");
			sb.append(" SELECT "
					+ "a.account_no,"
					+ "#{transDate},"
					+ "a.org_no,"
					+ "a.currency_no,"
					+ "a.subject_no,"
					+ "a.account_name,"
					+ "a.curr_balance,"
					+ "a.control_amount,"
					+ "a.parent_trans_balance,"
					+ "a.account_status");
			sb.append(" FROM bill_ins_account a");
			return sb.toString();
		}
	}
}
