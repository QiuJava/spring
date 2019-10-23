package cn.eeepay.framework.daoSuperBank;

import cn.eeepay.framework.model.MposProductType;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MposProductTypeDao {


    @Select("SELECT mpt.* FROM mpos_product_type mpt ")
    @ResultType(MposProductType.class)
    List<MposProductType> getMposProductTypeListAll();
}
