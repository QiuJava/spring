package cn.qj.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.qj.entity.Menu;

/**
 * 菜单数据操作
 * 
 * @author Qiujian
 * @date 2019年3月26日
 *
 */
public interface MenuRepository extends JpaRepository<Menu, Long> {

}
