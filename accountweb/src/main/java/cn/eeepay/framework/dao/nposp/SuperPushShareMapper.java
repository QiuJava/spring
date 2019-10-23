package cn.eeepay.framework.dao.nposp;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.type.JdbcType;

import cn.eeepay.framework.dao.bill.SuperPushShareDaySettleMapper.SqlProvider;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.SuperPushShareDaySettle;
import cn.eeepay.framework.model.bill.TransShortInfo;
import cn.eeepay.framework.model.nposp.SuperPushShare;

/**
 * 超级推商户分润记录
 * @author zouruijin
 * @date 2016年8月16日17:19:42
 *
 */
public interface SuperPushShareMapper {
	
	
	@SelectProvider( type=SqlProvider.class,method="findSuperPushShareList")
	@ResultMap("cn.eeepay.framework.dao.nposp.SuperPushShareMapper.BaseResultMap")
	List<SuperPushShare> findSuperPushShareList(@Param("superPushShare")SuperPushShare superPushShare,@Param("sort")Sort sort,Page<SuperPushShare> page);
	
	
	@SelectProvider( type=SqlProvider.class,method="exportSuperPushShareList")
	@ResultMap("cn.eeepay.framework.dao.nposp.SuperPushShareMapper.BaseResultMap")
	List<SuperPushShare> exportSuperPushShareList(@Param("superPushShare")SuperPushShare superPushShare);
	
	
	@SelectProvider( type=SqlProvider.class,method="findCollectionGropByShareNo")
	@Results(value = {  
            @Result(property = "shareTotalAmount", column = "share_total_amount",javaType=BigDecimal.class,jdbcType=JdbcType.DECIMAL),  
            @Result(property = "shareTotalNum", column = "share_total_num",javaType=Integer.class,jdbcType=JdbcType.INTEGER),
            @Result(property = "createTime", column = "create_time",javaType=Date.class,jdbcType=JdbcType.TIMESTAMP),
            @Result(property = "shareType", column = "share_type",javaType=String.class,jdbcType=JdbcType.VARCHAR),
            @Result(property = "shareNo", column = "share_no",javaType=String.class,jdbcType=JdbcType.VARCHAR),
        })
	List<Map<String, Object>> findCollectionGropByShareNo(@Param("params") Map<String, String> params);

	@SelectProvider(type=SqlProvider.class,method="findSuperPushShareCollection")
	@Results(value = {  
            @Result(property = "allShareTotalAmount", column = "all_share_total_amount",javaType=BigDecimal.class,jdbcType=JdbcType.DECIMAL),  
            @Result(property = "allNoEnterShareTotalAmount", column = "all_no_enter_share_total_amount",javaType=BigDecimal.class,jdbcType=JdbcType.DECIMAL),
            @Result(property = "allAccountedShareTotalAmount", column = "all_accounted_share_total_amount",javaType=BigDecimal.class,jdbcType=JdbcType.DECIMAL),
            @Result(property = "allTransTotalNum", column = "all_super_push_share_trans_num",javaType=Integer.class,jdbcType=JdbcType.INTEGER),
        })
	Map<String, Object> findSuperPushShareCollection(@Param("superPushShare") SuperPushShare superPushShare);
	
	
	@SelectProvider(type=SqlProvider.class,method="findSuperPushShareCollectionTotalAmount")
	@Results(value = {  
            @Result(property = "allTransTotalAmount", column = "all_trans_total_amount",javaType=BigDecimal.class,jdbcType=JdbcType.DECIMAL),
        })
	Map<String, Object> findSuperPushShareCollectionTotalAmount(@Param("superPushShare") SuperPushShare superPushShare);
	
