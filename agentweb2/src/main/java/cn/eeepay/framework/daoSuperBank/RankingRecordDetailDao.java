package cn.eeepay.framework.daoSuperBank;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.RankingRecordDetailInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RankingRecordDetailDao {

    /**
     * 查询排行榜 榜单明细列表
     * @param recordId
     * @param page
     * @return
     */
    List<RankingRecordDetailInfo> queryRankingRecordDetailPage(@Param("bean") String recordId, Page<RankingRecordDetailInfo> page);

    /**
     * 查询 统计金额 汇总
     * @param recordId
     * @return
     */
    String queryRankingRecordDetailUserTotalAmountSum(@Param("bean") String recordId);
    
    /**
     * 更新排名
     * @param baseInfo
     * @return
     */
    int updSort(@Param("baseInfo") RankingRecordDetailInfo baseInfo);
}
