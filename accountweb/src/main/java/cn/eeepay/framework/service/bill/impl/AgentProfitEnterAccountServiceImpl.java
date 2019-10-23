package cn.eeepay.framework.service.bill.impl;

import cn.eeepay.framework.dao.bill.AgentShareDaySettleMapper;
import cn.eeepay.framework.enums.EnterAccountStatus;
import cn.eeepay.framework.enums.ReverseFlag;
import cn.eeepay.framework.model.bill.AgentPreRecordTotal;
import cn.eeepay.framework.model.bill.AgentShareDaySettle;
import cn.eeepay.framework.service.bill.*;
import cn.eeepay.framework.service.nposp.AgentInfoService;
import cn.eeepay.framework.util.DateUtil;
import cn.eeepay.framework.util.HttpConnectUtil;
import com.auth0.jwt.JWTSigner;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service("agentProfitEnterAccountService")
@Transactional
public class AgentProfitEnterAccountServiceImpl implements AgentProfitEnterAccountService{
	private static final Logger log = LoggerFactory.getLogger(AgentProfitEnterAccountServiceImpl.class);
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
	public AgentShareDaySettleService agentShareDaySettleService;
	
	@Value("${accountApi.http.url}")
	private String accountApiHttpUrl;

	@Value("${accountApi.http.secret}")
	private String accountApiHttpSecret;

