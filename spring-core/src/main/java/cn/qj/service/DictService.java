package cn.qj.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.qj.entity.Dict;
import cn.qj.repository.DictRepository;

/**
 * 字典服务
 * 
 * @author Qiujian
 * @date 2019年5月14日
 *
 */
@Service
public class DictService {

	@Autowired
	private DictRepository dictRepository;

	public List<Dict> getAll() {
		return dictRepository.findAll();
	}

}
