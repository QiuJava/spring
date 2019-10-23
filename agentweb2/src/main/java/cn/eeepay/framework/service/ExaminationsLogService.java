package cn.eeepay.framework.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.eeepay.framework.model.ExaminationsLog;

public interface ExaminationsLogService {

	 public int insert(ExaminationsLog record);

	 public List<ExaminationsLog> selectByMerchantId(String merchantId);
}
