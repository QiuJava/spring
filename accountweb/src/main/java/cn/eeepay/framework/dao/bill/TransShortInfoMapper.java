package cn.eeepay.framework.dao.bill;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.TransShortInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;
/**
 * 交易部分信息表
 * 
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2017年4月15日13:40:44
 *
 */
public interface TransShortInfoMapper {
	@Insert("insert into trans_short_info(plate_order_no,trans_time,hardware_product,business_product_id,service_id,card_type,merchant_no,merchant_name,one_agent_no,one_agent_name,"
			+ "agent_no,agent_name,agent_node,agent_level,parent_agent_no,trans_amount,merchant_rate,merchant_fee,acq_org_id,acq_ename,acq_out_cost,agent_share_amount,mer_cash_fee,cash_agent_share_amount,dai_cost,"
			+ "dian_cost,sale_name,acq_out_profit,hardware_product_name,business_product_name,service_name,agent_profit_collection_status,"
			+ "collection_batch_no,agent_profit_group_time,trans_deduction_fee,actual_fee,merchant_price,deduction_mer_fee,actual_optional_fee)"
			+ " values(#{transShortInfo.plateOrderNo},#{transShortInfo.transTime},#{transShortInfo.hardwareProduct},#{transShortInfo.businessProductId},#{transShortInfo.serviceId},#{transShortInfo.cardType},#{transShortInfo.merchantNo},#{transShortInfo.merchantName},#{transShortInfo.agentNo},#{transShortInfo.agentName},"
			+ "#{transShortInfo.agentNo},#{transShortInfo.agentName},#{transShortInfo.agentNode},#{transShortInfo.agentLevel},#{transShortInfo.parentAgentNo},#{transShortInfo.transAmount},#{transShortInfo.merchantRate},#{transShortInfo.merchantFee},#{transShortInfo.acqOrgId},#{transShortInfo.acqEname},#{transShortInfo.acqOutCost},#{transShortInfo.agentShareAmount},#{transShortInfo.merCashFee},#{transShortInfo.cashAgentShareAmount},#{transShortInfo.daiCost},"
			+ "#{transShortInfo.dianCost},#{transShortInfo.saleName},#{transShortInfo.acqOutProfit},#{transShortInfo.hardwareProductName},#{transShortInfo.businessProductName},#{transShortInfo.serviceName},#{transShortInfo.agentProfitCollectionStatus},"
			+ "#{transShortInfo.collectionBatchNo},#{transShortInfo.agentProfitGroupTime},"
			+ "#{transShortInfo.transDeductionFee},#{transShortInfo.actualFee},#{transShortInfo.merchantPrice},#{transShortInfo.deductionMerFee},#{transShortInfo.actualOptionalFee})")
	@ResultMap("cn.eeepay.framework.dao.bill.TransShortInfoMapper.BaseResultMap")
	int insertTransShortInfo(@Param("transShortInfo")TransShortInfo transShortInfo);
	
