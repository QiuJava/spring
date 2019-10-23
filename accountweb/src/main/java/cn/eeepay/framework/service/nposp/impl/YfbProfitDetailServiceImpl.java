package cn.eeepay.framework.service.nposp.impl;

import cn.eeepay.framework.dao.nposp.YfbProfitDetailMapper;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.nposp.AgentInfo;
import cn.eeepay.framework.model.nposp.YfbPayOrder;
import cn.eeepay.framework.model.nposp.YfbProfitCollection;
import cn.eeepay.framework.model.nposp.YfbProfitDetail;
import cn.eeepay.framework.service.bill.GenericTableService;
import cn.eeepay.framework.service.nposp.AgentInfoService;
import cn.eeepay.framework.service.nposp.YfbProfitCollectionService;
import cn.eeepay.framework.service.nposp.YfbProfitDetailService;
import cn.eeepay.framework.util.ListUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

@Service("yfbProfitDetailService")
@Transactional
public class YfbProfitDetailServiceImpl implements YfbProfitDetailService{

    private static final Logger log = LoggerFactory.getLogger(YfbProfitDetailServiceImpl.class);

    @Resource
    private YfbProfitDetailMapper yfbProfitDetailMapper;
    @Resource
    public GenericTableService genericTableService;
    @Resource
    private YfbProfitCollectionService yfbProfitCollectionService;
    @Resource
    public AgentInfoService agentInfoService;

    @Override
    public List<YfbProfitDetail> findServicePushShareList(YfbProfitDetail yfbRepayPlan, Sort sort, Page<YfbProfitDetail> page) {
        if (StringUtils.isNotBlank(yfbRepayPlan.getOrderTime1())) {
            yfbRepayPlan.setOrderTime1(yfbRepayPlan.getOrderTime1() + " 00:00:00");
        }
        if (StringUtils.isNotBlank(yfbRepayPlan.getOrderTime1())) {
            yfbRepayPlan.setOrderTime2(yfbRepayPlan.getOrderTime2() + " 23:59:59");
        }

        yfbProfitDetailMapper.findYfbProfitDetailList(yfbRepayPlan,sort,page);
        List<YfbProfitDetail> list = page.getResult();
        if(list!=null && list.size()>0){
            for(YfbProfitDetail pd : list){
                if(pd.getProfitType().equals("3")){
                    String orderNo = pd.getOrderNo();
                    YfbPayOrder yfbPayOrder = findPayOrderByOrderNo(orderNo);
                    pd.setAcqCode(yfbPayOrder.getAcqCode());        //交易通道


                    pd.setRepayFeeRate(String.valueOf(yfbPayOrder.getTransFeeRate()));     //实际费率
                    pd.setFee(new BigDecimal(yfbPayOrder.getAcqFeeRate()));      //收单扣率
                    pd.setPayFl(yfbPayOrder.getAcqFee());           //收单手续费
                    if(pd.getParentId().equals("0")){
                        pd.setOneAgentName(pd.getAgentName());
                    }else{
                        AgentInfo agentInfo = agentInfoService.findEntityByAgentNo(pd.getOneLevelId());
                        pd.setOneAgentName(agentInfo.getAgentName());
                    }
                }else{
                    if(pd.getAcqCode()!=null){
                        Map<String,Object> map = yfbProfitDetailMapper.findYfbPayChannelByAcqCode(pd.getAcqCode());
                        pd.setSuccessPayAmount(pd.getSuccessPayAmount()==null?new BigDecimal(0):pd.getSuccessPayAmount());
                        BigDecimal fee = (BigDecimal) map.get("pay_quick_fee_rate");        //收单扣率
                        fee = fee==null?new BigDecimal(0):fee;
                        BigDecimal payFl = fee.multiply(pd.getSuccessPayAmount());      //收单手续费
                        pd.setFee(fee);
                        pd.setPayFl(payFl);
                    }
                    if(pd.getParentId().equals("0")){
                        pd.setOneAgentName(pd.getAgentName());
                    }else{
                        AgentInfo agentInfo = agentInfoService.findEntityByAgentNo(pd.getOneLevelId());
                        pd.setOneAgentName(agentInfo.getAgentName());
                    }
                }
            }
        }
        return null;
    }

