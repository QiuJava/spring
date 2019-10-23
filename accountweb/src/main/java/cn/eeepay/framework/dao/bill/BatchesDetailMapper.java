package cn.eeepay.framework.dao.bill;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import cn.eeepay.framework.model.bill.BatchesDetail;

public interface BatchesDetailMapper {
	@Select("select bd.*,bs.step_name from batches_detail bd left join batches_step bs on bd.step_id=bs.id where bd.batches_id=#{batchesId}")
	@ResultType(BatchesDetail.class)
	List<BatchesDetail> findByBatchesId(@Param("batchesId") Integer batchesId);
	
	@Update("update batches_detail set status=#{status} where id=#{id}")
	int updateStatusById(@Param("status")Integer status, @Param("id")Integer id);
	
	
	@Select("select execute_result from batches_detail where id=#{id}")
	@ResultType(BatchesDetail.class)
	BatchesDetail getExecuteLog(@Param("id")Integer id);
	
	@Update("update batches_detail set execute_result=concat(execute_result,#{log}) where id=#{id}")
	int updateExecuteLog(@Param("id")Integer id, @Param("log")String log);
}
