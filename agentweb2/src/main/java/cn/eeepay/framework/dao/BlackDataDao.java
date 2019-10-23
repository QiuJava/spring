package cn.eeepay.framework.dao;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.blackAgent.*;
import cn.eeepay.framework.util.ReadOnlyDataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

@ReadOnlyDataSource
public interface BlackDataDao {
    @SelectProvider(type = BlackDataDao.SqlProvider.class, method = "selectByParamas")
    @ResultType(BlackInfo.class)
    List<BlackInfo> selectByParam(@Param("page")Page<BlackInfo> page, @Param("blackInfo") BlackInfo blackInfo);
    @Select("select one_agent_no as agentNo,order_no,merchant_no from black_data_info where order_no = #{orderNo} ")
    @ResultType(BlackInfo.class)
    BlackInfo selectByOrderNo(@Param("orderNo") String orderNo);
    @Select("SELECT br.order_no,br.orig_order_no,bi.mer_risk_rules_no as riskDealTemplateNo,br.risk_deal_msg,br.create_time,"
            + " ai.agent_no as oneAgentNo,ai.agent_Name as oneAgentName,mi.merchant_no,mi.merchant_name FROM black_data_deal_record br "
            + "LEFT JOIN black_data_info bi ON br.orig_order_no = bi.order_no "
            + "LEFT JOIN merchant_info mi ON mi.merchant_no = bi.merchant_no "
            + "LEFT JOIN agent_info ai  ON ai.agent_no = mi.agent_no "
            + "WHERE br.orig_order_no = #{orderNo} ORDER BY br.create_time DESC ")
    @ResultType(RiskNewAnswer.class)
    List<RiskNewAnswer> selectRiskNewAnswer(String orderNo);
    @Select("select *,date_format(create_time,'%Y-%m-%d %H:%i:%s') as createTime " +
            "from black_data_reply_record " +
            "where orig_order_no=#{orderNo} order by create_time DESC limit 1")
    @ResultType(ReplyRecord.class)
    ReplyRecord selectReplyRecord(@Param("orderNo") String orderNo);
    @Select("select *,date_format(create_time,'%Y-%m-%d %H:%i:%s') as createTime " +
            "from black_data_deal_record " +
            "where order_no=#{orderNo} and status <> '0' " +
            "order by create_time DESC limit 1")
    @ResultType(DealRecord.class)
    DealRecord selectByOrder(@Param("orderNo") String orderNo);

    @Select("SELECT bdi.mer_risk_rules_no AS rulesNo," +
            "bddr.risk_deal_msg AS dealMsg,date_format(bddr.create_time,'%Y-%m-%d %H:%i:%s') as dealTime," +
            "bdrr.reply_remark AS replyMsg,date_format(bdrr.create_time,'%Y-%m-%d %H:%i:%s') as replyTime," +
            "bdrr.replier_type, "+
            "bdrr.reply_files_name AS filesName " +
            "FROM black_data_deal_record bddr " +
            "LEFT JOIN black_data_info bdi ON bdi.order_no=bddr.orig_order_no " +
            "LEFT JOIN (SELECT * FROM black_data_reply_record WHERE STATUS='1') bdrr ON bdrr.deal_record_order_no=bddr.order_no " +
            "WHERE bddr.orig_order_no=#{orderNo} AND bddr.status='2' ORDER BY dealTime ASC")
    @ResultType(DealReplyRecord.class)
    List<DealReplyRecord> selectDealReplyRecord(@Param("orderNo") String orderNo);

    @Select("select *,date_format(create_time,'%Y-%m-%d %H:%i:%s') as createTime " +
            "from black_data_deal_record " +
            "where orig_order_no=#{origOrderNo} and status=#{status} " +
            "order by create_time DESC limit 1")
    @ResultType(DealRecord.class)
    DealRecord selectByOrigOrderNoAndStatus(@Param("origOrderNo") String origOrderNo, @Param("status") String status);


    class SqlProvider {
        public String selectByParamas(Map<String, Object> param) {
           final BlackInfo blackInfo = (BlackInfo) param.get("blackInfo");
            SQL sql = new SQL(){{
                SELECT("DISTINCT bd.id,bd.status,bd.order_no,bd.merchant_no,mi.merchant_name,ai.agent_no,ai.agent_name,bd.mer_last_deal_status,bd.risk_last_deal_status,bd.create_time,bd.mer_last_deal_time,bd.risk_last_deal_time");
                FROM("black_data_info bd "
                        +"LEFT JOIN merchant_info mi ON mi.merchant_no = bd.merchant_no "
                        +"LEFT JOIN agent_info ai  ON ai.agent_no = mi.agent_no "

                );
                if (StringUtils.isNotBlank(blackInfo.getMerchantName())){
                    WHERE("mi.merchant_name  LIKE #{blackInfo.merchantName}");
                }
                if (StringUtils.isNotBlank(blackInfo.getAgentName())){
                    WHERE("ai.agent_name  LIKE #{blackInfo.agentName}");
                }
                if (StringUtils.isNotBlank(blackInfo.getMerchantNo())){
                    WHERE("bd.merchant_no  = #{blackInfo.merchantNo}");
                }
                if (StringUtils.isNotBlank(blackInfo.getAgentNo())){
                    if (blackInfo.getAgentNo().contains("%")) {
                        WHERE("bd.agent_node LIKE #{blackInfo.agentNo}");
                    } else {
                        WHERE("mi.parent_node = #{blackInfo.agentNo}");
                    }
                }
                if (StringUtils.isNotBlank(blackInfo.getMerLastDealStatus()) && !blackInfo.getMerLastDealStatus().equals("-1")  ){
                    if (blackInfo.getMerLastDealStatus().equals("3")){
                        WHERE("bd.risk_last_deal_status  = '2'");
                    }else if (blackInfo.getMerLastDealStatus().equals("1")){
                        WHERE("bd.mer_last_deal_status  <= #{blackInfo.merLastDealStatus} and bd.risk_last_deal_status < '2'");
                    }
                    else {
                        WHERE("bd.mer_last_deal_status  = #{blackInfo.merLastDealStatus} and bd.risk_last_deal_status < '2'" );
                    }

                }
                if (StringUtils.isNotBlank(blackInfo.getCreateTimeStart())) {
                    WHERE("bd.create_time>=#{blackInfo.createTimeStart}");
                }
                if (  StringUtils.isNotBlank(blackInfo.getCreateTimeEnd())){
                    WHERE("bd.create_time<=#{blackInfo.createTimeEnd}");
                }
                    ORDER_BY("bd.create_time desc");
            }};
            return sql.toString();
        }
    }
}
