package cn.eeepay.framework.daoSuperBank;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.MposTradeOrder;

public interface MposTradeOrderDao {

	/**
	 * 	获取Mpos交易信息
	 * @param mposTradeOrder
	 * @param page
	 * @return
	 */
	@SelectProvider(type = MposTradeOrderDao.SqlProvider.class, method = "selectMposTradeOrderList")
	@ResultType(MposTradeOrder.class)
	List<MposTradeOrder> selectMposTradeOrderList(@Param("baseInfo") MposTradeOrder mposTradeOrder,@Param("page")  Page<MposTradeOrder> page);

	/**
	 * 获取Mpos交易汇总信息
	 * @param mposTradeOrder
	 * @return
	 */
	@SelectProvider(type = MposTradeOrderDao.SqlProvider.class, method = "selectMposTradeOrderSum")
	@ResultType(MposTradeOrder.class)
	MposTradeOrder selectMposTradeOrderSum(@Param("baseInfo") MposTradeOrder mposTradeOrder);

	@SelectProvider(type = MposTradeOrderDao.SqlProvider.class, method = "selectMposTradeOrderDetail")
	@ResultType(MposTradeOrder.class)
	MposTradeOrder selectMposTradeOrderDetail(@Param("orderNo") String orderNo);

	class SqlProvider{
		public String selectMposTradeOrderList(Map<String, Object> param){
			MposTradeOrder baseInfo = (MposTradeOrder) param.get("baseInfo");
			SQL sql = new SQL();
			sql.SELECT("T1.id, T1.order_no, T1.v2_order_no, T1.user_code, T1.status , T1.trans_status,T1.trans_type,T1.org_id, T1.v2_merchant_code, T1.v2_merchant_name");
			sql.SELECT("T1.v2_merchant_phone, T1.sn_no , T1.product_type, T1.trade_amount, T1.trade_date, T1.is_active_trade, T1.settle_cycle , T1.receive_type");
			sql.SELECT("T1.platform_rate, T1.merchant_rate, T1.merchant_payment_fee, T1.platform_payment_fee , T1.merchant_trade_fee, T1.total_bonus_conf");
			sql.SELECT("T1.total_bonus, T1.plate_profit, T1.plate_trade_profit , T1.plate_payment_profit, T1.org_profit, T1.org_trade_profit_conf");
			sql.SELECT("T1.org_trade_profit, T1.org_payment_profit , T1.one_user_code, T1.one_user_type, T1.one_user_profit, T1.two_user_code, T1.two_user_type");
			sql.SELECT("T1.two_user_profit, T1.thr_user_code, T1.thr_user_type, T1.thr_user_profit, T1.fou_user_code , T1.fou_user_type, T1.fou_user_profit, T1.profit_status");
			sql.SELECT("T1.account_status, T1.province_name , T1.city_name, T1.district_name, T1.remark, T1.create_by, T1.create_date ,T1.update_by, T1.update_date,T1.complete_date");
			sql.SELECT("T1.actual_payment_fee,T1.settle_status");
			sql.SELECT("T2.user_name,T2.phone,T7.org_name,T3.user_name oneUserName,T4.user_name twoUserName,T5.user_name thrUserName,T6.user_name fouUserName");
			sql.SELECT("T8.basic_bonus_amount");
			sql.SELECT("T9.type_name as productTypeName");
			whereSql(sql,baseInfo);

			return sql.toString();
		}
		public String selectMposTradeOrderDetail(Map<String, Object> param){
			String orderNo = (String) param.get("orderNo");
			SQL sql = new SQL();
			sql.SELECT("T1.id, T1.order_no, T1.v2_order_no, T1.user_code, T1.status , T1.trans_status,T1.trans_type,T1.org_id, T1.v2_merchant_code, T1.v2_merchant_name");
			sql.SELECT("T1.v2_merchant_phone, T1.sn_no , T1.product_type, T1.trade_amount, T1.trade_date, T1.is_active_trade, T1.settle_cycle , T1.receive_type");
			sql.SELECT("T1.platform_rate, T1.merchant_rate, T1.merchant_payment_fee, T1.platform_payment_fee , T1.merchant_trade_fee, T1.total_bonus_conf");
			sql.SELECT("T1.total_bonus, T1.plate_profit, T1.plate_trade_profit , T1.plate_payment_profit, T1.org_profit, T1.org_trade_profit_conf");
			sql.SELECT("T1.org_trade_profit, T1.org_payment_profit , T1.one_user_code, T1.one_user_type, T1.one_user_profit, T1.two_user_code, T1.two_user_type");
			sql.SELECT("T1.two_user_profit, T1.thr_user_code, T1.thr_user_type, T1.thr_user_profit, T1.fou_user_code , T1.fou_user_type, T1.fou_user_profit, T1.profit_status");
			sql.SELECT("T1.account_status, T1.province_name , T1.city_name, T1.district_name, T1.remark, T1.create_by, T1.create_date ,T1.update_by, T1.update_date,T1.complete_date");
			sql.SELECT("T1.actual_payment_fee,T1.settle_status");
			sql.SELECT("T2.user_name,T2.phone,T7.org_name,T3.user_name oneUserName,T4.user_name twoUserName,T5.user_name thrUserName,T6.user_name fouUserName");
			sql.SELECT("T8.basic_bonus_amount");
			sql.SELECT("T9.type_name as productTypeName");
			sql.FROM("mpos_trade_order T1");
			sql.LEFT_OUTER_JOIN("user_info T2 on T1.user_code  = T2.user_code");
			sql.LEFT_OUTER_JOIN("user_info T3 on T1.one_user_code  = T3.user_code");
			sql.LEFT_OUTER_JOIN("user_info T4 on T1.two_user_code  = T4.user_code");
			sql.LEFT_OUTER_JOIN("user_info T5 on T1.thr_user_code  = T5.user_code");
			sql.LEFT_OUTER_JOIN("user_info T6 on T1.fou_user_code  = T6.user_code");
			sql.LEFT_OUTER_JOIN("org_info T7 on T1.org_id  = T7.org_id");
			sql.LEFT_OUTER_JOIN("red_territory_order_detail T8 on T1.order_no = T8.order_no");
			sql.LEFT_OUTER_JOIN("mpos_product_type T9 on T1.product_type = T9.id");
			sql.WHERE("T1.order_no = #{orderNo}");

			return sql.toString();
		}

