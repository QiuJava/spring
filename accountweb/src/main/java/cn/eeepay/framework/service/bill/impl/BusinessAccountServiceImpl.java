package cn.eeepay.framework.service.bill.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.eeepay.framework.dao.bill.BusinessAccountDetailMapper;
import cn.eeepay.framework.dao.bill.BusinessAccountMapper;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.BusinessAccount;
import cn.eeepay.framework.model.bill.BusinessAccountDetail;
import cn.eeepay.framework.service.bill.BusinessAccountService;
import cn.eeepay.framework.service.bill.ExtAccountService;
import cn.eeepay.framework.service.bill.RecordAccountRuleConfigService;
import cn.eeepay.framework.util.DateUtil;
import cn.eeepay.framework.util.HttpConnectUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.auth0.jwt.JWTSigner;

@Service
public class BusinessAccountServiceImpl implements BusinessAccountService {
	private static final Logger log = LoggerFactory.getLogger(BusinessAccountServiceImpl.class);
	@Autowired
	private BusinessAccountMapper businessAccountMapper;
	@Autowired
	private BusinessAccountDetailMapper businessAccountDetailMapper;
	@Autowired
	private RecordAccountRuleConfigService recordAccountRuleConfigService;
	@Autowired
	private ExtAccountService extAccountService;
	@Value("${accountApi.http.url}")  
	private String accountApiHttpUrl;
	@Value("${accountApi.http.secret}")  
	private String accountApiHttpSecret;

	@Override
	public List<BusinessAccount> findBusinessAccount(
			BusinessAccount account, Map<String, String> params, Sort sort,
			Page<BusinessAccount> page) throws Exception {
		return businessAccountMapper.findBusinessAccount(account,params,sort,page);
	}

	@Override
	public String insertBusinessAccount(BusinessAccount account, File file) {
		try {
			businessAccountMapper.insertBusinessAccount(account);

		} catch (Exception e) {
			log.error("插入调账数据异常！",e);
			log.error("异常:",e);
			return "插入调账数据异常";
		}
		String result = saveAdjustDetail(account, file);
		if(result != null){
			throw new RuntimeException(result) ;
		}
		return result ; 
	}


	public String saveAdjustDetail(BusinessAccount adjustAccount,File file){
		//读取文件
		HSSFWorkbook wb=null;
		List<BusinessAccountDetail> adjustDetailList =new ArrayList<BusinessAccountDetail>(); 
		try {
			wb = new HSSFWorkbook(new FileInputStream(file));
			HSSFSheet sheet=wb.getSheetAt(0);
			//获取总行数	
			int rowNum = sheet.getLastRowNum();
			for(int i=1;i<rowNum;i++){
				BusinessAccountDetail  adjustDetail = new BusinessAccountDetail() ;
				HSSFRow row=sheet.getRow(i);
				HSSFCell cell0=row.getCell(0);//调入外部用户编号
				HSSFCell cell1=row.getCell(1);//调出外部用户编号
				HSSFCell cell2=row.getCell(2);//调入外部用户编号
				HSSFCell cell3=row.getCell(3);//金额
				HSSFCell cell4=row.getCell(4);//调账类型
				HSSFCell cell5=row.getCell(5);//调账原因

				String cell0Value = getStringCell(cell0);
				String cell1Value = getStringCell(cell1);
				String cell2Value = getStringCell(cell2);
				String cell3Value_ = getStringCell(cell3);
				String cell4Value = getStringCell(cell4);
				String cell5Value = getStringCell(cell5);

				if(StringUtils.isBlank(cell0Value)&&StringUtils.isBlank(cell1Value)
						&&StringUtils.isBlank(cell2Value)&&StringUtils.isBlank(cell4Value)
						&&StringUtils.isBlank(cell3Value_)&&StringUtils.isBlank(cell5Value))
					break;
				Double cell3Value = new BigDecimal(cell3Value_).doubleValue();
				if(StringUtils.isBlank(cell0Value)){
					return "第"+i+"行【交易序号】为空";
				}
				/*
				if(StringUtils.isBlank(cell1Value)){
					return "第"+i+"行【调出外部用户编号】为空";
				}
				if(StringUtils.isBlank(cell2Value)){
					return "第"+i+"行【调入外部用户编号】为空";
				}*/
				if(StringUtils.isBlank(cell3Value.toString())){
					return "第"+i+"行【金额】为空";
				}
				if(StringUtils.isBlank(cell4Value)){
					return "第"+i+"行【调账类型】为空";
				}

				cell4Value = cell4Value.substring(0, cell4Value.indexOf("-"));
				adjustDetail.setTransNo(cell0Value);
				adjustDetail.setOutUserNo(cell1Value);
				adjustDetail.setInUserNo(cell2Value);
				adjustDetail.setAmount(new BigDecimal(cell3Value));
				adjustDetail.setAccountType(cell4Value);
				adjustDetail.setReason(cell5Value);
				adjustDetailList.add(adjustDetail);
			}

			log.info("Excel表格数据校验完毕开始进入平衡检查");
			log.info("Excel数据借贷平衡校验完毕进行插入数据表");
			for(BusinessAccountDetail adjustDetail : adjustDetailList){
				adjustDetail.setBusinessId(adjustAccount.getId());
			}
			businessAccountDetailMapper.insertBatchBusinessAccountDetail(adjustDetailList);
		} catch (Exception e) {
			//log.error("读取模板异常！",e);
			log.error("插入数据异常！",e);
			throw new RuntimeException("插入数据异常！");
		}finally{
			try {
				if(wb!=null){
					wb.close();
				}
			} catch (IOException e) {
				log.error("关闭模板失败");
			}
		}
		return null;
	}

