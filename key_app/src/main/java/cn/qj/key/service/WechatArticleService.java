package cn.qj.key.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.qj.key.entity.WechatArticle;
import cn.qj.key.repository.WechatArticleDao;

/**
 * 微信文章
 * 
 * @author Qiujian
 * @date 2019/01/29
 */
@Service
public class WechatArticleService {

	@Autowired
	private WechatArticleDao dao;

	public void save(WechatArticle article) {
		dao.save(article);
	}

	public List<WechatArticle> getByStatus(Integer valid) {
		return dao.findByStatus(valid);
	}

}
