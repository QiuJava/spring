package cn.loan.core.common;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.loan.core.util.StringUtil;

/**
 * 异常控制器
 * 
 * @author qiujian
 *
 */
@ControllerAdvice
public class ExceptionController {

	@ExceptionHandler(LogicException.class)
	@ResponseBody
	public BaseResult logicExceptionHandler(LogicException e) {
		return BaseResult.err(e.getMessage(), StringUtil.EMPTY);
	}

}
