package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.daoRedem.RedemOrderDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.RedemOrderBean;
import cn.eeepay.framework.service.RedemOrderService;
import cn.eeepay.framework.util.StringUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by 666666 on 2018/5/7.
 */
@Service
public class RedemOrderServiceImpl implements RedemOrderService {
    @Resource
    private RedemOrderDao redemOrderDao;

    @Override
    public List<RedemOrderBean> listActivityOrder(RedemOrderBean redemOrderBean, AgentInfo loginAgent, Page<RedemOrderBean> page) {
        List<RedemOrderBean> redemOrderBeans = redemOrderDao.listActivityOrder(redemOrderBean, loginAgent, page);
//        if (redemOrderBeans != null && !redemOrderBeans.isEmpty()){
//            for (RedemOrderBean bean : redemOrderBeans){
//                bean.setMobile(StringUtil.filterNull(bean.getMobile()).replaceAll("^(.{3}).*?(.{4})$", "$1****$2"));
//            }
//        }
        return redemOrderBeans;
    }

    @Override
    public List<RedemOrderBean> listDeclareOrder(RedemOrderBean redemOrderBean, AgentInfo loginAgent, Page<RedemOrderBean> page) {
        List<RedemOrderBean> redemOrderBeans = redemOrderDao.listDeclareOrder(redemOrderBean, loginAgent, page);
//        if (redemOrderBeans != null && !redemOrderBeans.isEmpty()){
//            for (RedemOrderBean bean : redemOrderBeans){
//                bean.setMobile(StringUtil.filterNull(bean.getMobile()).replaceAll("^(.{3}).*?(.{4})$", "$1****$2"));
//            }
//        }
        return redemOrderBeans;
    }

    @Override
    public List<RedemOrderBean> listShareOrder(RedemOrderBean redemOrderBean, AgentInfo loginAgent, Page<RedemOrderBean> page) {
        List<RedemOrderBean> redemOrderBeans = redemOrderDao.listShareOrder(redemOrderBean, loginAgent, page);
//        if (redemOrderBeans != null && !redemOrderBeans.isEmpty()){
//            for (RedemOrderBean bean : redemOrderBeans){
//                bean.setMobile(StringUtil.filterNull(bean.getMobile()).replaceAll("^(.{3}).*?(.{4})$", "$1****$2"));
//                bean.setProfitMobile(StringUtil.filterNull(bean.getProfitMobile()).replaceAll("^(.{3}).*?(.{4})$", "$1****$2"));
//            }
//        }
        return redemOrderBeans;
    }

    @Override
    public Map<String,Object> listShareOrderSum(RedemOrderBean redemOrderBean, AgentInfo loginAgent, Page<RedemOrderBean> page) {
        Map<String,Object> map = redemOrderDao.listShareOrderSum(redemOrderBean, loginAgent, page);
        return map;
    }

    @Override
    public List<Map<String, Object>> listOrgCode() {
        return redemOrderDao.listOrgCode();
    }

    @Override
    public Map<String, Object> summaryActivityOrder(RedemOrderBean redemOrderBean, AgentInfo loginAgent) {
        return redemOrderDao.summaryActivityOrder(redemOrderBean, loginAgent);
    }

    @Override
    public Map<String, Object> summaryDeclareOrder(RedemOrderBean redemOrderBean, AgentInfo loginAgent) {
        return redemOrderDao.summaryDeclareOrder(redemOrderBean, loginAgent);
    }
}
