package cn.eeepay.framework.dao;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.ProviderBean;
import cn.eeepay.framework.util.WriteReadDataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * Created by 666666 on 2017/10/27.
 */
@WriteReadDataSource
public interface ProviderDao {
    @SelectProvider(type=SqlProvider.class, method = "listActivationCode")
    List<ProviderBean> listProvider(@Param("bean") ProviderBean providerBean, Page<AgentInfo> page, @Param("loginAgent") AgentInfo loginAgent);

    @SelectProvider(type=SqlProvider.class, method = "chechAgentNoIsDirectChildren")
    int chechAgentNoIsDirectChildren(@Param("list") List<String> agentNoList, @Param("loginAgent")AgentInfo loginAgent);

    @InsertProvider(type=SqlProvider.class, method = "openSuperRepayment")
    void openSuperRepayment(@Param("list") List<ProviderBean> wantAddAgent);

    @Update("update yfb_service_cost set rate = #{bean.rate}, single_amount = #{bean.singleAmount}," +
            "account_ratio = #{bean.accountRatio}," +
            "full_repay_rate = #{bean.fullRepayRate},full_repay_single_amount = #{bean.fullRepaySingleAmount}," +
            "perfect_repay_rate = #{bean.perfectRepayRate},perfect_repay_single_amount = #{bean.perfectRepaySingleAmount} " +
            "where agent_no = #{bean.agentNo}")
    int updateServiceCost(@Param("bean") ProviderBean bean);

    @Select("SELECT agent_no,rate,single_amount,full_repay_rate,full_repay_single_amount,perfect_repay_rate,perfect_repay_single_amount FROM yfb_service_cost WHERE service_type = 'repay' AND agent_no = #{agentNo}")
    ProviderBean queryRepayServiceCost(String agentNo);

    @Select("SELECT MIN(rate) rate,MIN(single_amount) singleAmount,MIN(full_repay_rate) fullRepayRate," +
            "MIN(full_repay_single_amount) fullRepaySingleAmount,MIN(perfect_repay_rate) perfectRepayRate," +
            "MIN(perfect_repay_single_amount) perfectRepaySingleAmount FROM yfb_service_cost " +
            "WHERE agent_no IN (SELECT agent_no FROM agent_info WHERE agent_node LIKE concat(#{agentNode},'%') AND agent_no != #{agentNo})")
    ProviderBean queryMinCostOfLastAgent(@Param("agentNode") String agentNode, @Param("agentNo") String agentNo);

    @Select("SELECT agent_no, " +
            "trade_fee_rate rate,trade_single_fee singleAmount," +
            "full_repay_fee_rate fullRepayRate,full_repay_single_fee fullRepaySingleAmount," +
            "perfect_repay_fee_rate perfectRepayRate,perfect_repay_single_fee perfectRepaySingleAmount"+
            " FROM yfb_oem_service WHERE agent_no = #{agentNo} AND oem_type = 'repay'")
    ProviderBean queryRepayOemServiceCost(String agentNo);

    @Select("SELECT agent_node agentNode FROM agent_info WHERE agent_no = #{agentNo}")
    String queryAgentNoByAgentNo(String agentNo);


    class SqlProvider{
        public String listActivationCode(Map<String, Object> param){
            final ProviderBean bean = (ProviderBean) param.get("bean");
            SQL sql = new SQL(){{
                SELECT("ysc.account_ratio,ai.agent_no,ai.agent_name,ai.agent_level,ai.mobilephone,ysc.rate,ysc.single_amount,ai.parent_id, ysc.full_repay_rate, ysc.full_repay_single_amount, ysc.perfect_repay_rate, ysc.perfect_repay_single_amount");
                FROM("agent_info ai");
                LEFT_OUTER_JOIN("yfb_service_cost ysc ON ysc.agent_no = ai.agent_no AND ysc.service_type = 'repay'");
                WHERE("ai.agent_node like concat(#{loginAgent.agentNode}, '%')");
                if (StringUtils.isNotBlank(bean.getAgentLevel())){
                    WHERE("ai.agent_level = #{bean.agentLevel}");
                }
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

        public String chechAgentNoIsDirectChildren(Map<String, Object> param){
            final List<String> agentNoList = (List<String>) param.get("list");
            SQL sql = new SQL(){{
                SELECT("count(*)");
                FROM("agent_info ai");
                LEFT_OUTER_JOIN("yfb_service_cost ysc ON ai.agent_no =  ysc.agent_no");
                WHERE("ai.parent_id = #{loginAgent.agentNo}");
                WHERE("IFNULL(ysc.agent_no, '') = ''");
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < agentNoList.size(); i ++){
                    sb.append("#{list["+i+"]},");
                }
                sb.deleteCharAt(sb.lastIndexOf(","));
                WHERE("ai.agent_no in ("+sb.toString()+")");
            }};
            return sql.toString();
        }

        public String openSuperRepayment(Map<String, Object> param){
            List<ProviderBean> agentList = (List<ProviderBean>) param.get("list");
            StringBuilder sb = new StringBuilder();
            sb.append("REPLACE INTO yfb_service_cost(agent_no, rate, single_amount, service_type,full_repay_rate,full_repay_single_amount) values ");
            for (int i = 0; i < agentList.size(); i ++){
                sb.append("(#{list["+i+"].agentNo}, #{list["+i+"].rate}, #{list["+i+"].singleAmount}, 'repay', #{list["+i+"].fullRepayRate}, #{list["+i+"].fullRepaySingleAmount}),");
            }
            sb.deleteCharAt(sb.lastIndexOf(","));
            return sb.toString();
        }
    }
}
