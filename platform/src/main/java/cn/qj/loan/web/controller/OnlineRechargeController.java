
package cn.qj.loan.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.qj.core.common.AjaxResult;
import cn.qj.core.entity.OnlineRecharge;
import cn.qj.core.service.OnlineRechargeService;
import cn.qj.core.util.StringUtil;

/**
 * 线上充值
 * 
 * @author Qiujian
 * @date 2018/11/05
 */
@RestController
@RequestMapping("/onlineRecharge")
public class OnlineRechargeController {

	@Autowired
	private OnlineRechargeService service;

	@RequestMapping("/create")
	public AjaxResult createRecharge(OnlineRecharge onlineRecharge) {
		service.save(onlineRecharge);
		return AjaxResult.success("创建线上支付订单成功");
	}

	@RequestMapping("/pay")
	public AjaxResult pay(Long id, String channel) {
		if (id == null) {
			return AjaxResult.err("线上充值ID不能为空");
		}
		if (!StringUtil.hasLength(channel)) {
			return AjaxResult.err("支付渠道不能为为空");
		}

		// 查询线下充值
		OnlineRecharge recharge = service.get(id);
		if (recharge == null) {
			return AjaxResult.err("无此笔充值");
		} else {
			Integer status = recharge.getTransStatus();
			if (OnlineRecharge.TRANS_SUCCESS == status) {
				return AjaxResult.err("该笔充值已支付，请勿重复支付");
			}
		}
		recharge.setChannel(channel);
		// 进行充值
		service.pay(recharge);

		return AjaxResult.success("支付成功");
	}

	@RequestMapping("/wechatPayCallback")
	public void wechatPayCallback(HttpServletRequest request) {
		System.out.println("=================================");
	}
}
