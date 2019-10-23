package cn.eeepay.framework.dao.nposp;

import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import cn.eeepay.framework.model.nposp.PosCardBin;
import cn.eeepay.framework.model.nposp.SysCalendar;

/**
 * 卡bin
 * @author zouruijin
 * @date 2016年8月16日17:19:42
 *
 */
public interface PosCardBinMapper {
	
	@Select(" select * from pos_card_bin c  where  c.card_length = length(#{cardNo}) AND c.verify_code = left(#{cardNo},  c.verify_length)")
	@ResultMap("cn.eeepay.framework.dao.nposp.PosCardBinMapper.BaseResultMap")
	public PosCardBin findPosCardBinByCardNo(String cardNo);
}
