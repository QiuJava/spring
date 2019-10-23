package cn.eeepay.boss.task;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import cn.eeepay.framework.model.bill.TimeTaskRecord;
import cn.eeepay.framework.service.bill.TaskService;
import cn.eeepay.framework.service.bill.TimeTaskRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.eeepay.framework.model.bill.SysDict;
import cn.eeepay.framework.service.bill.DuiAccountDetailService;
import cn.eeepay.framework.service.bill.SysDictService;
import cn.eeepay.framework.util.DateUtil;
import org.springframework.transaction.annotation.Transactional;


@Component
public class NewEptokAutoCheck {
	private static final Logger log = LoggerFactory.getLogger(NewEptokAutoCheck.class);
	
	@Autowired
	public DuiAccountDetailService duiAccountDetailService;
	@Resource
	private TimeTaskRecordService timeTaskRecordService;
	@Resource
	private TaskService taskService;

	@Resource
	public SysDictService sysDictService;
	
    //@Scheduled(cron="0 55 * * * ? ") //间隔5秒执行
//    public void XinYinShen(){
//
//    	SysDict sysDict = null;
//		try {
//			sysDict = sysDictService.findSysDuiAccountGroup("sys_neweptok_account_status");
//		} catch (Exception e) {
//			log.error("异常:", e);
//		}
//		if(sysDict == null) {
//			log.info("查询 sys_neweptok_account_status 字典为空，直接返回");
//			return;
//		}
//		if("1".equals(sysDict.getSysValue())){
//	    	String tom = DateUtil.subDay(new Date(),1);
//	    	String fileName="ACC"+tom+"CHK";
//	    	String acqName="neweptok";//neweptok新银盛
//	    	Map<String, String> map=new HashMap<String, String>();
//	    	map.put("fileName", fileName);
//	    	map.put("acqOrg", acqName);
//	    	try{
//		    	boolean flag = duiAccountDetailService.duiAccountFileDown(map);
//		    	if(flag){
//		    		log.info(acqName+"对账文件下载成功，开始对账~");
//		    		duiAccountDetailService.duiAccountFileUpload(fileName,acqName);
//		    	}else{
//		    		log.info(acqName+"对账文件未下载成功！");
//		    	}
//	    	}catch(Exception e){
//	    		log.error("异常:",e);
//	    		log.error(acqName+"自动对账失败");
//	    	}
//		}else{
//			log.info("sys_neweptok_account_status 定时对账开关被关闭");
//		}
//
//    }

	public boolean YinShenZhiQing(String runningNo){
		TimeTaskRecord timeTaskRecord = new TimeTaskRecord();
		timeTaskRecord.setCreateTime(new Date());
		timeTaskRecord.setRunningNo(runningNo);
		timeTaskRecord.setRunningStatus("running");
		timeTaskRecordService.add(timeTaskRecord);

		try {
			if(taskService.autoDownLoadYsFile()){
				taskService.autoYinShenZhiQingDuiZhang();
			}
		}catch (Exception e){
			log.error("银盛自动对账出错:{}",e.getMessage());
			e.printStackTrace();
//			TimeTaskRecord failt = new TimeTaskRecord();
//			failt.setCreateTime(new Date());
//			failt.setRunningNo(runningNo);
//			failt.setRunningStatus("failt");
//			timeTaskRecordService.updateByRunningNoAndStatus(failt,"running");
//			return false;
		}
		timeTaskRecord.setRunningStatus("complete");
		timeTaskRecord.setLastUpdateTime(new Date());
		timeTaskRecordService.updateByRunningNoAndStatus(timeTaskRecord,"running");
		return true;
  }
}