package cn.eeepay.framework.service.bill;

import cn.eeepay.framework.model.bill.TimeTaskRecord;

import java.util.List;

public interface TimeTaskRecordService {

    public List<TimeTaskRecord> findByRunningNo(String runningNo);

    public void add(TimeTaskRecord timeTaskRecord);

    public void updateByRunningNoAndStatus(TimeTaskRecord timeTaskRecord,String status);

}
