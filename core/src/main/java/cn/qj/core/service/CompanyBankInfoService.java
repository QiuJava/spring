package cn.qj.core.service;

import java.util.List;

import cn.qj.core.common.PageResult;
import cn.qj.core.entity.CompanyBankInfo;
import cn.qj.core.pojo.qo.CompanyBankInfoQo;

/**
 * 公司银行卡服务
 * 
 * @author Qiujian
 * @date 2018/11/01
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
