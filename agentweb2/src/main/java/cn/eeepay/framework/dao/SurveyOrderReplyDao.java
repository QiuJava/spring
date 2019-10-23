package cn.eeepay.framework.dao;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.surveyOrder.SurveyReplyRecord;
import cn.eeepay.framework.util.WriteReadDataSource;
import org.apache.ibatis.annotations.*;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author MXG
 * create 2018/09/13
 */
@WriteReadDataSource
public interface SurveyOrderReplyDao {

    @Insert("INSERT INTO survey_reply_record(order_no,agent_node,reply_role_type,reply_role_no,reply_result," +
            "mer_name,mer_mobile,card_person_name,card_person_mobile,real_name,province,city,trans_address," +
            "reply_files_name,reply_remark,create_time) " +
            "VALUES(#{record.orderNo},#{record.agentNode},#{record.replyRoleType},#{record.replyRoleNo},#{record.replyResult}," +
            "#{record.merName},#{record.merMobile},#{record.cardPersonName},#{record.cardPersonMobile},#{record.realName}," +
            "#{record.province},#{record.city},#{record.transAddress},#{record.replyFilesName},#{record.replyRemark},#{record.createTime})")
    int saveReply(@Param("record") SurveyReplyRecord record);

    @Select("SELECT id,order_no,agent_node,reply_role_type,reply_role_no,reply_result,mer_name,mer_mobile," +
            "card_person_name,card_person_mobile,real_name,province,city,trans_address,reply_files_name," +
            "reply_remark,last_deal_remark,bak1,bak2," +
            "date_format(create_time,'%Y-%m-%d %H:%i:%s') as create_time," +
            "date_format(last_update_time,'%Y-%m-%d %H:%i:%s') as last_update_time " +
            "FROM survey_reply_record WHERE order_no=#{orderNo} and agent_node like concat(#{agentNode},'%') ORDER BY create_time DESC LIMIT 1")
    @ResultType(SurveyReplyRecord.class)
    SurveyReplyRecord selectRecordByOrderNo(@Param("orderNo") String orderNo, @Param("agentNode") String agentNode);

    @Update("UPDATE survey_reply_record SET " +
            "reply_result=#{record.replyResult},mer_name=#{record.merName},mer_mobile=#{record.merMobile}," +
            "card_person_name=#{record.cardPersonName},card_person_mobile=#{record.cardPersonMobile}," +
            "real_name=#{record.realName},province=#{record.province},city=#{record.city}," +
            "trans_address=#{record.transAddress},reply_files_name=#{record.replyFilesName}," +
            "reply_remark=#{record.replyRemark}, reply_role_type=#{record.replyRoleType}," +
            "reply_role_no=#{record.replyRoleNo}" +
            "WHERE id=#{record.id}")
    int updateSurveyReplyRecord(@Param("record") SurveyReplyRecord record);

    @Select("SELECT id,order_no,agent_node,reply_role_type,reply_role_no,reply_result,mer_name,mer_mobile," +
            "card_person_name,card_person_mobile,real_name,province,city,trans_address,reply_files_name," +
            "reply_remark,last_deal_remark,bak1,bak2," +
            "date_format(create_time,'%Y-%m-%d %H:%i:%s') as create_time," +
            "date_format(last_update_time,'%Y-%m-%d %H:%i:%s') as last_update_time " +
            "FROM survey_reply_record WHERE order_no=#{orderNo} and agent_node like concat(#{agentNode},'%') ORDER BY create_time DESC")
    @ResultType(SurveyReplyRecord.class)
    List<SurveyReplyRecord> selectRecordList(@Param("orderNo") String orderNo, @Param("agentNode") String agentNode);
}
