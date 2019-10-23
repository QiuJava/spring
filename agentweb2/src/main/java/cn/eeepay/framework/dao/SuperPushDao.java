package cn.eeepay.framework.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import cn.eeepay.framework.model.*;
import cn.eeepay.framework.util.ReadOnlyDataSource;
import cn.eeepay.framework.util.WriteReadDataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;

@ReadOnlyDataSource
public interface SuperPushDao {
    /**
     * 分页查询微创业商户信息
     * @param superPush         查询条件
     * @param loginAgentInfo    登陆代理商
     * @param bpId              微创业无卡支付编号
     * @param page              分页信息
     * @return 查询结果
     */
    List<SuperPush> listSuperPushMerchant(@Param("bean") SuperPush superPush,
                                          @Param("loginAgentInfo") AgentInfo loginAgentInfo,
                                          @Param("bpId") String bpId, Page<SuperPush> page);

    /**
     * 根据用户id获取微创业信息
     * @param userId 用户id
     * @return 微创业基本信息
     */
    SuperPush getSuperPushUser(@Param("userId")String userId);

    /**
     * 查询商户的基本信息
     * @param merchantNo 商户编号
     * @return
     */
    MerchantInfo selectMerchantDetail(@Param("merchantNo") String merchantNo);

    /**
     * 根据商户获取机具信息
     * @param merchantNo 商户编号
     * @return 机具信息
     */
    List<Map<String,Object>> selectFromTerminalInfo(String merchantNo);

    /**
     * 根据商户获取结算信息
     * @param merchantNo 商户编号
     * @return 结算信息
     */
    MerchantCardInfo selectMerchantCardInfo(String merchantNo);

    /**
     * 分页查询超级分润信息
     * @param superPushShare    查询条件
     * @param loginAgentNo         登陆代理商agentNode
     * @param page              分页信息
     * @return
     */
    List<SuperPushShare> listSuperPushShare(@Param("bean") SuperPushShare superPushShare,
                                                 @Param("loginAgentNo") String loginAgentNo,
                                                 Page<SuperPushShare> page);

    /**
     * 统计超级分润信息
     * @param superPushShare    查询条件
     * @param loginAgentNo         登陆代理商agentNode
     * @return
     */
    Map<String,Object> countSuperPushShare(@Param("bean") SuperPushShare superPushShare,
                                           @Param("loginAgentNo") String loginAgentNo);
    /**
     * 统计微创业分润总金额
     * @param superPushShare 查询条件
     * @param loginAgentNo 登陆代理商node
     * @return 微创业分润总金额
     */
    BigDecimal countSuperPushTransAmount(@Param("bean")SuperPushShare superPushShare,
                                     @Param("loginAgentNo") String loginAgentNo);
    /**
     * 查询超级分润信息(用于导出)
     * @param superPushShare    查询条件
     * @param loginAgentNo         登陆代理商agentNode
     * @return
     */
    List<SuperPushShare> exportSuperPushShare(@Param("bean")SuperPushShare superPushShare,
                                              @Param("loginAgentNo")String loginAgentNo);

