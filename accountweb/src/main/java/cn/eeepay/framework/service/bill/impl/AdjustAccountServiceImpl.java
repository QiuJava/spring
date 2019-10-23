package cn.eeepay.framework.service.bill.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.bill.AdjustAccountMapper;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.enums.AccountType;
import cn.eeepay.framework.enums.DebitCreditSide;
import cn.eeepay.framework.enums.ReverseFlag;
import cn.eeepay.framework.model.bill.AdjustAccount;
import cn.eeepay.framework.model.bill.AdjustDetail;
import cn.eeepay.framework.model.bill.Currency;
import cn.eeepay.framework.model.bill.OrgInfo;
import cn.eeepay.framework.service.bill.AdjustAccountService;
import cn.eeepay.framework.service.bill.CurrencyService;
import cn.eeepay.framework.service.bill.OrgInfoService;
import cn.eeepay.framework.util.HttpConnectUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.auth0.jwt.JWTSigner;
@Service("adjustAccountService")
@Transactional
public class AdjustAccountServiceImpl implements AdjustAccountService {

	private static final Logger log = LoggerFactory.getLogger(AdjustAccountServiceImpl.class);

	@Resource
	private AdjustAccountMapper adjustAccountMapper;
	@Resource
	private InsAccountServiceImpl insAccountService ;
	@Resource
	private ExtAccountServiceImpl extAccountService ;
	@Resource
	private CurrencyService currencyService;
	@Resource
	private OrgInfoService orgInfoService;
	@Value("${accountApi.http.url}")  
    private String accountApiHttpUrl;
	@Value("${accountApi.http.secret}")  
    private String accountApiHttpSecret;
	
