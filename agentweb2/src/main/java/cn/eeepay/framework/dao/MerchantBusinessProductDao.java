package cn.eeepay.framework.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.boss.action.MerchantBusinessProductAction.SelectParams;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.BusinessProductDefine;
import cn.eeepay.framework.model.MerchantBusinessProduct;
import cn.eeepay.framework.model.MerchantBusinessProductHistory;
import cn.eeepay.framework.model.ServiceInfoBean;
import cn.eeepay.framework.model.ServiceQuota;
import cn.eeepay.framework.model.ServiceRate;
import cn.eeepay.framework.util.WriteReadDataSource;

@WriteReadDataSource
public interface MerchantBusinessProductDao {
	
	@Select("select * from merchant_business_product where merchant_no =#{merchantNo} and bp_id =#{bpId}")
	@ResultType(MerchantBusinessProduct.class)
	MerchantBusinessProduct selectMerBusPro(@Param("merchantNo") String merchantNo, @Param("bpId") String bpId);

	@Insert("insert merchant_business_product(merchant_no,bp_id,status,item_source,auditor_id)"
			+"values(#{merProduct.merchantNo},#{merProduct.bpId},#{merProduct.status},#{merProduct.itemSource},#{merProduct.auditorId})")
	int insertMerProduct(@Param("merProduct") MerchantBusinessProduct merProduct);
    @Select("SELECT mbp.*,bpb.bp_name"
    		+ " from merchant_business_product mbp "
    		+ "LEFT JOIN business_product_define bpb on bpb.bp_id=mbp.bp_id where mbp.id=#{id}")
    @ResultType(MerchantBusinessProduct.class)
    MerchantBusinessProduct selectByPrimaryKey(@Param("id") Long id);
    
    @Update("update merchant_business_product set `status`=#{status},auditor_id=#{auditorId} where id=#{mbpId}")
    int updateBymbpId(@Param("mbpId") Long mbpId, @Param("status") String status, @Param("auditorId") String auditorId);
    
    @Update("update merchant_business_product set bp_id=#{record.bpId},`status`=#{record.status} where id=#{record.id}")
    int updateByItemAndMbpId(@Param("record") MerchantBusinessProduct record);
   
    @Delete("delete from  merchant_business_product where   merchant_no=#{merchantNo} and bp_id=#{bpId}")
	int deleteMerBusPro(@Param("merchantNo") String merchantNo, @Param("bpId") String bpId);
    @Delete("delete from merchant_require_item where merchant_no=#{merchantNo}  and status='2'")
    int delectMerBusItem(@Param("merchantNo") String merchantNo);
    @Delete("delete msq FROM merchant_service_quota msq,merchant_service ms  WHERE msq.service_id=ms.service_id AND msq.merchant_no=ms.merchant_no  AND ms.bp_id=#{bpId} AND ms.merchant_no=#{merchantNo}")
    int delectMerRate(@Param("bpId") String bpId, @Param("merchantNo") String merchantNo);
    @Delete("delete msr FROM merchant_service_rate msr,merchant_service ms  WHERE msr.service_id=ms.service_id AND msr.merchant_no=ms.merchant_no  AND ms.bp_id=#{bpId} AND ms.merchant_no=#{merchantNo} ")
    int delectMerQuota(@Param("bpId") String bpId, @Param("merchantNo") String merchantNo);
    @Delete("delete from merchant_service  where merchant_no=#{merchantNo} and bp_id=#{bpId}")
    int delectMerService(@Param("merchantNo") String merchantNo, @Param("bpId") String bpId);
    @Insert("insert merchant_business_product_history"
    		+ "(source_bp_id,new_bp_id,operation_type,operation_person_type,create_time,"
    		+ "operation_person_no,merchant_no) "
    		+ "values(#{mbpHis.sourceBpId},#{mbpHis.newBpId},#{mbpHis.operationType},#{mbpHis.operationPersonType},#{mbpHis.createTime}"
    		+ ",#{mbpHis.operationPersonNo},#{mbpHis.merchantNo})")
    int insertMerBusProHis(@Param("mbpHis") MerchantBusinessProductHistory mbpHis);
    
    @Update("update terminal_info set bp_id=#{newBpId}  where merchant_no=#{merchantNo} and bp_id =#{oldBpId} and open_status ='2' ")
    int updateMerBusTer(@Param("newBpId") String newBpId, @Param("merchantNo") String merchantNo, @Param("oldBpId") String oldBpId);

