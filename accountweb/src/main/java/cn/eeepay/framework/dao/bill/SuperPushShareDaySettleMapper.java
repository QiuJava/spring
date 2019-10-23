package cn.eeepay.framework.dao.bill;
import java.math.BigDecimal;
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
import org.apache.ibatis.type.JdbcType;

import cn.eeepay.framework.dao.nposp.SuperPushShareMapper.SqlProvider;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.SuperPushShareDaySettle;
/**
 * 数据字典
 * 
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2016年4月12日13:45:54
 *
 */
public interface SuperPushShareDaySettleMapper {
	@Insert(" insert into super_push_share_day_settle(collection_batch_no,group_time,create_time,share_type,share_no,share_name,"
			+ " share_total_amount,share_total_num,enter_account_status,enter_account_time,enter_account_message)"
			+ " values(#{superPushShareDaySettle.collectionBatchNo},#{superPushShareDaySettle.groupTime},#{superPushShareDaySettle.createTime},"
			+ " #{superPushShareDaySettle.shareType},#{superPushShareDaySettle.shareNo},"
			+ " #{superPushShareDaySettle.shareName},#{superPushShareDaySettle.shareTotalAmount},#{superPushShareDaySettle.shareTotalNum},"
			+ " #{superPushShareDaySettle.enterAccountStatus},#{superPushShareDaySettle.enterAccountTime},#{superPushShareDaySettle.enterAccountMessage})")
	@ResultMap("cn.eeepay.framework.dao.bill.SuperPushShareDaySettleMapper.BaseResultMap")
	int insert(@Param("superPushShareDaySettle")SuperPushShareDaySettle superPushShareDaySettle);
	
	
	@Update(" update super_push_share_day_settle set collection_batch_no=#{superPushShareDaySettle.collectionBatchNo},group_time=#{superPushShareDaySettle.groupTime},"
			+ " create_time=#{superPushShareDaySettle.createTime},share_type=#{superPushShareDaySettle.shareType},share_no=#{superPushShareDaySettle.shareNo},share_name=#{superPushShareDaySettle.shareName},"
			+ " share_total_amount=#{superPushShareDaySettle.shareTotalAmount},share_total_num=#{superPushShareDaySettle.shareTotalNum},enter_account_status=#{superPushShareDaySettle.enterAccountStatus},"
			+ " enter_account_time=#{superPushShareDaySettle.enterAccountTime},enter_account_message=#{superPushShareDaySettle.enterAccountMessage}"
			+"   where id=#{superPushShareDaySettle.id}")
	int updateSuperPushShareDaySettle(@Param("superPushShareDaySettle")SuperPushShareDaySettle superPushShareDaySettle);
	
	@Delete("delete from super_push_share_day_settle where id = #{id}")
	int delete(@Param("id")Integer id);
	
	
	@SelectProvider(type=SqlProvider.class,method="findSuperPushShareDaySettleList")
	@ResultMap("cn.eeepay.framework.dao.bill.SuperPushShareDaySettleMapper.BaseResultMap")
	List<SuperPushShareDaySettle> findSuperPushShareDaySettleList(@Param("superPushShareDaySettle")SuperPushShareDaySettle superPushShareDaySettle,@Param("sort")Sort sort,Page<SuperPushShareDaySettle> page);
	
	@SelectProvider(type=SqlProvider.class,method="exportSuperPushShareDaySettleList")
	@ResultMap("cn.eeepay.framework.dao.bill.SuperPushShareDaySettleMapper.BaseResultMap")
	List<SuperPushShareDaySettle> exportSuperPushShareDaySettleList(@Param("superPushShareDaySettle")SuperPushShareDaySettle superPushShareDaySettle);
	
	@Insert("<script>"
			+" insert into super_push_share_day_settle(collection_batch_no,group_time,create_time,share_type,share_no,share_name,"
			+ " share_total_amount,share_total_num,enter_account_status,enter_account_time,enter_account_message)"
            + " values "
            + " <foreach collection =\"list\" item=\"item\" index= \"index\" separator =\",\"> "
			+ "( #{item.collectionBatchNo},#{item.groupTime},#{item.createTime},#{item.shareType},"
			+ " #{item.shareNo},#{item.shareName},#{item.shareTotalAmount},"
			+ " #{item.shareTotalNum},#{item.enterAccountStatus},#{item.enterAccountTime},#{item.enterAccountMessage})"
			+ " </foreach > "
            + " </script>")
	int insertSuperPushShareDaySettleBatch(@Param("list")List<SuperPushShareDaySettle> list);
	
