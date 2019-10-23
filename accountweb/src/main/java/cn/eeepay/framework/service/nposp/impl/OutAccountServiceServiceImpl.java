package cn.eeepay.framework.service.nposp.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.nposp.OutAccountServiceMapper;
import cn.eeepay.framework.model.nposp.OutAccountService;
import cn.eeepay.framework.model.nposp.OutAccountServiceRate;
import cn.eeepay.framework.service.nposp.OutAccountServiceRateService;
import cn.eeepay.framework.service.nposp.OutAccountServiceService;

@Service("outAccountServiceService")
@Transactional("nposp")
public class OutAccountServiceServiceImpl implements OutAccountServiceService{
	@Resource
	private OutAccountServiceMapper outAccountServiceMapper;
	@Resource
	private OutAccountServiceRateService outAccountServiceRateService;

//	@Override
//	public OutAccountService getByAcqEnnameAndServiceType(String acqEnname,
//			Integer serviceType) {
//		List<OutAccountService> list = outAccountServiceMapper.findByAcqEnnameAndServiceType(acqEnname, serviceType);
//		return list != null && list.size() > 0 ? list.get(0) : null;
//	}
	
	@Override
	public Map<String, BigDecimal> acqOutServiceMoney(String acqEnname, String outAccountId, BigDecimal transAmount,
			String agentRateType, String costRateType) throws Exception {
		Map<String, BigDecimal> jsonMap=new HashMap<String, BigDecimal>();
		BigDecimal acqOutMoney1 = BigDecimal.ZERO;	//出款服务上游成本金额1（服务费用）
		BigDecimal acqOutMoney2 = BigDecimal.ZERO;	//出款服务上游成本金额2（资金成本）
		Map<String, BigDecimal> zeroMap=new HashMap<String, BigDecimal>();
		zeroMap.put("acqOutMoney1", BigDecimal.ZERO);
		zeroMap.put("acqOutMoney2", BigDecimal.ZERO);
		try {
			Map<String,Object> map1 = new HashMap<>();
			map1.put("id", outAccountId);
			map1.put("acqEnname", acqEnname);
			OutAccountService oas = outAccountServiceMapper.findOutAccountServiceByIdAndAcqEnname(map1);
			if(oas==null){
				throw new RuntimeException("出款服务没找到");
			}
			Map<String,Object> map2 = new HashMap<>();
			map2.put("serviceId", oas.getId());
			map2.put("agentRateType", agentRateType);
			OutAccountServiceRate oasr = outAccountServiceRateService.findOutAccountServiceRateBySrvIdAndArType(map2);
			if(oasr==null){
				throw new RuntimeException("出款服务费率没找到");
			}
			if(oasr!=null){
				switch (oasr.getAgentRateType()) {
				case 1:
					acqOutMoney1=oasr.getSingleAmount();
					break;
				case 2:
					acqOutMoney1=transAmount.multiply(oasr.getRate()).divide(new BigDecimal(100));
					break;
				case 3:
					if(transAmount.multiply(oasr.getRate()).divide(new BigDecimal(100)).compareTo(oasr.getCapping())==1){
						acqOutMoney1 = oasr.getCapping();
					}else if(transAmount.multiply(oasr.getRate()).divide(new BigDecimal(100)).compareTo(oasr.getSafeLine())==-1){
						acqOutMoney1 = oasr.getSafeLine();
					}else{
						acqOutMoney1=transAmount.multiply(oasr.getRate()).divide(new BigDecimal(100));
					}
					break;
				case 4:
					acqOutMoney1 = transAmount.multiply(oasr.getRate()).divide(new BigDecimal(100)).add(oasr.getSingleAmount());
					break;
				case 5:
					BigDecimal ladder1Max = oasr.getLadder1Max();
					BigDecimal ladder2Max = oasr.getLadder2Max();
					BigDecimal ladder3Max = oasr.getLadder3Max();
					BigDecimal ladder4Max = oasr.getLadder4Max();
					if(ladder1Max != null && transAmount.compareTo(ladder1Max)==-1){ 
						acqOutMoney1=transAmount.multiply(oasr.getLadder1Rate());
					}
					if(ladder1Max != null && transAmount.compareTo(oasr.getLadder1Max())!=-1 && ladder2Max != null &&  transAmount.compareTo(oasr.getLadder2Max())==-1){
						acqOutMoney1=transAmount.multiply(oasr.getLadder2Rate());
					}
					if(ladder2Max != null && transAmount.compareTo(oasr.getLadder2Max())!=-1 && ladder3Max != null && transAmount.compareTo(oasr.getLadder3Max())==-1){
						acqOutMoney1=transAmount.multiply(oasr.getLadder3Rate());
					}
					if(ladder3Max != null && transAmount.compareTo(oasr.getLadder3Max())!=-1 && ladder4Max != null && transAmount.compareTo(oasr.getLadder4Max())==-1){
						acqOutMoney1=transAmount.multiply(oasr.getLadder4Rate());
					}
					if(ladder4Max != null && transAmount.compareTo(oasr.getLadder4Max())!=-1){
						acqOutMoney1=transAmount.multiply(oasr.getLadder4Rate());
					}
					break;
				case 6:
					acqOutMoney1=transAmount.multiply(oasr.getLadder1Rate());
					break;

				}
				jsonMap.put("acqOutMoney1", acqOutMoney1);//出款服务上游成本金额1（服务费用）
				if(oas.getServiceType()==2){	//判断是否需要成本金额2
					Map<String,Object> map3 = new HashMap<>();
					map3.put("serviceId", oas.getId());
					map3.put("costRateType", costRateType);
					OutAccountServiceRate oasrt = outAccountServiceRateService.findOutAccountServiceRateBySrvIdAndCrType(map3);
					if(oasrt!=null){
						switch (oasrt.getCostRateType()) {
						case 1:
							acqOutMoney2=oasrt.getSingleAmount();
							break;
						case 2:
							acqOutMoney2=transAmount.multiply(oasrt.getRate()).divide(new BigDecimal(100));
							break;
						case 3:
							if(transAmount.multiply(oasrt.getRate()).divide(new BigDecimal(100)).compareTo(oasrt.getCapping())==1){
								acqOutMoney2=oasrt.getCapping();
							}else if(transAmount.multiply(oasrt.getRate()).divide(new BigDecimal(100)).compareTo(oasrt.getSafeLine())==-1){
								acqOutMoney2=oasrt.getSafeLine();
							}else{
								acqOutMoney2=transAmount.multiply(oasrt.getRate()).divide(new BigDecimal(100));
							}
							break;
						case 4:
							acqOutMoney2 = transAmount.multiply(oasrt.getRate()).divide(new BigDecimal(100)).add(oasrt.getSingleAmount());
							break;
						}
					}
					
				}else{
					acqOutMoney2 = BigDecimal.ZERO;
				}
				jsonMap.put("acqOutMoney2", acqOutMoney2);//出款服务上游成本金额2（资金成本）
			}
			
		} catch (Exception e) {
			throw new RuntimeException("计算出款服务上游成本费用:"+ e.getMessage());
		}
		return jsonMap;
	}

