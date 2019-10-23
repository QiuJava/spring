package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.*;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.BusinessProductDefineService;
import cn.eeepay.framework.util.ALiYunOssUtil;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.StringUtil;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service("businessProductDefineService")
@Transactional
public class BusinessProductDefineServiceImpl implements BusinessProductDefineService {

	private static final Logger log = LoggerFactory.getLogger(BusinessProductDefineServiceImpl.class);

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
	public List<BusinessProductDefine> selectAllInfo() {
		return businessProductDefineDao.selectAllInfo();
	}

	@Override
	public List<BusinessProductDefine> selectAllInfoByBpId(Long bpId) {
		return businessProductDefineDao.selectAllInfoByBpId(bpId);
	}

	@Override
	public List<BusinessProductDefine> selectAllInfoByName(String bpId) {
		return businessProductDefineDao.selectAllInfoByName(bpId);
	}

	@Override
	public Map<Long, String> selectBpNameMap() {
		List<BusinessProductDefine> list = selectAllInfo();
		if(list == null || list.isEmpty()){
			return null;
		}
		Map<Long, String> map = new HashMap<>();
		for(BusinessProductDefine item: list){
			map.put(item.getBpId(), item.getBpName());
		}
		return map;
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
		String userName = info.get("createPerson")==null?"":info.get("createPerson").toString();
		if (product != null) {
			if( "100010".equals(product.getTeamId())){
				product.setOwnBpId(null);
				product.setIsOem("0");
			} else {
				product.setIsOem("1");
			}
			if (product.getBpId() != null) { 
				// 保存修改后的业务产品
				num = businessProductDefineDao.update(product);
				businessProductInfoDao.deleteProductByPid(product.getBpId().toString());
				businessRequireItemDao.deleteProductByPid(product.getBpId().toString());
				businessProductHardwareDao.deleteProductByPid(product.getBpId().toString());
				if (services != null) {
					Set<Long> serviceIdSet = new HashSet<>();
					for (ServiceInfo service : services) {
						if (service != null) {
							serviceIdSet.add(service.getServiceId());
							if(service.getLinkService()!=null){
								serviceIdSet.add(service.getLinkService());
							}
						}
					}
					Object[]serviceIds = serviceIdSet.toArray();
					businessProductInfoDao.insertBatchService(product.getBpId().toString(),serviceIds);
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
					businessProductHardwareDao.insert("0",product.getBpId().toString());
				} else {										
					if (hards != null) {
						businessProductHardwareDao.insertBatch(hards, product.getBpId().toString());
					}
				}
			} else {
				// 保存新增的业务产品,以及相关联表
			//	product.setBpId(seqService.createKey(Constants.PRODUCT_SEQ, "%01d"));
				product.setEffectiveStatus(1);//新增的业务产品，默认为生效
				product.setCreateTime(new Date());
				product.setCreatePerson(userName);
				num = businessProductDefineDao.insert(product);
				if (services != null) {
					Set<Long> serviceIdSet = new HashSet<>();
					for (ServiceInfo service : services) {
						if (service != null) {
							serviceIdSet.add(service.getServiceId());
							if(service.getLinkService()!=null){
								serviceIdSet.add(service.getLinkService());
							}
						}
					}
					Object[]serviceIds = serviceIdSet.toArray();
					businessProductInfoDao.insertBatchService(product.getBpId().toString(),serviceIds);
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
					businessProductHardwareDao.insert("0",product.getBpId().toString());
				} else {										
					if (hards != null) {
						businessProductHardwareDao.insertBatch(hards, product.getBpId().toString());
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
		List<BusinessProductDefine> otherOemProducts = businessProductDefineDao.queryOtherOemProduct(bpId);
		msg.put("allService", allService);
		msg.put("allItem", allItem);
		msg.put("allHard", allHard);
		msg.put("allTeam", allTeam);
		msg.put("otherProducts", otherProducts);
		msg.put("otherOemProducts", otherOemProducts);
		return msg;
	}

	@Override
	public Map<String, Object> selectDetailById(String id) {
		Map<String, Object> msg = new HashMap<>();
		BusinessProductDefine product = businessProductDefineDao.selectDetailById(id);
//		if(product.getTwoCode() != null && !"".equals(product.getTwoCode())){
//			String url = ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_ATTCH_TUCKET, product.getTwoCode(), new Date(64063065600000L));
//			product.setTwoCodeUrl(url);
//		}
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
		List<HardwareProduct> hards = businessProductHardwareDao.findByProduct(id.toString());
		if(hards != null && hards.size()>0){
			product.setLimitHard("1");
		} else {
			product.setLimitHard("0");
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
	public int selectExistName(Long bpId, String bpName) {
		return businessProductInfoDao.selectExistName(bpId, bpName);
	}

	@Override
	public List<Long> findByService(Long serviceId) {
		return businessProductInfoDao.findByService(serviceId);
	}

	@Override
	public Map<String, Object> getProductBase(Integer bpId) {
		Map<String, Object> msg = new HashMap<>();
		BusinessProductDefine product = businessProductInfoDao.getProductBase(bpId);
		if(product.getBpImg() != null && !"".equals(product.getBpImg())){
			String url = ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_ATTCH_TUCKET, product.getBpImg(), new Date(64063065600000L));
			product.setBpImgUrl(url);
		}
		List<BusinessProductDefine> list = businessProductInfoDao.getOtherProduct(product);
		
		// 利用查询得到的业务产品ID，查询中间关联表`business_product_hardware`，查出所有对应的硬件产品id
		// 通过这些硬件产品ID，查询得到硬件产品的ID和名称
		List<HardwareProduct> hards = businessProductHardwareDao.findByProduct(bpId.toString());
		if(hards != null && hards.size()>0){
			product.setLimitHard("1");
		} else {
			product.setLimitHard("0");
		}
		List<HardwareProduct> allHards = hardwareProductDao.findAllHardwareName();
		//gw
		List<AddRequireItem> allItem = addRequireItemDao.selectAllRequireName();
		msg.put("allItem", allItem);
		
		List<AddRequireItem> bpIdItem = addRequireItemDao.selectItemByBpId(bpId.toString());
		msg.put("bpIdItem", bpIdItem);
		//
		msg.put("allHards", allHards);
		msg.put("hards", hards);
		msg.put("product", product);
		msg.put("otherProducts", list);
		return msg;
				
	}

	@Override
	public int update(BusinessProductDefine product) {
		return businessProductDefineDao.update(product);
	}

	@Override
	public int updateProductBase(String params) {
		JSONObject json = JSON.parseObject(params);
		BusinessProductDefine product = json.getObject("baseInfo", BusinessProductDefine.class);
		String bpId = product.getBpId().toString();
		String [] reqItemsArray ={};
		String [] sItemsArray ={};
        StringBuilder reqItems = new StringBuilder("");
        StringBuilder sItems = new StringBuilder("");
		List<AddRequireItem> items = JSON.parseArray(json.getJSONArray("items").toJSONString(),AddRequireItem.class);
		//对进件项进行处理开始
		if (items != null) {
			for (AddRequireItem item : items) {
				if (item != null) {
					reqItems.append(item.getItemId().toString());
					reqItems.append(",");
				}
			}
		}
			if(reqItems.length()>0){
				reqItemsArray =  reqItems.toString().split(",");
			}
			
			List<String> requireIds = businessRequireItemDao.findByProduct(bpId);
			
			if (requireIds != null) {
				for (String requireId : requireIds) {
					sItems.append(requireId);
					sItems.append(",");
				}
			}
			
			if(sItems.length()>0){
				sItemsArray =  sItems.toString().split(",");
			}
			
			
		log.info(bpId+"原items是==>"+ Arrays.asList(sItemsArray)+"修改之后==>"+Arrays.asList(reqItemsArray));
		//String[] result_union = StringUtil.union(sItemsArray, reqItemsArray);//
		String[] result_insect = StringUtil.intersect(sItemsArray, reqItemsArray);//交集不变
		String[] delResult_minus = StringUtil.minus(result_insect, sItemsArray);//差集
		String[] insertResult_minus = StringUtil.minus(result_insect, reqItemsArray);//差集
		for (String str : delResult_minus) {
			System.out.println("交集需要删除==>" + str);
			businessRequireItemDao.deleteProductByItemAndbpId(bpId, str);
		}
		for (String str : insertResult_minus) {
			System.out.println("修改之后需要插入==>" + str);
			businessRequireItemDao.insertProductByItemAndbpId(bpId, str);
		}
		//对进件项进行处理结束
		List<HardwareProduct> hards = JSON.parseArray(json.getJSONArray("hards").toJSONString(),
				HardwareProduct.class);
		int num = businessProductDefineDao.update(product);
		businessProductHardwareDao.deleteProductByPid(product.getBpId().toString());
		if (product.getLimitHard()!=null && "0".equals(product.getLimitHard())){	//硬件产品不限，业务产品和硬件的关联表将硬件的ID置为“0”表示不限
			businessProductHardwareDao.insert("0",product.getBpId().toString());
		} else {					
			businessProductHardwareDao.insertBatch(hards,product.getBpId().toString());
		}
		return num;
	}

	@Override
	public BusinessProductDefine selectBybpIdAndAgentNo(String bpId, String agentNo) {
		
		return businessProductDefineDao.selectBybpIdAndAgentNo(bpId, agentNo);
	}

	@Override
	public List<BusinessProductDefine> getProductByServiceType(String[] serviceTypes) {
		return businessProductDefineDao.getProductByServiceType(serviceTypes);
	}

	@Override
	public List<BusinessProductDefine> getProduct() {
		return businessProductDefineDao.getProduct();
	}

	@Override
	public List<BusinessProductDefine> getProductByTeam(String teamId) {
		List<String> teamIdList = new ArrayList<>();
		if(StringUtils.isNotEmpty(teamId)) {
			teamIdList = Arrays.asList(teamId.split(","));
		}
		return businessProductDefineDao.getProductByTeam(teamIdList);
	}

	@Override
	public List<BusinessProductDefine> getTeamOtherBp(String teamId, String bpId) {
		return businessProductDefineDao.getTeamOtherBp(teamId, bpId);
	}

	@Override
	public Result updateEffectiveStatus(BusinessProductDefine baseInfo) {
		Result result = new Result();
		if(baseInfo == null || baseInfo.getBpId() == null
				|| baseInfo.getEffectiveStatus() == null){
			result.setMsg("参数非法");
			return result;
		}
		if(baseInfo.getEffectiveStatus() == 1){
			result.setMsg("失效业务产品不能再生效");
			return result;
		}
		//如果业务产品下，有服务未失效，提示服务名称，并返回
		if (existsEffectiveService(baseInfo, result)) return result;
		//检查自己是队长还是队员
		BusinessProductDefine product = businessProductDefineDao.selectBybpId(String.valueOf(baseInfo.getBpId()));
		if(product.getAllowIndividualApply() == 1){
			//如果队员有未失效的业务产品，提示业务产品名称，并返回
			if (existsEffectiveProduct(baseInfo, result)) return result;
		}
		//更改业务产品状态
		int num = businessProductDefineDao.updateEffectiveStatus(baseInfo);
		if(num == 1){
			result.setStatus(true);
			result.setMsg("操作成功");
		} else {
			result.setMsg("操作失败");
		}
		return result;
	}

	/**
	 * 检查是否有生效的队员
	 * @param baseInfo
	 * @param result
	 * @return
	 */
	private boolean existsEffectiveProduct(BusinessProductDefine baseInfo, Result result) {
		List<BusinessProductDefine> effectiveProductList = businessProductDefineDao.selectEffectiveMemberProductByBpId(baseInfo.getBpId());
		if(effectiveProductList != null && effectiveProductList.size() > 0){
            StringBuilder productName = new StringBuilder();
            for(BusinessProductDefine item: effectiveProductList){
                productName.append(item.getBpName()).append(",");
            }
            result.setMsg("有队员业务产品在使用中:" + productName.substring(0, productName.length() - 1) + "。");
			return true;
        }
		return false;
	}

	/**
	 * 检查是否有生效的服务
	 * @param baseInfo
	 * @param result
	 * @return
	 */
	private boolean existsEffectiveService(BusinessProductDefine baseInfo, Result result) {
		List<ServiceInfo> effectiveServiceList = businessProductDefineDao.selectEffectiveServiceByBpId(baseInfo.getBpId());
		if(effectiveServiceList != null && effectiveServiceList.size() > 0){
			StringBuilder serviceName = new StringBuilder();
			for(ServiceInfo item: effectiveServiceList){
				serviceName.append(item.getServiceName()).append(",");
			}
			result.setMsg("有服务在使用中:" + serviceName.substring(0, serviceName.length() - 1) + "。");
			return true;
		}
		return false;
	}
}