	@Update("update trans_short_info set trans_time=#{transShortInfo.transTime},"
			+ " hardware_product=#{transShortInfo.hardwareProduct},"
			+ " business_product_id=#{transShortInfo.businessProductId},"
			+ " service_id=#{transShortInfo.serviceId},"
			+ " card_type=#{transShortInfo.cardType},"
			+ " merchant_no=#{transShortInfo.merchantNo},"
			+ " merchant_name=#{transShortInfo.merchantName},"
			+ " agent_no=#{transShortInfo.agentNo},"
			+ " agent_name=#{transShortInfo.agentName},"
			+ " agent_node=#{transShortInfo.agentNode},"
			+ " agent_level=#{transShortInfo.agentLevel},"
			+ " parent_agent_no=#{transShortInfo.parentAgentNo},"
			+ " trans_amount=#{transShortInfo.transAmount},"
			+ " merchant_rate=#{transShortInfo.merchantRate},"
			+ " merchant_fee=#{transShortInfo.merchantFee},"
			+ " acq_org_id=#{transShortInfo.acqOrgId},"
			+ " acq_enname=#{transShortInfo.acqEnname},"
			+ " acq_out_cost=#{transShortInfo.acqOutCost},"
			+ " agent_share_amount=#{transShortInfo.agentShareAmount},"
			+ " mer_cash_fee=#{transShortInfo.merCashFee},"
			+ " cash_agent_share_amount=#{transShortInfo.cashAgentShareAmount},"
			+ " dai_cost=#{transShortInfo.daiCost},"
			+ " dian_cost=#{transShortInfo.dianCost},"
			+ " agent_share_collect=#{transShortInfo.agentShareCollect},"
			+ " sale_name=#{transShortInfo.saleName},"
			+ " acq_out_profit=#{transShortInfo.acqOutProfit},"
			+ " hardware_product_name=#{transShortInfo.hardwareProductName},"
			+ " business_product_name=#{transShortInfo.businessProductName},"
			+ " service_name=#{transShortInfo.serviceName},"
			+ " agent_profit_collection_status=#{transShortInfo.agentProfitCollectionStatus},"
			+ " collection_batch_no=#{transShortInfo.collectionBatchNo},"
			+ " agent_profit_group_time=#{transShortInfo.agentProfitGroupTime},"
			+ " trans_deduction_fee=#{transShortInfo.transDeductionFee},"
			+ " actual_fee=#{transShortInfo.actualFee},"
			+ " merchant_price=#{transShortInfo.merchantPrice},"
			+ " deduction_mer_fee=#{transShortInfo.deductionMerFee},"
			+ " actual_optional_fee=#{transShortInfo.actualOptionalFee}"
			+"   where plate_order_no=#{transShortInfo.plateOrderNo}")
	int updateTransShortInfo(@Param("transShortInfo")TransShortInfo transShortInfo);
	
	
	@Update("<script>"
			+ " <foreach collection=\"list\" item=\"item\" index=\"index\" open=\"\" "
			+ "         close=\"\" separator=\";\"> "
			+"  update trans_short_info set trans_time=#{item.transTime},"
			+ " hardware_product=#{item.hardwareProduct},"
			+ " business_product_id=#{item.businessProductId},"
			+ " service_id=#{item.serviceId},"
			+ " card_type=#{item.cardType},"
			+ " merchant_no=#{item.merchantNo},"
			+ " merchant_name=#{item.merchantName},"
			+ " one_agent_no=#{item.oneAgentNo},"
			+ " one_agent_name=#{item.oneAgentName},"
			+ " agent_no=#{item.agentNo},"
			+ " agent_name=#{item.agentName},"
			+ " agent_node=#{item.agentNode},"
			+ " agent_level=#{item.agentLevel},"
			+ " parent_agent_no=#{item.parentAgentNo},"
			+ " trans_amount=#{item.transAmount},"
			+ " merchant_rate=#{item.merchantRate},"
			+ " merchant_fee=#{item.merchantFee},"
			+ " acq_org_id=#{item.acqOrgId},"
			+ " acq_enname=#{item.acqEnname},"
			+ " acq_out_cost=#{item.acqOutCost},"
			+ " agent_share_amount=#{item.agentShareAmount},"
			+ " mer_cash_fee=#{item.merCashFee},"
			+ " cash_agent_share_amount=#{item.cashAgentShareAmount},"
			+ " dai_cost=#{item.daiCost},"
			+ " dian_cost=#{item.dianCost},"
			+ " sale_name=#{item.saleName},"
			+ " acq_out_profit=#{item.acqOutProfit},"
			+ " hardware_product_name=#{item.hardwareProductName},"
			+ " business_product_name=#{item.businessProductName},"
			+ " service_name=#{item.serviceName},"
			+ " agent_profit_collection_status=#{item.agentProfitCollectionStatus},"
			+ " collection_batch_no=#{item.collectionBatchNo},"
			+ " agent_profit_group_time=#{item.agentProfitGroupTime},"
			+ " trans_deduction_fee=#{item.transDeductionFee},"
			+ " actual_fee=#{item.actualFee},"
			+ " merchant_price=#{item.merchantPrice},"
			+ " deduction_mer_fee=#{item.deductionMerFee},"
			+ " actual_optional_fee=#{item.actualOptionalFee}"
			+"   where plate_order_no=#{item.plateOrderNo}"
			+ "     </foreach> "
            + " </script>")
	int updateTransShortInfoBatchByPlateOrderNo(@Param("list")List<TransShortInfo> list);

	@Update("update trans_short_info set collection_batch_no = #{param.collectionBatchNo},agent_profit_collection_status = 'COLLECTED',agent_profit_group_time = #{param.groupTime}  where trans_time between #{param.date1} and #{param.date2}")
	int updateTransShortInfoByDate(@Param("param") Map<String,String> param);


	@Delete("delete from trans_short_info where plate_order_no = #{plateOrderNo}")
	int deleteTransShortInfo(@Param("plateOrderNo")String plateOrderNo);
	
