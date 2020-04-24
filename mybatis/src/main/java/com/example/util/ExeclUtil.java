package com.example.util;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * 
 * @author Qiu Jian
 *
 */
public class ExeclUtil {

	private ExeclUtil() {
	}

	@SuppressWarnings("resource")
	public static void exportExecl(HttpServletResponse response, String sheetName, List<Map<String, Object>> dataList,
			String[] headerColumnNameArray, String[] columnPropertyNameArray, String fileName) throws IOException {

		HSSFWorkbook workBook = new HSSFWorkbook();
		HSSFSheet newSheet = workBook.createSheet(sheetName);

		HSSFRow topRow = newSheet.createRow(0);
		for (int i = 0; i < headerColumnNameArray.length; i++) {
			String headerColumnName = headerColumnNameArray[i];
			HSSFCell newCell = topRow.createCell(i);
			ExeclUtil.setCellValue(newCell, headerColumnName);
		}

		for (int i = 0; i < dataList.size(); i++) {
			HSSFRow newRow = newSheet.createRow(i + 1);
			Map<String, Object> map = dataList.get(i);
			for (int j = 0; j < columnPropertyNameArray.length; j++) {
				HSSFCell newCell = newRow.createCell(j);
				String columnPropertyName = columnPropertyNameArray[j];
				ExeclUtil.setCellValue(newCell, map.get(columnPropertyName));
			}
		}

		response.setHeader("Content-disposition", "attachment;filename=" + fileName);
		ServletOutputStream outputStream = response.getOutputStream();
		workBook.write(outputStream);
		outputStream.flush();
		outputStream.close();
	}

	private static void setCellValue(HSSFCell newCell, Object obj) {
		if (obj instanceof Boolean) {
			newCell.setCellValue((Boolean) obj);
		} else if (obj instanceof Date) {
			newCell.setCellValue((Date) obj);
		} else {
			newCell.setCellValue(new HSSFRichTextString(obj.toString()));
		}
	}

}
