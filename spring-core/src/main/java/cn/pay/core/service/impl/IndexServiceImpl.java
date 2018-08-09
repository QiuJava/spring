package cn.pay.core.service.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import cn.pay.core.consts.BidConst;
import cn.pay.core.domain.business.Borrow;
import cn.pay.core.pojo.vo.IndexSummaryVO;
import cn.pay.core.service.BorrowService;
import cn.pay.core.service.IndexService;

@Service
public class IndexServiceImpl implements IndexService {

	private static final String INDEX_SUMMARY_VO_KEY = "INDEX_SUMMARY_VO_KEY";

	@Autowired
	private BorrowService borrowService;

	@Resource
	private ValueOperations<String, IndexSummaryVO> valueOperations;

	public void updateIndexSummaryVO() {
		IndexSummaryVO indexVO = new IndexSummaryVO();
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
		valueOperations.set(INDEX_SUMMARY_VO_KEY, indexVO);
	}

	@Override
	public IndexSummaryVO getIndexSummaryVO() {
		return valueOperations.get(INDEX_SUMMARY_VO_KEY);
	}

}
