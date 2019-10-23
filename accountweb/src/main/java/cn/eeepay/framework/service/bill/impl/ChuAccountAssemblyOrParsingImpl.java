package cn.eeepay.framework.service.bill.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import cn.eeepay.framework.model.bill.SubOutBillDetail;
import cn.eeepay.framework.service.bill.ChuAccountAssemblyOrParsing;

@Service("chuAccountAssemblyOrParsing")
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class ChuAccountAssemblyOrParsingImpl  implements ChuAccountAssemblyOrParsing {
	private static final Logger log = LoggerFactory.getLogger(ChuAccountAssemblyOrParsingImpl.class);

	@Override
	public Map<String, Object> resolveOutBillFile(File temp) {
		
		Map<String,Object> map = new HashMap<String, Object>();
		List<SubOutBillDetail> subOutBillDetails = new ArrayList<SubOutBillDetail>();
		Workbook workbook = null;
		try {  
		    workbook = WorkbookFactory.create(temp);
		    Sheet sheet = workbook.getSheetAt(0);
		    int rowCount = sheet.getLastRowNum() + 1;
		    log.info("导入账单的行数是：" + rowCount);
		    //从第2行开始逐条
			for(int i = 1; i < rowCount; i++){
				Row row = sheet.getRow(i);
				if (row == null) {
					break;
				}
				Cell cell0 = row.getCell(0); //订单参考号
				Cell cell1 = row.getCell(1);  //商户号
				SubOutBillDetail subOutBillDetail = new SubOutBillDetail();
				if(!StringUtils.isEmpty(getStringCell(cell0))){
					subOutBillDetail.setOrderReferenceNo(getStringCell(cell0));
					subOutBillDetail.setMerchantNo(getStringCell(cell1));
					subOutBillDetails.add(subOutBillDetail);
				}
				
			}
		}  
		catch (FileNotFoundException e)  {  
			//e.printStackTrace();  
			log.error("异常 " + e.getMessage());
		}  
		catch (IOException e)  {  
			//e.printStackTrace(); 
			log.error("异常 " + e.getMessage());
		}  
		catch (Exception e)  {  
			//e.printStackTrace();  
			log.error("异常 " + e.getMessage());
		} 
		map.put("subOutBillDetails", subOutBillDetails);
		return map;
	}

	private static String getStringCell(Cell cell) {
		if (cell != null)
			cell.setCellType(Cell.CELL_TYPE_STRING);
		return cell != null ? cell.getStringCellValue() : null;
	}
}
