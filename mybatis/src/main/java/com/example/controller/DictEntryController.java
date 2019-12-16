package com.example.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.entity.DictEntry;
import com.example.service.DictEntryServiceImpl;

import lombok.extern.slf4j.Slf4j;

/**
 * 字典条目控制器
 * 
 * @author Qiu Jian
 *
 */
@Controller
@Slf4j
public class DictEntryController {

	@Autowired
	private DictEntryServiceImpl dictEntryService;

	@GetMapping("/dictEntry/employeeTypeList")
	@ResponseBody
	public List<DictEntry> getEmployeeTypeList() {
		try {
			List<DictEntry> list = dictEntryService.listByDictKey(DictEntry.EMPLOYEE_TYPE);
			return list;
		} catch (Exception e) {
			log.error("系统异常", e);
			return new ArrayList<>();
		}
	}
}
