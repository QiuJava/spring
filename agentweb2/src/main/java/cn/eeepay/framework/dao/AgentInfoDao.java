package cn.eeepay.framework.dao;

import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.eeepay.framework.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentActivity;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.AgentShareRule;
import cn.eeepay.framework.model.AgentShareRuleTask;
import cn.eeepay.framework.model.AgentUserEntity;
import cn.eeepay.framework.model.AgentUserInfo;
import cn.eeepay.framework.model.JoinTable;
import cn.eeepay.framework.model.ServiceQuota;
import cn.eeepay.framework.model.ServiceRate;
import cn.eeepay.framework.model.UserInfo;
import cn.eeepay.framework.util.WriteReadDataSource;

@WriteReadDataSource
public interface AgentInfoDao {


    @Select(" SELECT * FROM out_account_service where service_type = #{serviceType} and out_account_status = '1' order by level ")
    @ResultType(List.class)
    List<Map<String, Object>> selectByServiceType(@Param("serviceType") String serviceType);

    @Select("select count(1) from user_info where mobilephone=#{agent.mobilephone} and team_id=#{agent.teamId}")
    public int existUserByMobilephoneAndTeamId(@Param("agent") AgentInfo agent);

    @Select("select count(1) from user_info where email=#{agent.email} and team_id=#{agent.teamId}")
    int existUserByEmailAndTeamId(@Param("agent") AgentInfo agent);

    //tgh331欢乐送代理商补贴提现
    @Insert("insert settle_order_info(create_time,settle_type,source_system,create_user,settle_user_type,settle_user_no,settle_status"
            + ",syn_status,settle_order_status,settle_amount,agent_node,holidays_mark,acq_enname,source_order_no,source_batch_no,sub_type,per_agent_fee) "
            + "values(#{map.createTime},#{map.settleType},#{map.sourceSystem},#{map.createUser},#{map.settleUserType},#{map.settleUserNo},#{map.settleStatus},"
            + "#{map.synStatus},#{map.settleOrderStatus},#{map.settleAmount},#{map.agentNode},#{map.holidaysMark},#{map.acqenname},#{map.sourceOrderNo},"
            + "#{map.sourceBatchNo},#{map.subType},#{map.perAgentFee})")
    @SelectKey(statement = "select LAST_INSERT_ID()", keyProperty = "map.settle_order", before = false, resultType = Long.class)
    public int insertWithDrawCash(@Param("map") Map<String, Object> map);

    @Select("select agent_no,agent_node from agent_info where status='1' and agent_no=#{agentNo}")
    @ResultType(AgentInfo.class)
    AgentInfo selectByName(@Param("agentNo") String agentNo);

    @Select("select * from agent_info where status='1' and "
            + "agent_node like (select CONCAT(agent_node,'%') from agent_info where agent_no=#{agentNo})")
    @ResultType(AgentInfo.class)
    List<AgentInfo> selectAllInfo(@Param("agentNo") String agentNo);

    @Select("select * from agent_info where agent_no=#{agentNo}")
    @ResultType(AgentInfo.class)
    AgentInfo selectByAgentNo(@Param("agentNo") String agentNo);

    @Select("select * from agent_info where agent_node = #{agentNode}")
    @ResultType(AgentInfo.class)
    AgentInfo selectByAgentNode(@Param("agentNode") String agentNode);

    @Select("select * from agent_info where parent_id=#{parentId}")
    @ResultType(AgentInfo.class)
    AgentInfo selectByParentId(@Param("parentId") String parentId);


    @Select("select * from agent_info where parent_id=#{parentId}")
    @ResultType(AgentInfo.class)
    List<AgentInfo> selectAgentByParentId(@Param("parentId") String parentId);

    /**
     * 查找所有的一级代理商：名称 + 编号（关联表中需要用到）
     */
//    @SelectProvider(type=SqlProvider.class, method="selectOneAgentName")
// // @Select("select agent_no,agent_name from agent_info where status='1' and agent_level='1'")
//    @ResultType(AgentInfo.class)
//	List<AgentInfo> selectOneAgentName(@Param("agentName")String agentName);

    //新增代理商的用户
    @Insert("insert into user_info(user_id,user_name,mobilephone,status,password,team_id,create_time,email) values(#{agent.userId},#{agent.userName},"
            + "#{agent.mobilephone},1,#{agent.password},#{agent.teamId},now(),#{agent.email})")
    @SelectKey(statement = "select LAST_INSERT_ID()", keyProperty = "agent.id", before = false, resultType = Long.class)
    public int insertAgentUser(@Param("agent") AgentUserInfo agent);

    //新增代理商的结构组织
    @Insert("insert into user_entity_info(user_id,user_type,entity_id,apply,manage,status,last_notice_time,is_agent) values(#{agent.userId},1,#{agent.entityId},1,1,1,now(),#{agent.isAgent})")
    @SelectKey(statement = "select LAST_INSERT_ID()", keyProperty = "agent.id", before = false, resultType = Long.class)
    public int insertAgentEntity(@Param("agent") AgentUserEntity agent);

    //给代理商授权管理员授
    @Insert("insert into agent_user_role(user_id,role_id) values(#{userId},5)")
    public int insertAgentRole(Long userId);

    /**
     * 查询非直营的一级代理商
     *
     * @return
     */
    @Select("select agent_no,agent_name from agent_info a left join team_info t on a.team_id=t.team_id "
            + " where a.status='1' and a.agent_level='1' and t.team_type='2'")
    @ResultType(AgentInfo.class)
    List<AgentInfo> selectOneAgent();

    /**
     * 根据代理商编号查询该代理商下所有的子代理商
     *
     * @param agentLevel
     * @return
     */
    @SelectProvider(type = SqlProvider.class, method = "selectChildAgentByAgentNode")
    @ResultType(AgentInfo.class)
    List<AgentInfo> selectChildAgentByAgentNode(@Param("agentNode") String agentNode, @Param("agentLevel") Integer agentLevel);

    /**
     * 根据代理商ID查询名称
     *
     * @param oemId by tans
     * @return
     */
    @Select("select agent_name from agent_info where agent_no=#{id}")
    @ResultType(java.lang.String.class)
    String selectNameById(@Param("id") String oemId);

    //    @Select("SELECT a.*,s.service_name FROM agent_share_rule a LEFT JOIN service_info s ON a.service_id=s.service_id WHERE a.agent_no=#{agentNo}")
    //313xy
//    @Select("SELECT a.link_service,si2.service_type AS service_type2,a.service_type,a.bp_name,a.service_name, a.service_id,a.card_type, a.holidays_mark,a.bp_id, asr.* "
//    		+ " FROM ( SELECT s.link_service, s.service_type,abd.bp_name, abp.agent_no, abp.bp_id, bpi.service_id, s.service_name, r1.card_type,  r1.holidays_mark "
//    		+ "FROM agent_business_product abp, business_product_info bpi, service_info s, agent_info ai, agent_share_rule r1,business_product_define abd "
//    		+ "WHERE abp.agent_no = #{agentNo} AND abp.bp_id = abd.bp_id AND abp.bp_id = bpi.bp_id AND ai.agent_no = abp.agent_no AND bpi.service_id = s.service_id AND abp. STATUS = '1' "
//    		+ "AND r1.agent_no = ai.one_level_id AND r1.service_id = s.service_id ) a "
//			+ "LEFT JOIN service_info si2 ON  si2.link_service = a.service_id "
//    		+ "LEFT JOIN agent_share_rule asr ON a.agent_no = asr.agent_no AND a.service_id = asr.service_id and a.card_type = asr.card_type "
//    		+ "and a.holidays_mark = asr.holidays_mark  " +
//			"ORDER BY a.bp_id, IFNULL(a.link_service, a.service_id), a.service_type DESC")
//    @ResultType(AgentShareRule.class)
    List<AgentShareRule> getAgentShareInfos(@Param("bpIds") List<String> bpIds, @Param("agentNo") String agentNo);

    @SelectProvider(type = SqlProvider.class, method = "getServiceRate")
    @ResultType(ServiceRate.class)
    List<ServiceRate> getServiceRate(@Param("bpIds") List<Integer> bpIds, @Param("agentNo") String agentId);

    @SelectProvider(type = SqlProvider.class, method = "getServiceQuota")
    @ResultType(ServiceQuota.class)
    List<ServiceQuota> getServiceQuota(@Param("bpIds") List<Integer> bpIds, @Param("agentNo") String agentId);

