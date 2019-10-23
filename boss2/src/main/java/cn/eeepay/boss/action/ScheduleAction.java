package cn.eeepay.boss.action;

import cn.eeepay.framework.service.ScheduleService;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.util.Md5;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * date : 2018-06-22
 * author : ls
 * desc : 定时调度系统   老卢接口调用
 */
@Controller
@RequestMapping("bossTask")
public class ScheduleAction {
    @Resource
    private ScheduleService scheduleService;
    private static final Logger log = LoggerFactory.getLogger(ScheduleAction.class);

    @Resource
    private SysDictService sysDictService;

    @RequestMapping("startHappyRun")
    @ResponseBody
    public Map<String,Object> startRun(@RequestParam("runningNo")String runningNo,
                                       @RequestParam("hmac")String hmac){
        return task(runningNo,hmac,"happy");
    }

    /**
     * 订单任务统一接口
     * 实现 Runnable 分支
     * 在ScheduleServiceImpl 实现分支
     */
    @RequestMapping("/{interfaceType}")
    @ResponseBody
    public Map<String,Object> startRun(@RequestParam("runningNo")String runningNo,
                                       @RequestParam("hmac")String hmac,
                                       @PathVariable("interfaceType") String interfaceType){
        return task(runningNo,hmac,interfaceType);
    }


    private Map<String,Object> task(String runningNo,String hmac,String interfaceType){
        Map<String,Object> msg = new HashMap<>();
        try{
            String BOSS_TASK_KEY=sysDictService.getValueByKey("BOSS_TASK_KEY");
            String sign = Md5.md5Str(runningNo+BOSS_TASK_KEY);
            if(!sign.equalsIgnoreCase(hmac)){
                msg.put("code","403");
                msg.put("msg","非法请求");
            }else{
                msg=scheduleService.process(runningNo,interfaceType);
            }
            //msg=scheduleService.process(runningNo,interfaceType);
        }catch (Exception e) {
            msg.put("code","403");
            msg.put("msg","非法请求");
            log.error("eeeee",e);
        }
        return msg;
    }

    public static void main(String[] args) {
        String key = "YFBTASKMGR20180422";
        String runningNo = "0416";
        String sign = Md5.md5Str(runningNo+key);
        System.out.println(sign);
//        http://localhost:8088/boss2/bossTask/accountRecord?runningNo=0416&hmac=1296fd744aa841436de925990470317c

    }
}
