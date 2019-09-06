package cn.loan.core.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.loan.core.common.ApiResult;
import cn.loan.core.service.LoginLogService;

/**
 * 登录日志接口
 *
 * @author Qiujian
 *
 */
@RestController
public class LoginLogApi {

	private static final Logger log = LoggerFactory.getLogger(LoginLogApi.class);

	@Autowired
	private LoginLogService loginLogService;

	@GetMapping("/api/loginLog")
	public ApiResult loginLog() {
		ApiResult result = new ApiResult();
		try {
			result.setSucceed(true);
			result.setData(loginLogService.getAll());
		} catch (Exception e) {
			result.setSucceed(false);
			log.error(e.getMessage(), e);
		}
		return result;
	}

}
