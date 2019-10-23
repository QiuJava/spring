package cn.eeepay.framework.service.bill.impl;

import cn.eeepay.framework.model.bill.DuiAccountDetail;
import cn.eeepay.framework.service.bill.DuiAccountAssemblyOrParsing;
import cn.eeepay.framework.service.bill.DuiAccountDetailService;
import cn.eeepay.framework.service.bill.DuiAccountService;
import cn.eeepay.framework.util.DateUtil;
import cn.eeepay.framework.util.StringUtil;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("duiAccountAssemblyOrParsing")
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class DuiAccountAssemblyOrParsingImpl implements DuiAccountAssemblyOrParsing {
	private static final Logger log = LoggerFactory.getLogger(DuiAccountAssemblyOrParsingImpl.class);
	@Resource
	public DuiAccountDetailService duiAccountDetailService;
	@Resource
	private DuiAccountService duiAccountService;

	/**
	 * 解析快钱对账文件
	 * @return
	 */
	public Map<String, Object> resolveKQZQFileTxt(File file,List<DuiAccountDetail> checkAccountDetails) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		log.info("==============开始快钱对账文件==============");
		FileInputStream fis = new FileInputStream(file);
		BufferedReader reader = new BufferedReader(new InputStreamReader(fis,"utf-8"));
		String line = reader.readLine();
		int count = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		while (true){
			count++;
			if (count <= 4) {
				line = reader.readLine();
				continue;
			}
			if(line != null && !"".equals(line)){
//				log.info("==============line==============" + line);
				String[] detail = line.split("\\|");
				if (detail.length >= 30) {
					DuiAccountDetail checkDetail = new DuiAccountDetail();
					checkDetail.setAcqMerchantNo(detail[0].trim());
					checkDetail.setAcqMerchantName(detail[1].trim());
	                checkDetail.setAcqReferenceNo(detail[3].trim());//交易参考号，对账的唯一字段
					String transTime =DateUtil.StrDateToFormatStr(detail[5].trim(),"yyyyMMddHHmmss","yyyy-MM-dd HH:mm:ss");
					checkDetail.setAcqTransTime(sdf.parse(transTime));
					checkDetail.setAcqAccountNo(detail[6].trim());
					checkDetail.setAcqTransAmount(new BigDecimal(replaceBlank(detail[7].trim().replace(",","")).trim()));
					checkDetail.setAcqTransType(detail[8].trim());//收单机构交易类型
					checkDetail.setAcqCheckDate(sdf.parse(transTime));
					checkDetail.setAcqRefundAmount(new BigDecimal(replaceBlank(detail[24].trim().replace(",","")).trim()));//手续费
					checkDetail.setCreateTime(new Date());
	//				log.info("checkDetail=" + ToStringBuilder.reflectionToString(checkDetail, ToStringStyle.MULTI_LINE_STYLE));
					checkAccountDetails.add(checkDetail);
				}	
			}else{
				break;
			}
			line = reader.readLine();
		}
		reader.close();
		fis.close();
		log.info("==============解析快钱对账文件结束==============");
		return map;
	}
	public static String replaceBlank(String str) {
		String dest = "";
		if (str!=null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}

}
