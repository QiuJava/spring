package cn.eeepay.boss.action.cjt;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.Result;
import cn.eeepay.framework.model.cjt.CjtConstant;
import cn.eeepay.framework.model.cjt.CjtProfitDetail;
import cn.eeepay.framework.service.BossSysConfigService;
import cn.eeepay.framework.service.cjt.CjtProfitDetailService;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.ResponseUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tans
 * @date 2019-06-14
 */
@RestController
@RequestMapping("/cjtProfitDetail")
public class CjtProfitDetailAction {

    private static final Logger log = LoggerFactory.getLogger(CjtProfitDetailAction.class);

    @Resource
    private CjtProfitDetailService cjtProfitDetailService;

    @Resource
    private BossSysConfigService bossSysConfigService;

    /**
     * 条件查询超级推收益明细
     * @param baseInfo
     * @return
     */
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping("/selectPage")
    public Result selectPage(@RequestBody CjtProfitDetail baseInfo,
                                     @RequestParam(defaultValue = "1") int pageNo,
                                     @RequestParam(defaultValue = "10") int pageSize){
        Result result = new Result();
        try {
            Page<CjtProfitDetail> page = new Page<>(pageNo, pageSize);
            cjtProfitDetailService.selectPage(page, baseInfo);
            Map<String, Object> totalMap = cjtProfitDetailService.selectTotal(baseInfo);
            Map<String, Object> totalTransMap = cjtProfitDetailService.selectTotalTrans(baseInfo);

            String profitAutoSwitch = bossSysConfigService.selectValueByKey(CjtConstant.CJT_PROFIT_AUTO_SWITCH);

            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("page", page);
            dataMap.put("totalMap", totalMap);
            dataMap.put("totalTransMap", totalTransMap);
            dataMap.put("profitAutoSwitch", Integer.valueOf(profitAutoSwitch));

            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(dataMap);
        } catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("条件查询超级推收益明细异常", e);
        }
        return result;
    }

    /**
     * 超级推分润明细导出
     * @param baseInfo
     * @param response
     */
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping("/export")
    @SystemLog(operCode = "cjtProfitDetail.export", description = "导出")
    public void export(String baseInfo, HttpServletResponse response){
        try {
            if(StringUtils.isNotEmpty(baseInfo)){
                baseInfo = URLDecoder.decode(baseInfo, "utf-8");
            }
            CjtProfitDetail item = JSONObject.parseObject(baseInfo, CjtProfitDetail.class);
            cjtProfitDetailService.export(response, item);
        } catch (Exception e){
            log.error("超级推分润明细导出异常", e);
        }
    }

    /**
     * 批量入账
     * @param orderNoList
     * @return
     */
    @RequestMapping("/rechargeBatch")
    @SystemLog(operCode = "cjtProfitDetail.rechargeBatch", description = "批量入账")
    public Result rechargeBatch(@RequestBody List<String> orderNoList){
        Result result;
        if(orderNoList == null || orderNoList.size() < 1) {
            return Result.fail("参数不能为空");
        }
        try {
            result = cjtProfitDetailService.rechargeBatch(orderNoList);
        } catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("批量入账异常", e);
        }
        return result;
    }


    /**
     * 修改收益自动入账开关
     * @param profitAutoSwitch
     * @return
     */
    @RequestMapping("/updateProfitAutoSwitch")
    @SystemLog(operCode = "cjtProfitDetail.updateProfitAutoSwitch", description = "修改收益自动入账开关")
    public Result updateProfitAutoSwitch(@RequestBody Integer profitAutoSwitch){
        Result result;
        if(profitAutoSwitch == null) {
            return Result.fail("参数不能为空");
        }
        try {
            int num = bossSysConfigService.updateValueByKey(CjtConstant.CJT_PROFIT_AUTO_SWITCH, String.valueOf(profitAutoSwitch));
            if(num == 1){
                result = Result.success();
            } else {
                result = Result.fail();
            }
        } catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("修改收益自动入账开关异常", e);
        }
        return result;
    }

    /**
     * 查询收益自动入账开关
     * @return
     */
    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping("/selectProfitSwitch")
    public Result selectProfitSwitch(){
        Result result = new Result();
        try {
            String profitAutoSwitch = bossSysConfigService.selectValueByKey(CjtConstant.CJT_PROFIT_AUTO_SWITCH);
            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(Integer.valueOf(profitAutoSwitch));
        } catch (Exception e){
            result = ResponseUtil.buildResult(e);
            log.error("条件查询超级推收益明细异常", e);
        }
        return result;
    }
}
