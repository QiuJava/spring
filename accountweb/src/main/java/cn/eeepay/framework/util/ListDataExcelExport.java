package cn.eeepay.framework.util;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddressList;

public class ListDataExcelExport {

	private String[] cols;
	private String[] colsName;
	private double[] cellWidth;
	private List<Map<String, String>> data;
	private HSSFWorkbook wb;
	private HSSFSheet sheet;
	private int rowIdx = 0;

	public ListDataExcelExport() {
		wb = new HSSFWorkbook();
		sheet = wb.createSheet("Sheet1");
	}

	public String getFileSuffix() {
		return "xls";
	}

	public void export(String[] cols, String[] colsName,double[] cellWidth, List<Map<String, String>> data, OutputStream outputStream)
			throws IOException {
		this.cols = cols;
		this.data = data;
		this.colsName = colsName;
		this.cellWidth = cellWidth;
		builTitle();
		writeData();
		wb.write(outputStream);
		outputStream.flush();
	}
	
	public void export(String[] cols, String[] colsName, List<Map<String, String>> data, OutputStream outputStream) throws IOException {
		export(cols, colsName, null, data, outputStream);
	}


	/**
	 * 构建表格表头
	 * 
	 * @throws IOException
	 */
	private void builTitle() throws IOException {
		HSSFCell cell = null;
		HSSFRow row = null;
		int cols = this.colsName.length;
		HSSFCellStyle headStyle = createHeaderStyle(wb);
		row = sheet.createRow(rowIdx);
		row.setHeightInPoints((short) 25);
		for (short i = 0; i < cols; i++) {
			double _cellWidth  = 6000 ;
			if (cellWidth != null) {
				_cellWidth = cellWidth[i];
			}
			sheet.setColumnWidth(i, (short) _cellWidth);
			cell = row.createCell(i);
			cell.setCellStyle(headStyle);
			// 定义单元格为字符串类型,不设置默认为“常规”
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(this.colsName[i]);
		}
	}

	private void writeData() throws IOException {
		Object cellValue;
		HSSFCellStyle bodyStyle = createBodyStyle(wb);
		for (int i = 0; i < data.size(); i++) {
			// 创建行
			HSSFRow row = sheet.createRow(++rowIdx);
			for (short j = 0; j < this.cols.length; j++) {
				HSSFCell cell = row.createCell(j);
				// 定义单元格为字符串类型,不设置默认为“常规”
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellStyle(bodyStyle);
				cellValue = data.get(i).get(this.cols[j]);
				cell.setCellValue(cellValue == null ? "" : cellValue.toString());
			}
		}
	}

	/**
	 * 构建表格列头样式
	 * 
	 * @param wb
	 * @return
	 */
	private HSSFCellStyle createHeaderStyle(HSSFWorkbook wb) {

		// 设置字体
		HSSFFont font = wb.createFont();
		font.setFontHeightInPoints((short) 12); // 字体高度
		font.setColor(HSSFFont.COLOR_NORMAL); // 字体颜色
		font.setFontName("黑体"); // 字体
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 宽度

		// 设置单元格样式
		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setFont(font);
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平布局：居中
		// 边框
		cellStyle.setBorderTop((short) 1);
		cellStyle.setBorderBottom((short) 1);
		cellStyle.setBorderLeft((short) 1);
		cellStyle.setBorderRight((short) 1);

		cellStyle.setWrapText(true);
		return cellStyle;

	}

	/**
	 * 构建表格列样式
	 * 
	 * @param wb
	 * @return
	 */
	private HSSFCellStyle createBodyStyle(HSSFWorkbook wb) {

		// 设置单元格样式
		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 水平布局：居中
		// 边框
		cellStyle.setBorderTop((short) 1);
		cellStyle.setBorderBottom((short) 1);
		cellStyle.setBorderLeft((short) 1);
		cellStyle.setBorderRight((short) 1);

		cellStyle.setWrapText(true);
		return cellStyle;
	}

	
	/** 
     * 设置某些列的值只能输入预制的数据,显示下拉框. 
     * @param textlist 下拉框显示的内容 
     * @param firstRow 开始行 
     * @param endRow 结束行 
     * @param firstCol   开始列 
     * @param endCol  结束列 
     * @return 设置好的sheet. 
     */  
    public HSSFSheet setHSSFValidation(String[] textlist, int firstRow, 
    		int endRow, int firstCol,  
            int endCol) {  
        // 加载下拉列表内容  
        DVConstraint constraint = DVConstraint.createExplicitListConstraint(textlist);  
        // 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列  
        CellRangeAddressList regions = new CellRangeAddressList(firstRow,endRow, firstCol, endCol);  
        // 数据有效性对象  
        HSSFDataValidation data_validation_list = new HSSFDataValidation(regions, constraint);  
        sheet.addValidationData(data_validation_list);  
        return sheet;  
    }  
}
