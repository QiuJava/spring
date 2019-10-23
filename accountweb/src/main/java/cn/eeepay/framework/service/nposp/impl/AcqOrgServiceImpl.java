package cn.eeepay.framework.service.nposp.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.nposp.AcqOrgMapper;
import cn.eeepay.framework.model.bill.AcqOrg;
import cn.eeepay.framework.service.nposp.AcqOrgService;

@Service("acqOrgService")
@Transactional("nposp")
public class AcqOrgServiceImpl implements AcqOrgService {
	@Resource
	private AcqOrgMapper acqOrgMapper;

	@Override
	public List<AcqOrg> findAllAcqOrg() {
		return acqOrgMapper.findAll();
	}

	@Override
	public AcqOrg findAcqOrgByUserId(String userId) {
		return acqOrgMapper.findAcqOrgByUserId(userId);
	}

	@Override
	public AcqOrg findAcqOrgByAcqEnname(String acqEnname) {
		return acqOrgMapper.findAcqOrgByAcqEnname(acqEnname);
	}

	@Override
	public List<String> findAcqOrgListByParams(String userName, String mobilephone) {
		return acqOrgMapper.findAcqOrgListByParams(userName, mobilephone);
	}

}
