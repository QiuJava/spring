package cn.eeepay.framework.dao;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.util.WriteReadDataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.TerminalApply;

@WriteReadDataSource
public interface TerminalApplyDao {

	@SelectProvider(type=SqlProvider.class,method="queryAllInfo")
	@ResultType(TerminalApply.class)
	List<TerminalApply> queryAllInfo(Page<TerminalApply> page,@Param("terminalApply")TerminalApply terminalApply,@Param("loginAgentNode")String loginAgentNode);
	
	@Select("SELECT ta.*, CONCAT(type_name,version_nu) AS hp_name,\n" +
			"\t(\n" +
			"\t\tSELECT COUNT(1) FROM terminal_info ti \n" +
			"\t\tWHERE ti.merchant_no=ta.merchant_no \n" +
			"\t\tAND ti.open_status=2 \n" +
			"\t\tAND ti.collection_code IS NULL\n" +
			"\t) is_bind \n" +
			"FROM terminal_apply ta \n" +
			"LEFT JOIN hardware_product hp ON hp.hp_id=ta.product_type "
			+ "where ta.id=#{id}")
	@ResultType(TerminalApply.class)
	TerminalApply queryInfoDetail(@Param("id")String id);
	
	@Update("update terminal_apply set status=#{status},remark=#{remark},sn=#{sn} where id=#{id}")
	int updateInfo( @Param("id") String id, @Param("status") String status, @Param("remark") String remark,@Param("sn") String sn);
	
	 public class SqlProvider{
	    	public String queryAllInfo(Map<String,Object> param){
	    		final TerminalApply terminalApply=(TerminalApply)param.get("terminalApply");
	    		return new SQL(){{
	    			SELECT("ta.*,mis.merchant_name,ais.agent_name,ais.agent_no,ais.one_level_id," +
							"(SELECT COUNT(1) FROM terminal_info ti " +
							"WHERE ti.merchant_no=ta.merchant_no " +
							"AND ti.open_status=2 and ti.collection_code is null) is_bind");
	    			FROM("terminal_apply ta "
	    					+ "LEFT JOIN merchant_info mis on mis.merchant_no=ta.merchant_no "
	    					+ "LEFT JOIN agent_info ais on ais.agent_no=mis.agent_no");
	    			WHERE("ais.agent_node LIKE (CONCAT(#{loginAgentNode}, '%'))");
	    			if(StringUtils.isNotBlank(terminalApply.getMobilephone())){
	    				WHERE("ta.mobilephone = #{terminalApply.mobilephone}");
	    			}
	    			if(StringUtils.isNotBlank(terminalApply.getAgentName())){
	    				terminalApply.setAgentName("%" + terminalApply.getAgentName()+"%");
	    				WHERE("ais.agent_name like #{terminalApply.agentName}");
	    			}
	    			if(StringUtils.isNotBlank(terminalApply.getStatus())){
	    				WHERE(" ta.status=#{terminalApply.status}");
	    			}
	    			if(StringUtils.isNotBlank(terminalApply.getMerAccount())){
	    				WHERE(" mis.agent_no=#{terminalApply.mer_account}");
	    			}
	    			if(StringUtils.isNotBlank(terminalApply.getsTime())){
						WHERE(" ta.create_time>=#{terminalApply.sTime}");
					}
					if(StringUtils.isNotBlank(terminalApply.geteTime())){
						WHERE(" ta.create_time<=#{terminalApply.eTime}");
					}
					if (StringUtils.isNotBlank(terminalApply.getSn())){
						WHERE(" ta.sn=#{terminalApply.sn}");
					}
					if (StringUtils.isNotBlank(terminalApply.getHasSn())){
						if (StringUtils.equals("0", terminalApply.getHasSn())){
							WHERE(" ifnull(ta.sn,'') = ''");
						}else{
							WHERE(" ifnull(ta.sn,'') != '' ");
						}
					}
					if (StringUtils.isNotBlank(terminalApply.getIsBindParam())){
						StringBuilder sb = new StringBuilder();
						if (StringUtils.equals("0", terminalApply.getIsBindParam())){
							sb.append(" not ");
						}
						sb.append("EXISTS( " +
								"SELECT 1 FROM terminal_info ti " +
								"WHERE ti.merchant_no=ta.merchant_no " +
								"AND ti.open_status=2 " +
								"AND ti.collection_code is null" +
								")");
						WHERE(sb.toString());
					}
					ORDER_BY("ta.create_time DESC");
	    		}}.toString();
	    	}
	    	
	    }
}
