package cn.eeepay.framework.service;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.blackAgent.BlackInfo;
import cn.eeepay.framework.model.blackAgent.RiskNewAnswer;

import java.util.List;
import java.util.Map;

public interface BlackDataService {

    /**
     * create by: zhangcheng
     * description: 黑名单条件查询
     * create time: 2019/8/28 17:47
     *
     *[page, blackInfo]
     * @return java.util.List<cn.eeepay.framework.model.blackAgent.BlackInfo>
     */
    public List<BlackInfo> selectByParam(Page<BlackInfo> page, BlackInfo blackInfo);
    /**
     * create by: zhangcheng
     * description: 获取最新风控回复
     * create time: 2019/8/28 17:49
     *
     *[orderNo]
     * @return cn.eeepay.framework.model.blackAgent.RiskNewAnswer
     */
    public RiskNewAnswer selectRiskNewAnwser(String orderNo);
    /**
     * create by: zhangcheng
     * description: 代理商回复
     * create time: 2019/8/28 17:49
     *
     *[orderNo, replyFilesName, replyRemark]
     * @return java.lang.String
     */
    public String agentReply(String orderNo,String replyFilesName,String replyRemark);

    Map<String, Object> selectRiskHandleDetail(String orderNo);
}
