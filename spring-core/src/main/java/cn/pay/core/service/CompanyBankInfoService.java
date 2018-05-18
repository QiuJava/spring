package cn.pay.core.service;

import java.util.List;

import cn.pay.core.domain.business.CompanyBankInfo;
import cn.pay.core.obj.qo.CompanyBankInfoQo;
import cn.pay.core.obj.vo.PageResult;

/**
 * 公司银行账号信息
 * 
 * @author Administrator
 *
 */
public interface CompanyBankInfoService {

	/**
	 * 获取公司所有银行账号信息
	 * 
	 * @return
	 */
	List<CompanyBankInfo> list();

	/**
	 * 更新信息
	 * 
	 * @param info
	 */
	void update(CompanyBankInfo info);

	/**
	 * 获取公司银行账号信息列表用于页面分页查询
	 * 
	 * @param qo
	 * @return
	 */
	PageResult page(CompanyBankInfoQo qo);

	/**
	 * 获取公司账户信息
	 * 
	 * @param bankInfoId
	 * @return
	 */
	CompanyBankInfo get(Long bankInfoId);

}