	@Select("SELECT * FROM super_push_share_day_settle WHERE collection_batch_no = #{collectionBatchNo}")
	@ResultMap("cn.eeepay.framework.dao.bill.SuperPushShareDaySettleMapper.BaseResultMap")
	List<SuperPushShareDaySettle> findSuperPushShareDaySettleByCollectionBatchNo(@Param("collectionBatchNo")String collectionBatchNo);
	
	@Select("SELECT * FROM super_push_share_day_settle WHERE id = #{id} and enter_account_status = 'NOENTERACCOUNT' ")
	@ResultMap("cn.eeepay.framework.dao.bill.SuperPushShareDaySettleMapper.BaseResultMap")
	SuperPushShareDaySettle findSuperPushShareDaySettleById(@Param("id")String id);
	
	@SelectProvider(type=SqlProvider.class,method="findSuperPushShareDaySettleCollection")
	@Results(value = {  
            @Result(property = "allShareTotalAmount", column = "all_share_total_amount",javaType=BigDecimal.class,jdbcType=JdbcType.DECIMAL),  
            @Result(property = "allNoEnterShareTotalAmount", column = "all_no_enter_share_total_amount",javaType=BigDecimal.class,jdbcType=JdbcType.DECIMAL),
            @Result(property = "allAccountedShareTotalAmount", column = "all_accounted_share_total_amount",javaType=BigDecimal.class,jdbcType=JdbcType.DECIMAL),
        })
	Map<String, Object> findSuperPushShareDaySettleCollection(@Param("superPushShareDaySettle")SuperPushShareDaySettle superPushShareDaySettle);
	
	@SelectProvider(type=SqlProvider.class,method="judgeSuperPushShareEnterTodayAccount")
	@Results(value = {  
            @Result(property = "allEnterAmount", column = "all_enter_amount",javaType=BigDecimal.class,jdbcType=JdbcType.DECIMAL),  
            @Result(property = "allEnterNum", column = "all_enter_num",javaType=Integer.class,jdbcType=JdbcType.INTEGER),
        })
	Map<String, Object> judgeSuperPushShareEnterTodayAccount(@Param("currentDate")String currentDate);
	
	@Select("SELECT * FROM super_push_share_day_settle where DATE_FORMAT(group_time, '%Y-%m-%d') = DATE_FORMAT(#{currentDate}, '%Y-%m-%d') and enter_account_status = 'NOENTERACCOUNT' ")
	@ResultMap("cn.eeepay.framework.dao.bill.SuperPushShareDaySettleMapper.BaseResultMap")
	List<SuperPushShareDaySettle> findSuperPushShareTodaySettleByCurrentDate(@Param("currentDate")String currentDate);
	
	public class SqlProvider{
		
		public String judgeSuperPushShareEnterTodayAccount(final Map<String, Object> parameter){
			final String currentDate = (String) parameter.get("currentDate");
			return new SQL(){{
				SELECT(" sum(share_total_amount) all_enter_amount,"
						+ " sum(1) as all_enter_num "); 
				FROM(" super_push_share_day_settle where DATE_FORMAT(group_time, '%Y-%m-%d') = DATE_FORMAT(#{currentDate}, '%Y-%m-%d') and enter_account_status = 'NOENTERACCOUNT' ");
			}}.toString();
			
		}
		
