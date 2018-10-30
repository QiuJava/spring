package cn.qj.loan.web.pay;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.qj.core.pojo.vo.AjaxResult;

/**
 * 微信支付
 * 
 * @author Qiujian
 * @date 2018/10/30
 */
@RestController
public class WeChatPayController {

	@GetMapping("/pay/wechat")
	public AjaxResult pay() {
		AjaxResult result = new AjaxResult(true, "支付成功");
		
		
		
		return result;
	}

}
