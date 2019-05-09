package cn.qj.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.qj.entity.Dict;

/**
 * 字典数据操作
 * 
 * @author Qiujian
 * @date 2019年3月26日
 *
 */
public interface DictRepository extends JpaRepository<Dict, Long> {

}
