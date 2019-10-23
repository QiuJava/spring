package framework.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.eeepay.framework.model.bill.SysDict;
import cn.eeepay.framework.service.bill.RedisService;
import cn.eeepay.framework.service.bill.SysDictService;
import cn.eeepay.framework.service.bill.impl.RedisServiceImpl;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.RedisUtil;

public class RedisTest extends BaseTest{
	
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
//	@Resource
//	private RedisServiceImpl c;
	@Resource
	private RedisUtil redisUtil;
	@Resource
	private SysDictService sysDictService;
	@Resource
	private RedisService redisService;
//	@Resource
//	private CustomDao customDao;
	@Test
	public void test() throws Exception{
		
		logger.info("=========start===========");
		/*
		      String的操作*/
		redisService.insertString("java:string:english", "java_insert_string");
		logger.info("查询redis中的值:{}",redisService.select("java:string:english"));
		redisService.insertString("java:string:cn", "redis学习");
		logger.info("查询redis中的值:{}",redisService.select("java:string:cn"));
		
		logger.info("查询redis中的xxx值:{}",redisService.keys("*"));	
		
		
		/*
            	列表操作
        	
		List<Object> list = new ArrayList<Object>();
		list.add("redis学习");
		list.add("123456789");
		c.insertList("java:list", list,(long) (60*60)); //60分钟
		logger.info("查询redis中的值:{}",c.select("java:list"));
		//创建一个列表之后 再次添加一个Map到列表里面
		Map<String,String> map = new HashMap<String,String>();
		map.put("A", "sss");
		map.put("AB", "sssdf");
		c.insertList("java:list", map.toString(),(long) (60*60));
		logger.info("查询redis中的值:{}",c.select("java:list"));
		*/    
		
		/*
              	哈希表操作
            	
		c.insertHash("java:map", "key1", "123");
		//得到这个Hash类型  再次放入一个对象
		MsgEntity bean = new MsgEntity();
		bean.setStatus("123");
		bean.setMsg("ABC");
		logger.info("查询redis中的值:{}",c.select("java:map"));
		c.insertHash("java:map", "key2", bean.toString());
		logger.info("查询redis中的值:{}",c.select("java:map"));
		
		*/  
		/*
		       读取redis中的数据
	    logger.info("查询redis中的值:{}",c.select("java:map"));
	    Object map = c.select("java:map");
	    if(map instanceof Map){
	    	Map<String,Object> resultMap = (Map<String,Object>)map;
	    	
	    	Iterator<Object> it = resultMap.values().iterator();
	    	while(it.hasNext())
	    		logger.info("遍历Map:{}",it.next());
	    }
	    */
		
		
        //删除数据
//		logger.info("查询redis中的值:{}",c.select(Constants.sys_dict_list_redis_key));
//		List<String> keys = new ArrayList<String>();
//		keys.add(Constants.sys_dict_list_redis_key);
//		c.delete(keys);
//		//logger.info("查询redis中的值:{}",c.select(Constants.sys_config_list_redis_key));
//		
		logger.info("=========end===========");
	}
//	@Test
	public void test1(){
		try {
//			List<SysDict> sysDictList = sysDictService.getAllConfigListByRedis();
			List<SysDict> sysDictList = sysDictService.findSysDictGroup("sys_subject_type");
//			sysDictList.stream().filter(sysDict -> sysDict.)
			logger.info("=========end==========="+sysDictList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
