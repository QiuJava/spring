package cn.eeepay.framework.dao.bill;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.OptLogs;

public interface OptLogsMapper {

	@Insert("insert into opt_logs(" + "out_bill_id,"// 出账单ID
			+ "check_batch_no,"// 对账批次号
			+ "log_msg,"// 操作内容
			+ "log_type,"// 日志类型
			+ "operator,"// 操作人
			+ "operate_time)"// 操作时间
			+ "values(" + "#{log.outBillId},"// 出账单ID
			+ "#{log.checkBatchNo},"// 对账批次号
			+ "#{log.operateContent},"// 操作内容
			+ "#{log.logType},"// 日志类型
			+ "#{log.operator},"// 操作人
			+ "now())")
	int insertOptLogs(@Param("log") OptLogs log);

	@SelectProvider(type = SqlProvider.class, method = "findDuiAccountOptLogs")
	@ResultMap("cn.eeepay.framework.dao.bill.OptLogsMapper.BaseResultMap")
	List<OptLogs> findDuiAccountOptLogs(@Param("optLogs") OptLogs operateLog,
			@Param("params") Map<String, String> params, @Param("sort") Sort sort, Page<OptLogs> page);

	public class SqlProvider {
		public String findDuiAccountOptLogs(final Map<String, Object> parameter) {

			final OptLogs optLogs = (OptLogs) parameter.get("optLogs");
			@SuppressWarnings("unchecked")
			final Map<String, String> params = (Map<String, String>) parameter.get("params");
			String beginDate1 = params.get("beginDate");
			String endDate1 = params.get("endDate");
			if (StringUtils.isNotBlank(beginDate1)) {
				beginDate1 += " 00:00:00";
			}
			if (StringUtils.isNotBlank(endDate1)) {
				endDate1 += " 23:59:59";
			}
			final String beginDate = beginDate1;
			final String endDate = endDate1;
			params.put("beginDate", beginDate);
			params.put("endDate", endDate);
			String sql = new SQL() {
				{
					SELECT(" * ");
					FROM(" opt_logs ");
					if (!StringUtils.isBlank(optLogs.getLogType()))
						WHERE("  log_type = #{optLogs.logType} ");
					if (!StringUtils.isBlank(optLogs.getOperator()))
						WHERE("  operator like  \"%\"#{optLogs.operator}\"%\" ");

					if ("duiAccountLog".equals(optLogs.getLogType())) {// 对账日志
						if (!StringUtils.isBlank(optLogs.getCheckBatchNo()))
							WHERE(" check_batch_no like \"%\"#{optLogs.checkBatchNo}\"%\" ");
					} else if ("confirmAccountLog".equals(optLogs.getLogType())) {// 确认出账日志
						if (optLogs.getOutBillId() != null)
							WHERE(" out_bill_id = #{optLogs.outBillId}");
					}

					if (beginDate != null && StringUtils.isNotBlank(beginDate))
						WHERE(" operate_time >= #{params.beginDate} ");
					if (endDate != null && StringUtils.isNotBlank(endDate))
						WHERE(" operate_time <= #{params.endDate} ");

					ORDER_BY(" operate_time desc ");
				}
			}.toString();
			return sql;

		}
	}
}
