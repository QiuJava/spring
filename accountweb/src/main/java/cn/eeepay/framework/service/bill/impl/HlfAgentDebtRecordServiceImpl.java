package cn.eeepay.framework.service.bill.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.bill.HlfAgentDebtRecordMapper;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.HlfAgentDebtRecord;
import cn.eeepay.framework.model.nposp.AgentInfo;
import cn.eeepay.framework.service.bill.HlfAgentDebtRecordService;
import cn.eeepay.framework.service.nposp.AgentInfoService;
@Transactional
@Service("hlfAgentDebtRecordService")
public class HlfAgentDebtRecordServiceImpl implements HlfAgentDebtRecordService {
    @Resource
    private AgentInfoService agentInfoService;
    @Resource
    private HlfAgentDebtRecordMapper hlfAgentDebtRecordMapper;
	@Override
	public List<HlfAgentDebtRecord> findHlfAgentDebtRecordList(HlfAgentDebtRecord hlfAgentDebtRecord,
			Map<String, String> params, Sort sort, Page<HlfAgentDebtRecord> page) throws Exception {
		if(StringUtils.isNotBlank(hlfAgentDebtRecord.getDate1())){
            String date1 = hlfAgentDebtRecord.getDate1()+" 00:00:00";
            hlfAgentDebtRecord.setDate1(date1);
        }
        if(StringUtils.isNotBlank(hlfAgentDebtRecord.getDate2())){
            String date2 = hlfAgentDebtRecord.getDate2()+" 23:59:59";
            hlfAgentDebtRecord.setDate2(date2);
        }
        List<HlfAgentDebtRecord> list = hlfAgentDebtRecordMapper.findHlfAgentDebtRecordList(hlfAgentDebtRecord,sort,page);
        for (HlfAgentDebtRecord hlfAgentDebtRecordQuery : list) {
        	 AgentInfo agentInfo = agentInfoService.findAgentByUserId(hlfAgentDebtRecordQuery.getAgentNo());
        	 hlfAgentDebtRecordQuery.setAgentName(agentInfo == null ? "": agentInfo.getAgentName());
        	 AgentInfo parentAgentInfo = agentInfoService.findAgentByUserId(hlfAgentDebtRecordQuery.getParentAgentNo());
        	 hlfAgentDebtRecordQuery.setParentAgentName(parentAgentInfo == null ? "": parentAgentInfo.getAgentName());
             AgentInfo oneAgentInfo = agentInfoService.findAgentByUserId(hlfAgentDebtRecordQuery.getOneAgentNo());
             hlfAgentDebtRecordQuery.setOneAgentName(oneAgentInfo == null ? "": oneAgentInfo.getAgentName());
             hlfAgentDebtRecordQuery.setAdjustAmount(hlfAgentDebtRecordQuery.getAdjustAmount().multiply(new BigDecimal(-1)));
             hlfAgentDebtRecordQuery.setDebtAmount(hlfAgentDebtRecordQuery.getDebtAmount().multiply(new BigDecimal(-1)));
             hlfAgentDebtRecordQuery.setShouldDebtAmount(hlfAgentDebtRecordQuery.getShouldDebtAmount().multiply(new BigDecimal(-1)));
		}
        return list;
	}

	@Override
	public List<HlfAgentDebtRecord> exportHlfAgentDebtRecordList(HlfAgentDebtRecord hlfAgentDebtRecord)
			throws Exception {
		if(StringUtils.isNotBlank(hlfAgentDebtRecord.getDate1())){
            String date1 = hlfAgentDebtRecord.getDate1()+" 00:00:00";
            hlfAgentDebtRecord.setDate1(date1);
        }
        if(StringUtils.isNotBlank(hlfAgentDebtRecord.getDate2())){
            String date2 = hlfAgentDebtRecord.getDate2()+" 23:59:59";
            hlfAgentDebtRecord.setDate2(date2);
        }
		return hlfAgentDebtRecordMapper.exportHlfAgentDebtRecordList(hlfAgentDebtRecord);
	}

	@Override
	public Map<String, BigDecimal> findHlfAgentDebtRecordListCollection(HlfAgentDebtRecord hlfAgentDebtRecord) {
		if(StringUtils.isNotBlank(hlfAgentDebtRecord.getDate1())){
            String date1 = hlfAgentDebtRecord.getDate1()+" 00:00:00";
            hlfAgentDebtRecord.setDate1(date1);
        }
        if(StringUtils.isNotBlank(hlfAgentDebtRecord.getDate2())){
            String date2 = hlfAgentDebtRecord.getDate2()+" 23:59:59";
            hlfAgentDebtRecord.setDate2(date2);
        }
		return hlfAgentDebtRecordMapper.findHlfAgentDebtRecordListCollection(hlfAgentDebtRecord);
	}


	@Override
	public List<HlfAgentDebtRecord> findHlfAgentDebtRecordAgentNo(HlfAgentDebtRecord hlfAgentDebtRecord) {
		if(StringUtils.isNotBlank(hlfAgentDebtRecord.getDate1())){
            String date1 = hlfAgentDebtRecord.getDate1()+" 00:00:00";
            hlfAgentDebtRecord.setDate1(date1);
        }
        if(StringUtils.isNotBlank(hlfAgentDebtRecord.getDate2())){
            String date2 = hlfAgentDebtRecord.getDate2()+" 23:59:59";
            hlfAgentDebtRecord.setDate2(date2);
        }
		return hlfAgentDebtRecordMapper.findHlfAgentDebtRecordAgentNo(hlfAgentDebtRecord);
	}

	@Override
	public Map<String, BigDecimal> findHlfAgentDebtRecordListCollectionByList(String agentInfoList) {
		return hlfAgentDebtRecordMapper.findHlfAgentDebtRecordListCollectionByList(agentInfoList);
	}

	@Override
	public Map<String, BigDecimal> findHlfAgentDebtRecordShouldDebtAmountCollection(
			HlfAgentDebtRecord hlfAgentDebtRecord) {
		if(StringUtils.isNotBlank(hlfAgentDebtRecord.getDate1())){
            String date1 = hlfAgentDebtRecord.getDate1()+" 00:00:00";
            hlfAgentDebtRecord.setDate1(date1);
        }
        if(StringUtils.isNotBlank(hlfAgentDebtRecord.getDate2())){
            String date2 = hlfAgentDebtRecord.getDate2()+" 23:59:59";
            hlfAgentDebtRecord.setDate2(date2);
        }
		return hlfAgentDebtRecordMapper.findHlfAgentDebtRecordShouldDebtAmountCollection(hlfAgentDebtRecord);
	}


}
