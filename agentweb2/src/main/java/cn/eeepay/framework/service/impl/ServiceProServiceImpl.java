package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.ServiceDao;
import cn.eeepay.framework.model.ServiceInfo;
import cn.eeepay.framework.model.ServiceQuota;
import cn.eeepay.framework.model.ServiceRate;
import cn.eeepay.framework.service.ServiceProService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.regex.Pattern;

/**
 * <p>Description: <／p>
 * <p>Company: ls.eeepay.cn<／p> 
 * @author 沙
 * @date 2016年5月11日
 */
@Service("serviceProService")
public class ServiceProServiceImpl implements ServiceProService {
	private static final Pattern pattern=Pattern.compile("^(\\d+(\\.\\d+)?)~(\\d+(\\.\\d+)?)%~(\\d+(\\.\\d+)?)$");
	private static final DecimalFormat format=new java.text.DecimalFormat("0.00");
	@Resource
	private ServiceDao serviceDao;
	@Resource
	private SeqService seqService;
		
	@Override
	public String profitExpression(ServiceRate rule){
		if(rule==null||StringUtils.isEmpty(rule.getRateType())) return "";
		String profitExp=null;
		switch(rule.getRateType()){
			case "1": 
				profitExp=rule.getSingleNumAmount()==null?"":rule.getSingleNumAmount().setScale(2, RoundingMode.HALF_UP).toString();
				break;
			case "2":
				profitExp=rule.getRate()==null?"":rule.getRate().setScale(3, RoundingMode.HALF_UP).toString()+"%";
				break;
			case "3":
				profitExp=format.format(rule.getSafeLine())+"~"+rule.getRate().setScale(3, RoundingMode.HALF_UP).toString()+"%~"+rule.getCapping().setScale(2, RoundingMode.HALF_UP).toString();
				break;
			case "4":
				profitExp=rule.getRate().setScale(3, RoundingMode.HALF_UP).toString()+"%+"+rule.getSingleNumAmount().setScale(2, RoundingMode.HALF_UP).toString();	
				break;
			case "5":
				StringBuffer sb=new StringBuffer();
				sb.append(rule.getLadder1Rate().setScale(3, RoundingMode.HALF_UP).toString()).append("%").append("<").append(rule.getLadder1Max().setScale(2, RoundingMode.HALF_UP).toString())
				  .append("<").append(rule.getLadder2Rate().setScale(3, RoundingMode.HALF_UP).toString()).append("%");
				if(rule.getLadder2Max()!=null){
					sb.append("<").append(rule.getLadder2Max().setScale(2, RoundingMode.HALF_UP).toString())
					  .append("<").append(rule.getLadder3Rate().setScale(3, RoundingMode.HALF_UP).toString()).append("%");
					if(rule.getLadder3Max()!=null){
						sb.append("<").append(rule.getLadder3Max().setScale(2, RoundingMode.HALF_UP).toString())
						  .append("<").append(rule.getLadder4Rate().setScale(3, RoundingMode.HALF_UP).toString()).append("%");
						if(rule.getLadder4Max()!=null){
							sb.append("<").append(rule.getLadder4Max().setScale(2, RoundingMode.HALF_UP).toString());
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
	public List<ServiceQuota> getServiceAllQuota(Long serviceId, String agentId) {
		return serviceDao.getServiceQuota(serviceId, agentId);
	}
	@Override
	public List<ServiceRate> getServiceAllRate(Long serviceId, String agentId) {
		return serviceDao.getServiceAllRate(serviceId, agentId);
	}
	
	
}
