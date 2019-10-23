package cn.eeepay.boss.action;

import cn.eeepay.framework.encryptor.md5.Md5;
import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.service.ScheduleService;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * date : 2018-06-22
 * author : ls
 * desc : 定时调度系统   老卢接口调用
 */
@Controller
@RequestMapping("/agentTask")
public class ScheduleAction {
    @Resource
    private ScheduleService scheduleService;
    private static final Logger log = LoggerFactory.getLogger(ScheduleAction.class);

    @Resource
    private SysDictService sysDictService;

    /**
     * 订单任务统一接口
     * 实现 Runnable 分支
     * 在ScheduleServiceImpl 实现分支
     */
    @RequestMapping("/{interfaceType}")
    @ResponseBody
    public Map<String,Object> startRun(@PathVariable("interfaceType") String interfaceType,
                                       @RequestParam Map<String, String> param){
        String runningNo = param.get("runningNo");
        String hmac = param.get("hmac");
        log.info("定时任务: runningNo = " + runningNo + " hmac= " + hmac + " interfaceType = " + interfaceType + " parameterMap = " + param);
        if (StringUtils.isBlank(runningNo) || StringUtils.isBlank(hmac)) {
            log.error("定时任务:  runningNo或hmac为空,非法请求");
            Map<String,Object> map = new HashMap<>();
            map.put("code","403");
            map.put("msg","非法请求!");
            return map;
        }
        return task(runningNo,hmac,interfaceType, param);
    }


    private Map<String,Object> task(String runningNo, String hmac, String interfaceType, Map<String, String> otherParams){
        Map<String,Object> msg = new HashMap<>();
        try{
            SysDict taskKey = sysDictService.getByKey("BOSS_TASK_KEY");
            String sign = Md5.md5Str(runningNo+ (taskKey == null ? "" : taskKey.getSysValue()));
            if(!sign.equalsIgnoreCase(hmac)){
                log.error("定时任务: 签名校验,非法请求");
                msg.put("code","403");
                msg.put("msg","非法请求");
            }else{
                msg=scheduleService.process(runningNo,interfaceType, otherParams);
            }
        }catch (Exception e) {
            msg.put("code","403");
            msg.put("msg","非法请求");
            log.error("eeeee",e);
        }
        return msg;
    }
}
