package cn.eeepay.framework.service.peragent.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
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

import cn.eeepay.framework.dao.peragent.PaShareDetailMapper;
import cn.eeepay.framework.enums.EnterAccountStatus;
import cn.eeepay.framework.model.bill.SysDict;
import cn.eeepay.framework.model.peragent.PaShareDetail;
import cn.eeepay.framework.service.bill.SysDictService;
import cn.eeepay.framework.service.peragent.PaShareDetailAccountService;
import cn.eeepay.framework.util.DateUtil;
import cn.eeepay.framework.util.HttpConnectUtil;

@Service("paShareDetailAccountService")
@Transactional
public class PaShareDetailAccountServiceImpl implements PaShareDetailAccountService {

	private static final Logger log = LoggerFactory.getLogger(PaShareDetailAccountServiceImpl.class);

	@Resource
	private PaShareDetailMapper paShareDetailMapper;
	
	@Resource
	public SysDictService sysDictService;

	@Value("${accountApi.http.url}")
	private String accountApiHttpUrl;

	@Value("${accountApi.http.secret}")
	private String accountApiHttpSecret;

	@Override
	public Map<String, Object> singleEnterAccount(PaShareDetail paShareDetailQuery, String userName) throws JsonParseException, JsonMappingException, IOException {
		Map<String, Object> msg = new HashMap<>();
		if (paShareDetailQuery != null) {
			msg = this.singleEnterAccountForApi(paShareDetailQuery);
			if ((boolean) msg.get("status")) {
				paShareDetailQuery.setAccStatus(EnterAccountStatus.ENTERACCOUNTED.toString());
				paShareDetailQuery.setAccMessage((String) msg.get("msg"));
				paShareDetailQuery.setAccTime(new Date());
				paShareDetailQuery.setAccOperator(userName);
				
				//入账金额不为零切入账成功，调推送信息接口
				profitPush(paShareDetailQuery);
				
			} else {
				paShareDetailQuery.setAccMessage((String) msg.get("msg"));
				paShareDetailQuery.setAccTime(new Date());
				paShareDetailQuery.setAccOperator(userName);
			}
			paShareDetailMapper.updatePaShareDetailById(paShareDetailQuery);
		}
		return msg;

	}
	
	@Override
	public Map<String, Object> bacthAccount(String accountMonth, String username) {
		List<PaShareDetail> list = new ArrayList<>();
		Map<String, Object>  result = new HashMap<>();
		list = paShareDetailMapper.findBacthAccount(accountMonth);
		if(list == null || list.size() == 0 ){
			result.put("status", false);
			result.put("msg", "没有要入账的数据！");
		}else{
			result = this.bacthEnterAccount(list,username);
		}
		return result;
	}

