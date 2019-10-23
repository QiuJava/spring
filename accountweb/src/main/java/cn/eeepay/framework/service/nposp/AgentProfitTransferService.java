package cn.eeepay.framework.service.nposp;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.nposp.SettleOrderInfo;

import java.util.List;

public interface AgentProfitTransferService {

	List<SettleOrderInfo> findSettleOrderInfoList(SettleOrderInfo settleOrderInfo, Sort sort, Page<SettleOrderInfo> page);

	List<SettleOrderInfo> exportSettleOrderInfoList(SettleOrderInfo settleOrderInfo);

}
