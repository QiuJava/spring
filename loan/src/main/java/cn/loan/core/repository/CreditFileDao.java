package cn.loan.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.loan.core.entity.CreditFile;
import cn.loan.core.entity.LoginUser;

/**
 * 信用材料数据操作
 * 
 * @author qiujian
 *
 */
public interface CreditFileDao extends JpaRepository<CreditFile, Long>, JpaSpecificationExecutor<CreditFile> {

	/**
	 * 根据提交者查找信用材料 且材料类型不为空
	 * 
	 * @param currentUser
	 * @return
	 */
	List<CreditFile> findBySubmitterAndFileTypeIsNotNull(LoginUser currentUser);

	/**
	 * 根据提交者查找信用材料 且材料类型为空
	 * 
	 * @param currentUser
	 * @return
	 */
	List<CreditFile> findBySubmitterAndFileTypeIsNull(LoginUser currentUser);

}
