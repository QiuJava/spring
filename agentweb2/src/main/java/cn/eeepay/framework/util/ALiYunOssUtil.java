package cn.eeepay.framework.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import cn.eeepay.framework.dao.SysConfigDao;
import com.aliyun.openservices.oss.OSSClient;
import com.aliyun.openservices.oss.OSSException;
import com.aliyun.openservices.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.openservices.oss.model.ObjectMetadata;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component
public class ALiYunOssUtil {
	public static final String ACCESS_KEY="Ck76WULSZApw3ZFv";
	public static final String ACCESS_KEY_SECRET="DNTBfd8EQmx2f5ojFhpx5vR02WiHQh";
	public static final String ENDPOINT = "http://oss-cn-hangzhou.aliyuncs.com";

	private static SysConfigDao sysConfigDao;

	@Resource
	public void setSysConfigDao(SysConfigDao sysConfigDao) {
		ALiYunOssUtil.sysConfigDao = sysConfigDao;
	}

	/**
	 * 保存文件到阿里云
	 * @param bucketName bucket的名称
	 * @param fileName 保存在阿里云的文件名
	 * @param file 要保存的文件对象
	 * @throws IOException 
	 */
	public static void  saveFile(String bucketName,String fileName,File file) throws IOException{
		FileInputStream fis=new FileInputStream(file);
		saveFile(bucketName, fileName, fis);
	}
	/**
	 * 保存文件到阿里云
	 * @param bucketName bucket的名称
	 * @param fileName 保存在阿里云的文件名
	 * @param is 数据输入流 输入流会在方法执行完毕 关闭
	 * @throws IOException 
	 */
	public static void saveFile(String bucketName,String fileName,InputStream is) throws IOException{
		ObjectMetadata metadata=new ObjectMetadata();
		metadata.setContentLength(is.available());
		OSSClient client=new OSSClient(ENDPOINT,ACCESS_KEY, ACCESS_KEY_SECRET);
		System.out.println("-----------------------");
		client.putObject(bucketName, fileName, is, metadata);
		is.close();
	}
	/**
	 * 复制阿里云上的文件
	 * @param srcBucketName 源 bucket名称
	 * @param srcFileName 源文件名
	 * @param bucketName 目的地bucket名称
	 * @param fileName 目的文件名
	 * @throws IOException 
	 */
	public static void cp(String srcBucketName,String srcFileName,String bucketName,String fileName) throws IOException{
		OSSClient client=new OSSClient(ENDPOINT,ACCESS_KEY, ACCESS_KEY_SECRET);
		client.copyObject(srcBucketName, srcFileName, bucketName, fileName);
		
	}
	
	
	/**
	 * 删除文件  
	 * @param bucketName bucket名称
	 * @param fileName 文件名称
	 */
	public static void rm(String bucketName,String fileName){
		OSSClient client=new OSSClient(ENDPOINT,ACCESS_KEY, ACCESS_KEY_SECRET);
		client.deleteObject(bucketName, fileName);
	}
	/**
	 * 生成url地址
	 * @param bucketName
	 * @param fileName
	 * @param expiresDate 过期时间 (此参数无效)
	 * @return
	 * @deprecated 过期时间将从数据库获取,
	 * @see #genUrl(String, String)
	 */
	@Deprecated
	public static String genUrl(String bucketName,String fileName,Date expiresDate){
		return genUrl(bucketName, fileName);
//		OSSClient client=new OSSClient(ENDPOINT,ACCESS_KEY, ACCESS_KEY_SECRET);
//		GeneratePresignedUrlRequest generatePresignedUrlRequest =new GeneratePresignedUrlRequest(bucketName, fileName);
//		generatePresignedUrlRequest.setExpiration(expiresDate);
//		return client.generatePresignedUrl(generatePresignedUrlRequest).toString();
	}

	public static String genUrl(String bucketName,String fileName){
		OSSClient client=new OSSClient(ENDPOINT,ACCESS_KEY, ACCESS_KEY_SECRET);
		GeneratePresignedUrlRequest generatePresignedUrlRequest =new GeneratePresignedUrlRequest(bucketName, fileName);
		generatePresignedUrlRequest.setExpiration(DateUtils.addMinute(new Date(), getAliyunOssTTL()));
		return client.generatePresignedUrl(generatePresignedUrlRequest).toString();
	}

	private static int getAliyunOssTTL() {
		String ttl = sysConfigDao.getStringValueByKey("aliyun_oss_time_to_live");
		try {
			int ttlValue = Integer.valueOf(ttl);
			if (ttlValue <= 0) {
				ttlValue = 30;
			}
			return ttlValue;
		}catch (Exception e){
			return 30;
		}
	}

	/**
	 * 判断文件是否存在
	 * @param bucketName
	 * @param fileName
	 * @return
	 */
	public static boolean exists(String bucketName,String fileName){
		OSSClient client=new OSSClient(ENDPOINT,ACCESS_KEY, ACCESS_KEY_SECRET);
		try{
			client.getObject(bucketName, fileName);
		}catch(OSSException e){
			if(e.getErrorCode().equals("NoSuchKey")){
				return false;
			}else{
				throw e;
			}
		}
		return true;
	}
}
