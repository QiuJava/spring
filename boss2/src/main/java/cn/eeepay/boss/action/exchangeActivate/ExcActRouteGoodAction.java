package cn.eeepay.boss.action.exchangeActivate;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchangeActivate.ExcActRouteGood;
import cn.eeepay.framework.service.exchangeActivate.ExcActRouteGoodService;
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
 * Created by Administrator on 2018/9/30/030.
 * @author  liuks
 * 路由商品
 */
@Controller
@RequestMapping(value = "/excActRouteGood")
public class ExcActRouteGoodAction {

    private static final Logger log = LoggerFactory.getLogger(ExcActRouteGoodAction.class);

    @Resource
    private ExcActRouteGoodService excActRouteGoodService;

    /**
     * 查询路由商品列表
     */
    @RequestMapping(value = "/selectAll")
    @ResponseBody
    public Map<String,Object> selectByParam(@RequestParam("info") String param, @ModelAttribute("page")
            Page<ExcActRouteGood> page) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ExcActRouteGood good = JSONObject.parseObject(param, ExcActRouteGood.class);
            excActRouteGoodService.selectAllList(good, page);
            msg.put("page",page);
            msg.put("status", true);
        } catch (Exception e){
            log.error("查询路由商品列表异常!",e);
            msg.put("status", false);
            msg.put("msg", "查询路由商品列表异常!");
        }
        return msg;
    }

    /**
     * 获取路由商品详情
     */
    @RequestMapping(value = "/getRouteGood")
    @ResponseBody
    public Map<String,Object> getRouteGood(@RequestParam("id") int id) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ExcActRouteGood good =excActRouteGoodService.getRouteGood(id);
            msg.put("good",good);
            msg.put("status", true);
        } catch (Exception e){
            log.error("获取路由商品详情异常!",e);
            msg.put("status", false);
            msg.put("msg", "获取路由商品详情异常!");
        }
        return msg;
    }
    /**
     * 修改核销渠道商品
     */
    @RequestMapping(value = "/updateGood")
    @ResponseBody
    @SystemLog(description = "修改核销渠道商品",operCode="excActRouteGood.updateGood")
    public Map<String,Object> updateGood(@RequestParam("info") String param) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            ExcActRouteGood good = JSONObject.parseObject(param, ExcActRouteGood.class);
            if(excActRouteGoodService.checkRoute(good)){
                int num=excActRouteGoodService.updateExcActRouteGood(good);
                if(num>0){
                    msg.put("status", true);
                    msg.put("msg", "修改核销渠道商品成功!");
                }else{
                    msg.put("status", false);
                    msg.put("msg", "修改核销渠道商品失败!");
                }
            }else{
                msg.put("status", false);
                msg.put("msg", "该核销渠道下该对应商品ID已存在!");
            }
        } catch (Exception e){
            log.error("修改核销渠道商品异常!",e);
            msg.put("status", false);
            msg.put("msg", "修改核销渠道商品异常!");
        }
        return msg;
    }

    /**
     * 开启/关闭商品
     */
    @RequestMapping(value = "/closeGood")
    @ResponseBody
    @SystemLog(description = "开启/关闭商品",operCode="excActRouteGood.closeGood")
    public Map<String,Object> closeGood(@RequestParam("id") int id,@RequestParam("state") String state) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            int num=excActRouteGoodService.closeGood(id,state);
            if(num>0){
                msg.put("status", true);
                msg.put("msg", "操作成功!");
            }else{
                msg.put("status", false);
                msg.put("msg", "操作失败!");
            }
        } catch (Exception e){
            log.error("开启/关闭商品异常!",e);
            msg.put("status", false);
            msg.put("msg", "开启/关闭商品异常!");
        }
        return msg;
    }

    /**
     * 批量开启/关闭商品
     */
    @RequestMapping(value = "/closeGoodBatch")
    @ResponseBody
    @SystemLog(description = "批量开启/关闭商品",operCode="excActRouteGood.closeGood")
    public Map<String,Object> closeGoodBatch(@RequestParam("ids") String ids,@RequestParam("state") String state) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
           excActRouteGoodService.closeGoodBatch(ids,state,msg);
        } catch (Exception e){
            log.error("批量开启/关闭商品异常!",e);
            msg.put("status", false);
            msg.put("msg", "批量开启/关闭商品异常!");
        }
        return msg;
    }

    /**
     * 删除商品
     */
    @RequestMapping(value = "/deleteGood")
    @ResponseBody
    @SystemLog(description = "删除商品",operCode="excActRouteGood.deleteGood")
    public Map<String,Object> deleteGood(@RequestParam("id") int id) throws Exception{
        Map<String, Object> msg=new HashMap<String,Object>();
        try{
            int num=excActRouteGoodService.deleteGood(id);
            if(num>0){
                msg.put("status", true);
                msg.put("msg", "删除商品成功!");
            }else{
                msg.put("status", false);
                msg.put("msg", "删除商品失败!");
            }
        } catch (Exception e){
            log.error("删除商品异常!",e);
            msg.put("status", false);
            msg.put("msg", "删除商品异常!");
        }
        return msg;
    }

    /**
     * 导出核销渠道商品
     */
    @RequestMapping(value="/importDetail")
    @ResponseBody
    @SystemLog(description = "导出核销渠道商品",operCode="excActRouteGood.importDetail")
    public Map<String, Object> importDetail(@RequestParam("info") String param, HttpServletResponse response, HttpServletRequest request){
        Map<String, Object> msg=new HashMap<String,Object>();
        try {
            param=new String(param.getBytes("ISO-8859-1"),"UTF-8");
            ExcActRouteGood good = JSONObject.parseObject(param, ExcActRouteGood.class);
            List<ExcActRouteGood> list=excActRouteGoodService.importDetailSelect(good);
            excActRouteGoodService.importDetail(list,response);
        }catch (Exception e){
            log.error("导出核销渠道商品异常!",e);
            msg.put("status", false);
            msg.put("msg", "导出核销渠道商品异常!");
        }
        return msg;
    }
}
