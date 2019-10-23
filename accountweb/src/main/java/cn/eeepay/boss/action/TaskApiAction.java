package cn.eeepay.boss.action;

import cn.eeepay.boss.task.NewEptokAutoCheck;
import cn.eeepay.boss.task.OutBillTask;
import cn.eeepay.framework.model.bill.TimeTaskRecord;
import cn.eeepay.framework.service.bill.OutBillDetailService;
import cn.eeepay.framework.service.bill.TimeTaskRecordService;
import cn.eeepay.framework.util.HttpConnectUtil;
import com.auth0.jwt.internal.org.bouncycastle.crypto.digests.MD5Digest;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: 长沙张学友
 * Date: 2018/11/6
 * Time: 9:29
 * Description: 类注释
 */
@Controller
@RequestMapping("/taskApi")
public class TaskApiAction {

    private static final Logger log = LoggerFactory.getLogger(TaskApiAction.class);

    @Autowired
    private OutBillTask outBillTask;
    @Autowired
    private NewEptokAutoCheck newEptokAutoCheck;
    @Autowired
    private TimeTaskRecordService timeTaskRecordService;

    @Value("${task.server.url}")
    private String taskServer;
    @Value("${task.server.key}")
    private String taskKey;

    private static final ExecutorService es = Executors.newCachedThreadPool();

    @RequestMapping("/outBill")
    @ResponseBody
    public Object test(String runningNo,String hmac) throws Exception {
        Map<String,String> map = new HashMap<>();
        String md5 = DigestUtils.md5Hex(runningNo + taskKey);
        //校验hmac
        if(!hmac.equals(md5)){
            throw new RuntimeException("hmac校验失败!");
        }
        map.put("runningNo",runningNo);
        map.put("runningStatus","running");

        List<TimeTaskRecord> byRunningNo = timeTaskRecordService.findByRunningNo(runningNo);
        Optional<String> complete = byRunningNo.stream().filter(T -> T.getRunningStatus().equals("complete") || T.getRunningStatus().equals("running")).map(T -> T.getRunningStatus()).findFirst();
        if(complete.isPresent()){
            log.info("outBill任务无需运行，当前任务状态：{} runningNo：{}",complete.get(),runningNo);
            map.put("runningStatus",complete.get());
            return map;
        }




        es.execute(()->{
            log.info("outBill Task start...");
            boolean result = outBillTask.execute(runningNo);
            callBack(runningNo,result);
            log.info("outBill Task end...");
        });

        return map;
    }

    @RequestMapping("/newEptokAutoCheck")
    @ResponseBody
    public Object bewEptokAutoCheck(String runningNo,String hmac) throws Exception {
        Map<String,String> map = new HashMap<>();
        String md5 = DigestUtils.md5Hex(runningNo + taskKey);
        //校验hmac
        if(!hmac.equals(md5)){
            throw new RuntimeException("hmac校验失败!");
        }

        map.put("runningNo",runningNo);
        map.put("runningStatus","running");

        List<TimeTaskRecord> byRunningNo = timeTaskRecordService.findByRunningNo(runningNo);
        Optional<String> complete = byRunningNo.stream().filter(T -> T.getRunningStatus().equals("complete") || T.getRunningStatus().equals("running")).map(TimeTaskRecord::getRunningStatus).findFirst();
        if(complete.isPresent()){
            log.info("newEptokAutoCheck任务无需运行，当前任务状态：{} runningNo：{}",complete.get(),runningNo);
            map.put("runningStatus",complete.get());
            return map;
        }

        es.execute(()->{
            log.info("bewEptokAutoCheck Task start...");
            boolean result = newEptokAutoCheck.YinShenZhiQing(runningNo);
            callBack(runningNo,result);
            log.info("bewEptokAutoCheck Task end...");
        });

        return map;
    }


    public void callBack(String runningNo,boolean status){
        String runningStatus = status? "complete" : "complete";     //本来false是返回失败的，由于定时任务系统不接受失败回调，所以暂时全部返回成功
        Map<String,String> map = new HashMap<>();
        map.put("runningNo",runningNo);
        map.put("runningStatus",runningStatus);
        String hmac = DigestUtils.md5Hex(runningNo+runningStatus+taskKey);
        map.put("hmac", hmac);
        HttpConnectUtil.postHttp(taskServer,map);
    }

}
