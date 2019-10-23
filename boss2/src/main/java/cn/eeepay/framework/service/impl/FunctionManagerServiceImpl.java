package cn.eeepay.framework.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.FunctionManagerDao;
import cn.eeepay.framework.model.FunctionManager;
import cn.eeepay.framework.model.IndustrySwitch;
import cn.eeepay.framework.model.IndustrySwitchInfo;
import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.service.FunctionManagerService;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.util.BossBaseException;

@Service("functionManagerService")
@Transactional
public class FunctionManagerServiceImpl implements FunctionManagerService {

	@Resource
	private FunctionManagerDao functionManagerDao;

	@Autowired
	private SysDictService sysDictService;

	@Override
	public List<FunctionManager> selectFunctionManagers() {
		return functionManagerDao.selectFunctionManagers();
	}

	@Override
	public int updateFunctionSwitch(FunctionManager info) {
		return functionManagerDao.updateFunctionSwitch(info);
	}

	@Override
	public int updateAgentControl(FunctionManager info) {
		return functionManagerDao.updateAgentControl(info);
	}

	@Override
	public FunctionManager getFunctionManager(int id) {
		return functionManagerDao.getFunctionManager(id);
	}

	@Override
	public FunctionManager getFunctionManagerByNum(String funcNum) {
		return functionManagerDao.getFunctionManagerByNum(funcNum);
	}

	@Override
	public IndustrySwitchInfo getIndustrySwitchInfo() {
		IndustrySwitchInfo info = new IndustrySwitchInfo();
		// 获取总开关
		String value = sysDictService.getValueByKey("INDUSTRY_SWITCH");
		info.setIndustrySwitch(Integer.valueOf(value));
		// 获取行业切换信息
		List<IndustrySwitch> list = functionManagerDao.findAll();
		// 获取所有商户类别
		List<SysDict> merchantList = sysDictService.getAcqMerchantList("ACQ_MERCHANT_TYPE");
		for (IndustrySwitch industrySwitch : list) {
			industrySwitch.setStartTime(
					industrySwitch.getStartTime().substring(0, industrySwitch.getStartTime().lastIndexOf(":")));
			industrySwitch
					.setEndTime(industrySwitch.getEndTime().substring(0, industrySwitch.getEndTime().lastIndexOf(":")));
			
			for (SysDict sysDict : merchantList) {
				String acqMerchantType = industrySwitch.getAcqMerchantType();
				if (acqMerchantType.equals(sysDict.getSysValue())) {
					industrySwitch.setAcqMerchantTypeName(sysDict.getSysName());
				}
			}
		}
		info.setIndustrySwitchList(list);
		info.setSysDicts(merchantList);
		return info;
	}

	@Override
	public void industrySwitchSave(IndustrySwitch data) {
		String startTime = data.getStartTime();
		String endTime = data.getEndTime();
		// 修改
		if (data.getId() != null ) {
			// 先删除
			this.industrySwitchDelete(data.getId());
			this.containTimeSection(startTime, endTime);
			if ("23:59".equals(endTime)) {
				endTime = endTime + ":59";
				data.setEndTime(endTime);
				functionManagerDao.industrySwitchSaveAllData(data);
			}else {
				functionManagerDao.industrySwitchSaveAll(data);
			}
			
		} else {
			data.setCreateTime(new Date());
			this.containTimeSection(startTime, endTime);
			if ("23:59".equals(endTime)) {
				endTime = endTime + ":59";
				data.setEndTime(endTime);
				functionManagerDao.industrySwitchSaveData(data);
			}else {
				functionManagerDao.industrySwitchSave(data);
			}
			
		}
	}
		
	
	private void containTimeSection(String startTime,String endTime) {
		/*
		String formatDate = null;
		try {
			Date parseDate = DateUtils.parseDate(startTime+":00", DateUtil.TIME_FORMAT);
			parseDate= DateUtils.addSeconds(parseDate, 1);
			formatDate = DateUtil.getFormatDate(DateUtil.TIME_FORMAT, parseDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		*/
		// 判断时间是否在其他区间
		long count = functionManagerDao.industrySwitchCount(startTime,endTime);
		if (count > 0) {
			throw new BossBaseException("");
		}
	}

	@Override
	public void industrySwitchDelete(Long id) {
		functionManagerDao.industrySwitchDelete(id);
	}

	@Override
	public void industrySwitchUpdate(Integer industrySwitch) {
		functionManagerDao.updateSysDictValue("INDUSTRY_SWITCH",industrySwitch);
	}

}