    @Insert("insert into agent_info(agent_no,agent_node,agent_name,agent_level,parent_id,one_level_id,is_oem,team_id,email,phone,cluster,"
            + "agent_area,mobilephone,link_name,id_card_no,address,account_name,account_type,account_no,bank_name,cnaps_no,sale_name,creator,"
            + "status,create_date,province,city,area,count_level,account_province,account_city,agent_oem,agent_type,agent_share_level) values("
            + "#{agent.agentNo},#{agent.agentNode},#{agent.agentName},#{agent.agentLevel},#{agent.parentId},#{agent.oneLevelId},#{agent.isOem},#{agent.teamId},"
            + "#{agent.email},#{agent.phone},#{agent.cluster},#{agent.agentArea},#{agent.mobilephone},#{agent.linkName},#{agent.idCardNo},"
            + "#{agent.address},#{agent.accountName},#{agent.accountType},#{agent.accountNo},#{agent.bankName},#{agent.cnapsNo},#{agent.saleName},#{agent.creator},"
            + "#{agent.status},now(),#{agent.province},#{agent.city},#{agent.area},#{agent.countLevel},#{agent.accountProvince},#{agent.accountCity},"
            + "#{agent.agentOem},#{agent.agentType},#{agent.agentShareLevel})")
    @SelectKey(statement = "select LAST_INSERT_ID()", keyProperty = "agent.id", before = false, resultType = Long.class)
    public int insertAgentInfo(@Param("agent") AgentInfo agent);

    @InsertProvider(type = SqlProvider.class, method = "insertAgentProductList")
    int insertAgentProductList(@Param("list") List<JoinTable> bp);

    @InsertProvider(type = SqlProvider.class, method = "insertAgentShareList")
    int insertAgentShareList(@Param("list") List<AgentShareRule> shareList);

    @InsertProvider(type = SqlProvider.class, method = "insertAgentActivity")
    int insertAgentActivity(@Param("list") List<AgentActivity> happyBackList);

    @SelectProvider(type = SqlProvider.class, method = "queryAgentInfoList")
    @ResultType(AgentInfo.class)
    List<AgentInfo> queryAgentInfoList(@Param("params") Map<String, Object> params, @Param("page") Page<AgentInfo> page);

    @Select("select * from  user_info where mobilephone=#{mobilephone} and team_id=#{teamId}")
    @ResultType(AgentUserInfo.class)
    AgentUserInfo selectAgentUser(@Param("mobilephone") String mobilephone, @Param("teamId") String teamId);

    @Select("select * from  user_entity_info where user_id=#{userId} and entity_id=#{agentNo}")
    @ResultType(AgentUserEntity.class)
    AgentUserEntity selectAgentUserEntity(@Param("userId") String userId, @Param("agentNo") String agentNo);

    @SelectProvider(type = SqlProvider.class, method = "existAgentByMobilephoneAndTeamId")
    int existAgentByMobilephoneAndTeamId(@Param("agent") AgentInfo agent);

    /**
     * 根据业务产品，查询同组业务产品的所有除提现外的服务
     *
     * @param rule
     * @return
     * @author tans
     * @date 2017年4月15日 下午4:39:32
     */
    @Select("SELECT smr.service_id FROM service_info si,business_product_info bpi,service_manage_rate smr,agent_business_product abp,"
            + "(SELECT bpg1.bp_id FROM business_product_group bpg1,business_product_group bpg2 "
            + " WHERE bpg1.group_no = bpg2.group_no AND bpg1.bp_id != #{bpId} AND bpg2.bp_id = #{bpId}"
            + " ) bpids "
            + " WHERE bpi.bp_id = bpids.bp_id AND bpi.service_id = si.service_id "
            + " AND abp.bp_id=bpi.bp_id"
            + " AND abp.agent_no=#{agentNo}"
            + " AND smr.service_id=si.service_id"
            + " AND smr.agent_no=0"
            + " AND si.service_type=#{serviceType}"
            + " AND smr.card_type=#{cardType}"
            + " AND smr.holidays_mark=#{holidaysMark};")
    @ResultType(ServiceRate.class)
    List<ServiceRate> getOtherServiceRate(AgentShareRule rule);

    /**
     * 根据业务产品，查询同组业务产品的所有提现服务
     *
     * @param rule
     * @return
     * @author tans
     * @date 2017年4月25日 上午9:28:31
     */
    @Select("SELECT si3.service_id FROM"
            + " service_info si1,"
            + " business_product_info bi,"
            + " business_product_group bg1,"
            + " business_product_group bg2,"
            + " business_product_info bi2,"
            + " service_info si2,"
            + " service_manage_rate smr1,"
            + " service_info si3,"
            + " agent_business_product abp"
            + " WHERE"
            + " si1.link_service = #{serviceId}"
            + " AND si1.service_id = bi.service_id"
            + " AND bi.bp_id = bg1.bp_id"
            + " AND bg1.group_no = bg2.group_no"
            + " AND bg1.bp_id <> bg2.bp_id"
            + " AND abp.bp_id = bg2.bp_id"
            + " AND abp.agent_no = #{agentNo}"
            + " AND bg2.bp_id = bi2.bp_id"
            + " AND bi2.service_id = si2.service_id"
            + " AND si2.service_type = si1.service_type"
            + " AND si2.link_service = smr1.service_id"
            + " AND smr1.card_type = #{cardType}"
            + " AND smr1.holidays_mark = #{holidaysMark}"
            + " AND smr1.agent_no = 0"
            + " AND si3.service_id = si2.link_service"
            + " AND si3.service_type = #{serviceType}")
    @ResultType(ServiceRate.class)
    List<ServiceRate> getOtherTXServiceRate(AgentShareRule rule);

    @Select("SELECT IF(COUNT(1) > 0, TRUE, FALSE) FROM agent_info WHERE agent_no = #{childrenAgent} AND parent_id = #{parentAgentNo}")
    boolean isDirectSubordinate(@Param("parentAgentNo") String parentAgentNo, @Param("childrenAgent") String childrenAgent);

    @Update("UPDATE agent_info " +
            "SET profit_switch = 0 " +
            "WHERE  agent_node LIKE #{parentAgentNode}")
    void updateChildAndSelfProfitSwitch2off(@Param("parentAgentNode") String parentAgentNode);

    @Update("UPDATE agent_info " +
            "SET profit_switch = 1 " +
            "WHERE agent_no = #{agentNo}")
    void updateProfitSwitch2on(@Param("agentNo") String agentNo);

    /**
     * 根据业务产品id,获取到分润信息
     *
     * @param bpIds 已经挑选出队长和可单独申请的业务产品id
     * @return 分润规则信息集合
     */
    List<ServiceRate> getNewAgentServicesByBpId(@Param("bpIds") List<String> bpIds);


    /**
     * 根据选中的业务产品集合一级父代理商编号,获取队长或者可独立申请的业务产品id
     *
     * @param bpIds         需要挑选的业务产品id
     * @param parentAgentNO 上级代理商编号
     * @return 队长或者可独立申请的业务产品id集合
     */
    List<String> getLearderOrIndividualBpId(@Param("bpIds") List<String> bpIds, @Param("parentAgentNo") String parentAgentNO);

    /**
     * 根据自身所代理的业务产品,获取队长或者可独立申请的业务产品id
     *
     * @param agentNo 代理商编号
     * @return 队长或者可独立申请的业务产品id集合
     */
    List<String> getLearderOrIndividualBpIdBySelf(@Param("agentNo") String agentNo);

    /**
     * 新增队员的服务Id
     *
     * @param agentShareRules  分润规则
     */
    void insertMemberAgentShare(@Param("rules") List<AgentShareRule> agentShareRules);

    /**
     * 根据队长的业务产品获取队员的分润信息
     *
     * @param leader  队长的业务id
     * @param member  队员的业务id
     * @param agentNo 需要新增分润的代理商编号
     */
    List<AgentShareRule> listMemberAgentShareByLeader(@Param("leader") String leader,
                                                      @Param("member") String member,
                                                      @Param("agentNo") String agentNo);

    /**
     * 获取同样类型的父级代理商的分润规则
     *
     * @param childrenRule 下级的代理商的分润规则
     * @return 上级代理商同样类型的分润规则
     */
    AgentShareRule getSameTypeParentAgentShare(@Param("childrenRule") AgentShareRule childrenRule);

    /**
     * 判断分润开关是否打开
     *
     * @param agentNo 代理商no
     * @return
     */
    boolean profitSwithIsOpen(@Param("agentNo") String agentNo);

    /**
     * 获取同样类型的顶级代理商的最小服务费率
     *
     * @param rule       分润规则
     * @param oneLevelId 顶级代理商
     * @return
     */
    Map<String, Object> getSameTypeRootAgentMinServiceRate(@Param("rule") AgentShareRule rule, @Param("oneLevelId") String oneLevelId, @Param("agentNo") String agentNo);

    /**
     * 获取同样类型的顶级代理商的最大服务费率
     *
     * @param rule       分润规则
     * @param oneLevelId 顶级代理商
     * @return
     */
    Map<String, Object> getSameTypeRootAgentMaxServiceRate(@Param("rule") AgentShareRule rule, @Param("oneLevelId") String oneLevelId, @Param("agentNo") String agentNo);


