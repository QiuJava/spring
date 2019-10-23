package cn.eeepay.framework.dao.nposp;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import cn.eeepay.framework.model.nposp.BusinessProductDefine;

public interface BusinessProductDefineMapper {
	@Select("select * from business_product_define where bp_id=#{id}")
	@ResultType(BusinessProductDefine.class)
	BusinessProductDefine getById(@Param("id")Integer id);
}
