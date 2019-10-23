package cn.eeepay.framework.service.bill.impl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.bill.SettleTransferMapper;
import cn.eeepay.framework.model.bill.SettleTransfer;
import cn.eeepay.framework.model.bill.SettleTransferFile;
import cn.eeepay.framework.service.bill.GenericTableService;
import cn.eeepay.framework.service.bill.SettleTransferFileService;
import cn.eeepay.framework.service.bill.SettleTransferService;



@Service("settleTransferService")
@Transactional
public class SettleTransferServiceImpl implements SettleTransferService{
	
	@Resource
	public SettleTransferMapper settleTransferMapper;
	@Resource
	public SettleTransferFileService settleTransferFileService;
	@Resource
	public  GenericTableService  genericTableService;
	private static final int SETTLE_TRANSFER_BATCH_NUM=200;
	
	private static final Logger log = LoggerFactory.getLogger(SettleTransferServiceImpl.class);

	@Override
	public int insertSettleTransfer(SettleTransfer st) throws Exception {
		return settleTransferMapper.insertSettleTransfer(st);
	}

	@Override
	public int updateSettleTransferById(SettleTransfer st) throws Exception {
		return settleTransferMapper.updateSettleTransferById(st);
	}

	private int saveSettleTransfer(SettleTransfer st,List<Map<String, String>> list) throws Exception {
		 int resultCount = 0;
		    for (Map<String, String> dataMap : list) {
		    	st.setInAccNo(dataMap.get("inAccNo"));
		    	st.setInAccName(dataMap.get("inAccName"));
		    	st.setInSettleBankNo(dataMap.get("inSettleBankNo"));
		    	st.setInBankNo(dataMap.get("inBankNo"));
		    	st.setAmount(new BigDecimal(dataMap.get("amount")));
		    	st.setInBankName(dataMap.get("inBankName"));
		    	st.setStatus("0");
		    	st.setErrCode("");
		    	st.setErrMsg("");
		    	st.setBak1(dataMap.get("bak1"));
		    	st.setBak2(dataMap.get("bak2"));
		      int temp = this.insertSettleTransfer(st);
		      resultCount += temp;
		    }
		return resultCount;
	}
	@Override
	public Map<String,Object> saveBatchSettleTransfer(SettleTransferFile stf,List<Map<String, String>> list,String fileName,String totalNum) throws Exception {
//	    String fileId="";
		Map<String,Object> result=new HashMap<String, Object>();
	    try {
//	      fileId = 
	    		  settleTransferFileService.insertSettleTransferFile(stf);
	    } catch (SQLException sqlException) {
	      log.error("操作数据库异常,请重试:"+sqlException.getMessage());
//	      model.addAttribute("status", "error");
//	      model.addAttribute("msg", "操作数据库异常,请重试:"+sqlException.getMessage());
	      result.put("state",false);
		  result.put("msg","导入对账异常！");
//	      return "settleTransfer/fileUpload";
			return result;
	    }
	    
	    int maxBatch=SETTLE_TRANSFER_BATCH_NUM;
	    int totalSaveCount=0;
	    int batchCount=Integer.parseInt(totalNum)/maxBatch;
	    int batchEndNum=Integer.parseInt(totalNum)%maxBatch;
	    if (batchEndNum!=0) {
	      batchCount++;
	    }
//	    settleParams.put("fileId", fileId);
	    
	    for (int i = 0; i < batchCount; i++) {
	      int batchWidth=maxBatch;
	      if (i==batchCount-1&&batchEndNum!=0) {
	        batchWidth=batchEndNum;
	      }
	      List<Map<String, String>> tempList=list.subList(maxBatch*i, maxBatch*i+batchWidth);
	      String batchId = genericTableService.createSettleTransferBatchId();
	      SettleTransfer st = new SettleTransfer();
	      st.setBatchId(batchId);
	      st.setFileId(stf.getId().toString());
	      st.setFileName(fileName);
	      st.setSettleBank(stf.getSettleBank());
	      st.setCreateTime(new Date());
//	      .put("batchId", batchId);
	      int saveCount=this.saveSettleTransfer(st, tempList);
	      totalSaveCount+=saveCount;
	    }
	    if (totalSaveCount==0) {
//	      model.addAttribute("status", "error");
//	      model.addAttribute("msg", "保存数据失败");
	      result.put("state",false);
		  result.put("msg","保存数据失败");
	    }else {
//	      model.addAttribute("status", "saveSuccess");
//	      model.addAttribute("msg", "成功保存"+totalSaveCount+"条数据");
	      result.put("state",true);
		  result.put("msg","保存成功，保存"+totalSaveCount+"条数据");
	    }	
	    return result;
	}

	
}
