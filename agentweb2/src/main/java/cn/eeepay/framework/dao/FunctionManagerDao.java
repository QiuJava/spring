package cn.eeepay.framework.dao;

import cn.eeepay.framework.model.FunctionManager;
import cn.eeepay.framework.util.ReadOnlyDataSource;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

@ReadOnlyDataSource
public interface FunctionManagerDao {


	@Select(" select * from function_manage where function_number=#{funcNum}")
	@ResultType(FunctionManager.class)
    FunctionManager getFunctionManagerByNum(@Param("funcNum") String funcNum);
}