	@Select("SELECT * FROM super_push_share WHERE share_no = #{superPushShareDaySettle.shareNo} "
			+ " AND DATE_FORMAT(create_time, '%Y-%m-%d') = DATE_FORMAT(#{superPushShareDaySettle.createTime}, '%Y-%m-%d') "
			//+ " AND share_type = #{superPushShareDaySettle.shareType} "
			+ " AND collection_status = 'COLLECTIONED' "
			+ " AND collection_batch_no = #{superPushShareDaySettle.collectionBatchNo} "
			+ " AND share_status = '0' ")
	@ResultMap("cn.eeepay.framework.dao.nposp.SuperPushShareMapper.BaseResultMap")
	List<SuperPushShare> findSuperPushShareListEnterByModel(@Param("superPushShareDaySettle")SuperPushShareDaySettle superPushShareDaySettle);
	
	
	@Select("SELECT * FROM super_push_share WHERE share_no = #{superPushShareDaySettle.shareNo} "
			+ " AND DATE_FORMAT(create_time, '%Y-%m-%d') = DATE_FORMAT(#{superPushShareDaySettle.createTime}, '%Y-%m-%d') "
			//+ " AND share_type = #{superPushShareDaySettle.shareType} "
			+ " AND collection_status = 'NOCOLLECTION' "
			+ " AND share_status = '0' ")
	@ResultMap("cn.eeepay.framework.dao.nposp.SuperPushShareMapper.BaseResultMap")
	List<SuperPushShare> findSuperPushShareListCollectionByModel(@Param("superPushShareDaySettle")SuperPushShareDaySettle superPushShareDaySettle);
	
	
	@Update("<script>"
			+ " <foreach collection=\"list\" item=\"item\" index=\"index\" open=\"\" "
			+ "         close=\"\" separator=\";\"> "
			+"  update super_push_share set order_no=#{item.orderNo},"
			+ " trans_amount=#{item.transAmount},"
			+ " trans_time=#{item.transTime},"
			+ " merchant_no=#{item.merchantNo},"
			+ " mobile=#{item.mobile},"
			+ " agent_node=#{item.agentNode},"
			+ " share_type=#{item.shareType},"
			+ " share_no=#{item.shareNo},"
			+ " share_amount=#{item.shareAmount},"
			+ " share_rate=#{item.shareRate},"
			+ " share_status=#{item.shareStatus},"
			+ " share_time=#{item.shareTime},"
			+ " create_time=#{item.createTime},"
			+ " collection_status=#{item.collectionStatus},"
			+ " collection_batch_no=#{item.collectionBatchNo}"
			+"   where id=#{item.id}"
			+ "     </foreach> "
            + " </script>")
	int updateSuperPushShareBatch(@Param("list")List<SuperPushShare> list);
	
	
	public class SqlProvider{

		
		public String findSuperPushShareCollectionTotalAmount(final Map<String, Object> parameter) {
			final SuperPushShare superPushShare = (SuperPushShare) parameter.get("superPushShare");
			return new SQL(){{
				SELECT("  sum(trans_amount) as all_trans_total_amount ");
				
				FROM(" (select order_no,trans_amount from super_push_share ");
				WHERE(" 1=1 ");
				//分润创建时间
				if(StringUtils.isNotBlank(superPushShare.getCreateTime1()) ){
					WHERE(" create_time >= #{superPushShare.createTime1} ");
				}
				if(StringUtils.isNotBlank(superPushShare.getCreateTime2()) ){
					WHERE(" create_time <= #{superPushShare.createTime2} ");
				}
				//分润入账时间
				if(StringUtils.isNotBlank(superPushShare.getShareTime1()) ){
					WHERE(" share_time >= #{superPushShare.shareTime1} ");
				}
				if(StringUtils.isNotBlank(superPushShare.getShareTime2()) ){
					WHERE(" share_time <= #{superPushShare.shareTime2} ");
				}
				//商户名称、编号
				if (!StringUtils.isBlank(superPushShare.getShareNo()))
					WHERE(" share_no like  \"%\"#{superPushShare.shareNo}\"%\"  ");
				//汇总状态
				if(StringUtils.isNotBlank(superPushShare.getCollectionStatus()) && !"ALL".equals(superPushShare.getCollectionStatus()))
					WHERE(" collection_status = #{superPushShare.collectionStatus} ");
				//交易商户编号
				if (!StringUtils.isBlank(superPushShare.getMerchantNo()))
					WHERE(" merchant_no like  \"%\"#{superPushShare.merchantNo}\"%\"  ");
				//交易订单号
				if (!StringUtils.isBlank(superPushShare.getOrderNo()))
					WHERE(" order_no like  \"%\"#{superPushShare.orderNo}\"%\"  ");
				//入账状态
				if(StringUtils.isNotBlank(superPushShare.getShareStatus()) && !"ALL".equals(superPushShare.getShareStatus()))
					WHERE(" share_status like  \"%\"#{superPushShare.shareStatus}\"%\"  ");
				//分润级别
				if(StringUtils.isNotBlank(superPushShare.getShareType()) && !"ALL".equals(superPushShare.getShareType()))
					WHERE(" share_type like  \"%\"#{superPushShare.shareType}\"%\"  ");
				GROUP_BY("  order_no) temp");
			}}.toString();
		}
		
