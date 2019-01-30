package cn.qj.key.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.qj.key.entity.WechatUser;
import cn.qj.key.repository.WechatUserDao;

/**
 * 微信用户服务
 * 
 * @author Qiujian
 * @date 2019/01/29
 */
@Service
public class WechatUserService {

	@Autowired
	private WechatUserDao dao;

	public WechatUser getByOpenId(String wechatUserOpenId) {
		return dao.findOne(wechatUserOpenId);
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public void save(WechatUser wechatUser) {
		dao.save(wechatUser);
	}

}
