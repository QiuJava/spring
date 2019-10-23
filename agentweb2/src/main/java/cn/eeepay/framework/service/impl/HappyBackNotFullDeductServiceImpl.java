package cn.eeepay.framework.service.impl;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWTSigner;

import cn.eeepay.framework.dao.AgentInfoDao;
import cn.eeepay.framework.model.AccountSysData;
import cn.eeepay.framework.model.AccountSysResult;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.HappyBackNotFullDeductDetail;
import cn.eeepay.framework.model.HappyBackNotFullDeductDetailList;
import cn.eeepay.framework.model.HappyBackNotFullDeductDetailQo;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.service.HappyBackNotFullDeductService;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.HttpUtils;

@Service
public class HappyBackNotFullDeductServiceImpl implements HappyBackNotFullDeductService {

	@Autowired
	private AgentInfoDao agentInfoDao;
	
	@Autowired
	private AgentInfoService agentInfoService;

	@Override
	public HappyBackNotFullDeductDetail queryHappyBackNotFullDeductDetail(HappyBackNotFullDeductDetailQo qo) {
		String secret = Constants.ACCOUNT_API_SECURITY;
		final long iat = System.currentTimeMillis() / 1000L; // issued at claim
		final long exp = iat + 60L; // expires claim. In this case the token expires in 60 seconds
		final String jti = UUID.randomUUID().toString();
		final JWTSigner signer = new JWTSigner(secret);
		final HashMap<String, Object> claims = new HashMap<String, Object>();

		claims.put("agentNo", agentInfoService.getCurAgentNo()); // 当前代理商编号
		claims.put("selectAgentNo", qo.getAgentNo()); // 下拉框选择的代理商编号
		claims.put("orderNo", qo.getOrderNo()); // 订单编号
		claims.put("debtTime1", qo.getsTime()); // 查询开始时间
		claims.put("debtTime2", qo.geteTime()); // 查询结束时间
		claims.put("page", qo.getPageNo());
		claims.put("pageSize", qo.getPageSize());
		claims.put("exp", exp);
		claims.put("iat", iat);
		claims.put("jti", jti);

		final String token = signer.sign(claims);
		String url = Constants.happyBackNotFullDeductUrl + "?page=" + qo.getPageNo() + "&pageSize=" + qo.getPageSize();
		String response = HttpUtils.sendPost(url, "token=" + token, "utf-8");
		
		JSONObject parseObject = JSON.parseObject(response);
		String data = parseObject.get("data").toString();

		AccountSysData accountSysData = JSON.parseObject(data, AccountSysData.class);
		
		HappyBackNotFullDeductDetail detail = new HappyBackNotFullDeductDetail();
		detail.setTotal(accountSysData.getTotal());
		detail.setList(this.getList(response));
		this.setHappyBackNotFullDeductDetail(detail, qo);
		return detail;
	}

	private void setHappyBackNotFullDeductDetail(HappyBackNotFullDeductDetail detail,
			HappyBackNotFullDeductDetailQo qo) {
		String secret = Constants.ACCOUNT_API_SECURITY;
		final long iat = System.currentTimeMillis() / 1000L; // issued at claim
		final long exp = iat + 60L; // expires claim. In this case the token expires in 60 seconds
		final String jti = UUID.randomUUID().toString();
		final JWTSigner signer = new JWTSigner(secret);
		final HashMap<String, Object> claims = new HashMap<String, Object>();

		claims.put("agentNo", agentInfoService.getCurAgentNo()); // 当前代理商编号
		claims.put("selectAgentNo", qo.getAgentNo()); // 下拉框选择的代理商编号
		claims.put("orderNo", qo.getOrderNo()); // 订单编号
		claims.put("debtTime1", qo.getsTime()); // 查询开始时间
		claims.put("debtTime2", qo.geteTime()); // 查询结束时间

		claims.put("exp", exp);
		claims.put("iat", iat);
		claims.put("jti", jti);

		final String token = signer.sign(claims);
		String url = Constants.happyBackNotFullDeductSumUrl;
		String response = HttpUtils.sendPost(url, "token=" + token, "utf-8");
		HappyBackNotFullDeductDetail detailNew = JSON.parseObject(response, HappyBackNotFullDeductDetail.class);
		detail.setDebtAmount(detailNew.getDebtAmount());
		detail.setShouldDebtAmount(detailNew.getShouldDebtAmount());
		detail.setTotalDebtAmount(detailNew.getTotalDebtAmount());
	}

	@Override
	public List<HappyBackNotFullDeductDetailList> exportHappyBackNotFullDeductDetailQuery(HappyBackNotFullDeductDetailQo qo) {
		String secret = Constants.ACCOUNT_API_SECURITY;
		final long iat = System.currentTimeMillis() / 1000L; // issued at claim
		final long exp = iat + 60L; // expires claim. In this case the token expires in 60 seconds
		final String jti = UUID.randomUUID().toString();
		final JWTSigner signer = new JWTSigner(secret);
		final HashMap<String, Object> claims = new HashMap<String, Object>();

		claims.put("agentNo", agentInfoService.getCurAgentNo()); // 当前代理商编号
		claims.put("selectAgentNo", qo.getAgentNo()); // 下拉框选择的代理商编号
		claims.put("orderNo", qo.getOrderNo()); // 订单编号
		claims.put("debtTime1", qo.getsTime()); // 查询开始时间
		claims.put("debtTime2", qo.geteTime()); // 查询结束时间
		claims.put("exp", exp);
		claims.put("iat", iat);
		claims.put("jti", jti);

		final String token = signer.sign(claims);
		String url = Constants.happyBackNotFullDeductExportUrl;
		String response = HttpUtils.sendPost(url, "token=" + token, "utf-8");
		return this.getList(response);
	}

	private List<HappyBackNotFullDeductDetailList> getList(String response){
		AccountSysResult result = JSON.parseObject(response, AccountSysResult.class);
		if (result.isStatus()) {
			JSONObject parseObject = JSON.parseObject(response);
			String data = parseObject.get("data").toString();

			AccountSysData accountSysData = JSON.parseObject(data, AccountSysData.class);

			List<HappyBackNotFullDeductDetailList> list = accountSysData.getList();
            if (list == null || list.size() == 0) {
                return null;
            }
			// 拿到所有代理商编号 和 金额
			Set<String> agentNoList = new HashSet<>();
			for (HappyBackNotFullDeductDetailList item : list) {
				agentNoList.add(item.getAgentNo());
			}
			List<AgentInfo> agentList = agentInfoDao.findAgentInfoListByAgentNoSet(agentNoList);
			if (agentList == null || agentList.size() == 0) {
				return list;
			}
			Map<String, String> agentNo2AgentName = new HashMap<>();
			for (AgentInfo agentInfo : agentList) {
				agentNo2AgentName.put(agentInfo.getAgentNo(), agentInfo.getAgentName());
			}
			for (HappyBackNotFullDeductDetailList item : list) {
				item.setAgentName(agentNo2AgentName.get(item.getAgentNo()));
			}
			return list;
		}
		return null;
	}
}
