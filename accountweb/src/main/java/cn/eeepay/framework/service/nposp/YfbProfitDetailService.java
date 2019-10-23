package cn.eeepay.framework.service.nposp;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.nposp.YfbPayOrder;
import cn.eeepay.framework.model.nposp.YfbProfitDetail;

import java.util.List;
import java.util.Map;

public interface YfbProfitDetailService {

    List<YfbProfitDetail> findServicePushShareList(YfbProfitDetail yfbRepayPlan, Sort sort, Page<YfbProfitDetail> page);

    Map<String,Object> serviceShareCollection(String createDate, String username) throws Exception;

    List<YfbProfitDetail> exportServiceShareList(YfbProfitDetail yfbProfitDetail);

    Map<String,Object> serviceCollectionDataCount(YfbProfitDetail yfbProfitDetail);

    YfbPayOrder findPayOrderByOrderNo(String orderNo);
}
