package cn.eeepay.framework.util;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

/**
 * 
 * @author zouruijin
 * 2016年8月4日11:36:08
 *
 */
public class HttpConnectUtil {
	private static final Logger log = LoggerFactory.getLogger(HttpConnectUtil.class);
//    private static String DUOSHUO_SHORTNAME = "accountApi2";//短域名 ****.yoodb.****
//    private static String DUOSHUO_SECRET = "xxxxxxxxxxxxxxxxx";// 秘钥
     
    /**
     * get方式
     * @param url
     * @author 
     * @return
     */
    public static String getHttp(String url) {
        String responseMsg = "";
        HttpClient httpClient = new HttpClient();
        GetMethod getMethod = new GetMethod(url);
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler());
        try {
            httpClient.executeMethod(getMethod);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = getMethod.getResponseBodyAsStream();
            int len = 0;
            byte[] buf = new byte[1024];
            while((len=in.read(buf))!=-1){
                out.write(buf, 0, len);
            }
            responseMsg = out.toString("UTF-8");
        } catch (HttpException e) {
            log.error("异常:",e);
        } catch (IOException e) {
            log.error("异常:",e);
        } finally {
            //释放连接
            getMethod.releaseConnection();
        }
        return responseMsg;
    }
 
    /**
     * post方式
     * @param url
     * @author
     * @return
     */
    public static String postHttp(String url,String paramterName,String paramterValue) {
        String responseMsg = "";
        HttpClient httpClient = new HttpClient();
        httpClient.getParams().setContentCharset("GBK");
        PostMethod postMethod = new PostMethod(url);
        postMethod.addParameter(paramterName, paramterValue);
        try {
            httpClient.executeMethod(postMethod);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = postMethod.getResponseBodyAsStream();
            int len = 0;
            byte[] buf = new byte[1024];
            while((len=in.read(buf))!=-1){
                out.write(buf, 0, len);
            }
            responseMsg = out.toString("UTF-8");
        } catch (HttpException e) {
            log.error("异常:",e);
        } catch (IOException e) {
            log.error("异常:",e);
        } finally {
            postMethod.releaseConnection();
        }
        return responseMsg;
    }

    /**
     * post方式
     * @param url
     * @author
     * @return
     */
    public static String postHttp(String url,Map<String,String> paramterMap) {
        String responseMsg = "";
        HttpClient httpClient = new HttpClient();
        httpClient.getParams().setContentCharset("GBK");
        PostMethod postMethod = new PostMethod(url);
        Iterator<Map.Entry<String,String>> iterator = paramterMap.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<String,String> entry = iterator.next();
            String key = entry.getKey();
            String value = entry.getValue();
            postMethod.addParameter(key, value);
        }
        try {
            httpClient.executeMethod(postMethod);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = postMethod.getResponseBodyAsStream();
            int len = 0;
            byte[] buf = new byte[1024];
            while((len=in.read(buf))!=-1){
                out.write(buf, 0, len);
            }
            responseMsg = out.toString("UTF-8");
        } catch (HttpException e) {
            log.error("异常:",e);
        } catch (IOException e) {
            log.error("异常:",e);
        } finally {
            postMethod.releaseConnection();
        }
        return responseMsg;
    }



}