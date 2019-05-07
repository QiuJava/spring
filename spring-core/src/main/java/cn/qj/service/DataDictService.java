package cn.qj.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.qj.entity.DataDict;
import cn.qj.repository.DataDictRepository;

/**
 * 字典服务
 * 
 * @author Qiujian
 * @date 2019年3月26日
 *
 */
@Service
public class DataDictService {

	@Autowired
	private DataDictRepository dataDictRepository;

	public Integer getValueByDictkey(String dictKey) {
		return Integer.valueOf(getDictValueByDictkey(dictKey));
	}

	public String getDictValueByDictkey(String dictKey) {
		DataDict dict = dataDictRepository.findByDictKey(dictKey);
		return dict.getDictValue();
	}

	public List<DataDict> getAll() {
		return dataDictRepository.findAll();
	}

}
