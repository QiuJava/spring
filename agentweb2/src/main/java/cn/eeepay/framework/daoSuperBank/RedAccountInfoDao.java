package cn.eeepay.framework.daoSuperBank;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.RedAccountDetail;
import cn.eeepay.framework.model.RedAccountInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface RedAccountInfoDao {

    @Update("update red_account_info set total_amount=total_amount+#{notReceiveAmount}" +
            " where relation_id=#{relationId}")
    int updateAmount(@Param("relationId") Long relationId,
                     @Param("notReceiveAmount") BigDecimal notReceiveAmount);

    @Select("select * from red_account_info where relation_id=#{relationId}")
    @ResultType(RedAccountInfo.class)
    RedAccountInfo selectByRelationId(@Param("relationId") Long relationId);

    @Insert("insert into red_account_detail(red_account_id,account_code,create_date,type," +
            "trans_amount,red_order_id,remark)" +
            "values(#{redAccountId},#{accountCode},#{createDate},#{type}," +
            "#{transAmount},#{redOrderId},#{remark})")
    int insertAccountDetail(RedAccountDetail accountDetail);

    @Select("select id,type,relation_id,account_code,total_amount from red_account_info where type=0 limit 1")
    RedAccountInfo plateAccountInfo();

    @SelectProvider(type=SqlProvider.class, method="selectAccountDetailPage")
    @ResultType(RedAccountDetail.class)
    List<RedAccountDetail> selectAccountDetailPage(@Param("baseInfo") RedAccountDetail baseInfo, @Param("page")Page<RedAccountDetail> page);

    @SelectProvider(type=SqlProvider.class, method="selectAccountDetailPage")
    @ResultType(RedAccountDetail.class)
    RedAccountDetail selectAccountDetailSum(@Param("baseInfo")RedAccountDetail baseInfo);

    class SqlProvider{
        public String selectAccountDetailPage(Map<String, Object> param){
            RedAccountDetail baseInfo = (RedAccountDetail)param.get("baseInfo");
            SQL sql = new SQL();
            if(baseInfo != null && baseInfo.getSelectType() != null && baseInfo.getSelectType() == 1){
                sql.SELECT("sum(rad.trans_amount) as transAmountSum");
            } else {
                sql.SELECT("rad.*,ro.bus_type");
            }
            sql.FROM("red_account_detail rad");
            sql.LEFT_OUTER_JOIN("red_orders ro on ro.id=rad.red_order_id");
            sql.WHERE("rad.red_account_id=#{baseInfo.redAccountId}");
            if(StringUtils.isNotBlank(baseInfo.getCreateDateStart())){
                sql.WHERE("rad.create_date >= #{baseInfo.createDateStart}");
            }
            if(StringUtils.isNotBlank(baseInfo.getCreateDateEnd())){
                sql.WHERE("rad.create_date <= #{baseInfo.createDateEnd}");
            }
            if(StringUtils.isNotBlank(baseInfo.getType())){
                sql.WHERE("rad.type=#{baseInfo.type}");
            }
            if(StringUtils.isNotBlank(baseInfo.getBusType())){
                sql.WHERE("ro.bus_type=#{baseInfo.busType}");
            }
            return sql.toString();
        }
    }
}
