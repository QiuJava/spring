package cn.eeepay.framework.service.bill;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.DuiAccountDetail;
import cn.eeepay.framework.model.bill.SubOutBillDetail;

public interface SubOutBillDetailService {

	int insertOutBillDetail(SubOutBillDetail subOutBillDetail) throws Exception;

	List<SubOutBillDetail> findSubOutBillDetailList(SubOutBillDetail subOutBillDetail, String merchantNo, String acqOrgNo,
			String merchantBalance1, String merchantBalance2, String outAccountTaskAmount1,
			String outAccountTaskAmount2, String isChangeRemark, String timeStart, String timeEnd, Sort sort, Page<SubOutBillDetail> page);

	int updateOutBillDetailChangeRemark(SubOutBillDetail subOutBillDetail);

	int updateOutBillDetailById(List<SubOutBillDetail> subOutBillDetailList);

	List<SubOutBillDetail> findSubMerChuAccountList(SubOutBillDetail subOutBillDetail, Sort sort,
			Page<SubOutBillDetail> page);
	
	int  updateOutBillDetailByOrderReNum(SubOutBillDetail subOutBillDetail, String isAddBill);

	SubOutBillDetail queryOutBillDetailById(SubOutBillDetail subOutBillDetail);

	int updateSubOutDetaiAndCheckOutDetail(List<String> subDetailIdList, BigDecimal subTotal, Integer integer);

	List<SubOutBillDetail> exportOutBillDetailList(SubOutBillDetail subOutBillDetail);

	SubOutBillDetail queryOutBillDetailByOrderRefNum(SubOutBillDetail subOutBillDetail);

	List<SubOutBillDetail> exportSubOutBillDetailList(SubOutBillDetail subOutBillDetail, String merchantNo,
			String acqOrgNo, String merchantBalance1, String merchantBalance2, String outAccountTaskAmount1,
			String outAccountTaskAmount2, String isChangeRemark, String timeStart, String timeEnd);

	String insertAndUpdateRecordStatus(DuiAccountDetail returnDuiAccountDetail, Integer outBillId);

	Map<String, Object> judgeIsAddSubOutDetail(List<String> subDetailIdList, Integer outBillId);

	int findVerifyFlag(Integer id);

	Map<String, Object> updateRemarkBacthOutBillDetailById(List<SubOutBillDetail> subOutBillDetailList);


}
