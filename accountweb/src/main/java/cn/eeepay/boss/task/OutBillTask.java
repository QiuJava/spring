package cn.eeepay.boss.task;

import javax.annotation.Resource;

import cn.eeepay.framework.model.bill.TimeTaskRecord;
import cn.eeepay.framework.service.bill.TaskService;
import cn.eeepay.framework.service.bill.TimeTaskRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.eeepay.framework.service.bill.OutBillDetailService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 定时3分钟，检查当天，‘已提交出款系统’，‘出款中’的出账明细，调用出款系统查询接口，依据订单号及类型来查询改订单号的出款状态后，如果是出款成功，或出款失败，或出款中；则更新到本地；
 * @author Administrator
 *
 */

@Component
public class OutBillTask {
	private static final Logger log = LoggerFactory.getLogger(OutBillTask.class);

	@Resource
	private TimeTaskRecordService timeTaskRecordService;
	@Resource
	private TaskService taskService;

	@Transactional
	public boolean execute(String runningNo) {

		TimeTaskRecord timeTaskRecord = new TimeTaskRecord();
		timeTaskRecord.setCreateTime(new Date());
		timeTaskRecord.setRunningNo(runningNo);
		timeTaskRecord.setRunningStatus("running");
		timeTaskRecordService.add(timeTaskRecord);

		boolean flag = true;
		try {
			//执行具体的定时任务逻辑
			int result = taskService.syncOutBillDetailStatus();
			if(result != 1){
				flag = false;
			}
		}catch (Exception e){
			flag = false;
			log.error("OutBillTask定时任务业务异常...{}",e.getMessage());
			e.printStackTrace();
		}
		//不管业务逻辑失败还是成功，都标记为成功
		timeTaskRecord.setRunningStatus("complete");
		timeTaskRecord.setLastUpdateTime(new Date());
		timeTaskRecordService.updateByRunningNoAndStatus(timeTaskRecord,"running");
//		if(flag){
//			timeTaskRecord.setRunningStatus("complete");
//			timeTaskRecord.setLastUpdateTime(new Date());
//			timeTaskRecordService.updateByRunningNoAndStatus(timeTaskRecord,"running");
//		}else{
//			timeTaskRecord.setRunningStatus("failt");
//			timeTaskRecord.setLastUpdateTime(new Date());
//			timeTaskRecordService.updateByRunningNoAndStatus(timeTaskRecord,"running");
//		}
		return true;
	}
}
