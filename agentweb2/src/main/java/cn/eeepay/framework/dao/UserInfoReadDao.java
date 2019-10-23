package cn.eeepay.framework.dao;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.UserInfo;
import cn.eeepay.framework.util.ReadOnlyDataSource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@ReadOnlyDataSource
public interface UserInfoReadDao {
	/**
	 * 超级管理员查询用户列表
	 */
	List<UserInfo> findUserInfoListByAdmin(@Param("user") UserInfo user,
										   @Param("sort") String sort, Page<UserInfo> page);

	/**
	 * 代理商查询用户列表
	 */
	List<UserInfo> findUserInfoListByAgent(@Param("user") UserInfo user,
										   @Param("teamId") Integer teamId,
										   @Param("sort") String sort,
										   @Param("loginAgentNode") String loginAgentNode, Page<UserInfo> page);
}
