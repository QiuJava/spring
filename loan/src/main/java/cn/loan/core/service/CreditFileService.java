package cn.loan.core.service;

import java.util.ArrayList;
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
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.loan.core.common.PageResult;
import cn.loan.core.config.cache.SystemDictionaryHashService;
import cn.loan.core.entity.CreditFile;
import cn.loan.core.entity.SystemDictionaryItem;
import cn.loan.core.entity.UserInfo;
import cn.loan.core.entity.qo.CreditFileQo;
import cn.loan.core.repository.CreditFileDao;
import cn.loan.core.repository.specification.CreditFileSpecification;
import cn.loan.core.util.DateUtil;
import cn.loan.core.util.SecurityContextUtil;
import cn.loan.core.util.StringUtil;
import cn.loan.core.util.SystemDictionaryUtil;

/**
 * 信用材料服务
 * 
 * @author qiujian
 *
 */
@Service
public class CreditFileService {

	@Autowired
	private CreditFileDao creditFileDao;

	@Autowired
	private UserInfoService userInfoService;
	@Autowired
	private SystemDictionaryItemService systemDictionaryItemService;
	@Autowired
	private SystemDictionaryHashService systemDictionaryHashService;

	public List<CreditFile> listCurrentFilesTypeIsNull() {
		List<CreditFile> list = creditFileDao.findBySubmitterAndFileTypeIsNull(SecurityContextUtil.getCurrentUser());
		List<SystemDictionaryItem> audits = SystemDictionaryUtil.getItems(SystemDictionaryUtil.AUDIT,
				systemDictionaryHashService);
		for (CreditFile creditFile : list) {
			for (SystemDictionaryItem item : audits) {
				if (creditFile.getAuditStatus().equals(Integer.valueOf(item.getItemValue()))) {
					creditFile.setAuditStatusDisplay(item.getItemName());
				}
			}
		}
		return list;
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public void save(String fileName) {
		CreditFile file = new CreditFile();
		file.setFileName(fileName);
		file.setSubmitter(SecurityContextUtil.getCurrentUser());
		file.setSubmissionTime(DateUtil.getNewDate());
		Integer itemValue = SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.AUDIT,
				SystemDictionaryUtil.AUDIT_NORMAL, systemDictionaryHashService);
		file.setAuditStatus(itemValue);
		creditFileDao.save(file);
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public void updateType(Long[] ids, Long[] fileTypes) {
		for (int i = 0; i < ids.length; i++) {
			CreditFile file = creditFileDao.findOne(ids[i]);
			file.setFileType(systemDictionaryItemService.get(fileTypes[i]));
			creditFileDao.save(file);
		}
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public void audit(Long id, Integer auditStatus, Integer score, String remark) {
		CreditFile file = creditFileDao.findOne(id);
		file.setAuditStatus(auditStatus);
		file.setScore(score);
		file.setRemark(remark);
		file.setAuditor(SecurityContextUtil.getCurrentUser());
		file.setAuditTime(DateUtil.getNewDate());
		creditFileDao.save(file);
		// 增加提交者信用分
		Integer itemValue = SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.AUDIT,
				SystemDictionaryUtil.AUDIT_PASS, systemDictionaryHashService);
		if (score != null && itemValue.equals(auditStatus)) {
			UserInfo userInfo = userInfoService.get(file.getSubmitter().getId());
			userInfo.setAuthScore(userInfo.getAuthScore() + score);
			userInfoService.save(userInfo);
		}

	}

	public PageResult pageQuery(CreditFileQo qo) {
		Page<CreditFile> page = creditFileDao.findAll(
				Specifications.where(CreditFileSpecification.equalAuditStatus(qo.getAuditStatus()))
						.and(CreditFileSpecification.greaterThanOrEqualToSubmissionTime(qo.getBeginTime()))
						.and(CreditFileSpecification.lessThanOrEqualToSubmissionTime(qo.getEndTime())),
				new PageRequest(qo.getPage(), qo.getSize(), Direction.DESC, StringUtil.SUBMISSION_TIME));
		List<CreditFile> list = page.getContent();
		List<SystemDictionaryItem> audits = SystemDictionaryUtil.getItems(SystemDictionaryUtil.AUDIT,
				systemDictionaryHashService);
		for (CreditFile creditFile : list) {
			for (SystemDictionaryItem item : audits) {
				if (creditFile.getAuditStatus().equals(Integer.valueOf(item.getItemValue()))) {
					creditFile.setAuditStatusDisplay(item.getItemName());
				}
			}
		}
		return new PageResult(list, page.getTotalPages(), qo.getCurrentPage());
	}

	public List<CreditFile> listCurrentFilesTypeIsNotNull() {
		List<CreditFile> list = creditFileDao.findBySubmitterAndFileTypeIsNotNull(SecurityContextUtil.getCurrentUser());
		List<SystemDictionaryItem> audits = SystemDictionaryUtil.getItems(SystemDictionaryUtil.AUDIT,
				systemDictionaryHashService);
		for (CreditFile creditFile : list) {
			for (SystemDictionaryItem item : audits) {
				if (creditFile.getAuditStatus().equals(Integer.valueOf(item.getItemValue()))) {
					creditFile.setAuditStatusDisplay(item.getItemName());
				}
			}
		}
		return list;
	}

	public List<CreditFile> query(CreditFileQo qo) {
		return creditFileDao.findAll(new Specification<CreditFile>() {
			@Override
			public Predicate toPredicate(Root<CreditFile> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<>();
				Integer auditStatus = qo.getAuditStatus();
				if (auditStatus != null && auditStatus != -1) {
					list.add(cb.equal(root.get(StringUtil.AUDIT_STATUS), auditStatus));
				}
				list.add(cb.equal(root.get(StringUtil.SUBMITTER), qo.getSubmitter()));
				Predicate[] ps = new Predicate[list.size()];
				return cb.and(list.toArray(ps));
			}
		});
	}

}
