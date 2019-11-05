package com.example.mapper;

import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;

import com.example.cache.MybatisSecondCache;
import com.example.entity.Employee;
import com.example.mapper.provider.EmployeeSqlProvider;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.type.JdbcType;

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
@CacheNamespace(implementation = MybatisSecondCache.class)
public interface EmployeeMapper {

	@Delete({ "delete from employee", "where id = #{id,jdbcType=BIGINT}" })
	int deleteByPrimaryKey(Long id);

	@Insert({ "insert into employee (id, username, ", "password, email, ", "nickname, password_errors, ",
			"status, super_admin, ", "employee_type, employee_number, ", "intro, create_time, ",
			"update_time, lock_time)", "values (#{id,jdbcType=BIGINT}, #{username,jdbcType=VARCHAR}, ",
			"#{password,jdbcType=VARCHAR}, #{email,jdbcType=VARCHAR}, ",
			"#{nickname,jdbcType=VARCHAR}, #{passwordErrors,jdbcType=INTEGER}, ",
			"#{status,jdbcType=INTEGER}, #{superAdmin,jdbcType=INTEGER}, ",
			"#{employeeType,jdbcType=INTEGER}, #{employeeNumber,jdbcType=VARCHAR}, ",
			"#{intro,jdbcType=VARCHAR}, #{createTime,jdbcType=TIMESTAMP}, ",
			"#{updateTime,jdbcType=TIMESTAMP}, #{lockTime,jdbcType=TIMESTAMP})" })
	int insert(Employee record);

	@InsertProvider(type = EmployeeSqlProvider.class, method = "insertSelective")
	int insertSelective(Employee record);

	@Select({ "select", "id, username, password, email, nickname, password_errors, status, super_admin, ",
			"employee_type, employee_number, intro, create_time, update_time, lock_time", "from employee",
			"where id = #{id,jdbcType=BIGINT}" })
	@Results({ @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
			@Result(column = "username", property = "username", jdbcType = JdbcType.VARCHAR),
			@Result(column = "password", property = "password", jdbcType = JdbcType.VARCHAR),
			@Result(column = "email", property = "email", jdbcType = JdbcType.VARCHAR),
			@Result(column = "nickname", property = "nickname", jdbcType = JdbcType.VARCHAR),
			@Result(column = "password_errors", property = "passwordErrors", jdbcType = JdbcType.INTEGER),
			@Result(column = "status", property = "status", jdbcType = JdbcType.INTEGER),
			@Result(column = "super_admin", property = "superAdmin", jdbcType = JdbcType.INTEGER),
			@Result(column = "employee_type", property = "employeeType", jdbcType = JdbcType.INTEGER),
			@Result(column = "employee_number", property = "employeeNumber", jdbcType = JdbcType.VARCHAR),
			@Result(column = "intro", property = "intro", jdbcType = JdbcType.VARCHAR),
			@Result(column = "create_time", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
			@Result(column = "update_time", property = "updateTime", jdbcType = JdbcType.TIMESTAMP),
			@Result(column = "lock_time", property = "lockTime", jdbcType = JdbcType.TIMESTAMP) })
	Employee selectByPrimaryKey(Long id);

	@UpdateProvider(type = EmployeeSqlProvider.class, method = "updateByPrimaryKeySelective")
	int updateByPrimaryKeySelective(Employee record);

	@Update({ "update employee", "set username = #{username,jdbcType=VARCHAR},",
			"password = #{password,jdbcType=VARCHAR},", "email = #{email,jdbcType=VARCHAR},",
			"nickname = #{nickname,jdbcType=VARCHAR},", "password_errors = #{passwordErrors,jdbcType=INTEGER},",
			"status = #{status,jdbcType=INTEGER},", "super_admin = #{superAdmin,jdbcType=INTEGER},",
			"employee_type = #{employeeType,jdbcType=INTEGER},",
			"employee_number = #{employeeNumber,jdbcType=VARCHAR},", "intro = #{intro,jdbcType=VARCHAR},",
			"create_time = #{createTime,jdbcType=TIMESTAMP},", "update_time = #{updateTime,jdbcType=TIMESTAMP},",
			"lock_time = #{lockTime,jdbcType=TIMESTAMP}", "where id = #{id,jdbcType=BIGINT}" })
	int updateByPrimaryKey(Employee record);

	@Select({ "SELECT ", "	count( * )  ", "FROM ", "	`employee`  ", "WHERE ", "	super_admin = 1" })
	int countBySuperAdmin();

	@Select({ "select", "id, username, password, email, nickname, password_errors, status, super_admin, ",
			"employee_type, employee_number, intro, create_time, update_time, lock_time", "from employee",
			"where username = #{username,jdbcType=VARCHAR}" })
	@Results({ @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
			@Result(column = "username", property = "username", jdbcType = JdbcType.VARCHAR),
			@Result(column = "password", property = "password", jdbcType = JdbcType.VARCHAR),
			@Result(column = "email", property = "email", jdbcType = JdbcType.VARCHAR),
			@Result(column = "nickname", property = "nickname", jdbcType = JdbcType.VARCHAR),
			@Result(column = "password_errors", property = "passwordErrors", jdbcType = JdbcType.INTEGER),
			@Result(column = "status", property = "status", jdbcType = JdbcType.INTEGER),
			@Result(column = "super_admin", property = "superAdmin", jdbcType = JdbcType.INTEGER),
			@Result(column = "employee_type", property = "employeeType", jdbcType = JdbcType.INTEGER),
			@Result(column = "employee_number", property = "employeeNumber", jdbcType = JdbcType.VARCHAR),
			@Result(column = "intro", property = "intro", jdbcType = JdbcType.VARCHAR),
			@Result(column = "create_time", property = "createTime", jdbcType = JdbcType.TIMESTAMP),
			@Result(column = "update_time", property = "updateTime", jdbcType = JdbcType.TIMESTAMP),
			@Result(column = "lock_time", property = "lockTime", jdbcType = JdbcType.TIMESTAMP) })
	Employee selectByUsername(String username);

	@Update({ "UPDATE employee  ", "SET password_errors = #{passwordErrors,jdbcType=INTEGER}  ", "WHERE ",
			"	id = #{id,jdbcType=BIGINT}" })
	int updatePasswordErrorsByPrimaryKey(Employee employee);

	@Update({ "UPDATE employee  ", "SET password_errors = #{passwordErrors,jdbcType=INTEGER}, ",
			"`status` = #{status,jdbcType=INTEGER}, ", "lock_time = #{lockTime,jdbcType=TIMESTAMP}  ", "WHERE ",
			"	id = #{id,jdbcType=BIGINT}" })
	int updatePasswordErrorsAndStatusAndLockTimeByPrimaryKey(Employee employee);

}