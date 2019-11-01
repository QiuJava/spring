package com.example.util;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.example.common.LogicException;

/**
 * xml工具
 * 
 * @author Qiu Jian
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
			throw new LogicException(e.getMessage());
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
			throw new LogicException(e.getMessage());
		}
		return xmlObject;
	}
}
