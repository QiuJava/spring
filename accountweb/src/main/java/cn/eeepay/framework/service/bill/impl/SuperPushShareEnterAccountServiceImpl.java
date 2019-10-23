package cn.eeepay.framework.service.bill.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auth0.jwt.JWTSigner;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.eeepay.framework.dao.bill.AgentShareDaySettleMapper;
import cn.eeepay.framework.enums.EnterAccountStatus;
import cn.eeepay.framework.enums.ReverseFlag;
import cn.eeepay.framework.model.bill.AgentPreRecordTotal;
import cn.eeepay.framework.model.bill.AgentShareDaySettle;
import cn.eeepay.framework.model.bill.SuperPushShareDaySettle;
import cn.eeepay.framework.service.bill.AgentPreFreezeService;
import cn.eeepay.framework.service.bill.AgentPreRecordTotalService;
import cn.eeepay.framework.service.bill.AgentShareDaySettleService;
import cn.eeepay.framework.service.bill.DuiAccountDetailService;
import cn.eeepay.framework.service.bill.ExtAccountService;
import cn.eeepay.framework.service.bill.GenericTableService;
import cn.eeepay.framework.service.bill.SuperPushShareDaySettleService;
import cn.eeepay.framework.service.bill.SuperPushShareEnterAccountService;
import cn.eeepay.framework.service.bill.SysDictService;
import cn.eeepay.framework.service.bill.TransShortInfoService;
import cn.eeepay.framework.service.nposp.AgentInfoService;
import cn.eeepay.framework.util.DateUtil;
import cn.eeepay.framework.util.HttpConnectUtil;

@Service("superPushShareEnterAccountService")
@Transactional
public class SuperPushShareEnterAccountServiceImpl implements SuperPushShareEnterAccountService{
	private static final Logger log = LoggerFactory.getLogger(SuperPushShareEnterAccountServiceImpl.class);
	@Resource
	public AgentShareDaySettleMapper agentShareDaySettleMapper;
	@Resource
	public GenericTableService genericTableService;
	@Resource
	public TransShortInfoService transShortInfoService;
	@Resource
	public AgentPreRecordTotalService agentPreRecordTotalService;
	@Resource
	public DuiAccountDetailService duiAccountDetailService;
	@Resource
	public ExtAccountService extAccountService;
	@Resource
	public SysDictService sysDictService;
	@Resource
	public AgentPreFreezeService agentPreFreezeService;
	@Resource
	public AgentInfoService agentInfoService;
	@Resource
	public SuperPushShareDaySettleService superPushShareDaySettleService;
	
	@Value("${accountApi.http.url}")
	private String accountApiHttpUrl;

	@Value("${accountApi.http.secret}")
	private String accountApiHttpSecret;

