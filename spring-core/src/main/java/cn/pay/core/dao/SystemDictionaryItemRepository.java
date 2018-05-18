package cn.pay.core.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.pay.core.domain.sys.SystemDictionaryItem;

public interface SystemDictionaryItemRepository
		extends JpaRepository<SystemDictionaryItem, Long>, JpaSpecificationExecutor<SystemDictionaryItem> {

	/**
	 * 查询这个字典下的所有的字典明细
	 * 
	 * @param id
	 * @return
	 */
	List<SystemDictionaryItem> findBySystemDictionaryId(Long id);

}
