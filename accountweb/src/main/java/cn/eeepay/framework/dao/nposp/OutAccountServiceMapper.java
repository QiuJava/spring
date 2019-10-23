package cn.eeepay.framework.dao.nposp;

import cn.eeepay.framework.model.nposp.OutAccountService;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

public interface OutAccountServiceMapper {
//	@Select("select * from out_account_service where acq_enname=#{acqEnname} and service_type in(4,5) and out_account_status = 1")
//	@ResultType(OutAccountService.class)
//	List<OutAccountService> findByAcqEnnameAndServiceType(@Param("acqEnname")String acqEnname, @Param("serviceType")Integer serviceType);
	
	@Select("select * from out_account_service where acq_enname=#{acqEnname} and service_type=#{serviceType} and out_account_status = 1")
	@ResultType(OutAccountService.class)
	OutAccountService findEntityByAcqEnnameAndServiceType(@Param("acqEnname")String acqEnname, @Param("serviceType")Integer serviceType);

	@Select("select * from out_account_service where acq_enname=#{acqEnname} and out_account_status = 1")
	@ResultMap("cn.eeepay.framework.dao.nposp.OutAccountServiceMapper.BaseResultMap")
	List<OutAccountService> findEntity(@Param("acqEnname")String acqEnname);
	
	@Select("select * from out_account_service where id=#{id} and acq_enname=#{acqEnname}")
	@ResultMap("cn.eeepay.framework.dao.nposp.OutAccountServiceMapper.BaseResultMap")
	OutAccountService findOutAccountServiceByIdAndAcqEnname(Map<String, Object> map);
	
	@Select("select * from out_account_service where id=#{id}")
	@ResultType(OutAccountService.class)
	OutAccountService getById(Integer id);
	
//	@Select("select * from out_account_service where acq_enname=#{acqEnname} and service_type in(4,5) and out_account_status = 1")
//	OutAccountService getByAcqEnname(@Param("acqEnname")String acqEnname);
	
	@Select("select * from out_account_service where acq_enname=#{acqEnname} and service_type in(4,5) and out_account_status = 1")
	@ResultMap("cn.eeepay.framework.dao.nposp.OutAccountServiceMapper.BaseResultMap")
	List<OutAccountService> findOutAccountServiceListByAcqEnname(@Param("acqEnname")String acqEnname);

	@SelectProvider( type=OutAccountServiceMapper.SqlProvider.class,method="findOutAccSerListByAcqNname")
	@ResultMap("cn.eeepay.framework.dao.nposp.OutAccountServiceMapper.BaseResultMap")
	List<OutAccountService> findOutAccSerListByAcqNname(@Param("acqEnname")String acqEnname,@Param("serviceTypes")String serviceTypes);

	@Select("select * from out_account_service where out_account_status = 1")
	@ResultMap("cn.eeepay.framework.dao.nposp.OutAccountServiceMapper.BaseResultMap")
	List<OutAccountService> findAllOutAccountServiceList();
	
	@Select("select DISTINCT oas.* from out_account_service oas "
			+ ",out_account_service_rate oasr where oasr.out_account_service_id=oas.id "
			+ "and ((oasr.agent_rate_type = 5 and oasr.cost_rate_type is null) or (oasr.cost_rate_type = 5 and oasr.agent_rate_type is null))")
	@ResultMap("cn.eeepay.framework.dao.nposp.OutAccountServiceMapper.BaseResultMap")
	List<OutAccountService> findAllOutAccountServiceByType();

	public class SqlProvider {
		public String findOutAccSerListByAcqNname(final Map<String, Object> parameter) {
			final String serviceTypes = (String) parameter.get("serviceTypes");
			final String acqEnname = (String) parameter.get("acqEnname");
			return new SQL() {{
				SELECT(" * ");
				FROM("out_account_service");
				WHERE(" out_account_status = 1 ");
				if (StringUtils.isNotBlank(acqEnname))
					WHERE(" acq_enname  = #{acqEnname} ");

				if (StringUtils.isNotBlank(serviceTypes)) {
					if (serviceTypes.contains(",")) {
						String[] serTypes = serviceTypes.split(",");
						StringBuilder sb = new StringBuilder();
						sb.append(" service_type in(");
						for (String s : serTypes) {
							sb.append(s+",");
						}
						sb.setLength(sb.length()-1);
						sb.append(") ");
						WHERE(sb.toString());
					} else {
						WHERE(" service_type = " + serviceTypes);
					}
				}

			}}.toString();
		}
	}
}
