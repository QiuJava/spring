package cn.eeepay.framework.service;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.BusinessProductDefine;
import cn.eeepay.framework.model.JoinTable;

public interface BusinessProductDefineService {

	public List<BusinessProductDefine> selectAllInfo(String agentNo);

	public List<BusinessProductDefine> selectByCondition(Page<BusinessProductDefine> page,BusinessProductDefine bpd);

	public BusinessProductDefine selectById(String id);
	
	public BusinessProductDefine selectBybpId(String bpId);

	public List<BusinessProductDefine> selectBpTeam();

	public List<BusinessProductDefine> selectOtherProduct(String i);

	public int insertOrUpdate(Map<String, Object> info);

	public Map<String, Object> selectLinkInfo(String bpId);

	public Map<String, Object> selectDetailById(String id);

	public boolean selectRecord(Integer bpId);

	public List<JoinTable> selectProductByAgent(String entityId);

	public String selectTeamIdByBpId(String bpId);

	/**
	 * 根据代理商所代理商的业务产品列出所有的组织信息
	 * @param agentNo
	 * @return
	 */
	Map<String, String> selectTeamByAgentAndBp(String agentNo);
}
