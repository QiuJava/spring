package cn.eeepay.boss.action;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.model.redemActive.MerInfoTotal;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.service.RedemMerchantService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by 666666 on 2018/5/9.
 */
@RequestMapping("/redemMerchant")
@Controller
public class RedemMerchantAction {

    @Resource
    private RedemMerchantService redemMerchantService;

    @Resource
    private AgentInfoService agentInfoService;

    /**
     * 用户管理查询
     * @return
     * @throws Exception
     */
    @RequestMapping("/selectByUserRedemMerchant")
    @ResponseBody
    public ResponseBean selectByUserRedemMerchant(@RequestBody RedemMerchantBean userRedemMerchant,
                                            @RequestParam(defaultValue = "1") int pageNo,
                                            @RequestParam(defaultValue = "10") int pageSize) {
        try{
            Page<ResponseBean> page = new Page<>(pageNo, pageSize);
            AgentInfo loginAgent = agentInfoService.selectByPrincipal();
            List<RedemMerchantBean> result = redemMerchantService.selectByUserRedemMerchant(userRedemMerchant, loginAgent, page);
            return new ResponseBean(result, page.getTotalCount());
        }catch(Exception e){
            return new ResponseBean(e);
        }
    }

    @RequestMapping("/selectSum")
    @ResponseBody
    public ResponseBean selectSum(@RequestBody RedemMerchantBean userRedemMerchant,
                                             @RequestParam(defaultValue = "1") int pageNo,
                                             @RequestParam(defaultValue = "10") int pageSize) {
        try{
            Page<ResponseBean> page = new Page<>(pageNo, pageSize);
            AgentInfo loginAgent = agentInfoService.selectByPrincipal();
            MerInfoTotal result = redemMerchantService.selectSum(userRedemMerchant, loginAgent, page);
            return new ResponseBean(result);
        }catch(Exception e){
            return new ResponseBean(e);
        }
    }

    @RequestMapping("/queryAddMerchantInfo")
    @ResponseBody
    public ResponseBean queryAddMerchantInfo(String merchantNo) {
        try {
            AgentInfo loginAgent = agentInfoService.selectByPrincipal();
            return new ResponseBean(redemMerchantService.queryAddMerchantInfo(merchantNo, loginAgent));
        }catch (Exception e){
            return new ResponseBean(e);
        }
    }

    @RequestMapping("/queryUpdateMerchantInfo")
    @ResponseBody
    public ResponseBean queryUpdateMerchantInfo(String merchantNo) {
        try {
            AgentInfo loginAgent = agentInfoService.selectByPrincipal();
            return new ResponseBean(redemMerchantService.queryUpdateMerchantInfo(merchantNo, loginAgent));
        }catch (Exception e){
            return new ResponseBean(e);
        }
    }

    @RequestMapping("/userAddAgent")
    @ResponseBody
    @SystemLog(description = "超级兑分享版开通二级代理商")
    public ResponseBean userAddAgent(@RequestBody RedemMerchantBean userRedemMerchant) {
        try {
            AgentInfo loginAgent = agentInfoService.selectByPrincipal();
            return new ResponseBean(redemMerchantService.userAddAgent(userRedemMerchant, loginAgent));
        }catch (Exception e){
            return new ResponseBean(e);
        }
    }
    @RequestMapping("/userUpdateAgent")
    @ResponseBody
    @SystemLog(description = "超级兑分享版修改二级代理商")
    public ResponseBean userUpdateAgent(@RequestBody RedemMerchantBean userRedemMerchant) {
        try {
            AgentInfo loginAgent = agentInfoService.selectByPrincipal();
            return new ResponseBean(redemMerchantService.userUpdateAgent(userRedemMerchant, loginAgent));
        }catch (Exception e){
            return new ResponseBean(e);
        }
    }

    @RequestMapping("/queryMerchantDetails")
    @ResponseBody
    public ResponseBean queryMerchantDetails(String merchantNo) {
        try {
            AgentInfo loginAgent = agentInfoService.selectByPrincipal();
            return new ResponseBean(redemMerchantService.queryMerchantDetails(merchantNo, loginAgent));
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
            List<Map<String, Object>> result = redemMerchantService.listBalanceHis(startTime, endTime, service, merchantNo, page);
            return new ResponseBean(result, page.getTotalCount());
        }catch (Exception e){
            return new ResponseBean(e);
        }
    }
}
