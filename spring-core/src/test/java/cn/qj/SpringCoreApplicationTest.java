package cn.qj;

import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.qj.config.security.AuthenticationProviderImpl;
import cn.qj.entity.LoginUser;
import cn.qj.service.LoginUserServiceImpl;

/**
 * 应用测试
 * 
 * @author Qiujian
 * @date 2019年3月26日
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringCoreApplicationTest {
	
	@Autowired
	private LoginUserServiceImpl loginUserService;

	@Test
	public void contextLoads() {
		LoginUser loginUser = new LoginUser();
		loginUser.setUsername("admin");
		loginUser.setPassword(AuthenticationProviderImpl.B_CRYPT.encode("123"));
		loginUser.setUserStatus(LoginUser.NORMAL);
		loginUser.setPasswordExpiration(DateUtils.addMonths(new Date(), 6));
		loginUserService.save(loginUser);
	}

}
