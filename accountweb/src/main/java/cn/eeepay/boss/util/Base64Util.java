//package cn.eeepay.boss.util;
//
///**
// * base64编码工具类
// * @author wanggang
// * @version 2010-12-31
// */
//public class Base64Util {
//
//    /**
//     *  将 s 进行 BASE64 编码
//     * @param s
//     * @return
//     */
//    public static String encode(byte[] s) {
//        if (s == null)
//            return null;
//        return (new sun.misc.BASE64Encoder()).encode(s);
//    }
//    /**
//     *  将 s 进行 BASE64 编码
//     * @param s
//     * @return
//     */
//    public static String encode(String s) {
//        
//        if (s == null)
//            return null;
//        return encode(s.getBytes());
//    }
//
//    /**将 BASE64 编码的字符串 s 进行解码
//     *
//     * @param s
//     * @return
//     */
//    public static byte[] decode(String s) {
//        if (s == null)
//            return null;
//        sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
//        try {
//            byte[] b = decoder.decodeBuffer(s);
//            return b;
//        } catch (Exception e) {
//            return null;
//        }
//    }
//
//    /**
//     * @param args
//     */
//    public static void main(String[] args) {
//        String a = "asdfsfsd";
//        String b = encode(a);
//        System.out.println(b);
//        System.out.println(decode(b));
//
//    }
//
//}