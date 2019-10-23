package cn.eeepay.framework.service.nposp;

import java.util.List;

import cn.eeepay.framework.model.nposp.ScanCodeTrans;

public interface ScanCodeTransService {
	List<ScanCodeTrans> queryTransOrder(String acqEnname,
			String jhTimeStart, String jhTimeEnd);	
	ScanCodeTrans queryByAcqTransInfo(String acqOrderNo,String acqEnname);
}
