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

import cn.eeepay.framework.model.bill.ExtAccount;
import cn.eeepay.framework.model.bill.ExtAccountHistoryBalance;

/**
 * 外部用户账户历史余额表
 * 
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2016年4月12日13:45:54
 *
 */
public interface ExtAccountHistoryBalanceMapper {
	static final Logger log = LoggerFactory.getLogger(ExtAccountHistoryBalanceMapper.class);
	
	@Insert("insert into bill_ext_history_balance(account_no,bill_date,org_no,currency_no,subject_no,curr_balance,account_status,control_amount,parent_trans_balance)" 
			+"values(#{entity.accountNo},#{entity.billDate},#{entity.orgNo},#{entity.currencyNo},#{entity.subjectNo},#{entity.currBalance},#{entity.accountStatus},#{entity.controlAmount},#{entity.parentTransBalance})"
			)
	int insertExtAccountHistoryBalance(@Param("entity")ExtAccountHistoryBalance entity);
	
	@Insert("update bill_ext_history_balance set org_no=#{entity.orgNo},currency_no=#{entity.currencyNo},subject_no=#{entity.subjectNo},"
			+ "curr_balance=#{entity.currBalance},account_status=#{entity.accountStatus},control_amount=#{entity.controlAmount}"
			+ ",parent_trans_balance=#{entity.parentTransBalance} where account_no = #{entity.accountNo} and bill_date =#{entity.billDate}" 
			)
	int updateExtAccountHistoryBalance(@Param("entity")ExtAccountHistoryBalance entity);
	
	@Delete("delete from bill_ext_history_balance where bill_date=#{billDate}")
	int deleteByBillDate(@Param("billDate")String billDate);
	
	@InsertProvider(type = SqlProvider.class, method = "insertBatch")
	int insertBatch(@Param("list")List<ExtAccount> list);
	
	@InsertProvider(type = SqlProvider.class, method = "insertInto")
	int insertInto(@Param("transDate")String transDate);
	
	public class SqlProvider {
		public String insertBatch(Map<String, List<ExtAccount>> map) {
			List<ExtAccount> list = map.get("list");
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("insert into bill_ext_history_balance (account_no, bill_date, org_no, currency_no,subject_no,account_name,curr_balance,control_amount,parent_trans_balance,account_status,create_time,balance_add_from,balance_from,day_bal_flag,sum_flag,settling_amount,pre_freeze_amount) values ");
			MessageFormat messageFormat = new MessageFormat("(#'{'list[{0}].accountNo},CURDATE(),#'{'list[{0}].orgNo},#'{'list[{0}].currencyNo},#'{'list[{0}].subjectNo},#'{'list[{0}].accountName},#'{'list[{0}].currBalance},#'{'list[{0}].controlAmount},#'{'list[{0}].parentTransBalance},#'{'list[{0}].accountStatus},#'{'list[{0}].createTime},#'{'list[{0}].balanceAddFrom},#'{'list[{0}].balanceFrom},#'{'list[{0}].dayBalFlag},#'{'list[{0}].sumFlag},#'{'list[{0}].settlingAmount},#'{'list[{0}].preFreezeAmount})");
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
			sb.append("INSERT INTO bill_ext_history_balance"
					+ "(account_no, bill_date, org_no, currency_no,"
					+ "subject_no,account_name,curr_balance,control_amount,"
					+ "parent_trans_balance,account_status,create_time,"
					+ "balance_add_from,balance_from,day_bal_flag,sum_flag,"
					+ "settling_amount,pre_freeze_amount)");
			sb.append(" SELECT b.account_no, #{transDate}, b.org_no, b.currency_no,"
					+ "b.subject_no,b.account_name,b.curr_balance,b.control_amount,"
					+ "b.parent_trans_balance,b.account_status,b.create_time,"
					+ "b.balance_add_from,b.balance_from,b.day_bal_flag,b.sum_flag,"
					+ "b.settling_amount,b.pre_freeze_amount");
			sb.append(" FROM bill_ext_account b");
			System.out.println(sb.toString());
			return sb.toString();
		}
	}
	
	
}
