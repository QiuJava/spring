package cn.eeepay.framework.service.bill;

public interface TaskService {

    int syncOutBillDetailStatus();

    void autoYinShenZhiQingDuiZhang();

    boolean autoDownLoadYsFile();

}
