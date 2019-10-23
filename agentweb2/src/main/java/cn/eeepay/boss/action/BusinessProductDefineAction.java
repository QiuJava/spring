package cn.eeepay.boss.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AddRequireItem;
import cn.eeepay.framework.model.BusinessProductDefine;
import cn.eeepay.framework.model.HardwareProduct;
import cn.eeepay.framework.model.JoinTable;
import cn.eeepay.framework.model.ServiceInfo;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.service.BusinessProductDefineService;

@Controller
@RequestMapping(value = "/businessProductDefine")
public class BusinessProductDefineAction {
	private static final Logger log = LoggerFactory.getLogger(BusinessProductDefineAction.class);

	@Resource
	private BusinessProductDefineService businessProductDefineService;

	@RequestMapping(value = "/selectAllInfo.do")
	@ResponseBody
	public Object selectAllInfo() throws Exception {
		List<BusinessProductDefine> list = new ArrayList<>();
		try {
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			list = businessProductDefineService.selectAllInfo(principal.getUserEntityInfo().getEntityId());
		} catch (Exception e) {
			log.error("查询所有业务产品失败！",e);
		}
		return list;
	}
	
	/**
	 * 条件查询业务产品 by tans
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/selectProduct.do")
	public @ResponseBody Object selectProduct(@RequestParam("baseInfo") String param,
			@ModelAttribute("page") Page<BusinessProductDefine> page) throws Exception {
		try {
			BusinessProductDefine bpd = JSON.parseObject(param,BusinessProductDefine.class);
			businessProductDefineService.selectByCondition(page, bpd);
		} catch (Exception e) {
			log.error("条件查询业务产品！", e);
		}
		return page;
	}

	/**
	 * 查询业务产品详情 by tans
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/productDetailCtrl/{id}")
	@ResponseBody
	public Map<String, Object> productDetail(@PathVariable("id") String id) throws Exception {
		Map<String, Object> msg = new HashMap<String, Object>();
		System.out.println("查询业务产品详情Id = " + id);
		try {
			msg = businessProductDefineService.selectDetailById(id);
			msg.put("status", true);
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "查询业务产品详情失败！");
			log.error("查询业务产品详情失败！", e);
		}
		return msg;
	}

	/**
	 * 进入增加业务产品页面时，需要携带的数据
	 */
	@RequestMapping(value = "/addProductCtrl")
	@ResponseBody
	public Map<String, Object> addOrUpdate() {
		Map<String, Object> msg = new HashMap<>();
		try {
			String bpid = "";
			msg = businessProductDefineService.selectLinkInfo(bpid);
		} catch (Exception e) {
			log.error("加载增加业务产品页面时失败！", e);
		}
		return msg;
	}

	/**
	 * 进入业务产品的修改时，需要带入的数据
	 */
	@RequestMapping(value = "/editProductCtrl/{id}")
	@ResponseBody
	public Map<String, Object> editProduct(@PathVariable("id") String id) throws Exception {
		Map<String, Object> msg = new HashMap<>();
		try {
			msg = productDetail(id);
			Map<String, Object> linkInfo = businessProductDefineService.selectLinkInfo(id);
			msg.put("linkInfo", linkInfo);
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "进入新增业务产品页面时失败！");
		}

		return msg;
	}

	/**
	 * 保存业务产品的增加 || 修改
	 */
	@RequestMapping(value = "/saveProduct")
	@ResponseBody
	public Map<String, Object> saveProduct(@RequestBody String params) throws Exception {
		Map<String, Object> msg = new HashMap<>();
		try {
			JSONObject json = JSON.parseObject(params);
			BusinessProductDefine product = json.getObject("baseInfo", BusinessProductDefine.class);
			List<ServiceInfo> services = JSON.parseArray(json.getJSONArray("services").toJSONString(),
					ServiceInfo.class);
			List<AddRequireItem> items = JSON.parseArray(json.getJSONArray("items").toJSONString(),
					AddRequireItem.class);
			List<HardwareProduct> hards = JSON.parseArray(json.getJSONArray("hards").toJSONString(),
					HardwareProduct.class);
			Map<String, Object> info = new HashMap<>();
			info.put("product", product);
			info.put("services", services);
			info.put("items", items);
			info.put("hards", hards);
			int sum = businessProductDefineService.insertOrUpdate(info);
			if (sum > 0) {
				msg.put("statue", true);
				msg.put("msg", "添加 || 修改业务产品成功！");
			} else {
				msg.put("statue", false);
				msg.put("msg", "添加 || 修改业务产品失败！");
			}
		} catch (Exception e) {
			log.error("添加 || 修改业务产品失败!", e);
			msg.put("status", false);
			msg.put("msg", "添加 || 修改业务产品失败！");
		}

		return msg;
	}

	/**
	 * 查询业务产品 返回业务产品的：id，名称bpName，所属组织名称teamName
	 */
	@RequestMapping("/selectBpTeam.do")
	@ResponseBody
	public List<BusinessProductDefine> selectBpTeam() throws Exception {
		List<BusinessProductDefine> list = null;
		try {
			list = businessProductDefineService.selectBpTeam();
		} catch (Exception e) {
			log.error("查询业务产品名称及组织名称失败", e);
		}
		return list;
	}
	
	/**
	 * 根据业务产品Id，查看能否修改
	 */
	@RequestMapping("/isUsed/{id}")
	@ResponseBody
	public Map<String, Object> isUsed(@PathVariable("id") Integer bpId) throws Exception{
		Map<String, Object> msg = new HashMap<>();
		try {
			boolean flag = businessProductDefineService.selectRecord(bpId);
			msg.put("flag", flag);
		} catch (Exception e) {
			log.error("查询业务产品名称及组织名称失败", e);
		}
		return msg;
	}
	
	@RequestMapping("/selectProductByAgent")
	@ResponseBody
	public Map<String,Object> selectProductByAgent() throws Exception{
		Map<String, Object> msg = new HashMap<>();
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		try {
			List<JoinTable> parentProducts = businessProductDefineService.selectProductByAgent(principal.getUserEntityInfo().getEntityId());
			Map<String, String> teamMap = businessProductDefineService.selectTeamByAgentAndBp(principal.getUserEntityInfo().getEntityId());
			msg.put("parentProducts", parentProducts);
			msg.put("teamInfo", teamMap);
		} catch (Exception e) {
			log.error("查询业务产品名称及组织名称失败", e);
		}
		return msg;
	}
}
