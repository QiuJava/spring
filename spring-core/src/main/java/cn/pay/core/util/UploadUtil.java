package cn.pay.core.util;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * 上传工具类
 * 
 * @author Qiujian
 *
 */
public class UploadUtil {
	private UploadUtil() {
	}

	public static String upload(MultipartFile file, String basePath) throws IOException {
		// 获取上传文件的原始名称
		String orgFileName = file.getOriginalFilename();
		// 使用UUID作为文件的新名称
		StringBuilder builder = new StringBuilder();
		builder.append(UUID.randomUUID().toString()).append(".").append(FilenameUtils.getExtension(orgFileName));
		String fileName = builder.toString();
		// 保存到公共的绝对路径 或者是文件服务器
		File copyFile = new File(basePath, fileName);
		FileUtils.writeByteArrayToFile(copyFile, file.getBytes());
		return fileName;
	}
}
