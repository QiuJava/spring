package cn.eeepay.framework.service;

import cn.eeepay.framework.model.surveyOrder.SurveyReplyRecord;

import java.util.List;
import java.util.Map;

/**
 * @author MXG
 * create 2018/09/13
 */
public interface SurveyOrderReplyService {

    int saveReply(SurveyReplyRecord record) throws Exception;

    SurveyReplyRecord selectRecordByOrderNo(String orderNo, String agentNode);

    void confirm(SurveyReplyRecord record) throws Exception;

    List<SurveyReplyRecord> selectRecordList(String orderNo, String agentNode);
}
