package cn.eeepay.boss.action;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.HardwareProduct;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.service.HardwareProductService;

@Controller
@RequestMapping(value = "/hardwareProduct")
public class HardwareProductAction {
	private static final Logger log = LoggerFactory.getLogger(BusinessProductDefineAction.class);

	@Resource
	private HardwareProductService hardwareProductServiceImpl;
	
	@Resource
	private AgentInfoService agentInfoService;

	@RequestMapping(value = "/selectAllInfo.do")
	public @ResponseBody Object selectAllInfo() {
		List<HardwareProduct> list = new ArrayList<>();
		try {
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String entityId = principal.getUserEntityInfo().getEntityId();
			AgentInfo entityInfo = agentInfoService.selectByagentNo(entityId);
			list = hardwareProductServiceImpl.selectAllInfo(entityId,entityInfo.getAgentOem());
			for (HardwareProduct hardwareProduct : list) {
				hardwareProduct.setTypeName(hardwareProduct.getTypeName() + hardwareProduct.getVersionNu());
			}
		} catch (Exception e) {
			log.error("硬件产品各类查询失败！",e);
		}
		return list;
	}

	/**
	 * 查询硬件产品类型,加上PN条件
	 * @return
	 */
	@RequestMapping(value = "/selectAllInfoByPn.do")
	public @ResponseBody Object selectAllInfoByPn() {
		List<HardwareProduct> list = new ArrayList<>();
		try {
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String entityId = principal.getUserEntityInfo().getEntityId();
			AgentInfo entityInfo = agentInfoService.selectByagentNo(entityId);
			list = hardwareProductServiceImpl.selectAllInfoByPn(entityId,entityInfo.getAgentOem());
			for (HardwareProduct hardwareProduct : list) {
				hardwareProduct.setTypeName(hardwareProduct.getTypeName() + hardwareProduct.getVersionNu());
			}
		} catch (Exception e) {
			log.error("根据PN匹配查询硬件产品种类失败！",e);
		}
		return list;
	}
}
