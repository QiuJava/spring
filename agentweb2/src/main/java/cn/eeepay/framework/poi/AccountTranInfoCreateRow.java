package cn.eeepay.framework.poi;

import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.util.CreateRow;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 666666 on 2018/1/12.
 */
@Component
public class AccountTranInfoCreateRow extends CreateRow<Map<String, Object>>{

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat sdf2 = new SimpleDateFormat(" HH:mm:ss");

    @Resource
    private AgentInfoService agentInfoService;
    private Map<String, String> subjectNoMap = new HashMap<>();
    private Map<String, String> debitCreditSideMap = new HashMap<>();
    @PostConstruct
    public void init(){
//        List<Map<String, String>> mapList = agentInfoService.selectSubjectNo();
//        for (Map<String, String> map : mapList){
//            subjectNoMap.put(map.get("sys_value"), map.get("sys_name"));
//        }
        debitCreditSideMap.put("credit", "增加");
        debitCreditSideMap.put("debit", "减少");
        debitCreditSideMap.put("freeze", "冻结");
        debitCreditSideMap.put("unFreeze", "解冻");
    }

    @Override
    public void beforeWrite() {
        subjectNoMap.clear();
        List<Map<String, String>> mapList = agentInfoService.selectSubjectNo();
        for (Map<String, String> map : mapList){
            subjectNoMap.put(map.get("sys_value"), map.get("sys_name"));
        }
    }

    @Override
    public void writeRow(Row row, Map<String, Object> map) {
        int index = 0;
        row.createCell(index ++).setCellValue(subjectNoMap.get(trimToEmpty(map.get("subjectNo"))));    //账户类别
        row.createCell(index ++).setCellValue(trimToEmpty(map.get("transOrderNo")));    //订单编号
        String dateTime = trimToDateFormat(map.get("recordDate"), sdf) + trimToDateFormat(map.get("recordTime"), sdf2);
        row.createCell(index ++).setCellValue(dateTime);    //记账时间
        row.createCell(index ++).setCellValue(trimToEmpty(map.get("transTypeCn")));    //交易类型
        row.createCell(index ++).setCellValue(debitCreditSideMap.get(trimToEmpty(map.get("debitCreditSide"))));    //操作
        row.createCell(index ++).setCellValue(trimToEmpty(map.get("recordAmount")));    //金额
        row.createCell(index ++).setCellValue(trimToEmpty(map.get("balance")));    //账户余额
        row.createCell(index ++).setCellValue(trimToEmpty(map.get("avaliBalance")));    //可用余额
        row.createCell(index ++).setCellValue(trimToEmpty(map.get("summaryInfo")));    //摘要
    }
}
