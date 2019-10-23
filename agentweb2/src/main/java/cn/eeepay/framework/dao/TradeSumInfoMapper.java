package cn.eeepay.framework.dao;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.StringUtils;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.TradeSumInfo;
import cn.eeepay.framework.model.TradeSumInfoQo;
import cn.eeepay.framework.util.WriteReadDataSource;

@WriteReadDataSource
public interface TradeSumInfoMapper {

	int deleteByPrimaryKey(Long id);

	int insert(TradeSumInfo record);

	int insertSelective(TradeSumInfo record);

	TradeSumInfo selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(TradeSumInfo record);

	int updateByPrimaryKey(TradeSumInfo record);

	@SelectProvider(type = TradeSumInfoMapper.SqlProvider.class, method = "page")
	@ResultType(TradeSumInfo.class)
	List<TradeSumInfo> page(Page<TradeSumInfo> page, @Param("qo") TradeSumInfoQo qo);

	@Select("SELECT link_level FROM agent_authorized_link WHERE agent_link =#{agentNo}")
	Integer findByAgentLink(String agentNo);

	public class SqlProvider {
		public String page(Map<String, Object> params) throws ParseException {
			final TradeSumInfoQo qo = (TradeSumInfoQo) params.get("qo");

			SQL sql = new SQL() {
				{
					StringBuilder builder = new StringBuilder();
					builder.append(" create_time,branch,one_level,two_level,three_level,four_level,five_level, ");
					builder.append(" sum( trade_sum ) as trade_sum, ");
					builder.append(" sum( mer_sum ) as mer_sum, ");
					builder.append(" sum( activate_sum ) as activate_sum, ");
					builder.append(" sum( machines_stock ) as machines_stock, ");
					builder.append(" sum( unused_machines ) as unused_machines, ");
					builder.append(" sum( expired_not_activated ) as expired_not_activated ");
					SELECT(builder.toString());
					FROM(" trade_sum_info ");

					if (StringUtils.hasLength(qo.getStartTime())) {
						qo.setStartTime(qo.getStartTime() + " 00:00:00");
						WHERE(" create_time >= #{qo.startTime} ");
					}
					if (StringUtils.hasLength(qo.getEndTime())) {
						qo.setEndTime(qo.getEndTime() + " 23:59:59");
						WHERE(" create_time <= #{qo.endTime} ");
					}
					if(org.apache.commons.lang3.StringUtils.isNotBlank(qo.getTeamId()) && !"-1".equals(qo.getTeamId())){
						WHERE("team_id=#{qo.teamId}");
					}

					if (qo.getAgentNoList() != null && qo.getAgentNoList().size() > 0) {
						StringBuilder sb = new StringBuilder();
						sb.append(" agent_no in ( ");
						for (String agentNo : qo.getAgentNoList()) {
							sb.append(agentNo).append(",");
						}
						sb.deleteCharAt(sb.length() - 1);
						sb.append(") ");
						WHERE(sb.toString());
					}
					GROUP_BY(" agent_no,create_time ");
					ORDER_BY(" create_time DESC ,id ");
				}
			};
			return sql.toString();
		}

		public String sum(Map<String, Object> params) {
			final TradeSumInfoQo qo = (TradeSumInfoQo) params.get("qo");

			SQL sql =  new SQL() {
				{
					SELECT(" sum(trade_sum) ");
					FROM(" trade_sum_info tsi");

					if (StringUtils.hasLength(qo.getStartTime())) {
						qo.setStartTime(qo.getStartTime() + " 00:00:00");
						WHERE(" tsi.create_time >= #{qo.startTime} ");
					}
					if (StringUtils.hasLength(qo.getEndTime())) {
						qo.setEndTime(qo.getEndTime() + " 23:59:59");
						WHERE(" tsi.create_time <= #{qo.endTime} ");
					}
					if(!"-1".equals(qo.getTeamId())){
						WHERE("tsi.team_id=#{qo.teamId}");
					}

					if (qo.getAgentNoList() != null && qo.getAgentNoList().size() > 0) {
						StringBuilder sb = new StringBuilder();
						sb.append(" tsi.agent_no in ( ");
						for (String agentNo : qo.getAgentNoList()) {
							sb.append(agentNo).append(",");
						}
						sb.deleteCharAt(sb.length() - 1);
						sb.append(") ");
						WHERE(sb.toString());
					}
				}
			};
			return sql.toString();
		}

	}

	@Select("SELECT " +
	//
			"	agent_link " +
			//
			"FROM " +
			//
			"	agent_authorized_link t " +
			//
			"WHERE " +
			//
			"	t.agent_authorized = #{agentNo} and t.record_status=1 and t.record_check=1 and link_level <=5 ")
	@ResultType(List.class)
	List<String> findConfigAgentNo(String agentNo);

	@SelectProvider(type = TradeSumInfoMapper.SqlProvider.class, method = "sum")
	@ResultType(String.class)
	String sum(@Param("qo") TradeSumInfoQo qo);

	@SelectProvider(type = TradeSumInfoMapper.SqlProvider.class, method = "page")
	@ResultType(TradeSumInfo.class)
	List<TradeSumInfo> findByQo(@Param("qo") TradeSumInfoQo qo);

	@Select("SELECT DISTINCT " +
	//
			" " +
			//
			"IF ( " +
			//
			"	( " +
			//
			"		SELECT " +
			//
			"			count(*) " +
			//
			"		FROM " +
			//
			"			agent_authorized_link " +
			//
			"		WHERE " +
			//
			"			agent_link = a_a_l_w.agent_authorized " +
			//
			"		AND record_status = 1 " +
			//
			"		AND record_check = 1 " +
			//
			"		AND link_level <= 5 " +
			//
			"	) > 0, " +
			//
			"	NULL, " +
			//
			"	a_a_l_w.agent_authorized " +
			//
			") AS agent_no " +
			//
			"FROM " +
			//
			"	agent_authorized_link a_a_l_w " +
			//
			"WHERE " +
			//
			"	a_a_l_w.record_status = 1 " +
			//
			"AND a_a_l_w.record_check = 1 " +
			//
			"AND a_a_l_w.link_level <= 5")
	@ResultType(List.class)
	List<String> findAllTopAgentNo();

	@Select("SELECT " +
	//
			"	agent_link " +
			//
			"FROM " +
			//
			"	agent_authorized_link t " +
			//
			"WHERE " +
			//
			"	t.agent_authorized = #{agentNo} and t.record_status=1 and t.record_check=1 and t.is_look=1 and link_level <=5")
	List<String> findLookAgentNo(String agentNo);
}