package cn.eeepay.boss.action;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.eeepay.framework.util.*;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.CollectiveTransOrder;
import cn.eeepay.framework.model.MerchantRequireItem;
import cn.eeepay.framework.model.PageBean;
import cn.eeepay.framework.model.PosCardBin;
import cn.eeepay.framework.model.ResponseBean;
import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.service.AccessService;
import cn.eeepay.framework.service.AgentFunctionService;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.service.CollectiveTransOrderService;
import cn.eeepay.framework.service.MerchantInfoService;
import cn.eeepay.framework.service.MerchantRequireItemService;
import cn.eeepay.framework.service.PerAgentService;
import cn.eeepay.framework.service.PosCardBinService;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.service.TerminalInfoService;
import cn.eeepay.framework.service.TransInfoService;
import cn.eeepay.framework.util.ALiYunOssUtil;
import cn.eeepay.framework.util.BigExcel;
import cn.eeepay.framework.util.CommonUtil;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.ListDataExcelExport;
import cn.eeepay.framework.util.ToolUtils;

@Controller
@RequestMapping("transInfoAction")
public class TransInfoAction {
	private static final Logger log = LoggerFactory.getLogger(TransInfoAction.class);

	@Resource
	public TerminalInfoService terminalInfoService;
	@Resource
	private SysDictService sysDictService;
	@Resource
	private TransInfoService transInfoService;
	@Resource
	private AgentInfoService agentInfoService;
	@Resource
	private PosCardBinService posCardBinService;
	@Resource
	private MerchantRequireItemService merchantRequireItemService;
	@Resource
	public CollectiveTransOrderService collectiveTransOrderService;
	@Resource
	public AgentFunctionService agentFunctionService;
	@Resource
	private PerAgentService perAgentService;
	@Resource
	private AccessService accessService;

	@Autowired
	private MerchantInfoService merchantInfoService;

	/**
	 * 数据初始化和分页查询
	 *
	 * @param tis
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/getAllInfo")
	@ResponseBody
	public ResponseBean getAllInfo(@RequestBody CollectiveTransOrder tis, @RequestParam(defaultValue = "1") int pageNo,
			@RequestParam(defaultValue = "10") int pageSize) {
		try {
			ResponseBean responseBean = checkDate(tis);
			if (responseBean != null) {
				return responseBean;
			}
			AgentInfo loginAgent = agentInfoService.selectByPrincipal();
			String loginAgentNo = loginAgent.getAgentNo();
			// 判断当前登录用户是否有设置业务范围，防止非法操作
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			String accessTeamId = principal.getUserEntityInfo().getAccessTeamId();
			if (StringUtils.isNotBlank(accessTeamId)) {
				if(!"-1".equals(accessTeamId) && !accessTeamId.equals(tis.getMerTeamId())){
					log.info("没有查询权限");
					return new ResponseBean(null, 0);
				}
			}
			tis.setInitAgentNo(loginAgentNo);
			// 根据当前记录的代理商查询出节点
			AgentInfo aa1 = agentInfoService.selectByagentNo(loginAgentNo);
			AgentInfo aa2 = agentInfoService.selectByagentNo(tis.getAgentNo());
			tis.setDirectlyAgentName(aa2.getAgentName());
			int level = aa1.getAgentLevel();
			int level1 = level + 1;
			String numAndMoney = transInfoService.queryNumAndMoney(tis, loginAgentNo);
			Page<CollectiveTransOrder> page = new Page<>(pageNo, pageSize);
			List<CollectiveTransOrder> queryAllInfo = transInfoService.queryAllInfo(page, tis, level, level1);
			Map<String, AgentInfo> searchAgentChildren = ToolUtils.collection2Map(
					agentInfoService.selectSelfAndDirectChildren(tis.getAgentNo()),
					new ToolUtils.Transformer<AgentInfo>() {
						@Override
						public String transform(AgentInfo value) {
							return value.getAgentNode();
						}
					});
			AgentInfo searchAgent = agentInfoService.selectByagentNo(tis.getAgentNo());

			// =======20170112,tiangh=======
			Pattern p = Pattern.compile("\\d+(\\d{4})");
			for (CollectiveTransOrder collectiveTransOrder : queryAllInfo) {
				String orderNo = collectiveTransOrder.getOrderNo();
				if ("11".equals(loginAgent.getAgentType())) {
					// Map<String,Object> map =
					// perAgentService.selectFromPaTransInfo(orderNo);
					Map<String, Object> map = perAgentService
							.selectFromPaMerInfoByMerchantNo(collectiveTransOrder.getMerchantNo());
					if (map != null) {
						collectiveTransOrder
								.setBelongUserCode(map.get("user_code") == null ? "" : map.get("user_code").toString());
					}
					log.info("订单号 " + orderNo + "在pa_trans_info表查询到数据 " + map + "======");
				}
				String accountNo = collectiveTransOrder.getAccountNo();
				if (accountNo != null) {
					Matcher matcher = p.matcher(accountNo);
					collectiveTransOrder.setAccountNo(matcher.replaceAll("***************$1"));
				}
				if (!agentFunctionService.queryFunctionValue(loginAgent, "001")) {
					collectiveTransOrder.setMobilephone(
							CommonUtil.replaceMask(collectiveTransOrder.getMobilephone(), "^(.{4}).*?$", "$1*******"));
				}
				AgentInfo childrenAgent = searchAgentChildren
						.get(cutOutTopNAgentNode(collectiveTransOrder.getAgentNode(), searchAgent));
				if (childrenAgent != null) {
					collectiveTransOrder.setDirectlyAgentName(childrenAgent.getAgentName());
					collectiveTransOrder.setDirectlyAgentNo(childrenAgent.getAgentNo());
				}
			}

			Map<String, Object> resultMap = new HashMap<>();
			resultMap.put("numAndMoney", StringUtils.isBlank(numAndMoney) ? "0" : numAndMoney);
			resultMap.put("transData", page.getResult());
			return new ResponseBean(resultMap, page.getTotalCount());
		} catch (Exception e) {
			e.printStackTrace();
			log.error("查询报错", e);
			return new ResponseBean(e);
		}
	}

	private ResponseBean checkDate(CollectiveTransOrder tis) throws ParseException {
		if (StringUtils.isBlank(tis.getMerchantNo()) && (StringUtils.isBlank(tis.getMobilephone()))){
			if (StringUtils.isBlank(tis.getEdate()) || StringUtils.isBlank(tis.getSdate())) {
				return new ResponseBean("查询交易时间不能为空", false);
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date endDate = sdf.parse(tis.getEdate());
			Date startDate = sdf.parse(tis.getSdate());
			if (endDate.getTime() - startDate.getTime() > 31 * 24 * 60 * 60 * 1000L) {
				return new ResponseBean("查询范围不能超过一个月", false);
			}
		}
		return null;
	}

	/**
	 * 截取代理商节点的前level级的agentNode
	 * <ul>
	 * <li>agentNode = "0-1572-" searchAgent.agentLevel = 2 return "0-1572-"
	 * </li>
	 * <li>agentNode = "0-1572-1574-" searchAgent.agentLevel = 2 return
	 * "0-1572-1574-"</li>
	 * <li>agentNode = "0-1572-1574-1576-" searchAgent.agentLevel = 2 return
	 * "0-1572-1574-"</li>
	 * </ul>
	 *
	 * @param agentNode
	 * @param searchAgent
	 * @return
	 */
	private static String cutOutTopNAgentNode(String agentNode, AgentInfo searchAgent) {
		if (StringUtils.isBlank(agentNode) || searchAgent == null) {
			return "";
		}
		Pattern pattern = Pattern.compile("^(0-(?:(?:\\d+)-){1," + (searchAgent.getAgentLevel() + 1) + "}).*$");
		Matcher matcher = pattern.matcher(agentNode);
		if (matcher.matches()) {
			return matcher.group(1);
		}
		return "";
	}

