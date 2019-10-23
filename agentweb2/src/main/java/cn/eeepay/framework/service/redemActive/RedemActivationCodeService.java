package cn.eeepay.framework.service.redemActive;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.redemActive.RedemActivationCodeBean;

import java.util.List;

/**
 * Created by 666666 on 2017/10/26.
 */
public interface RedemActivationCodeService {

    /**
     * 分页获取激活码信息
     * @param bean 查询信息
     * @param loginAgentInfo 登陆代理商
     *@param page 分页信息  @return
     */
    List<RedemActivationCodeBean> listActivationCode(RedemActivationCodeBean bean, AgentInfo loginAgentInfo, Page<RedemActivationCodeBean> page);

    /**
     * 划分激活码
     * @param startId   激活码开始id
     * @param endId     激活码结束id
     * @param agentNo   代理商编号
     * @param loginAgentInfo 登陆代理商
     * @return
     */
    long divideActivationCode(long startId, long endId, String agentNo, AgentInfo loginAgentInfo);


    /**
     * 回收激活码
     * @param startId   激活码开始id
     * @param endId     激活码结束id
     * @param loginAgentInfo 登陆代理商
     * @return
     */
    long recoveryActivation(long startId, long endId, AgentInfo loginAgentInfo);
}
