package cn.eeepay.framework.dao;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import cn.eeepay.framework.util.WriteReadDataSource;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.jdbc.SQL;

import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.GatherCode;

@WriteReadDataSource
public interface GatherCodeDao {

	@InsertProvider(type=SqlProvider.class, method="insertBatch")
	int insertBatch(@Param("list")List<GatherCode> list);
	
	public class SqlProvider{
		
		
		public String insertBatch(Map<String, Object> param){
			List<GatherCode> list = (List<GatherCode>) param.get("list");
			StringBuilder sb = new StringBuilder("insert into gather_code(SN,gather_code,status,material_type,create_time,gather_name,device_sn,merchant_no) values");
			MessageFormat message = new MessageFormat("(nextval('''gather_code_seq'''),#'{'list[{0}].gatherCode},2,#'{'list[{0}].materialType},now(),#'{'list[{0}].gatherName},#'{'list[{0}].deviceSn},#'{'list[{0}].merchantNo}),");
			for(int i=0; i<list.size();i++){
				sb.append(message.format(new Integer[]{i}));
			}
			sb.setLength(sb.length()-1);
			return sb.toString();
		}
		
	
	}


	
}
