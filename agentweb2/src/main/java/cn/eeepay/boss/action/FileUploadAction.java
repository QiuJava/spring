package cn.eeepay.boss.action;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.eeepay.framework.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import cn.eeepay.framework.util.ALiYunOssUtil;
import cn.eeepay.framework.util.Constants;

@Controller
@RequestMapping(value="/upload")
public class FileUploadAction {
	private static final Logger log = LoggerFactory.getLogger(FileUploadAction.class);
	
	
	/**
	 * 上传
	 * @param mfile
	 * @param request
	 * @return 上传路径：url
	 * @throws Exception
	 */
	@RequestMapping(value = "/fileUpload.do")
	public @ResponseBody Object fileUpload( @RequestParam("file") MultipartFile mfile,HttpServletRequest request) throws Exception{
		Map<String, Object> msg = new HashMap<>();
		try {
			if(mfile != null && mfile.getSize()>0){
				String fileOriginalName = mfile.getOriginalFilename();
				if (fileOriginalName != null && !fileOriginalName.equals("")) {
					Date date = new Date();
					int random = new Random().nextInt(100000);
					String[] str=fileOriginalName.split("-");
					String fileName = str[0]+"_"+date.getTime()+"_"+random +".jpg";//时间戳+文件后缀名
					ALiYunOssUtil.saveFile(Constants.ALIYUN_OSS_ATTCH_TUCKET, fileName, mfile.getInputStream());
					msg.put("url", fileName);
				}
			}
		} catch (Exception e) {
			log.error("上传图片失败!");
		}
		return msg;
	}
	
	
	/**
	 * 多个上传
	 * @param mfile
	 * @param request
	 * @return 上传路径：url
	 * @throws Exception
	 */
	@RequestMapping(value = "/fileUploads.do")
	public @ResponseBody Object fileUpload(@RequestParam("file") List<MultipartFile> mfile,HttpServletRequest request) throws Exception{
		Map<String, Object> msg = new HashMap<>();
		try {
			if(request instanceof MultipartRequest){
				List<String> str=new ArrayList<String>();
				MultipartRequest qq=(MultipartRequest)request;
				Map<String,MultipartFile> maps =qq.getFileMap();
				for (String key : maps.keySet()) {
					System.out.println(key.substring(5,key.length()-4));
					String fileOriginalName = maps.get(key).getOriginalFilename();
					if (fileOriginalName != null && !fileOriginalName.equals("")) {
						Date date = new Date();
						int random = new Random().nextInt(100000);
						String fileName = key.substring(5,key.length()-4)+"_"+date.getTime()+"_"+random+ ".jpg";//时间戳+文件后缀名
						ALiYunOssUtil.saveFile(Constants.ALIYUN_OSS_ATTCH_TUCKET, fileName, maps.get(key).getInputStream());
						str.add(fileName);
					}
				}
				msg.put("str", str);
			}
		} catch (Exception e) {
			log.error("上传图片失败!");
		}
		return msg;
	}

	/**
	 * 上传文件
	 * @param mfile
	 * @param request
	 * @return 上传路径：fileName
	 * @throws Exception
	 */
	@RequestMapping(value = "/fileUploadAll.do")
	public @ResponseBody Object fileUploadAll( @RequestParam("file") MultipartFile mfile,HttpServletRequest request) throws Exception{
		Map<String, Object> msg = new HashMap<>();
		try {
			if(mfile != null && mfile.getSize()>0){
				String fileOriginalName = mfile.getOriginalFilename();
				if (fileOriginalName != null && !fileOriginalName.equals("")) {
					Date date = new Date();
					int random = new Random().nextInt(100000);
					String fileName = "";
					if(fileOriginalName.indexOf("_") > 0){
						fileName = fileOriginalName.substring(0,fileOriginalName.indexOf("_"))+"_" +
								date.getTime()+random +
								fileOriginalName.substring(fileOriginalName.lastIndexOf("."),fileOriginalName.length());//原文件名+时间戳+文件后缀名
					}else {
						fileName = fileOriginalName.substring(0,fileOriginalName.indexOf("."))+"_" +
								date.getTime()+random +
								fileOriginalName.substring(fileOriginalName.lastIndexOf("."),fileOriginalName.length());//原文件名+时间戳+文件后缀名
					}
					ALiYunOssUtil.saveFile(Constants.ALIYUN_OSS_ATTCH_TUCKET, fileName, mfile.getInputStream());
					msg.put("url", fileName);
				}
			}
		} catch (Exception e) {
			log.error("上传文件失败!", e);
		}
		return msg;
	}

	/**
	 * 下载文档
	 */
	@RequestMapping(value="/updateFile")
	@ResponseBody
	public Map<String, Object> updateFile(@RequestParam("name") String name,HttpServletResponse response, HttpServletRequest request){
		Map<String, Object> msg=new HashMap<String,Object>();
		OutputStream outStream =null;
		InputStream inStream=null;

		try {
			if(name!=null&&!"".equals(name)){
				response.setHeader("Content-disposition", "attachment;filename="+new String(name.getBytes("GBK"),"ISO-8859-1"));
				response.setCharacterEncoding("UTF-8");
				String fileUrl=CommonUtil.getImgUrlAgent(name);
				URL url = new URL(fileUrl);
				URLConnection conn = url.openConnection(); // 打开连接
				inStream = conn.getInputStream();
				outStream=response.getOutputStream();
				byte[] bs = new byte[1024]; //1K的数据缓冲
				int len;
				while ((len = inStream.read(bs)) != -1) {
					outStream.write(bs, 0, len);
				}
				outStream.flush();
			}
		}catch (Exception e){
			log.error("下载文档异常!",e);
			msg.put("status", false);
			msg.put("msg", "下载文档异常!");
		}finally {
			if(outStream!=null){
				try {
					outStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(inStream!=null){
				try {
					inStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return msg;
	}
}