    @Override
    public List<YfbProfitDetail> exportServiceShareList(YfbProfitDetail yfbProfitDetail) {
        if (StringUtils.isNotBlank(yfbProfitDetail.getOrderTime1())) {
            yfbProfitDetail.setOrderTime1(yfbProfitDetail.getOrderTime1() + " 00:00:00");
        }
        if (StringUtils.isNotBlank(yfbProfitDetail.getOrderTime1())) {
            yfbProfitDetail.setOrderTime2(yfbProfitDetail.getOrderTime2() + " 23:59:59");
        }
        List<YfbProfitDetail> list =  yfbProfitDetailMapper.exportServiceShareList(yfbProfitDetail);
        if(list!=null && list.size()>0){
            for(YfbProfitDetail pd : list){
                if(pd.getProfitType().equals("3")){
                    String orderNo = pd.getOrderNo();
                    YfbPayOrder yfbPayOrder = findPayOrderByOrderNo(orderNo);
                    pd.setAcqCode(yfbPayOrder.getAcqCode());        //交易通道


                    pd.setRepayFeeRate(String.valueOf(yfbPayOrder.getTransFeeRate()));     //实际费率
                    pd.setFee(new BigDecimal(yfbPayOrder.getAcqFeeRate()));      //收单扣率
                    pd.setPayFl(yfbPayOrder.getAcqFee());           //收单手续费
                    if(pd.getParentId().equals("0")){
                        pd.setOneAgentName(pd.getAgentName());
                    }else{
                        AgentInfo agentInfo = agentInfoService.findEntityByAgentNo(pd.getOneLevelId());
                        pd.setOneAgentName(agentInfo.getAgentName());
                    }
                }else{
                    if(pd.getAcqCode()!=null){
                        Map<String,Object> map = yfbProfitDetailMapper.findYfbPayChannelByAcqCode(pd.getAcqCode());
                        pd.setSuccessPayAmount(pd.getSuccessPayAmount()==null?new BigDecimal(0):pd.getSuccessPayAmount());
                        BigDecimal fee = (BigDecimal) map.get("pay_quick_fee_rate");        //收单扣率
                        fee = fee==null?new BigDecimal(0):fee;
                        BigDecimal payFl = fee.multiply(pd.getSuccessPayAmount());      //收单手续费
                        pd.setFee(fee);
                        pd.setPayFl(payFl);
                    }
                    if(pd.getParentId().equals("0")){
                        pd.setOneAgentName(pd.getAgentName());
                    }else{
                        AgentInfo agentInfo = agentInfoService.findEntityByAgentNo(pd.getOneLevelId());
                        pd.setOneAgentName(agentInfo.getAgentName());
                    }
                }
            }
        }
        return list;
    }

    @Override
    public Map<String, Object> serviceCollectionDataCount(YfbProfitDetail yfbProfitDetail) {
        if (StringUtils.isNotBlank(yfbProfitDetail.getOrderTime1())) {
            yfbProfitDetail.setOrderTime1(yfbProfitDetail.getOrderTime1() + " 00:00:00");
        }
        if (StringUtils.isNotBlank(yfbProfitDetail.getOrderTime1())) {
            yfbProfitDetail.setOrderTime2(yfbProfitDetail.getOrderTime2() + " 23:59:59");
        }
        return yfbProfitDetailMapper.serviceCollectionDataCount(yfbProfitDetail);
    }

