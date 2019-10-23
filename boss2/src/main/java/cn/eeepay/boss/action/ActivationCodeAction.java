package cn.eeepay.boss.action;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.ActivationCodeBean;
import cn.eeepay.framework.service.ActivationCodeService;
import cn.eeepay.framework.service.ActivityService;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.ResponseUtil;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by 666666 on 2017/10/26.
 */
@RestController
@RequestMapping("activationCodeAction")
public class ActivationCodeAction {

    @Resource
    private ActivationCodeService activationCodeService;

    @DataSource(Constants.DATA_SOURCE_SLAVE)
    @RequestMapping("/listActivationCode")
    public Map<String, Object> listActivationCode(@RequestBody ActivationCodeBean bean,
                                                  @RequestParam(defaultValue = "1") int pageNo,
                                                  @RequestParam(defaultValue = "20")int pageSize){
        Page<ActivationCodeBean> page = new Page<>(pageNo, pageSize);
        try {
            List<ActivationCodeBean> list = activationCodeService.listActivationCode(bean, page);
            return ResponseUtil.buildResponseMap(list, page.getTotalCount());
        }catch (Exception e){
            return ResponseUtil.buildResponseMap(e);
        }
    }

    @SystemLog(description = "生成激活码",operCode="activationCode.build")
    @RequestMapping("/buildActivationCode")
    public Map<String, Object> buildActivationCode(int count){
        try {
            return ResponseUtil.buildResponseMap(activationCodeService.buildActivationCode(count));
        }catch (Exception e){
            return ResponseUtil.buildResponseMap(e);
        }
    }

    @SystemLog(description = "划分激活码",operCode="activationCode.divide")
    @RequestMapping("/divideActivationCode")
    public Map<String, Object> divideActivationCode(long startId, long endId, String agentNode){
        try {
            return ResponseUtil.buildResponseMap(activationCodeService.divideActivationCode(startId, endId, agentNode));
        }catch (Exception e){
            return ResponseUtil.buildResponseMap(e);
        }
    }

    @SystemLog(description = "回收激活码",operCode="activationCode.recovery")
    @RequestMapping("/recoveryActivation")
    public Map<String, Object> recoveryActivation(long startId, long endId){
        try {
            return ResponseUtil.buildResponseMap(activationCodeService.recoveryActivation(startId, endId));
        }catch (Exception e){
            return ResponseUtil.buildResponseMap(e);
        }
    }
}
