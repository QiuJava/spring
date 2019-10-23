package cn.eeepay.boss.util;

import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ConfigUtil {
	
	private static final Logger log = LoggerFactory.getLogger(ConfigUtil.class);
	
	public static String getConfig(String config) {
		return getConfig("config.properties", config);
	}

	public static String getConfig(String configfile, String config) {
		InputStream inputstream = ConfigUtil.class.getResourceAsStream("/"
				+ configfile);
		Properties properties = new Properties();
		try {
			properties.load(inputstream);
		} catch (Exception e) {
			log.error("异常:",e);
		}

		String s = properties.getProperty(config, null);
		return s;
	}
}
