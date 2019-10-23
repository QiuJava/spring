package cn.eeepay.framework.service.bill.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.bill.OrgInfoMapper;
import cn.eeepay.framework.model.bill.OrgInfo;
import cn.eeepay.framework.service.bill.OrgInfoService;

@Service("orgInfoService")
@Transactional
public class OrgInfoServiceImpl implements OrgInfoService {

	@Resource
	public OrgInfoMapper  orgInfoMapper;
	
	@Override
	public List<OrgInfo> findOrgInfo() throws Exception {
		return orgInfoMapper.findOrgInfo();
	}

	@Override
	public OrgInfo findOrgNoByName(String orgName) throws Exception {
		return orgInfoMapper.findOrgNoByName(orgName);
	}

}
