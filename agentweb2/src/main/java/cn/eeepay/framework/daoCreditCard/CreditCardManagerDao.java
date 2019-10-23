package cn.eeepay.framework.daoCreditCard;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.CreditCardManagerShare;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

public interface CreditCardManagerDao {


    @SelectProvider(type=SqlProvider.class,method="queryCreditCardManagerShareList")
    @ResultType(CreditCardManagerShare.class)
    List<CreditCardManagerShare> queryCreditCardManagerShareList(@Param("params")Map<String, Object> params, @Param("page")Page<CreditCardManagerShare> page);

    @SelectProvider(type=SqlProvider.class,method="queryCreditCardManagerShareList")
    @ResultType(CreditCardManagerShare.class)
    List<CreditCardManagerShare> queryExportAllInfoList(@Param("params")Map<String,Object> params);

    @Select(" SELECT SUM(share_cash) FROM cm_share WHERE share_agent_no = #{entityId} AND DATE(create_date) = CURRENT_DATE")
    String getTodayShareAmount(String entityId);

    @SelectProvider(type=SqlProvider.class,method="getShareTotalMoney")
    @ResultType(String.class)
    String getShareTotalMoney(@Param("params")Map<String,Object> params);

    @SelectProvider(type=SqlProvider.class,method="getTradeTotalMoney")
    @ResultType(String.class)
    String getTradeTotalMoney(@Param("params")Map<String,Object> params);


    class SqlProvider{
        public String queryCreditCardManagerShareList(Map<String,Object> map){
            final Map<String,Object> param=(Map<String,Object>)map.get("params");
            String sql = new SQL(){{
                SELECT("share.*");
                FROM("cm_share share");
                //订单类型
                if(StringUtils.isNotBlank(param.get("orderType") == null ? "" : param.get("orderType").toString())){
                    WHERE("share.order_type = #{params.orderType}");
                }
                //用户ID
                if (StringUtils.isNotBlank(param.get("userId") == null ? "" : param.get("userId").toString())){
                    WHERE("share.user_id = #{params.userId}");
                }
                //订单号
                if (StringUtils.isNotBlank(param.get("orderNo") == null ? "" : param.get("orderNo").toString())){
                    WHERE("share.related_order_no = #{params.orderNo}");
                }
                //入账状态
                if (StringUtils.isNotBlank(param.get("enterStatus") == null ? "" : param.get("enterStatus").toString())){
                    WHERE("share.enter_status = #{params.enterStatus}");
                }
                //创建时间
                if (StringUtils.isNotBlank(param.get("sCreateTime") == null ? "" : param.get("sCreateTime").toString())){
                    WHERE("share.create_date >= #{params.sCreateTime}");
                }
                if (StringUtils.isNotBlank(param.get("eCreateTime") == null ? "" : param.get("eCreateTime").toString())){
                    WHERE("share.create_date <= #{params.eCreateTime}");
                }

                //是否包含下级,不包含下级,直接
                String temp;
                temp=(String)param.get("agentNo");
                if("0".equals(param.get("bool"))&&StringUtils.isBlank(temp)){//不包含下级,名称为空,不显示数据
                    WHERE("1<0");
                }else if("1".equals(param.get("bool"))&&StringUtils.isNotBlank(temp)){//包含下级,有名称,显示根据名称查询出来的所有代理商
                    param.put("agentNo",temp);
                    WHERE("share.agent_node like concat((select agent_node from cm_share cs where cs.share_agent_no = #{params.agentNo}),'%')");
                }else if("0".equals(param.get("bool"))&&StringUtils.isNotBlank(temp)){//不包含,有名称,显示名称本身代理商
                    param.put("agentNo",temp);
                    WHERE("share.share_agent_no = #{params.agentNo}");
                }

                ORDER_BY("share.create_date desc");

            }}.toString();
            System.out.println(sql);
            return sql;
        }

