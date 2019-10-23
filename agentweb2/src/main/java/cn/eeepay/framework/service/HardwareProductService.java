package cn.eeepay.framework.service;

import java.util.List;

import cn.eeepay.framework.model.HardwareProduct;

public interface HardwareProductService {

	    public List<HardwareProduct> selectAllInfo(String agentNo,String agentOem);

	    List<HardwareProduct> selectAllInfoByPn(String agentNo,String agentOem);

		public HardwareProduct selectHardwareName(String hardWareId);

		public List<HardwareProduct> selectAllHardwareName();
}