    /**
     * 获取同样类型的顶级代理商的最小服务费率
     *
     * @param rule       分润规则
     * @param oneLevelId 顶级代理商
     * @return
     */
    Map<String, Object> getSameTypeRootAgentMinServiceRate(@Param("rule") AgentShareRuleTask rule, @Param("oneLevelId") String oneLevelId, @Param("agentNo") String agentNo);

    /**
     * 查询代理商的业务产品拥有多少条未生效的分润记录
     *
     * @param bpId    业务产品id
     * @param agentNo 代理商编号
     * @return
     */
    int countHasNotEfficientRule(@Param("bpId") int bpId, @Param("agentNo") String agentNo);

    /**
     * 判断 bpId 是否为自定义的业务业务产品
     * 必须在某一个组(business_product_group)里,且不能单独申请
     *
     * @param bpId 业务产品id
     * @return
     */
    int countUserDefinedBusinessProduct(@Param("bpId") String bpId);


    int closeAgentProStatus(@Param("agentNode") String agentNode, @Param("bpId") String bpId);

    @Update("UPDATE agent_business_product " +
            " SET STATUS = 1 " +
            " WHERE agent_no = #{agentNo} " +
            " AND bp_Id = #{bpId} ")
    int openAgentProStatus(@Param("agentNo") String agentNo, @Param("bpId") String bpId);

    /**
     * 汇总分润成本大于服务费率的分润条数
     *
     * @param agentNo 代理商编号
     * @param bpId    业务产品id
     * @return
     */
    int countShareDateMoreThanServiceRate(@Param("agentNo") String agentNo, @Param("bpId") String bpId);

    /**
     * 判断childAgentNo是否为parentAgentNo的下属代理商
     *
     * @param parentAgentNo 父代理商
     * @param childAgentNo  子代理商
     * @return
     */
    boolean isSubordinate(@Param("parentAgentNo") String parentAgentNo, @Param("childAgentNo") String childAgentNo);

    /**
     * 查询该商户的是否属于登陆的代理商的商户
     *
     * @param merchantBpId 商户进件Id
     * @param loginAgentNo 登陆代理商
     * @return
     */
    boolean merchantIsBelongToAgent(@Param("merchantBpId") String merchantBpId, @Param("loginAgentNo") String loginAgentNo);

    /**
     * 查询该商户的是否属于登陆的代理商的直营商户,且审核状态为失败的
     *
     * @param merchantBpId 商户进件Id
     * @param loginAgentNo 登陆代理商
     * @return
     */
    boolean merchantIsDirectBelongToAgent(@Param("merchantBpId") String merchantBpId, @Param("loginAgentNo") String loginAgentNo);

    /**
     * 查询该机具的是否属于登陆的代理商的商户
     *
     * @param terminalId   商户进件Id
     * @param loginAgentNo 登陆代理商
     * @return
     */
    boolean terminalIsBelongToAgent(@Param("terminalId") String terminalId, @Param("loginAgentNo") String loginAgentNo);

    /**
     * 将bpId同组的业务产品都设置为非默认
     *
     * @param bpId         业务产品
     * @param loginAgentNo 登陆代理商
     */
    void updateDefaultFlagGroup2Off(@Param("bpId") String bpId, @Param("loginAgentNo") String loginAgentNo);

    /**
     * 将bpId的业务产品都设置为默认
     *
     * @param bpId         业务产品
     * @param loginAgentNo 登陆代理商
     */
    void updateDefaultFlag2On(@Param("bpId") String bpId, @Param("loginAgentNo") String loginAgentNo);

    /**
     * 将业务产品集合中队长和普通业务设置为默认
     *
     * @param bps     业务产品集合
     * @param agentNo 代理商
     */
    void updateDefaultFlagLeader2On(@Param("bps") List<Integer> bps, @Param("agentNo") String agentNo);

    /**
     * 判断代理商推广功能是否打开
     *
     * @param agentNo 代理商no
     * @return
     */
    @Select("SELECT promotion_switch FROM agent_info WHERE agent_no = #{agentNo}")
    @ResultType(Boolean.class)
    boolean promotionSwitchIsOpen(@Param("agentNo") String agentNo);

    @Select("SELECT cash_back_switch FROM agent_info WHERE agent_no = #{agentNo}")
    @ResultType(Boolean.class)
    boolean cashBackSwitchIsOpen(@Param("agentNo") String agentNo);


    @Update("UPDATE agent_info " +
            "SET promotion_switch = 0 " +
            "WHERE  agent_node LIKE #{parentAgentNode}")
    void updateChildAndSelfPromotionSwitch2off(@Param("parentAgentNode") String parentAgentNode);

    @Update("UPDATE agent_info " +
            "SET cash_back_switch = 0 " +
            "WHERE  agent_node LIKE #{parentAgentNode}")
    void updateChildAndSelfCashBackSwitch2off(@Param("parentAgentNode") String parentAgentNode);

    @Update("UPDATE agent_info " +
            "SET promotion_switch = 1 " +
            "WHERE agent_no = #{agentNo}")
    void updatePromotionSwitch2on(@Param("agentNo") String agentNo);

    @Update("UPDATE agent_info " +
            "SET cash_back_switch = 1 " +
            "WHERE agent_no = #{agentNo}")
    void updateCashBackSwitch2on(@Param("agentNo") String agentNo);

    @Select("SELECT oem_type FROM agent_oem_info WHERE one_agent_no = #{oneLevelId}")
    String selectOneAgentOemType(@Param("oneLevelId") String oneLevelId);

    @Select("SELECT COUNT(1) FROM service_info si \n" +
            "WHERE CURRENT_TIME BETWEEN si.trad_Start AND si.trad_end\n" +
            "AND CURRENT_DATE BETWEEN si.use_starttime AND si.use_endtime\n" +
            "AND  si.service_id = #{serviceId}")
    boolean canWithdrawCash(Long serviceId);

    @Select("SELECT ai.agent_no,agent_name FROM agent_info ai WHERE mobilephone=#{mobilephone} and team_id=#{teamId}")
    AgentInfo selectByMobilephone(@Param("mobilephone") String mobilephone, @Param("teamId") String teamId);

    @Update("UPDATE agent_activity SET `status`=#{status} WHERE id=#{id}")
    int updateAgentActivityStatus(@Param("id") Long id, @Param("status")boolean status);

    @Update("UPDATE agent_activity SET `status`=#{status} WHERE agent_node like concat(#{agentNode},'%') AND activity_type_no=#{activityTypeNo}")
    int updateAgentActivityStatusByAgentNode(@Param("agentNode")String agentNode, @Param("status")boolean status, @Param("activityTypeNo") String activityTypeNo);

    @Select("SELECT * FROM agent_activity WHERE agent_no=#{agentNo} AND activity_type_no=#{activityTypeNo}")
    @ResultType(AgentActivity.class)
    AgentActivity selectByAgentNoAndActivityType(@Param("agentNo") String agentNo, @Param("activityTypeNo") String activityTypeNo);

    @Select("SELECT activity_type_no FROM agent_activity WHERE agent_no=#{agentNo}")
    @ResultType(String.class)
    List<String> selectActivityTypeNos(@Param("agentNo") String agentNo);

    @Select("SELECT activity_type_no FROM agent_activity WHERE agent_no=#{agentNo} and status=#{status}")
    @ResultType(String.class)
    List<String> selectActivityTypeNosWithStatus(@Param("agentNo") String agentNo, @Param("status")boolean status);

    @Select("SELECT update_agent_status FROM activity_hardware_type WHERE activity_type_no=#{activityTypeNo}")
    int selectUpdateAgentStatusByActivityTypeNo(@Param("activityTypeNo") String activityTypeNo);

    @Select("select * from agent_activity where agent_no=#{agentNo} and activity_type_no=#{activityTypeNo}")
    @ResultType(AgentActivity.class)
    AgentActivity selectAgentActivityByAgentNoAndTypeNo(@Param("agentNo") String agentNo, @Param("activityTypeNo") String activityTypeNo);

    List<Map<String, Object>> getActivityTypeNoAndTeamId(@Param("agentNo") String agentNo);


    public class SqlProvider {
        public String existAgentByMobilephoneAndTeamId(Map<String, Object> map) {
            final AgentInfo agent = (AgentInfo) map.get("agent");
            return new SQL() {{
                SELECT("count(1)");
                FROM("agent_info");
                // 因为邮箱改为非必填,所以邮箱为空时不检验邮箱,不为空时才校验
                if (StringUtils.isBlank(agent.getEmail())) {
                    WHERE("team_id=#{agent.teamId} and (mobilephone=#{agent.mobilephone} OR agent_name=#{agent.agentName})");
                }else {
                    WHERE("team_id=#{agent.teamId} and (mobilephone=#{agent.mobilephone} OR agent_name=#{agent.agentName} OR email=#{agent.email})");
                }
                if (agent.getId() != null) {
                    WHERE("id<>#{agent.id}");
                }
            }}.toString();
        }

