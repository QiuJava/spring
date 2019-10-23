package cn.eeepay.boss.action;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.exception.AgentWebException;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.ProviderBean;
import cn.eeepay.framework.model.ResponseBean;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.service.ProviderService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 信用卡还款服务商管理
 * Created by 666666 on 2017/10/27.
 */
@RestController
@RequestMapping("/providerAction")
public class ProviderAction {

    @Resource
    private ProviderService providerService;
    private final static Pattern costPattern = Pattern.compile("^(\\d+(?:\\.\\d{0,4})?)%\\+(\\d+(?:\\.\\d{0,2})?)$");
    @Resource
    private AgentInfoService agentInfoService;
    @RequestMapping("/listProvider")
    public ResponseBean listProvider(@RequestBody ProviderBean providerBean,
                                            int pageNo,
                                            int pageSize){
        try {
            AgentInfo agentInfo = agentInfoService.selectByPrincipal();
            Page<AgentInfo> page = new Page<>(pageNo, pageSize);
            List<ProviderBean> listProvider = providerService.listProvider(providerBean, page, agentInfo);
            if (!CollectionUtils.isEmpty(listProvider)){
                for (ProviderBean bean : listProvider){
                    if (bean.getRate() != null){
                        bean.setRate(bean.getRate().multiply(new BigDecimal(100)));
                    }
                    if (bean.getFullRepayRate() != null){
                        bean.setFullRepayRate(bean.getFullRepayRate().multiply(new BigDecimal(100)));
                    }
                    if(bean.getPerfectRepayRate() != null){
                        bean.setPerfectRepayRate(bean.getPerfectRepayRate().multiply(new BigDecimal(100)));
                    }
                }
            }
            return new ResponseBean(listProvider, page.getTotalCount());
        }catch (Exception e){
            return new ResponseBean(e);
        }
    }

    @SystemLog(description = "开通信用卡超级还款")
    @RequestMapping("/openSuperRepayment")
    public ResponseBean openSuperRepayment(@RequestBody List<String> agentNoList){
        try {
            AgentInfo agentInfo = agentInfoService.selectByPrincipal();
            return new ResponseBean(providerService.openSuperRepayment(agentNoList, agentInfo));
        }catch (Exception e){
            return new ResponseBean(e);
        }
    }

    @SystemLog(description = "信用卡超级还款修改成本")
    @RequestMapping("/updateServiceCost")
    public ResponseBean updateServiceCost(@RequestBody ProviderBean bean){
        try {
            AgentInfo loginAgent = agentInfoService.selectByPrincipal();
            // 成本检查
            Matcher matcher = costPattern.matcher(bean.getCost());
            if (!matcher.matches()){
                throw new AgentWebException("修改的成本格式不合法(例:0.6000%+1.00)");
            }
            bean.setRate(new BigDecimal(matcher.group(1)).divide(new BigDecimal(100)));
            bean.setSingleAmount(new BigDecimal(matcher.group(2)));

            // 全额还款成本格式检查
            Matcher fullMatcher = costPattern.matcher(bean.getFullRepayCost());
            if (!fullMatcher.matches()){
                throw new AgentWebException("修改的全额还款成本格式不合法(例:2.6000%+1.00)");
            }
            //修改后的完美还款格式检查
            Matcher perfectMatcher = costPattern.matcher(bean.getPerfectRepayCost());
            if(!perfectMatcher.matches()){
                throw new AgentWebException("修改的完美还款成本格式不合法(例:2.6000%+1.00)");
            }
            //修改后的分润入账比例
            if (StringUtils.isBlank(bean.getAccountRatio()) || !bean.getAccountRatio().matches("^([1-9])|([1-9]\\d)|(100)$")){
                throw new AgentWebException("修改后的分润入账比例必须为正整数且在1~100之间");
            }
            bean.setFullRepayRate(new BigDecimal(fullMatcher.group(1)).divide(new BigDecimal(100)));
            bean.setFullRepaySingleAmount(new BigDecimal(fullMatcher.group(2)));

            bean.setPerfectRepayRate(new BigDecimal(perfectMatcher.group(1)).divide(new BigDecimal(100)));
            bean.setPerfectRepaySingleAmount(new BigDecimal(perfectMatcher.group(2)));

            return new ResponseBean(providerService.updateServiceCost(bean, loginAgent));
        }catch (Exception e){
            return new ResponseBean(e);
        }
    }
}
