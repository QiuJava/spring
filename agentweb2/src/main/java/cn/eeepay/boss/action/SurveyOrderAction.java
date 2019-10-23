package cn.eeepay.boss.action;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.dao.YfbPayOrderDao;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.CollectiveTransOrder;
import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.model.YfbPayOrder;
import cn.eeepay.framework.model.surveyOrder.SurveyOrderInfo;
import cn.eeepay.framework.model.surveyOrder.SurveyReplyRecord;
import cn.eeepay.framework.model.surveyOrder.SurveyUrgeRecord;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.service.CollectiveTransOrderService;
import cn.eeepay.framework.service.SurveyOrderService;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.util.Constants;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author MXG
 * create 2018/09/07
 */
@Controller
@RequestMapping("/surveyOrder")
public class SurveyOrderAction {

    @Resource
    private SurveyOrderService surveyOrderService;
    @Resource
    private AgentInfoService agentInfoService;

    private static final Logger log = LoggerFactory.getLogger(SurveyOrderAction.class);

    /**
     * 条件查询（分页）
     * @param order
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/selectOrderByParam")
    @ResponseBody
    public Map<String,Object> selectOrderByParam(
            @RequestBody SurveyOrderInfo order,
            @RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "10") int pageSize){

        Map<String, Object> result = new HashMap<>();
        try {
            result = surveyOrderService.selectOrderByParam(order, pageNo, pageSize);
        } catch (Exception e) {
           result.put("status", false);
           result.put("msg", "调单查询失败");
           log.error("获取调单失败", e);
        }
        return result;
    }


    /**
     * 调单详情
     * @param id
     * @return
     */
    @RequestMapping("/info")
    @ResponseBody
    public Map<String, Object> info(String id){
        AgentInfo loginAgent = agentInfoService.selectByPrincipal();
        Map<String, Object> result = new HashMap<>();
        try {
            SurveyOrderInfo order = surveyOrderService.selectByIdAndAgentNode(id,loginAgent.getAgentNode());
            result.put("order",order);
        } catch (Exception e) {
            log.error("根据id获取调单信息失败", e);
        }
        return result;
    }


    /**
     * 导出
     * @param param
     * @param response
     * @param request
     */
    @SystemLog(description = "导出调单")
    @RequestMapping("/export")
    @ResponseBody
    public void export(@RequestParam("order") String param, HttpServletResponse response, HttpServletRequest request){
        SurveyOrderInfo order = JSONObject.parseObject(param, SurveyOrderInfo.class);
        try {
            surveyOrderService.export(order, response, request);
        } catch (Exception e) {
            log.error("调单列表导出失败", e);
        }
    }


    /**
     * 催单列表
     * @param orderNo
     * @return
     */
    @RequestMapping("/urgeRecordList")
    @ResponseBody
    public Map<String, Object> urgeRecordList(String orderNo){
        Map<String, Object> result = new HashMap<>();
        try {
            List<SurveyUrgeRecord> urgeRecordList = surveyOrderService.selectUrgeRecordList(orderNo);
            result.put("urgeRecordList", urgeRecordList);
        } catch (Exception e) {
            log.error("保存确认失败", e);
            result.put("status", false);
        }
        return result;
    }



    /**
     * 首页展示的未处理调单和催单弹窗
     * @return
     */
    @RequestMapping("/getUnDealOrder")
    @ResponseBody
    public Map<String, Object> getUnDealOrder(){
        Map<String, Object> result = new HashMap<>();
        AgentInfo loginAgent = agentInfoService.selectByPrincipal();
        try {
            result = surveyOrderService.getUnDealOrder();
            //修改状态为已阅
            List<SurveyUrgeRecord> surveyUrgeRecords = (List<SurveyUrgeRecord>) result.get("surveyUrgeRecords");
            if(surveyUrgeRecords != null && surveyUrgeRecords.size() > 0){
                for (SurveyUrgeRecord record : surveyUrgeRecords) {
                    surveyOrderService.haveLook(record.getId());
                }
            }
        } catch (Exception e) {
            log.error("获取调单首页展示信息失败", e);
        }
        return result;
    }

    /**
     * 交易详情
     * @param orderNo
     * @return
     */
    @RequestMapping(value = "/selectTradeOrderDetail")
    @ResponseBody
    public Map<String, Object> selectTradeOrderDetail(String orderNo) {
        Map<String, Object> msg = new HashMap<>();
        try {
            if (orderNo == null || orderNo.isEmpty()) {
                msg.put("status", false);
                msg.put("msg", "订单号为空");
                return msg;
            }
            YfbPayOrder info = surveyOrderService.selectTradeOrderDetail(orderNo);
            msg.put("status", true);
            msg.put("info", info);
        } catch (Exception e) {
            msg.put("status", false);
            msg.put("msg", "查询失败");
            log.error("超级还-交易详情查询失败", e);
        }
        return msg;
    }

}
