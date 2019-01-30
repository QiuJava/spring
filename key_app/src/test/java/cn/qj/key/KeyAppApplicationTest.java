package cn.qj.key;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.qj.key.entity.Dict;
import cn.qj.key.entity.WechatArticle;
import cn.qj.key.service.DictService;
import cn.qj.key.service.WechatArticleService;
import cn.qj.key.util.DateTimeUtil;

/**
 * 测试
 * 
 * @author Qiujian
 * @date 2019/01/17
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class KeyAppApplicationTest {

	@Autowired
	private DictService dictSerivice;

	@Autowired
	private WechatArticleService wechatArticleService;
	
	@Test
	public void testArticle() {
		WechatArticle article = new WechatArticle();
		article.setPicUrl("http://qjwxkf.nat300.top/图片1.png");
		article.setTitle("图文消息测试");
		article.setDescription("图文消息测试，测试测试测试");
		article.setUrl("http://qjwxkf.nat300.top/图片1.png");
		article.setCreateTime(DateTimeUtil.getDate());
		article.setUpdateTime(DateTimeUtil.getDate());
		article.setStatus(WechatArticle.VALID);
		wechatArticleService.save(article);
		article = new WechatArticle();
		article.setPicUrl("http://qjwxkf.nat300.top/图片1.png");
		article.setTitle("图文消息测试");
		article.setDescription("图文消息测试，测试测试测试");
		article.setUrl("http://qjwxkf.nat300.top/图片1.png");
		article.setCreateTime(DateTimeUtil.getDate());
		article.setUpdateTime(DateTimeUtil.getDate());
		article.setStatus(WechatArticle.VALID);
		wechatArticleService.save(article);
	}

	@Test
	public void testDict() {
		Dict dict = new Dict();
		dict.setDictName("classinfo");
		dict.setDictValue("北京java一期\n上海java二期 \n深圳java三期");
		dict.setIntro("开班信息");
		dict.setState(Dict.VALID);
		dictSerivice.save(dict);
		dict = new Dict();
		dict.setDictName("address");
		dict.setDictValue("北京\n上海\n深圳");
		dict.setIntro("校区地址");
		dict.setState(Dict.VALID);
		dictSerivice.save(dict);
	}

	@Test
	public void contextLoads() {

	}

}
