package cn.eeepay.framework.service.bill.impl;

import cn.eeepay.framework.dao.bill.AgentPreAdjustMapper;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.AgentPreAdjust;
import cn.eeepay.framework.model.bill.AgentPreFreeze;
import cn.eeepay.framework.model.bill.AgentPreRecordTotal;
import cn.eeepay.framework.model.nposp.AgentInfo;
import cn.eeepay.framework.service.bill.AgentPreAdjustService;
import cn.eeepay.framework.service.bill.AgentPreFreezeService;
import cn.eeepay.framework.service.bill.AgentPreRecordTotalService;
import cn.eeepay.framework.service.nposp.AgentInfoService;
import cn.eeepay.framework.util.HttpConnectUtil;
import cn.eeepay.framework.util.StringUtil;
import com.auth0.jwt.JWTSigner;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Transactional
@Service("agentPreAdjustService")
public class AgentPreAdjustServiceImpl  implements AgentPreAdjustService{

	private static final Logger log = LoggerFactory.getLogger(AgentPreAdjustServiceImpl.class);
	@Resource
	private AgentPreAdjustMapper agentPreAdjustMapper;
	@Resource
	private AgentPreRecordTotalService agentPreRecordTotalService;
	@Resource
	private AgentInfoService agentInfoService;
	@Resource
	private AgentPreFreezeService agentPreFreezeService;

	@Value("${accountApi.http.secret}")
	private String accountApiHttpSecret;
	@Value("${accountApi.http.url}")
	private String accountApiHttpUrl;

