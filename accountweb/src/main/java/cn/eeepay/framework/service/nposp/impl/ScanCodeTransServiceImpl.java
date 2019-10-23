package cn.eeepay.framework.service.nposp.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.eeepay.framework.dao.nposp.ScanCodeTransMapper;
import cn.eeepay.framework.model.nposp.ScanCodeTrans;
import cn.eeepay.framework.service.nposp.ScanCodeTransService;

@Service
public class ScanCodeTransServiceImpl implements ScanCodeTransService{
	@Autowired
	private ScanCodeTransMapper scanCodeTransMapper;

	@Override
	public List<ScanCodeTrans> queryTransOrder(String acqEnname,
			String jhTimeStart, String jhTimeEnd) {
		List<ScanCodeTrans>  ct = scanCodeTransMapper.findCheckData(acqEnname, jhTimeStart, jhTimeEnd);
		return ct;
	}

	@Override
	public ScanCodeTrans queryByAcqTransInfo(String acqOrderNo, String acqEnname) {
		ScanCodeTrans ct = scanCodeTransMapper.findDuiAccountDetailByTransInfo(acqOrderNo,acqEnname);
		return ct;
	}

}
