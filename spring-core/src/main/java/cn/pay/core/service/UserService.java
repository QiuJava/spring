package cn.pay.core.service;

import cn.pay.core.domain.sys.User;

/**
 * 用户服务
 * 
 * @author Qiujian
 *
 */
public interface UserService {
	
	User getByLoginInfoId(Long id);

}
