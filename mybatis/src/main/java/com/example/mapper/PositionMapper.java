package com.example.mapper;

import java.util.List;

import com.example.entity.Position;

public interface PositionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Position record);

    int insertSelective(Position record);

    Position selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Position record);

    int updateByPrimaryKey(Position record);

	List<Position> listByDepartmentId(Integer departmentId);
}