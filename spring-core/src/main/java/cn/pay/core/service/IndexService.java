package cn.pay.core.service;

import cn.pay.core.obj.vo.IndexSummaryVO;

public interface IndexService {
	/**
	 * 获取首页借款统计数据
	 * 
	 * @return
	 */
	IndexSummaryVO getIndexSummaryVO();

	void updateIndexSummaryVO();
}
