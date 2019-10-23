package cn.eeepay.framework.daoSuperBank;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.OrgInfo;
import cn.eeepay.framework.model.UserCard;
import cn.eeepay.framework.model.UserInfoSuperBank;

public interface UserInfoSuperBankDao {
	
	/**
	 * 用户管理用户数量统计
	 * @param userInfoSuperBank
	 * @return
	 */
	@SelectProvider(type = SqlProvider.class, method = "selectTotal")
	@ResultType(List.class)
	List<Map<String,Object>> selectTotal(@Param("userInfoSuperBank")UserInfoSuperBank userInfoSuperBank,@Param("agentNo")String agentNo);
	
	@Select("select user_id,user_code,open_province,open_city,open_region,remark,phone from user_info where user_code = #{userCode}")
    @ResultType(UserInfoSuperBank.class)
	UserInfoSuperBank selectAddress(@Param("userCode")String userCode);
	
	@Select("select income_type from user_income where order_no = #{orderNo}")
    @ResultType(UserInfoSuperBank.class)
	String selectIncomeType(@Param("orderNo")String orderNo);
	
	@Select("select oem_default_user from user_info where phone = #{telNo} and second_user_node = #{secondUserNode}")
    @ResultType(String.class)
	String isOem(@Param("telNo")String telNo, @Param("secondUserNode")String secondUserNode);
	
	@Select("select TOTAL_AMOUNT from account_info where USER_CODE = #{userCode}")
    @ResultType(UserInfoSuperBank.class)
	String selectTotalAmount(@Param("userCode")String userCode);
	
	@Select("select user_id, user_code, user_name from user_info")
    @ResultType(UserInfoSuperBank.class)
    List<UserInfoSuperBank> getAllList();
	
	@SelectProvider(type = SqlProvider.class, method = "selectUserInfoList")
    @ResultType(UserInfoSuperBank.class)
    List<UserInfoSuperBank> selectUserInfoList(@Param("userCode") String userCode);
	/**
	 * 用户银行卡信息
	 * @param userCode
	 * @return
	 */
	@Select("select * from user_card where user_code = #{userCode} and status = '1'")
	@ResultType(UserCard.class)
	UserCard selectUserCard(@Param("userCode")String userCode);
	
	/**
	 * 用户管理详情
	 * @param userId
	 * @return
	 */
	@Select("select ui.*,oi.org_name from user_info ui left join org_info oi on ui.org_id = oi.org_id where ui.user_id = #{userId}")
	@ResultType(UserInfoSuperBank.class)
	UserInfoSuperBank selectDetail(@Param("userId")String userId);
	
	@Select("select ui.* from user_info ui where ui.user_code = #{userCode}")
	@ResultType(UserInfoSuperBank.class)
	UserInfoSuperBank selectDetailByUserNode(@Param("userCode")String userCode);
	
	/**
	 * 直接下级代理数量,成为代理的数量
	 * @param userCode
	 * @return
	 */
	@Select("select count(1) from user_info where top_one_code = #{userCode}  and user_type in('20','30','40') ")
	@ResultType(Integer.class)
	Integer selectAgentCount(@Param("userCode")String userCode);

	/**
	 * 直营用户数量,未成为代理的数量
	 * @param userCode
	 * @return
	 */
	@Select("select count(1) from user_info where top_one_code = #{userCode}  and (user_type = '10' or user_type is null or user_type = '')")
	@ResultType(Integer.class)
	Integer selectNotAgentCount(@Param("userCode")String userCode);
	/**
	 * 查询所有银行家组织
	 * @return
	 */
	@Select("select org_id, org_name from org_info where org_id = #{orgId}")
	@ResultType(OrgInfo.class)
	List<OrgInfo> getOrgInfoList(@Param("orgId")String orgId);
	
	/**
	 * 用户管理查询
	 * @param userInfoSuperBank
	 * @param entityId
	 * @param page
	 * @return
	 */
	@SelectProvider(type=SqlProvider.class,method="selectByCondions")
    @ResultType(UserInfoSuperBank.class)
	List<UserInfoSuperBank> selectByCondions(@Param("userInfoSuperBank")UserInfoSuperBank userInfoSuperBank,@Param("agentNo")String entityId,@Param("page")Page<UserInfoSuperBank> page);
	
