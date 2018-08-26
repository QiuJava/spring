package cn.pay.core.service;

import java.util.List;

import cn.pay.core.domain.sys.SystemDictionary;
import cn.pay.core.pojo.qo.SystemDictionaryQo;
import cn.pay.core.pojo.vo.PageResult;

/**
 * 系统字典服务
 * 
 * @author Administrator
 *
 */
public interface SystemDictionaryService {

	/**
	 * 根据ID删除
	 * 
	 * @param id
	 */
	void deleteById(Long id);

	/**
	 * 保存系统字典
	 * 
	 * @param systemDictionary
	 * @return
	 */
	SystemDictionary saveSystemDictionary(SystemDictionary systemDictionary);

	/**
	 * 系统字典分页查询
	 * 
	 * @param qo
	 * @return
	 */
	PageResult pageQuerySystemDictionary(SystemDictionaryQo qo);

	/**
	 * 获取字典
	 * 
	 * @return
	 */
	List<SystemDictionary> listAll();

	/**
	 * 根据编码 获取字典
	 * 
	 * @param sn
	 * @return
	 */
	SystemDictionary getSystemDictionaryBySn(String sn);

	/**
	 * 更新系统字典
	 * 
	 * @return
	 */
	SystemDictionary updateSystemDictionary(SystemDictionary systemDictionary);

}
