package framework.test;

import javax.annotation.Resource;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.eeepay.framework.service.bill.GenericTableService;

import java.math.BigInteger;

public class GenericTableServiceTest  extends BaseTest {
	private static final Logger log = LoggerFactory.getLogger(GenericTableServiceTest.class);
	@Resource
	public GenericTableService genericTableService;

//	@Test
	public void test() throws Exception {
		String outBillDetailId = genericTableService.outBillDetailId();
		log.info(outBillDetailId);
		
	}

	@Test
	public void createKey() throws Exception {
		String num = genericTableService.updateKey("bill_ins_account");
		String s = String.format("3%011d", new BigInteger(num));
		System.out.println(s);
	}

	@Test
	public void test1() throws Exception {
		
			new Thread() {
				public void run(){
					for (int j = 0; j < 100; j++) {
						try {
	//						synchronized (GenericTableServiceTest.class) {
								String testId = genericTableService.createTest();
								log.info("response:" + testId);
								Thread.sleep(100);
	//						}
						} catch (InterruptedException e) {
							log.error("异常:",e);
						} catch (Exception e) {
							log.error("异常:",e);
						}
					}
					
				}
			}.start();
			
			new Thread() {
				public void run(){
					for (int j = 0; j < 100; j++) {
						try {
	//						synchronized (GenericTableServiceTest.class) {
								String testId = genericTableService.createTest();
								log.info("response:" + testId);
								Thread.sleep(100);
	//						}
						} catch (InterruptedException e) {
							log.error("异常:",e);
						} catch (Exception e) {
							log.error("异常:",e);
						}
					}
					
				}
			}.start();
			
			new Thread() {
				public void run(){
					for (int j = 0; j < 100; j++) {
						try {
	//						synchronized (GenericTableServiceTest.class) {
								String testId = genericTableService.createTest();
								log.info("response:" + testId);
								Thread.sleep(100);
	//						}
						} catch (InterruptedException e) {
							log.error("异常:",e);
						} catch (Exception e) {
							log.error("异常:",e);
						}
					}
					
				}
			}.start();
			
			new Thread() {
				public void run(){
					for (int j = 0; j < 100; j++) {
						try {
	//						synchronized (GenericTableServiceTest.class) {
								String testId = genericTableService.createTest();
								log.info("response:" + testId);
								Thread.sleep(100);
	//						}
						} catch (InterruptedException e) {
							log.error("异常:",e);
						} catch (Exception e) {
							log.error("异常:",e);
						}
					}
					
				}
			}.start();
			
			new Thread() {
				public void run(){
					for (int j = 0; j < 100; j++) {
						try {
	//						synchronized (GenericTableServiceTest.class) {
								String testId = genericTableService.createTest();
								log.info("response:" + testId);
								Thread.sleep(100);
	//						}
						} catch (InterruptedException e) {
							log.error("异常:",e);
						} catch (Exception e) {
							log.error("异常:",e);
						}
					}
					
				}
			}.start();
		
		
		
		Thread.sleep(3600000*1);//1个小时
		log.info("test2");
	}
}
