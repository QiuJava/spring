package cn.eeepay.framework.daoSuperBank;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.LotteryOrder;
import cn.eeepay.framework.model.RankingRecordInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface RankingRecordDao {

    /**
     * 查询排行榜列表
     *
     * @param baseInfo
     * @param page
     * @return
     */
    List<RankingRecordInfo> queryRankingRecord(@Param("bean") RankingRecordInfo baseInfo, Page<LotteryOrder> page);

    /**
     * 查询排行榜汇总
     * @param baseInfo
     * @return
     */
    Map<String,Object> queryRankingRecordSum(@Param("bean") RankingRecordInfo baseInfo);

    /**
     * 根据id查询排行榜详情
     * @param baseInfo
     * @return
     */
    RankingRecordInfo getRankingRecordById(@Param("bean") RankingRecordInfo baseInfo);
}
