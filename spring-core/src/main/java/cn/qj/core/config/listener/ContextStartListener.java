package cn.qj.core.config.listener;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import cn.qj.core.entity.Authority;
import cn.qj.core.entity.DataDict;
import cn.qj.core.service.AuthorityService;
import cn.qj.core.service.DataDictService;

/**
 * 应用启动监听
 * 
 * @author Qiujian
 * @date 2019年3月7日
 *
 */
@Component
public class ContextStartListener implements ApplicationListener<ContextRefreshedEvent> {

	public static final String DATA_DICT = "DATA_DICT";
	public static final String AUTHORITY = "AUTHORITY";

	@Autowired
	private ValueOperations<String, Object> valueOperations;

	@Autowired
	private HashOperations<String, String, Object> hashOperations;

	@Autowired
	private DataDictService dataDictService;

	@Autowired
	private AuthorityService authorityService;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		List<DataDict> dicts = dataDictService.getAll();
		for (DataDict dataDict : dicts) {
			hashOperations.put(DATA_DICT, dataDict.getDictKey(), dataDict);
		}
		List<Authority> Authorities = authorityService.getAll();
		valueOperations.set(AUTHORITY, Authorities);

	}

}
