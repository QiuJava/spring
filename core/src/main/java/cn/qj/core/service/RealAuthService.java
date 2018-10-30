package cn.qj.core.service;

import org.springframework.data.domain.Page;

import cn.qj.core.entity.RealAuth;
import cn.qj.core.pojo.qo.RealAuthQo;

/**
 * 实名认证服务
 * 
 * @author Administrator
 *
 */
public interface RealAuthService {

	/**
	 * 获取实名认证
	 * 
	 * @param realAuthId
	 * @return
	 */
	RealAuth get(Long realAuthId);

	/**
	 * 保存
	 * 
	 * @param realAuth
	 */
	void save(RealAuth realAuth);

	/**
	 * 获取用户实名认证列表用于页面分页查询
	 * 
	 * @param qo
	 * @return
	 */
	Page<RealAuth> page(RealAuthQo qo);

	/**
	 * 实名认证审核
	 * 
	 * @param id
	 * @param state
	 * @param remark
	 */
	void autid(Long id, Integer state, String remark);

}
