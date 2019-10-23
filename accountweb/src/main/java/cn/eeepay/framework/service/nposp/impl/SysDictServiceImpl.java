package cn.eeepay.framework.service.nposp.impl;


import cn.eeepay.framework.dao.nposp.SysDictNpospMapper;
import cn.eeepay.framework.model.nposp.SysDict;
import cn.eeepay.framework.service.nposp.SysDictNpospService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.annotation.Resource;

@Service("sysDictServiceNposp")
@Transactional
public class SysDictServiceImpl implements SysDictNpospService {
	@Resource
	public SysDictNpospMapper sysDictMapperNposp;


	@Override
	public SysDict findAccountantShareAccounting() {
		return sysDictMapperNposp.findAccountantShareAccounting();
	}

	@Override
	public int updateSysDictShareAccounting(SysDict sysDict) {
		return sysDictMapperNposp.updateSysDictShareAccounting(sysDict);
	}

	@Override
	public List<SysDict> findSysDictGroup(String sysKey) throws Exception {
		return sysDictMapperNposp.findSysDictGroup(sysKey);
	}
}
