package cn.eeepay.boss.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DownloadUtil {
	private static final Logger log = LoggerFactory.getLogger(DownloadUtil.class);
	
	/**
	 * 下载文件
	 * @param response 输出流对象
	 * @param path 文件路径
	 * @param fileName 下载文件名称
	 */
	public static void download(HttpServletResponse response, String path,String fileName) {
		File f = new File(path);
		download(response, f,fileName);
	}

	/**
	 * 下载文件
	 * @param response 输出流对象
	 * @param file 文件
	 */
	public static void download(HttpServletResponse response, File file,String fileName) {
		BufferedInputStream br = null;
		OutputStream out = null;
		try {
			File f = file;
			if (!f.exists()) {
				response.sendError(404, "File not found!");
				return;
			}
			br = new BufferedInputStream(new FileInputStream(f));
			byte[] buf = new byte[1024];
			int len = 0;

			response.reset();
			if(StringUtils.isBlank(fileName))
				fileName=f.getName();
			fileName = new String(fileName.getBytes("gb2312"), "ISO8859-1");
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
		    response.setHeader( "Content-Disposition", "attachment;filename=" + fileName);  
			out = response.getOutputStream();
			while ((len = br.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			br.close();
			out.flush();
			out.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				log.error("异常:",e);
			}

		}
	}

}
