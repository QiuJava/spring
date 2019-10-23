package cn.eeepay.boss.action;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.CreditCardManagerShare;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.poi.CreditCardManagerShareCreateRow;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.service.CreditCardManagerService;
import cn.eeepay.framework.util.ExcelUtils;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/creditCardManager")
public class CreditCardManagerAction {
    private static final Logger log = LoggerFactory.getLogger(CreditCardManagerAction.class);

    @Resource
    private CreditCardManagerService creditCardManagerService;
    @Resource
    private AgentInfoService agentInfoService;
    @Resource
    private CreditCardManagerShareCreateRow creditCardManagerShareCreateRow;

    /**
     * 查询分润
     *
     * @param data
     * @param page
     * @return
     */
    @RequestMapping(value = "/selectShareByParam")
    @ResponseBody
    public Map<String, Object> selectShareByParam(@RequestParam("baseInfo") String data, @Param("page") Page<CreditCardManagerShare> page) {
        Map<String, Object> msg = new HashMap<>();
        try {
            Map<String, Object> params = JSONObject.parseObject(data, Map.class);
            msg = creditCardManagerService.queryCreditCardManagerShareList(params, page);
            String shareTotalMoney=creditCardManagerService.getShareTotalMoney(params);
            String tradeTotalMoney = creditCardManagerService.getTradeTotalMoney(params);
            msg.put("shareTotalMoney",shareTotalMoney);
            msg.put("tradeTotalMoney",tradeTotalMoney);
        } catch (Exception e) {
            log.error("查询信用卡管家分润信息异常！", e);
        }
        return msg;
    }


    /**
     * 查询全部代理
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/selectAllAgentInfo")
    public @ResponseBody
    Object selectAllAgentInfo() throws Exception {
        List<AgentInfo> list = null;
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            list = agentInfoService.selectAllInfo(principal.getUserEntityInfo().getEntityId());
        } catch (Exception e) {
            log.error("查询当前代理商下的所有代理商失败！");
        }
        return list;
    }

    /**
     * 导出分润信息
     * @param baseInfo
     * @param response
     * @param request
     * @throws Exception
     */
    @SystemLog(description = "导出信用卡管家分润信息")
    @RequestMapping(value = "/exportShareInfo")
    @ResponseBody
    public void exportInfo(@RequestParam("baseInfo") String baseInfo, HttpServletResponse response, HttpServletRequest request) throws Exception {
        baseInfo = new String(baseInfo.getBytes("ISO-8859-1"), "UTF-8");
        List<CreditCardManagerShare> list = new ArrayList<>();
        try {
            Map<String, Object> params = JSONObject.parseObject(baseInfo, Map.class);
            list = creditCardManagerService.exportAllInfo(params);
        } catch (Exception e) {
            log.error("查询信用卡管家分润信息异常！", e);
        }
        System.out.println("==" + list.size());
        String fileName = "信用卡管家分润记录" + new SimpleDateFormat("YYYYMMdd_HHmmss").format(new Date());
        String[] columnNames = {"分润创建时间", "分润金额", "分润百分比", "入账状态", "分润代理商名称", "分润代理商编号", "关联订单号", "订单金额", "订单类型", "用户ID", "所属代理商编号", "所属代理商名称", "入账时间"};
        Workbook workbook = ExcelUtils.createWorkBook("分润记录", list, columnNames, creditCardManagerShareCreateRow);
        response.reset();
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String((fileName + ".xls").getBytes(), "ISO8859-1"));
        workbook.write(response.getOutputStream());
        response.flushBuffer();
    }


}
