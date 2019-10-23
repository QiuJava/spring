package cn.eeepay.framework.service.impl;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import cn.eeepay.framework.model.exchangeActivate.ExcActRoute;
import cn.eeepay.framework.model.exchangeActivate.ExcActRouteGood;
import cn.eeepay.framework.model.risk.TradeRestrict;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.util.CellUtil;
import cn.eeepay.framework.util.ListDataExcelExport;
import cn.eeepay.framework.util.StringUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.AcqMerchantDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AcqMerchant;
import cn.eeepay.framework.model.AcqTerminal;
import cn.eeepay.framework.service.AcqMerchantService;
import cn.eeepay.framework.service.AcqTerminalService;
import org.springframework.web.multipart.MultipartFile;

@Service("acqMerchantService")
@Transactional
public class AcqMerchantServiceImpl implements AcqMerchantService {
	private static final Logger log = LoggerFactory.getLogger(AcqMerchantServiceImpl.class);
	@Resource
	private AcqMerchantDao acqMerchantDao;
	
	//收单机构终端
	@Resource
	private AcqTerminalService acqTerminalService;
	@Resource
	private SysDictService sysDictService;
	
	/**
	 * 收单机构商户新增和终端新增
	 */
	@Override
	public int insert(AcqMerchant record,List<String> list) {
		int i=0;
	    i = acqMerchantDao.insert(record);
	    if(i!=1){
			throw new RuntimeException("收单机构商户新增失败");
		}
	    AcqTerminal act=new AcqTerminal();
		act.setAcqMerchantNo(record.getAcqMerchantNo());
		act.setAcqOrgId(record.getAcqOrgId().toString());
		act.setCreatePerson(record.getCreatePerson());
		for (int j = 0; j < list.size(); j++) {
			if(list.get(j)==null||"".equals(list.get(j))){
				return 0;
			}
			act.setAcqTerminalNo(list.get(j));
			if(acqTerminalService.selectTerminalByTerNo(act)!=null){
				throw new RuntimeException("收单机构终端已存在，请注意检查");
			}
			i=acqTerminalService.insert(act);
			if(i!=1){
				throw new RuntimeException("收单机构商户新增失败");
			}
		}
		return i;
	}

	@Override
	public int updateByPrimaryKey(AcqMerchant record) {
		return acqMerchantDao.updateByPrimaryKey(record);
	}

	@Override
	public List<AcqMerchant> selectAllInfo(Page<AcqMerchant> page, AcqMerchant amc) {
		return acqMerchantDao.selectAllInfo(page, amc);
	}

	@Override
	public int updateStatusByid(AcqMerchant record) {
		return acqMerchantDao.updateStatusByid(record);
	}
	@Override
	public AcqMerchant selectInfoByAcqmerNo(AcqMerchant record) {
		return acqMerchantDao.selectInfoByAcqmerNo(record);
	}

	@Override
	public AcqMerchant selectByPrimaryKey(Integer id) {
		return acqMerchantDao.selectByPrimaryKey(id);
	}

	@Override
	public AcqMerchant selectInfoByMerNo(String merchantNo) {
		return acqMerchantDao.selectInfoByMerNo(merchantNo);
	}

	@Override
	public int selectOrgMerExistByMerNo(String acqMerchantNo) {
		return acqMerchantDao.selectOrgMerExistByMerNo(acqMerchantNo);
	}

	@Override
	public int insertInfo(AcqMerchant record,AcqTerminal aa) {
		int num=0;
		AcqTerminal aa1 = acqTerminalService.selectTerminalByTerNo(aa);
		if(aa1!=null){//判断是否存在这个收单终端 存在就修改，不存在就新增
			if(aa1.getAcqMerchantNo()==null||aa1.getAcqMerchantNo().equals("")){
				num=acqTerminalService.updateInfo(aa);
				if(num!=1){
					throw new RuntimeException("收单商户为："+record.getAcqMerchantNo()+"新增失败，请注意检查");
				}
			}
		}else{
			num=acqMerchantDao.insert(record);
			if(num!=1){
				throw new RuntimeException("收单商户为："+record.getAcqMerchantNo()+"新增失败，请注意检查");
			}
			num=acqTerminalService.insert(aa);
			if(num!=1){
				throw new RuntimeException("收单商户为："+record.getAcqMerchantNo()+"新增失败，请注意检查");
			}
		}
		return num;
	}

	@Override
	public List<AcqMerchant> importDetailSelect(AcqMerchant amc) {
		return acqMerchantDao.importDetailSelect(amc);
	}