	@Override
	public String insertAdjust(AdjustAccount adjustAccount,File file){
		try {
			//adjustAccount.setApproveTime(new Date());
			adjustAccountMapper.insertAdjustAccount(adjustAccount);

		} catch (Exception e) {
				log.error("插入调账数据异常！",e);
				log.error("异常:",e);
				return "插入调账数据异常" ;
		}
		String result = saveAdjustDetail(adjustAccount, file);
		if(result != null){
			throw new RuntimeException(result) ;
		}
		return result ; 
		
	}
	@Override
	public String updateAdjust(AdjustAccount adjustAccount,File file){
		try {
			//adjustAccount.setApproveTime(new Date());
			adjustAccountMapper.updateAdjustAccount(adjustAccount);
			//删除调账ID所有明细
			adjustAccountMapper.deleteAdjustDetail(adjustAccount);
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
	
	public String saveAdjustDetail(AdjustAccount adjustAccount,File file){
		EnumMap accountTypeEnumMap = new EnumMap(AccountType.class);  
	    accountTypeEnumMap.put(AccountType.ACQ,"收单机构");
	    accountTypeEnumMap.put(AccountType.AGENT,"代理商");  
	    accountTypeEnumMap.put(AccountType.MERCHANT,"商户");  

		//读取文件
		HSSFWorkbook wb=null;
		List<AdjustDetail> adjustDetailList =new ArrayList<AdjustDetail>(); 
		try {
			wb = new HSSFWorkbook(new FileInputStream(file));
			HSSFSheet sheet=wb.getSheetAt(0);
			//获取总行数	
			int rowNum = sheet.getLastRowNum();
			
			for(int i=1;i<=rowNum;i++){
				AdjustDetail  adjustDetail = new AdjustDetail() ;
				HSSFRow row=sheet.getRow(i);
				HSSFCell cell0=row.getCell(0);//交易序号
				HSSFCell cell1=row.getCell(1);//分录号
				HSSFCell cell2=row.getCell(2);//子交易号
				HSSFCell cell3=row.getCell(3);//账号
				HSSFCell cell4=row.getCell(4);//借贷标识
				HSSFCell cell5=row.getCell(5);//金额
				HSSFCell cell6=row.getCell(6);//检查借贷平衡
				HSSFCell cell7=row.getCell(7);//内部账外部账标志
				HSSFCell cell8=row.getCell(8);//外部账用户类型
				HSSFCell cell9=row.getCell(9);//外部用户（商户、代理商、收单机构）编号
				HSSFCell cell10=row.getCell(10);//账户归属
				HSSFCell cell11=row.getCell(11);//科目编号
				HSSFCell cell12=row.getCell(12);//币种号
				HSSFCell cell13=row.getCell(13);//卡号（预付卡号，若没有，则为空）
				HSSFCell cell14=row.getCell(14);//备注
				
				String cellOValue = getStringCell(cell0);
				String  cell1Value = getStringCell(cell1);
				String  cell2Value = getStringCell(cell2);
				String  cell3Value = getStringCell(cell3);
				String  cell4Value = getStringCell(cell4);
				String	cell5Value_ = getStringCell(cell5);
				String  cell6Value = getStringCell(cell6);
				String  cell7Value = getStringCell(cell7);
				String  cell8Value = getStringCell(cell8);
				String  cell9Value = getStringCell(cell9);
				String  cell10Value = getStringCell(cell10);
				String  cell11Value = getStringCell(cell11);
				String  cell12Value = getStringCell(cell12);
				String  cell13Value = getStringCell(cell13);
				String  cell14Value = getStringCell(cell14);
				
				if(StringUtils.isBlank(cellOValue)&&StringUtils.isBlank(cell1Value)&&StringUtils.isBlank(cell2Value)&&StringUtils.isBlank(cell3Value)&&StringUtils.isBlank(cell4Value)
						&&StringUtils.isBlank(cell5Value_)&&StringUtils.isBlank(cell6Value)&&StringUtils.isBlank(cell7Value)&&StringUtils.isBlank(cell8Value)
						&&StringUtils.isBlank(cell9Value)&&StringUtils.isBlank(cell10Value)&&StringUtils.isBlank(cell11Value)&&StringUtils.isBlank(cell12Value))
					break;
				
				Double cell5Value = new BigDecimal(cell5Value_).doubleValue();
				//int realRow=(1+rowNum);
				if(StringUtils.isBlank(cellOValue)){
					 return "第"+i+"行【交易序号】为空";
				}
				if(StringUtils.isBlank(cell1Value)){
					 return "第"+i+"行【分录号】为空";
				}
				if(StringUtils.isBlank(cell2Value)){
					 return "第"+i+"行【子交易号】为空";
				}
				if(StringUtils.isBlank(cell4Value)){
					 return "第"+i+"行【借货标识】为空";
				}
				if(StringUtils.isBlank(cell5Value.toString())){
					 return "第"+i+"行【金额】为空";
				}
				if(StringUtils.isBlank(cell6Value)){
					 return "第"+i+"行【检查借贷平衡】为空";
				}else{
					cell6Value = "是".equals(cell6Value)?"1":"否".equals(cell6Value)?"0":"" ;
					if("".equals(cell6Value))		
						return "第"+i+"行【检查借贷平衡】值不正确" ;
				}
				if(StringUtils.isBlank(cell7Value)){
					 return "第"+i+"行【内部账外部账标志】为空";
				}else{
					cell7Value = "内部账".equals(cell7Value)?"1":"外部账".equals(cell7Value)?"0":"" ;
					if("".equals(cell7Value))		
						return "第"+i+"行【内部账外部账标志】值不正确" ;
				}
				EnumMap debitCreditSideEnumMap = new EnumMap(DebitCreditSide.class);  
			    debitCreditSideEnumMap.put(DebitCreditSide.DEBIT,"借");
			    debitCreditSideEnumMap.put(DebitCreditSide.CREDIT,"贷");  
				
				//如果为内部账户
				if("1".equals(cell7Value)){
					//如果账号为空
					if(StringUtils.isBlank(cell3Value)){
						if(StringUtils.isBlank(cell9Value)){
							 return "第"+i+"行【外部用户编号-机构号】为空";
						}
						if(StringUtils.isBlank(cell11Value)){
							 return "第"+i+"行【科目编号】为空";
						}
						
						if(StringUtils.isBlank(cell12Value)){
							 return "第"+i+"行【币种号】为空";
						}else{
							//cell12Value = "RMB".equals(cell12Value)?"1":"" ;
							String currencyName = cell12Value;
							Currency currency= currencyService.findCurrencyNoByName(currencyName);
							if(currency == null)		
								return "第"+i+"行【币种号】值不正确" ;
							else{
								cell12Value = currency.getCurrencyNo();
							}
						}
						adjustDetail.setUserId(cell9Value);
						adjustDetail.setSubjectNo(cell11Value);
						adjustDetail.setCurrencyNo(cell12Value);
					}
				}else{
					//账号为空
					if(StringUtils.isBlank(cell3Value)){
						//外部用户类型，外部用户id，账户归属，卡号，科目编号，币种号
						debitCreditSideEnumMap.put(DebitCreditSide.FREEZE,"冻结");
					    debitCreditSideEnumMap.put(DebitCreditSide.UNFREEZE,"解冻");
						if(StringUtils.isBlank(cell8Value)){
							 return "第"+i+"行【外部账用户类型】为空";
						}else{
							//cell8Value = "商户".equalsIgnoreCase(cell8Value)?"M":"代理商".equalsIgnoreCase(cell8Value)?"A":"收单机构".equalsIgnoreCase(cell8Value)?"Acq":"" ;
							Iterator<AccountType> enumKeySet = accountTypeEnumMap.keySet().iterator();
							boolean isExist = false;
					        while(enumKeySet.hasNext()){
					        	AccountType currentState = enumKeySet.next();
					            String value = accountTypeEnumMap.get(currentState).toString();
					            if (value.equals(cell8Value)) {
					            	isExist = true;
					            	cell8Value = currentState.toString();
					            	break;
								}
					        }
							if(!isExist)		
								return "第"+i+"行【外部账用户类型】值不正确" ;
						}
						if(StringUtils.isBlank(cell9Value)){
							 return "第"+i+"行【外部用户编号】为空";
						}
						if(StringUtils.isBlank(cell10Value)){
							 return "第"+i+"行【账户归属】为空";
						}else{
							//cell10Value = "移付宝".equals(cell10Value)?"000001":"" ;
							String orgName = cell10Value;
							OrgInfo orgInfo = orgInfoService.findOrgNoByName(orgName);
							if(orgInfo == null)		
								return "第"+i+"行【账户归属】值不正确" ;
							else{
								cell10Value = orgInfo.getOrgNo();
							}
						}
						if(StringUtils.isBlank(cell11Value)){
							 return "第"+i+"行【科目编号】为空";
						}
						if(StringUtils.isBlank(cell12Value)){
							 return "第"+i+"行【币种号】为空";
						}else{
							//cell12Value = "RMB".equals(cell12Value)?"1":"" ;
							String currencyName = cell12Value;
							Currency currency= currencyService.findCurrencyNoByName(currencyName);
							if(currency == null)		
								return "第"+i+"行【币种号】值不正确" ;
							else{
								cell12Value = currency.getCurrencyNo();
							}
						}
						adjustDetail.setAccountType(cell8Value);
						adjustDetail.setUserId(cell9Value);
						adjustDetail.setAccountOwner(cell10Value);
						adjustDetail.setSubjectNo(cell11Value);
						adjustDetail.setCurrencyNo(cell12Value);
					}
				}
				Iterator<DebitCreditSide> enumKeySet = debitCreditSideEnumMap.keySet().iterator();
				boolean isExist = false;
		        while(enumKeySet.hasNext()){
		        	DebitCreditSide currentState = enumKeySet.next();
		            String value = debitCreditSideEnumMap.get(currentState).toString();
		            if (value.equals(cell4Value)) {
		            	isExist = true;
		            	cell4Value = currentState.toString();
		            	break;
					}
		        }
				if(!isExist)		
					return "第"+i+"行【借货标识】值不正确，内部账户类型的借贷标识只能为借或贷" ;
				
				adjustDetail.setTransNo(cellOValue);
				adjustDetail.setJournalNo(cell1Value);
				adjustDetail.setChildTransNo(cell2Value);
				adjustDetail.setAccount(cell3Value);
				adjustDetail.setAmountFrom(cell4Value);
				adjustDetail.setAmount(new BigDecimal(cell5Value));
				adjustDetail.setDebitCreditBalance(cell6Value);
				adjustDetail.setAccountFlag(Integer.valueOf(cell7Value).intValue());
				adjustDetail.setCardNo(cell13Value);
				adjustDetail.setRemark(cell14Value);
				adjustDetailList.add(adjustDetail);
			}
			
			Collections.sort(adjustDetailList,new Comparator<AdjustDetail>() {
				@Override
				public int compare(AdjustDetail o1, AdjustDetail o2) {
					return o1.getTransNo().equals(o2.getTransNo())?0:-1;
				}
			});
			log.info("Excel表格数据校验完毕开始进入平衡检查");
			
			BigDecimal debitBigDecimal= new BigDecimal("0");
			BigDecimal creditBigDecimal= new BigDecimal("0");

			String  transNoTemp=adjustDetailList.get(0).getTransNo();
			String  journalNoTemp=adjustDetailList.get(0).getJournalNo();		
			for (int i = 0; i < adjustDetailList.size() ; i++) {
				AdjustDetail adjustDetailInfo= adjustDetailList.get(i);
				String transNo = adjustDetailInfo.getTransNo();
				String journalNo = adjustDetailInfo.getJournalNo();
				//是否检查借贷平衡
				if("0".equals(adjustDetailInfo.getDebitCreditBalance())){
					continue ;
				}else{
					if(!transNoTemp.equalsIgnoreCase(transNo)){//交易序号不相等的时候检查借贷平衡	
						if (debitBigDecimal.compareTo(creditBigDecimal)!=0) {
							return "【"+transNoTemp+"】交易序号、【"+journalNoTemp+"】分录号金额借贷平衡不相等";
						}
						transNoTemp=transNo;//改变交易序号
						journalNoTemp=journalNo;//改变分录号
						debitBigDecimal = debitBigDecimal.subtract(debitBigDecimal);
						creditBigDecimal = creditBigDecimal.subtract(creditBigDecimal);
						if(journalNoTemp.equalsIgnoreCase(journalNo)){//分录号相等的情况下
							if(adjustDetailInfo.getAmountFrom().equals("debit")) {
									debitBigDecimal = debitBigDecimal.add(adjustDetailInfo.getAmount());										
							}
							if(adjustDetailInfo.getAmountFrom().equals("credit")) {
									creditBigDecimal = creditBigDecimal.add(adjustDetailInfo.getAmount());
							}
						}
					}else{
						if(!journalNoTemp.equalsIgnoreCase(journalNo)){//分录号不相等的时候检查借贷平衡	
							if (debitBigDecimal.compareTo(creditBigDecimal)!=0) {
								return "第【"+transNoTemp+"】交易序号、【"+journalNoTemp+"】分录号金额借贷平衡不相等";
							}
							journalNoTemp=journalNo;//改变分录号
							debitBigDecimal = debitBigDecimal.subtract(debitBigDecimal);
							creditBigDecimal = creditBigDecimal.subtract(creditBigDecimal);
						}
						if(journalNoTemp.equalsIgnoreCase(journalNo)){//分录号相等的情况下
							if(adjustDetailInfo.getAmountFrom().equals("debit")) {
									debitBigDecimal = debitBigDecimal.add(adjustDetailInfo.getAmount());										
							}
							if(adjustDetailInfo.getAmountFrom().equals("credit")) {
									creditBigDecimal = creditBigDecimal.add(adjustDetailInfo.getAmount());
							}
						}
					}
				}
			}
			
			//最后一笔时判断是否借贷平衡
			if (debitBigDecimal.compareTo(creditBigDecimal)!=0) {
				return "第【"+transNoTemp+"】交易序号、【"+journalNoTemp+"】分录号金额借贷平衡不相等";
			}
			log.info("Excel数据借贷平衡校验完毕进行插入数据表");
			for(AdjustDetail adjustDetail : adjustDetailList){
				adjustDetail.setAdjustId(adjustAccount.getId());
			}
			adjustAccountMapper.insertBatchAdjustDetail(adjustDetailList);
		} catch (Exception e) {
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

	@Override
	public List<AdjustAccount> findAdjustAccountApprove(AdjustAccount adjustAccount,Map<String, String> params,
			Sort sort, Page<AdjustAccount> page) throws Exception {
		if(!"".equals(params.get("beginDate"))){
			params.put("beginDate", params.get("beginDate") + " 00:00:00") ;
		}
		if(!"".equals(params.get("endDate"))){
			params.put("endDate", params.get("endDate") + " 23:59:59") ;
		}
		return adjustAccountMapper.findAdjustAccountApprove(adjustAccount,params,sort,page);
	}
	
	@Override
	public List<AdjustAccount> findAdjustAccount(AdjustAccount adjustAccount,Map<String, String> params,
			Sort sort, Page<AdjustAccount> page) throws Exception {
		if(!"".equals(params.get("beginDate"))){
			params.put("beginDate", params.get("beginDate") + " 00:00:00") ;
		}
		if(!"".equals(params.get("endDate"))){
			params.put("endDate", params.get("endDate") + " 23:59:59") ;
		}
		return adjustAccountMapper.findAdjustAccount(adjustAccount,params,sort,page);
	}
/*
	@Override
	public AdjustAccount getAdjustAccount(int id) throws Exception {
		return adjustAccountDao.getAdjustAccount(id);
	}*/


	@Override
	public List<AdjustDetail> findAdjustDetail(AdjustDetail adjustDetail,
			Map<String, String> params, Sort sort, Page<AdjustDetail> page)
			throws Exception {
		return adjustAccountMapper.findAdjustdetail(adjustDetail,params,sort,page);
	}

	@Override
	public Map<String,Object> updateadjustExamine(AdjustAccount adjustAccount) throws Exception {
		Map<String,Object> msg=new HashMap<>();
		String url = "" ;
		List<AdjustDetail> adjustDetails = adjustAccountMapper.findAdjustDetailByAdjustId(adjustAccount) ;
		if(adjustAccount.getStatus()==2){//审核意见(状态) 为 同意
			for(AdjustDetail detail:adjustDetails){
				final String secret = accountApiHttpSecret;
		        final long iat = System.currentTimeMillis() / 1000l; // issued at claim 
		        final long exp = iat + 60L; // expires claim. In this case the token expires in 60 seconds
		        final String jti = UUID.randomUUID().toString();
		        final JWTSigner signer = new JWTSigner(secret);
		        final HashMap<String, Object> claims = new HashMap<String, Object>();
		        
				if(detail.getAccountFlag() == 0){
					//外部账户
					claims.put("accountType", detail.getAccountType()) ;//外部账用户类型
			        claims.put("userId", detail.getUserId()) ;//外部账用户Id
			        claims.put("accountOwner", detail.getAccountOwner()) ;//账号归属
			        claims.put("cardNo", detail.getCardNo()) ;//卡号
						if("debit".equals(detail.getAmountFrom())){
							//外部账借
					        url = accountApiHttpUrl+"/extAccountController/extAccountDebit.do" ;
					        claims.put("transAmount", detail.getAmount().toString()) ;//调账金额
					        claims.put("debitCreditSide", detail.getAmountFrom()) ;//借贷方向
						}else if("credit".equals(detail.getAmountFrom())){
							//外部账贷
							url = accountApiHttpUrl+"/extAccountController/extAccountCredit.do" ;
							claims.put("transAmount", detail.getAmount().toString()) ;//调账金额
					        claims.put("debitCreditSide", detail.getAmountFrom()) ;//借贷方向
						}else if("freeze".equals(detail.getAmountFrom())){
							//外部账冻结
							url = accountApiHttpUrl+"/extAccountController/extAccountFreezePartAmount.do" ;
							claims.put("opt", "freeze") ;//freeze-冻结，unfreeze-解冻
					        claims.put("amount", detail.getAmount().toString()) ;//操作(冻结解冻)金额
					        claims.put("transOrderNo", "") ;//交易订单号
						}else if("thaw".equals(detail.getAmountFrom())){
							//外部账解冻
							url = accountApiHttpUrl+"/extAccountController/extAccountFreezePartAmount.do" ;
							claims.put("opt", "unfreeze") ;//freeze-冻结，unfreeze-解冻
					        claims.put("amount", detail.getAmount().toString()) ;//操作(冻结解冻)金额
					        claims.put("transOrderNo", "") ;//交易订单号
						}
					}else{
					//内部账户
						claims.put("orgNo", "000001") ;//机构号
				        claims.put("transAmount", detail.getAmount().toString()) ;//调账金额
				        claims.put("debitCreditSide", detail.getAmountFrom()) ;//借贷方向
						if("debit".equals(detail.getAmountFrom())){
							//内部账借
							url = accountApiHttpUrl+"/insAccountController/insAccountDebit.do" ;
						}else if("credit".equals(detail.getAmountFrom())){
							//内部账贷
							url = accountApiHttpUrl+"/insAccountController/insAccountCredit.do" ;
						}
					}
					
				
					//调用6.4.13调账记账接口调用
			        if(detail.getAccount() == null || "".equals(detail.getAccount())){
			        	claims.put("selectType", "2") ;//账号查询方式
			        }else{
			        	claims.put("selectType", "1") ;//账号查询方式
			        }
			        claims.put("accountNo", detail.getAccount()) ;//账号
			        claims.put("subjectNo", detail.getSubjectNo()) ;//科目号
			        claims.put("currencyNo", detail.getCurrencyNo()) ;//币种号
			        claims.put("reverseFlag", ReverseFlag.NORMAL.toString()) ;//冲销标志
			        claims.put("summaryInfo", detail.getRemark()) ;//调账说明
			        
			        final String token = signer.sign(claims);
					String response = HttpConnectUtil.postHttp(url, "token", token);
					System.out.println("response:" + response);

					if (StringUtils.isBlank(response)) {
						//记账失败
						adjustAccount.setStatus(5);
						adjustAccount.setRecordFailRemark("");
						msg.put("state", false) ;
						msg.put("msg", "记账失败！"+adjustAccount.getRecordFailRemark()) ;
						break ;
					}else{
						Map<String, Object> respMap = JSON.parseObject(response, new TypeReference<Map<String, Object>>() {});
						if((boolean)respMap.get("status") == false){
							adjustAccount.setStatus(5);
							adjustAccount.setRecordFailRemark((String)respMap.get("msg"));
							msg.put("state", false) ;
							msg.put("msg", "记账失败,原因:"+adjustAccount.getRecordFailRemark()) ;
							break ;
						}
					}
			}
		}
		
		//更改调账审核状态
		adjustAccountMapper.updateadjustExamine(adjustAccount);
		if(adjustAccount.getStatus() == 2){
			msg.put("state", true) ;
			msg.put("msg", "审核成功！") ;
		}
		if(adjustAccount.getStatus() == 3){
			msg.put("state", true) ;
			msg.put("msg", "提交成功，意见为不通过！") ;
		}
		
		return msg ;
	}
	
	public AdjustAccount getAdjustAccount(Integer id) throws Exception {
		return adjustAccountMapper.getAdjustAccount(id);
	}
	@Override
	public int updateadjustExamineDate(AdjustAccount adjustAccount)
			throws Exception {
		// TODO Auto-generated method stub
		return adjustAccountMapper.updateadjustExamineDate(adjustAccount);
	}
	@Override
	public List<AdjustDetail> findAdjustDetailByAdjustId(AdjustAccount adjustAccount) throws Exception {
		return adjustAccountMapper.findAdjustDetailByAdjustId(adjustAccount);
	}
	@Override
	public String updateAdjustAccount(AdjustAccount adjustAccount) throws Exception {

		AdjustAccount adjustAccountQ = adjustAccountMapper.getAdjustAccount(adjustAccount.getId()) ;
		adjustAccountQ.setAccountType(adjustAccount.getAccountType()); //调账类型
		adjustAccountQ.setApprover(adjustAccount.getApprover());//收款人
		adjustAccountQ.setRemark(adjustAccount.getRemark()); //提交备注
		
		int i = adjustAccountMapper.updateAdjustAccount(adjustAccountQ) ;
		if(i>0){
			return null;
		}
		return "修改失败！";
	}
}
