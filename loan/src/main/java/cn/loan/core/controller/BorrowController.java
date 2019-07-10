package cn.loan.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.loan.core.common.BaseResult;
import cn.loan.core.config.cache.SystemDictionaryHashService;
import cn.loan.core.entity.Borrow;
import cn.loan.core.entity.LoginUser;
import cn.loan.core.entity.SystemDictionary;
import cn.loan.core.entity.UserInfo;
import cn.loan.core.entity.qo.BorrowQo;
import cn.loan.core.entity.qo.CreditFileQo;
import cn.loan.core.service.AccountService;
import cn.loan.core.service.BorrowAuditHistroyService;
import cn.loan.core.service.BorrowService;
import cn.loan.core.service.CreditFileService;
import cn.loan.core.service.RealAuthService;
import cn.loan.core.service.UserInfoService;
import cn.loan.core.util.SecurityContextUtil;
import cn.loan.core.util.StringUtil;
import cn.loan.core.util.SystemDictionaryUtil;

/**
 * 借款控制器
 * 
 * @author Qiujian
 * 
 */
@Controller
public class BorrowController {
	public static final String BORROW = "borrow";
	public static final String BORROW_INFO = "borrow/info";
	public static final String BORROW_FULL = "borrow/full";
	public static final String BORROW_APPLY = "borrow_apply";
	public static final String BORROW_APPLY_RESULT = "borrow_apply_result";
	public static final String BORROW_STATIC = "borrow_static";
	public static final String BORROW_URL_PUBLISH = "borrow/publish";
	public static final String WEBSITE_BORROW_MAPPING = "/website/borrow";
	public static final String WEBSITE_BORROW_APPLY_MAPPING = "/website/borrow/apply";
	public static final String REDIRECT_WEBSITE_BORROW_APPLY = "redirect:/website/borrow/apply";
	public static final String MANAGE_BORROW_PUBLISH_MAPPING = "/manage/borrow/publish";
	public static final String MANAGE_BORROW_INFO_MAPPING = "/manage/borrow/info";
	public static final String MANAGE_BORROW_PUBLISH_AUDIT_MAPPING = "/manage/borrow/publish_audit";
	public static final String MANAGE_BORROW_FULL_MAPPING = "/manage/borrow/full";
	public static final String MANAGE_BORROW_FULL_AUDIT_MAPPING = "/manage/borrow/full_audit";

	@Autowired
	private UserInfoService userInfoService;
	@Autowired
	private SystemDictionaryHashService systemDictionaryHashService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private BorrowService borrowService;
	@Autowired
	private RealAuthService realAuthService;
	@Autowired
	private CreditFileService creditFileService;
	@Autowired
	private BorrowAuditHistroyService borrowAuditHistroyService;

	@GetMapping(WEBSITE_BORROW_MAPPING)
	public String borrow(Model model) {
		LoginUser currentUser = SecurityContextUtil.getCurrentUser();
		if (currentUser == null) {
			return BORROW_STATIC;
		}
		model.addAttribute(StringUtil.USER_INFO, userInfoService.get(currentUser.getId()));
		model.addAttribute(StringUtil.CREDIT_BORROW_SCORE, SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.INIT,
				SystemDictionaryUtil.INIT_CREDIT_SCORE, systemDictionaryHashService));
		model.addAttribute(StringUtil.INIT_BORROW_LIMIT, SystemDictionaryUtil.getItemValue(
				SystemDictionaryUtil.INIT_BORROW_LIMIT, systemDictionaryHashService.get(SystemDictionaryUtil.INIT)));
		return BORROW;
	}

	@GetMapping(WEBSITE_BORROW_APPLY_MAPPING)
	public String apply(Model model) {
		// 是否有借款在申请中
		if (!userInfoService.getCurrent().isBorrowProcess()) {
			// 设置可解金额
			model.addAttribute(StringUtil.ACCOUNT, accountService.getCurrent());
			SystemDictionary init = systemDictionaryHashService.get(SystemDictionaryUtil.INIT);
			model.addAttribute(StringUtil.MIN_BORROW_AMOUNT,
					SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.INIT_MIN_BORROW_AMOUNT, init));
			model.addAttribute(StringUtil.MIN_BID_AMOUNT,
					SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.INIT_MIN_BID_AMOUNT, init));
			return BORROW_APPLY;
		}
		return BORROW_APPLY_RESULT;
	}

	@PostMapping(WEBSITE_BORROW_APPLY_MAPPING)
	public String borrowApply(Borrow borrow) {
		borrowService.apply(borrow);
		return REDIRECT_WEBSITE_BORROW_APPLY;
	}

	@GetMapping(MANAGE_BORROW_PUBLISH_MAPPING)
	public String publish(@ModelAttribute(StringUtil.QO) BorrowQo qo, Model model) {
		Integer releaseBeforeStatus = SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.BORROW_STATUS,
				SystemDictionaryUtil.RELEASE_BEFORE, systemDictionaryHashService);
		qo.setBorrowStatus(releaseBeforeStatus);
		model.addAttribute(StringUtil.PAGE_RESULT, borrowService.pageQuery(qo));
		return BORROW_URL_PUBLISH;
	}

	@GetMapping(MANAGE_BORROW_INFO_MAPPING)
	public String borrowInfo(Long id, Model model) {
		Borrow borrow = borrowService.get(id);
		// 上传借款对象
		model.addAttribute(StringUtil.BORROW, borrow);
		UserInfo userInfo = userInfoService.get(borrow.getBorrower().getId());
		model.addAttribute(StringUtil.USER_INFO, userInfo);
		// 查询出所有已审核的历史
		model.addAttribute(StringUtil.BORROW_AUDIT_HISTROY_LIST, borrowAuditHistroyService.listByBorrowId(id));
		model.addAttribute(StringUtil.REAL_AUTH, realAuthService.get(userInfo.getRealAuthId()));
		CreditFileQo qo = new CreditFileQo();
		qo.setAuditStatus(SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.AUDIT, SystemDictionaryUtil.AUDIT_PASS,
				systemDictionaryHashService));

		qo.setSubmitter(borrow.getBorrower());
		model.addAttribute(StringUtil.CREDIT_FILES, creditFileService.query(qo));
		return BORROW_INFO;
	}

	@PostMapping(MANAGE_BORROW_PUBLISH_AUDIT_MAPPING)
	@ResponseBody
	public BaseResult publishAudit(Long id, Integer auditStatus, String remark) {
		borrowService.publishAudit(id, auditStatus, remark);
		return BaseResult.ok(StringUtil.EMPTY);
	}

	@GetMapping(MANAGE_BORROW_FULL_MAPPING)
	public String full(@ModelAttribute(StringUtil.QO) BorrowQo qo, Model model) {
		Integer bidFull = SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.BORROW_STATUS,
				SystemDictionaryUtil.BID_FULL, systemDictionaryHashService);
		qo.setBorrowStatus(bidFull);
		model.addAttribute(StringUtil.PAGE_RESULT, borrowService.pageQuery(qo));
		return BORROW_FULL;
	}

	@PostMapping(MANAGE_BORROW_FULL_AUDIT_MAPPING)
	@ResponseBody
	public BaseResult fullAudit(Long id, String remark, Integer auditStatus) {
		borrowService.fullAudit(id, remark, auditStatus);
		return BaseResult.ok(StringUtil.EMPTY);
	}

}
