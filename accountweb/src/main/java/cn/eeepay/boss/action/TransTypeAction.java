package cn.eeepay.boss.action;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;

import cn.eeepay.boss.annotation.Logs;
import cn.eeepay.framework.model.bill.RecordAccountRuleTransType;
import cn.eeepay.framework.service.bill.RecordAccountRuleTransTypeService;
import cn.eeepay.framework.util.StringUtil;

@Controller
@RequestMapping(value = "/transTypeAction")
public class TransTypeAction {

	@Resource
	public RecordAccountRuleTransTypeService recordAccountRuleTransTypeService;

	@Value("${accountApi.http.secret}")
	private String accountApiHttpSecret;

	private static final Logger log = LoggerFactory.getLogger(TransTypeAction.class);

	/**
	 * 根据代理商类型查看所有交易类型
	 * 
	 * @param token
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/findTransTypeListByTransGroup")
	@Logs(description = "根据代理商类型查看所有交易类型")
	@ResponseBody
	public Map<String, Object> findTransTypeListByTransGroup(@RequestParam String token) throws Exception {
		Map<String, Object> msg = new HashMap<>();
		msg.put("name", "根据代理商类型查看所有交易类型");
		final String secret = accountApiHttpSecret;
		String transGroup = null;
		boolean isReturn = false;
		try {
			final JWTVerifier verifier = new JWTVerifier(secret);
			final Map<String, Object> claims = verifier.verify(token);
			transGroup = (String) claims.get("transGroup");
//			log.info("findTransTypeListByTransGroup {}", claims.toString());
		} catch (JWTVerifyException | InvalidKeyException | NoSuchAlgorithmException | IllegalStateException
				| SignatureException | IOException e) {
			// Invalid Token
			log.debug("Invalid Token");
			msg.put("status", false);
			msg.put("msg", "Invalid Token");
			return msg;
		}
		if (StringUtils.isBlank(transGroup)) {
			isReturn = true;
			msg.put("status", false);
			msg.put("msg", "transGroup参数不能为空");
		}
		if (isReturn) {
			return msg;
		}
		try {
			List<Map<String, String>> returnDataList = new ArrayList<Map<String, String>>();
			List<RecordAccountRuleTransType> list = recordAccountRuleTransTypeService.findAllTransType();
			for (RecordAccountRuleTransType recordAccountRuleTransType : list) {
				String[] transGroupList = new String[] {};
				if (!StringUtil.isBlank(recordAccountRuleTransType.getTransGroup())) {
					if (recordAccountRuleTransType.getTransGroup().contains(",")) {
						transGroupList = recordAccountRuleTransType.getTransGroup().split(",");
					} else {
						transGroupList = new String[] { recordAccountRuleTransType.getTransGroup() };
					}
				}
				Map<String, String> returnMap = new HashMap<>();
				for (String transGroupStr : transGroupList) {
					if (!StringUtil.isBlank(transGroupStr)) {
						if (transGroupStr.trim().equalsIgnoreCase(transGroup.trim())) {
							if ("TRADE_GROUP_CJMZ".equals(transGroup.trim()) && "000031".equals(recordAccountRuleTransType.getTransTypeCode())) {
								returnMap.put("transTypeCode", recordAccountRuleTransType.getTransTypeCode());
								returnMap.put("transTypeName", "固定收益");
								returnDataList.add(returnMap);
							} else if("TRADE_GROUP_CJMZ".equals(transGroup.trim()) && "000035".equals(recordAccountRuleTransType.getTransTypeCode())) {
								returnMap.put("transTypeCode", recordAccountRuleTransType.getTransTypeCode());
								returnMap.put("transTypeName", "大盟主固定收益");
								returnDataList.add(returnMap);
							} else {
								returnMap.put("transTypeCode", recordAccountRuleTransType.getTransTypeCode());
								returnMap.put("transTypeName", recordAccountRuleTransType.getTransTypeName());
								returnDataList.add(returnMap);
							}

						}
					}
				}
			}
			String data = "";
			data = JSON.toJSONString(returnDataList);
			msg.put("data", data);
			msg.put("status", true);
			msg.put("msg", "查询成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg.put("status", false);
			msg.put("msg", e.getMessage());
			log.error("异常:" + e);
		}
//		log.info("findTransTypeListByTransGroup returnMsg: " + msg.toString());
		return msg;

	}
}
