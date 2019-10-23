package cn.eeepay.framework.dao;

import cn.eeepay.framework.daoPerAgent.PerAgentDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.PaUserInfo;
import cn.eeepay.framework.model.surveyOrder.SurveyOrderInfo;
import cn.eeepay.framework.model.surveyOrder.SurveyReplyRecord;
import cn.eeepay.framework.model.surveyOrder.SurveyUrgeRecord;
import cn.eeepay.framework.util.WriteReadDataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * @author MXG
 * create 2018/09/07
 */
@WriteReadDataSource
public interface SurveyOrderDao {

    @SelectProvider(type = SurveyOrderDao.SqlProvider.class, method = "selectByParam")
    @ResultType(SurveyOrderInfo.class)
    List<SurveyOrderInfo> selectByParamWithPage(@Param("page") Page<SurveyOrderInfo> page, @Param("order") SurveyOrderInfo order);

    @SelectProvider(type = SurveyOrderDao.SqlProvider.class, method = "selectByParam")
    @ResultType(SurveyOrderInfo.class)
    List<SurveyOrderInfo> selectByParam(@Param("order") SurveyOrderInfo order);

    @Select("SELECT s.id,s.order_no,s.merchant_no,s.trans_order_no,s.acq_reference_no,s.trans_order_database,s.trans_status," +
            "s.order_service_code,s.order_type_code,s.template_files_name,s.order_remark,s.order_status," +
            "s.reply_status,s.deal_status,s.deal_remark,s.agent_node,s.trans_amount,s.trans_account_no," +
            "s.acq_code,s.acq_merchant_no,s.pay_method,s.urge_num," +
            "date_format(s.create_time,'%Y-%m-%d %H:%i:%s') as create_time," +
            "date_format(s.reply_end_time,'%Y-%m-%d %H:%i:%s') as reply_end_time," +
            "date_format(s.trans_time,'%Y-%m-%d %H:%i:%s') as trans_time," +
            "m.merchant_name,m.mobilephone,t.serial_no  " +
            "FROM survey_order_info s " +
            "LEFT JOIN merchant_info m ON s.merchant_no=m.merchant_no " +
            "LEFT JOIN trans_info t ON s.trans_order_no=t.order_no " +
            "WHERE s.id=#{id} and agent_node like concat(#{agentNode},'%')")
    @ResultType(SurveyOrderInfo.class)
    SurveyOrderInfo selectByIdAndAgentNode(@Param("id") String id, @Param("agentNode") String agentNode);

    @Select("SELECT s.* FROM survey_order_info s WHERE s.order_no=#{orderNo}")
    @ResultType(SurveyOrderInfo.class)
    SurveyOrderInfo selectSurveyOrderByOrderNo(@Param("orderNo") String orderNo);

    @Update("update survey_order_info set reply_status=#{replyStatus},last_reply_time=now() where order_no=#{orderNo}")
    int updateReplyStatusByOrderNo(@Param("orderNo") String orderNo, @Param("replyStatus")String replyStatus);

    @Select("SELECT order_no,DATE_FORMAT(create_time,'%Y-%m-%d %H:%i:%s') AS create_time FROM survey_urge_record WHERE order_no=#{orderNo}")
    @ResultType(SurveyUrgeRecord.class)
    List<SurveyUrgeRecord> selectUrgeRecordList(@Param("orderNo") String orderNo);

    @Select("SELECT *,reply_end_time,reply_end_time<NOW() AS overdue " +
            "FROM survey_order_info " +
            "WHERE (reply_status='0' or reply_status='3') AND agent_node=#{agentNode} AND order_status='1'")
    @ResultType(SurveyOrderInfo.class)
    List<SurveyOrderInfo> countUnReplyOrder(@Param("agentNode") String agentNode);

