package cn.eeepay.framework.dao;

import cn.eeepay.framework.model.peragent.PaChangeLog;
import cn.eeepay.framework.util.WriteReadDataSource;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectKey;

/**
 * @author MXG
 * create 2018/11/19
 */
@WriteReadDataSource
public interface PaChangeLogDao {

    @Insert("INSERT INTO pa_change_log(change_pre,change_after,remark,create_time,operater,oper_method) " +
            "VALUES(#{log.changePre},#{log.changeAfter},#{log.remark},NOW(),#{log.operater},#{log.operMethod})")
    int insert(@Param("log") PaChangeLog log);
}
