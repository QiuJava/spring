package cn.eeepay.framework.service.bill.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auth0.jwt.JWTSigner;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.eeepay.framework.dao.bill.SuperPushShareDaySettleMapper;
import cn.eeepay.framework.enums.EnterAccountStatus;
import cn.eeepay.framework.model.bill.SuperPushShareDaySettle;
import cn.eeepay.framework.model.nposp.SuperPushShare;
import cn.eeepay.framework.service.bill.SuperPushEnterAccountService;
import cn.eeepay.framework.service.nposp.SuperPushShareService;
import cn.eeepay.framework.util.DateUtil;
import cn.eeepay.framework.util.HttpConnectUtil;

@Service("superPushEnterAccountService")
@Transactional
public class SuperPushEnterAccountServiceImpl implements SuperPushEnterAccountService {

	private static final Logger log = LoggerFactory.getLogger(SuperPushEnterAccountServiceImpl.class);
	
	@Resource
	public SuperPushShareDaySettleMapper superPushShareDaySettleMapper;
	
	@Resource
	public SuperPushShareService superPushShareService;
	
	@Value("${accountApi.http.url}")
	private String accountApiHttpUrl;

	@Value("${accountApi.http.secret}")
	private String accountApiHttpSecret;

	@Override
	public Map<String, Object> superPushEnterAccount(SuperPushShareDaySettle superPushShareDaySettle) throws JsonParseException, JsonMappingException, IOException {

		Map<String, Object> msg = new HashMap<>();
		// '0: 一级代理商分润, 1: 直属代理商分润, 2 上一级商户 3 上二级商户 4 上三级商户'
		if (superPushShareDaySettle != null) {
			String shareType = superPushShareDaySettle.getShareType();
			switch (shareType) {
			case "0":
				msg = this.superPushEnterAgentAccount(superPushShareDaySettle);
				break;
			case "1":
				msg = this.superPushEnterMchAccount(superPushShareDaySettle);
				break;
			default:
				break;
			}
			if((boolean)msg.get("status")){
				superPushShareDaySettle.setEnterAccountStatus(EnterAccountStatus.ENTERACCOUNTED.toString());
				superPushShareDaySettle.setEnterAccountTime(new Date());
				int retrunNum = superPushShareDaySettleMapper.updateSuperPushShareDaySettle(superPushShareDaySettle);
				if( retrunNum > 0 ){
					List<SuperPushShare> superPushShareList = superPushShareService.findSuperPushShareListEnterByModel(superPushShareDaySettle);
					for (SuperPushShare superPushShare : superPushShareList) {
						superPushShare.setShareStatus("1");
						superPushShare.setShareTime(superPushShareDaySettle.getEnterAccountTime());
					}
					superPushShareService.updateSuperPushShareSplitBatch(superPushShareList);
				}
				
			}
		}
		return msg;

	}

	private Map<String, Object> superPushEnterAgentAccount(SuperPushShareDaySettle superPushShareDaySettle) throws JsonParseException, JsonMappingException, IOException {
		//超级推商户入账
		Map<String, Object> msg = new HashMap<>();
		BigDecimal shareTotalAmount = superPushShareDaySettle.getShareTotalAmount();
		if (shareTotalAmount.compareTo(BigDecimal.ZERO) == 0) {
			msg.put("status", true);
			msg.put("msg", "超级推代理商记账金额都为0,不需要记账");
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
		claims.put("transDate", DateUtil.getDefaultFormatDate(new Date()));
		claims.put("amount", shareTotalAmount.toString());
		claims.put("fromSerialNo", superPushShareDaySettle.getId().toString());
		claims.put("agentNo", superPushShareDaySettle.getShareNo());
		claims.put("transTypeCode", "000040");
		claims.put("exp", exp);
		claims.put("iat", iat);
		claims.put("jti", jti);

		final String token = signer.sign(claims);
		String url = accountApiHttpUrl + "/superPushController/agentProfitRecordAccount.do";
		String response = HttpConnectUtil.postHttp(url, "token", token);
		log.info("超级推代理商分润入账返回结果：" + response);

		if (response == null || "".equals(response)) {
			msg.put("status", false);
			msg.put("msg", "超级推代理商分润入账返回为空");
			return msg;
		} else {
			ObjectMapper om = new ObjectMapper();
			Map<String, Object> resp = om.readValue(response, Map.class);
			if ((boolean) resp.get("status") == false) {

				msg.put("status", false);
				msg.put("msg", "超级推代理商分润入账失败:" + resp.get("msg").toString());
				return msg;
			}
		}
		msg.put("status", true);
		msg.put("msg", "超级推代理商分润入账成功");
		return msg;

	}

	private Map<String, Object> superPushEnterMchAccount(SuperPushShareDaySettle superPushShareDaySettle)
			throws JsonParseException, JsonMappingException, IOException {
		// 超级推代理商入账
		Map<String, Object> msg = new HashMap<>();
		BigDecimal shareTotalAmount = superPushShareDaySettle.getShareTotalAmount();
		if (shareTotalAmount.compareTo(BigDecimal.ZERO) == 0) {
			msg.put("status", true);
			msg.put("msg", "超级推商户记账金额都为0,不需要记账");
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
		claims.put("transDate", DateUtil.getDefaultFormatDate(new Date()));
		claims.put("amount", shareTotalAmount.toString());
		claims.put("fromSerialNo", superPushShareDaySettle.getId().toString());
		claims.put("merchantNo", superPushShareDaySettle.getShareNo());
		claims.put("transTypeCode", "000034");
		claims.put("exp", exp);
		claims.put("iat", iat);
		claims.put("jti", jti);

		final String token = signer.sign(claims);
		String url = accountApiHttpUrl + "/superPushController/mchProfitRecordAccount.do";
		String response = HttpConnectUtil.postHttp(url, "token", token);
		log.info("超级推代理商分润入账返回结果：" + response);

		if (response == null || "".equals(response)) {
			msg.put("status", false);
			msg.put("msg", "超级推代理商分润入账返回为空");
			return msg;
		} else {
			ObjectMapper om = new ObjectMapper();
			Map<String, Object> resp = om.readValue(response, Map.class);
			if ((boolean) resp.get("status") == false) {

				msg.put("status", false);
				msg.put("msg", "超级推代理商分润入账失败:" + resp.get("msg").toString());
				return msg;
			}
		}
		msg.put("status", true);
		msg.put("msg", "超级推代理商分润入账成功");
		return msg;

	}


}
