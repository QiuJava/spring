package cn.eeepay.framework.service.unTransactionalImpl;

import cn.eeepay.framework.dao.ScheduleDao;
import cn.eeepay.framework.service.ScheduleService;
import cn.eeepay.framework.service.unTransactionalImpl.abstractJob.ScheduleJob;
import cn.eeepay.framework.service.unTransactionalImpl.job.*;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service("scheduleService")
public class ScheduleServiceImpl implements ScheduleService {
    @Resource
    private ScheduleDao scheduleDao;

    @Resource
    private TaskExecutor taskExecutor;

    @Resource
    private BeanFactory beanFactory;

    @Override
    public int insertTask(String runNo,String interfaceName) {
        return scheduleDao.insert(runNo,interfaceName);
    }

    @Override
    public int updateTask(String runNo, String status) {
        return scheduleDao.updateStatus(runNo, status);
    }

    @Override
    public Map<String, Object> queryTask(String runNo) {
        return scheduleDao.query(runNo);
    }

    public Map<String, Object> process(String runNo, String interfaceType) {
        Map<String, Object> msg = new HashMap<>();
        msg.put("runningNo", runNo);
        Map<String, Object> task = queryTask(runNo);
        if (task != null) {
            //针对定时系统 只支持传值running,complete类型
            if("init".equals(task.get("running_status"))){
                msg.put("runningStatus","running");
            }else{
                msg.put("runningStatus", task.get("running_status"));
            }
        } else {
            insertTask(runNo,interfaceType);
            //添加线程池
            ScheduleJob runnable = null;
            if ("happy".equals(interfaceType)) { //欢乐返定时任务
                runnable = getSrpingBean(HappybackJob.class);

            } else if("surveyOverdue".equals(interfaceType)){//调单
                runnable = getSrpingBean(SurveyOrderJob.class);

            }else if("accountRecord".equals(interfaceType)){//交易调账户系统记账
                runnable = getSrpingBean(AccountRecordJob.class);

            }else if("createMerAcc".equals(interfaceType)){//未开户的商户到账户系统开户
                runnable = getSrpingBean(CreateMerAccJob.class);

            }else if("settleStatus".equals(interfaceType)){//交易的结算状态更新
                runnable = getSrpingBean(SettleStatusJob.class);

            }else if("couponStatus".equals(interfaceType)){//已过期的优惠劵更新状态
                runnable = getSrpingBean(CouponStatusJob.class);

            }else if("ysMerAdd".equals(interfaceType)){//YS商户同步
                runnable = getSrpingBean(YSmerAddJob.class);

            }else if("reSettle".equals(interfaceType)){//再次出款
                runnable = getSrpingBean(ReSettleJob.class);

            }else if("superBankUserAccount".equals(interfaceType)){//未开户的超级银行家账户系统开户
                runnable = getSrpingBean(SuperBankUserJob.class);

            }else if("outAccountServiceBalance".equals(interfaceType)){//出款服务余额和上游同步
                runnable = getSrpingBean(OutAccountServiceBalanceJob.class);

            }else if("transactionTime".equals(interfaceType)){//交易异常预警
                runnable = getSrpingBean(TransactionTimeDayJob.class);

            }else if("paymentTimeSettlementing".equals(interfaceType)){//出款结算中预警
                runnable = getSrpingBean(PaymentTimeSettlementingJob.class);

            }else if("paymentTimeSettlemenFailure".equals(interfaceType)){//出款结算失败预警
                runnable = getSrpingBean(PaymentTimeSettlemenFailureJob.class);

            }else if("paymentServiceQuota".equals(interfaceType)){//出款服务额度预警
                runnable = getSrpingBean(PaymentServiceQuotaJob.class);

            }else if("happyBackSumAmount".equals(interfaceType)){//欢乐返金额统计
                runnable = getSrpingBean(HappyBackCumulativeJob.class);

            }else if("activityDetailBackstage".equals(interfaceType)){//欢乐返活动延时核算清算定时
                runnable = getSrpingBean(ActivityDetailBackstageJob.class);

            }else if("lotteryMatch".equals(interfaceType)){//超级银行家彩票导入文件匹配的定时任务
                runnable = getSrpingBean(LotteryMatchJob.class);

            }else if("analysisData".equals(interfaceType)){//超级银行家数据分析定时任务
                runnable = getSrpingBean(AnalysisDataJob.class);

            }else if("updateAcqServiceRate".equals(interfaceType)){//替换收单服务费率
                runnable = getSrpingBean(UpdateAcqServiceRateJob.class);

            }else if("updateAgentShare".equals(interfaceType)){//代理商分润任务生效
                runnable = getSrpingBean(UpdateAgentShareJob.class);

            }else if("updateOutAccountService".equals(interfaceType)){//出款费率定时任务
                runnable = getSrpingBean(UpdateOutAccountServiceJob.class);

            }else if("updateAcqMerchantQuota".equals(interfaceType)){//重置acq_merchant交易限额
                runnable = getSrpingBean(UpdateAcqMerchantQuotaJob.class);

            }else if("merchantMigrate".equals(interfaceType)){//商户迁移转移一级代理商
                runnable = getSrpingBean(MerchantMigrateJob.class);

            }else if("updateCarManagerStatus".equals(interfaceType)){//车管家订单状态维护
                runnable = getSrpingBean(CarManagerJob.class);

            }else if("syncTransStatus".equals(interfaceType)){//2小时同步一次交易状态
                runnable = getSrpingBean(SyncTransStatusJob.class);

            }else if("couponTimeStatus".equals(interfaceType)){//2分钟更新上下线时间状态状态
                runnable = getSrpingBean(CouponActivityTimeJob.class);

            }else if("redemptionExpired".equals(interfaceType)){//兑奖状态过期
                runnable = getSrpingBean(RedemptionJob.class);

            }else if("amountSysWarning".equals(interfaceType)){//目标金额预警
                runnable = getSrpingBean(SysWarningJob.class);

            }else if("tradeSum".equals(interfaceType)){//三方交易汇总报表 大约一小时
            	runnable = getSrpingBean(TradeSumJob.class);

            }else if("initLuckCount".equals(interfaceType)) {//抽奖次数初始化0 凌晨0时更新
                runnable = getSrpingBean(InitAwardsConfigCountJob.class);

            }else if("subscribeVipPush".equals(interfaceType)){//VIP优享到期推送
                runnable = getSrpingBean(SubscribeVipPushJob.class);

            }else if("tradeRestrict".equals(interfaceType)){//风控交易限制记录失效
                runnable = getSrpingBean(TradeRestrictJob.class);
            }else if("terminalUpdateDueDays".equals(interfaceType)){//机具活动考核更新考核剩余天数
                runnable = getSrpingBean(TerminalUpdateDueDaysJob.class);
            }else if("PushManagerJob".equals(interfaceType)){//推送管理
                runnable = getSrpingBean(PushManagerJob.class);
            }else if("happyBackMerSumAmount".equals(interfaceType)){//活跃商户金额统计
                runnable = getSrpingBean(HlfActivityMerchantJob.class);
            }

            if (runnable != null){
                scheduleDao.updateStatus(runNo,"running");
                runnable.setRunNo(runNo);
                taskExecutor.execute(runnable);
            }
            msg.put("runningStatus", "running");
        }
        return msg;
    }

    /**
     *加载多列bean
     */
    private ScheduleJob getSrpingBean(Class name){
        Object obj=beanFactory.getBean(name);
        if(obj instanceof ScheduleJob){
            return (ScheduleJob)obj;
        }
        return null;
    }

}
