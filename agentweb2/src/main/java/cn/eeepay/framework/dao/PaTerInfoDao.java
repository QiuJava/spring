package cn.eeepay.framework.dao;

import cn.eeepay.framework.model.peragent.PaTerInfo;
import cn.eeepay.framework.util.WriteReadDataSource;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

/**
 * @author MXG
 * create 2018/11/19
 */
@WriteReadDataSource
public interface PaTerInfoDao {

    @Select("select * from pa_ter_info where id=#{id}")
    @ResultType(PaTerInfo.class)
    PaTerInfo selectById(@Param("id") String id);

    @Select("select * from pa_ter_info where sn=#{sn}")
    PaTerInfo selectBySn(@Param("sn") String sn);

    @Select("select * from pa_ter_info where sn=#{sn} and user_code=#{userCode} and callback_lock=#{callbackLock}")
    PaTerInfo selectBySnWithUserCodeWithCallbackLock(@Param("sn")String sn, @Param("userCode")String userCode, @Param("callbackLock")String callbackLock);

}
