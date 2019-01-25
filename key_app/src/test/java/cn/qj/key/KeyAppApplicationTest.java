package cn.qj.key;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.qj.key.util.WechatUtils;

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
	private WechatUtils util;

	@Test
	public void contextLoads() {
		System.out.println(util.getAppId());
		System.out.println(util.getAppsecret());
	}

}
