package cn.qj.key.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.qj.key.entity.Dict;

/**
 * 字典数据操作
 * 
 * @author Qiujian
 * @date 2019/01/29
 */
public interface DictDao extends JpaRepository<Dict, Long> {

	/**
	 * 字典名称全模糊查找字典
	 * 
	 * @param content
	 * @return
	 */
	Dict findByDictNameContaining(String content);

}
