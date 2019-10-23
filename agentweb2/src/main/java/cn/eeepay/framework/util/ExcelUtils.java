package cn.eeepay.framework.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/5/24.
 */
public class ExcelUtils {
    public static <T> Workbook createWorkBook(String sheetName, List<T> obj, String columnName[], CreateRow<T> createRow){
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet(sheetName);
        createRow.setColumnWidth(sheet, columnName.length);

        Row headRow = sheet.createRow(0);
        createRow.configHeadRowStyle(headRow);

        Font headFont = wb.createFont();
        CellStyle headCellStyle = wb.createCellStyle();
        createRow.configHeadRowCellStyle(headFont, headCellStyle);

        Font rowFont = wb.createFont();
        CellStyle oddCellStyle = wb.createCellStyle();
        CellStyle evenCellStyle = wb.createCellStyle();
        createRow.configRowCellStyle(rowFont, oddCellStyle, evenCellStyle);

        for (int i = 0; i < columnName.length; i ++){
            Cell cell = headRow.createCell(i);
            cell.setCellValue(columnName[i]);
            cell.setCellStyle(headCellStyle);
        }
        if (!CollectionUtils.isEmpty(obj)){
            createRow.beforeWrite();
            for (int i = 1; i <= obj.size(); i ++){
                Row row = sheet.createRow(i);
                createRow.configRowStyle(i, row);
                createRow.writeRow(row, obj.get(i - 1));
                for (int j = 0; j < row.getLastCellNum(); j ++){
                    row.getCell(j).setCellStyle((i % 2 == 0) ? evenCellStyle : oddCellStyle);
                }
            }
            createRow.afterWrite();
        }
        return wb;
    }
}