        public String getServiceRate(Map<String, Object> map) {
            @SuppressWarnings("unchecked") final List<Integer> ids = (List<Integer>) map.get("bpIds");
            final String agentNo = (String) map.get("agentNo");
            String sql = new SQL() {{
                SELECT_DISTINCT("ser.*,info.agent_show_name service_name,info.service_type,info2.service_type as service_type2,bpd.bp_id,bpd.agent_show_name bp_name,bpd.allow_individual_apply,bpd.team_id");
                FROM("business_product_info bp ");
                LEFT_OUTER_JOIN("service_info info ON bp.service_id = info.service_id");
                LEFT_OUTER_JOIN("service_info info2 ON info2.link_service = info.service_id");
                LEFT_OUTER_JOIN("service_manage_rate ser ON bp.service_id = ser.service_id");
                //LEFT_OUTER_JOIN("agent_share_rule asr ON bp.service_id = asr.service_id");
                INNER_JOIN("business_product_define bpd on bp.bp_id=bpd.bp_id");
                WHERE("info.effective_status = 1");
                if (StringUtils.isNotBlank(agentNo) && !"0".equals(agentNo)) {
                    LEFT_OUTER_JOIN("agent_info ai on ser.agent_no=ai.one_level_id");
                    WHERE("ai.agent_no=#{agentNo}");
                } else {
                    WHERE("ser.agent_no=#{agentNo}");
                }
                StringBuilder sb = new StringBuilder(" bp.bp_id in (");
                MessageFormat messageFormat = new MessageFormat("#'{'bpIds[{0}]}");
                for (int i = 0; i < ids.size(); i++) {
                    sb.append(messageFormat.format(new Integer[]{i}));
                    sb.append(",");
                }
                sb.setLength(sb.length() - 1);
                sb.append(") ");
                WHERE(sb.toString());
                ORDER_BY("bp.bp_id,"
                        + "case when service_type2 is null THEN info.service_type ELSE service_type2 end,"
                        + "case when info.service_type in ('10000','10001') then '1' else '0' end"
                );
            }}.toString();
            return sql;
        }

        public String selectOneAgentName(Map<String, Object> param) {
            final String agentName = (String) param.get("agentName");
            return new SQL() {{
                SELECT("agent_no,agent_name");
                FROM("agent_info");
                WHERE("status='1'");
                WHERE("agent_level='1'");
                if (StringUtils.isNotBlank(agentName)) {
                    WHERE("agent_name like concat(#{agentName,'%'})");
                }
            }}.toString();
        }

        public String getServiceQuota(Map<String, Object> map) {
            @SuppressWarnings("unchecked") final List<Integer> ids = (List<Integer>) map.get("bpIds");
            final String agentNo = (String) map.get("agentNo");
            return new SQL() {{
                SELECT_DISTINCT("bp.bp_id,quota.*,info.service_name");
                FROM("business_product_info bp ");
                LEFT_OUTER_JOIN("service_info info ON bp.service_id = info.service_id");
                LEFT_OUTER_JOIN("service_manage_quota quota ON bp.service_id = quota.service_id");
                WHERE("info.effective_status = 1 ");
                if (StringUtils.isNotBlank(agentNo) && !"0".equals(agentNo)) {
                    LEFT_OUTER_JOIN("agent_info ai on quota.agent_no=ai.one_level_id");
                    WHERE("ai.agent_no=#{agentNo}");
                } else {
                    WHERE("quota.agent_no=#{agentNo}");
                }
                StringBuilder sb = new StringBuilder(" bp.bp_id in (");
                MessageFormat messageFormat = new MessageFormat("#'{'bpIds[{0}]}");
                for (int i = 0; i < ids.size(); i++) {
                    sb.append(messageFormat.format(new Integer[]{i}));
                    sb.append(",");
                }
                sb.setLength(sb.length() - 1);
                sb.append(")");
                WHERE(sb.toString());
                ORDER_BY("bp.bp_id");
            }}.toString();
        }

        public String insertAgentShareList(Map<String, List<AgentShareRule>> param) {
            List<AgentShareRule> list = param.get("list");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("replace into agent_share_rule(agent_no,service_id,card_type,holidays_mark,efficient_date,disabled_date,profit_type,per_fix_income,per_fix_inrate,safe_line,capping,"
                    + "share_profit_percent,ladder,cost_rate_type,per_fix_cost,cost_rate,cost_capping,cost_safeline,check_status,lock_status,ladder1_rate,ladder1_max,ladder2_rate,ladder2_max,ladder3_rate,"
                    + "ladder3_max,ladder4_rate,ladder4_max) values");
            MessageFormat messageFormat = new MessageFormat("(#'{'list[{0}].agentNo},#'{'list[{0}].serviceId},#'{'list[{0}].cardType},#'{'list[{0}].holidaysMark},now(),#'{'list[{0}].disabledDate},"
                    + "#'{'list[{0}].profitType},#'{'list[{0}].perFixIncome},#'{'list[{0}].perFixInrate},#'{'list[{0}].safeLine},#'{'list[{0}].capping},#'{'list[{0}].shareProfitPercent},#'{'list[{0}].ladder},#'{'list[{0}].costRateType},"
                    + "#'{'list[{0}].perFixCost},#'{'list[{0}].costRate},#'{'list[{0}].costCapping},#'{'list[{0}].costSafeline},#'{'list[{0}].checkStatus},#'{'list[{0}].lockStatus},#'{'list[{0}].ladder1Rate},#'{'list[{0}].ladder1Max},"
                    + "#'{'list[{0}].ladder2Rate},#'{'list[{0}].ladder2Max},#'{'list[{0}].ladder3Rate},#'{'list[{0}].ladder3Max},#'{'list[{0}].ladder4Rate},#'{'list[{0}].ladder4Max})");
            for (int i = 0; i < list.size(); i++) {
                stringBuilder.append(messageFormat.format(new Integer[]{i}));
                stringBuilder.append(",");
            }
            stringBuilder.setLength(stringBuilder.length() - 1);
            return stringBuilder.toString();
        }

        public String insertAgentActivity(Map<String, List<AgentActivity>> param) {
            List<AgentActivity> list = param.get("list");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("insert into agent_activity(activity_type_no,agent_no,agent_node,cash_back_amount,tax_rate,create_time,repeat_register_amount,repeat_register_ratio,full_prize_amount,not_full_deduct_amount,repeat_full_prize_amount,repeat_not_full_deduct_amount,status) values");
            MessageFormat messageFormat = new MessageFormat("(#'{'list[{0}].activityTypeNo},#'{'list[{0}].agentNo},#'{'list[{0}].agentNode}"
                    + ",#'{'list[{0}].cashBackAmount},#'{'list[{0}].taxRate},#'{'list[{0}].currentTime},#'{'list[{0}].repeatRegisterAmount},#'{'list[{0}].repeatRegisterRatio},#'{'list[{0}].fullPrizeAmount},#'{'list[{0}].notFullDeductAmount},#'{'list[{0}].repeatFullPrizeAmount},#'{'list[{0}].repeatNotFullDeductAmount},#'{'list[{0}].status})");
            for (int i = 0; i < list.size(); i++) {
                stringBuilder.append(messageFormat.format(new Integer[]{i}));
                stringBuilder.append(",");
            }
            stringBuilder.setLength(stringBuilder.length() - 1);
            return stringBuilder.toString();
        }

        public String insertAgentProductList(Map<String, List<JoinTable>> param) {
            List<JoinTable> list = param.get("list");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("insert into agent_business_product(agent_no,bp_id,status) values");
            MessageFormat messageFormat = new MessageFormat("(#'{'list[{0}].key3},#'{'list[{0}].key1},#'{'list[{0}].key2})");
            for (int i = 0; i < list.size(); i++) {
                stringBuilder.append(messageFormat.format(new Integer[]{i}));
                stringBuilder.append(",");
            }
            stringBuilder.setLength(stringBuilder.length() - 1);
            return stringBuilder.toString();
        }

