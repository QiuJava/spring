package cn.eeepay.framework.dao.bill;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.eeepay.framework.model.bill.OutAccountTask;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.OutBill;
import org.springframework.security.access.method.P;

/**
 * 
 * @author Administrator
 *
 */
public interface OutBillMapper {
	
	@Insert("insert into out_bill(out_account_task_id,out_account_task_amount,calc_out_amount,balance_up_count,balance_merchant_count,sys_time,create_time,creator,acq_enname)"
			+"values(#{outBill.outAccountTaskId},#{outBill.outAccountTaskAmount},#{outBill.calcOutAmount},#{outBill.balanceUpCount},#{outBill.balanceMerchantCount},#{outBill.sysTime},#{outBill.createTime},#{outBill.creator},#{outBill.acqEnname})"
			)
	@SelectKey(statement=" SELECT LAST_INSERT_ID() AS id", keyProperty="outBill.id", before=false, resultType=int.class)
	int insertOutBill(@Param("outBill")OutBill outBill);
	
	
	@Update("update out_bill set out_account_task_id=#{outBill.outAccountTaskId},out_account_task_amount=#{outBill.outAccountTaskAmount},calc_out_amount=#{outBill.calcOutAmount}"
			+",balance_up_count=#{outBill.balanceUpCount},balance_merchant_count=#{outBill.balanceMerchantCount},out_bill_status=#{outBill.outBillStatus},sys_time=#{outBill.sysTime}"
			+",create_time=#{outBill.createTime},account_owner=#{outBill.accountOwner},acq_enname=#{outBill.acqEnname},file_name=#{outBill.fileName},updator=#{outBill.updator},update_time= now() ,out_account_bill_method= #{outBill.outAccountBillMethod}"
			+ " where id=#{outBill.id}")
	int updateOutBillById(@Param("outBill")OutBill outBill);
	
	@Update("update out_bill set out_bill_status=1,updator=#{updator},update_time=now() where id=#{id}")
	int updateOutBillStatus(@Param("id")Integer id, @Param("updator")String updator);
	
	@Update("update out_bill set out_account_task_amount=#{amount} where id=#{id}")
	int updateOutAccountTaskAmount(@Param("id")Integer id, @Param("amount")BigDecimal amount);
	
	@Update("update out_bill set calc_out_amount=#{amount},updator=#{updator},update_time=now() where id=#{id}")
	int updateCalcOutAmount(@Param("id")Integer id, @Param("amount")BigDecimal amount, @Param("updator")String updator);
	
	@Update("update out_bill set back_operator=#{backOperator},back_time =now() where id=#{outBillId}")
	int updateBackOperator(@Param("outBillId")Integer outBillId, @Param("backOperator")String uname);
	
	@Delete("delete from out_bill where id = #{id}")
	int deleteOutBillById(@Param("id")Integer id);
	
	@Delete("delete from out_bill where out_account_task_id = #{taskId}")
	int deleteOutBillByTaskId(@Param("taskId")Integer taskId);
	
	@Select("select * from out_bill where out_account_task_id = #{taskId}")
	@ResultMap("cn.eeepay.framework.dao.bill.OutBillMapper.BaseResultMap")
	OutBill findOutBillByTaskId(@Param("taskId")Integer taskId);
	
	@Select("select * from out_bill where id = #{id}")
	@ResultMap("cn.eeepay.framework.dao.bill.OutBillMapper.BaseResultMap")
	OutBill findOutBillById(@Param("id")Integer id);

	@Select("select ob.*,ot.out_bill_range from out_bill ob left join out_account_task ot on ob.id=ot.out_account_id where ob.id = #{id}")
	@ResultMap("cn.eeepay.framework.dao.bill.OutBillMapper.BaseResultMap")
	OutBill findOutBillTaskById(@Param("id")Integer id);

	@SelectProvider( type=SqlProvider.class,method="findOutBillList")
	@ResultMap("cn.eeepay.framework.dao.bill.OutBillMapper.BaseResultMap")
	List<OutBill> findOutBillList(@Param("createTime1")String createTime1,@Param("createTime2")String createTime2, @Param("param")Map<String, String> param, @Param("sort")Sort sort, Page<OutBill> page);
	
	@Select("select * from out_bill where out_bill_status='0'")
	@ResultMap("cn.eeepay.framework.dao.bill.OutBillMapper.BaseResultMap")
	List<OutBill> findFailedOutBill();
	
	@Update("update out_bill set out_bill_status='2' where DATE(create_time)=DATE(#{transTime}) and out_bill_status='0'")
	int updateToClosedByTransTime(@Param("transTime")Date transTime);
	
	@Update("update out_bill set file_name=#{fileName},updator=#{updator},update_time=now()  where id=#{outBillId}")
	int updateExportFileName(@Param("outBillId")Integer outBillId, @Param("fileName")String fileName, @Param("updator")String updator);
	
