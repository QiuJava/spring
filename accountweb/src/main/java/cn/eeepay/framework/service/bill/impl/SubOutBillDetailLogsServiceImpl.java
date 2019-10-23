package cn.eeepay.framework.service.bill.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.bill.SubOutBillDetailLogsMapper;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.SubOutBillDetail;
import cn.eeepay.framework.model.bill.SubOutBillDetailLogs;
import cn.eeepay.framework.service.bill.SubOutBillDetailLogsService;



@Service("subOutBillDetailLogsService")
@Transactional
public class SubOutBillDetailLogsServiceImpl implements SubOutBillDetailLogsService {
	private static final Logger log = LoggerFactory.getLogger(SubOutBillDetailLogsServiceImpl.class);
	@Resource
	public SubOutBillDetailLogsMapper subOutBillDetailLogsMapper;
	@Override
	public int insertOutBillDetailLogs(SubOutBillDetail subOutBillDetail) throws Exception {
		subOutBillDetail.setSubOutBillDetailId(subOutBillDetail.getId());
		return subOutBillDetailLogsMapper.insertOutBillDetailLogs(subOutBillDetail);
	}
	@Override
	public List<SubOutBillDetailLogs> findOutBillDetailLogsList(SubOutBillDetailLogs subOutBillDetailLogs, Sort sort,
			Page<SubOutBillDetailLogs> page) {
		
		// 查询子出账单明细
		if (StringUtils.isNotBlank(subOutBillDetailLogs.getStartTime())) {
			subOutBillDetailLogs.setStartTime(subOutBillDetailLogs.getStartTime() + " 00:00:00");
		}
		if (StringUtils.isNotBlank(subOutBillDetailLogs.getEndTime())) {
			subOutBillDetailLogs.setEndTime(subOutBillDetailLogs.getEndTime() + " 23:59:59");
		}

		// 查询子出账单明细
		if (StringUtils.isNotBlank(subOutBillDetailLogs.getTransTimeStart())) {
			subOutBillDetailLogs.setTransTimeStart(subOutBillDetailLogs.getTransTimeStart() + " 00:00:00");
		}
		if (StringUtils.isNotBlank(subOutBillDetailLogs.getTransTimesEnd())) {
			subOutBillDetailLogs.setTransTimesEnd(subOutBillDetailLogs.getTransTimesEnd() + " 23:59:59");
		}
		
		return subOutBillDetailLogsMapper.findOutBillDetailLogsList(subOutBillDetailLogs,sort,page);
	}
	@Override
	public List<SubOutBillDetailLogs> exportOutBillDetailLogsList(SubOutBillDetailLogs subOutBillDetailLogs) {
		// 查询子出账单明细
				if (StringUtils.isNotBlank(subOutBillDetailLogs.getStartTime())) {
					subOutBillDetailLogs.setStartTime(subOutBillDetailLogs.getStartTime() + " 00:00:00");
				}
				if (StringUtils.isNotBlank(subOutBillDetailLogs.getEndTime())) {
					subOutBillDetailLogs.setEndTime(subOutBillDetailLogs.getEndTime() + " 23:59:59");
				}

				// 查询子出账单明细
				if (StringUtils.isNotBlank(subOutBillDetailLogs.getTransTimeStart())) {
					subOutBillDetailLogs.setTransTimeStart(subOutBillDetailLogs.getTransTimeStart() + " 00:00:00");
				}
				if (StringUtils.isNotBlank(subOutBillDetailLogs.getTransTimesEnd())) {
					subOutBillDetailLogs.setTransTimesEnd(subOutBillDetailLogs.getTransTimesEnd() + " 23:59:59");
				}
		return subOutBillDetailLogsMapper.exportOutBillDetailLogsList(subOutBillDetailLogs);
	}


	
	
}