        public String queryAgentInfoList(Map<String, Object> map) {
            final Map<String, Object> param = (Map<String, Object>) map.get("params");
            String sql = new SQL() {{
                SELECT("info.*, ai.agent_name as parentName");
                FROM("agent_info info");
                LEFT_OUTER_JOIN("agent_info ai ON info.parent_id = ai.agent_no");
                Object profitSwitch = param.get("profitSwitch");
                Object promotionSwitch = param.get("promotionSwitch");
                Object cashBackSwitch = param.get("cashBackSwitch");
                if (StringUtils.isNotBlank(profitSwitch == null ? "" : profitSwitch.toString())) {
                    WHERE("info.profit_switch = #{params.profitSwitch}");
                }
                if (StringUtils.isNotBlank(promotionSwitch == null ? "" : promotionSwitch.toString())) {
                    WHERE("info.promotion_switch = #{params.promotionSwitch}");
                }
                if (StringUtils.isNotBlank(cashBackSwitch == null ? "" : cashBackSwitch.toString())) {
                    WHERE("info.cash_back_switch = #{params.cashBackSwitch}");
                }
                if (param.get("mobilephone") != null && StringUtils.isNotBlank(param.get("mobilephone").toString())) {
                    WHERE("info.mobilephone like concat(#{params.mobilephone},'%')");
                }
                //是否包含下级,不包含下级,直接
                String temp;
                temp = (String) param.get("agentName");
                if ("0".equals(param.get("bool")) && StringUtils.isBlank(temp)) {//不包含下级,名称为空,不显示数据
                    WHERE("1<0");
                } else if ("1".equals(param.get("bool")) && StringUtils.isNotBlank(temp)) {//包含下级,有名称,显示根据名称查询出来的所有代理商
                    param.put("agentName", temp);
                    WHERE("info.agent_node like concat((select agent_node from agent_info ai where ai.agent_no = #{params.agentName}),'%')");
                } else if ("0".equals(param.get("bool")) && StringUtils.isNotBlank(temp)) {//不包含,有名称,显示名称本身代理商
                    param.put("agentName", temp);
                    WHERE("info.agent_no = #{params.agentName}");
                }
                if ("2".equals(param.get("bool"))) {//只显示直属下级
                    WHERE("info.parent_id = #{params.agentName}");
                }
                if (StringUtils.isNotBlank((String) param.get("agentNo"))) {
                    WHERE("info.agent_no like concat(#{params.agentNo},'%')");
                }
//    			if(param.get("belongAgentName") != null && param.get("belongAgentName") != ""){
//    				WHERE("info.agent_name = #{params.belongAgentName}");
//    			}
//    			if(param.get("belongAgentNo") != null && param.get("belongAgentNo") != ""){
//    				WHERE("info.agent_no = #{params.belongAgentNo}");
//    			}
                param.put("agentNode", param.get("agentNode").toString() + "%");
                WHERE("info.agent_node LIKE #{params.agentNode}");
                WHERE("info.agent_level > #{params.agentLevel}");

            }}.toString();
            return sql;
        }

        public String updateAgentProStatus(Map<String, Object> map) {
            @SuppressWarnings("unchecked") final Map<String, Object> agent = (Map<String, Object>) map.get("params");
            final Integer status = (Integer) agent.get("status");
            return new SQL() {{
                UPDATE("agent_business_product bp");
                SET("bp.status= #{params.status}");
                if (1 == status) {
                    WHERE("bp.agent_no=#{params.agentNo}");
                    WHERE("bp.status=0");
                } else if (0 == status) {
                    if (StringUtils.isBlank((String) agent.get("agentNode"))) {
                        agent.put("agentNode", "-1000");
                    } else {
                        agent.put("agentNode", (String) agent.get("agentNode") + "%");
                    }
                    WHERE("EXISTS (SELECT 1 FROM agent_info a WHERE a.agent_node LIKE #{params.agentNode} AND a.agent_no=bp.agent_no)");
                    WHERE("bp.status=1");
                }
                WHERE("bp.bp_id=#{params.bpId}");
            }}.toString();
        }

        public String getNewServiceRate(Map<String, Object> map) {
            @SuppressWarnings("unchecked") final List<Integer> ids = (List<Integer>) map.get("bpIds");
            return new SQL() {{
                SELECT_DISTINCT("ser.*,info.service_name");
                FROM("business_product_info bp ");
                LEFT_OUTER_JOIN("service_info info ON bp.service_id = info.service_id");
                LEFT_OUTER_JOIN("service_manage_rate ser ON bp.service_id = ser.service_id");
                WHERE("ser.agent_no='0'");
                StringBuilder sb = new StringBuilder(" bp.bp_id in (");
                MessageFormat messageFormat = new MessageFormat("#'{'bpIds[{0}]}");
                for (int i = 0; i < ids.size(); i++) {
                    sb.append(messageFormat.format(new Integer[]{i}));
                    sb.append(",");
                }
                sb.setLength(sb.length() - 1);
                sb.append(") ");
                WHERE(sb.toString());
                WHERE(" NOT EXISTS (SELECT 1 FROM agent_business_product abp LEFT JOIN "
                        + "business_product_info bpi ON  abp.bp_id =bpi.bp_id WHERE abp.agent_no=#{agentNo} "
                        + "AND ser.service_id=bpi.service_id)");
//				ORDER_BY("bp.bp_id");
            }}.toString();
        }

        public String selectChildAgentByAgentNode(Map<String, Object> param) {
            final String agentNode = (String) param.get("agentNode");
            final Integer agentLevel = (Integer) param.get("agentLevel");
            return new SQL() {{
                SELECT("agent_no,agent_name");
                FROM("agent_info");
                if (StringUtils.isNotBlank(agentNode)) {
                    WHERE("agent_node like concat(#{agentNode},'%')");
                }
                WHERE("agent_level=#{agentLevel}");
            }}.toString();
        }

        public String updateAgentUser(Map<String, Object> params) {
            final AgentUserInfo agentUser = (AgentUserInfo) params.get("agentUser");
            return new SQL() {{
                UPDATE("user_info");
                SET("user_name=#{agentUser.userName},mobilephone=#{agentUser.mobilephone},email=#{agentUser.email}");
                if (StringUtils.isNotBlank(agentUser.getPassword())) {
                    SET("password=#{agentUser.password},update_pwd_time=now()");
                }
                WHERE("user_id=#{agentUser.userId}");
            }}.toString();
        }

        public String getRate(Map<String, Object> map) {
            @SuppressWarnings("unchecked") final List<Integer> ids = (List<Integer>) map.get("bpIds");
            return new SQL() {{
                SELECT("r.*");
                FROM("agent_share_rule r ");
                LEFT_OUTER_JOIN("business_product_info abp ON r.service_id=abp.service_id ");
                LEFT_OUTER_JOIN("service_info si ON si.service_id=r.service_id ");
                StringBuilder sb = new StringBuilder("  abp.bp_id IN ( ");
                MessageFormat messageFormat = new MessageFormat("#'{'bpIds[{0}]}");
                for (int i = 0; i < ids.size(); i++) {
                    sb.append(messageFormat.format(new Integer[]{i}));
                    sb.append(",");
                }
                sb.setLength(sb.length() - 1);
                sb.append(") ");
                WHERE(sb.toString());
                WHERE("  r.agent_no=#{oneLevelId} AND NOT EXISTS (SELECT 1 FROM agent_share_rule r1 WHERE r1.agent_no=#{agentNo} AND r.service_id=r1.service_id )");
            }}.toString();
        }

        public String findAgentInfoListByAgentNoSet(Map<String, Object> map) {
            final Set<String> agentNoSet = (Set<String>) map.get("agentNoList");
            return new SQL() {{
                SELECT("a_i.*");
                FROM("agent_info a_i");
                if (agentNoSet !=null && agentNoSet.size()>0) {
		            StringBuilder sb = new StringBuilder("  a_i.agent_no IN ( ");
		            for (String agentNo : agentNoSet) {
		                sb.append("'" + agentNo + "'");
		                sb.append(",");
		            }
		            sb.setLength(sb.length() - 1);
		            sb.append(") ");
		            WHERE(sb.toString());
                }else {
                    WHERE(" 1 != 1");
                }

            }}.toString();

        }
    }

    @Select("SELECT SUM(counts) FROM ( "
            + "SELECT COUNT(1) counts FROM merchant_info m WHERE m.agent_no=#{agentNo} "
            + "UNION ALL "
            + "SELECT COUNT(1) counts FROM agent_info a "
            + "WHERE EXISTS ("
            + "SELECT 1 FROM agent_info b WHERE a.agent_node LIKE CONCAT(b.agent_node,'-%') "
            + "AND b.agent_no=#{agentNo}) ) t ")
    @ResultType(Integer.class)
    Integer getAgentAndMerchantCount(String agentNo);

    @Delete("delete from agent_user_role where user_id=#{userId} and role_id=5")
    public int delAgentRole(String userId);

    @Delete("delete from user_info where user_id=#{userId}")
    public int delAgentUser(String userId);

    @Delete("delete from user_entity_info where user_id=#{userId} and user_type=1")
    public int delAgentEntity(String userId);

    @Delete("delete FROM agent_share_rule_task USING agent_share_rule_task,agent_share_rule r WHERE r.agent_no=#{agentNo} AND r.id =agent_share_rule_task.share_id")
    int deleteAgentShareTasks(String agentNo);