    @Select("SELECT *,reply_end_time<NOW() AS overdue " +
            "FROM survey_order_info " +
            "WHERE (reply_status='0' OR reply_status='1' OR reply_status='3' or reply_status='4') " +
            "AND agent_node LIKE concat(#{agentNode},'%') " +
            "AND order_status='1'")
    @ResultType(SurveyOrderInfo.class)
    List<SurveyOrderInfo> countUnConfirmOrder(@Param("agentNode") String agentNode);

    //（非一级）直属代理商催单
    @Select("SELECT sur.*,NOW() AS currentTime,soi.trans_order_no,soi.id AS surveyOrderId " +
            "FROM survey_urge_record sur  " +
            "LEFT JOIN survey_order_info soi ON sur.order_no=soi.order_no " +
            "WHERE sur.agent_node=#{agentNode} and status='1' and NOT FIND_IN_SET(#{agentNo}, have_look_no)" +
            "ORDER BY create_time DESC")
    @ResultType(SurveyUrgeRecord.class)
    List<SurveyUrgeRecord> selectUrgeRecordByAgentNode(@Param("agentNode") String agentNode, @Param("agentNo")String agentNo);


    @Update("UPDATE survey_urge_record SET have_look_no=#{urgeRecord.haveLookNo} WHERE id=#{urgeRecord.id}")
    int haveLook(@Param("urgeRecord") SurveyUrgeRecord urgeRecord);

    //一级代理商的催单
    @Select("SELECT sur.*,NOW() AS currentTime,soi.trans_order_no,soi.id AS surveyOrderId " +
            "FROM survey_urge_record sur  " +
            "LEFT JOIN survey_order_info soi ON sur.order_no=soi.order_no " +
            "WHERE sur.agent_node like concat(#{agentNode},'%') and status='1' and (NOT FIND_IN_SET(#{agentNo}, have_look_no) OR have_look_no IS NULL)" +
            "ORDER BY create_time DESC")
    @ResultType(SurveyUrgeRecord.class)
    List<SurveyUrgeRecord> selectOneAgentUrgeRecord(@Param("agentNode") String agentNode, @Param("agentNo")String agentNo);

    @Select("select * from survey_urge_record where id=#{id}")
    @ResultType(SurveyUrgeRecord.class)
    SurveyUrgeRecord selectUrgeRecordById(@Param("id") String id);

    @Update("update survey_order_info set deal_status=#{dealStatus} where order_no=#{orderNo}")
    int updateDealStatusByOrderNo(@Param("orderNo") String orderNo, @Param("dealStatus") String dealStatus);

    @Select("select * from survey_order_info where order_no = #{orderNo} and agent_node like concat(#{agentNode},'%')")
    SurveyOrderInfo selectByOrderNo(@Param("orderNo") String orderNo, @Param("agentNode") String agentNode);


    public class SqlProvider{

        public String selectByParam(Map<String, Object> param){
            final SurveyOrderInfo order = (SurveyOrderInfo)param.get("order");
            SQL sql = new SQL(){
                {
                    SELECT("s.id,s.order_no,s.merchant_no,s.trans_order_no,s.acq_reference_no,s.trans_order_database,s.trans_status," +
                            "s.order_service_code,s.order_type_code,s.template_files_name,s.order_remark,s.order_status," +
                            "s.reply_status,s.deal_status,s.deal_remark,s.agent_node,s.trans_amount,s.trans_account_no," +
                            "s.acq_code,s.acq_merchant_no,s.pay_method,s.urge_num," +
                            "date_format(s.create_time,'%Y-%m-%d %H:%i:%s') as create_time," +
                            "date_format(s.reply_end_time,'%Y-%m-%d %H:%i:%s') as reply_end_time," +
                            "date_format(s.trans_time,'%Y-%m-%d %H:%i:%s') as trans_time,a.agent_name," +
                            "(SELECT DISTINCT a_i.team_name FROM app_info a_i WHERE a_i.team_id = mi.team_id) AS merGroup");
                    FROM("survey_order_info s");
                    //LEFT_OUTER_JOIN("trans_info t on s.trans_order_no=t.order_no");
                    LEFT_OUTER_JOIN("agent_info a on s.agent_node=a.agent_node");
                    LEFT_OUTER_JOIN("merchant_info mi ON mi.merchant_no = s.merchant_no");
                }
            };
            sqlWhere(sql, order);
            sql.ORDER_BY("s.id DESC");
            return sql.toString();
        }

