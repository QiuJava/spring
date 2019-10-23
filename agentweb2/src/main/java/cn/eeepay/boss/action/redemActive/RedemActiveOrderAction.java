package cn.eeepay.boss.action.redemActive;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.ResponseBean;
import cn.eeepay.framework.model.redemActive.RedemActiveOrderBean;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.service.redemActive.RedemActiveOrderService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 积分兑换订单查询
 * Created by 666666 on 2018/5/7.
 */
@Controller
@RequestMapping("/redemActiveOrderAction")
public class RedemActiveOrderAction {

    @Resource
    private RedemActiveOrderService redemActiveOrderService;
    @Resource
    private AgentInfoService agentInfoService;

    @RequestMapping("/listDeclareOrder")
    @ResponseBody
    public ResponseBean listDeclareOrder(@RequestBody RedemActiveOrderBean bean,
                                         @RequestParam(defaultValue = "1") int pageNo,
                                         @RequestParam(defaultValue = "10") int pageSize){
        try {
            Page<RedemActiveOrderBean> page = new Page<>(pageNo, pageSize);
            AgentInfo loginAgent = agentInfoService.selectByPrincipal();
            List<RedemActiveOrderBean> result = redemActiveOrderService.listDeclareOrder(bean, loginAgent, page);
            RedemActiveOrderBean redemActiveOrderBean = redemActiveOrderService.countDeclareOrder(bean, loginAgent);
            Map<String, Object> resutMap = new HashedMap();
            resutMap.put("list", result);
            resutMap.put("count", redemActiveOrderBean);
            return new ResponseBean(resutMap, page.getTotalCount());
        }catch (Exception e){
            return new ResponseBean(e);
        }
    }

    @RequestMapping("/listReceiveOrder")
    @ResponseBody
    public ResponseBean listReceiveOrder(@RequestBody RedemActiveOrderBean bean,
                                         @RequestParam(defaultValue = "1") int pageNo,
                                         @RequestParam(defaultValue = "10") int pageSize){
        try {
            Page<RedemActiveOrderBean> page = new Page<>(pageNo, pageSize);
            AgentInfo loginAgent = agentInfoService.selectByPrincipal();
            List<RedemActiveOrderBean> result = redemActiveOrderService.listReceiveOrder(bean, loginAgent, page);
            RedemActiveOrderBean redemActiveOrderBean = redemActiveOrderService.countReceiveOrder(bean, loginAgent);
            Map<String, Object> resutMap = new HashedMap();
            resutMap.put("list", result);
            resutMap.put("count", redemActiveOrderBean);
            return new ResponseBean(resutMap, page.getTotalCount());
        }catch (Exception e){
            return new ResponseBean(e);
        }
    }

    @RequestMapping("/listRepayOrder")
    @ResponseBody
    public ResponseBean listRepayOrder(@RequestBody RedemActiveOrderBean bean,
                                         @RequestParam(defaultValue = "1") int pageNo,
                                         @RequestParam(defaultValue = "10") int pageSize){
        try {
            Page<RedemActiveOrderBean> page = new Page<>(pageNo, pageSize);
            AgentInfo loginAgent = agentInfoService.selectByPrincipal();
            List<RedemActiveOrderBean> result = redemActiveOrderService.listRepayOrder(bean, loginAgent, page);
            RedemActiveOrderBean redemActiveOrderBean = redemActiveOrderService.countRepayOrder(bean, loginAgent);
            Map<String, Object> resutMap = new HashedMap();
            resutMap.put("list", result);
            resutMap.put("count", redemActiveOrderBean);
            return new ResponseBean(resutMap, page.getTotalCount());
        }catch (Exception e){
            return new ResponseBean(e);
        }
    }

    @RequestMapping("/listShareOrder")
    @ResponseBody
    public ResponseBean listShareOrder(@RequestBody RedemActiveOrderBean bean,
                                       @RequestParam(defaultValue = "1") int pageNo,
                                       @RequestParam(defaultValue = "10") int pageSize){
        try {
            Page<RedemActiveOrderBean> page = new Page<>(pageNo, pageSize);
            AgentInfo loginAgent = agentInfoService.selectByPrincipal();
            List<RedemActiveOrderBean> result = redemActiveOrderService.listShareOrder(bean, loginAgent, page);
            return new ResponseBean(result, page.getTotalCount());
        }catch (Exception e){
            return new ResponseBean(e);
        }
    }
    @RequestMapping("/listOrgCode")
    @ResponseBody
    public ResponseBean listOrgCode(){
        try {
            return new ResponseBean(redemActiveOrderService.listOrgCode());
        }catch (Exception e){
            return new ResponseBean(e);
        }
    }
}
