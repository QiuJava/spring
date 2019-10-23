package cn.eeepay.framework.dao.nposp;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.model.nposp.ScanCodeTrans;

public interface ScanCodeTransMapper {
	@Select("select * from scan_code_trans where trade_no = #{orderNo} and acq_enname = #{acqEnname} ")
	@ResultMap("cn.eeepay.framework.dao.nposp.ScanCodeTransMapper.BaseResultMap")
	ScanCodeTrans findDuiAccountDetailByTransInfo(@Param("orderNo")String orderNo,@Param("acqEnname")String acqEnname);
	
	@SelectProvider( type=SqlProvider.class,method="findCheckData")
	@ResultMap("cn.eeepay.framework.dao.nposp.ScanCodeTransMapper.BaseResultMap")
	List<ScanCodeTrans> findCheckData(@Param("acqEnname")String acqEnname, @Param("transTimeBegin")String transTimeBegin, @Param("transTimeEnd")String transTimeEnd);
	
	public class SqlProvider{
			
			public String findCheckData(final Map<String, Object> parameter) {
				final String acqEnname = (String) parameter.get("acqEnname");
				final String transTimeBegin = (String) parameter.get("transTimeBegin");
				final String transTimeEnd = (String) parameter.get("transTimeEnd");
				return new SQL(){{
					SELECT(" * ");
					FROM("scan_code_trans ");
					WHERE(" trade_state = 'SUCCESS' ");
					if (StringUtils.isNotBlank(acqEnname))
						WHERE(" acq_enname  = #{acqEnname} ");
					if (StringUtils.isNotBlank(transTimeBegin) && StringUtils.isNotBlank(transTimeEnd))
						WHERE(" time_start between #{transTimeBegin} and #{transTimeEnd} ");
				}}.toString();
			}
	}
}
