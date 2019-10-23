package cn.eeepay.framework.dao.nposp;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.model.bill.AcqOrg;

public interface AcqOrgMapper {
	@Select("select * from acq_org where acq_status = 1")
	@ResultType(AcqOrg.class)
	List<AcqOrg> findAll();
	
	@Select("select acq_name,phone from acq_org where id = #{userId}")
	@ResultType(AcqOrg.class)
	AcqOrg findAcqOrgByUserId(@Param("userId")String userId);
	
	@Select("select * from acq_org where acq_enname = #{acq_enname}")
	@ResultType(AcqOrg.class)
	AcqOrg findAcqOrgByAcqEnname(@Param("acq_enname")String acq_enname);
	

	@SelectProvider( type=SqlProvider.class,method="findAcqOrgListByParams")
	@ResultType(String.class)
	List<String> findAcqOrgListByParams(@Param("userName")String userName ,@Param("mobilephone")String mobilephone ) ;
	
	
	@Select("select concat(date_sub(curdate(),interval 2 day),' ',day_altered_time)as day_altered_time from acq_org where acq_enname = #{acq_enname}")
	@ResultType(String.class)
	String findDayAlteredStartTimeByAcqEnname(@Param("acq_enname")String acqEnname);

	@Select("select concat(date_sub(curdate(),interval 1 day),' ',day_altered_time)as day_altered_time from acq_org where acq_enname = #{acq_enname}")
	@ResultType(String.class)
	String findDayAlteredEndTimeByAcqEnname(@Param("acq_enname")String acqEnname);
	
	
public class SqlProvider{
		

		public String findAcqOrgListByParams(final Map<String, Object> parameter) {
			final String mobilephone = parameter.get("mobilephone").toString() ;
			final String userName = parameter.get("userName").toString() ;

			return new SQL(){{
				SELECT(" id ");						
				FROM(" acq_org ");
				if (!StringUtils.isBlank(mobilephone))
					WHERE(" phone like  \"%\"#{mobilephone}\"%\" ");		
				if (!StringUtils.isBlank(userName))
					WHERE(" acq_name like  \"%\"#{userName}\"%\" ");	
			}}.toString();
		}
}




	
	
	
}
