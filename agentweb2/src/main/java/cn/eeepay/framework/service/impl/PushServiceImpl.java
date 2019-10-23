package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.PushDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.SupertuiRule;
import cn.eeepay.framework.service.PushService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 超级推service实现
 * 
 * @author junhu
 *
 */
@Service("pushService")
public class PushServiceImpl implements PushService {

	@Resource
	private PushDao pushDao;

	@Override
	public int insertSupertuiRule(SupertuiRule supertuiRule) {

		return pushDao.insertSupertuiRule(supertuiRule);
	}

	@Override
	public List<SupertuiRule> getSupertuiRule(Map<String, Object> param, Page<SupertuiRule> page) {

		return pushDao.getSupertuiRule(param, page);
	}

	@Override
	public SupertuiRule getSupertuiRule(Map<String, Object> param) {

		return pushDao.getSupertuiRuleInfo(param);
	}

	@Override
	public int updateSupertuiRule(SupertuiRule supertuiRule) {

		return pushDao.updateSupertuiRule(supertuiRule);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Map> getSupertuiOrder(Map<String, Object> param, Page<Map> page) {

		return pushDao.getSupertuiOrder(param, page);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map getSupertuiOrderInfo(Map<String, Object> param) {

		return pushDao.getSupertuiOrderInfo(param);
	}

}
