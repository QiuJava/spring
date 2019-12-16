package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.DictEntry;
import com.example.mapper.DictEntryMapper;

/**
 * 字典条目服务
 * 
 * @author Qiu Jian
 *
 */
@Service
public class DictEntryServiceImpl {
	
	@Autowired
	private DictEntryMapper dictEntryMapper;

	public List<DictEntry> listByDictKey(String dictKey) {
		return dictEntryMapper.selectByDictKey(dictKey);
	}

}
