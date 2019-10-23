package cn.eeepay.framework.service;

import java.util.Map;

public interface SysWarningService {

    Map getByType(String type);

    int updateSysWarning(Map<String,Object> map);
}
