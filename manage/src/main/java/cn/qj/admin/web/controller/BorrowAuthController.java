package cn.qj.admin.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.qj.core.consts.BidConst;
import cn.qj.core.entity.Borrow;
import cn.qj.core.entity.UserFile;
import cn.qj.core.entity.UserInfo;
import cn.qj.core.pojo.qo.BorrowQo;
import cn.qj.core.pojo.qo.UserFileQo;
import cn.qj.core.pojo.vo.AjaxResult;
import cn.qj.core.pojo.vo.PageResult;
import cn.qj.core.service.BorrowService;
import cn.qj.core.service.RealAuthService;
import cn.qj.core.service.UserFileService;
import cn.qj.core.service.UserInfoService;

/**
 * 借款审核相关
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/borrow")
public class BorrowAuthController {
	public static final String AUDIT_1 = "borrow/audit1";
	public static final String AUDIT_2 = "borrow/audit2";
	public static final String PUBLISH_AUDIT = "borrow/publish";

	@Autowired
	private BorrowService service;
	@Autowired
	private UserInfoService userInfoService;
	@Autowired
	private RealAuthService realAuthService;
	@Autowired
	private UserFileService userFileService;

	/**
	 * 发标前审核
	 * 
	 * @param qo
	 * @param model
	 * @return
	 */
	@RequestMapping("/publish")
	public String publish(@ModelAttribute("qo") BorrowQo qo, Model model) {
		// 设置借款类型
		qo.setBorrowState(BidConst.BORROW_STATE_PUBLISH_PENDING);
		PageResult pageResult = service.list(qo);
		model.addAttribute("pageResult", pageResult);
		return PUBLISH_AUDIT;
	}

	@RequestMapping("/publish/audit")
	@ResponseBody
	public AjaxResult publishAudit(Long id, int state, String remark) {
		AjaxResult result = new AjaxResult();
		service.publishAudit(id, state, remark);
		result.setSuccess(true);
		return result;
	}

	@RequestMapping("/audit1")
	public String audit1(@ModelAttribute("qo") BorrowQo qo, Model model) {
		qo.setBorrowState(BidConst.BORROW_STATE_APPROVE_PENDING_1);
		model.addAttribute("pageResult", service.list(qo));
		return AUDIT_1;
	}

	@RequestMapping("/audit1/audit")
	@ResponseBody
	public AjaxResult audit1Audit(Long id, String remark, int state) {
		AjaxResult result = new AjaxResult();
		service.audit1Audit(id, remark, state);
		result.setSuccess(true);
		return result;
	}

	@RequestMapping("/audit2")
	public String audit2(BorrowQo qo, Model model) {
		qo.setBorrowState(BidConst.BORROW_STATE_APPROVE_PENDING_2);
		PageResult pageResult = service.list(qo);
		model.addAttribute("pageResult", pageResult);
		return AUDIT_2;
	}

	@RequestMapping("/audit2/audit")
	@ResponseBody
	public AjaxResult audit2Audit(Long id, String remark, int state) {
		AjaxResult result = new AjaxResult();
		service.audit2Audit(id, remark, state);
		result.setSuccess(true);
		return result;
	}

	@RequestMapping("/info")
	public String borrowInfo(Long id, Model model) {
		Borrow borrow = service.get(id);
		// 上传借款对象
		model.addAttribute("borrow", borrow);
		// 拿到借款人的id
		UserInfo userInfo = userInfoService.get(borrow.getCreateUser().getId());
		model.addAttribute("userInfo", userInfoService.get(borrow.getCreateUser().getId()));
		// 查询出所有已审核的历史
		model.addAttribute("authHistroyList", service.getAuthHistroys(id));
		model.addAttribute("realAuth", realAuthService.get(userInfo.getRealAuthId()));
		UserFileQo qo = new UserFileQo();
		qo.setState(UserFile.AUTH_PASS);
		qo.setLoginInfoId(borrow.getCreateUser().getId());
		model.addAttribute("userFiles", userFileService.page(qo).getContent());
		return "borrow/info";
	}
}
