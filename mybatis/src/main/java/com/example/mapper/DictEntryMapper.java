package com.example.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;

import com.example.entity.DictEntry;

/**
 * 字典条目数据操作
 * 
 * @author Qiu Jian
 *
 */
public interface DictEntryMapper {

	@Select({ "SELECT ", //
			"	name, ", //
			"	value  ", //
			"FROM ", //
			"	dict_entry  ", //
			"WHERE ", //
			"	dict_key = #{dictKey}" })
	@Results(value = { @Result(column = "name", property = "name", jdbcType = JdbcType.VARCHAR) ,
			@Result(column = "value", property = "value", jdbcType = JdbcType.VARCHAR) })
	List<DictEntry> selectByDictKey(String dictKey);

}
