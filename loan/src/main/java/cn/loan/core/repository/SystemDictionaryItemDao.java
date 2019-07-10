package cn.loan.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.loan.core.entity.SystemDictionary;
import cn.loan.core.entity.SystemDictionaryItem;

/**
 * 系统字典条目数据操作
 * 
 * @author Qiujian
 * 
 */
public interface SystemDictionaryItemDao
		extends JpaRepository<SystemDictionaryItem, Long>, JpaSpecificationExecutor<SystemDictionaryItem> {

	/**
	 * 根据条目键和所属字典统计条目
	 * 
	 * @param itemKey
	 * @param systemDictionary
	 * @return
	 */
	Long countByItemKeyAndSystemDictionary(String itemKey, SystemDictionary systemDictionary);

}
