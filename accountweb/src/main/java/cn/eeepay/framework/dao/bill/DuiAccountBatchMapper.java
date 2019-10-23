package cn.eeepay.framework.dao.bill;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.DuiAccountBatch;

/**
 * 对账批次表
 * @author Administrator
 *
 */
public interface DuiAccountBatchMapper {
	/**
	 * 新增对账批次
	 * @param subject
	 * @return
	 */
	@Insert("insert into check_account_batch(check_batch_no,acq_enname,acq_cnname,acq_total_amount,acq_total_items,acq_total_success_items,acq_total_failed_items,total_amount,total_items,total_success_items,"
			+ "total_failed_items,check_result,check_file_date,check_time,check_file_name,operator,create_time,record_status)"
			+"values(#{dui.checkBatchNo},#{dui.acqEnname},#{dui.acqCnname},#{dui.acqTotalAmount},#{dui.acqTotalItems},#{dui.acqTotalSuccessItems},#{dui.acqTotalFailedItems},#{dui.totalAmount},#{dui.totalItems},#{dui.totalSuccessItems},"
			+ "#{dui.totalFailedItems},#{dui.checkResult},#{dui.checkFileDate},#{dui.checkTime},#{dui.checkFileName},#{dui.operator},#{dui.createTime},#{dui.recordStatus})"
			)
	int insertDuiAccountBatch(@Param("dui")DuiAccountBatch dui);
	
	
	@Update("update check_account_batch set check_batch_no=#{dui.checkBatchNo},acq_enname=#{dui.acqEnname},acq_cnname=#{dui.acqCnname},acq_total_amount=#{dui.acqTotalAmount},acq_total_items=#{dui.acqTotalItems},"
			+ "acq_total_success_items=#{dui.acqTotalSuccessItems},acq_total_failed_items=#{dui.acqTotalFailedItems},total_amount=#{dui.totalAmount},total_items=#{dui.totalItems},total_success_items=#{dui.totalSuccessItems},"
			+" total_failed_items=#{dui.totalFailedItems}, check_result=#{dui.checkResult},check_file_date=#{dui.checkFileDate},check_time=#{dui.checkTime},check_file_name=#{dui.checkFileName},"
			+" operator=#{dui.operator}, create_time=#{dui.createTime} where id=#{dui.id}")
	int updateDuiAccountBatch(@Param("dui")DuiAccountBatch dui);
			
	
	@Delete("delete from check_account_batch where id = #{id} ")
	int deleteDuiAccountBatch(@Param("id")Integer id);
	
	@Select("select * from check_account_batch where check_file_name = #{fileName} and acq_enname = #{acqEnname} order by create_time")
	@ResultMap("cn.eeepay.framework.dao.bill.DuiAccountBatchMapper.BaseResultMap")
	DuiAccountBatch findDuiAccountBatchByFileNameAndAcqEnname(@Param("fileName")String fileName,@Param("acqEnname")String acqEnname);
	
	@Select("select * from check_account_batch where check_file_name = #{fileName} order by create_time")
	@ResultMap("cn.eeepay.framework.dao.bill.DuiAccountBatchMapper.BaseResultMap")
	DuiAccountBatch findDuiAccountBatchByFileName(@Param("fileName")String fileName);
	
	@Select("select * from check_account_batch where check_batch_no = #{BatchNo} order by create_time")
	@ResultMap("cn.eeepay.framework.dao.bill.DuiAccountBatchMapper.BaseResultMap")
	DuiAccountBatch findDuiAccountBatchByBatchNo(@Param("BatchNo")String BatchNo);
	
