package cn.pay.core.service;

import java.util.List;

import org.springframework.data.domain.Page;

import cn.pay.core.domain.sys.SystemDictionaryItem;
import cn.pay.core.obj.qo.SystemDictionaryQo;

/**
 * 系统字典明细
 * 
 * @author Administrator
 *
 */
public interface SystemDictionaryItemService {

	/**
	 * 根据字典编码获取字典明细
	 * 
	 * @param string
	 * @return
	 */
	List<SystemDictionaryItem> getBySn(String string);

	void delete(Long id);

	/**
	 * 根据字典明细获取字典ID
	 * 
	 * @param itemId
	 * @return
	 */
	Long getSystemDictionaryId(Long itemId);

	void update(SystemDictionaryItem item);

	/**
	 * 获取字典明细列表用于页面分页查询
	 * 
	 * @param qo
	 * @return
	 */
	Page<SystemDictionaryItem> page(SystemDictionaryQo qo);

	/**
	 * 获取字典明细
	 * 
	 * @param long1
	 * @return
	 */
	SystemDictionaryItem get(Long id);

}
