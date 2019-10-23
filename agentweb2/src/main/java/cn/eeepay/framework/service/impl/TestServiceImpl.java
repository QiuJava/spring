package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.TestReadDao;
import cn.eeepay.framework.dao.TestWriteDao;
import cn.eeepay.framework.service.TestService;
import cn.eeepay.framework.util.ReadOnlyDataSource;
import cn.eeepay.framework.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by 666666 on 2017/12/29.
 */
@Service
public class TestServiceImpl implements TestService{
    @Resource
    private TestReadDao testReadDao;
    @Resource
    private TestWriteDao testWriteDao;

    @Override
    public String queryRead(String id) {
        return testReadDao.query(id);
    }

    @Override
    public boolean insertRead(String val) {
        testReadDao.insert(val);
        return true;
    }

    @Override
    public boolean insertWrite(String val) {
        testWriteDao.insert(val);
        return true;
    }

    @Override
    public String queryWrite(String id) {
        return testWriteDao.query(id);
    }

    @Override
    public boolean queryReadInsertWrite(String id) {
        String query = testReadDao.query(id);
        if (StringUtils.isBlank(query)){
            testWriteDao.insert("not found" + id);
        }else{
            testWriteDao.insert(query + query);
        }
        return true;
    }

    @Override
    public boolean queryWriteInsertRead(String id) {
        String query = testWriteDao.query(id);
        if (StringUtils.isBlank(query)){
            testReadDao.insert("not found" + id);
        }else{
            testReadDao.insert(query + query);
        }
        return true;
    }

    @Override
    public boolean insertWriteInsertRead(String val) {
        testWriteDao.insert("write " +val);
        testReadDao.insert("read " + val);
        return true;
    }
}
