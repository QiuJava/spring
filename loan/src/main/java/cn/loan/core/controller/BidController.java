package cn.loan.core.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.loan.core.common.BaseResult;
import cn.loan.core.service.BidService;
import cn.loan.core.util.StringUtil;

/**
 * 投标控制
 * 
 * @author qiujian
 *
 */
@Controller
public class BidController {

	public static final String WEBSITE_BORROW_BID_MAPPING = "/website/borrow/bid";

	@Autowired
	private BidService bidService;

	@PostMapping(WEBSITE_BORROW_BID_MAPPING)
	@ResponseBody
	public BaseResult bid(Long borrowId, BigDecimal amount) {
		bidService.bid(borrowId, amount);
		return BaseResult.ok(StringUtil.EMPTY);
	}

}
