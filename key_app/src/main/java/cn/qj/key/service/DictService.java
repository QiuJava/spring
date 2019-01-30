package cn.qj.key.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.qj.key.entity.Dict;
import cn.qj.key.repository.DictDao;

/**
 * 字典服务
 * 
 * @author Qiujian
 * @date 2019/01/29
 */
@Service
public class DictService {

	@Autowired
	private DictDao dao;
	
	public void save(Dict dict) {
		dao.save(dict);
	}

	public String getByDictName(String content) {
		Dict dict = dao.findByDictNameContaining(content);
		if (dict != null) {
			return dict.getDictValue();
		}
		return "";
	}

}
