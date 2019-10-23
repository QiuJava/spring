package cn.eeepay.framework.dao.bill;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import cn.eeepay.framework.model.bill.OrgInfo;

/*
 * 机构表相关
 */
public interface OrgInfoMapper {
	
	@Select("select * from org_info")
	@ResultMap("cn.eeepay.framework.dao.bill.OrgInfoMapper.BaseResultMap")
	List<OrgInfo> findOrgInfo();
	
	@Select("select * from org_info where org_name =#{orgName}")
	@ResultMap("cn.eeepay.framework.dao.bill.OrgInfoMapper.BaseResultMap")
	OrgInfo findOrgNoByName(@Param("orgName")String orgName);
}
