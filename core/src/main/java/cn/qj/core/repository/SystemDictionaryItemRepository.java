package cn.qj.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.qj.core.entity.SystemDictionaryItem;

/**
 * 系统字典明细相关持久化
 * 
 * @author Qiujian
 * @date 2018/8/10
 */
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
