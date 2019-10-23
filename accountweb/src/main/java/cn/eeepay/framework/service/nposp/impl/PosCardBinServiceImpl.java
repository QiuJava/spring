package cn.eeepay.framework.service.nposp.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.nposp.PosCardBinMapper;
import cn.eeepay.framework.model.nposp.PosCardBin;
import cn.eeepay.framework.service.nposp.PosCardBinService;

/**
 * 
 * 卡bin表
 * @author zouruijin
 * @date 2016年8月17日11:29:40
 */

@Service("posCardBinService")
@Transactional("nposp")
public class PosCardBinServiceImpl  implements PosCardBinService{

	@Resource
	public PosCardBinMapper posCardBinMapper;

	@Override
	public PosCardBin findPosCardBinByCardNo(String cardNo) throws Exception {
		return posCardBinMapper.findPosCardBinByCardNo(cardNo);
	}

}
