package cn.eeepay.framework.daoSuperBank;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.OrgInfo;
import cn.eeepay.framework.model.OrgInfoOpenConf;

public interface OrgInfoDao {

    @Insert("insert into org_info(org_id,org_name,org_logo,weixin_sign,super_orgcode,v2_agent_number" +
            ",v2_orgcode,agent_price,agent_cost,agent_push_cost,receive_cost,receive_push_cost" +
            ",repayment_cost,repayment_push_cost,up_manager_num,up_manager_cardnum,up_banker_num" +
            ",up_banker_cardnum,remark,update_by,update_date,create_date)" +
            " values(#{orgId},#{orgName},#{orgLogo},#{weixinSign},#{superOrgcode},#{v2AgentNumber}" +
            ",#{v2Orgcode},#{agentPrice},#{agentCost},#{agentPushCost},#{receiveCost},#{receivePushCost}" +
            ",#{repaymentCost},#{repaymentPushCost},#{upManagerNum},#{upManagerCardnum},#{upBankerNum}" +
            ",#{upBankerCardnum},#{remark},#{updateBy},#{updateDate},#{createDate})")
    int insert(OrgInfo orgInfo);

    @Update("update org_info set org_name=#{orgName},weixin_sign=#{weixinSign},agent_price=#{agentPrice}," +
            "agent_cost=#{agentCost},agent_push_cost=#{agentPushCost},receive_cost=#{receiveCost}," +
            "receive_push_cost=#{receivePushCost},repayment_cost=#{repaymentCost}," +
            "repayment_push_cost=#{repaymentPushCost},up_manager_num=#{upManagerNum}," +
            "up_manager_cardnum=#{upManagerCardnum},up_banker_num=#{upBankerNum}," +
            "up_banker_cardnum=#{upBankerCardnum},remark=#{remark},update_by=#{updateBy}," +
            "update_date=#{updateDate},org_logo=#{orgLogo} where org_id = #{orgId}")
    int update(OrgInfo orgInfo);

    @SelectProvider(type = SqlProvider.class, method = "selectOrgInfoPage")
    @ResultType(OrgInfo.class)
    List<OrgInfo> selectOrgInfoPage(@Param("baseInfo") OrgInfo baseInfo2, @Param("page")Page<OrgInfo> page);

    @Select("select org_id, org_name from org_info order by convert(org_name using gbk)")
    @ResultType(OrgInfo.class)
    List<OrgInfo> getOrgInfoList();
    
    @Select("select withdraw_money_min from org_info where org_id = #{orgId}")
    @ResultType(OrgInfo.class)
    String selectWithdrawMoneyMinByOrgId(@Param("orgId")String orgId);

    @Select("select count(1) from org_info where org_id = #{orgId}")
    Long checkExistsOrgId(@Param("orgId")Long orgId);

    @Select("select * from org_info where org_id = #{orgId}")
    @ResultType(OrgInfo.class)
    OrgInfo selectDetail(@Param("orgId") Long orgId);

    @Select("select * from org_info_open_conf where org_id = #{entityId}")
    @ResultType(List.class)
    List<OrgInfoOpenConf> selectDevelopmentConfiguration(@Param("entityId")String entityId);
    
    @Select("select * from open_function_conf")
    @ResultType(List.class)
    List<OrgInfoOpenConf> selectFunctionUrl();
    
    @Select("select is_open from org_info where org_id = #{entityId}")
    @ResultType(Integer.class)
    Integer selectIsOpen(@Param("entityId")String entityId);
    
    public class SqlProvider{

        public String selectOrgInfoPage(Map<String, Object> param){
            String sql = "";
            final OrgInfo baseInfo = (OrgInfo) param.get("baseInfo");
            sql = new SQL(){{
                SELECT("oi.org_id, oi.org_name, oi.org_logo, oi.weixin_sign, oi.super_orgcode");
                SELECT("oi.v2_agent_number, oi.v2_orgcode, oi.agent_price, oi.agent_cost");
                SELECT("oi.agent_push_cost, oi.receive_cost, oi.receive_push_cost");
                SELECT("oi.repayment_cost, oi.repayment_push_cost, oi.up_manager_num");
                SELECT("oi.up_manager_cardnum, oi.up_banker_num, oi.up_banker_cardnum");
                SELECT("oi.remark");
                FROM("org_info oi");
                if(baseInfo != null && baseInfo.getOrgId() != -1){
                    WHERE("oi.org_id = #{baseInfo.orgId}");
                }
                ORDER_BY("oi.create_date desc");
            }}.toString();
            return sql;
        }
    }

}
