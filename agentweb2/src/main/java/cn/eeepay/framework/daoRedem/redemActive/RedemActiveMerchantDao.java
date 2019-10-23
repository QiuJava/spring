package cn.eeepay.framework.daoRedem.redemActive;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.redemActive.RedemActiveMerchantBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by 666666 on 2018/5/16.
 */
public interface RedemActiveMerchantDao {

    List<RedemActiveMerchantBean> listMerchant(@Param("bean") RedemActiveMerchantBean bean,
                                               @Param("loginAgent") AgentInfo loginAgent,
                                               Page<RedemActiveMerchantBean> page);

    Map<String,Object> queryMerchantInfo(@Param("merchantNo") String merchantNo);

    /**
     * 获取帐号明细
     */
    List<Map<String,Object>> listBalanceHis(@Param("startTime") String startTime,
                                            @Param("endTime") String endTime,
                                            @Param("service") String service,
                                            @Param("merchantNo") String merchantNo,
                                            Page<List<Map<String, Object>>> page);
}
