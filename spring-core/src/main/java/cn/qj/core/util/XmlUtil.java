package cn.qj.core.util;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import cn.qj.core.common.LogicException;

/**
 * xml工具
 * 
 * @author Qiujian
 * @date 2019年3月26日
 *
 */
public class XmlUtil {
	private XmlUtil() {
	}

	public static String toXml(Object obj) {
		StringWriter writer = new StringWriter();
		try {
			JAXBContext context = JAXBContext.newInstance(obj.getClass());
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshaller.marshal(obj, writer);
		} catch (JAXBException e) {
			e.printStackTrace();
			throw new LogicException();
		}
		return writer.toString();
	}

	public static Object toObject(String xml, Class<?> clz) {
		Object xmlObject = null;
		try {
			JAXBContext context = JAXBContext.newInstance(clz);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			StringReader reader = new StringReader(xml);
			xmlObject = unmarshaller.unmarshal(reader);
		} catch (JAXBException e) {
			e.printStackTrace();
			throw new LogicException();
		}
		return xmlObject;
	}
}
