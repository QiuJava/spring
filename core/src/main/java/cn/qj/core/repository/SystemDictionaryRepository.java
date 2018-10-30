package cn.qj.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.qj.core.entity.SystemDictionary;

/**
 * 系统字典相关持久化
 * 
 * @author Qiujian
 * @date 2018年8月10日
 */
public interface SystemDictionaryRepository
		extends JpaRepository<SystemDictionary, Long>, JpaSpecificationExecutor<SystemDictionary> {

	/**
	 * 根据字典编码查询字典
	 * 
	 * @param sn
	 * @return
	 */
	SystemDictionary findBySn(String sn);

}
