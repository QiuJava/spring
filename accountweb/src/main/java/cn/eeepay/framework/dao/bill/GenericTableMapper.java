package cn.eeepay.framework.dao.bill;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import cn.eeepay.framework.model.bill.GenericTable;

/**
 * 自定义主键策略
 * @author 沙
 *	自定义序列号
 */
public interface GenericTableMapper {
	
	@Select("select primary_key from generic_table where table_name=#{tableName}")
	@ResultType(String.class)
	public String getCurrKey(@Param("tableName")String tableName);
	
	@Select("select * from generic_table where table_name=#{tableName}")
	@ResultType(GenericTable.class)
	public GenericTable getGenericTableByTableName(@Param("tableName")String tableName);
	
	@Update("update generic_table set primary_key=#{primaryKey} where table_name=#{tableName} ")
	public int updataKey(@Param("tableName")String tableName,@Param("primaryKey")String primaryKey);
	
	@Update("update generic_table set primary_key=#{primaryKey} where id=#{id} ")
	public int updataPrimaryKeyById(@Param("id")Integer id,@Param("primaryKey")String primaryKey);
}
