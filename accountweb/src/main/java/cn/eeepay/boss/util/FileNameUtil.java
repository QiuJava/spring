package cn.eeepay.boss.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 文件名工具类
 * @author yangle
 *
 */
public class FileNameUtil {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	
	//F03_20160810_001.xls
	public static String exportOutBill(String type, int i) {
		String current = sdf.format(new Date());
		StringBuilder sb = new StringBuilder();
		String number = "";
		switch(type) {
		case Constants.NEWEPTOK:
			number = String.format("%03d", i);
			sb.append("F03_");
			sb.append(current);
			sb.append("_");
			sb.append(number);
			break;
		case "ZF_ZQ":
			number = String.format("%03d", i);
			sb.append("ZF_ZQ_");
			sb.append(current);
			sb.append("_");
			sb.append(number);
			break;
		case "ZFYL_ZQ":
			number = String.format("%03d", i);
			sb.append("ZFYL_ZQ_");
			sb.append(current);
			sb.append("_");
			sb.append(number);
			break;
		default:
			break;
		}
		
		return sb.toString();
	}
	
	public static String getDateDic() {
		return sdf.format(new Date());
	}
	
	public static String getDateByString(String dateStr) {
		String date = sdf.format(dateStr);
		return date;
	}
}
