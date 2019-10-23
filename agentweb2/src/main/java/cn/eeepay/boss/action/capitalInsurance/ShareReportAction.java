package cn.eeepay.boss.action.capitalInsurance;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.capitalInsurance.OrderTotal;
import cn.eeepay.framework.model.capitalInsurance.ShareReport;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.service.capitalInsurance.ShareReportService;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/7/23/023.
 * @author  liuks
 * 分润月报Action
 */
@Controller
@RequestMapping(value = "/shareReport")
public class ShareReportAction {

    private static final Logger log = LoggerFactory.getLogger(ShareReportAction.class);

    @Resource
    private ShareReportService shareReportService;

    @Resource
    private AgentInfoService agentInfoService;

    /**
     * 查询分润月报
     */
    @RequestMapping(value = "/selectAll")
    @ResponseBody
    public Map<String,Object> selectByParam(@RequestParam("info") String param, @ModelAttribute("page")
            Page<ShareReport> page) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ShareReport share = JSONObject.parseObject(param, ShareReport.class);
            AgentInfo loginAgent = agentInfoService.selectByPrincipal();
            share.setLoginAgentNo(loginAgent.getAgentNo());

            shareReportService.selectAllList(share, page);
            OrderTotal total=shareReportService.selectSum(share, page);
            msg.put("total",total);
            msg.put("page",page);
            msg.put("status", true);
        } catch (Exception e){
            log.error("查询分润月报异常!",e);
            msg.put("status", false);
            msg.put("msg", "查询分润月报异常!");
        }
        return msg;
    }

    /**
     * 导出分润月报
     */
    @RequestMapping(value="/exportDetail")
    @ResponseBody
    public Map<String, Object> exportDetail(@RequestParam("info") String param, HttpServletResponse response, HttpServletRequest request){
        ShareReport share = JSONObject.parseObject(param, ShareReport.class);
        Map<String, Object> msg=new HashMap<String,Object>();
        AgentInfo loginAgent = agentInfoService.selectByPrincipal();
        share.setLoginAgentNo(loginAgent.getAgentNo());

        List<ShareReport> list=shareReportService.exportDetailSelect(share);
        try {
            shareReportService.exportDetail(list,response);
        }catch (Exception e){
            log.error("导出分润月报异常!",e);
            msg.put("status", false);
            msg.put("msg", "导出分润月报异常!");
        }
        return msg;
    }
}