		public String selectMposTradeOrderSum(Map<String, Object> param){
			MposTradeOrder baseInfo = (MposTradeOrder) param.get("baseInfo");
			SQL sql = new SQL(){{
				SELECT("count(DISTINCT T1.v2_order_no) as orderCount");
				SELECT("sum(merchant_trade_fee) as tradeFeeSum");
				SELECT("sum(merchant_payment_fee) as paymentFeeSum");
				SELECT("sum(plate_profit) as plateProfitSum");
				SELECT("sum(plate_trade_profit) as plateTradeProfitSum");
				SELECT("sum(plate_payment_profit) as platePaymentProfitSum");
				SELECT("sum(trade_amount) as tradeAmountSum");
			}};

			whereSql(sql, baseInfo);
			return sql.toString();
		}

		private void whereSql(SQL sql,MposTradeOrder baseInfo) {
			sql.FROM("mpos_trade_order T1");
			sql.LEFT_OUTER_JOIN("user_info T2 on T1.user_code  = T2.user_code");
			sql.LEFT_OUTER_JOIN("user_info T3 on T1.one_user_code  = T3.user_code");
			sql.LEFT_OUTER_JOIN("user_info T4 on T1.two_user_code  = T4.user_code");
			sql.LEFT_OUTER_JOIN("user_info T5 on T1.thr_user_code  = T5.user_code");
			sql.LEFT_OUTER_JOIN("user_info T6 on T1.fou_user_code  = T6.user_code");
			sql.LEFT_OUTER_JOIN("org_info T7 on T1.org_id  = T7.org_id");
			sql.LEFT_OUTER_JOIN("red_territory_order_detail T8 on T1.order_no = T8.order_no");
			sql.LEFT_OUTER_JOIN("mpos_product_type T9 on T1.product_type = T9.id");

			if(baseInfo != null){
				if(StringUtils.isNotBlank(baseInfo.getOrderNo())) {
					sql.WHERE("T1.order_no = #{baseInfo.orderNo}");
				}
				if(StringUtils.isNotBlank(baseInfo.getV2OrderNo())) {
					sql.WHERE("T1.v2_order_no = #{baseInfo.v2OrderNo}");
				}
				if(StringUtils.isNotBlank(baseInfo.getV2MerchantCode())) {
					sql.WHERE("T1.v2_merchant_code = #{baseInfo.v2MerchantCode}");
				}
				if(StringUtils.isNotBlank(baseInfo.getV2MerchantPhone())) {
					sql.WHERE("T1.v2_merchant_phone = #{baseInfo.v2MerchantPhone}");
				}
				if(StringUtils.isNotBlank(baseInfo.getSnNo())) {
					sql.WHERE("T1.sn_no = #{baseInfo.snNo}");
				}
				if(null != baseInfo.getSettleCycle()) {
					sql.WHERE("T1.settle_cycle = #{baseInfo.settleCycle}");
				}
				if(StringUtils.isNotBlank(baseInfo.getTradeDateStart()) && StringUtils.isNotBlank(baseInfo.getTradeDateEnd())) {
					sql.WHERE("T1.trade_date BETWEEN #{baseInfo.tradeDateStart} AND #{baseInfo.tradeDateEnd}");
				}
				if(StringUtils.isNotBlank(baseInfo.getTransType())) {
					sql.WHERE("T1.trans_type = #{baseInfo.transType}");
				}
				if(null != baseInfo.getReceiveType()) {
					sql.WHERE("T1.receive_type = #{baseInfo.receiveType}");
				}
				if(StringUtils.isNotBlank(baseInfo.getProfitStatus())) {
					sql.WHERE("T1.profit_status = #{baseInfo.profitStatus}");
				}
				if(StringUtils.isNotBlank(baseInfo.getStatus())) {
					sql.WHERE("T1.status = #{baseInfo.status}");
				}
				if(StringUtils.isNotBlank(baseInfo.getAccountStatus())) {
					sql.WHERE("T1.account_status = #{baseInfo.accountStatus}");
				}
				if(null != baseInfo.getOrgId() && -1 != baseInfo.getOrgId()) {
					sql.WHERE("T1.org_id = #{baseInfo.orgId}");
				}
				if(StringUtils.isNotBlank(baseInfo.getUserCode())) {
					sql.WHERE("T1.user_code = #{baseInfo.userCode}");
				}
				if(StringUtils.isNotBlank(baseInfo.getPhone())) {
					sql.WHERE("T2.phone = #{baseInfo.phone}");
				}
				if(StringUtils.isNotBlank(baseInfo.getOneUserCode())) {
					sql.WHERE("T1.one_user_code = #{baseInfo.oneUserCode}");
				}
				if(StringUtils.isNotBlank(baseInfo.getOneUserName())) {
					sql.WHERE("T3.oneUserName = #{baseInfo.oneUserName}");
				}
				if(StringUtils.isNotBlank(baseInfo.getOneUserType())) {
					sql.WHERE("T1.one_user_type = #{baseInfo.oneUserType}");
				}
				if(StringUtils.isNotBlank(baseInfo.getTwoUserCode())) {
					sql.WHERE("T1.two_user_code = #{baseInfo.twoUserCode}");
				}
				if(StringUtils.isNotBlank(baseInfo.getTwoUserName())) {
					sql.WHERE("T4.twoUserName = #{baseInfo.twoUserName}");
				}
				if(StringUtils.isNotBlank(baseInfo.getTwoUserType())) {
					sql.WHERE("T1.two_user_type = #{baseInfo.twoUserType}");
				}
				if(StringUtils.isNotBlank(baseInfo.getThrUserCode())) {
					sql.WHERE("T1.thr_user_code = #{baseInfo.thrUserCode}");
				}
				if(StringUtils.isNotBlank(baseInfo.getThrUserName())) {
					sql.WHERE("T5.thrUserName = #{baseInfo.thrUserName}");
				}
				if(StringUtils.isNotBlank(baseInfo.getThrUserType())) {
					sql.WHERE("T1.thr_user_type = #{baseInfo.thrUserType}");
				}
				if(StringUtils.isNotBlank(baseInfo.getFouUserCode())) {
					sql.WHERE("T1.fou_user_code = #{baseInfo.fouUserCode}");
				}
				if(StringUtils.isNotBlank(baseInfo.getFouUserName())) {
					sql.WHERE("T6.fouUserName = #{baseInfo.fouUserName}");
				}
				if(StringUtils.isNotBlank(baseInfo.getFouUserType())) {
					sql.WHERE("T1.fou_user_type = #{baseInfo.fouUserType}");
				}
				if(StringUtils.isNotBlank(baseInfo.getProvinceName())) {
					sql.WHERE("T1.province_name = #{baseInfo.provinceName}");
				}
				if(StringUtils.isNotBlank(baseInfo.getCityName())) {
					sql.WHERE("T1.city_name = #{baseInfo.cityName}");
				}
				if(StringUtils.isNotBlank(baseInfo.getDistrictName())) {
					sql.WHERE("T1.district_name = #{baseInfo.districtName}");
				}
				if(StringUtils.isNotBlank(baseInfo.getTransStatus())) {
					sql.WHERE("T1.trans_status = #{baseInfo.transStatus}");
				}
				if(StringUtils.isNotBlank(baseInfo.getProductType())) {
					sql.WHERE("T1.product_type = #{baseInfo.productType}");
				}
				if(StringUtils.isNotBlank(baseInfo.getSettleStatus())) {
					sql.WHERE("T1.settle_status = #{baseInfo.settleStatus}");
				}
			}
		}

	}

}
