package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.PaChangeLogDao;
import cn.eeepay.framework.model.peragent.PaChangeLog;
import cn.eeepay.framework.service.PaChangeLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author MXG
 * create 2018/11/19
 */
@Service
public class PaChangeLogServiceImpl implements PaChangeLogService{

    @Resource
    private PaChangeLogDao paChangeLogDao;

    @Override
    public int insert(PaChangeLog log) {
        return paChangeLogDao.insert(log);
    }
}
