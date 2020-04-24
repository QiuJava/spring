package com.example.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

import com.example.config.listener.ContextStartListener;
import com.example.entity.DataDictionary;
import com.example.mapper.DataDictionaryMapper;

/**
 * 数据字典服务
 * 
 * @author Qiu Jian
 *
 */
@Service
public class DataDictionaryServiceImpl {

	@Autowired
	private DataDictionaryMapper dataDictionaryMapper;

	@Autowired
	private HashOperations<String, String, Object> hashOperation;

	public List<DataDictionary> listByDataKey(String employeeType) {
		Map<String, Object> entries = hashOperation.entries(ContextStartListener.DATA_DICTIONARY_LIST);
		Set<String> keySet = entries.keySet();
		List<DataDictionary> list = new ArrayList<>();
		keySet.forEach(key -> {
			if (key.contains(employeeType)) {
				list.add((DataDictionary) entries.get(key));
			}

		});
		return list;
	}

	public List<DataDictionary> listAll() {
		return dataDictionaryMapper.listAll();
	}

	public void settingDataDictionaryList() {
		hashOperation.getOperations().delete(ContextStartListener.DATA_DICTIONARY_LIST);
		List<DataDictionary> list = this.listAll();
		list.forEach(dataDictionary -> hashOperation.put(ContextStartListener.DATA_DICTIONARY_LIST,
				dataDictionary.getDataKey().concat(dataDictionary.getDataValue()), dataDictionary));

	}

}
