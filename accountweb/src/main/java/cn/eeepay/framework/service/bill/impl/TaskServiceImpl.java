package cn.eeepay.framework.service.bill.impl;

import cn.eeepay.framework.model.bill.SysDict;
import cn.eeepay.framework.service.bill.DuiAccountDetailService;
import cn.eeepay.framework.service.bill.OutBillDetailService;
import cn.eeepay.framework.service.bill.SysDictService;
import cn.eeepay.framework.service.bill.TaskService;
import cn.eeepay.framework.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service("taskService")
public class TaskServiceImpl implements TaskService {

    private static final Logger log = LoggerFactory.getLogger(TaskServiceImpl.class);

    @Resource
    private OutBillDetailService outBillDetailService;
    @Resource
    public DuiAccountDetailService duiAccountDetailService;
    @Resource
    public SysDictService sysDictService;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int syncOutBillDetailStatus() {
        return outBillDetailService.syncOutBillDetailStatus();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void autoYinShenZhiQingDuiZhang() {
        String tom = DateUtil.subDay(new Date(),1);
        String fileName="ACC"+tom+"CHK";
        String acqName="YS_ZQ";//YS_ZQ新银盛
//        duiAccountDetailService.duiAccountFileUpload(fileName,acqName);
    }

    @Override
    public boolean autoDownLoadYsFile() {
        SysDict sysDict = null;
        try {
            sysDict = sysDictService.findSysDuiAccountGroup("sys_yszq_account_status");
        } catch (Exception e) {
            log.error("异常:", e);
        }
        if(sysDict == null) {
            log.info("查询 sys_yszq_account_status 字典为空，直接返回");
            throw new RuntimeException("在数据字典中为查找到sys_yszq_account_status");
        }
        if("1".equals(sysDict.getSysValue())){
            String tom = DateUtil.subDay(new Date(),1);
            String fileName="ACC"+tom+"CHK";
            String acqName="YS_ZQ";//YS_ZQ新银盛
            Map<String, String> map=new HashMap<String, String>();
            map.put("fileName", fileName);
            map.put("acqOrg", acqName);
            boolean flag = duiAccountDetailService.duiAccountFileDown(map);
            if(flag){
                log.info(acqName+"对账文件下载成功，开始对账~");
            }else{
                log.info(acqName+"对账文件未下载成功！");
            }
            return flag;
        }else{
            log.info("sys_yszq_account_status 定时对账开关被关闭");
        }
        return false;
    }
}
