package cn.eeepay.framework.daoSuperBank;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.MposMerchantTradeCount;

public interface MposMerchantTradeCountDao {

	@SelectProvider(type = MposMerchantTradeCountDao.SqlProvider.class, method = "selectMposMerchantTradeCountList")
	@ResultType(MposMerchantTradeCount.class)
	List<MposMerchantTradeCount> selectMposMerchantTradeCountList(@Param("baseInfo") MposMerchantTradeCount mposMerchantTradeCount,
																  @Param("page") Page<MposMerchantTradeCount> page);

	class SqlProvider{
		public String selectMposMerchantTradeCountList(Map<String, Object> param){
			MposMerchantTradeCount baseInfo = (MposMerchantTradeCount) param.get("baseInfo");
			SQL sql = new SQL();
			sql.SELECT("T1.id, T1.v2_merchant_code, T1.v2_merchant_name, T1.v2_merchant_phone, T1.sn_no , T1.product_type");
			sql.SELECT("T1.register_date, T1.user_code, T1.org_id, T1.total_trade_amount , T1.month_trade_amount, T1.near30_trade_amount,T1.create_date");
			sql.SELECT("T2.user_name,T2.phone,T3.org_name");
			sql.FROM("mpos_merchant_trade_count T1");
			sql.LEFT_OUTER_JOIN("user_info T2 on T1.user_code  = T2.user_code");
			sql.LEFT_OUTER_JOIN("org_info T3 on T1.org_id  = T3.org_id");

			if(baseInfo != null){
				if(StringUtils.isNotBlank(baseInfo.getV2MerchantCode())) {
					sql.WHERE("T1.v2_merchant_code = #{baseInfo.v2MerchantCode}");
				}
				if(StringUtils.isNotBlank(baseInfo.getV2MerchantPhone())) {
					sql.WHERE("T1.v2_merchant_phone = #{baseInfo.v2MerchantPhone}");
				}
				if(null != baseInfo.getOrgId() && -1 != baseInfo.getOrgId()) {
					sql.WHERE("T1.org_id = #{baseInfo.orgId}");
				}
				if(StringUtils.isNotBlank(baseInfo.getRegisterDateStart()) && StringUtils.isNotBlank(baseInfo.getRegisterDateEnd())) {
					sql.WHERE("T1.register_date BETWEEN #{baseInfo.registerDateStart} AND #{baseInfo.registerDateEnd}");
				}
				if(StringUtils.isNotBlank(baseInfo.getSnNo())) {
					sql.WHERE("T1.sn_no = #{baseInfo.snNo}");
				}
				if(StringUtils.isNotBlank(baseInfo.getUserCode())) {
					sql.WHERE("T1.user_code = #{baseInfo.userCode}");
				}
				if(StringUtils.isNotBlank(baseInfo.getUserName())) {
					sql.WHERE("T2.user_name = #{baseInfo.userName}");
				}
				if(StringUtils.isNotBlank(baseInfo.getPhone())) {
					sql.WHERE("T2.phone = #{baseInfo.phone}");
				}
			}
			return sql.toString();
		}
	}
}