	@RequestMapping("/getAllInfoByMerchant")
	@ResponseBody
	public ResponseBean getAllInfoByMerchant(@RequestBody CollectiveTransOrder tis, PageBean page) throws Exception {
		try {
			AgentInfo loginAgent = agentInfoService.selectByPrincipal();
			int count = transInfoService.countAllInfoByMerchant(tis, loginAgent);
			if (count == 0) {
				return new ResponseBean(null);
			}
			List<CollectiveTransOrder> queryAllInfo = transInfoService.queryAllInfoByMerchant(page, tis, loginAgent);
			Map<String, AgentInfo> searchAgentChildren = ToolUtils.collection2Map(
					agentInfoService.selectSelfAndDirectChildren(tis.getAgentNo()),
					new ToolUtils.Transformer<AgentInfo>() {
						@Override
						public String transform(AgentInfo value) {
							return value.getAgentNode();
						}
					});
			AgentInfo searchAgent = agentInfoService.selectByagentNo(tis.getAgentNo());
			for (CollectiveTransOrder order : queryAllInfo) {
				String orderNo = order.getOrderNo();
				if ("11".equals(loginAgent.getAgentType())) {
					// Map<String,Object> map =
					// perAgentService.selectFromPaTransInfo(orderNo);
					Map<String, Object> map = perAgentService.selectFromPaMerInfoByMerchantNo(order.getMerchantNo());
					if (map != null) {
						order.setBelongUserCode(map.get("user_code") == null ? "" : map.get("user_code").toString());
					}
					log.info("订单号 " + orderNo + "在pa_trans_info表查询到数据 " + map + "======");
				}
				AgentInfo agentInfo = searchAgentChildren.get(cutOutTopNAgentNode(order.getAgentNode(), searchAgent));
				if (agentInfo != null) {
					order.setDirectlyAgentNo(agentInfo.getAgentNo());
					order.setDirectlyAgentName(agentInfo.getAgentName());
				}
				if (!agentFunctionService.queryFunctionValue(loginAgent, "001")) {
					order.setMobilephone(CommonUtil.replaceMask(order.getMobilephone(), "^(.{4}).*?$", "$1*******"));
				}
			}
			return new ResponseBean(queryAllInfo, count);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("查询报错", e);
			return new ResponseBean(e);
		}
	}

	@SystemLog(description = "导出商户交易汇总")
	@RequestMapping(value = "/exportTransInfoByMerchant")
	@ResponseBody
	public void exportTransInfoByMerchant(CollectiveTransOrder transInfo, HttpServletResponse response)
			throws Exception {
		// 判断当前登录用户是否有设置业务范围，防止非法操作
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		String accessTeamId = principal.getUserEntityInfo().getAccessTeamId();
		if (StringUtils.isNotBlank(accessTeamId) && !accessTeamId.equals(transInfo.getMerTeamId())) {
			log.error("到处商户交易汇总失败---非法操作");
		}

		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		AgentInfo loginAgent = agentInfoService.selectByPrincipal();
		List<CollectiveTransOrder> list = transInfoService.exportAllInfoByMerchant(transInfo, loginAgent);
		String fileName = "商户交易记录" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".xls";
		String fileNameFormat = new String(fileName.getBytes("GBK"), "ISO-8859-1");
		response.setHeader("Content-disposition", "attachment;filename=" + fileNameFormat);
		List<Map<String, String>> data = new ArrayList<>();
		if (list.size() < 1) {
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("merchantName", null);
			maps.put("merchantNo", null);
			maps.put("agentNo", null);
			maps.put("agentName", null);
			maps.put("parentAgentNo", null);
			// maps.put("parentAgentName", null);
			maps.put("mobilephone", null);
			maps.put("transAmount", null);
			maps.put("directlyAgentNo", null);
			maps.put("directlyAgentName", null);
			maps.put("ct", null);
			data.add(maps);
		} else {
			Map<String, AgentInfo> searchAgentChildren = ToolUtils.collection2Map(
					agentInfoService.selectSelfAndDirectChildren(transInfo.getAgentNo()),
					new ToolUtils.Transformer<AgentInfo>() {
						@Override
						public String transform(AgentInfo value) {
							return value.getAgentNode();
						}
					});
			AgentInfo searchAgent = agentInfoService.selectByagentNo(transInfo.getAgentNo());
			for (CollectiveTransOrder order : list) {
				// String orderNo = order.getOrderNo();
				// Map<String,Object> map =
				// perAgentService.selectFromPaTransInfo(orderNo);
				Map<String, Object> map = perAgentService.selectFromPaMerInfoByMerchantNo(order.getMerchantNo());
				if (map != null) {
					order.setBelongUserCode(map.get("user_code") == null ? "" : map.get("user_code").toString());
				}
				Map<String, String> maps = new HashMap<>();
				maps.put("merchantName", StringUtils.trimToEmpty(order.getMerchantName()));
				maps.put("merchantNo", StringUtils.trimToEmpty(order.getMerchantNo()));
				maps.put("belongUserCode", order.getBelongUserCode());
				maps.put("agentNo", StringUtils.trimToEmpty(order.getAgentNo()));
				maps.put("agentName", StringUtils.trimToEmpty(order.getAgentName()));
				maps.put("parentAgentNo", StringUtils.trimToEmpty(order.getParentAgentNo()));
				// maps.put("parentAgentName",
				// StringUtils.trimToEmpty(order.getParentAgentName()));
				AgentInfo agentInfo = searchAgentChildren.get(cutOutTopNAgentNode(order.getAgentNode(), searchAgent));
				maps.put("directlyAgentNo", StringUtils.trimToEmpty(agentInfo == null ? "" : agentInfo.getAgentNo()));
				maps.put("directlyAgentName",
						StringUtils.trimToEmpty(agentInfo == null ? "" : agentInfo.getAgentName()));
				maps.put("mobilephone", CommonUtil.replaceMask(order.getMobilephone(), "^(.{4}).*?$", "$1*******"));
				maps.put("transAmount", order.getTransAmount() == null ? "" : order.getTransAmount().toString());
				Date ct = order.getCt();
				String merchantCreateTime = "";
				if (ct != null) {
					merchantCreateTime = sdf1.format(ct);
				}
				maps.put("ct", merchantCreateTime);
				data.add(maps);
			}
		}
		ListDataExcelExport export = new ListDataExcelExport();
		String[] cols = new String[] { "merchantName", "merchantNo", "belongUserCode", "agentNo", "agentName",
				"parentAgentNo", "directlyAgentNo", "directlyAgentName", "mobilephone", "transAmount", "ct" };
		String[] colsName = new String[] { "商户简称", "商户编号", "所属盟主编号", "所属代理商编号", "所属代理商名称", "上级代理商编号", "直属下级代理商编号",
				"直属下级代理商名称", "商户手机号", "金额（元）", "商户创建时间" };
		OutputStream ouputStream = response.getOutputStream();
		export.export(cols, colsName, data, ouputStream);
		ouputStream.close();
	}

