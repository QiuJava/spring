package cn.eeepay.framework.service;

import java.util.List;

import cn.eeepay.framework.model.AuditorManager;

public interface AuditorManagerService {

	List<AuditorManager> findAllInfo(String bpId);
}
