package cn.eeepay.framework.dao;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.TerminalInfo;
import cn.eeepay.framework.util.ReadOnlyDataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2019/5/30/030.
 * @author  liuks
 * 机具只读查询
 */
@ReadOnlyDataSource
public interface TerminalInfoReadDao {

	@SelectProvider(type = TerminalInfoReadDao.SqlProvider.class, method = "selectByParamas")
	@ResultType(TerminalInfo.class)
	List<TerminalInfo> selectByParam(@Param("page") Page<TerminalInfo> page, @Param("terminalInfo") TerminalInfo terminalInfo);
	@SelectProvider(type = TerminalInfoReadDao.SqlProvider.class, method = "selectByParamas")
	@ResultType(TerminalInfo.class)
	List<TerminalInfo> exportSelectByParam(@Param("terminalInfo") TerminalInfo terminalInfo);



	class SqlProvider {
		public String selectByParamas(Map<String, Object> param) {
			final TerminalInfo terminalInfo = (TerminalInfo) param.get("terminalInfo");
			SQL sql = new SQL(){{
				SELECT("DISTINCT ti.id,ti.activity_type,ti.sn,ats.ter_no terminal_id,ti.merchant_no,ti.START_TIME,ti.CREATE_TIME,ti.open_status,ti.agent_no ,ato.create_time as downDate,ato1.create_time as receiptDate,mi.merchant_name,ti.type,"
						+ "ai.agent_name,ti.agent_node,ai.agent_type,ai.agent_level,pti.user_code,pti.status,pti.callback_lock ,a_h_t.update_agent_status as update_agent_status ," +
						" ti.activity_type_no as  activity_type_no,a_h_t.activity_type_name as activityTypeNoName,tn.team_name  ");
				FROM("terminal_info ti "
						+ "left JOIN merchant_info mi on ti.merchant_no=mi.merchant_no "
						+ "left JOIN pa_ter_info pti on pti.sn=ti.SN "
						+ "left JOIN agent_info ai on ti.agent_no=ai.agent_no "
						+ "left JOIN acq_terminal_store ats on ats.sn=ti.SN "
						+ "LEFT JOIN activity_hardware_type a_h_t ON ti.activity_type_no = a_h_t.activity_type_no "
						+ "LEFT JOIN hardware_product hp ON ti.type = hp.hp_id "
						+ "LEFT JOIN team_info tn ON hp.org_id=tn.team_id "
						+ "LEFT JOIN agent_terminal_operate ato ON ti.SN =ato.sn AND ato.oper_type='2' AND ato.agent_no = #{terminalInfo.agentName} "
						+ "LEFT JOIN agent_terminal_operate ato1 ON ti.SN =ato1.sn AND ato1.oper_type='1' AND  ato1.agent_no = #{terminalInfo.agentName} "
				);
				if (StringUtils.isNotBlank(terminalInfo.getTeamName()) && (!"全部".equals(terminalInfo.getTeamName()))) {
					WHERE("tn.team_id = #{terminalInfo.teamName}");
				}
				if (StringUtils.isNotBlank(terminalInfo.getMerchantName())) {
					WHERE("(mi.merchant_no = #{terminalInfo.merchantName} or mi.merchant_name = #{terminalInfo.merchantName})");
				}
				if (StringUtils.isNotBlank(terminalInfo.getTeamEntryId())) {
					WHERE(" mi.team_entry_id = #{terminalInfo.teamEntryId} ");
				}
				if (StringUtils.isNotBlank(terminalInfo.getSnStart()) && StringUtils.isBlank(terminalInfo.getSnEnd())) {
					WHERE("ti.sn like concat(#{terminalInfo.snStart},'%')");
				} else if (StringUtils.isBlank(terminalInfo.getSnStart()) && StringUtils.isNotBlank(terminalInfo.getSnEnd())) {
					WHERE("ti.sn like concat(#{terminalInfo.snEnd},'%')");
				} else if (StringUtils.isNotBlank(terminalInfo.getSnStart()) && StringUtils.isNotBlank(terminalInfo.getSnEnd())) {
					WHERE("ti.sn >= #{terminalInfo.snStart} and ti.sn <= #{terminalInfo.snEnd}");
				}
				if (StringUtils.isNotBlank(terminalInfo.getType()) && !StringUtils.equals("-1", terminalInfo.getType())) {
					WHERE("ti.type = #{terminalInfo.type}");
				}
				if (StringUtils.isNotBlank(terminalInfo.getOpenStatus())) {
					if (!StringUtils.equals("-1", terminalInfo.getOpenStatus())) {
						WHERE("ti.open_status =#{terminalInfo.openStatus}");
					} else {
						WHERE("ti.open_status != 0");
					}
				}
				if (StringUtils.isNotBlank(terminalInfo.getUserCode())) {
					WHERE("pti.user_code = #{terminalInfo.userCode}");
				}
				if (StringUtils.isNotBlank(terminalInfo.getRealName())) {
					WHERE("pui.real_name = #{terminalInfo.realName}");
				}

				if (StringUtils.isNotBlank(terminalInfo.getBool())) {
					if (terminalInfo.getBool().contains("%")) {
						WHERE("ai.agent_node LIKE #{terminalInfo.bool}");
					} else {
						WHERE("ai.agent_node = #{terminalInfo.bool}");
					}
				}

				if (StringUtils.isNotBlank(terminalInfo.getActivityType())) {
					WHERE("FIND_IN_SET(#{terminalInfo.activityType},ti.activity_type)");
				}

				if (StringUtils.isNotBlank(terminalInfo.getActivityTypeNo())) {
					WHERE("ti.activity_type_no = #{terminalInfo.activityTypeNo}");
				}
				if (!"-1".equals(terminalInfo.getMerTeamId()) && StringUtils.isNotBlank(terminalInfo.getMerTeamId())) {
					WHERE("mi.team_id=#{terminalInfo.merTeamId}");
				}

				if (StringUtils.isNotBlank(terminalInfo.getStartReceiptdate())) {
					WHERE("atoe.create_time>=#{terminalInfo.startReceiptdate}");
				}
				if (  StringUtils.isNotBlank(terminalInfo.getEndReceiptdate())){
					WHERE("atoe.create_time<=#{terminalInfo.endReceiptdate}");
				}
				if (StringUtils.isNotBlank(terminalInfo.getStartDowndate()) ) {
					WHERE("ato.create_time>=#{terminalInfo.startDowndate}");
				}
				if ( StringUtils.isNotBlank(terminalInfo.getEndDowndate())){
					WHERE("ato.create_time<=#{terminalInfo.endDowndate}");
				}
			}};
			return sql.toString();
		}
	}
}
