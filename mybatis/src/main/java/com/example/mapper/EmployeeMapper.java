package com.example.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.mapping.FetchType;
import org.apache.ibatis.type.JdbcType;

import com.example.entity.Employee;
import com.example.mapper.provider.EmployeeSqlProvider;
import com.example.qo.EmployeeQo;

/**
 * 员工数据操作
 * 
 * @Options(useGeneratedKeys = true, keyProperty = "id") 插入记录
 *
 * @author Qiu Jian
 *
 */
public interface EmployeeMapper {

	int deleteByPrimaryKey(Integer id);

	int insert(Employee record);

	int insertSelective(Employee record);

	Employee selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(Employee record);

	int updateByPrimaryKey(Employee record);

	@Delete({ "DELETE  ", //
			"FROM ", //
			"	employee  ", //
			"WHERE ", //
			"	id = #{id,jdbcType=BIGINT}" })
	int deleteById(Long id);

	@Select({ "SELECT ", //
			"	id, ", //
			"	username, ", //
			"	`PASSWORD`, ", //
			"	email, ", //
			"	password_errors, ", //
			"	`STATUS`, ", //
			"	employee_type, ", //
			"	lock_time  ", //
			"FROM ", //
			"	employee  ", //
			"WHERE ", //
			"	username = #{username,jdbcType=VARCHAR}" })
	@Results({ @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
			@Result(column = "username", property = "username", jdbcType = JdbcType.VARCHAR),
			@Result(column = "password", property = "password", jdbcType = JdbcType.VARCHAR),
			@Result(column = "email", property = "email", jdbcType = JdbcType.VARCHAR),
			@Result(column = "password_errors", property = "passwordErrors", jdbcType = JdbcType.INTEGER),
			@Result(column = "status", property = "status", jdbcType = JdbcType.VARCHAR),
			@Result(column = "employee_type", property = "employeeType", jdbcType = JdbcType.VARCHAR),
			@Result(column = "lock_time", property = "lockTime", jdbcType = JdbcType.TIMESTAMP),
			@Result(column = "id", property = "authorities", many = @Many(select = "com.example.mapper.PermissionMapper.selectByEmployeeId", fetchType = FetchType.LAZY)) })
	Employee selectByUsername(String username);

	@Select({ "SELECT ", //
			"	id, ", //
			"	password_errors, ", //
			"	`STATUS`  ", //
			"FROM ", //
			"	employee  ", //
			"WHERE ", //
			"	username = #{username,jdbcType=VARCHAR}" })
	@Results({ @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
			@Result(column = "password_errors", property = "passwordErrors", jdbcType = JdbcType.INTEGER),
			@Result(column = "status", property = "status", jdbcType = JdbcType.VARCHAR) })
	Employee selectPasswordErrorsAndIdAndStatusByUsername(String username);

	@Select({ "SELECT ", //
			"	count( * )  ", //
			"FROM ", //
			"	`employee`  ", //
			"WHERE ", //
			"	username = #{username,jdbcType=VARCHAR}" })
	int countByUsername(String username);

	@Update({ "UPDATE employee  ", //
			"SET password_errors = 0 ", //
			"WHERE ", //
			"	`status` = 'NORMAL_STATUS' AND password_errors > 0 " })
	int updateAllPasswordErrors();

	@Select({ "SELECT ", //
			"	count( * )  ", //
			"FROM ", //
			"	`employee_role`  ", //
			"WHERE ", //
			"	role_id = #{roleId,jdbcType=BIGINT}" })
	int countEmployeeRoleByRoleId(Long roleId);

	@Delete({ "DELETE ", //
			"FROM ", //
			"	`employee_role`  ", //
			"WHERE ", //
			"	role_id = #{roleId,jdbcType=INTEGER}" })
	int deleteEmployeeRoleByRoleId(Integer roleId);

	@SelectProvider(type = EmployeeSqlProvider.class, method = "selectByListByQo")
	@Results({ @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
			@Result(column = "username", property = "username", jdbcType = JdbcType.VARCHAR),
			@Result(column = "email", property = "email", jdbcType = JdbcType.VARCHAR),
			@Result(column = "status", property = "status", jdbcType = JdbcType.VARCHAR),
			@Result(column = "employee_type", property = "employeeType", jdbcType = JdbcType.VARCHAR)})
	List<Employee> selectByListByQo(EmployeeQo employeeQo);

	@Select({ "SELECT ", //
		"	count( * )  ", //
		"FROM ", //
		"	`employee`  ", //
		"WHERE ", //
		"	email = #{email,jdbcType=VARCHAR}" })
	int countByEmail(String email);

	@Select({ "SELECT ", //
		"	email  ", //
		"FROM ", //
		"	`employee`  ", //
		"WHERE ", //
		"	id = #{id,jdbcType=BIGINT}" })
	String selectEmailById(Long id);

	@Select({ "SELECT ", //
		"	username  ", //
		"FROM ", //
		"	`employee`  ", //
		"WHERE ", //
		"	id = #{id,jdbcType=BIGINT}" })
	String selectUsernameById(Long id);
	
	long countByEmployeeType(String employeeType);

	Employee getByEmployeeName(String employeeName);

	List<Employee> listByQo(EmployeeQo employeeQo);

}