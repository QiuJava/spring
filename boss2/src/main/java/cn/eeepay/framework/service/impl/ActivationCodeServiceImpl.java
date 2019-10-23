package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.ActivationCodeDao;
import cn.eeepay.framework.dao.AgentInfoDao;
import cn.eeepay.framework.dao.ProviderDao;
import cn.eeepay.framework.dao.SysDictDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.ActivationCodeBean;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.RepayOemServiceCostBean;
import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.service.ActivationCodeService;
import cn.eeepay.framework.util.BossBaseException;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by 666666 on 2017/10/26.
 */
@Service
public class ActivationCodeServiceImpl implements ActivationCodeService {

    @Resource
    private ActivationCodeDao activationCodeDao;
    @Resource
    private AgentInfoDao agentInfoDao;
    @Resource
    private SysDictDao sysDictDao;
    @Resource
    private ProviderDao providerDao;

    private static Lock buildLock = new ReentrantLock();

    @Override
    public Map<String, String> buildActivationCode(int count) {
        if(buildLock.tryLock()){
            try{
                int maxCount = getIntValueFromSysDIct("ACTIVATION_CODE_BUILD_MAX_COUNT", 10000);
                if (count <= 0 || count > maxCount){
                    throw new BossBaseException("输入个数必须大于0且小于" + maxCount);
                }
                Map<String, String> resultMap = new HashedMap();
                ActivationCodeBean minBean = new ActivationCodeBean();
                ActivationCodeBean maxBean = new ActivationCodeBean();
                activationCodeDao.insert(minBean);
                resultMap.put("minId", minBean.getId() + "");
                if (count == 1){
                    resultMap.put("maxId", minBean.getId() + "");
                    return resultMap;
                }
                if (count > 2){
                    batchInsert(count - 2);
                }
                activationCodeDao.insert(maxBean);
                resultMap.put("maxId", maxBean.getId() + "");
                return resultMap;
            } finally {
                buildLock.unlock();
            }
        }else{
            throw new BossBaseException("激活码正在生成中,请稍后再试.");
        }
    }

    private int getIntValueFromSysDIct(String key, int defaultValue) {
        SysDict dict = sysDictDao.getByKey("YFB_ACTIVATION_CODE_BUILD_MAX_COUNT");
        if (dict == null){
            return defaultValue;
        }
        try {
            Integer result = Integer.valueOf(dict.getSysValue());
            if (result <= 0){
                return defaultValue;
            }else{
                return result;
            }
        }catch (NumberFormatException e){
            return defaultValue;
        }
    }

    private void batchInsert(int sumCount) {
        if (sumCount <= 0){
            return;
        }
        int maxCountPerTime = getIntValueFromSysDIct("YFB_ACTIVATION_CODE_BUILD_MAX_COUNT_PER_TIME", 1000);
        int count = sumCount / maxCountPerTime;
        for (int i = 0; i < count; i ++){
            activationCodeDao.batchInsert(maxCountPerTime);
            sumCount -= maxCountPerTime;
        }
        if (sumCount > 0){
            activationCodeDao.batchInsert(sumCount);
        }
    }

    @Override
    public List<ActivationCodeBean> listActivationCode(ActivationCodeBean bean, Page<ActivationCodeBean> page) {
        return activationCodeDao.listActivationCode(bean, page);
    }

    @Override
    public long divideActivationCode(long startId, long endId, String agentNode) {
        if (StringUtils.isBlank(agentNode)){
            throw new BossBaseException("未选择服务商.");
        }
        AgentInfo agentInfo = agentInfoDao.selectByAgentNode(agentNode);
        if (agentInfo == null || agentInfo.getAgentLevel() == null || !agentInfo.getAgentLevel().equals(1)){
            throw new BossBaseException("只能分配给一级代理商.");
        }
        if(activationCodeDao.chechAgentOpenRepay(agentInfo.getAgentNo()) == 0){
            throw new BossBaseException("该代理没有开通超级还款功能.");
        }
        if ((startId + "").length() != 12){
            throw new BossBaseException("开始编号格式不正确");
        }
        if ((endId + "").length() != 12){
            throw new BossBaseException("结束编号格式不正确");
        }
        if (endId < startId){
            throw new BossBaseException("结束编号不能小于开始编号");
        }

        RepayOemServiceCostBean oemServiceCost = providerDao.queryRepayOemServiceCost(agentInfo.getAgentNo());
        if (oemServiceCost == null){
            oemServiceCost = providerDao.queryRepayOemServiceCost("default");
            if (oemServiceCost == null){
                throw new BossBaseException("请配置默认的交易服务费率.");
            }
        }
        return activationCodeDao.allotActivationCode2Agent(startId, endId, agentInfo, oemServiceCost.getOemNo());
    }

    @Override
    public long recoveryActivation(long startId, long endId) {
//        int count = activationCodeDao.countActivationCodeByStatus(startId, endId, "1");
//        if (endId - startId + 1 != count){
//            throw new BossBaseException("输入的编号存在不能回收的激活码");
//        }
        if ((startId + "").length() != 12){
            throw new BossBaseException("开始编号格式不正确");
        }
        if ((endId + "").length() != 12){
            throw new BossBaseException("结束编号格式不正确");
        }
        if (endId < startId){
            throw new BossBaseException("结束编号不能小于开始编号");
        }
        return activationCodeDao.recoveryActivation(startId, endId);
    }
}
