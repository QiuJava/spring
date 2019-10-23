package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.AgentInfoDao;
import cn.eeepay.framework.dao.AgentShareDao;
import cn.eeepay.framework.exception.AgentWebException;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.service.AgentShareTaskService;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service("agentShareTaskService")
public class AgentShareTaskServiceImpl implements AgentShareTaskService {
    private static final Logger log = LoggerFactory.getLogger(AgentShareTaskServiceImpl.class);
    @Resource
    private AgentShareDao agentShareDao;
    @Resource
    private AgentInfoService agentInfoService;
    @Resource
    private AgentInfoDao agentInfoDao;

    @Override
    public int insertAgentShareList(AgentShareRuleTask share) {
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String entityId = principal.getUserEntityInfo().getEntityId();
        AgentInfo agentInfo = agentInfoService.selectByagentNo(entityId);

        AgentShareRule agentShareRule = new AgentShareRule();
//		agentShareRule.setProfitType(share.getProfitType());
        // 现在的分润类型只剩下第5种
        agentShareRule.setProfitType(5);
        agentShareRule.setIncome(share.getIncome());
        agentShareRule.setCost(share.getCost());
        agentShareRule.setLadderRate(share.getLadderRate());
        agentShareRule.setEfficientDate(share.getEfficientDate());
        agentShareRule.setShareProfitPercent(share.getShareProfitPercent());
        Long shareId = share.getShareId();
        AgentShareRule ruleHistory = agentShareDao.selectByShareId(shareId);
        // 如果是提现业务
        if (StringUtils.equalsIgnoreCase(share.getServiceType() + "", "10000") ||
                StringUtils.equalsIgnoreCase(share.getServiceType() + "", "10001")) {
            share.setCostRateType("1");
            share.setPerFixCost(new BigDecimal(share.getCost()));
            share.setCostHistory(ruleHistory.getPerFixCost().setScale(2,BigDecimal.ROUND_HALF_UP));
        } else {
            share.setCostRateType("2");
            share.setCostRate(new BigDecimal(share.getCost()));
            share.setCostHistory(ruleHistory.getCostRate().setScale(2,BigDecimal.ROUND_HALF_UP));
        }
        share.setCheckStatus(1);
        int count = agentShareDao.queryAgentShareListTaskByEfficientDate(shareId, share.getEfficientDate());
        if (count >= 1) {
            throw new AgentWebException("已经存在相同生效日期的分润记录.");
        }
        AgentShareRule parentShareRule = agentShareDao.getSameTypeParentAgentShare(shareId);
        if (parentShareRule == null) {
            throw new AgentWebException("上级代理商该分润规则没有配置");
        }
        isChildrenRuleLessThanParent(parentShareRule, share);
        compareServiceRate(parentShareRule, share, agentInfo.getOneLevelId(), agentShareDao.findAgentNo(shareId));
        int selectKey = agentShareDao.insertAgentShareListTask(share);
        log.info("插入task表返回主键 {}",share.getId());

        //========= start ====== tgh 需求新增操作: 插入修改记录 =========
        log.info("根据分润id {} 查出分润历史分润信息 {}",shareId,ruleHistory);
        share.setShareProfitPercentHistory(ruleHistory.getShareProfitPercent());
        ProfitUpdateRecord record = new ProfitUpdateRecord();
        record.setShareId(shareId.toString());
        if ("1".equals(share.getCostRateType())){
            record.setCostHistory(share.getCostHistory() == null ? "" : share.getCostHistory() + "元");
            record.setCost((new BigDecimal(share.getCost()).setScale(2, BigDecimal.ROUND_HALF_UP)) + "元");
        }else if("2".equals(share.getCostRateType())){
            record.setCostHistory(share.getCostHistory() == null ? "" : share.getCostHistory() + "%");
            record.setCost((new BigDecimal(share.getCost()).setScale(2, BigDecimal.ROUND_HALF_UP)) + "%");
        }
        record.setShareProfitPercentHistory(share.getShareProfitPercentHistory());
        record.setShareProfitPercent(share.getShareProfitPercent());
        record.setEfficientDate(share.getEfficientDate());
        record.setEffectiveStatus("0");
        record.setAuther(agentInfo.getAgentNo());
        record.setShareTaskId(share.getId());
        agentShareDao.insertShareUpdateRecord(record);
        //=========== end ==================

        // 之后判断插入的记录是否为队长的记录,如果是,则队员相应也要增加
        Long leaderShareId = share.getShareId();
        List<Long> bpIds = agentShareDao.queryMemberBpId(leaderShareId);
        if (!CollectionUtils.isEmpty(bpIds)) {
            for (Long memberBpId : bpIds) {
                Long memberShareId = agentShareDao.getMemberShareId(memberBpId, leaderShareId);
                if (memberShareId != null) {
                    share.setShareId(memberShareId);
                    agentShareDao.insertAgentShareListTask(share);
                }
            }
        }
        return selectKey;
    }

