//package framework.test;
//
//import java.util.List;
//
//import javax.annotation.Resource;
//
//import org.junit.Test;
//
//import cn.eeepay.framework.dao.InputAccountDao;
//import cn.eeepay.framework.model.InputAccount;
//
//public class InputAccountServiceTest extends BaseTest {
//	@Resource
//	private InputAccountDao inputAccountDao;
//	@Test
//	public void test() {
//		List<InputAccount> list=inputAccountDao.findInputAccountList();
//		for(InputAccount account:list){
//			System.out.println(account.getAccountName()+" "+account.getSubject().getSubjectName());
//		}
//	}
//
//}