	@Select("select * from check_account_batch where id = #{id}")
	@ResultMap("cn.eeepay.framework.dao.bill.DuiAccountBatchMapper.BaseResultMap")
	DuiAccountBatch getById(@Param("id")Integer id);
	
	
//	@Delete("delete from check_account_detail where acq_merchant_no = #{acqMerchantNo} and acq_terminal_no = #{acqTerminalNo} and acq_batch_no = #{acqBatchNo}"
//			+ " and acq_serial_no = #{acqSerialNo} and acq_account_no = #{acqAccountNo} ")
//	int deleteDuiAccountBatchByParams(@Param("acqMerchantNo")String acqMerchantNo,@Param("acqTerminalNo")String acqTerminalNo,@Param("acqBatchNo")String acqBatchNo,
//			@Param("acqSerialNo")String acqSerialNo,@Param("acqAccountNo")String acqAccountNo);
//	
//	
//	@Select("select * from check_account_detail where plate_trans_time between #{startTime} and #{endTime}")
//	@ResultMap("cn.eeepay.framework.dao.DuiAccountDetailMapper.BaseResultMap")
//	List<DuiAccountDetail> findDuiAccountBatchListByStartEndTransTime(@Param("startTime")String startTime,@Param("endTime")String endTime);
	
	/**
	 * 查询所有的对账信息
	 * @param duiAccountBatch
	 * @param sort
	 * @param page
	 * @return
	 */
	@SelectProvider( type=SqlProvider.class,method="queryDuiAccountList")
	@ResultMap("cn.eeepay.framework.dao.bill.DuiAccountBatchMapper.BaseResultMap")
	List<DuiAccountBatch> queryDuiAccountList(@Param("duiAccountBatch")DuiAccountBatch duiAccountBatch, @Param("sort")Sort sort, Page<DuiAccountBatch> page);
	
	/**
	 *  获取长款总数，金额总数
	 * @param checkBatchNo
	 * @return
	 */
	@Select("select SUM(t1.acq_trans_amount) as sum, count(*) as count from check_account_detail t1 "
			+ "where check_batch_no = #{checkBatchNo} and t1.check_account_status = 'ACQ_SINGLE'")
	@Results(value = {
			@Result(column="sum", property="sum", javaType=BigDecimal.class),
			@Result(column="count", property="count", javaType=Long.class)
		})
	Map<String, Object> getAcqTransAmountSumAndCount(@Param("checkBatchNo") String checkBatchNo) ;
	
	/**
	 * 获取短款总数，金额总数
	 * @param checkBatchNo
	 * @return
	 */
	@Select("select SUM(t1.plate_trans_amount) as sum, count(*) as count from check_account_detail t1 "
			+ "where check_batch_no = #{checkBatchNo} and t1.check_account_status = 'PLATE_SINGLE'")
	@Results(value = {
			@Result(column="sum", property="sum", javaType=BigDecimal.class),
			@Result(column="count", property="count", javaType=Long.class)
		})
	Map<String, Object> getPlateTransAmountSumAndCount(@Param("checkBatchNo") String checkBatchNo) ;
	
	/**
	 * 获取渠道交易总数，总手续费, 金额总数
	 * @param checkBatchNo
	 * @return
	 */
	@Select("select SUM(t1.acq_trans_amount) as sum, SUM(t1.acq_refund_amount) as refundSum, count(*) as count from check_account_detail t1 "
			+ "where check_batch_no = #{checkBatchNo} and t1.check_account_status != 'PLATE_SINGLE'")
	@Results(value = {
			@Result(column="sum", property="sum", javaType=BigDecimal.class),
			@Result(column="refundSum", property="refundSum", javaType=BigDecimal.class),
			@Result(column="count", property="count", javaType=Long.class)
		})
	Map<String, Object> getAcqTransSumAndCount(@Param("checkBatchNo") String checkBatchNo) ;
	
	/**
	 * 获取平台交易总数，金额总数
	 * @param checkBatchNo
	 * @return
	 */
	@Select("select SUM(t1.plate_trans_amount) as sum, count(*) as count from check_account_detail t1 "
			+ "where check_batch_no = #{checkBatchNo} and t1.check_account_status != 'ACQ_SINGLE'")
	@Results(value = {
			@Result(column="sum", property="sum", javaType=BigDecimal.class),
			@Result(column="count", property="count", javaType=Long.class)
		})
	Map<String, Object> getPlateSumAndCount(@Param("checkBatchNo") String checkBatchNo) ;
	
