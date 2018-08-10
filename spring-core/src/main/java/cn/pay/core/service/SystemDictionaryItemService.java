package cn.pay.core.service;

import java.util.List;

import org.springframework.data.domain.Page;

import cn.pay.core.domain.sys.SystemDictionaryItem;
import cn.pay.core.pojo.qo.SystemDictionaryQo;

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

	/**
	 * 根据ID删除
	 * 
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 根据字典明细获取字典ID
	 * 
	 * @param itemId
	 * @return
	 */
	Long getSystemDictionaryId(Long itemId);

	/**
	 * 更新
	 * 
	 * @param item
	 */
	void update(SystemDictionaryItem item);

	/**
	 * 获取字典明细列表用于页面分页查询
	 * 
	 * @param qo
	 * @return
	 */
	Page<SystemDictionaryItem> page(SystemDictionaryQo qo);

	/**
	 * 根据id获取单个对象
	 * 
	 * @param id
	 * @return
	 */
	SystemDictionaryItem get(Long id);

}
