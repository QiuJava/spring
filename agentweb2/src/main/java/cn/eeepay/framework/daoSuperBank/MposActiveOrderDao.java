package cn.eeepay.framework.daoSuperBank;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.MposActiveOrder;

public interface MposActiveOrderDao {

	/**
	 *	 获取Mpos激活信息
	 * @param mposActiveOrder
	 * @param page
	 * @return
	 */
	@SelectProvider(type = MposActiveOrderDao.SqlProvider.class, method = "selectMposActiveOrderList")
	@ResultType(MposActiveOrder.class)
	List<MposActiveOrder> selectMposActiveOrderList(@Param("baseInfo") MposActiveOrder mposActiveOrder,@Param("page") Page<MposActiveOrder> page);

	/**
	 * 获取Mpos激活汇总信息
	 * @param mposActiveOrder
	 * @return
	 */
	@SelectProvider(type = MposActiveOrderDao.SqlProvider.class, method = "selectMposActiveOrderSum")
	@ResultType(MposActiveOrder.class)
	MposActiveOrder selectMposActiveOrderSum(@Param("baseInfo") MposActiveOrder mposActiveOrder);

	class SqlProvider{
		public String selectMposActiveOrderList(Map<String, Object> param){
			MposActiveOrder baseInfo = (MposActiveOrder) param.get("baseInfo");
			SQL sql = new SQL();
			sql.SELECT("T1.id, T1.order_no, T1.user_code, T1.status, T1.org_id , T1.v2_merchant_code, T1.v2_merchant_name, T1.v2_merchant_phone, T1.sn_no,T0.type_name as productTypeName");
			sql.SELECT("T1.product_type , T1.register_date, T1.active_date, T1.active_num, T1.active_return_bonus, T1.active_return_date,T1.total_bonus , T1.plate_profit, T1.org_profit");
			sql.SELECT("T1.one_user_code, T1.one_user_type, T1.one_user_profit , T1.two_user_code, T1.two_user_type, T1.two_user_profit, T1.thr_user_code, T1.thr_user_type");
			sql.SELECT("T1.thr_user_profit, T1.fou_user_code, T1.fou_user_type, T1.fou_user_profit, T1.profit_status , T1.account_status, T1.province_name, T1.city_name, T1.district_name");
			sql.SELECT("T1.remark ,T1.complete_date, T1.create_by, T1.create_date, T1.update_by, T1.update_date");
			sql.SELECT("T2.user_name,T2.phone,T7.org_name,T3.user_name oneUserName,T4.user_name twoUserName,T5.user_name thrUserName,T6.user_name fouUserName,T8.type_name");
			whereSql(sql,baseInfo);

			return sql.toString();
		}

		public String selectMposActiveOrderSum(Map<String, Object> param){
			MposActiveOrder baseInfo = (MposActiveOrder) param.get("baseInfo");
			SQL sql = new SQL(){{
				SELECT("count(DISTINCT T1.v2_merchant_code) as v2MerchantSum");
				SELECT("sum(case when T1.status = 2 or T1.status = 3 then 1 else 0 end) as activeSum");
				SELECT("sum(case when T1.status = 1 then 1 else 0 end) as inActiveSum");
				SELECT("sum(case when T1.account_status = 1 then T1.active_return_bonus else 0 end) as activeReturnBonusSum");
				SELECT("sum(case when T1.account_status = 1 then T1.total_bonus else 0 end) as totalBonusSum");
			}};
			whereSql(sql, baseInfo);
			return sql.toString();
		}

		private void whereSql(SQL sql,MposActiveOrder baseInfo) {
			sql.FROM("mpos_active_order T1");
			sql.LEFT_OUTER_JOIN("mpos_product_type T0 on T1.product_type = T0.id");
			sql.LEFT_OUTER_JOIN("user_info T2 on T1.user_code  = T2.user_code");
			sql.LEFT_OUTER_JOIN("user_info T3 on T1.one_user_code  = T3.user_code");
			sql.LEFT_OUTER_JOIN("user_info T4 on T1.two_user_code  = T4.user_code");
			sql.LEFT_OUTER_JOIN("user_info T5 on T1.thr_user_code  = T5.user_code");
			sql.LEFT_OUTER_JOIN("user_info T6 on T1.fou_user_code  = T6.user_code");
			sql.LEFT_OUTER_JOIN("org_info T7 on T1.org_id  = T7.org_id");
			sql.LEFT_OUTER_JOIN("mpos_product_type T8 on T1.product_type = T8.id");

			if(baseInfo != null){
				if(StringUtils.isNotBlank(baseInfo.getOrderNo())) {
					sql.WHERE(" T1.order_no = #{baseInfo.orderNo}");
				}
				if(StringUtils.isNotBlank(baseInfo.getSnNo())) {
					sql.WHERE(" T1.sn_no = #{baseInfo.snNo}");
				}
				if(StringUtils.isNotBlank(baseInfo.getStatus())) {
					sql.WHERE(" T1.status = #{baseInfo.status}");
				}
				if(null != baseInfo.getOrgId() && -1 != baseInfo.getOrgId()) {
					sql.WHERE(" T1.org_id = #{baseInfo.orgId}");
				}
				if(StringUtils.isNotBlank(baseInfo.getRegisterDateStart()) && StringUtils.isNotBlank(baseInfo.getRegisterDateEnd())) {
					sql.WHERE("T1.register_date BETWEEN #{baseInfo.registerDateStart} AND #{baseInfo.registerDateEnd}");
				}
				if(StringUtils.isNotBlank(baseInfo.getV2MerchantCode())) {
					sql.WHERE("T1.v2_merchant_code = #{baseInfo.v2MerchantCode}");
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
				if(StringUtils.isNotBlank(baseInfo.getRemark())) {
					sql.WHERE("T1.remark = #{baseInfo.remark}");
				}
			}
		}

	}

}