	private static String getStringCell(HSSFCell cell){
		if(cell!=null)
			cell.setCellType(Cell.CELL_TYPE_STRING);
		return  cell!=null?cell.getStringCellValue():null;
	}

	private static Double getDoubleCell(HSSFCell cell){
		if(cell!=null)
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
		return  cell!=null?cell.getNumericCellValue():null;
	}

	/** 
	 * 按照异常批次号对已开单数据进行分组 
	 * @param billingList 
	 * @return 
	 * @throws Exception 
	 */  
	private Map<String, List<BusinessAccountDetail>> groupDataByAccountType(List<BusinessAccountDetail> billingList) throws Exception{  
		Map<String, List<BusinessAccountDetail>> resultMap = new HashMap<String, List<BusinessAccountDetail>>();  

		try{  
			for(BusinessAccountDetail tmExcpNew : billingList){  

				if(resultMap.containsKey(tmExcpNew.getAccountType())){//map中异常批次已存在，将该数据存放到同一个key（key存放的是异常批次）的map中  
					resultMap.get(tmExcpNew.getAccountType()).add(tmExcpNew);  
				}else{//map中不存在，新建key，用来存放数据  
					List<BusinessAccountDetail> tmpList = new ArrayList<BusinessAccountDetail>();  
					tmpList.add(tmExcpNew);  
					resultMap.put(tmExcpNew.getAccountType(), tmpList);  
				}  
			}  
		}catch(Exception e){  
			throw new Exception("按照异常批次号对已开单数据进行分组时出现异常", e);  
		}  
		return resultMap;  
	}

	@Override
	public BusinessAccount getBusinessAccount(Integer id) throws Exception {
		return businessAccountMapper.getBusinessAccount(id);
	}

	@Override
	public String updateBusiness(BusinessAccount adjustAccount, File file) {
		try {
			businessAccountMapper.updateBusinessAccount(adjustAccount);
			//删除调账ID所有明细
			businessAccountDetailMapper.deleteBusinessDetail(adjustAccount);
		} catch (Exception e) {
			log.error("修改调账数据异常！",e);
			log.error("异常:",e);
			return "修改调账数据异常！" ;
		}
		String result = saveAdjustDetail(adjustAccount, file);
		if(result != null){
			throw new RuntimeException(result) ;
		}
		return result ; 

	}

