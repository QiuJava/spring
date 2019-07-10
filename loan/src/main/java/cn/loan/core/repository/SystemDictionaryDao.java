package cn.loan.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.loan.core.entity.SystemDictionary;

/**
 * 系统字典数据操作
 * 
 * @author Qiujian
 * 
 */
public interface SystemDictionaryDao
		extends JpaRepository<SystemDictionary, Long>, JpaSpecificationExecutor<SystemDictionary> {

	/**
	 * 根据字典键查询字典
	 * 
	 * @param dictKey
	 * @return
	 */
	SystemDictionary findByDictKey(String dictKey);

	/**
	 * 根据字典键统计字典
	 * 
	 * @param dictKey
	 * @return
	 */
	Long countByDictKey(String dictKey);

	/**
	 * 获取所有字典根据序号排序
	 * 
	 * @return
	 */
	List<SystemDictionary> findByOrderBySequence();

}
