package com.example.mapper;

import java.util.List;

import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;

import com.example.config.cache.MybatisSecondCache;
import com.example.entity.Employee;
import com.example.mapper.sqlprovider.EmployeeSqlProvider;

/**
 * 员工数据操作
 *
 * @author Qiu Jian
 *
 */
@CacheNamespace(implementation = MybatisSecondCache.class)
public interface EmployeeMapper {
	@Delete({ "delete from employee",
			//
			"where id = #{id,jdbcType=BIGINT}"
			//
	})
	int deleteByPrimaryKey(Long id);

	@DeleteProvider(type = EmployeeSqlProvider.class, method = "deleteByPrimaryKeyList")
	int deleteByPrimaryKeyList(@Param("idList") List<Long> idList);

	@Insert({ "insert into employee (id, username, ",
			//
			"password)",
			//
			"values (#{id,jdbcType=BIGINT}, #{username,jdbcType=VARCHAR}, ",
			//
			"#{password,jdbcType=VARCHAR})"
			//
	})
	@Options(useGeneratedKeys = true, keyProperty = "id")
	int insert(Employee record);

	@InsertProvider(type = EmployeeSqlProvider.class, method = "insertSelective")
	int insertSelective(Employee record);

	@Select({ "select",
			//
			"id, username, password",
			//
			"from employee",
			//
			"where id = #{id,jdbcType=BIGINT}"
			//
	})
	@Results(value = { @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
			@Result(column = "username", property = "username", jdbcType = JdbcType.VARCHAR),
			@Result(column = "password", property = "password", jdbcType = JdbcType.VARCHAR) }, id = "baseResults")
	Employee selectByPrimaryKey(Long id);

	@UpdateProvider(type = EmployeeSqlProvider.class, method = "updateByPrimaryKeySelective")
	int updateByPrimaryKeySelective(Employee record);

	@Update({ "update employee",
			//
			"set username = #{username,jdbcType=VARCHAR},",
			//
			"password = #{password,jdbcType=VARCHAR}",
			//
			"where id = #{id,jdbcType=BIGINT}"
			//
	})
	int updateByPrimaryKey(Employee record);

	@Select({ "select",
			//
			"id, username, password",
			//
			"from employee "
			//
	})
	@ResultMap("baseResults")
	List<Employee> selectAll();

	@InsertProvider(type = EmployeeSqlProvider.class, method = "insertList")
	@SelectKey(before = false, keyProperty = "id", resultType = Long.class, statement = { "SELECT LAST_INSERT_ID()" })
	int insertList(@Param("employeeList") List<Employee> employeeList);
}