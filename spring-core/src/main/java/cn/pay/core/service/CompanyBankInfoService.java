package cn.pay.core.service;

import java.util.List;

import cn.pay.core.domain.business.CompanyBankInfo;
import cn.pay.core.obj.qo.CompanyBankInfoQo;
import cn.pay.core.obj.vo.PageResult;

/**
 * 公司银行账号信息服务
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
	 * 更新信息 并进行乐观锁检查
	 * 
	 * @param info
	 */
	void update(CompanyBankInfo info);

	/**
	 * 根据公司银行账号查询条件对象获取页面结果集
	 * 
	 * @param qo
	 * @return
	 */
	PageResult page(CompanyBankInfoQo qo);

	/**
	 * 根据局公司银行信息ID获取公司账户信息
	 * 
	 * @param bankInfoId
	 * @return
	 */
	CompanyBankInfo get(Long bankInfoId);

}
