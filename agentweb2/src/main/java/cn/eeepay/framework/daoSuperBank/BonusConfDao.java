package cn.eeepay.framework.daoSuperBank;

import cn.eeepay.framework.model.BonusConf;
import cn.eeepay.framework.util.WriteReadDataSource;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author panfuhao
 * @date 2018/8/17
 */
@WriteReadDataSource
public interface BonusConfDao {


    /**
     * 查询所有银行家组织
     * @return
     */
    @Select("select org_id, agency_alias from bonus_conf where type = 1")
    @ResultType(BonusConf.class)
    List<BonusConf> getBonusConfList();
}
