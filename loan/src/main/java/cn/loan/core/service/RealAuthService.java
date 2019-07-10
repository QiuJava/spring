package cn.loan.core.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.loan.core.common.PageResult;
import cn.loan.core.config.cache.SystemDictionaryHashService;
import cn.loan.core.entity.RealAuth;
import cn.loan.core.entity.SystemDictionaryItem;
import cn.loan.core.entity.UserInfo;
import cn.loan.core.entity.qo.RealAuthQo;
import cn.loan.core.repository.RealAuthDao;
import cn.loan.core.util.DateUtil;
import cn.loan.core.util.SecurityContextUtil;
import cn.loan.core.util.StringUtil;
import cn.loan.core.util.SystemDictionaryUtil;
import cn.loan.core.util.UserInfoStatusUtil;

/**
 * 实名认证服务
 * 
 * @author qiujian
 *
 */
@Service
public class RealAuthService {

	@Autowired
	private RealAuthDao realAuthDao;

	@Autowired
	private UserInfoService userInfoService;

	@Autowired
	private SystemDictionaryHashService systemDictionaryHashService;

	public RealAuth get(Long realAuthId) {
		RealAuth realAuth = realAuthDao.findOne(realAuthId);
		List<SystemDictionaryItem> genders = SystemDictionaryUtil.getItems(SystemDictionaryUtil.GENDER,
				systemDictionaryHashService);
		for (SystemDictionaryItem item : genders) {
			if (realAuth.getGender().equals(Integer.valueOf(item.getItemValue()))) {
				realAuth.setDisplayGender(item.getItemName());
			}
		}
		return realAuth;
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public void save(RealAuth realAuth) {
		UserInfo current = userInfoService.getCurrent();
		// 没有实名认证
		if (!current.isRealAuth()) {
			Integer auditNormal = SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.AUDIT,
					SystemDictionaryUtil.AUDIT_NORMAL, systemDictionaryHashService);
			realAuth.setAuditStatus(auditNormal);
			realAuth.setSubmitter(SecurityContextUtil.getCurrentUser());
			realAuth.setSubmissionTime(DateUtil.getNewDate());
			realAuthDao.save(realAuth);
			current.setRealAuthId(realAuth.getId());
			userInfoService.save(current);
		}
	}

	public PageResult pageQuery(RealAuthQo qo) {
		Page<RealAuth> page = realAuthDao.findAll(new Specification<RealAuth>() {
			@Override
			public Predicate toPredicate(Root<RealAuth> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<>();
				Integer auditStatus = qo.getAuditStatus();
				if (auditStatus != null && auditStatus != -1) {
					Predicate auditStatusPredicate = cb.equal(root.get(StringUtil.AUDIT_STATUS), auditStatus);
					predicateList.add(auditStatusPredicate);
				}
				Date beginTime = qo.getBeginTime();
				if (beginTime != null) {
					Predicate beginTimePredicate = cb.greaterThanOrEqualTo(root.get(StringUtil.SUBMISSION_TIME),
							beginTime);
					predicateList.add(beginTimePredicate);
				}
				Date endTime = qo.getEndTime();
				if (endTime != null) {
					Predicate endTimePredicate = cb.lessThanOrEqualTo(root.get(StringUtil.SUBMISSION_TIME), endTime);
					predicateList.add(endTimePredicate);
				}
				Predicate[] predicateArray = new Predicate[predicateList.size()];
				return cb.and(predicateList.toArray(predicateArray));
			}
		}, new PageRequest(qo.getPage(), qo.getSize(), Direction.DESC, StringUtil.SUBMISSION_TIME));
		List<RealAuth> content = page.getContent();
		List<SystemDictionaryItem> audits = SystemDictionaryUtil.getItems(SystemDictionaryUtil.AUDIT,
				systemDictionaryHashService);
		List<SystemDictionaryItem> genders = SystemDictionaryUtil.getItems(SystemDictionaryUtil.GENDER,
				systemDictionaryHashService);
		// 设置审核状态显示 性别显示
		for (RealAuth realAuth : content) {
			for (SystemDictionaryItem item : genders) {
				if (realAuth.getGender().equals(Integer.valueOf(item.getItemValue()))) {
					realAuth.setDisplayGender(item.getItemName());
				}
			}
			for (SystemDictionaryItem item : audits) {
				if (realAuth.getAuditStatus().equals(Integer.valueOf(item.getItemValue()))) {
					realAuth.setAuditStatusDisplay(item.getItemName());
				}
			}
		}
		return new PageResult(page.getContent(), page.getTotalPages(), qo.getCurrentPage());
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public void autid(Long id, Integer auditStatus, String remark) {
		RealAuth realAuth = this.get(id);
		Integer auditNormal = SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.AUDIT,
				SystemDictionaryUtil.AUDIT_NORMAL, systemDictionaryHashService);
		if (auditNormal.equals(realAuth.getAuditStatus())) {
			// 审核通过
			Integer auditPass = SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.AUDIT,
					SystemDictionaryUtil.AUDIT_PASS, systemDictionaryHashService);
			Integer auditReject = SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.AUDIT,
					SystemDictionaryUtil.AUDIT_REJECT, systemDictionaryHashService);
			realAuth.setAuditor(SecurityContextUtil.getCurrentUser());
			realAuth.setAuditTime(DateUtil.getNewDate());
			realAuth.setRemark(remark);
			UserInfo info = userInfoService.get(realAuth.getSubmitter().getId());
			if (auditPass.equals(auditStatus)) {
				realAuth.setAuditStatus(auditPass);
				info.addStatus(UserInfoStatusUtil.OP_REAL_AUTH);
				info.setRealName(realAuth.getRealName());
				info.setIdNumber(realAuth.getIdNumber());
				realAuthDao.save(realAuth);
			} else if (auditReject.equals(auditReject)) {
				realAuth.setAuditStatus(auditReject);
				info.setRealAuthId(null);
				realAuthDao.save(realAuth);
			}
			userInfoService.save(info);
		}

	}

}
