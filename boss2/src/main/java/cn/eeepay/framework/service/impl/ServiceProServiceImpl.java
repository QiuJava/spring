package cn.eeepay.framework.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import cn.eeepay.framework.model.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.sql.visitor.functions.Length;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.eeepay.framework.dao.AgentBusinessProductDao;
import cn.eeepay.framework.dao.AgentInfoDao;
import cn.eeepay.framework.dao.BusinessProductInfoDao;
import cn.eeepay.framework.dao.ServiceDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.service.ServiceProService;

/**
 * <p>Description: <／p>
 * <p>Company: ls.eeepay.cn<／p> 
 * @author 沙
 * @date 2016年5月11日
 */
@Service("serviceProService")
@Transactional
public class ServiceProServiceImpl implements ServiceProService {
	private static final Pattern pattern=Pattern.compile("^(\\d+(\\.\\d+)?)~(\\d+(\\.\\d+)?)%~(\\d+(\\.\\d+)?)$");
	private static final DecimalFormat format=new java.text.DecimalFormat("0.00");
	@Resource
	private ServiceDao serviceDao;
	@Resource
	private SeqService seqService;
	@Resource
	private BusinessProductInfoDao businessProductInfoDao;
	
	@Resource
	private AgentInfoDao agentInfoDao;
	
	@Resource
	private AgentInfoService agentInfoService;
	
	@Resource
	private AgentBusinessProductDao agentBusinessProductDao;
	
	public int existServiceName(ServiceInfo serviceInfo){
		return serviceDao.existServiceName(serviceInfo);
	}
	
	/**
	 * 添加服务类别的基础信息
	 * @param serviceInfo
	 * @return 
	 */
	@Override
	public int insertServiceInfo(ServiceInfo serviceInfo){
		if(serviceInfo==null){
			return 0;
		}
		int serviceType = serviceInfo.getServiceType();
		if(serviceType == 10000 || serviceType == 10001){//贴现与关联提现服务类型
			serviceInfo.settFlag(null);
		}
		if(serviceInfo.gettFlag()==null || serviceInfo.gettFlag() == 0 || serviceInfo.gettFlag() == 2){
			serviceInfo.setLinkService(null);
		}
		int num=serviceDao.insertServiceInfo(serviceInfo);
		if(serviceInfo.getRates()!=null&&serviceInfo.getRates().size()>0){
			for(ServiceRate rate:serviceInfo.getRates()){
				rate.setServiceId(serviceInfo.getServiceId());
				rate.setIsGlobal(1);
			}
			serviceDao.insertServiceRateList(serviceInfo.getRates());
		}	
		if(serviceInfo.getQuotas()!=null&&serviceInfo.getQuotas().size()>0){
			for(ServiceQuota quota:serviceInfo.getQuotas()){
				quota.setServiceId(serviceInfo.getServiceId());
				quota.setIsGlobal(1);
			}
			serviceDao.insertServiceQuotaList(serviceInfo.getQuotas());
		}
		return num;
	}
	/**
	 * 批量添加服务费率
	 * @param list
	 * @return 条数
	 */
	@Override
	public int insertServiceRateList(List<ServiceRate> list){
		return serviceDao.insertServiceRateList(list);
	}
	
	/**
	 * 批量添加服务额度
	 * @param list
	 * @return
	 */
	@Override
	public int insertServiceQuotaList(List<ServiceQuota> list){
		return serviceDao.insertServiceQuotaList(list);
	}
	