	@Override
	public OutAccountService getById(Integer id) {
		return outAccountServiceMapper.getById(id);
	}

//	@Override
//	public OutAccountService getByAcqEnname(String acqEnname) {
//		return outAccountServiceMapper.getByAcqEnname(acqEnname);
//	}
	@Override
	public List<OutAccountService> findAllOutAccountServiceByType() {
		return outAccountServiceMapper.findAllOutAccountServiceByType();
	}

	@Override
	public OutAccountService findEntityByAcqEnnameAndServiceType(String acqEnname, Integer serviceType) {
		return outAccountServiceMapper.findEntityByAcqEnnameAndServiceType(acqEnname, serviceType);
	}
	@Override
	public List<OutAccountService> findEntity(String acqEnname){
		return outAccountServiceMapper.findEntity(acqEnname);
	}

	@Override
	public List<OutAccountService> findOutAccountServiceListByAcqEnname(String acqEnname) {
		return outAccountServiceMapper.findOutAccountServiceListByAcqEnname(acqEnname);
	}
	@Override
	public List<OutAccountService> findOutAccSerListByAcqNname(String acqEnname,String serviceTypes) {
		return outAccountServiceMapper.findOutAccSerListByAcqNname(acqEnname,serviceTypes);
	}

	@Override
	public List<OutAccountService> findAllOutAccountServiceList() {
		return outAccountServiceMapper.findAllOutAccountServiceList();
	}

}
