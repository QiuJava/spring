package cn.eeepay.framework.service;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.model.AreaInfo;
import cn.eeepay.framework.model.JoinTable;

/**
 * 地区信息service
 * 
 * @author junhu
 *
 */
public interface AreaInfoService {
	/**
	 * 获取所有省份
	 * 
	 * @return
	 */
	List<AreaInfo> provinceSelectBox();

	/**
	 * 根据省份查询该省份下的所有地级市
	 * 
	 * @param province
	 * @return
	 */
	List<AreaInfo> citySelectBox(String province);

	List<Map<String,Object>> getItemByParentId(Integer pid);

	Map<String, Object> getAreaByNames(String province, String city, String area);

	List<Map<String, Object>> getAreaByName(String type, String name);
}
