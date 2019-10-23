package cn.eeepay.boss.action;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.ActivityService;
import cn.eeepay.framework.service.AgentFunctionManagerService;
import cn.eeepay.framework.service.TerminalInfoService;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;
import org.apache.http.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/activity")
public class ActivityAction {
    private static final Logger log = LoggerFactory.getLogger(ActivityAction.class);

    @Resource
    private ActivityService activityService;
    @Resource
    private AgentFunctionManagerService agentFunctionManagerService;
    @Resource
    public TerminalInfoService terminalInfoService;

    // 活动初始化
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/happySendActivity")
    @ResponseBody
    public Map<String, Object> happySendActivity(@ModelAttribute("page") Page<ActivityHardware> page,
                                                 @RequestParam("info") String param) throws Exception {
        Map<String, Object> msg = new HashMap<String, Object>();
        try {
            log.info("======>进入欢乐送活动开始");
            JSONObject jsonObject = JSON.parseObject(param);
            String activityCode = (String) jsonObject.get("activityCode");
            activityService.selectActivityHardware(page, activityCode);
            ActivityConfig info = activityService.selectActivityCofig(activityCode);
            msg.put("page", page);
            msg.put("info", info);
            msg.put("status", true);
            msg.put("msg", "查询成功");
            return msg;

        } catch (Exception e) {
            msg.put("status", false);
            msg.put("msg", "查询失败");
            log.error("欢乐送活动初始化失败----", e);
        }
        return msg;
    }

    /**
     * 添加欢乐送硬件信息
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/addActivityHardWare", method = RequestMethod.POST)
    @SystemLog(description = "添加欢乐送硬件信息", operCode = "activity.addActivityHardWare")
    public @ResponseBody
    Map<String, Object> addActivityHardWare(@RequestBody String param) {
        Map<String, Object> msg = new HashMap<String, Object>();
        try {
            JSONObject jsonObject = JSON.parseObject(param);
            ActivityHardware activityConfig = jsonObject.getObject("hardWare", ActivityHardware.class);
            String activityCode = (String) jsonObject.get("activityCode");
            String activiyName = (String) jsonObject.get("activiyName");
            List<ActivityHardware> ActivityList = activityService.selectActivityConfig(activityCode);
            int reqHpId = activityConfig.getHardId();
            for (ActivityHardware info : ActivityList) {
                int hpId = info.getHardId();
                if (reqHpId == hpId) {
                    msg.put("status", false);
                    msg.put("msg", "该硬件已存在不能重复添加");
                    return msg;
                }

            }
            activityConfig.setActivityCode(activityCode);
            activityConfig.setActiviyName(activiyName);
            int i = activityService.insertActivityHardware(activityConfig);
            if (i != 1) {
                msg.put("status", false);
                msg.put("msg", "添加活动硬件信息失败");
                return msg;
            }
            msg.put("status", true);
            msg.put("msg", "添加活动硬件信息成功");

        } catch (Exception e) {
            log.error("添加活动硬件信息失败！", e);
            msg.put("status", false);
            msg.put("msg", "添加活动硬件信息失败");
        }
        return msg;
    }

    /**
     * @param param
     * @return
     * @author Ivan
     * @date 2017/03/29 更新欢乐送硬件信息(暂支持更新冻结金额和目标金额数据修改)
     */
    @RequestMapping(value = "/editActivityHardWare", method = RequestMethod.POST)
    @SystemLog(description = "修改欢乐送硬件信息", operCode = "activity.editActivityHardWare")
    public @ResponseBody
    Map<String, Object> editActivityHardWare(@RequestBody String param) {
        Map<String, Object> msg = new HashMap<String, Object>();
        try {
            msg.put("status", false);
            msg.put("msg", "活动硬件信息更新失败");
            ActivityHardware activityHardware = JSON.parseObject(param, ActivityHardware.class);
            int i = activityService.updateActivityHardware(activityHardware);
            if (i != 0) {
                msg.put("status", true);
                msg.put("msg", "活动硬件信息更新成功");
                return msg;
            }
        } catch (Exception e) {
            log.error("活动硬件信息更新失败！", e);
            msg.put("status", false);
            msg.put("msg", "活动硬件信息更新失败");
        }
        return msg;
    }

    @RequestMapping(value = "/addHappySendActivity", method = RequestMethod.POST)
    @SystemLog(description = "新增欢乐送活动", operCode = "activity.addHappySendActivity")
    public @ResponseBody
    Map<String, Object> addHappySendActivity(@RequestBody String param) {
        Map<String, Object> msg = new HashMap<String, Object>();
        try {
            JSONObject jsonObject = JSON.parseObject(param);
            ActivityConfig activityConfig = jsonObject.getObject("info", ActivityConfig.class);
            ActivityConfig info = activityService.selectActivityCofig(activityConfig.getActivityCode());
            activityConfig.setActivityCode(activityConfig.getActivityCode());
            if (info == null) {
                activityService.insetActivityConfig(activityConfig);
            } else {
                activityService.updateActivityConfig(activityConfig);
            }
            msg.put("status", true);
            msg.put("msg", "添加欢乐送活动成功");
            return msg;
        } catch (Exception e) {
            log.error("添加欢乐送活动失败！", e);
            msg.put("status", false);
            String str = e.getMessage();
            if (str.contains("\r\n") || str.contains("\n"))
                msg.put("msg", "添加欢乐送活动失败");
            else
                msg.put("msg", str);
        }
        return msg;
    }

    // --------------------↓↓↓↓↓↓↓↓↓↓以下是欢乐返活动 ↓↓↓↓↓↓↓↓↓↓-----------------------

