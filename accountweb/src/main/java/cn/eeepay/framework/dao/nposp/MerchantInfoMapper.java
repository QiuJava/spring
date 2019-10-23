package cn.eeepay.framework.dao.nposp;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cn.eeepay.framework.model.nposp.TransInfoPreFreezeLog;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;

import cn.eeepay.framework.model.nposp.MerchantInfo;

public interface MerchantInfoMapper {
	@Select("select merchant_name,mobilephone,agent_no,one_agent_no from merchant_info where merchant_no = #{userId}")
	@ResultType(MerchantInfo.class)
	MerchantInfo findMerchantInfoByUserId(@Param("userId")String userId);
	
	@Select("select merchant_name,mobilephone,agent_no,one_agent_no from merchant_info where risk_status = '1' AND merchant_no = #{userId}")
	@ResultType(MerchantInfo.class)
	MerchantInfo findMerchantInfoByUserIdRiskStatus(@Param("userId")String userId);
	
	@SelectProvider(type = SqlProvider.class, method = "findByParams")
	@ResultType(String.class)
	List<String> findByNameAndMobile(@Param("merchantNo")String merchantNo, @Param("merchantName")String merchantName, @Param("mobile")String mobile);
	

	@SelectProvider( type=SqlProvider.class,method="findMerchantListByParams")
	@ResultType(String.class)
	List<String> findMerchantListByParams(@Param("userName")String userName ,@Param("mobilephone")String mobilephone ) ;

	//通过银联商户编号查询同步状态
	@Select("SELECT * FROM zq_merchant_info WHERE unionpay_mer_no = #{acqMerchantNo}")
	@ResultType(Map.class)
	Map<String,Object> queryQrMerchantInfo(@Param("acqMerchantNo")String acqMerchantNo) ;

	//通过银联商户编号查询同步状态
	@Select("SELECT * FROM zq_merchant_info WHERE merchant_no = #{merchantNo}")
	@ResultType(Map.class)
	Map<String,Object> queryQrMerInfo(@Param("merchantNo")String merchantNo) ;

	@Select("SELECT pre_frozen_amount from merchant_info "
			+ " where merchant_no=#{merchantNo}")
	@ResultType(MerchantInfo.class)
	MerchantInfo selectByMerchantNo(@Param("merchantNo") String merchantNo);

	@Insert("insert into trans_info_pre_freeze_log("
			+ "merchant_no,pre_freeze_note,oper_log,"
			+ "oper_time,oper_id,oper_name)"
			+ " values(#{record.merchantNo},#{record.preFreezeNote},#{record.operLog},"
			+ "#{record.operTime},#{record.operId},#{record.operName})")
	int insertPreFreezeLog(@Param("record")TransInfoPreFreezeLog record);

	@Update("update merchant_info set pre_frozen_amount=#{preFrozenAmount} "
			+ " where merchant_no=#{merchantNo}")
	int updatePreFrozenAmountByMerId(@Param("preFrozenAmount") BigDecimal preFrozenAmount, @Param("merchantNo") String merchantNo);

	@Select("select * from zq_merchant_info where mbp_id = #{bpmId} and channel_code = #{channelCode}")
	@ResultType(Map.class)
	Map<String,Object> findMerchantInfoByBpmIdAndChannelCode(@Param("bpmId") String bpmId,@Param("channelCode") String channelCode);

    public class SqlProvider{
		public String findByParams(final Map<String, Object> parameter) {
			final String merchantNo = (String) parameter.get("merchantNo");
			final String merchantName = (String) parameter.get("merchantName");
			final String mobile = (String) parameter.get("mobile");
			return new SQL() {{
				SELECT("merchant_no");
				FROM("merchant_info");
				if (StringUtils.isNotBlank(merchantNo)) {
					StringBuilder sb = new StringBuilder();
					sb.append(" merchant_no in(");
					sb.append(merchantNo);
					sb.append(") ");
					WHERE(sb.toString());
				}
				if (StringUtils.isNotBlank(merchantName)) {
					WHERE(" merchant_name like \"%\"#{merchantName}\"%\" ");
				}
				if (StringUtils.isNotBlank(mobile)) {
					WHERE(" mobilephone like \"%\"#{mobile}\"%\" ");
				}

			}}.toString();
		}
		

		public String findMerchantListByParams(final Map<String, Object> parameter) {
			final String merchantName = (String) parameter.get("userName");
			final String mobile = (String) parameter.get("mobilephone");
			return new SQL() {{
				SELECT("merchant_no");
				FROM("merchant_info");
				if (StringUtils.isNotBlank(merchantName)) {
					WHERE(" merchant_name like \"%\"#{userName}\"%\" ");
				}
				if (StringUtils.isNotBlank(mobile)) {
					WHERE(" mobilephone like \"%\"#{mobilephone}\"%\" ");
				}
			}}.toString();
		}
		
	}

	
}
