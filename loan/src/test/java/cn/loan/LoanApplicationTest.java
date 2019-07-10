package cn.loan;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 应用测试
 * 
 * @author qiujian
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Profile("dev")
public class LoanApplicationTest {

	@Test
	public void contextLoads() {
	}

}
