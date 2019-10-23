package cn.eeepay.framework.dao.nposp;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import cn.eeepay.framework.model.nposp.MerchantCardInfo;

public interface MerchantCardInfoMapper {
	@Select("select mci.*,mi.mobilephone,mi.merchant_name from merchant_card_info mci "
			+ " left join merchant_info mi "
			+ " on mci.merchant_no=mi.merchant_no  "
			+ " where mi.merchant_no=#{merchantNo} "
			+ " and mci.def_settle_card = '1' "
			+ " and mci.status = '1' "
			+ " order by mci.create_time desc limit 1 " )
	List<MerchantCardInfo> getByMerchantNo(@Param("merchantNo")String merchantNo);
	
	@Select("select mci.*,mi.mobilephone from merchant_card_info mci "
			+ " left join merchant_info mi "
			+ " on mci.merchant_no=mi.merchant_no "
			+ " where mi.mobilephone=#{mobile} "
			+ " and mci.def_settle_card = '1' "
			+ " and mci.status = '1' "
			+ " order by mci.create_time desc limit 1 ")
	List<MerchantCardInfo> getByMobile(@Param("mobile")String mobile);
}