		public String findSuperPushShareDaySettleList(Map<String,Object> param){
			final SuperPushShareDaySettle superPushShareDaySettle=(SuperPushShareDaySettle)param.get("superPushShareDaySettle");
			final Sort sord=(Sort)param.get("sort");
			return new SQL(){{
				SELECT(" * ");
				FROM("super_push_share_day_settle");
				//分润创建日期
				if(StringUtils.isNotBlank(superPushShareDaySettle.getCreateTime1()) ){
					WHERE(" create_time >= #{superPushShareDaySettle.createTime1} ");
				}
				if(StringUtils.isNotBlank(superPushShareDaySettle.getCreateTime2()) ){
					WHERE(" create_time <= #{superPushShareDaySettle.createTime2} ");
				}
				//汇总时间
				if(StringUtils.isNotBlank(superPushShareDaySettle.getGroupTime1()) ){
					WHERE(" group_time >= #{superPushShareDaySettle.groupTime1} ");
				}
				if(StringUtils.isNotBlank(superPushShareDaySettle.getGroupTime2()) ){
					WHERE(" group_time <= #{superPushShareDaySettle.groupTime2} ");
				}
				//入账状态
				if(StringUtils.isNotBlank(superPushShareDaySettle.getEnterAccountStatus()) && !"ALL".equals(superPushShareDaySettle.getEnterAccountStatus()))
					WHERE(" enter_account_status like  \"%\"#{superPushShareDaySettle.enterAccountStatus}\"%\"  ");
				//商户/代理商编号
				if (!StringUtils.isBlank(superPushShareDaySettle.getShareNo()))
					WHERE(" share_no like  \"%\"#{superPushShareDaySettle.shareNo}\"%\"  ");
				//汇总批次号
				if(StringUtils.isNotBlank(superPushShareDaySettle.getCollectionBatchNo())){
					WHERE("collection_batch_no like \"%\" #{superPushShareDaySettle.collectionBatchNo} \"%\" ");
				}
				//用户类别
				if(StringUtils.isNotBlank(superPushShareDaySettle.getShareType()) && !"ALL".equals(superPushShareDaySettle.getShareType()))
					WHERE(" share_type like  \"%\"#{superPushShareDaySettle.shareType}\"%\"  ");
				//分润总金额
				if (StringUtils.isNotBlank(superPushShareDaySettle.getShareTotalAmount1()))
					WHERE(" share_total_amount >= #{superPushShareDaySettle.shareTotalAmount1} ");
				if (StringUtils.isNotBlank(superPushShareDaySettle.getShareTotalAmount2()))
					WHERE(" share_total_amount <= #{superPushShareDaySettle.shareTotalAmount2} ");
				
				if(sord != null && StringUtils.isNotBlank(sord.getSidx())){
					ORDER_BY(propertyMapping(sord.getSidx(), 0)+" "+sord.getSord());
				} else {
					ORDER_BY(" group_time desc ");
				}
			}}.toString();
		}
		
		
		public String exportSuperPushShareDaySettleList(Map<String,Object> param){
			final SuperPushShareDaySettle superPushShareDaySettle=(SuperPushShareDaySettle)param.get("superPushShareDaySettle");
			return new SQL(){{
				SELECT(" * ");
				FROM("super_push_share_day_settle");
				//分润创建日期
				if(StringUtils.isNotBlank(superPushShareDaySettle.getCreateTime1()) ){
					WHERE(" create_time >= #{superPushShareDaySettle.createTime1} ");
				}
				if(StringUtils.isNotBlank(superPushShareDaySettle.getCreateTime2()) ){
					WHERE(" create_time <= #{superPushShareDaySettle.createTime2} ");
				}
				//汇总时间
				if(StringUtils.isNotBlank(superPushShareDaySettle.getGroupTime1()) ){
					WHERE(" group_time >= #{superPushShareDaySettle.groupTime1} ");
				}
				if(StringUtils.isNotBlank(superPushShareDaySettle.getGroupTime2()) ){
					WHERE(" group_time <= #{superPushShareDaySettle.groupTime2} ");
				}
				//入账状态
				if(StringUtils.isNotBlank(superPushShareDaySettle.getEnterAccountStatus()) && !"ALL".equals(superPushShareDaySettle.getEnterAccountStatus()))
					WHERE(" enter_account_status like  \"%\"#{superPushShareDaySettle.enterAccountStatus}\"%\"  ");
				//商户/代理商编号
				if (!StringUtils.isBlank(superPushShareDaySettle.getShareNo()))
					WHERE(" share_no like  \"%\"#{superPushShareDaySettle.shareNo}\"%\"  ");
				//汇总批次号
				if(StringUtils.isNotBlank(superPushShareDaySettle.getCollectionBatchNo())){
					WHERE(" collection_batch_no like \"%\" #{superPushShareDaySettle.collectionBatchNo} \"%\" ");
				}
				//用户类别
				if(StringUtils.isNotBlank(superPushShareDaySettle.getShareType()) && !"ALL".equals(superPushShareDaySettle.getShareType()))
					WHERE(" share_type like  \"%\"#{superPushShareDaySettle.shareType}\"%\"  ");
				//分润总金额
				if (StringUtils.isNotBlank(superPushShareDaySettle.getShareTotalAmount1()))
					WHERE(" share_total_amount >= #{superPushShareDaySettle.shareTotalAmount1} ");
				if (StringUtils.isNotBlank(superPushShareDaySettle.getShareTotalAmount2()))
					WHERE(" share_total_amount <= #{superPushShareDaySettle.shareTotalAmount2} ");
					ORDER_BY(" group_time desc ");
			}}.toString();
		}
		
