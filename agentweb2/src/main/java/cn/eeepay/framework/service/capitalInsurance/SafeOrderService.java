package cn.eeepay.framework.service.capitalInsurance;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.capitalInsurance.OrderTotal;
import cn.eeepay.framework.model.capitalInsurance.SafeConfig;
import cn.eeepay.framework.model.capitalInsurance.SafeOrder;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Administrator on 2018/7/23/023.
 * @author  liuks
 * 保险订单service
 */
public interface SafeOrderService {

    List<SafeOrder> selectAllList(SafeOrder order, Page<SafeOrder> page);

    OrderTotal selectSum(SafeOrder order, Page<SafeOrder> page);

    List<SafeConfig> getSafeConfigList();

    List<SafeOrder> exportDetailSelect(SafeOrder order);

    void exportDetail(List<SafeOrder> list, HttpServletResponse response) throws Exception;
}
