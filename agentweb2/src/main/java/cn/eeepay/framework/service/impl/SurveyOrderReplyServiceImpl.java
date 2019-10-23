package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.SurveyOrderDao;
import cn.eeepay.framework.dao.SurveyOrderReplyDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.surveyOrder.SurveyOrderInfo;
import cn.eeepay.framework.model.surveyOrder.SurveyReplyRecord;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.service.SurveyOrderReplyService;
import cn.eeepay.framework.service.SurveyOrderService;
import cn.eeepay.framework.util.DateUtil;
import cn.eeepay.framework.util.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 调单回复记录
 * @author MXG
 * create 2018/09/13
 */
@Service
public class SurveyOrderReplyServiceImpl implements SurveyOrderReplyService{

    @Resource
    private SurveyOrderReplyDao replyDao;
    @Resource
    private SurveyOrderDao surveyOrderDao;
    @Resource
    private AgentInfoService agentInfoService;

    /**
     * 代理商回复
     * @param record
     * @return
     */
    @Override
    public int saveReply(SurveyReplyRecord record) throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //保存回复记录
        AgentInfo loginAgent = agentInfoService.selectByPrincipal();
        record.setReplyRoleType("A");
        record.setReplyRoleNo(loginAgent.getAgentNo());
        record.setCreateTime(DateUtil.getFrontMinDate(0));
        //修改回复状态 一级代理商直接为已回复2和逾期已回复5 所属代理商为未回复（下级已提交）1和逾期微幅会（下级已提交）4
        SurveyOrderInfo order = surveyOrderDao.selectSurveyOrderByOrderNo(record.getOrderNo());
        String nowTime = DateUtil.getFrontMinDate(0);
        String replyEndTime = order.getReplyEndTime();
        Date nowDate = sdf.parse(nowTime);
        Date replyEndDate = sdf.parse(replyEndTime);
        String replyStatus = "1";//已提交
        if(loginAgent.getAgentLevel()==1){
            replyStatus = "2";//已确认
            if(replyEndDate.before(nowDate)){
                replyStatus = "5";//逾期确认
            }
        }else {
            if(replyEndDate.before(nowDate)){
                replyStatus = "4";//逾期提交
            }
        }
        surveyOrderDao.updateReplyStatusByOrderNo(order.getOrderNo(), replyStatus);
        if(loginAgent.getAgentLevel() != 1 && "8".equals(order.getDealStatus())){
        }else {
            surveyOrderDao.updateDealStatusByOrderNo(order.getOrderNo(),"0");
        }
        return replyDao.saveReply(record);
    }


    /**
     * 回复的修改、审核
     * @param record
     * @return
     */
    @Override
    public void confirm(SurveyReplyRecord record) throws Exception{
        //更新回复的修改
        record.setLastUpdateTime(DateUtils.getCurrentDateTime());
        replyDao.updateSurveyReplyRecord(record);

        //更新调单的回复状态
        AgentInfo loginAgent = agentInfoService.selectByPrincipal();
        if(loginAgent.getAgentLevel() == 1){
            SurveyOrderInfo order = surveyOrderDao.selectSurveyOrderByOrderNo(record.getOrderNo());
            String replyStatus = order.getReplyStatus();
            if("4".equals(replyStatus)){//逾期提交
                replyStatus = "5";//逾期确认
            }
            if("1".equals(replyStatus)){//已提交
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String nowTime = DateUtil.getFrontMinDate(0);
                String replyEndTime = order.getReplyEndTime();
                Date nowDate = sdf.parse(nowTime);
                Date replyEndDate = sdf.parse(replyEndTime);
                if(replyEndDate.before(nowDate)){
                    replyStatus = "5";//逾期确认
                }else{
                    replyStatus = "2";//已确认
                }
            }
            surveyOrderDao.updateReplyStatusByOrderNo(order.getOrderNo(), replyStatus);
            // 一级修改时分控已经处理了且处理结果非终态，则将状态改为未处理
            surveyOrderDao.updateDealStatusByOrderNo(order.getOrderNo(), "0");
        }
    }

    @Override
    public List<SurveyReplyRecord> selectRecordList(String orderNo, String agentNode) {
        return replyDao.selectRecordList(orderNo, agentNode);
    }


    @Override
    public SurveyReplyRecord selectRecordByOrderNo(String orderNo, String agentNode) {
        SurveyReplyRecord record = replyDao.selectRecordByOrderNo(orderNo, agentNode);
        if(record != null){
            if(StringUtils.isNotBlank(record.getReplyFilesName())){
                String[] str = record.getReplyFilesName().split(",");
                if(str!=null&&str.length>0){
                    record.setFileList(Arrays.asList(str));
                }
            }
        }
        return record;
    }

}
