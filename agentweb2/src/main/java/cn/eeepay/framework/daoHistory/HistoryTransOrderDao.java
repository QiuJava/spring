package cn.eeepay.framework.daoHistory;

import cn.eeepay.framework.model.CollectiveTransOrder;
import cn.eeepay.framework.model.TransInfo;
import cn.eeepay.framework.util.WriteReadDataSource;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

/**
 * @author MXG
 * create 2018/09/25
 */
@WriteReadDataSource
public interface HistoryTransOrderDao {

    @Select("select * from collective_trans_order where order_no=#{transOrderNo}")
    @ResultType(CollectiveTransOrder.class)
    CollectiveTransOrder selectTransInfoByOrder(@Param("transOrderNo") String transOrderNo);
}
