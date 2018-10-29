package cn.pay.core.service.impl;

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

import cn.pay.core.entity.business.UserFile;
import cn.pay.core.entity.business.UserInfo;
import cn.pay.core.entity.sys.LoginInfo;
import cn.pay.core.entity.sys.SystemDictionaryItem;
import cn.pay.core.pojo.qo.UserFileQo;
import cn.pay.core.repository.UserFileRepository;
import cn.pay.core.service.LoginInfoService;
import cn.pay.core.service.SystemDictionaryItemService;
import cn.pay.core.service.UserFileService;
import cn.pay.core.service.UserInfoService;
import cn.pay.core.util.HttpServletContext;
import cn.pay.core.util.LogicException;

/**
 * 用户材料服务实现
 * 
 * @author Qiujian
 * @date 2018年8月10日
 */
@Service
public class UserFileServiceImpl implements UserFileService {

	@Autowired
	private UserFileRepository repository;

	@Autowired
	private SystemDictionaryItemService systemDictionaryItemService;
	@Autowired
	private UserInfoService userInfoService;
	@Autowired
	private LoginInfoService loginInfoService;

	@Override
	public List<UserFile> listByUser(Long id, boolean b) {
		return repository.findAll(new Specification<UserFile>() {
			@Override
			public Predicate toPredicate(Root<UserFile> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<>();
				list.add(cb.equal(root.get("applier").as(LoginInfo.class), loginInfoService.getLoginInfoById(id)));
				if (b) {
					list.add(cb.isNotNull(root.get("fileType").as(SystemDictionaryItem.class)));
				} else {
					list.add(cb.isNull(root.get("fileType").as(SystemDictionaryItem.class)));
				}
				Predicate[] ps = new Predicate[list.size()];
				return cb.and(list.toArray(ps));
			}
		});
	}

	@Override
	@Transactional(rollbackFor = { RuntimeException.class })
	public void apply(String fileName) {
		UserFile userFile = new UserFile();
		userFile.setApplier(HttpServletContext.getCurrentLoginInfo());
		userFile.setApplyTime(new Date());
		userFile.setFile(fileName);
		userFile.setState(UserFile.AUTH_NORMAL);
		repository.saveAndFlush(userFile);
	}

	@Override
	@Transactional(rollbackFor = { LogicException.class })
	public void updateType(Long[] id, Long[] fileType) {
		Integer idLen = id.length;
		Integer typeLen = fileType.length;
		if (idLen > 0 && idLen.equals(typeLen)) {
			for (int i = 0; i < idLen; i++) {
				UserFile userFile = repository.findOne(id[i]);
				userFile.setFileType(systemDictionaryItemService.get(fileType[i]));
				UserFile file = repository.saveAndFlush(userFile);
				if (file == null) {
					throw new LogicException("修改异常");
				}
			}
		}
	}

	@Override
	public Page<UserFile> page(UserFileQo qo) {
		Page<UserFile> page = repository.findAll(new Specification<UserFile>() {
			@Override
			public Predicate toPredicate(Root<UserFile> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
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
				if (qo.getLoginInfoId() != null) {
					list.add(cb.equal(root.get("applier").as(LoginInfo.class),
							loginInfoService.getLoginInfoById((qo.getLoginInfoId()))));
				}
				Predicate[] ps = new Predicate[list.size()];
				return cb.and(list.toArray(ps));
			}
		}, new PageRequest(qo.getCurrentPage() - 1, qo.getPageSize(), Direction.DESC, "applyTime"));
		return page;
	}

	@Override
	@Transactional(rollbackFor = { RuntimeException.class })
	public void audit(Long id, Integer state, Integer score, String remark) {
		UserFile userFile = repository.findOne(id);
		if (userFile.getState().equals(UserFile.AUTH_NORMAL)) {
			userFile.setAuditor(HttpServletContext.getCurrentLoginInfo());
			userFile.setAuditTime(new Date());
			userFile.setRemark(remark);
			userFile.setState(state);
			if (state.equals(UserFile.AUTH_PASS)) {
				userFile.setScore(score);
				UserInfo info = userInfoService.get(userFile.getApplier().getId());
				info.setAuthScore(info.getAuthScore() + score);
				userInfoService.update(info);
			}
			repository.saveAndFlush(userFile);
		}
	}

}
