package bossweb.bky;

import java.security.MessageDigest;

public class MD5
{
  private static final String ALGORITHM = "MD5";
  private static final char[] HEX_DIGITS = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102 };
  
  public static String encode(String paramString1, String paramString2)
  {
    if (paramString2 == null) {
      return null;
    }
    try
    {
      MessageDigest localMessageDigest = MessageDigest.getInstance(paramString1);
      localMessageDigest.update(paramString2.getBytes());
      String str = getFormattedText(localMessageDigest.digest());
      return str;
    }
    catch (Exception localException)
    {
      throw new RuntimeException(localException);
    }
  }
  
  public static String encodeByMD5(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    try
    {
      MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
      localMessageDigest.update(paramString.getBytes());
      String str = getFormattedText(localMessageDigest.digest());
      return str;
    }
    catch (Exception localException)
    {
      throw new RuntimeException(localException);
    }
  }
  
  private static String getFormattedText(byte[] paramArrayOfByte)
  {
    int i = paramArrayOfByte.length;
    StringBuilder localStringBuilder = new StringBuilder(i * 2);
    for (int j = 0; j < i; j++)
    {
      localStringBuilder.append(HEX_DIGITS[(0xF & paramArrayOfByte[j] >> 4)]);
      localStringBuilder.append(HEX_DIGITS[(0xF & paramArrayOfByte[j])]);
    }
    return localStringBuilder.toString();
  }
}