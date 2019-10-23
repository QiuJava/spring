package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.*;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.BusinessProductDefineService;
import cn.eeepay.framework.util.ALiYunOssUtil;
import cn.eeepay.framework.util.Constants;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service("businessProductDefineService")
public class BusinessProductDefineServiceImpl implements BusinessProductDefineService {

	@Resource
	private BusinessProductDefineDao businessProductDefineDao;
	
	@Resource
	private BusinessProductHardwareDao businessProductHardwareDao;
	
	@Resource
	private BusinessProductInfoDao businessProductInfoDao;
	
	@Resource
	private BusinessRequireItemDao businessRequireItemDao;
	
	@Resource
	private SeqService seqService;
	
	@Resource
	private TeamInfoDao teamInfoDao;
	
	@Resource
	private ServiceDao serviceDao;
	
	@Resource
	private AddRequireItemDao addRequireItemDao;
	
	@Resource
	private HardwareProductDao hardwareProductDao;
	
	@Resource
	private AgentBusinessProductDao agentBusinessProductDao;
	
	@Resource
	private MerchantBusinessProductDao merchantBusinessProductDao;
	
	
	@Override
	public List<BusinessProductDefine> selectAllInfo(String agentNo) {
		return businessProductDefineDao.selectAllInfo(agentNo);
	}

	@Override
	/**
	 * 业务产品查询 条件查询 by tans
	 */
	public List<BusinessProductDefine> selectByCondition(Page<BusinessProductDefine> page, BusinessProductDefine bpd) {
		return businessProductDefineDao.selectByParam(page, bpd);
	}

	/**
	 * 根据业务产品ID，查询业务产品的详情 包含关联的自营业务产品名称
	 */
	@Override
	public BusinessProductDefine selectById(String id) {
		return businessProductDefineDao.selectDetailById(id);
	}

	/**
	 * 根据业务产品ID，查询业务产品的详情
	 */
	@Override
	public BusinessProductDefine selectBybpId(String bpId) {
		return businessProductDefineDao.selectBybpId(bpId);
	}

	@Override
	public List<BusinessProductDefine> selectBpTeam() {
		return businessProductDefineDao.selectBpTeam();
	}

	@Override
	public List<BusinessProductDefine> selectOtherProduct(String i) {
		// TODO Auto-generated method stub
		return businessProductDefineDao.queryOtherProduct("");
	}

	@SuppressWarnings("unchecked")
	@Override
	public int insertOrUpdate(Map<String, Object> info) {
		int num = 0;
		BusinessProductDefine product = (BusinessProductDefine) info.get("product");
		List<ServiceInfo> services = (List<ServiceInfo>) info.get("services");
		List<AddRequireItem> items = (List<AddRequireItem>) info.get("items");
		List<HardwareProduct> hards = (List<HardwareProduct>) info.get("hards");
		if (product != null) {
			if (product.getBpId() != null) {
				// 保存修改后的业务产品
				num = businessProductDefineDao.update(product);
				businessProductInfoDao.deleteProductByPid(product.getBpId().toString());
				businessRequireItemDao.deleteProductByPid(product.getBpId().toString());
				businessProductHardwareDao.deleteProductByPid(product.getBpId().toString());
				if (services != null) {
					for (ServiceInfo service : services) {
						if (service != null) {
							Map<String, Object> map = new HashMap<>();
							map.put("bpId", product.getBpId().toString());
							map.put("serviceId", service.getServiceId());
							businessProductInfoDao.insert(map);
						}
					}
				}
				if (items != null) {
					for (AddRequireItem item : items) {
						if (item != null) {
							Map<String, String> map = new HashMap<>();
							map.put("bpId", product.getBpId().toString());
							map.put("itemId", item.getItemId().toString());
							businessRequireItemDao.insert(map);
						}
					}
				}
				if (product.getLimitHard()!=null && "0".equals(product.getLimitHard())){	//硬件产品不限，业务产品和硬件的关联表将硬件的ID置为“0”表示不限
					Map<String, String> map = new HashMap<>();
					map.put("bpId", product.getBpId().toString());
					map.put("hpId", product.getLimitHard());
					businessProductHardwareDao.insert(map);
				} else {										
					if (hards != null) {
						for (HardwareProduct hard : hards) {
							if (hard != null) {
								Map<String, String> map = new HashMap<>();
								map.put("bpId", product.getBpId().toString());
								map.put("hpId", hard.getHpId().toString());
								businessProductHardwareDao.insert(map);
							}
						}
					}
				}
			} else {
				// 保存新增的业务产品,以及相关联表
			//	product.setBpId(seqService.createKey(Constants.PRODUCT_SEQ, "%01d"));
				num = businessProductDefineDao.insert(product);
				if (services != null) {
					for (ServiceInfo service : services) {
						if (service != null) {
							Map<String, Object> map = new HashMap<>();
							map.put("bpId", product.getBpId().toString());
							map.put("serviceId", service.getServiceId());
							businessProductInfoDao.insert(map);
						}
					}
				}
				if (items != null) {
					for (AddRequireItem item : items) {
						if (item != null) {
							Map<String, String> map = new HashMap<>();
							map.put("bpId", product.getBpId().toString());
							map.put("itemId", item.getItemId().toString());
							businessRequireItemDao.insert(map);
						}
					}
				}
				if (product.getLimitHard()!=null && "0".equals(product.getLimitHard())){	//硬件产品不限，业务产品和硬件的关联表将硬件的ID置为“0”表示不限
					Map<String, String> map = new HashMap<>();
					map.put("bpId", product.getBpId().toString());
					map.put("hpId", product.getLimitHard());
					businessProductHardwareDao.insert(map);
				} else {										
					if (hards != null) {
						for (HardwareProduct hard : hards) {
							if (hard != null) {
								Map<String, String> map = new HashMap<>();
								map.put("bpId", product.getBpId().toString());
								map.put("hpId", hard.getHpId().toString());
								businessProductHardwareDao.insert(map);
							}
						}
					}
				}
			}
		}
		return num;
	}

