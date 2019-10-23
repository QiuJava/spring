package cn.eeepay.framework.dao;

import java.util.List;

import cn.eeepay.framework.util.WriteReadDataSource;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import cn.eeepay.framework.model.AuditorManager;

@WriteReadDataSource
public interface AuditorManagerDao {

	@Select("select * from auditor_manager where bp_id=#{bpId} and status='1'")
	@ResultType(AuditorManager.class)
	List<AuditorManager> findAllInfo(@Param("bpId")String bpId);
}