	/**
	 * 更新服务信息,更新服务定义
	 * @param serviceInfo
	 * @return
	 */
	@Override
	public int updateServiceInfo(ServiceInfo serviceInfo){
		int numServiceName = serviceDao.existServiceName(serviceInfo);
		if(numServiceName==1){
			throw new RuntimeException("服务名称已存在!");
		}
		int serviceType = serviceInfo.getServiceType();
		if(serviceType==10000||serviceType==10001){//贴现与关联提现服务类型
			serviceInfo.settFlag(null);
		}
		if(serviceInfo.gettFlag()==null || serviceInfo.gettFlag()==0||serviceInfo.gettFlag()==2 ){
			serviceInfo.setLinkService(null);
		}
		
		int count=serviceDao.updateServiceInfo(serviceInfo);
		if(count!=1){
			throw new RuntimeException("修改失败");
		}
		serviceDao.deleteServiceRateByFK(serviceInfo.getServiceId(),"0");
		serviceDao.deleteServiceQuotaByFK(serviceInfo.getServiceId(),"0");
		serviceDao.insertServiceRateList(serviceInfo.getRates());
		serviceDao.insertServiceQuotaList(serviceInfo.getQuotas());
		//修改一级代理商勾选了“与公司一致”的费率和限额
		for(ServiceRate rate: serviceInfo.getRates()){
			serviceDao.updateAgentServiceRateList(rate);
		}
		for(ServiceQuota quota: serviceInfo.getQuotas()){
			serviceDao.updateAgentServiceQuotaList(quota);
		}
		return count;
	}
	
	/**
	 * 删除服务信息,删除服务定义
	 * @param serviceInfo
	 * @return
	 */
	@Override
	public int deleteServiceInfo(ServiceInfo serviceInfo){
		serviceDao.deleteServiceRateByFK(serviceInfo.getServiceId(),"0");
		serviceDao.deleteServiceQuotaByFK(serviceInfo.getServiceId(),"0");
		return serviceDao.deleteServiceInfo(serviceInfo.getServiceId());
	}
	
	
	
	@Override
	public List<ServiceInfo> getServiceInfo(Map<String,Object> info,Page<ServiceInfo> page){
		return serviceDao.getServiceInfo(info,page);
	}
	/*
	 * 查询服务信息，费率和控管信息
	 */
	@Override
	public List<ServiceInfo> getServiceInfoWithDetail(ServiceInfo info,Page<ServiceInfo> page){
		
		return null;
	}
	@Override
	public ServiceInfo queryServiceDetail(ServiceInfo info) {
		ServiceInfo serviceInfo=serviceDao.queryServiceInfo(info.getServiceId());
		if(serviceInfo!=null){
			List<ServiceRate> rates=serviceDao.getServiceRate(serviceInfo.getServiceId(),"0");
			for(ServiceRate r:rates){
				r.setMerRate(profitExpression(r));
			}
			List<ServiceQuota> quotas=serviceDao.getServiceQuota(serviceInfo.getServiceId(),"0");
			serviceInfo.setRates(rates);
			serviceInfo.setQuotas(quotas);
			//获取关联的服务
			if(serviceInfo.getLinkService()!=null && serviceInfo.getLinkService()!=0){
				ServiceInfo linkService = serviceDao.findServiceName(serviceInfo.getLinkService().toString());
				if(linkService!=null){
					serviceInfo.setLinkServiceName(linkService.getServiceName());
				}
			}
			serviceInfo.setUsedStatus(0);
			//当代理商或者商户已含有时，基本信息和服务管控费率方式不能修改，但是值可以改
			//1.根据服务Id去找所有包含此服务的业务产品的bpId
			List<Long> bpIds = businessProductInfoDao.findByService(serviceInfo.getServiceId()); 
			//2.根据找到的业务产品的bpId去判断是否有代理商已经使用，如果有，设置服务已被使用
			if(bpIds!=null&& bpIds.size()>0){
				int num = agentBusinessProductDao.findIdByBpIds(bpIds);
				if(num>0){
					serviceInfo.setUsedStatus(1);
				}
			}
		}
		return serviceInfo;
	}
	
	/**
	 * 修改服务费率 
	 */
	@Override
	public int updateServiceStatus(String id, String status) {
		return serviceDao.updateServiceStatus(id,status);
	}
	
