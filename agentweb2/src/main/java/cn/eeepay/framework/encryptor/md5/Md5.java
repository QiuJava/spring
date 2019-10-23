package cn.eeepay.framework.encryptor.md5;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 生产md5
 * 
 * @author dj
 * 
 */
public class Md5 {
	/**
	 * 根据明文生成md5密文
	 * 
	 * @param str
	 *            要加密的明文
	 * @return md5密文
	 */
	public static String md5Str(String str) {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		byte[] byteArray = messageDigest.digest();

		StringBuffer md5StrBuff = new StringBuffer();

		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(
						Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}

		return md5StrBuff.toString();
	}

	public static String MD5Encode(String strSrc) {
		return MD5Encode(strSrc, "",null);
	}
	/**
	 * 27 md5加密产生，产生128位（bit）的mac 28 将128bit Mac转换成16进制代码 29
	 *
	 * @param strSrc
	 *            30
	 * @param key
	 *            31
	 * @return 32
	 */
	public static String MD5Encode(String strSrc, String key, String charset) {
		try {
			if(StringUtils.isBlank(charset))
				charset="UTF-8";
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(strSrc.getBytes(charset));
			StringBuilder result = new StringBuilder(32);
			byte[] temp;
			temp = md5.digest(key.getBytes(charset));
			for (int i = 0; i < temp.length; i++) {
				result.append(Integer.toHexString(
						(0x000000ff & temp[i]) | 0xffffff00).substring(6));
			}
			return result.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}
