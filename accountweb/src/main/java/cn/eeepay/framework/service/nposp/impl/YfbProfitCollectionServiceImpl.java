package cn.eeepay.framework.service.nposp.impl;

import cn.eeepay.framework.dao.nposp.YfbProfitCollectionMapper;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.nposp.YfbProfitCollection;
import cn.eeepay.framework.model.nposp.YfbRepayPlan;
import cn.eeepay.framework.service.nposp.YfbProfitCollectionService;
import cn.eeepay.framework.util.ListUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("yfbProfitCollectionService")
@Transactional
public class YfbProfitCollectionServiceImpl implements YfbProfitCollectionService {

    private static final Logger log = LoggerFactory.getLogger(YfbProfitCollectionServiceImpl.class);

    @Resource
    private YfbProfitCollectionMapper yfbProfitCollectionMapper;

    @Override
    public int insertServiceShareDaySettleBatch(List<YfbProfitCollection> list) throws Exception {
        return yfbProfitCollectionMapper.insertServiceShareDaySettleBatch(list);
    }

    @Override
    public Map<String, Object> insertServiceShareDaySettleSplitBatch(List<YfbProfitCollection> yfbProfitCollections) throws Exception {
        Map<String,Object> msg=new HashMap<>();
        int i = 0;
        int batchCount = 200;
        List<YfbProfitCollection> asdList = new ArrayList<>();
        List<List<?>> superPushShareDaySettleSplitList = ListUtil.batchList(yfbProfitCollections, batchCount);
        for (List<?> clist : superPushShareDaySettleSplitList) {
            for (Object object : clist) {
                YfbProfitCollection asd = (YfbProfitCollection) object;
                asdList.add(asd);
            }
            if (asdList.size() > 0) {
                log.info("插入服务商分润{}条",asdList.size());
                int j = this.insertServiceShareDaySettleBatch(asdList);
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
            msg.put("msg", "没有任何数据插入");
        }
        return msg;
    }

    @Override
    public List<YfbProfitCollection> findServiceShareDaySettleByCollectionBatchNo(String collectionBatchNo) {
        return yfbProfitCollectionMapper.findServiceShareDaySettleByCollectionBatchNo(collectionBatchNo);
    }

    @Override
    public List<YfbProfitCollection> findServiceShareList(YfbProfitCollection yfbProfitCollection, Sort sort, Page<YfbProfitCollection> page) {
        if (StringUtils.isNotBlank(yfbProfitCollection.getTallyTime1())) {
            yfbProfitCollection.setTallyTime1(yfbProfitCollection.getTallyTime1() + " 00:00:00");
        }
        if (StringUtils.isNotBlank(yfbProfitCollection.getTallyTime2())) {
            yfbProfitCollection.setTallyTime2(yfbProfitCollection.getTallyTime2() + " 23:59:59");
        }
        return yfbProfitCollectionMapper.findServiceShareList(yfbProfitCollection,sort,page);
    }

    @Override
    public List<YfbProfitCollection> exportServiceInAccountList(YfbProfitCollection yfbProfitCollection) {
        if (StringUtils.isNotBlank(yfbProfitCollection.getTallyTime1())) {
            yfbProfitCollection.setTallyTime1(yfbProfitCollection.getTallyTime1() + " 00:00:00");
        }
        if (StringUtils.isNotBlank(yfbProfitCollection.getTallyTime2())) {
            yfbProfitCollection.setTallyTime2(yfbProfitCollection.getTallyTime2() + " 23:59:59");
        }
        return yfbProfitCollectionMapper.exportServiceInAccountList(yfbProfitCollection);
    }

    @Override
    public Map<String, Object> serviceInAccountCollectionDataCount(YfbProfitCollection yfbProfitCollection) {
        if (StringUtils.isNotBlank(yfbProfitCollection.getTallyTime1())) {
            yfbProfitCollection.setTallyTime1(yfbProfitCollection.getTallyTime1() + " 00:00:00");
        }
        if (StringUtils.isNotBlank(yfbProfitCollection.getTallyTime2())) {
            yfbProfitCollection.setTallyTime2(yfbProfitCollection.getTallyTime2() + " 23:59:59");
        }
        return yfbProfitCollectionMapper.serviceInAccountCollectionDataCount(yfbProfitCollection);
    }
}
