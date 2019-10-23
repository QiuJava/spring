package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.GatherCodeDao;
import cn.eeepay.framework.model.GatherCode;
import cn.eeepay.framework.service.GatherCodeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("gatherCodeService")
public class GatherCodeServiceImpl implements GatherCodeService {
	
	@Resource
	private GatherCodeDao gatherCodeDao;

	@Override
	public int insertBatch(List<GatherCode> list) {
		
		return gatherCodeDao.insertBatch(list);
	}
	
}
