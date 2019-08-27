package cn.loan.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.loan.core.common.LogicException;
import cn.loan.core.common.PageResult;
import cn.loan.core.config.cache.SystemDictionaryHashService;
import cn.loan.core.entity.SystemDictionary;
import cn.loan.core.entity.SystemDictionaryItem;
import cn.loan.core.entity.qo.SystemDictionaryItemQo;
import cn.loan.core.repository.SystemDictionaryItemDao;
import cn.loan.core.repository.specification.SystemDictionaryItemSpecification;
import cn.loan.core.util.StringUtil;
import cn.loan.core.util.SystemDictionaryUtil;

/**
 * 字典明细服务
 * 
 * @author qiujian
 *
 */
@Service
public class SystemDictionaryItemService {

	@Autowired
	private SystemDictionaryItemDao systemDictionaryItemDao;

	@Autowired
	private SystemDictionaryService systemDictionaryService;

	@Autowired
	private SystemDictionaryHashService systemDictionaryHashService;

	public PageResult pageQuery(SystemDictionaryItemQo qo) {
		Page<SystemDictionaryItem> page = systemDictionaryItemDao.findAll(
				Specifications.where(SystemDictionaryItemSpecification.orKeyword(qo.getKeyword()))
						.and(SystemDictionaryItemSpecification.equalSystemDictionaryId(qo.getSystemDictionaryId())),
				new PageRequest(qo.getPage(), qo.getSize(), Direction.ASC, StringUtil.SEQUENCE));
		return new PageResult(page.getContent(), page.getTotalPages(), qo.getCurrentPage());
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public void save(SystemDictionaryItem item) {
		String itemKey = item.getItemKey();
		Long id = item.getId();
		if (id != null) {
			SystemDictionaryItem dictionaryItem = systemDictionaryItemDao.findOne(id);
			dictionaryItem.setItemKey(itemKey);
			dictionaryItem.setItemName(item.getItemName());
			dictionaryItem.setItemValue(item.getItemValue());
			dictionaryItem.setSequence(item.getSequence());
			if (!dictionaryItem.getItemKey().equals(itemKey)) {
				this.itemKeyUniquenessCheck(itemKey, item.getSystemDictionary());
			}
			systemDictionaryItemDao.save(dictionaryItem);
		} else {
			this.itemKeyUniquenessCheck(itemKey, item.getSystemDictionary());
			systemDictionaryItemDao.save(item);
		}
		systemDictionaryService.updateHash();
	}

	private void itemKeyUniquenessCheck(String itemKey, SystemDictionary dict) {
		Long count = systemDictionaryItemDao.countByItemKeyAndSystemDictionary(itemKey, dict);
		// 保证Key的唯一性
		if (count > 0) {
			throw new LogicException("条目键:" + itemKey + "已存在");
		}
	}

	@Transactional(rollbackFor = RuntimeException.class)
	public void delete(Long id) {
		systemDictionaryItemDao.delete(id);
		systemDictionaryService.updateHash();
	}

	public List<SystemDictionaryItem> list(String dictKey) {
		return SystemDictionaryUtil.getItems(dictKey, systemDictionaryHashService);
	}

	public SystemDictionaryItem get(Long id) {
		return systemDictionaryItemDao.getOne(id);
	}

}
