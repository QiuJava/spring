package cn.eeepay.framework.service.bill;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.AgentPreAdjust;
import cn.eeepay.framework.model.bill.BeforeAdjustApply;
import cn.eeepay.framework.model.nposp.AgentInfo;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ActivityAccAdjustmentService {

    public void insertActivityAdjustment( Map<String, Object> msg,AgentPreAdjust agentPreAdjust,AgentInfo agentInfo) throws IOException;

    void findBeforeAdjustApplyList(BeforeAdjustApply beforeAdjustApply, Map<String, String> params, Sort sort, Page<BeforeAdjustApply> page);

    List<BeforeAdjustApply> exportBeforeAdjustApplyList(BeforeAdjustApply beforeAdjustApply);

    Map<String,BigDecimal> findBeforeAdjustApplyListCollection(BeforeAdjustApply beforeAdjustApply);

    Map<String, Object> resolvebatchPreAdjustFile(File temp, String uname) throws Exception;

    Map<String, Object> leadingInBatchPreAdjustDetails(Map<String, Object> map) throws Exception;
}
