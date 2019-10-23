package cn.eeepay.boss.action;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.surveyOrder.SurveyOrderInfo;
import cn.eeepay.framework.model.surveyOrder.SurveyReplyRecord;
import cn.eeepay.framework.service.AccessService;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.service.SurveyOrderReplyService;
import cn.eeepay.framework.service.SurveyOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 调单回复记录
 * @author MXG
 * create 2018/09/13
 */
@Controller
@RequestMapping("/reply")
public class SurveyOrderReplyAction {

    @Resource
    private SurveyOrderReplyService replyService;
    @Resource
    private AgentInfoService agentInfoService;
    @Resource
    private SurveyOrderService surveyOrderService;

    private static final Logger log = LoggerFactory.getLogger(SurveyOrderReplyAction.class);


    /**
     * 保存调单回复
     * @param record
     * @return
     */
    //@SystemLog(description = "保存调单回复")
    @RequestMapping("/saveReply")
    @ResponseBody
    public Map<String, Object> saveReply(@RequestBody SurveyReplyRecord record){
        Map<String, Object> result = new HashMap<>();
        AgentInfo loginAgent = agentInfoService.selectByPrincipal();
        SurveyOrderInfo surveyOrder = surveyOrderService.selectByOrderNo(record.getOrderNo(), loginAgent.getAgentNode());
        if(surveyOrder == null){
            result.put("status", false);
            result.put("msg", "非法操作");
            return result;
        }
        String replyStatus = surveyOrder.getReplyStatus();
        if("1".equals(replyStatus) || "2".equals(replyStatus)
                || "4".equals(replyStatus) || "5".equals(replyStatus)){// 一级回复之前 直属/商户已经 回复
            if(loginAgent.getAgentLevel() == 1){
                result.put("status", false);
                result.put("type", "1");
                return result;
            }else { // 直属回复之前 商户已经 回复（一级回复后状态为2或5，此时直属也不能进行回复了）
                result.put("status", false);
                result.put("type", "2");
                return result;
            }
        }
        return reply(record);
    }

    @SystemLog(description = "保存调单回复")
    @RequestMapping("/reply")
    @ResponseBody
    public Map<String, Object> reply(@RequestBody SurveyReplyRecord record){
        Map<String, Object> result = new HashMap<>();
        try {
            replyService.saveReply(record);
            result.put("status", true);
        } catch (Exception e) {
            log.error("保存调单回复失败", e);
            result.put("status", false);
        }
        return result;
    }

    /**
     * 获取回复
     * @param orderNo
     * @return
     */
    @RequestMapping("/getRecord")
    @ResponseBody
    public Map<String, Object> getRecord(String orderNo){
        AgentInfo loginAgent = agentInfoService.selectByPrincipal();
        Map<String, Object> result = new HashMap<>();
        try {
            SurveyReplyRecord record = replyService.selectRecordByOrderNo(orderNo, loginAgent.getAgentNode());
            List<SurveyReplyRecord> replyList = replyService.selectRecordList(orderNo, loginAgent.getAgentNode());
            for (SurveyReplyRecord reply : replyList) {
                if(reply.getReplyFilesName()!=null && !"".equals(reply.getReplyFilesName())){
                    String[] str = reply.getReplyFilesName().split(",");
                    if(str!=null&&str.length>0){
                        reply.setFileList(Arrays.asList(str));
                    }
                }
            }
            result.put("record",record);
            result.put("replyList",replyList);
        } catch (Exception e) {
            log.error("根据调单号获取调单回复失败", e);
        }
        return result;
    }

    /**
     * 获取最后的回复
     * @param orderNo
     * @return
     */
    @RequestMapping("/getLastRecord")
    @ResponseBody
    public Map<String, Object> getLastRecord(String orderNo){
        AgentInfo loginAgent = agentInfoService.selectByPrincipal();
        Map<String, Object> result = new HashMap<>();
        try {
            SurveyReplyRecord record = replyService.selectRecordByOrderNo(orderNo,loginAgent.getAgentNode());
            result.put("record",record);
        } catch (Exception e) {
            log.error("根据调单号获取调单回复失败", e);
        }
        return result;
    }

    /**
     * 回复的修改、审核
     * @param record
     * @param type 1：修改  2：审核
     * @return
     */
    @SystemLog(description = "保存调单回复修改/审核")
    @RequestMapping("/confirm")
    @ResponseBody
    public Map<String, Object> confirm(@RequestBody SurveyReplyRecord record, int type){
        Map<String, Object> result = new HashMap<>();
        AgentInfo loginAgent = agentInfoService.selectByPrincipal();
        SurveyOrderInfo surveyOrder = surveyOrderService.selectByOrderNo(record.getOrderNo(), loginAgent.getAgentNode());
        if(surveyOrder == null){
            result.put("status", false);
            result.put("msg", "非法操作");
            return result;
        }
        if(type == 1){
            record.setReplyRoleType("A");
            record.setReplyRoleNo(loginAgent.getAgentNo());
            // 直属修改的时候，如果一级已经修改或确认 直属不能修改
            if(loginAgent.getAgentLevel() != 1){
                String replyStatus = surveyOrder.getReplyStatus();
                if("2".equals(replyStatus) || "5".equals(replyStatus)){
                    result.put("status", false);
                    result.put("msg", "已有其他人更新了调单内容，请返回调单列表查看。");
                    return result;
                }
            }
            //一级修改时风控先处理了，分为终态处理和非终态处理
            if(loginAgent.getAgentLevel() == 1){
                String dealStatus = surveyOrder.getDealStatus();
                if("2".equals(dealStatus) || "3".equals(dealStatus) || "6".equals(dealStatus)){
                    result.put("status", false);
                    result.put("msg", "提交失败，该调单已处理完毕。");
                    return result;
                }
            }
        }
        try {
            replyService.confirm(record);
            result.put("status", true);
        } catch (Exception e) {
            log.error("保存确认失败", e);
            result.put("status", false);
            result.put("msg", "提交失败");
        }
        return result;
    }
}
