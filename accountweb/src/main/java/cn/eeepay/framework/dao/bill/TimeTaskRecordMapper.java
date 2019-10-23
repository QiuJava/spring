package cn.eeepay.framework.dao.bill;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.Batches;
import cn.eeepay.framework.model.bill.TimeTaskRecord;
import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

public interface TimeTaskRecordMapper {

	@Select("select * from time_task_record where running_no = #{runningNo}")
	@ResultMap("cn.eeepay.framework.dao.bill.SysMenuMapper.BaseResultMap")
	public List<TimeTaskRecord> findByRunningNo(@Param("runningNo") String runningNo);

	@Insert("insert time_task_record(running_no,running_status,create_time,last_update_time) " +
			"values(#{timeTaskRecord.runningNo},#{timeTaskRecord.runningStatus},#{timeTaskRecord.createTime},#{timeTaskRecord.lastUpdateTime})")
	public void add(@Param("timeTaskRecord") TimeTaskRecord timeTaskRecord);

	@Update("update time_task_record set running_status = #{timeTaskRecord.runningStatus},last_update_time = #{timeTaskRecord.lastUpdateTime} " +
			"where running_no = #{timeTaskRecord.runningNo} and running_status = #{status}")
	public void updateByRunningNoAndStatus(@Param("timeTaskRecord") TimeTaskRecord timeTaskRecord,@Param("status") String status);

}
