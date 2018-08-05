package cn.pay;

import java.util.Date;
import java.util.List;

import javax.jms.Queue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;

import cn.pay.core.consts.SysConst;
import cn.pay.core.domain.sys.IpLog;
import cn.pay.core.domain.sys.LoginInfo;
import cn.pay.core.domain.sys.Role;
import cn.pay.core.domain.sys.SystemDictionary;
import cn.pay.core.domain.sys.SystemDictionaryItem;
import cn.pay.core.service.LoginInfoService;
import cn.pay.core.service.RoleService;
import cn.pay.core.service.SystemDictionaryItemService;
import cn.pay.core.service.SystemDictionaryService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LoanApplicationTests {

	@Autowired
	private JmsMessagingTemplate jmsMessagingTemplate;
	@Autowired
	private Queue ipLogQueue;
	@Autowired
	private Queue loginInfoQueue;

	@Autowired
	private SystemDictionaryService systemDictionaryService;
	@Autowired
	private SystemDictionaryItemService systemDictionaryItemService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private LoginInfoService loginInfoSerivce;

	@Test
	public void initData() throws Exception {
		List<LoginInfo> listLoginInfo = loginInfoSerivce.listAll();
		for (LoginInfo loginInfo : listLoginInfo) {
			Thread.sleep(1000);
			for (int i = 0; 0 < 500; i++) {
				IpLog ipLog = new IpLog();
				ipLog.setIp("127.0.0." + i);
				ipLog.setUsername(loginInfo.getUsername());
				ipLog.setUserType(LoginInfo.MANAGER);
				ipLog.setLoginTime(new Date());
				ipLog.setLoginState(IpLog.LOGIN_SUCCESS);
				jmsMessagingTemplate.convertAndSend(ipLogQueue, JSON.toJSONString(ipLog));
			}
		}
	}

	public void initManageLoginInfo() {
		List<Role> all = roleService.getAll();
		// 500个后台用户
		for (int i = 0; i < 500; i++) {
			LoginInfo info = new LoginInfo();
			info.setAdmin(false);
			info.setPassword(new BCryptPasswordEncoder().encode(SysConst.LOGIN_PASSWORD));
			info.setUsername(i + "后台用户" + i);
			info.setUserType(LoginInfo.MANAGER);
			info.setRoles(all);
			jmsMessagingTemplate.convertAndSend(loginInfoQueue, JSON.toJSONString(info));
		}
	}

	public void initDictionary() {
		// 200个数据字典 一个字典20个明细
		for (int i = 0; i < 200; i++) {
			SystemDictionary sd = new SystemDictionary();
			sd.setIntro(i + "字典" + i);
			sd.setSequence(i);
			sd.setSn("system_dictionary_" + i);
			sd.setTitle(i + "我是字典" + i);
			SystemDictionary dictionary = systemDictionaryService.save(sd);
			for (int j = 0; j < 20; j++) {
				SystemDictionaryItem item = new SystemDictionaryItem();
				item.setIntro(i + "字典明细" + i);
				item.setSequence(i);
				item.setSystemDictionaryId(dictionary.getId());
				item.setTitle(i + "我是字典明细" + i);
				systemDictionaryItemService.update(item);
			}
		}
	}

	@Test
	public void test() throws Exception {
		System.out.println("应用测试启动");
	}
}
