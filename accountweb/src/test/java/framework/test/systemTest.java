package framework.test;

import javax.annotation.Resource;

import cn.eeepay.framework.model.bill.DuiAccountDetail;
import cn.eeepay.framework.service.bill.DuiAccountDetailService;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import cn.eeepay.framework.service.bill.SystemInfoService;

import java.io.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class systemTest  extends BaseTest {
	@Resource
	public SystemInfoService systemInfoService;

	@Autowired
	DuiAccountDetailService duiAccountDetailService;

//	@Value("${boss.http.api.url}")
//    private String bossHttpApiUrl;
	@Test
	public void test1() {
//		String childSerialNo = StringUtils.leftPad(String.valueOf(1), 3, "0");
//		System.out.println("childSerialNo:" + childSerialNo);
		
//		System.out.println(bossHttpApiUrl);
		
	}

	@Test
	public void test10() throws Exception {
		DuiAccountDetail duiAccountDetailById = duiAccountDetailService.findDuiAccountDetailById("58891110");
		BigDecimal b1 = new BigDecimal("156.80");
		System.out.println(duiAccountDetailById.getAcqTransAmount().toString());
		System.out.println(duiAccountDetailById.getAcqTransAmount().compareTo(b1));
	}



	@Test
	public void test()throws Exception{
		List<DuiAccountDetail> list = duiAccountDetailService.findAllDuiAccountDetailList();

		List<String> collect = list.stream().map((T) -> T.getPlateAcqReferenceNo()).collect(Collectors.toList());


		File file = new File("G:/ACC20181217CHK");
		InputStream inputStream = new FileInputStream(file);
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));


		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("G:/ACC20181217CHK_test"),"UTF-8"));


		String line = reader.readLine();
		while(line != null){
			String[] fileds = line.split(",");
			String filed = fileds[11];
			if(collect.contains(filed)){
				writer.write(line+"\r\n");
			}
			line = reader.readLine();
		}
		writer.flush();
		reader.close();
		inputStream.close();

		writer.close();

	}

}