		public String findSuperPushShareCollection(final Map<String, Object> parameter) {
			final SuperPushShare superPushShare = (SuperPushShare) parameter.get("superPushShare");
			return new SQL(){{
				SELECT(" sum(share_amount) all_share_total_amount,"
						+ " sum(case when share_status = '0' then share_amount else 0 end) as all_no_enter_share_total_amount,"
						+ " sum(case when share_status = '1' then share_amount else 0 end) as all_accounted_share_total_amount, "
						+ " count(DISTINCT(order_no)) as all_super_push_share_trans_num ");
				
				FROM(" super_push_share");
				//分润创建时间
				if(StringUtils.isNotBlank(superPushShare.getCreateTime1()) ){
					WHERE(" create_time >= #{superPushShare.createTime1} ");
				}
				if(StringUtils.isNotBlank(superPushShare.getCreateTime2()) ){
					WHERE(" create_time <= #{superPushShare.createTime2} ");
				}
				//分润入账时间
				if(StringUtils.isNotBlank(superPushShare.getShareTime1()) ){
					WHERE(" share_time >= #{superPushShare.shareTime1} ");
				}
				if(StringUtils.isNotBlank(superPushShare.getShareTime2()) ){
					WHERE(" share_time <= #{superPushShare.shareTime2} ");
				}
				//商户名称、编号
				if (!StringUtils.isBlank(superPushShare.getShareNo()))
					WHERE(" share_no like  \"%\"#{superPushShare.shareNo}\"%\"  ");
				//汇总状态
				if(StringUtils.isNotBlank(superPushShare.getCollectionStatus()) && !"ALL".equals(superPushShare.getCollectionStatus()))
					WHERE(" collection_status = #{superPushShare.collectionStatus} ");
				//交易商户编号
				if (!StringUtils.isBlank(superPushShare.getMerchantNo()))
					WHERE(" merchant_no like  \"%\"#{superPushShare.merchantNo}\"%\"  ");
				//交易订单号
				if (!StringUtils.isBlank(superPushShare.getOrderNo()))
					WHERE(" order_no like  \"%\"#{superPushShare.orderNo}\"%\"  ");
				//入账状态
				if(StringUtils.isNotBlank(superPushShare.getShareStatus()) && !"ALL".equals(superPushShare.getShareStatus()))
					WHERE(" share_status like  \"%\"#{superPushShare.shareStatus}\"%\"  ");
				//分润级别
				if(StringUtils.isNotBlank(superPushShare.getShareType()) && !"ALL".equals(superPushShare.getShareType()))
					WHERE(" share_type like  \"%\"#{superPushShare.shareType}\"%\"  ");
			}}.toString();
		}
		
		public String findSuperPushShareList(final Map<String, Object> parameter) {
			final SuperPushShare superPushShare = (SuperPushShare) parameter.get("superPushShare");
			//final Sort sord=(Sort)parameter.get("sort");
			return new SQL(){{
				SELECT(" *  ");
				FROM(" super_push_share");
				//分润创建时间
				if(StringUtils.isNotBlank(superPushShare.getCreateTime1()) ){
					WHERE(" create_time >= #{superPushShare.createTime1} ");
				}
				if(StringUtils.isNotBlank(superPushShare.getCreateTime2()) ){
					WHERE(" create_time <= #{superPushShare.createTime2} ");
				}
				//分润入账时间
				if(StringUtils.isNotBlank(superPushShare.getShareTime1()) ){
					WHERE(" share_time >= #{superPushShare.shareTime1} ");
				}
				if(StringUtils.isNotBlank(superPushShare.getShareTime2()) ){
					WHERE(" share_time <= #{superPushShare.shareTime2} ");
				}
				//商户名称、编号
				if (!StringUtils.isBlank(superPushShare.getShareNo()))
					WHERE(" share_no like  \"%\"#{superPushShare.shareNo}\"%\"  ");
				//汇总状态
				if(StringUtils.isNotBlank(superPushShare.getCollectionStatus()) && !"ALL".equals(superPushShare.getCollectionStatus()))
					WHERE(" collection_status = #{superPushShare.collectionStatus} ");
				//交易商户编号
				if (!StringUtils.isBlank(superPushShare.getMerchantNo()))
					WHERE(" merchant_no like  \"%\"#{superPushShare.merchantNo}\"%\"  ");
				//交易订单号
				if (!StringUtils.isBlank(superPushShare.getOrderNo()))
					WHERE(" order_no like  \"%\"#{superPushShare.orderNo}\"%\"  ");
				//入账状态
				if(StringUtils.isNotBlank(superPushShare.getShareStatus()) && !"ALL".equals(superPushShare.getShareStatus()))
					WHERE(" share_status like  \"%\"#{superPushShare.shareStatus}\"%\"  ");
				//分润级别
				if(StringUtils.isNotBlank(superPushShare.getShareType()) && !"ALL".equals(superPushShare.getShareType()))
					WHERE(" share_type like  \"%\"#{superPushShare.shareType}\"%\"  ");
			}}.toString();
		}
		
