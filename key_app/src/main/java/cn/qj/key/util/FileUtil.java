package cn.qj.key.util;

import java.io.File;
import java.util.Scanner;

/**
 * 文件操作工具
 * 
 * @author Qiujian
 * @date 2019/01/30
 */
public class FileUtil {

	private FileUtil() {
	}

	public static String jsonRead(File file) {
		Scanner scanner = null;
		StringBuilder buffer = new StringBuilder();
		try {
			scanner = new Scanner(file, "utf-8");
			while (scanner.hasNextLine()) {
				buffer.append(scanner.nextLine());
			}
		} catch (Exception e) {
			throw new LogicException("json 文件读取异常");
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}
		return buffer.toString();
	}
}