    @Delete("delete from agent_share_rule where agent_no=#{agentNo}")
    int deleteAgentShares(String agentNo);

    @Delete("delete from agent_business_product where agent_no=#{agentNo}")
    int deleteAgentProducts(String agentNo);

    @Delete("delete from agent_info where agent_no=#{agentNo}")
    int deleteAgent(String agentNo);

    //    @Select("select * from user_entity_info where entity_id=#{agentNo} and user_type=1 and apply=1 and is_agent=1")
    //保存代理商修改tgh425
//    @Select("SELECT * FROM user_info WHERE mobilephone = (SELECT mobilephone FROM agent_info WHERE agent_no = #{agentNo}) AND team_id = " + Constants.TEAM_ID)
    @Select("SELECT * FROM user_info WHERE mobilephone = (SELECT mobilephone FROM agent_info WHERE agent_no = #{agentNo}) AND team_id = #{teamId}")
    @ResultType(Map.class)
    public Map<String, Object> getAgentEntity(@Param("agentNo") String agentNo, @Param("teamId") Integer teamId);

    @Select("select id,bp_id key1,`status` key2,agent_no key3 from agent_business_product where agent_no=#{agentNo} and bp_id=#{bpId}")
    @ResultType(JoinTable.class)
    JoinTable getAgentPro(@Param("agentNo") String agentNo, @Param("bpId") Integer bpId);

    //更新状态
    @UpdateProvider(type = SqlProvider.class, method = "updateAgentProStatus")
    int updateAgentProStatus(@Param("params") Map<String, Object> map);

    @Update("update agent_info set agent_name=#{agent.agentName},is_oem=#{agent.isOem},email=#{agent.email},phone=#{agent.phone},invest=#{agent.invest},"
            + "agent_area=#{agent.agentArea},mobilephone=#{agent.mobilephone},link_name=#{agent.linkName},id_card_no=#{agent.idCardNo},invest_amount=#{agent.investAmount},address=#{agent.address},account_name=#{agent.accountName},"
            + "account_type=#{agent.accountType},account_no=#{agent.accountNo},bank_name=#{agent.bankName},cnaps_no=#{agent.cnapsNo},sale_name=#{agent.saleName},mender=#{agent.mender},"
            + "public_qrcode=#{agent.publicQrcode},manager_logo=#{agent.managerLogo},logo_remark=#{agent.logoRemark},"
            + "client_logo=#{agent.clientLogo},custom_tel=#{agent.customTel},is_approve=#{agent.isApprove},count_level=#{agent.countLevel},"
            + " province=#{agent.province},city=#{agent.city},area=#{agent.area},account_province=#{agent.accountProvince},account_city=#{agent.accountCity} where id=#{agent.id}")
    int updateAgent(@Param("agent") AgentInfo agent);

    @Update("update agent_info set agent_name=#{agent.agentName},is_oem=#{agent.isOem},email=#{agent.email},phone=#{agent.phone},invest=#{agent.invest},"
            + "agent_area=#{agent.agentArea},mobilephone=#{agent.mobilephone},link_name=#{agent.linkName},invest_amount=#{agent.investAmount},address=#{agent.address},"
            + "sale_name=#{agent.saleName},mender=#{agent.mender},"
            + "public_qrcode=#{agent.publicQrcode},manager_logo=#{agent.managerLogo},logo_remark=#{agent.logoRemark},"
            + "client_logo=#{agent.clientLogo},custom_tel=#{agent.customTel},is_approve=#{agent.isApprove},count_level=#{agent.countLevel},"
            + " province=#{agent.province},city=#{agent.city},area=#{agent.area} where id=#{agent.id}")
    int updateAgentNoSafePhone(@Param("agent") AgentInfo agent);
    
    @UpdateProvider(type = SqlProvider.class, method = "updateAgentUser")
    int updateAgentUser(@Param("agentUser") AgentUserInfo agentUser);

    @SelectProvider(type = SqlProvider.class, method = "getNewServiceRate")
    @ResultType(ServiceRate.class)
    List<ServiceRate> getNewServiceRate(@Param("bpIds") List<Integer> bpIds, @Param("agentNo") String agentId);

    //修改代理商是否已开账户
    @Update("update agent_info set has_account=#{status} where agent_no=#{agentNo}")
    public int updateAgentAccount(@Param("agentNo") String agentNo, @Param("status") int status);

    @Select("select * from agent_info where status='1' and team_id = #{teamId}")
    @ResultType(AgentInfo.class)
    List<AgentInfo> selectAllInfoByTeamId(@Param("teamId") String teamId);

    @Select("select * from agent_info where status='1' and team_id = #{teamId} and mobilephone=#{mobilephone}")
    @ResultType(AgentInfo.class)
    AgentInfo selectByMobilephoneAndTeamId(@Param("mobilephone") String mobilephone, @Param("teamId") String teamId);

    @Select("select one_level_id from agent_info where agent_no=#{agentNo}")
    @ResultType(String.class)
    String getOneAgentNo(@Param("agentNo") String agentNo);

    @Select("SELECT SUM(agent_share_amount) as profitAmount FROM agent_trans_collect WHERE agent_no =#{agentNo} AND DATE(trans_time) = CURDATE()  ")
    String selectProfitToday(String agentNo);

    //20170112
    @Select("SELECT ai.* FROM agent_info ai WHERE agent_no=("
            + "SELECT uei.entity_id FROM user_entity_info uei WHERE uei.user_type=1 AND uei.apply=1 "
            + "AND uei.user_id = (SELECT ui.user_id FROM user_info ui WHERE ui.id = #{userId})) AND agent_level = 1")
    @ResultType(AgentInfo.class)
    AgentInfo selectLevelOne(Integer userId);

    //227tgh重置密码
    @Update("UPDATE user_info SET password = #{user.password},update_pwd_time=now() WHERE mobilephone = #{user.mobilephone} AND team_id = #{user.teamId}")
    int updateRestPwd(@Param("user") UserInfo user);

    //227tgh重置密码
    @Select("select * from agent_info WHERE mobilephone = #{mobilephone} AND team_id = #{teamId}")
    @ResultType(UserInfo.class)
    UserInfo selectByCons(@Param("mobilephone") String mobilephone, @Param("teamId") String teamId);

    //tgh330
    @Select("select * from function_manage where function_number= #{functionNumber}")
    @ResultType(Map.class)
    Map<String, Object> findFunctionManage(@Param("functionNumber") String functionNumber);

    //tgh330
//	@Select("select * from agent_function_manage where agent_no = #{agentNo}  and function_number= #{functionNumber}  and team_id ='"+Constants.TEAM_ID+"'")
    @Select("select * from agent_function_manage where agent_no = #{agentNo}  and function_number= #{functionNumber}  and team_id = #{teamId}")
    @ResultType(Map.class)
    Map<String, Object> findActivityIsSwitch(@Param("functionNumber") String functionNumber, @Param("agentNo") String agentNo, @Param("teamId") Integer teamId);

    @Select("SELECT sys_name,sys_value FROM sys_dict WHERE sys_key = 'SYS_SUBJECTNO'")
    @ResultType(Map.class)
    List<Map<String, String>> selectSubjectNo();

    //	@Select("SELECT * from 	settle_order_info i where "
//					+ "i.settle_user_no = #{entityId} AND NOT EXISTS (SELECT 1 from settle_transfer st WHERE "
//					+ "i.settle_order = st.trans_id AND st.settle_type = i.settle_type) AND i.create_time > CURDATE() and i.syn_status = '1' "
//					+ " and i.settle_status = '0' and i.sub_type = #{subType}")
    // 2017.09.05 sql 优化
    @Select("SELECT count(1) from 	settle_order_info i LEFT JOIN settle_transfer st ON i.settle_order = st.trans_id AND st.settle_type = i.settle_type " +
            "where i.settle_user_no = #{entityId} and st.trans_id is null "
            + " AND i.create_time > CURDATE() and i.syn_status = '1' "
            + " and i.settle_status = '0' and i.sub_type = #{subType}")
    @ResultType(Map.class)
    int findWithDrawCash(@Param("entityId") String entityId, @Param("subType") String subType);

    @Select("SELECT * FROM service_manage_rate WHERE service_id = #{serviceId} AND agent_no = 0")
    @ResultType(Map.class)
    Map<String, Object> getSingleNumAmount(@Param("serviceId") String serviceId);

    @Select("SELECT bp_id FROM agent_business_product WHERE agent_no = #{agentNo}")
    @ResultType(Integer.class)
    List<Integer> selectBpIdByAgentNo(@Param("agentNo") String agentNo);

    @SelectProvider(type = SqlProvider.class, method = "getRate")