	@Select("select * from trans_short_info ")
	@ResultMap("cn.eeepay.framework.dao.bill.TransShortInfoMapper.BaseResultMap")
	List<TransShortInfo> findAllTransShortInfo();
	
//	@Select("select * from trans_short_info as b where DATE_FORMAT(b.trans_time, '%Y-%m-%e') = DATE_FORMAT(#{transDate1}, '%Y-%m-%e')")
//	@ResultMap("cn.eeepay.framework.dao.bill.TransShortInfoMapper.BaseResultMap")
//	List<TransShortInfo> findAllTransShortInfoByTransTime(@Param("transDate1")String transDate1);

	//上面写的%e，并没区别
	@Select("select * from trans_short_info as b where b.trans_time between #{date1} and #{date2}")
	@ResultMap("cn.eeepay.framework.dao.bill.TransShortInfoMapper.BaseResultMap")
	List<TransShortInfo> findAllTransShortInfoByTransTime(@Param("date1")String date1,@Param("date2")String date2);
	
	@SelectProvider(type=SqlProvider.class,method="findTransShortInfoList")
	@ResultMap("cn.eeepay.framework.dao.bill.TransShortInfoMapper.BaseResultMap")
	List<TransShortInfo> findTransShortInfoList(@Param("transShortInfo")TransShortInfo transShortInfo,@Param("sort")Sort sort,Page<TransShortInfo> page);
	
	@SelectProvider(type=SqlProvider.class,method="findOneAgentTransShortInfoList")
	@ResultMap("cn.eeepay.framework.dao.bill.TransShortInfoMapper.BaseResultMap")
	List<TransShortInfo> findOneAgentTransShortInfoList(@Param("transShortInfo")TransShortInfo transShortInfo,@Param("sort")Sort sort,Page<TransShortInfo> page);
	
	@Select("select * from trans_short_info where plate_order_no = #{plateOrderNo} ")
	@ResultMap("cn.eeepay.framework.dao.bill.TransShortInfoMapper.BaseResultMap")
	TransShortInfo findTransShortInfoByPlateOrderNo(@Param("plateOrderNo")String plateOrderNo);
	
	@SelectProvider(type=SqlProvider.class,method="findNoCollectTransShortInfo")
	@ResultMap("cn.eeepay.framework.dao.bill.TransShortInfoMapper.BaseResultMap")
	List<TransShortInfo> findNoCollectTransShortInfo(@Param("params") Map<String, String> params);
	
	@SelectProvider(type=SqlProvider.class,method="exportAgentsProfitTransShortInfoList")
	@ResultMap("cn.eeepay.framework.dao.bill.TransShortInfoMapper.BaseResultMap")
	List<TransShortInfo> exportAgentsProfitTransShortInfoList(@Param("transShortInfo")TransShortInfo transShortInfo);
	
	
	@SelectProvider(type=SqlProvider.class,method="exportOneAgentsProfitTransShortInfoList")
	@ResultMap("cn.eeepay.framework.dao.bill.TransShortInfoMapper.BaseResultMap")
	List<TransShortInfo> exportOneAgentsProfitTransShortInfoList(@Param("transShortInfo")TransShortInfo transShortInfo);
	
	public class SqlProvider{
		public String findTransShortInfoList(final Map<String, Object> parameter){
			final TransShortInfo transShortInfo=(TransShortInfo)parameter.get("transShortInfo");
			final Sort sord=(Sort)parameter.get("sort");
			String sql = "";
			 sql = new SQL(){{
				SELECT("tsi.*,cad.check_account_status , cad.plate_merchant_rate , cad.plate_acq_merchant_rate");
				FROM(" trans_short_info tsi left join check_account_detail cad on tsi.plate_order_no = cad.plate_order_no ");
				//是否查询节点信息
				if(StringUtils.isNotBlank(transShortInfo.getAgentNode())){
					WHERE(" tsi.agent_node like #{transShortInfo.agentNode}\"%\" ");
				}
				//这里不需要指定代理商了，此功能已经被查询节点信息代替
//				if(StringUtils.isNotBlank(transShortInfo.getAgentNo()) && !"ALL".equals(transShortInfo.getAgentNo())){
//					WHERE(" tsi.agent_no=#{transShortInfo.agentNo} ");
//				}
				if(StringUtils.isNotBlank(transShortInfo.getMerchantName()) ){
					WHERE(" tsi.merchant_name like  \"%\"#{transShortInfo.merchantName}\"%\" or tsi.merchant_no like  \"%\"#{transShortInfo.merchantName}\"%\" ");
				}
				if(StringUtils.isNotBlank(transShortInfo.getTransTime1()) ){
					WHERE(" tsi.trans_time >= #{transShortInfo.transTime1} ");
				}
				if(StringUtils.isNotBlank(transShortInfo.getTransTime2()) ){
					WHERE(" tsi.trans_time <= #{transShortInfo.transTime2} ");
				}
				if(StringUtils.isNotBlank(transShortInfo.getCollectionTime1()) ){
					WHERE(" tsi.agent_profit_group_time >= #{transShortInfo.collectionTime1} ");
				}
				if(StringUtils.isNotBlank(transShortInfo.getCollectionTime2()) ){
					WHERE(" tsi.agent_profit_group_time <= #{transShortInfo.collectionTime2} ");
				}
				if(StringUtils.isNotBlank(transShortInfo.getCollectionBatchNo()) ){
					WHERE(" tsi.collection_batch_no like  \"%\"#{transShortInfo.collectionBatchNo}\"%\" ");
				}
				if(sord != null && StringUtils.isNotBlank(sord.getSidx())){
					ORDER_BY(propertyMapping(sord.getSidx(), 0)+" "+sord.getSord());
				} else {
					ORDER_BY(" tsi.trans_time desc ");
				}
			}}.toString();
			return sql;
		}
		
