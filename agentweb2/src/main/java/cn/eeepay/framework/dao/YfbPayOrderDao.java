package cn.eeepay.framework.dao;

import cn.eeepay.framework.model.YfbPayOrder;
import cn.eeepay.framework.util.ReadOnlyDataSource;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

/**
 * @author MXG
 * create 2018/11/27
 */
@ReadOnlyDataSource
public interface YfbPayOrderDao {

    @Select("select order_no orderNo,trans_status transStatus " +
            "from yfb_pay_order " +
            "where order_no=#{orderNo}")
    @ResultType(Map.class)
    Map<String,String> selectByOrderNO(@Param("orderNo") String orderNo);

    //交易详情
    @Select("SELECT ypo.order_no,ypo.trans_type,ypo.trans_status,ypo.trans_amount,ypo.account_no,ypo.trans_time,ypo.card_type,ypo.res_msg,ycm.bank_name,"
            + "ypo.merchant_no,yrmi.one_agent_no,ypo.trans_fee_rate,ypo.trans_fee,ai.agent_name one_agent_name,"
            + "ypo.acq_code,ypo.acq_merchant_no,ypc.pay_merchant_name "
            + "FROM yfb_pay_order ypo "
            + "LEFT JOIN yfb_card_manage ycm ON ycm.account_no = ypo.account_no "
            + "LEFT JOIN yfb_repay_merchant_info yrmi ON yrmi.merchant_no = ypo.merchant_no "
            + "LEFT JOIN agent_info ai ON ai.agent_no = yrmi.one_agent_no "
            + "LEFT JOIN yfb_pay_channel ypc ON ypc.pay_merchant_no = ypo.acq_merchant_no "
            + "WHERE ypo.order_no = #{orderNo}")
    @ResultType(YfbPayOrder.class)
    YfbPayOrder selectTradeOrderDetail(String orderNo);
}
