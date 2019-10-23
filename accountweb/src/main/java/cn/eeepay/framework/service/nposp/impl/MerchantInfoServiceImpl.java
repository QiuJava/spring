package cn.eeepay.framework.service.nposp.impl;

import cn.eeepay.framework.dao.nposp.MerchantInfoMapper;
import cn.eeepay.framework.model.nposp.MerchantInfo;
import cn.eeepay.framework.model.nposp.TransInfoPreFreezeLog;
import cn.eeepay.framework.service.nposp.MerchantInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service("merchantInfoService")
@Transactional("nposp")
public class MerchantInfoServiceImpl implements MerchantInfoService {
	@Resource
	private MerchantInfoMapper merchantInfoMapper;

	@Override
	public MerchantInfo findMerchantInfoByUserId(String userId) {
		return merchantInfoMapper.findMerchantInfoByUserId(userId);
	}

	@Override
	public Map<String,Object> findMerchantInfoByBpmIdAndChannelCode(String bpmId, String channelCode) {
		return merchantInfoMapper.findMerchantInfoByBpmIdAndChannelCode(bpmId,channelCode);
	}

	@Override
	public List<String> findByNameAndMobile(String merchantNo,
			String merchantName, String mobile) {
		return merchantInfoMapper.findByNameAndMobile(merchantNo, merchantName, mobile);
	}

	@Override
	public List<String> findMerchantListByParams(String userName, String mobilephone) {
		return merchantInfoMapper.findMerchantListByParams(userName, mobilephone);
	}
	@Override
	public Map<String, Object> queryQrMerchantInfo(String acqMerchantNo) {
		return merchantInfoMapper.queryQrMerchantInfo(acqMerchantNo);
	}

	@Override
	public Map<String, Object> queryQrMerInfo(String merchantNo) {
		return merchantInfoMapper.queryQrMerInfo(merchantNo);
	}
	//增加预冻结金额
	@Override
	public void insertLogAndUpdateMerchantInfoAmount(TransInfoPreFreezeLog record) {
		MerchantInfo merchantInfo = null;
		try {
			// 获得该商户之前的预冻结金额
			BigDecimal originalPreFrozenMoney = BigDecimal.ZERO;
			merchantInfo = merchantInfoMapper.selectByMerchantNo(record.getMerchantNo());
			originalPreFrozenMoney = originalPreFrozenMoney.add(merchantInfo.getPreFrozenAmount());

			BigDecimal preFreezeAmount = merchantInfo.getPreFrozenAmount().add(record.getPreFreezeAmount());
			record.setOperLog("预冻结金额从" + originalPreFrozenMoney + "更改为" + preFreezeAmount);

			// 插入预冻结金额操作日志
			merchantInfoMapper.insertPreFreezeLog(record);
			// 更新商户表中的预冻结金额
			merchantInfoMapper.updatePreFrozenAmountByMerId(preFreezeAmount, record.getMerchantNo());
		} catch (Exception e) {
			throw new RuntimeException("修改预冻结金额失败");
		}
	}
	//减少预冻结金额
	@Override
	public void insertPreFreezeLog(TransInfoPreFreezeLog record) {
		MerchantInfo merchantInfo = null;
		try {
			// 获得该商户之前的预冻结金额
			BigDecimal originalPreFrozenMoney = BigDecimal.ZERO;
			merchantInfo = merchantInfoMapper.selectByMerchantNo(record.getMerchantNo());
			originalPreFrozenMoney = originalPreFrozenMoney.add(merchantInfo.getPreFrozenAmount());

			BigDecimal preFreezeAmount = merchantInfo.getPreFrozenAmount().subtract(record.getPreFreezeAmount());
			record.setOperLog("预冻结金额从" + originalPreFrozenMoney + "更改为" + preFreezeAmount);

			// 插入预冻结金额操作日志
			merchantInfoMapper.insertPreFreezeLog(record);
			// 更新商户表中的预冻结金额
			merchantInfoMapper.updatePreFrozenAmountByMerId(preFreezeAmount, record.getMerchantNo());
		} catch (Exception e) {
			throw new RuntimeException("修改预冻结金额失败");
		}
	}
}
