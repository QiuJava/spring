package cn.eeepay.framework.service.peragent;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import cn.eeepay.framework.model.peragent.PaShareDetail;

public interface PaShareDetailAccountService {

	Map<String, Object> singleEnterAccount(PaShareDetail paShareDetailQuery, String userName)  throws JsonParseException, JsonMappingException, IOException;

	Map<String, Object> bacthAccount(String accountMonth, String username);

}