	/**
	 * 用户管理导出
	 * @param userInfoSuperBank
	 * @param entityId
	 * @return
	 */
	@SelectProvider(type=SqlProvider.class,method="selectByCondions")
    @ResultType(UserInfoSuperBank.class)
	List<UserInfoSuperBank> exportUserInfoSuperBank(@Param("userInfoSuperBank")UserInfoSuperBank userInfoSuperBank, @Param("agentNo")String entityId);
	
    public class SqlProvider{
    	
    	public String selectUserInfoList(Map<String, Object> param){
            final String userCode = (String) param.get("userCode");
            SQL sql = new SQL(){{
                SELECT("user_code, user_name");
                FROM("user_info");
                if(StringUtils.isNotBlank(userCode)){
                    WHERE("(user_code like concat(#{userCode},'%') or user_name like concat(#{userCode},'%'))");
                }
                ORDER_BY("user_code limit 0,50");
            }};
            return sql.toString();
        }
    	
    	public String selectByCondions(Map<String,Object> param){
    		final UserInfoSuperBank info = (UserInfoSuperBank)param.get("userInfoSuperBank");
    		final String agentNo = (String)param.get("agentNo");
    		String sql = new SQL(){{
    			SELECT(" ui.user_id, ui.user_code,ui.org_id,ui.user_name,ui.nick_name,ui.phone,ui.weixin_code,ui.total_profit,ui.user_node,ui.status_agent,"
    					+ "ui.user_type,ui.pay_back,  ui.account_status,ui.create_date,ui.repayment_user_no,ui.oem_default_user,ui.open_province,ui.open_city,ui.open_region,"
    					+ "ui.receive_user_no,ui.top_one_code,ui.top_two_code,ui.top_three_code,oi.org_name,ai.total_amount,ui.second_user_node,ui.remark,"
    					+ "ui.account_status hasAccount,ui.toagent_date ");
    			FROM(" user_info ui ");
    			LEFT_OUTER_JOIN("org_info oi on ui.org_id = oi.org_id ");
    			LEFT_OUTER_JOIN("account_info ai on ai.user_code = ui.user_code ");
    			if (StringUtils.isNotBlank(info.getOneAgentPhone())) {
    				LEFT_OUTER_JOIN("user_info u1 on u1.user_code = ui.top_one_code ");
    			}
    			if (StringUtils.isNotBlank(info.getTwoAgentPhone())) {
    				LEFT_OUTER_JOIN("user_info u2 on u2.user_code = ui.top_two_code ");
    			}
    			if (StringUtils.isNotBlank(info.getThreeAgentPhone())) {
    				LEFT_OUTER_JOIN("user_info u3 on u3.user_code = ui.top_three_code ");
    			}
    			if (StringUtils.isNotBlank(info.getOneAgentPhone())) {
    				WHERE("u1.phone = #{userInfoSuperBank.oneAgentPhone}");
    			}
    			if (StringUtils.isNotBlank(info.getTwoAgentPhone())) {
    				WHERE("u2.phone = #{userInfoSuperBank.twoAgentPhone}");
    			}
    			if (StringUtils.isNotBlank(info.getThreeAgentPhone())) {
    				WHERE("u3.phone = #{userInfoSuperBank.threeAgentPhone}");
    			}
    			if (info.getUserId() != null) {
    				WHERE("ui.user_id = #{userInfoSuperBank.userId}");
				}
    			if (StringUtils.isNotBlank(info.getPhone())) {
    				WHERE("ui.phone = #{userInfoSuperBank.phone}");
    			}
    			if (info.getOrgId() != null) {
    				WHERE("ui.org_id = #{userInfoSuperBank.orgId}");
    			}
    			if (StringUtils.isNotBlank(info.getUserCode())) {
    				WHERE("ui.user_code = #{userInfoSuperBank.userCode}");
    			}
    			if (StringUtils.isNotBlank(info.getUserType())) {
    				WHERE("ui.user_type = #{userInfoSuperBank.userType}");
    			}
    			if (StringUtils.isNotBlank(info.getTopOneCode())) {
    				WHERE("ui.top_one_code = #{userInfoSuperBank.topOneCode}");
    			}
    			if (StringUtils.isNotBlank(info.getTopTwoCode())) {
    				WHERE("ui.top_two_code = #{userInfoSuperBank.topTwoCode}");
    			}
    			if (StringUtils.isNotBlank(info.getTopThreeCode())) {
    				WHERE("ui.top_three_code = #{userInfoSuperBank.topThreeCode}");
    			}
    			if (StringUtils.isNotBlank(info.getReceiveUserNo())) {
    				WHERE("ui.receive_user_no = #{userInfoSuperBank.receiveUserNo}");
    			}
    			if (StringUtils.isNotBlank(info.getRepaymentUserNo())) {
    				WHERE("ui.repayment_user_no = #{userInfoSuperBank.repaymentUserNo}");
    			}
    			if (info.getStartTime() != null) {
    				WHERE("ui.create_date >= #{userInfoSuperBank.startTime}");
    			}
    			if (info.getEndTime() != null) {
    				WHERE("ui.create_date <= #{userInfoSuperBank.endTime}");
    			}
    			if (info.getStartPayDate() != null) {
    				WHERE("ui.toagent_date >= #{userInfoSuperBank.startPayDate}");
    			}
    			if (info.getEndPayDate() != null) {
    				WHERE("ui.toagent_date <= #{userInfoSuperBank.endPayDate}");
    			}
    			if (StringUtils.isNotBlank(info.getPayBack())) {
    				WHERE("ui.pay_back = #{userInfoSuperBank.payBack}");
    			}
    			if (StringUtils.isNotBlank(info.getHasAccount())) {
    				WHERE("ui.account_status = #{userInfoSuperBank.hasAccount}");
    			}
    			if (StringUtils.isNotBlank(info.getUserName())) {
    				WHERE("ui.user_name = #{userInfoSuperBank.userName}");
    			}
    			if (StringUtils.isNotBlank(info.getSecondUserNode())) {
					WHERE("ui.second_user_node = #{userInfoSuperBank.secondUserNode}");
    			}
    			if (StringUtils.isNotBlank(info.getRemark())) {
    				WHERE("ui.remark = #{userInfoSuperBank.remark}");
    			}
    			if (StringUtils.isNotBlank(info.getOpenProvince())) {
    				WHERE("ui.open_province = #{userInfoSuperBank.openProvince}");
    			}
    			if (StringUtils.isNotBlank(info.getOpenCity())) {
    				WHERE("ui.open_city = #{userInfoSuperBank.openCity}");
    			}
    			if (StringUtils.isNotBlank(info.getOpenRegion())) {
    				WHERE("ui.open_region = #{userInfoSuperBank.openRegion}");
    			}
    			if (StringUtils.isNotBlank(info.getIsPay())) {
    				if ("1".equals(info.getIsPay())) {
    					WHERE("ui.user_type != '10'");
					}else{
						WHERE("ui.user_type = '10'");
					}
    			}
    			//查询当前代理商
    			if(StringUtils.isNotBlank(agentNo)){
    				WHERE("ui.org_id = #{agentNo}");
    			}
    			ORDER_BY(" ui.create_date desc ");
    		}}.toString();
    		System.out.println("用户管理查询sql=====>" + sql);
    		return sql;
    	}
    	
