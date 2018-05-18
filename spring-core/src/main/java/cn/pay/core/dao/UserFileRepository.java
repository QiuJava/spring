package cn.pay.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.pay.core.domain.business.UserFile;

public interface UserFileRepository extends JpaRepository<UserFile, Long>, JpaSpecificationExecutor<UserFile> {

}
