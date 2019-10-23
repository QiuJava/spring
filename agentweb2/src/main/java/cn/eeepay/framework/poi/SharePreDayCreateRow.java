package cn.eeepay.framework.poi;

import cn.eeepay.framework.util.CreateRow;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 666666 on 2018/1/12.
 */
@Component
public class SharePreDayCreateRow extends CreateRow<Map<String, Object>>{
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
    private static Map<String, String> enterAccountStatusMap = new HashMap<>();
    static {
        enterAccountStatusMap.put("ENTERACCOUNTED", "已入账");
        enterAccountStatusMap.put("NOENTERACCOUNT", "未入账");
    }
    @Override
    public void writeRow(Row row, Map<String, Object> map) {
        int index = 0;
        row.createCell(index ++).setCellValue(trimToDateFormat(map.get("transDate"), sdf2));    //交易日期
        row.createCell(index ++).setCellValue(trimToEmpty(map.get("agentNo")));    //代理商编号
        row.createCell(index ++).setCellValue(trimToEmpty(map.get("agentName")));    //代理商名称
        row.createCell(index ++).setCellValue(trimToEmpty(map.get("transTotalAmount")));    //交易总金额
        row.createCell(index ++).setCellValue(trimToEmpty(map.get("transTotalNum")));    //交易总笔数
        row.createCell(index ++).setCellValue(trimToEmpty(map.get("cashTotalNum")));    //提现总笔数
        row.createCell(index ++).setCellValue(trimToEmpty(map.get("merFee")));    //商户交易手续费
        row.createCell(index ++).setCellValue(trimToEmpty(map.get("merCashFee")));    //商户提现手续费
        row.createCell(index ++).setCellValue(trimToEmpty(map.get("preTransShareAmount")));    //原交易分润
        row.createCell(index ++).setCellValue(trimToEmpty(map.get("preTransCashAmount")));    //原提现分润
        row.createCell(index ++).setCellValue(trimToEmpty(map.get("adjustAmount")));    //调账金额
        row.createCell(index ++).setCellValue(trimToEmpty(map.get("adjustTransShareAmount")));    //调整后交易分润
        row.createCell(index ++).setCellValue(trimToEmpty(map.get("adjustTransCashAmount")));    //调整后提现分润
        row.createCell(index ++).setCellValue(trimToEmpty(map.get("adjustTotalShareAmount")));    //调整后总分润
        row.createCell(index ++).setCellValue(trimToEmpty(map.get("realEnterShareAmount")));    //实际到账分润
        row.createCell(index ++).setCellValue(trimToEmpty(map.get("freezeAmount")));    //冻结金额
        row.createCell(index ++).setCellValue(trimToDateFormat(map.get("enterAccountTime"), sdf));    //入账时间
        row.createCell(index ++).setCellValue(enterAccountStatusMap.get(trimToEmpty(map.get("enterAccountStatus"))));    //入账状态
    }

}