	/**
	 * @param rate 表达式中的设置成相应的属性
	 * @param isChange： true-允许类型可以变，false-不允许类型改变
	 */
	@Override
	public void setServiceRate(ServiceRate rate,boolean isChange){
		String type=rate.getRateType();
		if(rate.getMerRate().indexOf("<")!=-1){
			if(!rate.getMerRate().matches("^(\\d+(\\.\\d+)?)%(<(\\d+(\\.\\d+)?)<(\\d+(\\.\\d+)?)%)+"))
				throw new RuntimeException("服务费率设置错误！");
			String[] strs=rate.getMerRate().split("<");
			rate.setLadder1Rate(new BigDecimal(strs[0].substring(0, strs[0].lastIndexOf("%"))));
			rate.setLadder1Max(new BigDecimal(strs[1]));
			rate.setLadder2Rate(new BigDecimal(strs[2].substring(0, strs[2].lastIndexOf("%"))));
			if(strs.length>3){
				rate.setLadder2Max(new BigDecimal(strs[3]));
				rate.setLadder3Rate(new BigDecimal(strs[4].substring(0, strs[4].lastIndexOf("%"))));
				if(rate.getLadder2Max().compareTo(rate.getLadder1Max())<=0)
					throw new RuntimeException("服务费率设置错误！");
			}
			if(strs.length>5){
				rate.setLadder3Max(new BigDecimal(strs[5]));
				rate.setLadder4Rate(new BigDecimal(strs[6].substring(0, strs[6].lastIndexOf("%"))));
				if(rate.getLadder3Max().compareTo(rate.getLadder2Max())<=0)
					throw new RuntimeException("服务费率设置错误！");
			}
			if(strs.length>7){
				rate.setLadder4Max(new BigDecimal(strs[7]));
				if(rate.getLadder4Max().compareTo(rate.getLadder3Max())<=0)
					throw new RuntimeException("服务费率设置错误！");
			}
			rate.setRateType("5");
		}else if(rate.getMerRate().indexOf("+")!=-1){
			if(!rate.getMerRate().matches("^(\\d+(\\.\\d+)?)%\\+(\\d+(\\.\\d+)?)"))
				throw new RuntimeException("服务费率设置错误！");
			String[] temp=rate.getMerRate().split("%\\+");
			rate.setRate(new BigDecimal(temp[0]));
			rate.setSingleNumAmount(new BigDecimal(temp[1]));
			rate.setRateType("4");
		}else if(rate.getMerRate().indexOf("~")!=-1){
			Matcher m = pattern.matcher(rate.getMerRate());
			while(m.find()){
				rate.setSafeLine(new BigDecimal(m.group(1)));
				rate.setRate(new BigDecimal(m.group(3)));
				rate.setCapping(new BigDecimal(m.group(5)));
			}
			if(rate.getSafeLine()==null||rate.getCapping().compareTo(rate.getSafeLine())<0)
				throw new RuntimeException("服务费率设置错误！");
			rate.setRateType("3");
		}else if(rate.getMerRate().indexOf("%")!=-1){
			String str_=rate.getMerRate().substring(0, rate.getMerRate().indexOf("%"));
			if(str_.matches("\\d+(\\.\\d+)?")){
				rate.setRate(new BigDecimal(str_));
			}else{
				throw new RuntimeException("服务费率设置错误！");
			}
			rate.setRateType("2");
		}else {
			if(rate.getMerRate().matches("\\d+(\\.\\d+)?")){
				rate.setSingleNumAmount(new BigDecimal(rate.getMerRate()));
			}else{
				throw new RuntimeException("服务费率设置错误！");
			}
			rate.setRateType("1");
		}
		if(!isChange&&!rate.getRateType().equals(type)){
			throw new RuntimeException("服务费率类型的格式不正确！");
		}
	}
	
