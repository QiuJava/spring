package cn.loan.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import cn.loan.core.config.cache.SystemDictionaryHashService;
import cn.loan.core.entity.Borrow;
import cn.loan.core.entity.LoginUser;
import cn.loan.core.entity.UserInfo;
import cn.loan.core.entity.qo.BorrowQo;
import cn.loan.core.entity.qo.CreditFileQo;
import cn.loan.core.service.AccountService;
import cn.loan.core.service.BorrowService;
import cn.loan.core.service.CreditFileService;
import cn.loan.core.service.RealAuthService;
import cn.loan.core.service.UserInfoService;
import cn.loan.core.util.SecurityContextUtil;
import cn.loan.core.util.StringUtil;
import cn.loan.core.util.SystemDictionaryUtil;

/**
 * 投资控制
 * 
 * @author qiujian
 *
 */
@Controller
public class InvestController {

	public static final String INVEST = "invest";
	public static final String INVEST_LIST = "invest_list";
	public static final String BORROW_INFO = "borrow_info";
	public static final String WEBSITE_INVEST_MAPPING = "/website/invest";
	public static final String WEBSITE_INVEST_PAGEQUERY_MAPPING = "/website/invest/pageQuery";
	public static final String WEBSITE_BORROW_INFO_MAPPING = "/website/borrow_info";

	@Autowired
	private BorrowService borrowService;
	@Autowired
	private UserInfoService userInfoService;
	@Autowired
	private RealAuthService realAuthService;
	@Autowired
	private SystemDictionaryHashService systemDictionaryHashService;
	@Autowired
	private CreditFileService creditFileService;
	@Autowired
	private AccountService accountService;

	@GetMapping(WEBSITE_BORROW_INFO_MAPPING)
	public String borrowInfo(Long id, Model model) {
		Borrow borrow = borrowService.get(id);
		Long borrowId = borrow.getBorrower().getId();
		UserInfo userInfo = userInfoService.get(borrowId);
		model.addAttribute(StringUtil.BORROW, borrow);
		model.addAttribute(StringUtil.USER_INFO, userInfo);
		model.addAttribute(StringUtil.REAL_AUTH, realAuthService.get(userInfo.getRealAuthId()));

		CreditFileQo qo = new CreditFileQo();
		qo.setSubmitter(borrow.getBorrower());
		qo.setAuditStatus(SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.AUDIT, SystemDictionaryUtil.AUDIT_PASS,
				systemDictionaryHashService));

		model.addAttribute(StringUtil.CREDIT_FILES, creditFileService.query(qo));

		LoginUser currentUser = SecurityContextUtil.getCurrentUser();
		if (currentUser != null) {
			Long currentId = currentUser.getId();
			if (currentId.equals(borrowId)) {
				model.addAttribute(StringUtil.SELF, true);
			} else {
				model.addAttribute(StringUtil.SELF, false);
				model.addAttribute(StringUtil.ACCOUNT, accountService.get(currentId));
			}
		} else {
			model.addAttribute(StringUtil.SELF, false);
		}
		return BORROW_INFO;
	}

	@GetMapping(WEBSITE_INVEST_MAPPING)
	public String invest() {
		return INVEST;
	}

	@PostMapping(WEBSITE_INVEST_PAGEQUERY_MAPPING)
	public String investList(BorrowQo qo, Model model) {
		// 设置投标人能看到的状态 招标中 已完成
		Integer releaseIn = SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.BORROW_STATUS,
				SystemDictionaryUtil.RELEASE_IN, systemDictionaryHashService);
		Integer payOff = SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.BORROW_STATUS,
				SystemDictionaryUtil.PAY_OFF, systemDictionaryHashService);
		Integer bidFull = SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.BORROW_STATUS,
				SystemDictionaryUtil.BID_FULL, systemDictionaryHashService);
		Integer paymentIn = SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.BORROW_STATUS,
				SystemDictionaryUtil.PAYMENT_IN, systemDictionaryHashService);
		Integer[] statusList = new Integer[] { releaseIn, payOff, bidFull, paymentIn };
		qo.setBorrowStatusList(statusList);
		model.addAttribute(StringUtil.PAGE_RESULT, borrowService.pageQuery(qo));
		return INVEST_LIST;
	}
}
