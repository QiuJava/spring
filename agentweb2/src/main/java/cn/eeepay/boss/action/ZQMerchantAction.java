package cn.eeepay.boss.action;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.model.ZQMerchantInfo;
import cn.eeepay.framework.model.ZQTransOrderInfo;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.service.ZQMerchantService;
import cn.eeepay.framework.util.CreateRow;
import cn.eeepay.framework.util.ExcelUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/22.
 * 直清商户action
 */
@Controller
@RequestMapping(value = "/zqMerchantAction")
public class ZQMerchantAction {

    @Resource
    private ZQMerchantService zqMerchantService;
    @Resource
    private SysDictService sysDictService;

    @RequestMapping(value = "/queryZQMerchantInfo")
    @ResponseBody
    public Page<ZQMerchantInfo> queryZQMerchantInfo(@RequestBody ZQMerchantInfo zqMerchantInfo, int pageNo, int pageSize){
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String agentNo = principal.getUserEntityInfo().getEntityId();
        Page<ZQMerchantInfo> page = new Page<>();
        page.setPageNo(pageNo);
        page.setPageSize(pageSize);
        zqMerchantService.queryZQMerchantInfo(zqMerchantInfo, agentNo, page);
        return page;
    }
    @RequestMapping(value = "/queryZQTransOrder")
    @ResponseBody
    public Page<ZQTransOrderInfo> queryZQTransOrder(@RequestBody ZQTransOrderInfo zqTransOrderInfo, int pageNo, int pageSize){
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String agentNo = principal.getUserEntityInfo().getEntityId();
        Page<ZQTransOrderInfo> page = new Page<>();
        page.setPageNo(pageNo);
        page.setPageSize(pageSize);
        zqMerchantService.queryZQTransOrder(zqTransOrderInfo, agentNo, page);
        return page;
    }

    private CreateRow<ZQMerchantInfo> zqMerchantInfoCreateRow = null;
    private CreateRow<ZQTransOrderInfo> zqTransOrderInfoCreateRow = null;

