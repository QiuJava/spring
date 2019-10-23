package cn.eeepay.framework.service.bill.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.bill.SettleTransferFileMapper;
import cn.eeepay.framework.model.bill.SettleTransferFile;
import cn.eeepay.framework.service.bill.SettleTransferFileService;



@Service("settleTransferFileService")
@Transactional
public class SettleTransferFileServiceImpl implements SettleTransferFileService{
	
	@Resource
	public SettleTransferFileMapper settleTransferFileMapper;

	@Override
	public int insertSettleTransferFile(SettleTransferFile stf) throws Exception {
		return settleTransferFileMapper.insertSettleTransferFile(stf);
	}

	@Override
	public int updateSettleTransferFileById(SettleTransferFile stf) throws Exception {
		return settleTransferFileMapper.updateSettleTransferFileById(stf);
	}

	@Override
	public int updateSettleTransferFileByIdAndStatus(SettleTransferFile stf) throws Exception {
		return settleTransferFileMapper.updateSettleTransferFileByIdAndStatus(stf);
	}

	@Override
	public List<SettleTransferFile> findFileByFileName(String fileName) throws Exception {
		return settleTransferFileMapper.findFileByFileName(fileName);
	}

	@Override
	public List<SettleTransferFile> findFileByFileMD5(String fileMd5) throws Exception {
		return settleTransferFileMapper.findFileByFileMD5(fileMd5);
	}
	
}
