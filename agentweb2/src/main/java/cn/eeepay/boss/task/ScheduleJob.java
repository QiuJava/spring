package cn.eeepay.boss.task;

import cn.eeepay.framework.dao.ScheduleDao;
import cn.eeepay.framework.encryptor.md5.Md5;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.util.ClientInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/9/15/015.
 * @author  liuks
 * 定时任务JOB
 */
@Component
public abstract class ScheduleJob implements Runnable{

    private static final Logger log = LoggerFactory.getLogger(ScheduleJob.class);

    private static ThreadLocal<String> runningNoThreadLocal = new ThreadLocal<>();
    private static ThreadLocal<Map<String, String>> otherParamsThrealLocal = new ThreadLocal<>();
    @Resource
    private SysDictService sysDictService;

    @Resource
    private ScheduleDao scheduleDao;


    public String getRunningNo() {
        return runningNoThreadLocal.get();
    }

    public void setRunningNo(String runNo) {
        runningNoThreadLocal.set(runNo);
    }

    public void setOtherParams(Map<String, String> otherParams) {
        otherParamsThrealLocal.set(otherParams);
    }

    public Map<String, String> getOtherParams() {
        return otherParamsThrealLocal.get();
    }
    /**
     * 任务执行
     */
    protected abstract void runTask(String runNo, Map<String, String> otherParam);

    @Override
    public void run() {
        String runNo = getRunningNo();
        Map<String, String> otherParams = getOtherParams();
        try {
            log.info("定时任务执行,runningNo:{}, otherParams: {}", runNo, otherParams);
            runTask(runNo, otherParams);
            log.info("定时任务执行回调,runningNo:{}, otherParams : {}", runNo, otherParams);
            callback(runNo,"complete");
        }catch (Exception e){
            e.printStackTrace();
            log.error("定时任务执行异常,runningNo:{}, otherParams: {}, 异常: {}", runNo, otherParams, e);
        }finally {
            runningNoThreadLocal.remove();
        }
    }
    private void callback(String runNo, String status) {
        try {
            String BOSS_TASK_KEY=sysDictService.getStringValueByKey("BOSS_TASK_KEY");
            String BOSS_TASK_CALLBACK=sysDictService.getStringValueByKey("BOSS_TASK_CALLBACK");
            scheduleDao.updateStatus(runNo, status);
            Map<String, String> param = new HashMap<>();
            param.put("runningNo", runNo);
            param.put("runningStatus", status);
            param.put("hmac", Md5.md5Str(runNo + status + BOSS_TASK_KEY));
            log.info("任务回调,runningNo:{}",runNo);
            String result=new ClientInterface(BOSS_TASK_CALLBACK, param).postRequest();
            log.info("时任务回调,runningNo:{}，返回值：{}",runNo,result);
        } catch (Exception e) {
            log.error("回调调度系统异常,runningNo:{}", runNo, e);
        }
    }

}
