package cn.pay.core.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.pay.core.consts.BidConst;
import cn.pay.core.domain.business.Borrow;
import cn.pay.core.pojo.vo.IndexSummaryVO;
import cn.pay.core.service.BorrowService;
import cn.pay.core.service.IndexService;

@Service
public class IndexServiceImpl implements IndexService {

	@Autowired
	private BorrowService borrowService;

	/**
	 * 首页借款统计数据缓存
	 */
	private IndexSummaryVO indexVO = new IndexSummaryVO();

	@Override
	public IndexSummaryVO getIndexSummaryVO() {
		return indexVO;
	}

	public void updateIndexSummaryVO() {
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
	}

}
