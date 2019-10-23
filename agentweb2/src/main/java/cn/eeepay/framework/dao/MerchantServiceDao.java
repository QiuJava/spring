package cn.eeepay.framework.dao;

import java.util.List;

import cn.eeepay.framework.util.WriteReadDataSource;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import cn.eeepay.framework.model.MerchantService;

@WriteReadDataSource
public interface MerchantServiceDao {


    @Update("update merchant_service set status=#{status} where id=#{id}")
    int updateByPrimaryKey(MerchantService record);
    
    @Select("select msi.*,sis.service_name from merchant_service msi "
    		+ "left join service_info sis on sis.service_id=msi.service_id "
    		+ "where msi.merchant_no=#{merId}")
    @ResultType(MerchantService.class)
    List<MerchantService> selectByMerId(@Param("merId")String merId);
    
    @Select("SELECT DISTINCT sis.service_type from merchant_service msi "
    		+ "LEFT JOIN service_info sis on sis.service_id=msi.service_id "
    		+ "WHERE merchant_no=#{merId}")
    @ResultType(String.class)
    List<String> selectServiceTypeByMerId(@Param("merId")String merId);
}