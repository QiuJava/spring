package cn.eeepay.framework.dao;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.util.WriteReadDataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.model.BusinessProductInfo;

@WriteReadDataSource
public interface BusinessProductInfoDao {

    @Select("SELECT service_id FROM business_product_info WHERE bp_id=#{bpId}")
    @ResultType(java.lang.String.class)
	List<String> findByProduct(@Param("bpId")String bpId);
    
    @Insert("INSERT INTO business_product_info(bp_id,service_id) values (#{map.bpId},#{map.serviceId})")
    int insert(@Param("map")Map<String, Object> map);

    @Delete("DELETE FROM business_product_info WHERE bp_id=#{bpId}")
	int deleteProductByPid(@Param("bpId")String id);
    
    @SelectProvider(type=SqlProvider.class,method="selectInfoByBpId")
    @ResultType(BusinessProductInfo.class)
	List<BusinessProductInfo> selectInfoByBpId(@Param("bpId")String bpId);
    
    public class SqlProvider{
    	public String selectInfoByBpId(Map<String,Object> param){
    		final String bpId=(String)param.get("bpId");
    		return new SQL(){{
    			SELECT("bpi.id,bpi.bp_id,bpi.service_id,sis.service_name");
    			FROM("business_product_info bpi "
    					+ "LEFT JOIN service_info sis on sis.service_id=bpi.service_id");
    			if(StringUtils.isNotBlank(bpId) && !bpId.equals("-1")){
    				WHERE(" bpi.bp_id=#{bpId}");
    			}
    		}}.toString();
    	}
    	
    }
}