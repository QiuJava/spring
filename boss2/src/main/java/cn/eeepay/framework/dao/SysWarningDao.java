package cn.eeepay.framework.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Map;

public interface SysWarningDao {

    @Select("select * from sys_warning where type=#{type}")
    @ResultType(Map.class)
    Map getByType(@Param("type") String type);

    @Update("UPDATE sys_warning SET cycle=#{map.cycle},num=#{map.num}, phones=#{map.phones} WHERE type=#{map.type}")
    int updateSysWarning(@Param("map") Map<String, Object> map);
}
