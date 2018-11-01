package cn.qj.core.service;

import java.util.List;

import org.springframework.data.domain.Page;

import cn.qj.core.entity.UserFile;
import cn.qj.core.pojo.qo.UserFileQo;

/**
 * 用户材料服务
 * 
 * @author Qiujian
 * @date 2018/11/01
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
	void audit(Long id, Integer state, Integer score, String remark);

	/**
	 * 获取用户的风控材料列表
	 * 
	 * @param id
	 * @param b
	 * @return
	 */
	List<UserFile> listByUser(Long id, boolean b);

}
