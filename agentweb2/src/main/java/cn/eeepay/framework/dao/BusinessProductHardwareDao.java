package cn.eeepay.framework.dao;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.util.WriteReadDataSource;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

@WriteReadDataSource
public interface BusinessProductHardwareDao {

	@Select("SELECT hp_id FROM business_product_hardware WHERE bp_id=#{bpId}")
	@ResultType(java.lang.String.class)
	List<String> findByProduct(@Param("bpId") String bpId);

	@Insert("INSERT INTO business_product_hardware(bp_id,hp_id) values (#{map.bpId},#{map.hpId})")
	int insert(@Param("map")Map<String, String> map);

	@Delete("DELETE FROM business_product_hardware WHERE bp_id=#{bpId}")
	int deleteProductByPid(@Param("bpId") String id);
}