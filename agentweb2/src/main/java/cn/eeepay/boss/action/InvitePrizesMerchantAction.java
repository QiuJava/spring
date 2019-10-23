package cn.eeepay.boss.action;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.InvitePrizesMerchantBean;
import cn.eeepay.framework.model.ResponseBean;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.service.InvitePrizesMerchantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/19.
 */
@Controller
@RequestMapping("/invitePrizesMerchant")
public class InvitePrizesMerchantAction {

    protected static final Logger LOGGER = LoggerFactory.getLogger(InvitePrizesMerchantAction.class);
    @Resource
    private InvitePrizesMerchantService invitePrizesMerchantService;
    @Resource
    private AgentInfoService agentInfoService;

    @ResponseBody
    @RequestMapping("/listInvitePrizesMerchant")
    public ResponseBean listInvitePrizesMerchant(@RequestBody InvitePrizesMerchantBean bean,
                                                 @RequestParam(defaultValue = "1") int pageNo,
                                                 @RequestParam(defaultValue = "10")int pageSize){
        try {
            final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String loginAgentNo = principal.getUserEntityInfo().getEntityId();
            Page<InvitePrizesMerchantBean> page = new Page<>(pageNo, pageSize);
            List<InvitePrizesMerchantBean> result = invitePrizesMerchantService.listInvitePrizesMerchant(bean,loginAgentNo, page);
            AgentInfo agentInfo = agentInfoService.selectByPrincipal();
            int agentLevel = agentInfo.getAgentLevel();
            if (agentLevel >= 2){
                for (InvitePrizesMerchantBean info : result){
                    info.setPrizesAmount("**");
                }
            }
            return new ResponseBean(result, page.getTotalCount());
        }catch (Exception e){
            LOGGER.error("异常信息: " , e);
            return new ResponseBean(e);
        }
    }

    @ResponseBody
    @RequestMapping("/countInvitePrizesMerchant")
    public ResponseBean countInvitePrizesMerchant(@RequestBody InvitePrizesMerchantBean bean){
        try {
            AgentInfo agentInfo = agentInfoService.selectByPrincipal();
            int agentLevel = agentInfo.getAgentLevel();
            if (agentLevel >= 2){
                Map<String, String> tempResultMap = new HashMap<>();
                tempResultMap.put("recordedAmount", "**");
                tempResultMap.put("sumAmount", "**");
                tempResultMap.put("unrecordedAmount", "**");
                return new ResponseBean(tempResultMap);
            }
            final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String loginAgentNo = principal.getUserEntityInfo().getEntityId();
            Map<String,String> resultMap = invitePrizesMerchantService.countInvitePrizesMerchant(bean,loginAgentNo);
            return new ResponseBean(resultMap);
        }catch (Exception e){
            LOGGER.error("异常信息: " , e);
            return new ResponseBean(e);
        }
    }
}
