package com.example.mapper.provider;

import com.example.entity.Employee;
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

	public String insertSelective(Employee record) {
		SQL sql = new SQL();
		sql.INSERT_INTO("employee");
		if (record.getUsername() != null) {
			sql.VALUES("username", "#{username,jdbcType=VARCHAR}");
		}
		if (record.getPassword() != null) {
			sql.VALUES("password", "#{password,jdbcType=VARCHAR}");
		}
		if (record.getEmail() != null) {
			sql.VALUES("email", "#{email,jdbcType=VARCHAR}");
		}
		if (record.getPasswordErrors() != null) {
			sql.VALUES("password_errors", "#{passwordErrors,jdbcType=INTEGER}");
		}
		if (record.getStatus() != null) {
			sql.VALUES("status", "#{status,jdbcType=VARCHAR}");
		}
		if (record.getEmployeeType() != null) {
			sql.VALUES("employee_type", "#{employeeType,jdbcType=VARCHAR}");
		}
		if (record.getCreateTime() != null) {
			sql.VALUES("create_time", "#{createTime,jdbcType=TIMESTAMP}");
		}
		if (record.getUpdateTime() != null) {
			sql.VALUES("update_time", "#{updateTime,jdbcType=TIMESTAMP}");
		}

		return sql.toString();
	}

	public String updateByPrimaryKeySelective(Employee record) {
		SQL sql = new SQL();
		sql.UPDATE("employee");

		if (record.getUsername() != null) {
			sql.SET("username = #{username,jdbcType=VARCHAR}");
		}

		if (record.getPassword() != null) {
			sql.SET("password = #{password,jdbcType=VARCHAR}");
		}

		if (record.getEmail() != null) {
			sql.SET("email = #{email,jdbcType=VARCHAR}");
		}

		if (record.getPasswordErrors() != null) {
			sql.SET("password_errors = #{passwordErrors,jdbcType=INTEGER}");
		}

		if (record.getStatus() != null) {
			sql.SET("status = #{status,jdbcType=VARCHAR}");
		}

		if (record.getEmployeeType() != null) {
			sql.SET("employee_type = #{employeeType,jdbcType=VARCHAR}");
		}

		if (record.getCreateTime() != null) {
			sql.SET("create_time = #{createTime,jdbcType=TIMESTAMP}");
		}

		if (record.getUpdateTime() != null) {
			sql.SET("update_time = #{updateTime,jdbcType=TIMESTAMP}");
		}

		if (record.getLockTime() != null) {
			sql.SET("lock_time = #{lockTime,jdbcType=TIMESTAMP}");
		}

		sql.WHERE("id = #{id,jdbcType=BIGINT}");

		return sql.toString();
	}

	public String selectByListByQo(EmployeeQo employeeQo) {
		SQL sql = new SQL();
		sql.SELECT(new String[] { "	id ", //
				"	username ", //
				"	email ", //
				"	`status` ", //
				"	employee_type "});
		sql.FROM("employee");

		if (StrUtil.hasText(employeeQo.getUsername())) {
			sql.WHERE("username = #{username,jdbcType=VARCHAR}");
		}

		if (employeeQo.getEmployeeType() != null) {
			sql.WHERE("employee_type = #{employeeType,jdbcType=INTEGER}");
		}

		return sql.toString();
	}
}