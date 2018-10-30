package cn.qj.core.service;

import java.util.List;

import cn.qj.core.entity.SystemDictionaryItem;
import cn.qj.core.pojo.qo.SystemDictionaryItemQo;
import cn.qj.core.pojo.vo.PageResult;

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
	void deleteById(Long id);

	/**
	 * 更新
	 * 
	 * @param item
	 */
	void update(SystemDictionaryItem item);

	/**
	 * 字典明细分页查询
	 * 
	 * @param qo
	 * @return
	 */
	PageResult pageQuery(SystemDictionaryItemQo qo);

	/**
	 * 根据id获取单个对象
	 * 
	 * @param id
	 * @return
	 */
	SystemDictionaryItem get(Long id);

}
