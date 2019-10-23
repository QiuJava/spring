package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.ActivityOrderInfoDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.service.ActivityOrderInfoService;
import cn.eeepay.framework.service.AgentInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("activityOrderInfoService")
public class ActivityOrderInfoServiceImpl implements ActivityOrderInfoService {

    @Resource
    private ActivityOrderInfoDao activityOrderInfoDao;
    @Resource
    private AgentInfoService agentInfoService;

    @Override
    public List<Map<String, Object>> actOrderInfoQuery(Map<String, String> params, Page<Map<String, Object>> page,AgentInfo loginAgent) {
        List<Map<String, Object>> list=activityOrderInfoDao.actOrderInfoQuery(params,page,loginAgent);
        return list;
    }

    @Override
    public Map<String, Object> actOrderInfoCount(Map<String, String> params,AgentInfo loginAgent) {
        return activityOrderInfoDao.actOrderInfoCount(params,loginAgent);
    }

    @Override
    public List<Map<String, Object>> actOrderInfoExport(Map<String,String> params,AgentInfo loginAgent) {
        return activityOrderInfoDao.actOrderInfoExport(params,loginAgent);
    }

    @Override
    public SysDict sysDict(String sysKey, String sysValue) {
        return activityOrderInfoDao.sysDict(sysKey,sysValue);
    }

    @Override
    public Map<String, Object> actOrderInfo(String id) {
        return activityOrderInfoDao.actOrderInfo(id);
    }

    @Override
    public Map<String, Object> queryCouponInfo(String couponNo) {
        return activityOrderInfoDao.queryCouponInfo(couponNo);
    }

    @Override
    public Map<String, Object> actOrderSettleInfoQuery(String payOrderNo) {
        return activityOrderInfoDao.actOrderSettleInfoQuery(payOrderNo);
    }

    @Override
    public Map<String, Object> selectByOrderNo(String orderNo) {
        return activityOrderInfoDao.selectByOrderNo(orderNo);
    }
}
