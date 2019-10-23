package cn.eeepay.framework.dao.bill;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.AcqOutBill;

/**
 * 
 * @author Administrator
 *
 */
public interface AcqOutBillMapper {
	
	@Insert("insert into acq_out_bill(out_bill_id,acq_org_no,today_amount,today_history_balance,today_balance,out_account_task_amount,up_balance,calc_out_amount,out_amount,out_bill_result,acq_reference,create_time)"
			+"values(#{acqOutBill.outBillId},#{acqOutBill.acqOrgNo},#{acqOutBill.todayAmount},#{acqOutBill.todayHistoryBalance},#{acqOutBill.todayBalance},#{acqOutBill.outAccountTaskAmount},#{acqOutBill.upBalance},#{acqOutBill.calcOutAmount},#{acqOutBill.outAmount}"
			+ ",#{acqOutBill.outBillResult},#{acqOutBill.acqReference},#{acqOutBill.createTime})"
			)
	int insertAcqOutBill(@Param("acqOutBill")AcqOutBill acqOutBill);
	
	
	@Update("update acq_out_bill set out_bill_id=#{acqOutBill.outBillId},acq_org_no=#{acqOutBill.acqOrgNo} "
			+", today_amount=#{acqOutBill.todayAmount},today_history_balance=#{acqOutBill.todayHistoryBalance},today_balance=#{acqOutBill.todayBalance}"
			+", out_account_task_amount=#{acqOutBill.outAccountTaskAmount},calc_out_amount=#{acqOutBill.calcOutAmount},out_amount=#{acqOutBill.outAmount},out_bill_result=#{acqOutBill.outBillResult},acq_reference=#{acqOutBill.acqReference}"
			+ ",create_time=#{acqOutBill.createTime}"
			+ " where id=#{acqOutBill.id}")
	int updateAcqOutBillById(@Param("acqOutBill")AcqOutBill acqOutBill);
	
	@Update("update acq_out_bill set calc_out_amount=#{amount} where out_bill_id=#{id}")
	int udpateCalcOutAmount(@Param("amount")BigDecimal amount, @Param("id")Integer id);
	
	
	@Delete("delete from acq_out_bill where id = #{id}")
	int deleteAcqOutBillById(@Param("id")Integer id);
	
	@Delete("delete from acq_out_bill where out_bill_id = #{outBillId}")
	int deleteAcqOutBillByOutBillId(@Param("outBillId")Integer outBillId);
	
	@SelectProvider( type=SqlProvider.class,method="findAcqOutBillList")
	@ResultMap("cn.eeepay.framework.dao.bill.AcqOutBillMapper.BaseResultMap")
	List<AcqOutBill> findAcqOutBillList(@Param("acqOutBill")AcqOutBill acqOutBill,@Param("sort")Sort sort,Page<AcqOutBill> page);

	@Select("select * from acq_out_bill where out_bill_id=#{outBillId}")
	@ResultMap("cn.eeepay.framework.dao.bill.AcqOutBillMapper.BaseResultMap")
	List<AcqOutBill> findByOutBillId(@Param("outBillId")Integer outBillId);
	
	@Update("update acq_out_bill set out_amount=#{outAmount} where out_bill_id=#{id}")
	int updateOutAmountByOutBillId(@Param("outAmount")BigDecimal outAmount, @Param("id")Integer id);
	
	public class SqlProvider{
		
		public String findAcqOutBillList(final Map<String, Object> parameter) {
			final AcqOutBill acqOutBill = (AcqOutBill) parameter.get("acqOutBill");
			final Sort sord=(Sort)parameter.get("sort");
			return new SQL(){{
				SELECT("*");
				FROM(" acq_out_bill ");
					WHERE(" out_bill_id  = #{acqOutBill.outBillId} ");
				if(sord != null && StringUtils.isNotBlank(sord.getSidx())){
					ORDER_BY(propertyMapping(sord.getSidx(), 0)+" "+sord.getSord());
				}
			}}.toString();
		}
		
		
		
		public String propertyMapping(String name,int type){
			final String[] propertys={"id","outBillId","acqOrgNo","todayAmount","todayHistoryBalance","todayBalance","outAccountTaskAmount","upBalance","calcOutAmount","outAmount","createTime"};
		    final String[] columns={"id","out_bill_id","acq_org_no","today_amount","today_history_balance","today_balance","out_account_task_amount","up_balance","calc_out_amount","out_amount","create_time"};
		    if(StringUtils.isNotBlank(name)){
		    	if(type==0){//属性查出字段名
		    		for(int i=0;i<propertys.length;i++){
		    			if(name.equalsIgnoreCase(propertys[i])){
		    				return columns[i];
		    			}
		    		}
		    	}else if(type==1){//字段名查出属性
		    		for(int i=0;i<propertys.length;i++){
		    			if(name.equalsIgnoreCase(columns[i])){
		    				return propertys[i];
		    			}
		    		}
		    	}
		    }
			return null;
		}
		
		
	}

	
}
