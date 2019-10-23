package cn.eeepay.framework.service;

import cn.eeepay.framework.model.HappyBackNotFullDeductDetailQo;

import java.util.List;

import cn.eeepay.framework.model.HappyBackNotFullDeductDetail;
import cn.eeepay.framework.model.HappyBackNotFullDeductDetailList;

public interface HappyBackNotFullDeductService {

	HappyBackNotFullDeductDetail queryHappyBackNotFullDeductDetail(HappyBackNotFullDeductDetailQo qo);

	List<HappyBackNotFullDeductDetailList> exportHappyBackNotFullDeductDetailQuery(HappyBackNotFullDeductDetailQo qo);

}
