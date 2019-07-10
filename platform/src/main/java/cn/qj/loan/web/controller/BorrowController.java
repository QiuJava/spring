package cn.qj.loan.web.controller;

import java.math.BigDecimal;

import javax.jms.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.qj.core.common.BaseResult;
import cn.qj.core.consts.BidConst;
import cn.qj.core.entity.Borrow;
import cn.qj.core.entity.LoginInfo;
import cn.qj.core.pojo.dto.BidDto;
import cn.qj.core.service.AccountService;
import cn.qj.core.service.BorrowService;
import cn.qj.core.service.UserInfoService;
import cn.qj.core.util.HttpSessionUtil;

/**
 * 借款控制器
 * 
 * @author Qiujian
 * @date 2018/11/01
 */
@Controller
public class BorrowController {

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
		LoginInfo current = HttpSessionUtil.getCurrentLoginInfo();
		// 如果当前用户没有登录应该直接导向到静态页面
		if (current == null) {
			return "redirect:/borrow.html";
		}
		model.addAttribute("userInfo", userInfoService.get(current.getId()));
		model.addAttribute("creditBorrowScore", BidConst.BORROW_CREDIT_SCORE);
		model.addAttribute("initBorrowLimit", BidConst.INIT_BORROW_LIMIT);
		return "borrow";
	}

	@RequestMapping("/borrow/info")
	public String info(Model model) {
		if (service.isApplyBorrow()) {
			// 查询用户账户信息
			model.addAttribute("account", accountService.get(HttpSessionUtil.getCurrentLoginInfo().getId()));
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
	public BaseResult bid(Long borrowId, BigDecimal amount) {
		// service.bid(borrowId, amount);
		BidDto dto = new BidDto();
		dto.setBorrowId(borrowId);
		dto.setAmount(amount);
		dto.setLoginInfoId(HttpSessionUtil.getCurrentLoginInfo().getId());
		// 发送消息服
		jmsMessagingTemplate.convertAndSend(bidQueue, JSON.toJSONString(dto));
		return BaseResult.ok("投标成功", null);
	}
	
}
