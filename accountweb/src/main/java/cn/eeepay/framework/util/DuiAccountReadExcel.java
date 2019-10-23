package cn.eeepay.framework.util;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: 长沙张学友
 * Date: 2018/8/14
 * Time: 14:18
 * Description: 读取excel类型的对账文件
 */
public class DuiAccountReadExcel {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     *
     * @param inputStream
     * @param jumpRow   //指定从第几行开始读取，因为对账文件都有表头
     * @throws IOException
     */
    public static List<List<String>> read(InputStream inputStream,int jumpRow) throws IOException {
        jumpRow = jumpRow - 1;      //如果指定从第三行开始读取，POI操作是下标0开始所以需要减1
        List<List<String>> list = new ArrayList<>();
        XSSFWorkbook work = new XSSFWorkbook(inputStream);// 得到这个excel表格对象
        XSSFSheet sheet = work.getSheetAt(0);           //得到第一个sheet
        int rowNo = sheet.getLastRowNum();      //得到行数
        for (int i = jumpRow; i <= rowNo; i++) {
            XSSFRow row = sheet.getRow(i);
            if(row!=null){
                int columnNum = row.getPhysicalNumberOfCells();  //获取总列数
                List<String> temp = new ArrayList<>();
                for(int j=0;j<columnNum;j++){
                    XSSFCell cell = row.getCell(j);
                    if(HSSFCell.CELL_TYPE_NUMERIC == cell.getCellType()){       //日期类型需要特殊处理
                        if(HSSFDateUtil.isCellDateFormatted(cell)){
                            String cellVal = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
                            temp.add(cellVal);
                            continue;
                        }
                    }
                    cell.setCellType(Cell.CELL_TYPE_STRING);        //将内容全部作为字符串读取
                    String cellVal = cell.getStringCellValue()==null?"":cell.getStringCellValue();
                    temp.add(cellVal);
                }
                list.add(temp);
            }
        }
        return list;
    }

}
