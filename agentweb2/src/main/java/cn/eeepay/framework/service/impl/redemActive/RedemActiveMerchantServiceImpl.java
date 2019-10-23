package cn.eeepay.framework.service.impl.redemActive;

import cn.eeepay.framework.dao.AgentInfoDao;
import cn.eeepay.framework.daoRedem.redemActive.RedemActiveMerchantDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.exception.AgentWebException;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.redemActive.RedemActiveMerchantBean;
import cn.eeepay.framework.service.redemActive.RedemActiveMerchantService;
import cn.eeepay.framework.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by 666666 on 2018/5/16.
 */
@Service
public class RedemActiveMerchantServiceImpl implements RedemActiveMerchantService {
    @Resource
    private RedemActiveMerchantDao redemActiveMerchantDao;
    @Resource
    private AgentInfoDao agentInfoDao;

    @Override
    public List<RedemActiveMerchantBean> listMerchant(RedemActiveMerchantBean bean, AgentInfo loginAgent, Page<RedemActiveMerchantBean> page) {
        List<RedemActiveMerchantBean> result = redemActiveMerchantDao.listMerchant(bean, loginAgent, page);
//        if (result != null && !result.isEmpty()){
//            for (RedemActiveMerchantBean item : result){
//                item.setMobileUsername(StringUtil.filterNull(item.getMobileUsername()).replaceAll("^(.{3}).*?(.{4})$", "$1****$2"));
//            }
//        }
        return result;
    }

    @Override
    public Map<String, Object> queryMerchantDetails(String merchantNo, AgentInfo loginAgent) {
        Map<String, Object> merchantInfo = redemActiveMerchantDao.queryMerchantInfo(merchantNo);
        if (merchantInfo == null || merchantInfo.isEmpty()){
            throw new AgentWebException("查不到该商户的信息");
        }
        String agent_node = StringUtil.filterNull(merchantInfo.get("agent_node"));
        if (StringUtils.isBlank(agent_node) || !agent_node.startsWith(loginAgent.getAgentNode())){
            throw new AgentWebException("您无权查看该商户的信息");
        }
        String mobile_username = StringUtil.filterNull(merchantInfo.get("mobile_username"));
        String business_code = StringUtil.filterNull(merchantInfo.get("business_code"));
        String mobile_no = StringUtil.filterNull(merchantInfo.get("mobile_no"));
        String account_no = StringUtil.filterNull(merchantInfo.get("account_no"));
        merchantInfo.put("business_code", business_code.replaceAll("^(.{4}).*?(.{4})$", "$1****$2"));
//        merchantInfo.put("mobile_no", mobile_no.replaceAll("^(.{3}).*?(.{4})$", "$1****$2"));
//        merchantInfo.put("mobile_username", mobile_username.replaceAll("^(.{3}).*?(.{4})$", "$1****$2"));
        merchantInfo.put("account_no", account_no.replaceAll("^(.{4}).*?(.{4})$", "$1****$2"));
        String agent_no = StringUtil.filterNull(merchantInfo.get("agent_no"));
        String one_agent_no = StringUtil.filterNull(merchantInfo.get("one_agent_no"));
        AgentInfo agentInfo = agentInfoDao.selectByAgentNo(agent_no);
        AgentInfo oneAgentInfo = agentInfoDao.selectByAgentNo(one_agent_no);
        merchantInfo.put("agent_name", agentInfo == null ? "" : agentInfo.getAgentName());
        merchantInfo.put("one_agent_name", oneAgentInfo == null ? "" : oneAgentInfo.getAgentName());
        return merchantInfo;
    }

    @Override
    public List<Map<String, Object>> listBalanceHis(String startTime, String endTime, String service, String merchantNo, Page<List<Map<String, Object>>> page) {
        return redemActiveMerchantDao.listBalanceHis(startTime, endTime, service, merchantNo, page);
    }
}
