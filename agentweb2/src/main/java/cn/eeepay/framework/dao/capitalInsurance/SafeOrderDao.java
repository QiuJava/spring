package cn.eeepay.framework.dao.capitalInsurance;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.capitalInsurance.OrderTotal;
import cn.eeepay.framework.model.capitalInsurance.SafeConfig;
import cn.eeepay.framework.model.capitalInsurance.SafeOrder;
import cn.eeepay.framework.util.WriteReadDataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/7/23/023.
 * @author liuks
 * 保险订单DAO
 */
@WriteReadDataSource
public interface SafeOrderDao {

    @SelectProvider(type=SafeOrderDao.SqlProvider.class,method="selectAllList")
    @ResultType(SafeOrder.class)
    List<SafeOrder> selectAllList(@Param("order") SafeOrder order, @Param("page") Page<SafeOrder> page);

    @SelectProvider(type=SafeOrderDao.SqlProvider.class,method="selectSum")
    @ResultType(OrderTotal.class)
    OrderTotal selectSum(@Param("order") SafeOrder order, @Param("page") Page<SafeOrder> page);

    @SelectProvider(type=SafeOrderDao.SqlProvider.class,method="selectAllList")
    @ResultType(SafeOrder.class)
    List<SafeOrder> exportDetailSelect(@Param("order")SafeOrder order);

    @Select(
            "select * from zjx_safe_config "
    )
    List<SafeConfig> getSafeConfigList();


    class SqlProvider{
        public String selectAllList(final Map<String, Object> param) {
            return  getSelectSql(param,1);
        }
        public String selectSum(final Map<String, Object> param) {
            return  getSelectSql(param,2);
        }
        public String getSelectSql(final Map<String, Object> param,int sta) {
            final SafeOrder order = (SafeOrder) param.get("order");
            StringBuffer sb=new StringBuffer();
            sb.append("select ");
            if(sta==1){
                sb.append(" ord.id,ord.bx_order_no,ord.order_no,ord.third_order_no,ord.merchant_no, ");
                sb.append(" ord.one_agent_no,ord.bx_unit,ord.prod_no,ord.n_amt,ord.n_prm, ");
                sb.append(" ord.bx_type,ord.t_time,ord.t_begin_time,ord.t_end_time,ord.create_time, ");
                sb.append(" cto.settlement_method,cto.trans_amount,cto.trans_status  ");
            }else if(sta==2){
                sb.append(" count(*) countTotal, ");
                sb.append(" sum(ord.n_prm) nPrmTotal ");
            }
            sb.append("from zjx_trans_order ord ");
            sb.append(" LEFT JOIN collective_trans_order cto ON cto.order_no=ord.order_no ");

            sb.append(" where 1=1 ");
            if(StringUtils.isNotBlank(order.getBxOrderNo())){
                sb.append(" and ord.bx_order_no = #{order.bxOrderNo} ");
            }
            if(StringUtils.isNotBlank(order.getBxUnit())){
                sb.append(" and ord.bx_unit = #{order.bxUnit} ");
            }
            if(StringUtils.isNotBlank(order.getProdNo())){
                sb.append(" and ord.prod_no = #{order.prodNo} ");
            }
            if(StringUtils.isNotBlank(order.getThirdOrderNo())){
                sb.append(" and ord.third_order_no = #{order.thirdOrderNo} ");
            }
            if(StringUtils.isNotBlank(order.getBxType())){
                sb.append(" and ord.bx_type = #{order.bxType} ");
            }
            if(StringUtils.isNotBlank(order.getOrderNo())){
                sb.append(" and ord.order_no = #{order.orderNo} ");
            }
            if(StringUtils.isNotBlank(order.getSettlementMethod())){
                sb.append(" and cto.settlement_method = #{order.settlementMethod} ");
            }
            if(StringUtils.isNotBlank(order.getMerchantNo())){
                sb.append(" and ord.merchant_no = #{order.merchantNo} ");
            }
            if (order.gettTimeBegin()!=null) {
                sb.append(" and  ord.t_time>=#{order.tTimeBegin}");
            }
            if (order.gettTimeEnd()!=null) {
                sb.append(" and  ord.t_time<=#{order.tTimeEnd}");
            }
            //代理商是否包含下级
            if(StringUtils.isNotBlank(order.getAgentNo())){
                if(order.getLowerAgent()!=null){
                    if(order.getLoginAgentNo().equals(order.getAgentNo())){
                        if(order.getLowerAgent().intValue()==1){
                            sb.append(" and cto.agent_node LIKE (SELECT CONCAT(agent_node,'%') FROM agent_info a where a.agent_no=#{order.loginAgentNo})");
                        }else{
                            sb.append(" and cto.agent_node = (SELECT agent_node FROM agent_info a where a.agent_no=#{order.agentNo})");
                            sb.append(" and cto.agent_node LIKE (SELECT CONCAT(agent_node,'%') FROM agent_info a where a.agent_no=#{order.loginAgentNo})");
                        }
                    }else{
                        sb.append(" and cto.agent_node LIKE (SELECT CONCAT(agent_node,'%') FROM agent_info a where a.agent_no=#{order.agentNo})");
                        sb.append(" and cto.agent_node LIKE (SELECT CONCAT(agent_node,'%') FROM agent_info a where a.agent_no=#{order.loginAgentNo})");
                    }
                }
            }else{
                sb.append(" and cto.agent_node LIKE (SELECT CONCAT(agent_node,'%') FROM agent_info a where a.agent_no=#{order.loginAgentNo})");
            }
            sb.append("order by ord.id desc ");
            return  sb.toString();
        }
    }
}
