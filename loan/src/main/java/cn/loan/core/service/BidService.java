package cn.loan.core.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.loan.core.common.LogicException;
import cn.loan.core.config.cache.SystemDictionaryHashService;
import cn.loan.core.entity.Account;
import cn.loan.core.entity.Bid;
import cn.loan.core.entity.Borrow;
import cn.loan.core.entity.LoginUser;
import cn.loan.core.repository.BidDao;
import cn.loan.core.util.BigDecimalUtil;
import cn.loan.core.util.DateUtil;
import cn.loan.core.util.SecurityContextUtil;
import cn.loan.core.util.SystemDictionaryUtil;

/**
 * 投标服务
 * 
 * @author qiujian
 *
 */
@Service
public class BidService {

	@Autowired
	private BidDao bidDao;
	@Autowired
	private BorrowService borrowService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private SystemDictionaryHashService systemDictionaryHashService;
	@Autowired
	private AccountFlowService accountFlowService;

	@Transactional(rollbackFor = RuntimeException.class)
	public void bid(Long borrowId, BigDecimal bidAmount) {
		// 拿到借款对象
		Borrow borrow = borrowService.get(borrowId);
		Integer releaseIn = SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.BORROW_STATUS,
				SystemDictionaryUtil.RELEASE_IN, systemDictionaryHashService);
		// 1.检查标是否存在，检查是否是招标中，检查招标的时间是否到期
		if (borrow != null && borrow.getBorrowStatus().equals(releaseIn)
				&& DateUtil.getNewDate().before(borrow.getDeadline())) {
			LoginUser currentUser = SecurityContextUtil.getCurrentUser();
			// 拿到投资者账户对象
			Account bidAccount = accountService.get(currentUser.getId());
			// 2.检查投资 投标金额小于可用金额 大于最小投标金额 小于剩余可投金额 投资之后剩余金额 0=amount>=100
			BigDecimal validRemainAmount = borrow.getRemainAmount().subtract(bidAmount);
			boolean valid = validRemainAmount.compareTo(BigDecimalUtil.ZERO) == 0
					|| validRemainAmount.compareTo(borrow.getMinBidAmount()) >= 0;
			if (bidAmount.compareTo(bidAccount.getUsableBalance()) <= 0
					&& bidAmount.compareTo(borrow.getMinBidAmount()) >= 0
					&& bidAmount.compareTo(borrow.getRemainAmount()) <= 0 && valid) {

				// 3.账户金额减少，冻结金额增加
				bidAccount.setUsableBalance(bidAccount.getUsableBalance().subtract(bidAmount));
				bidAccount.setFreezedAmount(bidAccount.getFreezedAmount().add(bidAmount));

				// 4.增加一个bid对象
				Bid bid = new Bid();
				bid.setReturnRate(borrow.getRate());
				bid.setBidAmount(bidAmount);
				bid.setBorrow(borrow);
				bid.setBorrowTitle(borrow.getTitle());
				bid.setBidTime(DateUtil.getNewDate());
				bid.setInvestor(currentUser);
				bidDao.save(bid);
				// 生成投标流水
				accountFlowService.bidFLow(bid, bidAccount);

				// 5.修改借款相关属性
				borrow.setBidNum(borrow.getBidNum() + 1);
				borrow.setBidTotal(borrow.getBidTotal().add(bidAmount));

				Integer bidFull = SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.BORROW_STATUS,
						SystemDictionaryUtil.BID_FULL, systemDictionaryHashService);
				// 6.判断标是否投满，投满进入满标一审状态
				if (borrow.getBidTotal().compareTo(borrow.getBorrowAmount()) == 0) {
					borrow.setBorrowStatus(bidFull);
				}

				borrowService.save(borrow);
				accountService.save(bidAccount);

			}
		} else {
			throw new LogicException("招标日期到期");
		}
	}

}
