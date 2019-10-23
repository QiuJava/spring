package cn.eeepay.framework.service;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.SupertuiRule;

/**
 * 超级推service接口
 * 
 * @author junhu
 *
 */
public interface PushService {
	int insertSupertuiRule(SupertuiRule supertuiRule);

	List<SupertuiRule> getSupertuiRule(Map<String, Object> param, Page<SupertuiRule> page);

	SupertuiRule getSupertuiRule(Map<String, Object> param);

	int updateSupertuiRule(SupertuiRule supertuiRule);

	@SuppressWarnings("rawtypes")
	List<Map> getSupertuiOrder(Map<String, Object> param, Page<Map> page);

	@SuppressWarnings("rawtypes")
	Map getSupertuiOrderInfo(Map<String, Object> param);
}
