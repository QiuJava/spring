package cn.eeepay.framework.dao;

import cn.eeepay.framework.util.WriteReadDataSource;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author kco1989
 * @email kco1989@qq.com
 * @date 2019-04-11 14:04
 */
@WriteReadDataSource
public interface TransDaySummaryDao {

    /**
     * 根据交易日期删除数据
     * @param transDate 交易日期
     */
    void deleteByTransDate(@Param("transDate") String transDate);

    /**
     * 批量插入汇总之后的日交易数据
     * @param transDate 汇总的交易日期
     * @param agentLevel 代理商级别
     * @param result    汇总数据
     */
    void batchInsert(@Param("transDate") String transDate,
                     @Param("agentLevel") int agentLevel,
                     @Param("list") List<Map<String, String>> result);

    /**
     * 更新最下级代理商的总交易金额
     * @param transDate 交易日期
     * @param agentLevel 代理商级别
     */
    void updateMaxLevelAllTransAmount(@Param("transDate") String transDate,
                                      @Param("agentLevel") int agentLevel);

    /**
     * 更新其他级别代理商的总交易金额
     * @param transDate 交易日期
     * @param agentLevel 代理商级别
     */
    void updateOtherLevelAllTransAmount(@Param("transDate") String transDate,
                                        @Param("agentLevel") int agentLevel);
}
