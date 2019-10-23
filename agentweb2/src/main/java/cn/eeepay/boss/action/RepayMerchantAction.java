package cn.eeepay.boss.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.eeepay.framework.model.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.service.RepayMerchantService;
import cn.eeepay.framework.util.ALiYunOssUtil;
import cn.eeepay.framework.util.ClientInterface;
import cn.eeepay.framework.util.Constants;

/**
 * 超级还款用户管理
 * @author mays
 * @date 2017年10月31日
 */
@Controller
@RequestMapping(value = "/repayMerchant")
public class RepayMerchantAction {

	private Logger log = LoggerFactory.getLogger(RepayMerchantAction.class);

	@Resource
	private RepayMerchantService repayMerchantService;
	@Resource
	private AgentInfoService agentInfoService;

	/**
	 * 用户查询
	 * @author mays
	 * @date 2017年10月31日
	 */
	@RequestMapping(value = "/selectRepayMerchantByParam")
	@ResponseBody
	public Map<String, Object> selectRepayMerchantByParam(@RequestBody RepayMerchantInfo info,
			@RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "10") int pageSize) {
		Map<String, Object> msg = new HashMap<>();
		try {
			Page<RepayMerchantInfo> page = new Page<>(pageNo, pageSize);
			AgentInfo loginAgent = agentInfoService.selectByPrincipal();
			if (loginAgent == null){
				msg.put("status", false);
				msg.put("msg", "无权访问");
				return msg;
			}
			if (StringUtils.isBlank(info.getAgentNode())) {
				info.setAgentNode(loginAgent.getAgentNode());
			}else if(!info.getAgentNode().startsWith(loginAgent.getAgentNode())){
				msg.put("status", false);
				msg.put("msg", "无权访问");
				return msg;
			}

			System.out.println(info.getAgentNode()+"----------------------");
			repayMerchantService.selectRepayMerchantByParam(page, info);
			  if (loginAgent.getAgentLevel() == 1){
	                if(page.getResult() != null && !page.getResult().isEmpty()){
	                    for (RepayMerchantInfo temp : page.getResult()){
	                        temp.setUserName(StringUtils.trimToEmpty(temp.getUserName()).replaceAll("^(.).*?$", "$1**"));
	                    }
	                }
	            }else{
	            	 if(page.getResult() != null && !page.getResult().isEmpty()){
		                    for (RepayMerchantInfo temp : page.getResult()){
		                        temp.setUserName(StringUtils.trimToEmpty(temp.getUserName()).replaceAll("^(.).*?$", "$1**"));
		                        temp.setMobileNo(StringUtils.trimToEmpty(temp.getMobileNo()).replaceAll("^(.{3}).*?(.{4})$", "$1****$2"));
		                    }
		                }
	            }
			
			msg.put("status", true);
			msg.put("page", page);
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "查询失败");
			log.error("信用卡还款用户查询失败", e);
		}
		return msg;
	}

	/**
	 * 用户开户
	 * @author mays
	 * @date 2017年10月31日
	 */
	/*@RequestMapping(value = "/batchOpenAccount")
	@ResponseBody
	@SystemLog(description = "信用卡用户开户", operCode = "repayMerchant.batchOpenAccount")
	public Map<String, Object> batchOpenAccount(@RequestBody List<String> merchantNos) {
		Map<String, Object> msg = new HashMap<>();
		if (merchantNos == null || merchantNos.size() < 1) {
			msg.put("status", false);
			msg.put("msg", "参数非法");
			return msg;
		}
		int successNum = 0;
		int failNum = 0;
		for (String merchantNo : merchantNos) {
			try {
				String returnMsg = ClientInterface.creditMerchantOpenAccount(merchantNo);
				Map<String, Object> result = JSON.parseObject(returnMsg);
				if (result != null && (Boolean)result.get("status")) {
					if (1 == repayMerchantService.updateRepayMerchantAccountStatus(merchantNo)) {
						successNum++;
					} else {
						failNum++;
					}
				} else {
					failNum++;
				}
			} catch (Exception e) {
				log.error("信用卡还款用户开户失败", e);
				failNum++;
			}
		}
		msg.put("status", true);
		msg.put("msg", "批量开户,成功条数：" + successNum + "，失败条数：" + failNum);
		return msg;
	}
*/
	/**
	 * 用户详情查询
	 * @author mays
	 * @date 2017年10月31日
	 */
	@RequestMapping(value = "/queryMerchantDetailByMerchantNo")
	@ResponseBody
	public Map<String, Object> queryMerchantDetailByMerchantNo(String merchantNo) {
		Map<String, Object> msg = new HashMap<>();
		AgentInfo loginAgent = agentInfoService.selectByPrincipal();
		try {
			if (merchantNo == null || "".equals(merchantNo)) {
				msg.put("status", false);
				msg.put("msg", "商户号为空");
				return msg;
			}
			//获取用户基本资料
			RepayMerchantInfo info = repayMerchantService.queryRepayMerchantByMerchantNo(merchantNo);
			if(info!=null){
				if(info.getMerAccount().equals("1")){
					info.setMerAccount("是");
				}else{
					info.setMerAccount("否");
				}
			}
			//为了方便页面展示图片,把图片信息封装到一个集合
			ArrayList<Object> imageList = processImage(info);
			//获取用户绑定的贷记卡和借记卡
			List<YfbCardManage> debitCardList = new ArrayList<>();
			List<YfbCardManage> creditCardList = new ArrayList<>();
			queryCardInfo(info, debitCardList, creditCardList);
			if(info!=null){
			if (loginAgent.getAgentLevel() == 1){
				info.setUserName(StringUtils.trimToEmpty(info.getUserName()).replaceAll("^(.).*?$", "$1**"));
				info.setIdCardNo(StringUtils.trimToEmpty(info.getIdCardNo()).replaceAll("^(.{2}).*?(.{2})$", "$1****$2"));
            }else{
            	info.setUserName(StringUtils.trimToEmpty(info.getUserName()).replaceAll("^(.).*?$", "$1**"));
            	info.setMobileNo(StringUtils.trimToEmpty(info.getMobileNo()).replaceAll("^(.{3}).*?(.{4})$", "$1****$2"));
            	info.setIdCardNo(StringUtils.trimToEmpty(info.getIdCardNo()).replaceAll("^(.{2}).*?(.{2})$", "$1****$2"));
            }
			}
			msg.put("status", true);
			msg.put("info", info);	//基本资料
			msg.put("imageList", imageList);	//基本资料的图片
			msg.put("debitCardList", debitCardList);	//借记卡
			msg.put("creditCardList", creditCardList);	//贷记卡
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "查询失败");
			log.error("用户详情查询失败", e);
		}
		return msg;
	}
	
	/**
	 * 查询借记卡和贷记卡信息并分到两个集合里
	 * @param info
	 * @param debitCardList 借记卡
	 * @param creditCardList 贷记卡
	 */
	private void queryCardInfo(RepayMerchantInfo info, List<YfbCardManage> debitCardList,
			List<YfbCardManage> creditCardList) {
		AgentInfo loginAgent = agentInfoService.selectByPrincipal();
		if (StringUtils.isNotBlank(info.getIdCardNo())) {
			List<YfbCardManage> cardList = repayMerchantService.queryCardByIdCardNo(info.getIdCardNo());
			String[] num = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二",
					"十三", "十四", "十五", "十六", "十七", "十八", "十九", "二十"};	//超过二十算我输
			int debitNum = 0;
			int creaditNum = 0;
			for (YfbCardManage card : cardList) {
				//获取照片url
				if (StringUtils.isNotBlank(card.getYhkzmUrl())) {
					Date expiresDate = new Date(Calendar.getInstance().getTime().getTime() * 3600 * 1000);
					String newUrl = ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_ATTCH_TUCKET, card.getYhkzmUrl(), expiresDate);
					card.setYhkzmUrl(newUrl);
				}
				//分类,借记卡DEBIT/贷记卡CREDIT
				if (StringUtils.isNotBlank(card.getCardType())) {
					if ("DEBIT".equals(card.getCardType())) {
						//判断是否为结算卡
						if (StringUtils.isNotBlank(info.getCardNo())) {
							card.setIsSettleCard(info.getCardNo().equals(card.getCardNo()) ? "是" : "否");
						} else {
							card.setIsSettleCard("否");
						}
						if (loginAgent.getAgentLevel() == 1){
							card.setAccountNo(StringUtils.trimToEmpty(card.getAccountNo()).replaceAll("^(.{6}).*?(.{4})$", "$1****$2"));
						}else{
							card.setMobileNo(StringUtils.trimToEmpty(info.getMobileNo()).replaceAll("^(.{3}).*?(.{4})$", "$1****$2"));
							card.setAccountNo(StringUtils.trimToEmpty(card.getAccountNo()).replaceAll("^(.{6}).*?(.{4})$", "$1****$2"));
						}
						card.setTitle("卡片" + (debitNum > 20 ? "" : num[++debitNum]));
						debitCardList.add(card);
					} else if ("CREDIT".equals(card.getCardType())) {
						card.setTitle("卡片" + (creaditNum > 20 ? "" : num[++creaditNum]));
						if (loginAgent.getAgentLevel() == 1){
							card.setAccountNo(StringUtils.trimToEmpty(card.getAccountNo()).replaceAll("^(.{6}).*?(.{4})$", "$1****$2"));
						}else{
							card.setMobileNo(StringUtils.trimToEmpty(info.getMobileNo()).replaceAll("^(.{3}).*?(.{4})$", "$1****$2"));
							card.setAccountNo(StringUtils.trimToEmpty(card.getAccountNo()).replaceAll("^(.{6}).*?(.{4})$", "$1****$2"));
						}
						creditCardList.add(card);
					}
				}
			}
		}
	}
	
	/**
	 * 为了方便页面展示图片,把图片信息封装到一个集合
	 * @param info
	 * @return 图片信息的集合
	 */
	private ArrayList<Object> processImage(RepayMerchantInfo info) {
		ArrayList<Object> imageList = new ArrayList<>();
		if (StringUtils.isNotBlank(info.getSfzzmUrl())) {
			HashMap<String, Object> map = new HashMap<>();
			map.put("itemName", "身份证正面");
			map.put("url", info.getSfzzmUrl());
			imageList.add(map);
		}
		if (StringUtils.isNotBlank(info.getSfzfmUrl())) {
			HashMap<String, Object> map = new HashMap<>();
			map.put("itemName", "身份证反面");
			map.put("url", info.getSfzfmUrl());
			imageList.add(map);
		}
		if (StringUtils.isNotBlank(info.getScsfzUrl())) {
			HashMap<String, Object> map = new HashMap<>();
			map.put("itemName", "手持身份证");
			map.put("url", info.getScsfzUrl());
			imageList.add(map);
		}
		if (StringUtils.isNotBlank(info.getHeadimgurl())) {
			HashMap<String, Object> map = new HashMap<>();
			map.put("itemName", "微信头像");
			map.put("url", info.getHeadimgurl());
			imageList.add(map);
		}
		return imageList;
	}
	
	/**
	 * 用户金额信息查询
	 * @author mays
	 * @date 2017年10月31日
	 */
	@RequestMapping(value = "/queryAccountAmount")
	@ResponseBody
	public Map<String, Object> queryAccountAmount(String merchantNo) {
		Map<String, Object> msg = new HashMap<>();
		try {
			if (merchantNo == null || "".equals(merchantNo)) {
				msg.put("status", false);
				msg.put("msg", "商户号为空");
				return msg;
			}
			List<YfbBalance> accountInfo = repayMerchantService.queryBalanceByMerchantNo(merchantNo);
			msg.put("status", true);
			msg.put("accountInfo", accountInfo);
//			String returnMsg = ClientInterface.repayAccountAmountInfo(merchantNo);
//			Map<String, Object> result = JSON.parseObject(returnMsg);
//			if (result != null && "200".equals(result.get("status").toString())) {
//				List<AccountInfo> list = JSON.parseArray(result.get("data").toString(), AccountInfo.class);
//				// 那边写死返回两条，把失败的数据移除
//				for (int i = 0; i < list.size(); i++) {
//					if (!list.get(i).getStatus()) {
//						list.remove(i--);
//					}
//				}
//				msg.put("status", true);
//				msg.put("accountInfo", list);
//			} else {
//				msg.put("status", false);
//				msg.put("msg", "账户余额查询失败");
//			}
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "账户余额查询失败");
			log.error("用户详情账户余额查询失败", e);
		}
		return msg;
	}

	public static class AccountInfo {
		private String accountNo;
		private String avaliBalance;
		private String freezeAmount;
		private Boolean status;
		public Boolean getStatus() {
			return status;
		}
		public void setStatus(Boolean status) {
			this.status = status;
		}
		public String getAccountNo() {
			return accountNo;
		}
		public void setAccountNo(String accountNo) {
			this.accountNo = accountNo;
		}
		public String getAvaliBalance() {
			return avaliBalance;
		}
		public void setAvaliBalance(String avaliBalance) {
			this.avaliBalance = avaliBalance;
		}
		public String getFreezeAmount() {
			return freezeAmount;
		}
		public void setFreezeAmount(String freezeAmount) {
			this.freezeAmount = freezeAmount;
		}
	}

}
