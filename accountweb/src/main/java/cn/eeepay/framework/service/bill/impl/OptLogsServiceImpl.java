package cn.eeepay.framework.service.bill.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.eeepay.framework.dao.bill.OptLogsMapper;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.OptLogs;
import cn.eeepay.framework.service.bill.OptLogsService;

@Service("optLogsService")
public class OptLogsServiceImpl implements OptLogsService {

	@Resource
	public OptLogsMapper optLogsMapper;

	@Override
	public List<OptLogs> findOperateLog(OptLogs operateLog, Map<String, String> params, Sort sort,
			Page<OptLogs> page) throws Exception {
		long id = 1;// 对页面序号进行重新排号
		List<OptLogs> list = optLogsMapper.findDuiAccountOptLogs(operateLog, params, sort, page);
		for (OptLogs log : list) {
			log.setId(id);
			id++;
		}
		return list;
	}

	@Override
	public int insertOptLogs(OptLogs log) {
		return optLogsMapper.insertOptLogs(log);
	}

}
