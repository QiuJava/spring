package cn.eeepay.boss.action;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.ActivationCodeBean;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.ResponseBean;
import cn.eeepay.framework.service.ActivationCodeService;
import cn.eeepay.framework.service.AgentInfoService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by 666666 on 2017/10/26.
 */
@RestController
@RequestMapping("activationCodeAction")
public class ActivationCodeAction {

    @Resource
    private ActivationCodeService activationCodeService;
    @Resource
    private AgentInfoService agentInfoService;

    @RequestMapping("/listActivationCode")
    public ResponseBean listActivationCode(@RequestBody ActivationCodeBean bean,
                                           @RequestParam(defaultValue = "1") int pageNo,
                                           @RequestParam(defaultValue = "20")int pageSize){
        Page<ActivationCodeBean> page = new Page<>(pageNo, pageSize);
        try {
            AgentInfo loginAgentInfo = agentInfoService.selectByPrincipal();
            List<ActivationCodeBean> list = activationCodeService.listActivationCode(bean,loginAgentInfo, page);
            return new ResponseBean(list, page.getTotalCount());
        }catch (Exception e){
            return new ResponseBean(e);
        }
    }

    @SystemLog(description = "下发激活码")
    @RequestMapping("/divideActivationCode")
    public ResponseBean divideActivationCode(long startId, long endId, String agentNo){
        try {
            AgentInfo loginAgentInfo = agentInfoService.selectByPrincipal();
            return new ResponseBean(activationCodeService.divideActivationCode(startId, endId, agentNo, loginAgentInfo));
        }catch (Exception e){
            return new ResponseBean(e);
        }
    }

    @SystemLog(description = "回收激活码")
    @RequestMapping("/recoveryActivation")
    public ResponseBean recoveryActivation(long startId, long endId){
        try {
            AgentInfo loginAgentInfo = agentInfoService.selectByPrincipal();
            return new ResponseBean(activationCodeService.recoveryActivation(startId, endId, loginAgentInfo));
        }catch (Exception e){
            return new ResponseBean(e);
        }
    }
}
