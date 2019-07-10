package cn.loan.core.util;

import java.io.File;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * 上传工具
 * 
 * @author Qiujian
 * 
 */
public class UploadUtil {
	private UploadUtil() {
	}

	public static final String UPLOAD_IMAGE = "/upload/image/";

	public static String upload(MultipartFile file, String basePath) {
		// 获取上传文件的原始名称
		String orgFileName = file.getOriginalFilename();
		// 使用UUID作为文件的新名称
		StringBuilder builder = new StringBuilder();
		builder.append(UUID.randomUUID().toString()).append(StringUtil.DOT)
				.append(FilenameUtils.getExtension(orgFileName));
		String fileName = builder.toString();
		// 保存到公共的绝对路径 或者是文件服务器
		File copyFile = new File(basePath, fileName);
		try {
			FileUtils.writeByteArrayToFile(copyFile, file.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		StringBuilder sb = new StringBuilder();
		return sb.append(UPLOAD_IMAGE).append(fileName).toString();
	}

}
