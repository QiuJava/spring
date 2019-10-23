package cn.eeepay.framework.dao.nposp;

import java.util.List;

import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import cn.eeepay.framework.model.nposp.AgentShareRule;

/**
 * 代理商分润规则Dao
 * @author Administrator
 *
 */
public interface AgentShareRuleMapper {
	
	@Select("select * from agent_share_rule where profit_type = #{profitType}")
	@ResultType(AgentShareRule.class)
	List<AgentShareRule> findAgentShareRuleByProfitType(String profitType);
	
}
