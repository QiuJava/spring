package cn.pay.loan.web.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.pay.core.consts.BidConst;
import cn.pay.core.domain.business.Borrow;
import cn.pay.core.domain.sys.LoginInfo;
import cn.pay.core.obj.vo.AjaxResult;
import cn.pay.core.service.AccountService;
import cn.pay.core.service.BorrowService;
import cn.pay.core.service.UserInfoService;
import cn.pay.core.util.HttpSessionContext;

/**
 * 借款相关
 * 
 * @author Administrator
 *
 */
@Controller
public class BorrowController {
	public static final String BORROW = "borrow";

	@Autowired
	private BorrowService service;
	@Autowired
	private UserInfoService userInfoService;
	@Autowired
	private AccountService accountService;

	@Autowired
	private JmsMessagingTemplate jmsMessagingTemplate;
	@Autowired
	private Queue bidQueue;

	/**
	 * 根据用户是否登录分配不同页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/borrow/home")
	public String borrow(Model model) {
		LoginInfo current = HttpSessionContext.getCurrentLoginInfo();
		// 如果当前用户没有登录应该直接导向到静态页面
		if (current == null) {
			return "redirect:/borrow.html";
		}
		model.addAttribute("userInfo", userInfoService.get(current.getId()));
		model.addAttribute("creditBorrowScore", BidConst.CREDIT_BORROW_SCORE);
		model.addAttribute("initBorrowLimit", BidConst.INIT_BORROW_LIMIT);
		return BORROW;
	}

	@RequestMapping("/borrow/info")
	public String info(Model model) {
		if (service.isApplyBorrow()) {
			// 查询用户账户信息
			model.addAttribute("account", accountService.get(HttpSessionContext.getCurrentLoginInfo().getId()));
			model.addAttribute("minBorrowAmount", BidConst.MIN_BORROW_AMOUNT);
			model.addAttribute("minBidAmount", BidConst.MIN_BID_AMOUNT);
			return "borrow_apply";
		}
		return "borrow_apply_result";
	}

	@RequestMapping("/borrow/apply")
	public String apply(Borrow borrow) {
		borrow.setType(BidConst.BORROW_TYPE_CREDIT);
		service.apply(borrow);
		return "redirect:/borrow/info.do";
	}

	@RequestMapping("/borrow/bid")
	@ResponseBody
	public AjaxResult bid(Long borrowId, BigDecimal amount) {
		AjaxResult result = new AjaxResult();
		// service.bid(borrowId, amount);
		Map<String, Object> map = new HashMap<>();
		map.put("borrowId", borrowId);
		map.put("amount", amount);
		map.put("loginInfoId", HttpSessionContext.getCurrentLoginInfo().getId());
		// 发送消息服
		jmsMessagingTemplate.convertAndSend(bidQueue,map.toString());
		result.setSuccess(true);
		return result;
	}
}
