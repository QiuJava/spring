//package cn.eeepay.boss.util;
//
//import java.io.UnsupportedEncodingException;
//import java.net.URLDecoder;
//import java.net.URLEncoder;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//
///**
// * 加密解密工具类
// * by zouruijin
// * email rjzou@qq.com zrj@eeepay.cn
// * 2016年6月7日11:44:13
// *
// */
//public class Encrypt {
//    private static final Log log = LogFactory.getLog(Encrypt.class);
//    
//    /***
//     * 加密密钥
//     */
//    final static byte[] keyBytes = { 0x11, 0x22, 0x4F, 0x58, (byte) 0x88, 0x09,
//            0x40, 0x38, 0x74, 0x25, (byte) 0x99, 0x21, (byte) 0xCB, (byte) 0xDD,
//            0x58, 0x66, 0x77, 0x22, 0x74, (byte) 0x98, 0x30, 0x40, 0x36,
//            (byte) 0xE2 }; // 24字节的密钥
//    /**
//     * 加密方法提供3des加密，并且base64编码
//     * @param key 24字节的密钥
//     * @param str 明文
//     * @return
//     */
//    public static String DataEncrypt(byte[] key, String str ) {
//        String encrypt = null;
//        try {
//            byte[] ret = ThreeDes.encryptMode(key,str.getBytes("UTF-8"));
//
//            encrypt = new String(Base64Util.encode(ret));
//        } catch (Exception e) {
//            System.out.print(e);
//            encrypt = str;
//        }
//        return encrypt;
//    }
//    /***
//     * 解密方法，先解密base64,在按3des解密
//     * @param key 24字节的密钥
//     * @param str 密文
//     * @return
//     */
//    public static String DataDecrypt(byte[] key,String str ) {
//        String decrypt = null;
//        try {
//            byte[] ret = ThreeDes.decryptMode( key,Base64Util.decode(str));
//            decrypt = new String(ret, "UTF-8");
//        } catch (Exception e) {
//            System.out.print(e);
//            decrypt = str;
//        }
//        return decrypt;
//    }
//    
//    /***
//     * 使用默认密钥，3des编码,base64编码，url编码
//     * @param str 明文
//     * @return 密文
//     */
//    public static String encodeUrl(String str){
//        
//        String encoded = DataEncrypt(keyBytes,str );
//        try {
//            encoded = URLEncoder.encode(encoded, "utf-8");
//        } catch (UnsupportedEncodingException e) {
//        
//        }
//        return encoded;
//        
//    }
//    public static String encodeUrl(byte[] key,String str){
//        
//        String encoded = DataEncrypt(key,str );
//        try {
//            encoded = URLEncoder.encode(encoded, "utf-8");
//        } catch (UnsupportedEncodingException e) {
//        
//        }
//        return encoded;
//        
//    }
//    /**
//     * 使用默认密钥解密方法，url解码，base64解码，3des解码
//     * @param str 密文
//     * @return 明文
//     */
//    public static String decodeUrl(String str){
//        
//        String encoded = "";
//        try {
//            encoded = URLDecoder.decode(str, "utf-8");
//        } catch (UnsupportedEncodingException e) {
//        
//        }
//         encoded = DataDecrypt(keyBytes,encoded );
//        return encoded;
//        
//    }
//    public static String decodeUrl(byte[] key,String str){
//        
//        String encoded = "";
//        try {
//            encoded = URLDecoder.decode(str, "utf-8");
//        } catch (UnsupportedEncodingException e) {
//        
//        }
//         encoded = DataDecrypt(key,encoded );
//        return encoded;
//        
//    }
//    
//    /**
//     * @param args
//     */
//    public static void main(String[] args) {
//        String str = "a=1000000000000000010";
//        System.out.println(str);
//        str = Encrypt.encodeUrl(keyBytes,str);
//        System.out.println(str);
//        str = Encrypt.decodeUrl(keyBytes,str);
//        System.out.println(str);
//    }
//
//}