		public String findOneAgentTransShortInfoList(final Map<String, Object> parameter){
			final TransShortInfo transShortInfo=(TransShortInfo)parameter.get("transShortInfo");
			final Sort sord=(Sort)parameter.get("sort");
			String sql = "";
			 sql = new SQL(){{
				SELECT("tsi.*,cad.check_account_status , cad.plate_merchant_rate , cad.plate_acq_merchant_rate");
				FROM(" trans_short_info tsi left join check_account_detail cad on tsi.plate_order_no = cad.plate_order_no ");
//				WHERE(" tsi.agent_level='1' ");
				if(StringUtils.isNotBlank(transShortInfo.getAgentNo()) && !"ALL".equals(transShortInfo.getAgentNo())){
					WHERE(" tsi.one_agent_no=#{transShortInfo.agentNo} ");
				}
				if(StringUtils.isNotBlank(transShortInfo.getMerchantName()) ){
					WHERE(" tsi.merchant_name like  \"%\"#{transShortInfo.merchantName}\"%\" or tsi.merchant_no like  \"%\"#{transShortInfo.merchantName}\"%\" ");
				}
				if(StringUtils.isNotBlank(transShortInfo.getTransTime1()) ){
					WHERE(" tsi.trans_time >= #{transShortInfo.transTime1} ");
				}
				if(StringUtils.isNotBlank(transShortInfo.getTransTime2()) ){
					WHERE(" tsi.trans_time <= #{transShortInfo.transTime2} ");
				}
				if(StringUtils.isNotBlank(transShortInfo.getCollectionTime1()) ){
					WHERE(" tsi.agent_profit_group_time >= #{transShortInfo.collectionTime1} ");
				}
				if(StringUtils.isNotBlank(transShortInfo.getCollectionTime2()) ){
					WHERE(" tsi.agent_profit_group_time <= #{transShortInfo.collectionTime2} ");
				}
				if(StringUtils.isNotBlank(transShortInfo.getCollectionBatchNo()) ){
					WHERE(" tsi.collection_batch_no like  \"%\"#{transShortInfo.collectionBatchNo}\"%\" ");
				}
				if(sord != null && StringUtils.isNotBlank(sord.getSidx())){
					ORDER_BY(propertyMapping(sord.getSidx(), 0)+" "+sord.getSord());
				} else {
					ORDER_BY(" tsi.trans_time desc ");
				}
			}}.toString();
			return sql;
		}
		
		
		public String exportAgentsProfitTransShortInfoList(final Map<String, Object> parameter){
			final TransShortInfo transShortInfo=(TransShortInfo)parameter.get("transShortInfo");
			String sql = "";
			 sql = new SQL(){{
				SELECT(" tsi.*,cad.check_account_status , cad.plate_acq_merchant_rate ");
				FROM(" trans_short_info tsi left join check_account_detail cad on tsi.plate_order_no = cad.plate_order_no ");
				 //是否查询节点信息
				 if(StringUtils.isNotBlank(transShortInfo.getAgentNode())){
					 WHERE(" tsi.agent_node like #{transShortInfo.agentNode}\"%\" ");
				 }
				 //这里不需要指定代理商了，此功能已经被查询节点信息代替
//				if(StringUtils.isNotBlank(transShortInfo.getAgentNo()) && !"ALL".equals(transShortInfo.getAgentNo())){
//					WHERE(" tsi.one_agent_no=#{transShortInfo.agentNo} ");
//				}
				if(StringUtils.isNotBlank(transShortInfo.getMerchantName()) ){
					WHERE(" tsi.merchant_name like  \"%\"#{transShortInfo.merchantName}\"%\" or tsi.merchant_no like  \"%\"#{transShortInfo.merchantName}\"%\" ");
				}
				if(StringUtils.isNotBlank(transShortInfo.getTransTime1()) ){
					WHERE(" tsi.trans_time >= #{transShortInfo.transTime1} ");
				}
				if(StringUtils.isNotBlank(transShortInfo.getTransTime2()) ){
					WHERE(" tsi.trans_time <= #{transShortInfo.transTime2} ");
				}
				if(StringUtils.isNotBlank(transShortInfo.getCollectionTime1()) ){
					WHERE(" tsi.agent_profit_group_time >= #{transShortInfo.collectionTime1} ");
				}
				if(StringUtils.isNotBlank(transShortInfo.getCollectionTime2()) ){
					WHERE(" tsi.agent_profit_group_time <= #{transShortInfo.collectionTime2} ");
				}
				if(StringUtils.isNotBlank(transShortInfo.getCollectionBatchNo()) ){
					WHERE(" tsi.collection_batch_no like  \"%\"#{transShortInfo.collectionBatchNo}\"%\" ");
				}
				ORDER_BY(" tsi.trans_time desc ");
			}}.toString();
			return sql;
		}
		
