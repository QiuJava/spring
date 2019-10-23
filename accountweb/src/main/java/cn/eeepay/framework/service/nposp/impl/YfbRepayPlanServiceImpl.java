package cn.eeepay.framework.service.nposp.impl;

import cn.eeepay.framework.dao.nposp.YfbRepayPlanMapper;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.nposp.YfbRepayPlan;
import cn.eeepay.framework.service.nposp.YfbRepayPlanService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


@Service("yfbRepayPlanService")
public class YfbRepayPlanServiceImpl implements YfbRepayPlanService {

    @Resource
    private YfbRepayPlanMapper yfbRepayPlanMapper;

    @Override
    public List<YfbRepayPlan> findSuperPushShareList(YfbRepayPlan yfbRepayPlan, Sort sort, Page<YfbRepayPlan> page) {
        if (StringUtils.isNotBlank(yfbRepayPlan.getTallyTime1())) {
            yfbRepayPlan.setTallyTime1(yfbRepayPlan.getTallyTime1() + " 00:00:00");
        }
        if (StringUtils.isNotBlank(yfbRepayPlan.getTallyTime2())) {
            yfbRepayPlan.setTallyTime2(yfbRepayPlan.getTallyTime2() + " 00:00:00");
        }
        yfbRepayPlanMapper.findYfbRepayPlanList(yfbRepayPlan,sort,page);
        return null;
    }

    @Override
    public List<YfbRepayPlan> exportSuperPushShareList(YfbRepayPlan yfbRepayPlan) {
        if (StringUtils.isNotBlank(yfbRepayPlan.getTallyTime1())) {
            yfbRepayPlan.setTallyTime1(yfbRepayPlan.getTallyTime1() + " 00:00:00");
        }
        if (StringUtils.isNotBlank(yfbRepayPlan.getTallyTime2())) {
            yfbRepayPlan.setTallyTime2(yfbRepayPlan.getTallyTime2() + " 00:00:00");
        }
        return yfbRepayPlanMapper.exportYfbRepayPlanList(yfbRepayPlan);
    }

    @Override
    public Map<String, Object> cardOrderCollectionDataCount(YfbRepayPlan yfbRepayPlan,Integer type) {
        if (StringUtils.isNotBlank(yfbRepayPlan.getTallyTime1())) {
            yfbRepayPlan.setTallyTime1(yfbRepayPlan.getTallyTime1() + " 00:00:00");
        }
        if (StringUtils.isNotBlank(yfbRepayPlan.getTallyTime2())) {
            yfbRepayPlan.setTallyTime2(yfbRepayPlan.getTallyTime2() + " 00:00:00");
        }
        return yfbRepayPlanMapper.cardOrderCollectionDataCount(yfbRepayPlan,type);
    }
}
