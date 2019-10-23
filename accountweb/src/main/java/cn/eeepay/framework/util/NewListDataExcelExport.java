package cn.eeepay.framework.util;

import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public class NewListDataExcelExport {

    private String[] cols;
    private String[] colsName;
    private double[] cellWidth;
    private List<Map<String, String>> data;
    private XSSFWorkbook wb;
    private SXSSFSheet sheet;
    private SXSSFWorkbook sxssfWorkbook;
    private int rowIdx = 0;

    public NewListDataExcelExport(){
        wb = new XSSFWorkbook();
        sxssfWorkbook = new SXSSFWorkbook(wb, 100);
        sheet = sxssfWorkbook.createSheet("Sheet1");
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
        sxssfWorkbook.write(outputStream);
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
        SXSSFCell cell = null;
        SXSSFRow row = null;
        int cols = this.colsName.length;
        XSSFCellStyle headStyle = createHeaderStyle(wb);
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
            cell.setCellType(XSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(this.colsName[i]);
        }
    }

    private void writeData() throws IOException {
        Object cellValue;
        XSSFCellStyle bodyStyle = createBodyStyle(wb);
        for (int i = 0; i < data.size(); i++) {
            // 创建行
            SXSSFRow row = sheet.createRow(++rowIdx);
            for (short j = 0; j < this.cols.length; j++) {
                SXSSFCell cell = row.createCell(j);
                // 定义单元格为字符串类型,不设置默认为“常规”
                cell.setCellType(XSSFCell.CELL_TYPE_STRING);
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
    private XSSFCellStyle createHeaderStyle(XSSFWorkbook wb) {

        // 设置字体
        XSSFFont font = wb.createFont();
        font.setFontHeightInPoints((short) 12); // 字体高度
        font.setColor(XSSFFont.COLOR_NORMAL); // 字体颜色
        font.setFontName("黑体"); // 字体
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD); // 宽度

        // 设置单元格样式
        XSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER); // 水平布局：居中
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
    private XSSFCellStyle createBodyStyle(XSSFWorkbook wb) {

        // 设置单元格样式
        XSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(XSSFCellStyle.ALIGN_LEFT); // 水平布局：居中
        // 边框
        cellStyle.setBorderTop((short) 1);
        cellStyle.setBorderBottom((short) 1);
        cellStyle.setBorderLeft((short) 1);
        cellStyle.setBorderRight((short) 1);

        cellStyle.setWrapText(true);
        return cellStyle;
    }

}
