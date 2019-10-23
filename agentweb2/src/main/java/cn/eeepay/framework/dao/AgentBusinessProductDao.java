package cn.eeepay.framework.dao;

import java.util.List;

import cn.eeepay.framework.util.WriteReadDataSource;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import cn.eeepay.framework.model.BusinessProductDefine;

@WriteReadDataSource
public interface AgentBusinessProductDao {

	@Select("select bp_id from agent_business_product where agent_no = #{agentNo}")
	@ResultType(java.lang.String.class)
	List<String> selectProductByAgent(@Param("agentNo")String agentNo);
	/**
	 * gaowei
	 * @param agentNo
	 * @return
	 */
	@Select("SELECT bpd.* FROM business_product_define bpd,agent_business_product abp WHERE bpd.bp_id=abp.bp_id AND abp.status='1' and  abp.agent_no=#{agentNo}")
	@ResultType(BusinessProductDefine.class)
	List<BusinessProductDefine> queryAgentProduct(@Param("agentNo")String agentNo);
	
	@Select("select id from agent_business_product where bp_id= #{bpId} limit 1")
	@ResultType(java.lang.Integer.class)
	Integer findIdByBp(@Param("bpId")String bpId);
	
	@Select("SELECT d.* FROM agent_info a LEFT JOIN agent_business_product p ON a.agent_no = p.agent_no  "
			+"LEFT JOIN business_product_define d ON  d.bp_id = p.bp_id WHERE a.agent_no=#{agentNo} and d.bp_type=#{merType} and p.status='1' "
			+ "and date(d.sale_starttime) <= curdate() and date(d.sale_endtime) >= curdate() and d.allow_web_item='1' " +
			"and d.effective_status = 1 ")
	List<BusinessProductDefine> queryMerProduct(@Param("agentNo")String agentNo,@Param("merType")String merType);
}
