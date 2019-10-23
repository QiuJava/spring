package cn.eeepay.framework.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 生产md5
 * 
 * @author dj
 * 
 */
public class Md5 {
	private static final Logger log = LoggerFactory.getLogger(Md5.class);
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
			log.error("异常:",e);
		} catch (UnsupportedEncodingException e) {
			log.error("异常:",e);
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
	
	
	
	 public static String getMd5ByFile(File file) {
    String value = "";
    FileInputStream in=null;
    try {
      in = new FileInputStream(file);
      MappedByteBuffer byteBuffer = in.getChannel().map(
          FileChannel.MapMode.READ_ONLY, 0, file.length());
      MessageDigest md5 = MessageDigest.getInstance("MD5");
      md5.update(byteBuffer);
      BigInteger bi = new BigInteger(1, md5.digest());
      value = bi.toString(16);
    } catch (Exception e) {
      log.error("异常:",e);
    } finally {
      if (null != in) {
        try {
          in.close();
        } catch (IOException e) {
          log.error("异常:",e);
        }
      }
    }
    return value;
 }
	 
	 
	 public static void main(String[] args) {
     String v = Md5.getMd5ByFile(new File("C:/Users/Administrator/Desktop/settleTest/teststestst.xls"));
     String v2 = Md5.getMd5ByFile(new File("C:/Users/Administrator/Desktop/settleTest/teststestst"));
     System.out.println(v.toUpperCase());
     System.out.println(v2.toUpperCase());
 }
	 
}
