package cn.pay.core.service;

import java.util.List;

import org.springframework.data.domain.Page;

import cn.pay.core.domain.sys.SystemDictionary;
import cn.pay.core.obj.qo.SystemDictionaryQo;

/**
 * 系统字典
 * 
 * @author Administrator
 *
 */
public interface SystemDictionaryService {

	void delete(Long id);

	void save(SystemDictionary systemDictionary);

	/**
	 * 获取字典列表用于页面分页查询
	 * 
	 * @param qo
	 * @return
	 */
	Page<SystemDictionary> page(SystemDictionaryQo qo);

	/**
	 * 获取所有的字典
	 * 
	 * @return
	 */
	List<SystemDictionary> list();

	/**
	 * 根据编码 获取列表
	 * 
	 * @param sn
	 * @return
	 */
	SystemDictionary listBySn(String sn);

}
