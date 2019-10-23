package bossweb.bky;

import org.junit.Test;

public class test1 {
	//@Test
	public  void abc2() {
		String phone = "17722677560";
		String password = "rjzou123";
		String str = MD5.encodeByMD5(password).toLowerCase();
		System.out.println(str);
		String hmac = MD5.encodeByMD5(phone + str + "aVkeAZGqm4QxcfDr").toLowerCase();
		System.out.println(hmac);
		String merchantNo ="1000003406";
		hmac = MD5.encodeByMD5(merchantNo + "2" + "5SADBBRGG40005C867AA675").toLowerCase();
		System.out.println(hmac);
		String id ="3";
		hmac = MD5.encodeByMD5(id + "aVkeAZGqm4QxcfDr").toLowerCase();
		System.out.println(hmac);
	}
	@Test
	public  void abc3() {
		String phone = "13211224506";
		String password = "rjzou123";
		String str = MD5.encodeByMD5(password).toLowerCase();
		System.out.println(str);
		String hmac = MD5.encodeByMD5(phone + str + "aVkeAZGqm4QxcfDr").toLowerCase();
		System.out.println(hmac);
		String merchantNo ="1000001330";
		hmac = MD5.encodeByMD5(merchantNo + "2" + "5SADBBRGG40005C867AA675").toLowerCase();
		System.out.println(hmac);
//		String id ="3";
//		hmac = MD5.encodeByMD5(id + "aVkeAZGqm4QxcfDr").toLowerCase();
//		System.out.println(hmac);
	}
}
