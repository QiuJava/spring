package cn.eeepay.framework.util;

import cn.eeepay.boss.action.PerMerAction;
import cn.eeepay.framework.encryptor.md5.Md5;
import cn.eeepay.framework.model.UserLoginInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class CommonUtil {

    private static Logger log = LoggerFactory.getLogger(CommonUtil.class);

    /**
     * 获取当前登录的UserLoginInfo
     * @return
     */
    public static UserLoginInfo getLoginUser(){
        UserLoginInfo userInfo = null;
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal != null){
            userInfo = principal;
        } else {
            throw new RuntimeException("用户未登录");
        }
        return userInfo;
    }

    /**
     * 将图片的地址转换为阿里云的地址
     * @param imgUrl
     * @return
     */
    public static String getImgUrl(String imgUrl){
        if(StringUtils.isBlank(imgUrl)){
            return imgUrl;
        }
        Long dateTime = new Date().getTime() + 60*60*1000L;
        Date date = new Date(dateTime);
        imgUrl = ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_TEMP_TUCKET, imgUrl, date);
        return imgUrl;
    }

    public static String getImgUrlAgent(String imgUrl){
        if(StringUtils.isBlank(imgUrl)){
            return imgUrl;
        }
        Long dateTime = new Date().getTime() + 60*60*1000L;
        Date date = new Date(dateTime);
        imgUrl = ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_ATTCH_TUCKET, imgUrl, date);
        return imgUrl;
    }

    public static String replaceMask(String replaceStr, String regex, String mask){
        if (StringUtils.isBlank(replaceStr)){
            return "";
        }
        try {
            return replaceStr.replaceAll(regex, mask);
        }catch (Exception e){
            return replaceStr;
        }
    }

    public static final String sortASCIISign(Map<String, String> params, String signKey) {
        StringBuffer signStr = new StringBuffer();
        // 所有参与传参的参数按照ASCII排序（升序）
        TreeMap<String, Object> tempMap = new TreeMap<String, Object>();
        for (String  key : params.keySet()) {
            if (StringUtils.isNotBlank(String.valueOf(params.get(key)))) {
                tempMap.put(key, params.get(key));
            }
        }
        for (String key : tempMap.keySet()) {
            signStr.append(key).append("=").append(tempMap.get(key)).append("&");
        }
        signStr = signStr.append("key=").append(signKey);
        log.info("签名串：{}",signStr.toString());
        // MD5加密,结果转换为大写字符
        //String sign = MD5Util.encode(signStr.toString()).toUpperCase();
        String sign = Md5.md5Str(signStr.toString()).toUpperCase();
        return sign;
    }

}
