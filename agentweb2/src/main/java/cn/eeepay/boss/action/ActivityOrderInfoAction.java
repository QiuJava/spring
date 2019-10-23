package cn.eeepay.boss.action;


import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.service.AccessService;
import cn.eeepay.framework.service.ActivityOrderInfoService;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.ListDataExcelExport;
import cn.eeepay.framework.util.StringUtil;
import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/activityOrder")
public class ActivityOrderInfoAction {

    private static final Logger log = LoggerFactory.getLogger(ActivityOrderInfoAction.class);

    @Resource
    private ActivityOrderInfoService activityOrderInfoService;
    @Resource
    private AgentInfoService agentInfoService;
    @Resource
    private AccessService accessService;

    @ResponseBody
    @RequestMapping("/actOrderInfoQuery")
    public Map<String, Object> actOrderInfoQuery(@RequestParam String info, @Param("page") Page<Map<String, Object>> page) {
        Map<String, Object> res = new HashMap<>();
        JSONObject json = JSONObject.parseObject(info);
        Map params = json;
        AgentInfo loginAgent = agentInfoService.selectByPrincipal();
        activityOrderInfoService.actOrderInfoQuery(params, page,loginAgent);
        res.put("page", page);
        return res;
    }


    @ResponseBody
    @RequestMapping("/actOrderInfo")
    public Map<String, Object> actOrderInfo(@RequestParam String id) {
        Map<String, Object> res = new HashMap<>();

        Map<String,Object> infoDetail = activityOrderInfoService.actOrderInfo(id);

        //权限验证
        boolean access = accessService.canQueryActOrderInfo((String)infoDetail.get("orderNo"),false);
        if(!access){
            res.put("msg", "非法操作");
            return res;
        }

        String couponNo = "";
        String payOrderNo = "";
        if(infoDetail !=null){
            couponNo = StringUtil.filterNull(infoDetail.get("couponNo"));
            payOrderNo = StringUtil.filterNull(infoDetail.get("payOrderNo"));

            infoDetail.put("merInfo",StringUtil.filterNull(infoDetail.get("merchantName"))
                    +"("+StringUtil.filterNull(infoDetail.get("merchantNo"))+")");

        }

        Map<String,Object> couponInfo = activityOrderInfoService.queryCouponInfo(couponNo);
        if(couponInfo!=null){
            String startTime = StringUtil.filterNull(couponInfo.get("startTime"));
            String endTime = StringUtil.filterNull(couponInfo.get("endTime"));
            String startEndTime  = startTime.substring(0,10)+"~"+endTime.substring(0,10);
            couponInfo.put("startEndTime",startEndTime);
        }

        Map<String,Object> settleInfo = activityOrderInfoService.actOrderSettleInfoQuery(payOrderNo);

        res.put("infoDetail", infoDetail);
        res.put("couponInfo", couponInfo);
        res.put("settleInfo", settleInfo);

        return res;
    }

    @ResponseBody
    @RequestMapping("/actOrderInfoCount")
    public Map<String, Object> actOrderInfoCount(@RequestParam String info, @Param("page") Page<Map<String, Object>> page) {
        Map<String, Object> res = new HashMap<>();
        JSONObject json = JSONObject.parseObject(info);
        Map params = json;
        AgentInfo loginAgent = agentInfoService.selectByPrincipal();
        Map<String, Object> total = activityOrderInfoService.actOrderInfoCount(params,loginAgent);
        res.put("total", total);
        return res;
    }

    /*@ResponseBody
    @RequestMapping("/actOrderRemarkUpdate")
    public int  actOrderRemarkUpdate(@RequestParam Map<String,String> params, @Param("page") Page<Map<String, Object>> page) {
        int result = activityOrderInfoService.actOrderRemarkUpdate(params);
        return result;
    }*/

    @RequestMapping("/actOrderInfoExport")
    @ResponseBody
    public void actOrderInfoExport(@RequestParam String info, HttpServletResponse response) {

        JSONObject json = JSONObject.parseObject(info);
        Map params = json;
        log.info("[{}]",params);
        AgentInfo loginAgent = agentInfoService.selectByPrincipal();
        List<Map<String, Object>> listData = activityOrderInfoService.actOrderInfoExport(params,loginAgent);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String fileName = "充值返购买记录" + sdf.format(new Date()) + ".xls";
            String fileNameFormat = new String(fileName.getBytes("GBK"), "ISO-8859-1");
            response.setHeader("Content-disposition", "attachment;filename=" + fileNameFormat);
            List<Map<String, String>> data = new ArrayList<>();
            if (listData.size() < 1) {
                Map<String, String> maps = new HashMap<String, String>();
                maps.put("id", null);
                maps.put("orderNo", null);
                maps.put("merchantNo", null);
                maps.put("merchantName", null);
                maps.put("agentName", null);
                maps.put("mobileNo", null);
                maps.put("transAmount", null);
                maps.put("couponCode", null);
                maps.put("transStatus", null);
                maps.put("merAccNo", null);
                maps.put("transTime", null);
                maps.put("payOrderNo", null);
                maps.put("remark", null);
                data.add(maps);
            } else {
                for (Map<String,Object> i : listData) {
                    String couponCode = "";
                    String transStatus = "";
                    if (i.get("couponCode") != null) {
                        SysDict sysDict = activityOrderInfoService.sysDict("COUPON_CODE",i.get("couponCode").toString());
                        couponCode = sysDict.getSysName();
                    }
                    if (i.get("transStatus") != null) {
                        SysDict sysDict = activityOrderInfoService.sysDict("TRANS_STATUS",i.get("transStatus").toString());
                        transStatus = sysDict.getSysName();
                    }
                    Map<String, String> maps = new HashMap<String, String>();
                    maps.put("id", null == i.get("id") ? "" :i.get("id").toString());
                    maps.put("orderNo", null == i.get("orderNo") ? "" :i.get("orderNo").toString());
                    maps.put("merchantNo", null == i.get("merchantNo") ? "" :i.get("merchantNo").toString());
                    maps.put("merchantName", null == i.get("merchantName") ? "" :i.get("merchantName").toString());
                    maps.put("agentName", null == i.get("agentName") ? "" :i.get("agentName").toString());
                    maps.put("mobileNo", i.get("mobileNo").toString().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
                    maps.put("transAmount", null == i.get("transAmount") ? "0" :i.get("transAmount").toString());
                    maps.put("couponCode", couponCode);
                    maps.put("transStatus", transStatus);
                    maps.put("merAccNo", null == i.get("merAccNo") ? "" :i.get("merAccNo").toString());
                    maps.put("transTime", null == i.get("transTime") ? "" : sdf1.format(i.get("transTime")));
                    maps.put("payOrderNo", null == i.get("payOrderNo") ? "" :i.get("payOrderNo").toString());
                    maps.put("remark", null == i.get("remark") ? "" :i.get("remark").toString());
                    data.add(maps);
                }
            }
            ListDataExcelExport export = new ListDataExcelExport();
            String[] cols = new String[]{"id", "orderNo", "merchantNo", "merchantName","agentName", "mobileNo", "transAmount",
                    "couponCode", "transStatus", "merAccNo", "transTime", "payOrderNo", "remark"};
            String[] colsName = new String[]{"序号", "业务订单编号", "商户编号", "商户名称","所属代理商","商户手机号", "交易金额",
                    "订单类型", "订单状态","收款商户", "交易时间", "支付订单号", "备注"};
            OutputStream ouputStream = response.getOutputStream();
            export.export(cols, colsName, data,ouputStream );
            ouputStream.close();
        }catch (Exception e){
            log.error("",e);
        }


    }

}
