package cn.eeepay.boss.action.redemActive;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.daoRedem.redemActive.RedemSysConfigDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.ResponseBean;
import cn.eeepay.framework.model.redemActive.RedemActivationCodeBean;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.service.redemActive.RedemActivationCodeService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by 666666 on 2017/10/26.
 */
@RestController
@RequestMapping("/redemActivationCodeAction")
public class RedemActivationCodeAction {

    @Resource
    private RedemActivationCodeService redemActivationCodeService;
    @Resource
    private AgentInfoService agentInfoService;
    @Resource
    private RedemSysConfigDao redemSysConfigDao;

    @RequestMapping("/listActivationCode")
    public ResponseBean listActivationCode(@RequestBody RedemActivationCodeBean bean,
                                           @RequestParam(defaultValue = "1") int pageNo,
                                           @RequestParam(defaultValue = "20")int pageSize){
        Page<RedemActivationCodeBean> page = new Page<>(pageNo, pageSize);
        try {
            AgentInfo loginAgentInfo = agentInfoService.selectByPrincipal();
            List<RedemActivationCodeBean> list = redemActivationCodeService.listActivationCode(bean,loginAgentInfo, page);
            return new ResponseBean(list, page.getTotalCount());
        }catch (Exception e){
            return new ResponseBean(e);
        }
    }

    @SystemLog(description = "积分兑换下发激活码")
    @RequestMapping("/divideActivationCode")
    public ResponseBean divideActivationCode(long startId, long endId, String agentNo){
        try {
            AgentInfo loginAgentInfo = agentInfoService.selectByPrincipal();
            return new ResponseBean(redemActivationCodeService.divideActivationCode(startId, endId, agentNo, loginAgentInfo));
        }catch (Exception e){
            return new ResponseBean(e);
        }
    }

    @SystemLog(description = "积分兑换回收激活码")
    @RequestMapping("/recoveryActivation")
    public ResponseBean recoveryActivation(long startId, long endId){
        try {
            AgentInfo loginAgentInfo = agentInfoService.selectByPrincipal();
            return new ResponseBean(redemActivationCodeService.recoveryActivation(startId, endId, loginAgentInfo));
        }catch (Exception e){
            return new ResponseBean(e);
        }
    }

    @RequestMapping("/getPreActivationCodeUrl")
    @ResponseBody
    public ResponseBean getPreActivationCodeUrl(){
        try {
            String redeem_jhcode = redemSysConfigDao.getSysConfigValue("redeem_jhcode");
            ResponseBean responseBean = new ResponseBean(true);
            responseBean.setData(redeem_jhcode);
            return responseBean;
        }catch (Exception e){

            return new ResponseBean(e);
        }
    }

}