		public String exportSuperPushShareList(final Map<String, Object> parameter) {
			final SuperPushShare superPushShare = (SuperPushShare) parameter.get("superPushShare");
			return new SQL(){{
				SELECT(" *  ");
				FROM(" super_push_share");
				//分润创建时间
				if(StringUtils.isNotBlank(superPushShare.getCreateTime1()) ){
					WHERE(" create_time >= #{superPushShare.createTime1} ");
				}
				if(StringUtils.isNotBlank(superPushShare.getCreateTime2()) ){
					WHERE(" create_time <= #{superPushShare.createTime2} ");
				}
				//分润入账时间
				if(StringUtils.isNotBlank(superPushShare.getShareTime1()) ){
					WHERE(" share_time >= #{superPushShare.shareTime1} ");
				}
				if(StringUtils.isNotBlank(superPushShare.getShareTime2()) ){
					WHERE(" share_time <= #{superPushShare.shareTime2} ");
				}
				//商户名称、编号
				if (!StringUtils.isBlank(superPushShare.getShareNo()))
					WHERE(" share_no like  \"%\"#{superPushShare.shareNo}\"%\"  ");
				//汇总状态
				if(StringUtils.isNotBlank(superPushShare.getCollectionStatus()) && !"ALL".equals(superPushShare.getCollectionStatus()))
					WHERE(" collection_status = #{superPushShare.collectionStatus} ");
				//交易商户编号
				if (!StringUtils.isBlank(superPushShare.getMerchantNo()))
					WHERE(" merchant_no like  \"%\"#{superPushShare.merchantNo}\"%\"  ");
				//交易订单号
				if (!StringUtils.isBlank(superPushShare.getOrderNo()))
					WHERE(" order_no like  \"%\"#{superPushShare.orderNo}\"%\"  ");
				//入账状态
				if(StringUtils.isNotBlank(superPushShare.getShareStatus()) && !"ALL".equals(superPushShare.getShareStatus()))
					WHERE(" share_status like  \"%\"#{superPushShare.shareStatus}\"%\"  ");
				//分润级别
				if(StringUtils.isNotBlank(superPushShare.getShareType()) && !"ALL".equals(superPushShare.getShareType()))
					WHERE(" share_type like  \"%\"#{superPushShare.shareType}\"%\"  ");
			}}.toString();
		}
		
		
		public String findCollectionGropByShareNo(final Map<String, Object> parameter){
			final Map<String, String> params = (Map<String, String>) parameter.get("params");
			final String createTime1 = params.get("createTime1");
			return new SQL(){{
				SELECT(" sum(share_amount) as share_total_amount, "
						+ " count(DISTINCT(order_no)) as share_total_num, "
						+ " s.create_time, "
						+ " CASE s.share_type "
						+ " WHEN '0' THEN '0' "
                        + " WHEN '1' THEN '0' "
						+ " WHEN '2' THEN '1' "
						+ " WHEN '3' THEN '1' "
						+ " WHEN '4' THEN '1' "
                        + " END  as share_type, "
						+ " s.share_no ");
				FROM(" super_push_share as s");
				if(StringUtils.isNotBlank(createTime1) ){
					WHERE(" DATE_FORMAT(s.create_time, '%Y-%m-%d') = DATE_FORMAT(#{params.createTime1}, '%Y-%m-%d') ");
				}
				WHERE(" s.collection_status='NOCOLLECTION' and s.collection_batch_no is null ");
				GROUP_BY(" DATE_FORMAT(s.create_time, '%Y-%m-%d'), "
						//+ " s.share_type, "
						+ " s.share_no");
				ORDER_BY(" s.create_time ");
			}}.toString();
		}
		
		public String propertyMapping(String name,int type){
			final String[] propertys={"id","orderNo","transAmount","transTime","merchantNo","agentNode","shareType","shareNo",
		    		"shareAmount","shareRate","shareStatus"};
		    final String[] columns={"id","order_no","trans_amount","trans_time","merchant_no","agent_node","share_type","share_no",
		    		"share_amount","share_rate","share_status"};
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
