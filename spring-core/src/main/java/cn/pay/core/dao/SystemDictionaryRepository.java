package cn.pay.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.pay.core.domain.sys.SystemDictionary;

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