    /**
     * 查询欢乐返活动
     */
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/happyReturnConfig")
    @ResponseBody
    public Map<String, Object> happyReturnConfig() throws Exception {
        Map<String, Object> msg = new HashMap<String, Object>();
        try {
            // 欢乐返-循环送和欢乐返活动配置一致,这里使用008即可('008'为欢乐返-循环送的活动代码)
            ActivityConfig info = activityService.selectActivityCofig("008");
            msg.put("info", info);
            msg.put("status", true);
            msg.put("msg", "查询成功");
        } catch (Exception e) {
            msg.put("status", false);
            msg.put("msg", "查询失败");
            log.error("欢乐返活动查询失败----", e);
        }
        return msg;
    }

    /**
     * 新增或修改欢乐返活动配置
     */
    @RequestMapping(value = "/addHappyReturnConfig")
    @SystemLog(description = "新增或修改欢乐返活动配置", operCode = "activity.addHappyReturnConfig")
    public @ResponseBody
    Map<String, Object> addHappyReturnConfig(@RequestBody String param) {
        Map<String, Object> msg = new HashMap<String, Object>();
        try {
            // 因为欢乐返-循环送和欢乐返分别有自己的活动代码(activityCode)
            // 所以活动配置写两条,配置数据一致
            JSONObject jsonObject = JSON.parseObject(param);
            ActivityConfig activityConfig = jsonObject.getObject("info", ActivityConfig.class);


            ActivityConfig info80 = activityService.selectActivityCofig("008");
            ActivityConfig info150 = activityService.selectActivityCofig("009");
            ActivityConfig info128 = activityService.selectActivityCofig("021");
            if (info80 == null && info150 == null && info128 == null) {
                if (activityService.insertHlfConfig(activityConfig))
                    msg.put("msg", "添加欢乐返活动成功");
                else
                    msg.put("msg", "添加欢乐返活动失败");
            } else if (info80 != null && info150 != null && info128 != null) {
                if (activityService.updateHlfConfig(activityConfig))
                    msg.put("msg", "修改欢乐返活动成功");
                else
                    msg.put("msg", "修改欢乐返活动失败");
            } else {
                msg.put("msg", "数据异常,请联系后台");
                msg.put("status", false);
                return msg;
            }
            msg.put("status", true);
        } catch (Exception e) {
            log.error("添加或修改欢乐返活动失败！", e);
            msg.put("status", false);
            msg.put("msg", "添加或修改欢乐返活动失败");
        }
        return msg;
    }

    /**
     * 添加欢乐返硬件信息
     */
    @RequestMapping(value = "/addHlfHardWare", method = RequestMethod.POST)
    @SystemLog(description = "添加欢乐送硬件信息", operCode = "activity.addHlfHardWare")
    public @ResponseBody
    Map<String, Object> addHlfHardWare(@RequestBody String param) {
        Map<String, Object> msg = new HashMap<String, Object>();
        try {
            JSONObject jsonObject = JSON.parseObject(param);
            HlfHardware hlfHardware = jsonObject.getObject("hardWare", HlfHardware.class);
            List<HardwareProduct> hards = jsonObject.parseArray(jsonObject.getJSONArray("hards").toJSONString(),
                    HardwareProduct.class);
            // 数据安全判断
            if (StringUtils.isEmpty(hlfHardware.getActivityCode()) || hards == null) {
                msg.put("status", false);
                msg.put("msg", "非法参数,请重新输入");
                return msg;
            }
            for (HardwareProduct h : hards) {
                hlfHardware.setHardId(h.getHpId());
                // 查询该活动代码下该硬件是否存在
                if (activityService.isExistHardware(hlfHardware)) {
                    msg.put("status", false);
                    msg.put("msg", "硬件设备" + h.getTypeName() + "已存在,不能重复添加");
                    return msg;
                }
            }
            // 添加
            UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
                    .getPrincipal();
            hlfHardware.setOperator(principal.getId() + "");
            hlfHardware.setActiviyName("008".equals(hlfHardware.getActivityCode()) ? "欢乐返-循环送" : "欢乐返");
            int i = activityService.insertHlfHardware(hlfHardware, hards);
            if (i > 0) {
                msg.put("status", true);
                msg.put("msg", "添加活动硬件信息成功");
            } else {
                msg.put("status", false);
                msg.put("msg", "添加活动硬件信息失败");
            }
        } catch (Exception e) {
            log.error("添加活动硬件信息失败！", e);
            msg.put("status", false);
            msg.put("msg", "添加活动硬件信息失败");
        }
        return msg;
    }

    /**
     * 获取设备列表
     */
    @RequestMapping(value = "/selectHlfHardware")
    @ResponseBody
    public Map<String, Object> selectHlfHardware(@RequestBody String activityCode) throws Exception {
        Map<String, Object> msg = new HashMap<String, Object>();
        try {
            List<HlfHardware> list = activityService.selectHlfHardware(activityCode);
            msg.put("page", list);
            msg.put("status", true);
            msg.put("msg", "查询成功");
        } catch (Exception e) {
            msg.put("status", false);
            msg.put("msg", "查询失败");
            log.error("硬件设备查询失败----", e);
        }
        return msg;
    }

    /**
     * 获取欢乐返列表
     */
    @RequestMapping(value = "/selectHlfActivityHardware")
    @ResponseBody
    public Map<String, Object> selectHlfActivityHardware(@RequestBody String param) throws Exception {
        Map<String, Object> msg = new HashMap<String, Object>();
        try {
            JSONObject jsonObject = JSON.parseObject(param);
            HlfHardware hlfHardware = jsonObject.getObject("queryInfo", HlfHardware.class);
            List<HlfHardware> list = activityService.selectHlfActivityHardware(hlfHardware);
            msg.put("page", list);
            msg.put("status", true);
            msg.put("msg", "查询成功");
        } catch (Exception e) {
            msg.put("status", false);
            msg.put("msg", "查询失败");
            log.error("硬件设备查询失败----", e);
        }
        return msg;
    }