	private Map<String, Object> bacthEnterAccount(List<PaShareDetail> list, String username) {
		Map<String,Object> msg = new HashMap<>();
		for (PaShareDetail paShareDetail : list) {
			try {
				Map<String, Object>  result = singleEnterAccountForApi(paShareDetail);
				Boolean resultStatus =  (Boolean) result.get("status");
				String resultMsg =  (String) result.get("msg");
				if (resultStatus) {
					paShareDetail.setAccStatus(EnterAccountStatus.ENTERACCOUNTED.toString());
					paShareDetail.setAccMessage(resultMsg);
					paShareDetail.setAccTime(new Date());
					paShareDetail.setAccOperator(username);
					paShareDetailMapper.updatePaShareDetailById(paShareDetail);
					msg.put("status", resultStatus);
					msg.put("msg", resultMsg);
					log.info(msg.toString());
					
					//入账金额不为零切入账成功，调推送信息接口
					profitPush(paShareDetail);
					
				}else{
					paShareDetail.setAccMessage(resultMsg);
					paShareDetail.setAccTime(new Date());
					paShareDetail.setAccOperator(username);
					paShareDetailMapper.updatePaShareDetailById(paShareDetail);
					msg.put("status", resultStatus);
					msg.put("msg", resultMsg);
					log.info(msg.toString());
				}
			} catch (Exception e) {
				log.info("异常:"+e);
				String s = String.format("账号%s入账异常:"+ e.getMessage(), paShareDetail.getAccUserAgent());
				log.info("批量入账返回信息" + s);
				paShareDetail.setAccMessage(s);
				paShareDetail.setAccTime(new Date());
				paShareDetail.setAccOperator(username);
				paShareDetailMapper.updatePaShareDetailById(paShareDetail);
			}
		}
		return msg;
	}

	
	  private void profitPush(PaShareDetail paShareDetailQuery)
	  {
	    if ((paShareDetailQuery.getShareAmount() != null) && (paShareDetailQuery.getShareAmount().compareTo(BigDecimal.ZERO) != 0)) {
	      Map<String, String> msg = new HashMap<String, String>();
	      msg.put("userCode", paShareDetailQuery.getUserCode());
	      msg.put("typeCode", "profit_" + paShareDetailQuery.getShareType());
	      msg.put("extMsg", paShareDetailQuery.getShareAmount().toString());
	      SysDict acqOrg = null;
	      try {
	        acqOrg = sysDictService.findSysDictByKeyName("peragent_profit_push", "peragent_profit_push");
	        String url = acqOrg.getSysValue();
	        log.info("推送接口url:" + url + "  推送接口准备参数:" + msg.toString());
	        String response = HttpConnectUtil.postHttp(url, msg);
	        log.info("推送接口返回参数:" + response);
	      } catch (Exception e) {
	        e.printStackTrace();
	        log.error("超级盟主分润入账推送报错：" + e.getMessage());
	      }
	    }
	  }
	
	private Map<String, Object> singleEnterAccountForApi(PaShareDetail paShareDetailQuery)
			throws JsonParseException, JsonMappingException, IOException {
		Map<String, Object> msg = new HashMap<>();
		BigDecimal shareTotalAmount = paShareDetailQuery.getShareAmount();
		if (shareTotalAmount.compareTo(BigDecimal.ZERO) == 0) {
			msg.put("status", true);
			msg.put("msg", "记账金额都为0,不需要记账");
			return msg;
		}
		final String secret = accountApiHttpSecret;
		final long iat = System.currentTimeMillis() / 1000l;
		final long exp = iat + 180L;
		final String jti = UUID.randomUUID().toString();
		final JWTSigner signer = new JWTSigner(secret);
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		claims.put("fromSystem", "accountWeb");
		claims.put("transDate", DateUtil.getDefaultFormatDate(new Date()));
		claims.put("amount", shareTotalAmount.toString());
		claims.put("fromSerialNo", paShareDetailQuery.getId().toString());
		claims.put("agentNo", paShareDetailQuery.getAccUserAgent());
		claims.put("transOrderNo",  paShareDetailQuery.getId().toString());
		
//		000091  3  管理津贴(月结)
//		000092  4  成长津贴(月结)
//		000103  5  王者奖金(月结)
//		000093  6  荣耀奖金(月结)
		if ("3".equals(paShareDetailQuery.getShareType())) {
			claims.put("transTypeCode", "000091");
		} else if ("4".equals(paShareDetailQuery.getShareType())) {
			claims.put("transTypeCode", "000092");
		} else if ("5".equals(paShareDetailQuery.getShareType())) {
			claims.put("transTypeCode", "000103");
		} else if ("6".equals(paShareDetailQuery.getShareType())) {
			claims.put("transTypeCode", "000093");
		} else {
			msg.put("status", false);
			msg.put("msg", "入账失败，没有这种分润类别");
			return msg;
		}
		claims.put("exp", exp);
		claims.put("iat", iat);
		claims.put("jti", jti);
		final String token = signer.sign(claims);
		String url = accountApiHttpUrl + "/peragentController/peragentAccount.do";
		log.info("超级盟主分润入账请求url：" + url);
		log.info("超级盟主分润入账请求参数：" + claims.toString());
		String response = HttpConnectUtil.postHttp(url, "token", token);
		log.info("超级盟主分润入账返回结果：" + response);
		if (response == null || "".equals(response)) {
			msg.put("status", false);
			msg.put("msg", "超级盟主分润入账返回为空");
			return msg;
		} else {
			ObjectMapper om = new ObjectMapper();
			@SuppressWarnings("unchecked")
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



}
