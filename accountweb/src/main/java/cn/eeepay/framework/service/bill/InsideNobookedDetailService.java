package cn.eeepay.framework.service.bill;

import java.util.Date;
import java.util.List;

import cn.eeepay.framework.model.bill.InsideNobookedDetail;

public interface InsideNobookedDetailService {
	int insertInsideNobookedDetail(InsideNobookedDetail insideNobookedDetail) throws Exception;
	int updateInsideNobookedDetail(InsideNobookedDetail insideNobookedDetail) throws Exception;
	List<InsideNobookedDetail> findInsideNobookedDetailByParams(String accountNo,Date transDate,String bookedFlag) throws Exception;
}
