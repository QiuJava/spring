package cn.eeepay.framework.service.bill;

import java.util.List;

import cn.eeepay.framework.model.bill.OrgInfo;



public interface OrgInfoService {
	List<OrgInfo> findOrgInfo() throws Exception;
	OrgInfo findOrgNoByName(String orgName) throws Exception;
}
