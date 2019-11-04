package com.example.mapper;

import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;

import com.example.cache.MybatisSecondCache;
import com.example.entity.Employee;
import com.example.mapper.provider.EmployeeSqlProvider;

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

	@InsertProvider(type = EmployeeSqlProvider.class, method = "insertSelective")
	int insertSelective(Employee record);

	@Select({ "select", " * ", "where id = #{id,jdbcType=BIGINT}" })
	Employee selectByPrimaryKey(Long id);

	@UpdateProvider(type = EmployeeSqlProvider.class, method = "updateByPrimaryKeySelective")
	int updateByPrimaryKeySelective(Employee record);

	@Select({ "SELECT ", "	count( * )  ", "FROM ", "	`employee`  ", "WHERE ", "	super_admin = 1" })
	int countBySuperAdmin();

	@Select({ "SELECT ", "	*  ", "FROM ", "	`employee`  ", "WHERE ", "	username = #{username}" })
	Employee selectByUsername(String username);

}