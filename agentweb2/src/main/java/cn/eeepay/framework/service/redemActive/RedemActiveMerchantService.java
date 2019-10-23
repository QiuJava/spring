package cn.eeepay.framework.service.redemActive;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.redemActive.RedemActiveMerchantBean;

import java.util.List;
import java.util.Map;

/**
 * Created by 666666 on 2018/5/16.
 */
public interface RedemActiveMerchantService {

    List<RedemActiveMerchantBean> listMerchant(RedemActiveMerchantBean bean, AgentInfo loginAgent, Page<RedemActiveMerchantBean> page);

    Map<String, Object> queryMerchantDetails(String merchantNo, AgentInfo loginAgent);

    List<Map<String,Object>> listBalanceHis(String startTime, String endTime, String service, String merchantNo, Page<List<Map<String,Object>>> page);
}
