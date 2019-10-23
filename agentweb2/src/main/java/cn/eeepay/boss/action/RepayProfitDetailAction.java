package cn.eeepay.boss.action;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.RepayProfitDetailBean;
import cn.eeepay.framework.model.ResponseBean;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.service.RepayProfitDetailService;
import cn.eeepay.framework.util.CreateRow;
import cn.eeepay.framework.util.ExcelUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 666666 on 2017/11/20.
 */
@Controller
@RequestMapping(value = "/repayProfitDetail")
public class RepayProfitDetailAction {
    @Resource
    private RepayProfitDetailService repayProfitDetailService;
    @Resource
    private AgentInfoService agentInfoService;

    @RequestMapping("/listRepayProfitDetail")
    @ResponseBody
    public ResponseBean listRepayProfitDetail(@RequestBody RepayProfitDetailBean bean,
                                              Page<RepayProfitDetailBean> page){
        try {
            AgentInfo loginAgent = agentInfoService.selectByPrincipal();
            List<RepayProfitDetailBean> detailBeans = repayProfitDetailService.listRepayProfitDetail(bean, loginAgent, page);
//            RepayProfitDetailBean sum = repayProfitDetailService.sumRepayProfitDetail(bean, loginAgent);
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("resultList", detailBeans);
//            resultMap.put("sunProfitDate", sum);
            return new ResponseBean(resultMap, page.getTotalCount());
        }catch (Exception e){
            return new ResponseBean(e);
        }
    }
    @SystemLog(description = "计算信用卡还款分润")
    @RequestMapping("/calcShareAmount")
    @ResponseBody
    public ResponseBean calcShareAmount(@RequestBody RepayProfitDetailBean bean){
        try {
            AgentInfo loginAgent = agentInfoService.selectByPrincipal();
            Map<String, String> result = repayProfitDetailService.calcShareAmount(bean, loginAgent);
            return new ResponseBean(result);
        }catch (Exception e){
            return new ResponseBean(e);
        }
    }


    @RequestMapping("/exportRepayProfitDetail")
    public void exportRepayProfitDetail(RepayProfitDetailBean bean, HttpServletResponse response) throws IOException {


        AgentInfo loginAgent = agentInfoService.selectByPrincipal();

        List<RepayProfitDetailBean> detailBeans=repayProfitDetailService.exportRepayProfitDetail(bean, loginAgent);
        String fileName = "超级还款分润记录" + new SimpleDateFormat("YYYYMMdd_HHmmss").format(new Date());
        String[] columnNames = {"分润流水号","服务商编号","服务商名称","订单类型", "分润", "产生分润金额", "任务金额","保证金", "服务费","已消费总额","已还款总额","实际消费手续费",
                "实际还款手续费","关联还款订单", "订单用户","订单状态", "终态时间"};
        Workbook workbook = ExcelUtils.createWorkBook("交易记录", detailBeans, columnNames, createRepayProfitDetail());
        response.reset();
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String((fileName + ".xls").getBytes(), "ISO8859-1"));
        workbook.write(response.getOutputStream());
        response.flushBuffer();
    }

    private static CreateRow<RepayProfitDetailBean> repayProfitDetailCreateRow = null;
    private CreateRow<RepayProfitDetailBean> createRepayProfitDetail() {
        if (repayProfitDetailCreateRow != null){
            return repayProfitDetailCreateRow;
        }
        return repayProfitDetailCreateRow = new CreateRow<RepayProfitDetailBean>() {
            private Map<String, String> profitTypeMap = new HashMap<>();
            private final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            private Map<String, String> orderStatusMap = new HashMap<>();
            {
                profitTypeMap.put("1", "分期还款");
                profitTypeMap.put("2", "全额还款");
                profitTypeMap.put("3", "保证金");
                profitTypeMap.put("4", "完美还款");

                orderStatusMap.put("0", "初始化");
                orderStatusMap.put("1", "未执行");
                orderStatusMap.put("2", "还款中");
                orderStatusMap.put("3", "还款成功");
                orderStatusMap.put("4", "还款失败");
                orderStatusMap.put("5", "挂起");
                orderStatusMap.put("6", "终止");
            }
            @Override
            public void writeRow(Row row, RepayProfitDetailBean bean) {
                int index = 0;
                row.createCell(index ++).setCellValue(bean.getProfitNo());             //分润流水号
                row.createCell(index ++).setCellValue(bean.getProfitMerNo());          //服务商编号
                row.createCell(index ++).setCellValue(bean.getAgentName());            //服务商名称
                row.createCell(index ++).setCellValue(profitTypeMap.get(bean.getProfitType()));            //订单类型
                row.createCell(index ++).setCellValue(bean.getShareAmount());          //分润
                row.createCell(index ++).setCellValue(bean.getToProfitAmount());          //产生分润金额
                row.createCell(index ++).setCellValue(bean.getRepayAmount());          //任务金额
                row.createCell(index ++).setCellValue(bean.getEnsureAmount());         //保证金
                row.createCell(index ++).setCellValue(bean.getRepayFee());             //服务费
                row.createCell(index ++).setCellValue(bean.getSuccessPayAmount());     //已消费总额
                row.createCell(index ++).setCellValue(bean.getSuccessRepayAmount());   //已还款总额
                row.createCell(index ++).setCellValue(bean.getActualPayFee());         //实际消费手续费
                row.createCell(index ++).setCellValue(bean.getActualWithdrawFee());    //实际还款手续费
                row.createCell(index ++).setCellValue(bean.getBatchNo());              //关联还款订单
                row.createCell(index ++).setCellValue(bean.getMerchantNo());           //订单用户
                row.createCell(index ++).setCellValue(orderStatusMap.get(bean.getOrderStatus()));           //订单状态
//                row.createCell(index ++).setCellValue(bean.getOrderCreateTime() == null ? "" : DATE_FORMAT.format(bean.getOrderCreateTime()));      //订单时间
                row.createCell(index ++).setCellValue(bean.getCompleteTime() == null ? "" : DATE_FORMAT.format(bean.getCompleteTime()));      //终态时间
            }
        };
    }
}
