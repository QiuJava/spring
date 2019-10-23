package cn.eeepay.framework.dao.bill;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.OutAccountTaskDetail;

/**
 * 出账任务明细
 * @author Administrator
 *
 */
public interface OutAccountTaskDetailMapper {
	@Insert("insert into out_account_task_detail(out_account_task_id,acq_org_no,today_amount,up_balance,today_balance,out_account_amount,sys_time,create_time) values("
			+ "#{detail.outAccountTaskId},#{detail.acqOrgNo},#{detail.todayAmount},#{detail.upBalance},#{detail.todayBalance},#{detail.outAccountAmount},#{detail.sysTime},#{detail.createTime})")
	int insert(@Param("detail")OutAccountTaskDetail outAccountTaskDetail);
	
	/**
	 * 查询出所有的   出账任务明细
	 * @param outAccountTaskDetail
	 * @param sort
	 * @param page
	 * @return
	 */
	@SelectProvider( type=SqlProvider.class,method="findOutAccountTaskDetailList")
	@ResultMap("cn.eeepay.framework.dao.bill.OutAccountTaskDetailMapper.BaseResultMap")
	List<OutAccountTaskDetail> findOutAccountTaskDetailList(@Param("outAccountTaskDetail")OutAccountTaskDetail outAccountTaskDetail, @Param("sort")Sort sort, Page<OutAccountTaskDetail> page);
	
	/**
	 * 通过  id 查询  出账任务
	 * @param id
	 * @return
	 */
	@Select("select * FROM out_account_task_detail where id = #{id}")
	@ResultMap("cn.eeepay.framework.dao.bill.OutAccountTaskDetailMapper.BaseResultMap")
	OutAccountTaskDetail findOutAccountTaskDetailById(@Param("id")Integer id);
	
	/**
	 * 通过  out_account_task_id 查询 出账任务明细
	 * @param id
	 * @return
	 */
	@Select("select oatd.id,oatd.out_account_task_id,oatd.acq_org_no,oatd.today_amount,oatd.up_balance,oatd.today_balance,oatd.out_account_amount, "
			+ " oatd.acq_org_no "
			+ " FROM out_account_task_detail as oatd "
			+ " where  out_account_task_id = #{taskId}")
	@ResultType(OutAccountTaskDetail.class)
	List<OutAccountTaskDetail> findOutAccountTaskDetailByTaskId(@Param("taskId")Integer taskId);
	

	/**
	 * 查询出账任务明细列表
	 * @param outAccountTaskDetail
	 * @param sort
	 * @param page
	 * @return
	 */
	@SelectProvider( type=SqlProvider.class,method="findOutAccountTaskUpdateList")
	@ResultMap("cn.eeepay.framework.dao.bill.OutAccountTaskDetailMapper.BaseResultMap")
	List<OutAccountTaskDetail> findOutAccountTaskUpdateList(@Param("outAccountTaskDetail")OutAccountTaskDetail outAccountTaskDetail,@Param("sort")Sort sort,Page<OutAccountTaskDetail> page);

	@Delete("delete from out_account_task_detail where out_account_task_id=#{taskId}")
	int deleteByTaskId(@Param("taskId")Integer taskId);
	
	/**
	 * 修改出账任务明细出账金额
	 * @param outAccountTaskDetail
	 * @return
	 */
	@Update("update out_account_task_detail set out_account_amount = #{outAccountTaskDetail.outAccountAmount} where id =#{outAccountTaskDetail.id}")
	int updateOutAccountAmount(@Param("outAccountTaskDetail")OutAccountTaskDetail outAccountTaskDetail);
	
	@InsertProvider(type = SqlProvider.class, method = "inserBatch")
	int insertBatch(@Param("list")List<OutAccountTaskDetail> list);
	
	
	public class SqlProvider{
		
		public String inserBatch(Map<String, List<OutAccountTaskDetail>> map) {
			List<OutAccountTaskDetail> list = map.get("list");
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("insert into out_account_task_detail(out_account_task_id,acq_org_no,today_amount, up_balance,today_balance,out_account_amount,sys_time,create_time) values ");
			MessageFormat messageFormat = new MessageFormat("(#'{'list[{0}].outAccountTaskId},#'{'list[{0}].acqOrgNo},#'{'list[{0}].todayAmount},#'{'list[{0}].upBalance},#'{'list[{0}].todayBalance},#'{'list[{0}].outAccountAmount},#'{'list[{0}].sysTime},#'{'list[{0}].createTime})");
			for (int i = 0; i < list.size(); i++) {
				stringBuilder.append(messageFormat.format(new Integer[]{i}));
				stringBuilder.append(",");
			}
			stringBuilder.setLength(stringBuilder.length() - 1);
			return stringBuilder.toString();
		}
		
		public String findOutAccountTaskDetailList(final Map<String, Object> parameter) {
			final OutAccountTaskDetail outAccountTask = (OutAccountTaskDetail) parameter.get("outAccountTaskDetail");
			//final Sort sord=(Sort)parameter.get("sort");
			//日期格式的处理，查询时去掉createTime 的时间部分
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd") ;
			final String strCreateTime = outAccountTask.getCreateTime()==null?"":sdf.format(outAccountTask.getCreateTime()) ;
			return new SQL(){{
				SELECT("*");
				FROM(" out_account_task ");
				if (null!=outAccountTask.getCreateTime() )
					WHERE(" creat_time like  \"%"+strCreateTime+"%\" ");
				/*if(sord != null && StringUtils.isNotBlank(sord.getSidx())){
					ORDER_BY(propertyMapping(sord.getSidx(), 0)+" "+sord.getSord());
				}*/
			}}.toString();
		}
		
		public String findOutAccountTaskUpdateList(final Map<String, Object> parameter) {
			final OutAccountTaskDetail outAccountTaskDetail = (OutAccountTaskDetail) parameter.get("outAccountTaskDetail");
			final Sort sord=(Sort)parameter.get("sort");
			return new SQL(){{
				SELECT(" t.id, "+
					" 	t.out_account_task_id, "+
					" 	t.acq_org_no, "+
					" 	t.today_amount, "+
					" 	t.up_balance, "+
					" 	t.today_balance, "+
					" 	t.out_account_amount, "+
					" 	t.sys_time, "+
					" 	t.create_time ");
				FROM("out_account_task_detail AS t ");
				
				if (outAccountTaskDetail != null && outAccountTaskDetail.getOutAccountTaskId() != null) {
					WHERE("t.out_account_task_id=#{outAccountTaskDetail.outAccountTaskId}");
				}
				
				if(StringUtils.isNotBlank(sord.getSidx())){
					ORDER_BY(propertyMapping(sord.getSidx(), 0)+" "+sord.getSord());
				}
			}}.toString();
		}
		
		public String propertyMapping(String name,int type){
			final String[] propertys={"id","outAccountTaskId","acqOrgNo","historyBalance","todayBalance","outAccountAmount"};
		    final String[] columns={"id","out_account_task_id","acq_org_no","history_balance","today_balance","out_account_amount"};
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
