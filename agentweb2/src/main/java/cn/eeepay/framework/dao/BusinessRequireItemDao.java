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
public interface BusinessRequireItemDao {

	@Select("SELECT br_id FROM business_require_item WHERE bp_id=#{bpId} order by id")
	@ResultType(java.lang.String.class)
	List<String> findByProduct(@Param("bpId") String bpId);
	
	@Insert("INSERT INTO business_require_item(bp_id,br_id) values (#{map.bpId},#{map.itemId})")
	int insert(@Param("map")Map<String, String> map);

	@Delete("DELETE FROM business_require_item WHERE bp_id=#{bpId}")
	int deleteProductByPid(@Param("bpId") String id);
	
	@Select("SELECT br_id FROM business_require_item WHERE bp_id=#{bpId} and br_id=#{itemId}")
	@ResultType(java.lang.String.class)
	List<String> findMerItem(@Param("bpId") String bpId,@Param("itemId") String itemId);
}