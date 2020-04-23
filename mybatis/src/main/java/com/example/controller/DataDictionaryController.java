package com.example.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.entity.DataDictionary;
import com.example.service.DataDictionaryServiceImpl;

import lombok.extern.slf4j.Slf4j;

/**
 * 字典条目控制器
 * 
 * @author Qiu Jian
 *
 */
@Controller
@Slf4j
public class DataDictionaryController {

	@Autowired
	private DataDictionaryServiceImpl dataDictionaryService;

	@GetMapping("/dataDictionary/listByDataKey")
	@ResponseBody
	public List<DataDictionary> listByDataKey(String dataKey) {
		try {
			List<DataDictionary> list = dataDictionaryService.listByDataKey(dataKey);
			return list;
		} catch (Exception e) {
			log.error("系统异常", e);
			return new ArrayList<>();
		}
	}
}
