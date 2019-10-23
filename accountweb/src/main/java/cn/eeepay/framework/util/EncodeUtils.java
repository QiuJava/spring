package cn.eeepay.framework.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

public class EncodeUtils {

  /**
   * Hex编码.
   */
  public static String hexEncode(byte[] input) {
    return Hex.encodeHexString(input);
  }

  /**
   * Hex解码.
   */
  public static byte[] hexDecode(String input) {
    try {
      return Hex.decodeHex(input.toCharArray());
    } catch (DecoderException e) {
      throw new IllegalStateException("Hex Decoder exception", e);
    }
  }

  /**
   * Base64编码.
   */
  public static String base64Encode(byte[] input) {
    return new String(Base64.encodeBase64(input));
  }

  /**
   * Base64编码, URL安全(将Base64中的URL非法字符如+,/=转为其他字符, 见RFC3548).
   */
  public static String base64UrlSafeEncode(byte[] input) {
    return Base64.encodeBase64URLSafeString(input);
  }

  /**
   * Base64解码.
   */
  public static byte[] base64Decode(String input) {
    return Base64.decodeBase64(input);
  }





  

	/** 解码接压缩 **/
	public static byte[] decodeInflate(String raw) throws IOException {
		if (raw == null || raw.length() == 0) {
			throw new IllegalArgumentException("Decode and inflate error with null raw byte[]");
		}
		byte[] tmpByte = Base64.decodeBase64(raw.getBytes());
		return inflater(tmpByte);
	}

	private static byte[] inflater(byte[] inputByte) throws IOException {
		int compressedDataLength = 0;
		Inflater compresser = new Inflater(false);
		compresser.setInput(inputByte, 0, inputByte.length);
		ByteArrayOutputStream o = new ByteArrayOutputStream(inputByte.length);
		byte[] result = new byte[1024];
		try {
			while (!compresser.finished()) {
				compressedDataLength = compresser.inflate(result);
				if (compressedDataLength == 0) {
					break;
				}
				o.write(result, 0, compressedDataLength);
			}
		} catch (DataFormatException e) {
			throw new IOException("decompress catch data format exception",e);
		}  finally {
			o.close();
		}
		compresser.end();
		return o.toByteArray();
	}
	

}
