package cn.eeepay.framework.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.ActivityDetail;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.CashBackDetail;
import cn.eeepay.framework.model.MerchantIncomeBean;
import cn.eeepay.framework.model.TerminalInfo;
import cn.eeepay.framework.model.UserCouponBean;

public interface ActivityDetailService {
	/**
	 * 欢乐送业务活动查询
	 * @param page
	 * @param activityDetail
	 * @return
	 */
	List<ActivityDetail> selectActivityDetail(Page<ActivityDetail> page,String loginAgentNo,ActivityDetail activityDetail);

	/**
	 * 查询是否有参加活动
	 * @param agentNode
	 * @return
	 */
	List<TerminalInfo> selectTermi(String phone);
	
	void exportExcel(Page<ActivityDetail> page, ActivityDetail activityDetail,String loginAgentNo, HttpServletResponse response) throws UnsupportedEncodingException, IOException;

	/**
	 * 查询欢乐送提现服务ID
	 * @param string
	 * @return
	 */
	String getServiceId(String activityCode);

	/**
	 * 欢乐返活动查询
	 * @param page
	 * @param loginAgent
	 * @param activityDetail
	 */
	List<ActivityDetail> selectHappyBackDetail(Page<ActivityDetail> page, AgentInfo loginAgent, ActivityDetail activityDetail);

	/**
	 * 欢乐返活动导出
	 * @param activityDetail
	 * @param loginAgent
	 * @param response
	 */
	void exportHappyBack(Page<ActivityDetail> page, ActivityDetail activityDetail, AgentInfo loginAgent, HttpServletResponse response)  throws IOException;

	/**
	 * 统计总金额
	 * @param activityDetail
	 * @return
	 */
	Map<String,Object> selectTotalMoney(ActivityDetail activityDetail, AgentInfo loginAgent);
	
	/** 
	 * 分页查询注册法和登陆返的券数据
	 * @param userCouponBean    查询条件
	 * @param currentAgentNo	当前登陆的代理商
     * @param page               分页信息 
     * @return	查询数据
	 */
    List<UserCouponBean> listUserCouponsByPage(UserCouponBean userCouponBean, String currentAgentNo, Page<UserCouponBean> page);

	/**
	 * 按条件统计注册返,签到返的 赠送金额 可用金额 已使用金额 过期金额的数据
	 * @param userCouponBean    查询条件
	 * @param currentAgentNo    当前登陆的代理商
	 */
    UserCouponBean countUserCoupons(UserCouponBean userCouponBean, String currentAgentNo);

	/**
	 *	查询云闪付商户收益
	 * @param bean 查询条件
	 * @param page	分页信息
	 * @return
	 */
    List<MerchantIncomeBean> listMerchantIncome(MerchantIncomeBean bean, Page<MerchantIncomeBean> page);
    
    /**
     * 根据欢乐返类型查询欢乐返子类型
     * 
     * @param activityCode
     * @return
     */
	List<Map<String, Object>> queryByactivityTypeNoList(String activityCode);

	ActivityDetail getHappyBackDetailById(int parseInt);

	List<CashBackDetail> queryAgentReturnCashDetailAll(Integer id, int i);

	List<CashBackDetail> queryAgentReturnCashDetailAll(String zeroOrder, Integer id, int i);

	CashBackDetail queryAgentReturnCashDetailAll(String zeroOrder, Integer id, int i, String entityId);

	CashBackDetail queryAgentReturnCashDetailAll(Integer id, int i, String entityId);

}
