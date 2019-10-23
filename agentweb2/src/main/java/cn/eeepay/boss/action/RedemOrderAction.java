package cn.eeepay.boss.action;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.RedemOrderBean;
import cn.eeepay.framework.model.ResponseBean;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.service.RedemOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 积分兑换订单查询
 * Created by 666666 on 2018/5/7.
 */
@Controller
@RequestMapping("/redemOrder")
public class RedemOrderAction {

    @Resource
    private RedemOrderService redemOrderService;
    @Resource
    private AgentInfoService agentInfoService;
//    @Resource
//    private ActivityOrderCreateRow activityOrderCreateRow;

    @RequestMapping("/listActivityOrder")
    @ResponseBody
    public ResponseBean listActivityOrder(@RequestBody RedemOrderBean redemOrderBean,
                                         @RequestParam(defaultValue = "1") int pageNo,
                                         @RequestParam(defaultValue = "10") int pageSize){
        try {
            Page<RedemOrderBean> page = new Page<>(pageNo, pageSize);
            AgentInfo loginAgent = agentInfoService.selectByPrincipal();
            List<RedemOrderBean> result = redemOrderService.listActivityOrder(redemOrderBean, loginAgent, page);
            return new ResponseBean(result, page.getTotalCount());
        }catch (Exception e){
            return new ResponseBean(e);
        }
    }
    @RequestMapping("/summaryActivityOrder")
    @ResponseBody
    public ResponseBean summaryActivityOrder(@RequestBody RedemOrderBean redemOrderBean){
        try {
            AgentInfo loginAgent = agentInfoService.selectByPrincipal();
            Map<String, Object> result = redemOrderService.summaryActivityOrder(redemOrderBean, loginAgent);
            return new ResponseBean(result);
        }catch (Exception e){
            return new ResponseBean(e);
        }
    }
//    @RequestMapping("/exportActivityOrder")
//    public void exportActivityOrder(RedemOrderBean redemOrderBean, HttpServletResponse response) throws IOException {
//        Page<ResponseBean> page = new Page<>(1, Integer.MAX_VALUE);
//        AgentInfo loginAgent = agentInfoService.selectByPrincipal();
//        List<RedemOrderBean> result = redemOrderService.listActivityOrder(redemOrderBean, loginAgent, page);
//        String fileName = "授权订单查询" + new SimpleDateFormat("YYYYMMddHHmmss").format(new Date()) + ".xlsx";
//        String[] columnNames = 	{"订单号","组织ID", "组织名词", "订单状态", "贡献人ID", "贡献人名称",
//                "贡献人手机号", "售价", "发放奖金", "创建时间", "支付时间",
//                "关联支付订单", "品牌商分润", "记账状态"};
//        Workbook workbook = ExcelUtils.createWorkBook("授权订单查询", result, columnNames, activityOrderCreateRow);
//        response.reset();
//        response.setContentType("application/vnd.ms-excel;charset=utf-8");
//        response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1"));
//        workbook.write(response.getOutputStream());
//        response.flushBuffer();
//    }

    @RequestMapping("/listDeclareOrder")
    @ResponseBody
    public ResponseBean listDeclareOrder(@RequestBody RedemOrderBean redemOrderBean,
                                         @RequestParam(defaultValue = "1") int pageNo,
                                         @RequestParam(defaultValue = "10") int pageSize){
        try {
            Page<RedemOrderBean> page = new Page<>(pageNo, pageSize);
            AgentInfo loginAgent = agentInfoService.selectByPrincipal();
            List<RedemOrderBean> result = redemOrderService.listDeclareOrder(redemOrderBean, loginAgent, page);
            return new ResponseBean(result, page.getTotalCount());
        }catch (Exception e){
            return new ResponseBean(e);
        }
    }

    @RequestMapping("/summaryDeclareOrder")
    @ResponseBody
    public ResponseBean summaryDeclareOrder(@RequestBody RedemOrderBean redemOrderBean){
        try {
            AgentInfo loginAgent = agentInfoService.selectByPrincipal();
            Map<String, Object> result = redemOrderService.summaryDeclareOrder(redemOrderBean, loginAgent);
            return new ResponseBean(result);
        }catch (Exception e){
            return new ResponseBean(e);
        }
    }

    @RequestMapping("/listShareOrder")
    @ResponseBody
    public ResponseBean listShareOrder(@RequestBody RedemOrderBean redemOrderBean,
                                         @RequestParam(defaultValue = "1") int pageNo,
                                         @RequestParam(defaultValue = "10") int pageSize){
        try {
            Page<RedemOrderBean> page = new Page<>(pageNo, pageSize);
            AgentInfo loginAgent = agentInfoService.selectByPrincipal();
            List<RedemOrderBean> result = redemOrderService.listShareOrder(redemOrderBean, loginAgent, page);
            return new ResponseBean(result, page.getTotalCount());
        }catch (Exception e){
            return new ResponseBean(e);
        }
    }

    @RequestMapping("/listShareOrderSum")
    @ResponseBody
    public ResponseBean listShareOrderSum(@RequestBody RedemOrderBean redemOrderBean,
                                       @RequestParam(defaultValue = "1") int pageNo,
                                       @RequestParam(defaultValue = "10") int pageSize){
        try {
            Page<RedemOrderBean> page = new Page<>(pageNo, pageSize);
            AgentInfo loginAgent = agentInfoService.selectByPrincipal();
            Map<String,Object> map = redemOrderService.listShareOrderSum(redemOrderBean, loginAgent, page);
            return new ResponseBean(map);
        }catch (Exception e){
            return new ResponseBean(e);
        }
    }

    @RequestMapping("/listOrgCode")
    @ResponseBody
    public ResponseBean listOrgCode(){
        try {
            return new ResponseBean(redemOrderService.listOrgCode());
        }catch (Exception e){
            return new ResponseBean(e);
        }
    }
}
