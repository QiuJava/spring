package cn.qj.key.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.qj.key.entity.WechatArticle;

/**
 * 微信文章数据操作
 * 
 * @author Qiujian
 * @date 2019/01/29
 */
public interface WechatArticleDao extends JpaRepository<WechatArticle, Long> {

	/**
	 * 根据状态查找微信文章
	 * 
	 * @param valid
	 * @return
	 */
	List<WechatArticle> findByStatus(Integer valid);

}
