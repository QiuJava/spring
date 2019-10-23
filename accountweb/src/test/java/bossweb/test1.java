package bossweb;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import cn.eeepay.framework.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class test1 {

	public static void main(String[] args) throws ParseException {
//		System.out.println(StringUtils.leftPad("129018", 10, "0"));
//		System.out.println(StringUtils.join("abc".concat("123")));
		//org.springframework.scheduling.TaskScheduler
		//org.apache.commons.pool2.impl.GenericObjectPoolConfig
//		org.springframework.web.servlet.DispatcherServlet
		//SessionRepositoryFilter

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String s = "2018-04-25 08:37:00";
//		Date parse = sdf.parse(s);
//		System.out.println(DateUtil.isZfTomorrow(parse));

		SimpleDateFormat dataTimeFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String s = "20180703105313";
		Date parse = dataTimeFormat.parse(s);

		String format = sdf.format(parse);
		System.out.println(format);
	}
	//@Test
	public  void abc() {
		  List<String> list = new ArrayList<String>();
		  list.add("JavaWeb编程词典");        //向列表中添加数据
		  list.add("Java编程词典");        //向列表中添加数据
		  list.add("C#编程词典");         //向列表中添加数据
		  list.add("ASP.NET编程词典");        //向列表中添加数据
		  list.add("VC编程词典");         //向列表中添加数据
		  list.add("SQL编程词典");        //向列表中添加数据
		  Iterator<String> its = list.iterator();     //获取集合迭代器
		  System.out.println("集合中所有元素对象：");
		  while (its.hasNext()) {        //循环遍历集合
		   System.out.print(its.next() + "  ");     //输出集合内容
		  }
		  List<String> subList = list.subList(4, 10);    //获取子列表
		  System.out.println("\n截取集合中部分元素：");
		  Iterator it = subList.iterator();
		  while (it.hasNext()) {
		   System.out.print(it.next() + "  ");
		  }
	}
	//@Test
	public  void abc2() {
		BigDecimal amount = new BigDecimal("100");
		BigDecimal fee  = new BigDecimal("-0.35");
		BigDecimal feeAmount = amount.subtract(fee);
		System.out.println(feeAmount);
	}
	
	@Test
	public  void abc3() {
		String orderId = getOrderIdByUUId();
		System.out.println(orderId);
		System.out.println(System.currentTimeMillis());
		System.out.println(System.nanoTime());
		System.out.println(UUID.randomUUID().toString());
	}
	
	
	public static String getOrderIdByUUId() {
		int machineId = 1;//最大支持1-9个集群机器部署
			int hashCodeV = UUID.randomUUID().toString().hashCode();
			if(hashCodeV < 0) {//有可能是负数
				hashCodeV = - hashCodeV;
			}
		// 0 代表前面补充0     
		// 20 代表长度为20     
		// d 代表参数为正数型
		return machineId+String.format("%020d", hashCodeV);
	}
//    public void save(User user) { 
//    //获取Spring容器的对象
//    	WebApplicationContext contextLoader = ContextLoader.getCurrentWebApplicationContext(); 
//    	//1.获取事务控制管理器 
//    	DataSourceTransactionManager transactionManager = contextLoader.getBean( "txManager", DataSourceTransactionManager.class); 
//    	//2.获取事务定义
//    	DefaultTransactionDefinition def = new DefaultTransactionDefinition(); 
//    	//3.设置事务隔离级别，开启新事务
//    	def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW); 
//    	//4.获得事务状态 
//    	TransactionStatus status = transactionManager.getTransaction(def); 
//    	try { 
//    		//5.具体的数据库操作（多个） 
//    		User user1=new User(); 
//    		user1.setUserName("今天"); 
//    		user1.setAge("11"); 
//    		mapper.save(user1); 
//    		//Integer.parseInt("dddddddd"); 
//    		status.flush(); 
//    		User user2=new User(); 
//    		user2.setUserName("今天2"); 
//    		user2.setAge("12"); 
//    		mapper.save(user2); 
//    		} catch (Exception e)
//    		{ 
//    			transactionManager.rollback(status); 
//    			}finally {
//    				transactionManager.commit(status); 
//    				}  
//    	   }
//    	}
}
