package cn.eeepay.framework.util;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFFont;

public class BigExcel {

/*	public static void main(String[] args) throws Exception {
		OutputStream os = new FileOutputStream("D:/1.xlsx");
		
		String[] cols = {"a"};
		String[] colsName = {"A"}; 
		
		List<Map<String, String>> data = new ArrayList();
		Map<String, String> map = new HashMap<>();
		map.put("a", "1");
		data.add(map);
		
		export(cols,colsName,data,os);
		
		os.close();
	}*/

	public void export(String[] cols, String[] colsName, List<Map<String, String>> data, OutputStream os) {
		SXSSFWorkbook xWorkbook = null;
		try {
			if (data != null && data.size() > 0) {
				int rowaccess = 1000;
				int totle = data.size();
				int mus = 100000;
				int avg = totle / mus;
				xWorkbook = new SXSSFWorkbook(rowaccess);
				CellStyle cs = xWorkbook.createCellStyle();
				for (int i = 0; i < avg + 1; i++) {
					// set Sheet页名称
					Sheet sh = xWorkbook.createSheet("Sheet" + (i + 1));
					// set Sheet页头部
					setSheetHeader(xWorkbook, sh, colsName);
					int num = i * mus;
					int index = 0;
					for (int m = num; m < totle; m++) {
						if (index == mus) {
							break;
						}
						Row xRow = sh.createRow(m + 1 - mus * i);

						for (short j = 0; j < cols.length; j++) {
							Cell xCell = xRow.createCell(j);
							cs.setWrapText(true); // 是否自动换行
							xCell.setCellStyle(cs); // 设置表格样式
							xCell.setCellType(Cell.CELL_TYPE_STRING);
							xCell.setCellValue(data.get(m).get(cols[j]));
						}
						index++;
						// 每当行数达到设置的值就刷新数据到硬盘,以清理内存
						if (m % rowaccess == 0) {
							((SXSSFSheet) sh).flushRows();
						}
					}
				}

			}

			xWorkbook.write(os);
			os.flush();
			xWorkbook.dispose();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != os) {
				try {
					os.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (null != xWorkbook) {
				try {
					xWorkbook.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

	private void setSheetHeader(SXSSFWorkbook xWorkbook, Sheet sh, String[] colsName) {
		CellStyle cs = xWorkbook.createCellStyle();
		// 设置水平垂直居中
		cs.setAlignment(CellStyle.ALIGN_CENTER);
		cs.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// 设置字体
		Font headerFont = xWorkbook.createFont();
		headerFont.setFontHeightInPoints((short) 12);
		headerFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		headerFont.setFontName("宋体");
		cs.setFont(headerFont);
		
		cs.setWrapText(true); // 是否自动换行
		Row xRow0 = sh.createRow(0);
		for (int i = 0; i < colsName.length; i++) {
			sh.setColumnWidth(i, 20 * 156);
			Cell xCell = xRow0.createCell(i);
			xCell.setCellType(Cell.CELL_TYPE_STRING);
			xCell.setCellValue(colsName[i]);
		}
	}
}
