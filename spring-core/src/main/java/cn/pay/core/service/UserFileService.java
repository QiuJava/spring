package cn.pay.core.service;

import java.util.List;

import org.springframework.data.domain.Page;

import cn.pay.core.domain.business.UserFile;
import cn.pay.core.pojo.qo.UserFileQo;

/**
 * 用户风控材料
 * 
 * @author Administrator
 *
 */
public interface UserFileService {

	/**
	 * 风控材料上传申请
	 * 
	 * @param fileName
	 */
	void apply(String fileName);

	/**
	 * 批量更新用户材料的类型
	 * 
	 * @param id
	 * @param fileType
	 */
	void updateType(Long[] id, Long[] fileType);

	/**
	 * 根据用户材料查询对象获取用户材料页面结果集
	 * 
	 * @param qo
	 * @return
	 */
	Page<UserFile> page(UserFileQo qo);

	/**
	 * 风控材料认证审核给分
	 * 
	 * @param id
	 * @param state
	 * @param score
	 * @param remark
	 */
	void audit(Long id, int state, int score, String remark);

	/**
	 * 获取用户的风控材料列表
	 * 
	 * @param id
	 * @param b
	 * @return
	 */
	List<UserFile> listByUser(Long id, boolean b);

}
