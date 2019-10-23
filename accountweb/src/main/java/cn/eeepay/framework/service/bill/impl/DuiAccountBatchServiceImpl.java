package cn.eeepay.framework.service.bill.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.bill.DuiAccountBatchMapper;
import cn.eeepay.framework.dao.bill.DuiAccountDetailMapper;
import cn.eeepay.framework.dao.nposp.CollectiveTransOrderMapper;
import cn.eeepay.framework.dao.nposp.TransInfoMapper;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.enums.DuiAccountStatus;
import cn.eeepay.framework.enums.TransType;
import cn.eeepay.framework.model.bill.DuiAccountBatch;
import cn.eeepay.framework.model.bill.DuiAccountDetail;
import cn.eeepay.framework.model.bill.SysDict;
import cn.eeepay.framework.model.nposp.CollectiveTransOrder;
import cn.eeepay.framework.model.nposp.TransInfo;
import cn.eeepay.framework.service.bill.DuiAccountBatchService;
import cn.eeepay.framework.service.bill.SysDictService;
import cn.eeepay.framework.util.DateUtil;



@Service("duiAccountBatchService")
@Transactional
public class DuiAccountBatchServiceImpl implements DuiAccountBatchService{
	
	private static final Logger log = LoggerFactory.getLogger(DuiAccountBatchServiceImpl.class);
	
	@Resource
	public DuiAccountBatchMapper duiAccountBatchMapper;
	@Resource
	private DuiAccountDetailMapper duiAccountDetailMapper;
	@Resource
	private CollectiveTransOrderMapper collectiveTransOrderMapper;
	@Resource
	private TransInfoMapper transInfoMapper;
	@Resource
	public SysDictService sysDictService;
	@Override
	public int insertDuiAccountBatch(DuiAccountBatch dui) {
		return duiAccountBatchMapper.insertDuiAccountBatch(dui);
	}
	@Override
	public int updateDuiAccountBatch(DuiAccountBatch dui) {
		return duiAccountBatchMapper.updateDuiAccountBatch(dui);
	}

	@Override
	public int deleteDuiAccountBatch(Integer id) {
		DuiAccountBatch item = duiAccountBatchMapper.getById(id);
		List<DuiAccountDetail> list = duiAccountDetailMapper.findByCheckBatchNoAndErrorHandleSatus(item.getCheckBatchNo(), "pendingTreatment");
		
		
		if (list != null && list.size() > 0) {
			//包含不是待处理状态的明细数据，不允许删除
			return -2;
		} else {
			int re1 = duiAccountBatchMapper.deleteDuiAccountBatch(id);
			int re2 = duiAccountDetailMapper.deleteByCheckBatchNo(item.getCheckBatchNo());
			List<DuiAccountDetail> list2 =  duiAccountDetailMapper.findDuiAccountDetailListByCheckBatchNo(item.getCheckBatchNo());
			if((re1 + re2) > 0){
				updateTranDataByOrderNo(list2,item);
			}
			return re1 + re2;
		}
	}
	
	
	private void updateTranDataByOrderNo(List<DuiAccountDetail> list, DuiAccountBatch item) {

		try {
			for (DuiAccountDetail duiAccountDetail : list) {
				switch (item.getAcqEnname()) {
				case "KQ_ZQ":
					updateDskjDetailByInfo(duiAccountDetail);
					duiAccountDetail.setCheckAccountStatus("NO_CHECKED");
					duiAccountDetail.setRecordStatus(null);
					duiAccountDetail.setErrorHandleStatus("");
					duiAccountDetail.setErrorHandleCreator("");
					duiAccountDetail.setCheckBatchNo("");
					duiAccountDetail.setRecordStatus(2);
					duiAccountDetailMapper.updateDuiAccountDetail(duiAccountDetail);
					break;
				case "ZG_ZQ":
					updateKqZgDetailByInfo(duiAccountDetail);
					duiAccountDetail.setCheckAccountStatus("NO_CHECKED");
					duiAccountDetail.setRecordStatus(null);
					duiAccountDetail.setErrorHandleStatus("");
					duiAccountDetail.setErrorHandleCreator("");
					duiAccountDetail.setCheckBatchNo("");
					duiAccountDetail.setRecordStatus(2);
					duiAccountDetailMapper.updateDuiAccountDetail(duiAccountDetail);
					break;
				default:
					break;
				}
			}
		} catch (Exception e) {
			log.error("重新抓交易数据异常:", e);
			e.printStackTrace();
		}
	}
	/**
	 * 快钱
	 *
	 * @param detail
	 */
	private void updateDskjDetailByInfo(DuiAccountDetail detail) {
		detail.setAcqMerchantNo("");
		detail.setAcqMerchantName("");
        detail.setAcqReferenceNo("");//交易参考号，对账的唯一字段
		detail.setAcqTransTime(null);
		detail.setAcqAccountNo("");
		detail.setAcqTransAmount(new BigDecimal(0.00));//交易金额
		detail.setAcqTransType("");//收单机构交易类型
		detail.setAcqCheckDate(null);
		detail.setAcqRefundAmount(new BigDecimal(0.00));//手续费
		detail.setCreateTime(new Date());
	}