	@Override
	public String profitExpression(ServiceRate rule){
		if(rule==null||StringUtils.isEmpty(rule.getRateType())) return "";
		String profitExp=null;
		switch(rule.getRateType()){
			case "1": 
				profitExp=rule.getSingleNumAmount()==null?"":format.format(rule.getSingleNumAmount());
				break;
			case "2":
				profitExp=rule.getRate()==null?"":format.format(rule.getRate())+"%";
				break;
			case "3":
				profitExp=format.format(rule.getSafeLine())+"~"+format.format(rule.getRate())+"%~"+format.format(rule.getCapping());
				break;
			case "4":
				profitExp=format.format(rule.getRate())+"%+"+format.format(rule.getSingleNumAmount());	
				break;
			case "5":
				StringBuffer sb=new StringBuffer();
				sb.append(format.format(rule.getLadder1Rate())).append("%").append("<").append(format.format(rule.getLadder1Max()))
				  .append("<").append(format.format(rule.getLadder2Rate())).append("%");
				if(rule.getLadder2Max()!=null){
					sb.append("<").append(format.format(rule.getLadder2Max()))
					  .append("<").append(format.format(rule.getLadder3Rate())).append("%");
					if(rule.getLadder3Max()!=null){
						sb.append("<").append(format.format(rule.getLadder3Max()))
						  .append("<").append(format.format(rule.getLadder4Rate())).append("%");
						if(rule.getLadder4Max()!=null){
							sb.append("<").append(format.format(rule.getLadder4Max()));
						}
					}
				}
				profitExp=sb.toString();
			default : ;	
		}
		return profitExp;
	}
	@Override
	public ServiceRate queryServiceRate(ServiceRate sr) {
		return serviceDao.queryServiceRate(sr);
	}
	@Override
	public ServiceInfo queryServiceInfo(Long serviceId) {
		return serviceDao.queryServiceInfo(serviceId);
	}
	@Override
	public ServiceQuota queryServiceQuota(ServiceQuota sq) {
		return serviceDao.queryServiceQuota(sq);
	}
	@Override
	public List<ServiceInfo> selectServiceInfo() {
		return serviceDao.selectServiceInfo();
	}
	@Override
	public List<ServiceRate> getServiceAllRate(Long serviceId,String agentId) {
		return serviceDao.getServiceAllRate(serviceId,agentId);
	}
	@Override
	public List<ServiceQuota> getServiceAllQuota(Long serviceId,String agentId) {
		return serviceDao.getServiceAllQuota(serviceId,agentId);
	}
	@Override
	public List<ServiceInfo> getLinkServices() {
		return serviceDao.getLinkServices();
	}


	//=========sober===================================
	@Override
	public List<AgentShareRule> queryAgentProfit(ServiceInfo info) {
		List<AgentShareRule> list = serviceDao.queryByParams(info.getServiceId());
		if (list.size()==0) {
			list=serviceDao.queryAgentProfit(info.getServiceId());
		}
		for (AgentShareRule rule : list) {
			agentInfoService.profitExpression(rule);
//			StringBuffer sb=new StringBuffer();
//			if (rule.getLadder1Rate()!=null||rule.getLadder1Max()!=null||rule.getLadder1Max()!=null||rule.getLadder2Rate()!=null||
//					rule.getLadder2Max()!=null||rule.getLadder3Rate()!=null||rule.getLadder3Max()!=null||rule.getLadder4Rate()!=null) {
//				sb.append(rule.getLadder1Rate()).append("%<").append(rule.getLadder1Max())
//				  .append("<").append(rule.getLadder2Rate()).append("%<").append(rule.getLadder2Max())
//				  .append("<").append(rule.getLadder3Rate()).append("%<").append(rule.getLadder3Max())
//				  .append("<").append(rule.getLadder4Rate()).append("%");
//				rule.setLadderRate(sb.toString());	
//			}
		}
		return list;
	}