	@Override
	public String updateBusinessAccount(BusinessAccount adjustAccount) {
		BusinessAccount adjustAccountQ = businessAccountMapper.getBusinessAccount(adjustAccount.getId()) ;
		adjustAccountQ.setApprover(adjustAccount.getApprover());//收款人
		adjustAccountQ.setRemark(adjustAccount.getRemark()); //提交备注

		int i = businessAccountMapper.updateBusinessAccount(adjustAccountQ) ;
		if(i > 0){
			return null;
		}
		return "修改失败！";
	}

	@Override
	public List<BusinessAccountDetail> findBusinessAccountDetail(
			BusinessAccountDetail adjustDetail, Map<String, String> params,
			Sort sort, Page<BusinessAccountDetail> page) {
		return businessAccountDetailMapper.findBusinessAccountDetail(adjustDetail, params, sort, page);
	}

	@Override
	public int updateBusinessExamineDate(BusinessAccount adjustAccount) {
		return businessAccountMapper.updateBusinessExamineDate(adjustAccount);
	}

	@Override
	public Map<String, Object> updateBusinessExamine(
			BusinessAccount adjustAccount) {
		Map<String,Object> msg=new HashMap<>();
		String url = accountApiHttpUrl + "/recordAccountController/adjustAgentShareAmount.do" ;
		BusinessAccount oldAdjustAccount = businessAccountMapper.getBusinessAccount(adjustAccount.getId());
		int failedNum = 0;
		List<BusinessAccountDetail> adjustDetails = businessAccountDetailMapper.findBusinessDetailByBusinessId(adjustAccount) ;
		if(adjustAccount.getStatus() == 2){//审核意见(状态) 为 同意
			final String secret = accountApiHttpSecret;
			final long iat = System.currentTimeMillis() / 1000l; // issued at claim 
			final long exp = iat + 60L; // expires claim. In this case the token expires in 60 seconds
			final String jti = UUID.randomUUID().toString();
			final JWTSigner signer = new JWTSigner(secret);
			HashMap<String, Object> claims = null;

			for(BusinessAccountDetail detail : adjustDetails){
				claims = new HashMap<String, Object>();
				claims.put("fromSystem", "accountWeb");
				claims.put("transDate", DateUtil.getCurrentDate());
				claims.put("fromSerialNo", detail.getId().toString());
				claims.put("transAmount", detail.getAmount().toString());
				claims.put("transTypeCode", detail.getAccountType());
				switch(detail.getAccountType()) {
				case "000023":
					claims.put("agentNo", detail.getInUserNo());
					break;
				default:
					break;
				}

				final String token = signer.sign(claims);
				log.info("业务调账接口调用url:" + url);
				String response = HttpConnectUtil.postHttp(url, "token", token);
				log.info("业务调账接口响应response:" + response);

				if (StringUtils.isBlank(response)) {
					failedNum ++;
					//记账失败
					detail.setRecordStatus(2);
					detail.setRecordResult("记账失败：记账接口无返回结果!");
				}else{
					Map<String, Object> respMap = JSON.parseObject(response, new TypeReference<Map<String, Object>>() {});
					if((boolean)respMap.get("status") == false){
						failedNum ++;
						detail.setRecordStatus(2);
						detail.setRecordResult("记账失败：" + (String)respMap.get("msg"));
					} else {
						detail.setRecordStatus(1);
						detail.setRecordResult("记账成功!");
					}
				}
				businessAccountDetailMapper.updateRecordResult(detail);
			}
		}  
		//更改调账审核状态
		businessAccountMapper.updateBusinessExamineDate(adjustAccount);
		if(adjustAccount.getStatus() == 2){
			if (failedNum > 0) {
				msg.put("state", false) ;
			} else {
				msg.put("state", true) ;
			}
			msg.put("msg", "提交成功，意见为通过！调账结果：共" +adjustDetails.size()+"条，" + (adjustDetails.size() - failedNum) + "成功，"+failedNum+"失败") ;
		}
		if(adjustAccount.getStatus() == 3){
			msg.put("state", true) ;
			msg.put("msg", "提交成功，意见为不通过！") ;
		}
		return msg ;
	}
}