package cn.qj.key.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.qj.key.config.datasource.DataSourceKey;
import cn.qj.key.config.datasource.DataSourceUtil;
import cn.qj.key.entity.SysUser;
import cn.qj.key.mapper.SysUserMapper;

/**
 * 用户服务
 * 
 * @author Qiujian
 * @date 2018/12/17
 */
@Service
public class SysUserService {
	
	@Autowired
	private SysUserMapper sysUserMapper;

	@Transactional
	public int save(SysUser sysUser) {
		return sysUserMapper.insert(sysUser);
	}

	@DataSourceKey(DataSourceUtil.READ_ONE_KEY)
	public SysUser getById(Long id) {
		return sysUserMapper.selectByPrimaryKey(id);
	}

}
