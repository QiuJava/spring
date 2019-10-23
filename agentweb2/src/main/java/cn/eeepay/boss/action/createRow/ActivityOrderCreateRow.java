package cn.eeepay.boss.action.createRow;

import cn.eeepay.framework.model.RedemOrderBean;
import cn.eeepay.framework.util.CreateRow;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 666666 on 2018/5/8.
 */
@Component
public class ActivityOrderCreateRow extends CreateRow<RedemOrderBean> {
    private static final Map<String, String> orderStatusMap = new HashMap<>();
    private static final Map<String, String> accStatusMap = new HashMap<>();

    static {
        orderStatusMap.put("INIT", "初始化");
        orderStatusMap.put("SUCCESS", "成功");
        orderStatusMap.put("FAILED", "失败");
        orderStatusMap.put("UNKNOW", "未知");

        accStatusMap.put("0", "未记账");
        accStatusMap.put("1", "记账成功");
        accStatusMap.put("2", "记账失败");
        accStatusMap.put("3", "已提交记账");
    }

    @Override
    public void writeRow(Row row, RedemOrderBean order) {
        int index = 0;
        row.createCell(index ++).setCellValue(order.getOrderNo());  //        orderNo       订单号
        row.createCell(index ++).setCellValue(order.getOemNo());  //        oemNo       组织ID
        row.createCell(index ++).setCellValue(order.getOemName());  //        oemName       组织名称
        row.createCell(index ++).setCellValue(orderStatusMap.get(order.getOrderStatus()));  //        orderStatus   订单状态
        row.createCell(index ++).setCellValue(order.getMerchantNo());  //        merchantNo    贡献人ID
        row.createCell(index ++).setCellValue(order.getUserName());  //        userName      贡献人名称
        row.createCell(index ++).setCellValue(order.getMobile());  //        mobile        贡献人手机号
        row.createCell(index ++).setCellValue(order.getAmount());  //        amount        售价
        row.createCell(index ++).setCellValue(order.getProvideAmout());  //        provideAmout  发放奖金
        row.createCell(index ++).setCellValue(order.getCreateTime());  //        createTime    创建时间
        row.createCell(index ++).setCellValue(order.getPayTime());  //        payTime       支付时间
        row.createCell(index ++).setCellValue(order.getPayOrderNo());  //        payOrderNo    关联支付订单
        row.createCell(index ++).setCellValue(order.getOemShare());  //        oemShare      品牌商分润
        row.createCell(index ++).setCellValue(accStatusMap.get(order.getAccStatus()));  //        accStatus     记账状态
    }
}
