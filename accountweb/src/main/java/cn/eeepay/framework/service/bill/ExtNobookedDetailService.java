package cn.eeepay.framework.service.bill;

import java.util.Date;
import java.util.List;

import cn.eeepay.framework.model.bill.ExtNobookedDetail;

public interface ExtNobookedDetailService {
	int insertExtNobookedDetail(ExtNobookedDetail extNobookedDetail) throws Exception;
	int updateExtNobookedDetail(ExtNobookedDetail extNobookedDetail) throws Exception;
	List<ExtNobookedDetail> findExtNobookedDetailByParams(String accountNo,Date recordDate,String bookedFlag) throws Exception;
}
