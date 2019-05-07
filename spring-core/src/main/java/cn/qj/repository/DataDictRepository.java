package cn.qj.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.qj.entity.DataDict;

/**
 * 字典数据操作
 * 
 * @author Qiujian
 * @date 2019年3月26日
 *
 */
public interface DataDictRepository extends JpaRepository<DataDict, Long> {

	/**
	 * 根据字典key 查询数据字典
	 * 
	 * @param dictKey
	 * @return
	 */
	DataDict findByDictKey(String dictKey);

}
