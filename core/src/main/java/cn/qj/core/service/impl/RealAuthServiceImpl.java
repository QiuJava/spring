package cn.qj.core.service.impl;

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

import cn.qj.core.entity.RealAuth;
import cn.qj.core.entity.UserInfo;
import cn.qj.core.pojo.event.RealAuthEvent;
import cn.qj.core.pojo.qo.RealAuthQo;
import cn.qj.core.repository.RealAuthRepository;
import cn.qj.core.service.RealAuthService;
import cn.qj.core.service.UserInfoService;
import cn.qj.core.util.BidStateUtil;
import cn.qj.core.util.HttpServletContext;

/**
 * 实名认证服务实现
 * 
 * @author Qiujian
 * @date 2018/8/10
 */
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
	@Transactional(rollbackFor = { RuntimeException.class })
	public void save(RealAuth realAuth) {
		UserInfo userInfo = userInfoService.get(HttpServletContext.getCurrentLoginInfo().getId());
		if (!userInfo.getIsRealAuth() && userInfo.getRealAuthId() == null) {
			realAuth.setState(RealAuth.AUTH_NORMAL);
			realAuth.setApplier(HttpServletContext.getCurrentLoginInfo());
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
	@Transactional(rollbackFor = { RuntimeException.class })
	public void autid(Long id, Integer state, String remark) {
		// 查询当前实名认证对象
		RealAuth realAuth = repository.findOne(id);
		if (realAuth.getState() == RealAuth.AUTH_NORMAL) {
			// 设置审核人审核时间
			realAuth.setAuditor(HttpServletContext.getCurrentLoginInfo());
			realAuth.setAuditTime(new Date());
			realAuth.setRemark(remark);
			realAuth.setState(state);
			// 拿到申请人基本资料对象
			UserInfo applierInfo = userInfoService.get(realAuth.getApplier().getId());
			if (state.equals(RealAuth.AUTH_PASS)) {
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
