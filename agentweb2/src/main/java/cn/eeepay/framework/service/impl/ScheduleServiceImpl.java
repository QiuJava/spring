package cn.eeepay.framework.service.impl;

import cn.eeepay.boss.task.ScheduleJob;
import cn.eeepay.framework.dao.ScheduleDao;
import cn.eeepay.framework.service.ScheduleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service("scheduleService")
public class ScheduleServiceImpl implements ScheduleService {
    private static final Logger logger = LoggerFactory.getLogger(ScheduleServiceImpl.class);
    @Resource
    private ScheduleDao scheduleDao;

    @Resource
    private TaskExecutor taskExecutor;

    @Resource
    private ApplicationContext context;

    @Override
    public int insertTask(String runNo, String interfaceType) {
        return scheduleDao.insert(runNo, interfaceType);
    }

    @Override
    public int updateTask(String runNo, String status) {
        return scheduleDao.updateStatus(runNo, status);
    }

    @Override
    public Map<String, Object> queryTask(String runNo) {
        return scheduleDao.query(runNo);
    }

    public Map<String, Object> process(final String runNo, String interfaceType, final Map<String, String> otherParams) {
        Map<String, Object> msg = new HashMap<>();
        msg.put("runningNo", runNo);
        Map<String, Object> task = queryTask(runNo);
        if (task != null) {
            logger.info("定时任务: 从数据库获取定时任务 runNo = "+runNo+", 状态 = " + task.get("running_status"));
            msg.put("runningStatus", task.get("running_status"));
        } else {
            //添加线程池
            ScheduleJob job = getJobBean(interfaceType);
            if (job == null){
                msg.put("code","403");
                msg.put("msg","非法请求");
            }else {
                insertTask(runNo, interfaceType);
                logger.info("定时任务: 开始执行定时任务: runNo = " + runNo);
                final ScheduleJob finalJob = job;
                taskExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        finalJob.setRunningNo(runNo);
                        finalJob.setOtherParams(otherParams);
                        finalJob.run();
                    }
                });
                msg.put("runningStatus", "running");
            }
        }
        return msg;
    }

    private ScheduleJob getJobBean(String interfaceType){
        if (!context.containsBean(interfaceType)){
            return null;
        }
        try {
            return context.getBean(interfaceType, ScheduleJob.class);
        }catch (Exception e){
            return null;
        }

    }

}
