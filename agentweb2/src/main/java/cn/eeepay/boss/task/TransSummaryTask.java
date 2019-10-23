package cn.eeepay.boss.task;

import cn.eeepay.framework.dao.SysConfigDao;
import cn.eeepay.framework.dao.TransDaySummaryDao;
import cn.eeepay.framework.dao.TransInfoDao;
import cn.eeepay.framework.model.SysConfig;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author kco1989
 * @email kco1989@qq.com
 * @date 2019-04-11 11:02
 */
@Component
public class TransSummaryTask extends ScheduleJob {
    private static final String RUNNING_STATUS_KEY = "agent_trans_day_summary_job_running_status";
    private static final String TRAN_PAGE_SIZE_KEY = "agent_trans_day_summary_job_page_size";
    private static final Lock LOCK = new ReentrantLock();
    private static final Logger LOGGER = LoggerFactory.getLogger(TransSummaryTask.class);
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @Resource
    private TransInfoDao transInfoDao;
    @Resource
    private TransDaySummaryDao transDaySummaryDao;
    @Resource
    private SysConfigDao sysConfigDao;

    @Override
    protected void runTask(String runNo, Map<String, String> otherParam) {
        LOGGER.info("开始执行交易汇总任务, runNo:{}, otherParam: {}", runNo, otherParam);
        long startTime = System.currentTimeMillis();
        LOCK.lock();
        try {
            updateTaskRunning();
            String transDate = otherParam.get("trans_date");
            summaryTrans(transDate);
        }finally {
            LOCK.unlock();
            updateTaskRunDone();
        }
        long endTime = System.currentTimeMillis();
        LOGGER.info("结束执行交易汇总任务,执行时间{}, runNo:{}, otherParam: {}", endTime - startTime, runNo, otherParam);
    }

    /**
     * 标识任务正在执行中
     */
    private void updateTaskRunDone() {
        sysConfigDao.updateParamValueByParamKey(RUNNING_STATUS_KEY, "2");
    }

    /**
     * 标识任务已经执行完成
     */
    private void updateTaskRunning() {
        SysConfig sysConfig = sysConfigDao.queryTheFirstByKey(RUNNING_STATUS_KEY);
        if (sysConfig == null) {
            sysConfig = new SysConfig();
            sysConfig.setParamKey(RUNNING_STATUS_KEY);
            sysConfig.setParamValue("1");
            sysConfig.setRemark("代理商交易统计定时任务执行状态 1 正在执行中, 2 执行完成");
            sysConfigDao.insert(sysConfig);
        }else {
            sysConfigDao.updateParamValueByParamKey(RUNNING_STATUS_KEY, "1");
        }
    }

    /**
     * 开始统计交易记录
     * @param transDate 统计日期,
     *                  如果transDate为null,或者不是yyyy-MM-dd格式,这默认统计昨天交易数据
     */
    private void summaryTrans(String transDate) {
        String yesterday = SIMPLE_DATE_FORMAT.format(DateUtils.addDays(new Date(), -1));
        try {
            SIMPLE_DATE_FORMAT.parse(transDate);
        } catch (Exception e) {
            transDate = yesterday;
        }
        Integer maxAgentLevel = transInfoDao.queryMaxAgentLevel();
        if (maxAgentLevel == null) {
            LOGGER.warn("查不到最大的代理商编号");
        }
        // 1. 先删除需要统计日期当天的数据
        transDaySummaryDao.deleteByTransDate(transDate);
        // 2. 然后从代理商最下级往上开始统计
        for (int level = maxAgentLevel; level > 0; level--) {
            summaryTransByAgentLevel(transDate, level);
            if (level == maxAgentLevel) {
                transDaySummaryDao.updateMaxLevelAllTransAmount(transDate, level);
            }else {
                transDaySummaryDao.updateOtherLevelAllTransAmount(transDate, level);
            }
        }
    }

    /**
     * 根据代理商级别分页统计交易数据
     * @param transDate 统计日期
     * @param agentLeve 代理商级别
     */
    private void summaryTransByAgentLevel(String transDate, int agentLeve) {
        int start = 0;
        int offset = getPageSize();
        String startTransTime = transDate + " 00:00:00";
        String endTransTime = transDate + " 23:59:59";
        do {
            // 分页查询汇总代理商日交易数据
            List<Map<String, String>> result = transInfoDao.summaryTransAmount(startTransTime, endTransTime, agentLeve, start, offset);
            // 如果查不到数据,说明该层级的代理商已经统计完成
            if (CollectionUtils.isEmpty(result)) {
                return;
            }
            transDaySummaryDao.batchInsert(transDate, agentLeve, result);
            // 如果查询出来的数据小于页面数据,说明分页已经到了最后一页
            if (result.size() < offset) {
                return;
            }
            // 继续分页查询汇总数据
            start = start + offset;
        } while (true);
    }

    private int getPageSize() {
        try {
            String pageSize = sysConfigDao.getStringValueByKey(TRAN_PAGE_SIZE_KEY);
            return Integer.valueOf(pageSize);
        } catch (Exception e) {
            return 2000;
        }
    }
}
