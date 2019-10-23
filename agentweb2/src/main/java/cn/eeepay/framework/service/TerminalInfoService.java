package cn.eeepay.framework.service;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface TerminalInfoService {

    /**
     * 活动考核机具查询
     * @param terActivityCheck
     * @return
     */
    List<TerActivityCheck> selectTerActivityCheck(Page<TerActivityCheck> page,TerActivityCheck terActivityCheck);

    void  exportTerActivityCheck(HttpServletResponse response,TerActivityCheck terActivityCheck);

    public int insert(TerminalInfo record);

    public int insertSelective(List<TerminalInfo> record);

    public int updateByPrimaryKey(TerminalInfo record);

    public List<TerminalInfo> selectAllInfo(Page<TerminalInfo> page);

    public List<TerminalInfo> selectUserCodeLists(String agentNode);

    public TerminalInfo selectObjInfo(Long id,String agentNo);

    public List<TerminalInfo> selectByParam(Page<TerminalInfo> page, TerminalInfo terminalInfo);

    public List<TerminalInfo> selectByAddParam(TerminalInfo terminalInfo);

    List<TerminalInfo> selectAllInfoBymerNoAndBpId(String merNo, String bpId);

    //回收
    public int recoverById(PaUserInfo paUserInfo, String id);

    //解分
    public int updateSolutionById(TerminalInfo record);

    public  int updateTer(String agentNo, String userCode, String sn, String entityUserCode);

    //解绑
    //public int updateUnbundlingById(TerminalInfo record);

    //public int updateBundlingById(TerminalInfo record);

    public TerminalInfo selectBySameData(TerminalInfo record);

    /*
     * 校验SN三种状态
     */
    public TerminalInfo checkSn(String agentNo, String sn, String status);

    public TerminalInfo querySn(String sn);

    public TerminalInfo checkAgentSn(String agentNo, String sn);

    int updateBundlingItem(TerminalInfo record);

    /**
     * 228tgh查询所有机具活动
     *
     * @return
     */
    public List<Map<String, String>> selectAllActivityType();

    /**
     * 机具批量下发308tgh
     *
     * @param snStart1
     * @param snEnd1
     * @param agentNo
     * @return
     */
    public Map<String, Object> UpdateTerminalInfoBySn(String snStart1, String snEnd1, String agentNo, String userCode);

    public String selectAgentNode(String sn);

    public Map<String, Object> selectPaTerInfo(String sn);

    public List<TerminalInfo> selectByUserCode(Page<PaOrder> page, String userCode, TerminalInfo info);

    public List<TerminalInfo> selectByOrderNo(Page<TerminalInfo> page, String orderNo, String agentNode);


    /**
     * 查找订单对应的机具
     *
     * @param userCode
     * @param snStart
     * @param snEnd
     * @return
     */
    public List<TerminalInfo> queryTerminalByOrder(String userCode, String snStart, String snEnd);

    /**
     * 机具解绑
     * @param id
     * @return
     */
	Map<String, Object> untiedById(Long id);

	/**
	 * 查询当前登录代理商所有的商户编号及名称,一级查所有,二级只查所属
	 * @return
	 */
	List<Map<String, String>> selectAllMerchantInfo();

	/**
	 * 绑定机具
	 * @param merNo
	 * @param sn
	 * @param id
	 * @return
	 */
	Map<String, Object> updateBindingTerminal(String merNo, String sn,String id,String bpId);
	
	/**
	 * 三方机具查询
	 * @param page
	 * @param terminalInfo
	 * @param agentNode
	 * @param currAgentNo
	 * @return
	 */
	public List<TerminalInfo> selectByParamThree(Page<TerminalInfo> page, TerminalInfo terminalInfo, String agentNode,
			String currAgentNo);


    String selectMerchantNameByMerchantNo(String merchantNo);

    MerchantInfo selectMerchantDetail(String merchantNo);

    HardwareProduct selectHardwareProductBySn(String sn);

	public List<HardwareAcvitityType> getActivityType(List<TerminalInfo> snList);

	public int updateActivity(String sn, String activityTypeNo);

	public UpdateActivityBatchResult updateActivityBatch(List<TerminalInfo> snList, String activityTypeNo);

	public List<HardwareAcvitityType> getActivityType(String sn);
    /**
     * 导出机具信息
     * @param terminalInfo
     */
    void  exportTerinalInfo(HttpServletResponse response, HttpServletRequest request, TerminalInfo terminalInfo);


    TerminalInfo selectBySn(String sn);

    void addAgentActivity(String sn, String agentNo);

    int countAgentNodeAndActivityTypeNo(String agentNode, String activityTypeNo);
}