	/**
	 * 查询下拉框值
	 *
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getBoxInfo")
	@ResponseBody
	public Object getBoxInfo() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<SysDict> payMethod = sysDictService.selectPayMethodTypeAllDict();
			List<SysDict> serviceType = sysDictService.selectServiceTypeAllDict();
			List<SysDict> tranStatus = sysDictService.selectTranStatusAllDict();
			List<SysDict> transType = sysDictService.selectTransTypeAllDict();
			List<SysDict> cardType = sysDictService.selectCardTypeAllDict();
			map.put("payMethod", payMethod);
			map.put("serviceType", serviceType);
			map.put("tranStatus", tranStatus);
			map.put("transType", transType);
			map.put("cardType", cardType);
			map.put("bols", true);
		} catch (Exception e) {
			log.error("查询报错", e);
			map.put("bols", false);
			map.put("msg", "查询报错");
		}
		return map;
	}

	@RequestMapping("/queryInfoDetail")
	@ResponseBody
	public Object queryInfoDetail(String id) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			String agentNo = principal.getUserEntityInfo().getEntityId();// 当前代理商

			// 权限验证
			boolean access = accessService.canQueryTheCollectiveTransOrderInfo(id, false);
			if (!access) {
				map.put("msg", "非法操作");
				map.put("bols", false);
				return map;
			}

			// 当前记录的详细记录
			CollectiveTransOrder tt = transInfoService.queryInfoDetail(id);
			if (tt != null) {
				// //20170112
				Pattern p = Pattern.compile("\\d+(\\d{4})");
				String accountNo = tt.getAccountNo();
				if (accountNo != null) {
					Matcher matcher = p.matcher(accountNo);
					tt.setAccountNo(matcher.replaceAll("***************$1"));
				}

				if (tt.getTransTime() != null) {
					String orderId = tt.getTisId() + "";
					orderId = StringUtils.leftPad(orderId, 8, "0");
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
					String d = sdf.format(tt.getTransTime());
					String content = d + orderId + ".png";
					Date expiresDate = new Date(Calendar.getInstance().getTime().getTime() * 3600 * 1000);
					String newContent = ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_SIGN_TUCKET, content, expiresDate);
					tt.setSignImg(newContent);
				}
			} else {
				map.put("msg", "没有查询记录");
				map.put("bols", false);
				return map;
			}

			map.put("tt", tt);

			// 根据当前记录的代理商查询出节点
			AgentInfo aa1 = agentInfoService.selectByagentNo(agentNo);
			// 下级
			AgentInfo aa2 = agentInfoService.selectByParentId(tt.getAgentNo());
			if (aa1 == null) {
				map.put("msg", "没有查询代理商");
				map.put("bols", false);
				return map;
			}
			String node = aa1.getAgentNode();
			String[] nstr = node.split("-");
			for (int i = 1; i < nstr.length; i++) {
				if (agentNo.equals(nstr[i])) {
					CollectiveTransOrder c1 = collectiveTransOrderService.selectByOrderNo(i, tt.getOrderNo());
					String num1 = "0";
					if (c1 == null) {
						aa1.setSaleName(num1);
					} else {
						num1 = c1.getNum();
						aa1.setSaleName(num1);
					}
					if (aa2 != null) {
						CollectiveTransOrder c2 = collectiveTransOrderService.selectByOrderNo(i + 1, tt.getOrderNo());
						if (c2 == null) {
							aa2.setSaleName(num1);
						} else {
							aa2.setSaleName(c2.getNum());
						}
					}
				}
			}
			map.put("aa1", aa1);
			map.put("aa2", aa2);
			if (!tt.getPayMethod().equals("3")) {
				String accountNo = transInfoService.queryInfoDetail(id).getAccountNo();
				if (accountNo != null) {
					// 当前记录的账号的详情
					PosCardBin pcb = posCardBinService.queryInfo(accountNo);
					if (pcb == null) {
						map.put("msg", "没有查询到账号信息");
						map.put("bols", false);
						return map;
					}
					map.put("pcb", pcb);
				}
			}
			// 根据商户查结算账号
			MerchantRequireItem mri = merchantRequireItemService.selectByAccountNo(tt.getMerchantNo());
			if (mri == null) {
				map.put("msg", "没有查询到结算账号");
				map.put("bols", false);
				return map;
			}
			PosCardBin pcb1 = posCardBinService.queryInfo(mri.getContent());
			if (pcb1 != null) {
				// 20170112
				Pattern p = Pattern.compile("\\d+(\\d{4})");
				if (mri.getContent() != null) {
					Matcher matcher = p.matcher(mri.getContent());
					pcb1.setBankNo(matcher.replaceAll("***************$1"));
				}
			} else {
				map.put("msg", "没有查询到账号信息");
				map.put("bols", false);
				return map;
			}
			// pcb1.setBankNo(mri.getContent());
			map.put("pcb1", pcb1);
			map.put("bols", true);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("查询报错", e);
			map.put("bols", false);
			map.put("msg", "查询报错");
		}
		return map;
	}

	/**
	 * 风控调单查询订单信息
	 *
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/queryInfoDetailForSurveyOrder")
	@ResponseBody
	public Object queryInfoDetailForSurveyOrder(String id) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			String agentNo = principal.getUserEntityInfo().getEntityId();// 当前代理商
			// 权限验证
			AgentInfo loginAgent = agentInfoService.selectByPrincipal();
			CollectiveTransOrder cto = transInfoService.selectByOrderNoAndAgentNode(id, loginAgent.getAgentNode());
			if (cto == null) {
				map.put("msg", "没有查询记录");
				map.put("bols", false);
				return map;
			}
			// 当前记录的详细记录
			CollectiveTransOrder tt = transInfoService.queryInfoDetailForSurveyOrder(id);
			if (tt != null) {
				// //20170112
				Pattern p = Pattern.compile("\\d+(\\d{4})");
				String accountNo = tt.getAccountNo();
				if (accountNo != null) {
					Matcher matcher = p.matcher(accountNo);
					tt.setAccountNo(matcher.replaceAll("***************$1"));
				}

				if (tt.getTransTime() != null) {
					String orderId = tt.getTisId() + "";
					orderId = StringUtils.leftPad(orderId, 8, "0");
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
					String d = sdf.format(tt.getTransTime());
					String content = d + orderId + ".png";
					Date expiresDate = new Date(Calendar.getInstance().getTime().getTime() * 3600 * 1000);
					String newContent = ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_SIGN_TUCKET, content, expiresDate);
					tt.setSignImg(newContent);
				}
			} else {
				map.put("msg", "没有查询记录");
				map.put("bols", false);
				return map;
			}

			map.put("tt", tt);

			// 根据当前记录的代理商查询出节点
			AgentInfo aa1 = agentInfoService.selectByagentNo(agentNo);
			// 下级
			AgentInfo aa2 = agentInfoService.selectByParentId(tt.getAgentNo());
			if (aa1 == null) {
				map.put("msg", "没有查询代理商");
				map.put("bols", false);
				return map;
			}
			String node = aa1.getAgentNode();
			String[] nstr = node.split("-");
			for (int i = 1; i < nstr.length; i++) {
				if (agentNo.equals(nstr[i])) {
					CollectiveTransOrder c1 = collectiveTransOrderService.selectByOrderNo(i, tt.getOrderNo());
					String num1 = "0";
					if (c1 == null) {
						aa1.setSaleName(num1);
					} else {
						num1 = c1.getNum();
						aa1.setSaleName(num1);
					}
					if (aa2 != null) {
						CollectiveTransOrder c2 = collectiveTransOrderService.selectByOrderNo(i + 1, tt.getOrderNo());
						if (c2 == null) {
							aa2.setSaleName(num1);
						} else {
							aa2.setSaleName(c2.getNum());
						}
					}
				}
			}
			map.put("aa1", aa1);
			map.put("aa2", aa2);
			if (!tt.getPayMethod().equals("3")) {
				String accountNo = transInfoService.queryInfoDetailForSurveyOrder(id).getAccountNo();
				if (accountNo != null) {
					// 当前记录的账号的详情
					PosCardBin pcb = posCardBinService.queryInfo(accountNo);
					if (pcb == null) {
						map.put("msg", "没有查询到账号信息");
						map.put("bols", false);
						return map;
					}
					map.put("pcb", pcb);
				}
			}
			// 根据商户查结算账号
			MerchantRequireItem mri = merchantRequireItemService.selectByAccountNo(tt.getMerchantNo());
			if (mri == null) {
				map.put("msg", "没有查询到结算账号");
				map.put("bols", false);
				return map;
			}
			PosCardBin pcb1 = posCardBinService.queryInfo(mri.getContent());
			if (pcb1 != null) {
				// 20170112
				Pattern p = Pattern.compile("\\d+(\\d{4})");
				if (mri.getContent() != null) {
					Matcher matcher = p.matcher(mri.getContent());
					pcb1.setBankNo(matcher.replaceAll("***************$1"));
				}
			} else {
				map.put("msg", "没有查询到账号信息");
				map.put("bols", false);
				return map;
			}
			// pcb1.setBankNo(mri.getContent());
			map.put("pcb1", pcb1);
			map.put("bols", true);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("查询报错", e);
			map.put("bols", false);
			map.put("msg", "查询报错");
		}


		return map;
	}

	@SystemLog(description = "导出交易查询")
	@RequestMapping(value = "/exportTransInfo.do")
	@ResponseBody
	public void exportInfo(@RequestParam("info") String info, HttpServletResponse response, HttpServletRequest request)
			throws Exception {
//		info = new String(info.getBytes("ISO-8859-1"), "UTF-8");
		CollectiveTransOrder transInfo = JSON.parseObject(info, CollectiveTransOrder.class);
		ResponseBean responseBean = checkDate(transInfo);
		if (responseBean != null) {
			log.error("跳过页面js日期校验,非法操作,导出日期超出范围");
			return;
		}
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		String agentNo = principal.getUserEntityInfo().getEntityId();
		transInfo.setInitAgentNo(agentNo);
		// 根据当前记录的代理商查询出节点
		AgentInfo aa1 = agentInfoService.selectByagentNo(agentNo);
		int level = aa1.getAgentLevel();
		int level1 = level + 1;
		List<CollectiveTransOrder> list = transInfoService.exportAllInfo(transInfo, level, level1);

		// 2019-6-25 修改导出逻辑,并且aa1.getAgentType()为盛钱包是才赋值
		if ("11".equals(aa1.getAgentType())) {
			for (CollectiveTransOrder collectiveTransOrder : list) {
				// String orderNo = collectiveTransOrder.getOrderNo();
				// Map<String,Object> map =
				// perAgentService.selectFromPaTransInfo(orderNo);
				Map<String, Object> map = perAgentService.selectFromPaMerInfoByMerchantNo(collectiveTransOrder.getMerchantNo());
				if (map != null) {
					collectiveTransOrder
							.setBelongUserCode(map.get("user_code") == null ? "" : map.get("user_code").toString());
				}
				/*
				 * String accountNo = collectiveTransOrder.getAccountNo(); if
				 * (accountNo!=null) { Matcher matcher = p.matcher(accountNo);
				 * collectiveTransOrder.setAccountNo(matcher.replaceAll(
				 * "***************$1")); }
				 */
			}
		}

	/*	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		AgentInfo loginAgent = agentInfoService.selectByPrincipal();
		//List<CollectiveTransOrder> list = transInfoService.importAllInfoByMerchant(transInfo, loginAgent);
		//String fileName = "商户交易记录" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".xls";
		String fileNameFormat = new String(fileName.getBytes("GBK"), "ISO-8859-1");
		response.setHeader("Content-disposition", "attachment;filename=" + fileNameFormat);*/

		String fileName = "交易记录" + new SimpleDateFormat("YYYYMMdd_HHmmss").format(new Date());
		Map<String, AgentInfo> searchAgentChildren = ToolUtils.collection2Map(
				agentInfoService.selectSelfAndDirectChildren(transInfo.getAgentNo()),
				new ToolUtils.Transformer<AgentInfo>() {
					@Override
					public String transform(AgentInfo value) {
						return value.getAgentNode();
					}
				});
		AgentInfo searchAgent = agentInfoService.selectByagentNo(transInfo.getAgentNo());
	/*	String[] columnNames = { "订单号", "结算周期", "商户简称", "商户编号", "所属盟主编号", "业务产品", "收款类型", "交易类型", "活动类型", "卡种", "交易卡号",
				"代理商名称", "直属下级代理商编号", "直属下级代理商名称", "商户手机号", "金额（元）", "交易状态", "结算状态", "交易时间", "机具活动类型", "商户创建时间", "机具编号",
				"商户组织" };*/
		/*Workbook workbook = ExcelUtils.createWorkBook("交易记录", list, columnNames,
				createTransOrderInfoCreateRow(sysDictService, terminalInfoService, searchAgentChildren, searchAgent));*/
		response.reset();


	    //response.setHeader("Content-disposition", "attachment; filename = " + URLEncoder.encode(fileName, "UTF-8"));
        response.setContentType("application/octet-streem");
		//response.setContentType("application/vnd.ms-excel;charset=utf-8");
		response.setHeader("Content-Disposition", "attachment;filename=" + new String((fileName + ".xlsx").getBytes("UTF-8"), "ISO8859-1"));
		//fileName = URLEncoder.encode((fileName+".xlsx"),"UTF-8");
		//response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
		/*workbook.write(response.getOutputStream());
		response.flushBuffer();*/

		// 数据字典
		List<Map<String, String>> data = filter(list, searchAgentChildren, searchAgent);


		String[] cols = new String[] { "orderNo", "settlementMethod", "merchantName", "merchantNo", "belongUserCode",
				"bpName", "commendedSource","serviceType", "transType1", "orderType", "cardType1", "accountNo", "agentName",
				"nextAgentNo", "nextAgentName", "mobilephone", "transAmount", "transStatus1", "settleStatus1",
				"transTime", "activityType", "ct", "sn", "merGroup" };

		String[] colsName = { "订单号", "结算周期", "商户简称", "商户编号", "所属盟主编号", "业务产品","超级推商户", "收款类型", "交易类型", "活动类型", "卡种", "交易卡号",
				"代理商名称", "直属下级代理商编号", "直属下级代理商名称", "商户手机号", "金额（元）", "交易状态", "结算状态", "交易时间", "机具活动类型", "商户创建时间", "机具编号",
				"商户组织" };


		BigExcel export = new BigExcel();
		OutputStream ouputStream = response.getOutputStream();
		export.export(cols, colsName, data, ouputStream);
	}

	private List<Map<String, String>> filter(List<CollectiveTransOrder> list,
			Map<String, AgentInfo> searchAgentChildren, AgentInfo searchAgent) {
		Map<String, String> settlementMethodMap = new HashedMap();

		settlementMethodMap.put("0", "T0");
		settlementMethodMap.put("1", "T1");

		Map<String, String> transTypeMap = new HashedMap();
		Map<String, String> cardTypeMap = new HashedMap();
		Map<String, String> tranStatusMap = new HashedMap();
		Map<String, String> settleStatusMap = new HashedMap();
		Map<String, String> activityTypeMap = new HashedMap();
		Map<String, String> orderTypeMap = new HashedMap();
		Map<String, String> serviceTypeMap = new HashedMap();
	//	String superPushBpId = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		List<SysDict> transType = sysDictService.selectTransTypeAllDict();
		List<SysDict> cardType = sysDictService.selectCardTypeAllDict();
		List<SysDict> tranStatus = sysDictService.selectTranStatusAllDict();
		List<SysDict> settleStatus = sysDictService.listSysDictGroup("SETTLE_STATUS");
		List<SysDict> orderType = sysDictService.listSysDictGroup("ORDER_TYPE");
		List<SysDict> serviceType = sysDictService.listSysDictGroup("SERVICE_TYPE");
		for (int i = 0; i < transType.size(); i++) {
			transTypeMap.put(transType.get(i).getSysValue(), transType.get(i).getSysName());
		}
		for (int i = 0; i < cardType.size(); i++) {
			cardTypeMap.put(cardType.get(i).getSysValue(), cardType.get(i).getSysName());
		}
		for (int i = 0; i < tranStatus.size(); i++) {
			tranStatusMap.put(tranStatus.get(i).getSysValue(), tranStatus.get(i).getSysName());
		}
		for (int i = 0; i < settleStatus.size(); i++) {
			settleStatusMap.put(settleStatus.get(i).getSysValue(), settleStatus.get(i).getSysName());
		}
		for (int i = 0; i < orderType.size(); i++) {
			orderTypeMap.put(orderType.get(i).getSysValue(), orderType.get(i).getSysName());
		}
		List<Map<String, String>> activityTypeList = terminalInfoService.selectAllActivityType();
		for (int i = 0; i < activityTypeList.size(); i++) {
			activityTypeMap.put(activityTypeList.get(i).get("sys_value"), activityTypeList.get(i).get("sys_name"));
		}
		for (int i = 0; i < serviceType.size(); i++) {
			String value = serviceType.get(i).getSysValue();
			String name = serviceType.get(i).getSysName();
			if (StringUtils.equals("4", value)) {
				name = "POS刷卡";
			} else if (StringUtils.equals("9", value)) {
				name = "VIP收款";
			}
			serviceTypeMap.put(value, name);
		}
		//superPushBpId = sysDictService.getSuperPushBpId();

		List<Map<String, String>> data = new ArrayList<>();
		 Pattern p = Pattern.compile("\\d+(\\d{4})");
		for (CollectiveTransOrder order : list) {
			Map<String, String> maps = new HashMap<>();

			maps.put("orderNo", order.getOrderNo());
			maps.put("settlementMethod", StringUtils.trimToEmpty(settlementMethodMap.get(order.getSettlementMethod()))); // 结算周期
			// row.createCell(index++).setCellValue(StringUtils.trimToEmpty(settlementMethodMap.get(order.getSettlementMethod());
			maps.put("merchantName", order.getMerchantName());
			// row.createCell(index++).setCellValue(order.getMerchantName()); //
			// 商户简称
			maps.put("merchantNo", order.getMerchantNo());
			// row.createCell(index++).setCellValue(order.getMerchantNo()); //
			// 商户编号
			maps.put("belongUserCode", order.getBelongUserCode());
			// row.createCell(index++).setCellValue(order.getBelongUserCode());
			// // 所属盟主编号
			maps.put("bpName", order.getBpName());
			// row.createCell(index++).setCellValue(order.getBpName()); // 业务产品
			maps.put("serviceType", serviceTypeMap.get(order.getServiceType()));
			// row.createCell(index++).setCellValue(serviceTypeMap.get(order.getServiceType()));

			// 超级推商户
			maps.put("commendedSource", "1".equalsIgnoreCase(order.getRecommendedSource())?"是":"否");
			// // 收款服务
			maps.put("transType1",
					StringUtils.trimToEmpty(transTypeMap.get(order.getTransType1())));
			// row.createCell(index++).setCellValue(StringUtils.trimToEmpty(transTypeMap.get(order.getTransType1())));
			// // 交易类型
			maps.put("orderType", StringUtils.trimToEmpty(orderTypeMap.get(order.getOrderType())));
			// row.createCell(index++).setCellValue(StringUtils.trimToEmpty(orderTypeMap.get(order.getOrderType())));
			// // 交易活动类型
			maps.put("cardType1", StringUtils.trimToEmpty(cardTypeMap.get(order.getCardType1())));
			// row.createCell(index++).setCellValue(StringUtils.trimToEmpty(cardTypeMap.get(order.getCardType1())));
			// // 卡种
			String accountNo = order.getAccountNo();
			if (accountNo != null) {
				Matcher matcher = p.matcher(accountNo);
				order.setAccountNo(matcher.replaceAll("***************$1"));
			}
			maps.put("accountNo", order.getAccountNo());
			// row.createCell(index++).setCellValue(order.getAccountNo()); //
			// 交易卡号
			maps.put("agentName", order.getAgentName());
			// row.createCell(index++).setCellValue(order.getAgentName()); //
			// 代理商名称
			AgentInfo agentInfo = searchAgentChildren
					.get(TransInfoAction.cutOutTopNAgentNode(order.getAgentNode(), searchAgent));
			maps.put("nextAgentNo", agentInfo == null ? "" : agentInfo.getAgentNo());
			// row.createCell(index++).setCellValue(agentInfo == null ? "" :
			// agentInfo.getAgentNo()); // 直属下级代理商编号
			maps.put("nextAgentName", agentInfo == null ? "" : agentInfo.getAgentName());
			// row.createCell(index++).setCellValue(agentInfo == null ? "" :
			// agentInfo.getAgentName()); // 直属下级代理商名称
			maps.put("mobilephone", CommonUtil.replaceMask(order.getMobilephone(), "^(.{4}).*?$", "$1*******"));
			// row.createCell(index++).setCellValue(CommonUtil.replaceMask(order.getMobilephone(),
			// "^(.{4}).*?$", "$1*******")); // 商户手机号
			maps.put("transAmount", ObjectUtils.toString(order.getTransAmount()));
			// row.createCell(index++).setCellValue(ObjectUtils.toString(order.getTransAmount()));
			// // 金额（元）
			maps.put("transStatus1", StringUtils.trimToEmpty(tranStatusMap.get(order.getTransStatus1())));
			// row.createCell(index++).setCellValue(StringUtils.trimToEmpty(tranStatusMap.get(order.getTransStatus1())));
			// // 交易状态
			maps.put("settleStatus1", StringUtils.trimToEmpty(settleStatusMap.get(order.getSettleStatus1())));
			// row.createCell(index++).setCellValue(StringUtils.trimToEmpty(settleStatusMap.get(order.getSettleStatus1())));
			// // 结算状态
			maps.put("transTime", order.getTransTime() == null ? "" : sdf.format(order.getTransTime()));
			// row.createCell(index++).setCellValue(order.getTransTime() == null
			// ? "" : sdf.format(order.getTransTime())); // 交易时间
			maps.put("activityType",
					StringUtils.trimToEmpty(getActivityType(order.getActivityType(), activityTypeMap)));

			// row.createCell(index++).setCellValue(StringUtils.trimToEmpty(getActivityType(order.getActivityType())));
			// // 机具活动类型
			// row.createCell(index ++).setCellValue(StringUtils.equals("0",
			// order.getDeductionFee()) ? "否" : "是"); // 是否抵扣鼓励金
			maps.put("ct", order.getCt() == null ? "" : sdf.format(order.getCt()));
			// row.createCell(index++).setCellValue(order.getCt() == null ? "" :
			// sdf.format(order.getCt())); // 商户创建时间
			maps.put("sn", order.getSn());
			// row.createCell(index++).setCellValue(order.getSn());
			maps.put("merGroup", order.getMerGroup());
			// row.createCell(index++).setCellValue(order.getMerGroup());
			data.add(maps);
		}

		return data;
	}

	private String getActivityType(String activityType, Map<String, String> activityTypeMap) {
		if (StringUtils.isBlank(activityType)) {
			return "";
		}
		String[] split = activityType.split(",");
		StringBuilder sb = new StringBuilder();
		for (String s : split) {
			String type = activityTypeMap.get(s);
			if (StringUtils.isNotBlank(type)) {
				sb.append(type).append(",");
			}
		}
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

	// ===================== start 交易查询导出相关 =====================
	/*private static TransOrderInfoCreateRow transOrderInfoCreateRow = null;

	private TransOrderInfoCreateRow createTransOrderInfoCreateRow(SysDictService sysDictService,
			TerminalInfoService terminalInfoService, Map<String, AgentInfo> searchAgentChildren,
			AgentInfo searchAgent) {
		if (transOrderInfoCreateRow != null) {
			transOrderInfoCreateRow.searchAgentChildren = searchAgentChildren;
			transOrderInfoCreateRow.searchAgent = searchAgent;
			return transOrderInfoCreateRow;
		}
		transOrderInfoCreateRow = new TransOrderInfoCreateRow().accept(sysDictService, terminalInfoService);
		transOrderInfoCreateRow.searchAgentChildren = searchAgentChildren;
		transOrderInfoCreateRow.searchAgent = searchAgent;
		return transOrderInfoCreateRow;
	}

	private static class TransOrderInfoCreateRow extends CreateRow<CollectiveTransOrder> {
		public Map<String, AgentInfo> searchAgentChildren;
		public AgentInfo searchAgent;
		private Map<String, String> settlementMethodMap = new HashedMap();
		private Map<String, String> transTypeMap = new HashedMap();
		private Map<String, String> cardTypeMap = new HashedMap();
		private Map<String, String> tranStatusMap = new HashedMap();
		private Map<String, String> settleStatusMap = new HashedMap();
		private Map<String, String> activityTypeMap = new HashedMap();
		private Map<String, String> orderTypeMap = new HashedMap();
		private Map<String, String> serviceTypeMap = new HashedMap();
		private String superPushBpId = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		public TransOrderInfoCreateRow() {
			settlementMethodMap.put("0", "T0");
			settlementMethodMap.put("1", "T1");
		}

		@Override
		public void writeRow(Row row, CollectiveTransOrder order) {
			int index = 0;
			row.createCell(index++).setCellValue(order.getOrderNo()); // 订单号
			String settlementMethod = settlementMethodMap.get(order.getSettlementMethod());
			row.createCell(index++).setCellValue(StringUtils.trimToEmpty(settlementMethod)); // 结算周期
			row.createCell(index++).setCellValue(order.getMerchantName()); // 商户简称
			row.createCell(index++).setCellValue(order.getMerchantNo()); // 商户编号
			row.createCell(index++).setCellValue(order.getBelongUserCode()); // 所属盟主编号
			row.createCell(index++).setCellValue(order.getBpName()); // 业务产品
			row.createCell(index++).setCellValue(serviceTypeMap.get(order.getServiceType())); // 收款服务
			row.createCell(index++).setCellValue(StringUtils.trimToEmpty(transTypeMap.get(order.getTransType1()))); // 交易类型
			row.createCell(index++).setCellValue(StringUtils.trimToEmpty(orderTypeMap.get(order.getOrderType()))); // 交易活动类型
			row.createCell(index++).setCellValue(StringUtils.trimToEmpty(cardTypeMap.get(order.getCardType1()))); // 卡种
			row.createCell(index++).setCellValue(order.getAccountNo()); // 交易卡号
			row.createCell(index++).setCellValue(order.getAgentName()); // 代理商名称
			AgentInfo agentInfo = searchAgentChildren
					.get(TransInfoAction.cutOutTopNAgentNode(order.getAgentNode(), searchAgent));
			row.createCell(index++).setCellValue(agentInfo == null ? "" : agentInfo.getAgentNo()); // 直属下级代理商编号
			row.createCell(index++).setCellValue(agentInfo == null ? "" : agentInfo.getAgentName()); // 直属下级代理商名称
			row.createCell(index++)
					.setCellValue(CommonUtil.replaceMask(order.getMobilephone(), "^(.{4}).*?$", "$1*******")); // 商户手机号
			row.createCell(index++).setCellValue(ObjectUtils.toString(order.getTransAmount())); // 金额（元）
			row.createCell(index++).setCellValue(StringUtils.trimToEmpty(tranStatusMap.get(order.getTransStatus1()))); // 交易状态
			row.createCell(index++)
					.setCellValue(StringUtils.trimToEmpty(settleStatusMap.get(order.getSettleStatus1()))); // 结算状态
			Date transTime = order.getTransTime();
			row.createCell(index++).setCellValue(transTime == null ? "" : sdf.format(transTime)); // 交易时间
			row.createCell(index++).setCellValue(StringUtils.trimToEmpty(getActivityType(order.getActivityType()))); // 机具活动类型
			// row.createCell(index ++).setCellValue(StringUtils.equals("0",
			// order.getDeductionFee()) ? "否" : "是"); // 是否抵扣鼓励金

			Date ct = order.getCt();
			row.createCell(index++).setCellValue(ct == null ? "" : sdf.format(ct)); // 商户创建时间

			row.createCell(index++).setCellValue(order.getSn());
			row.createCell(index++).setCellValue(order.getMerGroup());
		}

		private String getActivityType(String activityType) {
			if (StringUtils.isBlank(activityType)) {
				return "";
			}
			String[] split = activityType.split(",");
			StringBuilder sb = new StringBuilder();
			for (String s : split) {
				String type = activityTypeMap.get(s);
				if (StringUtils.isNotBlank(type)) {
					sb.append(type).append(",");
				}
			}
			if (sb.length() > 0) {
				sb.deleteCharAt(sb.length() - 1);
			}
			return sb.toString();
		}

		public TransOrderInfoCreateRow accept(SysDictService sysDictService, TerminalInfoService terminalInfoService) {
			List<SysDict> transType = sysDictService.selectTransTypeAllDict();
			List<SysDict> cardType = sysDictService.selectCardTypeAllDict();
			List<SysDict> tranStatus = sysDictService.selectTranStatusAllDict();
			List<SysDict> settleStatus = sysDictService.listSysDictGroup("SETTLE_STATUS");
			List<SysDict> orderType = sysDictService.listSysDictGroup("ORDER_TYPE");
			List<SysDict> serviceType = sysDictService.listSysDictGroup("SERVICE_TYPE");
			for (int i = 0; i < transType.size(); i++) {
				transTypeMap.put(transType.get(i).getSysValue(), transType.get(i).getSysName());
			}
			for (int i = 0; i < cardType.size(); i++) {
				cardTypeMap.put(cardType.get(i).getSysValue(), cardType.get(i).getSysName());
			}
			for (int i = 0; i < tranStatus.size(); i++) {
				tranStatusMap.put(tranStatus.get(i).getSysValue(), tranStatus.get(i).getSysName());
			}
			for (int i = 0; i < settleStatus.size(); i++) {
				settleStatusMap.put(settleStatus.get(i).getSysValue(), settleStatus.get(i).getSysName());
			}
			for (int i = 0; i < orderType.size(); i++) {
				orderTypeMap.put(orderType.get(i).getSysValue(), orderType.get(i).getSysName());
			}
			List<Map<String, String>> activityTypeList = terminalInfoService.selectAllActivityType();
			for (int i = 0; i < activityTypeList.size(); i++) {
				activityTypeMap.put(activityTypeList.get(i).get("sys_value"), activityTypeList.get(i).get("sys_name"));
			}
			for (int i = 0; i < serviceType.size(); i++) {
				String value = serviceType.get(i).getSysValue();
				String name = serviceType.get(i).getSysName();
				if (StringUtils.equals("4", value)) {
					name = "POS刷卡";
				} else if (StringUtils.equals("9", value)) {
					name = "VIP收款";
				}
				serviceTypeMap.put(value, name);
			}
			superPushBpId = sysDictService.getSuperPushBpId();
			return this;
		}
	}*/
	// ===================== end 交易查询导出相关 =====================

	/**
	 * 数据初始化和分页查询
	 *
	 * @param tis
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/getThreeAllInfo")
	@ResponseBody
	public ResponseBean getThreeAllInfo(@RequestBody CollectiveTransOrder tis,
			@RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "10") int pageSize) {
		try {
			ResponseBean responseBean = null;
			if (StringUtils.isBlank(tis.getEdate()) || StringUtils.isBlank(tis.getSdate())) {
				responseBean = new ResponseBean("查询交易时间不能为空", false);
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date endDate = sdf.parse(tis.getEdate());
			Date startDate = sdf.parse(tis.getSdate());
			if (endDate.getTime() - startDate.getTime() > 93 * 24 * 60 * 60 * 1000L) {
				responseBean = new ResponseBean("查询范围不能超过三个月", false);
			}
			if (responseBean != null) {
				return responseBean;
			}
			AgentInfo loginAgent = agentInfoService.selectByPrincipal();
			String loginAgentNo = loginAgent.getAgentNo();
			String selectAgentNo = tis.getAgentNo();
			boolean auth = agentInfoService.isAuth(loginAgentNo, selectAgentNo);
			if (!auth) {
				Map<String, Object> resultMap = new HashMap<>();
				resultMap.put("numAndMoney", null);
				resultMap.put("transData", new ArrayList<>());
				return new ResponseBean(resultMap, 0);
			}
			loginAgentNo = selectAgentNo;
			AgentInfo aa2 = agentInfoService.selectByagentNo(selectAgentNo);
			loginAgent = aa2;
			tis.setInitAgentNo(loginAgentNo);
			// 根据当前记录的代理商查询出节点
			AgentInfo aa1 = agentInfoService.selectByagentNo(loginAgentNo);
			tis.setDirectlyAgentName(aa2.getAgentName());
			int level = aa1.getAgentLevel();
			int level1 = level + 1;
			String numAndMoney = transInfoService.queryNumAndMoney(tis, loginAgentNo);
			Page<CollectiveTransOrder> page = new Page<>(pageNo, pageSize);
			List<CollectiveTransOrder> queryAllInfo = transInfoService.queryAllInfo(page, tis, level, level1);
			Map<String, AgentInfo> searchAgentChildren = ToolUtils.collection2Map(
					agentInfoService.selectSelfAndDirectChildren(tis.getAgentNo()),
					new ToolUtils.Transformer<AgentInfo>() {
						@Override
						public String transform(AgentInfo value) {
							return value.getAgentNode();
						}
					});
			AgentInfo searchAgent = agentInfoService.selectByagentNo(tis.getAgentNo());

			// =======20170112,tiangh=======
			Pattern p = Pattern.compile("\\d+(\\d{4})");
			for (CollectiveTransOrder collectiveTransOrder : queryAllInfo) {
				String orderNo = collectiveTransOrder.getOrderNo();
				if ("11".equals(loginAgent.getAgentType())) {
					// Map<String,Object> map =
					// perAgentService.selectFromPaTransInfo(orderNo);
					Map<String, Object> map = perAgentService
							.selectFromPaMerInfoByMerchantNo(collectiveTransOrder.getMerchantNo());
					if (map != null) {
						collectiveTransOrder
								.setBelongUserCode(map.get("user_code") == null ? "" : map.get("user_code").toString());
					}
					log.info("订单号 " + orderNo + "在pa_trans_info表查询到数据 " + map + "======");
				}
				String accountNo = collectiveTransOrder.getAccountNo();
				if (accountNo != null) {
					Matcher matcher = p.matcher(accountNo);
					collectiveTransOrder.setAccountNo(matcher.replaceAll("***************$1"));
				}
				/*
				 * if (!agentFunctionService.queryFunctionValue(loginAgent,
				 * "001")) {
				 * collectiveTransOrder.setMobilephone(CommonUtil.replaceMask(
				 * collectiveTransOrder.getMobilephone(), "^(.{4}).*?$",
				 * "$1*******")); }
				 */
				collectiveTransOrder.setMobilephone(CommonUtil.replaceMask(collectiveTransOrder.getMobilephone(),
						"(?<=[\\d]{3})\\d(?=[\\d]{3})", "*"));
				AgentInfo childrenAgent = searchAgentChildren
						.get(cutOutTopNAgentNode(collectiveTransOrder.getAgentNode(), searchAgent));
				if (childrenAgent != null) {
					collectiveTransOrder.setDirectlyAgentName(childrenAgent.getAgentName());
					collectiveTransOrder.setDirectlyAgentNo(childrenAgent.getAgentNo());
				}
			}

			Map<String, Object> resultMap = new HashMap<>();
			resultMap.put("numAndMoney", StringUtils.isBlank(numAndMoney) ? "0" : numAndMoney);
			resultMap.put("transData", page.getResult());
			return new ResponseBean(resultMap, page.getTotalCount());
		} catch (Exception e) {
			e.printStackTrace();
			log.error("查询报错", e);
			return new ResponseBean(e);
		}
	}
}