    /**
     * 获取登陆代理商今日微创业收益
     * @param loginAgentNo 登陆代理商
     * @return
     */
    String getTodaySuperPushAmount(@Param("loginAgentNo") String loginAgentNo);


//
//	@Select("select mi.merchant_no,mi.merchant_name,ai.agent_no,ai.agent_name from merchant_info mi left join agent_info ai on ai.agent_no=mi.agent_no "
//			+ " where mi.merchant_no=#{merchantNo}")
//	@ResultType(SuperPush.class)
//	SuperPush getCashMerchantDetail(@Param("merchantNo")String merchantNo);
//
//	@Select("select sum(result.sumShare) from "
//			+" (select sum(sps.self_share)sumShare from super_push_user spu"
//			+" LEFT JOIN super_push_share sps on sps.merchant_no=spu.merchant_no"
//			+" where spu.merchant_no=#{merchantNo}"
//			+" UNION ALL select sum(sps.one_share) sumShare from super_push_user spu"
//			+" LEFT JOIN super_push_share sps ON sps.merchant_no = spu.merchant_no WHERE spu.one_merchant_no = #{merchantNo} "
//			+" UNION ALL select sum(sps.two_share) sumShare from super_push_user spu"
//			+" LEFT JOIN super_push_share sps on sps.merchant_no=spu.merchant_no where spu.two_merchant_no=#{merchantNo} "
//			+" UNION ALL select sum(sps.three_share) sumShare from super_push_user spu"
//			+" LEFT JOIN super_push_share sps on sps.merchant_no=spu.merchant_no where spu.three_merchant_no=#{merchantNo}) result;")
//	@ResultType(BigDecimal.class)
//	BigDecimal getTotalAmount(@Param("merchantNo")String merchantNo);
//
//	@Select("select soi.settle_order,soi.create_time,soi.settle_amount,soi.settle_status from settle_order_info soi "
//			+ " where soi.settle_type=#{settleOrderInfo.settleType} and soi.sub_type=#{settleOrderInfo.subType}"
//			+ " and soi.settle_user_type=#{settleOrderInfo.settleUserType}"
//			+ " and soi.settle_user_no=#{settleOrderInfo.settleUserNo}")
//	@ResultType(SettleOrderInfo.class)
//	List<SettleOrderInfo> getCashPage(@Param("settleOrderInfo")SettleOrderInfo settleOrderInfo, Page<SuperPush> page);
//
//	@Select(" SELECT m.*,a.agent_name FROM merchant_info m left join agent_info a on m.agent_no =  a.agent_no WHERE merchant_no = #{mertId} ")
//	@ResultType(MerchantInfo.class)
//	MerchantInfo selectMerchantDetail(@Param("mertId")String mertId);
//
//	@Select(" SELECT spu.*, (SELECT merchant_name FROM merchant_info m WHERE m.merchant_no = spu.one_merchant_no)oneMerchantName, "
//			+ "(SELECT merchant_name FROM merchant_info m WHERE m.merchant_no = spu.two_merchant_no)twoMerchantName, "
//			+ "(SELECT merchant_name FROM merchant_info m WHERE m.merchant_no = spu.three_merchant_no)threeMerchantName "
//			+ "FROM super_push_user spu "
//			+ "LEFT JOIN merchant_info mi ON spu.merchant_no = mi.merchant_no "
//			+ "WHERE spu.merchant_no = #{mertId} ")
//	@ResultType(SuperPush.class)
//	SuperPush selectFromSuperPushUser(@Param("mertId")String mertId);
//
//	@Select(" SELECT ti.SN,bpd.bp_name bpName FROM terminal_info ti LEFT JOIN business_product_define bpd ON ti.bp_id = bpd.bp_id WHERE ti.merchant_no = #{mertId} ")
//	@ResultType(List.class)
//	List<Map<String,Object>> selectFromTerminalInfo(@Param("mertId")String mertId);
//
//	//提现详情
//	@Select(" SELECT * FROM merchant_card_info WHERE merchant_no = #{mertId} and def_settle_card = '1' ")
//	@ResultType(MerchantCardInfo.class)
//	MerchantCardInfo selectMerchantCardInfo(@Param("mertId")String mertId);
//
//	//分润详情
//	@SelectProvider(type=SqlProvider.class,method="selectShareDetail")
//	List<SuperPushShare> selectShareDetail(@Param("page")Page<SuperPush> page, @Param("superPushShare")SuperPushShare superPushShare);
//	//分润详情导出
//	@SelectProvider(type=SqlProvider.class,method="exportTransInfo")
//	List<SuperPushShare> exportTransInfo(@Param("superPushShare")SuperPushShare superPushShare);
//
//	@Select(" SELECT mi.merchant_no,mi.merchant_name,mi.agent_no,(SELECT agent_name FROM agent_info WHERE agent_no = mi.agent_no) agent_name,"
//			+ "spu.one_merchant_no,spu.two_merchant_no,spu.three_merchant_no,sps.totalAmount,mci.bank_name,mci.account_no,mci.account_name,mci.cnaps_no "
//			+ "FROM merchant_info mi "
//			+ "LEFT JOIN super_push_user spu ON mi.merchant_no = spu.merchant_no "
//			+ "LEFT JOIN (select SUM(self_share) totalAmount,merchant_no "
//			+ "FROM super_push_share GROUP BY merchant_no) sps on sps.merchant_no = spu.merchant_no "
//			+ "LEFT JOIN merchant_card_info mci ON mi.merchant_no = mci.merchant_no "
//			+ "WHERE mi.merchant_no = #{mertId} ")
//	@ResultType(Map.class)
//	Map<String,Object> selectCashDetail(@Param("mertId")String mertId);
//
//	@SelectProvider(type = SqlProvider.class, method = "selectSuperPush")
//	@ResultType(SuperPush.class)
//	List<SuperPush> selectSuperPush(@Param("page") Page<SuperPush> page,@Param("phone")String phone,@Param("superPush")SuperPush superPush);
//
//	public class SqlProvider {
//
//		public String selectSuperPush(Map<String, Object> param) {
//			final SuperPush superPush = (SuperPush) param.get("superPush");
//			String sql = new SQL() {
//				{
//					SELECT(" sps.totalAmount,spu.*,ai.agent_name,ai.one_level_id,(SELECT agent_name FROM agent_info WHERE agent_no = ai.one_level_id) oneAgentName,"
//							+ "mi0.merchant_name,mi0.mobilephone");
//					FROM("super_push_user spu ");
//					INNER_JOIN("merchant_info mi0 on mi0.merchant_no=spu.merchant_no ");
//					INNER_JOIN("agent_info ai on mi0.agent_no = ai.agent_no ");
//					LEFT_OUTER_JOIN("merchant_info mi1 on spu.one_merchant_no=mi1.merchant_no ");
//					LEFT_OUTER_JOIN("merchant_info mi2 on spu.two_merchant_no=mi2.merchant_no ");
//					LEFT_OUTER_JOIN("merchant_info mi3 on spu.three_merchant_no=mi3.merchant_no ");
//					LEFT_OUTER_JOIN("(select SUM(self_share) totalAmount,merchant_no "
//							+ "from super_push_share GROUP BY merchant_no) sps on sps.merchant_no=spu.merchant_no ");
//					WHERE(" EXISTS ( SELECT 1  FROM agent_info a WHERE a.mobilephone = #{phone} "
//							+ "AND ai.agent_node LIKE CONCAT(a.agent_node,'%')) ");//根据手机号找到节点,然后根据节点来判断下级
//					if (StringUtils.isNotBlank(superPush.getMobilephone())) {
//						WHERE(" mi0.mobilephone=#{superPush.mobilephone} ");
//					}
//					if (StringUtils.isNotBlank(superPush.getMerchantNo())) {
//						WHERE(" (mi0.merchant_no=#{superPush.merchantNo} or mi0.merchant_name like concat(#{superPush.merchantNo},'%'))");
//					}
//					if (StringUtils.isNotBlank(superPush.getAgentNo())) {
//						WHERE(" (ai.agent_no=#{superPush.agentNo} or ai.agent_name like concat(#{superPush.agentNo},'%'))");
//					}
//					if (StringUtils.isNotBlank(superPush.getOneLevelId())) {
//						WHERE(" (ai.one_level_id=#{superPush.oneLevelId} or ai.agent_name like concat(#{superPush.oneLevelId},'%'))");
//					}
//					if (StringUtils.isNotBlank(superPush.getOneMerchantNo())) {
//						WHERE(" (mi1.merchant_no=#{superPush.oneMerchantNo} or mi1.merchant_name like concat(#{superPush.oneMerchantNo},'%'))");
//					}
//					if (StringUtils.isNotBlank(superPush.getTwoMerchantNo())) {
//						WHERE(" (mi2.merchant_no=#{superPush.twoMerchantNo} or mi2.merchant_name like concat(#{superPush.twoMerchantNo},'%'))");
//					}
//					if (StringUtils.isNotBlank(superPush.getThreeMerchantNo())) {
//						WHERE(" (mi3.merchant_no=#{superPush.threeMerchantNo} or mi3.merchant_name like concat(#{superPush.threeMerchantNo},'%'))");
//					}
//					ORDER_BY("spu.create_time desc");
//				}
//			}.toString();
//			return sql;
//		}
//
//		public String selectShareDetail(Map<String,Object> param) {
//			final SuperPushShare superPushShare = (SuperPushShare) param.get("superPushShare");
//			return new SQL(){
//				{
//					SELECT(" m.merchant_no,m.merchant_name,t.trans_time,t.level,t.share,t.rule,t.amount ");
//					FROM("(SELECT u.merchant_no,s.trans_time,0 as level,s.self_share share,s.self_rule rule,s.amount "
//							+ "FROM super_push_user u ,super_push_share s WHERE u.merchant_no=s.merchant_no AND u.merchant_no=#{superPushShare.mertId}  "
//							+ "UNION ALL SELECT u.merchant_no,s.trans_time,1 as level,s.one_share share,s.one_rule rule,s.amount "
//							+ "FROM super_push_user u ,super_push_share s WHERE u.merchant_no=s.merchant_no AND u.one_merchant_no=#{superPushShare.mertId} "
//							+ "UNION ALL SELECT u.merchant_no,s.trans_time,2 as level,s.two_share share,s.two_rule rule,s.amount "
//							+ "FROM super_push_user u ,super_push_share s WHERE u.merchant_no=s.merchant_no AND u.two_merchant_no=#{superPushShare.mertId} "
//							+ "UNION ALL SELECT u.merchant_no,s.trans_time,3 as level,s.three_share share,s.three_rule rule,s.amount "
//							+ "FROM super_push_user u ,super_push_share s WHERE u.merchant_no=s.merchant_no AND u.three_merchant_no=#{superPushShare.mertId})  t ");
//					LEFT_OUTER_JOIN("merchant_info m ON m.merchant_no = t.merchant_no  ");
//					if (StringUtils.isNotBlank(superPushShare.getsDate())) {
//						WHERE(" t.trans_time >= #{superPushShare.sDate}");
//					}
//					if (StringUtils.isNotBlank(superPushShare.geteDate())) {
//						WHERE(" t.trans_time <= #{superPushShare.eDate}");
//					}
//					ORDER_BY(" t.trans_time desc");
//				}
//			}.toString();
//		}
//		public String exportTransInfo(Map<String,Object> param) {
//			final SuperPushShare superPushShare = (SuperPushShare) param.get("superPushShare");
//			return new SQL(){
//				{
//					SELECT(" m.merchant_no,m.merchant_name,t.trans_time,t.level,t.share,t.rule,t.amount ");
//					FROM("(SELECT u.merchant_no,s.trans_time,0 as level,s.self_share share,s.self_rule rule,s.amount "
//							+ "FROM super_push_user u ,super_push_share s WHERE u.merchant_no=s.merchant_no AND u.merchant_no=#{superPushShare.mertId}  "
//							+ "UNION ALL SELECT u.merchant_no,s.trans_time,1 as level,s.one_share share,s.one_rule rule,s.amount "
//							+ "FROM super_push_user u ,super_push_share s WHERE u.merchant_no=s.merchant_no AND u.one_merchant_no=#{superPushShare.mertId} "
//							+ "UNION ALL SELECT u.merchant_no,s.trans_time,2 as level,s.two_share share,s.two_rule rule,s.amount "
//							+ "FROM super_push_user u ,super_push_share s WHERE u.merchant_no=s.merchant_no AND u.two_merchant_no=#{superPushShare.mertId} "
//							+ "UNION ALL SELECT u.merchant_no,s.trans_time,3 as level,s.three_share share,s.three_rule rule,s.amount "
//							+ "FROM super_push_user u ,super_push_share s WHERE u.merchant_no=s.merchant_no AND u.three_merchant_no=#{superPushShare.mertId})  t ");
//					LEFT_OUTER_JOIN("merchant_info m ON m.merchant_no = t.merchant_no  ");
//					if (StringUtils.isNotBlank(superPushShare.getsDate())) {
//						WHERE(" t.trans_time >= #{superPushShare.sDate}");
//					}
//					if (StringUtils.isNotBlank(superPushShare.geteDate())) {
//						WHERE(" t.trans_time <= #{superPushShare.eDate}");
//					}
//					ORDER_BY(" t.trans_time desc");
//				}
//			}.toString();
//		}
//	}


}