    private void compareServiceRate(AgentShareRule parentShareRule, AgentShareRuleTask rule, String oneLevelId, String agentNo) {
        Map<String, Object> serviceRateMap = agentInfoDao.getSameTypeRootAgentMinServiceRate(rule, oneLevelId, agentNo);
        if (serviceRateMap == null || serviceRateMap.size() == 0) {
            throw new AgentWebException("该代理商的服务费率没有配置.");
        }
        String serviceName = MapUtils.getString(serviceRateMap, "service_name");
        String bpName = MapUtils.getString(serviceRateMap, "bp_name");
        BigDecimal rate = new BigDecimal(MapUtils.getString(serviceRateMap, "rate"));
        BigDecimal singleNumAmount = new BigDecimal(MapUtils.getString(serviceRateMap, "single_num_amount"));
        String isTx = MapUtils.getString(serviceRateMap, "isTx");
        if (StringUtils.equals(isTx, "1")) {
            if (rule.getPerFixCost().compareTo(singleNumAmount) > 0) {
                throw new AgentWebException("代理商(" + bpName + "-" + serviceName + ")的分润成本"
                        + rule.getPerFixCost().setScale(4) + " 元高于服务费率 " + singleNumAmount.setScale(4) + "元");
            }
        } else {
            if (rule.getCostRate().compareTo(rate) > 0) {
                throw new AgentWebException("代理商(" + bpName + "-" + serviceName + ")的分润成本"
                        + rule.getCostRate().setScale(4) + " %高于服务费率 " + rate.setScale(4) + " %");
            }
        }
        compareServiceRateNew(parentShareRule,rule,oneLevelId,agentNo);
    }

    private void compareServiceRateNew(AgentShareRule parentShareRule, AgentShareRuleTask rule, String oneLevelId, String agentNo) {




        Map<String, Object> serviceRateMap = agentInfoDao.getSameTypeRootAgentMaxServiceRate(parentShareRule, oneLevelId, agentNo);
        if (serviceRateMap == null || serviceRateMap.size() == 0) {
            throw new AgentWebException("该代理商的服务费率没有配置.");
        }

        log.info("parentShareRule="+parentShareRule);
        log.info("serviceRateMap="+serviceRateMap);
        log.info("rule="+rule);


        String cardType = MapUtils.getString(serviceRateMap, "card_type");
        String holidaysMark = MapUtils.getString(serviceRateMap, "holidays_mark");
        BigDecimal rate = new BigDecimal(MapUtils.getString(serviceRateMap, "rate"));
        BigDecimal singleNumAmount = new BigDecimal(MapUtils.getString(serviceRateMap, "single_num_amount"));
        String isTx = MapUtils.getString(serviceRateMap, "isTx");
        String bpStr = "(" + parentShareRule.getBpName() + "-" + parentShareRule.getServiceName() + ")";
        if (StringUtils.equals(isTx, "1")) {
            Map<String, Object> agentShareRuleMap = agentInfoDao.selectAgentShareRule((String) serviceRateMap.get("agent_no"),
                    rule.getServiceId(), cardType, holidaysMark);
            log.info("agentShareRuleMap="+agentShareRuleMap);
            if (agentShareRuleMap == null) {
                throw new AgentWebException(bpStr + "上级代理商没有配置此产品");
            }
            if (agentShareRuleMap.get("per_fix_cost") == null || agentShareRuleMap.get("share_profit_percent") == null) {
                throw new AgentWebException(bpStr + "上级代理商成本扣率或者分润百分比没有配置");
            }
            BigDecimal aResult = (singleNumAmount.subtract(parentShareRule.getPerFixCost())).multiply(parentShareRule.getShareProfitPercent());
            BigDecimal myCostRate = rule.getPerFixCost();
            BigDecimal myShareProfitPercent = rule.getShareProfitPercent();
            BigDecimal bResult = (singleNumAmount.subtract(myCostRate).multiply(myShareProfitPercent));
            if (aResult.compareTo(bResult) < 0) {
                throw new AgentWebException(bpStr + "下级提现分润大于上级，请重新设置");
            }

        } else {
            //在原判断逻辑上，在修改代理商固定分润百分比时，
            // 被修改的代理商(商户签约扣率 - 代理商成本扣率) * 代理商分润百分比
            // 不能高于上级的(商户签约扣率 - 代理商成本扣率) * 代理商分润百分比，
            // 如：1级代理商设置2级代理商的分润百分比时，设置的不能高于1级的，以此类推

            Map<String, Object> agentShareRuleMap = agentInfoDao.selectAgentShareRule(agentNo, rule.getServiceId(), cardType, holidaysMark);
            log.info("agentShareRuleMap="+agentShareRuleMap);
            if (agentShareRuleMap == null) {
                throw new AgentWebException(bpStr + "上级代理商没有配置此产品");
            }
            if (agentShareRuleMap.get("cost_rate") == null || agentShareRuleMap.get("share_profit_percent") == null) {
                throw new AgentWebException(bpStr + "上级代理商成本扣率或者分润百分比没有配置");
            }
            BigDecimal aResult = (rate.subtract(rule.getCostRate())).multiply(rule.getShareProfitPercent());
            BigDecimal myCostRate = parentShareRule.getCostRate();
            BigDecimal myShareProfitPercent = parentShareRule.getShareProfitPercent();
            BigDecimal bResult = (rate.subtract(myCostRate).multiply(myShareProfitPercent));
            if (aResult.compareTo(bResult) > 0) {
                throw new AgentWebException(bpStr + "下级交易分润大于上级，请重新设置");
            }
        }
    }


