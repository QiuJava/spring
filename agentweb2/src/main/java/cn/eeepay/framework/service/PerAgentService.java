package cn.eeepay.framework.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;

import cn.eeepay.framework.model.PaAfterSale;
import cn.eeepay.framework.model.PaCashBackDetail;
import cn.eeepay.framework.model.PaSnBack;
import cn.eeepay.framework.model.PaUserInfo;

/**
 * @author MXG
 * create 2018/07/11
 */
public interface PerAgentService {
	
	/**
	 * 盟友活动明细查询
	 * @param info
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	Map<String, Object> selectPaCashBackDetail(PaCashBackDetail info, int pageNo, int pageSize);

	/**
	 * 盟主返现明细导出
	 * @param info
	 * @param response
	 * @param request
	 */
	void exportCashBackDetail(PaCashBackDetail info, HttpServletResponse response, HttpServletRequest request) throws Exception ;

    /**
     * 查找用户（不分页）
     * @param info
     * @return
     */
    List<PaUserInfo> selectAllList(PaUserInfo info);

    /**
     * 统计从商户转过来的用户数量
     * @param info
     * @return
     */
    long countUserFromMer(PaUserInfo info);

    /**
     * 根据代理商编号查询
     * @param entityId
     * @return
     */
    Map<String, String> selectByAgentNo(String entityId);

    /**
     * 代理商后台修改密码时同步修改人人代理客户端登录密码
     * @param agentNo
     * @param newPwd
     * @return
     */
    int updatePassword(String agentNo, String newPwd);

    /**
     * 调整分润等级
     * @param param
     * @return
     */
    Map<String,Object> updateShareLevel(String param);

    /**
     * 查询所有下级盟主
     * @param info
     * @param pageNo
     * @param pageSize
     * @return
     */
    Map<String, Object> queryUserByParam(PaUserInfo info, int pageNo, int pageSize);

    public List<PaUserInfo> selectChildPaUser(PaUserInfo info);


    /**
     * 导出数据
     * @param info
     * @param response
     * @param request
     */
    void exportPerAgentUser(PaUserInfo info, HttpServletResponse response, HttpServletRequest request) throws Exception;

    /**
     * 根据登录代理编号查询出人人代理用户信息
     * @param agentNo
     * @return
     */
    PaUserInfo selectUserByAgentNo(String agentNo);

    List<Map> getShareLevelList();

    /**
     * 查用户信息
     * @param userCode
     * @return
     */
	PaUserInfo selectByUserCode(String userCode);

	/**
	 * 查结算卡信息
	 * @param userCode
	 * @return
	 */
	Map<String,Object> selectPaUserCardByUserCode(String userCode);

	Integer updateCanProfitChange(String canProfitChange, String userCode);

	Integer selectStatus();

	/**
	 * 售后订单
	 * @param info
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	Map<String, Object> selectPaAfterSale(PaAfterSale info, int pageNo, int pageSize);

	/**
	 * 售后订单导出
	 * @param info
	 * @param response
	 * @param request
	 */
	void exportAfterSale(PaAfterSale info, HttpServletResponse response, HttpServletRequest request) throws Exception ;
	/**
	 * 导出SN回拨记录
	 * @param info
	 * @param response
	 * @param request
	 * @throws Exception
	 */
	void exportSnBack(PaSnBack info, HttpServletResponse response, HttpServletRequest request) throws Exception ;

	/**
	 * sn回拨记录查询
	 * @param baseInfo
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	Map<String, Object> selectSnBackByParam(PaSnBack baseInfo, int pageNo, int pageSize);

	/**
	 * 回拨记录号查询回拨的机具
	 * @param orderNo
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	List<Map<String,Object>> selectSnByOrder(String orderNo);

	/**
	 * 确定接收,拒绝接收
	 * @param orderNo
	 * @return
	 */
	Integer updateStatus(String orderNo,String status);

	/**
	 * 盟主交易表
	 * @param orderNo
	 * @return
	 */
	Map<String, Object> selectFromPaTransInfo(String orderNo);

	/**
	 * 售后订单立即处理
	 * @param jsonObject
	 * @return
	 */
	Integer updateNowAfterSale(JSONObject jsonObject);

	Map<String, Object> selectFromPaMerInfoByMerchantNo(String merchantNo);

	List<Integer> selectShareLevelList(String agentOem, String agentShareLevel);

	String selectPaTerminalBackByOrderNo(String userNode, String orderNo);

	/**
	 * md5 key
	 * @param string
	 * @return
	 */
	String selectMd5Key(String string);

	/**
	 * 根据v2数据库商户号在盟主库查询商户
	 * @param merchantNo
	 * @return
	 */
	Map<String, String> selectByMerchantNo(String merchantNo);
}