        private void sqlWhere(SQL sql, SurveyOrderInfo order){
            if(StringUtils.isBlank(order.getContainSubordinate())){
                sql.WHERE("s.agent_node like concat(#{order.agentNode},'%')");
            }else{
                if("0".equals(order.getContainSubordinate())){
                   sql.WHERE("s.agent_node=#{order.agentNode}");
                }else {
                    sql.WHERE("s.agent_node like concat(#{order.agentNode},'%')");
                }
            }
            sql.WHERE("s.reply_status!=6");//状态6为无需处理
            if(StringUtils.isNotBlank(order.getTransOrderNo())){
                sql.WHERE("s.trans_order_no=#{order.transOrderNo}");
            }
            if(StringUtils.isNotBlank(order.getAcqReferenceNo())){
                sql.WHERE("s.acq_reference_no=#{order.acqReferenceNo}");
            }
            if(StringUtils.isNotBlank(order.getTransAccountNo())){
                sql.WHERE("s.trans_account_no=#{order.transAccountNo}");
            }
            if(StringUtils.isNotBlank(order.getOrderTypeCode())){
                sql.WHERE("s.order_type_code=#{order.orderTypeCode}");
            }
            if(StringUtils.isNotBlank(order.getReplyStatus())){
                sql.WHERE("s.reply_status=#{order.replyStatus}");
            }
            if(StringUtils.isNotBlank(order.getDealStatus())){
                if("1".equals(order.getDealStatus())){
                    sql.WHERE("(s.deal_status='1' or s.deal_status='2' or s.deal_status='3' or s.deal_status='4' " +
                            "or s.deal_status='5' or s.deal_status='6' or s.deal_status='7' )");
                }else {
                    sql.WHERE("s.deal_status=#{order.dealStatus}");
                }

            }
            if(StringUtils.isNotBlank(order.getMerchantNo())){
                sql.WHERE("s.merchant_no=#{order.merchantNo}");
            }
            if(StringUtils.isNotBlank(order.getPayMethod())){
                sql.WHERE("s.pay_method=#{order.payMethod}");
            }
            if(StringUtils.isNotBlank(order.getTransStatus())){
                sql.WHERE("s.trans_status=#{order.transStatus}");
            }
            if(StringUtils.isNotBlank(order.getOrderServiceCode())){
                sql.WHERE("s.order_service_code=#{order.orderServiceCode}");
            }
            if(StringUtils.isNotBlank(order.getCreateTimeStart())){
                sql.WHERE("s.create_time>=#{order.createTimeStart}");
            }
            if(StringUtils.isNotBlank(order.getCreateTimeEnd())){
                sql.WHERE("s.create_time<=#{order.createTimeEnd}");
            }
            if(StringUtils.isNotBlank(order.getReplyEndTimeStart())){
                sql.WHERE("s.reply_end_time>=#{order.replyEndTimeStart}");
            }
            if(StringUtils.isNotBlank(order.getReplyEndTimeEnd())){
                sql.WHERE("s.reply_end_time<=#{order.replyEndTimeEnd}");
            }
            if(StringUtils.isNotBlank(order.getLastUpdateTimeStart())){
                sql.WHERE("s.last_update_time>=#{order.lastUpdateTimeStart}");
            }
            if(StringUtils.isNotBlank(order.getLastUpdateTimeEnd())){
                sql.WHERE("s.last_update_time<=#{order.lastUpdateTimeEnd}");
            }
            if(!"-1".equals(order.getMerTeamId())){
                sql.WHERE(" mi.team_id=#{order.merTeamId}");
            }
            sql.WHERE("order_status='1'");
        }
    }
}
