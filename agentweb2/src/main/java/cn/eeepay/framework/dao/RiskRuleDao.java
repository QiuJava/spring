package cn.eeepay.framework.dao;

import cn.eeepay.framework.model.ActivityDetail;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.util.ReadOnlyDataSource;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

/**
 * Created by 666666 on 2017/12/29.
 */
@ReadOnlyDataSource
public interface RiskRuleDao {

    @Select("select * from risk_rules where rules_no = #{ruleNo} and status='1'")
    @ResultType(Map.class)
    Map<String,Object> selectRiskRule(@Param("ruleNo")String ruleNo);

}