    @Override
    public Map<String, Object> serviceShareCollection(String createDate, String username) throws Exception {
        String collectionBatchNo = genericTableService.createServiceShareCollectionBatchNo();
        log.info("服务商分润汇总生成批次号 collectionBatchNo------> " + collectionBatchNo);
        Map<String, String> params = new HashMap<>();
        params.put("createTime1",createDate);
        List<Map<String, Object>> mapList = yfbProfitDetailMapper.findCollectionGropByDate(params);     //获取要汇总的数据
        if(mapList == null || mapList.size() == 0){
            Map<String,Object> result = new HashMap<>();
            result.put("status", true);
            result.put("msg", createDate + "没有要汇总的数据！");
            return result;
        }

        List<YfbProfitCollection> list = new ArrayList<>();
        for (Map<String, Object> map : mapList) {
            YfbProfitCollection sp = new YfbProfitCollection();
            //sp.setCollectionNo();     //分润汇总编号  全局唯一
            sp.setCollectionBatchNo(collectionBatchNo);
            sp.setMerType(map.get("mer_type").toString());
            sp.setMerNo(map.get("mer_no").toString());
            sp.setAgentNode(map.get("agent_node").toString());
            sp.setServiceCostRate((BigDecimal) map.get("service_cost_rate"));
            //sp.setServiceCostSingleFee(new BigDecimal(map.get("shareTotalAmount").toString()));
            sp.setServiceCostSingleFee((BigDecimal) map.get("service_cost_single_fee"));
            sp.setProfitAmount((BigDecimal) map.get("profit_amount"));
            sp.setOperator(username);
            sp.setIncomeStatus(0);
            sp.setCreateTime(new Date());
            sp.setLastUpdateTime(new Date());
            list.add(sp);
        }

        Map<String, Object> result = new HashMap<>();
        try {
            //批量添加汇总数据
            result = yfbProfitCollectionService.insertServiceShareDaySettleSplitBatch(list);
            //汇总成功之后需要更新 分润明细表中的 汇总批次，汇总状态
            if((boolean)result.get("status")){
                //根据批次号，拿到汇总数据
                List<YfbProfitCollection> yfbProfitCollections = yfbProfitCollectionService.findServiceShareDaySettleByCollectionBatchNo(collectionBatchNo);
                for (YfbProfitCollection yfbProfitCollection : yfbProfitCollections) {
                    List<YfbProfitDetail> ycs = yfbProfitDetailMapper.findYfbProfitDetailListByModel(yfbProfitCollection);
                    for (YfbProfitDetail yfbProfitDetail : ycs) {
                        //yfbProfitDetail.setProfitNo(yfbProfitCollection.getCollectionNo());     //分润明细编号 ,未知
                        yfbProfitDetail.setCollectionBatchNo(collectionBatchNo);            //汇总批次
                        yfbProfitDetail.setCollectionTime(yfbProfitCollection.getCreateTime());     //汇总时间
                    }
                    Map<String, Object> resultShare = updateServiceShareSplitBatch(ycs);
                    if((boolean)resultShare.get("status")){
                        result.put("status", true);
                        result.put("msg", "汇总成功！");
                    }else{
                        result.put("status", false);
                        result.put("msg", "汇总失败！");
                        throw new RuntimeException(createDate + "汇总数据出现异常！数据进行回滚！");
                    }
                }

            }else{
                result.put("status", true);
                result.put("msg", "更新数据失败，汇总失败！");
            }
        } catch (Exception e) {
            result.put("status", true);
            result.put("msg", "更新数据失败，汇总失败！");
            throw new RuntimeException(createDate + "汇总数据出现异常！数据进行回滚！" +e.getMessage());
        }
        return result;
    }

    public Map<String, Object> updateServiceShareSplitBatch(List<YfbProfitDetail> serviceShareList){
        Map<String,Object> msg=new HashMap<>();
        int i = 0;
        int batchCount = 200;
        List<YfbProfitDetail> asdList = new ArrayList<>();
        List<List<?>> serviceShareSplitList = ListUtil.batchList(serviceShareList, batchCount);
        for (List<?> clist : serviceShareSplitList) {
            for (Object object : clist) {
                YfbProfitDetail asd = (YfbProfitDetail) object;
                asdList.add(asd);
            }
            if (asdList.size() > 0) {
                log.info("修改服务商分润记录{}条",asdList.size());
                int j = yfbProfitDetailMapper.updateServiceShareBatch(asdList);
                if (j > 0) {
                    i = i + j;
                }
                if (!asdList.isEmpty()) {
                    asdList.clear();
                }
            }
        }
        if (i > 0) {
            msg.put("status", true);
            msg.put("msg", "执行成功");
        }
        else{
            msg.put("status", true);
            msg.put("msg", "没有修改任何数据");
        }
        return msg;
    }

    @Override
    public YfbPayOrder findPayOrderByOrderNo(String orderNo) {
        return yfbProfitDetailMapper.findPayOrderByOrderNo(orderNo);
    }
}