    @Select("SELECT d.*,g1.group_no FROM business_product_group g1,business_product_define d,agent_business_product ab "
	+" WHERE g1.bp_id = d.bp_id AND g1.bp_id = ab.bp_id AND ab.agent_no = #{agentNo} and ab.status = '1' AND g1.group_no IN ( "
	+" SELECT g.group_no FROM business_product_group g WHERE 	g.bp_id = #{bpId} ) AND g1.bp_id <> #{bpId} " +
			"AND d.effective_status = 1 ")
    List<BusinessProductDefine> selectGroupBpInfo(@Param("agentNo") String agentNo, @Param("bpId") String bpId, @Param("bpId1") String bpId1);
    
    @Select("select * from merchant_business_product where merchant_no =#{merchantNo} and bp_id =#{bpId}")
    public Map<String,Object> selectMerBpInfo(@Param("merchantNo") String merchantNo, @Param("bpId") String bpId);
    @Select("select * from terminal_info  where merchant_no =#{merchantNo} and bp_id =#{bpId} and open_status ='2' ")
    public List<Map<String,Object>> selectTerBpInfo(@Param("merchantNo") String merchantNo, @Param("bpId") String bpId);

    /**
     * 更新商户业务产品
     * @param merchantNo 商户编号
     * @param oldBpId    旧的业务产品
     * @param newBpId    新的业务产品
     * @return 影响条数
     */
    int updateMerchantBusinessProduct(@Param("merchantNo") String merchantNo,
                                      @Param("oldBpId") String oldBpId,
                                      @Param("newBpId") String newBpId);

    /**
     * 根据业务产品id获取服务信息
     * @param bpId 业务产品信息
     * @return
     */
    List<ServiceInfoBean> listServiceInfoByBpId(@Param("bpId") String bpId);

    /**
     * 更新商户服务
     * @param merchantNo	商户编号
     * @param oldBpId		旧业务产品
     * @param newBpId		新业务产品
     * @param oldServiceId	旧服务id
     * @param newServiceId	新服务id
     */
    int updateMerchantService(@Param("merchantNo") String merchantNo,
                              @Param("oldBpId") String oldBpId,
                              @Param("newBpId") String newBpId,
                              @Param("oldServiceId") String oldServiceId,
                              @Param("newServiceId") String newServiceId);

    /**
     * 批量插入商户服务限额
     * @param merchantNo            商户编号
     * @param newServiceQuotaList   新服务限额列表
     */
    void bacthInsertServiceQuota(@Param("quotaList") List<ServiceQuota> newServiceQuotaList, @Param("merchantNo") String merchantNo);

    /**
     * 批量插入商户服务费率
     * @param merchantNo            商户编号
     * @param newServiceRateList    新服务费率列表
     */
    void bacthInsertServiceRate(@Param("rateList") List<ServiceRate> newServiceRateList, @Param("merchantNo") String merchantNo);

    @Select("SELECT COUNT(1) FROM zq_merchant_info zmi\n" +
			"JOIN merchant_business_product mbp ON mbp.id = zmi.mbp_id\n" +
			"WHERE zmi.sync_status = 1 \n" +
			"AND zmi.channel_code = 'ZF_ZQ'\n" +
			"AND mbp.merchant_no = #{merchantNo}\n" +
			"AND mbp.bp_id = #{bpId}")
    int countZF_ZQAndSyncSuccess(@Param("merchantNo") String merchantNo, @Param("bpId") String bpId);

    @Select("select id from merchant_business_product where bp_id= #{bpId} limit 1")
	@ResultType(Integer.class)
    Integer findIdByBp(String bpId);
    
    /**
     * 查询商户已成功的进件
    * @author: gaowei
    * @date: 2017年4月19日 下午2:21:44  
    * @param merchantNo
    * @return
     */
    @Select("SELECT  *   FROM merchant_business_product  WHERE merchant_no= #{merchantNo} and status='4' ")
    @ResultType(MerchantBusinessProduct.class)
    List<MerchantBusinessProduct> selectMerPro(@Param("merchantNo") String merchantNo);
    
    @Select(" SELECT mbp.bp_id,bpb.bp_name FROM merchant_business_product mbp "
    		+ "LEFT JOIN business_product_define bpb on bpb.bp_id = mbp.bp_id  where mbp.merchant_no = #{merId}")
	@ResultType(List.class)
	List<MerchantBusinessProduct> selectByParamByMerId(@Param("merId") String merId);

	public class SqlProvider{

	}

}