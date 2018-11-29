package cn.qj.core.mapper;

import cn.qj.core.entity.IpLog;

/**
 * 登录日志数据操作
 * 
 * @author Qiujian
 * @date 2018/11/29
 */
public interface IpLogMapper {
	/**
	 * 根据主键删除
	 * 
	 * @param id
	 * @return
	 */
	int deleteByPrimaryKey(Long id);

	/**
	 * 插入一条记
	 * 
	 * @param record
	 * @return
	 */
	int insert(IpLog record);

	/**
	 * 插入部分列的记录
	 * 
	 * @param record
	 * @return
	 */
	int insertSelective(IpLog record);

	/**
	 * 根据主键查询一条记录
	 * 
	 * @param id
	 * @return
	 */
	IpLog selectByPrimaryKey(Long id);

	/**
	 * 根据主键更新不部分列
	 * 
	 * @param record
	 * @return
	 */
	int updateByPrimaryKeySelective(IpLog record);

	/**
	 * 根据主键更新
	 * 
	 * @param record
	 * @return
	 */
	int updateByPrimaryKey(IpLog record);
}