		public String findSuperPushShareDaySettleCollection(final Map<String, Object> parameter){
			final SuperPushShareDaySettle superPushShareDaySettle=(SuperPushShareDaySettle)parameter.get("superPushShareDaySettle");
			return new SQL(){{
				SELECT(" sum(a.share_total_amount) all_share_total_amount,"
						+ " sum(case when a.enter_account_status = 'NOENTERACCOUNT' then a.share_total_amount else 0 end) as all_no_enter_share_total_amount,"
						+ " sum(case when a.enter_account_status = 'ENTERACCOUNTED' then a.share_total_amount else 0 end) as all_accounted_share_total_amount ");
				
				FROM(" super_push_share_day_settle as a ");
				//分润创建日期
				if(StringUtils.isNotBlank(superPushShareDaySettle.getCreateTime1()) ){
					WHERE(" a.create_time >= #{superPushShareDaySettle.createTime1} ");
				}
				if(StringUtils.isNotBlank(superPushShareDaySettle.getCreateTime2()) ){
					WHERE(" a.create_time <= #{superPushShareDaySettle.createTime2} ");
				}
				//汇总时间
				if(StringUtils.isNotBlank(superPushShareDaySettle.getGroupTime1()) ){
					WHERE(" a.group_time >= #{superPushShareDaySettle.groupTime1} ");
				}
				if(StringUtils.isNotBlank(superPushShareDaySettle.getGroupTime2()) ){
					WHERE(" a.group_time <= #{superPushShareDaySettle.groupTime2} ");
				}
				//入账状态
				if(StringUtils.isNotBlank(superPushShareDaySettle.getEnterAccountStatus()) && !"ALL".equals(superPushShareDaySettle.getEnterAccountStatus()))
					WHERE(" a.enter_account_status like  \"%\"#{superPushShareDaySettle.enterAccountStatus}\"%\"  ");
				//商户/代理商编号
				if (!StringUtils.isBlank(superPushShareDaySettle.getShareNo()))
					WHERE(" a.share_no like  \"%\"#{superPushShareDaySettle.shareNo}\"%\"  ");
				//汇总批次号
				if(StringUtils.isNotBlank(superPushShareDaySettle.getCollectionBatchNo())){
					WHERE(" a.collection_batch_no like \"%\" #{superPushShareDaySettle.collectionBatchNo} \"%\" ");
				}
				//用户类别
				if(StringUtils.isNotBlank(superPushShareDaySettle.getShareType()) && !"ALL".equals(superPushShareDaySettle.getShareType()))
					WHERE(" a.share_type like  \"%\"#{superPushShareDaySettle.shareType}\"%\"  ");
				//分润总金额
				if (StringUtils.isNotBlank(superPushShareDaySettle.getShareTotalAmount1()))
					WHERE(" share_total_amount >= #{superPushShareDaySettle.shareTotalAmount1} ");
				if (StringUtils.isNotBlank(superPushShareDaySettle.getShareTotalAmount2()))
					WHERE(" share_total_amount <= #{superPushShareDaySettle.shareTotalAmount2} ");

				ORDER_BY(" group_time desc ");
			}}.toString();
		}
		
		public String propertyMapping(String name,int type){
			final String[] propertys={"id","collectionBatchNo","groupTime","createTime","shareType","shareNo","shareName"};
		    final String[] columns={"id","collection_batch_no","group_time","create_time","share_type","share_no","share_name"};
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
