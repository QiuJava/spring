package cn.pay.core.service;

import java.util.List;

import org.springframework.data.domain.Page;

import cn.pay.core.domain.sys.SystemDictionary;
import cn.pay.core.pojo.qo.SystemDictionaryQo;

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
	void delete(Long id);

	/**
	 * 保存
	 * 
	 * @param systemDictionary
	 * @return
	 */
	SystemDictionary save(SystemDictionary systemDictionary);

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
