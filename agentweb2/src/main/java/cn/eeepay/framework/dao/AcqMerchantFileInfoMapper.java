package cn.eeepay.framework.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import cn.eeepay.framework.model.AcqMerchantFileInfo;
import cn.eeepay.framework.util.WriteReadDataSource;
@WriteReadDataSource
public interface AcqMerchantFileInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(AcqMerchantFileInfo record);

    int insertSelective(AcqMerchantFileInfo record);

    AcqMerchantFileInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AcqMerchantFileInfo record);

    int updateByPrimaryKey(AcqMerchantFileInfo record);
    
    @Update("UPDATE acq_merchant_file_info\r\n" + 
    		"SET file_url = #{url} \r\n" + 
    		"WHERE\r\n" + 
    		"	acq_into_no = #{acqIntoNo}\r\n" + 
    		"AND file_type = #{urlType}")
	int updateUrl(@Param("acqIntoNo")String acqIntoNo,@Param("urlType") String urlType, @Param("url")String url);
}