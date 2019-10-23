package cn.eeepay.framework.service.bill;

import java.math.BigDecimal;
import java.util.List;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.AcqOutBill;

public interface AcqOutBillService {
	
	int insertAcqOutBill(AcqOutBill acqOutBill) throws Exception;
	int updateAcqOutBillById(AcqOutBill acqOutBill) throws Exception;
	int deleteAcqOutBillById(Integer id) throws Exception;
	int deleteAcqOutBillByOutBillId(Integer outBillId) throws Exception;
	List<AcqOutBill> findAcqOutBillList(AcqOutBill acqOutBill,Sort sort,Page<AcqOutBill> page) throws Exception;
	
	List<AcqOutBill> findByOutBillId(Integer outBillId);
	
	int updateOutAmountByOutBillId(BigDecimal outAmount, Integer id);
}
