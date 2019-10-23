package cn.eeepay.boss.action.activity;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.activity.SubscribeVip;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.service.activity.SubscribeVipService;
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
import java.util.Map;

/**
 * VIP优享订单
 * @author MXG
 * create 2019/03/25
 */
@Controller
@RequestMapping("/subscribeVip")
public class SubscribeVipAction {

    @Resource
    private AgentInfoService agentInfoService;
    @Resource
    private SubscribeVipService subscribeVipService;

    private static final Logger log = LoggerFactory.getLogger(SubscribeVipAction.class);

    /**
     * 按条件分页查询
     * @param order
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/selectByParam")
    @ResponseBody
    public Map<String, Object> selectByParam(@RequestBody SubscribeVip order,
                                                  @RequestParam(defaultValue = "1") int pageNo,
                                             @RequestParam(defaultValue = "10") int pageSize){

        Map<String, Object> result = new HashMap<>();
        try {
            result = subscribeVipService.selectByParam(order, pageNo, pageSize);
        } catch (Exception e) {
            result.put("status", false);
            result.put("msg", "查询失败");
            log.error("VIP优享订单查询失败", e);
        }
        return result;
    }

    /**
     * 导出
     * @param param
     * @param response
     * @param request
     */
    @SystemLog(description = "导出VIP优享订单")
    @RequestMapping("/export")
    @ResponseBody
    public void export(@RequestParam("order") String param, HttpServletResponse response, HttpServletRequest request){
        SubscribeVip order = JSONObject.parseObject(param, SubscribeVip.class);
        try {
            subscribeVipService.export(order, response, request);
        } catch (Exception e) {
            log.error("VIP优享订单列表导出失败", e);
        }
    }

}
