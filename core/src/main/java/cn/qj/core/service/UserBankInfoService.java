package cn.qj.core.service;

import cn.qj.core.entity.UserBankInfo;

/**
 * 用户银行卡信息服务
 * 
 * @author Qiujian
 * @date 2018/11/01
 */
public interface UserBankInfoService {

	/**
	 * 获取登录用户的银行卡信息
	 * 
	 * @param id
	 * @return
	 */
	UserBankInfo getByLoginInfoId(Long id);

	/**
	 * 保存
	 * 
	 * @param userBankInfo
	 */
	void save(UserBankInfo userBankInfo);

}