    /**
     * 获取欢乐返下拉列表
     * @param hardId
     * @param activityCode
     * @return
     */
    @RequestMapping(value = "/selectHBActivityHardwareList")
    @ResponseBody
    public Map<String, Object> selectHBActivityHardwareList(String hardId, String activityCode){
        Map<String, Object> map = new HashMap<>();
        try {
            List<HlfHardware> list = activityService.selectHBActivityHardwareList(hardId, activityCode);
            map.put("status", true);
            map.put("list", list);
        } catch (Exception e) {
            map.put("status", false);
            map.put("msg", "查询欢乐返子类型列表失败");
            log.error("查询欢乐返子类型列表失败: " + e.getMessage());
        }
        return map;
    }


    @RequestMapping("/selectHlfHardwareByHardId")
    @ResponseBody
    public Object selectHlfHardwareByHardId(@RequestBody String param) {
        Map<String, Object> msg = new HashMap<>();
        try {
            HlfHardware hlfHardware = JSON.parseObject(param, HlfHardware.class);
            HlfHardware params = activityService.selectHlfHardwareByHardId(hlfHardware);
            msg.put("param", params);
            msg.put("status", true);
        } catch (Exception e) {
            log.error("查询失败！", e);
            msg.put("status", false);
        }
        return msg;
    }

    /**
     * 修改欢乐返硬件信息
     */
    @RequestMapping(value = "/editHlfHardWare")
    @SystemLog(description = "修改欢乐返硬件信息", operCode = "activity.editHlfHardWare")
    public @ResponseBody
    Map<String, Object> editHlfHardWare(@RequestBody String param) {
        Map<String, Object> msg = new HashMap<String, Object>();
        try {
            JSONObject jsonObject = JSON.parseObject(param);
            HlfHardware hlfHardware = jsonObject.getObject("hardWare", HlfHardware.class);
            UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
                    .getPrincipal();
            hlfHardware.setOperator(principal.getId() + "");
            // 查询该活动代码下该硬件是否存在
            if (activityService.isExistHardwareByNoId(hlfHardware)) {
                msg.put("status", false);
                msg.put("msg", "硬件设备" + hlfHardware.getTypeName() + "已存在,不能重复添加");
                return msg;
            }
            //欢乐返活动类型，修改绑定欢乐返子类型时需判断该欢乐返子类型是否绑定有机具，如有则提示：该欢乐返子类型已绑定有机具，暂不支持修改
            HlfHardware hlf = activityService.selectHlfHardwareById(hlfHardware.getId());
            if (!hlf.getActivityTypeNo().equals(hlfHardware.getActivityTypeNo())) {
                if (terminalInfoService.queryHlfBindSn(hlf.getActivityTypeNo()).size() > 0) {
                    msg.put("status", false);
                    msg.put("msg", "原欢乐返子类型已绑定有机具，暂不支持修改");
                    return msg;
                }
            }

            activityService.updateHlfHardware(hlfHardware);
            msg.put("status", true);
            msg.put("msg", "欢乐返活动硬件信息更新成功");
        } catch (Exception e) {
            log.error("欢乐返活动硬件信息更新失败！", e);
            msg.put("status", false);
            msg.put("msg", "欢乐返活动硬件信息更新失败");
        }
        return msg;
    }

    /**
     * 欢乐返子类型查询列表
     *
     * @param page
     * @return
     */
    @RequestMapping("/selectHappyReturnType")
    @ResponseBody
    public Object selectHappyReturnType(@RequestBody ActivityHardwareType ativityHardwareType, @ModelAttribute("page") Page<ActivityHardwareType> page) {
        Map<String, Object> msg = new HashMap<>();
        try {
            activityService.selectHappyReturnType(ativityHardwareType, page);
            msg.put("page", page);
            msg.put("status", true);
            msg.put("msg", "查询成功");
        } catch (Exception e) {
            msg.put("status", false);
            msg.put("msg", "查询失败");
            log.error("欢乐返子类型查询失败----", e);
        }
        return msg;
    }

    @RequestMapping("/exportActivityHardwareType")
    public void exportActivityHardwareType(String info, HttpServletResponse response){
        try {
            if(StringUtil.isNotBlank(info)){
                info = URLDecoder.decode(info, "utf-8");
            }
            ActivityHardwareType item = JSONObject.parseObject(info, ActivityHardwareType.class);
            activityService.exportActivityHardwareType(item, response);
        } catch (Exception e) {
            log.info("导出欢乐返子类型失败,参数:{}");
            log.info(e.toString());
        }
    }

    /**
     * 欢乐返子类型名称校验
     *
     * @param
     * @return
     */
    @RequestMapping("/queryByActivityTypeName")
    @ResponseBody
    public Object queryByActivityTypeName(@RequestBody ActivityHardwareType happyReturnType) {
        Map<String, Object> msg = new HashMap<>();
        try {
            msg.put("param", activityService.queryByActivityHardwareType(happyReturnType.getActivityTypeNo()));
            if (activityService.queryByActivityTypeName(happyReturnType)) {
                msg.put("status", true);
            } else {
                msg.put("status", false);
            }
        } catch (Exception e) {
            log.error("查询失败！", e);
            msg.put("status", false);
        }
        return msg;
    }