		public String exportOneAgentsProfitTransShortInfoList(final Map<String, Object> parameter){
			final TransShortInfo transShortInfo=(TransShortInfo)parameter.get("transShortInfo");
			String sql = "";
			 sql = new SQL(){{
				SELECT(" tsi.*,cad.check_account_status ");
				FROM(" trans_short_info tsi left join check_account_detail cad on tsi.plate_order_no = cad.plate_order_no ");
				if(StringUtils.isNotBlank(transShortInfo.getAgentNo()) && !"ALL".equals(transShortInfo.getAgentNo())){
					WHERE(" tsi.agent_no=#{transShortInfo.agentNo} ");
				}
				if(StringUtils.isNotBlank(transShortInfo.getMerchantName()) ){
					WHERE(" tsi.merchant_name like  \"%\"#{transShortInfo.merchantName}\"%\" or tsi.merchant_no like  \"%\"#{transShortInfo.merchantName}\"%\" ");
				}
				if(StringUtils.isNotBlank(transShortInfo.getTransTime1()) ){
					WHERE(" tsi.trans_time >= #{transShortInfo.transTime1} ");
				}
				if(StringUtils.isNotBlank(transShortInfo.getTransTime2()) ){
					WHERE(" tsi.trans_time <= #{transShortInfo.transTime2} ");
				}
				if(StringUtils.isNotBlank(transShortInfo.getCollectionTime1()) ){
					WHERE(" tsi.agent_profit_group_time >= #{transShortInfo.collectionTime1} ");
				}
				if(StringUtils.isNotBlank(transShortInfo.getCollectionTime2()) ){
					WHERE(" tsi.agent_profit_group_time <= #{transShortInfo.collectionTime2} ");
				}
				if(StringUtils.isNotBlank(transShortInfo.getCollectionBatchNo()) ){
					WHERE(" tsi.collection_batch_no like  \"%\"#{transShortInfo.collectionBatchNo}\"%\" ");
				}
				ORDER_BY(" tsi.trans_time desc ");
			}}.toString();
			return sql;
		}
		
		public String findNoCollectTransShortInfo(final Map<String, Object> parameter){
			final Map<String, String> params = (Map<String, String>) parameter.get("params");
			final String transDate1 = params.get("transDate1");
			String sql = "";
			 sql = new SQL(){{
				SELECT(" a.* ");
				FROM(" check_account_detail as a ,trans_short_info b ");
				WHERE(" a.plate_order_no = b.plate_order_no  ");
				WHERE(" b.agent_profit_collection_status='NOCOLLECT'  ");
				if(StringUtils.isNotBlank(transDate1) ){
					//WHERE(" DATE_FORMAT(b.trans_time, '%Y-%m-%d') = DATE_FORMAT(#{params.transDate1}, '%Y-%m-%d') ");
					WHERE(" b.trans_time BETWEEN #{params.transDate1} and  #{params.transDate2}");
				}
			}}.toString();
			return sql;
		}
			 
		
		
		public String propertyMapping(String name,int type){
			final String[] propertys={"id","trans_time","agent_no","agent_name"};
		    final String[] columns={"id","transTime","agentNo","agentName"};
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
