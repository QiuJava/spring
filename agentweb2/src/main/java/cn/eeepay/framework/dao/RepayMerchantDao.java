package cn.eeepay.framework.dao;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.model.*;
import cn.eeepay.framework.util.WriteReadDataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;

/**
 * 超级还款用户管理
 * @author mays
 * @date 2017年10月31日
 */
@WriteReadDataSource
public interface RepayMerchantDao {

	@SelectProvider(type = SqlProvider.class, method = "selectRepayMerchantByParam")
	
	/*@Select("SELECT yrmi.*,ywi.nickname,ywi.create_time AS enter_time,ywi.headimgurl,"
			+ "ai.agent_name,yua.sfzzm_url,yua.sfzfm_url,yua.scsfz_url "
			+ "FROM yfb_repay_merchant_info yrmi "
			+ "LEFT JOIN yfb_wechat_info ywi ON ywi.openid = yrmi.openid "
			+ "LEFT JOIN agent_info ai ON ai.agent_no = yrmi.agent_no "
			+ "LEFT JOIN yfb_unified_account yua ON yua.un_account_mer_no = yrmi.merchant_no "
			+ "WHERE yrmi.agent_node LIKE CONCAT(CONCAT('%', #{info.agentNo}), '%') and yrmi.merchant_no=#{info.merchantNo} and yrmi.mobile_no=#{info.mobileNo}")
	*/
	@ResultType(RepayMerchantInfo.class)
	List<RepayMerchantInfo> selectRepayMerchantByParam(@Param("page") Page<RepayMerchantInfo> page,
			@Param("info") RepayMerchantInfo info);
	

	@Select("SELECT yrmi.*,ywi.nickname,ywi.create_time AS enter_time,ywi.headimgurl,"
			+ "ai.agent_name,yua.sfzzm_url,yua.sfzfm_url,yua.scsfz_url "
			+ "FROM yfb_repay_merchant_info yrmi "
			+ "LEFT JOIN yfb_wechat_info ywi ON ywi.openid = yrmi.openid "
			+ "LEFT JOIN agent_info ai ON ai.agent_no = yrmi.agent_no "
			+ "LEFT JOIN yfb_unified_account yua ON yua.un_account_mer_no = yrmi.merchant_no "
			+ "WHERE yrmi.merchant_no=#{merchantNo}")
	@ResultType(RepayMerchantInfo.class)
	RepayMerchantInfo queryRepayMerchantByMerchantNo(@Param("merchantNo")String merchantNo);

	@Select("SELECT * FROM yfb_card_manage WHERE business_code=#{idCardNo} ")
	@ResultType(YfbCardManage.class)
	List<YfbCardManage> queryCardByIdCardNo(@Param("idCardNo")String idCardNo);

	@Update("UPDATE yfb_repay_merchant_info SET mer_account=1 WHERE merchant_no=#{merchantNo}")
	int updateRepayMerchantAccountStatus(String merchantNo);

	@Select("SELECT balance_no,balance,freeze_amount FROM yfb_balance WHERE mer_no = #{merchantNo}")
	@ResultType(YfbBalance.class)
	List<YfbBalance> queryBalanceByMerchantNo(@Param("merchantNo")String merchantNo);

	@Select("SELECT agent_no,agent_node, merchant_no FROM yfb_repay_merchant_info WHERE merchant_no = #{merchantNo}")
	@ResultType(RepayMerchantInfo.class)
	RepayMerchantInfo selectByMer(@Param("merchantNo")String merchantNo);

	public class SqlProvider{

		public String selectRepayMerchantByParam(Map<String, Object> param) {
			final RepayMerchantInfo info = (RepayMerchantInfo) param.get("info");
			SQL sql = new SQL() {
				{
					SELECT(" yrmi.merchant_no,yrmi.mobile_no,yrmi.user_name,yrmi.agent_no,yrmi.mer_account,yrmi.create_time,ywi.nickname,ywi.create_time AS enter_time,ai.agent_name,yii.pro_mer_no");
					FROM("yfb_repay_merchant_info yrmi "
							+ "INNER JOIN agent_info ai ON ai.agent_no = yrmi.agent_no "
							+"INNER JOIN yfb_unified_account_product yi ON yi.pro_mer_no = yrmi.merchant_no AND yi.pro_code = 'repay'"
							+"LEFT JOIN yfb_unified_account_product yii ON yii.un_account_mer_no = yi.un_account_mer_no AND yii.pro_code = 'gatherService'"
							+ "LEFT JOIN yfb_wechat_info ywi ON ywi.openid = yrmi.openid");
						WHERE("yrmi.agent_node LIKE CONCAT(CONCAT(#{info.agentNode}), '%')");
					if (StringUtils.isNotBlank(info.getMerchantNo())) {
						WHERE("yrmi.merchant_no=#{info.merchantNo} ");
					}
					if (StringUtils.isNotBlank(info.getMobileNo())) {
						WHERE("yrmi.mobile_no=#{info.mobileNo} ");
					}
					if (StringUtils.isNotBlank(info.getAgentNo())) {
						WHERE("yrmi.agent_no=#{info.agentNo} ");
					}
					if (StringUtils.isNotBlank(info.getsCreateTime())) {
						WHERE("yrmi.create_time>=#{info.sCreateTime} ");
					}
					if (StringUtils.isNotBlank(info.geteCreateTime())) {
						WHERE("yrmi.create_time<=#{info.eCreateTime} ");
					}
					if (StringUtils.isNotBlank(info.getsEnterTime())) {
						WHERE("ywi.create_time>=#{info.sEnterTime} ");
					}
					if (StringUtils.isNotBlank(info.geteEnterTime())) {
						WHERE("ywi.create_time<=#{info.eEnterTime} ");
					}
					if(StringUtils.isNotBlank(info.getAgentName())){
						WHERE("ai.agent_name=#{info.agentName} ");
					}
					if (StringUtils.isNotBlank(info.getMerAccount())) {
						WHERE("yrmi.mer_account=#{info.merAccount} ");
					}
					if(StringUtils.isNotBlank(info.getProMerNo())){
						WHERE("yii.pro_mer_no=#{info.proMerNo} ");
					}
					ORDER_BY("create_time DESC");
				}
			};
			return sql.toString();
		}
		
		public String listActivationCode(Map<String, Object> param){
            final ProviderBean bean = (ProviderBean) param.get("bean");
            SQL sql = new SQL(){{
                SELECT("ai.agent_no,ai.agent_name,ai.mobilephone,ysc.rate,ysc.single_amount,ai.parent_id");
                FROM("agent_info ai");
                LEFT_OUTER_JOIN("yfb_service_cost ysc ON ysc.agent_no = ai.agent_no AND ysc.service_type = 'repay'");
                WHERE("ai.agent_node like concat(#{loginAgent.agentNode}, '%')");
                if (StringUtils.isNotBlank(bean.getAgentNo())){
                    WHERE("ai.agent_no = #{bean.agentNo}");
                }
                if (StringUtils.isNotBlank(bean.getAgentName())){
                    WHERE("position(#{bean.agentName} in ai.agent_name)");
                }
                if (StringUtils.isNotBlank(bean.getMobilephone())){
                    WHERE("ai.mobilephone = #{bean.mobilephone}");
                }
                ORDER_BY("ai.agent_no");
            }};
            return sql.toString();
        }

	}

}
