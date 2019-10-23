package cn.eeepay.boss.action;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.daoPerAgent.PaOrderDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.encryptor.md5.Md5;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.*;
import cn.eeepay.framework.util.HttpUtils;
import cn.eeepay.framework.util.ResponseUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author MXG
 * create 2018/07/14
 */
@Controller
@RequestMapping("/paOrder")
public class PaOrderAction {

    private static final Logger log = LoggerFactory.getLogger(PaOrderAction.class);
    @Resource
    private PaOrderService paOrderService;
    @Resource
    private AgentInfoService agentInfoService;
    @Resource
    private TerminalInfoService terminalInfoService;
    @Resource
    private SysDictService sysDictService;
    @Resource
    private PaOrderDao paOrderDao;
    @Resource
    private PerAgentService perAgentService;

    //接口调用成功
    private static final String SUCCESS = "200";

    /**
     * 机具申购订单查询列表
     *
     * @param orderInfo
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/selectPaOrderByParam")
    @ResponseBody
    public Map<String, Object> selectPaOrderByParam(
            @RequestBody PaOrder orderInfo,
            @RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "10") int pageSize) {

        Map<String, Object> msg = new HashMap<>();
        try {
            Page<PaOrder> page = new Page<>(pageNo, pageSize);
            msg = paOrderService.selectPaOrderByParam(page, orderInfo);
        } catch (Exception e) {
            msg.put("status", false);
            msg.put("msg", "查询失败");
            log.error("订单列表查询失败", e);
        }
        return msg;
    }

    /**
     * 选择发货机具
     *
     * @param info
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/selectTerminal")
    @ResponseBody
    public Map<String, Object> selectTerminal(
            @RequestBody TerminalInfo info,
            @RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "10") int pageSize) {

        Map<String, Object> msg = new HashMap<>();
        AgentInfo loginAgent = agentInfoService.selectByPrincipal();
        if (loginAgent == null) {
            msg.put("status", false);
            msg.put("msg", "请先登录！");
        }
        try {
            //从pa_agent_user查询登录代理商所对应的userCode
            //PaUserInfo user =  perAgentService.selectUserByAgentNo(loginAgent.getAgentNo());
            //String userCode =user.getLoginUserCode();
            String userCode = paOrderService.selectUserCodeByAgentNo(loginAgent.getAgentNo());
            //根据登录代理商的userCode从nposp的pa_ter_info获取对应的机具SN号，根据sn号从terminal_info中获取类型信息
            Page<PaOrder> page = new Page<>(pageNo, pageSize);
            List<TerminalInfo> terminals = terminalInfoService.selectByUserCode(page, userCode, info);
            //补充机具活动类型
            for (int j = 0; j < terminals.size(); j++) {
            	TerminalInfo terminal = terminals.get(j);
                String activityType = terminal.getActivityType();
                if (StringUtils.isNotBlank(activityType)) {
                    String[] types = activityType.split(",");
                    List<String> typeNames = sysDictService.findActivityTypeBy(types);
                    StringBuilder typeValues = new StringBuilder();
                    for (int i = 0; i < typeNames.size(); i++) {
                        typeValues.append(i == typeNames.size() - 1 ? typeNames.get(i) : typeNames.get(i) + ",");
                    }
                    terminal.setActivityTypeValues(typeValues.toString());
                }
            }
            msg.put("status", true);
            msg.put("page", page);
        } catch (Exception e) {
            msg.put("status", false);
            msg.put("msg", "查询失败");
            log.error("机具列表查询失败", e);
        }
        return msg;
    }

    /**
     * 查询发货机具
     * @param orderNo
     * @return
     */
    @RequestMapping("/selectSendTerminal")
    @ResponseBody
    public Map<String, Object> selectSendTerminal(
            @RequestParam String orderNo,
            @RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "10") int pageSize) {
        Map<String, Object> msg = new HashMap<>();
        AgentInfo loginAgent = agentInfoService.selectByPrincipal();
        if (loginAgent == null) {
            msg.put("status", false);
            msg.put("msg", "请先登录！");
        }
        try {
            Page<TerminalInfo> page = new Page<>(pageNo, pageSize);
            List<TerminalInfo> info = terminalInfoService.selectByOrderNo(page, orderNo, loginAgent.getAgentNode());
            if (info != null) {
                msg.put("status", true);
                msg.put("page", page);
            } else {
                msg.put("status", false);
                msg.put("msg", "未查询到发货信息");
            }

        } catch (Exception e) {
            e.printStackTrace();
            msg.put("status", false);
            msg.put("msg", "查询失败");
            log.error("机具列表查询失败", e);
        }
        return msg;
    }

    /**
     * 调用机具发货接口
     *
     * @param params
     * @return
     */
    @RequestMapping("/toSend")
    @ResponseBody
    public Map<String, Object> toSend(@RequestBody String params) {
        Map<String, Object> result = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(params);
        String user_code = jsonObject.getString("userCode");
        String order_no = jsonObject.getString("orderNo");
        String transport_company = jsonObject.getString("transportCompany");
        String post_no = jsonObject.getString("postNo");
        String sn_list_array = jsonObject.getString("sns");
        if (StringUtils.isBlank(user_code) || StringUtils.isBlank(order_no) ||
                StringUtils.isBlank(sn_list_array)) {
            result.put("status", false);
            result.put("msg", "必要参数不能为空");
            return result;
        }
        //根据订单查询返回字段ship_way （商品发货类型  1机具发货  2物料发货 ） 来请求sn_list_array （机具类不能为空，商品类为“”）
        Map<String,Object>  map = paOrderService.selectPaOrderByOrderNo(order_no);
        if ("2".equals(map.get("ship_way").toString())) {
        	sn_list_array = "";
		}
        AgentInfo loginAgent = agentInfoService.selectByPrincipal();
        //查询登录代理商对应的人人代理用户信息
        PaUserInfo paUserInfo = perAgentService.selectUserByAgentNo(loginAgent.getAgentNo());
        // String currentUserCode = paOrderService.selectUserCodeByAgentNo(loginAgent.getAgentNo());
        String currentUserCode = paUserInfo.getUserCode();
        String userType = paUserInfo.getUserType();
        if ("1".equals(userType) && (StringUtils.isBlank(transport_company) || StringUtils.isBlank(post_no))) {
            result.put("status", false);
            result.put("msg", "必要参数不能为空");
            return result;
        }
        //调用机具发货接口
        List<SysDict> sysDicts = sysDictService.selectByKey("ALLAGENT_SERVICE_URL");
        String url = sysDicts.get(0).getSysValue() + "/transOrder/confirmPost";
//        String sendParams = "user_code=" + user_code + "&order_no=" + order_no + "&transport_company=" + transport_company
//        		+ "&post_no=" + post_no + "&sn_list_array=" + sn_list_array + "&current_user_code=" + currentUserCode 
//        		+ "&is_app=0";
        String sendParams = "current_user_code=" + currentUserCode + "&is_app=0" + "&order_no=" + order_no + "&post_no=" + post_no
        		+ "&sn_list_array=" + sn_list_array + "&transport_company=" + transport_company + "&user_code=" + user_code 
        		;
        log.info("调用机具发货接口,url -----> " + url + ",请求参数---->" + sendParams);
        String response = HttpUtils.sendPost(url,sendParams + "&sign=" + Md5.md5Str(sendParams + "&" + perAgentService.selectMd5Key("req_key")), "UTF-8");
        JSONObject responseObject = JSONObject.parseObject(response);
        String status = responseObject.getString("status");
        String msg = responseObject.getString("msg");
        if (SUCCESS.equals(status)) {
            log.info("------------机具发货成功-------------，订单号：" + order_no + ": " + msg);
            result.put("status", true);
            return result;
        } else {
            log.info("------------机具发货失败-------------，订单号：" + order_no + ": " + msg);
            result.put("status", false);
            result.put("msg", msg);
        }
        return result;
    }

    /**
     * 下载 机具批量下发模板
     *
     * @param response
     * @param request
     * @throws IOException
     */
    @RequestMapping("/downloadTemplate")
    @ResponseBody
    public void downTemplate(HttpServletResponse response, HttpServletRequest request) throws IOException {
        String filePath = request.getServletContext().getRealPath("/") + "template//"
                + "terminalBatchSendTemplate.xls";
        log.info(filePath);
        ResponseUtil.download(response, filePath, "机具批量发货模板.xls");
    }

    /**
     * 批量发货
     *
     * @param file
     * @param request
     * @return
     */
    @SystemLog(description = "物料批量发货")
    @RequestMapping("/sendTerBatch")
    @ResponseBody
    public Map<String, Object> sendTerBatch(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            if (!file.isEmpty()) {
                int errorCount = 0;
                int successCount = 0;
                //机具发货接口路径
                List<SysDict> sysDicts1 = sysDictService.selectByKey("ALLAGENT_SERVICE_URL");
                String url = sysDicts1.get(0).getSysValue() + "/transOrder/confirmPost";
                AgentInfo loginAgent = agentInfoService.selectByPrincipal();
                if (loginAgent == null) {
                    result.put("status", false);
                    result.put("msg", "请先登录！");
                    return result;
                }
                PaUserInfo paUserInfo =  perAgentService.selectUserByAgentNo(loginAgent.getAgentNo());
                //String userCode = paOrderService.selectUserCodeByAgentNo(loginAgent.getAgentNo());
                String userCode = paUserInfo.getUserCode();
                String userType = paUserInfo.getUserType();



                String format = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
                if (!format.equals(".xls") && !format.equals(".xlsx")) {
                    result.put("status", false);
                    result.put("msg", "文件格式错误");
                    return result;
                }
                Workbook wb = WorkbookFactory.create(file.getInputStream());
                Sheet sheet = wb.getSheetAt(0);
                // 遍历所有单元格，读取单元格
                int rowNum = sheet.getLastRowNum();
                if (rowNum < 1) {
                    result.put("status", false);
                    result.put("msg", "订单内容不能为空");
                    return result;
                }
                //List<SysDict> expresses = sysDictService.selectByKey("AGENT_EXPRESS");
                List<PaOrder> errorList = new ArrayList<PaOrder>();
                for (int i = 1; i <= rowNum; i++) {
                    PaOrder order = new PaOrder();
                    Row row = sheet.getRow(i);

                    Cell orderNoCell = row.getCell(0);
                    if (orderNoCell == null) {
                        errorCount++;
                        order.setErrorMsg("第" + i + "行订单编号为空");
                        errorList.add(order);
                        continue;
                    }
                    String orderNo = getCellValue(orderNoCell);
                    order.setOrderNo(orderNo);

                    Cell transportCompanyCell = row.getCell(1);
                    if (transportCompanyCell == null) {
                        errorCount++;
                        order.setErrorMsg("快递公司不能为空");
                        errorList.add(order);
                        continue;
                    }
                    String transportCompany = getCellValue(transportCompanyCell);
                    /*boolean flag = false;
                    for(SysDict s:expresses){
                        if(transportCompany.equals(s.getSysName())){
                            flag = true;
                            break;
                        }
                    }
                    if(!flag){
                        errorCount++;
                        order.setErrorMsg("快递公司有误");
                        errorList.add(order);
                        continue;
                    }*/
                    order.setTransportCompany(transportCompany);

                    Cell postNoCell = row.getCell(2);
                    if (postNoCell == null) {
                        errorCount++;
                        order.setErrorMsg("物流单号不能为空");
                        errorList.add(order);
                        continue;
                    }
                    order.setPostNo(getCellValue(postNoCell));

                    Cell snStartCell = row.getCell(3);
                    if (snStartCell == null) {
                        errorCount++;
                        order.setErrorMsg("机具SN开始号不能为空");
                        errorList.add(order);
                        continue;
                    }
                    order.setSnStart(getCellValue(snStartCell));

                    Cell snEndCell = row.getCell(4);
                    if (snEndCell == null) {
                        errorCount++;
                        order.setErrorMsg("机具SN结束号不能为空");
                        errorList.add(order);
                        continue;
                    }
                    order.setSnEnd(getCellValue(snEndCell));

                    PaOrder paOrder = paOrderDao.selectOrderByOrderNo(orderNo);
                    if (paOrder == null) {
                        errorCount++;
                        order.setErrorMsg("无此订单");
                        errorList.add(order);
                        continue;
                    } else if (!"0".equals(paOrder.getOrderStatus())) {
                        errorCount++;
                        order.setErrorMsg("此订单已发货");
                        errorList.add(order);
                        continue;
                    }
                    if(!"0".equals(paOrder.getIsPlatform())&&!"2".equals(paOrder.getIsPlatform())){
                        errorCount++;
                        order.setErrorMsg("您不是该订单的发货人");
                        errorList.add(order);
                        continue;
                    }

                    if("0".equals(paOrder.getIsPlatform())){
                        if(!"1".equals(userType)){
                            errorCount++;
                            order.setErrorMsg("您不是该订单的发货人");
                            errorList.add(order);
                            continue;
                        }
                    }

                    if("2".equals(paOrder.getIsPlatform())){
                        if(!"2".equals(userType)){
                            errorCount++;
                            order.setErrorMsg("您不是该订单的发货人");
                            errorList.add(order);
                            continue;
                        }
                    }


                    order.setUserCode(paOrder.getUserCode());

                    //根据登录代理商的userCode查询它所拥有的机具
                    List<TerminalInfo> terminalInfos = terminalInfoService.queryTerminalByOrder(userCode, order.getSnStart(), order.getSnEnd());
                    StringBuilder sbSn = new StringBuilder("[");
                    String sn = "";
                    if (terminalInfos != null && terminalInfos.size() > 0) {
                        if (terminalInfos.size() != paOrder.getNum()) {
                            errorCount++;
                            order.setErrorMsg("发货数量与申购数量不一致");
                            errorList.add(order);
                            continue;
                        }
                        for (TerminalInfo terminalInfo : terminalInfos) {
                            sbSn.append("{'sn':'" + terminalInfo.getSn() + "'},");
                        }
                        sn = sbSn.toString();
                        if (sn.length() > 0) {
                            sn = sn.substring(0, sn.length() - 1) + "]";
                        }
                        order.setSn(sn);
                    } else {
                        errorCount++;
                        order.setErrorMsg("没有查找到可发货的机具");
                        errorList.add(order);
                        continue;
                    }

                    //调用机具发货接口
                    String sendParams = "user_code=" + order.getUserCode() + "&order_no=" + order.getOrderNo()
                            + "&transport_company=" + order.getTransportCompany()
                            + "&post_no=" + order.getPostNo() + "&sn_list_array=" + order.getSn() + "&current_user_code=" + userCode;
                    String response = HttpUtils.sendPost(url, sendParams, "UTF-8");
                    JSONObject responseObject = JSONObject.parseObject(response);
                    String status = responseObject.getString("status");
                    String msg = responseObject.getString("msg");
                    if (SUCCESS.equals(status)) {
                        successCount++;
                        log.info("------------机具批量发货 发货成功-------------，订单号：" + order.getOrderNo() + ": " + msg);
                    } else {
                        order.setErrorMsg(msg);
                        errorList.add(order);
                        log.error("------------机具批量发货 发货失败-------------，订单号：" + order.getOrderNo() + ": " + msg);
                    }
                }

                result.put("errorCount", errorCount);
                result.put("successCount", successCount);
                result.put("errorList", errorList);
                result.put("status", true);
                result.put("msg", "操作成功");
                return result;
            } else {
                result.put("status", false);
                result.put("msg", "文件格式错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("status", false);
            result.put("msg", "数据异常");
        }

        return result;
    }

    /**
     * 导出
     *
     * @param param
     * @param response
     * @param request
     * @throws Exception
     */
    @RequestMapping("/exportOrder")
    @ResponseBody
    public void exportOrder(@RequestParam("baseInfo") String baseInfo, HttpServletResponse response, HttpServletRequest request) {
        try {
        	baseInfo = new String(request.getParameter("baseInfo").getBytes("ISO8859-1"), "UTF-8");
        	PaOrder info = JSONObject.parseObject(baseInfo, PaOrder.class);
            paOrderService.exportOrder(info, response, request);
        } catch (Exception e) {
            log.error("机具物料申购订单列表导出失败");
            e.printStackTrace();
        }
    }

    public String getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case Cell.CELL_TYPE_STRING:
                return cell.getStringCellValue();
            case Cell.CELL_TYPE_BLANK:
                return "";
            case Cell.CELL_TYPE_BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case Cell.CELL_TYPE_FORMULA:
                return cell.getStringCellValue();
            default:
                return null;
        }
    }

}
