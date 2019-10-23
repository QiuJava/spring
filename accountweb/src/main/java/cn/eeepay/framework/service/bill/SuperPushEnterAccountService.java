package cn.eeepay.framework.service.bill;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import cn.eeepay.framework.model.bill.SuperPushShareDaySettle;

public interface SuperPushEnterAccountService {

	Map<String, Object> superPushEnterAccount(SuperPushShareDaySettle superPushShareDaySettle) throws JsonParseException, JsonMappingException, IOException;

}