	/**
	 * 获取我方平台交易总数，金额总数
	 * @param checkBatchNo
	 * @return
	 */
	@Select("select SUM(t1.plate_trans_amount) as sum, count(*) as count from check_account_detail t1 "
			+ "where check_batch_no = #{checkBatchNo} and t1.check_account_status != 'PLATE_SINGLE'")
	@Results(value = {
			@Result(column="sum", property="sum", javaType=BigDecimal.class),
			@Result(column="count", property="count", javaType=Long.class)
		})
	Map<String, Object> getPlateSumAndCountMe(@Param("checkBatchNo") String checkBatchNo) ;
	
	/**
	 * 通过 组织机构编号 获取到 组织机构名称
	 * @param acqEnname
	 * @return
	 */
	@Select("select acq_cnname from check_account_batch where acq_enname = #{acqEnname}")
	@Results(value = {
			@Result(column="acq_cnname", property="acqCnname", javaType=String.class)
		})
	Map<String, Object> getAcqCnnameByAcqEnname(@Param("acqEnname") String acqEnname) ;
	
	@Update("update check_account_batch set record_status=#{recordStatus} where check_batch_no=#{checkBatchNo}")
	int updateRecordStatus(@Param("checkBatchNo")String checkBatchNo, @Param("recordStatus")Integer recordStatus);
	
	@Select("select * from check_account_batch where check_batch_no=#{checkBatchNo}")
	@ResultMap("cn.eeepay.framework.dao.bill.DuiAccountBatchMapper.BaseResultMap")
	DuiAccountBatch getByCheckBatchNo(@Param("checkBatchNo")String checkBatchNo);
	
	public class SqlProvider{
		
		
		public String queryDuiAccountList(final Map<String, Object> parameter) {
			final DuiAccountBatch duiAccountBatch = (DuiAccountBatch) parameter.get("duiAccountBatch");
			final Sort sord=(Sort)parameter.get("sort");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd") ;
			final String strCreateTime = duiAccountBatch.getCreateTime()==null?"":sdf.format(duiAccountBatch.getCreateTime()) ;
			return new SQL(){{
				SELECT("* ");
				FROM(" check_account_batch ");
				if (!StringUtils.isBlank(duiAccountBatch.getCheckBatchNo()) )
					WHERE(" check_batch_no like  \"%\"#{duiAccountBatch.checkBatchNo}\"%\" ");
				if (!StringUtils.isBlank(duiAccountBatch.getAcqEnname()) && !"ALL".equals(duiAccountBatch.getAcqEnname()) )
					WHERE(" acq_enname=#{duiAccountBatch.acqEnname} ");
				if (!StringUtils.isBlank(duiAccountBatch.getCheckResult()) && !"ALL".equals(duiAccountBatch.getCheckResult()) )
					WHERE(" check_result=#{duiAccountBatch.checkResult} ");
				if (null!=duiAccountBatch.getCreateTime() )
					WHERE(" create_time like  \"%"+strCreateTime+"%\" ");
				if(sord != null && StringUtils.isNotBlank(sord.getSidx())){
					ORDER_BY(propertyMapping(sord.getSidx(), 0)+" "+sord.getSord());
				}else{
					ORDER_BY(" check_time desc") ;
				}
			}}.toString();
		}
		

		public String propertyMapping(String name,int type){
			final String[] propertys={"checkBatchNo","acqTotalAmount","acqTotalItems","acqTotalSuccessItems","totalAmount","totalItems","totalSuccessItems",
					"totalFailedItems","checkFileDate","checkTime","createTime"};
		    final String[] columns={"check_batch_no","acq_total_amount","acq_total_items","acq_total_success_items","total_amount","total_items","total_success_items",
		    		"total_failed_items","check_file_date","check_time","create_time"};
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
