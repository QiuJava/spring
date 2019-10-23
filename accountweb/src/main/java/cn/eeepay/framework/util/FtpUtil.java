/**
 * 
 */
package cn.eeepay.framework.util;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import com.jcraft.jsch.*;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.eeepay.framework.model.bill.MyFile;

/**
 * ftp工具
 * hdb
 * 2013-6-17 上午11:43:25 
 */
public class FtpUtil {

	private static final Logger log = LoggerFactory.getLogger(FtpUtil.class);
	
	public FTPClient ftpClient = new FTPClient();
	private String host;
	private int port;
	private String username;
	private String password;

	// 设置将过程中使用到的命令输出到控制台
	public FtpUtil(String host, int port, String username, String password) {
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
		this.ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
	}

	/**
	 * 连接到FTP服务器
	 * @throws IOException
	 */
	public boolean connect() throws IOException {
		ftpClient.connect(host, port);
		ftpClient.setControlEncoding("GBK");
		if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
			if (ftpClient.login(username, password)) {
				return true;
			}
		}
		disconnect();
		return false;
	}

	/**
	 * 从FTP服务器上下载文件
	 * @param remote 远程文件路径
	 * @param local 本地文件路径
	 * @return 下载是否成功
	 * @throws IOException
	 */
	public boolean download(String remote, String local)
			throws IOException {
		// 设置被动模式
		ftpClient.enterLocalPassiveMode();
		// 设置以二进制方式传输
		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		boolean result;
		// 检查远程文件是否存在
		FTPFile[] files = ftpClient.listFiles(new String(remote.getBytes("UTF-8"), "iso-8859-1"));
		if (files.length != 1) {
			return false;
		}

		File f = new File(local);
		File parentFile=f.getParentFile();
		if(!parentFile.exists()){
			log.info("make dir");
			parentFile.mkdirs();
		}else{
			log.info("exist");
		}
		OutputStream out = new FileOutputStream(f);
		InputStream in = ftpClient.retrieveFileStream(new String(remote.getBytes("UTF-8"), "iso-8859-1"));
		byte[] bytes = new byte[1024];
		int c;
		while ((c = in.read(bytes)) != -1) {
			out.write(bytes, 0, c);
		}
		in.close();
		out.close();
		boolean upNewStatus = ftpClient.completePendingCommand();
		if (upNewStatus) {
			result = true;
		} else {
			result = false;
		}
		return result;
	}
	/** 
     *  
     * @param file 上传的文件或文件夹 
     * @throws Exception 
     */ 
    public boolean upload(File file,String path) throws Exception{ 
    	boolean flag = false; 
    	ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
    	//跳转到FTP的根目录层级  
        ftpClient.changeWorkingDirectory("//");  
    	String[] ftpPath = path.split("\\/");
    	for (int i = 0; i < ftpPath.length; i++) {
    		ftpClient.makeDirectory(ftpPath[i]);  
    		ftpClient.changeWorkingDirectory(ftpPath[i]);
		} 
        boolean flagPath = ftpClient.changeWorkingDirectory(path);
        if(flagPath){
            File file2 = new File(file.getPath());    
            FileInputStream input = new FileInputStream(file2);    
            ftpClient.enterLocalPassiveMode();
            flag = ftpClient.storeFile(file2.getName(), input); 
            input.close();
            System.out.println(flag);
        }
        return flag;
    }    
	
		
	/**
	 * 断开与远程服务器的连接
	 * @throws IOException
	 */
	public void disconnect() throws IOException {
		if (ftpClient.isConnected()) {
			ftpClient.disconnect();
		}
	}



	public List<Map<String,String>> getFileNameList(String ftpDirectory,int size)   {
		List<Map<String,String>> list = new ArrayList<Map<String, String>>();

		try {
			//设置被动模式
			ftpClient.enterLocalPassiveMode();
			System.out.println("======开始获取文件名称=====");
			FTPFile[] files =  ftpClient.listFiles(ftpDirectory);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
			for(FTPFile f : files){
				if(!f.getName().startsWith("ACO")) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("fileName", f.getName());
					if(f.getName().startsWith("DZ")){
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(f.getTimestamp().getTime());
						calendar.add(Calendar.HOUR_OF_DAY,8);
						Date date = calendar.getTime();
						map.put("fileTime", sdf1.format(date));
					}else {
						map.put("fileTime", sdf.format(f.getTimestamp().getTime()));
					}
					list.add(map);
				}
			}
			if(list.size()>size) {
				return list.subList(list.size() - size, list.size());
			}else {
				return  list;
			}
		} catch (Exception e) {
			log.error("异常:",e);
		}
		return  list;
	}
	
	public List<MyFile> getAllFileList(String ftpDirectory)   {
		List<MyFile> list = new ArrayList<MyFile>();

		try {
			//设置被动模式
			ftpClient.enterLocalPassiveMode();
			System.out.println("======开始获取文件名称=====");
			FTPFile[] files =  ftpClient.listFiles(ftpDirectory);
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
			for(FTPFile f : files){
				if(!f.getName().startsWith("ACO")) {
					MyFile file = new MyFile();
					file.setName(f.getName());
//					if(f.getName().startsWith("DZ")){
//						Calendar calendar = Calendar.getInstance();
//						calendar.setTime(f.getTimestamp().getTime());
//						calendar.add(Calendar.HOUR_OF_DAY,8);
//						Date date = calendar.getTime();
//						map.setCreateDate(date);
//					}else {
////						map.put("fileTime", sdf.format(f.getTimestamp().getTime()));
//						map.setCreateDate(f.getTimestamp().getTime());
//					}
					file.setCreateDate(f.getTimestamp().getTime());
					file.setSize(f.getSize());
					list.add(file);
				}
			}
		} catch (Exception e) {
			log.error("异常:",e);
		}
		return  list;
	}

	//获取中钢所有文件
	public static List<LsEntry> getZGAllFiles(String host, int port, String username, String password, String file) throws Exception {
		ChannelSftp sftp = null;
		JSch jsch = new JSch();
		jsch.getSession(username, host, port);
		Session sshSession = jsch.getSession(username, host, port);
		sshSession.setPassword(password);
		Properties sshConfig = new Properties();
		sshConfig.put("StrictHostKeyChecking", "no");
		sshSession.setConfig(sshConfig);
		sshSession.connect();
		Channel channel = sshSession.openChannel("sftp");
		channel.connect();
		sftp = (ChannelSftp) channel;
		List<LsEntry> v = sftp.ls(file);
		List<LsEntry> all = new ArrayList<>();
		for (ChannelSftp.LsEntry fileEntry : v) {
			List<LsEntry> v1 = sftp.ls(file + fileEntry.getFilename());
			all.addAll(v1);
		}
		sshSession.disconnect();
		channel.disconnect();
		return all;
	}

    public List<MyFile> getDFFileList(String ftpDirectory)   {
        List<MyFile> list = new ArrayList<MyFile>();

        try {
            //设置被动模式
            ftpClient.enterLocalPassiveMode();
            System.out.println("======开始获取文件名称=====");
            FTPFile[] files =  ftpClient.listFiles(ftpDirectory);
            for(FTPFile f : files){
                if(f.getName().startsWith("ACO") && f.getName().contains("DF")) {
                    MyFile file = new MyFile();
                    file.setName(f.getName());
                    file.setCreateDate(f.getTimestamp().getTime());
                    file.setSize(f.getSize());
                    list.add(file);
                }
            }
        } catch (Exception e) {
            log.error("异常:",e);
        }
        return  list;
    }

	public boolean downFiles(String remote,String local){
		ftpClient.enterLocalPassiveMode();
		boolean flag = false;
		try {

			FTPFile[] files =  ftpClient.listFiles(remote);
			for (FTPFile file : files){
				flag =  download(remote+"/"+file.getName(),local+"/"+file.getName());
			}

		} catch (IOException e) {
			log.error("异常:",e);
		}
		return flag;
	}


	public static List<LsEntry>  connect(String host, int port, String username, String password, String file) throws Exception
	{
		ChannelSftp sftp = null;
		JSch jsch = new JSch();
		jsch.getSession(username, host, port);
		Session sshSession = jsch.getSession(username, host, port);
		sshSession.setPassword(password);
		Properties sshConfig = new Properties();
		sshConfig.put("StrictHostKeyChecking", "no");
		sshSession.setConfig(sshConfig);
		sshSession.connect();
		Channel channel = sshSession.openChannel("sftp");
		channel.connect();
		sftp = (ChannelSftp) channel;
		List<LsEntry> v = sftp.ls(file);
		sshSession.disconnect();
		channel.disconnect();
		return v;
	}

	public static boolean  sftpDown(String host, int port, String username, String password,String downloadFile, OutputStream os) throws Exception
	{
		ChannelSftp sftp = null;
		JSch jsch = new JSch();
		jsch.getSession(username, host, port);
		Session sshSession = jsch.getSession(username, host, port);
		sshSession.setPassword(password);
		Properties sshConfig = new Properties();
		sshConfig.put("StrictHostKeyChecking", "no");
		sshSession.setConfig(sshConfig);
		sshSession.connect();
		Channel channel = sshSession.openChannel("sftp");
		channel.connect();
		sftp = (ChannelSftp) channel;
		sftp.get(downloadFile,os);
		sshSession.disconnect();
		channel.disconnect();
		return true;
	}

}