    @ResultType(ServiceRate.class)
    List<ServiceRate> getRate(@Param("bpIds") List<Integer> bpIds, @Param("oneLevelId") String oneLevelId, @Param("agentNo") String agentNo);

    /**
     * 获取代理商的所代理的业务产品中组长和组员的关系
     *
     * @param agentNo 代理商编号
     * @return 队长与队员的对应关系
     */
    List<Map<String, Object>> getLeaderAndMember(@Param("agentNo") String agentNo);

    /**
     * 欢乐返活动/我的信息
     *
     * @param agentNo
     * @return
     */
    @Select("SELECT " +
    		"	aa.*, " +
    		"	aht.activity_type_name, " +
    		"	aht.activity_code, " +
    		"	aht.trans_amount  " +
    		"FROM " +
    		"	agent_activity aa " +
    		"	LEFT JOIN activity_hardware_type aht ON aa.activity_type_no = aht.activity_type_no  " +
    		"WHERE " +
    		"	agent_no = #{agentNo}")
    @ResultType(List.class)
    List<AgentActivity> selectHappyBackActivity(@Param("agentNo") String agentNo);

    /**
     * 根据一级代理商编号查询欢乐返活动配置,新增代理商时欢乐返活动信息
     *
     * @param oneLevelId
     * @return
     */
    @Select("SELECT " +
    		"	*  " +
    		"FROM " +
    		"	agent_activity aa " +
    		"	LEFT JOIN activity_hardware_type aht ON aa.activity_type_no = aht.activity_type_no  " +
    		"WHERE " +
    		"	aa.agent_no = #{oneLevelId}")
    @ResultType(List.class)
    List<AgentActivity> selectHappyBackList(@Param("oneLevelId") String oneLevelId);

    @Select("SELECT * FROM agent_activity aa " +
            "LEFT JOIN activity_hardware_type aht ON aa.activity_type_no = aht.activity_type_no " +
            "WHERE aa.agent_no = #{agentNo} AND STATUS = #{status}")
    @ResultType(AgentActivity.class)
    List<AgentActivity> selectHappyBackListWithStatus(@Param("agentNo") String agentNo, @Param("status") boolean status);

    @Select("SELECT * " +
            "FROM agent_activity aa " +
            "LEFT JOIN activity_hardware_type aht ON aa.activity_type_no = aht.activity_type_no " +
            "WHERE aa.agent_no=#{currentAgentNo} " +
            "AND aa.activity_type_no NOT IN(SELECT activity_type_no FROM agent_activity WHERE agent_no=#{agentNo})")
    @ResultType(AgentActivity.class)
    List<AgentActivity> selectHappyBackListEclude(@Param("currentAgentNo") String currentAgentNo, @Param("agentNo")String agentNo);

    @Select("SELECT * " +
            "FROM agent_activity aa " +
            "LEFT JOIN activity_hardware_type aht ON aa.activity_type_no = aht.activity_type_no " +
            "WHERE aa.agent_no=#{currentAgentNo} and aa.status=true " +
            "AND aa.activity_type_no NOT IN(SELECT activity_type_no FROM agent_activity WHERE agent_no=#{agentNo} and status=#{status})")
    @ResultType(AgentActivity.class)
    List<AgentActivity> selectHappyBackListEcludeWithStatus(@Param("currentAgentNo") String currentAgentNo, @Param("agentNo")String agentNo, @Param("status") boolean status);


    @Select("select cash_back_amount,tax_rate,repeat_register_amount,repeat_register_ratio from agent_activity where agent_no = #{oneLevelId} and activity_type_no = #{activityTypeNo}")
    @ResultType(Map.class)
    Map<String, Object> selectByActivityTypeNo(@Param("oneLevelId") String oneLevelId, @Param("activityTypeNo") String activityTypeNo);

    /**
     * 修改欢乐返活动配置
     *
     * @return
     */
    @Update("UPDATE agent_activity  " +
    		"SET cash_back_amount = #{a.cashBackAmount}, " +
    		"tax_rate = #{a.taxRate}, " +
    		"repeat_register_amount = #{a.repeatRegisterAmount}, " +
    		"repeat_register_ratio = #{a.repeatRegisterRatio}, " +
    		"full_prize_amount = #{a.fullPrizeAmount}, " +
    		"not_full_deduct_amount = #{a.notFullDeductAmount}, " +
    		"repeat_full_prize_amount = #{a.repeatFullPrizeAmount}, " +
    		"repeat_not_full_deduct_amount = #{a.repeatNotFullDeductAmount} " +
    		"WHERE " +
    		"	agent_no = #{a.agentNo} " +
    		"	AND activity_type_no = #{a.activityTypeNo}")
    int updateHappyBackList(@Param("a") AgentActivity agentActivity);

    /**
     * 查询默认总开关的状态
     *
     * @return
     */
    @Select("SELECT * FROM agent_account_control where default_status = '1'")
    @ResultType(Map.class)
    Map<String, Object> selectDefaultStatus();

    /**
     * 查询当前代理商有没有差异化设置
     *
     * @param entityId
     * @return
     */
    @Select("SELECT * FROM agent_account_control where agent_no = #{entityId}")
    @ResultType(Map.class)
    Map<String, Object> selectAccountStatus(@Param("entityId") String entityId);

    /**
     * 判断身份证唯一
     *
     * @param idCardNo
     * @return
     */
    @Select("SELECT count(1) FROM agent_info where id_card_no = #{idCardNo} and agent_type = '11'")
    @ResultType(Integer.class)
    Integer selectByIdCardNo(@Param("idCardNo") String idCardNo);

    /**
     * 海涛,国栋,水育确认上游金额小于设置金额,自动关闭通道开关,然后账务手动开启
     *
     * @return
     */
    @Update(" UPDATE out_account_service SET out_account_status = '0' WHERE id = #{id} ")
    @ResultType(Integer.class)
    Integer updateWithdrawSwitch(@Param("id") Integer id);


    @Update(" update merchant_info set agent_no = #{agentNo},parent_node = #{agentNode} where merchant_no = #{merchantNo} ")
    @ResultType(Integer.class)
    Integer updateMerchantInfo(@Param("agentNo") String agentNo, @Param("agentNode") String agentNode, @Param("merchantNo") String merchantNo);

    @Update(" update pa_ter_info pti,terminal_info ti set pti.agent_no = #{agentNo},ti.agent_no = #{agentNo},ti.agent_node = #{agentNode}"
            + " where pti.sn = ti.SN and pti.user_code = #{userCode} ")
    @ResultType(Integer.class)
    Integer updatePaTerInfoAndTerminalInfo(@Param("userCode") String userCode, @Param("agentNo") String agentNo, @Param("agentNode") String agentNode);

    @Update(" update collective_trans_order set agent_node = #{agentNode} where merchant_no = #{merchantNo} ")
    @ResultType(Integer.class)
    Integer updateCollectiveTransOrder(@Param("agentNode") String agentNode, @Param("merchantNo") String merchantNo);

    @Update(" update activity_detail set agent_node = #{agentNode} where merchant_no = #{merchantNo} ")
    @ResultType(Integer.class)
    Integer updateActivityDetail(@Param("agentNode") String agentNode, @Param("merchantNo") String merchantNo);

    @Update(" update agent_info set safephone = #{safephone} where agent_no = #{agentNo} ")
    @ResultType(Integer.class)
	int updateSafePhoneByAgentNo(@Param("agentNo") String agentNo, @Param("safephone")String checkphone);

    @Update(" update agent_info set safe_password = #{safePassword} where agent_no = #{agentNo} ")
    @ResultType(Integer.class)
	int updateSafePassword(@Param("safePassword")String safePassword,@Param("agentNo") String agentNo);

    
    @Select("select agent_no agentNoTwo,agent_name agentNameTwo " +
            "from agent_info where status = '1' and parent_id = #{entityId}")
    @ResultType(List.class)
    List<Map<String,Object>> selectAllInfoBelong(@Param("entityId") String entityId);

    @Select("select safephone from agent_info where agent_no = #{agentNo}")
    @ResultType(String.class)
    String getSafePhone(@Param("agentNo")String agentNo);

    /**
     * 查询功能开关 一级代理商给二级代理商设置解、增绑机具权限 是否打开
     *
     * @return
     */
    @Select(" select * from function_manage where function_number = #{functionNumber} ")
    @ResultType(Map.class)
    Map<String, Object> selectFunctionManage(@Param("functionNumber") String functionNumber);




    /**
     * 查询代理商固定分润百分比
     *
     * @return
     */
    @Select(" select * from agent_share_rule " +
            "where agent_no = #{agentNo} " +
            "and service_id = #{serviceId} " +
            "and card_type= #{cardType} " +
            "and holidays_mark = #{holidaysMark} " +
            "limit 1")
    @ResultType(Map.class)
    Map<String, Object> selectAgentShareRule(@Param("agentNo") String agentNo,
                                             @Param("serviceId") String serviceId,
                                             @Param("cardType") String cardType,
                                             @Param("holidaysMark") String holidaysMark);

