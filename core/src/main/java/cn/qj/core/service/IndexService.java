package cn.qj.core.service;

import cn.qj.core.pojo.vo.IndexSummaryVo;

/**
 * 首页信息服务
 * 
 * @author Qiujian
 * @date 2018/11/01
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
