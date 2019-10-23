package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.PosCardBinDao;
import cn.eeepay.framework.model.PosCardBin;
import cn.eeepay.framework.service.PosCardBinService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("posCardBinService")
public class PosCardBinServiceImpl implements PosCardBinService {

	@Resource
	private PosCardBinDao posCardBinDao;
	
	@Override
	public PosCardBin queryInfo(String accountNo) {
		return posCardBinDao.queryInfo(accountNo);
	}

	@Override
	public String queryBankNo(String accountNo) {
		return posCardBinDao.queryBankNo(accountNo);
	}

	@Override
	public List<PosCardBin> queryAllInfo(String accountNo) {
		return posCardBinDao.queryAllInfo(accountNo);
	}

	@Override
	public List<String> queryAcpBearBankNameByType(Integer type){
		return posCardBinDao.queryAcpBearBankNameByType(type);
	}

	@Override
	public Map queryAcpBearBankByBankName(String bankName,Integer type){
		return posCardBinDao.queryAcpBearBankByBankName(bankName,type);
	}
}