	@Override
	public Map<String, Object> selectLinkInfo(String bpId) {
		Map<String, Object> msg = new HashMap<>();
		List<TeamInfo> allTeam = teamInfoDao.selectTeamName();
		List<ServiceInfo> allService = serviceDao.findAllServiceName();
		List<AddRequireItem> allItem = addRequireItemDao.selectAllRequireName();
		List<HardwareProduct> allHard = hardwareProductDao.findAllHardwareName();
//		List<String> hardIds = businessProductHardwareDao.findByProduct(bpId);
		List<BusinessProductDefine> otherProducts = businessProductDefineDao.queryOtherProduct(bpId);
		msg.put("allService", allService);
		msg.put("allItem", allItem);
		msg.put("allHard", allHard);
		msg.put("allTeam", allTeam);
		msg.put("otherProducts", otherProducts);
		return msg;
	}

	@Override
	public Map<String, Object> selectDetailById(String id) {
		Map<String, Object> msg = new HashMap<>();
		BusinessProductDefine product = businessProductDefineDao.selectDetailById(id);
		if(product.getTwoCode() != null && !"".equals(product.getTwoCode())){
			String url = ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_ATTCH_TUCKET, product.getTwoCode(), new Date(64063065600000L));
			product.setTwoCodeUrl(url);
		}
		if(product.getBpImg() != null && !"".equals(product.getBpImg())){
			String url = ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_ATTCH_TUCKET, product.getBpImg(), new Date(64063065600000L));
			product.setBpImgUrl(url);
		}
		// 利用查询得到的业务产品ID，查询中间关联表`business_product_info`，查出所有对应的服务id
		// 通过这些服务ID，查询得到服务的ID和名称
		List<String> serviceIds = businessProductInfoDao.findByProduct(id);
		List<ServiceInfo> services = new ArrayList<>();
		if (serviceIds != null) {
			for (String serviceId : serviceIds) {
				ServiceInfo service = serviceDao.findServiceName(serviceId);
				services.add(service);
			}
		}
		// 利用查询得到的业务产品ID，查询中间关联表`business_require_item`，查出所有对应的服务id
		// 通过这些服务ID，查询得到服务的ID和名称
		List<String> requireIds = businessRequireItemDao.findByProduct(id);
		List<AddRequireItem> items = new ArrayList<>();
		if (requireIds != null) {
			for (String requireId : requireIds) {
				AddRequireItem item = addRequireItemDao.selectRequireName(requireId);
				items.add(item);
			}
		}
		// 利用查询得到的业务产品ID，查询中间关联表`business_product_hardware`，查出所有对应的硬件产品id
		// 通过这些硬件产品ID，查询得到硬件产品的ID和名称
		List<String> hardWareIds = businessProductHardwareDao.findByProduct(id);
		List<HardwareProduct> hards = new ArrayList<>();
		
		if (hardWareIds != null) {
			for (String hardWareId : hardWareIds) {
				if(hardWareId != null && "0".equals(hardWareId)){
					product.setLimitHard("0");
				}else{
					product.setLimitHard("1");
				HardwareProduct hp = hardwareProductDao.findHardwareName(hardWareId);
				hards.add(hp);
				String[] hpIds = new String[hardWareIds.size()];
				hardWareIds.toArray(hpIds);
				product.setHpId(hpIds);
				}
			}
		} else {
			product.setLimitHard("1");
		}
		msg.put("status", true);
		msg.put("product", product);
		msg.put("services", services);
		msg.put("items", items);
		msg.put("hards", hards);
		return msg;
	}

	@Override
	public boolean selectRecord(Integer bpId) {
		boolean flag = false;
		if(bpId != null){
			Integer abpId = agentBusinessProductDao.findIdByBp(bpId.toString());
			Integer mbpId = merchantBusinessProductDao.findIdByBp(bpId.toString());
			if(abpId != null || mbpId != null){
				flag = true;
			}
		}
		return flag;
	}

	@Override
	public List<JoinTable> selectProductByAgent(String entityId) {
		List<JoinTable> parentProducts = businessProductDefineDao.getAgentProducts(entityId);
		return parentProducts;
	}

	@Override
	public Map<String, String> selectTeamByAgentAndBp(String agentNo) {
		List<JoinTable> teamInfos = businessProductDefineDao.selectTeamByAgentAndBp(agentNo);
		Map<String, String> teamMap = new HashMap<>();
		if (teamInfos != null && teamInfos.size() > 0) {
			for (JoinTable joinTable : teamInfos) {
				teamMap.put(joinTable.getTeamId(), joinTable.getTeamName());
			}
		}
		return teamMap;
	}

	@Override
	public String selectTeamIdByBpId(String bpId) {
		return businessProductDefineDao.selectTeamIdByBpId(bpId);
	}
	
}
