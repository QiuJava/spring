package cn.pay.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit4.SpringRunner;

import cn.pay.core.dao.LoginInfoRepository;
import cn.pay.core.domain.sys.LoginInfo;

@RunWith(SpringRunner.class)
@SpringBootTest
@Profile("dev")
public class AdminApplicationTests {
	
	@Autowired
	private LoginInfoRepository repository;

	@Test
	public void test() throws Exception {
		LoginInfo loginInfo = repository.findByUsername("admin");
		System.out.println(loginInfo);
	}
}
