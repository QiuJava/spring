package cn.eeepay.framework.dao;

import cn.eeepay.framework.util.WriteReadDataSource;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import cn.eeepay.framework.model.PyIdentification;

@WriteReadDataSource
public interface PyIdentificationDao {

	@Select("select * from py_identification where ident_name=#{name} and id_card=#{card} and account_no=#{accountNo}")
	@ResultType(PyIdentification.class)
	PyIdentification queryByCheckInfo(@Param("name")String name,@Param("card")String card,@Param("accountNo")String accountNo);
	
	int insert(PyIdentification pyIdentification);
}
