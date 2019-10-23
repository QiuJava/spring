package cn.eeepay.framework.dao.nposp;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.model.nposp.MerchantServiceRate;

public interface MerchantServiceRateMapper {
	
    @SelectProvider(type=SqlProvider.class,method="findMsrList")
    @ResultType(MerchantServiceRate.class)
    List<MerchantServiceRate> selectByMertId(@Param("record")MerchantServiceRate record);
    
    public class SqlProvider{
    	public String findMsrList(Map<String,Object> param){
			final MerchantServiceRate record=(MerchantServiceRate)param.get("record");
			return new SQL(){{
				SELECT("msr.*,sis.service_name");
				FROM("merchant_service_rate msr "
						+ "LEFT JOIN service_info sis on sis.service_id=msr.service_id ");
				if(StringUtils.isNotBlank(record.getMerchantNo())){
					WHERE(" msr.merchant_no=#{record.merchantNo}");
				}
			}}.toString();
		}
    	
    }
}