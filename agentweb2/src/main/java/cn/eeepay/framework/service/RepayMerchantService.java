package cn.eeepay.framework.service;

import java.util.List;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.RepayMerchantInfo;
import cn.eeepay.framework.model.YfbBalance;
import cn.eeepay.framework.model.YfbCardManage;

/**
 * 超级还款用户管理
 * @author mays
 * @date 2017年10月31日
 */
public interface RepayMerchantService {

	/**
	 * 用户查询
	 * @author mays
	 * @date 2017年10月31日
	 */
	List<RepayMerchantInfo> selectRepayMerchantByParam(Page<RepayMerchantInfo> page, RepayMerchantInfo info);

	/**
	 * 用户基本资料查询
	 * @author mays
	 * @date 2017年10月31日
	 */
	RepayMerchantInfo queryRepayMerchantByMerchantNo(String merchantNo);

	/**
	 * 用户绑定的贷记卡和借记卡查询
	 * @author mays
	 * @date 2017年10月31日
	 */
	List<YfbCardManage> queryCardByIdCardNo(String idCardNo);

	/**
	 * 用户开户
	 * @author mays
	 * @date 2017年10月31日
	 */
	int updateRepayMerchantAccountStatus(String merchantNo);

    List<YfbBalance> queryBalanceByMerchantNo(String merchantNo);
}
