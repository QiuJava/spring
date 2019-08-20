package cn.loan.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.loan.core.common.PageResult;
import cn.loan.core.config.cache.SystemDictionaryHashService;
import cn.loan.core.entity.RealAuth;
import cn.loan.core.entity.SystemDictionaryItem;
import cn.loan.core.entity.UserInfo;
import cn.loan.core.entity.qo.RealAuthQo;
import cn.loan.core.repository.RealAuthDao;
import cn.loan.core.repository.specification.RealAuthSpecification;
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
		Page<RealAuth> page = realAuthDao.findAll(
				Specifications.where(RealAuthSpecification.equalAuditStatus(qo.getAuditStatus()))
						.and(RealAuthSpecification.greaterThanOrEqualToSubmissionTime(qo.getBeginTime()))
						.and(RealAuthSpecification.lessThanOrEqualToSubmissionTime(qo.getEndTime())),
				new PageRequest(qo.getPage(), qo.getSize(), Direction.DESC, StringUtil.SUBMISSION_TIME));
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
