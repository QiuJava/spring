package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.CreditRepayOrderDetailDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.CreditRepayOrderDetail;
import cn.eeepay.framework.service.CreditRepayOrderDetailService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 还款订单处理流水
 * @author liuks
 */
@Service("creditRepayOrderDetailService")
public class CreditRepayOrderDetailServiceImpl implements CreditRepayOrderDetailService {
    @Resource
    private CreditRepayOrderDetailDao creditRepayOrderDetailDao;

    @Override
    public List<CreditRepayOrderDetail> selectDetailList(String batchNo) {
        return creditRepayOrderDetailDao.selectDetailList(batchNo);
    }

    @Override
    public List<CreditRepayOrderDetail> selectDetailAllList(CreditRepayOrderDetail orderDetail, AgentInfo loginAgent, Page<CreditRepayOrderDetail> page) {
        return creditRepayOrderDetailDao.selectDetailAllList(orderDetail,loginAgent, page);
    }

    @Override
    public List<CreditRepayOrderDetail> exportDetailAllList(CreditRepayOrderDetail orderDetail, AgentInfo loginAgent) {
        return creditRepayOrderDetailDao.exportDetailAllList(orderDetail, loginAgent);
    }
}
