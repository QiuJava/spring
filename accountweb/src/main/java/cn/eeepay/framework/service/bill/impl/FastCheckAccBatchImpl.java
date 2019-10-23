package cn.eeepay.framework.service.bill.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.bill.FastCheckAccountBatchMapper;
import cn.eeepay.framework.dao.bill.FastCheckAccountDetailMapper;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.FastCheckAccDetail;
import cn.eeepay.framework.model.bill.FastCheckAccountBatch;
import cn.eeepay.framework.model.bill.ShiroUser;
import cn.eeepay.framework.service.bill.FastCheckAccBatchService;
import cn.eeepay.framework.service.bill.ShiroUserService;
import cn.eeepay.framework.service.nposp.SysCalendarService;


@Service("fastCheckAccBatchService")
@Transactional
public class FastCheckAccBatchImpl implements FastCheckAccBatchService{
	@Resource
	private FastCheckAccountBatchMapper fastCheckAccountBatchMapper;
	
	@Resource
	private FastCheckAccountDetailMapper fastCheckAccountDetailMapper;
	@Resource
	public SysCalendarService sysCalendarService;
	@Resource
	public ShiroUserService shiroUserService;
	
	public FastCheckAccountBatch queryBatchByFileName(String fileName, String acqEnname) {
		return fastCheckAccountBatchMapper.findDuiAccountBatchByFileNameAndAcqEnname(fileName, acqEnname);
	}

	@Override
	public void saveFastCheckAccountBatch(FastCheckAccountBatch fb) {
		// TODO Auto-generated method stub
		fastCheckAccountBatchMapper.insertDuiAccountBatch(fb);
	}

	@Override
	public List<FastCheckAccountBatch> queryDuiAccountList(
			Map<String, String> params, Sort sort,
			Page<FastCheckAccountBatch> page) throws Exception {
		return fastCheckAccountBatchMapper.queryDuiAccountList(params, sort, page);
	}
	@Override
	public int deleteFastAccountBatch(Integer id) {
		FastCheckAccountBatch item = fastCheckAccountBatchMapper.getById(id);
		List<FastCheckAccDetail> list = fastCheckAccountDetailMapper.findByCheckBatchNoAndErrorHandleSatus(item.getCheckBatchNo(), "pendingTreatment");
		if (list != null && list.size() > 0) {
			//包含不是待处理状态的明细数据，不允许删除
			return -2;
		} else {
			int re1 = fastCheckAccountBatchMapper.deleteDuiAccountBatch(id);
			int re2 = fastCheckAccountDetailMapper.deleteByCheckBatchNo(item.getCheckBatchNo());
			return re1 + re2;
		}
	}

	@Override
	public Map<String, Object> getAcqTransAmountSumAndCount(String checkBatchNo) throws Exception {
		return fastCheckAccountBatchMapper.getAcqTransAmountSumAndCount(checkBatchNo);
	}
	@Override
	public Map<String, Object> getPlateTransAmountSumAndCount(String checkBatchNo) throws Exception {
		return fastCheckAccountBatchMapper.getPlateTransAmountSumAndCount(checkBatchNo);
	}
	@Override
	public Map<String, Object> getAcqTransSumAndCount(String checkBatchNo) throws Exception {
		return fastCheckAccountBatchMapper.getAcqTransSumAndCount(checkBatchNo);
	}
	@Override
	public Map<String, Object> getPlateSumAndCount(String checkBatchNo) throws Exception {
		return fastCheckAccountBatchMapper.getPlateSumAndCount(checkBatchNo);
	}
	@Override
	public Map<String, Object> getPlateSumAndCountMe(String checkBatchNo) throws Exception {
		return fastCheckAccountBatchMapper.getPlateSumAndCountMe(checkBatchNo);
	}

	@Override
	public int updateRecordStatus(String checkBatchNo, Integer recordStatus) {
		return fastCheckAccountBatchMapper.updateRecordStatus(checkBatchNo, recordStatus);
	}

	@Override
	public FastCheckAccountBatch getByCheckBatchNo(String checkBatchNo) {
		return fastCheckAccountBatchMapper.getByCheckBatchNo(checkBatchNo);
	}

	@Override
	public void test() throws Exception {
		List<ShiroUser> shiroUsers = shiroUserService.findAllShiroUser();
		for (ShiroUser shiroUser : shiroUsers) {
			System.out.println(shiroUser.getRealName());
		}
			boolean isHoliday = sysCalendarService.isHoliday("2016-10-2");
			System.out.println(isHoliday);
		
	}
	
		
}
