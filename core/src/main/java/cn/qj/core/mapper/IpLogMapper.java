package cn.qj.core.mapper;

import cn.qj.core.entity.IpLog;

public interface IpLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(IpLog record);

    int insertSelective(IpLog record);

    IpLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(IpLog record);

    int updateByPrimaryKey(IpLog record);
}