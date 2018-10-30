package cn.qj.loan.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import cn.qj.core.service.IndexService;

/**
 * 首页统计数据监听
 * 
 * @author Qiujian
 * @date 2018/10/30
 */
@Component
public class IndexSummaryListener implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private IndexService indexService;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		indexService.updateIndexSummaryVO();
	}

}
