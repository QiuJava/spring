package cn.qj.key.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.qj.key.entity.WechatUser;

/**
 * 微信用户数据操作
 * 
 * @author Qiujian
 * @date 2019/01/29
 */
public interface WechatUserDao extends JpaRepository<WechatUser, String> {

}
