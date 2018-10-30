package cn.qj.core.service;

import cn.qj.core.pojo.vo.IndexSummaryVo;

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
	IndexSummaryVo getIndexSummaryVO();

	/**
	 * 更新首页借款统计数据
	 */
	void updateIndexSummaryVO();
}
