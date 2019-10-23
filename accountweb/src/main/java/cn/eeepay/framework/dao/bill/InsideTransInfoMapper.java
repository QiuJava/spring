package cn.eeepay.framework.dao.bill;

import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.InsAccount;
import cn.eeepay.framework.model.bill.InsideTransInfo;
/**
 * 内部账交易明细表
 * @author Administrator
 *
 */
public interface InsideTransInfoMapper {
	
	@Insert("insert into inside_trans_info(account_no,record_amount,balance,avali_balance,serial_no,child_serial_no,record_date,record_time,debit_credit_side,summary_info)"
			+"values(#{insideTransInfo.accountNo},#{insideTransInfo.recordAmount},#{insideTransInfo.balance},#{insideTransInfo.avaliBalance},#{insideTransInfo.serialNo},#{insideTransInfo.childSerialNo},#{insideTransInfo.recordDate},#{insideTransInfo.recordTime},#{insideTransInfo.debitCreditSide},#{insideTransInfo.summaryInfo})"
			)
	int insertInsideTransInfo(@Param("insideTransInfo")InsideTransInfo insideTransInfo);
	
	@Insert("update inside_trans_info set record_amount=#{insideTransInfo.recordAmount},balance=#{insideTransInfo.balance},avali_balance=#{insideTransInfo.avaliBalance},serial_no=#{insideTransInfo.serialNo},child_serial_no=#{insideTransInfo.childSerialNo},"
			+ "record_date=#{insideTransInfo.recordDate},record_time=#{insideTransInfo.recordTime},debit_credit_side=#{insideTransInfo.debitCreditSide},summary_info=#{insideTransInfo.summaryInfo} where account_no=#{insideTransInfo.accountNo}"
			)
	int updateInsideTransInfo(@Param("insideTransInfo")InsideTransInfo insideTransInfo);
	
	@Select("select account_no,record_amount,balance,avali_balance,serial_no,child_serial_no,record_date,record_time,debit_credit_side,summary_info from inside_trans_info where account_no = #{accountNo}")
	@ResultMap("cn.eeepay.framework.dao.bill.InsideTransInfoMapper.BaseResultMap")
	InsAccount getInsideTransInfo(@Param("accountNo")String accountNo);
	
	
	public class SqlProvider{
		/*public String getSubjectInfoList(Map<String,Object> map){
			System.out.println(map);
			StringBuffer sbf=new StringBuffer("select * from bill_subject ");
			return sbf.toString();
		}
		public String exsitsSubject(final Map<String,Object> map){
			return new SQL(){{
				SELECT(" subject_no,subject_name,subject_legal_no,subject_level ");
				FROM(" bill_subject ");
				String type=map.get("type")==null?"inner":map.get("type").toString();
				if(StringUtils.equalsIgnoreCase("legal", type)){
					WHERE(" subject_legal_no=#{subjectNo} ");
				}else{
					WHERE(" subject_no=#{subjectNo} ");
				}
			}}.toString();
		}*/
		
		
		
	}
}
