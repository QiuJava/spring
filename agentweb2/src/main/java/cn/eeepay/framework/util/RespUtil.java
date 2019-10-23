package cn.eeepay.framework.util;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

/**
 * Response工具类
 * @author yangle
 *
 */
public class RespUtil {
	public static final Logger log = LoggerFactory.getLogger(RespUtil.class);
	public static void renderText(HttpServletResponse response, String text) {
		render(response, "text/plain;charset=UTF-8", text);
	}

	public static void renderJson(HttpServletResponse response, String text) {
		render(response, "application/json;charset=UTF-8", text);
	}

	public static void renderXml(HttpServletResponse response, String text) {
		render(response, "text/xml;charset=UTF-8", text);
	}

	public static void render(HttpServletResponse response, String contentType,
			String text) {
		response.setContentType(contentType);
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0L);
		try {
			response.getWriter().write(text);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}

	public static void renderJson(HttpServletResponse response,
			Map<String, Object> data) {
		JSONObject json = new JSONObject();
		try {
			json.putAll(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		renderJson(response, json.toString());

		System.out.println("json=" + json.toString());
	}

	public static void renderJson(HttpServletResponse response, Object data) {
		JSONObject json = new JSONObject();
		try {
			json.put("result", data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		renderJson(response, json.toString());
	}
}
