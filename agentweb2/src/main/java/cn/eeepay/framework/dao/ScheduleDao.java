package cn.eeepay.framework.dao;

import cn.eeepay.framework.util.WriteReadDataSource;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Map;

@WriteReadDataSource
public interface ScheduleDao {

    @Insert("INSERT INTO time_task_record(running_no,running_status,source_system, interface_name) \n" +
            "VALUE(#{runNo},'running', 'agentweb', #{interfaceType})")
    int insert(@Param("runNo") String runNo, @Param("interfaceType") String interfaceType);

    @Update("update time_task_record set running_status =#{status} where running_no=#{runNo}")
    int updateStatus(@Param("runNo") String runNo, @Param("status") String status);

    @Select("select * from time_task_record where running_no=#{runNo} limit 1")
    Map<String, Object> query(String runNo);
}