    /**
     * 欢乐返子类型新增
     *
     * @return
     */
    @RequestMapping("/addHappyReturnType")
    @SystemLog(description = "欢乐返子类型新增", operCode = "activity.addHappyReturnType")
    public @ResponseBody
    Object addHappyReturnType(@RequestBody ActivityHardwareType ativityHardwareType) {
        Map<String, Object> msg = new HashMap<>();
        try {
            // 数据安全判断
            BigDecimal zero = new BigDecimal(0);
            BigDecimal ta = ativityHardwareType.getTransAmount();
            BigDecimal cba = ativityHardwareType.getCashBackAmount();
            BigDecimal rra = ativityHardwareType.getRepeatRegisterAmount();
            BigDecimal fa = ativityHardwareType.getFullAmount();
            BigDecimal rfa = ativityHardwareType.getRepeatFullAmount();
            BigDecimal ea = ativityHardwareType.getEmptyAmount();
            BigDecimal rea = ativityHardwareType.getRepeatEmptyAmount();
            if (StringUtils.isEmpty(ativityHardwareType.getActivityCode())
                    || ta == null || ta.compareTo(zero) == -1 || cba == null || cba.compareTo(zero) == -1
                    || rra == null || rra.compareTo(zero) == -1 || fa == null || fa.compareTo(zero) == -1
                    || rfa == null || rfa.compareTo(zero) == -1 || ea == null || ea.compareTo(zero) == -1
                    || rea == null || rea.compareTo(zero) == -1) {
                msg.put("status", false);
                msg.put("msg", "非法参数,请重新输入");
                return msg;
            }
            if (activityService.insertHappyReturnType(ativityHardwareType)) {
                msg.put("status", true);
                msg.put("msg", "欢乐返子类型新增成功");
            } else {
                msg.put("status", false);
                msg.put("msg", "欢乐返子类型新增失败");
            }
        } catch (Exception e) {
            log.error("欢乐返子类型新增失败！", e);
            msg.put("status", false);
            msg.put("msg", "欢乐返子类型新增失败");
        }
        return msg;
    }

    @RequestMapping("/editHappyReturnType")
    @SystemLog(description = "欢乐返子类型修改", operCode = "activity.editHappyReturnType")
    public @ResponseBody
    Object editHappyReturnType(@RequestBody ActivityHardwareType ativityHardwareType) {
        Map<String, Object> msg = new HashMap<>();
        try {
            // 数据安全判断
            BigDecimal zero = new BigDecimal(0);
            BigDecimal ta = ativityHardwareType.getTransAmount();
            BigDecimal cba = ativityHardwareType.getCashBackAmount();
            BigDecimal claa = ativityHardwareType.getCashLastAllyAmount();
            BigDecimal clta = ativityHardwareType.getCashLastTeamAmount();
            BigDecimal rra = ativityHardwareType.getRepeatRegisterAmount();
            BigDecimal fa = ativityHardwareType.getFullAmount();
            BigDecimal rfa = ativityHardwareType.getRepeatFullAmount();
            BigDecimal ea = ativityHardwareType.getEmptyAmount();
            BigDecimal rea = ativityHardwareType.getRepeatEmptyAmount();
            if (StringUtils.isEmpty(ativityHardwareType.getActivityCode())
                    || ta == null || ta.compareTo(zero) == -1 || cba == null || cba.compareTo(zero) == -1
                    || rra == null || rra.compareTo(zero) == -1 || fa == null || fa.compareTo(zero) == -1
                    || rfa == null || rfa.compareTo(zero) == -1 || ea == null || ea.compareTo(zero) == -1
                    || rea == null || rea.compareTo(zero) == -1) {
                msg.put("status", false);
                msg.put("msg", "非法参数,请重新输入");
                return msg;
            }
            if ("010".equals(ativityHardwareType.getActivityCode())) {
                if (claa == null || claa.compareTo(zero) == -1 || clta == null || clta.compareTo(zero) == -1) {
                    msg.put("status", false);
                    msg.put("msg", "非法参数,请重新输入");
                    return msg;
                }
                ativityHardwareType.setCashBackAmount(null);
            } else {
                if (cba == null || cba.compareTo(zero) == -1) {
                    msg.put("status", false);
                    msg.put("msg", "非法参数,请重新输入");
                    return msg;
                }
                ativityHardwareType.setCashLastAllyAmount(null);
                ativityHardwareType.setCashLastTeamAmount(null);
            }
            if (activityService.updateHappyReturnType(ativityHardwareType)) {
                msg.put("status", true);
                msg.put("msg", "欢乐返子类型修改成功");
                return msg;
            } else {
                msg.put("status", false);
                msg.put("msg", "欢乐返子类型修改失败");

            }
        } catch (Exception e) {
            log.error("欢乐返子类型修改失败！", e);
            msg.put("status", false);
            msg.put("msg", "欢乐返子类型修改失败");
        }
        return msg;
    }

    /**
     * 欢乐返子类型删除
     *
     * @return
     */
    @RequestMapping("/deleteHappyReturnType")
    @SystemLog(description = "欢乐返子类型删除", operCode = "activity.deleteHappyReturnType")
    public @ResponseBody
    Object deleteHappyReturnType(@RequestBody String activityTypeNo) {
        Map<String, Object> msg = new HashMap<>();
        try {
            if (activityService.queryActivityCount(activityTypeNo)) {
                msg.put("status", false);
                msg.put("msg", "此活动类型已经被引用，不可以删除");
            } else {
                if (activityService.delHappyReturnType(activityTypeNo)) {
                    msg.put("status", true);
                    msg.put("msg", "删除成功");
                    return msg;
                } else {
                    msg.put("status", false);
                    msg.put("msg", "删除失败");
                }
            }
        } catch (Exception e) {
            log.error("删除失败！", e);
            msg.put("status", false);
            msg.put("msg", "删除失败");
        }
        return msg;
    }


    @RequestMapping(value = "/updateAgentStatusSwitch")
    @ResponseBody
    @SystemLog(description = "欢乐返子类型代理商开关", operCode = "activity.updateAgentStatusSwitch")
    public Object updateAgentStatusSwitch(@RequestBody ActivityHardwareType ativityHardwareType) {
        Map<String, Object> msg = new HashMap<>();
        try {
            if (activityService.updateAgentStatusSwitch(ativityHardwareType)) {
                msg.put("status", true);
                msg.put("msg", "设置成功");
            } else {
                msg.put("status", false);
                msg.put("msg", "设置失败");
            }
        } catch (Exception e) {
            msg.put("msg", "服务异常");
            e.printStackTrace();
            msg.put("status", false);
        }
        return msg;
    }