        public String getShareTotalMoney(Map<String,Object> map){
            final Map<String,Object> param=(Map<String,Object>)map.get("params");
            String sql = new SQL(){{
                SELECT("sum(share.share_cash) as shareCash");
                FROM("cm_share share");
                //订单类型
                if(StringUtils.isNotBlank(param.get("orderType") == null ? "" : param.get("orderType").toString())){
                    WHERE("share.order_type = #{params.orderType}");
                }
                //用户ID
                if (StringUtils.isNotBlank(param.get("userId") == null ? "" : param.get("userId").toString())){
                    WHERE("share.user_id = #{params.userId}");
                }
                //订单号
                if (StringUtils.isNotBlank(param.get("orderNo") == null ? "" : param.get("orderNo").toString())){
                    WHERE("share.related_order_no = #{params.orderNo}");
                }
                //入账状态
                if (StringUtils.isNotBlank(param.get("enterStatus") == null ? "" : param.get("enterStatus").toString())){
                    WHERE("share.enter_status = #{params.enterStatus}");
                }
                //创建时间
                if (StringUtils.isNotBlank(param.get("sCreateTime") == null ? "" : param.get("sCreateTime").toString())){
                    WHERE("share.create_date >= #{params.sCreateTime}");
                }
                if (StringUtils.isNotBlank(param.get("eCreateTime") == null ? "" : param.get("eCreateTime").toString())){
                    WHERE("share.create_date <= #{params.eCreateTime}");
                }

                //是否包含下级,不包含下级,直接
                String temp;
                temp=(String)param.get("agentNo");
                if("0".equals(param.get("bool"))&&StringUtils.isBlank(temp)){//不包含下级,名称为空,不显示数据
                    WHERE("1<0");
                }else if("1".equals(param.get("bool"))&&StringUtils.isNotBlank(temp)){//包含下级,有名称,显示根据名称查询出来的所有代理商
                    param.put("agentNo",temp);
                    WHERE("share.agent_node like concat((select agent_node from cm_share cs where cs.share_agent_no = #{params.agentNo}),'%')");
                }else if("0".equals(param.get("bool"))&&StringUtils.isNotBlank(temp)){//不包含,有名称,显示名称本身代理商
                    param.put("agentNo",temp);
                    WHERE("share.share_agent_no = #{params.agentNo}");
                }


            }}.toString();
            System.out.println(sql);
            return sql;
        }

        public String getTradeTotalMoney(Map<String,Object> map){
            final Map<String,Object> param=(Map<String,Object>)map.get("params");
            String sql = new SQL(){{
                SELECT("sum(share.order_cash) as orderCash");
                FROM("cm_share share");
                //订单类型
                if(StringUtils.isNotBlank(param.get("orderType") == null ? "" : param.get("orderType").toString())){
                    WHERE("share.order_type = #{params.orderType}");
                }
                //用户ID
                if (StringUtils.isNotBlank(param.get("userId") == null ? "" : param.get("userId").toString())){
                    WHERE("share.user_id = #{params.userId}");
                }
                //订单号
                if (StringUtils.isNotBlank(param.get("orderNo") == null ? "" : param.get("orderNo").toString())){
                    WHERE("share.related_order_no = #{params.orderNo}");
                }
                //入账状态
                if (StringUtils.isNotBlank(param.get("enterStatus") == null ? "" : param.get("enterStatus").toString())){
                    WHERE("share.enter_status = #{params.enterStatus}");
                }
                //创建时间
                if (StringUtils.isNotBlank(param.get("sCreateTime") == null ? "" : param.get("sCreateTime").toString())){
                    WHERE("share.create_date >= #{params.sCreateTime}");
                }
                if (StringUtils.isNotBlank(param.get("eCreateTime") == null ? "" : param.get("eCreateTime").toString())){
                    WHERE("share.create_date <= #{params.eCreateTime}");
                }

                //是否包含下级,不包含下级,直接
                String temp;
                temp=(String)param.get("agentNo");
                if("0".equals(param.get("bool"))&&StringUtils.isBlank(temp)){//不包含下级,名称为空,不显示数据
                    WHERE("1<0");
                }else if("1".equals(param.get("bool"))&&StringUtils.isNotBlank(temp)){//包含下级,有名称,显示根据名称查询出来的所有代理商
                    param.put("agentNo",temp);
                    WHERE("share.agent_node like concat((select agent_node from cm_share cs where cs.share_agent_no = #{params.agentNo}),'%')");
                }else if("0".equals(param.get("bool"))&&StringUtils.isNotBlank(temp)){//不包含,有名称,显示名称本身代理商
                    param.put("agentNo",temp);
                    WHERE("share.share_agent_no = #{params.agentNo}");
                }


            }}.toString();
            System.out.println(sql);
            return sql;
        }


    }

}
