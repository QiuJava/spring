package cn.eeepay.framework.dao.bill;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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
import cn.eeepay.framework.model.bill.OutAccountTask;

/**
 * 出账任务
 * @author Administrator
 *
 */
public interface OutAccountTaskMapper{
	
	@Insert("insert into out_account_task("
			+ "trans_time,"
			+ "trans_amount,"
			+ "up_balance,"
			+ "up_today_balance,"
			+ "out_account_task_amount,"
			+ "up_company_count,"
			+ "out_bill_range,"
			+ "sys_time,"
			+ "creator,"
			+ "create_time,"
			+ "acq_enname"
			+ ") "
			+ "values("
			+ "#{task.transTime}, "
			+ "#{task.transAmount}, "
			+ "#{task.upBalance}, "
			+ "#{task.upTodayBalance}, "
			+ "#{task.outAccountTaskAmount}, "
			+ "#{task.upCompanyCount}, "
			+ "#{task.outBillRange}, "
			+ "#{task.sysTime}, "
			+ "#{task.creator}, "
			+ "#{task.createTime},"
			+ "#{task.acqEnname})")
	@SelectKey(statement=" SELECT LAST_INSERT_ID() AS id", keyProperty="task.id", before=false, resultType=int.class) 
	int insert(@Param("task")OutAccountTask task);
	
	@Update("update out_account_task set "
			+ "trans_time=#{task.transTime},"
			+ "trans_amount=#{task.transAmount},"
			+ "up_balance=#{task.upBalance},"
			+ "up_today_balance=#{task.upTodayBalance},"
			+ "out_account_task_amount=#{task.outAccountTaskAmount},"
			+ "up_company_count=#{task.upCompanyCount},"
			+ "out_bill_range=#{task.outBillRange},"
			+ "sys_time=#{task.sysTime},"
			+ "out_account_id=#{task.outAccountId},"
			+ "create_time=#{task.createTime},"
			+ "acq_enname=#{task.acqEnname},"
			+ "bill_status=#{task.billStatus} ,"
			+ "updator=#{task.updator},"
			+ "update_time=now() "
			+ "where id=#{task.id}")
	int update(@Param("task")OutAccountTask task);
	
	@Select("select * from out_account_task where trans_time=#{transTime} and acq_enname=#{acqEnname} and out_bill_range=#{outBillRange}")
	@ResultMap("cn.eeepay.framework.dao.bill.OutAccountTaskMapper.BaseResultMap")
	List<OutAccountTask> findByTransTimeAndAcqEnname(@Param("transTime")Date transTime, @Param("acqEnname")String acqEnname, @Param("outBillRange")String outBillRange);
	
	@Select("select * from out_account_task where trans_time=#{transTime}")
	@ResultMap("cn.eeepay.framework.dao.bill.OutAccountTaskMapper.BaseResultMap")
	List<OutAccountTask> findOutAccountTaskByTransTime(@Param("transTime")Date transTime);
	
	@Update("update out_account_task set bill_status=-1 where DATE(trans_time)=DATE(#{transTime}) and bill_status=0")
	int updateToClosedByTransTime(@Param("transTime")Date transTime);
	
	@Select("select sum(ob.out_account_task_amount)  as amount from out_account_task oat left join out_bill ob on oat.id=ob.out_account_task_id where oat.acq_enname=#{acqEnname} and oat.trans_time=#{transTime} and ob.out_bill_status='0'")
	@ResultType(BigDecimal.class)
	BigDecimal calcOutAccountTaskAmountByAcqEnname(@Param("acqEnname")String acqEnname, @Param("transTime")Date transTime);
	
	/**
	 * 查询出所有的   出账任务
	 * @param outAccountTask
	 * @param sort
	 * @param page
	 * @return
	 */
	@SelectProvider( type=SqlProvider.class,method="findOutAccountTaskList")
	@ResultMap("cn.eeepay.framework.dao.bill.OutAccountTaskMapper.BaseResultMap")
	List<OutAccountTask> findOutAccountTaskList(@Param("param")Map<String, Object> param, @Param("sort")Sort sort, Page<OutAccountTask> page);
	
	/**
	 * 通过  id 查询  出账任务
	 * @param id
	 * @return
	 */
	@Select("select * FROM out_account_task where id = #{id}")
	@ResultMap("cn.eeepay.framework.dao.bill.OutAccountTaskMapper.BaseResultMap")
	OutAccountTask findOutAccountTaskById(@Param("id")Integer id);
	
