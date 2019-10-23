package cn.eeepay.framework.service.nposp;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.nposp.YfbRepayPlan;

import java.util.List;
import java.util.Map;

public interface YfbRepayPlanService {

    List<YfbRepayPlan> findSuperPushShareList(YfbRepayPlan yfbRepayPlan, Sort sort, Page<YfbRepayPlan> page);

    List<YfbRepayPlan> exportSuperPushShareList(YfbRepayPlan yfbRepayPlan);

    Map<String,Object> cardOrderCollectionDataCount(YfbRepayPlan yfbRepayPlan,Integer type);
}
