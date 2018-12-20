package cn.qj.key;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import cn.qj.key.util.BaseResult;
import cn.qj.key.util.LogicException;

/**
 * 异常处理控制器
 * 
 * @author Qiujian
 * @date 2018/10/30
 */
@RestControllerAdvice
public class ExceptionHandlerController {

	private static Logger logger = LoggerFactory.getLogger(ExceptionHandlerController.class);

	@ExceptionHandler(LogicException.class)
	public BaseResult handlerLogicException(LogicException e) {
		logger.info(e.getMessage() + ":" + e.getErrCode());
		return new BaseResult(false, e.getMessage(), e.getErrCode());
	}

	@ExceptionHandler(Exception.class)
	public BaseResult handlerException(Exception e) {
		logger.error(e.getMessage(), e);
		return new BaseResult(false, "系统异常");
	}

}
