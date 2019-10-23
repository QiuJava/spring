package cn.eeepay.framework.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.eeepay.framework.model.AcqMerchant;
import cn.eeepay.framework.util.CellUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.RouterOrgDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.service.RouterOrgService;
import org.springframework.web.multipart.MultipartFile;

/**
 * 集群中收单商户service实现
 * 
 * @author junhu
 *
 */
@Service
@Transactional
public class RouterOrgServiceImpl implements RouterOrgService {

	@Resource
	private RouterOrgDao routerOrgDao;

	@Override
	public List<Map> listRouterAcqMerchantByCon(Map<String, Object> param, Page<Map> page) {

		return routerOrgDao.listRouterAcqMerchantByCon(param, page);
	}

	@Override
	public int deleteRouterAcqMerchantById(Long id) {

		return routerOrgDao.deleteRouterAcqMerchantById(id);
	}

	@Override
	public int updateAcqMerchantQuota(Map<String, Object> param) {

		return routerOrgDao.updateAcqMerchantQuota(param);
	}

	@Override
	public List<Map<String,Object>> selecrAllInfoRecordInfo(Map<String,Object> map) {
		return routerOrgDao.selecrAllInfoRecordInfoExport(map);
	}

	@Override
	public Map<String, Object> routerOrgBatchDelete(MultipartFile file) throws Exception {
		Map<String, Object> msg = new HashMap<>();
		Workbook wb = WorkbookFactory.create(file.getInputStream());
		//读取第一个sheet
		Sheet sheet = wb.getSheetAt(0);
		// 遍历所有单元格，读取单元格
		int row_num = sheet.getLastRowNum();

		List<Map<String,Object>> errorList=new ArrayList<Map<String,Object>>();
		List<Map<String,String>> checkList=new ArrayList<Map<String,String>>();//校验
		List<Map<String,String>> deleteList=new ArrayList<Map<String,String>>();//需要更新的

		for (int i = 1; i <= row_num; i++) {
			Row row = sheet.getRow(i);
			String acqMerchantNo1 = CellUtil.getCellValue(row.getCell(0));//收单机构商户编号
			String groupCode1 = CellUtil.getCellValue(row.getCell(2));//集群编号

			Map<String,Object> errorMap=new HashMap<String,Object>();
			int rowNum=i+1;
			if(acqMerchantNo1==null||"".equals(acqMerchantNo1)){
				errorMap.put("msg","第"+rowNum+"行,收单机构商户编号为空!");
				errorList.add(errorMap);
				continue;
			}
			String acqMerchantNo=acqMerchantNo1.split("\\.")[0];

			if(groupCode1==null||"".equals(groupCode1)){
				errorMap.put("msg","第"+rowNum+"行,集群编号为空!");
				errorList.add(errorMap);
				continue;
			}
			String groupCode=groupCode1.split("\\.")[0];


			//验证重复
			if(existList(checkList,acqMerchantNo,groupCode)){
				errorMap.put("msg","导入失败,第"+rowNum+"行,收单机构商户编号("+acqMerchantNo+"),集群编号("+groupCode+")重复!");
				errorList.add(errorMap);
				continue;
			}else{
				Map<String,String> map=new HashMap<String,String>();
				map.put("acqMerchantNo",acqMerchantNo);
				map.put("groupCode",groupCode);
				checkList.add(map);
			}

			List<Map<String,Object>> oldList = routerOrgDao.getTransRouteGroupAcqMerchant(acqMerchantNo,groupCode);
			if(oldList!=null&&oldList.size()>0){
				Map<String,String> map=new HashMap<String,String>();
				map.put("acqMerchantNo",acqMerchantNo);
				map.put("groupCode",groupCode);
				deleteList.add(map);
			}else{
				errorMap.put("msg", "第"+rowNum+"行,集群编号("+groupCode+")中的收单机构商户编号("+acqMerchantNo+")不存在!");
				errorList.add(errorMap);
				continue;
			}
		}
		int num=0;
		if(deleteList!=null&&deleteList.size()>0){
			for(Map<String,String> item:deleteList){
				num=num+routerOrgDao.deleteAcqMer(item.get("acqMerchantNo"),item.get("groupCode"));
			}
		}
		msg.put("errorList", errorList);
		msg.put("status", true);
		msg.put("msg", "总共"+row_num+"条数据,成功删除"+num+"个收单机构商户,失败"+errorList.size()+"条数据!");
		return msg;
	}

	private boolean existList(List<Map<String,String>> list,String acqMerchantNo,String groupCode){
		if(list!=null&&list.size()>0){
			for(Map<String,String> map:list){
				if(map.get("acqMerchantNo").equals(acqMerchantNo)&&map.get("groupCode").equals(groupCode)){
					return true;
				}
			}
		}
		return false;
	}
}
