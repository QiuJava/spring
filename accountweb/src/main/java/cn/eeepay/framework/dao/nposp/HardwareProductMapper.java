package cn.eeepay.framework.dao.nposp;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import cn.eeepay.framework.model.nposp.HardwareProduct;

public interface HardwareProductMapper {
	@Select("select * from hardware_product where hp_id=#{id}")
	HardwareProduct getById(@Param("id")Long id);
}
