package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.AgentInfoDao;
import cn.eeepay.framework.dao.ZQMerchantDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.ZQMerchantInfo;
import cn.eeepay.framework.model.ZQTransOrderInfo;
import cn.eeepay.framework.service.ZQMerchantService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2017/5/22.
 */
@Service("zqMerchantService")
public class ZQMerchantServiceImpl implements ZQMerchantService{

    protected static final Logger LOGGER = LoggerFactory.getLogger(ZQMerchantServiceImpl.class);
    @Resource
    private ZQMerchantDao zqMerchantDao;
    @Resource
    private AgentInfoDao agentInfoDao;

    @Override
    public List<ZQMerchantInfo> queryZQMerchantInfo(ZQMerchantInfo zqMerchantInfo, String agentNo, Page<ZQMerchantInfo> page) {
        AgentInfo agentInfo = agentInfoDao.selectByAgentNo(agentNo);
        // 获取销售名称,在直清商户中,该销售名称代表直清编号 如ZF_ZH
        if (agentInfo == null || StringUtils.isBlank(agentInfo.getSaleName())){
            return null;
        }
        String saleName = agentInfo.getSaleName();
        return zqMerchantDao.queryZQMerchantInfo(zqMerchantInfo,saleName, page);
    }

    @Override
    public List<ZQMerchantInfo> exportZQMerchantInfo(ZQMerchantInfo zqMerchantInfo, String agentNo) {
        AgentInfo agentInfo = agentInfoDao.selectByAgentNo(agentNo);
        // 获取销售名称,在直清商户中,该销售名称代表直清编号 如ZF_ZH
        if (agentInfo == null || StringUtils.isBlank(agentInfo.getSaleName())){
            return null;
        }
        String saleName = agentInfo.getSaleName();
        return zqMerchantDao.queryZQMerchantInfo(zqMerchantInfo,saleName);
    }

    @Override
    public List<ZQTransOrderInfo> queryZQTransOrder(ZQTransOrderInfo zqTransOrderInfo, String agentNo, Page<ZQTransOrderInfo> page) {
        AgentInfo agentInfo = agentInfoDao.selectByAgentNo(agentNo);
        // 获取销售名称,在直清商户中,该销售名称代表直清编号 如ZF_ZH
        if (agentInfo == null || StringUtils.isBlank(agentInfo.getSaleName())){
            return null;
        }
        String saleName = agentInfo.getSaleName();
        Long zqAcqId = zqMerchantDao.getZQAcqId(saleName);
        if (zqAcqId == null){
            LOGGER.warn("找不到" + saleName + "的收单Id.");
            return null;
        }
        return zqMerchantDao.queryZQTransOrder(zqTransOrderInfo,saleName,zqAcqId, page );
    }

    @Override
    public List<ZQTransOrderInfo> exportZQTransOrder(ZQTransOrderInfo zqTransOrderInfo, String agentNo) {
        AgentInfo agentInfo = agentInfoDao.selectByAgentNo(agentNo);
        // 获取销售名称,在直清商户中,该销售名称代表直清编号 如ZF_ZH
        if (agentInfo == null || StringUtils.isBlank(agentInfo.getSaleName())){
            return null;
        }
        String saleName = agentInfo.getSaleName();
        Long zqAcqId = zqMerchantDao.getZQAcqId(saleName);
        if (zqAcqId == null){
            LOGGER.warn("找不到" + saleName + "的收单Id.");
            return null;
        }
        return zqMerchantDao.queryZQTransOrder(zqTransOrderInfo,saleName, zqAcqId);
    }
}
