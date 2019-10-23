package cn.eeepay.framework.service.bill;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.model.bill.DuiAccountDetail;

public interface DuiAccountService {
	
	String doDuiAccountForKqzq(String acqEnname, List<DuiAccountDetail> dbDetails, Map<String, Object> map);

    String doDuiAccountForKqZg(String acqEnname, List<DuiAccountDetail> dbDetails, Map<String, Object> map);
}