	/**
	 * 执行入账记账操作
	 */
	@Override
	public Map<String, Object> agentProfitEnterAccount(AgentShareDaySettle agentShareDaySettle)
			throws JsonMappingException, IOException, Exception {
		Map<String,Object> msg=new HashMap<>();
		
		String parentAgentNo = agentShareDaySettle.getParentAgentNo();
		String agentLevel = agentShareDaySettle.getAgentLevel();
		String batchNo = agentShareDaySettle.getCollectionBatchNo();
		
		
		Map<String,Object> enterAccountRecordResult = new HashMap<>();
		if (agentLevel.equals("1")) {
			enterAccountRecordResult = agentProfitEnterAccountRecordWithLevel1(agentShareDaySettle);
		}
		else{
			enterAccountRecordResult = agentProfitEnterAccountRecordWithLevel2(agentShareDaySettle);
			
		}
		
		Boolean enterAccountRecordResultStatus = (Boolean) enterAccountRecordResult.get("status");
		String enterAccountRecordResultMsg = (String) enterAccountRecordResult.get("msg");
		
		if (enterAccountRecordResultStatus) {
			log.info("代理商编号{} 代理商名称 {} 代理商分润入账记账 成功",agentShareDaySettle.getAgentNo(),agentShareDaySettle.getAgentName());
			Map<String,Object> terminalFreezeRecordResult = agentProfitTerminalFreezeRecord(agentShareDaySettle);
			Boolean terminalFreezeRecordResultStatus = (Boolean) terminalFreezeRecordResult.get("status");
			String terminalFreezeRecordResultMsg = (String) terminalFreezeRecordResult.get("msg");
			
			if (terminalFreezeRecordResultStatus) {
				log.info("代理商编号{} 代理商名称 {} 机具款冻结记账 成功",agentShareDaySettle.getAgentNo(),agentShareDaySettle.getAgentName());
				Map<String,Object> otherFreezeRecordResult = agentProfitOtherFreezeRecord(agentShareDaySettle);
				Boolean otherFreezeRecordResultStatus = (Boolean) otherFreezeRecordResult.get("status");
				String otherFreezeRecordResultMsg = (String) otherFreezeRecordResult.get("msg");
				if (otherFreezeRecordResultStatus) {
					log.info("代理商编号{} 代理商名称 {} 其他冻结记账 成功",agentShareDaySettle.getAgentNo(),agentShareDaySettle.getAgentName());
				}
				else{
					log.info("代理商编号{} 代理商名称 {} 其他冻结记账 记账 {}", new Object[]{agentShareDaySettle.getAgentNo(),agentShareDaySettle.getAgentName(),otherFreezeRecordResultMsg});
					//1.调解冻
					Map<String,Object> otherUnFreezeRecordResult = agentProfitOtherUnFreezeRecord(agentShareDaySettle);
					Boolean otherUnFreezeRecordResultStatus = (Boolean) otherUnFreezeRecordResult.get("status");
					String otherUnFreezeRecordResultMsg = (String) otherUnFreezeRecordResult.get("msg");
					if (otherUnFreezeRecordResultStatus) {
						log.info("代理商编号{} 代理商名称 {} 其他再解冻记账 成功",agentShareDaySettle.getAgentNo(),agentShareDaySettle.getAgentName());
					}
					else{
						log.info("代理商编号{} 代理商名称 {} 其他再解冻记账 ",new Object[]{agentShareDaySettle.getAgentNo(),agentShareDaySettle.getAgentName(),otherUnFreezeRecordResultMsg});
						
					}
					//2.调冲正
					Map<String,Object> chongZhengResult = chongZheng(agentShareDaySettle);
					Boolean chongZhengResultStatus = (Boolean) chongZhengResult.get("status");
					String chongZhengResultMsg = (String) chongZhengResult.get("msg");
					if (chongZhengResultStatus) {
						log.info("代理商编号{} 代理商名称 {} 其他冲正记账 成功",agentShareDaySettle.getAgentNo(),agentShareDaySettle.getAgentName());
					}
					else{
						log.info("代理商编号{} 代理商名称 {} 其他冲正记账 ",new Object[]{agentShareDaySettle.getAgentNo(),agentShareDaySettle.getAgentName(),chongZhengResultMsg});
					}
					throw new RuntimeException(otherFreezeRecordResultMsg);
				}
			}
			else{
				log.info("代理商编号{} 代理商名称 {} 机具款冻结记账 记账 {}", new Object[]{agentShareDaySettle.getAgentNo(),agentShareDaySettle.getAgentName(),terminalFreezeRecordResultMsg});
				//调冲正
				Map<String,Object> chongZhengResult = chongZheng(agentShareDaySettle);
				Boolean chongZhengResultStatus = (Boolean) chongZhengResult.get("status");
				String chongZhengResultMsg = (String) chongZhengResult.get("msg");
				if (chongZhengResultStatus) {
					log.info("代理商编号{} 代理商名称 {} 其他冲正记账 成功",agentShareDaySettle.getAgentNo(),agentShareDaySettle.getAgentName());
				}
				else{
					log.info("代理商编号{} 代理商名称 {} 其他冲正记账 ",new Object[]{agentShareDaySettle.getAgentNo(),agentShareDaySettle.getAgentName(),chongZhengResultMsg});
				}
				throw new RuntimeException(terminalFreezeRecordResultMsg);
			}
		}
		else{
			log.info("代理商编号{} 代理商名称 {} 代理商分润入账 记账 {}", new Object[]{agentShareDaySettle.getAgentNo(),agentShareDaySettle.getAgentName(),enterAccountRecordResultMsg});
//			msg.put("status", false);
//			msg.put("msg", enterAccountRecordResultMsg);
//			return msg;
			throw new RuntimeException(enterAccountRecordResultMsg);
		}
		agentShareDaySettle.setEnterAccountStatus(EnterAccountStatus.ENTERACCOUNTED.toString());
		
		agentShareDaySettle.setRealEnterShareAmount(agentShareDaySettle.getAdjustTotalShareAmount());
		agentShareDaySettle.setEnterAccountTime(new Date());



		
		if (!agentLevel.equals("1")) {
			AgentShareDaySettle parentAsds =  agentShareDaySettleService.findEntityByBatchNoAndAgentNo(batchNo, parentAgentNo);
			if (parentAsds != null) {
				parentAsds.setRealEnterShareAmount(parentAsds.getRealEnterShareAmount().subtract(agentShareDaySettle.getRealEnterShareAmount()));
				agentShareDaySettleService.updateAgentShareDaySettle(parentAsds);
			}
		}
		
		AgentPreRecordTotal agentPreRecordTotal= agentPreRecordTotalService.findAgentPreRecordTotalByAgentNo(agentShareDaySettle.getAgentNo());
		if (agentPreRecordTotal != null) {
			BigDecimal tuiCostAmount = agentPreRecordTotal.getTuiCostAmount();
			if (tuiCostAmount == null) tuiCostAmount = BigDecimal.ZERO;
			
			BigDecimal openBackAmount = agentPreRecordTotal.getOpenBackAmount();
			if (openBackAmount == null) openBackAmount = BigDecimal.ZERO;
			
			BigDecimal rateDiffAmount = agentPreRecordTotal.getRateDiffAmount();
			if (rateDiffAmount == null) rateDiffAmount = BigDecimal.ZERO;
			
			BigDecimal riskSubAmount = agentPreRecordTotal.getRiskSubAmount();
			if (riskSubAmount == null) riskSubAmount = BigDecimal.ZERO;
			
			BigDecimal merMgAmount = agentPreRecordTotal.getMerMgAmount();
			if (merMgAmount == null) merMgAmount = BigDecimal.ZERO;
			
			BigDecimal bailSubAmount = agentPreRecordTotal.getBailSubAmount();
			if (bailSubAmount == null) bailSubAmount = BigDecimal.ZERO;
			
			BigDecimal otherAmount = agentPreRecordTotal.getOtherAmount();
			if (otherAmount == null) otherAmount = BigDecimal.ZERO;
			
			BigDecimal terminalFreezeAmount = agentPreRecordTotal.getTerminalFreezeAmount();
			if (terminalFreezeAmount == null) terminalFreezeAmount = BigDecimal.ZERO;
			
			BigDecimal otherFreezeAmount = agentPreRecordTotal.getOtherFreezeAmount();
			if (otherFreezeAmount == null) otherFreezeAmount = BigDecimal.ZERO;
			
			BigDecimal asdTuiCostAmount = agentShareDaySettle.getTuiCostAmount();
			if (asdTuiCostAmount == null) {
				asdTuiCostAmount = BigDecimal.ZERO;
			}
			BigDecimal asdOpenBackAmount  = agentShareDaySettle.getOpenBackAmount();
			if (asdOpenBackAmount == null) {
				asdOpenBackAmount = BigDecimal.ZERO;
			}		
			BigDecimal asdRateDiffAmount = agentShareDaySettle.getRateDiffAmount();
			if (asdRateDiffAmount ==null) {
				asdRateDiffAmount = BigDecimal.ZERO;
			}		
			BigDecimal asdRiskSubAmount = agentShareDaySettle.getRiskSubAmount();
			if (asdRiskSubAmount == null) {
				asdRiskSubAmount = BigDecimal.ZERO;
			}
			BigDecimal asdMerMgAmount = agentShareDaySettle.getMerMgAmount();
			if (asdMerMgAmount == null) {
				asdMerMgAmount = BigDecimal.ZERO;
			}
			BigDecimal asdBailSubAmount = agentShareDaySettle.getBailSubAmount();
			if (asdBailSubAmount == null) {
				asdBailSubAmount = BigDecimal.ZERO;
			}
			BigDecimal asdOtherAmount = agentShareDaySettle.getOtherAmount();
			if (asdOtherAmount == null) {
				asdOtherAmount = BigDecimal.ZERO;
			}
			BigDecimal asdTerminalFreezeAmount = agentShareDaySettle.getTerminalFreezeAmount();
			if (asdTerminalFreezeAmount == null) {
				asdTerminalFreezeAmount = BigDecimal.ZERO;
			}
			BigDecimal asdOtherFreezeAmount = agentShareDaySettle.getOtherFreezeAmount();
			if (asdOtherFreezeAmount == null) {
				asdOtherFreezeAmount = BigDecimal.ZERO;
			}
			
			agentPreRecordTotal.setTuiCostAmount(tuiCostAmount.subtract(asdTuiCostAmount));
			agentPreRecordTotal.setOpenBackAmount(openBackAmount.subtract(asdOpenBackAmount));
			agentPreRecordTotal.setRateDiffAmount(rateDiffAmount.subtract(asdRateDiffAmount));
			agentPreRecordTotal.setRiskSubAmount(riskSubAmount.subtract(asdRiskSubAmount));
			agentPreRecordTotal.setMerMgAmount(merMgAmount.subtract(asdMerMgAmount));
			agentPreRecordTotal.setBailSubAmount(bailSubAmount.subtract(asdBailSubAmount));
			agentPreRecordTotal.setOtherAmount(otherAmount.subtract(asdOtherAmount));
			agentPreRecordTotal.setTerminalFreezeAmount(terminalFreezeAmount.subtract(asdTerminalFreezeAmount));
			agentPreRecordTotal.setOtherFreezeAmount(otherFreezeAmount.subtract(asdOtherFreezeAmount));
			agentPreRecordTotalService.updateAgentPreRecordTotal(agentPreRecordTotal);
		}
		agentShareDaySettleService.updateAgentShareDaySettle(agentShareDaySettle);
		
		msg.put("status", true);
		msg.put("msg", "入账成功");
		log.info(msg.toString());
		return msg;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> agentProfitEnterAccountRecordWithLevel1(AgentShareDaySettle agentShareDaySettle) throws Exception, JsonMappingException, IOException {
		Map<String,Object> msg=new HashMap<>();
		BigDecimal agentShareAmount = agentShareDaySettle.getAdjustTransShareAmount();
		BigDecimal agentSettleShareAmount = agentShareDaySettle.getAdjustTransCashAmount();
		if (agentShareAmount.compareTo(BigDecimal.ZERO) == 0 && agentSettleShareAmount.compareTo(BigDecimal.ZERO) == 0) {
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
		claims.put("transDate", DateUtil.getDefaultFormatDate(agentShareDaySettle.getTransDate()));
		claims.put("fromSerialNo", agentShareDaySettle.getId().toString());
		claims.put("agentShareAmount", agentShareAmount.toString());
		claims.put("agentSettleShareAmount", agentSettleShareAmount.toString());
		claims.put("transTypeCode", "000031");
		claims.put("agentNo", agentShareDaySettle.getAgentNo());
		claims.put("exp", exp);
		claims.put("iat", iat);
		claims.put("jti", jti);

		final String token = signer.sign(claims);
		String url = accountApiHttpUrl + "/agentProfitController/agentProfitEnterAccount.do";
		String response = HttpConnectUtil.postHttp(url, "token", token);
		log.info("代理商分润入账返回结果：" + response);

		if (response == null || "".equals(response)) {
			msg.put("status", false);
			msg.put("msg", "代理商分润入账返回为空");
			return msg;
		} else {
			ObjectMapper om = new ObjectMapper();
			Map<String, Object> resp = om.readValue(response, Map.class);
			if ((boolean) resp.get("status") == false) {
				
				msg.put("status", false);
				msg.put("msg", "代理商分润入账失败:" + resp.get("msg").toString());
				return msg;
			}
		}
		msg.put("status", true);
		msg.put("msg", "代理商分润入账成功");
		return msg;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> agentProfitEnterAccountRecordWithLevel2(AgentShareDaySettle agentShareDaySettle) throws Exception, JsonMappingException, IOException {
		Map<String,Object> msg=new HashMap<>();
		BigDecimal agentShareAmount = agentShareDaySettle.getAdjustTransShareAmount();
		BigDecimal agentSettleShareAmount = agentShareDaySettle.getAdjustTransCashAmount();
		if (agentShareAmount.compareTo(BigDecimal.ZERO) == 0 && agentSettleShareAmount.compareTo(BigDecimal.ZERO) == 0) {
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
		claims.put("transDate", DateUtil.getDefaultFormatDate(agentShareDaySettle.getTransDate()));
		claims.put("fromSerialNo", agentShareDaySettle.getId().toString());
		claims.put("agentShareAmount", agentShareAmount.toString());
		claims.put("agentSettleShareAmount", agentSettleShareAmount.toString());
		claims.put("transTypeCode", "000035");
		claims.put("agentNo1", agentShareDaySettle.getParentAgentNo());
		claims.put("agentNo2", agentShareDaySettle.getAgentNo());
		claims.put("exp", exp);
		claims.put("iat", iat);
		claims.put("jti", jti);

		final String token = signer.sign(claims);
		String url = accountApiHttpUrl + "/agentProfitController/agentProfitEnterAccountWithLevel2.do";
		String response = HttpConnectUtil.postHttp(url, "token", token);
		log.info("代理商分润入账返回结果：" + response);

		if (response == null || "".equals(response)) {
			msg.put("status", false);
			msg.put("msg", "代理商分润入账返回为空");
			return msg;
		} else {
			ObjectMapper om = new ObjectMapper();
			Map<String, Object> resp = om.readValue(response, Map.class);
			if ((boolean) resp.get("status") == false) {
				
				msg.put("status", false);
				msg.put("msg", "代理商分润入账失败:" + resp.get("msg").toString());
				return msg;
			}
		}
		msg.put("status", true);
		msg.put("msg", "代理商分润入账成功");
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
	@Override
	public Map<String, Object> agentProfitTerminalFreezeRecord(AgentShareDaySettle agentShareDaySettle) throws Exception, JsonMappingException, IOException {
		Map<String,Object> msg=new HashMap<>();
		String agentNo = agentShareDaySettle.getAgentNo();
		BigDecimal amount = agentShareDaySettle.getTerminalFreezeAmount();
		String subjectNo = "224105";
		if (amount.compareTo(BigDecimal.ZERO) == 0) {
			msg.put("status", true);
			msg.put("msg", "冻结金额都为0,不需要冻结");
			log.info(msg.toString());
			return msg;
		}
		
		Map<String,Object> result = agentProfitFreezeRecord(agentNo,subjectNo,amount);
		return result;
	}
	
	@Override
	public Map<String, Object> agentProfitFreezeRecord(String agentNo,String subjectNo,BigDecimal amount) throws Exception, JsonMappingException, IOException {
		return extAccountService.agentFreezeAmount(agentNo,subjectNo, amount);
	}
	
	
	@Override
	public Map<String, Object> agentProfitUnFreezeRecord(String agentNo,String subjectNo,BigDecimal amount) throws Exception, JsonMappingException, IOException {
		return extAccountService.agentUnFreezeAmount(agentNo,subjectNo, amount);
	}
	@Override
	public Map<String, Object> agentProfitTerminalUnFreezeRecord(AgentShareDaySettle agentShareDaySettle) throws Exception, JsonMappingException, IOException {
		Map<String,Object> msg=new HashMap<>();
		String agentNo = agentShareDaySettle.getAgentNo();
		BigDecimal amount = agentShareDaySettle.getTerminalFreezeAmount();
		String subjectNo = "224105";
		if (amount.compareTo(BigDecimal.ZERO) == 0) {
			msg.put("status", true);
			msg.put("msg", "解冻金额都为0,不需要解冻");
			log.info(msg.toString());
			return msg;
		}
		Map<String,Object> result = agentProfitUnFreezeRecord(agentNo,subjectNo,amount);
		return result;
	}
	
	@Override
	public Map<String, Object> agentProfitOtherFreezeRecord(AgentShareDaySettle agentShareDaySettle) throws Exception, JsonMappingException, IOException {
		Map<String,Object> msg=new HashMap<>();
		String agentNo = agentShareDaySettle.getAgentNo();
		BigDecimal amount = agentShareDaySettle.getOtherFreezeAmount();
		String subjectNo = "224105";
		if (amount.compareTo(BigDecimal.ZERO) == 0) {
			msg.put("status", true);
			msg.put("msg", "冻结金额都为0,不需要冻结");
			log.info(msg.toString());
			return msg;
		}
		Map<String,Object> result = agentProfitFreezeRecord(agentNo,subjectNo,amount);
		return result;
	}
	@Override
	public Map<String, Object> agentProfitOtherUnFreezeRecord(AgentShareDaySettle agentShareDaySettle) throws Exception, JsonMappingException, IOException {
		Map<String,Object> msg=new HashMap<>();
		String agentNo = agentShareDaySettle.getAgentNo();
		BigDecimal amount = agentShareDaySettle.getOtherFreezeAmount();
		String subjectNo = "224105";
		if (amount.compareTo(BigDecimal.ZERO) == 0) {
			msg.put("status", true);
			msg.put("msg", "解冻金额都为0,不需要解冻");
			log.info(msg.toString());
			return msg;
		}
		Map<String,Object> result = agentProfitUnFreezeRecord(agentNo,subjectNo,amount);
		return result;
	}

}
