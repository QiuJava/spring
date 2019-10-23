package cn.eeepay.framework.daoRedem.redemActive;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

/**
 * Created by 666666 on 2018/5/15.
 */
public interface RedemSysConfigDao {

    @Select("SELECT param_value FROM rdmp_sys_config WHERE param_key = #{keyName}")
    String getSysConfigValue(@Param("keyName") String keyName);

    @Select("SELECT oem_config_value FROM yfb_oem_config yoc\n" +
            "JOIN yfb_oem_agent yoa ON yoa.oem_no = yoc.oem_no\n" +
            "WHERE yoa.agent_no = #{agentNo}\n" +
            "AND yoa.agent_level = 1\n" +
            "AND yoc.oem_config_code = 'min_raise_amount' " +
            "limit 1")
    BigDecimal queryMinRaiseAmout(@Param("agentNo") String agentNo);


    @Select("SELECT oem_config_value FROM rdmp_oem_config yoc " +
            "JOIN rdmp_oem_agent yoa ON yoa.oem_no = yoc.oem_no " +
            "WHERE yoa.agent_no = #{agentNo} " +
            "AND yoa.agent_level = 1 " +
            "AND yoc.oem_config_code = 'min_raise_amount' " +
            "limit 1")
    BigDecimal queryRdmpMinRaiseAmout(@Param("agentNo") String agentNo);
}
