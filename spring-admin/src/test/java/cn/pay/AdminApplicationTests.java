package cn.pay;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.Setter;

/**
 * 使用SpringBoot测试，再多profile下需要在设置环境变量 spring.profile.active=dev
 * 
 * @author Qiujian
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ConfigurationProperties(prefix = "test.sms")
public class AdminApplicationTests {

	@Setter
	private String url;

	@Test
	public void contextLoads() {
		System.out.println(url);
	}

}
