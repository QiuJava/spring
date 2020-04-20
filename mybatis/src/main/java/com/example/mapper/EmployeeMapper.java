package com.example.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.mapping.FetchType;
import org.apache.ibatis.type.JdbcType;

import com.example.dto.ChangePasswordDto;
import com.example.entity.Employee;
import com.example.mapper.provider.EmployeeSqlProvider;
import com.example.qo.EmployeeQo;

/**
 * 员工数据操作
 * 
 * @Options(useGeneratedKeys = true, keyProperty = "id") 插入记录返回Id到对象
 * @SelectKey(before = false, keyProperty = "id", resultType = Long.class,
 *                   statement = { "SELECT LAST_INSERT_ID()" }) 先查询下一个Id封装到对象
 *
 * @author Qiu Jian
 *
 */
public interface EmployeeMapper {

	@Delete({ "DELETE  ", //
			"FROM ", //
			"	employee  ", //
			"WHERE ", //
			"	id = #{id,jdbcType=BIGINT}" })
	int deleteById(Long id);

	@InsertProvider(type = EmployeeSqlProvider.class, method = "insertSelective")
	int insertSelective(Employee record);

	@UpdateProvider(type = EmployeeSqlProvider.class, method = "updateByPrimaryKeySelective")
	int updateByPrimaryKeySelective(Employee record);

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

	@Update({ "UPDATE employee  ", //
			"SET password_errors = #{passwordErrors,jdbcType=INTEGER}, ", //
			"update_time = #{updateTime,jdbcType=TIMESTAMP} ", //
			"WHERE ", //
			"	id = #{id,jdbcType=BIGINT}" })
	int updatePasswordErrorsAndUpdateTimeById(Employee employee);

	@Update({ "UPDATE employee  ", //
			"SET password_errors = #{passwordErrors,jdbcType=INTEGER}, ", //
			"`status` = #{status,jdbcType=VARCHAR}, ", //
			"lock_time = #{lockTime,jdbcType=TIMESTAMP}, ", //
			"update_time = #{updateTime,jdbcType=TIMESTAMP} ", //
			"WHERE ", //
			"	id = #{id,jdbcType=BIGINT}" })
	int updatePasswordErrorsAndStatusAndLockTimeAndUpdateTimeById(Employee employee);

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

	@Update({ "UPDATE `employee`  ", //
			"SET `password` = #{encodePassword,jdbcType=VARCHAR}, ", //
			"update_time = #{updateTime,jdbcType=TIMESTAMP} ", //
			"WHERE ", //
			"	username = #{username,jdbcType=VARCHAR}" })
	int updatePasswordAndUpdateTimeByUsername(ChangePasswordDto changePasswordDto);

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
			"	role_id = #{roleId,jdbcType=BIGINT}" })
	int deleteEmployeeRoleByRoleId(Long roleId);

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

	@Select({ "SELECT ", //
		"	count(*)  ", //
		"FROM ", //
		"	`employee`  ", //
		"WHERE ", //
		"	employee_type = #{employeeType,jdbcType=VARCHAR}" })
	long countByEmployeeType(String employeeType);

}