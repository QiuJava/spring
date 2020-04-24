package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Position;
import com.example.mapper.PositionMapper;

/**
 * 职位服务
 * 
 * @author Qiu Jian
 *
 */
@Service
public class PositionServiceImpl {

	@Autowired
	private PositionMapper positionMapper;

	public List<Position> listByDepartmentId(Integer departmentId) {
		return positionMapper.listByDepartmentId(departmentId);
	}

}
