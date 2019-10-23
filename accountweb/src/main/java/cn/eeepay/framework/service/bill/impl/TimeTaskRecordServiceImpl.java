package cn.eeepay.framework.service.bill.impl;

import cn.eeepay.framework.dao.bill.TimeTaskRecordMapper;
import cn.eeepay.framework.model.bill.TimeTaskRecord;
import cn.eeepay.framework.service.bill.TimeTaskRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: 长沙张学友
 * Date: 2018/11/6
 * Time: 11:20
 * Description: 类注释
 */
@Service("timeTaskRecordService")
public class TimeTaskRecordServiceImpl implements TimeTaskRecordService{

    @Autowired
    private TimeTaskRecordMapper timeTaskRecordMapper;

    @Override
    public List<TimeTaskRecord> findByRunningNo(String runningNo) {
        return timeTaskRecordMapper.findByRunningNo(runningNo);
    }

    @Override
    public void add(TimeTaskRecord timeTaskRecord) {
        timeTaskRecordMapper.add(timeTaskRecord);
    }

    @Override
    public void updateByRunningNoAndStatus(TimeTaskRecord timeTaskRecord,String status) {
        timeTaskRecordMapper.updateByRunningNoAndStatus(timeTaskRecord,status);
    }
}
