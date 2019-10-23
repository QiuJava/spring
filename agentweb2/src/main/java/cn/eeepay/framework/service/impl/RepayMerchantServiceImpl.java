package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.RepayMerchantDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.exception.AgentWebException;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.RepayMerchantInfo;
import cn.eeepay.framework.model.YfbBalance;
import cn.eeepay.framework.model.YfbCardManage;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.service.RepayMerchantService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 超级还款用户管理
 * @author mays
 * @date 2017年10月31日
 */
@Service("repayMerchantService")
public class RepayMerchantServiceImpl implements RepayMerchantService {

	@Resource
	private RepayMerchantDao repayMerchantDao;

	@Resource
	private AgentInfoService agentInfoService;

	/**
	 * 用户查询
	 * @author mays
	 * @date 2017年10月31日
	 */
	public List<RepayMerchantInfo> selectRepayMerchantByParam(Page<RepayMerchantInfo> page,
			RepayMerchantInfo info) {
		return repayMerchantDao.selectRepayMerchantByParam(page, info);
	}

	/**
	 * 用户基本资料查询
	 * @author mays
	 * @date 2017年10月31日
	 */
	public RepayMerchantInfo queryRepayMerchantByMerchantNo(String merchantNo) {
		checkMerchant(merchantNo);
		return repayMerchantDao.queryRepayMerchantByMerchantNo(merchantNo);
	}

	/**
	 * 用户绑定的贷记卡和借记卡查询
	 * @author mays
	 * @date 2017年10月31日
	 */
	public List<YfbCardManage> queryCardByIdCardNo(String idCardNo) {
		return repayMerchantDao.queryCardByIdCardNo(idCardNo);
	}

	/**
	 * 用户开户
	 * @author mays
	 * @date 2017年10月31日
	 */
	public int updateRepayMerchantAccountStatus(String merchantNo) {
		checkMerchant(merchantNo);
		return repayMerchantDao.updateRepayMerchantAccountStatus(merchantNo);
	}

	@Override
	public List<YfbBalance> queryBalanceByMerchantNo(String merchantNo) {
		checkMerchant(merchantNo);
		return repayMerchantDao.queryBalanceByMerchantNo(merchantNo);
	}

	public void checkMerchant(String merchantNo){
		if(StringUtils.isBlank(merchantNo)){
			throw new AgentWebException("商户号不能为空");
		}
		//根据商户号获取对应的代理商编号,若没有结果,返回
		RepayMerchantInfo merchantInfo = repayMerchantDao.selectByMer(merchantNo);
		if(merchantInfo == null){
			throw new AgentWebException("无效的商户号");
		}
		//和当前登陆的代理商做比较,若不相等,返回
		AgentInfo loginAgent = agentInfoService.selectByPrincipal();
		if(!merchantInfo.getAgentNode().startsWith(loginAgent.getAgentNode())){
			throw new AgentWebException("商户和代理商不匹配");
		}
	}

}
