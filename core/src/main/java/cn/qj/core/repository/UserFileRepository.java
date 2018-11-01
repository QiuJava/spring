package cn.qj.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.qj.core.entity.UserFile;

/**
 * 用户文件持久化
 * 
 * @author Qiujian
 * @date 2018/8/10
 */
public interface UserFileRepository extends JpaRepository<UserFile, Long>, JpaSpecificationExecutor<UserFile> {

}
