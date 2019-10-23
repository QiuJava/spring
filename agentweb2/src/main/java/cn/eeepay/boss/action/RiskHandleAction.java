package cn.eeepay.boss.action;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.UserEntityInfo;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.model.blackAgent.BlackInfo;
import cn.eeepay.framework.model.blackAgent.RiskNewAnswer;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.service.BlackDataService;
import cn.eeepay.framework.service.MerchantInfoService;
import cn.eeepay.framework.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 黑名单
 * <p>
 * by zhangcheng
 * <p>
 * 2019年8月24日13:45:54
 */

@Controller
@RequestMapping(value = "/riskHandle")
public class RiskHandleAction {
    private static final Logger log = LoggerFactory.getLogger(RiskHandleAction.class);

    @Resource
    public AgentInfoService agentInfoService;
    @Resource
    private BlackDataService blackDataService;
    @Resource
    private MerchantInfoService merchantInfoService;

    //条件查询
    @RequestMapping(value = "/blackDataQuery")
    public @ResponseBody
    Object selectByCondition(@RequestParam("info") String param, @ModelAttribute("page") Page<BlackInfo> page) throws Exception {
        log.info("开始模糊查询");
        Map<String, Object> jsonMap = new HashMap<>();
        List<BlackInfo> list = null;
        BlackInfo blackInfo = JSON.parseObject(param, BlackInfo.class);
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserEntityInfo userEntityInfo = principal.getUserEntityInfo();
        try {
            if (StringUtils.isNotEmpty(blackInfo.getMerchantName())) {
                blackInfo.setMerchantName("%" + blackInfo.getMerchantName() + "%");
            }
            if (StringUtils.isNotEmpty(blackInfo.getAgentName())) {
                blackInfo.setAgentName("%" + blackInfo.getAgentName() + "%");
            }
            AgentInfo ais;
            if (StringUtils.isNotEmpty(blackInfo.getAgentNo())){
                //agentNo 不为空时查询的是 代理商下的直属商户
                ais = agentInfoService.selectByagentNo(blackInfo.getAgentNo());

/*
                if (ais==null){
                    jsonMap.put("result", false);
                    jsonMap.put("msg", "请输入正确的所属代理商编号");
                    return jsonMap;
                }
*/

                if (ais != null) {

                AgentInfo agentInfo = agentInfoService.selectByagentNo(userEntityInfo.getEntityId());
                if (agentInfo.getAgentLevel() != 1){
                    if ( agentInfo.getAgentLevel()>ais.getAgentLevel()){}
                        ais = agentInfoService.selectByagentNo(userEntityInfo.getEntityId());
                    }
                String agentNode = merchantInfoService.selectParentNodeByAgentNo(ais.getAgentNo());
                if (StringUtils.isNotBlank(agentNode)){
                    blackInfo.setAgentNo(agentNode);
                }else {
                    jsonMap.put("result", false);
                    jsonMap.put("msg", "您没有直营商户");
                    return jsonMap;
                }
                }
            }else {
                ais = agentInfoService.selectByagentNo(userEntityInfo.getEntityId());
                if (ais != null) {
                    //以及代理商
                    if (ais.getAgentLevel() == 1) {//包含下级
                        blackInfo.setAgentNo(ais.getAgentNode() + "%");
                    }else{
                        String agentNode = merchantInfoService.selectParentNodeByAgentNo(ais.getAgentNo());
                        if (StringUtils.isNotBlank(agentNode)){
                            blackInfo.setAgentNo(agentNode);
                        }else {
                            jsonMap.put("result", false);
                            jsonMap.put("msg", "您没有直营商户");
                            return jsonMap;
                        }
                    }
                }
            }

            list = blackDataService.selectByParam(page, blackInfo);
            jsonMap.put("list", page);
            jsonMap.put("result", true);
        } catch (Exception e) {
            log.error("黑名单条件查询出错-----", e);
            jsonMap.put("result", false);
            jsonMap.put("list", page);
        }
        log.info("222page.getPageNo()=" + page.getPageNo() + "--page.getPageSize()=" + page.getPageSize() + "--page.getTotalPages()=" + page.getTotalPages());
        return jsonMap;
    }

    @RequestMapping(value = "/newestanswer")
    @ResponseBody
    public Object selectNewestanswer(@Param("orderNo") String orderNo) {
        log.info("获取触犯风控事件");
        RiskNewAnswer riskNewAnswer = null;
        try {
            riskNewAnswer = blackDataService.selectRiskNewAnwser(orderNo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return riskNewAnswer;
    }

    @RequestMapping(value = "/handle")
    @ResponseBody
    public Object handle(@Param("orderNo") String orderNo, @Param("replyFilesName") String replyFilesName, @Param("replyRemark") String replyRemark) {
        log.info("开始处理");
        String s = null;
        try {
            s = blackDataService.agentReply(orderNo, replyFilesName, replyRemark);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }


    /**
     * 获取资料详情
     * @param orderNo
     * @return
     */
    @RequestMapping("/handleDetail")
    @ResponseBody
    public Map<String, Object> getDetailTwo(@Param("orderNo") String orderNo){
        log.info("开始查询回复详情");
        Map<String, Object> result = blackDataService.selectRiskHandleDetail(orderNo);
        return result;
    }

}
