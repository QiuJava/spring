package cn.eeepay.framework.dao.peragent;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.type.JdbcType;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.peragent.PaShareDetail;
import cn.eeepay.framework.model.peragent.PaBrand;

public interface PaShareDetailMapper {

	@SelectProvider(type = SqlProvider.class, method = "findPeragentList")
	@ResultMap("cn.eeepay.framework.dao.peragent.PaShareDetailMapper.BaseResultMap")
	public List<PaShareDetail>  findPeragentList(@Param("paShareDetail") PaShareDetail paShareDetail, @Param("sort") Sort sort,
			@Param("page") Page<PaShareDetail> page);

	@Select(" select brand_code as brandCode,brand_name as brandName from pa_brand ")
	@ResultType(PaBrand.class)
	public List<PaBrand>  findPaBrandList();
	
	@Select(" select brand_code as brandCode,brand_name as brandName from pa_brand where brand_code = #{brandCode}")
	@ResultType(PaBrand.class)
	public PaBrand findByBrandCode(@Param("brandCode") String brandCode);
	
	@SelectProvider(type=SqlProvider.class,method="findPeragentListCollection")
	@Results(value = {  
            @Result(property = "allShareTotalAmount", column = "all_share_total_amount",javaType=BigDecimal.class,jdbcType=JdbcType.DECIMAL),  
            @Result(property = "allNoEnterShareTotalAmount", column = "all_no_enter_share_total_amount",javaType=BigDecimal.class,jdbcType=JdbcType.DECIMAL),
            @Result(property = "allAccountedShareTotalAmount", column = "all_accounted_share_total_amount",javaType=BigDecimal.class,jdbcType=JdbcType.DECIMAL),
        })
	public Map<String, Object> findPeragentListCollection(@Param("paShareDetail")PaShareDetail paShareDetail);
	
	@SelectProvider(type=SqlProvider.class,method="comfirmBacthAccount")
	@Results(value = {  
            @Result(property = "enterAmount", column = "enterAmount",javaType=BigDecimal.class,jdbcType=JdbcType.DECIMAL),
        })
	public Map<String, Object> comfirmBacthAccount(@Param("accountMonth")String accountMonth);
	
	@SelectProvider(type = SqlProvider.class, method = "peragentExport")
	@ResultMap("cn.eeepay.framework.dao.peragent.PaShareDetailMapper.BaseResultMap")
	public List<PaShareDetail> peragentExport(@Param("paShareDetail")PaShareDetail paShareDetail);
	
	@Select(" select * from  pa_share_detail  where share_month = #{accountMonth} and acc_status = 'NOENTERACCOUNT'  and share_type in ('3','4','5','6') ")
	@ResultMap("cn.eeepay.framework.dao.peragent.PaShareDetailMapper.BaseResultMap")
	public List<PaShareDetail> findBacthAccount(@Param("accountMonth")String accountMonth);
	
	@Select(" select * from pa_share_detail where id = #{id} and acc_status = 'NOENTERACCOUNT' and share_type in ('3','4','5','6') ")
	@ResultMap("cn.eeepay.framework.dao.peragent.PaShareDetailMapper.BaseResultMap")
	public PaShareDetail findPaShareDetailById(@Param("id")Integer id);

	
	
	@Update(" update pa_share_detail set "
			+ " acc_status=#{paShareDetail.accStatus},"
			+ " acc_time=#{paShareDetail.accTime},"
			+ " acc_operator=#{paShareDetail.accOperator},"
			+ " acc_message=#{paShareDetail.accMessage}"
			+"  where id=#{paShareDetail.id}")
	public int updatePaShareDetailById(@Param("paShareDetail")PaShareDetail paShareDetail);
	
	public class SqlProvider {