    public static Map<String,String> mapStringToMap(String str){
        str=str.substring(1, str.length()-1);
        String[] strs=str.split(",");
        Map<String,String> map = new HashMap<String, String>();
        for (String string : strs) {
            String key=string.split("=")[0];
            String value=string.split("=")[1];
            map.put(key, value);
        }
        return map;
    }

    /**
     * 交易累计设置
     *
     * @return
     */
    @RequestMapping("/setCountTradeScope")
    @ResponseBody
    public Object setCountTradeScope(@RequestParam  String countTradeScope) {
        Map<String, Object> msg = new HashMap<>();
        String warmMsg="设置失败";
        boolean status=false;
        try {
            JSONObject jsMap=JSONObject.parseObject(countTradeScope);
            if(jsMap!=null&&!jsMap.isEmpty()){
                for(String key:jsMap.keySet()){
                    String value=jsMap.getString(key);
//                    if (!TextUtils.isEmpty(value) && !value.startsWith(",")) {
//                        value = "," + value;
//                    }
                    if(",".equals(value)){
                        value="";
                    }
                    if (!TextUtils.isEmpty(value)){
                        while(value.startsWith(",")){
                            value=value.substring(1);
                        }
                        while(value.endsWith(",")){
                            value=value.substring(0,value.length()-1);
                        }
                        while(value.contains(",,")){
                            value=value.replaceAll(",,",",");
                        }
                    }


                   activityService.updateCountTradeScope(key,value);
                }
                warmMsg= "设置成功";
               status=true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("设置失败！", e);

        }
        msg.put("msg",warmMsg);
        msg.put("status", status);
        return msg;
    }

    /**
     * 欢乐返子类型列表
     *
     * @return
     */
    @RequestMapping("/queryByactivityTypeNoList")
    @ResponseBody
    public Object queryByactivityTypeNoList(@RequestBody String activityCode) {
        Map<String, Object> msg = new HashMap<>();
        try {
            List<ActivityHardwareType> info = activityService.queryByactivityTypeNoList(activityCode);
            msg.put("info", info);
            msg.put("status", true);
        } catch (Exception e) {
            log.error("查询失败！", e);
            msg.put("status", false);
        }
        return msg;
    }


    @RequestMapping("/queryByActivityHardwareType")
    @ResponseBody
    public Object queryByActivityHardwareType(@RequestBody String activityTypeNo) {
        Map<String, Object> msg = new HashMap<>();
        try {
            ActivityHardwareType param = activityService.queryByActivityHardwareType(activityTypeNo);
            msg.put("param", param);
            msg.put("status", true);
        } catch (Exception e) {
            log.error("查询失败！", e);
            msg.put("status", false);
        }
        return msg;
    }
    // ---------------------↑↑↑↑↑↑↑↑↑↑以上是欢乐返活动↑↑↑↑↑↑↑↑↑↑-----------------------

    /**
     * 欢乐返活动个性化配置新增
     *
     * @return
     */
    @RequestMapping("/addHappyReturnRewardActivity")
    @SystemLog(description = "欢乐返活动个性化配置", operCode = "activity.addHappyReturnRewardActivity")
    public @ResponseBody
    Object addHappyReturnRewardActivity(@RequestBody String param) {
        Map<String, Object> msg = new HashMap<>();
        try {
            JSONObject jsonObject = JSON.parseObject(param);
            ActivityRewardConfig activityRewardConfig = jsonObject.getObject("info", ActivityRewardConfig.class);
            // 数据安全判断
            BigDecimal zero = new BigDecimal(0);
            BigDecimal ta = activityRewardConfig.getCumulateAmountMinus();
            BigDecimal cba = activityRewardConfig.getCumulateAmountAdd();
            if (StringUtils.isEmpty(activityRewardConfig.getActivityName())
                    || StringUtil.isBlank(activityRewardConfig.getStartTime()) || StringUtil.isBlank(activityRewardConfig.getEndTime())
                    || activityRewardConfig.getCumulateTransMinusDay() == null || activityRewardConfig.getCumulateTransDay() == null
                    || ta == null || ta.compareTo(zero) == -1 || cba == null || cba.compareTo(zero) == -1) {
                msg.put("status", false);
                msg.put("msg", "非法参数,请重新输入");
                return msg;
            }
            if (activityService.insertHappyReturnRewardActivity(activityRewardConfig)) {
                msg.put("status", true);
                msg.put("msg", "配置新增成功");
            } else {
                msg.put("status", false);
                msg.put("msg", "配置新增失败");
            }
        } catch (Exception e) {
            log.error("配置新增失败！", e);
            msg.put("status", false);
            msg.put("msg", "配置新增失败");
        }
        return msg;
    }

    /**
     * 欢乐返活动个性化配置编辑
     *
     * @return
     */
    @RequestMapping("/editHappyReturnRewardActivity")
    @SystemLog(description = "欢乐返活动个性化配置编辑", operCode = "activity.addHappyReturnRewardActivity")
    public @ResponseBody
    Object editHappyReturnRewardActivity(@RequestBody String param) {
        Map<String, Object> msg = new HashMap<>();
        try {
            JSONObject jsonObject = JSON.parseObject(param);
            ActivityRewardConfig activityRewardConfig = jsonObject.getObject("info", ActivityRewardConfig.class);
            // 数据安全判断
            BigDecimal zero = new BigDecimal(0);
            BigDecimal ta = activityRewardConfig.getCumulateAmountMinus();
            BigDecimal cba = activityRewardConfig.getCumulateAmountAdd();
            if (StringUtils.isEmpty(activityRewardConfig.getActivityName())
                    || StringUtil.isBlank(activityRewardConfig.getStartTime()) || StringUtil.isBlank(activityRewardConfig.getEndTime())
                    || activityRewardConfig.getCumulateTransMinusDay() == null || activityRewardConfig.getCumulateTransDay() == null
                    || ta == null || ta.compareTo(zero) == -1 || cba == null || cba.compareTo(zero) == -1) {
                msg.put("status", false);
                msg.put("msg", "非法参数,请重新输入");
                return msg;
            }
            if (activityService.updateHappyReturnRewardActivity(activityRewardConfig)) {
                msg.put("status", true);
                msg.put("msg", "配置更新成功");
            } else {
                msg.put("status", false);
                msg.put("msg", "配置更新失败");
            }
        } catch (Exception e) {
            log.error("配置新增失败！", e);
            msg.put("status", false);
            msg.put("msg", "配置新增失败");
        }
        return msg;
    }

    /**
     * 欢乐返活动个性化配置删除
     *
     * @return
     */
    @RequestMapping("/deleteHappyReturnRewardActivity")
    @SystemLog(description = "欢乐返活动个性化配置删除", operCode = "activity.deleteHappyReturnRewardActivity")
    public @ResponseBody
    Object deleteHappyReturnRewardActivity(@RequestBody String id) {
        Map<String, Object> msg = new HashMap<>();
        try {
            if (activityService.queryHappyReturnAgentActivityCount(id)) {
                msg.put("status", false);
                msg.put("msg", "此活动配置已经被引用，不可以删除");
            } else {
                if (activityService.deleteHappyReturnRewardActivity(id)) {
                    msg.put("status", true);
                    msg.put("msg", "删除成功");
                    return msg;
                } else {
                    msg.put("status", false);
                    msg.put("msg", "删除失败");
                }
            }
        } catch (Exception e) {
            log.error("删除失败！", e);
            msg.put("status", false);
            msg.put("msg", "删除失败");
        }
        return msg;
    }

    /**
     * 欢乐返活动个性化代理商配置
     *
     * @return
     */
    @RequestMapping("/addHappyReturnAgentActivity")
    @SystemLog(description = "欢乐返活动个性化代理商配置", operCode = "activity.addHappyReturnAgentActivity")
    public @ResponseBody
    Object addHappyReturnAgentActivity(@RequestBody AgentActivityRewardConfig info) {
        Map<String, Object> msg = new HashMap<>();
        try {
            AgentInfo agentInfo = agentFunctionManagerService.findAgentInfoByAgentNo(info.getAgentNo());
            if (agentInfo.getAgentLevel() != 1) {
                msg.put("status", false);
                msg.put("msg", "只能新增一级代理商配置");
            }
            AgentActivityRewardConfig agentActivityRewardConfig = activityService.queryHappyReturnAgentActivityByAgentNo(info.getAgentNo());
            if (agentActivityRewardConfig != null) {
                msg.put("status", false);
                msg.put("msg", "代理商已存在，不允许重复添加");
            } else {
                final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                // 数据安全判断
                info.setCreateUser(principal.getUsername());
                if (activityService.insertHappyReturnAgentActivity(info)) {
                    msg.put("status", true);
                    msg.put("msg", "代理商配置新增成功");
                } else {
                    msg.put("status", false);
                    msg.put("msg", "代理商配置新增失败");
                }
            }
        } catch (Exception e) {
            log.error("代理商配置新增失败！", e);
            msg.put("status", false);
            msg.put("msg", "代理商配置新增失败");
        }
        return msg;
    }

    /**
     * 欢乐返活动个性化代理商配置删除
     *
     * @return
     */
    @RequestMapping("/deleteHappyReturnAgentActivity")
    @SystemLog(description = "欢乐返活动个性化代理商配置删除", operCode = "activity.deleteHappyReturnAgentActivity")
    public @ResponseBody
    Object deleteHappyReturnAgentActivity(@RequestBody String id) {
        Map<String, Object> msg = new HashMap<>();
        try {
            if (activityService.deleteHappyReturnAgentActivity(id)) {
                msg.put("status", true);
                msg.put("msg", "删除成功");
                return msg;
            } else {
                msg.put("status", false);
                msg.put("msg", "删除失败");
            }
        } catch (Exception e) {
            log.error("删除失败！", e);
            msg.put("status", false);
            msg.put("msg", "删除失败");
        }
        return msg;
    }

    /**
     * 欢乐返活动个性化配置查询列表
     *
     * @param page
     * @return
     */
    @RequestMapping("/selectHappyReturnRewardActivity")
    @ResponseBody
    public Object selectHappyReturnRewardActivity(@RequestBody ActivityRewardConfig activityRewardConfig, @ModelAttribute("page") Page<ActivityRewardConfig> page) {
        Map<String, Object> msg = new HashMap<>();
        try {
            activityService.selectHappyReturnRewardActivity(activityRewardConfig, page);
            msg.put("page", page);
            msg.put("status", true);
            msg.put("msg", "查询成功");
        } catch (Exception e) {
            msg.put("status", false);
            msg.put("msg", "查询失败");
            log.error("欢乐返活动个性化配置查询列表失败----", e);
        }
        return msg;
    }

    /**
     * 欢乐返活动个性化配置查询列表
     *
     * @param id
     * @return
     */
    @RequestMapping("/selectHappyReturnRewardActivityById")
    @ResponseBody
    public Object selectHappyReturnRewardActivityById(@RequestBody String id) {
        Map<String, Object> msg = new HashMap<>();
        try {
            ActivityRewardConfig param = activityService.selectHappyReturnRewardActivityById(id);
            msg.put("param", param);
            msg.put("status", true);
            msg.put("msg", "查询成功");
        } catch (Exception e) {
            msg.put("status", false);
            msg.put("msg", "查询失败");
            log.error("欢乐返活动个性化配置查询列表失败----", e);
        }
        return msg;
    }

    /**
     * 欢乐返活动个性化配置代理商查询列表
     *
     * @param page
     * @return
     */
    @RequestMapping("/selectHappyReturnAgentActivity")
    @ResponseBody
    public Object selectHappyReturnAgentActivity(@RequestBody AgentActivityRewardConfig agentActivityRewardConfig, @ModelAttribute("page") Page<AgentActivityRewardConfig> page) {
        Map<String, Object> msg = new HashMap<>();
        try {
            activityService.selectHappyReturnAgentActivity(agentActivityRewardConfig, page);
            msg.put("page", page);
            msg.put("status", true);
            msg.put("msg", "查询成功");
        } catch (Exception e) {
            msg.put("status", false);
            msg.put("msg", "查询失败");
            log.error("欢乐返活动个性化配置代理商查询列表失败----", e);
        }
        return msg;
    }

    /**
     * VIP优享订单查询
     */
    @RequestMapping(value = "/queryActivityVipList")
    @ResponseBody
    public Map<String, Object> queryActivityVipList(@RequestParam("info") String param, @ModelAttribute("page")
            Page<Map> page) throws Exception {
        Map<String, Object> msg = new HashMap<String, Object>();
        try {
            Map info = JSONObject.parseObject(param, Map.class);
            activityService.queryActivityVipList(info, page);
            msg.put("page", page);
            msg.put("status", true);
        } catch (Exception e) {
            log.error("VIP优享订单查询异常!", e);
            msg.put("status", false);
            msg.put("msg", "VIP优享订单查询异常!");
        }
        return msg;
    }

    @RequestMapping("/exportActivityVip")
    public void exportShareDetail(@RequestParam String param, HttpServletResponse response) {
        try {
            Map<String, Object> info = JSON.parseObject(param, Map.class);
            activityService.exportActivityVip(info, response);
        } catch (Exception e) {
            log.info("导出VIP优享订单失败,参数:{}");
            log.info(e.toString());
        }
    }

    /**
     * 查询欢乐返活动重复注册全局配置
     */
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping(value = "/happyReturnRepeatRegistConfig")
    @ResponseBody
    public Map<String, Object> happyReturnRepeatRegistConfig() throws Exception {
        Map<String, Object> msg = new HashMap<String, Object>();
        try {
            //001为欢乐返活动重复注册全局配置
            ActivityConfig info = activityService.selectActivityCofig("001");
            msg.put("info", info);
            msg.put("status", true);
            msg.put("msg", "查询成功");
        } catch (Exception e) {
            msg.put("status", false);
            msg.put("msg", "查询失败");
            log.error("欢乐返活动查询失败----", e);
        }
        return msg;
    }

    /**
     * 新增或修改欢乐返活动重复注册配置
     */
    @RequestMapping(value = "/addHappyReturnRepeatRegistConfig")
    @SystemLog(description = "新增或修改欢乐返活动重复注册配置", operCode = "activity.addHappyReturnRepeatRegistConfig")
    public @ResponseBody
    Map<String, Object> addHappyReturnRepeatRegistConfig(@RequestBody String param) {
        Map<String, Object> msg = new HashMap<String, Object>();
        try {
            // 因为欢乐返-循环送和欢乐返分别有自己的活动代码(activityCode)
            // 所以活动配置写两条,配置数据一致
            JSONObject jsonObject = JSON.parseObject(param);
            ActivityConfig activityConfig = jsonObject.getObject("info", ActivityConfig.class);
            activityConfig.setActivityCode("001");
            ActivityConfig info001 = activityService.selectActivityCofig("001");
            if (info001 == null) {
                activityService.insetActivityConfig(activityConfig);
            } else if (info001 != null) {
                activityService.updateActivityConfig(activityConfig);
            } else {
                msg.put("msg", "数据异常,请联系后台");
                msg.put("status", false);
                return msg;
            }
            msg.put("msg", "保存成功");
            msg.put("status", true);
        } catch (Exception e) {
            log.error("添加或修改欢乐返活动失败！", e);
            msg.put("status", false);
            msg.put("msg", "添加或修改欢乐返活动失败");
        }
        return msg;
    }

    /**
     * 欢乐返0元配置列表
     *
     * @return
     */
    @RequestMapping("/queryByActivityRewardConfigList")
    @ResponseBody
    public Object queryByActivityRewardConfigList() {
        Map<String, Object> msg = new HashMap<>();
        try {
            List<ActivityRewardConfig> info = activityService.queryByActivityRewardConfigList();
            msg.put("info", info);
            msg.put("status", true);
        } catch (Exception e) {
            log.error("查询失败！", e);
            msg.put("status", false);
        }
        return msg;
    }

    /**
     * 欢乐返子类型列表
     * 查询所有
     *
     * @return
     */
    @RequestMapping("/getActivityTypeNoList")
    @ResponseBody
    public Object getActivityTypeNoList() {
        Map<String, Object> msg = new HashMap<>();
        try {
            List<ActivityHardwareType> info = activityService.getActivityTypeNoList();
            msg.put("info", info);
            msg.put("status", true);
        } catch (Exception e) {
            log.error("查询欢乐返子类型列表失败！", e);
            msg.put("status", false);
        }
        return msg;
    }

    @RequestMapping("/exportHlfActivityHardware")
    public void exportHlfActivityHardware(String queryInfo, HttpServletResponse response){
        try {
            if(StringUtil.isNotBlank(queryInfo)){
                queryInfo = URLDecoder.decode(queryInfo, "utf-8");
            }
            HlfHardware info = JSONObject.parseObject(queryInfo, HlfHardware.class);
            activityService.exportHlfActivityHardware(info, response);
        } catch (Exception e) {
            log.info("导出欢乐返活动设置失败,参数:{}");
            log.info(e.toString());
        }
    }

    /**
     * 新增活跃商户活动配置
     *
     * @return
     */
    @RequestMapping("/addHlfActivityMerchantRule")
    @SystemLog(description = "新增活跃商户活动配置", operCode = "activity.addHlfActivityMerchantRule")
    public @ResponseBody
    Object addHlfActivityMerchantRule(@RequestBody String param) {
        Map<String, Object> msg = new HashMap<>();
        try {
            JSONObject jsonObject = JSON.parseObject(param);
            HlfActivityMerchantRule info = jsonObject.getObject("info", HlfActivityMerchantRule.class);
            final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            info.setOperator(principal.getUsername());
            if(info.getFirstRepeatStatus()==1){
                info.setRepeatRewardType(info.getFirstRewardType());
                info.setRepeatRewardMonth(info.getFirstRewardMonth());
                info.setRepeatRewardTotalAmount(info.getFirstRewardTotalAmount());
                info.setRepeatRewardAmount(info.getFirstRewardAmount());
                info.setRepeatDeductType(info.getFirstDeductType());
                info.setRepeatDeductMonth(info.getFirstDeductMonth());
                info.setRepeatDeductTotalAmount(info.getFirstDeductTotalAmount());
                info.setRepeatDeductAmount(info.getFirstDeductAmount());
            }
            if (activityService.insertHlfActivityMerchantRule(info)) {
                msg.put("status", true);
                msg.put("msg", "配置新增成功");
            } else {
                msg.put("status", false);
                msg.put("msg", "配置新增失败");
            }
        } catch (Exception e) {
            log.error("配置新增失败！", e);
            msg.put("status", false);
            msg.put("msg", "配置新增失败");
        }
        return msg;
    }

    /**
     * 编辑活跃商户活动配置
     *
     * @return
     */
    @RequestMapping("/editHlfActivityMerchantRule")
    @SystemLog(description = "编辑活跃商户活动配置编辑", operCode = "activity.editHlfActivityMerchantRule")
    public @ResponseBody
    Object editHlfActivityMerchantRule(@RequestBody String param) {
        Map<String, Object> msg = new HashMap<>();
        try {
            JSONObject jsonObject = JSON.parseObject(param);
            HlfActivityMerchantRule info = jsonObject.getObject("info", HlfActivityMerchantRule.class);
            if(info.getFirstRepeatStatus()==1){
                info.setRepeatRewardType(info.getFirstRewardType());
                info.setRepeatRewardMonth(info.getFirstRewardMonth());
                info.setRepeatRewardTotalAmount(info.getFirstRewardTotalAmount());
                info.setRepeatRewardAmount(info.getFirstRewardAmount());
                info.setRepeatDeductType(info.getFirstDeductType());
                info.setRepeatDeductMonth(info.getFirstDeductMonth());
                info.setRepeatDeductTotalAmount(info.getFirstDeductTotalAmount());
                info.setRepeatDeductAmount(info.getFirstDeductAmount());
            }
            if (activityService.updateHlfActivityMerchantRule(info)) {
                msg.put("status", true);
                msg.put("msg", "配置更新成功");
            } else {
                msg.put("status", false);
                msg.put("msg", "配置更新失败");
            }
        } catch (Exception e) {
            log.error("配置新增失败！", e);
            msg.put("status", false);
            msg.put("msg", "配置新增失败");
        }
        return msg;
    }

    /**
     * 活跃商户活动配置查询列表
     *
     * @param page
     * @return
     */
    @RequestMapping("/selectHlfActivityMerchantRule")
    @ResponseBody
    public Object selectHlfActivityMerchantRule(@RequestBody HlfActivityMerchantRule info, @ModelAttribute("page") Page<HlfActivityMerchantRule> page) {
        Map<String, Object> msg = new HashMap<>();
        try {
            activityService.selectHlfActivityMerchantRule(info, page);
            msg.put("page", page);
            msg.put("status", true);
            msg.put("msg", "查询成功");
        } catch (Exception e) {
            msg.put("status", false);
            msg.put("msg", "查询失败");
            log.error("活跃商户活动配置查询列表失败----", e);
        }
        return msg;
    }

    /**
     * 活跃商户活动配置查询
     *
     * @param id
     * @return
     */
    @RequestMapping("/selectHlfActivityMerchantRuleById")
    @ResponseBody
    public Object selectHlfActivityMerchantRuleById(@RequestBody String id) {
        Map<String, Object> msg = new HashMap<>();
        try {
            HlfActivityMerchantRule param = activityService.selectHlfActivityMerchantRuleById(id);
            msg.put("param", param);
            msg.put("status", true);
            msg.put("msg", "查询成功");
        } catch (Exception e) {
            msg.put("status", false);
            msg.put("msg", "查询失败");
            log.error("活跃商户活动配置查询失败----", e);
        }
        return msg;
    }

    /**
     * 活跃商户活动配置删除
     *
     * @return
     */
    @RequestMapping("/deleteHlfActivityMerchantRule")
    @SystemLog(description = "活跃商户活动配置删除", operCode = "activity.deleteHlfActivityMerchantRule")
    public @ResponseBody Object
    deleteHlfActivityMerchantRule(@RequestBody String id) {
        Map<String, Object> msg = new HashMap<>();
        try {
            if(activityService.findActivityHardwareTypeByRuleIdCount(id)>0){
                msg.put("status", false);
                msg.put("msg", "此活动配置已经被欢乐返子类型引用，不可以删除");
                return msg;
            }
            if (activityService.deleteHlfActivityMerchantRule(id)) {
                msg.put("status", true);
                msg.put("msg", "删除成功");
                return msg;
            } else {
                msg.put("status", false);
                msg.put("msg", "删除失败");
            }
        } catch (Exception e) {
            log.error("删除失败！", e);
            msg.put("status", false);
            msg.put("msg", "删除失败");
        }
        return msg;
    }

    @RequestMapping(value = "/selectHlfActivityMerchantRuleAllInfo")
    public @ResponseBody Object selectHlfActivityMerchantRuleAllInfo(@RequestParam(value="item",required=false)String item) {
        List<HlfActivityMerchantRule> list = activityService.selectHlfActivityMerchantRuleAllInfo(item);
        return list;
    }
}
