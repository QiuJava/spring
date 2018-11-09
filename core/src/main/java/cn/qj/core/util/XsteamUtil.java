package cn.qj.core.util;

import com.thoughtworks.xstream.XStream;

/**
 * Xstream工具
 * 
 * 
 * 
 * @author Qiujian
 * @date 2018/11/09
 */
public class XsteamUtil {
	private XsteamUtil() {
	}

	public static Object toBean(Class<?> clazz, String xml) {
		Object xmlObject = null;
		XStream xstream = new XStream();
		xstream.processAnnotations(clazz);
		xstream.autodetectAnnotations(true);
		xmlObject = xstream.fromXML(xml);
		return xmlObject;
	}
}
