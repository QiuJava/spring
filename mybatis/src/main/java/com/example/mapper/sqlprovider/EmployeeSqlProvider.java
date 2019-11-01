package com.example.mapper.sqlprovider;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.jdbc.SQL;

import com.example.entity.Employee;
import com.example.util.SqlUtil;

/**
 * 员工Sql提供
 *
 * @author Qiu Jian
 *
 */
public class EmployeeSqlProvider {

	public String insertSelective(Employee record) {
		SQL sql = new SQL();
		sql.INSERT_INTO("employee");

		if (record.getId() != null) {
			sql.VALUES("id", "#{id,jdbcType=BIGINT}");
		}

		if (record.getUsername() != null) {
			sql.VALUES("username", "#{username,jdbcType=VARCHAR}");
		}

		if (record.getPassword() != null) {
			sql.VALUES("password", "#{password,jdbcType=VARCHAR}");
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

		sql.WHERE("id = #{id,jdbcType=BIGINT}");

		return sql.toString();
	}

	@SuppressWarnings("unchecked")
	public String deleteByPrimaryKeyList(Map<String, Object> params) {
		List<Long> idList = (List<Long>) params.get("idList");
		SQL sql = new SQL();
		sql.DELETE_FROM("employee");
		sql.WHERE(SqlUtil.inJoint("id", "idList", idList));
		return sql.toString();
	}

	@SuppressWarnings("unchecked")
	public String insertList(Map<String, Object> params) {
		List<Employee> employeeList = (List<Employee>) params.get("employeeList");
		SQL sql = new SQL();
		sql.INSERT_INTO("employee");
		String[] columns = new String[] { "username", "password" };
		sql.INTO_COLUMNS(columns);
		String[] properties = new String[] { "username", "password" };
		return SqlUtil.valuesJoint(sql.toString(),"employeeList", employeeList, properties);
	}

}