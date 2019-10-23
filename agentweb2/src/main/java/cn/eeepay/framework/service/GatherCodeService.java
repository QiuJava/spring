package cn.eeepay.framework.service;

import java.util.List;

import cn.eeepay.framework.model.GatherCode;

public interface GatherCodeService {


	int insertBatch(List<GatherCode> list);

	
}