	/**
	 * 执行入账记账操作
	 */
	@Override
	public Map<String, Object> agentProfitEnterAccount(SuperPushShareDaySettle superPushShareDaySettle)
			throws JsonMappingException, IOException, Exception {
		Map<String,Object> msg=new HashMap<>();
		
//		String parentAgentNo = agentShareDaySettle.getParentAgentNo();
//		String agentLevel = agentShareDaySettle.getAgentLevel();
		String batchNo = superPushShareDaySettle.getCollectionBatchNo();
		
		String shareType = superPushShareDaySettle.getShareType();
		
		Map<String,Object> enterAccountRecordResult = new HashMap<>();
		if (shareType.equals("0") || shareType.equals("1")) {
			enterAccountRecordResult = agentProfitRecordAccount(superPushShareDaySettle);
		}
		else{
			enterAccountRecordResult = mchProfitRecordAccount(superPushShareDaySettle);
			
		}
		
		Boolean enterAccountRecordResultStatus = (Boolean) enterAccountRecordResult.get("status");
		String enterAccountRecordResultMsg = (String) enterAccountRecordResult.get("msg");
		
		if (enterAccountRecordResultStatus) {
			log.info("代理商编号{} 入账记账 成功",superPushShareDaySettle.getShareNo());
		}
		else{
			log.info("代理商编号{}  代理商分润入账 记账 {}", new Object[]{superPushShareDaySettle.getShareNo(),enterAccountRecordResultMsg});
			throw new RuntimeException(enterAccountRecordResultMsg);
		}
		superPushShareDaySettle.setEnterAccountStatus(EnterAccountStatus.ENTERACCOUNTED.toString());
		superPushShareDaySettle.setEnterAccountTime(new Date());
		
		superPushShareDaySettleService.update(superPushShareDaySettle);
		
		msg.put("status", true);
		msg.put("msg", "入账成功");
		log.info(msg.toString());
		return msg;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> mchProfitRecordAccount(SuperPushShareDaySettle superPushShareDaySettle) throws Exception, JsonMappingException, IOException {
		Map<String,Object> msg=new HashMap<>();
		BigDecimal shareTotalAmount = superPushShareDaySettle.getShareTotalAmount();
		if (shareTotalAmount.compareTo(BigDecimal.ZERO) == 0 && shareTotalAmount.compareTo(BigDecimal.ZERO) == 0) {
			msg.put("status", true);
			msg.put("msg", "记账金额都为0,不需要记账");
			return msg;
		}
		final String secret = accountApiHttpSecret;

		final long iat = System.currentTimeMillis() / 1000l; // issued at claim
		final long exp = iat + 180L; // expires claim. In this case the token
										// expires in 60 seconds
		final String jti = UUID.randomUUID().toString();
		final JWTSigner signer = new JWTSigner(secret);
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		claims.put("fromSystem", "accountWeb");
		claims.put("transDate", DateUtil.getDefaultFormatDate(superPushShareDaySettle.getCreateTime()));
		claims.put("fromSerialNo", superPushShareDaySettle.getId().toString());
		claims.put("amount", shareTotalAmount.toString());
		claims.put("transTypeCode", "000034");
		claims.put("merchantNo", superPushShareDaySettle.getShareNo());
		claims.put("exp", exp);
		claims.put("iat", iat);
		claims.put("jti", jti);

		final String token = signer.sign(claims);
		String url = accountApiHttpUrl + "/superPushController/mchProfitRecordAccount.do";
		log.info("url:"+url);
		String response = HttpConnectUtil.postHttp(url, "token", token);
		log.info("入账返回结果：" + response);

		if (response == null || "".equals(response)) {
			msg.put("status", false);
			msg.put("msg", "入账返回为空");
			return msg;
		} else {
			ObjectMapper om = new ObjectMapper();
			Map<String, Object> resp = om.readValue(response, Map.class);
			if ((boolean) resp.get("status") == false) {
				
				msg.put("status", false);
				msg.put("msg", "入账失败:" + resp.get("msg").toString());
				return msg;
			}
		}
		msg.put("status", true);
		msg.put("msg", "入账成功");
		return msg;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> agentProfitRecordAccount(SuperPushShareDaySettle superPushShareDaySettle) throws Exception, JsonMappingException, IOException {
		Map<String,Object> msg=new HashMap<>();
		BigDecimal shareTotalAmount = superPushShareDaySettle.getShareTotalAmount();
		if (shareTotalAmount.compareTo(BigDecimal.ZERO) == 0 && shareTotalAmount.compareTo(BigDecimal.ZERO) == 0) {
			msg.put("status", true);
			msg.put("msg", "记账金额都为0,不需要记账");
			return msg;
		}
		final String secret = accountApiHttpSecret;

		final long iat = System.currentTimeMillis() / 1000l; // issued at claim
		final long exp = iat + 180L; // expires claim. In this case the token
										// expires in 60 seconds
		final String jti = UUID.randomUUID().toString();
		final JWTSigner signer = new JWTSigner(secret);
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		claims.put("fromSystem", "accountWeb");
		claims.put("transDate", DateUtil.getDefaultFormatDate(superPushShareDaySettle.getCreateTime()));
		claims.put("fromSerialNo", superPushShareDaySettle.getId().toString());
		claims.put("amount", shareTotalAmount.toString());
		claims.put("transTypeCode", "000040");
		claims.put("agentNo", superPushShareDaySettle.getShareNo());
		claims.put("exp", exp);
		claims.put("iat", iat);
		claims.put("jti", jti);

		final String token = signer.sign(claims);
		String url = accountApiHttpUrl + "/superPushController/agentProfitRecordAccount.do";
		log.info("url:"+url);
		String response = HttpConnectUtil.postHttp(url, "token", token);
		
		log.info("入账返回结果：" + response);

		if (response == null || "".equals(response)) {
			msg.put("status", false);
			msg.put("msg", "入账返回为空");
			return msg;
		} else {
			ObjectMapper om = new ObjectMapper();
			Map<String, Object> resp = om.readValue(response, Map.class);
			if ((boolean) resp.get("status") == false) {
				
				msg.put("status", false);
				msg.put("msg", "入账失败:" + resp.get("msg").toString());
				return msg;
			}
		}
		msg.put("status", true);
		msg.put("msg", "入账成功");
		return msg;
	}
	

	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> chongZheng(AgentShareDaySettle agentShareDaySettle) throws Exception {
		Map<String,Object> msg=new HashMap<>();
        final String secret = accountApiHttpSecret;

        final long iat = System.currentTimeMillis() / 1000l; // issued at claim 
        final long exp = iat + 60L; // expires claim. In this case the token expires in 60 seconds
        final String jti = UUID.randomUUID().toString();
        final JWTSigner signer = new JWTSigner(secret);
        final HashMap<String, Object> claims = new HashMap<String, Object>();
//        aa
    	claims.put("fromSystem", "accountWeb");
    	claims.put("transDate", DateUtil.getDefaultFormatDate(agentShareDaySettle.getTransDate()));
    	claims.put("fromSerialNo", agentShareDaySettle.getId().toString());
    	claims.put("oldTransTypeCode", "000031");
        claims.put("transTypeCode", "000098");
        claims.put("reverseFlag", ReverseFlag.REVERSE.toString());
        
        claims.put("exp", exp);
        claims.put("iat", iat);
        claims.put("jti", jti);

        final String token = signer.sign(claims);
        String url = accountApiHttpUrl + "/recordAccountController/chongZheng.do";
		log.info("chongZheng url：" + url);
		String response = HttpConnectUtil.postHttp(url, "token", token);
		log.info("chongZheng返回结果：" + response);
		ObjectMapper om = new ObjectMapper();
		Map<String, Object> resp = om.readValue(response, Map.class);
		if (response == null || "".equals(response)) {
			String errorMsg = "冲正 返回为空";
			msg.put("status", false);
			msg.put("msg", errorMsg);
			return msg;
		} else {
			if ((boolean) resp.get("status") == false) {
				String errMsg = "";
				if (resp.get("msg") == null || resp.get("msg") == "") {
					errMsg += "冲正message:返回为空";
				} else {
					errMsg = "冲正记账失败:" + resp.get("msg").toString();
					errMsg = errMsg.length() > 240 ? errMsg.substring(0, 240) : errMsg;
				}
				msg.put("status", false);
				msg.put("msg", errMsg);
				return msg;
			} else {
				msg.put("status", true);
				msg.put("msg", "冲正成功");
				return msg;
			}
		}
	}
	

}
