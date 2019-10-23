package cn.eeepay.boss.action.capitalInsurance;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.capitalInsurance.OrderTotal;
import cn.eeepay.framework.model.capitalInsurance.SafeConfig;
import cn.eeepay.framework.model.capitalInsurance.SafeOrder;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.service.capitalInsurance.SafeOrderService;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
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
 * Created by Administrator on 2018/7/23/023.
 * @author  liuks
 * 保险订单Action
 */
@Controller
@RequestMapping(value = "/safeOrder")
public class SafeOrderAction {

    private static final Logger log = LoggerFactory.getLogger(SafeOrderAction.class);

    @Resource
    private SafeOrderService safeOrderService;

    @Resource
    private AgentInfoService agentInfoService;

    /**
     * 查询保险订单列表
     */
    @RequestMapping(value = "/selectAll")
    @ResponseBody
    public Map<String,Object> selectByParam(@RequestParam("info") String param, @ModelAttribute("page")
            Page<SafeOrder> page) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            SafeOrder order = JSONObject.parseObject(param, SafeOrder.class);
            AgentInfo loginAgent = agentInfoService.selectByPrincipal();
            order.setLoginAgentNo(loginAgent.getAgentNo());
            safeOrderService.selectAllList(order, page);
            OrderTotal total=safeOrderService.selectSum(order, page);
            msg.put("total",total);
            msg.put("page",page);
            msg.put("status", true);
        } catch (Exception e){
            log.error("查询保险订单列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "查询保险订单列表异常!");
        }
        return msg;
    }

    /**
     * 获取资金险配置列表
     */
    @RequestMapping(value = "/getSafeConfigList")
    @ResponseBody
    public Map<String,Object> getSafeConfigList() throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            List<SafeConfig> list=safeOrderService.getSafeConfigList();
            msg.put("status", true);
            msg.put("list", list);
        } catch (Exception e){
            log.error("获取资金险配置列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "获取资金险配置列表异常!");
        }
        return msg;
    }

    /**
     * 导出保险订单列表
     */
    @RequestMapping(value="/exportDetail")
    @ResponseBody
    public Map<String, Object> exportDetail(@RequestParam("info") String param, HttpServletResponse response, HttpServletRequest request){
        SafeOrder order = JSONObject.parseObject(param, SafeOrder.class);
        Map<String, Object> msg=new HashMap<String,Object>();
        AgentInfo loginAgent = agentInfoService.selectByPrincipal();
        order.setLoginAgentNo(loginAgent.getAgentNo());
        List<SafeOrder> list=safeOrderService.exportDetailSelect(order);
        try {
            safeOrderService.exportDetail(list,response);
        }catch (Exception e){
            log.error("导出保险订单列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "导出保险订单列表异常!");
        }
        return msg;
    }
}