    @Select("SELECT " +
            "	a_i.* " +
            "FROM " +
            "	agent_authorized_link a_a_l " +
            "JOIN agent_info a_i ON a_a_l.agent_link = a_i.agent_no " +
            "WHERE " +
            "	a_a_l.agent_authorized = #{agentNo} and a_a_l.record_status=1 and a_a_l.record_check=1 ")
    List<AgentInfo> findConfigAgentInfo(String agentNo);

    @Select("SELECT " +
            "	count(a_i_l.id) " +
            "FROM " +
            "	agent_authorized_link a_i_l " +
            "WHERE " +
            "	a_i_l.agent_authorized = #{currAgentNo} " +
            "AND a_i_l.agent_link = #{selectAgentNo} " +
            "AND a_i_l.record_status = 1")
    long countAuthAgent(@Param("currAgentNo") String currAgentNo, @Param("selectAgentNo") String selectAgentNo);

    @Select("SELECT count(a_i.id) FROM agent_info a_i WHERE a_i.agent_level = 1 AND a_i.agent_no=#{agentNo}")
    long countOneAgentByUserName(@Param("agentNo") String agentNo);

    @Select("SELECT " +
            "	a_i.* " +
            "FROM " +
            "	agent_info a_i " +
            "WHERE " +
            "	a_i.agent_no = #{agentNo} ")
    @ResultType(AgentInfo.class)
    AgentInfo findByMobilePhone(@Param("agentNo") String agentNo);

    @Select(" " +
            "SELECT " +
            "	a_i.* " +
            "FROM " +
            "	agent_info a_i " +
            "JOIN agent_info a_info_2 ON a_i.agent_no = a_info_2.one_level_id " +
            "WHERE " +
            "	a_info_2.agent_no = #{entityId}")
    @ResultType(AgentInfo.class)
    AgentInfo findAgentInfoByUserAgentNo(String entityId);

    @Select("SELECT " +
            "	agent_link " +
            "FROM " +
            "	agent_authorized_link t " +
            "WHERE " +
            "	t.agent_authorized = #{agentNo} and t.record_status=1 and t.record_check=1 ")
    @ResultType(Set.class)
    Set<String> findConfigAgentNo(String agentNo);


    @SelectProvider(type = SqlProvider.class, method = "findAgentInfoListByAgentNoSet")
    @ResultType(AgentInfo.class)
    List<AgentInfo> findAgentInfoListByAgentNoSet(@Param("agentNoList") Set<String> agentNoList);

    @Select("SELECT full_prize_switch FROM agent_info WHERE agent_no = #{agentNo}")
    @ResultType(Boolean.class)
	boolean parentFullPrizeSwitch(String parentAgentNo);

    @Update("UPDATE agent_info " +
            "SET full_prize_switch = #{switch} " +
            "WHERE  agent_node LIKE #{parentAgentNode}")
	void updateChildAndSelfFullPrizeSwitch(@Param("parentAgentNode") String string, @Param("switch")int i);

    @Update("UPDATE agent_info " +
            "SET full_prize_switch = #{switch} " +
            "WHERE  agent_no = #{agentNo}")
	void updateFullPrizeSwitch(@Param("agentNo")String childrenAgentNo, @Param("switch")int fullPrizeSwitch);


    @Select("SELECT " +
    		"	count( * )  " +
    		"FROM " +
    		"	agent_activity aa " +
    		"	LEFT JOIN activity_hardware_type aht ON aa.activity_type_no = aht.activity_type_no  " +
    		"WHERE " +
    		"	aa.agent_no = #{parentAgentNo} " +
    		"	AND (aa.repeat_not_full_deduct_amount IS NULL OR aa.not_full_deduct_amount IS NULL )")
	long countFullAmountByAgentNo(String parentAgentNo);

    @Select("SELECT not_full_deduct_switch FROM agent_info WHERE agent_no = #{agentNo}")
    @ResultType(Boolean.class)
	boolean parentNotFullDeductSwitch(String parentAgentNo);

    @Update("UPDATE agent_info " +
            "SET not_full_deduct_switch = #{switch} " +
            "WHERE  agent_node LIKE #{parentAgentNode}")
	void updateChildAndSelfNotFullDeductSwitch(@Param("parentAgentNode")String string,@Param("switch") int notFullDeductSwitch);

    @Update("UPDATE agent_info " +
            "SET not_full_deduct_switch = #{switch} " +
            "WHERE  agent_no = #{agentNo}")
	void updateNotFullDeductSwitch(@Param("agentNo")String childrenAgentNo, @Param("switch")int notFullDeductSwitch);

    @Select("SELECT " +
    		"	*  " +
    		"FROM " +
    		"	agent_activity  " +
    		"WHERE " +
    		"	agent_no = #{agentNo}  " +
    		"	AND activity_type_no = #{activityTypeNo}")
	AgentActivity findAgentActivityByParentAndType(@Param("agentNo")String agentNo, @Param("activityTypeNo")String activityTypeNo);

    @Update("UPDATE agent_info  " +
    		"SET full_prize_switch = 0, " +
    		"not_full_deduct_switch = 0  " +
    		"WHERE " +
    		"	parent_id = #{agentNo}")
	int updateChildrenFullSwitch(String agentNo);

    @Update("UPDATE agent_info  " +
    		"SET full_prize_switch = 0 " +
    		"WHERE " +
    		"	parent_id = #{agentNo}")
	int updateChildrenFullPrizeSwitch(String agentNo);

    @Update("UPDATE agent_info  " +
    		"SET not_full_deduct_switch = 0  " +
    		"WHERE " +
    		"	parent_id = #{agentNo}")
	int updateChildrenNotFullDeductSwitch(String agentNo);

    @Update(" UPDATE agent_info SET id_card_no = #{idCardNo} WHERE agent_no = #{agentNo} ")
    @ResultType(Integer.class)
    Integer updateAgentByIdCardNo(@Param("idCardNo") String idCardNo,@Param("agentNo") String agentNo);

    @Select("SELECT " +
    		"	count( * )  " +
    		"FROM " +
    		"	agent_activity aa " +
    		"	LEFT JOIN activity_hardware_type aht ON aa.activity_type_no = aht.activity_type_no  " +
    		"WHERE " +
    		"	aa.agent_no = #{parentAgentNo} " +
    		"	AND ( aa.full_prize_amount IS NULL OR aa.repeat_full_prize_amount IS NULL )")
	long countFullPrizeAmountByAgentNo(String childrenAgentNo);

    @Select(
            "select aa.activity_type_no as activityTypeNo, aht.activity_type_name as activityTypeName " +
                    " from agent_activity aa " +
                    "   LEFT JOIN activity_hardware_type aht ON aht.activity_type_no = aa.activity_type_no " +
                    " where aa.agent_no=#{oneAgentNo} "
    )
    @ResultType(AgentActivity.class)
    List<AgentActivity> getAgentActivity(@Param("oneAgentNo") String oneAgentNo);
    

    @Select("SELECT " + 
            "    count(*)  " +
            "FROM " + 
            "    function_manage f_mana " + 
            "    JOIN agent_function_manage_blacklist a_f_mana ON f_mana.function_number = a_f_mana.function_number " +
            "    AND f_mana.function_switch = 1  " + 
            "    AND f_mana.function_number = '051' " +
            "    AND a_f_mana.agent_no = #{agentNo}  " + 
            "    AND a_f_mana.blacklist = 1  " + 
            "    AND a_f_mana.contains_lower =0")
    long countBlacklistNotContains(String agentNo);

    @Select("SELECT " + 
            "    count(*) " + 
            "FROM " + 
            "    function_manage f_mana " + 
            "    JOIN agent_function_manage_blacklist a_f_mana ON f_mana.function_number = a_f_mana.function_number " +
            "    JOIN agent_info a_info ON a_info.agent_no = a_f_mana.agent_no " + 
            "    JOIN agent_info a_info2 ON a_info2.agent_node LIKE CONCAT( a_info.agent_node, '%' )  " + 
            "    AND f_mana.function_switch = 1  " + 
            "    AND f_mana.function_number = '051'  " +
            "    AND a_info2.agent_node = #{agentNode}  " + 
            "    AND a_f_mana.blacklist = 1  " + 
            "    AND a_f_mana.contains_lower =1;")
    long countBlacklistContains(String agentNode);

    @Select("SELECT * FROM activity_hardware_type WHERE activity_type_no=#{activityTypeNo}")
    @ResultType(Map.class)
    Map<String, Object> selectHappyBackDefaultParam(@Param("activityTypeNo") String activityTypeNo);

}