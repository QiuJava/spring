package cn.eeepay.framework.daoRedem.redemActive;

import cn.eeepay.framework.model.redemActive.RedemProviderBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by 666666 on 2017/10/26.
 */
public interface RedemAgentShareDao {

    void openRedemActive(@Param("list") List<RedemProviderBean> wantAddAgent);

    void updateServiceCost(@Param("bean") RedemProviderBean bean);
}