	@Override
	public void importDetail(List<AcqMerchant> list, HttpServletResponse response) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
		String fileName = "收单机构商户列表"+sdf.format(new Date())+".xlsx" ;
		String fileNameFormat = new String(fileName.getBytes("UTF-8"),"ISO-8859-1");
		response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
		List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
		if(list.size()<1){
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("id",null);
			maps.put("agentName",null);
			maps.put("acqMerchantNo",null);
			maps.put("acqMerchantName",null);
			maps.put("source",null);
			maps.put("acqName",null);
			maps.put("serviceName",null);
			maps.put("dayAmount",null);
			maps.put("merchantNo",null);
			maps.put("merchantName",null);
			maps.put("acqMerchantCode",null);
			maps.put("repPay",null);
			maps.put("acqMerchantType",null);
			maps.put("acqStatus",null);
			maps.put("createTime", null);
			data.add(maps);
		}else{
			//代付
			Map<String, String> repPayMap=new HashMap<String, String>();
			repPayMap.put("1","否");
			repPayMap.put("2","是");

			Map<String, String> sourceMap=sysDictService.selectMapByKey("acqMerSource");//收单商户来源途径
			Map<String, String> acqMerchantTypeMap=sysDictService.selectMapByKey("ACQ_MERCHANT_TYPE");//收单商户类别

			Map<String, String> acqStatusMap=new HashMap<String, String>();
			acqStatusMap.put("0","关闭");
			acqStatusMap.put("1","开启");

			for (AcqMerchant or : list) {
				Map<String, String> maps = new HashMap<String, String>();
				maps.put("id",or.getId()==null?"":or.getId().toString());
				maps.put("agentName",or.getAgentName());
				maps.put("acqMerchantNo",or.getAcqMerchantNo());
				maps.put("acqMerchantName",or.getAcqMerchantName());
				maps.put("source",sourceMap.get(or.getSource()));
				maps.put("acqName",or.getAcqName());
				maps.put("serviceName",or.getServiceName());
				maps.put("dayAmount",or.getDayAmount()==null?"":or.getDayAmount().toString());
				maps.put("merchantNo",or.getMerchantNo());
				maps.put("merchantName",or.getMerchantName());
				maps.put("acqMerchantCode",or.getAcqMerchantCode());
				maps.put("repPay",repPayMap.get(or.getRepPay()==null?"":or.getRepPay().toString()));
				maps.put("acqMerchantType",acqMerchantTypeMap.get(or.getAcqMerchantType()==null?"":or.getAcqMerchantType().toString()));
				maps.put("acqStatus",acqStatusMap.get(or.getAcqStatus()==null?"":or.getAcqStatus().toString()));
				maps.put("createTime", or.getCreateTime()==null?"":sdf1.format(or.getCreateTime()));
				data.add(maps);
			}
		}
		ListDataExcelExport export = new ListDataExcelExport();
		String[] cols = new String[]{"id","agentName","acqMerchantNo","acqMerchantName","source","acqName",
				"serviceName","dayAmount","merchantNo","merchantName","acqMerchantCode","repPay","acqMerchantType",
				"acqStatus","createTime"
		};
		String[] colsName = new String[]{"序号","代理商名称","收单机构商户编号","收单机构商户名称","来源途径","收单机构",
				"收单服务","今日收单金额","商户编号","商户名称","收单机构对应收单商户进件编号","代付","类别",
				"状态","创建时间"
		};
		OutputStream ouputStream =null;
		try {
			ouputStream=response.getOutputStream();
			export.export(cols, colsName, data, response.getOutputStream());
		}catch (Exception e){
			log.error("导出收单机构商户列表失败!",e);
		}finally {
			if(ouputStream!=null){
				ouputStream.close();
			}
		}


	}

	@Override
	public Map<String, Object> acqMerBatchColseimport(MultipartFile file) throws Exception{
		Map<String, Object> msg = new HashMap<>();
		Workbook wb = WorkbookFactory.create(file.getInputStream());
		//读取第一个sheet
		Sheet sheet = wb.getSheetAt(0);
		// 遍历所有单元格，读取单元格
		int row_num = sheet.getLastRowNum();

		List<Map<String,Object>> errorList=new ArrayList<Map<String,Object>>();
		List<String> acqMerNoList=new ArrayList<>();//校验
		List<String> updateList=new ArrayList<String>();//需要更新的

		for (int i = 1; i <= row_num; i++) {
			Row row = sheet.getRow(i);
			String acqMerchantNo1 = CellUtil.getCellValue(row.getCell(0));//收单机构商户编号

			Map<String,Object> errorMap=new HashMap<String,Object>();
			int rowNum=i+1;
			if(acqMerchantNo1==null||"".equals(acqMerchantNo1)){
				errorMap.put("msg","第"+rowNum+"行,收单机构商户编号为空!");
				errorList.add(errorMap);
				continue;
			}
			String acqMerchantNo=acqMerchantNo1.split("\\.")[0];

			//验证重复
			if(acqMerNoList.contains(acqMerchantNo)){
				errorMap.put("msg","第"+rowNum+"行,收单机构商户编号("+acqMerchantNo+")重复!");
				errorList.add(errorMap);
				continue;
			}else{
				acqMerNoList.add(acqMerchantNo);
			}

			AcqMerchant old = acqMerchantDao.getAcqMerchantInfo(acqMerchantNo);
			if(old==null){
				errorMap.put("msg", "第"+rowNum+"行,收单机构商户编号("+acqMerchantNo+")不存在!");
				errorList.add(errorMap);
				continue;
			}else{
				if("0".equals(old.getAcqStatus()==null?"0":old.getAcqStatus().toString())){
					errorMap.put("msg", "第"+rowNum+"行,收单机构商户编号("+acqMerchantNo+")已经是关闭状态,不需要处理!");
					errorList.add(errorMap);
					continue;
				}else{
					updateList.add(acqMerchantNo);
				}
			}
		}
		int num=0;
		if(updateList!=null&&updateList.size()>0){
			for(String str:updateList){
				num=num+acqMerchantDao.updateInfoStatus(0,str);
			}
		}
		msg.put("errorList", errorList);
		msg.put("status", true);
		msg.put("msg", "导入成功,总共"+row_num+"条数据,成功关闭"+num+"个收单机构商户,失败"+errorList.size()+"条数据!");
		return msg;
	}


}
