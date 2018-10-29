package cn.pay.loan.web.controller;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.pay.core.consts.BidConst;
import cn.pay.core.entity.business.Borrow;
import cn.pay.core.entity.business.UserFile;
import cn.pay.core.entity.business.UserInfo;
import cn.pay.core.entity.sys.LoginInfo;
import cn.pay.core.pojo.qo.BorrowQo;
import cn.pay.core.pojo.qo.UserFileQo;
import cn.pay.core.service.AccountService;
import cn.pay.core.service.BorrowService;
import cn.pay.core.service.IndexService;
import cn.pay.core.service.RealAuthService;
import cn.pay.core.service.UserFileService;
import cn.pay.core.service.UserInfoService;
import cn.pay.core.util.HttpServletContext;

/**
 * 网站首页相关
 * 
 * @author Administrator
 * 
 */
@Controller
public class HomeController {
	public static final String MAIN = "main";
	public static final String INVERST = "invest";
	public static final String INVERST_LIST = "invest_list";
	public static final String BORROW_INFO = "borrow_info";

	@Autowired
	private BorrowService borrowService;
	@Autowired
	private UserInfoService userInfoService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private RealAuthService realAuthService;
	@Autowired
	private UserFileService userFileService;
	@Autowired
	private IndexService service;

	/**
	 * 前台系统首页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/home")
	public String index(Model model) {
		// 页面显示 首页需要显示的状态 投标中 还款中 已完成
		BorrowQo qo = new BorrowQo();
		qo.setStatus(Arrays.asList(new Integer[] { BidConst.BORROW_STATE_BIDDING, BidConst.BORROW_STATE_PAYING_BACK,
				BidConst.BORROW_STATE_COMPLETE_PAY_BACK }));
		qo.setOrderBy("state");
		qo.setOrderType(Direction.ASC);
		model.addAttribute("borrowList", borrowService.list(qo).getContent());
		// 添加统计数据
		model.addAttribute("summaryVO", service.getIndexSummaryVO());
		return MAIN;
	}
	
	/**
	 * 借款信息
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/borrow_info")
	public String borrowInfo(Long id, Model model) {
		Borrow borrow = borrowService.get(id);
		UserInfo userInfo = userInfoService.get(borrow.getCreateUser().getId());
		model.addAttribute("borrow", borrow);
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("realAuth", realAuthService.get(userInfo.getRealAuthId()));

		UserFileQo qo = new UserFileQo();
		qo.setLoginInfoId(borrow.getCreateUser().getId());
		qo.setState(UserFile.AUTH_PASS);

		model.addAttribute("userFiles", userFileService.page(qo).getContent());

		LoginInfo loginInfo = HttpServletContext.getCurrentLoginInfo();
		if (loginInfo != null) {
			Long currentId = loginInfo.getId();
			if (currentId.equals(borrow.getCreateUser().getId())) {
				model.addAttribute("self", true);
			} else {
				model.addAttribute("self", false);
				model.addAttribute("account", accountService.get(loginInfo.getId()));
			}
		} else {
			model.addAttribute("self", false);
		}
		return BORROW_INFO;
	}

	/**
	 * 进入到投标界面
	 */
	@RequestMapping("/invest")
	public String invest() {
		return INVERST;
	}

	/**
	 * 投资列表的里面的内容
	 */
	@RequestMapping("/invest/list")
	public String investList(BorrowQo qo, Model model) {
		// 设置投标人能看到的状态 招标中
		Integer[] status = new Integer[] { BidConst.BORROW_STATE_BIDDING, BidConst.BORROW_STATE_APPROVE_PENDING_1,
				BidConst.BORROW_STATE_APPROVE_PENDING_2, BidConst.BORROW_STATE_PAYING_BACK,
				BidConst.BORROW_STATE_COMPLETE_PAY_BACK };
		qo.setStatus(Arrays.asList(status));
		model.addAttribute("pageResult", borrowService.list(qo));
		return INVERST_LIST;
	}
}
