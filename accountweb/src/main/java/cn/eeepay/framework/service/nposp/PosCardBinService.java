package cn.eeepay.framework.service.nposp;

import cn.eeepay.framework.model.nposp.PosCardBin;

public interface PosCardBinService {
	PosCardBin findPosCardBinByCardNo(String cardNo) throws Exception;
}
