package cn.pay.loan.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import cn.pay.core.pojo.vo.AjaxResult;
import cn.pay.core.util.LogicException;

/**
 * 系统异常处理相关
 * 
 * @author Qiujian
 *
 */
@RestControllerAdvice
public class ExceptionHandlerController {

	private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerController.class);

	@ExceptionHandler(LogicException.class)
	public AjaxResult handlerLogicException(LogicException e, Model model) {
		logger.error("逻辑异常", e);
		return new AjaxResult(false, e.getMessage(), e.getErrCode());
	}

	@ExceptionHandler(Exception.class)
	public AjaxResult handlerException(Exception e, Model model) {
		logger.error("系统异常", e);
		return new AjaxResult(false, e.getMessage());
	}

}
