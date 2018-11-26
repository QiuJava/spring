package cn.qj.core.service.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import cn.qj.core.consts.BidConst;
import cn.qj.core.consts.RedisKeyConst;
import cn.qj.core.entity.Borrow;
import cn.qj.core.pojo.vo.IndexSummaryVo;
import cn.qj.core.service.BorrowService;
import cn.qj.core.service.IndexService;

/**
 * 首页服务实现
 * 
 * @author Qiujian
 * @date 2018/8/10
 */
@Service
public class IndexServiceImpl implements IndexService {

	@Autowired
	private BorrowService borrowService;

	@Resource
	private ValueOperations<String, IndexSummaryVo> valueOperations;

	@Override
	public void updateIndexSummaryVO() {
		IndexSummaryVo indexVO = new IndexSummaryVo();
		List<Borrow> borrowList = borrowService.listByState(BidConst.BORROW_STATE_PAYING_BACK,
				BidConst.BORROW_STATE_COMPLETE_PAY_BACK);
		BigDecimal totalBorrowAmount = BigDecimal.ZERO;
		BigDecimal totalInterestAmount = BigDecimal.ZERO;
		for (Borrow borrow : borrowList) {
			totalBorrowAmount = totalBorrowAmount.add(borrow.getAmount());
			totalInterestAmount = totalInterestAmount.add(borrow.getTotalInterestAmount());
		}
		indexVO.setTotalBorrowAmount(totalBorrowAmount);
		indexVO.setTotalBorrowUser(borrowList.size());
		indexVO.setTotalInterestAmount(totalInterestAmount);
		valueOperations.set(RedisKeyConst.INDEX_SUMMARY_VO_KEY, indexVO);
	}

	@Override
	public IndexSummaryVo getIndexSummaryVO() {
		return valueOperations.get(RedisKeyConst.INDEX_SUMMARY_VO_KEY);
	}

}
