package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.PaOrder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author MXG
 * create 2018/07/14
 */
public interface PaOrderService {

    Map<String, Object> selectPaOrderByParam(Page<PaOrder> page, PaOrder orderInfo);

    String selectUserCodeByAgentNo(String agentNo);

    /**
     * 导出数据
     * @param info
     * @param response
     * @param request
     */
    void exportOrder(PaOrder info, HttpServletResponse response, HttpServletRequest request) throws Exception;

    /**
     * 查找用户（不分页）
     * @param info
     * @return
     */
    List<PaOrder> selectAllList(PaOrder info);

    /**
     * 查询订单
     * @param order_no
     * @return
     */
    Map<String,Object> selectPaOrderByOrderNo(String order_no);

}