	@Override
	public int saveAgentProfit(JSONObject json) {
		List<AgentShareRule> list=JSONArray.parseArray(json.getString("serviceInfo"), AgentShareRule.class);
		int i = 0;
		Long serviceId = Long.valueOf(list.get(0).getServiceId());
		if (serviceDao.queryByAgentNo(serviceId).size()<=0) {
			System.out.println("进入新增..............");
			for(AgentShareRule rule:list){
				System.out.println("income: "+rule.getIncome());
				agentInfoService.setShareRule(rule);
				rule.setAgentNo("0");
				rule.setCheckStatus(0);
				rule.setLockStatus(0);
			}
			i = agentInfoDao.insertAgentShareList(list);
		}else {
			System.out.println("进入更新..............");
			List<AgentShareRule> listAgentShareRule = new ArrayList<>();
			for (AgentShareRule share : list) {
				AgentShareRule agentShareRule=new AgentShareRule();
				agentShareRule.setId(share.getId());
				agentShareRule.setProfitType(share.getProfitType());
				agentShareRule.setIncome(share.getIncome());
				agentShareRule.setLadderRate(share.getLadderRate());
				agentShareRule.setShareProfitPercent(share.getShareProfitPercent());
				if (share.getCost()!=null) {
					agentShareRule.setCost(share.getCost());
				}
				agentInfoService.setShareRule(agentShareRule);
				listAgentShareRule.add(agentShareRule);
			}
			System.out.println(listAgentShareRule.size()+"条数据");
			i = agentInfoDao.updateAgentShareList(listAgentShareRule);
		}
		
		return i;
	}

	@Override
	public Result updateEffectiveStatus(ServiceInfo baseInfo) {
		Result result = new Result();
		if(baseInfo == null || baseInfo.getServiceId() == null
				|| baseInfo.getEffectiveStatus() == null){
			result.setMsg("参数非法");
			return result;
		}
		if(baseInfo.getEffectiveStatus() == 1){
			result.setMsg("失效的服务不能再生效");
			return result;
		}
		//如果所属的业务产品，是队长，其队员下面如果有生效的同类型服务，则不能失效
        if (existsMemberService(baseInfo, result)) return result;
        int num = serviceDao.updateEffectiveStatus(baseInfo);
        if(num == 1){
            result.setStatus(true);
            result.setMsg("操作成功");
        } else {
            result.setMsg("操作失败");
        }
		return result;
	}

	@Override
	public List<ServiceInfo> selectServiceName() {
		return serviceDao.selectServiceName();
	}

	@Override
	public Map<Long, String> selectServiceNameMap() {
		List<ServiceInfo> list = selectServiceName();
		if(list == null || list.isEmpty()){
			return null;
		}
		Map<Long, String> map = new HashMap<>();
		for(ServiceInfo item: list){
			map.put(item.getServiceId(), item.getServiceName());
		}
		return map;
	}

	private boolean existsMemberService(ServiceInfo baseInfo, Result result) {
        //根据服务找到业务产品
        BusinessProductDefine product = serviceDao.getProductByService(baseInfo.getServiceId());
        if(product != null && product.getAllowIndividualApply() == 1){
            ServiceInfo serviceInfo = serviceDao.findServiceName(String.valueOf(baseInfo.getServiceId()));
            //如果业务产品有群组，且为队长，其队员下面如果有生效的同类型服务，则不能失效
            List<ServiceInfo> serviceList = serviceDao.getMemberServiceByProduct(product.getBpId(), serviceInfo.getServiceType());
            if(serviceList != null && serviceList.size() >0){
                StringBuilder serviceName = new StringBuilder();
                for(ServiceInfo item: serviceList){
                    serviceName.append(item.getServiceName()).append(",");
                }
                result.setMsg("有队员服务在使用中:" + serviceName.substring(0, serviceName.length() - 1) + "。");
                return true;
            }
        }
        return false;
    }

}
