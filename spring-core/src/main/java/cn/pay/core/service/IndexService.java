package cn.pay.core.service;

import cn.pay.core.obj.vo.IndexSummaryVO;

/**
 * 首页借款信息服务
 * 
 * @author Administrator
 *
 */
public interface IndexService {
	/**
	 * 获取首页借款统计数据
	 * 
	 * @return
	 */
	IndexSummaryVO getIndexSummaryVO();

	/**
	 * 更新首页借款统计数据
	 */
	void updateIndexSummaryVO();
}
