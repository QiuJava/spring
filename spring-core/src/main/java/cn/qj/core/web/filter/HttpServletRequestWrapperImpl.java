package cn.qj.core.web.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import cn.qj.core.util.StringUtil;

/**
 * 请求包装
 * 
 * @author Qiujian
 * @date 2018/12/18
 */
public class HttpServletRequestWrapperImpl extends HttpServletRequestWrapper {

	public HttpServletRequestWrapperImpl(HttpServletRequest request) {
		super(request);
	}

	@Override
	public String[] getParameterValues(String parameterName) {
		String[] values = super.getParameterValues(parameterName);
		if (values == null) {
			return null;
		}
		int len = values.length;
		String[] newValues = new String[len];
		for (int i = 0; i < len; i++) {
			String newValue = values[i].trim();
			newValue = StringUtil.cleanXSS(newValue);
			newValue = StringUtil.cleanSQL(newValue);
			newValues[i] = newValue;
		}
		return newValues;
	}

}