	@Override
	public List<AgentPreAdjust> findAgentsProfitPreAdjustList(AgentPreAdjust agentPreAdjust,Map<String, Object> param, Sort sort,
			Page<AgentPreAdjust> page) {
		
		if (StringUtils.isNotBlank(agentPreAdjust.getAdjustTime1())) {
			agentPreAdjust.setAdjustTime1(agentPreAdjust.getAdjustTime1() + " 00:00:00");
		}
		if (StringUtils.isNotBlank(agentPreAdjust.getAdjustTime2())) {
			agentPreAdjust.setAdjustTime2(agentPreAdjust.getAdjustTime2() + " 23:59:59");
		}
		return agentPreAdjustMapper.findAgentPreAdjustList(agentPreAdjust,sort,page);
	}
	@Override
	public int insertAgentPreAdjustAndUpdateAccount(AgentPreAdjust agentPreAdjust,AgentInfo agentInfo) throws Exception {

		if(agentPreAdjust.getRowflag()>1){
			throw new RuntimeException("一行数据不能导入多个金额");
		}
		if("open_return".equals(agentPreAdjust.getAdjustReason())){//开通返现
			if (agentPreAdjust.getAdjustAmount().compareTo(BigDecimal.ZERO) < 0) {
				throw new RuntimeException("开通返现不能为负值");
			}
			agentPreAdjust.setOpenBackAmount(agentPreAdjust.getAdjustAmount());
		}else if("tui_cost_deduction".equals(agentPreAdjust.getAdjustReason())){//超级推成本
			agentPreAdjust.setTuiCostAmount(agentPreAdjust.getAdjustAmount());
		}else if("rate_variance".equals(agentPreAdjust.getAdjustReason())){//费率差异
			if (agentPreAdjust.getAdjustAmount().compareTo(BigDecimal.ZERO) < 0) {
				throw new RuntimeException("费率差异不能为负值");
			}
			agentPreAdjust.setRateDiffAmount(agentPreAdjust.getAdjustAmount());
		}else if("merchant_management_fee".equals(agentPreAdjust.getAdjustReason())){//商户管理费
			agentPreAdjust.setMerMgAmount(agentPreAdjust.getAdjustAmount());
		}else if("risk_deduction".equals(agentPreAdjust.getAdjustReason())){//风控扣款
			agentPreAdjust.setRiskSubAmount(agentPreAdjust.getAdjustAmount());
		}else if("margin_deduction".equals(agentPreAdjust.getAdjustReason())){//保证金扣除
			agentPreAdjust.setBailSubAmount(agentPreAdjust.getAdjustAmount());
		}else if("other".equals(agentPreAdjust.getAdjustReason())){//其他
			agentPreAdjust.setOtherAmount(agentPreAdjust.getAdjustAmount());
		}
		
		if(agentPreAdjust.getOpenBackAmount() == null){
			agentPreAdjust.setOpenBackAmount(BigDecimal.ZERO);
		}
		if(agentPreAdjust.getRateDiffAmount() == null){
			agentPreAdjust.setRateDiffAmount(BigDecimal.ZERO);
		}
		if(agentPreAdjust.getTuiCostAmount() == null){
			agentPreAdjust.setTuiCostAmount(BigDecimal.ZERO);
		}
		if(agentPreAdjust.getRiskSubAmount() == null){
			agentPreAdjust.setRiskSubAmount(BigDecimal.ZERO);
		}
		if(agentPreAdjust.getBailSubAmount() == null){
			agentPreAdjust.setBailSubAmount(BigDecimal.ZERO);
		}
		if(agentPreAdjust.getMerMgAmount() == null){
			agentPreAdjust.setMerMgAmount(BigDecimal.ZERO);
		}
		if(agentPreAdjust.getOtherAmount() == null){
			agentPreAdjust.setOtherAmount(BigDecimal.ZERO);
		}
		if(agentPreAdjust.getAdjustAmount() == null){
			agentPreAdjust.setAdjustAmount(BigDecimal.ZERO);
		}
		if(agentPreAdjust.getTerminalFreezeAmount() == null){
			agentPreAdjust.setTerminalFreezeAmount(BigDecimal.ZERO);
		}
		if(agentPreAdjust.getOtherFreezeAmount() == null){
			agentPreAdjust.setOtherFreezeAmount(BigDecimal.ZERO);
		}
		if(agentPreAdjust.getActivityAvailableAmount() == null){
			agentPreAdjust.setActivityAvailableAmount(BigDecimal.ZERO);
		}
		if(agentPreAdjust.getActivityFreezeAmount() == null){
			agentPreAdjust.setActivityFreezeAmount(BigDecimal.ZERO);
		}
		if(agentPreAdjust.getGenerateAmount() == null){
			agentPreAdjust.setGenerateAmount(BigDecimal.ZERO);
		}

		String preFreezeMark = "";      //0 不需要新增预冻结记录  ，1 新增预冻结记录
		BigDecimal dongJieJiLuAmount =new BigDecimal(0);      //新增冻结记录的金额
		BigDecimal actDongJieAmount = new BigDecimal(0);    //账户冻结金额调账金额
		BigDecimal actYuAmount = new BigDecimal(0);  //账户预调账金额
		BigDecimal actkeYongAmount = new BigDecimal(0);  //账户可用余额调账金额

		try {


			if(agentPreAdjust.getAdjustAmount().compareTo(BigDecimal.ZERO) < 0){

				final String secret = accountApiHttpSecret;
				final JWTSigner signer = new JWTSigner(secret);
				final HashMap<String, Object> claims = new HashMap<String, Object>();
				claims.put("agentNo", agentPreAdjust.getAgentNo());
				claims.put("agentName", agentPreAdjust.getAgentName());
				claims.put("amount", agentPreAdjust.getAdjustAmount().toString());
				claims.put("agentLevel", String.valueOf(agentInfo.getAgentLevel()));
				claims.put("adjustReason",agentPreAdjust.getAdjustReason() );

				final String token = signer.sign(claims);
				String url = accountApiHttpUrl + "/agentAccountController/agentPreAdjustment.do";
				log.info("代理商分润预调账 url：" + url);
				String response = HttpConnectUtil.postHttp(url, "token", token);
				log.info("代理商分润预调账返回结果：" + response);


				ObjectMapper om = new ObjectMapper();
				if (response == null || "".equals(response)) {
					log.error("预调账请求账户API接口失败....");
					String errorMsg = "预调账请求接口失败";
					throw new RuntimeException("预调账出现异常！回滚！" +errorMsg);
				}

				Map<String, Object> resp = om.readValue(response, Map.class);
				if (!(Boolean) resp.get("status")) {
					log.error("预调账请求账户API接口返回处理失败....");
					String errorMsg = "预调账请求接口返回处理失败";
					throw new RuntimeException("预调账出现异常！回滚！" +errorMsg);
				}

				String resultDate = (String) resp.get("data");
				if (StringUtil.isBlank(resultDate)) {
					log.error("预调账请求账户API接口成功，金额数据为空，处理失败....");
					String errorMsg = "预调账请求账户API接口成功，金额数据为空，处理失败";
					throw new RuntimeException("预调账出现异常！回滚！" +errorMsg);
				}
				Map<String, Object> respDate = om.readValue(resultDate, Map.class);
				Boolean respStatus = (Boolean) respDate.get("status");
				if (!respStatus) {
					log.error("预调账请求账户API接口成功，但是status为失败，处理失败....");
					String errorMsg = "预调账请求账户API接口成功，但是status为失败，处理失败";
					throw new RuntimeException("预调账出现异常！回滚！" +errorMsg);
				}
				 preFreezeMark = (String) respDate.get("preFreezeMark");      //0 不需要新增预冻结记录  ，1 新增预冻结记录
				 dongJieJiLuAmount = new BigDecimal((String) respDate.get("dongJieJiLuAmount"));      //新增冻结记录的金额

				 actDongJieAmount = new BigDecimal((String) respDate.get("actDongJieAmount"));    //账户冻结金额调账金额
				 actYuAmount = new BigDecimal((String) respDate.get("actYuAmount"));  //账户预调账金额
				//BigDecimal avaliBalance = new BigDecimal((String) respDate.get("avaliBalance"));
				 actkeYongAmount = new BigDecimal((String) respDate.get("actkeYongAmount"));  //账户可用余额调账金额

				agentPreAdjust.setActivityAvailableAmount(actkeYongAmount);
				agentPreAdjust.setActivityFreezeAmount(actDongJieAmount);
				agentPreAdjust.setGenerateAmount(actYuAmount);

				if (preFreezeMark.equals("1") && dongJieJiLuAmount.compareTo(BigDecimal.ZERO)!=0) {
					log.info("发起账户预冻结，冻结金额："+dongJieJiLuAmount.toString()+"   代理商编号："+agentPreAdjust.getAgentNo());
					//申请预冻结
					AgentPreFreeze agentPreFreeze = new AgentPreFreeze();
					agentPreFreeze.setAgentNo(agentPreAdjust.getAgentNo());
					agentPreFreeze.setAgentName(agentPreAdjust.getAgentName());
					agentPreFreeze.setOperater(agentPreAdjust.getApplicant());
					agentPreFreeze.setRemark(agentPreAdjust.getRemark());
					agentPreFreeze.setFreezeReason("other");
					agentPreFreeze.setTerminalFreezeAmount(BigDecimal.ZERO);
					agentPreFreeze.setOtherFreezeAmount(dongJieJiLuAmount);
					agentPreFreeze.setFreezeTime(new Date());
					try {
						Map<String, Object> result = agentPreFreezeService.saveAgentsProfitPreFreeze(agentPreFreeze,agentPreFreeze.getFreezeReason());
						Boolean resultStatus = (Boolean) result.get("status");
						String resultMsg = (String) result.get("msg");
						if (!resultStatus) {
							log.error("发起预冻结失败，预冻金额："+dongJieJiLuAmount.toString()+" "+resultMsg);
						}
					} catch (Exception e) {
						log.error("分润账户发起预冻结失败", e.getMessage());
						log.error("分润账户发起预冻结失败，预冻金额："+dongJieJiLuAmount.toString());
						throw new RuntimeException("分润账户发起预冻结失败");
					}
				}

			}else {
				AgentPreRecordTotal  agentPreRecordTotal = agentPreRecordTotalService.findAgentPreRecordTotalByAgentNo(agentPreAdjust.getAgentNo());
				if(agentPreRecordTotal != null){
					BigDecimal openBackAmount = agentPreRecordTotal.getOpenBackAmount();
					if (openBackAmount == null) openBackAmount = BigDecimal.ZERO;
					agentPreRecordTotal.setOpenBackAmount(openBackAmount.add(agentPreAdjust.getOpenBackAmount()));

					BigDecimal rateDiffAmount = agentPreRecordTotal.getRateDiffAmount();
					if (rateDiffAmount == null) rateDiffAmount = BigDecimal.ZERO;
					agentPreRecordTotal.setRateDiffAmount(rateDiffAmount.add(agentPreAdjust.getRateDiffAmount()));

					BigDecimal tuiCostAmount = agentPreRecordTotal.getTuiCostAmount();
					if (tuiCostAmount == null) tuiCostAmount = BigDecimal.ZERO;
					agentPreRecordTotal.setTuiCostAmount(tuiCostAmount.add(agentPreAdjust.getTuiCostAmount()));

					BigDecimal riskSubAmount = agentPreRecordTotal.getRiskSubAmount();
					if (riskSubAmount == null) riskSubAmount = BigDecimal.ZERO;
					agentPreRecordTotal.setRiskSubAmount(riskSubAmount.add(agentPreAdjust.getRiskSubAmount()));

					BigDecimal merMgAmount = agentPreRecordTotal.getMerMgAmount();
					if (merMgAmount == null) merMgAmount = BigDecimal.ZERO;
					agentPreRecordTotal.setMerMgAmount(merMgAmount.add(agentPreAdjust.getMerMgAmount()));

					BigDecimal otherAmount = agentPreRecordTotal.getOtherAmount();
					if (otherAmount == null) otherAmount = BigDecimal.ZERO;
					agentPreRecordTotal.setOtherAmount(otherAmount.add(agentPreAdjust.getOtherAmount()));

					BigDecimal bailSubAmount = agentPreRecordTotal.getBailSubAmount();
					if (bailSubAmount == null) bailSubAmount = BigDecimal.ZERO;
					agentPreRecordTotal.setBailSubAmount(bailSubAmount.add(agentPreAdjust.getBailSubAmount()));

					agentPreRecordTotalService.updateAgentPreRecordTotal(agentPreRecordTotal);

//				.updateAgentPreRecordTotalByAgentPreAdjust(agentPreAdjust);
				}else{
					agentPreRecordTotal = new AgentPreRecordTotal();
					agentPreRecordTotal.setAgentNo(agentPreAdjust.getAgentNo());
					agentPreRecordTotal.setAgentName(agentPreAdjust.getAgentName());
					agentPreRecordTotal.setOpenBackAmount(agentPreAdjust.getOpenBackAmount());
					agentPreRecordTotal.setRateDiffAmount(agentPreAdjust.getRateDiffAmount());
					agentPreRecordTotal.setTuiCostAmount(agentPreAdjust.getTuiCostAmount());
					agentPreRecordTotal.setRiskSubAmount(agentPreAdjust.getRiskSubAmount());
					agentPreRecordTotal.setMerMgAmount(agentPreAdjust.getMerMgAmount());
					agentPreRecordTotal.setOtherAmount(agentPreAdjust.getOtherAmount());
					agentPreRecordTotal.setBailSubAmount(agentPreAdjust.getBailSubAmount());
					agentPreRecordTotalService.insertAgentPreRecordTotal(agentPreRecordTotal);
//				.insertAgentPreRecordTotalByAgentPreAdjust(agentPreAdjust);
				}
			}
			this.insertAgentPreAdjust(agentPreAdjust);

//			return 1;
		} catch (Exception e) {
			log.error("异常:",e);
			throw new RuntimeException("预调账出现异常！回滚！" +e.getMessage());
		}
//		if (preFreezeMark.equals("1")) {
//				log.info("发起账户预冻结，冻结金额："+dongJieJiLuAmount.toString()+"   代理商编号："+agentPreAdjust.getAgentNo());
//				//申请预冻结
//				AgentPreFreeze agentPreFreeze = new AgentPreFreeze();
//				agentPreFreeze.setAgentNo(agentPreAdjust.getAgentNo());
//				agentPreFreeze.setAgentName(agentPreAdjust.getAgentName());
//				agentPreFreeze.setOperater(agentPreAdjust.getApplicant());
//				agentPreFreeze.setRemark(agentPreAdjust.getRemark());
//				agentPreFreeze.setFreezeReason("other");
//				agentPreFreeze.setTerminalFreezeAmount(BigDecimal.ZERO);
//				agentPreFreeze.setOtherFreezeAmount(dongJieJiLuAmount);
//				agentPreFreeze.setFreezeTime(new Date());
//				try {
//					Map<String, Object> result = agentPreFreezeService.saveAgentsProfitPreFreeze(agentPreFreeze,agentPreFreeze.getFreezeReason());
//					Boolean resultStatus = (Boolean) result.get("status");
//					String resultMsg = (String) result.get("msg");
//					if (!resultStatus) {
//						log.error("发起预冻结失败，预冻金额："+dongJieJiLuAmount.toString()+" "+resultMsg);
//					}
//				} catch (Exception e) {
//					log.error("分润账户发起预冻结失败", e.getMessage());
//					log.error("分润账户发起预冻结失败，预冻金额："+dongJieJiLuAmount.toString());
//					throw new RuntimeException("分润账户发起预冻结失败");
//				}
//		}
		return 1;
	}
	@Override
	public List<AgentPreAdjust> exportAgentsProfitPreAdjustList(AgentPreAdjust agentPreAdjust) {
		if (StringUtils.isNotBlank(agentPreAdjust.getAdjustTime1())) {
			agentPreAdjust.setAdjustTime1(agentPreAdjust.getAdjustTime1() + " 00:00:00");
		}
		if (StringUtils.isNotBlank(agentPreAdjust.getAdjustTime2())) {
			agentPreAdjust.setAdjustTime2(agentPreAdjust.getAdjustTime2() + " 23:59:59");
		}
		return agentPreAdjustMapper.exportAgentsProfitPreAdjustList(agentPreAdjust);
	}
	@Override
	public int insertAgentPreAdjust(AgentPreAdjust agentPreAdjust) {
		if(agentPreAdjust.getOpenBackAmount() == null){
			agentPreAdjust.setOpenBackAmount(BigDecimal.ZERO);
		}
		if(agentPreAdjust.getRateDiffAmount() == null){
			agentPreAdjust.setRateDiffAmount(BigDecimal.ZERO);
		}
		if(agentPreAdjust.getTuiCostAmount() == null){
			agentPreAdjust.setTuiCostAmount(BigDecimal.ZERO);
		}
		if(agentPreAdjust.getRiskSubAmount() == null){
			agentPreAdjust.setRiskSubAmount(BigDecimal.ZERO);
		}
		if(agentPreAdjust.getBailSubAmount() == null){
			agentPreAdjust.setBailSubAmount(BigDecimal.ZERO);
		}
		if(agentPreAdjust.getMerMgAmount() == null){
			agentPreAdjust.setMerMgAmount(BigDecimal.ZERO);
		}
		if(agentPreAdjust.getOtherAmount() == null){
			agentPreAdjust.setOtherAmount(BigDecimal.ZERO);
		}
		return agentPreAdjustMapper.insertAgentPreAdjust(agentPreAdjust);
	}
	@Override
	public Map<String, Object> insertAgentPreAdjustAndUpdateAccountExcel(List<AgentPreAdjust> agentPreAdjustsExcel) throws Exception {
		Map<String,Object> resultMapData = new HashMap<String, Object>();
		for (AgentPreAdjust agentPreAdjust : agentPreAdjustsExcel) {
			//查询代理商编号是否存在
			String agentNo = agentPreAdjust.getAgentNo();
			AgentInfo agentInfo = agentInfoService.findEntityByAgentNo(agentNo);
			if(agentInfo == null ){
				//resultMapData.put( agentPreAdjust.getOneAgentNo(), "该代理商不存在！");
				throw new RuntimeException("代理商"+agentNo+"不存在,请检查后重新提交");
			}else{
				agentPreAdjust.setAgentName(agentInfo.getAgentName());
				try {
					this.insertAgentPreAdjustAndUpdateAccount(agentPreAdjust,agentInfo);
				} catch (Exception e) {
					throw new RuntimeException("代理商"+agentNo+"导入异常,"+e.getMessage());
				}
				resultMapData.put(agentPreAdjust.getAgentNo(), "导入成功！");
			}
		}
		return resultMapData;
	}

}
