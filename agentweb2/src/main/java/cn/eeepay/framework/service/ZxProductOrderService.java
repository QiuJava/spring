package cn.eeepay.framework.service;

import java.util.List;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.OrderMainSum;
import cn.eeepay.framework.model.ZxProductOrder;

public interface ZxProductOrderService {

   List<ZxProductOrder> selectByPage(ZxProductOrder record, Page<ZxProductOrder> page);

   ZxProductOrder selectByOrderNo(String orderNo);

   OrderMainSum selectOrderSum(ZxProductOrder record);

//   List <ZxApplyProductsEntity> selectProductAll();
}
