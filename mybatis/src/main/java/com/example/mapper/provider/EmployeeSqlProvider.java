package com.example.mapper.provider;

import com.example.qo.EmployeeQo;
import com.example.util.StrUtil;

import org.apache.ibatis.jdbc.SQL;

/**
 * 员工Sql供应
 * 
 * @author Qiu Jian
 *
 */
public class EmployeeSqlProvider {

	public String selectByListByQo(EmployeeQo employeeQo) {
		SQL sql = new SQL();
		sql.SELECT(new String[] { "	id ", //
				"	username ", //
				"	email ", //
				"	`status` ", //
				"	employee_type "});
		sql.FROM("employee");

		if (StrUtil.hasText(employeeQo.getEmployeeName())) {
			sql.WHERE("username = #{username,jdbcType=VARCHAR}");
		}

		if (employeeQo.getEmployeeType() != null) {
			sql.WHERE("employee_type = #{employeeType,jdbcType=INTEGER}");
		}

		return sql.toString();
	}
}