package cn.eeepay.boss.action;


import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.eeepay.framework.model.PaMerInfo;
import cn.eeepay.framework.service.PerMerService;

/**
 * @author RPC
 * create 2018/09/11
 */
@Controller
@RequestMapping("/perMer")
public class PerMerAction {

    private Logger log = LoggerFactory.getLogger(PerMerAction.class);


    @Resource
    private PerMerService perMerService;

    /**
     * 查询
     * @param info
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/selectPaMerByParam")
    @ResponseBody
    public Map<String, Object> queryMerByParam(@RequestBody PaMerInfo baseInfo,@RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "10") int pageSize){
         Map<String, Object> msg = new HashMap<>();
        try {
        	msg = perMerService.queryUserByParam(baseInfo, pageNo, pageSize);
        } catch (Exception e) {
            msg.put("status", false);
            msg.put("msg", "查询失败");
            log.error("盟主列表查询失败", e);
        }
        return msg;
    }
    /**
     * 盟主商户记录导出
     * @param param
     * @param response
     * @param request
     */
    @RequestMapping("/exportMerInfo")
    @ResponseBody
    public void exportMerInfo(@RequestParam("baseInfo") String baseInfo, HttpServletResponse response, HttpServletRequest request) {
    	PaMerInfo info = JSONObject.parseObject(baseInfo, PaMerInfo.class);
    	try {
    		perMerService.exportMerInfo(info, response, request);
    	} catch (Exception e) {
    		log.error("导出失败",e);
    	}
    }

}
