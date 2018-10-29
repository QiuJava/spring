package cn.pay.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.pay.core.entity.business.UserFile;

/**
 * 用户文件持久化
 * 
 * @author Qiujian
 * @date 2018年8月10日
 */
public interface UserFileRepository extends JpaRepository<UserFile, Long>, JpaSpecificationExecutor<UserFile> {

}
