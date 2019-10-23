package cn.eeepay.framework.service.nposp.impl;

import cn.eeepay.framework.dao.nposp.SettleOrderInfoMapper;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.nposp.SettleOrderInfo;
import cn.eeepay.framework.service.nposp.AgentProfitTransferService;
import cn.eeepay.framework.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service("agentProfitTransferService")
@Transactional("nposp")
public class  AgentProfitTransferServiceImpl implements AgentProfitTransferService {

    @Resource
    private SettleOrderInfoMapper settleOrderInfoMapper;
    @Override
    public List<SettleOrderInfo> findSettleOrderInfoList(SettleOrderInfo settleOrderInfo,
        Sort sort, Page<SettleOrderInfo> page) {

        String startTime = settleOrderInfo.getStartTime();
        String endTime = settleOrderInfo.getEndTime();
        if (StringUtils.isNotBlank(startTime)) {
            settleOrderInfo.setStartTime(startTime + " 00:00:00");
        }
        if (StringUtils.isNotBlank(endTime)) {
            settleOrderInfo.setEndTime(endTime + " 23:59:59");
        }
        return settleOrderInfoMapper.findSettleOrderInfoList(settleOrderInfo,sort,page);

    }

    @Override
    public List<SettleOrderInfo> exportSettleOrderInfoList(SettleOrderInfo settleOrderInfo) {

        String startTime = settleOrderInfo.getStartTime();
        String endTime = settleOrderInfo.getEndTime();
        if (StringUtils.isNotBlank(startTime)) {
            settleOrderInfo.setStartTime(startTime + " 00:00:00");
        }
        if (StringUtils.isNotBlank(endTime)) {
            settleOrderInfo.setEndTime(endTime + " 23:59:59");
        }
        return settleOrderInfoMapper.exportSettleOrderInfoList(settleOrderInfo,null);
    }
}