	private void updateKqZgDetailByInfo(DuiAccountDetail detail) {
		detail.setAcqMerchantNo("");
		detail.setAcqMerchantName("");
		detail.setAcqOrderNo("");
		detail.setAcqTransType("");
		detail.setAcqTransAmount(BigDecimal.ZERO);
		detail.setAcqRefundAmount(BigDecimal.ZERO);
		detail.setAcqTransTime(null);
		detail.setAcqTransStatus("");
		detail.setAcqMerchantOrderNo("");
	}

	@Override
	public DuiAccountBatch findDuiAccountBatchByFileNameAndAcqEnname(String fileName, String acqEnname) throws Exception {
		return duiAccountBatchMapper.findDuiAccountBatchByFileNameAndAcqEnname(fileName, acqEnname);
	}
	@Override
	public List<DuiAccountBatch> queryDuiAccountList(DuiAccountBatch duiAccountBatch, Sort sort,
			Page<DuiAccountBatch> page) throws Exception {
		return duiAccountBatchMapper.queryDuiAccountList(duiAccountBatch, sort, page);
	}
	
	@Override
	public Map<String, Object> getAcqTransAmountSumAndCount(String checkBatchNo) throws Exception {
		return duiAccountBatchMapper.getAcqTransAmountSumAndCount(checkBatchNo);
	}
	@Override
	public Map<String, Object> getPlateTransAmountSumAndCount(String checkBatchNo) throws Exception {
		return duiAccountBatchMapper.getPlateTransAmountSumAndCount(checkBatchNo);
	}
	@Override
	public Map<String, Object> getAcqTransSumAndCount(String checkBatchNo) throws Exception {
		return duiAccountBatchMapper.getAcqTransSumAndCount(checkBatchNo);
	}
	@Override
	public Map<String, Object> getPlateSumAndCount(String checkBatchNo) throws Exception {
		return duiAccountBatchMapper.getPlateSumAndCount(checkBatchNo);
	}
	@Override
	public Map<String, Object> getPlateSumAndCountMe(String checkBatchNo) throws Exception {
		return duiAccountBatchMapper.getPlateSumAndCountMe(checkBatchNo);
	}
	@Override
	public Map<String, Object> getAcqCnnameByAcqEnname(String acqEnname) throws Exception {
		return duiAccountBatchMapper.getAcqCnnameByAcqEnname(acqEnname);
	}
	@Override
	public int updateRecordStatus(String checkBatchNo, Integer recordStatus) {
		return duiAccountBatchMapper.updateRecordStatus(checkBatchNo, recordStatus);
	}
	@Override
	public DuiAccountBatch getByCheckBatchNo(String checkBatchNo) {
		return duiAccountBatchMapper.getByCheckBatchNo(checkBatchNo);
	}
}