    	public String selectTotal(Map<String,Object> param){
    		final UserInfoSuperBank info = (UserInfoSuperBank)param.get("userInfoSuperBank");
    		final String agentNo = (String)param.get("agentNo");
    		String sql = new SQL(){{
    			SELECT(" count(1) num ,user_type userType");
    			FROM(" user_info ui ");
    			LEFT_OUTER_JOIN("org_info oi on ui.org_id = oi.org_id ");
    			LEFT_OUTER_JOIN("account_info ai on ai.user_code = ui.user_code ");
    			if (StringUtils.isNotBlank(info.getOneAgentPhone())) {
    				LEFT_OUTER_JOIN("user_info u1 on u1.user_code = ui.top_one_code ");
    			}
    			if (StringUtils.isNotBlank(info.getTwoAgentPhone())) {
    				LEFT_OUTER_JOIN("user_info u2 on u2.user_code = ui.top_two_code ");
    			}
    			if (StringUtils.isNotBlank(info.getThreeAgentPhone())) {
    				LEFT_OUTER_JOIN("user_info u3 on u3.user_code = ui.top_three_code ");
    			}
    			if (StringUtils.isNotBlank(info.getOneAgentPhone())) {
    				WHERE("u1.phone = #{userInfoSuperBank.oneAgentPhone}");
    			}
    			if (StringUtils.isNotBlank(info.getTwoAgentPhone())) {
    				WHERE("u2.phone = #{userInfoSuperBank.twoAgentPhone}");
    			}
    			if (StringUtils.isNotBlank(info.getThreeAgentPhone())) {
    				WHERE("u3.phone = #{userInfoSuperBank.threeAgentPhone}");
    			}
    			if (info.getUserId() != null) {
    				WHERE("ui.user_id = #{userInfoSuperBank.userId}");
    			}
    			if (StringUtils.isNotBlank(info.getPhone())) {
    				WHERE("ui.phone = #{userInfoSuperBank.phone}");
    			}
    			if (info.getOrgId() != null) {
    				WHERE("ui.org_id = #{userInfoSuperBank.orgId}");
    			}
    			if (StringUtils.isNotBlank(info.getUserCode())) {
    				WHERE("ui.user_code = #{userInfoSuperBank.userCode}");
    			}
    			if (StringUtils.isNotBlank(info.getUserType())) {
    				WHERE("ui.user_type = #{userInfoSuperBank.userType}");
    			}
    			if (StringUtils.isNotBlank(info.getTopOneCode())) {
    				WHERE("ui.top_one_code = #{userInfoSuperBank.topOneCode}");
    			}
    			if (StringUtils.isNotBlank(info.getTopTwoCode())) {
    				WHERE("ui.top_two_code = #{userInfoSuperBank.topTwoCode}");
    			}
    			if (StringUtils.isNotBlank(info.getTopThreeCode())) {
    				WHERE("ui.top_three_code = #{userInfoSuperBank.topThreeCode}");
    			}
    			if (StringUtils.isNotBlank(info.getReceiveUserNo())) {
    				WHERE("ui.receive_user_no = #{userInfoSuperBank.receiveUserNo}");
    			}
    			if (StringUtils.isNotBlank(info.getRepaymentUserNo())) {
    				WHERE("ui.repayment_user_no = #{userInfoSuperBank.repaymentUserNo}");
    			}
    			if (info.getStartTime() != null) {
    				WHERE("ui.create_date >= #{userInfoSuperBank.startTime}");
    			}
    			if (info.getEndTime() != null) {
    				WHERE("ui.create_date <= #{userInfoSuperBank.endTime}");
    			}
    			if (StringUtils.isNotBlank(info.getPayBack())) {
    				WHERE("ui.pay_back = #{userInfoSuperBank.payBack}");
    			}
    			if (StringUtils.isNotBlank(info.getHasAccount())) {
    				WHERE("ui.account_status = #{userInfoSuperBank.hasAccount}");
    			}
    			if (StringUtils.isNotBlank(info.getUserName())) {
    				WHERE("ui.user_name = #{userInfoSuperBank.userName}");
    			}
    			if (StringUtils.isNotBlank(info.getSecondUserNode())) {
    				WHERE("ui.second_user_node = #{userInfoSuperBank.secondUserNode}");
    			}
    			if (StringUtils.isNotBlank(info.getRemark())) {
    				WHERE("ui.remark = #{userInfoSuperBank.remark}");
    			}
    			if (StringUtils.isNotBlank(info.getOpenProvince())) {
    				WHERE("ui.open_province = #{userInfoSuperBank.openProvince}");
    			}
    			if (StringUtils.isNotBlank(info.getOpenCity())) {
    				WHERE("ui.open_city = #{userInfoSuperBank.openCity}");
    			}
    			if (StringUtils.isNotBlank(info.getOpenRegion())) {
    				WHERE("ui.open_region = #{userInfoSuperBank.openRegion}");
    			}
    			if (StringUtils.isNotBlank(info.getIsPay())) {
    				if ("1".equals(info.getIsPay())) {
    					WHERE("ui.user_type != '10'");
					}else{
						WHERE("ui.user_type = '10'");
					}
    			}
    			//查询当前代理商
    			if(StringUtils.isNotBlank(agentNo)){
    				WHERE("ui.org_id = #{agentNo}");
    			}
    			GROUP_BY (" ui.user_type ");
    			
    		}}.toString();
    		System.out.println("统计用户数量sql=====>" + sql);
    		return sql;
    	}
    }



}