package cn.eeepay.framework.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by wz on 2016/8/26.
 */
public class HttpUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtils.class);
    public static String post(String serverUrl, String data) {
        StringBuilder responseBuilder = null;
        BufferedReader reader = null;
        OutputStreamWriter wr = null;
        URL url;
        try {
            LOGGER.info("请求url {}, 请求数据 {}", serverUrl, data);
            url = new URL(serverUrl);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            conn.setConnectTimeout(1000 * 5);
            wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write(data);
            wr.flush();

            reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "GBK"));
            responseBuilder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                responseBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            return "";
        } finally {

            if (wr != null) {
                try {
                    wr.close();
                } catch (IOException e) {
                    return "";
                }
            }

            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    return "";
                }
            }

        }
        String response = responseBuilder.toString();
        if(!StringUtil.isBlank(response)&& response.length() < 1000) {
            LOGGER.info("返回值{}", response);
        }
        return response;
    }

    public static String post(String serverUrl, String data,String ecode) {
        StringBuilder responseBuilder = null;
        BufferedReader reader = null;
        OutputStreamWriter wr = null;
        URL url;
        try {
            LOGGER.info("请求url {}, 请求参数{}, 请求编码{} ", serverUrl, data, ecode);
            url = new URL(serverUrl);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            conn.setConnectTimeout(1000 * 5);
            wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write(data);
            wr.flush();

            reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), ecode));
            responseBuilder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                responseBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            return "";
        } finally {

            if (wr != null) {
                try {
                    wr.close();
                } catch (IOException e) {
                    return "";
                }
            }

            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    return "";
                }
            }

        }
        String response = responseBuilder.toString();
        if(!StringUtil.isBlank(response)&& response.length() < 1000) {
            LOGGER.info("返回值{}", response);
        }
        return response;
    }

    public static CloseableHttpClient createSSLClientDefault() {
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                // 信任所有
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return HttpClients.createDefault();
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url        发送请求的 URL
     * @param param      请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @param reqCharset 请求参数，编码方式
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param, String reqCharset) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            LOGGER.info("请求url {}, 请求参数{}, 请求编码{} ", url, param, reqCharset);
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), reqCharset));
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if(!StringUtil.isBlank(result)&& result.length() < 1000) {
            LOGGER.info("返回值 {} ", result);
        }
        return result;
    }
    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) {
        String result = "";
        URL resource = HttpUtils.class.getClassLoader().getResource("cert/zf.jks");
        System.setProperty("javax.net.ssl.trustStore", resource.getFile());
        System.setProperty("javax.net.ssl.trustStorePassword","123456");
        BufferedReader in = null;
        try {
            LOGGER.info("请求url {}, 请求参数{} ", url, param);
            String urlNameString = url + "?" + param;
            HttpURLConnection conn = (HttpURLConnection)new URL(urlNameString).openConnection();
            // 发送GET请求必须设置如下两行
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            // flush输出流的缓冲
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        if(!StringUtil.isBlank(result)&& result.length() < 1000) {
            LOGGER.info("返回值 {} ", result);
        }
        return result;
    }

    public static String sendPostRequest(String requestUrl, Map<String, String> requestMap) {
        long responseLength = 0;       //响应长度
        String responseContent = null; //响应内容

        HttpClient httpClient = new DefaultHttpClient();                 //创建默认的httpClient实例
        HttpPost httpPost = new HttpPost(requestUrl);                    //创建HttpPost

        List<NameValuePair> formParams = new ArrayList<NameValuePair>(); //创建参数队列

        for (String key : requestMap.keySet()) {
            formParams.add(new BasicNameValuePair(key, requestMap.get(key)));
        }

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(formParams, "UTF-8"));
            HttpResponse response = httpClient.execute(httpPost); //执行POST请求

            HttpEntity entity = response.getEntity();             //获取响应实体
            if (null != entity) {
                responseLength = entity.getContentLength();
                responseContent = EntityUtils.toString(entity, "UTF-8");
                EntityUtils.consume(entity); //Consume response content
            }
            LOGGER.info("请求地址: " + httpPost.getURI());
            LOGGER.info("响应状态: " + response.getStatusLine());
            LOGGER.info("响应长度: " + responseLength);
            LOGGER.info("响应内容: " + responseContent);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpClient.getConnectionManager().shutdown(); //关闭连接，释放资源
            return responseContent;
        }
    }
}
