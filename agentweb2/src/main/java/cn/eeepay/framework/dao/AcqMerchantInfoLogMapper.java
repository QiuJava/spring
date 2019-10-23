package cn.eeepay.framework.dao;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import cn.eeepay.framework.model.AcqMerchantInfoLog;
import cn.eeepay.framework.util.WriteReadDataSource;
@WriteReadDataSource
public interface AcqMerchantInfoLogMapper {
    int deleteByPrimaryKey(Integer id);

	int insert(AcqMerchantInfoLog record);

	int insertSelective(AcqMerchantInfoLog record);

	AcqMerchantInfoLog selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(AcqMerchantInfoLog record);

	int updateByPrimaryKey(AcqMerchantInfoLog record);
    
	@Select("SELECT\r\n" + 
			"	*\r\n" + 
			"FROM\r\n" + 
			"	acq_merchant_info_log\r\n" + 
			"WHERE\r\n" + 
			"	acq_merchant_info_id = #{id} " +
			"ORDER BY\r\n" + 
			"	create_time DESC")
	List<AcqMerchantInfoLog> findAuditListByIntoId(Long id);

}