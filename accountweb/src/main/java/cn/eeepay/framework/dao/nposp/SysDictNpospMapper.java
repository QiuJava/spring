package cn.eeepay.framework.dao.nposp;


import cn.eeepay.framework.model.nposp.SysDict;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 数据字典
 *
 *
 */
public interface SysDictNpospMapper {


	@Update("update sys_dict set sys_value=#{sysDict.sysValue}  where sys_key=#{sysDict.sysKey}")
	int updateSysDictShareAccounting(@Param("sysDict") SysDict sysDict);


	@Select("SELECT * from sys_dict where sys_key='ACCOUNTANT_SHARE_ACCOUNTING' limit 1")
	@ResultType(SysDict.class)
	SysDict findAccountantShareAccounting();

	@Select("SELECT * from sys_dict  WHERE sys_key LIKE 'TRADE_GROUP%' ")
	@ResultType(SysDict.class)
	List<SysDict> findSysDictGroup(@Param("sysKey") String sysKey);
	


	
	
}
