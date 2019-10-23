package cn.eeepay.framework.service.bill.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.bill.FastCheckAccountDetailMapper;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.enums.DuiAccountStatus;
import cn.eeepay.framework.model.bill.AcqOrg;
import cn.eeepay.framework.model.bill.FastCheckAccDetail;
import cn.eeepay.framework.model.bill.FastCheckAccountBatch;
import cn.eeepay.framework.model.bill.UserInfo;
import cn.eeepay.framework.model.nposp.CollectiveTransOrder;
import cn.eeepay.framework.model.nposp.MerchantInfo;
import cn.eeepay.framework.service.bill.FastCheckAccBatchService;
import cn.eeepay.framework.service.bill.FastCheckAccDetailService;
import cn.eeepay.framework.service.bill.SysDictService;
import cn.eeepay.framework.service.nposp.CollectionTransOrderService;
import cn.eeepay.framework.service.nposp.TransInfoService;
import cn.eeepay.framework.util.HttpConnectUtil;

import com.auth0.jwt.JWTSigner;
import com.fasterxml.jackson.databind.ObjectMapper;



@Service("fastCheckAccDetailService")
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class FastCheckAccDetailServiceImpl implements FastCheckAccDetailService{

	private static final Logger log = LoggerFactory.getLogger(FastCheckAccDetailServiceImpl.class);
	@Resource
	private FastCheckAccBatchService fastCheckAccBatchService;
	@Resource
	private FastCheckAccountDetailMapper fastCheckAccountDetailMapper;
	@Resource
	public TransInfoService transInfoService;
	@Resource
	private CollectionTransOrderService collectionTransOrderService;

	@Autowired
	public SysDictService sysDictService;
	@Value("${accountApi.http.url}")  
	private String accountApiHttpUrl;

	@Value("${accountApi.http.secret}")  
	private String accountApiHttpSecret;


	@Override
	public String doCheckAccount(String acqEnname,
			List<CollectiveTransOrder> transInfos, Map<String, Object> map)
					throws Exception {
		List<FastCheckAccDetail> checkAccountDetails = (List<FastCheckAccDetail>) map.get("checkAccountDetails");
		String fileName = (String)map.get("fileName");
		String checkFileDate = (String)map.get("checkFileDate");
		FastCheckAccountBatch batch = fastCheckAccBatchService.queryBatchByFileName(fileName, acqEnname);
		if(batch != null){
			return "1";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat sdfL = new SimpleDateFormat("yyyy-MM-dd");
		FastCheckAccountBatch checkAccountBatch = new FastCheckAccountBatch();

		int random = (int) (Math.random() * 1000);
		String checkBatchNo =  acqEnname+ sdf.format(new Date()) + random;
		checkAccountBatch.setCheckBatchNo(checkBatchNo);
		try {
			checkAccountBatch.setCheckFileDate(sdfL.parse(checkFileDate));
		} catch (ParseException e) {
			log.error("异常:",e);
		}
		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
				.getAuthentication()
				.getPrincipal();
		//		SysDict acqOrg = sysDictService.findSysDictByKeyValue("fast_acq_org", acqEnname);
		checkAccountBatch.setCheckFileName(fileName);
		checkAccountBatch.setCheckTime(new Date());
		checkAccountBatch.setOperator(userInfo.getUsername());
		checkAccountBatch.setAcqEnname(acqEnname);
		BigDecimal acqTotalAmount = new BigDecimal("0.00");
		BigDecimal totalAmount = new BigDecimal("0.00");
		for(int i=0; i<checkAccountDetails.size();i++){
			acqTotalAmount = acqTotalAmount.add(checkAccountDetails.get(i).getAcqTransAmount());
		}

		checkAccountBatch.setAcqTotalAmount(acqTotalAmount);

		for(int i=0; i<transInfos.size();i++){
			totalAmount = totalAmount.add(transInfos.get(i).getTransAmount());
		}
		checkAccountBatch.setTotalAmount(totalAmount);

		checkAccountBatch.setAcqTotalItems(Long.parseLong(String.valueOf(checkAccountDetails.size())));
		checkAccountBatch.setTotalItems(Long.parseLong(String.valueOf(transInfos.size())));

		int acqTotalSuccessItems = 0;
		int acqTotalFailedItems = 0;
		int totalSuccessItems = 0;
		int totalFailedItems = 0;
		int count = 0;
		log.info("对账逻辑开始:"+count++);
		for(int i=0; i<checkAccountDetails.size(); i++){			
			FastCheckAccDetail detail = checkAccountDetails.get(i);
			detail.setAcqEnname(acqEnname);			
			detail.setCheckBatchNo(checkBatchNo);			
			for(int j=0; j<transInfos.size(); j++){
				CollectiveTransOrder info = transInfos.get(j);
				boolean flag = false;
				if( info.getOrderNo().equals(detail.getAcqOrderNo())){
					flag = true;
				}
				if(flag){
					installDetailByInfo(detail, info);
					BigDecimal transAmount = detail.getAcqTransAmount();
					if(info.getTransAmount().compareTo(transAmount) == 0){
						detail.setCheckAccountStatus(DuiAccountStatus.SUCCESS.toString());
						checkAccountDetails.remove(i);
						transInfos.remove(j);
						acqTotalSuccessItems++;
						totalSuccessItems++;
					}else{
						detail.setCheckAccountStatus(DuiAccountStatus.AMOUNT_FAILED.toString());
						checkAccountDetails.remove(i);
						transInfos.remove(j);
						acqTotalFailedItems++;
						totalFailedItems++;
					}
					fastCheckAccountDetailMapper.saveFastCheckAccountDetail(detail);
					i--;
					j--;
					break;
				}
			}

			if(detail.getCheckAccountStatus() == null || detail.getCheckAccountStatus().equals("")){
				String acqOrderNo = detail.getAcqOrderNo();
				CollectiveTransOrder transInfo = collectionTransOrderService.queryByAcqFastTransInfo(acqOrderNo, acqEnname);
				if(transInfo != null){					
					if("SUCCESS".equals(transInfo.getTransStatus())){
						detail.setCheckAccountStatus(DuiAccountStatus.SUCCESS.toString());
						installDetailByInfo(detail, transInfo);
						checkAccountBatch.setTotalItems(checkAccountBatch.getTotalItems()+1);
						totalSuccessItems++;
						acqTotalSuccessItems++;
					}else{
						detail.setCheckAccountStatus(DuiAccountStatus.FAILED.toString());
						installDetailByInfo(detail, transInfo);						
						checkAccountBatch.setTotalItems(checkAccountBatch.getTotalItems()+1);
						totalFailedItems++;
						acqTotalFailedItems++;
					}


				}else{
					detail.setCheckAccountStatus(DuiAccountStatus.ACQ_SINGLE.toString());
					acqTotalFailedItems++;
				}

				detail.setCheckBatchNo(checkBatchNo);
				detail.setErrorHandleStatus("pendingTreatment");
				checkAccountDetails.remove(i);
				fastCheckAccountDetailMapper.saveFastCheckAccountDetail(detail);
				i--;

			}		
		}

		if(transInfos.size() >= 0){
			for(int i=0; i<transInfos.size(); i++){			
				CollectiveTransOrder info = transInfos.get(i);
				/*if(judgeCheckEd(info)){
					checkAccountBatch.setTotalItems(checkAccountBatch.getTotalItems()-1);
					continue;
				}*/
				FastCheckAccDetail detail = new FastCheckAccDetail();
				installDetailByInfo(detail, info);
				detail.setAcqEnname(acqEnname);
				detail.setCheckBatchNo(checkBatchNo);
				detail.setCheckAccountStatus(DuiAccountStatus.PLATE_SINGLE.toString());
				fastCheckAccountDetailMapper.saveFastCheckAccountDetail(detail);
				totalFailedItems++;
			}
			//			totalFailedItems = totalFailedItems+transInfos.size();
		}


		checkAccountBatch.setAcqTotalSuccessItems(Long.parseLong(String.valueOf(acqTotalSuccessItems)));
		checkAccountBatch.setAcqTotalFailedItems(Long.parseLong(String.valueOf(acqTotalFailedItems)));
		checkAccountBatch.setTotalSuccessItems(Long.parseLong(String.valueOf(totalSuccessItems)));
		checkAccountBatch.setTotalFailedItems(Long.parseLong(String.valueOf(totalFailedItems)));

		if(acqTotalFailedItems>0 || totalFailedItems>0){
			checkAccountBatch.setCheckResult(DuiAccountStatus.FAILED.toString());
		}else{
			checkAccountBatch.setCheckResult(DuiAccountStatus.SUCCESS.toString());
		}


		fastCheckAccBatchService.saveFastCheckAccountBatch(checkAccountBatch);

		return "0";	
	}

	/**
	 * 通过平台交易数据赋值对账明细
	 * 
	 * */
	private void installDetailByInfo(FastCheckAccDetail detail, CollectiveTransOrder info){
		detail.setPlateMerchantNo(info.getMerchantNo());
		detail.setPlateTransAmount(info.getTransAmount());
		detail.setPlateMerchantFee(info.getMerchantFee());
		detail.setPlateAcqMerchantFee(info.getAcqMerchantFee());
		detail.setPlateTransType(info.getTransType());
		detail.setPlateTransStatus(info.getTransStatus());
		detail.setPlateOrderTime(info.getCreateTime());
		detail.setPlateOrderNo(info.getOrderNo());
		detail.setPlateCardNo(info.getAccountNo());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		try {
			if(info.getTransTime()!=null){
				String merchantSettleDate = sdf.format(info.getTransTime());
				detail.setPlateMerchantSettleDate(sdf.parse(merchantSettleDate));
			}
		} catch (ParseException e) {
			log.error("异常:",e);
		}
	}

	@Override
	public Map<String, Object> confirmAccount(String checkBatchNo, String orgId)
			throws Exception {
		Map<String, Object> accMsg = new HashMap<String, Object>();
		//查询所有未成功的对账数据
		List<FastCheckAccDetail> duiacc = fastCheckAccountDetailMapper.findErrorDuiAccountDetailList2(checkBatchNo);
		if(duiacc.size()>0){
			int successCount = 0;
			int failedCount = 0;

			for (int i = 0; i < duiacc.size(); i++) {
				FastCheckAccDetail errorDetail = duiacc.get(i);
				try{
					if("PLATE_SINGLE".equals(errorDetail.getCheckAccountStatus())){//调用记账接口，平台单边记存疑
						CollectiveTransOrder transInfo = new CollectiveTransOrder();
						transInfo = collectionTransOrderService.queryByAcqFastTransInfo(errorDetail.getAcqOrderNo(), errorDetail.getAcqEnname());//对账的交易数据

						Map<String,Object> msg = this.platformSingleMarkSuspect(errorDetail ,transInfo, null, orgId) ;
						if((boolean)msg.get("state") == false){
							log.info("平台单边记存疑记账失败原因："+msg.get("msg").toString());
							String errMsg = msg.get("msg").toString();
							errMsg = errMsg.length() > 240 ? errMsg.substring(0, 240) : errMsg;
							errorDetail.setErrorHandleStatus("accountFailed");//记账失败
							errorDetail.setErrorMsg(errMsg);
							failedCount++;
						}else{
							errorDetail.setErrorHandleStatus("checkForzen");
							errorDetail.setErrorMsg("记账成功");
							successCount++;
						}

					}else if("FAILED".equals(errorDetail.getCheckAccountStatus()) || "ACQ_SINGLE".equals(errorDetail.getCheckAccountStatus())){//调用记账接口，收单单边
						CollectiveTransOrder transInfo = new CollectiveTransOrder();
						transInfo = collectionTransOrderService.queryByAcqFastTransInfo(errorDetail.getAcqOrderNo(), errorDetail.getAcqEnname());
						Map<String,Object> msg = this.acqSingleMarkSuspect(errorDetail ,transInfo,orgId) ;
						if((boolean)msg.get("state") == false){
							String errMsg = msg.get("msg").toString();
							errMsg = errMsg.length() > 240 ? errMsg.substring(0, 240) : errMsg;
							errorDetail.setErrorHandleStatus("accountFailed");//记账失败
							errorDetail.setErrorMsg(errMsg);
							failedCount++;
							log.info("收单单边记账失败原因："+msg.get("msg").toString());
						}else{
							errorDetail.setErrorHandleStatus("upstreamDoubt");
							errorDetail.setErrorMsg("记账成功");
							successCount++;
						}
					}
				}catch(Exception e){
					errorDetail.setErrorHandleStatus("accountFailed");//记账失败
					errorDetail.setErrorMsg("记账过程异常");
					failedCount++;
				}
				fastCheckAccountDetailMapper.updateDuiAccount(errorDetail) ;
			}
			accMsg.put("state", "true");
			accMsg.put("msg", "记账成功"+successCount+"条，记账失败"+failedCount+"条");
		}else{
			accMsg.put("state", "false");
			accMsg.put("msg", "无差错数据需要处理！");
		}
		return accMsg;
	}

	/**
	 * 平台单边记存疑  11 (系统自动冻结调)√
	 */
	@Override
	public Map<String, Object> platformSingleMarkSuspect(FastCheckAccDetail duiAccountDetail, CollectiveTransOrder transInfo, MerchantInfo merInfo, String acqOrgId) throws Exception {
		Map<String,Object> msg=new HashMap<>();

		//调用6.4.8平台单边记存疑
		final String secret = accountApiHttpSecret;

		final long iat = System.currentTimeMillis() / 1000l; // issued at claim 
		final long exp = iat + 180L; // expires claim. In this case the token expires in 60 seconds
		final String jti = UUID.randomUUID().toString();
		final JWTSigner signer = new JWTSigner(secret);
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd") ;

		if(transInfo==null){
			msg.put("state",false);
			msg.put("msg","找不到交易源！");
			return msg;
		}
		try{
			claims.put("fromSystem", "accountWeb");
			claims.put("transDate", sdf.format(transInfo.getTransTime()));
			claims.put("fromSerialNo", duiAccountDetail.getId().toString());
			claims.put("transAmount", transInfo.getTransAmount() == null ?null:transInfo.getTransAmount().toString()) ;//交易金额
			claims.put("merchantNo", transInfo.getMerchantNo());
			claims.put("merchantFee", transInfo.getMerchantFee().toString());
			/*	claims.put("directAgentNo", merInfo.getAgentNo());
		    	claims.put("oneAgentNo", merInfo.getOneAgentNo());*/
			claims.put("acqEnname", transInfo.getAcqEnname());
			claims.put("serviceId", transInfo.getServiceId().toString());
			claims.put("acqServiceId", transInfo.getAcqServiceId().toString());
			claims.put("transTypeCode", "000008");
			claims.put("acqOrgId", acqOrgId);
			claims.put("transOrderNo", transInfo.getId().toString());
			claims.put("exp", exp);
			claims.put("iat", iat);
			claims.put("jti", jti);

			final String token = signer.sign(claims);
			String url = accountApiHttpUrl+"/recordAccountController/platformSingleMarkSuspect.do" ;
			String response = HttpConnectUtil.postHttp(url, "token", token);
			log.info("平台单边记存疑返回结果："+response);

			if (response == null || "".equals(response)) {
				//平台单边确认是日切
				msg.put("state",false);
				msg.put("msg","平台单边记存疑返回为空！");
				return msg;
			}else{
				ObjectMapper om = new ObjectMapper();
				Map<String, Object> resp = om.readValue(response, Map.class);
				if((boolean)resp.get("status") == false){
					//平台单边确认是日切
					msg.put("state",false);
					msg.put("msg","平台单边记存疑失败，reason:"+resp.get("msg").toString());
					return msg;
				}
			}
			/*//3、修改对账详细信息中的    差错处理状态   为'系统自动冻结'
				duiAccountDetail.setErrorHandleStatus("checkForzen");
				duiAccountDetailDao.updateDuiAccountDetail(duiAccountDetail) ;*/
			msg.put("state",true);
			msg.put("msg","平台单边记存疑成功！");
		}catch(Exception e){
			log.info("平台单边赔付商户异常"+e.getMessage());
			msg.put("state",false);
			msg.put("msg","平台单边记存疑异常！");
			log.error("异常:",e);
		}
		return msg;
	}

	/**
	 * 上游单边记存疑问(上传对账文件时，系统自动对账，自动调用)
	 */
	@Override
	public Map<String, Object> acqSingleMarkSuspect(FastCheckAccDetail duiAccountDetail, CollectiveTransOrder transInfo,String acqOrgId) throws Exception {
		Map<String,Object> msg=new HashMap<>();

		//调用6.4.8上游单边记存疑
		final String secret = accountApiHttpSecret;

		final long iat = System.currentTimeMillis() / 1000l; // issued at claim 
		final long exp = iat + 180L; // expires claim. In this case the token expires in 60 seconds
		final String jti = UUID.randomUUID().toString();
		final JWTSigner signer = new JWTSigner(secret);
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd") ;

		if(transInfo == null){
			msg.put("state",false);
			msg.put("msg","无交易来源！");
			return msg;
		}
		try{
			claims.put("fromSystem", "accountWeb");
			claims.put("transDate", transInfo.getTransTime() == null ?null:sdf.format(transInfo.getCreateTime())) ;//交易日期
			claims.put("fromSerialNo", duiAccountDetail.getId().toString());
			claims.put("transAmount", transInfo.getTransAmount() == null ?new BigDecimal("0.00").toString():transInfo.getTransAmount().toString()) ;//交易金额
			claims.put("merchantNo", transInfo.getMerchantNo());
			claims.put("merchantFee", transInfo.getMerchantFee().toString());
			//	    	claims.put("directAgentNo", "248");
			//	    	claims.put("oneAgentNo", "248");
			claims.put("acqEnname", transInfo.getAcqEnname());
			claims.put("acqServiceId", transInfo.getAcqServiceId().toString());
			claims.put("transTypeCode", "000004");
			claims.put("acqOrgId", acqOrgId);
			claims.put("transOrderNo", transInfo.getId().toString());
			claims.put("cardType", transInfo.getCardType());
			claims.put("exp", exp);
			claims.put("iat", iat);
			claims.put("jti", jti);

			final String token = signer.sign(claims);
			String url = accountApiHttpUrl+"/recordAccountController/acqSingleMarkSuspect.do" ;
			String response = HttpConnectUtil.postHttp(url, "token", token);
			log.info("上游单边记存疑问返回结果："+response);

			ObjectMapper om = new ObjectMapper();
			Map<String, Object> resp = om.readValue(response, Map.class);
			if (response == null || "".equals(response) || (boolean)resp.get("status") == false) {
				//上游单边，结算给商户失败
				msg.put("state",false);
				msg.put("msg","上游单边记存疑问失败,reason:"+resp.get("msg").toString());
				return msg;
			}
			/*	//3、修改对账详细信息中的    差错处理状态   为'上游单边记存疑问'
				duiAccountDetail.setErrorHandleStatus("upstreamDoubt");
				duiAccountDetailDao.updateDuiAccount(duiAccountDetail) ;*/
			msg.put("state",true);
			msg.put("msg","上游单边记存疑问成功！");
		}catch(Exception e){
			log.info("上游单边记存疑问异常");
			msg.put("state",false);
			msg.put("msg","上游单边记存疑问异常！");
			log.error("异常:",e);
		}
		return msg;
	}

	@Override
	public List<FastCheckAccDetail> findExportDuiAccountDetailList(
			String createTimeStart, String createTimeEnd,
			FastCheckAccDetail duiAccountDetail) throws Exception {
		if(StringUtils.isNotBlank(createTimeStart)){
			createTimeStart = createTimeStart + " 00:00:00" ;
		}
		if(StringUtils.isNotBlank(createTimeEnd)){
			createTimeEnd = createTimeEnd + " 23:59:59" ;
		}
		return fastCheckAccountDetailMapper.findExportDuiAccountDetailList(createTimeStart,createTimeEnd,duiAccountDetail);
	}

	@Override
	public List<FastCheckAccDetail> findErrorExportDuiAccountDetailList(
			String createTimeStart, String createTimeEnd,
			FastCheckAccDetail duiAccountDetail) throws Exception {
		if(StringUtils.isNotBlank(createTimeStart)){
			createTimeStart = createTimeStart + " 00:00:00" ;
		}
		if(StringUtils.isNotBlank(createTimeEnd)){
			createTimeEnd = createTimeEnd + " 23:59:59" ;
		}
		return fastCheckAccountDetailMapper.findErrorExportDuiAccountDetailList(createTimeStart,createTimeEnd,duiAccountDetail);
	}

	@Override
	public List<FastCheckAccDetail> findDuiAccountDetailList(
			String createTimeStart, String createTimeEnd,
			FastCheckAccDetail duiAccountDetail, Sort sort,
			Page<FastCheckAccDetail> page) throws Exception {
		if(StringUtils.isNotBlank(createTimeStart)){
			createTimeStart = createTimeStart + " 00:00:00" ;
		}
		if(StringUtils.isNotBlank(createTimeEnd)){
			createTimeEnd = createTimeEnd + " 23:59:59" ;
		}
		return fastCheckAccountDetailMapper.findQueryDuiAccountDetailList(createTimeStart,createTimeEnd,duiAccountDetail, sort, page);
	}

	@Override
	public List<FastCheckAccDetail> findErrorDuiAccountDetailList(
			String createTimeStart, String createTimeEnd,
			FastCheckAccDetail duiAccountDetail, Sort sort,
			Page<FastCheckAccDetail> page) throws Exception {
		if(StringUtils.isNotBlank(createTimeStart)){
			createTimeStart = createTimeStart + " 00:00:00" ;
		}
		if(StringUtils.isNotBlank(createTimeEnd)){
			createTimeEnd = createTimeEnd + " 23:59:59" ;
		}
		return fastCheckAccountDetailMapper.findErrorDuiAccountDetailList(createTimeStart,createTimeEnd,duiAccountDetail, sort, page);
	}

	@Override
	public FastCheckAccDetail findDuiAccountDetailById(String id)
			throws Exception {
		return fastCheckAccountDetailMapper.findDuiAccountDetailById(id);
	}

	/**
	 * 平台单边确认是日切(解冻调)【平台单边正常解冻结算】
	 */
	@Override
	public Map<String, Object> platformSingleForDayCut(FastCheckAccDetail duiAccountDetail, CollectiveTransOrder transInfo, MerchantInfo merInfo, AcqOrg acqOrg) throws Exception {
		Map<String,Object> msg=new HashMap<>();

		//调用6.4.13平台单边确认是日切
		final String secret = accountApiHttpSecret;

		final JWTSigner signer = new JWTSigner(secret);
		final HashMap<String, Object> claims = new HashMap<String, Object>();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd") ;
		if(transInfo==null){
			String errorMsg = "找不到交易源！";
			msg.put("state",false);
			msg.put("msg",errorMsg);
			//			duiAccountDetail.setErrorHandleStatus("accountFailed");
			duiAccountDetail.setErrorMsg(errorMsg);
			fastCheckAccountDetailMapper.updateDuiAccount(duiAccountDetail) ;
			return msg;
		}    

		try{
			claims.put("fromSystem", "accountWeb");
			claims.put("transDate", transInfo.getTransTime() == null ?null:sdf.format(transInfo.getTransTime()));
			claims.put("fromSerialNo", duiAccountDetail.getId().toString());
			claims.put("transAmount", transInfo.getTransAmount() == null ?new BigDecimal("0.00").toString():transInfo.getTransAmount().toString());
			claims.put("merchantNo", transInfo.getMerchantNo());
			claims.put("merchantFee", transInfo.getMerchantFee().toString());     //商户费率
			claims.put("directAgentNo", merInfo.getAgentNo());
			claims.put("oneAgentNo", merInfo.getOneAgentNo());          //一级代理商
			claims.put("acqEnname", transInfo.getAcqEnname());
			claims.put("serviceId", transInfo.getServiceId().toString());
			claims.put("acqServiceId", transInfo.getAcqServiceId().toString());     //收单服务
			claims.put("transTypeCode", "000009");
			claims.put("acqOrgId",acqOrg.getId().toString());
			claims.put("transOrderNo", transInfo.getId().toString());
			claims.put("cardType", transInfo.getCardType());
			claims.put("agentShareAmount", transInfo.getProfits1().toString());
			final String token = signer.sign(claims);
			String url = accountApiHttpUrl+"/recordAccountController/platformSingleForDayCut.do" ;
			String response = HttpConnectUtil.postHttp(url, "token", token);
			log.info("平台单边确认是日切【平台单边正常解冻结算】返回结果："+response);

			if (response == null || "".equals(response)) {
				//平台单边确认是日切
				String errorMsg = "平台单边确认是日切【平台单边正常解冻结算】 返回为空";
				msg.put("state",false);
				msg.put("msg",errorMsg);
				//				duiAccountDetail.setErrorHandleStatus("accountFailed");
				duiAccountDetail.setErrorMsg(errorMsg);
			}else{
				ObjectMapper om = new ObjectMapper();
				Map<String, Object> resp = om.readValue(response, Map.class);
				if((boolean)resp.get("status") == false){
					//平台单边确认是日切
					String errMsg = "";
					if(resp.get("msg")==null || resp.get("msg")==""){
						errMsg ="平台单边确认是日切【平台单边正常解冻结算】，message:返回为空";
					}else{
						errMsg = "平台单边确认是日切【平台单边正常解冻结算】记账失败，reason："+resp.get("msg").toString();
					}
					errMsg = errMsg.length() > 240 ? errMsg.substring(0, 240) : errMsg;
					msg.put("state",false);
					msg.put("msg",errMsg);

					//				    duiAccountDetail.setErrorHandleStatus("accountFailed");
					duiAccountDetail.setErrorMsg(errMsg);
				}else{
					//3、修改对账详细信息中的    差错处理状态   为'解冻结算'
					duiAccountDetail.setErrorHandleStatus("thawSettle");
					duiAccountDetail.setErrorMsg("记账成功");

					msg.put("state",true);
					msg.put("msg","平台单边确认是日切【平台单边正常解冻结算】成功！");
				}
			}

		}
		catch(Exception e){
			log.info("平台单边确认是日切【平台单边正常解冻结算】异常"+e.getMessage());
			String errMsg = e.getMessage();
			errMsg = errMsg.length() > 240 ? errMsg.substring(0, 240) : errMsg;
			//		     duiAccountDetail.setErrorHandleStatus("accountFailed");
			duiAccountDetail.setErrorMsg(errMsg);

			msg.put("state",false);
			msg.put("msg","平台单边确认是日切【平台单边正常解冻结算】异常！");
			log.error("异常:",e);
		}
		fastCheckAccountDetailMapper.updateDuiAccount(duiAccountDetail) ;
		return msg;
	}

	/**
	 * 平台单边赔付商户 (平台单边赔付商户 )调【交易解冻】
	 */
	@Override
	public Map<String, Object> platformSingleSettleToMerchant(FastCheckAccDetail duiAccountDetail, CollectiveTransOrder transInfo, MerchantInfo merInfo, AcqOrg acqOrg) throws Exception {
		Map<String,Object> msg=new HashMap<>();

		//调用6.4.13平台单边赔付商户
		final String secret = accountApiHttpSecret;

		final JWTSigner signer = new JWTSigner(secret);
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd") ;
		if(transInfo==null){
			String errorMsg = "找不到交易源！";
			msg.put("state",false);
			msg.put("msg",errorMsg);
			//			duiAccountDetail.setErrorHandleStatus("accountFailed");
			duiAccountDetail.setErrorMsg(errorMsg);
			fastCheckAccountDetailMapper.updateDuiAccount(duiAccountDetail) ;
			return msg;
		} 
		try{
			claims.put("fromSystem", "accountWeb");
			claims.put("transDate", sdf.format(transInfo.getTransTime()));
			claims.put("transAmount", transInfo.getTransAmount() == null ?new BigDecimal("0.00").toString():transInfo.getTransAmount().toString());
			claims.put("fromSerialNo", duiAccountDetail.getId().toString());
			claims.put("transOrderNo", transInfo.getId().toString());
			claims.put("oneAgentNo", merInfo.getOneAgentNo());
			claims.put("directAgentNo", merInfo.getAgentNo());
			claims.put("merchantNo", transInfo.getMerchantNo());
			claims.put("transTypeCode", "000014"); //交易解冻
			claims.put("serviceId", transInfo.getServiceId().toString());
			claims.put("acqServiceId", transInfo.getAcqServiceId().toString());
			claims.put("cardNo", transInfo.getAccountNo());
			//        claims.put("holidays", "1");
			claims.put("acqOrgId", acqOrg.getId().toString());
			claims.put("acqEnname", transInfo.getAcqEnname());
			claims.put("agentShareAmount", transInfo.getProfits1().toString());//单笔分润金额
			claims.put("cardType", transInfo.getCardType());
			claims.put("merchantFee", transInfo.getMerchantFee().toString());
			//claims.put("exp", exp);
			//claims.put("iat", iat);
			//claims.put("jti", jti);

			final String token = signer.sign(claims);
			String url = accountApiHttpUrl+"/recordAccountController/transFreeze.do" ;
			String response = HttpConnectUtil.postHttp(url, "token", token);
			log.info("平台单边赔付商户【交易解冻】返回结果："+response);

			ObjectMapper om = new ObjectMapper();
			Map<String, Object> resp = om.readValue(response, Map.class);
			if (response == null || "".equals(response)) {
				//平台单边确认是日切
				String errorMsg = "平台单边赔付商户【交易解冻】 返回为空";
				msg.put("state",false);
				msg.put("msg",errorMsg);
				//				duiAccountDetail.setErrorHandleStatus("accountFailed");
				duiAccountDetail.setErrorMsg(errorMsg);
			}else{
				if((boolean)resp.get("status") == false){
					String errMsg = "";
					if(resp.get("msg")==null || resp.get("msg")==""){
						errMsg+="平台单边赔付商户【交易解冻】;message:返回为空";
					}else{
						errMsg = "平台单边赔付商户【交易解冻】记账失败,reason:"+resp.get("msg").toString();
						errMsg = errMsg.length() > 240 ? errMsg.substring(0, 240) : errMsg;
					}
					msg.put("state",false);
					msg.put("msg",errMsg);

					//				    duiAccountDetail.setErrorHandleStatus("accountFailed");
					duiAccountDetail.setErrorMsg(errMsg);
				}else{
					//3、修改对账详细信息中的    差错处理状态   为'平台单边赔付商户'
					duiAccountDetail.setErrorHandleStatus("platformPayment");
					duiAccountDetail.setErrorMsg("记账成功");

					msg.put("state",true);
					msg.put("msg","平台单边赔付商户【交易解冻】修改成功！");
				}
			}

		}catch(Exception e){
			String errMsg = "平台单边赔付商户【交易解冻】异常";
			//      	   	duiAccountDetail.setErrorHandleStatus("accountFailed");//记账失败
			duiAccountDetail.setErrorMsg(errMsg);
			log.info(errMsg);
			msg.put("state",false);
			msg.put("msg",errMsg);
			log.error("异常:",e);

		}
		fastCheckAccountDetailMapper.updateDuiAccount(duiAccountDetail) ;
		return msg;
	}

	/**
	 * 上游单边补记账结算商户
	 */
	@Override
	public Map<String, Object> acqSingleSettleToMerchant(FastCheckAccDetail duiAccountDetail,CollectiveTransOrder transInfo ,MerchantInfo merInfo,AcqOrg acqOrg) throws Exception {
		Map<String,Object> msg=new HashMap<>();

		//调用6.4.8上游单边补记账结算商户
		final String secret = accountApiHttpSecret;

        final JWTSigner signer = new JWTSigner(secret);
        final HashMap<String, Object> claims = new HashMap<String, Object>();
       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd") ;
//       if(transInfo==null){
//	       	String errorMsg = "找不到交易源！";
//	       	msg.put("state",false);
//			msg.put("msg",errorMsg);
////			duiAccountDetail.setErrorHandleStatus("accountFailed");
//			duiAccountDetail.setErrorMsg(errorMsg);
//			fastCheckAccountDetailMapper.updateDuiAccount(duiAccountDetail) ;
//			return msg;
//       } 
       try{
//    	   claims.put("fromSystem", "accountWeb");
//			claims.put("transDate", duiAccountDetail.getAcqTransTime() == null ?null:sdf.format(duiAccountDetail.getAcqTransTime())) ;//交易日期
//			claims.put("fromSerialNo", duiAccountDetail.getId().toString());
//			claims.put("transAmount", duiAccountDetail.getAcqTransAmount() == null ? BigDecimal.ZERO.toString() : duiAccountDetail.getAcqTransAmount().toString()) ;//交易金额
//			claims.put("acqEnname", duiAccountDetail.getAcqEnname());
//			claims.put("transTypeCode", "000007");
//			claims.put("acqOrgId", acqOrg.getId().toString());
//			claims.put("transOrderNo", duiAccountDetail.getAcqSerialNo());
//			claims.put("acqServiceCostMoney", duiAccountDetail.getAcqRefundAmount().toString());
    	   
    	   claims.put("acqServiceCostMoney", duiAccountDetail.getAcqRefundAmount().toString());
	       claims.put("fromSystem", "accountWeb");
	       claims.put("transDate", sdf.format(duiAccountDetail.getAcqOrderTime()));
	  	   claims.put("fromSerialNo", transInfo.getId().toString());
	  	   claims.put("transAmount", transInfo.getTransAmount() == null ?new BigDecimal("0.00").toString():transInfo.getTransAmount().toString());
	  	   claims.put("merchantNo", transInfo.getMerchantNo());
	  	   claims.put("merchantFee", transInfo.getMerchantFee().toString());
	  	   claims.put("directAgentNo", merInfo.getAgentNo());//直属代理商
	  	   claims.put("oneAgentNo", merInfo.getOneAgentNo());//一级代理商
	  	   claims.put("acqEnname", transInfo.getAcqEnname());
	  	   claims.put("serviceId", transInfo.getServiceId().toString());//服务
	  	   claims.put("acqServiceId", transInfo.getAcqServiceId().toString());//收单服务
	       claims.put("transTypeCode", "000007");
		   claims.put("acqOrgId",acqOrg.getId().toString());
		   claims.put("transOrderNo", transInfo.getOrderNo().toString());
		   claims.put("agentShareAmount", transInfo.getProfits1().toString());
    	   
	        final String token = signer.sign(claims);
	        String url = accountApiHttpUrl+"/recordAccountController/acqSingleSettleToMerchant.do" ;
			String response = HttpConnectUtil.postHttp(url, "token", token);
			log.info("上游单边补记账结算商户返回信息："+response);
//			duiAccountDetail.setErrorHandleStatus("upstreamRecordAccount");
//			duiAccountDetail.setErrorMsg("记账成功");
//			msg.put("state",true);
//			msg.put("msg","修改差错账状态成功，请手工进行调账扣减余额");
			
			ObjectMapper om = new ObjectMapper();
			Map<String, Object> resp = om.readValue(response, Map.class);
			if (response == null || "".equals(response)) {
				//平台单边确认是日切
				String errorMsg = "上游单边补记账结算账户 返回为空";
				msg.put("state",false);
				msg.put("msg",errorMsg);
//				duiAccountDetail.setErrorHandleStatus("accountFailed");
				duiAccountDetail.setErrorMsg(errorMsg);
			}else{
				if((boolean)resp.get("status") == false){
					String errMsg = "";
					if(resp.get("msg")==null || resp.get("msg")==""){
						errMsg+="上游单边补记账结算账户,message:返回为空";
					}else{
						errMsg = "上游单边补记账结算账户记账失败,reason:"+resp.get("msg").toString();
					}
					errMsg = errMsg.length() > 240 ? errMsg.substring(0, 240) : errMsg;
					msg.put("state",false);
					msg.put("msg",errMsg);
					
//				    duiAccountDetail.setErrorHandleStatus("accountFailed");
					duiAccountDetail.setErrorMsg(errMsg);
				}else{
					//3、修改对账详细信息中的    差错处理状态   为'上游单边补记账结算商户'
					duiAccountDetail.setErrorHandleStatus("upstreamRecordAccount");
					duiAccountDetail.setErrorMsg("记账成功");
					msg.put("state",true);
					msg.put("msg","上游单边补记账结算账户成功！");
				}
			}
			
	       }catch (Exception e) {
	    	   String errMsg = "上游单边补记账结算账户异常";
	//    	   duiAccountDetail.setErrorHandleStatus("accountFailed");//记账失败
		       duiAccountDetail.setErrorMsg(errMsg);
		       	log.info(errMsg);
		       	msg.put("state",false);
		   		msg.put("msg",errMsg);
		       	log.error("异常:",e);
	       }
		fastCheckAccountDetailMapper.updateDuiAccount(duiAccountDetail);
		return msg;
	}

	/**
	 * 上游单边退款给持卡人
	 */
	@Override
	public Map<String, Object> acqSingleBackMoneyToOwner(FastCheckAccDetail duiAccountDetail,AcqOrg acqOrg) throws Exception {
		Map<String,Object> msg=new HashMap<>();

		//调用6.4.8上游单边退款给持卡人
		final String secret = accountApiHttpSecret;

		final JWTSigner signer = new JWTSigner(secret);
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd") ;
		if(duiAccountDetail==null){
			String errorMsg = "找不到交易源！";
			msg.put("state",false);
			msg.put("msg",errorMsg);
			//			duiAccountDetail.setErrorHandleStatus("accountFailed");
			duiAccountDetail.setErrorMsg(errorMsg);
			fastCheckAccountDetailMapper.updateDuiAccount(duiAccountDetail) ;
			return msg;
		} 
		try{

			claims.put("fromSystem", "accountWeb");
			claims.put("transDate", duiAccountDetail.getAcqOrderTime() == null ?null:sdf.format(duiAccountDetail.getAcqOrderTime())) ;//交易日期
			claims.put("fromSerialNo", duiAccountDetail.getId().toString());
			claims.put("transAmount", duiAccountDetail.getAcqTransAmount() == null ? BigDecimal.ZERO.toString() : duiAccountDetail.getAcqTransAmount().toString()) ;//交易金额
			claims.put("acqEnname", duiAccountDetail.getAcqEnname());
			claims.put("transTypeCode", "000006");
			claims.put("acqOrgId", acqOrg.getId().toString());
			claims.put("transOrderNo", duiAccountDetail.getAcqOrderNo());
			claims.put("acqServiceCostMoney", duiAccountDetail.getAcqRefundAmount().toString());

			//    	claims.put("fromSystem", "accountWeb");
			//    	claims.put("transDate", sdf.format(transInfo.getCreateTime()));
			//    	claims.put("fromSerialNo", duiAccountDetail.getId().toString());
			//    	claims.put("transAmount", transInfo.getTransAmount().toString());
			//    	claims.put("merchantNo", transInfo.getMerchantNo().toString());
			//    	claims.put("merchantFee", transInfo.getMerchantFee().toString());
			//    	claims.put("directAgentNo", merInfo.getAgentNo());
			//    	claims.put("oneAgentNo", merInfo.getOneAgentNo());
			//    	claims.put("acqEnname", transInfo.getAcqEnname());
			//    	claims.put("acqServiceId", transInfo.getAcqServiceId().toString());
			//        claims.put("transTypeCode", "000006");
			//		claims.put("acqOrgId", acqOrg.getId().toString());
			//		claims.put("transOrderNo", transInfo.getId().toString());
			//		claims.put("agentShareAmount", transInfo.getProfits1().toString());
			//        System.out.println(claims.toString());

			final String token = signer.sign(claims);
			String url = accountApiHttpUrl+"/recordAccountController/acqSingleBackMoneyToOwner.do" ;
			String response = HttpConnectUtil.postHttp(url, "token", token);
			log.info("上游单边退款给持卡人返回结果："+response);

			ObjectMapper om = new ObjectMapper();
			Map<String, Object> resp = om.readValue(response, Map.class);
			if (response == null || "".equals(response)) {
				//平台单边确认是日切
				String errorMsg = "上游单边退款给持卡人 返回为空";
				msg.put("state",false);
				msg.put("msg",errorMsg);
				//			duiAccountDetail.setErrorHandleStatus("accountFailed");
				duiAccountDetail.setErrorMsg(errorMsg);
			}else{
				if((boolean)resp.get("status") == false){
					String errMsg = "";
					if(resp.get("msg")==null || resp.get("msg")==""){
						errMsg+="上游单边退款给持卡人,message:返回为空";
					}else{
						errMsg = "上游单边退款给持卡人记账失败,reason:"+resp.get("msg").toString();
					}
					errMsg = errMsg.length() > 240 ? errMsg.substring(0, 240) : errMsg;
					msg.put("state",false);
					msg.put("msg",errMsg);

					//			    duiAccountDetail.setErrorHandleStatus("accountFailed");
					duiAccountDetail.setErrorMsg(errMsg);
				}else{
					//3、修改对账详细信息中的    差错处理状态   为'财务退款'
					duiAccountDetail.setErrorHandleStatus("upstreamRefund");
					duiAccountDetail.setErrorMsg("记账成功");
					msg.put("state",true);
					msg.put("msg","上游单边退款给持卡人成功！");
				}
			}
			//******财务自己线下处理*******
		}catch(Exception e){
			String errMsg = "上游单边退款给持卡人异常";
			//     	   duiAccountDetail.setErrorHandleStatus("accountFailed");//记账失败
			duiAccountDetail.setErrorMsg(errMsg);
			log.info(errMsg);
			msg.put("state",false);
			msg.put("msg",errMsg);
			log.error("异常:",e);
		}
		fastCheckAccountDetailMapper.updateDuiAccount(duiAccountDetail) ;
		return msg;
	}

	/**
	 * 上游单边确认是日切
	 */
	@Override
	public Map<String, Object> acqSingleThaw(FastCheckAccDetail duiAccountDetail,AcqOrg acqOrg) throws Exception {
		Map<String,Object> msg=new HashMap<>();

		//调用6.4.9上游单边确认是日切
		final String secret = accountApiHttpSecret;

		final JWTSigner signer = new JWTSigner(secret);
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd") ;
		if(duiAccountDetail==null){
			String errorMsg = "找不到交易源！";
			msg.put("state",false);
			msg.put("msg",errorMsg);
			//			duiAccountDetail.setErrorHandleStatus("accountFailed");
			duiAccountDetail.setErrorMsg(errorMsg);
			fastCheckAccountDetailMapper.updateDuiAccount(duiAccountDetail) ;
			return msg;
		} 
		try{

			claims.put("fromSystem", "accountWeb");
			claims.put("transDate", duiAccountDetail.getAcqOrderTime() == null ?null:sdf.format(duiAccountDetail.getAcqOrderTime())) ;//交易日期
			claims.put("fromSerialNo", duiAccountDetail.getId().toString());
			claims.put("transAmount", duiAccountDetail.getAcqTransAmount() == null ? BigDecimal.ZERO.toString() : duiAccountDetail.getAcqTransAmount().toString()) ;//交易金额
			claims.put("acqEnname", duiAccountDetail.getAcqEnname());
			claims.put("transTypeCode", "000005");
			claims.put("acqOrgId", acqOrg.getId().toString());
			claims.put("transOrderNo", duiAccountDetail.getAcqOrderNo());
			claims.put("acqServiceCostMoney", duiAccountDetail.getAcqRefundAmount().toString());

			final String token = signer.sign(claims);
			String url = accountApiHttpUrl+"/recordAccountController/acqSingleForDayCut.do" ;
			String response = HttpConnectUtil.postHttp(url, "token", token);
			log.info("上游单边退款给持卡人返回结果："+response);

			ObjectMapper om = new ObjectMapper();
			Map<String, Object> resp = om.readValue(response, Map.class);
			if (response == null || "".equals(response)) {
				String errorMsg = "上游单边退款给持卡人 返回为空";
				msg.put("state",false);
				msg.put("msg",errorMsg);
				duiAccountDetail.setErrorMsg(errorMsg);
			}else{
				if((boolean)resp.get("status") == false){
					String errMsg = "";
					if(resp.get("msg")==null || resp.get("msg")==""){
						errMsg+="上游单边确认是日切,message:返回为空";
					}else{
						errMsg = "上游单边确认是日切记账失败,reason:"+resp.get("msg").toString();
					}
					errMsg = errMsg.length() > 240 ? errMsg.substring(0, 240) : errMsg;
					msg.put("state",false);
					msg.put("msg",errMsg);

					//			    duiAccountDetail.setErrorHandleStatus("accountFailed");
					duiAccountDetail.setErrorMsg(errMsg);
				}else{
					//3、修改对账详细信息中的    差错处理状态   为'财务退款'
					duiAccountDetail.setErrorHandleStatus("upstreamThaw");
					duiAccountDetail.setErrorMsg("记账成功");
					msg.put("state",true);
					msg.put("msg","上游单边确认是日切成功！");
				}
			}
			//******财务自己线下处理*******
		}catch(Exception e){
			String errMsg = "上游单边确认是日切异常";
			//     	   duiAccountDetail.setErrorHandleStatus("accountFailed");//记账失败
			duiAccountDetail.setErrorMsg(errMsg);
			log.info(errMsg);
			msg.put("state",false);
			msg.put("msg",errMsg);
			log.error("异常:",e);
		}
		fastCheckAccountDetailMapper.updateDuiAccount(duiAccountDetail) ;
		return msg;
	}
	@Override
	public int saveFastCheckAccountDetail(FastCheckAccDetail dui) {
		return fastCheckAccountDetailMapper.saveFastCheckAccountDetail(dui);
	}

	@Override
	public List<FastCheckAccDetail> findErrorDuiAccountDetailList(
			String checkBatchNo) {
		return fastCheckAccountDetailMapper.findErrorDuiAccountDetailList2(checkBatchNo);
	}

	@Override
	public int updateDuiAccount(FastCheckAccDetail dui) {
		return fastCheckAccountDetailMapper.updateDuiAccount(dui);
	}

	@Override
	public int updateRemark(FastCheckAccDetail detail) {
		return fastCheckAccountDetailMapper.updateRemark(detail);
	}

}
