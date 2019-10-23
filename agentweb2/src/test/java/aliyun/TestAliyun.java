package aliyun;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.junit.Test;

import cn.eeepay.framework.util.ALiYunOssUtil;
import cn.eeepay.framework.util.Constants;

/**
 * <p>Description: <／p>
 * <p>Company: www.eeepay.cn<／p> 
 * @author liusha
 * @date 2016年6月1日
 */
public class TestAliyun {
	private static final String READ_PATH=TestAliyun.class.getResource("TestAliyun.class").getPath();
	private static final String WRITE_PATH="";
	private static final String FILE_NAME="TestAliyun.class";
	
	@Test
	public void testSave() throws IOException {
		File file=new File(READ_PATH);
		//判断文件是否存
		if(!ALiYunOssUtil.exists(Constants.ALIYUN_OSS_TEMP_TUCKET, FILE_NAME)){
			//保存文件名
			ALiYunOssUtil.saveFile(Constants.ALIYUN_OSS_TEMP_TUCKET, FILE_NAME, file);
		}
		//获取文件URL
		System.out.println(ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_TEMP_TUCKET, FILE_NAME, new Date(2016,5,2)));
		
		//判断文件是否存
		if(ALiYunOssUtil.exists(Constants.ALIYUN_OSS_TEMP_TUCKET, FILE_NAME)){
			//删除文件
			ALiYunOssUtil.rm(Constants.ALIYUN_OSS_TEMP_TUCKET, FILE_NAME);
		}
	}

}
