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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.loan.core.common.LogicException;
import cn.loan.core.common.PageResult;
import cn.loan.core.config.cache.SystemDictionaryHashService;
import cn.loan.core.config.datasource.DataSourceKey;
import cn.loan.core.config.datasource.DataSourceUtil;
import cn.loan.core.entity.SystemDictionary;
import cn.loan.core.entity.SystemDictionaryItem;
import cn.loan.core.entity.qo.SystemDictionaryQo;
import cn.loan.core.repository.SystemDictionaryDao;
import cn.loan.core.util.StringUtil;

/**
 * 系统字典服务
 * 
 * @author Qiujian
 *
 */
@Service
public class SystemDictionaryService {

	@Autowired
	private SystemDictionaryDao systemDictionaryDao;

	@Autowired
	private SystemDictionaryHashService systemDictionaryHashService;

	public PageResult pageQuery(SystemDictionaryQo qo) {
		Page<SystemDictionary> page = systemDictionaryDao.findAll(new Specification<SystemDictionary>() {
			@Override
			public Predicate toPredicate(Root<SystemDictionary> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<>();
				String dictKey = qo.getDictKey();
				if (StringUtil.hasLength(dictKey)) {
					Predicate dictKeyLike = cb.like(root.get(StringUtil.DICT_KEY), dictKey + StringUtil.PER_CENT);
					predicateList.add(dictKeyLike);
				}
				String dictName = qo.getDictName();
				if (StringUtil.hasLength(dictName)) {
					Predicate dictNameLike = cb.like(root.get(StringUtil.DICT_NAME), dictName + StringUtil.PER_CENT);
					predicateList.add(dictNameLike);
				}
				Predicate[] ps = new Predicate[predicateList.size()];
				return cb.and(predicateList.toArray(ps));
			}
		}, new PageRequest(qo.getPage(), qo.getSize(), Direction.ASC, StringUtil.SEQUENCE));

		return new PageResult(page.getContent(), page.getTotalPages(), qo.getCurrentPage());
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public void save(SystemDictionary sd) {
		// 保证Key的唯一性
		Long id = sd.getId();
		if (id != null) {
			SystemDictionary dictionary = systemDictionaryDao.findOne(id);
			dictionary.setDictKey(sd.getDictKey());
			dictionary.setDictName(sd.getDictName());
			dictionary.setDictValue(sd.getDictValue());
			dictionary.setSequence(sd.getSequence());
			// 不是原键
			if (!dictionary.getDictKey().equals(sd.getDictKey())) {
				this.dictKeyUniquenessCheck(sd.getDictKey());
			}
			systemDictionaryDao.save(dictionary);
		} else {
			this.dictKeyUniquenessCheck(sd.getDictKey());
			systemDictionaryDao.save(sd);
		}
		this.updateHash();
	}

	private void dictKeyUniquenessCheck(String dictKey) {
		Long count = systemDictionaryDao.countByDictKey(dictKey);
		if (count > 0) {
			throw new LogicException("字典键:" + dictKey + "已存在");
		}
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public void delete(SystemDictionary systemDictionary) {
		systemDictionaryDao.delete(systemDictionary.getId());
		this.updateHash();
	}

	@DataSourceKey(DataSourceUtil.READ_ONE_KEY)
	public List<SystemDictionary> getAll() {
		return systemDictionaryDao.findByOrderBySequence();
	}
	

	public void updateHash() {
		// 获取缓存中所有的数据字典条目
		List<SystemDictionary> systemDictionarys = systemDictionaryHashService.getAll();
		if (systemDictionarys.size() > 0) {
			systemDictionaryHashService.empty();
		}
		List<SystemDictionary> dicts = this.getAll();
		for (SystemDictionary systemDictionary : dicts) {
			List<SystemDictionaryItem> items = systemDictionary.getSystemDictionaryItems();
			if (items != null) {
				// 加载
				items.toString();
			}
			systemDictionaryHashService.put(systemDictionary.getDictKey(), systemDictionary, -1);
		}
	}

}