    private void isChildrenRuleLessThanParent(AgentShareRule parentAgentShareRule, AgentShareRuleTask share) {
        if (parentAgentShareRule.getProfitType() != 5) {
            throw new AgentWebException("代理商(" + parentAgentShareRule.getAgentNo() +
                    ")服务(" + parentAgentShareRule.getServiceId() +
                    ")的分润类型不是固定成本类型");
        }
        if (!StringUtils.equals(parentAgentShareRule.getCostRateType(), share.getCostRateType())) {
            throw new AgentWebException("代理商(" + parentAgentShareRule.getAgentNo() +
                    ")服务(" + parentAgentShareRule.getServiceId() +
                    ")与下级代理商分润类型不一致");
        }

        if (StringUtils.equals(parentAgentShareRule.getCostRateType(), "1")) {                        // 1-每笔固定金额，
            if (share.getPerFixCost().compareTo(parentAgentShareRule.getPerFixCost()) < 0) {
                throw new AgentWebException("代理商的分润成本 " + share.getPerFixCost() +
                        "元比上级代理商(" + parentAgentShareRule.getAgentNo() + ")的成本" +
                        parentAgentShareRule.getPerFixCost() + " 元低");
            }
        } else if (StringUtils.equals(parentAgentShareRule.getCostRateType(), "2")) {                // 2 扣率
            if (share.getCostRate().compareTo(parentAgentShareRule.getCostRate()) < 0) {
                throw new AgentWebException("代理商的分润成本 " + share.getCostRate() +
                        "%比上级代理商(" + parentAgentShareRule.getAgentNo() + ")的成本" +
                        parentAgentShareRule.getCostRate() + " %低");
            }
        } else {                                                                                            // 其他异常
            throw new AgentWebException("代理商的分润成本与上级代理商(" + parentAgentShareRule.getAgentNo() + ")的类型不一致");
        }
    }

    @Override
    public int deleteAgentShareTask(Integer id) {
        AgentShareRuleTask agentShareRuleTask = agentShareDao.queryAgentShareListTask(id);
        if (agentShareRuleTask == null) {
            return 0;
        }
        List<Long> bpIds = agentShareDao.queryMemberBpId(agentShareRuleTask.getShareId());
        if (!CollectionUtils.isEmpty(bpIds)) {
            for (Long memberBpId : bpIds) {
                Long memberShareId = agentShareDao.getMemberShareId(memberBpId, agentShareRuleTask.getShareId());
                if (memberShareId != null) {
                    Integer memberAgentShareTaskId = agentShareDao.getMemberAgentShareTaskId(memberShareId, id);
                    log.info("待删除队员分润task id:" + memberAgentShareTaskId.toString());
                    agentShareDao.deleteAgentShareTask(memberAgentShareTaskId);
//					deleteMemberAgentShareTask(memberAgentShareTaskId);
                }
            }
        }
        log.info("待删除队长分润task id:" + id);
        return agentShareDao.deleteAgentShareTask(id);
    }

//	private void deleteMemberAgentShareTask(List<Integer> memberAgentShareTaskId) {
//		if (!CollectionUtils.isEmpty(memberAgentShareTaskId)){
//			for (Integer id : memberAgentShareTaskId){
//				agentShareDao.deleteAgentShareTask(id);
//			}
//		}
//	}

    @Override
    public List<AgentShareRuleTask> getAgentShareRuleTask(Integer shareId) {
        List<AgentShareRuleTask> list = agentShareDao.getAgentShareRuleTask(shareId);
        for (AgentShareRuleTask task : list) {
            AgentShareRule agentShareRule = new AgentShareRule();
            BeanUtils.copyProperties(task, agentShareRule);
            agentInfoService.profitExpression(agentShareRule);
            task.setIncome(agentShareRule.getIncome());
            task.setCost(agentShareRule.getCost());
            task.setLadderRate(agentShareRule.getLadderRate());
        }
        return list;
    }

    @Override
    public AgentShareRule queryByAgentNoAndServiceId(String agentNo, String serviceId) {
        return agentShareDao.queryByAgentNoAndServiceId(agentNo, serviceId);
    }

    @Override
    public List<AgentShareRule> getAgentShareList(String param) {
        return agentShareDao.getAgentShareList(param);
    }
}
