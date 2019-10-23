package cn.eeepay.framework.service;

import cn.eeepay.framework.model.YfbPayOrder;
import cn.eeepay.framework.model.surveyOrder.SurveyOrderInfo;
import cn.eeepay.framework.model.surveyOrder.SurveyReplyRecord;
import cn.eeepay.framework.model.surveyOrder.SurveyUrgeRecord;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author MXG
 * create 2018/09/07
 */
public interface SurveyOrderService {

    Map<String,Object> selectOrderByParam(SurveyOrderInfo order, int pageNo, int pageSize);

    void export(SurveyOrderInfo order, HttpServletResponse response, HttpServletRequest request) throws Exception;

    SurveyOrderInfo selectByIdAndAgentNode(String id, String agentNode);

    List<SurveyUrgeRecord> selectUrgeRecordList(String orderNo);

    Map<String,Object> getUnDealOrder() throws Exception;

    void haveLook(String id);

    YfbPayOrder selectTradeOrderDetail(String orderNo);

    SurveyOrderInfo selectByOrderNo(String orderNo, String agentNode);

}
