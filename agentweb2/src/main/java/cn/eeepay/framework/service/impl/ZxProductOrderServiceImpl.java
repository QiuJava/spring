package cn.eeepay.framework.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import cn.eeepay.framework.daoSuperBank.OrderMainDao;
import cn.eeepay.framework.daoSuperBank.OrgInfoDao;
import cn.eeepay.framework.daoSuperBank.ZxProductOrderDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.OrderMainSum;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.model.ZxProductOrder;
import cn.eeepay.framework.service.ZxProductOrderService;
import cn.eeepay.framework.util.CommonUtil;
import cn.eeepay.framework.util.DateUtils;

@Service
public class ZxProductOrderServiceImpl implements ZxProductOrderService {

    @Resource
    private ZxProductOrderDao zxProductOrderDao;
    
    @Resource
    private OrderMainDao orderMainDao;

//    @Resource
//    private ZxApplyProductProvider zxApplyProductProvider;

    @Override
    public List<ZxProductOrder> selectByPage(ZxProductOrder record, Page<ZxProductOrder> page) {
    	final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String orgId = principal.getUserEntityInfo().getEntityId();
        record.setEntityId(orgId);
        List<ZxProductOrder> zxProductOrders = zxProductOrderDao.selectByPage(record, page);
        for(ZxProductOrder zxProductOrder:zxProductOrders){
        	zxProductOrder.setProfitDateStr(zxProductOrderDao.selectUserProfitByOrderNo(zxProductOrder.getOrderNo()));
            copyProductOrder(zxProductOrder);
        }
        return zxProductOrders;
    }


    @Override
    public ZxProductOrder selectByOrderNo(String orderNo) {
    	final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String orgId = principal.getUserEntityInfo().getEntityId();
        ZxProductOrder zxProductOrder = zxProductOrderDao.selectByOrderNo(orderNo,orgId);
        zxProductOrder.setProfitDateStr(zxProductOrderDao.selectUserProfitByOrderNo(zxProductOrder.getOrderNo()));
        copyProductOrder(zxProductOrder);
        return zxProductOrder;
    }

    @Override
    public OrderMainSum selectOrderSum(ZxProductOrder record) {
        OrderMainSum orderMainSum= zxProductOrderDao.selectOrderSum(record) ;
        if(orderMainSum==null){
            orderMainSum=new OrderMainSum();
        }
        return orderMainSum;
    }

//    @Override
//    public List<ZxApplyProductsEntity> selectProductAll() {
//        BaseResponse response = zxApplyProductProvider.selectAllPrice();
//        return (List<ZxApplyProductsEntity>) response.getData();
//    }

    void copyProductOrder(ZxProductOrder zxProductOrder){
        Map<String, String> userTypeMap = getUserTypeMap();
        zxProductOrder.setOneUserType(userTypeMap.get(zxProductOrder.getOneUserType()));
        zxProductOrder.setTwoUserType(userTypeMap.get(zxProductOrder.getTwoUserType()));
        zxProductOrder.setThrUserType(userTypeMap.get(zxProductOrder.getThrUserType()));
        zxProductOrder.setFouUserType(userTypeMap.get(zxProductOrder.getFouUserType()));
        if("1".equals(zxProductOrder.getStatus())){
            zxProductOrder.setStatus("待支付");
        }
        if("2".equals(zxProductOrder.getStatus())){
            zxProductOrder.setStatus("已支付");
        }
        if("3".equals(zxProductOrder.getStatus())){
            zxProductOrder.setStatus("已完成");
        }
        if("4".equals(zxProductOrder.getStatus())){
            zxProductOrder.setStatus("生成失败（退款处理中）");
        }
        if("5".equals(zxProductOrder.getStatus())){
            zxProductOrder.setStatus("退款成功");
        }
        if("6".equals(zxProductOrder.getStatus())){
            zxProductOrder.setStatus("退款失败");
        }
        if("7".equals(zxProductOrder.getStatus())){
            zxProductOrder.setStatus("已失效");
        }
        if("1".equals(zxProductOrder.getPayMethod())){
            zxProductOrder.setPayMethod("微信");
        }
        if("2".equals(zxProductOrder.getPayMethod())){
            zxProductOrder.setPayMethod("支付宝");
        }
        if("1".equals(zxProductOrder.getPayMethod())){
            zxProductOrder.setPayMethod("快捷");
        }
        if(zxProductOrder.getCreateTime()!=null){
            zxProductOrder.setCreateTimeStr(DateUtils.format(zxProductOrder.getCreateTime(), " yyyy-MM-dd HH:mm:ss "));
        }
        if(zxProductOrder.getPayDate()!=null){
            zxProductOrder.setPayDateStr(DateUtils.format(zxProductOrder.getPayDate(), " yyyy-MM-dd HH:mm:ss "));
        }
        if(zxProductOrder.getGenerationTime()!=null){
            zxProductOrder.setGenerationTimeStr(DateUtils.format(zxProductOrder.getGenerationTime(), " yyyy-MM-dd HH:mm:ss "));
        }
        if(zxProductOrder.getExpiryTime()!=null){
            zxProductOrder.setExpiryTimeStr(DateUtils.format(zxProductOrder.getExpiryTime(), " yyyy-MM-dd HH:mm:ss "));
        }
        if(zxProductOrder.getProfitDate()!=null){
            zxProductOrder.setProfitDateStr(DateUtils.format(zxProductOrder.getProfitDate(), " yyyy-MM-dd HH:mm:ss "));
        }
        if(zxProductOrder.getRefundDate()!=null){
        	zxProductOrder.setRefundDateStr(DateUtils.format(zxProductOrder.getRefundDate(), " yyyy-MM-dd HH:mm:ss "));
        }
        if("0".equals(zxProductOrder.getAccountStatus())){
            zxProductOrder.setAccountStatus("待入账");
        }
        if("1".equals(zxProductOrder.getAccountStatus())){
            zxProductOrder.setAccountStatus("已记账");
        }
        if("2".equals(zxProductOrder.getAccountStatus())){
            zxProductOrder.setAccountStatus("记账失败");
        }

        if("1".equals(zxProductOrder.getReportType())){
            zxProductOrder.setReportType("深度报告类型");
        }
        if("2".equals(zxProductOrder.getReportType())){
            zxProductOrder.setReportType("黑名单多头类型");
        }
        if("3".equals(zxProductOrder.getReportType())){
            zxProductOrder.setReportType("信用评测类型");
        }
        zxProductOrder.setRecordPhone(zxProductOrder.getRecordPhone().replaceAll("^(\\d{3}).*(\\d{4})$", "$1****$2"));
        zxProductOrder.setRecordIdNo(zxProductOrder.getRecordIdNo().replaceAll("^(\\d{6}).*(\\d{2})$", "$1**********$2"));
        String recordName = zxProductOrder.getRecordName();
        zxProductOrder.setRecordName("*" + recordName.substring(1, recordName.length()));
    }
    public Map<String, String> getUserTypeMap(){
        Map<String, String> map = new HashMap<>();
        map.put("10", "用户");
        map.put("20", "专员");
        map.put("30", "经理");
        map.put("40", "银行家");
        map.put("50", "OEM");
        map.put("60", "平台");
        return map;
    }
}
