package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CreditCardManagerShare;

import java.util.List;
import java.util.Map;

public interface CreditCardManagerService {


    Map<String, Object> queryCreditCardManagerShareList(Map<String, Object> params, Page<CreditCardManagerShare> page);

    List<CreditCardManagerShare> exportAllInfo(Map<String,Object> params);

    String getTodayShareAmount(String entityId);

    String getShareTotalMoney(Map<String,Object> params);

    String getTradeTotalMoney(Map<String,Object> params);
}
