package cn.eeepay.framework.service.bill.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.bill.SysDeptMapper;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.SysDept;
import cn.eeepay.framework.service.bill.SysDeptService;
@Service("sysDeptService")
@Transactional
public class SysDeptServiceImpl implements SysDeptService {
	@Resource
	public SysDeptMapper sysDeptMapper;

	@Override
	public int insert(SysDept sysDept) throws Exception {
		return sysDeptMapper.insert(sysDept);
	}

	@Override
	public List<SysDept> findSysDeptList(SysDept sysDept, Sort sort, Page<SysDept> page) throws Exception {
		return sysDeptMapper.findSysDeptList(sysDept, sort, page);
	}

	@Override
	public List<SysDept> findAllSysDeptList() throws Exception {
		return sysDeptMapper.findAllSysDeptList();
	}

	@Override
	public SysDept findSysDeptById(String id) throws Exception {
		return sysDeptMapper.findSysDeptById(id);
	}
}
