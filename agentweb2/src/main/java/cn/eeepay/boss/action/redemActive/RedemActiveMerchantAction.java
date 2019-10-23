package cn.eeepay.boss.action.redemActive;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.ResponseBean;
import cn.eeepay.framework.model.redemActive.RedemActiveMerchantBean;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.service.redemActive.RedemActiveMerchantService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by 666666 on 2018/5/16.
 */
@Controller
@RequestMapping("/redemActiveMerchantAction")
public class RedemActiveMerchantAction {

    @Resource
    private RedemActiveMerchantService redemActiveMerchantService;
    @Resource
    private AgentInfoService agentInfoService;

    @RequestMapping("/listMerchants")
    @ResponseBody
    public ResponseBean listMerchants(@RequestBody RedemActiveMerchantBean bean,
                                      @RequestParam(defaultValue = "1") int pageNo,
                                      @RequestParam(defaultValue = "20") int pageSize){
        try {
            AgentInfo loginAgent = agentInfoService.selectByPrincipal();
            Page<RedemActiveMerchantBean> page = new Page<>(pageNo, pageSize);
            List<RedemActiveMerchantBean> result = redemActiveMerchantService.listMerchant(bean, loginAgent, page);
            return new ResponseBean(result, page.getTotalCount());
        }catch (Exception e){
            return new ResponseBean(e);
        }
    }

    @RequestMapping("/queryMerchantDetails")
    @ResponseBody
    public ResponseBean queryMerchantDetails(String merchantNo) {
        try {
            AgentInfo loginAgent = agentInfoService.selectByPrincipal();
            return new ResponseBean(redemActiveMerchantService.queryMerchantDetails(merchantNo, loginAgent));
        }catch (Exception e){
            return new ResponseBean(e);
        }
    }

    @RequestMapping("/listBalanceHis")
    @ResponseBody
    public ResponseBean listBalanceHis(@RequestBody Map<String, String> param){
        try {
            int pageNo = Integer.valueOf(param.get("pageNo"));
            int pageSize = Integer.valueOf(param.get("pageSize"));
            String startTime = param.get("startTime");
            String endTime = param.get("endTime");
            String service = param.get("service");
            String merchantNo = param.get("merchantNo");
            Page<List<Map<String, Object>>> page = new Page<>(pageNo, pageSize);
            List<Map<String, Object>> result = redemActiveMerchantService.listBalanceHis(startTime, endTime, service, merchantNo, page);
            return new ResponseBean(result, page.getTotalCount());
        }catch (Exception e){
            return new ResponseBean(e);
        }
    }
}