	@Update("update out_account_task set out_account_task_amount=#{amount},updator=#{updator},update_time=now() where id=#{id}")
	int updateOutAccountTaskAmount(@Param("id")Integer id, @Param("amount")BigDecimal amount, @Param("updator")String updator);
	
//	/**
//	 * 通过  out_account_task_id 查询 出账任务明细
//	 * @param id
//	 * @return
//	 */
//	@Select("select oatd.id,oatd.out_account_task_id,sc.sys_name,oatd.today_amount,oatd.historical_balance,oatd.today_balance,oatd.out_account_amount "
//			+ "FROM out_account_task_detail as oatd,sys_dict as sc "
//			+ "where oatd.acq_org_id = sc.id AND out_account_task_id = #{id}")
//	@ResultMap("cn.eeepay.framework.dao.OutAccountTaskDetailMapper.BaseResultMap")
//	List<OutAccountTaskDetail> findOutAccountTaskDetailByTaskId(@Param("id")Integer id);
	

//	/**
//	 * 查询出账任务明细列表
//	 * @param outAccountTaskDetail
//	 * @param sort
//	 * @param page
//	 * @return
//	 */
//	@SelectProvider( type=SqlProvider.class,method="findOutAccountTaskUpdateList")
//	@ResultMap("cn.eeepay.framework.dao.OutAccountTaskDetailMapper.BaseResultMap")
//	List<OutAccountTaskDetail> findOutAccountTaskUpdateList(@Param("outAccountTaskDetail")OutAccountTaskDetail outAccountTaskDetail,@Param("sort")Sort sort,Page<OutAccountTaskDetail> page);
//
//	
//	/**
//	 * 修改出账任务明细出账金额
//	 * @param outAccountTaskDetail
//	 * @return
//	 */
//	@Update("update out_account_task_detail set out_account_amount = #{outAccountTaskDetail.outAccountAmount} where id =#{outAccountTaskDetail.id}")
//	int updateOutAccountAmount(@Param("outAccountTaskDetail")OutAccountTaskDetail outAccountTaskDetail);
	
	
	public class SqlProvider{
		
		public String findOutAccountTaskList(final Map<String, Object> parameter) {
			final Map<String, Object> param = (Map<String, Object>) parameter.get("param");
			final String startTime = (String) param.get("start");
			final String endTime = (String) param.get("end");
			final String billStatus = (String) param.get("billStatus");
			final String acqEnname = (String) param.get("acqEnname");
			final String outBillRange = (String) param.get("outBillRange");
			
			String idStr = param.get("id") == null ? "": param.get("id").toString();
			String outAccountIdStr = param.get("outAccountId") == null ? "": param.get("outAccountId").toString();
			
			param.put("id", idStr);
			param.put("outAccountId", outAccountIdStr);
			
			final Sort sord=(Sort)parameter.get("sort");
			return new SQL(){{
				SELECT(" oat.trans_time, oat.*, ob.out_bill_status");
				FROM(" out_account_task oat left join out_bill ob on oat.id=ob.out_account_task_id ");
				if (null != param){
					if (StringUtils.isNotBlank(startTime)) {
						WHERE(" oat.trans_time >= #{param.start} ");
					}
					if (StringUtils.isNotBlank(endTime)) {
						WHERE(" oat.trans_time <= #{param.end} ");
					}
					
					if (StringUtils.isNotBlank(idStr)) {//出账任务id
						WHERE(" oat.id  =  #{param.id}  ");
					}
					if (StringUtils.isNotBlank(outAccountIdStr) ) {//出账单id
						WHERE(" oat.out_account_id   =  #{param.outAccountId}  ");
					}
					
					if (StringUtils.isNotBlank(acqEnname) && !"ALL".equalsIgnoreCase(acqEnname)) {
						WHERE(" oat.acq_enname = #{param.acqEnname}");
					}
					if (StringUtils.isNotBlank(billStatus) && !"-2".equals(billStatus)) {
						WHERE(" oat.bill_status = #{param.billStatus}");
					}
					if (StringUtils.isNotBlank(outBillRange) && !"ALL".equals(outBillRange)) {
						WHERE(" oat.out_bill_range = #{param.outBillRange}");
					}
					
				}
				if(StringUtils.isNotBlank(sord.getSidx())){
					ORDER_BY(propertyMapping(sord.getSidx(), 0)+" "+sord.getSord());
				} else {
					ORDER_BY("oat.trans_time desc,oat.id desc");
				}
			}}.toString();
		}
		
//		public String findOutAccountTaskUpdateList(final Map<String, Object> parameter) {
//			final OutAccountTaskDetail outAccountTaskDetail = (OutAccountTaskDetail) parameter.get("outAccountTaskDetail");
//			final Sort sord=(Sort)parameter.get("sort");
//			return new SQL(){{
//				SELECT(" t.id, "+
//					" 	t.out_account_task_id, "+
//					" 	t.acq_org_id, "+
//					" 	t.today_amount, "+
//					" 	t.historical_balance, "+
//					" 	t.today_balance, "+
//					" 	t.out_account_amount, "+
//					" 	t.sys_time, "+
//					" 	t.create_time ");
//				FROM("out_account_task_detail AS t ");
//				
//				if(StringUtils.isNotBlank(sord.getSidx())){
//					ORDER_BY(propertyMapping(sord.getSidx(), 0)+" "+sord.getSord());
//				}
//			}}.toString();
//		}
		
		public String propertyMapping(String name,int type){
			final String[] propertys={"id","outBillStatus","outAccountId","outAccountTaskAmount","upCompanyCount","todayBalance","outAccountAmount","transTime","transAmount","upBalance","sysTime","acqEnname","billStatus"};
		    final String[] columns={"id","out_bill_status","out_account_id","out_account_task_amount","up_company_count","today_balance","out_account_amount","trans_time","trans_amount","up_balance","sys_time","acq_enname","bill_status"};
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
