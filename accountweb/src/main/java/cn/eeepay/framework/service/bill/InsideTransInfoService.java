package cn.eeepay.framework.service.bill;

import cn.eeepay.framework.model.bill.InsideTransInfo;


public interface InsideTransInfoService {
	int insertInsideTransInfo(InsideTransInfo insideTransInfo) throws Exception;
	int updateInsideTransInfo(InsideTransInfo insideTransInfo) throws Exception;
}
