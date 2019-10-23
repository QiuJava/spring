package cn.eeepay.framework.daoSuperBank;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.RankingPushRecordInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RankingPushRecordDao {
    /**
     * 查询排行榜 用户奖金发放记录
     * @param baseInfo
     * @param page
     * @return
     */
    List<RankingPushRecordInfo> queryRankingPushRecordPage(@Param("bean") RankingPushRecordInfo baseInfo, Page<RankingPushRecordInfo> page);

    /**
     * 查询总奖金汇总
     * @param baseInfo
     * @return
     */
    String selectRankingPushRecordTotalMoneySum(@Param("bean") RankingPushRecordInfo baseInfo);

}
