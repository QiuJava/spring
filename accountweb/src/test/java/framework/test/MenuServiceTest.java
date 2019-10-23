package framework.test;


import javax.annotation.Resource;

import org.junit.Test;

import cn.eeepay.framework.model.bill.SysMenu;
import cn.eeepay.framework.service.bill.RedisService;
import cn.eeepay.framework.service.bill.SysMenuService;
import cn.eeepay.framework.util.Constants;
public class MenuServiceTest extends BaseTest {
	@Resource
	public RedisService redisService;
	@Resource
	public SysMenuService sysMenuService;
	@Test
	public void testaaa() throws Exception{
		SysMenu sysMenu = sysMenuService.findSysMenuById(277);
		boolean isMember = redisService.selectIsMemberForSet(Constants.sys_menu_not_blank_url_list_redis_key, sysMenu);
		System.out.println(isMember);
	}
}