	@Select("select id as id, CONCAT(id,'  ',' (',acq_enname,') ') as acq_enname from out_bill where out_bill_status = '0'")
	@ResultMap("cn.eeepay.framework.dao.bill.OutBillMapper.BaseResultMap")
	List<OutBill> findAllNoOutBillId();
	
	@Select("select calc_out_amount from out_bill where acq_enname = #{acqEnname} and out_bill_status = '0'")
	@ResultType(BigDecimal.class)
	BigDecimal findCalAmountByAcqNameAndOutBillStatus(@Param("acqEnname")String acqEnname);

    @Select("select date_add(create_time,INTERVAL 1 DAY) create_time from out_bill t2 where acq_enname = #{acqEnname} and create_time < #{createTime} and out_bill_status = '1' order by create_time desc LIMIT 1")
    @ResultType(OutBill.class)
    OutBill findCreateTime(@Param("acqEnname") String acqEnname, @Param("createTime") String createTime);

    @Select(" select date_add(trans_time,INTERVAL 1 DAY) trans_time from out_account_task oat " +
            " where oat.acq_enname = #{acqName} and oat.trans_time < #{tTime} and EXISTS (SELECT 1 FROM out_bill b2 WHERE b2.out_bill_status = '1' AND b2.out_account_task_id = oat.id) order by oat.create_time desc limit 1 ")
    @ResultType(OutAccountTask.class)
    OutAccountTask findTransTime(@Param("acqName") String acqName, @Param("tTime") String tTime);

    public class SqlProvider{
		
		public String findOutBillList(final Map<String, Object> parameter) {
			final String createTime1 = (String) parameter.get("createTime1");
			final String createTime2 = (String) parameter.get("createTime2");
			final Map<String, String> param = (Map<String, String>) parameter.get("param");
			final String acqEnname = param.get("acqEnname");
			final String outBillRange = param.get("outBillRange");
			final String outBillStatus = param.get("outBillStatus");
			final String fileName = param.get("fileName");
			final Sort sord=(Sort)parameter.get("sort");
			
			String idStr = param.get("id") == null ? "": param.get("id").toString();
			param.put("id", idStr);
			
			return new SQL(){{
				SELECT(" ob.create_time,	ob.id, "+
						" 	ob.out_account_task_id, "+
						" 	ob.out_account_task_amount, "+
						" 	ob.calc_out_amount, "+
						" 	ob.balance_up_count, "+
						" 	ob.balance_merchant_count, "+
						" 	ob.out_bill_status, "+
						" 	ob.sys_time, "+
						" 	ob.create_time, "+
						" 	ob.creator, "+
						" 	ob.back_operator, "+
						"   ob.acq_enname, "+
						"   obd.out_amount, "+
						"   ob.out_account_bill_method, "+
						"   ot.out_bill_range, "+
						"   ob.file_name ");
				FROM(" out_bill ob  left join acq_out_bill obd on ob.id = obd.out_bill_id left join out_account_task ot on ob.id=ot.out_account_id ");
				if (StringUtils.isNotBlank(createTime1) )
					WHERE(" ob.create_time >=  #{createTime1} ");
				if (StringUtils.isNotBlank(createTime2) )
					WHERE(" ob.create_time <=  #{createTime2} ");
				if (StringUtils.isNotBlank(acqEnname) && !"ALL".equalsIgnoreCase(acqEnname)) 
					WHERE(" ob.acq_enname=#{param.acqEnname} ");
				if (StringUtils.isNotBlank(outBillRange) && !"ALL".equalsIgnoreCase(outBillRange))
					WHERE(" ot.out_bill_range=#{param.outBillRange} ");
				if (StringUtils.isNotBlank(outBillStatus) && !"-2".equals(outBillStatus))
					WHERE(" ob.out_bill_status = #{param.outBillStatus}");
				if (StringUtils.isNotBlank(fileName)) 
					WHERE(" ob.file_name = #{param.fileName}");
				
				if (StringUtils.isNotBlank(idStr))//出账单id
					WHERE(" ob.id  =  #{param.id}  ");

				if(sord != null && StringUtils.isNotBlank(sord.getSidx())){
					ORDER_BY(propertyMapping(sord.getSidx(), 0)+" "+sord.getSord());
				} else {
					ORDER_BY("ob.id desc");
				}
			}}.toString();
		}
		
		
		public String propertyMapping(String name,int type){
			final String[] propertys={"id","outAccountTaskId","outAccountTaskAmount","calcOutAmount","balanceUpCount","balanceMerchantCount","outBillStatus","createTime"};
		    final String[] columns={"ob.id","ob.out_account_task_id","ob.out_account_task_amount","ob.calc_out_amount","ob.balance_up_count","ob.balance_merchant_count","ob.out_bill_status","ob.create_time"};
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
