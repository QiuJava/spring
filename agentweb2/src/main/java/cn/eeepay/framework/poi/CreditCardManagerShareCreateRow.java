package cn.eeepay.framework.poi;

import cn.eeepay.framework.model.CreditCardManagerShare;
import cn.eeepay.framework.util.CreateRow;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@Component
public class CreditCardManagerShareCreateRow extends CreateRow<CreditCardManagerShare> {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static Map<String, String> enterAccountStatusMap = new HashMap<>(); //入账状态
    private static Map<String, String> orderTypeMap = new HashMap<>();          //订单类型
    static {
        enterAccountStatusMap.put("1", "未入账");
        enterAccountStatusMap.put("2", "已入账");
        enterAccountStatusMap.put("3", "入账失败");

        orderTypeMap.put("1", "会员服务费");
        orderTypeMap.put("2", "其他");
    }

    @Override
    public void beforeWrite() {
        super.beforeWrite();
    }

    @Override
    public void writeRow(Row row, CreditCardManagerShare creditCardManagerShare) {
        int index = 0;
        String createDate = sdf.format(creditCardManagerShare.getCreateDate());
        String enterDate = sdf.format(creditCardManagerShare.getEnterDate());
        row.createCell(index ++).setCellValue(createDate);                                          //分润创建时间
        row.createCell(index ++).setCellValue(creditCardManagerShare.getShareCash().toString());    //分润金额
        row.createCell(index ++).setCellValue(creditCardManagerShare.getSharePercentage()+"%");         //分润百分比
        row.createCell(index ++).setCellValue(enterAccountStatusMap.get(creditCardManagerShare.getEnterStatus().toString()));             //入账状态
        row.createCell(index ++).setCellValue(creditCardManagerShare.getShareAgentName());          //分润代理商名称
        row.createCell(index ++).setCellValue(creditCardManagerShare.getBelongAgentNo());           //分润代理商编号
        row.createCell(index ++).setCellValue(creditCardManagerShare.getRelatedOrderNo());          //关联订单号
        row.createCell(index ++).setCellValue(creditCardManagerShare.getOrderCash().toString());    //订单金额
        row.createCell(index ++).setCellValue(orderTypeMap.get(creditCardManagerShare.getOrderType().toString()));               //订单类型
        row.createCell(index ++).setCellValue(creditCardManagerShare.getUserId());                  //用户ID
        row.createCell(index ++).setCellValue(creditCardManagerShare.getBelongAgentNo());           //所属代理商编号
        row.createCell(index ++).setCellValue(creditCardManagerShare.getBelongAgentName());         //所属代理商名称
        row.createCell(index ++).setCellValue(enterDate);                                           //入账时间
    }
}
