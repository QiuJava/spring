/**
 * 版权 (c) 2014 深圳移付宝科技有限公司
 * 保留所有权利。
 */

package cn.eeepay.framework.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.read.biff.WorkbookParser;

/**
 * 描述：
 *
 * @author ym 
 * 创建时间：2014年7月8日
 */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class ExcelParseUtil {
	private static final Logger log = LoggerFactory.getLogger(ExcelParseUtil.class);
  /***
   * 
   * 功能：
   *
   * @param in excel文件流
   * @param head 解析的excel头字段数组，格式为【字段名:校验类型】，如果无需校验则【字段名】
   * @param list 解析后的数据集
   * @param errorList 校验信息集
   * @return rowCount 
   * @throws Exception
   */
  public static int  parserExcel(FileInputStream in,String[] head,List<Map<String, String>> list,List<String> errorList) throws Exception {
    try {
      Workbook workbook = WorkbookParser.getWorkbook(in);
      Sheet sheet=workbook.getSheet(0);
      int rowCount=sheet.getRows();
      int headLength=head.length;
      List<Map<String, String>> requirList=new ArrayList<Map<String,String>>();
      for (String headField : head) {
        String[] headFields=headField.split(":");
        Map<String, String> map=new HashMap<String, String>();
        if (headFields.length>0) {
          map.put("fieldName", headFields[0]);
          requirList.add(map);
        }
        if (headFields.length>1) {
          map.put("requirType", headFields[1]);
        }
       
      }
      
      // 第0行为字段标头
      everyRow:for (int i = 1; i < rowCount; i++) {
        Cell[] cells=sheet.getRow(i);
        
        Map<String, String> rowMap=new HashMap<String, String>();
        for (int j = 0; j < headLength; j++) {
          String fieldName=requirList.get(j).get("fieldName");
          String requirType=requirList.get(j).get("requirType");
          String content="";
          try {
            content=cells[j].getContents();
            content=StringUtil.trimChinese(content);
          } catch (Exception e) {
          }
     
          if (StringUtils.isNotEmpty(requirType)) {
            if ("string".equalsIgnoreCase(requirType)) {
              if (StringUtils.isEmpty(content)) {
                errorList.add("第"+i+"行数据【"+fieldName+"】为空,被忽略");
                continue everyRow;
              }
            }else if ("number".equalsIgnoreCase(requirType)) {
              if (!StringUtils.isNumeric(content)) {
                errorList.add("第"+i+"行数据【"+fieldName+"】不是数字,被忽略");
                continue everyRow;
              }
            }
          }
          
          rowMap.put(fieldName, content);
        }
          list.add(rowMap);
      }
        
      return rowCount;
    } catch (IOException e) {
      log.error("异常:",e);
      throw e;
    } catch (BiffException e) {
      log.error("异常:",e);
      throw e;
    }
  }
    
}
