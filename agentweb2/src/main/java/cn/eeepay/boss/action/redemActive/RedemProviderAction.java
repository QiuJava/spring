package cn.eeepay.boss.action.redemActive;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.exception.AgentWebException;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.ResponseBean;
import cn.eeepay.framework.model.redemActive.RedemProviderBean;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.service.redemActive.RedemProviderService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 信用卡还款服务商管理
 * Created by 666666 on 2017/10/27.
 */
@RestController
@RequestMapping("/redemProviderAction")
public class RedemProviderAction {

    private static final String percentReg = "^((100)|([1-9]\\d)|(\\d))(\\.\\d+)?$";
    @Resource
    private RedemProviderService redemProviderService;
    @Resource
    private AgentInfoService agentInfoService;

    @RequestMapping("/listProvider")
    public ResponseBean listProvider(@RequestBody RedemProviderBean providerBean,
                                            int pageNo,
                                            int pageSize){
        try {
            AgentInfo agentInfo = agentInfoService.selectByPrincipal();
            Page<AgentInfo> page = new Page<>(pageNo, pageSize);
            List<RedemProviderBean> listProvider = redemProviderService.listProvider(providerBean, page, agentInfo);
            return new ResponseBean(listProvider, page.getTotalCount());
        }catch (Exception e){
            return new ResponseBean(e);
        }
    }

    @SystemLog(description = "开通积分兑换-激活版")
    @RequestMapping("/openRedemActive")
    public ResponseBean openRedemActive(@RequestBody List<String> agentNoList){
        try {
            AgentInfo agentInfo = agentInfoService.selectByPrincipal();
            return new ResponseBean(redemProviderService.openRedemActive(agentNoList, agentInfo));
        }catch (Exception e){
            return new ResponseBean(e);
        }
    }

    @SystemLog(description = "积分兑换-激活版修改成本")
    @RequestMapping("/updateServiceCost")
    public ResponseBean updateServiceCost(@RequestBody RedemProviderBean bean){
        try {
            AgentInfo loginAgent = agentInfoService.selectByPrincipal();
            if (StringUtils.isBlank(bean.getShareRateStr()) || !bean.getShareRateStr().matches(percentReg)){
                throw new AgentWebException("修改的入账比例必须是数字,且在0~100之间.");
            }
            bean.setShareRate(new BigDecimal(bean.getShareRateStr()));
            if (NumberUtils.isNumber(bean.getOemFeeStr())){
                bean.setOemFee(new BigDecimal(bean.getOemFeeStr()));
            }else {
                throw new AgentWebException("修改的积分兑换成本必须是数字,且大于0.");
            }
            if (bean.getOemFee().compareTo(new BigDecimal(0)) < 0 ){
                throw new AgentWebException("修改的积分兑换成本必须是数字,且大于0.");
            }
            if (StringUtils.isBlank(bean.getReceiveShareRate()) || !bean.getReceiveShareRate().matches(percentReg)){
                throw new AgentWebException("修改后的商户收款入账比例必须是数字,且在0~100之间.");
            }
            if (StringUtils.isBlank(bean.getRepaymentShareRate()) || !bean.getRepaymentShareRate().matches(percentReg)){
                throw new AgentWebException("修改后的信用卡还款入账比例必须是数字,且在0~100之间.");
            }
            return new ResponseBean(redemProviderService.updateServiceCost(bean, loginAgent));
        }catch (Exception e){
            return new ResponseBean(e);
        }
    }
}