		public String findPeragentList(Map<String, Object> param) {
			final PaShareDetail paShareDetail = (PaShareDetail) param.get("paShareDetail");
			final Sort sord = (Sort) param.get("sort");
			return new SQL() {
				{
					SELECT(" psd.*, pui.user_type, pui.brand_code , pui.real_name , pui.one_user_code, pui.two_user_code, pui.share_level ");
					FROM(" pa_share_detail psd LEFT JOIN pa_user_info pui on psd.user_code = pui.user_code ");
					//用户名称
					if(StringUtils.isNotBlank(paShareDetail.getRealName()) ){
						WHERE(" pui.real_name  like  \"%\"#{paShareDetail.realName}\"%\" ");
					}
					//用户类型
					if(StringUtils.isNotBlank(paShareDetail.getUserType()) && !"ALL".equals(paShareDetail.getUserType()))
						WHERE(" pui.user_type = #{paShareDetail.userType}  ");
					//用户编号
					if(StringUtils.isNotBlank(paShareDetail.getUserCode()))
						WHERE(" psd.user_code = #{paShareDetail.userCode}  ");
					//分润类别
					if(StringUtils.isNotBlank(paShareDetail.getShareType()) && !"ALL".equals(paShareDetail.getShareType()))
						WHERE(" psd.share_type = #{paShareDetail.shareType}  ");
					//入账状态
					if(StringUtils.isNotBlank(paShareDetail.getAccStatus()) && !"ALL".equals(paShareDetail.getAccStatus()))
						WHERE(" psd.acc_status = #{paShareDetail.accStatus}  ");
					//所属品牌
					if(StringUtils.isNotBlank(paShareDetail.getBrandCode()) && !"ALL".equals(paShareDetail.getBrandCode()))
						WHERE(" pui.brand_code = #{paShareDetail.brandCode}  ");
					//所属机构
					if(StringUtils.isNotBlank(paShareDetail.getOneUserCode()))
						WHERE(" pui.one_user_code = #{paShareDetail.oneUserCode}  and pui.user_code <> #{paShareDetail.oneUserCode}  ");
					//所属大盟主
					if(StringUtils.isNotBlank(paShareDetail.getTwoUserCode()))
						WHERE(" pui.two_user_code = #{paShareDetail.twoUserCode} and pui.user_code <> #{paShareDetail.twoUserCode}  ");
					//入账时间
					if(StringUtils.isNotBlank(paShareDetail.getAccTime1()) ){
						WHERE(" psd.acc_time >= #{paShareDetail.accTime1} ");
					}
					if(StringUtils.isNotBlank(paShareDetail.getAccTime2()) ){
						WHERE(" psd.acc_time <= #{paShareDetail.accTime2} ");
					}
					//分润创建日期
					if(StringUtils.isNotBlank(paShareDetail.getCreateTime1()) ){
						WHERE(" psd.create_time >= #{paShareDetail.createTime1} ");
					}
					if(StringUtils.isNotBlank(paShareDetail.getCreateTime2()) ){
						WHERE(" psd.create_time <= #{paShareDetail.createTime2} ");
					}
					if (sord != null && StringUtils.isNotBlank(sord.getSidx())) {
						ORDER_BY(propertyMapping(sord.getSidx(), 0) + " " + sord.getSord());
					} else {
						ORDER_BY(" psd.create_time desc ");
					}
				}
			}.toString();
		}
		
		
		public String peragentExport(Map<String, Object> param) {
			final PaShareDetail paShareDetail = (PaShareDetail) param.get("paShareDetail");
			return new SQL() {
				{
					SELECT(" psd.*, pui.user_type, pui.brand_code, pui.real_name , pui.one_user_code, pui.two_user_code, pui.share_level ");
					FROM(" pa_share_detail psd LEFT JOIN pa_user_info pui on psd.user_code = pui.user_code ");
					//用户名称
					if(StringUtils.isNotBlank(paShareDetail.getRealName()) ){
						WHERE(" pui.real_name  like  \"%\"#{paShareDetail.realName}\"%\" ");
					}
					//用户类型
					if(StringUtils.isNotBlank(paShareDetail.getUserType()) && !"ALL".equals(paShareDetail.getUserType()))
						WHERE(" pui.user_type = #{paShareDetail.userType}  ");
					//用户编号
					if(StringUtils.isNotBlank(paShareDetail.getUserCode()))
						WHERE(" psd.user_code = #{paShareDetail.userCode}  ");
					//分润类别
					if(StringUtils.isNotBlank(paShareDetail.getShareType()) && !"ALL".equals(paShareDetail.getShareType()))
						WHERE(" psd.share_type = #{paShareDetail.shareType}  ");
					//入账状态
					if(StringUtils.isNotBlank(paShareDetail.getAccStatus()) && !"ALL".equals(paShareDetail.getAccStatus()))
						WHERE(" psd.acc_status = #{paShareDetail.accStatus}  ");
					//所属品牌
					if(StringUtils.isNotBlank(paShareDetail.getBrandCode()) && !"ALL".equals(paShareDetail.getBrandCode()))
						WHERE(" pui.brand_code = #{paShareDetail.brandCode}  ");
					//所属机构
					if(StringUtils.isNotBlank(paShareDetail.getOneUserCode()))
						WHERE(" pui.one_user_code = #{paShareDetail.oneUserCode} and pui.user_code <> #{paShareDetail.oneUserCode}  ");
					//所属大盟主
					if(StringUtils.isNotBlank(paShareDetail.getTwoUserCode()))
						WHERE(" pui.two_user_code = #{paShareDetail.twoUserCode} and pui.user_code <> #{paShareDetail.twoUserCode}  ");
					//入账时间
					if(StringUtils.isNotBlank(paShareDetail.getAccTime1()) ){
						WHERE(" psd.acc_time >= #{paShareDetail.accTime1} ");
					}
					if(StringUtils.isNotBlank(paShareDetail.getAccTime2()) ){
						WHERE(" psd.acc_time <= #{paShareDetail.accTime2} ");
					}
					//分润创建日期
					if(StringUtils.isNotBlank(paShareDetail.getCreateTime1()) ){
						WHERE(" psd.create_time >= #{paShareDetail.createTime1} ");
					}
					if(StringUtils.isNotBlank(paShareDetail.getCreateTime2()) ){
						WHERE(" psd.create_time <= #{paShareDetail.createTime2} ");
					}
                     ORDER_BY(" psd.create_time desc ");
					
				}
			}.toString();
		}
		
		
		public String findPeragentListCollection(final Map<String, Object> parameter){
			final PaShareDetail paShareDetail=(PaShareDetail)parameter.get("paShareDetail");
			return new SQL(){{
				SELECT(" sum(psd.share_amount) all_share_total_amount,"
						+ " sum(case when psd.acc_status = 'NOENTERACCOUNT' then psd.share_amount else 0 end) as all_no_enter_share_total_amount,"
						+ " sum(case when psd.acc_status = 'ENTERACCOUNTED' then psd.share_amount else 0 end) as all_accounted_share_total_amount ");
				FROM("  pa_share_detail psd LEFT JOIN pa_user_info pui on psd.user_code = pui.user_code  ");
				//用户名称
				if(StringUtils.isNotBlank(paShareDetail.getRealName()) ){
					WHERE(" pui.real_name  like  \"%\"#{paShareDetail.realName}\"%\" ");
				}
				//用户类型
				if(StringUtils.isNotBlank(paShareDetail.getUserType()) && !"ALL".equals(paShareDetail.getUserType()))
					WHERE(" pui.user_type = #{paShareDetail.userType}  ");
				//用户编号
				if(StringUtils.isNotBlank(paShareDetail.getUserCode()))
					WHERE(" psd.user_code = #{paShareDetail.userCode}  ");
				//分润类别
				if(StringUtils.isNotBlank(paShareDetail.getShareType()) && !"ALL".equals(paShareDetail.getShareType()))
					WHERE(" psd.share_type = #{paShareDetail.shareType}  ");
				//入账状态
				if(StringUtils.isNotBlank(paShareDetail.getAccStatus()) && !"ALL".equals(paShareDetail.getAccStatus()))
					WHERE(" psd.acc_status = #{paShareDetail.accStatus}  ");
				//所属品牌
				if(StringUtils.isNotBlank(paShareDetail.getBrandCode()) && !"ALL".equals(paShareDetail.getBrandCode()))
					WHERE(" pui.brand_code = #{paShareDetail.brandCode}  ");
				//所属机构
				if(StringUtils.isNotBlank(paShareDetail.getOneUserCode()))
					WHERE(" pui.one_user_code = #{paShareDetail.oneUserCode}  and pui.user_code <> #{paShareDetail.oneUserCode}   ");
				//所属大盟主
				if(StringUtils.isNotBlank(paShareDetail.getTwoUserCode()))
					WHERE(" pui.two_user_code = #{paShareDetail.twoUserCode} and pui.user_code <> #{paShareDetail.twoUserCode}  ");
				//入账时间
				if(StringUtils.isNotBlank(paShareDetail.getAccTime1()) ){
					WHERE(" psd.acc_time >= #{paShareDetail.accTime1} ");
				}
				if(StringUtils.isNotBlank(paShareDetail.getAccTime2()) ){
					WHERE(" psd.acc_time <= #{paShareDetail.accTime2} ");
				}
				//分润创建日期
				if(StringUtils.isNotBlank(paShareDetail.getCreateTime1()) ){
					WHERE(" psd.create_time >= #{paShareDetail.createTime1} ");
				}
				if(StringUtils.isNotBlank(paShareDetail.getCreateTime2()) ){
					WHERE(" psd.create_time <= #{paShareDetail.createTime2} ");
				}
			}}.toString();
		}
		
		
		public String comfirmBacthAccount(final Map<String, Object> parameter){
			final String accountMonth=(String)parameter.get("accountMonth");
			return new SQL(){{
				SELECT("  sum(case when psd.acc_status = 'NOENTERACCOUNT' then psd.share_amount else 0 end) as enterAmount ");
				FROM(" pa_share_detail as psd where psd.share_month = #{accountMonth} and  psd.share_type in ('3','4','5','6')  ");
			}}.toString();
		}
		

		public String propertyMapping(String name, int type) {
			final String[] propertys = { "id", "shareAmount", "shareType", "teamTotalAmount", "totalAmount","grade", "realName","userCode", "brandCode","createTime","accTime" };
			final String[] columns = {   "id", "share_amount", "share_type", "team_total_amount", "total_amount","grade", "realName","user_code", "brand_code","create_time","acc_time" };
			if (StringUtils.isNotBlank(name)) {
				if (type == 0) {// 属性查出字段名
					for (int i = 0; i < propertys.length; i++) {
						if (name.equalsIgnoreCase(propertys[i])) {
							return columns[i];
						}
					}
				} else if (type == 1) {// 字段名查出属性
					for (int i = 0; i < propertys.length; i++) {
						if (name.equalsIgnoreCase(columns[i])) {
							return propertys[i];
						}
					}
				}
			}
			return null;
		}
	}

	

	

	

	
}
