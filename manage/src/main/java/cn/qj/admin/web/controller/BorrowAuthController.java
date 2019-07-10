package cn.qj.admin.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.qj.core.common.BaseResult;
import cn.qj.core.common.PageResult;
import cn.qj.core.consts.BidConst;
import cn.qj.core.consts.StatusConst;
import cn.qj.core.entity.Borrow;
import cn.qj.core.entity.UserInfo;
import cn.qj.core.pojo.qo.BorrowQo;
import cn.qj.core.pojo.qo.UserFileQo;
import cn.qj.core.service.BorrowService;
import cn.qj.core.service.RealAuthService;
import cn.qj.core.service.UserFileService;
import cn.qj.core.service.UserInfoService;

/**
 * 借款审核控制器
 * 
 * @author Qiujian
 * @date 2018/11/01
 */
@Controller
@RequestMapping("/borrow")
public class BorrowAuthController {

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
		return "borrow/publish";
	}

	@RequestMapping("/publish/audit")
	@ResponseBody
	public BaseResult publishAudit(Long id, int state, String remark) {
		service.publishAudit(id, state, remark);
		return BaseResult.ok("发标前审核成功", null);
	}

	@RequestMapping("/audit1")
	public String audit1(@ModelAttribute("qo") BorrowQo qo, Model model) {
		qo.setBorrowState(BidConst.BORROW_STATE_APPROVE_PENDING_1);
		model.addAttribute("pageResult", service.list(qo));
		return "borrow/audit1";
	}

	@RequestMapping("/audit1/audit")
	@ResponseBody
	public BaseResult audit1Audit(Long id, String remark, int state) {
		service.audit1Audit(id, remark, state);
		return BaseResult.ok("满标一审成功", null);
	}

	@RequestMapping("/audit2")
	public String audit2(BorrowQo qo, Model model) {
		qo.setBorrowState(BidConst.BORROW_STATE_APPROVE_PENDING_2);
		PageResult pageResult = service.list(qo);
		model.addAttribute("pageResult", pageResult);
		return "borrow/audit2";
	}

	@RequestMapping("/audit2/audit")
	@ResponseBody
	public BaseResult audit2Audit(Long id, String remark, int state) {
		service.audit2Audit(id, remark, state);
		return BaseResult.ok("满标二审成功", null);
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
		qo.setState(StatusConst.AUTH_PASS);
		qo.setLoginInfoId(borrow.getCreateUser().getId());
		model.addAttribute("userFiles", userFileService.page(qo).getContent());
		return "borrow/info";
	}
}
