package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.SysDict;

import java.util.List;
import java.util.Map;

public interface ActivityOrderInfoService {
     List<Map<String,Object>> actOrderInfoQuery(Map<String, String> params, Page<Map<String, Object>> page,AgentInfo loginAgent);

     Map<String,Object> actOrderInfoCount(Map<String, String> params,AgentInfo loginAgent);

     List<Map<String,Object>> actOrderInfoExport(Map<String, String> params,AgentInfo loginAgent);

     SysDict sysDict(String sysKey, String sysValue);

     Map<String,Object> actOrderInfo(String id);

     Map<String,Object> queryCouponInfo(String couponNo);

     Map<String,Object> actOrderSettleInfoQuery(String payOrderNo);

     Map<String,Object> selectByOrderNo(String orderNo);
}