    @RequestMapping("/exportZQMerchantInfo")
    public void exportZQMerchantInfo(ZQMerchantInfo zqMerchantInfo,HttpServletRequest request, HttpServletResponse response) throws Exception {
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String agentNo = principal.getUserEntityInfo().getEntityId();
        List<ZQMerchantInfo> list = zqMerchantService.exportZQMerchantInfo(zqMerchantInfo, agentNo);
        String fileName = "商户查询" + new SimpleDateFormat("YYYYMMddHHmmss").format(new Date());
        String[] columnNames = {"银联报备商户编号", "商户进件编号", "商户编号", "同步状态", "手机号", "商户名称", "失败原因", "创建时间"};
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Workbook workbook = ExcelUtils.createWorkBook("商户查询", list, columnNames, createZQMerchantInfoCreateRow());
        response.reset();
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String((fileName + ".xls").getBytes(), "ISO8859-1"));
        workbook.write(response.getOutputStream());
        response.flushBuffer();
    }

    @RequestMapping("/exportZQTransOrder")
    public void exportZQTransOrder(ZQTransOrderInfo zqTransOrderInfo, HttpServletRequest request, HttpServletResponse response) throws Exception {
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String agentNo = principal.getUserEntityInfo().getEntityId();
        List<ZQTransOrderInfo> list = zqMerchantService.exportZQTransOrder(zqTransOrderInfo, agentNo);
        String fileName = "交易查询" + new SimpleDateFormat("YYYYMMddHHmmss").format(new Date());
        String[] columnNames = {"商户名称", "手机号", "银联报备商户编号", "交易卡号", "金额(元)", "交易类型", "交易状态", "支付类型", "通道名称", "交易时间"};
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Workbook workbook = ExcelUtils.createWorkBook("交易查询", list, columnNames, createZQTransOrderInfoCreateRow(sysDictService));
        response.reset();
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String((fileName + ".xls").getBytes(), "ISO8859-1"));
        workbook.write(response.getOutputStream());
        response.flushBuffer();
    }

    private CreateRow<ZQMerchantInfo> createZQMerchantInfoCreateRow() {
        if (zqMerchantInfoCreateRow != null){
            return zqMerchantInfoCreateRow;
        }
        zqMerchantInfoCreateRow = new CreateRow<ZQMerchantInfo>() {
            private Map<String,String> syncStatusMap = new HashMap<>();
            {
                syncStatusMap.put("0", "初始化");
                syncStatusMap.put("1", "同步成功");
                syncStatusMap.put("2", "同步失败");
            }
            @Override
            public void writeRow(Row row, ZQMerchantInfo zqMerchantInfo) {
                row.createCell(0).setCellValue(zqMerchantInfo.getUnionpayMerNo());
                row.createCell(1).setCellValue(zqMerchantInfo.getBpId());
                row.createCell(2).setCellValue(zqMerchantInfo.getMerchantNo());
                row.createCell(3).setCellValue(syncStatusMap.get(zqMerchantInfo.getSyncStatus()));
                row.createCell(4).setCellValue(zqMerchantInfo.getMobilephone());
                row.createCell(5).setCellValue(zqMerchantInfo.getMerchantName());
                row.createCell(6).setCellValue(zqMerchantInfo.getSyncRemark());
                row.createCell(7).setCellValue(zqMerchantInfo.getCreateTime());
            }
        };
        return zqMerchantInfoCreateRow;
    }

    private CreateRow<ZQTransOrderInfo> createZQTransOrderInfoCreateRow(SysDictService sysDictService){
        if (zqTransOrderInfoCreateRow != null){
            return zqTransOrderInfoCreateRow;
        }
        zqTransOrderInfoCreateRow = new CreateRow<ZQTransOrderInfo>() {
            private Map<String,String> syncStatusMap = new HashMap<>();
            private Map<String,String> tranStatusMap = new HashMap<>();
            private Map<String,String> transTypeMap = new HashMap<>();
            private Map<String,String> payMethodMap = new HashMap<>();
            {
                syncStatusMap.put("0", "初始化");
                syncStatusMap.put("1", "同步成功");
                syncStatusMap.put("2", "同步失败");
            }
            //        String[] columnNames = {"商户名称", "手机号", "银联报备商户编号", "交易卡号", "金额(元)", "交易类型", "交易状态", "支付类型", "通道名称", "交易时间"};
            @Override
            public void writeRow(Row row, ZQTransOrderInfo zqTransOrderInfo) {
                row.createCell(0).setCellValue(zqTransOrderInfo.getMerchantName());
                row.createCell(1).setCellValue(zqTransOrderInfo.getMobilephone());
                row.createCell(2).setCellValue(zqTransOrderInfo.getUnionpayMerNo());
                row.createCell(3).setCellValue(zqTransOrderInfo.getAccountNo());
                row.createCell(4).setCellValue(zqTransOrderInfo.getTransAmount());
                row.createCell(5).setCellValue(transTypeMap.get(zqTransOrderInfo.getTransType()));
                row.createCell(6).setCellValue(tranStatusMap.get(zqTransOrderInfo.getTransStatus()));
                row.createCell(7).setCellValue(payMethodMap.get(zqTransOrderInfo.getPayMethod()));
                row.createCell(8).setCellValue(zqTransOrderInfo.getChannelCode());
                row.createCell(9).setCellValue(zqTransOrderInfo.getTransTime());
            }

            public CreateRow<ZQTransOrderInfo> accept(SysDictService sysDictService) {
                List<SysDict> tranStatus = sysDictService.selectTranStatusAllDict();
                List<SysDict> transType = sysDictService.selectTransTypeAllDict();
                List<SysDict> payMethod = sysDictService.selectPayMethodTypeAllDict();
                for (int i = 0;i < tranStatus.size(); i ++){
                    tranStatusMap.put(tranStatus.get(i).getSysValue(),tranStatus.get(i).getSysName());
                }
                for (int i = 0;i < transType.size(); i ++){
                    transTypeMap.put(transType.get(i).getSysValue(),transType.get(i).getSysName());
                }
                for (int i = 0;i < payMethod.size(); i ++){
                    payMethodMap.put(payMethod.get(i).getSysValue(),payMethod.get(i).getSysName());
                }
                return this;
            }
        }.accept(sysDictService);
        return zqTransOrderInfoCreateRow;
    }

}
