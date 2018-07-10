package cn.pay.core.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.pay.core.dao.RealAuthRepository;
import cn.pay.core.domain.business.RealAuth;
import cn.pay.core.domain.business.UserInfo;
import cn.pay.core.obj.event.RealAuthEvent;
import cn.pay.core.obj.qo.RealAuthQo;
import cn.pay.core.service.RealAuthService;
import cn.pay.core.service.UserInfoService;
import cn.pay.core.util.BidStateUtil;
import cn.pay.core.util.HttpSessionContext;

@Service
public class RealAuthServiceImpl implements RealAuthService {

	@Autowired
	private RealAuthRepository repository;

	@Autowired
	private UserInfoService userInfoService;

	@Autowired
	private ApplicationContext ac;

	@Override
	public RealAuth get(Long realAuthId) {
		return repository.findOne(realAuthId);
	}

	@Override
	@Transactional
	public void save(RealAuth realAuth) {
		UserInfo userInfo = userInfoService.get(HttpSessionContext.getCurrentLoginInfo().getId());
		if (!userInfo.getIsRealAuth() && userInfo.getRealAuthId() == null) {
			realAuth.setState(RealAuth.AUTH_NORMAL);
			realAuth.setApplier(HttpSessionContext.getCurrentLoginInfo());
			realAuth.setApplyTime(new Date());
			userInfo.setRealAuthId(realAuth.getId());
			RealAuth auth = repository.saveAndFlush(realAuth);
			userInfo.setRealAuthId(auth.getId());
			userInfoService.update(userInfo);
		}
	}

	@Override
	public Page<RealAuth> page(RealAuthQo qo) {
		Page<RealAuth> page = repository.findAll(new Specification<RealAuth>() {
			@Override
			public Predicate toPredicate(Root<RealAuth> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<>();
				if (qo.getState() != -1) {
					list.add(cb.equal(root.get("state").as(Integer.class), qo.getState()));
				}
				if (qo.getBeginDate() != null) {
					list.add(cb.greaterThanOrEqualTo(root.get("applyTime").as(Date.class), qo.getBeginDate()));
				}
				if (qo.getEndDate() != null) {
					list.add(cb.lessThanOrEqualTo(root.get("applyTime").as(Date.class), qo.getEndDate()));
				}
				Predicate[] ps = new Predicate[list.size()];
				return cb.and(list.toArray(ps));
			}
		}, new PageRequest(qo.getCurrentPage() - 1, qo.getPageSize(), Direction.DESC, "applyTime"));
		return page;
	}

	@Override
	@Transactional
	public void autid(Long id, int state, String remark) {
		// 查询当前实名认证对象
		RealAuth realAuth = repository.findOne(id);
		if (realAuth.getState() == RealAuth.AUTH_NORMAL) {
			// 设置审核人审核时间
			realAuth.setAuditor(HttpSessionContext.getCurrentLoginInfo());
			realAuth.setAuditTime(new Date());
			realAuth.setRemark(remark);
			realAuth.setState(state);
			// 拿到申请人基本资料对象
			UserInfo applierInfo = userInfoService.get(realAuth.getApplier().getId());
			if (state == RealAuth.AUTH_PASS) {
				if (!applierInfo.getIsRealAuth()) {
					applierInfo.addState(BidStateUtil.OP_REAL_AUTH);
					// 把实名认证对象中的姓名身份证号码设置到申请人
					applierInfo.setRealName(realAuth.getRealname());
					applierInfo.setIdNumber(realAuth.getIdNumber());
					applierInfo.setRealAuthId(id);
				}
			} else {
				applierInfo.setRealAuthId(null);
			}
			userInfoService.update(applierInfo);
			repository.saveAndFlush(realAuth);
			// 实名认证成功发送短信
			ac.publishEvent(new RealAuthEvent(this, realAuth));
		}
	}

}
