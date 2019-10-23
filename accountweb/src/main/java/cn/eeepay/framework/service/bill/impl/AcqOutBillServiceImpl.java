package cn.eeepay.framework.service.bill.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.bill.AcqOutBillMapper;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.AcqOutBill;
import cn.eeepay.framework.service.bill.AcqOutBillService;



@Service("acqOutBillService")
@Transactional
public class AcqOutBillServiceImpl implements AcqOutBillService{
	
	@Resource
	public AcqOutBillMapper acqOutBillMapper;

	@Override
	public int insertAcqOutBill(AcqOutBill acqOutBill) throws Exception {
		return acqOutBillMapper.insertAcqOutBill(acqOutBill);
	}

	@Override
	public int updateAcqOutBillById(AcqOutBill acqOutBill) throws Exception {
		return acqOutBillMapper.updateAcqOutBillById(acqOutBill);
	}

	@Override
	public int deleteAcqOutBillById(Integer id) throws Exception {
		return acqOutBillMapper.deleteAcqOutBillById(id);
	}

	@Override
	public int deleteAcqOutBillByOutBillId(Integer outBillId) throws Exception {
		return acqOutBillMapper.deleteAcqOutBillByOutBillId(outBillId);
	}

	@Override
	public List<AcqOutBill> findAcqOutBillList(AcqOutBill acqOutBill, Sort sort, Page<AcqOutBill> page)
			throws Exception {
		return acqOutBillMapper.findAcqOutBillList(acqOutBill, sort, page);
	}

	@Override
	public List<AcqOutBill> findByOutBillId(Integer outBillId) {
		return acqOutBillMapper.findByOutBillId(outBillId);
	}

	@Override
	public int updateOutAmountByOutBillId(BigDecimal outAmount, Integer id) {
		return acqOutBillMapper.updateOutAmountByOutBillId(outAmount, id);
	}
	
}
