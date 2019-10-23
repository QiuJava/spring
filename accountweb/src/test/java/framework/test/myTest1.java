package framework.test;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.eeepay.boss.action.RoleAction;




public class myTest1 {
	private static final Logger log = LoggerFactory.getLogger(myTest1.class);
	//@Test
	public void test1() {
		String[] arrayA = new String[] { "a", "b" };
		String[] arrayB = new String[] { "a", "b", "d" };
		List<String> a = Arrays.asList(arrayA);
		List<String> b = Arrays.asList(arrayB);
		//并集      
		Collection<String> union = CollectionUtils.union(a, b);
		//交集      
		Collection<String> intersection = CollectionUtils.intersection(a, b);
		//交集的补集      
		Collection<String> disjunction = CollectionUtils.disjunction(a, b);
		//集合相减      
		Collection<String> subtract = CollectionUtils.subtract(a, b);
		Collections.sort((List<String>) union);
		Collections.sort((List<String>) intersection);
		Collections.sort((List<String>) disjunction);
		Collections.sort((List<String>) subtract);
		System.out.println("A: " + ArrayUtils.toString(a.toArray()));
		System.out.println("B: " + ArrayUtils.toString(b.toArray()));
		System.out.println("--------------------------------------------");
		System.out.println("并集    Union(A, B): " + ArrayUtils.toString(union.toArray()));
		System.out.println("交集 Intersection(A, B): " + ArrayUtils.toString(intersection.toArray()));
		System.out.println("交集的补集      Disjunction(A, B): " + ArrayUtils.toString(disjunction.toArray()));
		System.out.println("集合相减 Subtract(A, B): " + ArrayUtils.toString(subtract.toArray()));
	}
	//@Test
	public void test2() {
		Date date = new Date();
		DateFormat df3 = DateFormat.getTimeInstance();//只显示出时分秒
        System.out.println(df3.format(date));
	}
	@Test
	public void test3() {
		String s = null;
		BigDecimal a = new BigDecimal(s);
		System.out.println(a);
	}
	
	@Test
	public void test4() {
		BigDecimal s = new BigDecimal(594).divide(new BigDecimal(100));
		System.out.println(s.toString());
		
	}
}
